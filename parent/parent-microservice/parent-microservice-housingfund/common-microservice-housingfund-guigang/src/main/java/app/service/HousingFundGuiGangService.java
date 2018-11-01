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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.guigang.HousingGuiGangHtml;
import com.microservice.dao.repository.crawler.housing.guigang.HousingGuiGangHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import app.service.common.aop.ICrawlerLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.guigang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.guigang"})
public class HousingFundGuiGangService extends HousingBasicService  implements ICrawlerLogin{
	@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String filesavepath;
	@Autowired
	private HousingFundGuiGangCrawlerService housingFundGuiGangCrawlerService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingGuiGangHtmlRepository housingGuiGangHtmlRepository;
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		housingFundGuiGangCrawlerService.getUserInfo(taskHousing);
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			String url="http://"+loginHost+"/ggwt/";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				//获取图片验证码
				url="http://"+loginHost+"/ggwt/vericode.jsp";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				if(page!=null){
					String imagePath = HousingFundHelperService.getImagePath(page,filesavepath);
					String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1902");
					//验证登录信息
					url="http://"+loginHost+"/ggwt/per.login";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					String requestBody=""
							+ "certinum="+messageLoginForHousing.getNum().trim()+""
							+ "&pwd="+messageLoginForHousing.getPassword().trim()+""
							+ "&vericode="+code+"";
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);
					if(null!=page){
						String html=page.getWebResponse().getContentAsString();
						HousingGuiGangHtml loginHtml = new HousingGuiGangHtml();
						loginHtml.setPagenumber(1);
						loginHtml.setType("登陆信息验证结果源码页");
						loginHtml.setTaskid(messageLoginForHousing.getTask_id());
						loginHtml.setUrl(url);
						loginHtml.setHtml(html);
						housingGuiGangHtmlRepository.save(loginHtml);			
				    	tracer.addTag("登陆信息验证结果源码页：","已经入库");
						if(html.contains("欢迎您")){
							changeLoginStatusSuccess(taskHousing, webClient);
					    	Thread.sleep(1000);
						}else{
							Document doc = Jsoup.parse(html);
							String errorMsg= doc.getElementsByClass("WTLoginError").get(0).text();  //获取页面中可能出现的错误信息
							String[] split = errorMsg.split(" ");
							errorMsg=split[0];
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
								
							}else if(errorMsg.contains("密码错误请重试")){
								 tracer.addTag("进行身份校验时出错:您输入的密码有误，请重新输入！", taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
								 taskHousing.setDescription(errorMsg);
								 save(taskHousing);
							}else if(errorMsg.contains("满足条件的注册用户数据")){
								 tracer.addTag("操作失败:满足条件的注册用户数据不存在！", taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
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
			}
		} catch (Exception e) {
			tracer.addTag("action.login.taskid===>e",taskHousing.getTaskid()+"  "+e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，公积金网站系统繁忙，请稍后再试！");
			save(taskHousing);
		}
		return taskHousing;
	}
}
