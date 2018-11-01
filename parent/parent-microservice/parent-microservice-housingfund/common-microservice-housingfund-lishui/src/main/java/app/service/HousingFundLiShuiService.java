package app.service;

import java.net.URL;

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
import com.microservice.dao.entity.crawler.housing.lishui.HousingLiShuiHtml;
import com.microservice.dao.repository.crawler.housing.lishui.HousingLiShuiHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.lishui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.lishui"})
public class HousingFundLiShuiService extends HousingBasicService  implements ICrawlerLogin{
	@Autowired
	private HousingFundLiShuiCrawlerService housingFundLiShuiCrawlerService;
	@Autowired
	private HousingLiShuiHtmlRepository housingLiShuiHtmlRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Value("${filesavepath}") 
	public String fileSavePath;
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		//该网站一个链接就可以响应所有的数据——个人信息和缴费信息（故此处直接请求数据）
    	housingFundLiShuiCrawlerService.getAllInfo(taskHousing);
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
			String url="http://gjj.e0578.com/hdcy.aspx";
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
//			webClient.getOptions().setJavaScriptEnabled(false);   //不能禁用这个js，哪怕报错是js(因为页面上的提示信息是用js写的)
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage hpage = webClient.getPage(webRequest); 
			if(null!=hpage){
				//此处请求图片验证码链接
				String vericodeUrl="http://gjj.e0578.com/Identify/CheckCode.aspx";
				webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				//利用io流保存图片验证码
				String imgagePath=getImagePath(page,fileSavePath);
				String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1902");   
				//如下模拟点击的方式也可以登录成功
//				HtmlTextInput loginName = (HtmlTextInput)hpage.getFirstByXPath("//input[@id='txtName']"); 
//				HtmlPasswordInput loginPassword = (HtmlPasswordInput)hpage.getFirstByXPath("//input[@id='txtPass']"); 
//				HtmlTextInput validateCode = (HtmlTextInput)hpage.getFirstByXPath("//input[@id='txtCode']"); 
//				HtmlImageInput submitbt = (HtmlImageInput)hpage.getFirstByXPath("//input[@name='ctl00']"); 
//				loginName.setText(messageLoginForHousing.getNum().trim()); 
//				loginPassword.setText(messageLoginForHousing.getPassword().trim()); 	
//				validateCode.setText(code.trim()); 	
//				Page pPage= submitbt.click(); 
//				String alertMsg = WebCrawler.getAlertMsg();
//				tracer.addTag("若是登录失败，获取的弹框提示信息是：", alertMsg);
//				url="http://gjj.lishui.gov.cn/hdcy.aspx";   //后来登陆验证连接换成了如下：
				url="http://gjj.e0578.com/hdcy.aspx";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody=""
						+ "__EVENTTARGET="
						+ "&__EVENTARGUMENT="
						+ "&__VIEWSTATE=%2FwEPDwUJMzQyNTIwOTgwZBgBBR5fX0NvbnRyb2xzUmVxdWlyZVBvc3RCYWNrS2V5X18WAQUFY3RsMDA0pUW9XvEwGP%2BWjAUyrPD9moUP6I6l%2BZPVPpY6gxhusg%3D%3D"
						+ "&__EVENTVALIDATION=%2FwEWBgLTsuLvCALEhISFCwKrm7qxCwLKw6LdBQLChPzDDQKhwImNC8G1Tnm1uo97yd1Htl1L5TPdU9mW5l81I9cTYkx5s7A%2F"
						+ "&txtName="+messageLoginForHousing.getNum().trim()+""
						+ "&txtPass="+messageLoginForHousing.getPassword().trim()+""
						+ "&txtCode="+code.trim()+""
						+ "&ctl00.x=28"
						+ "&ctl00.y=14";
				webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				webRequest.setAdditionalHeader("Host", "gjj.e0578.com");
				webRequest.setAdditionalHeader("Origin", "http://gjj.e0578.com");
				webRequest.setAdditionalHeader("Referer", "http://gjj.e0578.com/hdcy.aspx");
				webRequest.setRequestBody(requestBody);
				Page pPage= webClient.getPage(webRequest);
				if(pPage!=null){
					String html=pPage.getWebResponse().getContentAsString();
					HousingLiShuiHtml loginHtml = new HousingLiShuiHtml();
					loginHtml.setPagenumber(1);
					loginHtml.setType("登陆信息验证结果源码页");
					loginHtml.setTaskid(messageLoginForHousing.getTask_id());
					loginHtml.setUrl(url);
					loginHtml.setHtml(html);
					housingLiShuiHtmlRepository.save(loginHtml);			
			    	tracer.addTag("登陆信息验证结果源码页：","已经入库");
					if(html.contains("欢迎您登录丽水市住房公积金管理中心")){
						//更新登录成功的信息
						changeLoginStatusSuccess(taskHousing, webClient);
					}else{
						if(html.contains("alert")){   //该网站的登陆错误的信息是用javascript弹出的
							//密码输入不正确的时候提示的是：错误的身份证号码或密码
							if(html.contains("错误的身份证号码或密码")){
								tracer.addTag("错误的身份证号码或密码", taskHousing.getTaskid());
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
								taskHousing.setDescription("错误的身份证号码或密码！");
								save(taskHousing);
							}else if(html.contains("错误的验证码")){
								captchaErrorCount++;
								tracer.addTag("action.login.auth.imageErrorCount", "这是第"+captchaErrorCount+"次因图片验证码识别错误重新调用登录方法");
								 //图片验证码识别错误，重试三次登录
								if(captchaErrorCount>3){
									tracer.addTag("验证码有误,请重新输入！"+captchaErrorCount, taskHousing.getTaskid());
									taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
									taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
									taskHousing.setDescription("验证码有误,请重新输入！");
									save(taskHousing);
								}else{
									login(messageLoginForHousing);
								}
							}else if(html.contains("错误的身份证号码")){
								tracer.addTag("错误的身份证号码！", taskHousing.getTaskid());
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription("错误的身份证号码！");
								save(taskHousing);
							}else{
								tracer.addTag("登录失败，出现了调研时没有遇到的错误，错误源码中有alert", "详见该登录结果源码页，已存入库中");
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription("系统繁忙，请稍后再试！");
								save(taskHousing);
							}
						}else{
							tracer.addTag("登录失败，出现了调研时没有遇到的错误", "详见该登录结果源码页，已存入库中");
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription("系统繁忙，请稍后再试！");
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
}
