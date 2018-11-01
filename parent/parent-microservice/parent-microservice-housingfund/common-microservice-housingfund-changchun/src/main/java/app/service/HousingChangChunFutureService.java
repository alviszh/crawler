package app.service;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.changchun")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.changchun")
public class HousingChangChunFutureService extends HousingBasicService implements ICrawlerLogin {

	public static final Logger log = LoggerFactory.getLogger(HousingChangChunFutureService.class);

	@Autowired
	private LoginAndGetService loginAndGetService;

	@Autowired
	private HousingChangChunAsyncService housingChangChunAsyncService;

	private WebClient webClient = null;

	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		webClient = WebCrawler.getInstance().getNewWebClient();
		tracer.addTag("parser.crawler.taskid", messageLoginForHousing.getTask_id());
		tracer.addTag("parser.crawler.auth", messageLoginForHousing.getNum());
		// List<WebParamHousing> list = new WebParamHousing();
		WebParamHousing<?> webParamHousing = null;
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		System.out.println(messageLoginForHousing.toString());
		try {
			if (messageLoginForHousing.getLogintype().contains(StatusCodeLogin.getIDNUM())) {
				webParamHousing = loginAndGetService.loginByIDCard(webClient, messageLoginForHousing.getNum().trim(),
						messageLoginForHousing.getPassword().trim());
			} else if (messageLoginForHousing.getLogintype().contains(StatusCodeLogin.getACCOUNT_NUM())) {
				webParamHousing = loginAndGetService.loginByIDPersonal(webClient,
						messageLoginForHousing.getNum().trim(), messageLoginForHousing.getPassword().trim());
			} else {
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("登录方式只支持身份证 ，个人公积金号登录");

				save(taskHousing);
				return taskHousing;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGINTWO_ERROR.getDescription());

			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			tracer.addTag("parser.login.auth", e.getMessage());
			save(taskHousing);
			return taskHousing;
		}
		if (webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("验证码不正确") != -1) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

			save(taskHousing);
			return taskHousing;
		}
		if (webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("查询出错") != -1) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());

			save(taskHousing);
			return taskHousing;
		}
		if (webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("系统中不存在该身份证，请重新输入") != -1) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < webParamHousing.getPage().getWebResponse().getContentAsString().length(); i++) {
				if ((webParamHousing.getPage().getWebResponse().getContentAsString().charAt(i) + "")
						.getBytes().length > 1) {
					sb.append(webParamHousing.getPage().getWebResponse().getContentAsString().charAt(i));
				}
			}
			System.out.println(sb);
			taskHousing.setDescription(sb.toString());
			save(taskHousing);
			return taskHousing;
		}
		if (webParamHousing.getPage().getWebResponse().getContentAsString().indexOf("success") == -1) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < webParamHousing.getPage().getWebResponse().getContentAsString().length(); i++) {
				if ((webParamHousing.getPage().getWebResponse().getContentAsString().charAt(i) + "")
						.getBytes().length > 1) {
					sb.append(webParamHousing.getPage().getWebResponse().getContentAsString().charAt(i));
				}
			}
			System.out.println(sb);
			taskHousing.setDescription(sb.toString());

			save(taskHousing);
			return taskHousing;
		}
		System.out.println("登录结果====" + webParamHousing.getPage().getWebResponse().getContentAsString());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

		taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
		taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
		taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
		save(taskHousing);

		return taskHousing;

	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();

		Map<String, String> map = new HashMap<>();
		for (Cookie cookie : cookies) {
			System.out.println(cookie.toString());
			if (cookie.getName().indexOf("gjjaccnum") != -1) {
				map.put(cookie.getName(), cookie.getValue());
			}
			if (cookie.getName().indexOf("gjjcertinum") != -1) {
				map.put(cookie.getName(), cookie.getValue());
			}
		}

		try {
			housingChangChunAsyncService.getAccountInfor(webClient, messageLoginForHousing, map, taskHousing);
			housingChangChunAsyncService.getAccountDetails(webClient, messageLoginForHousing, map, taskHousing);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		taskHousing = findTaskHousing(taskHousing.getTaskid());
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		// TODO Auto-generated method stub
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}