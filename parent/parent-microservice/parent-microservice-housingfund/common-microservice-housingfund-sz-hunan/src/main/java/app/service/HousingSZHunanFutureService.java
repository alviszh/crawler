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

import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingSZHunanParse;
import app.service.common.HousingBasicService;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.sz.hunan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.sz.hunan")
public class HousingSZHunanFutureService extends HousingBasicService {

	@Autowired
	private HousingSZHunanParse housingSZHunanParse;

	@Autowired
	private HousingSZHunanInfoService housingSZHunanInfoService;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, Integer count)
			throws Exception {

		tracer.addTag("HousingSZHunanFutureService.login", messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		if (null != taskHousing) {
			WebParam webParam = housingSZHunanParse.login(messageLoginForHousing, taskHousing);

			if (null == webParam) {
				tracer.addTag("HousingSZHunanFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				// 登录失败状态存储
				save(taskHousing);

				return taskHousing;
			} else {
				String html = webParam.getHtml();
				String text = webParam.getText();
				tracer.addTag("HousingSZHunanFutureService.login--次数:" + count,
						messageLoginForHousing.getTask_id() + "html---------" + html + "text---------" + text);
				if (null != webParam.getInfoParam() && null != webParam.getInfoParam().getZgzh()
						&& !"".equals(webParam.getInfoParam().getZgzh())) {
					taskHousing.setCookies(webParam.getCookies());
					taskHousing.setLoginMessageJson(jsonObject.toString());

					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_DONING.getError_code());

					save(taskHousing);

					housingSZHunanInfoService.getUserInfo(taskHousing, webParam.getInfoParam());
					housingSZHunanInfoService.getPay(messageLoginForHousing, taskHousing, webParam.getInfoParam());

					return taskHousing;
				} else if (null != text && !"".equals(text)) {

					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(text);

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
			}
		}

		return null;
	}

}