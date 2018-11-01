package app.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangHtml;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangProductInfo;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecord;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangAddvalueItemRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangPayMonthsRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangPointRecordRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangProductInfoRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangRealtimeFeeRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangRechargeRecordRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangSmsRecordRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangUserInfoRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecordRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.TelecomXinJiangHtmlUnit;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.xinjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.xinjiang")
public class TelecomUnitXinJiangService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomXinjiangUserInfoRepository telecomXinjiangUserInfoRepository;
	@Autowired
	private TelecomXinjiangProductInfoRepository telecomXinjiangProductInfoRepository;
	@Autowired
	private TelecomXinjiangRealtimeFeeRepository telecomXinjiangRealtimeFeeRepository;
	@Autowired
	private TelecomXinjiangHtmlRepository telecomXinjiangHtmlRepository;
	@Autowired
	private TelecomXinjiangRechargeRecordRepository telecomXinjiangRechargeRecordRepository;
	@Autowired
	private TelecomXinjiangSmsRecordRepository telecomXinjiangSmsRecordRepository;
	@Autowired
	private TelecomXinjiangPointRecordRepository telecomXinjiangPointRecordRepository;
	@Autowired
	private TelecomXinjiangVoiceRecordRepository telecomXinjiangVoiceRecordRepository;
	@Autowired
	private TelecomXinjiangAddvalueItemRepository telecomXinjiangAddvalueItemRepository;
	@Autowired
	private TelecomXinjiangPayMonthsRepository telecomXinjiangPayMonthsRepository;
	@Autowired
	private TelecomAsyncXinJiangService  telecomAsyncXinJiangService;
	@Autowired
	private TelecomXinJiangHtmlUnit telecomXinJiangHtmlUnit;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer; 
	// 抓取新疆用户个人信息及产品信息
	@Async
	public void getUserInfo(MessageLogin messageLogin){
		tracer.addTag("抓取新疆用户个人信息", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			int count=0;
			WebParam webParam = telecomXinJiangHtmlUnit.getUserInfo(messageLogin, taskMobile,count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("userInfo");
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);							
				}	
				// 保存用户信息
				if (null != webParam.getUserInfo()) {
					telecomXinjiangUserInfoRepository.save(webParam.getUserInfo());
					tracer.addTag("crawler.getUserInfo","用户信息入库  "+webParam.getUserInfo());			
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());				
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}else{					
					tracer.addTag("crawler.getUserInfo","用户信息爬取失败");
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());							
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}
			} else {
				tracer.addTag("crawler.getUserInfo", "用户信息爬取出错");
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());			
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("crawler.getUserInfo.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【用户信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			e.printStackTrace();
		}
		tracer.addTag("抓取新疆用户个人信息完成", messageLogin.getTask_id());
   }
	// 抓取新疆账户信息
	@Async
	public void getAccountInfo(MessageLogin messageLogin){
		tracer.addTag("抓取新疆账户信息", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			int count=0;
			WebParam webParam = telecomXinJiangHtmlUnit.getAccountInfo(messageLogin, taskMobile,count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("accountInfo");
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);								
				}	
				// 保存产品信息
				if (null != webParam.getProductInfo()) {
					telecomXinjiangProductInfoRepository.save(webParam.getProductInfo());	
					tracer.addTag("crawler.getAccountInfo","账户信息入库"+webParam.getProductInfo());					
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				} else {
					tracer.addTag("crawler.getAccountInfo","账户信息爬取结果为空");
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());									
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());			
				}
			} else {
				tracer.addTag("crawler.getAccountInfo","账户信息爬取失败");
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());						
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());			
			}
		} catch (Exception e) {
			tracer.addTag("crawler.getAccountInfo","更改用户状态------500---  ");
			tracer.addTag("crawler.getAccountInfo.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账户信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_ACCOUNT_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());												
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			e.printStackTrace();
		}	
		tracer.addTag("抓取新疆账户信息完成", messageLogin.getTask_id());
   }
	//抓取新疆用户实时话费信息
	@Async
	public void getRealtimefee(MessageLogin messageLogin) {
		tracer.addTag("抓取新疆用户实时话费信息", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebParam webParam =new WebParam();
		try {
			int count=0;
			webParam = telecomXinJiangHtmlUnit.getRealtimefee(messageLogin, taskMobile,count);
			if (null !=webParam.getHtml()) {
					TelecomXinjiangHtml   telecomXinjiangHtml=new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("realtimefee");
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);							
				}		
				//保存实时话费信息
				if (null !=webParam.getRealtimeFees() && !webParam.getRealtimeFees().isEmpty()) {
					telecomXinjiangRealtimeFeeRepository.saveAll(webParam.getRealtimeFees());				
					tracer.addTag("crawler.getRealtimeFees","实时话费信息入库"+webParam.getRealtimeFees());					
				}else{				
					tracer.addTag("crawler.getRealtimeFees","实时话费信息爬取结果为空");
				}		
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		} catch (Exception e) {
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			tracer.addTag("crawler.getRealtimeFees.Exception",e.toString());
			e.printStackTrace();
		}		
		tracer.addTag("抓取新疆用户实时话费信息完成", messageLogin.getTask_id());	
	}
	//抓取新疆电信充值记录
	@Async
	public void getRechargeRecord(MessageLogin messageLogin){
		tracer.addTag("抓取新疆电信充值记录", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			TelecomXinjiangProductInfo productInfo = telecomXinjiangProductInfoRepository.findTopByTaskid(messageLogin.getTask_id());
			tracer.addTag("crawler.proAccount",productInfo.toString());
			if (null != productInfo) {
				String proAccount = productInfo.getProAccount();
				tracer.addTag("crawler.proAccount",productInfo.toString());
				int temp=0;
				for (int i = 0; i < 6; i++){		
				String month=getDateBefore("yyyyMM",i);
				int count = 0;
				WebParam webParam = telecomXinJiangHtmlUnit.getRechargeRecord(messageLogin, taskMobile, proAccount,month, count);			
				 if (null != webParam.getHtml()) {
						TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
						telecomXinjiangHtml.setPageCount(1);
						telecomXinjiangHtml.setHtml(webParam.getHtml());
						telecomXinjiangHtml.setType("rechargeRecord");
						telecomXinjiangHtml.setUrl(webParam.getUrl());
						telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
						telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);					
					}
					if (null != webParam.getRechargeRecords() && !webParam.getRechargeRecords().isEmpty()) {
						telecomXinjiangRechargeRecordRepository.saveAll(webParam.getRechargeRecords());
						temp++;		
						tracer.addTag("crawler.getRechargeRecord"+month, month+"充值记录信息入库"+webParam.getRechargeRecords());
					} else {				
						tracer.addTag("crawler.getRechargeRecord"+month,  month+"月充值记录信息爬取结果为空");
					}
				}
				
				if(temp>0){
					tracer.addTag("crawler.getRechargeRecord.temp========",temp+"");					
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());											
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());					
				}else{					
					tracer.addTag("crawler.getRechargeRecord.temp========201",temp+"");	
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());											
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}
			}
		} catch (Exception e) {
			tracer.addTag("crawler.getRechargeRecord.Exception", e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【缴费信息】采集成功",StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());											
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());			
			e.printStackTrace();
		}
		tracer.addTag("抓取新疆电信充值记录完成", messageLogin.getTask_id());
	}
	//抓取新疆电信短信详情
	@Async
	public void getSmsRecord(MessageLogin messageLogin)   {
		tracer.addTag("抓取新疆电信短信信息", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		try {
			int temp=0;
			for (int i = 0; i < 6; i++) {
			int count=0;
			String month=getQueryMonth(i);
			WebParam webParam = telecomXinJiangHtmlUnit.getSmsRecord(messageLogin, taskMobile,month,"0",count);
				if (null !=webParam.getHtml()) {
					TelecomXinjiangHtml   telecomXinjiangHtml=new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("smsRecord");
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);
				}				
				if (null != webParam.getSmsRecords() && !webParam.getSmsRecords().isEmpty()) {
					telecomXinjiangSmsRecordRepository.saveAll(webParam.getSmsRecords());
					temp++;
					tracer.addTag("crawler.getSmsRecord"+month,month+"月短信明细信息入库"+webParam.getSmsRecords());
				}else{				
					tracer.addTag("crawler.getSmsRecord"+month,month+"月短信明细信息为空");
				}
				if (null !=webParam.getMaxPage()) {
					int maxPage=Integer.parseInt(webParam.getMaxPage());  
					tracer.addTag("crawler.getSmsRecord.maxPage"+month,month+"月 共"+maxPage+"页");
					if (maxPage>1) {
						for(int j=1;j<maxPage;j++ ){
						   telecomAsyncXinJiangService.getSmsRecordOtherPage(messageLogin, taskMobile,month,i+"");											
						}
					}	
				}					
			 }
		
			if(temp>0){
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【短信信息】采集完成", StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());				
			}else{
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【短信信息】采集完成", StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());			
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("crawler.getSmsRecord.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【短信信息】采集完成", StatusCodeEnum.TASKMOBILE_CRAWLER_SMS_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());						
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());			
			e.printStackTrace();
		}
		tracer.addTag("抓取新疆电信短信信息完成", messageLogin.getTask_id());
	}
	
	@Async
	public Future<String>  getVoiceRecordByMonth(MessageLogin messageLogin,TaskMobile taskMobile,int i) {
		tracer.addTag("getVoiceRecordByMonth.start", messageLogin.getTask_id());
	    taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		int temp=0;
		try {
			String month=getQueryMonth(i);
			int count=0;
			WebParam webParam = telecomXinJiangHtmlUnit.getVoiceRecord(messageLogin, taskMobile,month,"0",count);	
				if (null != webParam.getHtml()) {
					TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("voiceRecord"+month);
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);				
				}	
				if (null != webParam.voiceRecords &&  !webParam.voiceRecords.isEmpty()) {
					List<TelecomXinjiangVoiceRecord> list=telecomXinjiangVoiceRecordRepository.saveAll(webParam.getVoiceRecords());
					temp++;
					tracer.addTag("crawler.getVoiceRecord 月"+month,month+"月语言详单细信息入库成功 共"+list.size()+"条数据");	
				}else{					
					tracer.addTag("crawler.getVoiceRecord 月"+month,month+"语言详单信息爬取失败");				
				}				
				if (null != webParam.getMaxPage()) {
					int maxPage = Integer.parseInt(webParam.getMaxPage());
					tracer.addTag("crawler.getVoiceRecordOtherPage maxPage：" + month , maxPage + "");
					if (maxPage > 1) {						
						Map<String, Future<String>> future_content_map = new ConcurrentHashMap<String, Future<String>>(); 
						tracer.addTag("getVoiceRecordOtherPage begin","getVoiceRecordOtherPage begin");
						for (int j = 1; j < maxPage; j++) {
							Future<String> future = telecomAsyncXinJiangService.getVoiceRecordOtherPage(messageLogin, taskMobile, month,j + "");							
							future_content_map.put("otherpage"+j, future);						
						}			
						boolean isAlldone = true;	
						while (isAlldone) {
							for (Map.Entry<String, Future<String>> entry : future_content_map.entrySet()) {
								if (entry.getValue().isDone()) { // 判断是否执行完毕
									future_content_map.remove(entry.getKey());
								}
							} 
							if (future_content_map.size() <= 0) {
								tracer.addTag("crawler.getVoiceRecordOtherPage.done " + month + "", month + "月" );
								isAlldone = false;							
								break;								
							} 							
							Thread.sleep(3500);
						}				
					}			
				}			
		} catch (Exception e) {			
			tracer.addTag("crawler.getVoiceRecord.Exception",e.toString());
			e.printStackTrace();
		}	
		if (temp>0) {
			tracer.addTag("getVoiceRecordByMonth====end==此月数据采集成功====","此月数据采集成功");
			return new AsyncResult<>("数据采集成功");	
		}else{
			tracer.addTag("getVoiceRecordByMonth====end==此月数据采集结果为空","此月数据采集结果为空");
			return new AsyncResult<>("数据采集完成");	
		}		
	}
	// 抓取新疆电信语言详单
	@Async
	public void getVoiceRecord(MessageLogin messageLogin) {
		tracer.addTag("抓取新疆电信语言详单", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		int temp=0;
		try {		
			for (int i = 0; i < 6; i++) {
			int count=0;
			String month=getQueryMonth(i);
			WebParam webParam = telecomXinJiangHtmlUnit.getVoiceRecord(messageLogin, taskMobile,month,"0",count);	
				if (null != webParam.getHtml()) {
					TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("voiceRecord"+month);
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);				
				}	
				if (null != webParam.voiceRecords &&  !webParam.voiceRecords.isEmpty()) {
					telecomXinjiangVoiceRecordRepository.saveAll(webParam.getVoiceRecords());
					temp++;
					tracer.addTag("crawler.getVoiceRecord 月"+month,month+"月语言详单细信息入库成功");	
				}else{					
					tracer.addTag("crawler.getVoiceRecord 月"+month,month+"语言详单信息爬取失败");				
				}
				if (null != webParam.getMaxPage()) {
					int maxPage = Integer.parseInt(webParam.getMaxPage());
					tracer.addTag("crawler.getVoiceRecord maxPage=" + month + "月", maxPage + "");
					if (maxPage > 1) {
						for (int j = 1; j < maxPage; j++) {
							telecomAsyncXinJiangService.getVoiceRecordOtherPage(messageLogin, taskMobile, month,j + "");
							tracer.addTag("crawler.getVoiceRecord " + month + i + "",
									month + "月" + j + "页");
						}
					}
				}
			}
			if(temp>0){
				tracer.addTag("数据采集成功需要更新状态前"+System.currentTimeMillis(),"=====200====="+taskMobile.toString());	
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【语音信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());										
				tracer.addTag("数据采集成功需要更新状态后"+System.currentTimeMillis(),"=====300====="+taskMobile.toString());	
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}else{
				tracer.addTag("数据采集结果为空","=====201=====");	
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【语音信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());										
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());				
			}
		} catch (Exception e) {
			tracer.addTag("数据采集成功需要更新状态出错啦","=====500=====");	
			tracer.addTag("crawler.getVoiceRecord.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【语音信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CALL_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());								
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			e.printStackTrace();
		}		
		tracer.addTag("抓取新疆电信语言详单完成", messageLogin.getTask_id());
	}
	// 抓取新疆电信月账单
	@Async
	public void getPaymonths(MessageLogin messageLogin) {
		tracer.addTag("抓取新疆电信月账单", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());	
		try {
			int temp=0;
			for (int i = 1; i <=6; i++) {
		    String month=getDateBefore("yyyyMM",i);	
			int count=0;
			WebParam webParam = telecomXinJiangHtmlUnit.getPayMonths(messageLogin, taskMobile, month, count);		
			if (null != webParam.getHtml()) {
				TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
				telecomXinjiangHtml.setPageCount(1);
				telecomXinjiangHtml.setHtml(webParam.getHtml());
				telecomXinjiangHtml.setType("paymonth"+month);
				telecomXinjiangHtml.setUrl(webParam.getUrl());
				telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
				telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);				
			}	
			if (null != webParam.getPayMonths() &&  !webParam.getPayMonths().isEmpty()) {
				telecomXinjiangPayMonthsRepository.saveAll(webParam.getPayMonths());		
				temp++;
				tracer.addTag("crawler.getPaymonths 月"+month,month+"月账单详单细信息入库"+webParam.getPayMonths());					
			}else{
				tracer.addTag("crawler.getPaymonths 月"+month,month+"账单信息爬取失败");				
			 }
			}
			if(temp>0){
				tracer.addTag("数据采集成功需要更新状态","====200=======");	
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账单信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());										
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			}else{
				tracer.addTag("数据采集成功需要更新状态","====201=======");	
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账单信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());										
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			}
		} catch (Exception e) {
			tracer.addTag("数据采集成功需要更新状态出错啦","====500=======");	
			tracer.addTag("crawler.getPaymonths.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【账单信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_CHECK_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());						
				
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());			
			e.printStackTrace();
		}		
		tracer.addTag("抓取新疆电信月账单详单完成", messageLogin.getTask_id());
	}
	// 抓取新疆电信积分信息
	@Async
	public void getPoint(MessageLogin messageLogin){
		tracer.addTag("抓取新疆电积分信息", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		try {
			int count=0;
			WebParam webParam = telecomXinJiangHtmlUnit.getPiont(messageLogin, taskMobile,count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("point");
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);												
				}	
				//修改user表积分
				if (webParam.getHtml().contains("useablePoint")) {
					JSONObject jsonInsurObjs = JSONObject.fromObject(webParam.getHtml());
					if (null !=jsonInsurObjs && jsonInsurObjs.toString().contains("useablePoint")) {					
						String useablePoint = jsonInsurObjs.getString("useablePoint");
						String invalidPoint = jsonInsurObjs.getString("invalidPoint");	
						telecomXinjiangUserInfoRepository.updateXinjiangUserInfo(taskMobile.getTaskid(), useablePoint, invalidPoint);
						tracer.addTag("crawler.getPoint","用户表修改成功，当前积分信息入库");
					}		
				}			
				if (null != webParam.getPoints() && !webParam.getPoints().isEmpty()) {			
					telecomXinjiangPointRecordRepository.saveAll(webParam.getPoints());
					tracer.addTag("crawler.getPoint","积分信息入库"+webParam.getPoints());			
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}else{				
					tracer.addTag("数据采集成功需要更新状态","---------------201--------");	
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());					
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				}	
			}else{
				tracer.addTag("数据采集成功需要更新状态","----------------------500-");	
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());				
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("数据采集成功需要更新状态出错啦","---500-------------------");
			tracer.addTag("crawler.getPoint.Exception",e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【积分信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_INTEGRA_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());			
			e.printStackTrace();
		}		
		tracer.addTag("抓取新疆电积分信息完成", messageLogin.getTask_id());
	}
	// 抓取新疆电信已经开通的增值业务及功能
	@Async
	public void getAddvalueItems(MessageLogin messageLogin) throws Exception  {
		tracer.addTag("抓取新疆电信已经开通的增值业务及功能", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		int count=0;
		WebParam webParam;
		try {
			webParam = telecomXinJiangHtmlUnit.getAddvalueItem(messageLogin, taskMobile,count);
			if (null != webParam) {
				if (null != webParam.getHtml()) {
					TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
					telecomXinjiangHtml.setPageCount(1);
					telecomXinjiangHtml.setHtml(webParam.getHtml());
					telecomXinjiangHtml.setType("addvalueItem");
					telecomXinjiangHtml.setUrl(webParam.getUrl());
					telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
					telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);
				}
				if (null != webParam.getAddvalueItems() && !webParam.getAddvalueItems().isEmpty()) {
					telecomXinjiangAddvalueItemRepository.saveAll(webParam.getAddvalueItems());
					tracer.addTag("crawler.getAddvalueItems增值业务信息入库", webParam.getAddvalueItems().toString());	
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【增值业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 200, messageLogin.getTask_id());
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
				} else {
					tracer.addTag("crawler.getAddvalueItems", "采集结果为空");
					crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【增值业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 201, messageLogin.getTask_id());					
					crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());				
				}
			}else{
				tracer.addTag("crawler.getAddvalueItems", "增值业务信息爬失败");
				crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【增值业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());
				
				crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
			}
		} catch (Exception e) {
			tracer.addTag("crawler.getAddvalueItems.Exception", e.toString());
			crawlerStatusMobileService.changeCrawlerStatus("数据采集中，【增值业务信息】采集成功", StatusCodeEnum.TASKMOBILE_CRAWLER_BUSINESS_MSG_SUCCESS.getPhase(), 500, messageLogin.getTask_id());			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
			e.printStackTrace();
		}
		tracer.addTag("抓取新疆电信已经开通的增值业务及功能完成", messageLogin.getTask_id());
	}
	public static String getQueryMonth(int i) throws Exception {
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			enddate = today;
		}
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + "年" + monthint + "月";
        return month;
	}
	
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
}