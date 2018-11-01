package app.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * @description: 登录模板，南阳市、平顶山市、商丘市等社保的公共登录service
 * @author: sln 
 */

public class InsuranceNanYangLoginTemplateService extends InsuranceService{
	@Autowired
	private InsuranceService insuranceService;
	@Value("${zonecode}")    //区域码
	public String zonecode;
	@Value("${loginhost}")
	public String loginhost;
	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public static int captchaErrorCount;
	
	public TaskInsurance commonLogin(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance){
		try {
			//先请求链接登录链接，获取图片验证码对象
			String url="http://"+loginhost+"/siq/indexsz.jsp?zoneCode="+zonecode.trim()+"";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			HtmlPage hpage = webClient.getPage(webRequest);
			HtmlImage image = hpage.getFirstByXPath("//img[@id='perVaidImg']");   //个人登录方式的图片验证码
			String code = chaoJiYingOcrService.getVerifycode(image, "1902");
			//需要先校验图片验证码，图片验证码的校验链接和登录账号密码的验证链接不一样
			url="http://"+loginhost+"/siq/pages/security/result.jsp?s=0.9832108861612767";
			String requestBody="fieldId=perVaidImgText&fieldValue="+code+"";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page=webClient.getPage(webRequest);
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();
				if(html.contains("验证码错误")){
					captchaErrorCount++;
					if (captchaErrorCount<=2){
						tracer.addTag("action.login.captchaErrorCount","解析图片验证码失败"+captchaErrorCount+"次，重新执行登录方法");
						commonLogin(insuranceRequestParameters, taskInsurance);
					} else {
						captchaErrorCount=0;
						insuranceService.changeLoginStatusCaptError(taskInsurance);
						return taskInsurance;
					}
				}else{
					url="http://"+loginhost+"/siq/web/szloginWeb.action";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					requestBody=""
							+ "user.userCode="+insuranceRequestParameters.getUsername().trim()+""
							+ "&user.psw="+insuranceRequestParameters.getPassword().trim()+""
							+ "&user.zoneCode="+zonecode.trim()+""
							+ "&user.userType=3";
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);    
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						if(html.contains("success")){
							taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
							taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
							taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
							String logincookies = CommonUnit.transcookieToJson(webClient);
						    taskInsurance.setCookies(logincookies);
						    taskInsuranceRepository.save(taskInsurance);
						    Thread.sleep(1000);  
						}else{
							if(html.contains("用户不存在")){
								insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
										"用户不存在",taskInsurance);
							}else if(html.contains("用户名或密码错误")){
								tracer.addTag("登录信息有误：", "用户名或密码错误");
								insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
										InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription(),taskInsurance);
							}else{
								tracer.addTag("登录信息有误,详见验证登录信息之后返回的html", html);
								insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
										InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
										"系统繁忙，请稍后再试！",taskInsurance);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录时出现了异常，在catch中更新登录状态为系统繁忙", e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					"系统繁忙，请稍后再试！",taskInsurance);
		}
		return taskInsurance;
	}
}
