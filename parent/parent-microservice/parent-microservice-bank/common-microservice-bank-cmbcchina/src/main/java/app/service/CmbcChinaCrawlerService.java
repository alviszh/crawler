package app.service;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitCardHtml;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitcardDepositInfo;
import com.microservice.dao.entity.crawler.bank.cmbcchina.CmbcChinaDebitcardUserInfo;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaDebitCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaDebitCardTransFlowRepository;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaDebitcardDepositInfoRepository;
import com.microservice.dao.repository.crawler.bank.cmbcchina.CmbcChinaDebitcardUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.CmbcChinaParser;
import app.service.common.CmbcChinaHelperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月31日 下午5:51:46 
 */
@Component
public class CmbcChinaCrawlerService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmbcChinaDebitCardHtmlRepository cmbcChinaDebitCardHtmlRepository;
	@Autowired
	private CmbcChinaParser cmbcChinaParser;
	@Autowired
	private CmbcChinaDebitcardUserInfoRepository cmbcChinaDebitcardUserInfoRepository; 
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	@Autowired
	private CmbcChinaDebitCardTransFlowRepository cmbcChinaDebitCardTransFlowRepository;
	@Autowired
	private CmbcChinaHelperService cmbcChinaHelperService;
	@Autowired
	private CmbcChinaDebitcardDepositInfoRepository cmbcChinaDebitcardDepositInfoRepository;
	//爬取用户信息
	public void getUserInfo(TaskBank taskBank) throws Exception {
		WebClient webClient=cmbcChinaHelperService.addcookie(taskBank);
		String url="https://nper.cmbc.com.cn/pweb/AcListInitQry.do";  //将当前用户下所有的账户信息存储（多张卡情况）
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
 		Page hPage = webClient.getPage(webRequest);  
 		if(null!=hPage){
 			String html= hPage.getWebResponse().getContentAsString(); 
 			CmbcChinaDebitCardHtml cmbcChinaDebitCardHtml=new CmbcChinaDebitCardHtml();
 			cmbcChinaDebitCardHtml.setHtml(html);
 			cmbcChinaDebitCardHtml.setPagenumber(1);
 			cmbcChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
 			cmbcChinaDebitCardHtml.setType("用户基本信息源码页");
 			cmbcChinaDebitCardHtml.setUrl(url);
 			cmbcChinaDebitCardHtmlRepository.save(cmbcChinaDebitCardHtml);
			tracer.addTag("action.crawler.getUserInfo.html", "数据采集中，用户信息源码页已经入库");
			List<CmbcChinaDebitcardUserInfo> list=cmbcChinaParser.userInfoParser(taskBank,html);
			if(null!=list && list.size()>0){
				cmbcChinaDebitcardUserInfoRepository.saveAll(list);
				taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_SUCCESS.getError_code(), BankStatusCode.BANK_USERINFO_SUCCESS.getDescription(), taskBank.getTaskid());
			}else{
				taskBankStatusService.updateTaskBankUserinfo(BankStatusCode.BANK_USERINFO_ERROR.getError_code(), BankStatusCode.BANK_USERINFO_ERROR.getDescription(), taskBank.getTaskid());
			}
			//根据用户信息响应的各个账号的信息来爬取账号对应的存款信息
			JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("OList");  //此集合包含当前用户的所有账户信息
			int size=jsonArray.size();
			for(int i=0;i<size;i++){
				String acNo=jsonArray.getJSONObject(i).getString("AcNo");   //账号
				String bankAcType=jsonArray.getJSONObject(i).getString("BankAcType");  //账户类型代号
				//接下来获取该账户的存款信息
				getDepositInfo(taskBank,acNo,bankAcType);
			}
 		}
	}
	//爬取存款信息
	private void getDepositInfo(TaskBank taskBank, String acNo,String bankAcType) throws Exception {
		WebClient webClient=cmbcChinaHelperService.addcookie(taskBank);
		String url="https://nper.cmbc.com.cn/pweb/SubActsQry.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		String requestBody="{\"AcNo\":\""+acNo+"\",\"BankAcType\":\""+bankAcType+"\",\"mainpageflag\":null}";  //账号和类型的两侧必须要有""
		webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
		webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Referer", "https://nper.cmbc.com.cn/pweb/static/main.html");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
		webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
		webRequest.setAdditionalHeader("Connection", "Keep-Alive");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setRequestBody(requestBody);
		Page hPage=webClient.getPage(webRequest);
		if(null!=hPage){
 			String html = hPage.getWebResponse().getContentAsString(); 
 			CmbcChinaDebitCardHtml cmbcChinaDebitCardHtml=new CmbcChinaDebitCardHtml();
 			cmbcChinaDebitCardHtml.setHtml(html);
 			cmbcChinaDebitCardHtml.setPagenumber(1);
 			cmbcChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
 			cmbcChinaDebitCardHtml.setType("用户存款信息源码页");
 			cmbcChinaDebitCardHtml.setUrl(url);
 			cmbcChinaDebitCardHtmlRepository.save(cmbcChinaDebitCardHtml);
			tracer.addTag("action.crawler.getDepositInfo.html", "数据采集中，用户存款信息源码页已经入库");
			List<CmbcChinaDebitcardDepositInfo> list=cmbcChinaParser.depositInfoParser(taskBank,html);
			if(null!=list && list.size()>0){
				cmbcChinaDebitcardDepositInfoRepository.saveAll(list);
				tracer.addTag("action.crawler.getDepositInfo", "数据采集中，用户存款信息已经入库");
			}else{
				tracer.addTag("action.crawler.getDepositInfo", "数据采集中，用户存款信息无数据可供采集");
			}
		}		
	}
	public void getTransflow(TaskBank taskBank) throws Exception {
		WebClient webClient=cmbcChinaHelperService.addcookie(taskBank);
		String url="https://nper.cmbc.com.cn/pweb/AcListQryTwo.do";
		WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
		String requestPayLoad="{}";
		webRequest.setRequestBody(requestPayLoad);
		Page page = webClient.getPage(webRequest);
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("List");
			int size=jsonArray.size();
			//定义一个变量来判断最终获取数据情况，哪怕有一条数据，爬取状态也是200
			int successFlag=0;
			for(int n=0;n<size;n++){
				//准备相关参数
				String acNo=jsonArray.getJSONObject(n).getString("AcNo");
				String bankAcType=jsonArray.getJSONObject(n).getString("BankAcType");
				String cifname=jsonArray.getJSONObject(n).getString("CifName");
				String currencyName=jsonArray.getJSONObject(n).getString("CurrencyName");
				
				//先根据第一个响应获取总记录数据
				url="https://nper.cmbc.com.cn/pweb/ActTrsQry.do";  
				webRequest=new WebRequest(new URL(url),HttpMethod.POST);
				webRequest.setAdditionalHeader("Accept", "application/json, text/plain, */*");
				webRequest.setAdditionalHeader("Content-Type", "application/json;charset=utf-8");
				webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				webRequest.setAdditionalHeader("Referer", "https://nper.cmbc.com.cn/pweb/static/main.html");
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN");
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				webRequest.setAdditionalHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; InfoPath.3)");
				webRequest.setAdditionalHeader("Host", "nper.cmbc.com.cn");
				webRequest.setAdditionalHeader("Connection", "Keep-Alive");
				webRequest.setAdditionalHeader("Cache-Control", "no-cache");
				String threeMonthAgoDate=null;
				String threeMonthLaterDate=null;
				for(int i=1;i<=8;i++){    //只能获取近两年的数据，且查询跨度最大三个月  
					threeMonthAgoDate=CmbcChinaHelperService.getBeforeMonth(3*i);  //获取前三个月
					threeMonthLaterDate=CmbcChinaHelperService.getBeforeMonth(3*(i-1));
					String requestBody="{\"AcNo\":\""+acNo.trim()+"\","
							+ "\"BankAcType\":\""+bankAcType.trim()+"\","
							+ "\"BeginDate\":\""+threeMonthAgoDate+"\","   //只能获取近两年的数据
							+ "\"EndDate\":\""+threeMonthLaterDate+"\","
							+ "\"AcName\":\""+cifname.trim()+"\","
							+ "\"Remark\":\"-\","
							+ "\"Fee\":\"0.00\","
							+ "\"FeeRemark\":\"-\","
							+ "\"SubAcSeq\":\"0001\","
							+ "\"currentIndex\":0,"				
							+ "\"uri\":\"/pweb/ActTrsQry.do\"}"; 
		 			webRequest.setRequestBody(requestBody);
		 			Page hPage=webClient.getPage(webRequest);
		 			if(null!=hPage){
		 	 			String html0 = hPage.getWebResponse().getContentAsString(); 
		 	 			String totalRecordCount = JSONObject.fromObject(html0).getJSONObject("_PageHeader").getString("TotalCount");
		 				if(totalRecordCount.equals("0")){   //没有相关数据
		 					tracer.addTag(taskBank.getTaskid()+"日志"+i,threeMonthAgoDate+"至"+threeMonthLaterDate+"无流水信息可供采集");
		 					taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+threeMonthAgoDate+"至"+threeMonthLaterDate+"无流水信息可供采集", taskBank.getTaskid());
		 				}else{
		 					//有数据可以采集：
		 					successFlag++;
		 					CmbcChinaDebitCardHtml cmbcChinaDebitCardHtml=new CmbcChinaDebitCardHtml();
		 	 	 			cmbcChinaDebitCardHtml.setHtml(html0);
		 	 	 			cmbcChinaDebitCardHtml.setPagenumber(1);
		 	 	 			cmbcChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
		 	 	 			cmbcChinaDebitCardHtml.setType("初始数据"+threeMonthAgoDate+"至"+threeMonthLaterDate+"查询区间流水信息总记录数获取源码页");
		 	 	 			cmbcChinaDebitCardHtml.setUrl(url);
		 	 	 			cmbcChinaDebitCardHtmlRepository.save(cmbcChinaDebitCardHtml);
		 	 				tracer.addTag(threeMonthAgoDate+"至"+threeMonthLaterDate+"action.crawler.getTotalCount.html", "该查询区间获取的总记录数是："+totalRecordCount);
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
		 	 				for(int j=1;j<=totalPageCount;j++){
		 	 					currentIndex=(j-1)*10;
		 	 					requestBody="{\"AcNo\":\""+acNo.trim()+"\","
		 	 							+ "\"BankAcType\":\""+bankAcType.trim()+"\","
		 	 							+ "\"BeginDate\":\""+threeMonthAgoDate+"\","   //只能获取近两年的数据
		 	 							+ "\"EndDate\":\""+threeMonthLaterDate+"\","
		 	 							+ "\"AcName\":\""+cifname.trim()+"\","
		 	 							+ "\"Remark\":\"-\","
		 	 							+ "\"Fee\":\"0.00\","
		 	 							+ "\"FeeRemark\":\"-\","
		 	 							+ "\"SubAcSeq\":\"0001\","
		 	 							+ "\"currentIndex\":"+currentIndex+","				
		 	 							+ "\"uri\":\"/pweb/ActTrsQry.do\","
		 	 							+ "\"pageNo\":"+j+","
		 	 							+ "\"recordNumber\":"+totalRecordCount+"}"; 
		 	 					webRequest.setRequestBody(requestBody);
		 	 					page = webClient.getPage(webRequest);
		 	 					if(null!=page){
		 	 						html=page.getWebResponse().getContentAsString();
		 	 						cmbcChinaDebitCardHtml=new CmbcChinaDebitCardHtml();   //此处需要重新new对象，否则上边insert,此处就是update了
		 	 						cmbcChinaDebitCardHtml.setHtml(html);
	 	 		 	 	 			cmbcChinaDebitCardHtml.setPagenumber(j);  //页码
	 	 		 	 	 			cmbcChinaDebitCardHtml.setTaskid(taskBank.getTaskid());
	 	 		 	 	 			cmbcChinaDebitCardHtml.setType(threeMonthAgoDate+"至"+threeMonthLaterDate+"第"+j+"页流水信息");
	 	 		 	 	 			cmbcChinaDebitCardHtml.setUrl(url);
	 	 		 	 	 			cmbcChinaDebitCardHtmlRepository.save(cmbcChinaDebitCardHtml);
	 	 		 	 				tracer.addTag("action.crawler.getTransflow.html"+j, threeMonthAgoDate+"至"+threeMonthLaterDate+"第"+j+"页流水信息源码页已入库");
		 	 						List<CmbcChinaDebitCardTransFlow> list=cmbcChinaParser.transflowParser(taskBank,html,acNo,currencyName);
		 	 						if(null!=list && list.size()>0){
		 	 							cmbcChinaDebitCardTransFlowRepository.saveAll(list);
		 	 		 	 				taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，"+threeMonthAgoDate+"至"+threeMonthLaterDate+"第"+j+"页流水信息已采集完成", taskBank.getTaskid());
		 	 						}
		 	 						else{
		 	 							tracer.addTag("action.crawler.getTransflow.html"+j, threeMonthAgoDate+"至"+threeMonthLaterDate+"第"+j+"页流水信息无数据可供采集");
		 	 		 	 				taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，"+threeMonthAgoDate+"至"+threeMonthLaterDate+"第"+j+"页流水信息无数据可供采集", taskBank.getTaskid());
		 	 						}
		 	 					}
		 	 				}
		 				}
		 			}
				}
 			}
			//判断最终结果，更新task表
			if(successFlag>0){
				tracer.addTag("action.crawler.getTransflow.result", "流水信息全部采集完成");
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_SUCCESS.getError_code(), BankStatusCode.BANK_TRANSFLOW_SUCCESS.getDescription(), taskBank.getTaskid());
				//爬取完毕，爬取状态进行更新
				taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			}else{
				tracer.addTag("action.crawler.getTransflow.result", "流水信息无数据可供采集");
				taskBankStatusService.updateTaskBankTransflow(BankStatusCode.BANK_TRANSFLOW_ERROR.getError_code(), BankStatusCode.BANK_TRANSFLOW_ERROR.getDescription(), taskBank.getTaskid());
				//爬取完毕，爬取状态进行更新
				taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			}
 		}
	}
}
