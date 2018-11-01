package app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.service.common.CalendarParamService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.tianjin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.tianjin")
public class TelecomTianjinService implements ICrawlerLogin {
	@Autowired
	private AsyncTianjinGetAllDataService asyncTianjinGetAllDataService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;

	/* (non-Javadoc)
	 * @see app.service.aop.ICrawler#getAllData(com.crawler.mobile.json.MessageLogin)
	 */
	@Async
	@Override
	public TaskMobile getAllData(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		Map<String, Future<String>> listfuture = new HashMap<>();   //判断异步爬取是否完成
		try {
			Future<String> future =asyncTianjinGetAllDataService.getUserInfo(taskMobile);
			listfuture.put("getUserInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getUserInfo", e.toString());
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),404, "数据采集中，用户信息已采集完成");
		}
		//附加业务
		try {
			Future<String> future =asyncTianjinGetAllDataService.getSrvBusiness(taskMobile);
			listfuture.put("getSrvBusiness", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getSrvBusiness", e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，附加业务信息已采集完成");
		}
		//增值业务
		try {
			Future<String> future =asyncTianjinGetAllDataService.getUiBusiness(taskMobile);
			listfuture.put("getUiBusiness", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getUiBusiness", e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),404, "数据采集中，增值业务信息已采集完成");
		}
		//积分信息
		try {
			//获取当前时间
			String presentDate = CalendarParamService.getPresentDate();
			//获取往前推5个月的第一天
			String firstMonthdate = CalendarParamService.getFirstMonthdate(5);
			Future<String> future =asyncTianjinGetAllDataService.getIntegra(taskMobile,firstMonthdate,presentDate);
			listfuture.put(firstMonthdate+"至"+presentDate+"getIntegra", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getIntegra", e.toString());
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),404, "数据采集中，积分信息已采集完成");
		}
		
		//注意：为了保证通话详单和短信详单能够成功爬取，将其方法调用放在了用户信息爬取方法中，用同步方法执行，虽然爬取慢，但是可以爬取完全
		/*//通话详单
		try {
			Future<String> future = asyncTianjinGetAllDataService.getAllCallRecord(taskMobile);
			listfuture.put("action.crawler.auth.getAllCallRecord", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getAllCallRecord.e",e.toString());
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),200, "数据采集中,通话详单已采集完成");
		}	
		//短信记录
		try {
			Future<String> future = asyncTianjinGetAllDataService.getAllSMSRecord(taskMobile);
			listfuture.put("action.crawler.auth.getAllSMSRecord", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getAllSMSRecord.e",e.toString());
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),200, "数据采集中,短信详单已采集完成");
		}	*/
//		缴费信息
		try {
			//获取当前时间
			String presentDate = CalendarParamService.getPresentDate();
			//获取往前推5个月的第一天
			String firstMonthdate = CalendarParamService.getFirstMonthdate(5);
			Future<String> future =asyncTianjinGetAllDataService.getChargeInfo(taskMobile,firstMonthdate,presentDate);
			listfuture.put(firstMonthdate+"至"+presentDate+"getChargeInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getChargeInfo", e.toString());
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),404, "数据采集中,充值记录已采集完成");
		}
		//月账单信息
		try {
			List<String> monthList = CalendarParamService.getMonthIncludeThis();
			for(int i=0;i<6;i++){
				String yearmonth = monthList.get(i);
				Future<String> future =asyncTianjinGetAllDataService.getMonthBill(taskMobile,yearmonth,i);
				listfuture.put(yearmonth+"getMonthBill", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getMonthBill", e.toString());
		}
//			
//		获取我的现状信息
		try {
			Future<String> future =asyncTianjinGetAllDataService.getCurrentSituation(taskMobile);
			listfuture.put("getCurrentSituation", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getCurrentSituation", e.toString());
		}
//			获取我的话费余额信息
		try {
			Future<String> future =asyncTianjinGetAllDataService.getBalance(taskMobile);
			listfuture.put("getBalance", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getBalance",e.toString());
		}
		//账户信息
		try {
			Future<String> future =asyncTianjinGetAllDataService.getAccountInfo(taskMobile);
			listfuture.put("getAccountInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getAccountInfo", e.toString());
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),404, "数据采集中，账户信息已采集完成");
		}
		
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskMobile.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		} catch (Exception e) {
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			tracer.addTag("listfuture--ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
		return taskMobile;
	}
	/* (non-Javadoc)
	 * @see app.service.aop.ICrawler#getAllDataDone(java.lang.String)
	 */
	@Override
	public TaskMobile getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see app.service.aop.ICrawlerLogin#login(com.crawler.mobile.json.MessageLogin)
	 */
	@Override
	public TaskMobile login(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}
}
