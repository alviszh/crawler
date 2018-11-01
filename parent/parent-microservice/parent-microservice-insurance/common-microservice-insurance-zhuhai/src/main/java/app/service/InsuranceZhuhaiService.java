package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhuhai"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhuhai"})
public class InsuranceZhuhaiService implements InsuranceLogin{
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceZhuhaiUserinfoService insuranceZhuhaiUserinfoService;
	
	private HtmlPage page = null;

	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @param taskInsurance
	 */

	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		
		tracer.addTag("insurancezhuhai.login", "开始登录");
		try {
			page = loginByHtmlUnit(insuranceRequestParameters);
			String html = page.getWebResponse().getContentAsString();
			if("-1".equals(html)){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
						"数据库连接失败！登录超时！",taskInsurance);
			}else if("0".equals(html) || "2".equals(html)){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
						"登录账号或者登录密码输入不正确！",taskInsurance);				
			}else if("3".equals(html)){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
						"登录超时。",taskInsurance);
			}else if("4".equals(html)){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
						"调用接口错误！登录超时！",taskInsurance);
			}else if("9".equals(html)){
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
						"未知错误！",taskInsurance);
			}else{
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus(),
						InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription(),taskInsurance);
				
//				taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
				//获取用户信息
//				insuranceZhuhaiUserinfoService.getUserInfo(page.getWebClient(),taskInsurance);
			}
			
		} catch (Exception e) {
			tracer.addTag("登录报错", e.getMessage());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription(),taskInsurance);
		}
		return taskInsurance;
		
		
	}

	/**
	 * 通过htmlunit登录
	 * @param insuranceRequestParameters
	 * @return
	 * @throws Exception
	 */
	private HtmlPage loginByHtmlUnit(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		String url = "https://www.zhldj.gov.cn/zhrsClient/login.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage loginPage = (HtmlPage) insuranceService.getHtml(url,webClient);		
		//获取图片验证码并保存在指定路径
		HtmlImage image = loginPage.getFirstByXPath("//img[@id='getcode1']");		
		String code = chaoJiYingOcrService.getVerifycode(image, "1005");
		
		String loginUrl = "https://www.zhldj.gov.cn/zhrsClient/login.do?password="+insuranceRequestParameters.getPassword()+
				"&id_card="+insuranceRequestParameters.getName()+"&verifycode="+code+"&user_type=1&rand=0.0805975288931653";
		tracer.addTag("登录所请求的url", loginUrl);
		HtmlPage page = (HtmlPage) insuranceService.getHtml(loginUrl,webClient);
		tracer.addTag("点击登录后返回的页面", "<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
		return page;
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		//获取用户信息
		insuranceZhuhaiUserinfoService.getUserInfo(page.getWebClient(),taskInsurance);
		return null;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
