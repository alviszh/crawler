package app.service;

import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sanming"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sanming"})
public class InsuranceSanmingService implements InsuranceLogin{
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceSanmingUserinfoService insuranceSanmingUserinfoService;
	
	private HtmlPage page = null;

	/**
	 * 登录
	 * @param insuranceRequestParameters
	 * @param taskInsurance
	 */
	
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("insurancesanming.login", "开始登录");
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
		try {
			page = loginByHtmlUnit(insuranceRequestParameters);
			//连续4次加载图片验证码未加载出来
			if(null == page){
				tracer.addTag("login.error", "连续4次加载图片验证码未加载出来");
				insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
						InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
						"数据库连接失败！登录超时！",taskInsurance);
			}else{
				String html = page.getWebResponse().getContentAsString();
				if(html.contains("验证码输入错误")){
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
							"网络波动。登录超时！",taskInsurance);
				}else if(html.contains("密码错误")){
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
							"密码错误，请核对后重新登录！",taskInsurance);
				}else if(html.contains("社会保障卡")){
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
							"账户名不存在！",taskInsurance);
				}else{
					insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase(),
							InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus(),
							InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription(),taskInsurance);
					
//					taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
//					//获取用户信息
//					insuranceSanmingUserinfoService.getUserinfo(page.getWebClient(),taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录报错", e.getMessage());
			insuranceService.changeLoginStatus(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase(),
					InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus(),
					"登录超时！",taskInsurance);
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
		String loginUrl = "http://www.smsic.cn:8080/sheB/index.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//原网站每次刷新，图片验证码不一定会加载，所以循环4次。
		for(int i = 0;i<10;i++){
			HtmlPage html = getHtml(loginUrl,webClient);
			System.out.println(html.getWebResponse().getContentAsString());
			if(html.getWebResponse().getContentAsString().contains("Apache Tomcat")){
				continue;
			}
			Thread.sleep(10000);
			//获取登录名输入框
			HtmlTextInput idnum = html.getFirstByXPath("//input[@id='card']");
			System.out.println("用户名输入框："+idnum.asXml());
			//获取密码输入框
			HtmlPasswordInput password = html.getFirstByXPath("//input[@id='pwd']");
			System.out.println("密码输入框： "+password.asXml());
			//获取图片验证码输入框
			HtmlTextInput checkCode = html.getFirstByXPath("//input[@id='code']");
			System.out.println("验证码输入框： "+checkCode.asXml());
			//获取登录按钮
			HtmlImageInput button = html.getFirstByXPath("//input[@type='image']");
			System.out.println("登录按钮 ： "+button.asXml());
			//获取图片验证码并保存在指定路径
			HtmlImage image = html.getFirstByXPath("//img[@src='/sheB/jsp/code/image.jsp']");
			String code = chaoJiYingOcrService.getVerifycode(image, "1005");
			
			System.out.println("图片验证码："+code);
			
			if(StringUtils.isBlank(code)){
				continue;
			}else{
				//输入值
				idnum.setText(insuranceRequestParameters.getName());
				password.setText(insuranceRequestParameters.getPassword());
				checkCode.setText(code);
				
				HtmlPage page = (HtmlPage) button.click();
				tracer.addTag("点击登录后返回的页面：","<xmp>"+page.getWebResponse().getContentAsString()+"</xmp>");
				return page;
			}			
		}
		return null;
	}

	private HtmlPage getHtml(String loginUrl, WebClient webClient) throws Exception {
		WebRequest requestSettings = new WebRequest(new URL(loginUrl), HttpMethod.GET); 
		
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Host", "www.smsic.cn:8080"); 
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		HtmlPage page = webClient.getPage(requestSettings);
		
		return page;
	}

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		
		tracer.addTag("insurance.sanming.getalldata", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = insuranceService.findTaskInsurance(insuranceRequestParameters.getTaskId());
//		//获取用户信息
		insuranceSanmingUserinfoService.getUserinfo(page.getWebClient(),taskInsurance);
		return null;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
