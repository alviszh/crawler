package app.service;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.crawler.car.insurance.bean.CarInsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.CarInsuranceSms;
import app.service.aop.CarInsuranceVerify;
import app.unit.PingAnUnit;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.car.insurance.pingan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.car.insurance.pingan"})
public class PingAnService implements CarInsuranceVerify,CarInsuranceSms{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private CarInsuranceService carInsuranceService;
	@Autowired
	private PingAnServiceUnit pingAnServiceUnit;
	//登录
	@Override
	public TaskCarInsurance verify(CarInsuranceRequestBean carInsuranceRequestBean) {
		tracer.addTag("pingan.verify.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = carInsuranceService.getTaskCarInsurance(carInsuranceRequestBean.getTaskid());
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_LOADING.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_LOADING.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_LOADING.getDescription());
		taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		PingAnUnit pingAnUnit = pingAnServiceUnit.verify(carInsuranceRequestBean);
		if(pingAnUnit.getState().equals("保单号及其证件号正确，开始发送短信")){
			String transcookieToJson = CommonUnit.transcookieToJson(pingAnUnit.getPage().getEnclosingWindow().getWebClient());
			taskCarInsurance.setCookies(transcookieToJson);
			taskCarInsurance.setParam(pingAnUnit.getVerifyCode());
			taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_SUCCESS.getPhase());
			taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_SUCCESS.getPhasestatus());
			taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_SUCCESS.getDescription());
			taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		}else{
			taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_ERROR.getPhase());
			taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_LOGIN_ERROR.getPhasestatus());
			taskCarInsurance.setDescription(pingAnUnit.getState());
			taskCarInsurance.setFinished(false);
			taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		}
		return taskCarInsurance;
	}
	//发送短信
	@Override
	public TaskCarInsurance sendSms(CarInsuranceRequestBean carInsuranceRequestBean) {
		tracer.addTag("pingan.sendSms.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = carInsuranceService.getTaskCarInsurance(carInsuranceRequestBean.getTaskid());
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_SEND_CODE_DONING.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_SEND_CODE_DONING.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_SEND_CODE_DONING.getDescription());
		taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		try {
			PingAnUnit pingAnUnit= pingAnServiceUnit.sendSms(carInsuranceRequestBean,taskCarInsurance);
			if(pingAnUnit.getState().equals("短信发送成功")){
				String transcookieToJson = CommonUnit.transcookieToJson(pingAnUnit.getPage().getEnclosingWindow().getWebClient());
				taskCarInsurance.setCookies(transcookieToJson);
				taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_SUCCESS.getPhase());
				taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_SUCCESS.getPhasestatus());
				taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_SUCCESS.getDescription());
				taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
			}else{
				taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_ERROR.getPhase());
				taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_ERROR.getPhasestatus());
				taskCarInsurance.setDescription(pingAnUnit.getState());
				taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_ERROR.getPhase());
			taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_ERROR.getPhasestatus());
			taskCarInsurance.setDescription("短信发送失败，可能是网络延迟导致。。。。");
			taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
			tracer.addTag("pingan.sendSms.Exception", "发送短信页面获取不到按钮元素"+e.getMessage());
		}
		return taskCarInsurance;
	}
	//验证短信
	@Override
	public TaskCarInsurance verifySms(CarInsuranceRequestBean carInsuranceRequestBean) {
		TaskCarInsurance taskCarInsurance = carInsuranceService.getTaskCarInsurance(carInsuranceRequestBean.getTaskid());
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_DONING.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_DONING.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_WAIT_CODE_DONING.getDescription());
		taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		try {
			PingAnUnit pingAnUnit = pingAnServiceUnit.verifySms(carInsuranceRequestBean,taskCarInsurance);
			if(pingAnUnit.getState().equals("验证成功")){
				taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_SUCCESS.getPhase());
				taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_SUCCESS.getPhasestatus());
				taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_SUCCESS.getDescription());
				taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
			}else{
				taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_ERROR.getPhase());
				taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_ERROR.getPhasestatus());
				taskCarInsurance.setDescription(pingAnUnit.getState());
				taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("发送短信网址发送改变。。。。", e.getMessage());
			taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_ERROR.getPhase());
			taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_ERROR.getPhasestatus());
			taskCarInsurance.setDescription("验证短信时发生未知错误，可能是由于网络波动造成，请稍后重试！");
			taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		}
		return taskCarInsurance;
	}
	//获取信息
	@Override
	public TaskCarInsurance getAllData(CarInsuranceRequestBean carInsuranceRequestBean) {
		TaskCarInsurance taskCarInsurance = carInsuranceService.getTaskCarInsurance(carInsuranceRequestBean.getTaskid());
		taskCarInsurance.setCompanyName("平安保险");
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_DOING.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_DOING.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_DOING.getDescription());
		taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskCarInsurance.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		
		try {
			pingAnServiceUnit.getBasicInformation(carInsuranceRequestBean,taskCarInsurance,webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("首页信息没有获取成功", e.getMessage());
		}
		
		try {
			pingAnServiceUnit.getUserInfo(carInsuranceRequestBean,taskCarInsurance,webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("详细信息没有获取成功", e.getMessage());
		}
		
		/**
		 * 测试帐号没有出险信息（未完成）
		 */
		try {
			pingAnServiceUnit.getAccidentInfo(carInsuranceRequestBean,taskCarInsurance,webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("出险信息没有获取成功", e.getMessage());
		}
		
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_SUCCESS.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_SUCCESS.getDescription());
		taskCarInsurance.setFinished(true);
		taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		return taskCarInsurance;
	}








	@Override
	public TaskCarInsurance getAllDataDone(String taskid) {
		// TODO Auto-generated method stub
		return null;
	}
}
