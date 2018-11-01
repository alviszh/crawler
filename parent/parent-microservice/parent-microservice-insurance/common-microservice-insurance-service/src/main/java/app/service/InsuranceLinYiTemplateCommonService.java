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
 * @description: 登录模板，临沂、盘锦、铁岭等社保的公共service，该公共模板中的url是验证链接，因为通过该
 * 				    验证链接可以检验输入信息的正确性，该链接中的参数——图片验证码是需要传过来的
 * @author: sln 
 */

public class InsuranceLinYiTemplateCommonService extends InsuranceService{
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;

	@Value("${loginhost}")
	public String loginhost;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	//先获取图片验证码
	public String getCaptcha(TaskInsurance taskInsurance) throws Exception{
		//通过铁岭的登录url获取图片验证码的解析结果
		String url="http://"+loginhost+"/index.html";    //经测试，临沂和盘锦的登录页面也可以用这个url    
		//http://60.213.43.44/index.html     //临沂
		//http://123.190.194.146:8096/index.html   盘锦
		WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage hPage = webClient.getPage(webRequest);
		String captcha="";
		if(null!=hPage){
			HtmlImage image = hPage.getFirstByXPath("//img[@id='f_svl']");
			captcha = chaoJiYingOcrService.getVerifycode(image, "1902");
			//存储验证码获取成功的cookie
			webClient=hPage.getWebClient();
			String cookies = CommonUnit.transcookieToJson(webClient);
		    taskInsurance.setCookies(cookies);
		    taskInsuranceRepository.save(taskInsurance);
		}
		return captcha;
	}
	
	
	public Integer commonLogin(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance,String captcha, Integer captchaErrorCount) throws Exception{
		try {
			//校验url
			String url="http://"+loginhost+"/loginvalidate.html";    
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody="type=1"
					+ "&account="+insuranceRequestParameters.getUsername().trim()+""
					+ "&password="+insuranceRequestParameters.getPassword().trim()+""    //如下四个参数没有也可以登录成功
					+ "&captcha="+captcha.trim()+"";   //临沂市网站验证码输入错误也能登录成功，但是盘锦市、铁岭市的不可以，故为了统一，将验证码这个参数留着
	//				+ "&yzm=%E8%AF%B7%E8%BE%93%E5%85%A5%E8%81%94%E7%B3%BB%E5%87%BD%E5%8F%8A%E5%87%AD%E8%AF%81%E7%BC%96%E5%8F%B7"
	//				+ "&tab2_type=%E8%AF%B7%E9%80%89%E6%8B%A9%E4%B8%9A%E5%8A%A1%E7%B1%BB%E5%9E%8B"
	//				+ "&input3=%E8%AF%B7%E8%BE%93%E5%85%A5%E9%AA%8C%E8%AF%81%E7%A0%81";
			webRequest.setRequestBody(requestBody);
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());    //用获取验证码的登录页面的cookie
			webClient.getOptions().setJavaScriptEnabled(false);
			
			Page page = webClient.getPage(webRequest);    //用Page接收也可以
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				tracer.addTag("校验登录结果页面html为：", html);   //简短，可以打印日志
				if(html.contains("success")){
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("登录成功！");
					String cookies = CommonUnit.transcookieToJson(webClient);
				    taskInsurance.setCookies(cookies);
				    taskInsuranceRepository.save(taskInsurance);
				}else if(html.contains("success1")){   //盘锦市特有
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
					taskInsurance.setDescription("登录成功，温馨提示：该单位已经欠缴两个月,请及时补缴,以免影响登录！");
					String cookies = CommonUnit.transcookieToJson(webClient);
				    taskInsurance.setCookies(cookies);
				    taskInsuranceRepository.save(taskInsurance);
				}else{    //各种登录失败的情况
					if(html.contains("wrongaccount")){
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
								"登录失败，错误原因：用户名不存在！",taskInsurance);
					}else if(html.contains("wrongpass")){
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
								"登录失败，错误原因：密码和用户名不匹配！",taskInsurance);
					}else if(html.contains("allstop")){
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus(),
								"登录失败，错误原因：该人员已终止！",taskInsurance);
					}else if(html.contains("captchawrong")){
						captchaErrorCount++;
					}else if(html.contains("captchaexpire")){
						captchaErrorCount++;
					}else if(html.contains("isLocked")){
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
								"登录失败，错误原因：该账号已被冻结，请联系管理员！",taskInsurance);
					}else if(html.contains("isLocked1")){   //盘锦市特有
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
								"登录失败，错误原因：该单位已经欠缴三个月,在补全欠费之前不能登录账户！",taskInsurance);
					}else if(html.contains("errors")){
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
								"发生错误：卡中心:根据卡号未查询到卡中心信息！",taskInsurance);
					}else if(html.contains("发生服务器内部错误")){  //盘锦社保点击登录之后很长时间没反应
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
								"官网发生服务器内部错误，请稍后再试！",taskInsurance);
					}else{   //其他登录错误原因(决定提示用户系统繁忙)
						insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
								InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
								"登录失败，错误原因：系统繁忙，请稍后再试！",taskInsurance);
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录时出现了异常，在catch中更新登录状态为系统繁忙", "异常信息是："+e.toString());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					"登录失败，错误原因：系统繁忙，请稍后再试！",taskInsurance);
		}
		return captchaErrorCount;
	}
}
