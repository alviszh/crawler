package app.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

/**
 * 
 * 项目名称：common-microservice-housingfund-sz-anhui-tongyi
 * 类名称：HousingAnhuiTongyiService 类描述： 创建人：hyx 创建时间：2018年4月25日 上午11:31:02
 * 
 * @version
 */

@Component
@Service
@EnableAsync
public class HousingAnhuiTongyiService  extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	public LoginAndGetAnHuiTongYiCommonService loginAndGetAnHuiTongYiCommonService;
	@Autowired
	protected TaskHousingRepository taskHousingRepository;
	
	@Autowired
	private TracerLog traceLog;
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing  taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		try {
			 taskHousing = loginAndGetAnHuiTongYiCommonService.login(messageLoginForHousing, taskHousing);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getError_code());
			
		}
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		traceLog.output("login", taskHousing.toString());
		taskHousing = taskHousingRepository.save(taskHousing);
		
		return taskHousing;
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
