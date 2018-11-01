package app.service;

import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

import app.bean.WebParamHousing;
import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

/**   
*    
* 项目名称：common-microservice-housingfund-sz-anhui-tongyi-fuyang   
* 类名称：HousingFuYangCrawlerService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2018年5月3日 下午5:41:44   
* @version        
*/

@Component
@Service
@EnableAsync
public class HousingFuYangCrawlerService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	public TracerLog tracerLog;
	
//	@Autowired
//	public LoginAndGetAnHuiTongYiCommonService loginAndGetAnHuiTongYiCommonService;
	
	
	@Autowired
	public HousingFuYangAsyncService housingFuYangAsyncService;
		
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		try {
			
			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			webClient = addcookie(webClient, taskHousing);
			WebParamHousing<?> webParamHousing = null;
			String messageLoginJson = gs.toJson(messageLoginForHousing);
			taskHousing.setLoginMessageJson(messageLoginJson);
			System.out.println(messageLoginForHousing.toString());
			
			try {
				
				String persionid = housingFuYangAsyncService.getPersionId(webClient);
				
				
				housingFuYangAsyncService.getPersionInfo(webClient, persionid, taskHousing.getTaskid());
				
				housingFuYangAsyncService.getPayInfo(webClient, persionid, taskHousing.getTaskid());
				
				taskHousing = findTaskHousing(taskHousing.getTaskid());
				save(taskHousing);
				updateTaskHousing(taskHousing.getTaskid());
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_Five.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_Five.getMessage());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				tracer.addTag("parser.login.auth", e1.getMessage());
				save(taskHousing);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getDescription());

			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			tracer.addTag("parser.login.auth", e.getMessage());
			save(taskHousing);
		}
		
		
	
		return null;

	}

	public WebClient addcookie(WebClient webclient, TaskHousing taskHousing) {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}
}
