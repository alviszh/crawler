package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

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
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.eerduosi.HousingEErDuoSiParams;
import com.microservice.dao.repository.crawler.housing.eerduosi.HousingEErDuoSiParamsRepository;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.eerduosi"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.eerduosi"})
public class HousingFundEErDuoSiService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private HousingFundEErDuoSiCrawlerService housingFundEErDuoSiCrawlerService;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String fileSavePath;
	@Autowired
	private HousingEErDuoSiParamsRepository paramsRepository;
	private static int imageErrorCount;

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		HousingEErDuoSiParams params = paramsRepository.findTopByTaskidOrderByCreatetimeDesc(messageLoginForHousing.getTask_id().trim());
		String path=null;
		if(null!=params){
			path=params.getPath().trim();
		}
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=housingFundEErDuoSiCrawlerService.getUserInfo(taskHousing,path);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		try {
			Future<String> future=housingFundEErDuoSiCrawlerService.getFlowInfo(taskHousing,path);
			listfuture.put("getFlowInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getFlowInfo.e", e.toString());
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
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			//如下链接中的括号部分是会变化的，且起到token的作用
			String url="http://"+loginHost+"/(S(xnsldr45psnccw45b1x3el45))/login.aspx";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				//获取登录时中间参数，就是括号部分
				String path = hPage.getUrl().getPath(); 
				path = path.substring(1,path.lastIndexOf("/"));
				HousingEErDuoSiParams params=new HousingEErDuoSiParams();
				params.setTaskid(messageLoginForHousing.getTask_id());
				params.setPath(path);
				paramsRepository.save(params);
				tracer.addTag("从登陆连接中截取的参数已经入库：",path);
				int statusCode = hPage.getWebResponse().getStatusCode();
				if(200==statusCode){   //有时网站出问题，响应的状态是500
					tracer.addTag("action.login.normalService", "网站没有处于维护状态，可以正常使用");
					/**
					 * 图片验证码 (有时候登录的时候无法将验证码图片加载出来)
					 * 
					 * 之前刷不出来图片验证码的时候，模拟点击的是页面上图片验证码的位置，实际上应该模拟点击页面源代码验证码生成的那个a标签链接
					 * 但是在测试类中好用，放在此处又不起作用了
					 *//*
					HtmlAnchor validatecode = (HtmlAnchor)hPage.getFirstByXPath("//*[@id=\"form1\"]/dl/dd[3]/a");  
					hPage = validatecode.click();
					Thread.sleep(500);
					HtmlImage image = hPage.getFirstByXPath("//img[@id='ImageCheck']");
					String code =chaoJiYingOcrService.getVerifycode(image, "1902");
					System.out.println("识别出来的图片验证码是："+code);*/
					
					//请求验证码图片
					url="http://"+loginHost+"/"+path+"/ValidateCode.aspx";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page=webClient.getPage(webRequest);
					String imagePath="";
					if(page!=null){
						imagePath = getImagePath(page, fileSavePath);
					}
					String code =chaoJiYingOcrService.callChaoJiYingService(imagePath, "1902");
					System.out.println("识别出来的图片验证码是："+code);
					
					HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='txtUsername']"); 
					HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@id='txtUserpass']"); 
					HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='yzm']"); 
					HtmlSubmitInput submitbt = (HtmlSubmitInput)hPage.getFirstByXPath("//input[@id='Button1']"); 
					loginName.setText(messageLoginForHousing.getNum().trim()); 
					loginPassword.setText(messageLoginForHousing.getPassword().trim()); 	
					validateCode.setText(code); 	
					hPage = submitbt.click(); 
					if(null!=hPage){
						statusCode = hPage.getWebResponse().getStatusCode();
						if(200==statusCode){
							String html=hPage.asXml();
							//获取弹框提示内容(测试类好用，此处就失灵了)
//							String html=WebCrawler.gethtml();  
							tracer.addTag("action.login.loginResultHtml", html);
							if(html.contains("登陆成功")){
								changeLoginStatusSuccess(taskHousing, webClient);
							}else{
								if(html.contains("验证码输入错误")){
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
								}else if(html.contains("密码错误")){
									tracer.addTag("密码错误", taskHousing.getTaskid());
									taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
									taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
									taskHousing.setDescription("密码错误！");
									save(taskHousing);
								}else if(html.contains("用户名不存在，请重新输入")){
									tracer.addTag("用户名不存在，请重新输入！", taskHousing.getTaskid());
									taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
									taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
									taskHousing.setDescription("用户名不存在，请重新输入！");
									save(taskHousing);
								}else if(html.contains("数据库连接失败")){
									tracer.addTag("数据库连接失败！", taskHousing.getTaskid());
									taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
									taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
									taskHousing.setDescription("数据库连接失败！");
									save(taskHousing);
								}else{
									tracer.addTag("登录出现了其他错误，此处日志记录，详见html内容"+taskHousing.getTaskid(), html);
									taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
									taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
									taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
									save(taskHousing);
								}
							}
						}
					}
				}else{
					//有时候网站维护中，会响应状态码500
					tracer.addTag("登录页面无法访问，响应的状态码是："+statusCode, "Internal Server Error");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("网站维护中，请明日再试！");
					save(taskHousing);
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录时发生异常，异常信息是：",e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录失败，系统繁忙，请稍后再试！");
			save(taskHousing);
		}
		return taskHousing;
	}
}
