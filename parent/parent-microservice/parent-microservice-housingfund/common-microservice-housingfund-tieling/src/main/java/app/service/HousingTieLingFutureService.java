package app.service;

import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tieling")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tieling")
public class HousingTieLingFutureService extends HousingBasicService implements ICrawlerLogin,ICrawler{
	public static final Logger log = LoggerFactory.getLogger(HousingTieLingFutureService.class);
	
	
	@Autowired
	private LoginAndGetService loginAndGetService;
	
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public  Object crawler(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://www.tlgjj.com.cn/grdl.jhtml";
		HtmlPage htmlpage = null;
		try {
			//System.out.println(StatusCodeLogin.getIDNUM());
			if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getCO_BRANDED_CARD())){
				// 联名卡号
				 htmlpage = loginAndGetService.loginByCO_BRANDED_CARD(webClient, url, messageLoginForHousing,taskHousing);
			}else if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getACCOUNT_NUM())){
				// 个人账号登录
				htmlpage = loginAndGetService.loginByACCOUNT_NUM(webClient, url, messageLoginForHousing,taskHousing);
			}else if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getIDNUM())){
				//身份证号
				 htmlpage = loginAndGetService.loginByIDNUM(webClient, url, messageLoginForHousing,taskHousing);
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
		return htmlpage;
		
		
//		if(htmlpage.getWebResponse().getContentAsString().contains("欢迎您")){
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
//
//			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
//			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
//			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			save(taskHousing);
//			
//			taskHousing= housingTieLingService.getResult(messageLoginForHousing, taskHousing, webClient,htmlpage);
////			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
////			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
////			save(taskHousing);
////			updateTaskHousing(taskHousing.getTaskid());
//			return taskHousing;
//		}else{
//			taskHousing.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FIVE.getDescription());
//			taskHousing.setError_message(StatusCodeEnum.TASKMOBILE_CRAWLER_ID_Verific_ERROR.getDescription());
//			save(taskHousing);
//			return taskHousing;
//		}
		
		
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
