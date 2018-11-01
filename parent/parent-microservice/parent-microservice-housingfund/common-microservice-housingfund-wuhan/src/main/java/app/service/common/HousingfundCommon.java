package app.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.MessageResult;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuhan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuhan")
public class HousingfundCommon {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingfundCommonUnit housingfundCommonUnit;
	private MessageResult messageresult;
	
	@Async
	public void login(TaskHousing taskHousing, MessageLogin messageLogin) {
		tracer.addTag("parser.login.taskid",taskHousing.getTaskid());
		tracer.addTag("parser.login.auth", messageLogin.getName());
		
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getError_code());
		taskHousing.setError_message(null);
		save(taskHousing);
	
	//	MobileLogin mobileLogin = new MobileLogin();
		messageresult = new MessageResult();
		
//		mobileLogin.setName(messageLogin.getName().trim());
//		mobileLogin.setPassword(messageLogin.getPassword().trim());
//		mobileLogin.setUsrid(messageLogin.getUser_id());
//
//		messageresult.setUser_id(mobileLogin.getUsrid());
		
		try {
			housingfundCommonUnit.login(messageLogin);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void save(TaskHousing taskHousing) {
		taskHousingRepository.save(taskHousing);
	}
}
