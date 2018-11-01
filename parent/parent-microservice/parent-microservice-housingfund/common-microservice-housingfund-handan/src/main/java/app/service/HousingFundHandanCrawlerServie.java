package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.handan.HousingHandanPay;
import com.microservice.dao.entity.crawler.housing.handan.HousingHandanUserinfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.handan.HousingHandanPayRepository;
import com.microservice.dao.repository.crawler.housing.handan.HousingHandanUserinfoRepository;

import app.bean.PayBean;
import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.handan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.handan"})
public class HousingFundHandanCrawlerServie extends HousingBasicService{
	
	@Autowired
	private TracerLog tracer;	
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingHandanUserinfoRepository housingHandanUserinfoRepository;
	@Autowired
	private HousingHandanPayRepository housingHandanPayRepository;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 获取用户信息
	 * @param taskHousing
	 * @param webClient
	 */
	public void crawlerUserinfo(TaskHousing taskHousing, WebClient webClient, String _csrf) {
		
		String taskid = taskHousing.getTaskid();
		tracer.addTag("crawlerUserinfo", taskid);
		try {
			String html = getUserinfo(webClient,_csrf);
			if(html.contains("Unlogin")){
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_NEED_LOGIN.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_NEED_LOGIN.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_NEED_LOGIN.getDescription());
				taskHousingRepository.save(taskHousing);
			}else{
				HousingHandanUserinfo userinfo = new Gson().fromJson(html, HousingHandanUserinfo.class);
				String reCode = userinfo.getRecode();
				if(reCode.equals("000000")){				
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					taskHousingRepository.save(taskHousing);
					
					userinfo.setTaskid(taskid);
					housingHandanUserinfoRepository.save(userinfo);
					tracer.addTag("用户信息已入库", userinfo.toString());	
					
					//获取缴费详情
					getPay(webClient,_csrf,taskid);
				}else{				
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getDescription());
					taskHousing.setUserinfoStatus(201);
					taskHousingRepository.save(taskHousing);
					
					//获取缴费详情
					getPay(webClient,_csrf,taskid);
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("crawlerUserinfo", "error");
			tracer.addTag("获取用户信息失败", e.getMessage());
			
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription("获取用户信息失败，稍后再试");
			taskHousing.setUserinfoStatus(201);
			taskHousingRepository.save(taskHousing);			
			//获取缴费详情
			getPay(webClient,_csrf,taskid);
		}
		
		
	}

	/**
	 * 获取缴费详情
	 * @param webClient
	 * @param _csrf
	 * @param taskid
	 */
	private void getPay(WebClient webClient, String _csrf, String taskid) {
		
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(taskid);
		tracer.addTag("getPay", taskid);
		
		try {
			String html = getPayHtml(webClient,_csrf);
			if(html.contains("000000")){
				PayBean payBean = new Gson().fromJson(html, PayBean.class);
				List<HousingHandanPay> data = payBean.getResult();
				for(HousingHandanPay pay : data){
					pay.setTaskid(taskid);
				}
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(200);
				taskHousingRepository.save(taskHousing);
				
				housingHandanPayRepository.saveAll(data);
				tracer.addTag("缴费详情已入库", data.size()+"");
				//判断是否爬取成功
				updateTaskHousing(taskHousing.getTaskid()); 						
			}else{
				tracer.addTag("获取缴费详情失败", "未知原因");
				
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription("获取缴费信息失败，稍后再试");
				taskHousing.setPaymentStatus(201);
				taskHousingRepository.save(taskHousing);
				//判断是否爬取成功
				updateTaskHousing(taskHousing.getTaskid()); 
			}
		} catch (Exception e) {
			tracer.addTag("getPay", "error");
			tracer.addTag("获取缴费信息失败", e.getMessage());
			
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription("获取缴费信息失败，稍后再试");
			taskHousing.setPaymentStatus(201);
			taskHousingRepository.save(taskHousing);
			//判断是否爬取成功
			updateTaskHousing(taskHousing.getTaskid()); 
		}
	}

	/**
	 * 获取缴费详情页面
	 * @param webClient
	 * @param _csrf
	 * @throws Exception 
	 */
	private String getPayHtml(WebClient webClient, String _csrf) throws Exception {
		String url = "http://www.hdgjj.cn/olbh/qryPersonAccDetails.do";
		URL smsAction = new URL(url);
		WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("_csrf", _csrf));
		requestSettings.getRequestParameters().add(new NameValuePair("begDate", "2010-01-01"));
		requestSettings.getRequestParameters().add(new NameValuePair("currentPage", "1"));
		requestSettings.getRequestParameters().add(new NameValuePair("endDate", sdf.format(new Date())));
		requestSettings.getRequestParameters().add(new NameValuePair("limits", "1000"));
		requestSettings.setAdditionalHeader("Host", "www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Origin", "http://www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Referer", "http://www.hdgjj.cn/olbh/pub/home");
		
		Page page = webClient.getPage(requestSettings); 
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("getPayHtml.page",html);
		return html;
	}

	/**
	 * 获取用户信息页面
	 * @param webClient
	 * @param _csrf
	 * @return
	 * @throws Exception
	 */
	private String getUserinfo(WebClient webClient, String _csrf) throws Exception {
		URL smsAction = new URL("http://www.hdgjj.cn/olbh/qryPersonInfo.do?_csrf"+_csrf);
		WebRequest  requestSettings = new WebRequest(smsAction, HttpMethod.POST);
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("_csrf", _csrf));
		
		requestSettings.setAdditionalHeader("Host", "www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Origin", "http://www.hdgjj.cn");
		requestSettings.setAdditionalHeader("Referer", "http://www.hdgjj.cn/olbh/pub/home");
		
		Page page = webClient.getPage(requestSettings); 
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("userinfo.page",html);
		return html;
	}

}
