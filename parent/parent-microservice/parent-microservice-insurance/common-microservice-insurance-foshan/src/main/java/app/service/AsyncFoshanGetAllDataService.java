package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceFoshanParser;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.foshan"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.foshan"})
public class AsyncFoshanGetAllDataService implements InsuranceLogin{

	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceFoshanParser insuranceFoshanParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private GetUserInfoService getUserInfoService;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 */
	@Override
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters){
		
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		try {
			WebParam webParam = insuranceFoshanParser.login(insuranceRequestParameters);
			if(null == webParam){
				tracer.addTag("parser.login.Error", "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
			}else{
				tracer.addTag("parser.login.webParam", webParam.toString());
				
				String html = webParam.getPage().getWebResponse().getContentAsString();
				tracer.addTag("parser.login.html", "<xmp>"+html+"</xmp>");
				//网站500
				if(html.contains("Error 500--Internal Server Error")){
					taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
				}
				//账号或密码有误
				else if(html.contains("登陆的用户不存在或是密码不匹配!")){
					taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
				}
				else if(null == webParam.getPage()){
					taskInsurance = insuranceService.changeLoginStatusIdnumError(taskInsurance);
				}else if(html.contains("该用户不存在个人资料，您可能未参保。")){
					taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
				}else if(html.contains("请输入校验码")){
					tracer.addTag("parser.login", "进入图片验证码校验页面");
					//如果三次验证码都无法通过就不再继续登录，将taskinsurance状态改为验证码错误。
					for (int i = 0; i < 3; i++) {
						webParam = insuranceFoshanParser.loginTwo(webParam);
						if(null == webParam){
							tracer.addTag("parser.login.Error", "登录页获取超时！");
							taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
							return taskInsurance;
						}else{
							String html2 = webParam.getPage().getWebResponse().getContentAsString();
							tracer.addTag("parser.login.html2", "<xmp>"+html2+"</xmp>");
							//网站500
							if(!html2.contains("Error 500--Internal Server Error")){
								//判断验证码错误
//								if(html2.contains("图形校验码验证失败,请重新输入")){
//									if(i == 2){
//										taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
//									}
//								}
								if(html2.contains("次登陆！请妥善保管密码，并定期修改密码！")){
									taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance,webParam.getPage());
									tracer.addTag("parser.login.taskInsurance", taskInsurance.toString());
									return taskInsurance;
								}else if(i == 2){
									taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
								}
							}else{
								tracer.addTag("parser.login.Error", "页面500");
								taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
								return taskInsurance;
							}
						}
					}
				}else{
					tracer.addTag("parser.login.Error", "登录不成功，未能进入图片验证页面");
					taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
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
	 * @throws Exception 
	 */
	@Override
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		//爬取个人信息
		try {
			getUserInfoService.getUserInfo(taskInsurance);
			tracer.addTag("parser.crawler.getUserinfo", "个人信息已入库");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskInsurance;
	}
	
	/**
	 * @Des 判断是否真正登陆
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public boolean isLogin(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam webParam = insuranceFoshanParser.login(insuranceRequestParameters);
		if(null == webParam){
			return false;
		}else{
			tracer.addTag("parser.login.webParam", webParam.toString());
			
			String html = webParam.getPage().getWebResponse().getContentAsString();
			tracer.addTag("parser.login.html", "<xmp>"+html+"</xmp>");
			//网站500
			if(html.contains("Error 500--Internal Server Error")){
				return false;
			}
			//账号或密码有误
			else if(html.contains("登陆的用户不存在或是密码不匹配!")){
				return false;
			}
			if(null == webParam.getPage()){
				return false;
			}
			if(html.contains("该用户不存在个人资料，您可能未参保。")){
				return false;
			}
			if(html.contains("请输入校验码")){
				webParam = insuranceFoshanParser.loginTwo(webParam);
				String html2 = webParam.getPage().getWebResponse().getContentAsString();
				if(html2.contains("次登陆！请妥善保管密码，并定期修改密码！")){
					String cookies = CommonUnit.transcookieToJson(webParam.getPage().getWebClient());
					taskInsuranceRepository.updateCookiesByTaskid(cookies, taskInsurance.getTaskid());
					return true;
				}else{
					return false;
				}
			}
			return false;
			
		}
		
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
