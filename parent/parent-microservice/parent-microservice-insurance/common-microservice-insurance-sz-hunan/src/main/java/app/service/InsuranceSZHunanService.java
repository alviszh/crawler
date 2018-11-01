package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceSZHunanParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.hunan"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.hunan"})
public class InsuranceSZHunanService implements InsuranceLogin, InsuranceCrawler{

	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZHunanParser insuranceSZHunanParser;
	@Autowired
	private AsyncGetDataService asyncGetDataService;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 */
	@Override
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		tracer.addTag("parser.login.service",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			WebParam webParam = insuranceSZHunanParser.login(taskInsurance, insuranceRequestParameters);
			if(null != webParam){
				if(null != webParam.getCode() && 200 == webParam.getCode()){
					if(null != webParam.getHtmlPage()){
						HtmlPage htmlPage = webParam.getHtmlPage();
						tracer.addTag("parser.login.service.loginedHtmlPage","<xmp>"+htmlPage.asXml()+"</xmp>");
						//根据页面网址判断登陆后的页面是否登陆成功
						System.out.println(htmlPage);
						if(htmlPage.getBaseURI().contains("http://sbk.hn12333.com:7002/PubQuery/commbiz/commonInfoAction!queryPersonBaseInfo")){
							taskInsurance.setTesthtml(insuranceRequestParameters.toString());
							taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,webParam.getHtmlPage());
							tracer.addTag("parser.login.service.success","登陆成功！");
						}else{
							taskInsurance.setDescription("输入的身份证号、姓名或密码错误！");
							taskInsurance.setPhase("LOGIN");
							taskInsurance.setPhase_status("ERROR");
							taskInsuranceRepository.save(taskInsurance);
							tracer.addTag("parser.login.service.fail","登陆失败！");
						}
					}
				}else{
					taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
					tracer.addTag("parser.login.service.status","登录页面异常或点击登录后页面异常");
				}
			}else{
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				tracer.addTag("parser.login.service.status","登录页面异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
			tracer.addTag("parser.login.service.error",e.toString());
		}
		return taskInsurance;
	}
	
	
	/**
	 * @Des 更新taskInsurance
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {	
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}



	/**
	 * @Des 爬取总接口
	 * @param insuranceRequestParameters 
	 * @param taskInsurance
	 * @throws Exception 
	 */
	@Override
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if(null != taskInsurance){
			asyncGetDataService.getUserInfo(taskInsurance);
			asyncGetDataService.getPensionInfo(insuranceRequestParameters, taskInsurance);
			asyncGetDataService.getInsuranceInfo(taskInsurance);
		}
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
