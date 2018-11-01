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
import app.crawler.htmlparse.HousingBaojiParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.baoji")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.baoji")
public class HousingBaojiCrawlerService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private HousingBaojiParse housingBaojiParse;
	@Autowired
	private HousingBaojiInfoService housingBaojiInfoService;
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
		tracer.addTag("housingBaojiFutureService.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		WebParam webParam=new WebParam();
		webParam.setCode(0);		
		webParam = housingBaojiParse.login(messageLoginForHousing, taskHousing);
			String html = webParam.getHtml();
			String text = webParam.getText();
			tracer.addTag("housingBaojiFutureService.login--:",
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
                 if (text.contains("系统未登记该职工账号或身份证") || text.contains("输入的身份证位数不对")) {
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
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingBaojiFutureService.getAllData", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingBaojiInfoService.getUserInfo(messageLoginForHousing, infoParam);
		housingBaojiInfoService.getPay(messageLoginForHousing, infoParam);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}