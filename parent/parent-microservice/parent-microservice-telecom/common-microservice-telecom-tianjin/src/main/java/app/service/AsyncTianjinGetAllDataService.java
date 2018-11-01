package app.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinAccountInfo;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinBalance;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinBusiness;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinCallRecord;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinChargeInfo;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinCurrentSituation;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinHtml;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinIntegra;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinMonthBill;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinSMSRecord;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinAccountInfoRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinBalanceRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinBusinessRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinCallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinChargeInfoRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinCurrentSituationRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinIntegraRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinMonthBillRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinSMSRecordRepository;
import com.microservice.dao.repository.crawler.telecom.tianjin.TelecomTianjinUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.TelecomTianjinParser;
import app.retry.TelecomTianJinRetryService;
import app.service.common.CalendarParamService;
import app.service.common.LoginAndGetCommonService;
/**
 * 
 * @Description: 所有信息爬取
 * @author sln
 * @date 2007年9月14日
 */
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.tianjin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.tianjin")
public class AsyncTianjinGetAllDataService {
	@Autowired
	private TelecomTianjinHtmlRepository telecomTianjinHtmlRepository;
	@Autowired
	private TelecomTianjinIntegraRepository telecomTianjinIntegraRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private LoginAndGetCommonService loginAndGetCommonService;
	@Autowired
	private TelecomTianjinParser telecomTianjinParser;
	@Autowired
	private TelecomTianjinBusinessRepository telecomTianjinBusinessRepository;
	@Autowired
	private TelecomTianjinUserInfoRepository telecomTianjinUserInfoRepository;
	@Autowired
	private TelecomTianjinChargeInfoRepository telecomTianjinChargeInfoRepository;
	@Autowired
	private TelecomTianjinMonthBillRepository telecomTianjinMonthBillRepository;
	@Autowired
	private TelecomTianjinBalanceRepository telecomTianjinBalanceRepository;
	@Autowired
	private TelecomTianjinCurrentSituationRepository telecomTianjinCurrentSituationRepository;
	@Autowired
	private TelecomTianjinAccountInfoRepository telecomTianjinAccountInfoRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TelecomTianjinCallRecordRepository telecomTianjinCallRecordRepository;
	@Autowired
	private TelecomTianjinSMSRecordRepository telecomTianjinSMSRecordRepository;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	@Autowired
	private TelecomTianJinRetryService retryService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	public static int callRecordRetryCount;
	public static int smsRecordRetryCount;
	
