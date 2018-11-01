package app.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingXiNingParse;
import app.service.common.HousingBasicService;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xining")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xining")
public class HousingXiNingCrawlerService extends HousingBasicService {
	@Autowired
	private HousingXiNingParse housingXiNingParse;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing, Integer count)
			throws Exception {
		tracer.addTag("HousingXiNingFutureService.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		taskHousing.setLoginMessageJson(jsonObject.toString());
		save(taskHousing);
		try {
			if (null != taskHousing) {
				WebParam webParam = housingXiNingParse.login(messageLoginForHousing, taskHousing);
				if (null == webParam) {
					tracer.addTag("HousingXiNingFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
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
					tracer.addTag("HousingXiNingFutureService.login--次数:" + count,
							messageLoginForHousing.getTask_id() + html);
					if (!html.contains("个人网厅登录页")) {
						taskHousing.setCookies(webParam.getCookies());
						taskHousing.setLoginMessageJson(jsonObject.toString());
						
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
						taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
						taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
						taskHousing.setError_message(StatusCodeRec.MOBILE_LOGIN_SUCCESS.getMessage());
						save(taskHousing);										
					}else {
						Document doc = Jsoup.parse(html);
						String username_tip = doc.getElementById("username_tip").text();
						tracer.addTag("HousingXiNingFutureService.username_tip:" + count,
								messageLoginForHousing.getTask_id() + "---"+username_tip);
						
						String pwd_tip = doc.getElementById("pwd_tip").text();
						tracer.addTag("HousingXiNingFutureService.pwd_tip:" + count,
								messageLoginForHousing.getTask_id() + "---"+pwd_tip);

						String yzm_tip = doc.getElementById("yzm_tip").text();
						tracer.addTag("HousingXiNingFutureService.yzm_tip:" + count,
								messageLoginForHousing.getTask_id() + "---"+yzm_tip);
						if (username_tip.contains("用户名无效")) {

							taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription(username_tip);

							taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
							taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
							taskHousing.setLoginMessageJson(jsonObject.toString());
							save(taskHousing);
						}else if (pwd_tip.contains("个人密码错误")) {

							taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription(pwd_tip);

							taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
							taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
							taskHousing.setLoginMessageJson(jsonObject.toString());
							save(taskHousing);
						}else if (yzm_tip.contains("验证码错误")) {
							tracer.addTag("HousingXiNingFutureService.login--失败次数:" + count,
									messageLoginForHousing.getTask_id());
							if (count < 4) {
								login(messageLoginForHousing, ++count);
							} else {
								taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
								taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
								taskHousing.setDescription(yzm_tip);

								taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
								taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
								taskHousing.setLoginMessageJson(jsonObject.toString());
								// 登录失败状态存储
								save(taskHousing);								
							}
						}else if (pwd_tip.contains("个人密码错误")) {

							taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription(pwd_tip);

							taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
							taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
							taskHousing.setLoginMessageJson(jsonObject.toString());
							save(taskHousing);						
						}else {
							taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
							taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
							taskHousing.setDescription("用户名格式错误或密码格式不正确");
							taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
							taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
							taskHousing.setLoginMessageJson(jsonObject.toString());
							// 登录失败状态存储
							save(taskHousing);					
						}
					}
				}
			}
		} catch (Exception e) {
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("用户名格式错误或密码格式不正确");
			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			taskHousing.setLoginMessageJson(jsonObject.toString());
			// 登录失败状态存储
			save(taskHousing);
		}
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
}