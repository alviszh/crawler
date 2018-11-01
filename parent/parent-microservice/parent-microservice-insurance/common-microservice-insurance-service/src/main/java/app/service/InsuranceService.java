package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.impl.InsuranceCrawlerImpl;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic" })
public class InsuranceService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceService.class);

	@Autowired
	protected TaskInsuranceRepository taskInsuranceRepository;

	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceCrawlerImpl insuranceCrawlerImpl;

	public TaskInsurance save(TaskInsurance taskInsurance) {
		return taskInsuranceRepository.save(taskInsurance);
	}

	public TaskInsurance findTaskInsurance(String taskId) {
		return taskInsuranceRepository.findByTaskid(taskId);
	}

	/**
	 * @param insuranceRequestParameters
	 * @Des 更新task表（doing 正在登录状态）
	 */
	public TaskInsurance changeLoginStatusDoing(TaskInsurance taskInsurance) {
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @Des 更新task表（error 链接超时）
	 */
	public TaskInsurance changeLoginStatusTimeOut(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	public void changeLoginStatus(String phase, String phase_status, String desprition, TaskInsurance taskInsurance) {

		taskInsurance.setDescription(desprition);
		taskInsurance.setPhase(phase);
		taskInsurance.setPhase_status(phase_status);
		taskInsuranceRepository.save(taskInsurance);
	}

	public TaskInsurance changeStatus(String phase, String phase_status, String desprition,
			TaskInsurance taskInsurance) {

		taskInsurance.setDescription(desprition);
		taskInsurance.setPhase(phase);
		taskInsurance.setPhase_status(phase_status);
		taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * 更新task表（error 发生异常）
	 *
	 * @param taskInsurance
	 * @return
	 */
	public TaskInsurance changeLoginStatusException(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表（error 密码错误）
	 */
	public TaskInsurance changeLoginStatusPwdError(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表（error 账号或密码错误）
	 */
	public TaskInsurance changeLoginStatusIdnumOrPwdError(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表（登录账号与所选登录类型不符合 南京社保）
	 */
	public TaskInsurance changeLoginStatusLogintypeAndIdnumNotfit(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMLOGINTYPENOTFIT_ERROR.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMLOGINTYPENOTFIT_ERROR.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMLOGINTYPENOTFIT_ERROR.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表（error 图片验证码错误）
	 */
	public TaskInsurance changeLoginStatusCaptError(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表（error 用户名错误）
	 */
	public TaskInsurance changeLoginStatusIdnumError(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表（error 账号错误或者尚未制卡）
	 */
	public TaskInsurance changeLoginStatusIdnumNotExistError(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTEXIST.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTEXIST.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTEXIST.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表(error 该账号无数据)
	 */
	public TaskInsurance changeLoginStatusNoData(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTDATA.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTDATA.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUM_NOTDATA.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @return
	 * @Des 更新task表(error 身份证号或名字输错)
	 */
	public TaskInsurance changeLoginIdcadOrPwdError(TaskInsurance taskInsurance) {

		// taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDCADORPWD_ERROR.getDescription());
		// taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDCADORPWD_ERROR.getPhase());
		// taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDCADORPWD_ERROR.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param taskInsurance
	 * @param htmlPage
	 * @return
	 * @Des 更新task表（success 登陆成功,cookie入库）
	 */
	public TaskInsurance changeLoginStatusSuccess(TaskInsurance taskInsurance, HtmlPage htmlPage) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

		String cookies = CommonUnit.transcookieToJson(htmlPage.getWebClient());
		taskInsurance.setCookies(cookies);
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * 郑州社保
	 *
	 * @param taskInsurance
	 * @param htmlPage
	 * @param token
	 * @return
	 */
	public TaskInsurance changeLoginStatusSuccess(TaskInsurance taskInsurance, HtmlPage htmlPage, String token) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

		String cookies = CommonUnit.transcookieToJson(htmlPage.getWebClient());
		taskInsurance.setCookies(cookies);
		taskInsurance.setPid(token);

		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param insuranceRequestParameters
	 * @return
	 * @Des 更新task表（doing 正在采集）
	 */
	public TaskInsurance changeCrawlerStatusDoing(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhasestatus());
		taskInsurance.setCity(insuranceRequestParameters.getCity());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
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
	 * @param taskInsurance
	 * @param code
	 * @return
	 * @Des 更新task表（个人信息入库）
	 */
	public TaskInsurance changeCrawlerStatusUserInfo(TaskInsurance taskInsurance, Integer code) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
		// taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
		// taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
		taskInsurance.setUserInfoStatus(code);
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @param description
	 * @param phase
	 * @param phaseStatus
	 * @param code
	 * @param taskInsurance
	 * @return
	 * @return
	 * @Des 改变taskInsurance表状态
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)//这种事务隔离级别可以防止脏读，不可重复读。但是可能出现幻像读。
	public TaskInsurance changeCrawlerStatus(String description, String phase, Integer code,
			TaskInsurance taskInsurance) {

		TaskInsurance changeTaskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
		tracer.addTag("changeCrawlerStatus.begin", changeTaskInsurance.toString());
			
		changeTaskInsurance.setDescription(description);
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase())) {
			changeTaskInsurance.setYanglaoStatus(code);
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase())) {
			changeTaskInsurance.setShiyeStatus(code);
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase())) {
			changeTaskInsurance.setShengyuStatus(code);
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase())) {
			changeTaskInsurance.setYiliaoStatus(code);
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase())) {
			changeTaskInsurance.setGongshangStatus(code);
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase())) {
			changeTaskInsurance.setUserInfoStatus(code);
		}

		changeTaskInsurance = taskInsuranceRepository.save(changeTaskInsurance);

		
		System.out.println("changeTaskInsurance-----"+changeTaskInsurance.toString()); 
		tracer.addTag("changeCrawlerStatus.end", changeTaskInsurance.toString());
		
		
		return taskInsurance;
	}

	/**
	 * @param description
	 * @param phase
	 * @param phaseStatus
	 * @param code
	 * @param taskInsurance
	 * @return
	 * @return
	 * @Des 改变taskInsurance表状态
	 */
	 /*
	public TaskInsurance changeCrawlerStatus(String description, String phase, Integer code,
			TaskInsurance taskInsurance) {

		taskInsurance.setDescription(description);

		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase())) {
			taskInsuranceRepository.updatePensionStatusByTaskid(description, code, taskInsurance.getTaskid());
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase())) {
			taskInsuranceRepository.updateShiYeStatusByTaskid(description, code, taskInsurance.getTaskid());
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase())) {
			taskInsuranceRepository.updateShengyuStatusByTaskid(description, code, taskInsurance.getTaskid());
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase())) {
			taskInsuranceRepository.updateYiLiaoStatusByTaskid(description, code, taskInsurance.getTaskid());
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase())) {
			taskInsuranceRepository.updateGongshangStatusByTaskid(description, code, taskInsurance.getTaskid());
		}
		if (phase.equals(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase())) {
			taskInsuranceRepository.updateUserInfoStautsByTaskid(description, code, taskInsurance.getTaskid());
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());

		System.out.println("taskInsurance---------"+taskInsurance.toString());
		return taskInsurance;
	}
*/
	/**
	 * @param taskInsurance
	 * @Des 更新task表（爬取是否完成）
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)//这种事务隔离级别可以防止脏读，不可重复读。但是可能出现幻像读。
	public TaskInsurance changeCrawlerStatusSuccess(String taskid) {

		tracer.addTag("changeCrawlerStatusSuccess start", taskid);

		TaskInsurance changeTaskInsurance = taskInsuranceRepository.findByTaskid(taskid);

		tracer.addTag("taskInsurance", changeTaskInsurance.toString());

		if (null != changeTaskInsurance.getGongshangStatus() && null != changeTaskInsurance.getShengyuStatus()
				&& null != changeTaskInsurance.getShiyeStatus() && null != changeTaskInsurance.getYiliaoStatus()
				&& null != changeTaskInsurance.getYanglaoStatus() && null != changeTaskInsurance.getUserInfoStatus()) {

			changeTaskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
			changeTaskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
			changeTaskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
			changeTaskInsurance.setFinished(true);
			changeTaskInsurance = taskInsuranceRepository.save(changeTaskInsurance);

			// add by yanglei 2018.7.24
			insuranceCrawlerImpl.getAllDataDone(changeTaskInsurance.getTaskid());
			tracer.addTag("changeCrawlerStatusSuccess success", changeTaskInsurance.getTaskid());
		} else {
			tracer.addTag("changeCrawlerStatusSuccess fail", changeTaskInsurance.toString());
		} 

		return changeTaskInsurance;
	}

	/**
	 * 更新Task表状态
	 *
	 * @param taskInsurance
	 * @return
	 */
	public TaskInsurance changeTaskInsuranceStatus4Crawler(TaskInsurance taskInsurance,
			InsuranceStatusCode statusCode) {
		taskInsurance.setDescription(statusCode.getDescription());
		// 基本情况 解析完成
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS) {
			taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS.getError_code());
		}
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE) {
			taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());
		}
		// 养老保险 解析完成
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS) {
			taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS.getError_code());
		}
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE) {
			taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_AGED_FAILUE.getError_code());
		}
		// 医疗保险 解析完成
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS) {
			taskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS.getError_code());
		}
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE) {
			taskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_FAILUE.getError_code());
		}
		// 工伤保险 解析完成
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS) {
			taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS.getError_code());
		}
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE) {
			taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_INJURY_FAILUE.getError_code());
		}
		// 失业保险 解析完成
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS) {
			taskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS.getError_code());
		}
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE) {
			taskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_FAILUE.getError_code());
		}
		// 生育保险 解析完成&不存在
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS) {
			taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS.getError_code());
		}
		if (statusCode == InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE) {
			taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_FAILUE.getError_code());
		}
		if (statusCode == InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_NOT_FOUND) {
			taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_NOT_FOUND.getError_code());
		}

		// 所有保险均解析完毕后,标记为完成
		if (taskInsurance.getUserInfoStatus() != null && taskInsurance.getYanglaoStatus() != null
				&& taskInsurance.getYiliaoStatus() != null && taskInsurance.getGongshangStatus() != null
				&& taskInsurance.getShiyeStatus() != null && taskInsurance.getShengyuStatus() != null) {
			taskInsurance.setFinished(Boolean.TRUE);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			// add by yanglei 2018.7.24
			insuranceCrawlerImpl.getAllDataDone(taskInsurance.getTaskid());
		}
		return taskInsurance;
	}

	/**
	 * 
	 * 项目名称：common-microservice-insurance-service 所属包名：app.service 类描述： 修改状态为成功
	 * 创建人：hyx 创建时间：2018年3月13日
	 * 
	 * @version 1 返回值 void
	 * @return
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ)//这种事务隔离级别可以防止脏读，不可重复读。但是可能出现幻像读。
	public TaskInsurance changeCrawlerStatusSuccess(TaskInsurance taskInsurance) {

		tracer.addTag("changeCrawlerStatusSuccess start", taskInsurance.getTaskid());

		TaskInsurance changeTaskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());		
		// 养老保险 解析完成
		changeTaskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_AGED_SUCCESS.getError_code());
		// 医疗保险 解析完成
		changeTaskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_MEDICAL_SUCCESS.getError_code());
		// 工伤保险 解析完成
		changeTaskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_INJURY_SUCCESS.getError_code());
		// 失业保险 解析完成
		changeTaskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_UNEMPLOYMENT_SUCCESS.getError_code());
		// 生育保险 解析完成&不存在
		changeTaskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_MATERNITY_SUCCESS.getError_code());

		changeTaskInsurance.setFinished(Boolean.TRUE);
		changeTaskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getDescription());
		changeTaskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhase());
		changeTaskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
		changeTaskInsurance = taskInsuranceRepository.save(changeTaskInsurance);
		// add by yanglei 2018.7.24
		insuranceCrawlerImpl.getAllDataDone(changeTaskInsurance.getTaskid());
		return changeTaskInsurance;
	}

	/**
	 * 
	 * 项目名称：common-microservice-insurance-service 所属包名：app.service 类描述： 修改状态为成功
	 * 创建人：hyx 创建时间：2018年3月13日
	 * 
	 * @version 1 返回值 void
	 * @return
	 */
	public TaskInsurance changeCrawlerStatusError(TaskInsurance taskInsurance) {

		tracer.addTag("changeCrawlerStatusSuccess start", taskInsurance.getTaskid());

		// 养老保险 解析完成
		taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());
		// 医疗保险 解析完成
		taskInsurance.setYiliaoStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());
		// 工伤保险 解析完成
		taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());
		// 失业保险 解析完成
		taskInsurance.setShiyeStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());
		// 生育保险 解析完成&不存在
		taskInsurance.setShengyuStatus(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getError_code());

		taskInsurance.setFinished(Boolean.TRUE);
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE.getPhasestatus());
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		// add by yanglei 2018.7.24
		insuranceCrawlerImpl.getAllDataDone(taskInsurance.getTaskid());
		return taskInsurance;
	}

	/**
	 * @param insuranceRequestParameters
	 * @return
	 * @Des 判断当前状态是否爬取完毕
	 */
	public boolean isDoing(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		if (null == taskInsurance) {
			return true;
		}
		if (taskInsurance.getPhase().equals("CRAWLER") && taskInsurance.getPhase_status().equals("DOING")) {
			return true;
		}
		return false;

	}

	public TaskInsurance changeLoginStatusSuccess(TaskInsurance taskInsurance) {

		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	public TaskInsurance changeLoginStatusSuccessHttp(TaskInsurance taskInsurance, String cookie) {
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

		taskInsurance.setCookies(cookie);

		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	public TaskInsurance changeCrawlerStatusSuccessHttp(TaskInsurance taskInsurance, String cookie) {
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

		taskInsurance.setCookies(cookie);

		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		return taskInsurance;
	}

	public TaskInsurance getTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		return taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
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
	 * @Des 系统关闭，改变taskInsurance状态
	 */
	public TaskInsurance systemClose(boolean finished, String taskId) {
		tracer.addTag("systemClose", taskId);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskId);
		taskInsurance.setError_code(InsuranceStatusCode.SYSTEM_QUIT.getError_code());
		taskInsurance.setError_message(InsuranceStatusCode.SYSTEM_QUIT.getDescription());
		if (finished) {
			taskInsurance.setFinished(finished);
		}
		taskInsurance = taskInsuranceRepository.save(taskInsurance);
		// add by yanglei 2018.7.24
		insuranceCrawlerImpl.getAllDataDone(taskInsurance.getTaskid());
		return taskInsurance;
	}

	public TaskInsurance changeStatusException(String phase, String phasestatus, String description, Integer error_code,
			boolean finished, String taskId) {
        tracer.addTag("change task status", description);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskId);
		taskInsurance.setPhase(phase);
		taskInsurance.setPhase_status(phasestatus);
		taskInsurance.setDescription(description);
		taskInsurance.setError_code(error_code);
		if(finished){
			taskInsurance.setFinished(finished);
		}
		taskInsuranceRepository.save(taskInsurance);
		// add by yanglei 2018.7.24
		insuranceCrawlerImpl.getAllDataDone(taskInsurance.getTaskid());
		return taskInsurance;
	}
	
	
	/**
	 * sln (勿删此方法！！！)
	 * 
	 * 指定图片验证码保存的路径和随机生成的名称，拼接在一起	
	 * 利用IO流保存验证码成功后，将完整路径信息一并返回
	 * 
	 * 此方法针对部分网站加载登录页面时，图片验证码刷新不出来的情况，或者是部分网站的验证码
	 * 用元素定位方式，发现图片的src为""，无法获取，故需要用请求来获取
	 * 
	 * @param page
	 * @param imagePath
	 * @return
	 * @throws Exception
	 */
	
	public  String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////
		
		String imgagePath = codeImageFile.getAbsolutePath(); 
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
		if (inputStream != null && outputStream != null) {  
	        int temp = 0;  
	        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
	        	outputStream.write(temp);   // 边读边写  
	        }  
	        outputStream.close();  
	        inputStream.close();   // 关闭输入输出流  
	    }
		return imgagePath;
	}
}
