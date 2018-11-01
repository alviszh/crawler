package app.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.swing.JOptionPane;

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
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.guigang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.guigang" })
public class InsuranceGuiGangService extends InsuranceService implements InsuranceLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	protected TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceGuiGangServiceUnit insuranceGuiGangServiceUnit;
	Map<String, Future<String>> listfuture = new HashMap<>();

	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			tracer.addTag("action.guigang.login", parameter.getTaskId());
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://www.gxggsi.gov.cn/login.jsp";
			HtmlPage pag = (HtmlPage) getHtml(url, webClient);
			HtmlImage imag = (HtmlImage) pag.getFirstByXPath("//img[@id='codeimg']");
			HtmlPage page = (HtmlPage) imag.click();
			Thread.sleep(1000);
			HtmlTextInput c_username = (HtmlTextInput) page.getElementById("c_username");//身份证
			HtmlPasswordInput c_password = (HtmlPasswordInput) page.getElementById("c_password");//密码
			HtmlTextInput checkCode = (HtmlTextInput) page.getElementById("checkCode");//验证码
			HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='codeimg']");//验证码图片
			HtmlDivision log  = page.getFirstByXPath("//div[@class='login2_loginBtn']");//登录
			String ima = chaoJiYingOcrService.getVerifycode(image, "1902");
			c_username.setText(parameter.getUsername());//450702198112235113
			c_password.setText(parameter.getPassword());//Ffupeng1981
			checkCode.setText(ima);
			log.click();

			String url3 = "http://www.gxggsi.gov.cn:7005/indexAction.do?indexstyle=default";//登录成功首页
			Page page3 = getHtml(url3, webClient);

			String html3 = page3.getWebResponse().getContentAsString();
			System.out.println(html3);
			if(html3.indexOf("贵港市社会保险网上大厅")!=-1){
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setCookies(cookies);
				taskInsurance.setCity(parameter.getCity());
				taskInsurance.setTesthtml("Username:"+parameter.getUsername()+";password:"+parameter.getPassword());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);
			}else{
				String url2 = "http://www.gxggsi.gov.cn:7005/privateLoginAction!login.do?userid=278475";//登录报错json
				Page page4 = getHtml(url2, webClient);
				String err = page4.getWebResponse().getContentAsString();
				JSONObject obj = JSONObject.fromObject(err);
				String error = obj.getString("msg");
				System.out.println(error);
				tracer.addTag("action.jiyuan.login", "登录失败:"+error);
				System.out.println("登录失败：" + error);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_TIMEOUT.getPhasestatus());
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
		Future<String> userInfo = insuranceGuiGangServiceUnit.getuserinfo(parameter,taskInsurance,webClient);
		listfuture.put("getUserInfo", userInfo);

		//养老
		for (int j = 0; j < 5; j++) {
			LocalDate today = LocalDate.now();
			int year = today.getYear();
			year = year - j;
			Future<String> getyaolaoMsg = insuranceGuiGangServiceUnit.getyanglaoMsg(parameter,taskInsurance,webClient,year);
			listfuture.put(j+"getyaolaoMsg", getyaolaoMsg);
		}
		//医疗
		for (int j = 0; j < 5; j++) {
			LocalDate today = LocalDate.now();
			int year = today.getYear();
			year = year - j;
			Future<String> getyiliaoMsg = insuranceGuiGangServiceUnit.getyiliaoMsg(parameter,taskInsurance,webClient,year);
			listfuture.put(j+"getyiliaoMsg", getyiliaoMsg);
		}
		//工伤
		for (int j = 0; j < 5; j++) {
			LocalDate today = LocalDate.now();
			int year = today.getYear();
			year = year - j;
			Future<String> getgongshangMsg = insuranceGuiGangServiceUnit.getgongshangMsg(parameter,taskInsurance,webClient,year);
			listfuture.put(j+"getgongshangMsg", getgongshangMsg);
		}

		//失业
		for (int j = 0; j < 5; j++) {
			LocalDate today = LocalDate.now();
			int year = today.getYear();
			year = year - j;
			Future<String> getshiyeMsg = insuranceGuiGangServiceUnit.getshiyeMsg(parameter,taskInsurance,webClient,year);
			listfuture.put(j+"getshiyeMsg", getshiyeMsg);
		}

		//生育
		for (int j = 0; j < 5; j++) {
			LocalDate today = LocalDate.now();
			int year = today.getYear();
			year = year - j;
			Future<String> getshengyuMsg = insuranceGuiGangServiceUnit.getshengyuMsg(parameter,taskInsurance,webClient,year);
			listfuture.put(j+"getshengyuMsg", getshengyuMsg);
		}
		
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
		webRequest.setCharset(Charset.forName("UTF-8"));
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
