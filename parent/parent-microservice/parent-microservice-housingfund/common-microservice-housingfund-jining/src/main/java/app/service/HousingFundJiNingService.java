package app.service;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jining"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jining"})
public class HousingFundJiNingService extends HousingBasicService  implements ICrawlerLogin{
	@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String fileSavePath;
	@Autowired
	private HousingFundJiNingCrawlerService housingFundJiNingCrawlerService;
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing){
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		String loginUrl="http://"+loginHost+"/jnwsyyt/";
		String vericodeUrl="http://"+loginHost+"/jnwsyyt/vericode.jsp";
		try {
			WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				//此处请求图片验证码链接
				webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				//利用io流保存图片验证码
				String imgagePath=getImagePath(page,fileSavePath);
				String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1902");   
				HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='certinum']"); 
				HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='perpwd']"); 
				loginPassword.setText(messageLoginForHousing.getPassword().trim()); 	
				HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='vericode']"); 
				HtmlElement submitbt = (HtmlElement)hPage.getFirstByXPath("//button[@type='submit']"); 
				loginName.setText(messageLoginForHousing.getNum().trim()); 
				validateCode.setText(code); 	
				HtmlPage logonPage= submitbt.click(); 
				if(null!=logonPage){
					String html=logonPage.asXml();
					if(html.contains("欢迎您")){
						changeLoginStatusSuccess(taskHousing, webClient);
				    	Thread.sleep(1000);
					}else{
						Document doc = Jsoup.parse(html);
						String errorMsg= doc.getElementsByClass("text").get(0).text();  //获取页面中可能出现的错误信息
						tracer.addTag("登录失败时，跳转页面后，页面上的提示信息是：",errorMsg);
						//通过调研，发现图片验证码输入错误，这几个公积金网站的提示都是一样的
						if(errorMsg.contains("您输入的验证码与图片不符")){
							captchaErrorCount++;
							 tracer.addTag("action.login.auth.imageErrorCount", "这是第"+captchaErrorCount+"次因图片验证码识别错误重新调用登录方法");
							 //图片验证码识别错误，重试三次登录
							 if(captchaErrorCount>3){
								 tracer.addTag("操作失败:进行身份校验时出错:您输入的验证码与图片不符"+captchaErrorCount, taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
								 taskHousing.setDescription("进行身份校验时出错:您输入的验证码与图片不符");
								 save(taskHousing);
							 }else{
								 login(messageLoginForHousing);
							 }
						//身份证号输入有误的各网站提示
						}else if(errorMsg.contains("账户信息不存在")){
							 tracer.addTag("操作失败:进行身份校验时出错:账号输入有误！", taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							 taskHousing.setDescription(errorMsg);
							 save(taskHousing);
						//密码输入有误的各网站提示
						}else if(errorMsg.contains("登录密码输入错误")){
							 tracer.addTag("进行身份校验时出错:您输入的密码有误，请重新输入！", taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription(errorMsg);
							 save(taskHousing);
						//其他错误
						}else{
							tracer.addTag(taskHousing.getTaskid()+"登录失败，出现了调研时没有遇到的错误：",errorMsg);
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription(errorMsg);
							save(taskHousing);
						} 
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.login.taskid===>e",e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，公积金网站系统繁忙，请稍后再试！");
			save(taskHousing);
		}
		return taskHousing;
	}
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		housingFundJiNingCrawlerService.getUserInfo(taskHousing);
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
