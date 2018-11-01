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
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanCallDetail;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanHtml;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanMonthBill;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanPayment;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanSMSDetail;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanServer;
import com.microservice.dao.entity.crawler.telecom.henan.TelecomHenanUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanCallDetailRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanMonthBillRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanPaymentRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanSMSDetailRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanServerRepository;
import com.microservice.dao.repository.crawler.telecom.henan.TelecomHenanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.TelecomHenanParser;
import app.service.common.TelecomCommonService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.telecom.henan", "com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.telecom.henan", "com.microservice.dao.repository.crawler.mobile"})
public class AsyncGetDataService {

	@Autowired
	private TelecomHenanCallDetailRepository telecomHenanCallDetailRepository;
	@Autowired
	private TelecomHenanSMSDetailRepository telecomHenanSMSDetailRepository;
	@Autowired
	private TelecomHenanMonthBillRepository telecomHenanMonthBillRepository;
	@Autowired
	private TelecomHenanUserInfoRepository telecomHenanUserInfoRepository;
	@Autowired
	private TelecomHenanPaymentRepository telecomHenanPaymentRepository;
	@Autowired
	private TelecomHenanServerRepository telecomHenanServerRepository;
	@Autowired
	private TelecomHenanHtmlRepository telecomHenanHtmlRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomCommonService telecomCommonService;
	@Autowired
	private TelecomHenanParser telecomHenanParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	
	
