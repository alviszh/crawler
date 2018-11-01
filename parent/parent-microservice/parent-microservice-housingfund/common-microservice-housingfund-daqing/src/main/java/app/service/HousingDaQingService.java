package app.service;




import java.util.List;

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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.daqing.HousingDaQingPay;
import com.microservice.dao.entity.crawler.housing.daqing.HousingDaQingUserinfo;
import com.microservice.dao.repository.crawler.housing.daqing.HousingDaQingPayRepository;
import com.microservice.dao.repository.crawler.housing.daqing.HousingDaQingUserinfoRepository;

import app.bean.JsonRootBean;
import app.bean.WebParamHousing;
import app.crawler.htmlparse.HousingDQParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

/**   
*    
* 项目名称：common-microservice-housingfund-daqing   
* 类名称：HousingDaQingService   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月8日 下午3:42:40   
* @version        
*/
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.daqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.daqing")
public class HousingDaQingService extends HousingBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(HousingDaQingService.class);
		
	@Autowired
	private LoginAndGetService loginAndGetService;
	
	@Autowired
	private HousingDaQingUserinfoRepository housingDaQingUserinfoRepository;
	
	@Autowired
	private HousingDaQingPayRepository housingDaQingPayRepository;
	
	
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		
		tracer.addTag("parser.login.taskid", messageLoginForHousing.getTask_id());
		tracer.addTag("parser.login.auth", messageLoginForHousing.getNum());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		try {
			webParamHousing =  loginAndGetService.login(messageLoginForHousing,0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			webParamHousing.setErrormessage("网络连接超时");
			e.printStackTrace();
			tracer.addTag("parser.login.error", e.getMessage());
		}
		
		if (webParamHousing.getErrormessage()!=null) {
			if(!webParamHousing.getErrormessage().contains("强制登录可能会造成已登录用户未完成的操作无效")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(webParamHousing.getErrormessage());
				taskHousing.setError_message(webParamHousing.getErrormessage());
				save(taskHousing);
				tracer.addTag("parser.login.Errormessage", webParamHousing.getErrormessage());

				return taskHousing;
			}
			
		}
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

		taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
		taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
		taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
		taskHousing.setCookies(webParamHousing.getWebClient());
		save(taskHousing);
		System.out.println("============登录成功==========");
		return taskHousing;
		
	}
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		
		tracer.addTag("parser.crawler.taskid", messageLoginForHousing.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLoginForHousing.getNum());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		try {
			webParamHousing =  loginAndGetService.getUserInfo(webParamHousing, webClient);
			JsonRootBean jsonObject = HousingDQParse.userinfo_parse(webParamHousing.getPage().getWebResponse().getContentAsString());
		
			List<HousingDaQingUserinfo> results = jsonObject.getResults();
			for(HousingDaQingUserinfo result : results){
				result.setTaskid(taskHousing.getTaskid());
				result.setUserid(messageLoginForHousing.getUser_id());
				housingDaQingUserinfoRepository.save(result);
			}
			
			
			webParamHousing = loginAndGetService.getTranFlowByPDF(webParamHousing, webClient, results.get(0));
			List<HousingDaQingPay> result_list = HousingDQParse.tranflowPDF_parse(webParamHousing.getUrl());
			for(HousingDaQingPay result : result_list){
				result.setTaskid(taskHousing.getTaskid());
				result.setUserid(messageLoginForHousing.getUser_id());
				housingDaQingPayRepository.save(result);
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			webParamHousing.setErrormessage("网络连接超时");
			e.printStackTrace();
			tracer.addTag("parser.login.error", e.getMessage());
		}
		
		if (webParamHousing.getErrormessage()!=null) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ERROR.getPhasestatus());
			taskHousing.setUserinfoStatus(404);
			taskHousing.setPaymentStatus(404);
			taskHousing.setDescription(webParamHousing.getErrormessage());
			save(taskHousing);
			tracer.addTag("parser.login.Errormessage", webParamHousing.getErrormessage());
			taskHousing = findTaskHousing(taskHousing.getTaskid());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
			return taskHousing;
		}
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
		taskHousing.setUserinfoStatus(200);
		taskHousing.setPaymentStatus(200);
		taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
		save(taskHousing);
		taskHousing = findTaskHousing(taskHousing.getTaskid());
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
		
	}


	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
	
