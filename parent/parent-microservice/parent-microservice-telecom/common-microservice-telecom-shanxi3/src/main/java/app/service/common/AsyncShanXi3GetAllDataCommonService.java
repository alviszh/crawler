package app.service.common;

import java.net.URL;
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

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Balance;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3BalanceDetail;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3BalanceSum;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Business;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3CallRecord;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3ChargeInfo;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3CurrentSituation;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3FamilyCall;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Html;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Integra;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3MonthBill;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3SMSRecord;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3UserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3BalanceDetailRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3BalanceRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3BalanceSumRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3BusinessRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3CallRecordRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3ChargeInfoRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3CurrentSituationRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3FamilyCallRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3HtmlRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3IntegraRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3MonthBillRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3SMSRecordRepository;
import com.microservice.dao.repository.crawler.telecom.shanxi3.TelecomShanxi3UserInfoRepository;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.parser.TelecomShanXi3Parser;
import app.service.CrawlerStatusMobileService;


/**
 * @Description
 * @author sln
 * @date 2017年8月24日 下午6:22:02
 * 
 * 
 
 */
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.shanxi3")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.shanxi3")
public class AsyncShanXi3GetAllDataCommonService {
	@Autowired
	private TelecomShanxi3HtmlRepository telecomShanxi3HtmlRepository;
	@Autowired
	private TelecomShanxi3IntegraRepository telecomShanxi3IntegraRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private LoginAndGetCommonService loginAndGetCommonService;
	@Autowired
	private TelecomShanxi3FamilyCallRepository telecomShanxi3FamilyCallRepository;
	@Autowired
	private TelecomShanxi3CallRecordRepository telecomShanxi3CallRecordRepository;
	@Autowired
	private TelecomShanxi3SMSRecordRepository telecomShanxi3SMSRecordRepository;
	@Autowired
	private TelecomShanXi3Parser telecomShanXi3Parser;
	@Autowired
	private TelecomShanxi3BusinessRepository telecomShanxi3BusinessRepository;
	@Autowired
	private TelecomShanxi3UserInfoRepository telecomShanxi3UserInfoRepository;
	@Autowired
	private TelecomShanxi3ChargeInfoRepository telecomShanxi3ChargeInfoRepository;
	@Autowired
	private TelecomShanxi3MonthBillRepository telecomShanxi3MonthBillRepository;
	@Autowired
	private TelecomShanxi3BalanceSumRepository telecomShanxi3BalanceSumRepository;
	@Autowired
	private TelecomShanxi3BalanceDetailRepository telecomShanxi3BalanceDetailRepository;
	@Autowired
	private TelecomShanxi3BalanceRepository telecomShanxi3BalanceRepository;
	@Autowired
	private TelecomShanxi3CurrentSituationRepository telecomShanxi3CurrentSituationRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;