	/*
	 * @Des 获取客户资料里的用户信息
	 * 
	 */
	@Async
	public void getUserInfo(TaskMobile taskMobile) throws Exception{
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getUserInfo.taskid",taskMobile.getTaskid());
		WebParam<TelecomHenanUserInfo> webParam = telecomHenanParser.getUserInfo(taskMobile);
		if(null != webParam){
			tracer.addTag("parser.telecom.crawler.getUserInfo.webparam1",webParam.toString());
			if(null != webParam.getHtmlPage()){
				tracer.addTag("parser.telecom.crawler.getUserInfo.htmlpage","come in +"+webParam.getHtmlPage().asXml());
				TelecomHenanHtml telecomHenanHtml = new TelecomHenanHtml();
				telecomHenanHtml.setUrl(webParam.getUrl());
				telecomHenanHtml.setPageCount(1);
				telecomHenanHtml.setType("userinfo");
				telecomHenanHtml.setHtml(webParam.getHtmlPage().asXml());
				telecomHenanHtml.setTaskid(taskMobile.getTaskid());
				telecomHenanHtmlRepository.save(telecomHenanHtml);
				tracer.addTag("parser.telecom.crawler.getUserInfo.page1",webParam.getHtmlPage().asXml());
			}
			if(null != webParam.getHtml()){
				TelecomHenanHtml telecomHenanHtml = new TelecomHenanHtml();
				telecomHenanHtml.setUrl(webParam.getUrl());
				telecomHenanHtml.setPageCount(1);
				telecomHenanHtml.setType("userinfo");
				telecomHenanHtml.setHtml(webParam.getHtml());
				telecomHenanHtml.setTaskid(taskMobile.getTaskid());
				telecomHenanHtmlRepository.save(telecomHenanHtml);
				tracer.addTag("parser.telecom.crawler.getUserInfo.page2",webParam.getHtml());
			}
			if(null != webParam.getList()){
				telecomHenanUserInfoRepository.saveAll(webParam.getList());
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]用户信息爬取成功");
				tracer.addTag("parser.telecom.crawler.getUserInfo.list","用户信息入库"+webParam.getList().toString());
//				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]用户信息爬取完成");
				tracer.addTag("parser.telecom.crawler.getUserInfo.error","用户信息获取失败");
//				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		}else{
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]用户信息爬取完成");
			tracer.addTag("parser.telecom.crawler.getUserInfo.error1","用户信息获取失败");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/*
	 * @Des 获取在用业务信息
	 * 
	 */
	@Async
	public void getServer(TaskMobile taskMobile) throws Exception{
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getUserInfo.taskid",taskMobile.getTaskid());
		WebParam<TelecomHenanServer> webParam = telecomHenanParser.getServer(taskMobile);
		if(null != webParam){
			if(null != webParam.getHtmlPage()){
				TelecomHenanHtml telecomHenanHtml = new TelecomHenanHtml();
				telecomHenanHtml.setUrl(webParam.getUrl());
				telecomHenanHtml.setPageCount(1);
				telecomHenanHtml.setType("server");
				telecomHenanHtml.setHtml(webParam.getHtmlPage().asXml());
				telecomHenanHtml.setTaskid(taskMobile.getTaskid());
				telecomHenanHtmlRepository.save(telecomHenanHtml);
				tracer.addTag("parser.telecom.crawler.getServer.page","<xmp>"+webParam.getHtmlPage().asXml()+"</xmp>");
			}
			if(null != webParam.getList() && webParam.getList().size() > 0){
				telecomHenanServerRepository.saveAll(webParam.getList());
				taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]在用业务信息爬取成功");
				tracer.addTag("parser.telecom.crawler.getServer.list","在用业务信息入库"+webParam.getList().toString());
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}else{
				taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]在用业务信息爬取完成");
				tracer.addTag("parser.telecom.crawler.getServer.error","在用业务信息获取失败");
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		}else{
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]在用业务信息爬取完成");
			tracer.addTag("parser.telecom.crawler.getServer.error","在用业务信息获取失败");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/*
	 * @Des 获取账单信息 
	 * 只能获取最近6个月的账单信息，不能获取当前月信息
	 */
	@Async
	public void getBillData(TaskMobile taskMobile) throws Exception{
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getBillData.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 1; i < 7; i++) {
			WebParam<TelecomHenanMonthBill> webParam = telecomHenanParser.getBillData(taskMobile,i);
			if(null != webParam){
				if(null != webParam.getHtmlPage()){
					TelecomHenanHtml telecomHenanHtml = new TelecomHenanHtml();
					telecomHenanHtml.setUrl(webParam.getUrl());
					telecomHenanHtml.setPageCount(1);
					telecomHenanHtml.setType("monthbill"+i);
					telecomHenanHtml.setHtml(webParam.getHtmlPage().asXml());
					telecomHenanHtml.setTaskid(taskMobile.getTaskid());
					telecomHenanHtmlRepository.save(telecomHenanHtml);
					tracer.addTag("parser.telecom.crawler.getBillData.page"+i,webParam.getHtmlPage().asXml());
				}
				if(null != webParam.getList()){
					List<TelecomHenanMonthBill> bills = webParam.getList();
					telecomHenanMonthBillRepository.saveAll(bills);
					tracer.addTag("parser.telecom.crawler.getBillData.list"+i,"账单信息入库："+bills.toString());
					temp++;
				}else{
					tracer.addTag("parser.telecom.crawler.getBillData.list"+i,"无账单信息数据");
				}
			}else{
				tracer.addTag("parser.telecom.crawler.getBillData.error"+i,"该月账单信息页面异常");
			}
		}
		if(temp > 0){
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "[数据采集中]账单信息获取成功");
			tracer.addTag("parser.telecom.crawler.getBillData.status"+temp,"账单信息获取成功");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "[数据采集中]账单信息获取完成");
			tracer.addTag("parser.telecom.crawler.getBillData.status"+temp,"账单信息获取失败");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	
	/*
	 * @Des 获取充值缴费记录
	 * 
	 */
	@Async
	public void getPaymentHistory(TaskMobile taskMobile) throws Exception{
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getPaymentHistory.taskid",taskMobile.getTaskid());
		int temp = 0;
		WebParam<TelecomHenanPayment> webParam = telecomHenanParser.getPaymentHistory(taskMobile);
		if(null != webParam){
			if(null != webParam.getHtmlPage()){
				TelecomHenanHtml telecomHenanHtml = new TelecomHenanHtml();
				telecomHenanHtml.setUrl(webParam.getUrl());
				telecomHenanHtml.setPageCount(1);
				telecomHenanHtml.setType("payment");
				telecomHenanHtml.setHtml(webParam.getHtmlPage().asXml());
				telecomHenanHtml.setTaskid(taskMobile.getTaskid());
				telecomHenanHtmlRepository.save(telecomHenanHtml);
				tracer.addTag("parser.telecom.crawler.getPaymentHistory.page",webParam.getHtmlPage().asXml());
				if(null != webParam.getList()){
					List<TelecomHenanPayment> payments = webParam.getList();
					telecomHenanPaymentRepository.saveAll(payments);
					tracer.addTag("parser.telecom.crawler.getPaymentHistory.list","充值缴费记录已入库"+payments.toString());
					temp++;
				}
			}
		}else{
			tracer.addTag("parser.telecom.crawler.getPaymentHistory.error","充值缴费记录页面异常");
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
	}
	
	
	/*
	 * @Des 获取短信详单
	 * 
	 */
	@Async
	public void getSMSDetails(TaskMobile taskMobile) throws Exception{
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getSMSDetails.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			WebParam<TelecomHenanSMSDetail> webParam = telecomHenanParser.getSMSDetails(taskMobile,i);
			if(null != webParam){
				if(null != webParam.getHtmlPage()){
					TelecomHenanHtml telecomHenanHtml = new TelecomHenanHtml();
					telecomHenanHtml.setUrl(webParam.getUrl());
					telecomHenanHtml.setPageCount(1);
					telecomHenanHtml.setType("SMSDetails"+i);
					telecomHenanHtml.setHtml(webParam.getHtmlPage().asXml());
					telecomHenanHtml.setTaskid(taskMobile.getTaskid());
					telecomHenanHtmlRepository.save(telecomHenanHtml);
					tracer.addTag("parser.telecom.crawler.getSMSDetails.page"+i,"<xmp>"+webParam.getHtmlPage().asXml()+"</xmp>");
					List<TelecomHenanSMSDetail> telecomHenanSMSDetails = webParam.getList();
					if(null != telecomHenanSMSDetails && telecomHenanSMSDetails.size() > 0){
						telecomHenanSMSDetailRepository.saveAll(telecomHenanSMSDetails);
						tracer.addTag("parser.telecom.crawler.getSMSDetails.list"+i,"短信详单已入库"+i+telecomHenanSMSDetails.toString());
						temp++;
					}
				}
			}else{
				tracer.addTag("parser.telecom.crawler.getSMSDetails.error"+i,"该月短信详单记录页面异常");
			}
		}
		if(temp > 0){
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "[数据采集中]短信详单记录获取成功");
			tracer.addTag("parser.telecom.crawler.getSMSDetails.status"+temp,"短信详单记录获取成功");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "[数据采集中]短信详单记录获取完成");
			tracer.addTag("parser.telecom.crawler.getSMSDetails.status"+temp,"短信详单记录为空");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/*
	 * @Des 获取通话详单
	 * 
	 */
	@Async
	public void getCallDetails(TaskMobile taskMobile) throws Exception{
		taskMobile = telecomCommonService.findtaskMobile(taskMobile.getTaskid());
		tracer.addTag("parser.telecom.crawler.getCallDetails.taskid",taskMobile.getTaskid());
		int temp = 0;
		for (int i = 0; i < 6; i++) {
			WebParam<TelecomHenanCallDetail> webParam = null;
			String qryMonth = getDateBefore("yyyyMM", i);
			try {
				webParam = telecomHenanParser.getCallDetails(taskMobile,i);
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
			if(null != webParam){
				if(null != webParam.getHtmlPage()){
					TelecomHenanHtml telecomHenanHtml = new TelecomHenanHtml();
					telecomHenanHtml.setUrl(webParam.getUrl());
					telecomHenanHtml.setPageCount(1);
					telecomHenanHtml.setType("CallDetails"+i);
					telecomHenanHtml.setHtml(webParam.getHtmlPage().asXml());
					telecomHenanHtml.setTaskid(taskMobile.getTaskid());
					telecomHenanHtmlRepository.save(telecomHenanHtml);
					tracer.addTag("parser.telecom.crawler.getCallDetails.page"+i,"<xmp>"+webParam.getHtmlPage().asXml()+"</xmp>");
					List<TelecomHenanCallDetail> telecomHenanCallDetails = webParam.getList();
					if(null != telecomHenanCallDetails && telecomHenanCallDetails.size() > 0){
						telecomHenanCallDetailRepository.saveAll(telecomHenanCallDetails);
						tracer.addTag("parser.telecom.crawler.getCallDetails.list"+i,"通话详单已入库"+i+telecomHenanCallDetails.toString());
						temp++;
					}
				}
			}else{
				tracer.addTag("parser.telecom.crawler.getCallDetails.error"+i,"该月通话详单记录页面异常");
			}
		}
		if(temp > 0){
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "[数据采集中]通话详单记录获取成功");
			tracer.addTag("parser.telecom.crawler.getCallDetails.status"+temp,"通话详单记录获取成功");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 201, "[数据采集中]通话详单记录获取完成");
			tracer.addTag("parser.telecom.crawler.getCallDetails.status"+temp,"通话详单记录为空");
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
