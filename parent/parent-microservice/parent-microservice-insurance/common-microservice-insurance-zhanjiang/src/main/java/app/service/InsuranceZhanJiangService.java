package app.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhanjiang.InsuranceZhanJiangHtml;
import com.microservice.dao.repository.crawler.insurance.zhanjiang.InsuranceZhanJiangGeneralInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zhanjiang.InsuranceZhanJiangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.zhanjiang.InsuranceZhanJiangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceZhanJiangParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhanjiang"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhanjiang"})
public class InsuranceZhanJiangService implements InsuranceLogin, InsuranceCrawler{

	private static WebDriver driver;
	
	
	@Autowired
	private WebDriverService webDriverService;
//	private InsuranceSuQianAndLianYunGangCommonService insuranceSuQianAndLianYunGangCommonService;
	@Autowired
	private InsuranceZhanJiangGeneralInfoRepository insuranceZhanJiangGeneralInfoRepository;
	@Autowired
	private InsuranceZhanJiangUserInfoRepository insuranceZhanJiangUserInfoRepository;
	@Autowired
	private InsuranceZhanJiangHtmlRepository insuranceZhanJiangHtmlRepository;
	@Autowired
	private InsuranceZhanJiangParser insuranceZhanJiangParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private AgentService agentService;
	@Autowired
	private TracerLog tracer;
	
	@Override
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("crawler.service.login.taskid",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		try {
			driver = webDriverService.intiChrome();
			WebParam webParam = insuranceZhanJiangParser.login(insuranceRequestParameters, driver);
			driver = webParam.getDriver();
			
			InsuranceZhanJiangHtml html = new InsuranceZhanJiangHtml();
			html.setUrl(driver.getCurrentUrl().toString());
			html.setType("logined");
			html.setTaskid(taskInsurance.getTaskid());
			html.setHtml(driver.getPageSource());
			insuranceZhanJiangHtmlRepository.save(html);
			
			if(null != webParam.getHtml()){
				insuranceService.changeLoginStatus("LOGIN", "ERROR", webParam.getHtml(), taskInsurance);
				tracer.addTag("crawler.service.login.fail", "登陆失败："+webParam.getHtml());
				quitDriver(insuranceRequestParameters);
			}else{
				insuranceService.changeLoginStatus("LOGIN", "SUCCESS", "登录成功！", taskInsurance);
				tracer.addTag("crawler.service.login.success", "登陆成功");
				//登陆成功，开始采集数据。
//				taskInsurance = getData(insuranceRequestParameters, taskInsurance);
			}
		} catch (Exception e) {
			insuranceService.changeLoginStatus("LOGIN", "ERROR", "连接超时！", taskInsurance);
			tracer.addTag("crawler.service.login.Exception", e.toString());
			e.printStackTrace();
			quitDriver(insuranceRequestParameters);
		}
		taskInsurance = insuranceService.findTaskInsurance(taskInsurance.getTaskid());
		return taskInsurance;
	}
	
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		tracer.addTag("crawler.service.crawler.taskid", taskInsurance.getTaskid());
		String idNUm = insuranceRequestParameters.getUserIDNum();
		try {
			//获取用户信息
			String infoUrl = "http://219.132.4.6:6012/web/ajax.do?r=0.7635891010868991&_isModel=true&params={'oper':'JbgrxxcxAction.query','params':{'MenuId':'104014'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'"+idNUm+"'}}}}";
			driver.get(infoUrl);
			tracer.addTag("crawler.service.crawler.userinfo.page", driver.getPageSource());
			InsuranceZhanJiangHtml html = new InsuranceZhanJiangHtml();
			html.setUrl(infoUrl);
			html.setType("userInfo");
			html.setTaskid(taskInsurance.getTaskid());
			html.setHtml(driver.getPageSource());
			insuranceZhanJiangHtmlRepository.save(html);
			WebParam webParam = insuranceZhanJiangParser.getUserInfo(driver.getPageSource(), taskInsurance.getTaskid());
			if(null != webParam.getList()){
				insuranceZhanJiangUserInfoRepository.saveAll(webParam.getList());
				insuranceService.changeCrawlerStatus("数据采集中，【个人信息】已采集成功", "CRAWLER_USER_MSG", 200, taskInsurance);
				tracer.addTag("crawler.service.crawler.userinfo.success", "【个人信息】已采集成功");
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【个人信息】已采集完成", "CRAWLER_USER_MSG", 201, taskInsurance);
				tracer.addTag("crawler.service.crawler.userinfo.fail", "【个人信息】已采集完成");
			}
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
			insuranceService.changeCrawlerStatus("数据采集中，【个人信息】已采集完成", "CRAWLER_USER_MSG", 404, taskInsurance);
			tracer.addTag("crawler.service.crawler.userinfo.Exception", e.toString());
		}
		
