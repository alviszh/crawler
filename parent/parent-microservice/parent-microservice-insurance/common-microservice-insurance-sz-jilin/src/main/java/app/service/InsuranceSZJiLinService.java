package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.sz.jilin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.sz.jilin" })
public class InsuranceSZJiLinService extends InsuranceService implements InsuranceLogin{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZJiLinServiceUnit insuranceSZJiLinServiceUnit;
	Map<String, Future<String>> listfuture = new HashMap<>();
	/**
	 * 	登录
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("action.jilin.login", taskInsurance.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {

			String url = "https://wssb.jlsi.gov.cn:8443/login.jsp";
			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
			HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
			HtmlSubmitInput d = (HtmlSubmitInput) page.getFirstByXPath("//input[@type='submit']");
			
			username.setText(parameter.getUsername());
			password.setText(parameter.getPassword());
			Page page2 = d.click();
			
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			
			if(html.indexOf("菜单导航")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setCookies(cookies);
				taskInsurance.setCity(parameter.getCity());
				taskInsurance.setTesthtml("Username:"+parameter.getUsername()+";password:"+parameter.getPassword());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);
			}else{
				Document doc = Jsoup.parse(html);
				String text = doc.getElementById("loginError").text();
				System.out.println("登录失败:"+text);
				tracer.addTag("action.jilin.login", "登录失败:"+text);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setDescription(text);
				taskInsuranceRepository.save(taskInsurance);
			}

		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("action.jilin.login", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CARD_MISMATCHING_SOCIAL_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_CARD_MISMATCHING_SOCIAL_ERROR.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_CARD_MISMATCHING_SOCIAL_ERROR.getDescription());
			taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}

	/**
	 * 爬取
	 * @param parameter
	 * @param taskInsurance
	 */
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhasestatus());
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getDescription());
		taskInsuranceRepository.save(taskInsurance);
		String cookies = taskInsurance.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		//个人信息
		Future<String> userInfo = insuranceSZJiLinServiceUnit.getUserInfo(webClient,taskInsurance,parameter);
		listfuture.put("getUserInfo", userInfo);
		
		//个人缴费基数
		Future<String> paymentBase = insuranceSZJiLinServiceUnit.getPaymentBase(webClient,taskInsurance,parameter);
		listfuture.put("getpaymentBase", paymentBase);
		
		//个人缴费断档信息
		Future<String> soldout = insuranceSZJiLinServiceUnit.getSoldout(webClient,taskInsurance,parameter);
		listfuture.put("getsoldout", soldout);
		
		//缴费状态变更记录
		Future<String> statusChanges = insuranceSZJiLinServiceUnit.getStatusChanges(webClient,taskInsurance,parameter);
		listfuture.put("getstatusChanges", statusChanges);
		
		//养老保险缴纳明细
		Future<String> yanglaoInfo = insuranceSZJiLinServiceUnit.getyanglaoInfo(webClient,taskInsurance,parameter);
		listfuture.put("getyanglaoInfo", yanglaoInfo);
		
		//个人失业缴费明细
		Future<String> shiyeInfo = insuranceSZJiLinServiceUnit.getshiyeInfo(webClient,taskInsurance,parameter);
		listfuture.put("getshiyeInfo", shiyeInfo);
		
		//其他三险因吉林省直无
		Future<String> insurance = insuranceSZJiLinServiceUnit.getinsurance(taskInsurance,parameter);
		listfuture.put("getinsurance", insurance);
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
			taskInsurance = changeCrawlerStatusSuccess(parameter.getTaskId());
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("inTelecomjiangsuService-listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e.getMessage());
		}
		
		
		return taskInsurance;
		
	}


	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}


	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}





	
}
