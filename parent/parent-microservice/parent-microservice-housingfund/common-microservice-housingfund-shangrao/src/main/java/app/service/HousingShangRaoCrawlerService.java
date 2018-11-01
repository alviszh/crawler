package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;
import app.crawler.htmlparser.HousingShangRaoParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shangrao")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shangrao")
public class HousingShangRaoCrawlerService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingShangRaoParse housingShangRaoParse;
	@Autowired
	private HousingShangRaoInfoService housingShangRaoInfoService;
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
		tracer.addTag("HousingShangRaoFutureService.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		try {			
			webParam = housingShangRaoParse.login(messageLoginForHousing);
			String html = webParam.getHtml();
			String alertMsg = webParam.getAlertMsg();
			tracer.addTag("HousingShangRaoFutureService.login :",
					messageLoginForHousing.getTask_id() + "html---------" + html);
			if (null != webParam.getInfoParam() && null != webParam.getInfoParam().getZgxm()
					&& !"".equals(webParam.getInfoParam().getZgxm()) && (!html.contains("密码输入错误"))) {
				webParam.setCode(1);
				String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookies);
				taskHousing.setLoginMessageJson(jsonObject.toString());
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());
				save(taskHousing);
				infoParam=webParam.getInfoParam();
				return taskHousing;
			} else if (null != alertMsg && !"".equals(alertMsg)) {
				tracer.addTag(alertMsg, taskHousing.getTaskid());
				taskHousing.setPhase(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.MESSAGE_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(alertMsg);
				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				save(taskHousing);
				return taskHousing;
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
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingShangRaoFutureService.login.error", e.toString());
			tracer.addTag("HousingShangRaoFutureService.login.fail3", "登录异常");
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录异常，请您稍后重试。");
			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
			save(taskHousing);
			return taskHousing;
		}
 }
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingShangRaoFutureService.getAllData", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
	    housingShangRaoInfoService.getUserInfo(messageLoginForHousing, infoParam);
	    housingShangRaoInfoService.getPay(messageLoginForHousing, infoParam);
	    taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}