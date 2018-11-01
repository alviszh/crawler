package app.service;

import java.net.URL;
import java.time.LocalDate;
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

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeClass;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCallThemResult;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCusThemStringResult;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCustomerThemResult;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomPayMsgThemResult;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomCusThemStringResultRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomCustomThemResultRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomPayMsgThemResultRepository;
import com.microservice.dao.repository.crawler.telecom.heilongjiang.TelecomResultRepository;

import app.commontracerlog.TracerLog;
import app.crawler.telecom.htmlparse.TelecomParse;
import app.unit.GetThemUnitTelecom;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.heilongjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.heilongjiang")
public class TelecomUnitService {

	public static final Logger log = LoggerFactory.getLogger(TelecomUnitService.class);

	@Autowired
	private TelecomResultRepository telecomResultRepository;

	@Autowired
	private TelecomPayMsgThemResultRepository telecomPayMsgThemResultRepository;

	@Autowired
	private TelecomCusThemStringResultRepository telecomCusThemStringResultRepository;

	@Autowired
	private TelecomCustomThemResultRepository telecomCustomThemResultRepository;

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	
	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	
	// 爬取通话果集并存入数据库 第一次
	public Integer getCallThemHtml(MessageLogin messageLogin, TaskMobile taskMobile, int i, int seledType,int selectype) {

		tracerLog.addTag("==============>中国电信抓取 通话详单"+i+"月  taskid<===============", messageLogin.getTask_id());
		LocalDate nowdate = LocalDate.now();

		
		String date = nowdate.plusMonths(-i).getYear() + "" + nowdate.plusMonths(-i).getMonthValue();

		String html = GetThemUnitTelecom.getCallThemHtml(messageLogin, taskMobile, taskMobile.getPhonenum(), date, seledType,selectype, 1);
		
		tracerLog.addTag("==============>中国电信抓取 通话详单"+date+"月  html<===============", html);

		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		String monthint = nowdate.plusMonths(-i).getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String error_date = nowdate.plusMonths(-i).getYear()+monthint;
		tracerLog.addTag("==============>中国电信抓取 通话详单"+i+"月 <===============", taskMobile.toString());

		if (html == null) {

			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","当月返回html为null", 0);
			mobileDataErrRecRepository.save(dataErrRec);
			return 1;
		}
		tracerLog.addTag("==============>中国电信抓取 通话详单"+date+"月  存储通话信息详单 <===============", messageLogin.getTask_id());
		List<TelecomCallThemResult> results = null;
		
		if(seledType == 9){
			 results = TelecomParse.callParse(html);
		}else if(seledType == 5){
			 results = TelecomParse.smsParse(html);
		}
		
		if(results==null || results.size()<=0){
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setCallRecordStatus(StatusCodeRec.MESSAGE_SUCESS_TWO.getCode());
		}else{
			for (TelecomCallThemResult result : results) {
				result.setUserid(messageLogin.getUser_id());
				result.setTaskid(taskMobile.getTaskid());
				result.setMd5(CommonUnit.MD5(result.toStringmd5()));
				save(result);
			}
			

			
		}
		tracerLog.addTag("==============>中国电信抓取 通话详单"+date+"月  存储通话信息详单  结束<===============", messageLogin.getTask_id());
		
		Integer pagenum = TelecomParse.getPagecall(html);
		tracerLog.addTag("pagenum"+pagenum+"<===============", pagenum+"");

		return pagenum;
	}

	@Async
	public  Future<Integer> getCallThemHtml(MessageLogin messageLogin, TaskMobile taskMobile, int i, int seledType,int selectype,int page) {

		LocalDate nowdate = LocalDate.now();

		
		String date = nowdate.plusMonths(-i).getYear() + "" + nowdate.plusMonths(-i).getMonthValue();
		tracerLog.addTag("==============>中国电信抓取 通话详单"+date+"月  taskid<===============", messageLogin.getTask_id());

		String html = GetThemUnitTelecom.getCallThemHtml(messageLogin, taskMobile, taskMobile.getPhonenum(), date, seledType,selectype, page);
		
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		String monthint = nowdate.plusMonths(-i).getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String error_date = nowdate.plusMonths(-i).getYear()+monthint;
		tracerLog.addTag("==============>中国电信抓取 通话详单"+date+"月 <===============", taskMobile.toString());

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", error_date,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE","当月返回html为null", 0);
			mobileDataErrRecRepository.save(dataErrRec);
//			return page+1;
			
			
			return new AsyncResult<Integer>(page+1);
		}
