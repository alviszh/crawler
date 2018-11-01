package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardBillDetail;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardBillGeneral;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardGeneralInfo;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardHtml;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaCreditCardMyAccount;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaCreditCardBillDetailRepository;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaCreditCardBillGeneralRepository;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaCreditCardGeneralInfoRepository;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaCreditCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaCreditCardMyAccountRepository;

import app.commontracerlog.TracerLog;
import app.parser.CmbcChinaCreditParser;
import app.service.common.CmbcChinaHelperService;
import net.sf.json.JSONObject;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月14日 下午2:50:03 
 */
@Component
public class CmbcChinaCreditCrawlerService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmbcChinaCreditCardHtmlRepository cmbcChinaCreditCardHtmlRepository;
	@Autowired
	private CmbcChinaCreditCardGeneralInfoRepository cmbcChinaCreditCardGeneralInfoRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CmbcChinaCreditCardMyAccountRepository cmbcChinaCreditCardMyAccountRepository;
	@Autowired
	private CmbcChinaCreditCardBillGeneralRepository cmbcChinaCreditCardBillGeneralRepository;
	@Autowired
	private CmbcChinaCreditCardBillDetailRepository cmbcChinaCreditCardBillDetailRepository;
	@Autowired
	private CmbcChinaHelperService cmbcChinaHelperService;
	@Autowired
	private CmbcChinaCreditParser cmbcChinaCreditParser;
	
	public static String acNo;
	
	//信用卡信息
	@Async
	public Future<String> getGeneralInfo(TaskBank taskBank) throws Exception {
		String url="https://nper.cmbc.com.cn/pweb/CreditCardListQry.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			CmbcChinaCreditCardHtml cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
			cmbcChinaCreditCardHtml.setHtml(html);
			cmbcChinaCreditCardHtml.setPagenumber(1);
			cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
			cmbcChinaCreditCardHtml.setType("信用卡信息源码页");
			cmbcChinaCreditCardHtml.setUrl(url);
			cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
			tracer.addTag("action.crawler.getGeneralInfo.html.store", "信用卡信息源码页已入库");
			List<CmbcChinaCreditCardGeneralInfo> list=cmbcChinaCreditParser.generalInfoParser(taskBank,html);
			if(null!=list && list.size()>0){
				cmbcChinaCreditCardGeneralInfoRepository.saveAll(list);
				taskBankStatusService.updateTaskBankUserinfo(200, "数据采集中，信用卡信息已采集完成", taskBank.getTaskid());
				tracer.addTag("action.crawler.getGeneralInfo", "数据采集中，信用卡信息已采集完成");
			}else{
				taskBankStatusService.updateTaskBankUserinfo(201, "数据采集中，信用卡信息已采集完成", taskBank.getTaskid());
				tracer.addTag("action.crawler.getGeneralInfo", "数据采集中，信用卡信息未提供可采集数据");
			}
		}
		return new AsyncResult<String>("200");
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//我的账户信息
	@Async
	public Future<String> getMyAccount(TaskBank taskBank) throws Exception {
		String url="https://nper.cmbc.com.cn/pweb/CreditUserInfoQryTrans.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			CmbcChinaCreditCardHtml cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
			cmbcChinaCreditCardHtml.setHtml(html);
			cmbcChinaCreditCardHtml.setPagenumber(1);
			cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
			cmbcChinaCreditCardHtml.setType("我的账户信息源码页");
			cmbcChinaCreditCardHtml.setUrl(url);
			cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
			tracer.addTag("action.crawler.getMyAccount.html.store", "我的账户信息源码页已入库");
			CmbcChinaCreditCardMyAccount cmbcChinaCreditCardMyAccount=cmbcChinaCreditParser.myAccountParser(taskBank,html);
			if(null!=cmbcChinaCreditCardMyAccount){
				cmbcChinaCreditCardMyAccountRepository.save(cmbcChinaCreditCardMyAccount);
				taskBankStatusService.updateTaskBankUserinfo(200, "数据采集中，我的账户信息已采集完成", taskBank.getTaskid());
				tracer.addTag("action.crawler.getMyAccount", "数据采集中，我的账户信息已采集完成");
			}
		}
		return new AsyncResult<String>("200");
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//账单明细信息爬取
	public void getBillDetailBeginSixMonth(TaskBank taskBank, String billDetailDate, String billDay, String creditAcType) throws Exception {
		WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
		String getAcNoUrl="https://nper.cmbc.com.cn/pweb/CreditBillTitleQry.do";
		WebRequest webRequest=new WebRequest(new URL(getAcNoUrl),HttpMethod.POST);
		//期初CurrencyFlag设置的是L  表示人民币，  设置为null 表示全部币种
		String requestBody="{\"CreditAcType\":\""+creditAcType+"\",\"CurrencyFlag\":\"null\"," 
				+ "\"BillDate\":\""+billDetailDate+"\",\"BillDay\":\""+billDay+"\"}"; 
		webRequest.setRequestBody(requestBody);
		//如下请求头必须加上，否则无法响应正确的信息
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
		webRequest.setAdditionalHeader("Referer", "https://nper.cmbc.com.cn/pweb/static/main.html");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		Page page= webClient.getPage(webRequest);
	    if(null!=page){
	    	String html=page.getWebResponse().getContentAsString();
	    	CmbcChinaCreditCardHtml cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
			cmbcChinaCreditCardHtml.setHtml(html);
			cmbcChinaCreditCardHtml.setPagenumber(1);
			cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
			cmbcChinaCreditCardHtml.setType(billDetailDate+"账期内账号参数信息源码页");
			cmbcChinaCreditCardHtml.setUrl(getAcNoUrl);
			cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
			if(html.contains("jsonError")){
				taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+billDetailDate+"无账单明细信息可供采集", taskBank.getTaskid());
				tracer.addTag("action.crawler.getBillDetailBeginSixMonth", "数据采集中，"+billDetailDate+"无账单明细信息可供采集");
			}else{
//				String acNo=JSONObject.fromObject(html).getString("AcNo");
				acNo=JSONObject.fromObject(html).getString("AcNo");
				//爬取账单明细信息,先请求一次，获取该账期的总记录数，先根据上述返回值获取账号作为接下来的参数
				String url="https://nper.cmbc.com.cn/pweb/CreditBillQry.do";
				webRequest=new WebRequest(new URL(url),HttpMethod.POST);
				requestBody="{\"CreditAcType\":\""+creditAcType+"\",\"CurrencyFlag\":\"L\","
						+ "\"BillDate\":\""+billDetailDate+"\",\"BillDay\":\""+billDay+"\","
								+ "\"AcNo\":\""+acNo+"\",\"uri\":\"/pweb/CreditBillQry.do\","
										+ "\"currentIndex\":0,\"BillFlag\":\"1\"}";
				webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
				webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
				webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
				webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
				webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
				webRequest.setAdditionalHeader("Referer", "https://nper.cmbc.com.cn/pweb/static/main.html");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("Connection", "Keep-Alive");
				webRequest.setRequestBody(requestBody);
				page= webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
					cmbcChinaCreditCardHtml.setHtml(html);
					cmbcChinaCreditCardHtml.setPagenumber(1);
					cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
					cmbcChinaCreditCardHtml.setType(billDetailDate+"账期内初始化账单明细信息源码页");
					cmbcChinaCreditCardHtml.setUrl(url);
					cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
					tracer.addTag("action.crawler.getBillDetailBeginSixMonth.html.store", billDetailDate+"账期内初始化账单明细信息源码页已入库");
					if(html.contains("jsonError")){
						taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+billDetailDate+"无账单明细信息可供采集", taskBank.getTaskid());
						tracer.addTag("action.crawler.getBillDetailBeginSixMonth", "数据采集中，"+billDetailDate+"无账单明细信息可供采集");
					}else{
						//根据返回的信息获取该账期内的总记录数
						String totalRecordCount = JSONObject.fromObject(html).getString("recordNumber");
						//有数据可供采集
						int parseInt = Integer.parseInt(totalRecordCount);
		 				int remainder=parseInt%10;
		 				int totalPageCount=0;
		 				if(remainder>0){  //如果余数大于0，将如下运算的数据加1成为最终的总页数
		 					totalPageCount=parseInt/10+1;
		 				}else{
		 					totalPageCount=parseInt/10;
		 				}
		 				//接下来爬取该区间中每一页的数据
		 				int currentIndex=0;
		 				for(int i=1;i<=totalPageCount;i++){
		 					currentIndex=(i-1)*10;
		 					//接下来异步解析该账期内的每一页数据
		 					try {
		 						getBillDetailEachPage(taskBank,acNo,creditAcType,billDetailDate,billDay,totalRecordCount,i,currentIndex);
							} catch (Exception e) {
								taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成", taskBank.getTaskid());
								tracer.addTag("action.crawler.getBillDetailEachPage.e", e.toString());
							}
		 				}
					}
				}
			}
		}
	}
	private void getBillDetailEachPage(TaskBank taskBank, String acNo, String creditAcType, String billDetailDate,String billDay, String totalRecordCount, int i, int currentIndex) throws Exception, IOException {
		WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
		String url="https://nper.cmbc.com.cn/pweb/CreditBillQry.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		String requestBody="{\"CreditAcType\":\""+creditAcType+"\","
					+ "\"CurrencyFlag\":\"L\","
					+ "\"BillDate\":\""+billDetailDate+"\",\"BillDay\":\""+billDay+"\","
					+ "\"AcNo\":\""+acNo+"\","
					+ "\"uri\":\"/pweb/CreditBillQry.do\","
					+ "\"currentIndex\":"+currentIndex+",\"BillFlag\":\"1\","
					+ "\"pageNo\":"+i+",\"recordNumber\":"+totalRecordCount+"}";
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
		webRequest.setAdditionalHeader("Referer", "https://nper.cmbc.com.cn/pweb/static/main.html");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			CmbcChinaCreditCardHtml cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
			cmbcChinaCreditCardHtml.setHtml(html);
			cmbcChinaCreditCardHtml.setPagenumber(i);
			cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
			cmbcChinaCreditCardHtml.setType(billDetailDate+"账期内第"+i+"页账单明细信息源码页");
			cmbcChinaCreditCardHtml.setUrl(url);
			cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
			tracer.addTag("action.crawler.getBillDetail.html.store", billDetailDate+"账单明细信息源码页已入库");
			//{"jsonError":[{"_exceptionMessageCode":"EFE0004","_exceptionMessage":"EFE0004:未找到符合条件的记录。"}]}
			if(html.contains("jsonError")){
				taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成", taskBank.getTaskid());
				tracer.addTag("action.crawler.getBillDetail", "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成");
			}else{
				List<CmbcChinaCreditCardBillDetail> list=cmbcChinaCreditParser.billDetailParser(taskBank,html);
				if(null!=list && list.size()>0){
					cmbcChinaCreditCardBillDetailRepository.saveAll(list);
					taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成", taskBank.getTaskid());
					tracer.addTag("action.crawler.getBillDetail", "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成");
				}
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//账单概要信息爬取
	@Async
	public Future<String> getBillGeneral(TaskBank taskBank, String billDate, String billDay, String creditAcType) throws Exception {
		String url="https://nper.cmbc.com.cn/pweb/CreditBillTitleQry.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
		String requestBody="{\"CreditAcType\":\""+creditAcType+"\",\"CurrencyFlag\":\"L\",\"BillDate\":\""+billDate+"\",\"BillDay\":\""+billDay+"\"}";
		webRequest.setRequestBody(requestBody);
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			tracer.addTag("action.crawler.getBillGeneral.html", html);
			CmbcChinaCreditCardHtml cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
			cmbcChinaCreditCardHtml.setHtml(html);
			cmbcChinaCreditCardHtml.setPagenumber(1);
			cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
			cmbcChinaCreditCardHtml.setType(billDate+"账单概要信息源码页");
			cmbcChinaCreditCardHtml.setUrl(url);
			cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
			tracer.addTag("action.crawler.getBillGeneral.html.store", billDate+"账单概要信息源码页已入库");
			if(html.contains("无该卡片的账单记录")){
				taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+billDate+"无账单概要信息可供采集", taskBank.getTaskid());
				tracer.addTag("action.crawler.getBillGeneral", "数据采集中，"+billDate+"无账单概要信息可供采集");
			}else{
				//有概要信息，才爬取对应的明细信息
				CmbcChinaCreditCardBillGeneral cmbcChinaCreditCardBillGeneral=cmbcChinaCreditParser.billGeneralParser(taskBank,html);
				if(null!=cmbcChinaCreditCardBillGeneral){
					cmbcChinaCreditCardBillGeneralRepository.save(cmbcChinaCreditCardBillGeneral);
					taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，"+billDate+"账单概要信息已采集完成", taskBank.getTaskid());
					tracer.addTag("action.crawler.getBillGeneral", "数据采集中，"+billDate+"账单概要信息已采集完成");
				}
			}
		}
		return new AsyncResult<String>("200");	
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void getBillDetailOtherSixMonth(TaskBank taskBank,String billDetailDate, String billDay, String creditAcType) throws Exception {
		//为防止爬取该部分信息时，响应的页面说本外币标志不能为空，故爬取近期数据(getBillDetailBeginSixMonth)的时候，将accNo存为静态变量，下述内容直接使用，注释前提请求信息
		WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
		String url="https://nper.cmbc.com.cn/pweb/CreditBillQry.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		String requestBody="{\"CreditAcType\":\""+creditAcType+"\",\"CurrencyFlag\":\"L\","
				+ "\"BillDate\":\""+billDetailDate+"\",\"BillDay\":\""+billDay+"\","
						+ "\"AcNo\":\""+acNo+"\","
							+"\"BillSixQry\":\"BillSixQry\","
								+ "\"uri\":\"/pweb/CreditBillQry.do\","
								+ "\"currentIndex\":0,\"BillFlag\":\"1\"}";
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
		webRequest.setAdditionalHeader("Referer", "https://nper.cmbc.com.cn/pweb/static/main.html");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setRequestBody(requestBody);
		Page page= webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			CmbcChinaCreditCardHtml cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
			cmbcChinaCreditCardHtml.setHtml(html);
			cmbcChinaCreditCardHtml.setPagenumber(1);
			cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
			cmbcChinaCreditCardHtml.setType(billDetailDate+"账期内初始化账单明细信息源码页");
			cmbcChinaCreditCardHtml.setUrl(url);
			cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
			tracer.addTag("action.crawler.getBillDetailOtherSixMonth.html.store", billDetailDate+"账期内初始化账单明细信息源码页已入库");
//			if(html.contains("未找到符合条件的记录") || html.contains("无该卡片的账单记录") || html.contains("前置数据库错误 ")){
			if(html.contains("jsonError")){
				taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+billDetailDate+"无账单明细信息可供采集", taskBank.getTaskid());
				tracer.addTag("action.crawler.getBillDetailOtherSixMonth", "数据采集中，"+billDetailDate+"无账单明细信息可供采集");
			}else{
				//根据返回的信息获取该账期内的总记录数
				String totalRecordCount = JSONObject.fromObject(html).getString("recordNumber");
				//有数据可供采集
				int parseInt = Integer.parseInt(totalRecordCount);
 				int remainder=parseInt%10;
 				int totalPageCount=0;
 				if(remainder>0){  //如果余数大于0，将如下运算的数据加1成为最终的总页数
 					totalPageCount=parseInt/10+1;
 				}else{
 					totalPageCount=parseInt/10;
 				}
 				//接下来爬取该区间中每一页的数据
 				int currentIndex=0;
 				for(int i=1;i<=totalPageCount;i++){
 					currentIndex=(i-1)*10;
 					//接下来异步解析该账期内的每一页数据
 					try {
 						getBillDetailEachPageOtherSixMonth(taskBank,acNo,creditAcType,billDetailDate,billDay,totalRecordCount,i,currentIndex);
					} catch (Exception e) {
						taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成", taskBank.getTaskid());
						tracer.addTag("action.crawler.getBillDetailEachPageOtherSixMonth.e", e.toString());
					}
 				}
			}
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void getBillDetailEachPageOtherSixMonth(TaskBank taskBank, String acNo, String creditAcType,String billDetailDate, String billDay, String totalRecordCount, int i, int currentIndex) throws Exception{
		WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
		String url="https://nper.cmbc.com.cn/pweb/CreditBillQry.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		String requestBody="{\"CreditAcType\":\""+creditAcType+"\","
					+ "\"CurrencyFlag\":\"L\","
					+ "\"BillDate\":\""+billDetailDate+"\",\"BillDay\":\""+billDay+"\","
					+ "\"AcNo\":\""+acNo+"\","
					+"\"BillSixQry\":\"BillSixQry\","
					+ "\"uri\":\"/pweb/CreditBillQry.do\","
					+ "\"currentIndex\":"+currentIndex+",\"BillFlag\":\"1\","
					+ "\"pageNo\":"+i+",\"recordNumber\":"+totalRecordCount+"}";
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
		webRequest.setAdditionalHeader("Referer", "https://nper.cmbc.com.cn/pweb/static/main.html");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setRequestBody(requestBody);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			CmbcChinaCreditCardHtml cmbcChinaCreditCardHtml=new CmbcChinaCreditCardHtml();
			cmbcChinaCreditCardHtml.setHtml(html);
			cmbcChinaCreditCardHtml.setPagenumber(i);
			cmbcChinaCreditCardHtml.setTaskid(taskBank.getTaskid());
			cmbcChinaCreditCardHtml.setType(billDetailDate+"账期内第"+i+"页账单明细信息源码页");
			cmbcChinaCreditCardHtml.setUrl(url);
			cmbcChinaCreditCardHtmlRepository.save(cmbcChinaCreditCardHtml);
			tracer.addTag("action.crawler.getBillDetail.html.store", billDetailDate+"账单明细信息源码页已入库");
			//{"jsonError":[{"_exceptionMessageCode":"EFE0004","_exceptionMessage":"EFE0004:未找到符合条件的记录。"}]}
			if(html.contains("jsonError")){
				taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成", taskBank.getTaskid());
				tracer.addTag("action.crawler.getBillDetail", "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成");
			}else{
				List<CmbcChinaCreditCardBillDetail> list=cmbcChinaCreditParser.billDetailParser(taskBank,html);
				if(null!=list && list.size()>0){
					cmbcChinaCreditCardBillDetailRepository.saveAll(list);
					taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成", taskBank.getTaskid());
					tracer.addTag("action.crawler.getBillDetail", "数据采集中，"+billDetailDate+"账期内第"+i+"页账单明细信息已采集完成");
				}
			}
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
