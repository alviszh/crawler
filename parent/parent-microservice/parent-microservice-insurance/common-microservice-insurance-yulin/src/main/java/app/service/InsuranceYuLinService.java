package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.yulin.InsuranceYuLinHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.yulin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.yulin" })
public class InsuranceYuLinService extends InsuranceService implements InsuranceLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceYuLinServiceUnit insuranceYuLinServiceUnit;
	Map<String, Future<String>> listfuture = new HashMap<>();
	@Autowired
	private InsuranceYuLinHtmlRepository insuranceYuLinHtmlRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	/**
	 * 登录
	 * @param parameter
	 * @param taskInsurance
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			tracer.addTag("action.YuLin.login", parameter.getTaskId());
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			String url = "http://222.83.253.69/ylsbwz/login.jsp";

			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput sfzh = (HtmlTextInput) page.getElementById("sfzh");//帐号
			HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");//密码
			HtmlTextInput yzm = (HtmlTextInput) page.getElementById("yzm");//验证码输入框
			HtmlImage image = (HtmlImage) page.getElementById("Verify");//图片验证码
			HtmlAnchor btnLogin = (HtmlAnchor) page.getElementById("btnLogin");//登录
			String img = chaoJiYingOcrService.getVerifycode(image, "1902");//图片

			sfzh.setText(parameter.getUsername());
			password.setText(parameter.getPassword());
			yzm.setText(img);
			btnLogin.click();
			Thread.sleep(2000);
			String url2 = "http://222.83.253.69/ylsbwz/login?"
					+ "sfzh="+parameter.getUsername()
					+ "&password="+parameter.getPassword()
					+ "&yzm="+img;
			Page page3 = getHtml(url2, webClient);
			String html = page3.getWebResponse().getContentAsString();
			System.out.println(html);
			String alertMsg = WebCrawler.getAlertMsg();
			if(html.indexOf("个人查询")!=-1){
				System.out.println("登录成功");
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
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getDescription());
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

		//基本信息
		Future<String> userInfo = insuranceYuLinServiceUnit.getuserinfo(parameter,taskInsurance,webClient);
		listfuture.put("getUserInfo", userInfo);

		//养老保险个人缴费流水
		Future<String> getyaolaoMsg = insuranceYuLinServiceUnit.getyanglaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyaolaoMsg);

		//医疗保险个人缴费流水
		Future<String> getyiliaoMsg = insuranceYuLinServiceUnit.getyiliaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyiliaoMsg);

		//工伤保险个人缴费流水
		Future<String> getgongshangMsg = insuranceYuLinServiceUnit.getgongshangMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getgongshangMsg);

		//失业保险个人缴费流水
		Future<String> getshiyeMsg = insuranceYuLinServiceUnit.getshiyeMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getshiyeMsg);

		//生育保险个人缴费流水
		Future<String> getshengyuMsg = insuranceYuLinServiceUnit.getshengyuMsg(parameter,taskInsurance,webClient);
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
		return taskInsurance;
	}

	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		//		webClient.setJavaScriptTimeout(50000); 
		//		webClient.getOptions().setTimeout(50000); // 15->60 
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
