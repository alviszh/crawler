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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;


/**
 * @description:
 * @author: sln 
 * @date: 2017年10月25日 上午11:32:52 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.qingdao"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.qingdao"})
public class HousingFundQingDaoService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	@Value("${loginip}") 
	public String loginip;
	@Autowired
	private HousingFundQingDaoCrawlerService housingFundQingDaoCrawlerService;
	
	public static int imageErrorCount;
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			String url="http://"+loginip+"/grptLogin.htm";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				int status = hPage.getWebResponse().getStatusCode();
				if(200 == status){
					HtmlImage image = hPage.getFirstByXPath("//img[@id='grptlogin_yzm']");
					String code = chaoJiYingOcrService.getVerifycode(image, "4005");
					HtmlTextInput username = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='name']"); 
					HtmlPasswordInput password = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@name='password']");
					HtmlTextInput verifyCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@name='yzm']");
					HtmlButtonInput button = (HtmlButtonInput)hPage.getFirstByXPath("//input[@onclick='login()']");			
					username.setText(messageLoginForHousing.getNum());
					password.setText(messageLoginForHousing.getPassword());
					verifyCode.setText(code);
					HtmlPage logonPage = button.click();
					Thread.sleep(2000);  //由于网站自身登录有进度条，此处必须睡一点时间，让进度条走完，不然获取的登录页面信息中将会提示：登录中
					if(null!=logonPage){
						String logonHtml=logonPage.asXml();   //用.asXml在此网站中就能将弹出层的信息显示在处理后的返回String中
						if(logonHtml.contains("messager-window")){ //要是页面出现错误，将会弹框
							Document doc=Jsoup.parse(logonHtml);
							String errorMsg = doc.getElementsByClass("messager-body").get(0).getElementsByTag("div").get(0).text();
							String[] split = errorMsg.split(" ");
							errorMsg=split[0];
							if(errorMsg.contains("身份证号不存在")){
								 tracer.addTag("身份证号不存在！", taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								 taskHousing.setDescription("登录失败，身份证号不存在！");
								 save(taskHousing);
							}else if(errorMsg.contains("密码输入有误")){
								 tracer.addTag(errorMsg, taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
								 taskHousing.setDescription(errorMsg);
								 save(taskHousing);
							}else if(errorMsg.contains("验证码输入有误")){
								imageErrorCount++;
								if(imageErrorCount>3){
									 tracer.addTag("操作失败:进行身份校验时出错:您输入的验证码与图片不符"+imageErrorCount, taskHousing.getTaskid());
									 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
									 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhasestatus());
									 taskHousing.setDescription("登录失败，图片验证码识别错误！");
									 save(taskHousing);
								}else{
									login(messageLoginForHousing);
								}
							}else{
								tracer.addTag("登录出现了其他错误，此处日志记录：", errorMsg);
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription("登录失败，相关信息输入有误，请重试！");
								save(taskHousing);
							}
						}else{
							//登录成功
							changeLoginStatusSuccess(taskHousing, webClient);
						}
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("登录失败，程序出现异常："+taskHousing.getTaskid(), e.toString());
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
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=housingFundQingDaoCrawlerService.getUserInfo(taskHousing);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		try {
			Future<String> future=housingFundQingDaoCrawlerService.getDetailAccount(taskHousing);
			listfuture.put("getDetailAccount", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
			updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
		}
		try {
			Future<String> future=housingFundQingDaoCrawlerService.getCompInfo(taskHousing);
			listfuture.put("getCompInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getCompInfo.e", taskHousing.getTaskid()+"  "+e.toString());
		}
		try {
			Future<String> future=housingFundQingDaoCrawlerService.getChargeInfo(taskHousing);
			listfuture.put("getChargeInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeInfo.e", taskHousing.getTaskid()+"  "+e.toString());
		}
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
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
