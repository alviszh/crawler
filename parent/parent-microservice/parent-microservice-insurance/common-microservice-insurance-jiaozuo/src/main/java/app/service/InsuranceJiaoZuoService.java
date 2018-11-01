package app.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoHtml;
import com.microservice.dao.repository.crawler.insurance.jiaozuo.InsuranceJiaoZuoHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.jiaozuo" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.jiaozuo" })
public class InsuranceJiaoZuoService extends InsuranceService implements InsuranceLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceJiaoZuoServiceUnit insuranceJiaoZuoServiceUnit;
	static String IdCard;
	static String Name;
	static String UserMobile;
	Map<String, Future<String>> listfuture = new HashMap<>();
	@Autowired
	private InsuranceJiaoZuoHtmlRepository insuranceJiaoZuoHtmlRepository;
	/**
	 * 登录
	 * @param parameter
	 * @param taskInsurance
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			tracer.addTag("action.jiaozuo.login", parameter.getTaskId());
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			String url = "http://www.hajz12333.gov.cn:8080/web/login.html";

			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("idCardNumber");
			HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");

			HtmlAnchor hand = (HtmlAnchor) page.getByXPath("//a[@class='hand']").get(0);
			username.setText(parameter.getUsername());
			password.setText(parameter.getPassword());
			Page page2 = hand.click();
			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml = new InsuranceJiaoZuoHtml();
			insuranceJiaoZuoHtml.setHtml(page.getWebResponse().getContentAsString());
			insuranceJiaoZuoHtml.setPagenumber(1);
			insuranceJiaoZuoHtml.setTaskid(parameter.getTaskId());
			insuranceJiaoZuoHtml.setType("登录页面");
			insuranceJiaoZuoHtml.setUrl(url);
			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml);
			String alertMsg = WebCrawler.getAlertMsg();
			String html = page2.getWebResponse().getContentAsString();
			System.out.println(html);
			InsuranceJiaoZuoHtml insuranceJiaoZuoHtml2 = new InsuranceJiaoZuoHtml();
			insuranceJiaoZuoHtml2.setHtml(html);
			insuranceJiaoZuoHtml2.setPagenumber(1);
			insuranceJiaoZuoHtml2.setTaskid(parameter.getTaskId());
			insuranceJiaoZuoHtml2.setType("登录后页面");
			insuranceJiaoZuoHtml2.setUrl(url);
			insuranceJiaoZuoHtmlRepository.save(insuranceJiaoZuoHtml2);
			if(html.indexOf("退出")!=-1){
				System.out.println("登录成功");
				Document doc = Jsoup.parse(html);
				Name = doc.getElementById("userName").val();//姓名
				IdCard = doc.getElementById("userIdCardNumber").val();//身份证号码
				UserMobile = doc.getElementById("userMobile").val();//手机号码
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setCookies(cookies);
				taskInsurance.setCity(parameter.getCity());
				taskInsurance.setTesthtml(parameter.getPassword());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);
			}else{
				System.out.println("登录失败");
				if(alertMsg!=""){
					System.out.println(alertMsg);
					tracer.addTag("action.jiyuan.login", "登录失败:"+alertMsg);
					System.out.println("登录失败：" + alertMsg);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
					taskInsurance.setDescription(alertMsg);
					taskInsurance.setError_message(alertMsg);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					Document document = Jsoup.parse(html);
					String error = document.getElementsByTag("p").get(2).text();
					System.out.println(error);
					tracer.addTag("action.jiyuan.login", "登录失败:"+error);
					System.out.println("登录失败：" + error);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
					taskInsurance.setDescription(error);
					taskInsurance.setError_message(error);
					taskInsuranceRepository.save(taskInsurance);
				}
			}

		} catch (Exception e) {
			tracer.addTag("action.jiyuan.login", "登录失败:"+e.getMessage());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
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
		tracer.addTag("action.jiyuan.crawler", taskInsurance.getTaskid());
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
		String encodeName = null;
		try {
			encodeName = URLEncoder.encode(Name, "utf-8");
			
		} catch (UnsupportedEncodingException e1) {
		}
		if(encodeName!=null){
		parameter.setName(encodeName);
		parameter.setUserIDNum(IdCard);
		//基本信息
		Future<String> userInfo = insuranceJiaoZuoServiceUnit.getuserinfo(parameter,taskInsurance,webClient);
		listfuture.put("getUserInfo", userInfo);

		//养老保险个人缴费流水
		Future<String> getyaolaoMsg = insuranceJiaoZuoServiceUnit.getyanglaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyaolaoMsg);

		//医疗保险个人缴费流水
		Future<String> getyiliaoMsg = insuranceJiaoZuoServiceUnit.getyiliaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyiliaoMsg);

		//工伤保险个人缴费流水
		Future<String> getgongshangMsg = insuranceJiaoZuoServiceUnit.getgongshangMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getgongshangMsg);

		//失业保险个人缴费流水
		Future<String> getshiyeMsg = insuranceJiaoZuoServiceUnit.getshiyeMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getshiyeMsg);

		//生育保险个人缴费流水
		Future<String> getshengyuMsg = insuranceJiaoZuoServiceUnit.getshengyuMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getshengyuMsg);

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
			tracer.addTag("inInsurancejiyuanService-listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
		}
		}else{
			    taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
	            taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
	            taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
	            taskInsurance.setFinished(true);
	            taskInsuranceRepository.save(taskInsurance);
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
	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}


	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