	@Async
	public Future<String> getUserInfo(TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://tj.189.cn/tj/service/manage/modifyUserInfo.action?fastcode=02241349&amp;cityCode=tj";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			if(page!=null){
				if(html.contains("访问失败") || html.contains("抱歉，您没有权限访问此功能或您尚未登录") || html.contains("系统正忙，请稍后再试")){
					//无查询记录或者是出现其他情况
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("用户信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getUserInfo.html", "用户信息源码页无可查询数据，或系统繁忙，暂不提供服务");
					crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),200, "用户信息源码页无可查询数据，或系统繁忙，暂不提供服务");
				}else{
					//保存页面信息
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("用户信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getUserInfo.html", "用户信息源码页已经入库");
					WebParam<TelecomTianjinUserInfo> webParam= telecomTianjinParser.userInfoParser(taskMobile,html);
					if(null!=webParam.getTelecomTianjinUserInfo()){
						telecomTianjinUserInfoRepository.save(webParam.getTelecomTianjinUserInfo());
						tracer.addTag("action.crawler.getUserInfo", "用户信息已入库");
						crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode(), "数据采集中，用户信息已采集完成");
					}else{
						crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),201, "数据采集中，用户信息已采集完成");
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getUserInfo.e", e.toString());
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),404, "数据采集中，用户信息已采集完成");
		}
		//将如下代码放在此处的原因，避免用户基本信息采集过程中出现异常或者其他状况，导致无法更新最终状态
		//用调研账号调研的时候，亲情号没有开通设置，故为了最终的状态更新，在此处将亲情号状先设置为默认值：200
		crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，亲情号信息无可采集记录或此账号未设置相关信息");
		//===================================================================================
		//在个人信息的爬取过程中，爬取通话记录和短信记录：
		//通话详单
		try {
			getAllCallRecord(taskMobile);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getAllCallRecord.e",e.toString());
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),200, "数据采集中,通话详单已采集完成");
		}	
		//短信记录
		try {
			getAllSMSRecord(taskMobile);
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getAllSMSRecord.e",e.toString());
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),200, "数据采集中,短信详单已采集完成");
		}	
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取首页我的现状信息
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getCurrentSituation(TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String urlHuaFei="http://www.189.cn/dqmh/order/getHuaFei.do";
			String urlTaoCan="http://www.189.cn/dqmh/order/getTaoCan.do";
			Page pageHuaFei = loginAndGetCommonService.getPage(urlHuaFei, webClient);
			Page pageTaoCan = loginAndGetCommonService.getPage(urlTaoCan, webClient);
			if(null!=pageHuaFei && null!=pageTaoCan){
				String htmlHuaFei = pageHuaFei.getWebResponse().getContentAsString();
				String htmlTaoCan=pageTaoCan.getWebResponse().getContentAsString();
				//保存页面信息
				TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
				htmlStore.setTaskid(taskMobile.getTaskid());
				htmlStore.setType("我的现状信息源码页");
				htmlStore.setPageCount(1);
				htmlStore.setUrl("话费的url "+urlHuaFei+" 华丽丽的分界线 "+" 套餐的url "+urlTaoCan);
				htmlStore.setHtml("话费的html "+htmlHuaFei+" 信息分界线 "+" 套餐的html "+htmlTaoCan);
				List<String> monthList= CalendarParamService.getMonthIncludeThis();
				htmlStore.setYearmonth(monthList.get(0));
				telecomTianjinHtmlRepository.save(htmlStore);
				tracer.addTag("action.crawler.getCurrentSituation.html", "我的现状信息源码页已经入库");
				WebParam<TelecomTianjinCurrentSituation> webParam= telecomTianjinParser.currentSituationParser(taskMobile,htmlHuaFei,htmlTaoCan);
				if(null!=webParam.getTelecomTianjinCurrentSituation()){
					telecomTianjinCurrentSituationRepository.save(webParam.getTelecomTianjinCurrentSituation());
					tracer.addTag("action.crawler.getCurrentSituation", "我的现状信息已入库");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getCurrentSituation.e", e.toString());
		}
		return new AsyncResult<String>("200");
	}
	@Async
	//月初或者月末期间账户信息是无法采集完全的，故此处决定根据返回值信息进行判断
	public Future<String> getAccountInfo(TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://tj.189.cn/tj/service/jf/integralSearch.action";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			if(page!=null){
				String html = page.getWebResponse().getContentAsString();
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					if(html.contains("访问失败") || html.contains("抱歉，您没有权限访问此功能或您尚未登录") ||  html.contains("操作失败") || html.contains("系统正忙，请稍后再试")){
						//无查询记录或者是出现其他情况
						TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("账户信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomTianjinHtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getAccountInfo.html", "账户信息源码页无可查询数据，或系统繁忙，暂不提供服务");
						crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),201, "数据采集中，账户信息已采集完成");
					}else{
						//保存页面信息
						TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("账户信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomTianjinHtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getAccountInfo.html", "账户信息源码页已经入库");
						WebParam<TelecomTianjinAccountInfo> webParam= telecomTianjinParser.accountInfoParser(taskMobile,html);
						if(null!=webParam.getTelecomTianjinAccountInfo()){
							telecomTianjinAccountInfoRepository.save(webParam.getTelecomTianjinAccountInfo());
							tracer.addTag("action.crawler.getAccountInfo", "账户信息已入库");
							crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode(), "数据采集中，账户信息已采集完成");
							
						}else{
							crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),201, "数据采集中，账户信息已采集完成");
						}
					}
				}else{
					//无查询记录或者是出现其他情况
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("账户信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getAccountInfo.html", "账户信息源码页无可查询数据，或系统繁忙，暂不提供服务");
					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),201, "数据采集中，账户信息已采集完成");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getAccountInfo.e", e.toString());
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),404, "数据采集中，账户信息已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	@Async
	public Future<String> getBalance(TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://tj.189.cn/tj/service/bill/balanceQuery.action?requestFlag=asynchronism";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			if(page!=null){
				if(html.contains("访问失败") || html.contains("抱歉，您没有权限访问此功能或您尚未登录") || html.contains("操作失败") || html.contains("系统正忙，请稍后再试")){
					//无查询记录或者是出现其他情况
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("余额信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getBalance.html", "余额信息源码页无可查询数据，或系统繁忙，暂不提供服务");
					crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(),201, "数据采集中，余额信息已采集完成");
				}else{
					//保存页面信息
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("余额信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getBalance.html", "余额信息源码页已经入库");
					WebParam<TelecomTianjinBalance> webParam= telecomTianjinParser.BalanceParser(taskMobile,html);
					if(null!=webParam.getTelecomTianjinBalance()){
						telecomTianjinBalanceRepository.save(webParam.getTelecomTianjinBalance());
						tracer.addTag("action.crawler.getBalance", "余额信息已入库");
						crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，余额信息已采集完成");
					}else{
						crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，余额信息已采集完成");
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getBalance.e",e.toString());
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取本月在内的月账单数据
	 * @param taskMobile
	 * @param yearmonth
	 * @param i
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getMonthBill(TaskMobile taskMobile, String yearmonth, int i)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://tj.189.cn/tj/service/bill/queryBillInfo.action?billingCycle1="+yearmonth+"&dataCode=009";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			if(page!=null){
				String html =page.getWebResponse().getContentAsString();	
				if(!html.contains("用户账单查询记录为空") && !html.contains("没有查询到账单数据") && !html.contains("访问失败")
						&& !html.contains("您没有权限访问此功能或您尚未登录") && !html.contains("操作失败")	&& !html.contains("系统正忙，请稍后再试"))
				{
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(yearmonth+"月账单信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					htmlStore.setYearmonth(yearmonth);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getMonthBill.html"+yearmonth, "月账单信息源码页已经入库");
					List<TelecomTianjinMonthBill> list= telecomTianjinParser.getMonthBill(taskMobile,html,yearmonth);
					if(null != list){
						telecomTianjinMonthBillRepository.saveAll(list);
						tracer.addTag("action.crawler.getMonthBill"+yearmonth, "月账单信息已入库");
					}
				}else{
					//无查询记录或者是出现其他情况
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType(yearmonth+"月账单信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml("<xmp>"+html+"</xmp>");
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getMonthBill.html"+yearmonth, "月账单信息源码页无可查询数据，或系统繁忙，暂不提供服务");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getMonthBill.e", e.toString());
		}
		return new AsyncResult<String>("200");
	}
	//爬取六个月的充值记录
	@Async
	public Future<String> getChargeInfo(TaskMobile taskMobile, String firstMonthdate, String presentDate)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://tj.189.cn/tj/service/bill/queryPaymentRecord.action?paymentHistoryQueryIn.startDate="+firstMonthdate+"&paymentHistoryQueryIn.endDate="+presentDate+"&queryWebCard.page=1";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			if(page!=null){
				String html = page.getWebResponse().getContentAsString();
				if(!html.contains("访问失败") && !html.contains("您没有权限访问此功能或您尚未登录") && !html.contains("操作失败")	&& !html.contains("系统正忙，请稍后再试"))	{
					//保存页面信息
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("六个月充值记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getChargeInfo.html", "充值记录源码页已经入库");
					List<TelecomTianjinChargeInfo> list= telecomTianjinParser.chargeInfoParser(taskMobile,html,firstMonthdate,presentDate);
					if(null!=list && list.size()>0){
						telecomTianjinChargeInfoRepository.saveAll(list);
						tracer.addTag("action.crawler.getChargeInfo", "充值记录已入库");
						crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode(), "数据采集中，充值记录已采集完成");
					}else{
						crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),201, "数据采集中，充值记录已采集完成");
					}
				}else{
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("六个月充值记录源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getChargeInfo.html", "充值记录源码页无可查询数据，或系统繁忙，暂不提供服务");
					crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),201, "数据采集中,充值记录已采集完成");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getChargeInfo.e", e.toString());
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),404, "数据采集中,充值记录已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 一次爬取6个月的积分信息，可以跨月查询
	 * @param taskMobile
	 * @param presentDate 
	 * @param firstMonthdate 
	 * @param retryCount
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getIntegra(TaskMobile taskMobile, String firstMonthdate, String presentDate)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url = "http://tj.189.cn/tj/service/jf/integralHistorySearch.action?pointHistoryIn.startTime="+firstMonthdate+"&pointHistoryIn.endTime="+presentDate+"&requestFlag=asynchronism";
			Page tPage = loginAndGetCommonService.getPage(url, webClient);
			if(tPage!=null){
				String html = tPage.getWebResponse().getContentAsString();
				if(!html.contains("访问失败") && !html.contains("您没有权限访问此功能或您尚未登录") && !html.contains("操作失败")	&& !html.contains("系统正忙，请稍后再试"))	{
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("六个月积分信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getIntegra.html","积分信息源码页已经入库");
					List<TelecomTianjinIntegra> list =telecomTianjinParser.integraParser(html,taskMobile,firstMonthdate,presentDate);
					if(null != list){
						telecomTianjinIntegraRepository.saveAll(list);
						tracer.addTag("action.crawler.getIntegra","积分信息已入库");
						crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),200, "数据采集中，积分信息已采集完成");
					}else{
						//更新task_mobile表的相关信息	
						crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),201, "数据采集中，积分信息已采集完成");
					}
				}else{
					TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("六个月积分信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl(url);
					htmlStore.setHtml(html);
					telecomTianjinHtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getIntegra.html", "积分信息源码页无可查询数据");
					crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，无积分记录或此时处于月初，无法查询指定月积分");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getIntegra.e", e.toString());
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),404, "数据采集中，积分信息已采集完成");
		}
		return new AsyncResult<String>("200");
	}
