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

import com.crawler.htmlparser.EncryptAndDecryptUtil;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeRec;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.unicom.UnicomBalance;
import com.microservice.dao.entity.crawler.unicom.UnicomCallResult;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegraThemlResult;
import com.microservice.dao.entity.crawler.unicom.UnicomIntegralTotalResult;
import com.microservice.dao.entity.crawler.unicom.UnicomUserActivityInfo;
import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomBalanceThemResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomCallThemResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomIntegraThemlResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomIntegralTegralThemResultRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomUserActivityInfoRepository;
import com.microservice.dao.repository.crawler.unicom.UnicomUserInfoRepository;

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
public class UnicomCrawlerService {

	public static final String KEY = "秘钥就是不告诉你，自己猜去吧哈哈";

	public static final Logger log = LoggerFactory.getLogger(UnicomCrawlerService.class);
	@Autowired
	private UnicomUserInfoRepository unicomUserInfoRepository;

	@Autowired
	private UnicomCallThemResultRepository unicomCallThemResultRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;

	@Autowired
	private UnicomIntegraThemlResultRepository unicomIntegraThemlResultRepository;

	@Autowired
	private UnicomIntegralTegralThemResultRepository unicomIntegralTegralThemResultRepository;

	@Autowired
	private UnicomUserActivityInfoRepository unicomUserActivityInfoRepository;

	@Autowired
	private UnicomBalanceThemResultRepository unicomBalanceThemResultRepository;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private TracerLog tracerLog;


	@Autowired
	private GetThemUnit getThemUnit;
	
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	

	@Async
	public Future<String> getCallThemhtml(MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通抓取 通话详单" + i + "月 ", messageLogin.getTask_id());
		String html = null;
		
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate firstday = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		// 本月的最后一天
		LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		String month = firstday.getMonthValue()+"";
		if(month.length()<2){
			month = "0"+month;
		}
		
		String error_date = firstday.getYear()+month;
		try {
			
			html = getThemUnit.getCallThemhtml(taskMobile, firstday.toString().replaceAll("-", ""), lastDay.toString().replaceAll("-", ""), 0);
		} catch (Exception e) {

			e.printStackTrace();
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通抓取 通话详单" + i + "月", html);

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			tracerLog.addTag("中国联通抓取 通话详单" + i + "月 error html = null", taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());

			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			
			
			
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","未获取html", 0);
			mobileDataErrRecRepository.save(dataErrRec);
			
			return new AsyncResult<String>("error");
		}

		UnicomUserResultRoot<UnicomCallResult> rootresult = null;
		try {
			rootresult = HtmlParse.callthem_parse(html);
		} catch (Exception e) {
			tracerLog.addTag("中国联通抓取通话详单" + i + "月 存储通话详单 ", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			taskMobile.setError_message(StatusCodeRec.CRAWLER_CallRecordStatus_ERROR.getMessage());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","未获取html", 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return new AsyncResult<String>("error");
		}

		if (rootresult.getErrorMessage() != null) {
			tracerLog.addTag("中国联通抓取 通话详单" + i + "月 为空", messageLogin.getTask_id());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE",rootresult.getErrorMessage().toString(), 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return new AsyncResult<String>("error");
		}

		tracerLog.addTag("中国联通抓取 通话详单" + i + "月  开始存储", messageLogin.getTask_id());

		// 保存前做去重处理
		List<UnicomCallResult> result = rootresult.getResult();
		if (result != null) {
			for (UnicomCallResult resultset : result) {
				resultset.setUsernumber(messageLogin.getName());
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
			tracerLog.addTag("中国联通抓取 通话详单" + i + "月  存储结束", messageLogin.getTask_id());

			// 爬取成功状态更新
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);

			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("200");
		} else {
			// 爬取成功状态更新
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_CALL_MSG(i).getDescription());
			save(taskMobile);

			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return new AsyncResult<String>("404");

		}

	}

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

	// 获取用户信息详单
	public Object getUserInfoThem(MessageLogin messageLogin, TaskMobile taskMobile) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracerLog.addTag("中国联通获取用户信息详单月 ", messageLogin.getTask_id());

		String html = null;
		try {
			html = getThemUnit.getUserinfoThem(taskMobile, 0);// 获取通话详单html
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}

		tracerLog.addTag("中国联通获取用户信息详单月 ", html);

		if (html == null) {

			tracerLog.addTag("中国联通获取用户信息详单月   error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());
			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode());
			save(taskMobile);
			tracerLog.addTag("taskMobile ", taskMobile.toString());
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取用户信息详单月   error html = null", taskMobile.toString());

			return new AsyncResult<String>("200");

		}

		UnicomUserResultRoot<UnicomUserActivityInfo> rootresult = null;
		try {
			rootresult = HtmlParse.userinfo_parse(html);
		} catch (Exception e) {
			tracerLog.addTag("中国联通获取用户信息详单月   ", html);

			tracerLog.addTag("crawler error", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());

			taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());

			taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode());

			taskMobile.setError_message(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getMessage());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取用户信息详单月   ", taskMobile.toString());

			return new AsyncResult<String>("200");

		}

		UnicomUserInfo userinfo = rootresult.getUserInfo();
		if (rootresult.getErrorMessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());

			if (taskMobile.getUserMsgStatus() != null && taskMobile.getUserMsgStatus() != 200) {
				taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			} else {
				taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			}

			if (taskMobile.getBusinessMsgStatus() != null && taskMobile.getBusinessMsgStatus() != 200) {
				taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode());
			} else {
				taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode());
			}

			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());

			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取用户信息详单月   ", taskMobile.toString());

			return new AsyncResult<String>("200");

		}
		tracerLog.addTag("中国联通获取用户信息详单月   存储用户信息详单", messageLogin.getTask_id());

		// 保存前做去重处理
		List<UnicomUserActivityInfo> result = rootresult.getResult();
		List<UnicomUserActivityInfo> list = new ArrayList<>();
		if (result != null) {
			userinfo.setUserid(messageLogin.getUser_id());
			userinfo.setTaskid(taskMobile.getTaskid());
			try {
				userinfo.setPassword(EncryptAndDecryptUtil.encrypt(messageLogin.getPassword(), KEY));
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			save(userinfo);
			for (UnicomUserActivityInfo resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				resultset.setUserinfo(userinfo);
				list.add(resultset);
				save(resultset);
			}

		}

		tracerLog.addTag("中国联通获取用户信息详单月   存储用户信息详单结束", messageLogin.getTask_id());

		// 爬取成功状态更新
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());
		taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		tracerLog.addTag("中国联通获取用户信息详单月   ", taskMobile.toString());

		return new AsyncResult<String>("200");

	}

	// 获取用户信息详单
	@Async
	public Object getBalanceThem(MessageLogin messageLogin, TaskMobile taskMobile) {

		tracerLog.addTag("中国联通获取账户余额 ", messageLogin.getTask_id());

		String html = null;
		try {
			html = getThemUnit.getBalanceThem(taskMobile, 0);// 获取通话详单html
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			html = null;
		}
		tracerLog.addTag("中国联通获取账户余额 ", html);

		if (html == null) {

			tracerLog.addTag("中国联通获取账户余额   error html = null", taskMobile.getTaskid());

			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());

			log.info("=====================taskMobile " + taskMobile.toString());
			// crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取账户余额 ", taskMobile.toString());

			return null;
		}

		UnicomUserResultRoot<?> rootresult = null;
		try {
			rootresult = HtmlParse.balance_parse(html);
		} catch (Exception e) {
			tracerLog.addTag("crawler error", e.getMessage());
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());

			taskMobile.setError_message(StatusCodeRec.CRAWLER_Balance_ERROR.getMessage());

			save(taskMobile);
			// crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracerLog.addTag("中国联通获取账户余额 ", taskMobile.toString());

			return null;
		}

		if (rootresult.getErrorMessage() != null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());

			taskMobile.setError_message(rootresult.getErrorMessage().getRespDesc());

			save(taskMobile);
			tracerLog.addTag("中国联通获取账户余额 ", taskMobile.toString());

			return null;
		}
		tracerLog.addTag("中国联通获取账户余额   存储用账户余额", messageLogin.getTask_id());

		// 保存前做去重处理

		UnicomBalance unicomBalance = rootresult.getResource();

		if (unicomBalance != null) {
			unicomBalance.setUserid(messageLogin.getUser_id());
			unicomBalance.setTaskid(taskMobile.getTaskid());

			save(unicomBalance);

		}
		log.info("中国联通获取账户余额 存储账户余额详单结束");
		tracerLog.addTag("中国联通获取账户余额 存储账户余额详单结束", messageLogin.getTask_id());

		// 爬取成功状态更新
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_USER_MSG().getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_USER_MSG().getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_USER_MSG().getDescription());
		// taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());
		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		tracerLog.addTag("中国联通获取账户余额 ", taskMobile.toString());

		return null;
	}

	private void save(UnicomBalance unicomBalance) {
		unicomBalanceThemResultRepository.save(unicomBalance);
	}

	private void save(UnicomUserInfo userinfo) {
		unicomUserInfoRepository.save(userinfo);
	}

	private void save(UnicomCallResult resultset) {
		unicomCallThemResultRepository.save(resultset);
	}

	private void save(UnicomIntegraThemlResult resultset) {
		unicomIntegraThemlResultRepository.save(resultset);
	}

	private void save(UnicomIntegralTotalResult resultset) {
		unicomIntegralTegralThemResultRepository.save(resultset);
	}

	private void save(UnicomUserActivityInfo resultset) {
		unicomUserActivityInfoRepository.saveAndFlush(resultset);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	private void save(TaskMobile taskMobile) {
		taskMobileRepository.saveAndFlush(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}

}