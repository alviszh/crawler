package app.crawler.telecomhtmlunit;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.unit.CommonunitQingHai;
import app.unit.TeleComCommonUnit;

@Component
@Service
public class LognAndGetQingHai {

	public static final Logger log = LoggerFactory.getLogger(LognAndGetQingHai.class);

	// 获取青海账单信息
	public static String getBill(MessageLogin messageLogin, TaskMobile taskMobile,String stardate) throws Exception {
		String url = "http://qh.189.cn/service/bill/queryBillInfoDetail.action?month="+stardate;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
	    webClient = CommonunitQingHai.getWebClientForTeleComQingHai(taskMobile);

		Page page = TeleComCommonUnit.getHtml(url, webClient);
		System.out.println(page.getWebResponse().getContentAsString());
		return page.getWebResponse().getContentAsString();
	}

	// 获取青海用户积分 话费余额
	public static String getPointsAndCharges(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception, IOException {
		String url = "http://www.189.cn/dqmh/order/getHuaFei.do";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

		Page page = TeleComCommonUnit.gethtmlPost(webClient, null, url);

		return page.getWebResponse().getContentAsString();
	}

	// 获取青海用户缴费信息 =============================================
	public static String getpayResult(MessageLogin messageLogin, TaskMobile taskMobile, String stardate,
			String enddate) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
	    webClient = CommonunitQingHai.getWebClientForTeleComQingHai(taskMobile);

	    System.out.println(stardate+"====="+enddate);

	    String url =  "http://qh.189.cn/service/pay/payrecordlist.action";
		/*String url = "http://qh.189.cn/service/pay/payrecordlist.action"
				+ "?currentPage=1&pageSize=100&recordReq.beginDate="+stardate+
				"&recordReq.endDate="+enddate+"&recordReq.queryNum="+messageLogin.getName().trim() + ",4"+
				"&recordReq.queryType=undefined";*/
		System.out.println("========="+url);
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("currentPage", "1"));
		paramsList.add(new NameValuePair("pageSize", "10"));
		paramsList.add(new NameValuePair("recordReq.beginDate", stardate));
		paramsList.add(new NameValuePair("recordReq.endDate", enddate));
		paramsList.add(new NameValuePair("recordReq.queryNum", messageLogin.getName().trim() + ",4"));
		paramsList.add(new NameValuePair("recordReq.queryType", "undefined"));
		try {

			webClient.setJavaScriptTimeout(20000);

			webClient.getOptions().setTimeout(20000); // 15->60

			Page page = TeleComCommonUnit.gethtmlPost(webClient, paramsList, url);
			//Page page = TeleComCommonUnit.getHtml(url, webClient);
			return page.getWebResponse().getContentAsString();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	// 获取青海积分信息
	public static String getintegraResult(MessageLogin messageLogin, TaskMobile taskMobile, String stardate) throws Exception, IOException {
		String url = "http://qh.189.cn/service/point/pointDetailQueryAjax.action";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
	    webClient = CommonunitQingHai.getWebClientForTeleComQingHai(taskMobile);


		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("currentTime", stardate));

		Page page = TeleComCommonUnit.gethtmlPost(webClient, paramsList, url);
		return page.getWebResponse().getContentAsString();
	}

	// 获取青海通话详单
	public static String getPhoneBill(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile, String effDate,
			String expDate) throws Exception {
		
		
		String url = "http://qh.189.cn/service/bill/feeDetailrecordList.action"
				+ "?currentPage=1&pageSize=10"
				+ "&effDate="+effDate
				+ "&expDate="+expDate
				+ "&serviceNbr="+messageLogin.getName().trim()
				+ "&operListID=12"//11代表短信详单  12代表通话详单
				+ "&pOffrType=4&sendSmsFlag=true&num=1&callTypeVal=0";
		Page page = TeleComCommonUnit.getHtml(url, webClient);
		
		return page.getWebResponse().getContentAsString();
	}

	// 获取青海短信详单
	public static String getSMSBill(WebClient webClient,MessageLogin messageLogin, TaskMobile taskMobile, String effDate,
			String expDate) throws Exception {
		String url = "http://qh.189.cn/service/bill/feeDetailrecordList.action"
				+ "?currentPage=1&pageSize=1000"
				+ "&effDate="+effDate
				+ "&expDate="+expDate
				+ "&serviceNbr="+messageLogin.getName().trim()
				+ "&operListID=11" //11代表短信详单  12代表通话详单
				+ "&pOffrType=4&sendSmsFlag=true&num=1&callTypeVal=0";
		
		Page page = TeleComCommonUnit.getHtml(url, webClient);
		return page.getWebResponse().getContentAsString();
	}
	
	/**获取北京用户 获取青海用余额
	 * @param webClient
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static String getFeeBalance(MessageLogin messageLogin, TaskMobile taskMobile) throws FailingHttpStatusCodeException, IOException {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		
		String url = "http://qh.189.cn/service/account/feeBalance.action?rnd=0.18901843621324654";
		
		Page page = TeleComCommonUnit.gethtmlPost(webClient, null, url);
		return page.getWebResponse().getContentAsString();
	}
	
	/**获取北京用户 获取青海用 积分
	 * @param webClient
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static String getUseablePoint(MessageLogin messageLogin, TaskMobile taskMobile) throws FailingHttpStatusCodeException, IOException {
	
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		
		String url = "http://qh.189.cn/service/account/useablePoint.action?rnd=0.22502386000017371";
		
		Page page = TeleComCommonUnit.gethtmlPost(webClient, null, url);
		return page.getWebResponse().getContentAsString();
	}
	
	/**获取北京用户 获取青海 套餐
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws IOException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static String getHandledBiz(MessageLogin messageLogin, TaskMobile taskMobile) throws FailingHttpStatusCodeException, IOException {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		
		String url = "http://qh.189.cn/service/account/handledBiz.action?rnd=0.653946027545794";
		
		Page page = TeleComCommonUnit.gethtmlPost(webClient, null, url);
		return page.getWebResponse().getContentAsString();
	}
	
	
	/**获取北京用户 获取青海 用户名
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception 
	 */
	public static String getUserName(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		
		String url = "http://qh.189.cn/service/account/init.action?csrftoken=QQ_OPEN_TOKEN&fastcode=00900906&cityCode=qh&SSOURL=http://qh.189.cn/service/account/init.action?csrftoken=QQ_OPEN_TOKEN&fastcode=00900906&cityCode=qh";
		
		Page page = TeleComCommonUnit.getHtml(url, webClient);
		return page.getWebResponse().getContentAsString();
	}

	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		LocalDate effDate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-1);
		LocalDate expDate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-1);
		
		System.out.println(effDate);
		System.out.println(expDate);
	}
}
