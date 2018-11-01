package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.sz.zhejiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.sz.zhejiang" })
public class InsuranceSZZheJiangService extends InsuranceService implements InsuranceLogin{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private InsuranceSZZheJiangGongShangService insuranceSZZheJiangGongShangService;
	@Autowired
	private InsuranceSZZheJiangUserInfoService insuranceSZZheJiangUserInfoService;
	@Autowired
	private InsuranceSZZheJiangYangLaoService insuranceSZZheJiangYangLaoService;
	@Autowired
	private InsuranceSZZheJiangYiLiaoService insuranceSZZheJiangYiLiaoService;
	@Autowired
	private InsuranceSZZheJiangShiYeService insuranceSZZheJiangShiYeService;
	@Autowired
	private InsuranceSZZheJiangShengYuService insuranceSZZheJiangShengYuService;
	Map<String, Future<String>> listfuture = new HashMap<>();

	/**
	 * 登录
	 * @param parameter
	 * @param taskInsurance
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("action.zhejiang.login", taskInsurance.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		try {
			String url = "https://puser.zjzwfw.gov.cn/sso/usp.do?action=logout&servicecode=njdh";
			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			/**
			 * 版本1（无验证码）
			 */
			/*HtmlTextInput loginname = (HtmlTextInput) page.getElementById("loginname");//身份证
			HtmlPasswordInput loginpwd = (HtmlPasswordInput) page.getElementById("loginpwd");//密码
			HtmlButtonInput log  = page.getFirstByXPath("//input[@id='submit']");//登录
			loginname.setText(parameter.getUserIDNum());//18857002671
			loginpwd.setText(parameter.getPassword());//xuwang888888
			HtmlPage page3 = log.click();
			webClient.waitForBackgroundJavaScript(10000);
			String html3 = page3.getWebResponse().getContentAsString();
			System.out.println(html3);*/
			
			/**
			 * 版本2（有验证码）
			 */
			//新增验证码 （不久之后网站可能会去掉）
			//  - - - - - - - - >验证码< - - - - - - - - - -
			//
			HtmlTextInput loginname = (HtmlTextInput) page.getElementById("loginname");//身份证
			HtmlPasswordInput loginpwd = (HtmlPasswordInput) page.getElementById("loginpwd");//密码
			HtmlButtonInput log  = page.getFirstByXPath("//input[@id='submit']");//登录
			loginname.setText(parameter.getUserIDNum());//18857002671
			loginpwd.setText(parameter.getPassword());//xuwang888888
			HtmlPage page3 = log.click();
			String string = page3.getWebResponse().getContentAsString();
			System.out.println(string);
			webClient.waitForBackgroundJavaScript(10000);
//			HtmlTextInput loginname2 = (HtmlTextInput) page2.getElementById("loginname");//身份证
//			HtmlPasswordInput loginpwd2= (HtmlPasswordInput) page2.getElementById("loginpwd");
//			HtmlButtonInput log2  = page2.getFirstByXPath("//input[@id='submit']");//登录
//			HtmlTextInput verifycode = (HtmlTextInput) page2.getElementById("verifycode");//验证码
//			HtmlImage image = (HtmlImage) page2.getElementById("captcha_img");//图片
//			String img = chaoJiYingOcrService.getVerifycode(image, "1902");
//			verifycode.setText(img);
//			loginname2.setText(parameter.getUserIDNum());
//			loginpwd2.setText(parameter.getPassword());
//			HtmlPage page3 = log2.click();
//			webClient.waitForBackgroundJavaScript(10000);
//			String html3 = page3.getWebResponse().getContentAsString();
//			System.out.println(html3);
			
			//  - - - - - - - - >结  束< - - - - - - - - - -
			
			
			if(string.indexOf("浙江政务服务网（省级）")!=-1){
				System.out.println("成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance.setCookies(cookies);
				taskInsurance.setCity(parameter.getCity());
				taskInsurance.setTesthtml("Username:"+parameter.getUserIDNum()+";password:"+parameter.getPassword());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsuranceRepository.save(taskInsurance);
			}else{
				String error = "用户名、密码是否正确？请重试";
				if(page3.asText().contains("用户名、密码是否正确？请重试")){
					error = "用户名、密码是否正确？请重试";
				}else if(page3.asText().contains("密码错误,请重新输入")){
					error = "密码错误,请重新输入";
				}else if(page3.asText().contains("验证码不正确")){
					error = "网络有问题，请重试！";
				}
				System.out.println("失败"+error);
				tracer.addTag("action.baishan.login", "登录失败:"+error);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setDescription(error);
				taskInsuranceRepository.save(taskInsurance);
			}

		} catch (Exception e) {
			tracer.addTag("action.baishan.login", "登录失败:网页有改动");
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getDescription());
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
		insuranceService.changeCrawlerStatusDoing(parameter);
		String cookies = taskInsurance.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		webClient = insuranceService.getWebClient(set);

		//个人信息
		Future<String> userInfo = insuranceSZZheJiangUserInfoService.getUserInfo(webClient,taskInsurance,parameter);
		listfuture.put("getUserInfo", userInfo);

		//养老保险缴纳明细
		Future<String> yanglaoInfo = insuranceSZZheJiangYangLaoService.getyanglaoInfo(webClient,taskInsurance,parameter);
		listfuture.put("getyanglaoInfo", yanglaoInfo);

		//医疗保险个人缴费流水
		Future<String> getyiliaoMsg = insuranceSZZheJiangYiLiaoService.getyiliaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyiliaoMsg);

		//工伤保险个人缴费流水
		Future<String> getgongshangMsg = insuranceSZZheJiangGongShangService.getgongshangMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getgongshangMsg);

		//失业保险个人缴费流水
		Future<String> getshiyeMsg = insuranceSZZheJiangShiYeService.getshiyeMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getshiyeMsg);

		//生育保险个人缴费流水
		Future<String> getshengyuMsg = insuranceSZZheJiangShengYuService.getshengyuMsg(parameter,taskInsurance,webClient);
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
			tracer.addTag("inTelecomjiangsuService-listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
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
