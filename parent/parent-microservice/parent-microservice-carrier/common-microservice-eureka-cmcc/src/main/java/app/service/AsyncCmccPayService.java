package app.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.cmcc.domain.json.WebCmccParam;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.cmcc.CmccSMSMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserCallResult;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.cmcc.CmccPayMsgResultRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccSMSMsgResultRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccUserCallResultRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.parser.CmccCrawlerParser;
import app.unit.CmccUnit;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.cmcc", "com.microservice.dao.entity.crawler.mobile" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.cmcc",
		"com.microservice.dao.repository.crawler.mobile" })
public class AsyncCmccPayService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmccPayMsgResultRepository cmccPayMsgResultRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private CmccCrawlerParser crawlerParser;
	@Autowired
	private CmccUserCallResultRepository cmccUserCallResultRepository;
	@Autowired
	private CmccSMSMsgResultRepository cmccSMSMsgResultRepository;
	@Autowired
	private AsyncCmccRemedyService asyncCmccRemedyService;

	/**
	 * @Description: 获取缴费信息（一年）
	 * @param messageLogin
	 */
	@Async
	public void getPayMsg(MessageLogin messageLogin) {
		tracer.addTag("EurekaCmccController getPayMsg", messageLogin.getTask_id());

		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		Set<Cookie> cookies = CmccUnit.transferJsonToSet(taskMobile);
		WebCmccParam webCmccParam = crawlerParser.getPayMsg(messageLogin, cookies);

//		if (null != webCmccParam && null != webCmccParam.getCode() && 403 == webCmccParam.getCode()) {
//			taskMobile.setError_code(403);
//			taskMobile.setPayMsgStatus(201);
//			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhase());
//			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getPhasestatus());
//			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_INVALID.getDescription());
//			taskMobileRepository.save(taskMobile);
//
//			List<CmccUserCallResult> cmccUserCallResults = cmccUserCallResultRepository
//					.findByTaskId(messageLogin.getTask_id());
//			List<CmccSMSMsgResult> cmccSMSMsgResults = cmccSMSMsgResultRepository
//					.findByTaskId(messageLogin.getTask_id());
//
//			tracer.addTag("通话记录入库条数：", cmccUserCallResults.size() + "");
//			tracer.addTag("短信信息入库条数：", cmccSMSMsgResults.size() + "");
//			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
//			// 进行补救
//			// 暂时先不补救 by meidi 20180511
//			// asyncCmccRemedyService.getRemedy(messageLogin.getTask_id());
//		}

		if (null != webCmccParam && null != webCmccParam.getCmccPayMsgResults()) {
			cmccPayMsgResultRepository.saveAll(webCmccParam.getCmccPayMsgResults());
			tracer.addTag("getPayMsg", "缴费记录入库");

			taskMobile.setPayMsgStatus(webCmccParam.getPayMsgStatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);

			List<CmccUserCallResult> cmccUserCallResults = cmccUserCallResultRepository
					.findByTaskId(messageLogin.getTask_id());
			List<CmccSMSMsgResult> cmccSMSMsgResults = cmccSMSMsgResultRepository
					.findByTaskId(messageLogin.getTask_id());

			tracer.addTag("通话记录入库条数：", cmccUserCallResults.size() + "");
			tracer.addTag("短信信息入库条数：", cmccSMSMsgResults.size() + "");
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			// 进行补救
			// 暂时先不补救 by meidi 20180511
			// asyncCmccRemedyService.getRemedy(messageLogin.getTask_id());
		} else {
			taskMobile.setPayMsgStatus(404);
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription());
			taskMobileRepository.save(taskMobile);

			List<CmccUserCallResult> cmccUserCallResults = cmccUserCallResultRepository
					.findByTaskId(messageLogin.getTask_id());
			List<CmccSMSMsgResult> cmccSMSMsgResults = cmccSMSMsgResultRepository
					.findByTaskId(messageLogin.getTask_id());

			tracer.addTag("通话记录入库条数：", cmccUserCallResults.size() + "");
			tracer.addTag("短信信息入库条数：", cmccSMSMsgResults.size() + "");
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			// 进行补救
			// 暂时先不补救 by meidi 20180511
			// asyncCmccRemedyService.getRemedy(messageLogin.getTask_id());
		}
	}

}
