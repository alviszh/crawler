package app.service;

import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huizhou.HousingHuiZhouPay;
import com.microservice.dao.entity.crawler.housing.huizhou.HousingHuiZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.huizhou.HousingHuiZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.huizhou.HousingHuiZhouUserinfoRepository;
import com.module.htmlunit.WebCrawler;

import app.crawler.htmlparse.HousingHZParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.huizhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.huizhou")
public class HousingHuiZhouFutureService extends HousingBasicService implements ICrawlerLogin,ICrawler{
public static final Logger log = LoggerFactory.getLogger(HousingHuiZhouFutureService.class);
	
	@Autowired
	private LoginAndGetService loginAndGetService;
	@Autowired
	private HousingHuiZhouUserinfoRepository housingHuiZhouUserinfoRepository;
	@Autowired
	private HousingHuiZhouPayRepository housingHuiZhouPayRepository;
//	public static String html = null;
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Page htmlpage = null;
		try {
			htmlpage = loginAndGetService.loginByIDNUM(webClient, messageLoginForHousing.getUsername().trim(), messageLoginForHousing.getCountNumber(), messageLoginForHousing.getNum().trim(), messageLoginForHousing.getTelephone().trim());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
//			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆异常", e.getMessage());
			save(taskHousing);
			return taskHousing;
		}
		if(htmlpage!=null){
			tracer.addTag("登陆html", htmlpage.getWebResponse().getContentAsString());
			if(htmlpage.getWebResponse().getContentAsString().contains("缴存单位名称")){
				String html = htmlpage.getWebResponse().getContentAsString();
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setCookies(html);
//				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
				tracer.addTag("登陆成功", messageLoginForHousing.getTask_id());
				
				return taskHousing;
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
				save(taskHousing);
				tracer.addTag("登陆失败", messageLoginForHousing.getTask_id());
				return taskHousing;
			}
		}else{
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
//			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆html", null);
			save(taskHousing);
			return taskHousing;
		}
		
	}
	

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			String html = taskHousing.getCookies();
			tracer.addTag("爬取html", html);
			HousingHuiZhouUserInfo userinfo = HousingHZParse.userinfo_parse(html);
			if (userinfo == null) {
				taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			} else {
				userinfo.setTaskid(taskHousing.getTaskid());
				housingHuiZhouUserinfoRepository.save(userinfo);
				taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
			}
			List<HousingHuiZhouPay> listresult = HousingHZParse.paydetails_parse(html, taskHousing);
			if (listresult == null) {
				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
			} else {

				housingHuiZhouPayRepository.saveAll(listresult);
				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
			}
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
//			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("爬取异常", e.getMessage());
			save(taskHousing);
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
