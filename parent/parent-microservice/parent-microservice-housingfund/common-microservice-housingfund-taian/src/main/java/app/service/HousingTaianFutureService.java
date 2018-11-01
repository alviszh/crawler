package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingTaianParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.taian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.taian")
public class HousingTaianFutureService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingTaianParse housingTaianParse;
	@Autowired
	private HousingTaianInfoService housingTaianInfoService;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;	
	private InfoParam infoParam=new InfoParam();
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingTaianFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		if (null != taskHousing) {
			int count=0;
			WebParam webParam = housingTaianParse.login(messageLoginForHousing,count);
			if (null == webParam) {
				tracer.addTag("HousingTaianFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				// 登录失败状态存储
				save(taskHousing);
			} else {
				String html = webParam.getHtml();
				String text = webParam.getText();
				tracer.addTag("HousingTaianFutureService.login--次数:" + count,
						messageLoginForHousing.getTask_id() + "html---------" + html + "text---------" + text);
				if (null != webParam.getInfoParam() && null != webParam.getInfoParam().getZgxm()
						&& !"".equals(webParam.getInfoParam().getZgxm()) && (!html.contains("密码输入错误"))) {
					taskHousing.setCookies(webParam.getCookies());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());	
					save(taskHousing);
					infoParam=webParam.getInfoParam();				
				} else if (null != text && !"".equals(text)) {
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(text);

					taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
					taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					save(taskHousing);		
				} else {
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());

					taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
					taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					// 登录失败状态存储
					save(taskHousing);				
				}
			}
		}
		 taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingTaianInfoService.getUserInfo(messageLoginForHousing,infoParam);
		housingTaianInfoService.getPay(messageLoginForHousing, infoParam);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		return null;
	}

}