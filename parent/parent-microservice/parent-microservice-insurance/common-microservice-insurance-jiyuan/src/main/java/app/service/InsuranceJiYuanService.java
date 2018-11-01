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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jiyuan.InsuranceJiYuanHtml;
import com.microservice.dao.repository.crawler.insurance.jiyuan.InsuranceJiYuanHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.jiyuan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.jiyuan" })
public class InsuranceJiYuanService extends InsuranceService implements InsuranceLogin{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceJiYuanServiceUnit insuranceJiYuanServiceUnit;
	@Autowired
	private InsuranceJiYuanHtmlRepository insuranceJiYuanHtmlRepository;
	Map<String, Future<String>> listfuture = new HashMap<>();
	/***
	 * 登录
	 * @param parameter
	 * @param taskInsurance
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			tracer.addTag("action.jiyuan.login", parameter.getTaskId());
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://221.13.152.238:9000/login.html?"
					+ "redirect=http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbx_jbxxcx.shtml";
			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
			HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
			HtmlButtonInput btn_login_0 = (HtmlButtonInput) page.getElementById("btn_login_0");

			username.setText(parameter.getUsername());
			password.setText(parameter.getPassword());

			btn_login_0.click();
			InsuranceJiYuanHtml insuranceJiYuanHtml = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml.setHtml(page.getWebResponse().getContentAsString());
			insuranceJiYuanHtml.setPagenumber(1);
			insuranceJiYuanHtml.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml.setType("登录页面");
			insuranceJiYuanHtml.setUrl(url);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml);

			String url8 = "http://221.13.152.238:9000/loginAction.action?"
					+ "from="
					+ "&redirect=http://rspt.jiyuan.gov.cn:9000/grpt/zgbx/zgbx_jbxxcx.shtml"
					+ "&username="+parameter.getUsername()
					+ "&password="+parameter.getPassword()
					+ "&phoneNumber="
					+ "&smsVerificationCode="
					+ "&loginMode=0";
			Page page3 = gethtmlPost(webClient, null, url8);
			String string12 = page3.getWebResponse().getContentAsString();
			String err = JSONObject.fromObject(string12).getString("message");
			//网页信息
			InsuranceJiYuanHtml insuranceJiYuanHtml2 = new InsuranceJiYuanHtml();
			insuranceJiYuanHtml2.setHtml(string12);
			insuranceJiYuanHtml2.setPagenumber(1);
			insuranceJiYuanHtml2.setTaskid(parameter.getTaskId());
			insuranceJiYuanHtml2.setType("登录结果");
			insuranceJiYuanHtml2.setUrl(url8);
			insuranceJiYuanHtmlRepository.save(insuranceJiYuanHtml2);
			if(err.equals("登录成功")){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setCookies(cookies);
				taskInsurance.setTesthtml("Username:"+parameter.getUsername()+";password:"+parameter.getPassword());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);
			}else{
				tracer.addTag("action.jiyuan.login", "登录失败:"+err);
				System.out.println("登录失败：" + err);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setDescription(err);
				taskInsuranceRepository.save(taskInsurance);
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
		Future<String> userInfo = insuranceJiYuanServiceUnit.getuserinfo(parameter,taskInsurance,webClient);
		listfuture.put("getUserInfo", userInfo);
		
		//养老保险个人缴费流水
		Future<String> getyaolaoMsg = insuranceJiYuanServiceUnit.getyanglaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyaolaoMsg);
		
		//医疗保险个人缴费流水
		Future<String> getyiliaoMsg = insuranceJiYuanServiceUnit.getyiliaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyiliaoMsg);
		
		//工伤保险个人缴费流水
		Future<String> getgongshangMsg = insuranceJiYuanServiceUnit.getgongshangMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getgongshangMsg);
		
		//失业保险个人缴费流水
		Future<String> getshiyeMsg = insuranceJiYuanServiceUnit.getshiyeMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getshiyeMsg);
		
		//生育保险个人缴费流水
		Future<String> getshengyuMsg = insuranceJiYuanServiceUnit.getshengyuMsg(parameter,taskInsurance,webClient);
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