//		tracerLog.addTag("==============>中国电信抓取 通话详单"+i+"月  存储通话信息详单 <===============", messageLogin.getTask_id());
		List<TelecomCallThemResult> results = null;

		if(seledType == 9){
			 results = TelecomParse.callParse(html);
		}else if(seledType == 5){
			 results = TelecomParse.smsParse(html);
		}		if(results==null || results.size()<=0){
			tracerLog.addTag("中国电信抓取 通话详单"+date+"月  存储通话信息详单  无记录",html);
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

			taskMobile.setCallRecordStatus(StatusCodeRec.MESSAGE_SUCESS_TWO.getCode());
		}else{
			for (TelecomCallThemResult result : results) {
				result.setUserid(messageLogin.getUser_id());
				result.setTaskid(taskMobile.getTaskid());
				result.setMd5(CommonUnit.MD5(result.toStringmd5()));
				tracerLog.addTag("中国电信抓取 通话详单"+i+"月  存储通话信息详单 ", result.toString());
				save(result);
			}
			

			
		}
//		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
//
//		taskMobile.setError_message(StatusCodeRec.MESSAGE_SUCESS_TWO.getMessage()); // 爬取成功状态更新
//		taskMobile.setCallRecordStatus(StatusCodeRec.CRAWLER_CallRecordStatus_SUCESS.getCode());
//		save(taskMobile);
//		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		tracerLog.addTag("==============>中国电信抓取 通话详单"+date+"月  存储通话信息详单  结束<===============", messageLogin.getTask_id());
		
