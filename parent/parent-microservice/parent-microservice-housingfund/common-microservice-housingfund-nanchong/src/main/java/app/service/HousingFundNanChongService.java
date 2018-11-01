package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import app.service.common.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.nanchong"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.nanchong"})
public class HousingFundNanChongService extends HousingBasicService  implements ICrawlerLogin{
	@Autowired
	private HousingFundNanChongCrawlerService housingFundNanChongCrawlerService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Value("${loginhost}") 
	public String loginHost;
	private static int imageErrorCount;
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			String url="https://"+loginHost+"/wt-web/login";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(1000);  
			if(null!=hPage){
				HtmlImage image = hPage.getFirstByXPath("//img[@src='/wt-web/captcha']"); 
				String code = chaoJiYingOcrService.getVerifycode(image, "1902");   //识别图片验证码
				url="https://"+loginHost+"/wt-web/login";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody=""
						+ "username="+messageLoginForHousing.getNum().trim()+""
						+ "&password="+HousingFundHelperService.encryptedInfo(messageLoginForHousing.getPassword().trim())+""   //登录密码需要RSA加密
						+ "&captcha="+code.trim()+""
						+ "&logintype=1";   //用户登录方式的代号
				webRequest.setRequestBody(requestBody);
				hPage= webClient.getPage(webRequest);
				if(null!=hPage){
					String html=hPage.asXml();
					Document doc = Jsoup.parse(html);
					if(html.contains("欢迎您")){ 
						changeLoginStatusSuccess(taskHousing, webClient);
					}else if(html.contains("in_error")){
						String errorMsg = doc.getElementById("in_error").text();
						if(errorMsg.contains("验证码错误")){
							imageErrorCount++;
							if(imageErrorCount>3){
								 tracer.addTag("验证码输入错误"+imageErrorCount, taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
								 taskHousing.setDescription("登录失败，图片验证码识别错误！");
								 save(taskHousing);
							}else{
								login(messageLoginForHousing);
							}
						}else if(errorMsg.contains("密码格式不正确")){
							 tracer.addTag("密码格式不正确", taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription("密码格式不正确");
							 save(taskHousing);
						}else if(errorMsg.contains("个人密码错误")){
							tracer.addTag("个人密码错误", taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							 taskHousing.setDescription("个人密码错误");
							 save(taskHousing);
						}else if(errorMsg.contains("身份证格式错误")){   //调研的账号是身份证号，不管是输入错误，还是位数不够，都是这个提示
							 tracer.addTag("身份证格式错误", taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							 taskHousing.setDescription("用户名不存在，请重新输入！");
							 save(taskHousing);
							
						}else{
							tracer.addTag("登录出现了其他错误，此处日志记录"+taskHousing.getTaskid(), html);
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
							save(taskHousing);
						}
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录时发生异常，异常信息是：",e.toString());
			System.out.println("登录的异常信息是："+e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
			save(taskHousing);
		}
		return taskHousing;
	}
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=housingFundNanChongCrawlerService.getUserInfo(taskHousing);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		try {
			Future<String> future=housingFundNanChongCrawlerService.getFlowInfo(taskHousing);
			listfuture.put("getFlowInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getFlowInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updatePayStatusByTaskid("数据采集中，流水信息已采集完成",201,taskHousing.getTaskid());
		}
//		最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
//					 判断是否执行完毕
					if (entry.getValue().isDone()) { 
						tracer.addTag(taskHousing.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskHousing.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			tracer.addTag("listfuture--ERROR", taskHousing.getTaskid() + "---ERROR:" + e);
			updateTaskHousing(taskHousing.getTaskid());
		}
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
