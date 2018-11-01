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
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huzhou.HousingHuZhouHtml;
import com.microservice.dao.repository.crawler.housing.huzhou.HousingHuZhouHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;
/**
 * @author sln
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.huzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.huzhou"})
public class HousingFundHuZhouService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private HousingFundHuZhouCrawlerService housingFundHuZhouCrawlerService;
	@Autowired
	private HousingHuZhouHtmlRepository housingHuZhouHtmlRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future=housingFundHuZhouCrawlerService.getUserInfo(messageLoginForHousing,taskHousing);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		try {
			Future<String> future=housingFundHuZhouCrawlerService.getFlowInfo(messageLoginForHousing,taskHousing);
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
			String url="https://"+loginHost+"/hzgjj-wsyyt/personLogin.html";
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage hpage = webClient.getPage(webRequest); 
			if(null!=hpage){
				HtmlImage image = hpage.getFirstByXPath("//img[@id='veryCodeImg']"); 
				String code = chaoJiYingOcrService.getVerifycode(image, "1902");
				url="https://"+loginHost+"/hzgjj-wsyyt/ajax/login.action";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody=""
						+ "_sid=personLoginService_personLogin"
						+ "&json={\"certNo\":\""+messageLoginForHousing.getNum().trim()+"\","    //暂承载身份证号
						+ "\"personAcctNo\":\""+messageLoginForHousing.getHosingFundNumber().trim()+"\","
						+ "\"password\":\""+messageLoginForHousing.getPassword().trim()+"\","
						+ "\"veriCode\":\""+code.trim()+"\"}"
						+ "&uid=";
				webRequest.setRequestBody(requestBody);
				Page pPage= webClient.getPage(webRequest);
				if(pPage!=null){
					String html=pPage.getWebResponse().getContentAsString();
					if(html.contains("操作成功")){
						//更新登录成功的信息
						changeLoginStatusSuccess(taskHousing, webClient);
					}else{
						HousingHuZhouHtml loginHtml = new HousingHuZhouHtml();
						loginHtml.setPagenumber(1);
						loginHtml.setType("登陆信息验证结果源码页");
						loginHtml.setTaskid(messageLoginForHousing.getTask_id());
						loginHtml.setUrl(url);
						loginHtml.setHtml(html);
						housingHuZhouHtmlRepository.save(loginHtml);			
				    	tracer.addTag("登陆信息验证结果源码页：","已经入库");
						//包含false
						String errorMsg = JSONObject.fromObject(html).getString("msg");
						if(errorMsg.contains("验证码错误")){
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
						}else if(errorMsg.contains("个人账户信息不存在")){
							tracer.addTag("个人账户信息不存在", taskHousing.getTaskid());
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
							taskHousing.setDescription("个人账户信息不存在！");
							save(taskHousing);
						}else if(errorMsg.contains("密码错误")){
							tracer.addTag("密码错误", taskHousing.getTaskid());
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription("密码错误！");
							save(taskHousing);
						}else{
							tracer.addTag("登录失败，出现了调研时没有遇到的错误：", html);
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
			taskHousing.setDescription("系统繁忙，请稍后再试！");
			save(taskHousing);
		}
		return taskHousing;
	}
}
