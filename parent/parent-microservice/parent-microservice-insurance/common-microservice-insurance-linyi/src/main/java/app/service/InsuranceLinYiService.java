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
 * @date: 2017年12月7日 下午5:24:35 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.linyi"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.linyi"})
public class InsuranceLinYiService extends InsuranceLinYiTemplateCommonService  implements InsuranceLogin{
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceLinYiCrawlerService insuranceLinYiCrawlerService;
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
		//虽然临沂市的社保图片验证码识别错误也能登录，但为了避免网站改版，此处还是先获取验证码
		String captcha="";
		try {
			captcha=getCaptcha(taskInsurance);
		} catch (Exception e) {
			tracer.addTag("获取图片验证码出现异常", e.toString());
		}
		//调用公共登录方法
		try {
			commonLogin(insuranceRequestParameters,taskInsurance,captcha,captchaErrorCount);
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
		TaskInsurance taskInsurance =taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=insuranceLinYiCrawlerService.getUserInfo(insuranceRequestParameters,taskInsurance);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		
		try {
			Future<String> future=insuranceLinYiCrawlerService.getChargeInfo(insuranceRequestParameters,taskInsurance);
			listfuture.put("getChargeInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeInfo.e", taskInsurance.getTaskid()+"===>"+e.toString());
		}
		
		try {
			Future<String> future=insuranceLinYiCrawlerService.getChargeDetail(insuranceRequestParameters,taskInsurance);
			listfuture.put("getChargeDetail", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeDetail.e", taskInsurance.getTaskid()+"===>"+e.toString());
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
