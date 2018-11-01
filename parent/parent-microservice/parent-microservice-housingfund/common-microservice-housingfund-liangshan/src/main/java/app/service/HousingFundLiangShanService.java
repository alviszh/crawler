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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
import app.service.common.aop.ICrawlerLogin;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.liangshan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.liangshan"})
public class HousingFundLiangShanService extends HousingBasicService implements ICrawlerLogin {
	@Autowired
	private HousingFundLiangShanCrawlerService housingFundLiangShanCrawlerService;
	@Value("${loginhost}") 
	public String loginHost;
	@Value("${filesavepath}") 
	public String filesavepath;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://"+loginHost+"/ispobs/Forms/SysFiles/Sys_Yzm.aspx";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest); 
			if(null!=page){
				String imagePath = HousingFundHelperService.getImagePath(page,filesavepath);
				String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1902");
				//验证登录信息
				url="http://"+loginHost+"/ispobs/Forms/SysFiles/Login.aspx";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody=""
						+ "__LASTFOCUS="
						+ "&__VIEWSTATE=%2FwEPDwUJLTk1OTYyMTcyD2QWAgIDD2QWAgIBDxYCHglpbm5lcmh0bWwFCeW8gOWPkeWVhmRk%2FqbM1pnJ2ar8LuChlZX7EEi7Ph4%3D"
						+ "&__VIEWSTATEGENERATOR=FFBDC492"
						+ "&__EVENTTARGET="
						+ "&__EVENTARGUMENT="
						+ "&__EVENTVALIDATION=%2FwEWCgK0rZGhDgKl1bKzCQK1qbSWCwKM3frBCALChPzDDQKp4ZKPBwLV49m0DwLpxZniAwKY2Z6UCALF7OzXDve7UCXbS7fBJn8sTqSwhnAHtk9N"
						+ "&txtUserName="+messageLoginForHousing.getNum().trim()+""
						+ "&txtPassWord="+messageLoginForHousing.getPassword().trim()+""
						+ "&txtGRDLYZM="+code.trim()+""
						+ "&txtCode="
						+ "&hfUserType=1"
						+ "&btnReLogin="
						+ "&hfLoginEtpsBySMS=TRUE"
						+ "&hfLoginIndvBySMS=FALSE"
						+ "&_systabindexString=";
				webRequest.setRequestBody(requestBody);
				page = webClient.getPage(webRequest); 
				if(null!=page){
					String html=page.getWebResponse().getContentAsString();
					if(html.contains("个人账户")){
						changeLoginStatusSuccess(taskHousing, webClient);
					}else{
						if(html.contains("refreshVail")){    //有登录错误提示信息的标志
							if(html.contains("验证码有误,请重新输入！")){
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
							}else if(html.contains("密码不正确")){
								 tracer.addTag("密码不正确", taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
								 taskHousing.setDescription("您输入的密码有误，请重新输入！");
								 save(taskHousing);
							}else if(html.contains("用户不存在")){
								 tracer.addTag("用户不存在，请确认输入的账号是否正确！", taskHousing.getTaskid());
								 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								 taskHousing.setDescription("用户不存在，请确认输入的账号是否正确！");
								 save(taskHousing);
							}else{
								tracer.addTag("登录失败，出现了调研时没有遇到的错误", "具体错误，详见验证登录信息返回的结果页面，在页面信息最底部的弹框js中");
								taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription("系统繁忙，请稍后再试！");
								save(taskHousing);
							}
						}else{
							tracer.addTag("没有登录失败的相关标识", "将登录状态改为：系统繁忙，请稍后再试！");
							taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription("系统繁忙，请稍后再试！");
							save(taskHousing);
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
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing =findTaskHousing(messageLoginForHousing.getTask_id());
		Map<String, Future<String>> listfuture = new HashMap<>();
		try {
			Future<String> future=housingFundLiangShanCrawlerService.getUserInfo(taskHousing);
			listfuture.put("getUserInfo", future);
		}catch (Exception e) {
			tracer.addTag("异步爬取用户信息出现异常", e.toString());
		}
		try {
			Future<String> future=housingFundLiangShanCrawlerService.getDetailAccount(taskHousing);
			listfuture.put("getDetailAccount", future);
		}catch (Exception e) {
			tracer.addTag("异步爬取缴费信息出现异常", e.toString());
		}
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
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
