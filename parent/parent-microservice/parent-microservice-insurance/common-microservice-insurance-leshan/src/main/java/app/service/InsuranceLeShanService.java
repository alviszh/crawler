package app.service;

import java.util.HashMap;
import java.util.Iterator;
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
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.leshan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.leshan" })
public class InsuranceLeShanService extends InsuranceService implements InsuranceLogin{

	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private insuranceLeShanServiceUnit insuranceLeShanServiceUnit;
	Map<String, Future<String>> listfuture = new HashMap<>();
	/**
	 * 登录
	 * @param parameter
	 * @param taskInsurance
	 */
	@Override
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("action.leshan.login", parameter.getTaskId());
		try {
			String url = "http://www.scls.lss.gov.cn:8888/lswtqt/toLogin.jhtml";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			HtmlPage page = (HtmlPage) getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
			HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
			HtmlTextInput randomnum = (HtmlTextInput) page.getElementById("captcha_gr");
			HtmlImage image = (HtmlImage) page.getElementById("codeimg");//验证码

			String verifycode = chaoJiYingOcrService.getVerifycode(image, "1004");
			username.setText(parameter.getUserIDNum());
			password.setText(parameter.getPassword());
			randomnum.setText(verifycode);

			HtmlAnchor login_btn = page.getFirstByXPath("//a[@onclick='login();']");
			Thread.sleep(4000);
			HtmlPage click = login_btn.click();
			Thread.sleep(4000);
			
			String url22 = "http://www.scls.lss.gov.cn:8888/lswtqt/toUsercenter.jhtml";
			Page page2 = getHtml(url22, webClient);
			//Page page2 = login_btn.click();
		
			String asString = page2.getWebResponse().getContentAsString();
			System.out.println(asString);
			if(asString.indexOf("个人用户中心")!=-1){
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
				String error = "";
				if(click.asText().contains("用户名不能为空！")){
					System.out.println("用户名不能为空！");
					error = "用户名不能为空！";
				}else if(click.asText().contains("此处为个人用户登录，请输入正确的身份证号或手机号")){
					System.out.println("此处为个人用户登录，请输入正确的身份证号或手机号");
					error = "此处为个人用户登录，请输入正确的身份证号或手机号";
				}else if(click.asText().contains("未检测到"+parameter.getUserIDNum()+"的信息,请检查账号是否填写正确")){
					System.out.println("未检测到"+parameter.getUserIDNum()+"的信息,请检查账号是否填写正确");
					error = "未检测到"+parameter.getUserIDNum()+"的信息,请检查账号是否填写正确";
				}else if(click.asText().contains("请先输入密码！")){
					System.out.println("请先输入密码！");
					error = "请先输入密码！";
				}else if(click.asText().contains("请输入图形验证码!")){
					System.out.println("请输入图形验证码!");
					error = "请输入图形验证码!";
				}else if(click.asText().contains("验证码错误！")){
					System.out.println("验证码错误！");
					error = "网络繁忙，请稍后重试！";
					taskInsurance.setError_message("验证码错误！");
				}else if(click.asText().contains("用户名或者密码不正确，如忘记密码，可使用“忘记密码”功能找回密码。连续错误20次之后将锁定此账户。")){
					System.out.println("用户名或者密码不正确，如忘记密码，可使用“忘记密码”功能找回密码。连续错误20次之后将锁定此账户。");
					error = "用户名或者密码不正确，连续错误20次之后将锁定此账户。";
				}
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance.setTesthtml(parameter.getPassword());
				taskInsurance.setDescription(error);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			tracer.addTag("action.leshan.loginerr", parameter.getTaskId());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance.setTesthtml(parameter.getPassword());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
			taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		tracer.addTag("action.leshan.crawler", taskInsurance.getTaskid());
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
		Future<String> userInfo = insuranceLeShanServiceUnit.getuserinfo(parameter,taskInsurance,webClient);
		listfuture.put("getUserInfo", userInfo);

		//养老保险个人缴费流水
		Future<String> getyaolaoMsg = insuranceLeShanServiceUnit.getyanglaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyaolaoMsg);

		//医疗保险个人缴费流水
		Future<String> getyiliaoMsg = insuranceLeShanServiceUnit.getyiliaoMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getyiliaoMsg);

		//工伤保险个人缴费流水
		Future<String> getgongshangMsg = insuranceLeShanServiceUnit.getgongshangMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getgongshangMsg);

		//失业保险个人缴费流水
		Future<String> getshiyeMsg = insuranceLeShanServiceUnit.getshiyeMsg(parameter,taskInsurance,webClient);
		listfuture.put("getyaolaoMsg", getshiyeMsg);

		//生育保险个人缴费流水
		Future<String> getshengyuMsg = insuranceLeShanServiceUnit.getshengyuMsg(parameter,taskInsurance,webClient);
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
			tracer.addTag("inInsuranceleshanService-listfuture--ERROR", taskInsurance.getTaskid() + "---ERROR:" + e);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_MAINTAIN_ERROR.getPhasestatus());
			taskInsurance.setFinished(true);
			taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}


	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