//		Integer pagenum = TelecomParse.getPagecall(html);
//		tracerLog.addTag("pagenum"+pagenum+"<===============", pagenum+"");

		return new AsyncResult<Integer>(1);
	}
	// 爬取缴费信息并入库
	public String getPayMsgStatusThem(MessageLogin messageLogin, TaskMobile taskMobile) {
		tracerLog.addTag("==============>中国电信抓取 缴费信息<===============", messageLogin.getTask_id());
		String html = null;
		try{
		 html = GetThemUnitTelecom.getPayMsgStatusThem(messageLogin, taskMobile);// 获取通话详单html

		}catch(Exception e){
			tracerLog.addTag("==============>中国电信抓取 缴费信息 错误<===============",   e.getMessage());
			e.printStackTrace();
		}
		tracerLog.addTag("==============>中国电信抓取 缴费信息 html<===============",  html);
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracerLog.addTag("==============>中国电信抓取 缴费信息  taskMobile<===============", taskMobile.toString());

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			
			if(taskMobile.getPayMsgStatus()!=null&&taskMobile.getPayMsgStatus()!=200){
				taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			}else{
				taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			}
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}

		tracerLog.addTag("==============>中国电信抓取 缴费信息存储缴费详单<=============", messageLogin.getTask_id());

		// 保存前做去重处理
		List<TelecomPayMsgThemResult> result = null;
		try{
			result = TelecomParse.payMsg_parse(html);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(html);
		}
		
		if (result != null) {
			for (TelecomPayMsgThemResult resultset : result) {
				resultset.setUserid(messageLogin.getUser_id());
				resultset.setTaskid(taskMobile.getTaskid());
				save(resultset);
			}
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			taskMobile.setPayMsgStatus((StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode()));
			save(taskMobile);
		}else{
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(0).getDescription());
			taskMobile.setPayMsgStatus((StatusCodeRec.MESSAGE_SUCESS_ONE.getCode()));
			save(taskMobile);
		}

		tracerLog.addTag("==============>中国电信抓取 缴费信息 存储缴费详单结束<===============", messageLogin.getTask_id());

		
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		return null;

	}

	// 爬取客户账单集并以String 形式存入数据库
	public String getCustomThem(MessageLogin messageLogin, TaskMobile taskMobile, int i) {
		tracerLog.addTag("==============>中国电信抓取客户账单"+i+"月  <===============", messageLogin.getTask_id());
		String html = GetThemUnitTelecom.getCustomThem(messageLogin, taskMobile, i);
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		tracerLog.addTag("==============>中国电信抓取客户账单"+i+"月  html<===============", html);
		
		tracerLog.addTag("==============>中国电信抓取客户账单"+i+"月 taskMobile <===============", taskMobile.toString());

		if (html == null) {
			taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
			taskMobile.setPhase(StatusCodeClass.StatusCodeClass_PAY_MSG(5).getPhase());
			taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_PAY_MSG(5).getPhasestatus());
			taskMobile.setDescription(StatusCodeClass.StatusCodeClass_PAY_MSG(5).getDescription());
			
			
			/*if(taskMobile.getPayMsgStatus()!=null&&taskMobile.getPayMsgStatus()!=200){
				taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			}else{
				taskMobile.setPayMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			}*/
			if(taskMobile.getUserMsgStatus()!=null&&taskMobile.getUserMsgStatus()!=200){
				taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			}else{
				taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			}
			if(taskMobile.getFamilyMsgStatus()!=null&&taskMobile.getFamilyMsgStatus()!=200){
				taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_ERROR.getCode());
			}else{
				taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_ERROR.getCode());
			}
			
			if(taskMobile.getIntegralMsgStatus()!=null&&taskMobile.getIntegralMsgStatus()!=200){
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}else{
				taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_ERROR.getCode());
			}
			
			if(taskMobile.getSmsRecordStatus()!=null&&taskMobile.getSmsRecordStatus()!=200){
				taskMobile.setSmsRecordStatus(StatusCodeRec.CRAWLER_SMSRecordStatus_ERROR.getCode());
			}else{
				taskMobile.setSmsRecordStatus(StatusCodeRec.CRAWLER_SMSRecordStatus_ERROR.getCode());
			}
			if(taskMobile.getAccountMsgStatus()!=null&&taskMobile.getAccountMsgStatus()!=200){
				taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			}else{
				taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_ERROR.getCode());
			}
			if(taskMobile.getBusinessMsgStatus()!=null&&taskMobile.getBusinessMsgStatus()!=200){
				taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode());
			}else{
				taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_ERROR.getCode());
			}
								
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		
		TelecomCusThemStringResult resultstring = new TelecomCusThemStringResult();
		resultstring.setHtml(html);
		resultstring.setUserid(messageLogin.getUser_id());
		resultstring.setTaskid(taskMobile.getTaskid());
		save(resultstring);
		tracerLog.addTag("==============>中国电信抓取客户账单"+i+"月  存储客户账单信息详单<===============", messageLogin.getTask_id());

		TelecomCustomerThemResult result = TelecomParse.custom_parse(html);
		result.setUserid(messageLogin.getUser_id());
		result.setTaskid(taskMobile.getTaskid());
		System.out.println(result);
		save(result);
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		taskMobile.setPhase(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhase());
		taskMobile.setPhase_status(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getPhasestatus());
		taskMobile.setDescription(StatusCodeClass.StatusCodeClass_INTEGRA_MSG(i).getDescription());
		
		taskMobile.setUserMsgStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		
		taskMobile.setFamilyMsgStatus(StatusCodeRec.CRAWLER_FamilyMsgStatus_SUCESS.getCode());

		taskMobile.setIntegralMsgStatus(StatusCodeRec.CRAWLER_IntegralMsgStatus_SUCESS.getCode());

		taskMobile.setSmsRecordStatus(StatusCodeRec.CRAWLER_SMSRecordStatus_SUCESS.getCode());
		
		taskMobile.setAccountMsgStatus(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());
		
		taskMobile.setBusinessMsgStatus(StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode());


		save(taskMobile);
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		tracerLog.addTag("==============>中国电信抓取客户账单"+i+"月  存储客户账单信息详单结束<===============", messageLogin.getTask_id());

		return "sucess";

	}

	private void save(TelecomCustomerThemResult result) {
		telecomCustomThemResultRepository.save(result);
	}

	private void save(TelecomCusThemStringResult result) {
		telecomCusThemStringResultRepository.save(result);
	}

	private void save(TelecomPayMsgThemResult result) {
		telecomPayMsgThemResultRepository.save(result);
	}

	private void save(TelecomCallThemResult result) {
		telecomResultRepository.save(result);
	}

	private void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	public HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}