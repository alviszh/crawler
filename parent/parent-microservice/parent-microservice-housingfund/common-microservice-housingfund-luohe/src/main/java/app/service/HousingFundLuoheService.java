package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundLuoheParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.luohe" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.luohe" })
public class HousingFundLuoheService extends HousingBasicService implements ICrawler{

	@Autowired
	private HousingFundLuoheParser housingFundLuoheParser;

	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录爬取
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskHousing crawler(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingFundLuoheService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		
		if (null != taskHousing) {
			String msg = housingFundLuoheParser.getInfo(messageLoginForHousing);
			
			tracer.addTag("InsuranceLuoheService.login",
					messageLoginForHousing.getTask_id() + msg);
			if (msg.contains("未查询到信息")) {
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("未查询到信息，请检查查询条件！");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				
				taskHousing.setLoginMessageJson(jsonObject.toString());
				save(taskHousing);
				return taskHousing;
				
			} else if (msg != null && !msg.equals("")) {
				housingFundLuoheParser.htmlUserInfoParser(taskHousing.getTaskid(), msg);
				taskHousing.setLoginMessageJson(jsonObject.toString());
				save(taskHousing);
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			} else {
				tracer.addTag("HousingFundLuoheService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				// 登录失败状态存储
				save(taskHousing);

				return taskHousing;
			}
		}

		return null;
	}

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			crawler(messageLoginForHousing, taskHousing);
		} catch (Exception e) {
			tracer.addTag("HousingFundLuoheService.getInfo---ERROR:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
