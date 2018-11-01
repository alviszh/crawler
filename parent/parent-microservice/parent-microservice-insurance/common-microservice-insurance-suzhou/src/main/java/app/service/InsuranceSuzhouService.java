package app.service;

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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.suzhou.InsuranceSuzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.suzhou.HtmlInsuranceSuzhouRepository;
import com.microservice.dao.repository.crawler.insurance.suzhou.InsuranceSuzhouBirthRepository;
import com.microservice.dao.repository.crawler.insurance.suzhou.InsuranceSuzhouEnterprisePensionRepository;
import com.microservice.dao.repository.crawler.insurance.suzhou.InsuranceSuzhouTownsWorkersRepository;
import com.microservice.dao.repository.crawler.insurance.suzhou.InsuranceSuzhouUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.suzhou.InsuranceSuzhouUserRepository;
import com.microservice.dao.repository.crawler.insurance.suzhou.InsuranceSuzhouWorkInjuryRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.InsuranceSuzhouParser;


/**
 * @author lyx
 * @time 20170728
 * @category 社保-长春
 */

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance"})
//调用各种方法
public class InsuranceSuzhouService {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceSuzhouService.class);
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSuzhouParser insuranceSuzhouParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceSuzhouEnterprisePensionRepository insuranceSuzhouEnterprisePensionRepository;
	@Autowired
	private InsuranceSuzhouTownsWorkersRepository  insuranceSuzhouTownsWorkersRepository;
	@Autowired
	private InsuranceSuzhouUnemploymentRepository  insuranceSuzhouUnmploymentRepository;
	@Autowired
	private InsuranceSuzhouBirthRepository  insuranceSuzhouBirthRepository;
	@Autowired
	private InsuranceSuzhouWorkInjuryRepository insuranceSuzhouWorkInjuryRepository;
	@Autowired
	private HtmlInsuranceSuzhouRepository   htmlInsuranceSuzhouRepository;
	@Autowired
	private InsuranceSuzhouUserRepository insuranceSuzhouUserRepository;
	
	
	
	//登录
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters)throws Exception  {
		tracer.addTag("InsuranceSuzhouService.login", insuranceRequestParameters.getTaskId());
		
		//先通过taskid在数据控充查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		if(null != taskInsurance){
			WebParam webParam = insuranceSuzhouParser.login(insuranceRequestParameters,0);
			if(null == webParam){
				tracer.addTag("InsuranceSuzhouService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				log.info("登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;			
			}else{
				//获取登录也的html
				String html = webParam.getPage().getWebResponse().getContentAsString();
				tracer.addTag("InsuranceSuzhouService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				System.out.println(html);
				//判断各种异常登录信息
				if(html.contains("单位申报缴费")){
					taskInsurance= insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					tracer.addTag("InsuranceSuzhouService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败，所输入的个人编号和身份证号不匹配！" );
				}else if(html.contains("验证码输入有误")){
					taskInsurance=insuranceService.changeLoginStatusCaptError(taskInsurance);
					tracer.addTag("InsuranceSuzhouService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败，所输入的验证码错误！" );
				}else{
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,webParam.getPage());
			}
			}
		}
		return null;
	}


	//修改状态
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}


	//获取企业养老信息
	@Async
	public void getEnterprisePensionInfo(InsuranceRequestParameters insuranceRequestParameters)throws Exception  {
		//根据taskid查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
				
		//将cookies从库里取出来
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		
		//获取爬取企业养老信息时，网页的基础信息
		WebParam webParam = insuranceSuzhouParser.getEnterprisePensionInfo(taskInsurance,cookies,0);
		
		
		if(null !=  webParam){
			
			insuranceSuzhouEnterprisePensionRepository.saveAll(webParam.getList());
			
			tracer.addTag("InsuranceSuzhouService.getEnterprisePensionInfo 企业养老信息", "企业养老信息已入库!");
			
			insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】已采集完成", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
			
			tracer.addTag("InsuranceSuzhouService.getEnterprisePensionInfo:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceSuzhouHtml insuranceSuzhouHtml = new InsuranceSuzhouHtml();
			insuranceSuzhouHtml.setDescription("enterprisePensionInfo");
			insuranceSuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceSuzhouHtml.setUrl(webParam.getUrl());
			insuranceSuzhouHtml.setHtml(webParam.getHtml());			
			htmlInsuranceSuzhouRepository.save(insuranceSuzhouHtml);
			tracer.addTag("InsuranceSuzhouService.getEnterprisePensionInfo","企业养老信息源码表入库!");	
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		
		}


	//获取城镇职工医保信息
	@Async   
	public void getTownsWorkersInfo(InsuranceRequestParameters insuranceRequestParameters)throws Exception  {
		
		//根据taskid查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
						
		//将cookies从库里取出来
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
				
		//获取爬取城镇职工医保信息时，网页的基础信息
		WebParam  webParam = insuranceSuzhouParser.getTownsWorkersInfo(taskInsurance,cookies,0);
		
		if(null !=  webParam){
			insuranceSuzhouTownsWorkersRepository.saveAll(webParam.getList());
			
			tracer.addTag("InsuranceSuzhouService.getTownsWorkersInfo 城镇职工医保信息", "城镇职工医保信息已入库!");
			
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】已采集完成入库", "CRAWLER_YILIAO_MSG", 200, taskInsurance);
			
			tracer.addTag("InsuranceSuzhouService.getTownsWorkersInfo:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceSuzhouHtml insuranceSuzhouHtml = new InsuranceSuzhouHtml();
			insuranceSuzhouHtml.setDescription("townsWorkersInfo");
			insuranceSuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceSuzhouHtml.setUrl(webParam.getUrl());
			insuranceSuzhouHtml.setHtml(webParam.getHtml());			
			htmlInsuranceSuzhouRepository.save(insuranceSuzhouHtml);
			tracer.addTag("InsuranceSuzhouService.getTownsWorkersInfo","城镇职工医保信息源码表入库!");	
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	}


	//获取工伤保险信息
	@Async
	public void getWorkInjuryInfo(InsuranceRequestParameters insuranceRequestParameters)throws Exception{
		
		//根据taskid查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
								
		//将cookies从库里取出来
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
						
		//获取爬取用户信息时，网页的基础信息
		WebParam webParam = insuranceSuzhouParser.getWorkInjuryInfo(taskInsurance,cookies,0);
		
		if(null !=  webParam){
			insuranceSuzhouWorkInjuryRepository.saveAll(webParam.getList());
			
			insuranceService.changeCrawlerStatus("数据采集中，【工伤保险信息】已采集完成入库", "CRAWLER_GONGSHANG_MSG", 200, taskInsurance);
			
			tracer.addTag("InsuranceSuzhouService.getWorkInjuryInfo 工伤保险信息", "工伤保险信息已入库!");
			
			tracer.addTag("InsuranceSuzhouService.getWorkInjuryInfo:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceSuzhouHtml insuranceSuzhouHtml = new InsuranceSuzhouHtml();
			insuranceSuzhouHtml.setDescription("workInjuryInfo");
			insuranceSuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceSuzhouHtml.setUrl(webParam.getUrl());
			insuranceSuzhouHtml.setHtml(webParam.getHtml());			
			htmlInsuranceSuzhouRepository.save(insuranceSuzhouHtml);
			tracer.addTag("InsuranceSuzhouService.getWorkInjuryInfo","工伤保险信息源码表入库!");	
			
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	}

	//获取失业保险信息
	@Async
	public void getUnmploymentInfo(InsuranceRequestParameters insuranceRequestParameters)throws Exception   {
		
		//根据taskid查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
										
		//将cookies从库里取出来
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
								
		//获取爬取用户信息时，网页的基础信息
		WebParam webParam = insuranceSuzhouParser.getUnmploymentInfo(taskInsurance,cookies,0);
		
		if(null != webParam){
			insuranceSuzhouUnmploymentRepository.saveAll(webParam.getList());
			
			
			tracer.addTag("InsuranceSuzhouService.getUnmploymentInfo 失业保险信息", "失业保险信息已入库!");
			
			insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】已采集完成", "CRAWLER_SHIYE_MSG", 200, taskInsurance);
			
			tracer.addTag("InsuranceSuzhouService.getUnmploymentInfo:SUCCESS", insuranceRequestParameters.getTaskId());
			
			
			InsuranceSuzhouHtml insuranceSuzhouHtml = new InsuranceSuzhouHtml();
			insuranceSuzhouHtml.setDescription("unmploymentInfo");
			insuranceSuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceSuzhouHtml.setUrl(webParam.getUrl());
			insuranceSuzhouHtml.setHtml(webParam.getHtml());			
			htmlInsuranceSuzhouRepository.save(insuranceSuzhouHtml);
			tracer.addTag("InsuranceSuzhouService.getUnmploymentInfo","失业保险信息源码表入库!");	
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	}

	//获取生育保险信息
	@Async
	public void getBirthInfo(InsuranceRequestParameters insuranceRequestParameters)throws Exception  {
		
		
		//根据taskid查询当次任务
			TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
												
		//将cookies从库里取出来
			Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
										
		//获取爬取用户信息时，网页的基础信息
			WebParam webParam = insuranceSuzhouParser.getBirthInfo(taskInsurance,cookies,0);
			
			if(null!=webParam){
				insuranceSuzhouBirthRepository.saveAll(webParam.getList());
				
				insuranceService.changeCrawlerStatus("数据采集中，【生育保险信息】已采集完成入库", "CRAWLER_SHENGYU_MSG", 200, taskInsurance);
				
				tracer.addTag("InsuranceSuzhouService.getBirthInfo 生育保险信息", "生育保险信息已入库!");
				
				tracer.addTag("InsuranceSuzhouService.getBirthInfo:SUCCESS", insuranceRequestParameters.getTaskId());
				
				InsuranceSuzhouHtml insuranceSuzhouHtml = new InsuranceSuzhouHtml();
				insuranceSuzhouHtml.setDescription("birthInfo");
				insuranceSuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
				insuranceSuzhouHtml.setUrl(webParam.getUrl());
				insuranceSuzhouHtml.setHtml(webParam.getHtml());			
				htmlInsuranceSuzhouRepository.save(insuranceSuzhouHtml);
				tracer.addTag("InsuranceSuzhouService.getBirthInfo","生育保险信息源码表入库!");	
				insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
			}
			
	}


	@Async
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters)throws Exception {
		
		//根据taskid查询当次任务
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
											
	//将cookies从库里取出来
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
									
	//获取爬取用户信息时，网页的基础信息
		WebParam webParam = insuranceSuzhouParser.getUserfo(taskInsurance,cookies,0);
	
		if(null!=webParam){
			insuranceSuzhouUserRepository.save(webParam.getInsuranceSuzhouUser());
			
			insuranceService.changeCrawlerStatus("数据采集中，【个人信息】已采集完成", "CRAWLER_USER_MSG", 200, taskInsurance);
			
			tracer.addTag("InsuranceSuzhouService.getUserInfo 个人信息", "个人信息已入库!");
			
			tracer.addTag("InsuranceSuzhouService.getUserInfo:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceSuzhouHtml insuranceSuzhouHtml = new InsuranceSuzhouHtml();
			insuranceSuzhouHtml.setDescription("userInfo");
			insuranceSuzhouHtml.setTaskid(insuranceRequestParameters.getTaskId());
			insuranceSuzhouHtml.setUrl(webParam.getUrl());
			insuranceSuzhouHtml.setHtml(webParam.getHtml());			
			htmlInsuranceSuzhouRepository.save(insuranceSuzhouHtml);
			tracer.addTag("InsuranceSuzhouService.getUserfo","个人信息源码表入库!");	
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
	
	
	}
	
	
	
}
