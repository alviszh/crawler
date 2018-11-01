package app.service;

import java.net.URL;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.hangzhou.HousingHangZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.hangzhou.HousingHangZhouUserinfoRepository;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.hangzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.hangzhou")
public class HousingHangZhouFutureService extends HousingBasicService{
public static final Logger log = LoggerFactory.getLogger(HousingHangZhouFutureService.class);
	
	@Autowired
	private HousingHangZhouPayRepository housingHangZhouPayRepository;
	
	@Autowired
	private HousingHangZhouUserinfoRepository housingHangZhouUserinfoRepository;
	
	@Autowired
	private LoginAndGetService loginAndGetService;
	
	@Autowired
	private HousingHangZhouService housingHangZhouService;
	public  Object crawler(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://www.hzgjj.gov.cn:8080/WebAccounts/pages/per/login.jsp";
		HtmlPage htmlpage = null;
		try {
			//System.out.println(StatusCodeLogin.getIDNUM());
			if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getCO_BRANDED_CARD())){
				//客户号
				 htmlpage = loginAndGetService.loginByIDNUM(webClient, url, messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
			}else if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getACCOUNT_NUM())){
				// 根据个人登记号登录,用户名
				htmlpage = loginAndGetService.loginByACCOUNT_NUM(webClient, url, messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
			}else if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getCITIZEN_EMAIL())){
				// 市民邮箱
				 htmlpage = loginAndGetService.loginByCITIZEN_EMAIL(webClient, url, messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
			}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆异常", e.getMessage());
			save(taskHousing);
			return null;
		}
		
		
		if(htmlpage.getWebResponse().getContentAsString().contains(" 公告：")){
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			Future<String> future = housingHangZhouService.getResult(messageLoginForHousing, taskHousing, webClient);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
			return taskHousing;
		}else{
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
			save(taskHousing);
			return taskHousing;
		}
		
		
	}
}
