package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.changchun.InsuranceChangchunHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.changchun.HtmlInsuranceChangchunRepository;
import com.microservice.dao.repository.crawler.insurance.changchun.InsuranceChangchunAccountInfoRepository;
import com.microservice.dao.repository.crawler.insurance.changchun.InsuranceChangchunEndowmentInfoRepository;
import com.microservice.dao.repository.crawler.insurance.changchun.InsuranceChangchunUserInfoRespository;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.InsuranceChangchunParser;


/**
 * @author lyx
 * @time 20170728
 * @category 社保-长春
 */

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance"})

//调用各种方法

public class InsuranceChangchunService {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceChangchunService.class);
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceChangchunParser insuranceChangchunParser;
	@Autowired
	private InsuranceChangchunUserInfoRespository insuranceChangchunUserInfoRespository;
	@Autowired
	private InsuranceChangchunAccountInfoRepository insuranceChangchunAccountInfoRepository;
	@Autowired
	private InsuranceChangchunEndowmentInfoRepository insuranceChangchunEndowmentInfoRepository;
	@Autowired
	private HtmlInsuranceChangchunRepository htmlInsuranceChangchunRepository;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @param session 
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
			WebParam webParam = insuranceChangchunParser.login(insuranceRequestParameters);
			
			//如果是null，就说明timeout
			if(null == webParam){
				tracer.addTag("InsuranceChangchunService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				log.info("登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;			
			}else{
				//获取登录也的html
				String html = webParam.getPage().getWebResponse().getContentAsString();
				tracer.addTag("InsuranceChangchunService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				//判断各种异常登录信息
				if(html.contains("个人基本信息")){
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,webParam.getPage());
					return taskInsurance;					
				}else if(html.contains("登录信息有误")){
					taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					tracer.addTag("InsuranceChengduService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败！" );
					return taskInsurance;
				}else if(html.contains("Error 500--Internal Server Error")){
					taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
					tracer.addTag("InsuranceChengduService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败！" );
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
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		
		//获取爬取用户信息时，网页的基础信息
		WebParam webParam = insuranceChangchunParser.getUserInfo(taskInsurance,cookies);
		
		//如果有值
		if(null != webParam){
			//将爬取到的个人信息入库
			insuranceChangchunUserInfoRespository.save(webParam.getInsuranceChangchunUserInfo());
			tracer.addTag("InsuranceChangchunService.getUserInfo 个人信息", "个人信息已入库!");
			
			tracer.addTag("InsuranceChangchunService.getUserInfo:SUCCESS", insuranceRequestParameters.getTaskId());
			//在库里修改状态
			insuranceService.changeCrawlerStatusUserInfo(taskInsurance,webParam.getCode());
			
			//保存爬取网页的源代码
			InsuranceChangchunHtml insuranceChangchunHtml = new InsuranceChangchunHtml();
			insuranceChangchunHtml.setDescription("userinfo");
			insuranceChangchunHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceChangchunHtml.setUrl(webParam.getUrl());
			insuranceChangchunHtml.setHtml(webParam.getHtml());
			htmlInsuranceChangchunRepository.save(insuranceChangchunHtml);
			tracer.addTag("InsuranceChangchunService.getUserInfo 个人信息","个人信息源码表入库!");	
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		
		log.info("-----------个人信息已入库-------------");
	}
	/**
	 * @Des 获取账户信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getAccountInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		
		WebParam webParam = insuranceChangchunParser.getAccountInfo(taskInsurance,cookies);
		
		if(null != webParam){
			insuranceChangchunAccountInfoRepository.save(webParam.getInsuranceChangchunAccountInfo());
			tracer.addTag("InsuranceChangchunService.getAccountInfo 个人账户信息", "个人账户信息已入库!");
			
			
			tracer.addTag("InsuranceChangchunService.getAccountInfo:SUCCESS", insuranceRequestParameters.getTaskId());
//			insuranceService.changeCrawlerStatusAccountInfo(taskInsurance,webParam.getCode());
			
			InsuranceChangchunHtml insuranceChangchunHtml = new InsuranceChangchunHtml();
			insuranceChangchunHtml.setDescription("accountinfo");
			insuranceChangchunHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceChangchunHtml.setUrl(webParam.getUrl());
			insuranceChangchunHtml.setHtml(webParam.getHtml());			
			htmlInsuranceChangchunRepository.save(insuranceChangchunHtml);
			tracer.addTag("InsuranceChangchunService.getAccountInfo","个人账户信息源码表入库!");	
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		log.info("-----------个人账户信息已入库-------------");
	}
	
	/**
	 * @Des 获取养老保险信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	@Async
	public void getEndowmentInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam = insuranceChangchunParser.getEndowmentInfo(taskInsurance,cookies);
		if(null != webParam){
			if(null != webParam.getHtmlPage()){
				List<HtmlPage> htmls = webParam.getHtmlPage();
				List<InsuranceChangchunHtml> changchunHtmls = new ArrayList<InsuranceChangchunHtml>();
				for (int i = 0; i < htmls.size(); i++) {
					InsuranceChangchunHtml insuranceChangchunHtml = new InsuranceChangchunHtml();
					insuranceChangchunHtml.setDescription("endowmentinfo");
					insuranceChangchunHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceChangchunHtml.setUrl(htmls.get(i).getUrl().toString());
					insuranceChangchunHtml.setHtml(htmls.get(i).asXml());
					insuranceChangchunHtml.setPageCount(i+"");
					changchunHtmls.add(insuranceChangchunHtml);
				}
				htmlInsuranceChangchunRepository.saveAll(changchunHtmls);
				tracer.addTag("getAccountInfo ==>","养老保险信息源码表入库!");	
			}
			if(null != webParam.getList()){
				insuranceChangchunEndowmentInfoRepository.saveAll(webParam.getList());
				tracer.addTag("getEndowmentInfo ==>", "养老保险信息已入库!");
				insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】已采集完成", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】已采集完成,无数据", "CRAWLER_YANGLAO_MSG", 201, taskInsurance);
			}
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			
		}
		log.info("-----------养老保险信息已入库-------------");
	}

	/**
	 * 获取失业保险信息
	 * @param insuranceRequestParameters
	 * @throws Exception
	 */
	@Async
	public void getUnemploymentInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		WebParam webParam = insuranceChangchunParser.getUnemploymentInfo(taskInsurance,cookies);
		if(null != webParam){
			if(null !=webParam.getHtmlPage()){
				List<HtmlPage> htmls = webParam.getHtmlPage();
				List<InsuranceChangchunHtml> changchunHtmls = new ArrayList<InsuranceChangchunHtml>();
				for (int i = 0; i < htmls.size(); i++) {
					InsuranceChangchunHtml insuranceChangchunHtml = new InsuranceChangchunHtml();
					insuranceChangchunHtml.setDescription("unemploymentinfo");
					insuranceChangchunHtml.setTaskid(insuranceRequestParameters.getTaskId());
					insuranceChangchunHtml.setUrl(htmls.get(i).getUrl().toString());
					insuranceChangchunHtml.setHtml(htmls.get(i).asXml());
					insuranceChangchunHtml.setPageCount(i+"");
					changchunHtmls.add(insuranceChangchunHtml);
				}
				htmlInsuranceChangchunRepository.saveAll(changchunHtmls);
				tracer.addTag("getAccountInfo ==>","失业保险信息源码表入库!");	
			}
			if(null != webParam.getList()){
				insuranceChangchunEndowmentInfoRepository.saveAll(webParam.getList());
				tracer.addTag("getEndowmentInfo ==>", "失业保险信息已入库!");
				insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】已采集完成", "CRAWLER_SHIYE_MSG", 200, taskInsurance);
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】已采集完成,无数据", "CRAWLER_SHIYE_MSG", 201, taskInsurance);
			}
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		log.info("-----------失业保险信息已入库-------------");
		}
}
