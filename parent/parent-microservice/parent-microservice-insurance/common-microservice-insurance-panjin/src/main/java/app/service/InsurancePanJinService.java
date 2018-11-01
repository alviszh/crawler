package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * @description:
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.panjin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.panjin"})
public class InsurancePanJinService extends InsuranceLinYiTemplateCommonService  implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsurancePanJinCrawlerService insurancePanJinCrawlerService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	
	public static Integer captchaErrorCount;
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		captchaErrorCount=0;
		String captcha="";
		try {
			captcha=getCaptcha(taskInsurance);
		} catch (Exception e) {
			tracer.addTag("获取图片验证码出现异常", e.toString());
		}
		//调用公共登录方法(算上首次登录，验证码识别错误或者是过期，共计有三次重新识别并登录的机会)(即重试登录两次)
		try {
			captchaErrorCount=commonLogin(insuranceRequestParameters,taskInsurance,captcha,captchaErrorCount);  
			if(captchaErrorCount>0 && captchaErrorCount<3){      //验证码错误一次
				captcha = getCaptcha(taskInsurance); //重新调获取图片验证码的方法
				captchaErrorCount=commonLogin(insuranceRequestParameters,taskInsurance,captcha,captchaErrorCount);   
				if(captchaErrorCount>0 && captchaErrorCount<3){   //验证码错误两次
					captcha = getCaptcha(taskInsurance); 
					captchaErrorCount=commonLogin(insuranceRequestParameters,taskInsurance,captcha,captchaErrorCount);   
					if(captchaErrorCount>0 && captchaErrorCount<3){
						captcha = getCaptcha(taskInsurance); 
						captchaErrorCount=commonLogin(insuranceRequestParameters,taskInsurance,captcha,captchaErrorCount);  
					}else{
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus(),
								"登录失败，错误原因"
								+ "：验证码错误,请重新填写！",taskInsurance);
						return taskInsurance;
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录发生异常，异常信息是：", e.toString());
			insuranceService.changeLoginStatusException(taskInsurance);
		}
		return taskInsurance;
	}
	
	//异步爬取所有数据
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId().trim());
		try {
			Future<String> future=insurancePanJinCrawlerService.getUserInfo(insuranceRequestParameters,taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
//		如下是五种保险的爬取
		try {
			Future<String> future=insurancePanJinCrawlerService.getPension(insuranceRequestParameters,taskInsurance);
			listfuture.put("getPension", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getPension.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insurancePanJinCrawlerService.getMedical(insuranceRequestParameters,taskInsurance);
			listfuture.put("getMedical", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMedical.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insurancePanJinCrawlerService.getInjury(insuranceRequestParameters,taskInsurance);
			listfuture.put("getInjury", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getInjury.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insurancePanJinCrawlerService.getBear(insuranceRequestParameters,taskInsurance);
			listfuture.put("getBear", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBear.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		try {
			Future<String> future=insurancePanJinCrawlerService.getUnemployment(insuranceRequestParameters,taskInsurance);
			listfuture.put("getUnemployment", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUnemployment.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
					201, taskInsurance);
		}
		
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskInsurance.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);//没有生育险和工伤险，为了最后更改状态成功，此处设置为定值
			insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
