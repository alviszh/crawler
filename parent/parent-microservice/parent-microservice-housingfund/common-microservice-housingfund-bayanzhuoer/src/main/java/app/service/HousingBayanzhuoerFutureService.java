package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;

import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingBayanzhuoerParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.bayanzhuoer")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.bayanzhuoer")
public class HousingBayanzhuoerFutureService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingBayanzhuoerParse housingBayanzhuoerParse;

	@Autowired
	private HousingBayanzhuoerInfoService housingBayanzhuoerInfoService;
	private  InfoParam infoParam=new InfoParam();
	private String loginType=null;
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		tracer.addTag("HousingBayanzhuoerFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		if (null != taskHousing) {		
			WebParam webParam = housingBayanzhuoerParse.login(messageLoginForHousing, taskHousing);
			if (null == webParam) {
				tracer.addTag("HousingBayanzhuoerFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				// 登录失败状态存储
				save(taskHousing);
				return taskHousing;
			   }else{
				String html = webParam.getHtml();
				String text = webParam.getText();
				tracer.addTag("HousingBayanzhuoerFutureService.login--次数:",
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
					loginType=webParam.getLoginType();
					return taskHousing;
				} else if (null != text && !"".equals(text)) {				
				    if (text.contains("请输入15位或18位身份证号")) {
                    	tracer.addTag("身份证号输入有误！", taskHousing.getTaskid());
                		taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
    					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
    					taskHousing.setDescription(text);
    					taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
    					taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
    					taskHousing.setLoginMessageJson(jsonObject.toString());
    					save(taskHousing);
    					return taskHousing;
					}else if(text.contains("密码输入错误")){
						tracer.addTag("密码输入错误！", taskHousing.getTaskid());
						taskHousing.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhasestatus());
						taskHousing.setDescription(text);
						taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
    					taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
    					taskHousing.setLoginMessageJson(jsonObject.toString());
    					save(taskHousing);
    					return taskHousing;
					}else{
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
						taskHousing.setDescription(text);
						taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
						taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
						taskHousing.setLoginMessageJson(jsonObject.toString());
						save(taskHousing);
						return taskHousing;
					}
				} else {
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());

					taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
					taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
					taskHousing.setLoginMessageJson(jsonObject.toString());
					// 登录失败状态存储
					save(taskHousing);
					return taskHousing;
				}
			}
		}

		return taskHousing;
	}
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingBayanzhuoerFutureService.getAllData", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingBayanzhuoerInfoService.getUserInfo(messageLoginForHousing, taskHousing, infoParam, loginType);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}