		//获取社保缴纳信息
		for (int i = 1; i < 6; i++) {
			String type = "";
			String description = "";
			if(i == 1){
				type = "CRAWLER_YANGLAO_MSG";
				description = "养老信息";
			}
			if(i == 2){
				type = "CRAWLER_GONGSHANG_MSG";
				description = "工伤信息";
			}
			if(i == 3){
				type = "CRAWLER_SHENGYU_MSG";
				description = "生育信息";
			}
			if(i == 4){
				type = "CRAWLER_SHIYE_MSG";
				description = "失业信息";
			}
			if(i == 5){
				type = "CRAWLER_YILIAO_MSG";
				description = "医疗信息";
			}
			try {
				String Url = "http://219.132.4.6:6012/web/ajax.do?_isModel=true&params={'oper':'ZbgrjfqkcxAction.query','params':{'MenuId':'104020'},'datas':{'ncm_gt_查询条件':{'params':{'证件号码':'"+idNUm+"','险种类型':'"+i+"0'}},'ncm_glt_个人已缴历史明细':{'heads':[],'params':{'pageSize':500,'curPageNum':1,'rowsCount':0,'Total_showMsg':null,'Total_showMsgCell':null,'Total_Cols':[]},'heads_change':[],'dataset':[]}}}";
				tracer.addTag("crawler.service.crawler."+type+".url", Url);
				driver.get(Url);
				tracer.addTag("crawler.service.crawler."+type+".page", driver.getPageSource());
				InsuranceZhanJiangHtml html2 = new InsuranceZhanJiangHtml();
				html2.setUrl(Url);
				html2.setType(type);
				html2.setTaskid(taskInsurance.getTaskid());
				html2.setHtml(driver.getPageSource());
				insuranceZhanJiangHtmlRepository.save(html2);
				WebParam webParam = insuranceZhanJiangParser.getGeneralInfo(driver.getPageSource(), taskInsurance.getTaskid(), i);
				if(null != webParam.getList()){
					insuranceZhanJiangGeneralInfoRepository.saveAll(webParam.getList());
					insuranceService.changeCrawlerStatus("数据采集中，【"+description+"】已采集成功", type, 200, taskInsurance);
					tracer.addTag("crawler.service.crawler."+type+".success", "【"+description+"】已采集成功");
				}else{
					insuranceService.changeCrawlerStatus("数据采集中，【"+description+"】已采集完成", type, 201, taskInsurance);
					tracer.addTag("crawler.service.crawler."+type+".fail", "【"+description+"】已采集完成");
				}
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
				insuranceService.changeCrawlerStatus("数据采集中，【"+description+"】已采集完成", type, 404, taskInsurance);
				tracer.addTag("crawler.service.crawler."+type+".Exception", e.toString());
			}
		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		taskInsurance = insuranceService.findTaskInsurance(taskInsurance.getTaskid());
		quitDriver(insuranceRequestParameters);
		return taskInsurance;
	}


	public TaskInsurance quitDriver(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("quit", insuranceRequestParameters.toString()); 
		//关闭task (只是 finish = true 、 error_code=-1 、error_message = 系统超时请重试  , description、Phases、PhasesStatus 都不改变，以便查看当时的状态 )
		TaskInsurance taskInsurance = insuranceService.systemClose(true, insuranceRequestParameters.getTaskId());  
		//调用公用释放资源方法
		if(taskInsurance != null){
			agentService.releaseInstance(taskInsurance.getCrawlerHost(), driver);
		} else{
			tracer.addTag("quit taskInsurance is null",""); 
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
