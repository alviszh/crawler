package app.service;

import java.util.List;
import java.util.Set;
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
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xiaogan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xiaogan")
public class HousingXiaoGanFutureService extends HousingBasicService implements ICrawlerLogin,ICrawler{
	public static final Logger log = LoggerFactory.getLogger(HousingXiaoGanFutureService.class);

	@Autowired
	private LoginAndGetService loginAndGetService;
	@Autowired
	private HousingXiaoGanService housingXiaoGanService;
//	public static String html = null;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "http://www.xggjj.com/page/wzzs/wzzs_center_mode/wzzs_wzqtzs_mode_wsbsGjjcx.jsp";
		Page htmlpage = null;
		try {
			htmlpage = loginAndGetService.loginByIDNUM(webClient,url, messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
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
			return taskHousing;
		}
		
		if(htmlpage!=null){
			tracer.addTag("登陆html", htmlpage.getWebResponse().getContentAsString());
			if(htmlpage.getWebResponse().getContentAsString().contains("successGrxx")){
				String html = htmlpage.getWebResponse().getContentAsString();
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				
				taskHousing.setCookies(cookies);
				taskHousing.setWebdriverHandle(html);
				save(taskHousing);
				
				
//				taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
//				taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
//				save(taskHousing);
//				updateTaskHousing(taskHousing.getTaskid());
				return taskHousing;
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
				save(taskHousing);
				return taskHousing;
			}
		}else{
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆html", null);
			save(taskHousing);
			return taskHousing;
		}
		
	}
	
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String html = taskHousing.getWebdriverHandle();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			Future<String> future = housingXiaoGanService.getResult(messageLoginForHousing, taskHousing, webClient,
					html);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
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
