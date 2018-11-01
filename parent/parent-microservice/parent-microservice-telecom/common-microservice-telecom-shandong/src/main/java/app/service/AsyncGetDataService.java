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
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongCallDetail;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongHtml;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongIncrement;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongIntegral;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongMonthBill;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongPayment;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongSMSDetail;
import com.microservice.dao.entity.crawler.telecom.shandong.TelecomShandongUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongCallDetailRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongIncrementRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongIntegralRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongMonthBillRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongPaymentRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongSMSDetailRepository;
import com.microservice.dao.repository.crawler.telecom.shandong.TelecomShandongUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomShanDongParser;
import app.service.common.TelecomCommonService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.telecom.shandong", "com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.telecom.shandong", "com.microservice.dao.repository.crawler.mobile"})
public class AsyncGetDataService {

	@Autowired
	private TelecomShandongCallDetailRepository telecomShandongCallDetailRepository;
	@Autowired
	private TelecomShandongSMSDetailRepository telecomShandongSMSDetailRepository;
	@Autowired
	private TelecomShandongMonthBillRepository telecomShandongMonthBillRepository;
	@Autowired
	private TelecomShandongIncrementRepository telecomShandongIncrementRepository;
	@Autowired
	private TelecomShandongUserInfoRepository telecomShandongUserInfoRepository;
	@Autowired
	private TelecomShandongIntegralRepository telecomShandongIntegralRepository;
	@Autowired
	private TelecomShandongPaymentRepository telecomShandongPaymentRepository;
	@Autowired
	private TelecomShandongHtmlRepository telecomShandongHtmlRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TelecomShanDongParser telecomShandongParser;
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
				WebParam<TelecomShandongMonthBill> webParam = telecomShandongParser.getBillData(taskMobile,i);
				if(null != webParam.getPage()){
					TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
					telecomShandongHtml.setUrl(webParam.getUrl());
					telecomShandongHtml.setPageCount(1);
					telecomShandongHtml.setType("monthbill"+i);
					telecomShandongHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
					telecomShandongHtml.setTaskid(taskMobile.getTaskid());
					telecomShandongHtmlRepository.save(telecomShandongHtml);
					tracer.addTag("parser.telecom.crawler.getBillData.page"+i,webParam.getPage().getWebResponse().getContentAsString());
					if(null != webParam.getList()){
						List<TelecomShandongMonthBill> bills = webParam.getList();
						telecomShandongMonthBillRepository.saveAll(bills);
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
			tracer.addTag("parser.telecom.crawler.getBillData.Exception",e.getMessage());
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
			WebParam<TelecomShandongUserInfo> webParam = telecomShandongParser.getUserInfo(taskMobile);
			if(null != webParam.getHtmlPage()){
				TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
				telecomShandongHtml.setUrl(webParam.getUrl());
				telecomShandongHtml.setPageCount(1);
				telecomShandongHtml.setType("userinfo");
				telecomShandongHtml.setHtml(webParam.getHtmlPage().asXml());
				telecomShandongHtml.setTaskid(taskMobile.getTaskid());
				telecomShandongHtmlRepository.save(telecomShandongHtml);
				tracer.addTag("parser.telecom.crawler.getUserInfo.page1",webParam.getHtmlPage().asXml());
			}
			TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
			telecomShandongHtml.setUrl(webParam.getUrl());
			telecomShandongHtml.setPageCount(1);
			telecomShandongHtml.setType("userinfo");
			telecomShandongHtml.setHtml(webParam.getHtml());
			telecomShandongHtml.setTaskid(taskMobile.getTaskid());
			telecomShandongHtmlRepository.save(telecomShandongHtml);
			tracer.addTag("parser.telecom.crawler.getUserInfo.page1",webParam.getHtmlPage().asXml());
			if(null != webParam.getList()){
				telecomShandongUserInfoRepository.saveAll(webParam.getList());
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]用户信息爬取成功");
				tracer.addTag("parser.telecom.crawler.getUserInfo.list","用户信息入库"+webParam.getList().toString());
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]用户信息爬取完成");
				tracer.addTag("parser.telecom.crawler.getUserInfo.error","用户信息获取失败");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
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
		try {
			WebParam<TelecomShandongIntegral> webParam = telecomShandongParser.getIntegral(taskMobile);
			int temp = 0;
			if(null != webParam.getPage()){
				TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
				telecomShandongHtml.setUrl(webParam.getUrl());
				telecomShandongHtml.setPageCount(1);
				telecomShandongHtml.setType("integral");
				telecomShandongHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
				telecomShandongHtml.setTaskid(taskMobile.getTaskid());
				telecomShandongHtmlRepository.save(telecomShandongHtml);
				tracer.addTag("parser.telecom.crawler.getIntegral.page",webParam.getPage().getWebResponse().getContentAsString());
				if(null != webParam.getList()){
					List<TelecomShandongIntegral> tntegrals = webParam.getList();
					telecomShandongIntegralRepository.saveAll(tntegrals);
					tracer.addTag("parser.telecom.crawler.getIntegral.list","积分信息入库"+tntegrals.toString());
					temp++;
				}else{
					tracer.addTag("parser.telecom.crawler.getIntegral.errorpage",webParam.getHtml());
				}
			}
			if(temp == 0){
				taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]积分信息获取完成");
				tracer.addTag("parser.telecom.crawler.getIntegral.status","积分信息获取失败");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]积分信息获取成功");
				tracer.addTag("parser.telecom.crawler.getIntegral.status","积分信息获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getIntegral.exception",e.getMessage());
			e.printStackTrace();
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
		try {
			WebParam<TelecomShandongPayment> webParam = telecomShandongParser.getPaymentHistory(taskMobile);
			if(null != webParam.getPage()){
				TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
				telecomShandongHtml.setUrl(webParam.getUrl());
				telecomShandongHtml.setPageCount(1);
				telecomShandongHtml.setType("payment");
				telecomShandongHtml.setHtml(webParam.getPage().getWebResponse().getContentAsString());
				telecomShandongHtml.setTaskid(taskMobile.getTaskid());
				telecomShandongHtmlRepository.save(telecomShandongHtml);
				tracer.addTag("parser.telecom.crawler.getPaymentHistory.page",webParam.getPage().getWebResponse().getContentAsString());
				if(null != webParam.getList()){
					List<TelecomShandongPayment> payments = webParam.getList();
					telecomShandongPaymentRepository.saveAll(payments);
					tracer.addTag("parser.telecom.crawler.getPaymentHistory.list","充值缴费记录已入库"+payments.toString());
					temp++;
				}
			}
			if(temp == 0){
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]充值缴费记录获取完成");
				tracer.addTag("parser.telecom.crawler.getPaymentHistory.status"+temp,"充值缴费记录获取失败");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updatePayMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]充值缴费记录获取成功");
				tracer.addTag("parser.telecom.crawler.getPaymentHistory.status"+temp,"充值缴费记录获取成功");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("parser.telecom.crawler.getPaymentHistory.exception",e.getMessage());
			e.printStackTrace();
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
		for (int i = 1; i < 7; i++) {
			try {
				WebParam<TelecomShandongSMSDetail> webParam = telecomShandongParser.getSMSDetails(taskMobile,i);
				if(null != webParam.getHtml()){
					String html = webParam.getHtml();
					TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
					telecomShandongHtml.setUrl(webParam.getUrl());
					telecomShandongHtml.setPageCount(1);
					telecomShandongHtml.setType("SMSDetails"+i);
					telecomShandongHtml.setHtml(html);
					telecomShandongHtml.setTaskid(taskMobile.getTaskid());
					telecomShandongHtmlRepository.save(telecomShandongHtml);
					tracer.addTag("parser.telecom.crawler.getSMSDetails.page"+i,html);
					List<TelecomShandongSMSDetail> telecomShandongSMSDetails = webParam.getList();
					if(telecomShandongSMSDetails.size() > 0){
						telecomShandongSMSDetailRepository.saveAll(telecomShandongSMSDetails);
						tracer.addTag("parser.telecom.crawler.getSMSDetails.list"+i,"短信详单已入库"+i+telecomShandongSMSDetails.toString());
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
		for (int i = 1; i < 7; i++) {
			try {
				WebParam<TelecomShandongCallDetail> webParam = null;
				String qryMonth = getDateBefore("yyyyMM", i);
				try {
					webParam = telecomShandongParser.getCallDetails(taskMobile,i);
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
					TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
					telecomShandongHtml.setUrl(webParam.getUrl());
					telecomShandongHtml.setPageCount(1);
					telecomShandongHtml.setType("CallDetails"+i);
					telecomShandongHtml.setHtml(html);
					telecomShandongHtml.setTaskid(taskMobile.getTaskid());
					telecomShandongHtmlRepository.save(telecomShandongHtml);
					tracer.addTag("parser.telecom.crawler.getCallDetails.page"+i,html);
					List<TelecomShandongCallDetail> telecomShandongCallDetails = webParam.getList();
					if(telecomShandongCallDetails.size() > 0){
						telecomShandongCallDetailRepository.saveAll(telecomShandongCallDetails);
						tracer.addTag("parser.telecom.crawler.getCallDetails.list"+i,"通话详单已入库"+i+telecomShandongCallDetails.toString());
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
		for (int i = 1; i < 7; i++) {
			try {
				WebParam<TelecomShandongIncrement> webParam = telecomShandongParser.getIncrement(taskMobile,i);
				if(null != webParam.getHtml()){
					String html = webParam.getHtml();
					TelecomShandongHtml telecomShandongHtml = new TelecomShandongHtml();
					telecomShandongHtml.setUrl(webParam.getUrl());
					telecomShandongHtml.setPageCount(1);
					telecomShandongHtml.setType("increment"+i);
					telecomShandongHtml.setHtml(html);
					telecomShandongHtml.setTaskid(taskMobile.getTaskid());
					telecomShandongHtmlRepository.save(telecomShandongHtml);
					tracer.addTag("parser.telecom.crawler.getIncrement.page"+i,html);
					List<TelecomShandongIncrement> telecomShandongIncrements = webParam.getList();
					if(telecomShandongIncrements.size() > 0){
						telecomShandongIncrementRepository.saveAll(telecomShandongIncrements);
						tracer.addTag("parser.telecom.crawler.getIncrement.list"+i,"增值详单已入库"+i+telecomShandongIncrements.toString());
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
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "[数据采集中]增值详单记录获取完成");
			tracer.addTag("parser.telecom.crawler.getIncrement.Exception",e.getMessage());
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
