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
import app.parser.HousingFundJiyuanParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.jiyuan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.jiyuan" })
public class HousingFundJiyuanService extends HousingBasicService implements ICrawler{

	@Autowired
	private HousingFundJiyuanParser housingFundJiyuanParser;

	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录爬取
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskHousing crawler(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingFundJiyuanService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		
		if (null != taskHousing) {
			String msg = housingFundJiyuanParser.getInfo(messageLoginForHousing);
			
			tracer.addTag("InsuranceLiaochengService.login",
					messageLoginForHousing.getTask_id() + msg);
			if (msg.contains("数据库中没有该条用户的记录")) {
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("数据库中没有该条用户的记录");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				
				taskHousing.setLoginMessageJson(jsonObject.toString());
				save(taskHousing);
				return taskHousing;
				
			} else if (msg != null && !msg.equals("")) {
				housingFundJiyuanParser.htmlUserInfoParser(taskHousing, msg);
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			} else {
				tracer.addTag("HousingFundJiyuanService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				
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
			tracer.addTag("HousingFundJiyuanService.getInfo---ERROR:",
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
