package app.service;


import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jilin"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jilin"})
public class HousingFundJiLinService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Value("${loginhost}") 
	public String loginHost;
	public Integer login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, Integer imageErrorCount){
		try {
			String url="https://"+loginHost+"/jlwsyyt";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				HtmlImage image = hPage.getFirstByXPath("//img[@src='vericode.jsp']");
				String code = chaoJiYingOcrService.getVerifycode(image, "2004");
				HtmlTextInput username = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='certinum']"); 
				HtmlPasswordInput password = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='perpwd']");
				HtmlTextInput verifyCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='vericode']");
				HtmlButton button = (HtmlButton)hPage.getFirstByXPath("//button[@type='submit']");			
				username.setText(messageLoginForHousing.getNum());
				password.setText(messageLoginForHousing.getPassword());
				verifyCode.setText(code);
				HtmlPage logonPage = button.click();
				Thread.sleep(2000);  
				if(null!=logonPage){
					String logonHtml=logonPage.asXml();   
					tracer.addTagWrap("模拟点击登录后返回的页面是为：", logonHtml);
					if(logonHtml.contains("WTLoginError")){ 
						Document doc=Jsoup.parse(logonHtml);
						String errorMsg = doc.getElementsByClass("WTLoginError").get(0).text();
						if(errorMsg.contains("身份证转换错误")){
							 tracer.addTag("身份证号输入有误！", taskHousing.getTaskid());
							 System.out.println("身份证号输入有误！");
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							 taskHousing.setDescription("登录失败，身份证号输入有误！");
							 save(taskHousing);
						}else if(errorMsg.contains("用户密码不匹配")){
							 tracer.addTag(errorMsg, taskHousing.getTaskid());
							 System.out.println(errorMsg);
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription(errorMsg);
							 save(taskHousing);
						}else if(errorMsg.contains("系统日终尚未结束")){
							 tracer.addTag(errorMsg, taskHousing.getTaskid());
							 System.out.println(errorMsg);
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription(errorMsg);
							 save(taskHousing);
						}else if(errorMsg.contains("您输入的验证码与图片不符")){
							imageErrorCount++;
							if(imageErrorCount>3){
								 tracer.addTag("操作失败:进行身份校验时出错:您输入的验证码与图片不符"+imageErrorCount, taskHousing.getTaskid());
								 System.out.println("操作失败:进行身份校验时出错:您输入的验证码与图片不符");
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
								 taskHousing.setDescription("登录失败，图片验证码识别错误！");
								 save(taskHousing);
							}else{
								login(messageLoginForHousing,taskHousing,imageErrorCount);
							}
						}else{
							tracer.addTag("登录出现了其他错误，此处日志记录："+taskHousing.getTaskid(), errorMsg);
							System.out.println("登录出现了其他错误，此处日志记录："+errorMsg);
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhasestatus());
							taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
							save(taskHousing);
						}
					}else{
						//登录成功
						changeLoginStatusSuccess(taskHousing, webClient);
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录失败，程序出现异常："+taskHousing.getTaskid(), e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
			save(taskHousing);
		}
		return imageErrorCount;
	}
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}
}
