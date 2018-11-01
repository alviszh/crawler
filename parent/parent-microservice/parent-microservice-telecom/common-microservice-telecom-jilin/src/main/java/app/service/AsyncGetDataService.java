package app.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinCallDetails;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinHtml;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinIncrement;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinIntegral;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinMonthBill;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinPayment;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinSMSDetails;
import com.microservice.dao.entity.crawler.telecom.jilin.TelecomJilinUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinCallDetailsRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinIncrementRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinIntegralRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinMonthBillRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinPaymentRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinSMSDetailsRepository;
import com.microservice.dao.repository.crawler.telecom.jilin.TelecomJilinUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomJilinParser;
import app.service.common.TelecomCommonService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.telecom.jilin", "com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.telecom.jilin", "com.microservice.dao.repository.crawler.mobile"})
public class AsyncGetDataService {

	@Autowired
	private TelecomJilinCallDetailsRepository telecomJilinCallDetailsRepository;
	@Autowired
	private TelecomJilinSMSDetailsRepository telecomJilinSMSDetailsRepository;
	@Autowired
	private TelecomJilinMonthBillRepository telecomJilinMonthBillRepository;
	@Autowired
	private TelecomJilinIncrementRepository telecomJilinIncrementRepository;
	@Autowired
	private TelecomJilinUserInfoRepository telecomJilinUserInfoRepository;
	@Autowired
	private TelecomJilinIntegralRepository telecomJilinIntegralRepository;
	@Autowired
	private TelecomJilinPaymentRepository telecomJilinPaymentRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomJilinHtmlRepository telecomJilinHtmlRepository;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TelecomJilinParser telecomJilinParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	
	/*
	 * @Des 获取账单信息 
	 * 只能获取最近6个月的账单信息，不能获取当前月信息
	 */
	@Async
	public void getBillData(TaskMobile taskMobile){
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getBillData.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 1; i < 7; i++) {
			try {
				WebParam<TelecomJilinMonthBill> webParam = telecomJilinParser.getBillData(taskMobile,i);
				if(null != webParam.getPage()){
					TelecomJilinHtml telecomJilinHtml = new TelecomJilinHtml();
					telecomJilinHtml.setUrl(webParam.getUrl());
					telecomJilinHtml.setPageCount(1);
					telecomJilinHtml.setType("monthbill"+i);
					telecomJilinHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
					telecomJilinHtml.setTaskid(taskMobile.getTaskid());
					telecomJilinHtmlRepository.save(telecomJilinHtml);
					tracer.addTag("parser.telecom.crawler.getBillData.page"+i,webParam.getPage().getWebResponse().getContentAsString());
					if(null != webParam.getList()){
						List<TelecomJilinMonthBill> bills = webParam.getList();
						telecomJilinMonthBillRepository.saveAll(bills);
						tracer.addTag("parser.telecom.crawler.getBillData.list"+i,"账单信息入库："+bills.toString());
						temp++;
					}
					
				}
			} catch (Exception e) {
				tracer.addTag("parser.telecom.crawler.getBillData.exception"+i,e.toString());
				e.printStackTrace();
			}
			
		}
		try {
			if(temp > 0){
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]账单信息获取成功");
				tracer.addTag("parser.telecom.crawler.getBillData.status"+temp,"账单信息获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]账单信息获取完成");
				tracer.addTag("parser.telecom.crawler.getBillData.status"+temp,"账单信息获取失败");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.getBillData.Exception", e.getMessage());
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]账单信息获取完成");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/*
	 * @Des 获取客户资料里的用户信息
	 * 
	 */
	@Async
	public void getUserInfo(TaskMobile taskMobile){
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getUserInfo.taskid",taskMobile.getTaskid());
		try {
			WebParam<TelecomJilinUserInfo> webParam = telecomJilinParser.getUserInfo(taskMobile);
			if(null != webParam.getHtmlPage()){
				TelecomJilinHtml telecomJilinHtml = new TelecomJilinHtml();
				telecomJilinHtml.setUrl(webParam.getUrl());
				telecomJilinHtml.setPageCount(1);
				telecomJilinHtml.setType("userinfo");
				telecomJilinHtml.setHtml(webParam.getHtmlPage().asXml());
				telecomJilinHtml.setTaskid(taskMobile.getTaskid());
				telecomJilinHtmlRepository.save(telecomJilinHtml);
				tracer.addTag("parser.telecom.crawler.getUserInfo.page",webParam.getHtmlPage().asXml());
				if(null != webParam.getList()){
					telecomJilinUserInfoRepository.saveAll(webParam.getList());
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]用户信息爬取成功");
					tracer.addTag("parser.telecom.crawler.getUserInfo.list","用户信息入库"+webParam.getList().toString());
					crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
				}else{
					taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]用户信息爬取完成");
					tracer.addTag("parser.telecom.crawler.getUserInfo.error","用户信息获取失败");
					crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
				}
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getUserInfo.exception",e.getMessage());
			e.printStackTrace();
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]用户信息爬取完成");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/*
	 * @Des 获取我的积分里的积分信息
	 * 
	 */
	@Async
	public void getIntegral(TaskMobile taskMobile){
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getIntegral.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			try {
				WebParam<TelecomJilinIntegral> webParam = telecomJilinParser.getIntegral(taskMobile,i);
				if(null != webParam.getPage()){
					TelecomJilinHtml telecomJilinHtml = new TelecomJilinHtml();
					telecomJilinHtml.setUrl(webParam.getUrl());
					telecomJilinHtml.setPageCount(1);
					telecomJilinHtml.setType("integral"+i);
					telecomJilinHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
					telecomJilinHtml.setTaskid(taskMobile.getTaskid());
					telecomJilinHtmlRepository.save(telecomJilinHtml);
					tracer.addTag("parser.telecom.crawler.getIntegral.page"+i,webParam.getPage().getWebResponse().getContentAsString());
					if(null != webParam.getList()){
						List<TelecomJilinIntegral> tntegrals = webParam.getList();
						telecomJilinIntegralRepository.saveAll(tntegrals);
						tracer.addTag("parser.telecom.crawler.getIntegral.list"+i,"积分信息入库"+i+tntegrals.toString());
						temp++;
					}else{
						tracer.addTag("parser.telecom.crawler.getIntegral.errorpage"+i,webParam.getHtml());
					}
				}
			} catch (Exception e) {
				tracer.addTag("parser.telecom.crawler.getIntegral.exception"+i,e.toString());
				e.printStackTrace();
			}
		}
		try {
			if(temp > 0){
				taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]积分信息获取成功");
				tracer.addTag("parser.telecom.crawler.getIntegral.status"+temp,"积分信息获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]积分信息获取完成");
				tracer.addTag("parser.telecom.crawler.getIntegral.status"+temp,"积分信息获取失败");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.getIntegral.Exception",e.getMessage());
			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]积分信息获取完成");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/*
	 * @Des 获取充值缴费记录
	 * 
	 */
	@Async
	public void getPaymentHistory(TaskMobile taskMobile){
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getPaymentHistory.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			try {
				WebParam<TelecomJilinPayment> webParam = telecomJilinParser.getPaymentHistory(taskMobile,i);
				if(null != webParam.getPage()){
					TelecomJilinHtml telecomJilinHtml = new TelecomJilinHtml();
					telecomJilinHtml.setUrl(webParam.getUrl());
					telecomJilinHtml.setPageCount(1);
					telecomJilinHtml.setType("payment"+i);
					telecomJilinHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
					telecomJilinHtml.setTaskid(taskMobile.getTaskid());
					telecomJilinHtmlRepository.save(telecomJilinHtml);
					tracer.addTag("parser.telecom.crawler.getPaymentHistory.page"+i,webParam.getPage().getWebResponse().getContentAsString());
					if(null != webParam.getList()){
						List<TelecomJilinPayment> payments = webParam.getList();
						telecomJilinPaymentRepository.saveAll(payments);
						tracer.addTag("parser.telecom.crawler.getPaymentHistory.list"+i,"充值缴费记录已入库"+i+payments.toString());
						temp++;
					}
				}
			} catch (Exception e) {
				tracer.addTag("parser.telecom.crawler.getPaymentHistory.exception"+i,e.toString());
				e.printStackTrace();
			}
		}
		try {
			if(temp > 0){
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]充值缴费记录获取成功");
				tracer.addTag("parser.telecom.crawler.getPaymentHistory.status"+temp,"充值缴费记录获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]充值缴费记录获取完成");
				tracer.addTag("parser.telecom.crawler.getPaymentHistory.status"+temp,"充值缴费记录获取失败");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.getPaymentHistory.Exception",e.getMessage());
			taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]充值缴费记录获取完成");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	
	/*
	 * @Des 获取短信详单
	 * 
	 */
	@Async
	public void getSMSDetails(TaskMobile taskMobile){
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getSMSDetails.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			try {
				WebParam<TelecomJilinSMSDetails> webParam = telecomJilinParser.getSMSDetails(taskMobile,i);
				if(null != webParam.getHtml()){
					String html = webParam.getHtml();
					TelecomJilinHtml telecomJilinHtml = new TelecomJilinHtml();
					telecomJilinHtml.setUrl(webParam.getUrl());
					telecomJilinHtml.setPageCount(1);
					telecomJilinHtml.setType("SMSDetails"+i);
					telecomJilinHtml.setHtml(html);
					telecomJilinHtml.setTaskid(taskMobile.getTaskid());
					telecomJilinHtmlRepository.save(telecomJilinHtml);
					tracer.addTag("parser.telecom.crawler.getSMSDetails.page"+i,html);
					List<TelecomJilinSMSDetails> telecomJilinSMSDetails = webParam.getList();
					if(telecomJilinSMSDetails.size() > 0){
						telecomJilinSMSDetailsRepository.saveAll(telecomJilinSMSDetails);
						tracer.addTag("parser.telecom.crawler.getSMSDetails.list"+i,"短信详单已入库"+i+telecomJilinSMSDetails.toString());
						temp++;
					}
				}
			} catch (Exception e) {
				tracer.addTag("parser.telecom.crawler.getSMSDetails.exception"+i,e.toString());
				e.printStackTrace();
			}
		}
		try {
			if(temp > 0){
				taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "[数据采集中]短信详单记录获取成功");
				tracer.addTag("parser.telecom.crawler.getSMSDetails.status"+temp,"短信详单记录获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "[数据采集中]短信详单记录获取完成");
				tracer.addTag("parser.telecom.crawler.getSMSDetails.status"+temp,"短信详单记录为空");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.getSMSDetails.Exception",e.getMessage());
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "[数据采集中]短信详单记录获取完成");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/*
	 * @Des 获取通话详单
	 * 
	 */
	@Async
	public void getCallDetails(TaskMobile taskMobile){
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getCallDetails.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			try {
				WebParam<TelecomJilinCallDetails> webParam = null;
				String qryMonth = getDateBefore("yyyyMM", i);
				try {
					webParam = telecomJilinParser.getCallDetails(taskMobile,i);
				} catch (RuntimeException e) {
					MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", qryMonth,
							taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), 0);
					mobileDataErrRecRepository.save(dataErrRec);
					tracer.addTag("mobileNum:"+taskMobile.getPhonenum()+" billType:通话记录 dataErrRec qryMonth:"+qryMonth, dataErrRec.toString());
					tracer.addTag(qryMonth+" :cmccRetryService.retry.FailingHttpStatusCodeException", e.getMessage());
				} catch (IOException e) {
					MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", qryMonth,
							taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), 0);
					mobileDataErrRecRepository.save(dataErrRec);
					tracer.addTag("mobileNum:"+taskMobile.getPhonenum()+" billType:通话记录 dataErrRec qryMonth:"+qryMonth, dataErrRec.toString());
					tracer.addTag(qryMonth+" :cmccRetryService.retry.IOException", e.getMessage());
				}
				if(null != webParam.getHtml()){
					String html = webParam.getHtml();
					TelecomJilinHtml telecomJilinHtml = new TelecomJilinHtml();
					telecomJilinHtml.setUrl(webParam.getUrl());
					telecomJilinHtml.setPageCount(1);
					telecomJilinHtml.setType("CallDetails"+i);
					telecomJilinHtml.setHtml(html);
					telecomJilinHtml.setTaskid(taskMobile.getTaskid());
					telecomJilinHtmlRepository.save(telecomJilinHtml);
					tracer.addTag("parser.telecom.crawler.getCallDetails.page"+i,html);
					List<TelecomJilinCallDetails> telecomJilinCallDetails = webParam.getList();
					if(telecomJilinCallDetails.size() > 0){
						telecomJilinCallDetailsRepository.saveAll(telecomJilinCallDetails);
						tracer.addTag("parser.telecom.crawler.getCallDetails.list"+i,"通话详单已入库"+i+telecomJilinCallDetails.toString());
						temp++;
					}
				}
			} catch (Exception e) {
				tracer.addTag("parser.telecom.crawler.getCallDetails.exception"+i,e.toString());
				e.printStackTrace();
			}
		}
		try {
			if(temp > 0){
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "[数据采集中]通话详单记录获取成功");
				tracer.addTag("parser.telecom.crawler.getCallDetails.status"+temp,"通话详单记录获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 201, "[数据采集中]通话详单记录获取完成");
				tracer.addTag("parser.telecom.crawler.getCallDetails.status"+temp,"通话详单记录为空");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.getCallDetails.Exception",e.getMessage());
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "[数据采集中]通话详单记录获取完成");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	
	/*
	 * @Des 获取增值详单
	 * 
	 */
	@Async
	public void getIncrement(TaskMobile taskMobile){
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getIncrement.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			try {
				WebParam<TelecomJilinIncrement> webParam = telecomJilinParser.getIncrement(taskMobile,i);
				if(null != webParam.getHtml()){
					String html = webParam.getHtml();
					TelecomJilinHtml telecomJilinHtml = new TelecomJilinHtml();
					telecomJilinHtml.setUrl(webParam.getUrl());
					telecomJilinHtml.setPageCount(1);
					telecomJilinHtml.setType("increment"+i);
					telecomJilinHtml.setHtml(html);
					telecomJilinHtml.setTaskid(taskMobile.getTaskid());
					telecomJilinHtmlRepository.save(telecomJilinHtml);
					tracer.addTag("parser.telecom.crawler.getIncrement.page"+i,html);
					List<TelecomJilinIncrement> telecomJilinIncrements = webParam.getList();
					if(telecomJilinIncrements.size() > 0){
						telecomJilinIncrementRepository.saveAll(telecomJilinIncrements);
						tracer.addTag("parser.telecom.crawler.getIncrement.list"+i,"增值详单已入库"+i+telecomJilinIncrements.toString());
						temp++;
					}
				}
			} catch (Exception e) {
				tracer.addTag("parser.telecom.crawler.getIncrement.exception"+i,e.toString());
				e.printStackTrace();
			}
		}
		try {
			if(temp > 0){
				taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]增值详单记录获取成功");
				tracer.addTag("parser.telecom.crawler.getIncrement.status"+temp,"增值详单记录获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]增值详单记录获取完成");
				tracer.addTag("parser.telecom.crawler.getIncrement.status"+temp,"增值详单记录为空");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.telecom.crawler.getIncrement.Exception", e.getMessage());
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]增值详单记录获取完成");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		
	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public static String getDateBefore(String fmt, int i){
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
	
}