//	@Async   //不能分月和分页异步，否则爬取不到数据，故决定，整体异步
	public Future<String> getAllCallRecord(TaskMobile taskMobile) throws Exception{
		Thread.sleep(3000);  //抛出异常，为了避免当前月无法正常响应数据，先sleep段时间
		for(int i=0;i<6;i++){
			String firstMonthdate = CalendarParamService.getFirstMonthdate(i);
			String lastMonthdate = CalendarParamService.getLastMonthdate(i);
			try {
				Thread.sleep(3000);
				getCallRecord(taskMobile,firstMonthdate,lastMonthdate);
			} catch (Exception e) {
				tracer.addTag("action.crawler.getAllCallRecord.e"+" "+firstMonthdate+"-"+lastMonthdate,e.toString());
			}
		}
		//6个月爬取完成之后再更新最后的状态，哪怕实际上没有任何数据
		crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),200, "数据采集中,通话详单已采集完成");
		return new AsyncResult<String>("200");
	}
	public void getCallRecord(TaskMobile taskMobile, String firstMonthdate, String lastMonthdate) {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setTimeout(5*10000);
			webClient.getOptions().setJavaScriptEnabled(false);
			String initUrl="http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=1&sRandomCode=516509&randInputValue=2431&startTime="+firstMonthdate+"&endTime="+lastMonthdate+"&exFormat=1";
			WebRequest webRequest = new WebRequest(new URL(initUrl), HttpMethod.GET);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page page0 = webClient.getPage(webRequest);
			if(page0!=null){
				String html0 =page0.getWebResponse().getContentAsString();
				TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
				htmlStore.setTaskid(taskMobile.getTaskid());
				htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"第1页(默认)通话详单源码页");
				htmlStore.setPageCount(1);  //存储所属的页码
				htmlStore.setUrl(initUrl);
				htmlStore.setHtml(html0);
				htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
				telecomTianjinHtmlRepository.save(htmlStore);
				tracer.addTag(firstMonthdate+"(第1页)默认通话详单源码页—隐藏域中含有通话记录总页数",firstMonthdate+"至"+lastMonthdate+"默认通话详单源码页已入库");
				if(html0.contains("主叫号码")){  //如果页面正常响应，将会包含这个字段
					callRecordRetryCount=0;  //将数据归0,避免影响下个循环的使用
					//不管该查询区间是否只包含一页数据，此处都先将第一页数据解析存储
					//只有一页数据(就是默认页面数据，这种情况，网站调研时发现隐藏域相关字段是0，0表示只有一页数据)
					List<TelecomTianjinCallRecord> list= telecomTianjinParser.callRecordParser(taskMobile,html0,firstMonthdate,lastMonthdate);
					if(null != list && list.size()>1){
						telecomTianjinCallRecordRepository.saveAll(list);
						tracer.addTag("action.crawler.getCallRecord", firstMonthdate+"至"+lastMonthdate+"第1页通话详单已入库");
						taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第1页通话详单已采集完成",taskMobile.getTaskid());
					}else{
						taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第1页通话详单无数据可采集",taskMobile.getTaskid());
					}
					//之后再从第二页开始请求链接，继续解析
					Document doc = Jsoup.parse(html0);
					int totalPageNum = Integer.parseInt(doc.getElementById("detailTotalPage").val());
					String eachPageUrl="";
					String resultHtml="";
					if(totalPageNum>0){  //多页数据(重新访问新页面的时候从第二页开始,由于默认的第一页已经解析完毕，故此处代码不用考虑else条件)
						for(int curPageNo=2;curPageNo<=totalPageNum;curPageNo++){
							eachPageUrl="http://tj.189.cn/tj/service/bill/billDetailAjaxQuery.action?pageNo="+curPageNo+"&startTime="+firstMonthdate+"&endTime="+lastMonthdate+"&billDetailType=1&is1k2x_ret=undefined";
							try {
								Thread.sleep(1500);
								resultHtml=retryService.getEachCallPageSource(initUrl,eachPageUrl,taskMobile,firstMonthdate,lastMonthdate,curPageNo);
								//不管返回的结果如何，都将源码存储到数据库
								htmlStore = new TelecomTianjinHtml();
								htmlStore.setTaskid(taskMobile.getTaskid());
								htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页通话详单源码页");
								htmlStore.setPageCount(curPageNo);  //存储所属的页码
								htmlStore.setUrl(eachPageUrl);
								htmlStore.setHtml(resultHtml);
								htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
								htmlStore.setTotalpage(totalPageNum);  //存储总页码
								telecomTianjinHtmlRepository.save(htmlStore);
								tracer.addTag("action.crawler.getCallRecord.html"+curPageNo, firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页通话详单源码页已经入库");
								if(null!=resultHtml && resultHtml.contains("主叫号码")){
									list= telecomTianjinParser.callRecordParser(taskMobile,resultHtml,firstMonthdate,lastMonthdate);
									if(null != list && list.size()>1){
										telecomTianjinCallRecordRepository.saveAll(list);
										tracer.addTag("action.crawler.getCallRecord", firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页通话详单已入库");
										taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页通话详单已采集完成",taskMobile.getTaskid());
									}else{
										taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页通话详单无数据可采集",taskMobile.getTaskid());
									}
								}else{
									taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页通话详单无数据可采集",taskMobile.getTaskid());
								}
							} catch (Exception e) {
								MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
										"通话记录",firstMonthdate+"-"+lastMonthdate+"第"+curPageNo+"页",taskMobile.getCarrier(),taskMobile.getCity(),
										"exception", e.toString(), 1);		
								mobileDataErrRecRepository.save(m);
							}
						}
					}
				}else if(html0.contains("unavailable")){ //有时候访问频率过高，就会响应这个信息，需要重试
					callRecordRetryCount++;
					if(callRecordRetryCount>2){
						callRecordRetryCount=0;  //将数据归0,避免影响下个循环的使用
						taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"通话详单已采集完成",taskMobile.getTaskid());
						tracer.addTag(firstMonthdate+"至"+lastMonthdate+"通话详单暂无法正常响应数据页面", "原因：请求频率过高或因处于月初或月末，相关源码已存入数据库");
						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
								"通话记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
								"page temporarily unavailable", "请求频率过高或因处于月初或月末，该查询区间短信详单页面暂时无法响应", 1);		
						mobileDataErrRecRepository.save(m);
					}else{
						Thread.sleep(2000);
						tracer.addTag(firstMonthdate+"至"+lastMonthdate+"通话详单暂无法正常响应数据页面，请求重试次数：", callRecordRetryCount+"");
						getCallRecord(taskMobile,firstMonthdate,lastMonthdate);
					}
				}else{   //出现其他页面响应情况，确实无法提供数据进行爬取
					taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"通话详单已采集完成",taskMobile.getTaskid());
					tracer.addTag(firstMonthdate+"至"+lastMonthdate+"通话详单暂无法正常响应数据页面", "原因：请求频率过高或因处于月初或月末，相关源码已存入数据库");
				}
			}
		} catch (Exception e) {
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"通话记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
					"exception", e.toString(), 1);		
			mobileDataErrRecRepository.save(m);
			tracer.addTag("action.crawler.getCallRecord.e."+firstMonthdate+"至"+lastMonthdate, e.toString());
			taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"通话详单已采集完成",taskMobile.getTaskid());
		}
	}
