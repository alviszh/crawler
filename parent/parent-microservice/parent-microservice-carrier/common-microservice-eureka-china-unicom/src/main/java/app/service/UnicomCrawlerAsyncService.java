package app.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.unicom.UnicomDetailList;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegraThemlResult;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;
import com.microservice.dao.entity.crawler.unicom.UnicomNoteResult;
import com.microservice.dao.entity.crawler.unicom.UnicomPayMsgStatusResult;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomNoteThemResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomPayMsgStatusResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomDetailListRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomIntegraThemlResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomIntegralTegralThemResultRepository;


import app.bean.UnicomHistoryThemResultRoot;
import app.bean.UnicomIntegralreturnResultRoot;
import app.bean.UnicomUserResultRoot;
import app.commontracerlog.TracerLog;
import app.crawler.unicom.htmlparse.HtmlParse;
import app.unit.GetThemUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.unicom")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.unicom")
public class UnicomCrawlerAsyncService {

	public static final String KEY = "秘钥就是不告诉你，自己猜去吧哈哈";

	public static final Logger log = LoggerFactory.getLogger(UnicomCrawlerAsyncService.class);


	@Autowired
	private UnicomNoteThemResultRepository unicomNoteThemResultRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private UnicomDetailListRepository unicomUnicomDetailListRepository;

	@Autowired
	private UnicomIntegraThemlResultRepository unicomIntegraThemlResultRepository;

	@Autowired
	private UnicomIntegralTegralThemResultRepository unicomIntegralTegralThemResultRepository;

	@Autowired
	private UnicomPayMsgStatusResultRepository unicomPayMsgStatusResultRepository;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TracerLog tracerLog;


	@Autowired
	private GetThemUnit getThemUnit;
	
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;

	// 获取积分详单
	@Async
	public Object getIntegraThem2Production(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通获取积分详单单 2", messageLogin.getTask_id());

		String html = null;
		try {
			html = getThemUnit.getIntegraThem2Production(taskMobile, 0);// 获取积分详单html
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通获取积分详单单2", html);

		if (html == null) {

			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.toString());

			return null;
		}

		UnicomIntegralreturnResultRoot rootresult = null;
		try {
			rootresult = HtmlParse.integrathem_parse2_production(html);
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.toString());

			return null;
		}

		if (rootresult.getErrorMessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			taskMobile.setError_message(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getMessage());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.toString());