	/**
	 * 获取所有的积分信息
	 * @param messageLogin
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getIntegra(MessageLogin messageLogin, TaskMobile taskMobile,String yearmonth)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url = "http://sn.189.cn/service/point/pointDetailQueryAjax.action?pointVo.serviceNbr="+messageLogin.getName().trim()+"&currentTime="+yearmonth+"";
			webClient.getOptions().setTimeout(20000);	
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int integraStatusCode=page.getWebResponse().getStatusCode();
				if(200 == integraStatusCode){
					String html = page.getWebResponse().getContentAsString();
					tracer.addTag(taskMobile.getTaskid()+"获取的积分信息源码页是："+yearmonth+"===>", html);
					if(!"null".equals(html) && !"".equals(html) && !html.contains("无话单记录")
							&& !html.contains("参数验证失败") && !html.contains("登录失败")
							&& !html.contains("欢迎登录") && !html.contains("找不到您要的页面")
							&& !html.contains("您查询的时间段内没有详单数据") && !html.contains("无记录")
							&& !html.contains("外围接口服务不存在或错误") && !html.contains("连接超时")
							&& !html.contains("系统忙，请稍后再试"))
					{
						tracer.addTag("action.crawler.getIngegra.html"+yearmonth, yearmonth+"的积分html为："+html);
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(yearmonth+"积分信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						htmlStore.setYearmonth(yearmonth);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getIntegra.html"+yearmonth, yearmonth+"积分信息源码页已经入库"+taskMobile.getTaskid());
						List<TelecomShanxi3Integra> list =telecomShanXi3Parser.integraParser(html,taskMobile,yearmonth);
						if(null != list){
							telecomShanxi3IntegraRepository.saveAll(list);
							tracer.addTag("action.crawler.getIntegra"+yearmonth, yearmonth+"积分信息已入库"+taskMobile.getTaskid());
							crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),200, "数据采集中，积分信息"+yearmonth+"月已采集完成");
						}else{
							//没有数据可以提供采集也将状态改为200，因为网站查询积分的速度就很慢,避免某个月没有采集数据，状态是201的时候，别的程序都走完，导致前端页面积分信息状态为：采集已完成，无数据
							crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),201, "数据采集中，积分信息"+yearmonth+"月无数据可供采集");
						}
					}else{
						//由于积分信息本身查询速度慢，故此处不再重试三次
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(yearmonth+"积分信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						htmlStore.setYearmonth(yearmonth);
						htmlStore.setMonthtotalrow(0);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getIntegra.html"+yearmonth, yearmonth+"积分信息源码页无可查询数据"+taskMobile.getTaskid());
						crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(),201, "数据采集中，积分信息"+yearmonth+"月无数据可供采集");
					}
				}		
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getIntegra", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updateIntegralMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，积分记录已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	//===================================================================================================
	/**
	 * 爬取亲情号通话记录
	 * @param messageLogin
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getFamilyCall(MessageLogin messageLogin, TaskMobile taskMobile,String firstMonthdate,String lastMonthdate,int i)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			Integer maxValue=9999;
			String url="http://sn.189.cn/service/bill/familyRecordList.action?currentPage=1&pageSize="+maxValue+"&effDate="+firstMonthdate+"&expDate="+lastMonthdate+"&serviceNbr="+messageLogin.getName()+"&prdType=4&sendSmsFlag=true&validCode=282116";
			webClient.getOptions().setTimeout(20000);	
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					Document doc=Jsoup.parse(page.getWebResponse().getContentAsString());
					String strXml=page.getWebResponse().getContentAsString();
					if(!strXml.contains("无话单记录") && !strXml.contains("参数验证失败") && !strXml.contains("登录失败")
							&& !strXml.contains("欢迎登录") && !strXml.contains("找不到您要的页面")
							&& !strXml.contains("您查询的时间段内没有详单数据") && !strXml.contains("无记录")
							&& !strXml.contains("外围接口服务不存在或错误") && !strXml.contains("连接超时")
							&& !strXml.contains("系统忙，请稍后再试")
							&& !"null".equals(strXml) && !"".equals(strXml))
					{
						Integer totalRow =Integer.parseInt(doc.getElementById("totalRow").val());
						if(!"".equals(totalRow)){
							String html =page.getWebResponse().getContentAsString();
							//获取近6个月的亲情号通话记录
							List<TelecomShanxi3FamilyCall> list= telecomShanXi3Parser.familyCallParser(html,taskMobile,firstMonthdate,lastMonthdate);
							if(null !=page){
								TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
								htmlStore.setTaskid(taskMobile.getTaskid());
								htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"亲情号通话详单源码页");
								htmlStore.setPageCount(1);
								htmlStore.setUrl(url);
								htmlStore.setHtml(html);
								htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
								htmlStore.setMonthtotalrow(totalRow);
								telecomShanxi3HtmlRepository.save(htmlStore);
								tracer.addTag("action.crawler.getFamilyCall.html"+i, firstMonthdate+"至"+lastMonthdate+"亲情号通话详单源码页已经入库"+taskMobile.getTaskid());
							}
							if(null != list){
								telecomShanxi3FamilyCallRepository.saveAll(list);
								tracer.addTag("action.crawler.getFamilyCall", firstMonthdate+"至"+lastMonthdate+"时间段内亲情号通话详单已入库"+taskMobile.getTaskid());
								crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_FamilyMsgStatus_SUCESS.getCode(), "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"亲情号通话详单已采集完成");
							}else{
								crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"亲情号通话详单无数据可供采集");
							}
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"亲情号通话记录源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(strXml);
						htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
						htmlStore.setMonthtotalrow(0);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getFamilyCall.html"+i, firstMonthdate+"至"+lastMonthdate+"亲情号通话记录源码页无可查询数据"+taskMobile.getTaskid());
						crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"无亲情号通话详单数据可供采集");
					}
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getFamilyCall", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updateFamilyMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，亲情号通话详单已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	//============================================================================================
	/**
	 * 爬取通话详单
	 * @param messageLogin
	 * @param taskMobile
	 * @param callPOffrTypeID 
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getCallRecord(MessageLogin messageLogin, TaskMobile taskMobile,String firstMonthdate,String lastMonthdate, String callPOffrTypeID)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setTimeout(20000);
			Integer maxValue=9999;
			//&isPrepay=0 是否预付费这个参数可以省略
			String url="http://sn.189.cn/service/bill/feeDetailrecordList.action?currentPage=1&pageSize="+maxValue+"&effDate="+firstMonthdate+"&expDate=%20"+lastMonthdate+"%20&serviceNbr="+messageLogin.getName()+"&operListID="+callPOffrTypeID+"&pOffrType=481";
//			String url="http://sn.189.cn/service/bill/feeDetailrecordList.action?currentPage=1&pageSize="+maxValue+"&effDate="+firstMonthdate+"&expDate="+lastMonthdate+"&serviceNbr="+messageLogin.getName()+"&operListID="+callPOffrTypeID+"&pOffrType=481";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					Document doc=Jsoup.parse(page.getWebResponse().getContentAsString());
					String strXml=page.getWebResponse().getContentAsString();
					if(!strXml.contains("无话单记录") && !strXml.contains("参数验证失败") && !strXml.contains("登录失败")
							&& !strXml.contains("欢迎登录") && !strXml.contains("找不到您要的页面")
							&& !strXml.contains("您查询的时间段内没有详单数据") && !strXml.contains("无记录")
							&& !strXml.contains("外围接口服务不存在或错误") && !strXml.contains("连接超时")
							&& !strXml.contains("系统忙，请稍后再试") && !"null".equals(strXml) && !"".equals(strXml))
					{
						Integer totalRow =Integer.parseInt(doc.getElementById("totalRow").val());
						if(!"".equals(totalRow)){
							String html =page.getWebResponse().getContentAsString();
							//获取近6个月的通话记录
							List<TelecomShanxi3CallRecord> list= telecomShanXi3Parser.callRecordParser(taskMobile,html);
							if(null !=page){
								TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
								htmlStore.setTaskid(taskMobile.getTaskid());
								htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"通话详单源码页");
								htmlStore.setPageCount(1);
								htmlStore.setUrl(url);
								htmlStore.setHtml(html);
								htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
								htmlStore.setMonthtotalrow(totalRow);
								telecomShanxi3HtmlRepository.save(htmlStore);
								tracer.addTag("action.crawler.getCallRecord.html", firstMonthdate+"至"+lastMonthdate+"通话详单源码页已经入库"+taskMobile.getTaskid());
							}
							if(null != list){
								telecomShanxi3CallRecordRepository.saveAll(list);
								tracer.addTag("action.crawler.getCallRecord", firstMonthdate+"至"+lastMonthdate+"时间段内通话详单已入库"+taskMobile.getTaskid());
								crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(), 200, "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"通话详单已采集完成");
							}else{
								crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),201, "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"通话详单无数据可供采集");
							}
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"通话记录源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(strXml);
						htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
						htmlStore.setMonthtotalrow(0);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getCallRecord.html", firstMonthdate+"至"+lastMonthdate+"通话记录源码页无可查询数据"+taskMobile.getTaskid());
						crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),201, "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"通话详单无数据可供采集");
					}
				}
			}
			
		} catch (Exception e) {
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"通话记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "连接超时", 1);		
			mobileDataErrRecRepository.save(m);
			tracer.addTag("action.crawler.getCallRecord", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updateCallRecordStatus(taskMobile.getTaskid(),404, "数据采集中，通话详单已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	
	//============================================================================================
	/**
	 * 爬取短信记录
	 * @param messageLogin
	 * @param taskMobile
	 * @param callPOffrTypeID 
	 * @param i 
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getSMSRecord(MessageLogin messageLogin, TaskMobile taskMobile,String firstMonthdate,String lastMonthdate,String smsPOffrTypeID)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setTimeout(20000);
			Integer maxValue=9999;
			String url="http://sn.189.cn/service/bill/feeDetailrecordList.action?currentPage=1&pageSize="+maxValue+"&effDate="+firstMonthdate+"&expDate="+lastMonthdate+"&serviceNbr="+messageLogin.getName()+"&operListID="+smsPOffrTypeID+"&pOffrType=481";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String contentAsString=page.getWebResponse().getContentAsString();
					Document doc=Jsoup.parse(contentAsString);
					if(!contentAsString.contains("当您点击某个链接时，它可能已过期") && !contentAsString.contains("在地址中可能存在键入错误") 
							&& !contentAsString.contains("系统异常") && !contentAsString.contains("您查询的时间段内没有详单数据")
							&& !contentAsString.contains("登录失败") && !contentAsString.contains("欢迎登录") 
							&& !contentAsString.contains("外围接口服务不存在或错误") && !contentAsString.contains("连接超时"))
					{
						Integer totalRow =Integer.parseInt(doc.getElementById("totalRow").val());
						if(!"".equals(totalRow)){
							String html =page.getWebResponse().getContentAsString();
							//获取近6个月的短信记录
							List<TelecomShanxi3SMSRecord> list= telecomShanXi3Parser.SMSRecordParser(taskMobile,html);
							if(null !=page){
								TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
								htmlStore.setTaskid(taskMobile.getTaskid());
								htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"短信详单源码页");
								htmlStore.setPageCount(1);
								htmlStore.setUrl(url);
								htmlStore.setHtml(html);
								htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
								htmlStore.setMonthtotalrow(totalRow);
								telecomShanxi3HtmlRepository.save(htmlStore);
								tracer.addTag("action.crawler.getSMSRecord.html", firstMonthdate+"至"+lastMonthdate+"短信详单源码页已经入库"+taskMobile.getTaskid());
							}
							if(null != list){
								telecomShanxi3SMSRecordRepository.saveAll(list);
								tracer.addTag("action.crawler.getSMSRecord", firstMonthdate+"至"+lastMonthdate+"时间段内短信详单已入库"+taskMobile.getTaskid());
								crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_SMSRecordStatus_SUCESS.getCode(), "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"短信详单已采集完成");

							}else{
								crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),201, "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"短信详单无数据可供采集");
							}
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(firstMonthdate+"至"+lastMonthdate+"短信详单源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						htmlStore.setYearmonth(firstMonthdate+"至"+lastMonthdate);
						htmlStore.setMonthtotalrow(0);
						telecomShanxi3HtmlRepository.save(htmlStore);
						crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),201, "数据采集中，"+firstMonthdate+"至"+lastMonthdate+"短信详单无数据可供采集");
						tracer.addTag("action.crawler.getSMSRecord.html", firstMonthdate+"至"+lastMonthdate+"短信详单源码页无可查询数据"+taskMobile.getTaskid());
					}
				}
			}
			
		} catch (Exception e) {
			MobileDataErrRec m = new MobileDataErrRec(taskMobile.getTaskid(), 
					"短信记录",firstMonthdate+"-"+lastMonthdate,taskMobile.getCarrier(),taskMobile.getCity(),
					"timeout", "连接超时", 1);		
			mobileDataErrRecRepository.save(m);
			tracer.addTag("action.crawler.getSMSRecord", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updateSMSRecordStatus(taskMobile.getTaskid(),404, "数据采集中，短信详单已采集完成");
		}
		return new AsyncResult<String>("200");		
	}
	//========================================================================================
	/**
	 * 爬取业务信息
	 * @param messageLogin
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getBusiness(MessageLogin messageLogin, TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://sn.189.cn/service/manage/offerListView.action?currentPage=1";
			webClient.getOptions().setTimeout(20000);	
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String contentAsString=page.getWebResponse().getContentAsString();
					if(!contentAsString.contains("当您点击某个链接时，它可能已过期") && !contentAsString.contains("在地址中可能存在键入错误")
							&& !contentAsString.contains("系统异常") && !contentAsString.contains("登录失败")){
						String html =page.getWebResponse().getContentAsString();
						List<TelecomShanxi3Business> list= telecomShanXi3Parser.businessParser(taskMobile,html);
						if(null !=page){
							TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
							htmlStore.setTaskid(taskMobile.getTaskid());
							htmlStore.setType("业务信息源码页");
							htmlStore.setPageCount(1);
							htmlStore.setUrl(url);
							htmlStore.setHtml(html);
							telecomShanxi3HtmlRepository.save(htmlStore);
							tracer.addTag("action.crawler.getBusiness.html", "业务信息源码页已经入库"+taskMobile.getTaskid());
						}
						if(null != list){
							telecomShanxi3BusinessRepository.saveAll(list);
							tracer.addTag("action.crawler.getBusiness", "业务信息已入库"+taskMobile.getTaskid());
							crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_BusinessMsgStatus_SUCESS.getCode(), "数据采集中，业务信息已采集完成");
							
						}else{
							crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，业务信息无数据可供采集");
							
						}
					}else{
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("业务信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						htmlStore.setMonthtotalrow(0);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBusiness.html", "业务信息源码页无可查询数据，或网站升级维护中，暂不提供此类信息查询服务"+taskMobile.getTaskid());
						crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，业务信息无数据可供采集");
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBusiness", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updateBusinessMsgStatus(taskMobile.getTaskid(),404, "数据采集中，业务信息已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	//=======================================================
	/**
	 * 爬取用户信息
	 * @param messageLogin
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setJavaScriptEnabled(false);
//			String url="http://sn.189.cn/service/manage/showCustInfo.action?mobileNum="+messageLogin.getName()+"&listType=102";
			//改版后的连接方式
			String url="http://sn.189.cn/service/manage/showCustInfo.action?fastcode=10000429&cityCode=sn";
			webClient.getOptions().setTimeout(20000);	
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String html = page.getWebResponse().getContentAsString();
					if(!html.contains("欢迎登录") && !html.contains("当您点击某个链接时，它可能已过期") &&  !html.contains("系统异常") && !html.contains("登录失败")){
						WebParam<TelecomShanxi3UserInfo> webParam= telecomShanXi3Parser.userInfoParser(taskMobile,html);
						//保存页面信息
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("用户信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getUserInfo.html"+System.currentTimeMillis(), "用户信息源码页已经入库"+taskMobile.getTaskid());
						if(null!=webParam.getTelecomShanxi3UserInfo()){
							telecomShanxi3UserInfoRepository.save(webParam.getTelecomShanxi3UserInfo());
							tracer.addTag("action.crawler.getUserInfo"+System.currentTimeMillis(), "用户信息已入库"+taskMobile.getTaskid());
							crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode(), "数据采集中，用户信息已采集完成");
							
						}else{
							crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，用户信息无数据可供采集");
							
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("用户信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(html);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getUserInfo.html", "用户信息源码页无可查询数据，或网站升级维护中，暂不提供此类信息查询服务"+taskMobile.getTaskid());
						crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),201, "数据采集中，用户信息无数据可供采集");
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updateUserMsgStatus(taskMobile.getTaskid(),404, "数据采集中，用户信息已采集完成");
		}
		return new AsyncResult<String>("200");
	}
	
	//爬取月账单
	@Async
	public Future<String> getMonthBill(MessageLogin messageLogin, TaskMobile taskMobile, String yearmonth,int i) {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setTimeout(30000);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://sn.189.cn/service/bill/billDetail.action?billtype=1&month="+yearmonth+"&areacode=290&accnbr="+messageLogin.getName()+"&productid=41010300";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String contentAsString = page.getWebResponse().getContentAsString();
					if(!contentAsString.contains("当您点击某个链接时，它可能已过期") && !contentAsString.contains("在地址中可能存在键入错误") 
							&& !contentAsString.contains("系统异常") && !contentAsString.contains("您查询的时间段内没有详单数据")
							&& !contentAsString.contains("登录失败") && !contentAsString.contains("欢迎登录") 
							&& !contentAsString.contains("外围接口服务不存在或错误") && !contentAsString.contains("连接超时") 
							&& !contentAsString.contains("您的号码还没有登录，不可以查询账单"))
					{
						String html =page.getWebResponse().getContentAsString();
						List<TelecomShanxi3MonthBill> list= telecomShanXi3Parser.getMonthBill(messageLogin,taskMobile,html,yearmonth);
						if(null !=page){
							TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
							htmlStore.setTaskid(taskMobile.getTaskid());
							htmlStore.setType("月账单信息源码页");
							htmlStore.setPageCount(1);
							htmlStore.setUrl(url);
							htmlStore.setHtml(html);
							telecomShanxi3HtmlRepository.save(htmlStore);
							tracer.addTag("action.crawler.getMonthBill.html"+yearmonth, "月账单信息源码页已经入库"+taskMobile.getTaskid());
						}
						if(null != list){
							telecomShanxi3MonthBillRepository.saveAll(list);
							tracer.addTag("action.crawler.getMonthBill"+yearmonth, "月账单信息已入库"+taskMobile.getTaskid());
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("月账单信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getMonthBill.html"+yearmonth, "月账单信息源码页无可查询数据，或网站升级维护中，暂不提供此类信息查询服务"+taskMobile.getTaskid());
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMonthBill", taskMobile.getTaskid()+"    "+e.toString());
		}
		return new AsyncResult<String>("200");		
	}
	/**
	 * 爬取充值记录
	 * @param messageLogin
	 * @param taskMobile
	 * @return 
	 * @ 
	 */
	@Async
	public Future<String> getChargeInfo(MessageLogin messageLogin, TaskMobile taskMobile,String encryptedPhone,String month)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setTimeout(20000);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://sn.189.cn/service/bill/rechargeRecordSearch.action?phoneNum="+encryptedPhone+"&prodTypeId=4&billingCycle="+month+"";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String contentAsString = page.getWebResponse().getContentAsString();
					if(!contentAsString.contains("当您点击某个链接时，它可能已过期") && !contentAsString.contains("在地址中可能存在键入错误") 
							&& !contentAsString.contains("系统异常") && !contentAsString.contains("您查询的时间段内没有详单数据")
							&& !contentAsString.contains("登录失败") && !contentAsString.contains("欢迎登录") 
							&& !contentAsString.contains("外围接口服务不存在或错误") && !contentAsString.contains("连接超时")
							&& !contentAsString.contains("服务调用失败") && !contentAsString.contains("查询号码为空")
							&& !contentAsString.contains("选择的账期是空的"))
					{
						List<TelecomShanxi3ChargeInfo> list= telecomShanXi3Parser.chargeInfoParser(taskMobile,contentAsString,month);
						//保存页面信息
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(month+"充值记录源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						htmlStore.setYearmonth(month);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getChargeInfo.html"+month, month+"充值记录源码页已经入库"+taskMobile.getTaskid());
						if(null!=list && list.size()>0){
							telecomShanxi3ChargeInfoRepository.saveAll(list);
							tracer.addTag("action.crawler.getChargeInfo", month+"充值记录已入库"+taskMobile.getTaskid());
							crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),200, "数据采集中，"+month+"充值记录已采集完成");
						}else{
							crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),201, "数据采集中，"+month+"充值记录无数据可供采集");
						}
					}else{
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("充值记录源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getChargeInfo.html"+month, "充值记录源码页无可查询数据，或网站升级维护中，暂不提供此类信息查询服务"+taskMobile.getTaskid());
						crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),201, "数据采集中，"+month+"充值记录无数据可供采集");
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getChargeInfo", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updatePayMsgStatus(taskMobile.getTaskid(),404, "数据采集中，充值记录已采集完成");
		}
		return new AsyncResult<String>("200");	
	}
	/**
	 * 爬取可用余额汇总信息
	 * @param messageLogin
	 * @param taskMobile
	 * @param webClient
	 * @param encryptedPhone
	 * @param month
	 * @return 
	 */
	@Async
	public Future<String> getBalanceSum(MessageLogin messageLogin, TaskMobile taskMobile,String encryptedPhone, String month) {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://sn.189.cn/service/bill/surplusDetailSearch.action?phoneNum="+encryptedPhone+"&prodTypeId=4&billingCycle="+month+"";
			webClient.getOptions().setTimeout(20000);	
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String contentAsString = page.getWebResponse().getContentAsString();
					if(!contentAsString.contains("当您点击某个链接时，它可能已过期") && !contentAsString.contains("在地址中可能存在键入错误") 
							&& !contentAsString.contains("系统异常") && !contentAsString.contains("您查询的时间段内没有详单数据")
							&& !contentAsString.contains("登录失败") && !contentAsString.contains("欢迎登录") 
							&& !contentAsString.contains("外围接口服务不存在或错误") && !contentAsString.contains("连接超时")
							&& !contentAsString.contains("服务调用失败") && !contentAsString.contains("查询号码为空")
							&& !contentAsString.contains("选择的账期是空的"))
					{
						List<TelecomShanxi3BalanceSum> list= telecomShanXi3Parser.balanceSumParser(taskMobile,contentAsString,month);
						//保存页面信息
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(month+"可用余额汇总信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						htmlStore.setYearmonth(month);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBalanceSum.html"+month, month+"可用余额汇总信息源码页已经入库"+taskMobile.getTaskid());
						if(null!=list && list.size()>0){
							telecomShanxi3BalanceSumRepository.saveAll(list);
							tracer.addTag("action.crawler.getBalanceSum", month+"可用余额汇总信息已入库"+taskMobile.getTaskid());
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("可用余额汇总信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBalanceSum.html"+month, "可用余额汇总信息源码页无可查询数据，或网站升级维护中，暂不提供此类信息查询服务"+taskMobile.getTaskid());
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBalanceSum", taskMobile.getTaskid()+"    "+e.toString());
		}
		return new AsyncResult<String>("200");	
	}
	
	/**
	 * 爬取余额明细信息
	 * @param messageLogin
	 * @param taskMobile
	 * @param webClient
	 * @param encryptedPhone
	 * @param month
	 * @return 
	 */
	@Async
	public Future<String> getBalanceDetail(MessageLogin messageLogin, TaskMobile taskMobile,String encryptedPhone, String month) {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://sn.189.cn/service/bill/surplusDetailSearch.action?phoneNum="+encryptedPhone+"&prodTypeId=4&billingCycle="+month+"";
			webClient.getOptions().setTimeout(20000);	
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String contentAsString = page.getWebResponse().getContentAsString();
					if(!contentAsString.contains("当您点击某个链接时，它可能已过期") && !contentAsString.contains("在地址中可能存在键入错误") 
							&& !contentAsString.contains("系统异常") && !contentAsString.contains("您查询的时间段内没有详单数据")
							&& !contentAsString.contains("登录失败") && !contentAsString.contains("欢迎登录") 
							&& !contentAsString.contains("外围接口服务不存在或错误") && !contentAsString.contains("连接超时")
							&& !contentAsString.contains("服务调用失败") && !contentAsString.contains("查询号码为空")
							&& !contentAsString.contains("选择的账期是空的"))
					{
						List<TelecomShanxi3BalanceDetail> list= telecomShanXi3Parser.balanceDetailParser(taskMobile,contentAsString,month);
						//保存页面信息
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType(month+"余额明细信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						htmlStore.setYearmonth(month);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBalanceDetail.html"+month, month+"余额明细信息源码页已经入库"+taskMobile.getTaskid());
						if(null!=list && list.size()>0){
							telecomShanxi3BalanceDetailRepository.saveAll(list);
							tracer.addTag("action.crawler.getBalanceDetail", month+"余额明细信息已入库"+taskMobile.getTaskid());
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("可用余额明细信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBalanceDetail.html"+month, "可用余额明细信息源码页无可查询数据，或网站升级维护中，暂不提供此类信息查询服务"+taskMobile.getTaskid());
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBalanceDetail", taskMobile.getTaskid()+"    "+e.toString());
		}
		return new AsyncResult<String>("200");
	}
	//爬取我的现状信息
	@Async
	public Future<String> getCurrentSituation(MessageLogin messageLogin, TaskMobile taskMobile)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			webClient.getOptions().setJavaScriptEnabled(false);
			String urlHuaFei="http://www.189.cn/dqmh/order/getHuaFei.do";
			String urlTaoCan="http://www.189.cn/dqmh/order/getTaoCan.do";
			Page pageHuaFei = loginAndGetCommonService.getPage(urlHuaFei, webClient);
			Page pageTaoCan = loginAndGetCommonService.getPage(urlTaoCan, webClient);
			if(null!=pageHuaFei && null!=pageTaoCan){
				int statusCodeHuaFei = pageHuaFei.getWebResponse().getStatusCode();
				int statusCodeTaoCan=pageTaoCan.getWebResponse().getStatusCode(); 
				if(200==statusCodeHuaFei && 200==statusCodeTaoCan){
					String htmlHuaFei=pageHuaFei.getWebResponse().getContentAsString();
					String htmlTaoCan=pageTaoCan.getWebResponse().getContentAsString();
					WebParam<TelecomShanxi3CurrentSituation> webParam= telecomShanXi3Parser.currentSituationParser(taskMobile,htmlHuaFei,htmlTaoCan);
					//保存页面信息
					TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
					htmlStore.setTaskid(taskMobile.getTaskid());
					htmlStore.setType("我的现状信息源码页");
					htmlStore.setPageCount(1);
					htmlStore.setUrl("话费的url "+urlHuaFei+" 华丽丽的分界线 "+" 套餐的url "+urlTaoCan);
					htmlStore.setHtml("话费的html "+htmlHuaFei+" 信息分界线 "+" 套餐的html "+htmlTaoCan);
					List<String> monthList= CalendarParamService.getMonthIncludeThis();
					htmlStore.setYearmonth(monthList.get(0));
					telecomShanxi3HtmlRepository.save(htmlStore);
					tracer.addTag("action.crawler.getCurrentSituation.html", "我的现状信息源码页已经入库"+taskMobile.getTaskid());
					if(null!=webParam.getTelecomShanxi3CurrentSituation()){
						telecomShanxi3CurrentSituationRepository.save(webParam.getTelecomShanxi3CurrentSituation());
						tracer.addTag("action.crawler.getCurrentSituation", "我的现状信息已入库"+taskMobile.getTaskid());
						//此处爬取完现状信息，现状信息中有积分信息，且能稳定爬取，故将更新账户信息的代码放于此处（因为陕西的账户只有星级提供爬取）
						crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "数据采集中，账户信息已采集完成");
					}else{
						telecomShanxi3CurrentSituationRepository.save(webParam.getTelecomShanxi3CurrentSituation());
						tracer.addTag("action.crawler.getCurrentSituation", "我的现状信息已入库"+taskMobile.getTaskid());
						//此处爬取完现状信息，现状信息中有积分信息，且能稳定爬取，故将更新账户信息的代码放于此处（因为陕西的账户只有星级提供爬取）
						crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 201, "数据采集中，账户信息已采集完成");
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getCurrentSituation", taskMobile.getTaskid()+"    "+e.toString());
			crawlerStatusMobileService.updateAccountMsgStatus(taskMobile.getTaskid(), 404, "数据采集中，账户信息已采集完成");
		}
		
		return new AsyncResult<String>("200");	
	}
	/**
	 * 获取余额信息
	 * @param messageLogin
	 * @param taskMobile
	 * @param webClient
	 * @return 
	 */
	@Async
	public Future<String> getBalance(MessageLogin messageLogin, TaskMobile taskMobile,String encryptedPhone)  {
		try {
			WebClient webClient=loginAndGetCommonService.addcookie(taskMobile);
			String url="http://sn.189.cn/service/bill/realtimeSearch.action?phoneNum="+encryptedPhone+"&prodTypeId=4";
			webClient.getOptions().setTimeout(20000);	
			webClient.getOptions().setJavaScriptEnabled(false);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest);
			if(page!=null){
				int statusCode = page.getWebResponse().getStatusCode();
				if(200==statusCode){
					String contentAsString = page.getWebResponse().getContentAsString();
					if(!contentAsString.contains("当您点击某个链接时，它可能已过期") && !contentAsString.contains("在地址中可能存在键入错误") 
							&& !contentAsString.contains("系统异常") && !contentAsString.contains("您查询的时间段内没有详单数据")
							&& !contentAsString.contains("登录失败") && !contentAsString.contains("欢迎登录") 
							&& !contentAsString.contains("外围接口服务不存在或错误") && !contentAsString.contains("连接超时")
							&& !contentAsString.contains("服务调用失败") && !"".equals(contentAsString)
							&& !"null".equals(contentAsString) && !contentAsString.contains("查询号码为空"))
					{
						List<TelecomShanxi3Balance> list= telecomShanXi3Parser.balanceParser(taskMobile,contentAsString);
						//保存页面信息
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("余额信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBalance.html", "余额信息源码页已经入库"+taskMobile.getTaskid());
						if(null!=list && list.size()>0){
							telecomShanxi3BalanceRepository.saveAll(list);
							tracer.addTag("action.crawler.getBalance", "余额信息已入库"+taskMobile.getTaskid());
						}
					}else{
						//无查询记录或者是出现其他情况
						TelecomShanxi3Html htmlStore = new TelecomShanxi3Html();
						htmlStore.setTaskid(taskMobile.getTaskid());
						htmlStore.setType("余额信息源码页");
						htmlStore.setPageCount(1);
						htmlStore.setUrl(url);
						htmlStore.setHtml(contentAsString);
						telecomShanxi3HtmlRepository.save(htmlStore);
						tracer.addTag("action.crawler.getBalance.html", "余额信息源码页无可查询数据"+taskMobile.getTaskid());
					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBalance",taskMobile.getTaskid()+"    "+taskMobile.getTaskid()+"    "+e.toString());
		}
		return new AsyncResult<String>("200");	
	}
}
	
