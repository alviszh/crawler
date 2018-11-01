package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingRizhaoParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.rizhao")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.rizhao")
public class HousingRizhaoFutureService extends HousingBasicService implements ICrawlerLogin {

	@Autowired
	private HousingRizhaoParse housingRizhaoParse;

	@Autowired
	private HousingRizhaoInfoService housingRizhaoInfoService;
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
		tracer.addTag("housingRizhaoFutureService.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		if (null != taskHousing) {
			Integer errCount=0;
			WebParam webParam = housingRizhaoParse.login(messageLoginForHousing);
			if (null == webParam) {
				tracer.addTag("housingRizhaoFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				// 登录失败状态存储
				save(taskHousing);
			} else {
				String html = webParam.getHtml();
				String text = webParam.getText();
				tracer.addTag("housingRizhaoFutureService.login--次数:" + errCount,
						messageLoginForHousing.getTask_id() + "html---------" + html + "text---------" + text);
				if (null != webParam.getInfoParam() && null != webParam.getInfoParam().getZgxm()
						&& !"".equals(webParam.getInfoParam().getZgxm()) && (!html.contains("密码输入错误"))) {
					taskHousing.setCookies(webParam.getCookies());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());					
					save(taskHousing);
					infoParam=webParam.getInfoParam();
				} else if (null != text && !"".equals(text)) {
                     if (text.contains("请输入15位或18位身份证号")) {
                    	tracer.addTag("身份证号输入有误！", taskHousing.getTaskid());
                		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
    					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
    					taskHousing.setDescription(text);
    					taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
    					taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
    					taskHousing.setLoginMessageJson(jsonObject.toString());
    					save(taskHousing);   					
					}else if(text.contains("密码输入错误")){
						tracer.addTag("密码输入错误！", taskHousing.getTaskid());
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
						taskHousing.setDescription(text);
						taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
    					taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
    					taskHousing.setLoginMessageJson(jsonObject.toString());
    					save(taskHousing);    			
					}else if(text.contains("验证码输入有误")){
						tracer.addTag("验证码输入有误！", taskHousing.getTaskid());
						errCount++;
						if(errCount>3){
							 tracer.addTag("操作失败:进行身份校验时出错:您输入的验证码与图片不符"+errCount, taskHousing.getTaskid());
							 taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhase());
							 taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_TWO.getPhasestatus());
							 taskHousing.setDescription("登录失败，图片验证码识别错误！");
							 save(taskHousing);
						}else{
							taskHousing= login(messageLoginForHousing);
						}
					}
				}else {
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

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
		tracer.addTag("housingRizhaoFutureService.getAllData", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingRizhaoInfoService.getUserInfo(messageLoginForHousing, infoParam);
		housingRizhaoInfoService.getPay(messageLoginForHousing, infoParam);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}