//	@Async  //爬取规则同通话详单
	public Future<String> getAllSMSRecord(TaskMobile taskMobile) throws Exception {
		Thread.sleep(3000);  //抛出异常，为了避免当前月无法正常响应数据，先sleep段时间
		for(int i=0;i<6;i++){
			String firstMonthdate = CalendarParamService.getFirstMonthdate(i);
			String lastMonthdate = CalendarParamService.getLastMonthdate(i);
			try {
				Thread.sleep(3000);
				getSMSRecord(taskMobile,firstMonthdate,lastMonthdate);
			} catch (Exception e) {
				tracer.addTag("action.crawler.getAllSMSRecord.e"+" "+firstMonthdate+"-"+lastMonthdate,e.toString());
			}
		}
		crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),200, "数据采集中,短信详单已采集完成");
		return new AsyncResult<String>("200");
	}
	//不能使用异步，官网的访问频率有限制(爬取规则同通话详单，故注释写在了爬取通话详单中)
	public void getSMSRecord(TaskMobile taskMobile, String firstMonthdate, String lastMonthdate)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setTimeout(5*10000);
			webClient.getOptions().setJavaScriptEnabled(false);
			String initUrl="http://tj.189.cn/tj/service/bill/billDetailQuery.action?billDetailValidate=true&flag_is1k2x=false&billDetailType=2&sRandomCode=516509&randInputValue=2431&startTime="+firstMonthdate+"&endTime="+lastMonthdate+"&exFormat=1";
			WebRequest webRequest = new WebRequest(new URL(initUrl), HttpMethod.GET);
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page page0 = webClient.getPage(webRequest);
			if(page0!=null){
				String html0 =page0.getWebResponse().getContentAsString();
				TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
				htmlStore.setTaskid(taskMobile.getTaskid());
				htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"第1页(默认)短信详单源码页");
				htmlStore.setPageCount(1); 
				htmlStore.setUrl(initUrl);
				htmlStore.setHtml(html0);
				htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
				telecomTianjinHtmlRepository.save(htmlStore);
				tracer.addTag(firstMonthdate+"(第1页)默认短信详单源码页—隐藏域中含有短信记录总页数",firstMonthdate+"至"+lastMonthdate+"默认短信详单源码页已入库");
				if(html0.contains("发送号码")){  
					smsRecordRetryCount=0;  
					List<TelecomTianjinSMSRecord> list= telecomTianjinParser.SMSRecordParser(taskMobile,html0,firstMonthdate,lastMonthdate);
					if(null != list && list.size()>1){
						telecomTianjinSMSRecordRepository.saveAll(list);
						tracer.addTag("action.crawler.getSMSRecord", firstMonthdate+"至"+lastMonthdate+"第1页短信详单已入库");
						taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第1页短信详单已采集完成",taskMobile.getTaskid());
					}else{
						taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第1页短信详单无数据可采集",taskMobile.getTaskid());
					}
					Document doc = Jsoup.parse(html0);
					int totalPageNum = Integer.parseInt(doc.getElementById("detailTotalPage").val());
					String eachPageUrl="";
					String resultHtml="";
					if(totalPageNum>0){ 
						for(int curPageNo=2;curPageNo<=totalPageNum;curPageNo++){
							eachPageUrl="http://tj.189.cn/tj/service/bill/billDetailAjaxQuery.action?pageNo="+curPageNo+"&startTime="+firstMonthdate+"&endTime="+lastMonthdate+"&billDetailType=2&is1k2x_ret=undefined";
							try {
								Thread.sleep(1500);
								resultHtml=retryService.getEachSMSPageSource(initUrl,eachPageUrl,taskMobile,firstMonthdate,lastMonthdate,curPageNo);
								//不管返回的结果如何，都将源码存储到数据库
								htmlStore = new TelecomTianjinHtml();
								htmlStore.setTaskid(taskMobile.getTaskid());
								htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单源码页");
								htmlStore.setPageCount(curPageNo);  //存储所属的页码
								htmlStore.setUrl(eachPageUrl);
								htmlStore.setHtml(resultHtml);
								htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
								htmlStore.setTotalpage(totalPageNum);  //存储总页码
								telecomTianjinHtmlRepository.save(htmlStore);
								tracer.addTag("action.crawler.getSMSRecord.html"+curPageNo, firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单源码页已经入库");
								if(null!=resultHtml && resultHtml.contains("发送号码")){
									list= telecomTianjinParser.SMSRecordParser(taskMobile,resultHtml,firstMonthdate,lastMonthdate);
									if(null != list && list.size()>1){
										telecomTianjinSMSRecordRepository.saveAll(list);
										tracer.addTag("action.crawler.getSMSRecord", firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单已入库");
										taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单已采集完成",taskMobile.getTaskid());
									}else{
										taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单无数据可采集",taskMobile.getTaskid());
									}
								}else{
									taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"第"+curPageNo+"页短信详单无数据可采集",taskMobile.getTaskid());
								}
							} catch (Exception e) {
								MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
										"短信记录",firstMonthdate+"-"+lastMonthdate+"第"+curPageNo+"页",taskMobile.getCarrier(),taskMobile.getCity(),
										"exception", e.toString(), 1);		
								mobileDataErrRecRepository.save(m);
							}
						}
					}
				}else if(html0.contains("unavailable")){ //有时候访问频率过高，就会响应这个信息，需要重试
					smsRecordRetryCount++;
					if(smsRecordRetryCount>2){
						smsRecordRetryCount=0;  //将数据归0,避免影响下个循环的使用
						taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"短信详单已采集完成",taskMobile.getTaskid());
						tracer.addTag(firstMonthdate+"至"+lastMonthdate+"短信详单暂无法正常响应数据页面", "原因：请求频率过高或因处于月初或月末，相关源码已存入数据库");
						MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
								"短信记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
								"page temporarily unavailable", "请求频率过高或因处于月初或月末，该查询区间短信详单页面暂时无法响应", 1);		
						mobileDataErrRecRepository.save(m);
					}else{
						Thread.sleep(2000);
						tracer.addTag(firstMonthdate+"至"+lastMonthdate+"短信详单暂无法正常响应数据页面，请求重试次数：", smsRecordRetryCount+"");
						getSMSRecord(taskMobile,firstMonthdate,lastMonthdate);
					}
				}else{   //出现其他页面响应情况，确实无法提供数据进行爬取
					taskMobileRepository.updateDescriptionByTaskid("数据采集中，"+firstMonthdate+"至"+lastMonthdate+"短信详单已采集完成",taskMobile.getTaskid());
					tracer.addTag(firstMonthdate+"至"+lastMonthdate+"短信详单暂无法正常响应数据页面", "原因：请求频率过高或因处于月初或月末，相关源码已存入数据库");
				}
			}
		} catch (Exception e) {
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"短信记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
					"exception", e.toString(), 1);		
			mobileDataErrRecRepository.save(m);
			tracer.addTag("action.crawler.getSMSRecord.e."+firstMonthdate+"至"+lastMonthdate, e.toString());
			taskMobileRepository.updateDescriptionByTaskid("数据采集中,"+firstMonthdate+"至"+lastMonthdate+"短信详单已采集完成",taskMobile.getTaskid());
		}
	}
	/**
	 * 爬取增值业务
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getUiBusiness(TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://tj.189.cn/tj/vas/getSubscrUI.action";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			if(page!=null){
				String html =page.getWebResponse().getContentAsString();
				TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
				htmlStore.setTaskid(taskMobile.getTaskid());
				htmlStore.setType("增值业务信息源码页");
				htmlStore.setPageCount(1);
				htmlStore.setUrl(url);
				htmlStore.setHtml(html);
				telecomTianjinHtmlRepository.save(htmlStore);
				tracer.addTag("action.crawler.getUIBusiness.html", "增值业务信息源码页已经入库");
				List<TelecomTianjinBusiness> list= telecomTianjinParser.businessUIParser(taskMobile,html);
				if(null != list){
					telecomTianjinBusinessRepository.saveAll(list);
					tracer.addTag("action.crawler.getUIBusiness", "增值业务信息已入库");
					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode(), "数据采集中，增值业务信息已采集完成");
				}else{
					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),201, "数据采集中，增值业务信息已采集完成");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getUiBusiness.e", e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),404, "数据采集中，增值业务信息已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	/**
	 * 爬取附加业务
	 * @param taskMobile
	 * @return 
	 * @ 如今这样请求貌似不能够获取完全，需要：http://tj.189.cn/tj/service/transaction/orderingRela.action?fastcode=02241352&amp;cityCode=tj
	 * 作为前提页面，貌似
	 */
	@Async
	public Future<String> getSrvBusiness(TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://tj.189.cn/tj/service/bill/fixBusiness.action";
			Page page = loginAndGetCommonService.getPage(url, webClient);
			if(page!=null){
				String html =page.getWebResponse().getContentAsString();
				TelecomTianjinHtml htmlStore = new TelecomTianjinHtml();
				htmlStore.setTaskid(taskMobile.getTaskid());
				htmlStore.setType("附加业务信息源码页");
				htmlStore.setPageCount(1);
				htmlStore.setUrl(url);
				htmlStore.setHtml(html);
				telecomTianjinHtmlRepository.save(htmlStore);
				tracer.addTag("action.crawler.getSrvBusiness.html", "附加业务信息源码页已经入库");
				List<TelecomTianjinBusiness> list= telecomTianjinParser.businessSrvParser(taskMobile,html);
				if(null != list){
					telecomTianjinBusinessRepository.saveAll(list);
					tracer.addTag("action.crawler.getSrvBusiness", "附加业务信息已入库");
					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode(), "数据采集中，附加业务信息已采集完成");
				}else{
					crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，附加业务信息已采集完成");
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.auth.getSrvBusiness.e", e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，附加业务信息已采集完成");
		}
		return new AsyncResult<String>("200");
	}
}
