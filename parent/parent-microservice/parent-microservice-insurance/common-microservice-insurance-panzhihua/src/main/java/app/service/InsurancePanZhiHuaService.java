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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.panzhihua" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.panzhihua" })
public class InsurancePanZhiHuaService extends InsuranceService implements InsuranceLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsurancePanZhiHuaServiceUnit insurancePanZhiHuaServiceUnit;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	Map<String, Future<String>> listfuture = new HashMap<>();
	/**
	 * 登录
	 * @param parameter
	 * @param taskInsurance
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			tracer.addTag("action.PanZhiHua.login", parameter.getTaskId());
			String url = "http://www.scpzh.lss.gov.cn/";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput)  page.getFirstByXPath("//input[@id='SSNumber']");
			HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='SPWD']");
			HtmlTextInput randomnum = (HtmlTextInput) page.getFirstByXPath("//input[@id='Validate']");
			HtmlImage image = (HtmlImage)page.getFirstByXPath("//img[@id='_Validate']");//验证码
			String img = chaoJiYingOcrService.getVerifycode(image, "1902");
			username.setText(parameter.getUserIDNum());
			password.setText(parameter.getPassword());
			taskInsurance.setCity(parameter.getCity());
			randomnum.setText(img);
			HtmlImageInput login_btn = (HtmlImageInput) page.getElementById("button");
			Page page3 = login_btn.click();
			String alertMsg = WebCrawler.getAlertMsg();
			String asString2 = page3.getWebResponse().getContentAsString();
			System.out.println(asString2);
			if(asString2.indexOf("社保信息-攀枝花市人力资源和社会保障局")!=-1){
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
					if(alertMsg.equals("验证码错误，登录失败！")){
						alertMsg = "登录失败！请重新登录";
					}
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
					taskInsurance.setDescription(alertMsg);
					taskInsurance.setError_message(alertMsg);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
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
		Future<String> userInfo = insurancePanZhiHuaServiceUnit.getuserinfo(parameter,taskInsurance,webClient);
		listfuture.put("getUserInfo", userInfo);

		//养老保险个人缴费流水
		Future<String> getyaolaoMsg = insurancePanZhiHuaServiceUnit.getyanglaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyaolaoMsg);

		//医疗保险个人缴费流水
		Future<String> getyiliaoMsg = insurancePanZhiHuaServiceUnit.getyiliaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyiliaoMsg);

		//工伤保险个人缴费流水
		Future<String> getgongshangMsg = insurancePanZhiHuaServiceUnit.getgongshangMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getgongshangMsg);

		//失业保险个人缴费流水
		Future<String> getshiyeMsg = insurancePanZhiHuaServiceUnit.getshiyeMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getshiyeMsg);

		//生育保险个人缴费流水
		Future<String> getshengyuMsg = insurancePanZhiHuaServiceUnit.getshengyuMsg(parameter,taskInsurance,webClient);
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
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhasestatus());
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