			return null;
		}

		tracerLog.addTag("中国联通获取积分详单2    存储积分详单", messageLogin.getTask_id());

		// 保存前做去重处理
		List<UnicomIntegraThemlResult> result = rootresult.getUnicomIntegraThemlResultlist();

		UnicomIntegralTotalResult resultingeral = rootresult.getUnicomIntegralTegralThemResult();
		if (resultingeral != null) {
			resultingeral.setUserid(messageLogin.getUser_id());
			resultingeral.setTaskid(taskMobile.getTaskid());
			save(resultingeral);
		}

		if (result != null) {
			for (UnicomIntegraThemlResult resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				resultset.setResult(resultingeral);
				save(resultset);
			}
		}

		tracerLog.addTag("中国联通获取积分详单单2  存储结束积分详单 结束", messageLogin.getTask_id());

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());
		taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode());
		save(taskMobile);
		tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.toString());
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;

	}

	// 获取积分详单
	@Async
	public Object getIntegraThem2Total(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通获取积分详单单 2 ", messageLogin.getTask_id());

		String html = null;
		try {
			html = getThemUnit.getIntegraThem2Total(taskMobile, 0);// 获取积分详单html
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通获取积分详单单2 ", html);

		if (html == null) {

			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}

			save(taskMobile);
			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.toString());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			return null;
		}

		UnicomIntegralreturnResultRoot rootresult = null;
		try {
			rootresult = HtmlParse.integrathem_parse2_production(html);
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			taskMobile.setError_message(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getMessage());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.toString());

			return null;
		}

		if (rootresult.getErrorMessage() != null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());

			if (taskMobile.getIntegralMsgStatus() != null && taskMobile.getIntegralMsgStatus() != 200) {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取积分详单单2 error html = null", taskMobile.toString());

			return null;
		}

		tracerLog.addTag("中国联通获取积分详单2    存储积分详单 ", messageLogin.getTask_id());

		// 保存前做去重处理

		UnicomIntegralTotalResult resultingeral = rootresult.getUnicomIntegralTegralThemResult();
		if (resultingeral != null) {
			resultingeral.setUserid(messageLogin.getUser_id());
			resultingeral.setTaskid(taskMobile.getTaskid());
			save(resultingeral);
		}

		tracerLog.addTag("中国联通获取积分详单单2  存储结束积分详单 结束", messageLogin.getTask_id());

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(0).getDescription());
		taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		tracerLog.addTag("中国联通获取积分详单单2", taskMobile.toString());
		return null;

	}

	// 获取历史详单
	@Async
	public Future<String> getHistoryThem(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通抓取历史详单" + i + "月 ", messageLogin.getTask_id());
		LocalDate nowdate = LocalDate.now();

		String html = null;
		try {
			
			String month = nowdate.plusMonths(-i).getMonthValue() + "";
			if (month.length() < 2) {
				month = "0" + month;
			}
			month = nowdate.plusMonths(-i).getYear() + "" + month;
			html = getThemUnit.getHistoryThem(taskMobile, month, 0);// 获取通话详单html
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通抓取历史详单" + i + "月 ", html);

		if (html == null) {
			tracerLog.addTag("中国联通抓取历史详单" + i + "月 error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("404");
		}

		UnicomHistoryThemResultRoot<UnicomDetailList> rootresult = null;
		try {
			rootresult = HtmlParse.historythem_parse(html);
		} catch (Exception e) {
			tracerLog.addTag("中国联通抓取历史详单" + i + "月 存历史详单 ", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
			taskMobile.setError_message(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getMessage());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("404");
		}

		if (rootresult.getErrorMessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
		

			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("404");
		}
		tracerLog.addTag("中国联通抓取历史详单" + i + "月  存储历史详单", messageLogin.getTask_id());

		// 保存前做去重处理
		List<UnicomDetailList> result = rootresult.getResult();
		if (result != null) {
			for (UnicomDetailList resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				if (resultset.getDetailList() != null) {
					List<UnicomDetailList> listcon = new ArrayList<>();
					List<UnicomDetailList> list2 = resultset.getDetailList();
					for (UnicomDetailList resultlist : list2) {
						resultlist.setUserid(messageLogin.getUser_id());
						resultlist.setTaskid(taskMobile.getTaskid());

						String monththem = nowdate.plusMonths(-i).toString();
						resultlist.setMonth(monththem);

						resultlist.setDetailList2(resultset);
						listcon.add(resultlist);
					}
					resultset.setDetailList(listcon);
				}
				log.info("==================当前月份=====" + nowdate.plusMonths(-i).toString());
				String monththem = nowdate.plusMonths(-i).toString();
				resultset.setMonth(monththem);

				save(resultset);
			}
		}
		rootresult = null;
		rootresult = HtmlParse.historythem_parse2(html);

		if (rootresult.getErrorMessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());

			taskMobile.setError_message(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getMessage());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("404");
		}
		tracerLog.addTag("中国联通抓取历史详单2" + i + "月  存储历史详单", messageLogin.getTask_id());

		// 保存前做去重处理
		result = null;
		result = rootresult.getResult();
		if (result != null) {
			for (UnicomDetailList resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				if (resultset.getDetailList() != null) {
					List<UnicomDetailList> listcon = new ArrayList<>();
					List<UnicomDetailList> list2 = resultset.getDetailList();
					for (UnicomDetailList resultlist : list2) {
						resultlist.setUserid(messageLogin.getUser_id());
						resultlist.setTaskid(taskMobile.getTaskid());

						String monththem = nowdate.plusMonths(-i).toString();
						resultlist.setMonth(monththem);
						resultlist.setDetailList2(resultset);
						listcon.add(resultlist);
					}
					resultset.setDetailList(listcon);
				}
				log.info("==================当前月份=====" + nowdate.plusMonths(-i).toString());
				String monththem = nowdate.plusMonths(-i).toString();
				resultset.setMonth(monththem);
				save(resultset);
			}
		}

		tracerLog.addTag("中国联通抓取历史详单2" + i + "月  存储历史详单结束", messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CHECK_MSG(i).getDescription());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

		return new AsyncResult<String>("200");
	}

	// 获取短信详单
	@Async
	public Future<String> getNoteThem(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通抓取短信详单" + i + "月 ", messageLogin.getTask_id());
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		// 本月的最后一天
		LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		
		String html = null;
		try {
			html = getThemUnit.getNoteThem(taskMobile, firstday.toString(), lastDay.toString(), 0);
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通抓取短信详单" + i + "月 ", html);

		if (html == null) {
			tracerLog.addTag("中国联通抓取短信详单" + i + "月 error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());

			
			save(taskMobile);
			
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录", firstday.toString(),
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","未获取html".toString(), 0);
			mobileDataErrRecRepository.save(dataErrRec);

			return new AsyncResult<String>("404");
		}
		UnicomUserResultRoot<UnicomNoteResult> rootresult = null;
		try {
			rootresult = HtmlParse.notethem_parse(html);
		} catch (Exception e) {
			tracerLog.addTag("中国联通抓取短信详单" + i + "月 存储短信详单 ", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			
			taskMobile.setError_message(StatusCodeRec.CRAWLER_SMSRecordStatus_ERROR.getMessage());
			save(taskMobile);
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录", firstday.toString(),
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","获取错误html".toString(), 0);
			mobileDataErrRecRepository.save(dataErrRec);

			return new AsyncResult<String>("error");
		}

		if (rootresult.getErrorMessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
			
			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());
			save(taskMobile);
			
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "短信记录", firstday.toString(),
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE",rootresult.getErrorMessage().toString(), 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return new AsyncResult<String>("error");
		}

		tracerLog.addTag("中国联通抓取短信详单" + i + "月 存储短信详单 ", messageLogin.getTask_id());
		// 保存前做去重处理
		List<UnicomNoteResult> result = rootresult.getResult();
		List<UnicomNoteResult> list = new ArrayList<>();
		for (UnicomNoteResult resultset : result) {
			resultset.setUsernumber(taskMobile.getPhonenum());
			resultset.setUserid(messageLogin.getUser_id());
			resultset.setTaskid(taskMobile.getTaskid());
			list.add(resultset);
			save(resultset);
		}

		tracerLog.addTag("中国联通抓取短信详单" + i + "月  存储短信详单结束", messageLogin.getTask_id());

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_SMS_MSG(i).getDescription());
		
		tracerLog.addTag("中国联通抓取短信详单" + i + "月  存储短信详单结束", taskMobile.toString());
		save(taskMobile);
		return new AsyncResult<String>("200");

	}

	// 获取积分详单
	@Async
	public Future<String> getIntegraThem(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通获取积分详单单" + i + "月", messageLogin.getTask_id());

		String html = null;
		try {
			html = getThemUnit.getIntegraThem(taskMobile, i, 0);// 获取积分详单html
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通获取积分详单单" + i + "月  ", html);

		if (html == null) {

			tracerLog.addTag("中国联通获取积分详单单" + i + "月 error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			return new AsyncResult<String>("404");
		}

		UnicomIntegralreturnResultRoot rootresult = null;
		try {
			rootresult = HtmlParse.integrathem_parse(html);
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());

			taskMobile.setError_message(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getMessage());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			return new AsyncResult<String>("404");
		}

		if (rootresult.getErrorMessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());

			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());

			return new AsyncResult<String>("404");
		}

		tracerLog.addTag("中国联通获取积分详单单" + i + "月   存储积分详单", messageLogin.getTask_id());

		// 保存前做去重处理
		List<UnicomIntegraThemlResult> result = rootresult.getUnicomIntegraThemlResultlist();

		UnicomIntegralTotalResult resultingeral = rootresult.getUnicomIntegralTegralThemResult();
		if (resultingeral != null) {
			resultingeral.setUserid(messageLogin.getUser_id());
			resultingeral.setTaskid(taskMobile.getTaskid());
			save(resultingeral);
		}

		if (result != null) {
			for (UnicomIntegraThemlResult resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				resultset.setResult(resultingeral);
				save(resultset);
			}
		}

		tracerLog.addTag("中国联通获取积分详单单" + i + "月  存储结束积分详单 结束", messageLogin.getTask_id());

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
		save(taskMobile);
		tracerLog.addTag("中国联通获取积分详单单" + i + "月  存储结束积分详单 结束", taskMobile.toString());

		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return new AsyncResult<String>("200");

	}


	// 获取缴费详单
	@Async
	public Future<String> getPayMsgStatusThem(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通获取缴费详单" + i + "月 ", messageLogin.getTask_id());

		String html = null;
		try {
			html = getThemUnit.getPayMsgStatusThem(taskMobile, i, 0);// 获取通话详单html
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通获取缴费详单" + i + "月  html ", html);

		if (html == null) {

			tracerLog.addTag("中国联通获取缴费详单" + i + "月  error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getDescription());

			save(taskMobile);
			tracerLog.addTag("中国联通获取缴费详单" + i + "月  error html = null", taskMobile.toString());

			log.info("=====================taskMobile " + taskMobile.toString());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("200");

		}

		UnicomUserResultRoot<UnicomPayMsgStatusResult> rootresult = null;
		try {
			rootresult = HtmlParse.paymsgstatus_parse(html);
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getDescription());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("200");

		}

		if (rootresult.getErrorMessage() != null) {
			tracerLog.addTag("crawler error", rootresult.getErrorMessage().toString());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getDescription());

			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("200");

		}

		tracerLog.addTag("中国联通获取缴费详单" + i + "月  存储缴费详单", messageLogin.getTask_id());
		// 保存前做去重处理
		List<UnicomPayMsgStatusResult> result = rootresult.getResult();
		List<UnicomPayMsgStatusResult> list = new ArrayList<>();
		if (result != null) {
			for (UnicomPayMsgStatusResult resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				list.add(resultset);
				save(resultset);
			}
		}

		tracerLog.addTag("中国联通获取缴费详单" + i + "月  存储缴费详单结束", messageLogin.getTask_id());

		// 爬取成功状态更新
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(i).getDescription());
		save(taskMobile);
		tracerLog.addTag("中国联通获取缴费详单" + i + "月 ", taskMobile.toString());

		return new AsyncResult<String>("200");

	}

	
	private void save(UnicomNoteResult resultset) {
		unicomNoteThemResultRepository.save(resultset);
	}

	private void save(UnicomDetailList resultset) {
		unicomUnicomDetailListRepository.save(resultset);
	}

	private void save(UnicomIntegraThemlResult resultset) {
		unicomIntegraThemlResultRepository.save(resultset);
	}

	private void save(UnicomIntegralTotalResult resultset) {
		unicomIntegralTegralThemResultRepository.save(resultset);
	}

	private void save(UnicomPayMsgStatusResult resultset) {
		unicomPayMsgStatusResultRepository.save(resultset);
	}
	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	private void save(TaskMobile taskMobile) {
		taskMobileRepository.saveAndFlush(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}
	
	

}