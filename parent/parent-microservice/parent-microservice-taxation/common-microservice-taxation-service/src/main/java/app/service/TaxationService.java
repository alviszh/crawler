package app.service;

import java.net.URL;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.taxation.json.TaxationRequestParameters;
import com.crawler.taxation.json.TaxationStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.taxation.basic.TaskTaxation;
import com.microservice.dao.repository.crawler.taxation.basic.TaskTaxationRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.taxation.basic" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.taxation.basic" })
public class TaxationService {
	public static final Logger log = LoggerFactory.getLogger(TaxationService.class);

	@Autowired
	protected TaskTaxationRepository TaskTaxationRepository;

	@Autowired
	private TracerLog tracer;

	public TaskTaxation save(TaskTaxation TaskTaxation) {
		return TaskTaxationRepository.save(TaskTaxation);
	}

	public TaskTaxation findTaskTaxation(String taskId) {
		return TaskTaxationRepository.findByTaskid(taskId);
	}

	/**
	 * @param TaxationRequestParameters
	 * @Des 更新task表（doing 正在登录状态）
	 */
	public TaskTaxation changeLoginStatusDoing(TaskTaxation taskTaxation) {
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_DOING.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_DOING.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_DOING.getPhasestatus());
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}


	public void changeLoginStatus(String phase, String phase_status, String desprition, TaskTaxation taskTaxation) {

		taskTaxation.setDescription(desprition);
		taskTaxation.setPhase(phase);
		taskTaxation.setPhase_status(phase_status);
		TaskTaxationRepository.save(taskTaxation);
	}

	public TaskTaxation changeStatus(String phase, String phase_status, String desprition,
			TaskTaxation taskTaxation) {

		taskTaxation.setDescription(desprition);
		taskTaxation.setPhase(phase);
		taskTaxation.setPhase_status(phase_status);
		TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}

	/**
	 * 更新task表（error 发生异常）
	 *
	 * @param TaskTaxation
	 * @return
	 */
	public TaskTaxation changeLoginStatusException(TaskTaxation taskTaxation) {

		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_EXCEPTION.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_EXCEPTION.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_EXCEPTION.getPhasestatus());
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}

	/**
	 * @param TaskTaxation
	 * @return
	 * @Des 更新task表（error 密码错误）
	 */
	public TaskTaxation changeLoginStatusPwdError(TaskTaxation taskTaxation) {

		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_IDNUMORPWD_ERROR.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_IDNUMORPWD_ERROR.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}


	/**
	 * @param TaskTaxation
	 * @return
	 * @Des 更新task表（登录账号与所选登录类型不符合 南京社保）
	 */
	public TaskTaxation changeLoginStatusLogintypeAndIdnumNotfit(TaskTaxation taskTaxation) {

		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_IDNUMLOGINTYPENOTFIT_ERROR.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_IDNUMLOGINTYPENOTFIT_ERROR.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_IDNUMLOGINTYPENOTFIT_ERROR.getPhasestatus());
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}

	/**
	 * @param TaskTaxation
	 * @return
	 * @Des 更新task表（error 图片验证码错误）
	 */
	public TaskTaxation changeLoginStatusCaptError(TaskTaxation taskTaxation) {

		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_CAPTCHA_ERROR.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_CAPTCHA_ERROR.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_CAPTCHA_ERROR.getPhasestatus());
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}


	/**
	 * @param TaskTaxation
	 * @param htmlPage
	 * @return
	 * @Des 更新task表（success 登陆成功,cookie入库）
	 */
	public TaskTaxation changeLoginStatusSuccess(TaskTaxation taskTaxation, HtmlPage htmlPage) {

		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhasestatus());

		String cookies = CommonUnit.transcookieToJson(htmlPage.getWebClient());
		taskTaxation.setCookies(cookies);
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}


	/**
	 * @param TaxationRequestParameters
	 * @return
	 * @Des 更新task表（doing 正在采集）
	 */
	public TaskTaxation changeCrawlerStatusDoing(TaxationRequestParameters taxationRequestParameters) {

		TaskTaxation taskTaxation = TaskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_DOING.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_DOING.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_DOING.getPhasestatus());
		taskTaxation.setCity(taxationRequestParameters.getCity());
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}

	/**
	 * @param cookies
	 * @return
	 * @Des 获取webclient (带cookie)
	 */
	public WebClient getWebClient(Set<Cookie> cookies) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		return webClient;
	}

	/**
	 * @param html
	 * @param keyword
	 * @return
	 * @Des 通过关键字获取具体的标签内容
	 */
	public String getMsgByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			return element.text();
		}
		return null;
	}

	/**
	 * @param document
	 * @param keyword
	 * @return
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 */
	public String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	/**
	 * @param TaskTaxation
	 * @param code
	 * @return
	 * @Des 更新task表（个人信息入库）
	 */
	public TaskTaxation changeCrawlerStatusUserInfo(TaskTaxation taskTaxation, Integer code) {
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_SUCCESS.getDescription());
		taskTaxation.setUserMsgStatus(code);
		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}



	/**
	 * 更新Task表状态
	 *
	 * @param TaskTaxation
	 * @return
	 */
	public TaskTaxation changeTaskTaxationStatusCrawler(TaskTaxation taskTaxation,
			TaxationStatusCode statusCode) {
		taskTaxation.setDescription(statusCode.getDescription());
		if (statusCode == TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_SUCCESS) {
			taskTaxation.setUserMsgStatus(TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_SUCCESS.getError_code());
		}
		if (statusCode == TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_ERROR) {
			taskTaxation.setUserMsgStatus(TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_ERROR.getError_code());
		}
		if (statusCode == TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_TIMEOUT) {
			taskTaxation.setAccountMsgStatus(TaxationStatusCode.TAXATION_CRAWLER_USER_MSG_TIMEOUT.getError_code());
		}
		if (statusCode == TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_SUCCESS) {
			taskTaxation.setAccountMsgStatus(TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_SUCCESS.getError_code());
		}
		if (statusCode == TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_ERROR) {
			taskTaxation.setAccountMsgStatus(TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_ERROR.getError_code());
		}
		
		if (statusCode == TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_TIMEOUT) {
			taskTaxation.setAccountMsgStatus(TaxationStatusCode.TAXATION_CRAWLER_ACCOUNT_MSG_TIMEOUT.getError_code());
		}

		if (taskTaxation.getUserMsgStatus() != null && taskTaxation.getAccountMsgStatus() != null) {
			taskTaxation.setFinished(Boolean.TRUE);
			taskTaxation.setDescription(TaxationStatusCode.TAXATION_CRAWLER_ALL_SUCCESS.getDescription());
			taskTaxation.setPhase(TaxationStatusCode.TAXATION_CRAWLER_ALL_SUCCESS.getPhase());
			taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_CRAWLER_ALL_SUCCESS.getPhasestatus());
			taskTaxation = TaskTaxationRepository.save(taskTaxation);
		}
		return taskTaxation;
	}


	public TaskTaxation changeLoginStatusSuccess(TaskTaxation taskTaxation) {

		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhasestatus());

		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}

	public TaskTaxation changeLoginStatusSuccessHttp(TaskTaxation taskTaxation, String cookie) {
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhasestatus());

		taskTaxation.setCookies(cookie);

		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}

	public TaskTaxation changeCrawlerStatusSuccessHttp(TaskTaxation taskTaxation, String cookie) {
		taskTaxation.setDescription(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getDescription());
		taskTaxation.setPhase(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhase());
		taskTaxation.setPhase_status(TaxationStatusCode.TAXATION_LOGIN_SUCCESS.getPhasestatus());

		taskTaxation.setCookies(cookie);

		taskTaxation = TaskTaxationRepository.save(taskTaxation);
		return taskTaxation;
	}

	public TaskTaxation getTaskTaxation(TaxationRequestParameters taxationRequestParameters) {
		return TaskTaxationRepository.findByTaskid(taxationRequestParameters.getTaskId());
	}

	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	/**
	 * @param finished
	 * @param taskid
	 * @return
	 * @Des 系统关闭，改变TaskTaxation状态
	 */
	public TaskTaxation systemClose(boolean finished, String taskId) {
		tracer.addTag("systemClose", taskId);
		TaskTaxation TaskTaxation = TaskTaxationRepository.findByTaskid(taskId);
		TaskTaxation.setError_code(TaxationStatusCode.SYSTEM_QUIT.getError_code());
		TaskTaxation.setError_message(TaxationStatusCode.SYSTEM_QUIT.getDescription());
		if (finished) {
			TaskTaxation.setFinished(finished);
		}
		TaskTaxation = TaskTaxationRepository.save(TaskTaxation);
		return TaskTaxation;
	}

	public TaskTaxation changeStatusException(String phase, String phasestatus, String description, Integer error_code,
			boolean finished, String taskId) {
        tracer.addTag("change task status", description);
		TaskTaxation TaskTaxation = TaskTaxationRepository.findByTaskid(taskId);
		TaskTaxation.setPhase(phase);
		TaskTaxation.setPhase_status(phasestatus);
		TaskTaxation.setDescription(description);
		TaskTaxation.setError_code(error_code);
		if(finished){
			TaskTaxation.setFinished(finished);
		}
		TaskTaxationRepository.save(TaskTaxation);
		return TaskTaxation;
	}
	
	
	
}
