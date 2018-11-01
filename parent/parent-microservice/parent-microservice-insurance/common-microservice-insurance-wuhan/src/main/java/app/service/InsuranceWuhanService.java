package app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.wuhan.InsuranceWuhanHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.wuhan.HtmlInsuranceWuhanRepository;
import com.microservice.dao.repository.crawler.insurance.wuhan.InsuranceWuhanAllInfoRepository;
import com.microservice.dao.repository.crawler.insurance.wuhan.InsuranceWuhanPersonalRepository;
import com.microservice.dao.repository.crawler.insurance.wuhan.InsuranceWuhanUserInfoRespository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.InsuranceWuhanParser;


/**
 * @author lyx
 * @time 20170918
 * @category 社保-武汉
 */

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance"})

//调用各种方法

public class InsuranceWuhanService {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceWuhanService.class);
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceWuhanParser insuranceWuhanParser;
	@Autowired
	private InsuranceWuhanUserInfoRespository insuranceWuhanUserInfoRespository;
	@Autowired
	private InsuranceWuhanAllInfoRepository insuranceWuhanAllInfoRepository;
	@Autowired
	private InsuranceWuhanPersonalRepository insuranceWuhanPersonalRepository;
	@Autowired
	private HtmlInsuranceWuhanRepository htmlInsuranceWuhanRepository;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception 
	 */
	
	//登录
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		
		tracer.addTag("InsuranceChangchunService.login", insuranceRequestParameters.getTaskId());
		
		//先通过taskid在数据控充查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		//如果查询到
		if(null != taskInsurance){
			
			//获取parser中的登录方法中返回的WebParam
			WebParam webParam = insuranceWuhanParser.login(insuranceRequestParameters);
			//如果是null，就说明timeout
			if(null == webParam){
				tracer.addTag("InsuranceChangchunService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				log.info("登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;			
			}else{
				//获取登录页的html 
				String html = webParam.getHtml();
				tracer.addTag("InsuranceChangchunService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				//判断各种异常登录信息
				if(html.contains("url=/grws/jsp/framework/main.jsp")){
					taskInsurance = insuranceService.changeLoginStatusSuccessHttp(taskInsurance,webParam.getCookie());
					return taskInsurance;					
				}else if(html.contains("校验用户名口令时出现异常您的密码输入次数")) {
						taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
						tracer.addTag("InsuranceChengduService.login" + insuranceRequestParameters.getTaskId(),
								"登录失败,用户口令错误" );
						return taskInsurance;
				}else if(html.contains("验证码输入有误")){
					taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
					tracer.addTag("InsuranceChengduService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败,验证码错误" );
				}else{
					taskInsurance = insuranceService.changeLoginStatusIdnumError(taskInsurance);
					tracer.addTag("InsuranceChengduService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败,用户名不存在" );
					return taskInsurance;
				}
			}
		}
		return null;
	}
	
	/**
	 * @Des 更新taskInsurance
	 * @param insuranceRequestParameters
	 */
	
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {	
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}
	
	/**
	 * @Des 获取用户信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		
		//根据taskid查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		//将cookies从库里取出来
		String cookies = taskInsurance.getCookies();
		
		//获取爬取用户信息时，网页的基础信息
		WebParam webParam = insuranceWuhanParser.getUserInfo(taskInsurance,cookies);
		
		//如果有值
		if(null != webParam){
			//将爬取到的个人信息入库
			insuranceWuhanUserInfoRespository.save(webParam.getWuhanUserInfo());
			tracer.addTag("InsuranceWuhanService.getUserInfo 个人信息", "个人信息已入库!");
			
			tracer.addTag("InsuranceWuhanService.getUserInfo:SUCCESS", insuranceRequestParameters.getTaskId());
			//在库里修改状态
			insuranceService.changeCrawlerStatusUserInfo(taskInsurance,webParam.getCode());
			
			//保存爬取网页的源代码
			InsuranceWuhanHtml insuranceWuhanHtml = new InsuranceWuhanHtml();
			insuranceWuhanHtml.setDescription("userinfo");
			insuranceWuhanHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceWuhanHtml.setUrl(webParam.getUrl());
			insuranceWuhanHtml.setHtml(webParam.getHtml());
			htmlInsuranceWuhanRepository.save(insuranceWuhanHtml);
			tracer.addTag("InsuranceWuhanService.getUserInfo 个人信息","个人信息源码表入库!");	
			insuranceService.changeCrawlerStatus("数据采集中，【个人信息】已采集完成", "CRAWLER_USER_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		
	}
	/**
	 * @Des 获取个人参保信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getPersonalInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies = taskInsurance.getCookies();
		WebParam webParam = insuranceWuhanParser.getPersonalInfo(taskInsurance,cookies);
		if(null != webParam){
				List<InsuranceWuhanHtml> wuhanHtmls = new ArrayList<InsuranceWuhanHtml>();
					InsuranceWuhanHtml insuranceWuhanHtml = new InsuranceWuhanHtml();
					insuranceWuhanHtml.setDescription("personalInfo");
					insuranceWuhanHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceWuhanHtml.setUrl(webParam.getUrl());
					insuranceWuhanHtml.setHtml(webParam.getHtml());
					wuhanHtmls.add(insuranceWuhanHtml);
				htmlInsuranceWuhanRepository.saveAll(wuhanHtmls);
				tracer.addTag("getPersonalInfo ==>","个人参保信息源码表入库!");	
			if(null != webParam.getList()){
				insuranceWuhanPersonalRepository.saveAll(webParam.getList());
				tracer.addTag("getPersonalInfo ==>", "个人参保信息已入库!");
				insuranceService.changeCrawlerStatus("数据采集中，【个人参保信息】已采集完成", "CRAWLER_USER_MSG", 200, taskInsurance);
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【个人参保信息】已采集完成,无数据", "CRAWLER_USER_MSG", 201, taskInsurance);
			}
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			
		}
	}

	/**
	 * 获取五险信息
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	@Async
	public void getAllInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		String cookies =taskInsurance.getCookies();
		WebParam webParam = insuranceWuhanParser.getAllInfo(taskInsurance,cookies);
		
		if(null!=webParam){
			List<InsuranceWuhanHtml> wuhanHtmls = new ArrayList<InsuranceWuhanHtml>();
			InsuranceWuhanHtml insuranceWuhanHtml = new InsuranceWuhanHtml();
			insuranceWuhanHtml.setDescription("allInfo");
			insuranceWuhanHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceWuhanHtml.setUrl(webParam.getUrl());
			insuranceWuhanHtml.setHtml(webParam.getHtml());
			wuhanHtmls.add(insuranceWuhanHtml);
			htmlInsuranceWuhanRepository.saveAll(wuhanHtmls);
			tracer.addTag("getAllInfo ==>","个人参保信息源码表入库!");	
			if(null != webParam.getList()){
				insuranceWuhanAllInfoRepository.saveAll(webParam.getList());
				tracer.addTag("getAllInfo ==>", "个人参保信息已入库!");
			}
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成", "CRAWLER_YILIAO_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatus("数据采集中，【生育保险信息】已采集完成", "CRAWLER_SHENGYU_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatus("数据采集中，【工伤保险信息】已采集完成", "CRAWLER_GONGSHANG_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】已采集完成", "CRAWLER_SHIYE_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】已采集完成", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	}

	/**
	 * 获取失业保险信息
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
//	@Async
//	public void getUnemploymentInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
//		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
//		WebParam webParam = insuranceChangchunParser.getUnemploymentInfo(taskInsurance,cookies);
//		if(null != webParam){
//			if(null !=webParam.getHtmlPage()){
//				List<HtmlPage> htmls = webParam.getHtmlPage();
//				List<InsuranceChangchunHtml> changchunHtmls = new ArrayList<InsuranceChangchunHtml>();
//				for (int i = 0; i < htmls.size(); i++) {
//					InsuranceChangchunHtml insuranceChangchunHtml = new InsuranceChangchunHtml();
//					insuranceChangchunHtml.setDescription("unemploymentinfo");
//					insuranceChangchunHtml.setTaskid(insuranceRequestParameters.getTaskId());
//					insuranceChangchunHtml.setUrl(htmls.get(i).getUrl().toString());
//					insuranceChangchunHtml.setHtml(htmls.get(i).asXml());
//					insuranceChangchunHtml.setPageCount(i+"");
//					changchunHtmls.add(insuranceChangchunHtml);
//				}
//				htmlInsuranceChangchunRepository.save(changchunHtmls);
//				tracer.addTag("getAccountInfo ==>","失业保险信息源码表入库!");	
//			}
//			if(null != webParam.getList()){
//				insuranceChangchunEndowmentInfoRepository.save(webParam.getList());
//				tracer.addTag("getEndowmentInfo ==>", "失业保险信息已入库!");
//				insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】已采集完成", "CRAWLER_SHIYE_MSG", 200, taskInsurance);
//			}else{
//				insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】已采集完成,无数据", "CRAWLER_SHIYE_MSG", 201, taskInsurance);
//			}
//			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
//		}
//		log.info("-----------失业保险信息已入库-------------");
//		}
	
}
