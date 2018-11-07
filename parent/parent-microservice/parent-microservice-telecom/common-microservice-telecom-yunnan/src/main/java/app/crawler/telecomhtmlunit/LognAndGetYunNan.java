package app.crawler.telecomhtmlunit;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.BasicUser;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.TelecomYunNanCanShuAccidBean;
import app.bean.TelecomYunNanCanUserIdShuBean;
import app.bean.WebParamTelecom;
import app.unit.TeleComCommonUnit;

@Component
public class LognAndGetYunNan {

	public static final Logger log = LoggerFactory.getLogger(LognAndGetYunNan.class);

	// 获取账单信息
	public static String getBill(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean, String stardate) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_hislist_em.jsp?" + "TEMPLATE_ID=1000"
				+ "&BILLING_CYCLE=" + stardate + "&USER_ID=" + telecomYunNanCanUserIdShuBean.getUserid().trim()
				+ "&NUM=" + messageLogin.getName().trim() + "&AREA_CODE="
				+ telecomYunNanCanUserIdShuBean.getAreacode().trim();
		Page page = TeleComCommonUnit.getHtml(url, webClient);
		
		webClient.close();
		return page.getWebResponse().getContentAsString();
	}

	// 获取用户缴费信息
	public static String getpayResult(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean, String stardate, String enddate)
			throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_userchargeList.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("USER_FLAG", ""));
		paramsList.add(new NameValuePair("NAME", ""));
		paramsList.add(new NameValuePair("NUM", ""));
		paramsList.add(new NameValuePair("CITYNO", telecomYunNanCanUserIdShuBean.getAreacode().trim()));
		paramsList.add(new NameValuePair("FLAG", "1"));
		paramsList.add(new NameValuePair("S_AREA_CODE", telecomYunNanCanUserIdShuBean.getAreacode().trim()));
		paramsList.add(new NameValuePair("SERV_NAME", ""));
		paramsList.add(new NameValuePair("USER_FLAG", ""));
		paramsList.add(new NameValuePair("SERV_TYPE", ""));
		paramsList.add(new NameValuePair("SERV_NO", ""));
		paramsList.add(new NameValuePair("SERV_KIND", ""));
		paramsList.add(new NameValuePair("DESC_OPEN_TYPE", ""));
		paramsList.add(new NameValuePair("TB_SHOW_NUM", messageLogin.getName().trim()));
		paramsList.add(new NameValuePair("START_ASK_DATE", stardate));
		paramsList.add(new NameValuePair("END_ASK_DATE", enddate));

		webClient.setJavaScriptTimeout(20000);

		webClient.getOptions().setTimeout(20000); // 15->60

		Page page = TeleComCommonUnit.gethtmlPost(webClient, paramsList, url);
		
		webClient.close();

		return page.getWebResponse().getContentAsString();
	}

	// 获取积分信息
	public static String getintegraChangeResult(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomYunNanCanUserIdShuBean telecomYunNanCanUserIdShuBean, String stardate, String enddate)
			throws Exception, IOException {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		String url = "http://yn.189.cn/service/jf/parser/getBonusDetail_list.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("USER_ID", telecomYunNanCanUserIdShuBean.getUserid()));
		paramsList.add(new NameValuePair("StartDate", stardate));
		paramsList.add(new NameValuePair("EndDate", enddate));

		Page page = TeleComCommonUnit.gethtmlPost(webClient, paramsList, url);

		webClient.close();

		return page.getWebResponse().getContentAsString();

	}

	// 获取通话详单
	public static String getPhoneBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile,
			String date) throws Exception {
		

		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp?" + "NUM="
				+ messageLogin.getName().trim() + "&AREA_CODE=0871" + "&CYCLE_BEGIN_DATE=" + "&CYCLE_END_DATE="
				+ "&BILLING_CYCLE=" + date.trim() + "&QUERY_TYPE=10";
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Referer",
				"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Host", "yn.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");

		// webRequest.setRequestParameters(paramsList);

		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return page.getWebResponse().getContentAsString();
	}

	// 获取短信详单
	public static String getSMSBill(WebClient webClient, MessageLogin messageLogin, TaskMobile taskMobile, String date)
			throws Exception {
		// List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

		String url = "http://yn.189.cn/service/jt/bill/actionjt/ifr_bill_detailslist_em_new.jsp?" + "NUM="
				+ messageLogin.getName().trim() + "&AREA_CODE=0871" + "&CYCLE_BEGIN_DATE=" + "&CYCLE_END_DATE="
				+ "&BILLING_CYCLE=" + date.trim() + "&QUERY_TYPE=30";
		/*
		 * paramsList = new ArrayList<NameValuePair>(); paramsList.add(new
		 * NameValuePair("NUM", messageLogin.getName().trim()));
		 * paramsList.add(new NameValuePair("AREA_CODE", "0871"));
		 * paramsList.add(new NameValuePair("CYCLE_BEGIN_DATE", ""));
		 * paramsList.add(new NameValuePair("CYCLE_END_DATE", ""));
		 * paramsList.add(new NameValuePair("BILLING_CYCLE", date.trim()));
		 * paramsList.add(new NameValuePair("QUERY_TYPE", "30"));
		 */
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Referer",
				"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Host", "yn.189.cn");
		webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
		// webRequest.setRequestParameters(paramsList);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return page.getWebResponse().getContentAsString();
	}

	/**
	 * 爬取用户信息
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	public static String getUserInfo(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());

		String url = "http://yn.189.cn/service/manage/my_selfinfo.jsp";

		Page page = TeleComCommonUnit.getHtml(url, webClient);

		webClient.close();

		
		return page.getWebResponse().getContentAsString();
	}

	// http://yn.189.cn/service/jt/bill/actionjtv2/ifr_balance.jsp
	public static String getBalance(MessageLogin messageLogin, TaskMobile taskMobile,
			TelecomYunNanCanShuAccidBean telecomYunNanCanShuAccidBean) throws Exception {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String url = "http://yn.189.cn/service/jt/bill/actionjtv2/ifr_balance.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("NUM", messageLogin.getName().trim()));
		paramsList.add(new NameValuePair("AREA_CODE", telecomYunNanCanShuAccidBean.getAeracode().trim()));
		paramsList.add(new NameValuePair("ACCT_ID", telecomYunNanCanShuAccidBean.getAccid()));
		paramsList.add(new NameValuePair("PRODTYPE", telecomYunNanCanShuAccidBean.getProdtype()));
		paramsList.add(new NameValuePair("PRODNO", telecomYunNanCanShuAccidBean.getProdid()));
		Page page = TeleComCommonUnit.gethtmlPost(webClient, paramsList, url);
		
		webClient.close();

		return page.getWebResponse().getContentAsString();
	}

	public static String getBussiness(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String url = "http://yn.189.cn/service/manage/myprod_sm.jsp";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("BUREAUCODE", "0871"));
		paramsList.add(new NameValuePair("USER_PASSWD", ""));
		paramsList.add(new NameValuePair("ACCNBR", messageLogin.getName().trim()));
		paramsList.add(new NameValuePair("AuthFlag", "0"));
		paramsList.add(new NameValuePair("PROD_TYPE", "4"));
		Page page = TeleComCommonUnit.gethtmlPost(webClient, paramsList, url);
		
		webClient.close();

		return page.getWebResponse().getContentAsString();
	}

	public static WebParamTelecom<Object> setphonecode(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebParamTelecom<Object> webParamTelecom = new WebParamTelecom<>();
		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());

		/*String url_jsp ="http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn";
		
		try {
			Page page_jsp = TeleComCommonUnit.getHtml(url_jsp, webClient);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		
		try {
			
			BasicUser basicUser = taskMobile.getBasicUser();
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			String url = "http://yn.189.cn/public/custValid.jsp?_FUNC_ID_=WB_PAGE_PRODPASSWDQRY" + "&NAME="
					+ URLEncoder.encode(basicUser.getName().trim(),"utf8") + "&CUSTCARDNO=" + basicUser.getIdnum().trim()
					+ "&PROD_PASS=" + messageLogin.getPassword().trim() + "&MOBILE_CODE="
					+ messageLogin.getSms_code().trim() + "&NAME=" + URLEncoder.encode(basicUser.getName().trim(),"utf8")
					+ "&CUSTCARDNO=" + basicUser.getIdnum().trim();
			Page page = TeleComCommonUnit.getHtml(url, webClient);
			// <rsFlag>1</rsFlag>
			if (page.getWebResponse().getContentAsString().indexOf("<rsFlag>1</rsFlag>") == -1) {
				webParamTelecom.setErrormessage("证件姓名或证件号码不正确，请仔细核对您的客户信息");
				return webParamTelecom;
			}

			url = "http://yn.189.cn/public/pwValid.jsp";
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("_FUNC_ID_", "WB_PAGE_PRODPASSWDQRY"));
			paramsList.add(new NameValuePair("NAME", basicUser.getName().trim()));
			paramsList.add(new NameValuePair("CUSTCARDNO", basicUser.getIdnum().trim()));
			paramsList.add(new NameValuePair("PROD_PASS", messageLogin.getPassword().trim()));
			paramsList.add(new NameValuePair("MOBILE_CODE", messageLogin.getSms_code().trim()));
			paramsList.add(new NameValuePair("ACC_NBR", messageLogin.getName().trim()));
			paramsList.add(new NameValuePair("AREA_CODE", taskMobile.getAreacode().trim()));
			paramsList.add(new NameValuePair("LOGIN_TYPE", "21"));
			paramsList.add(new NameValuePair("PASSWORD", messageLogin.getPassword().trim()));
			paramsList.add(new NameValuePair("MOBILE_FLAG", "1"));
			paramsList.add(new NameValuePair("MOBILE_LOGON_NAME", messageLogin.getName().trim()));
			paramsList.add(new NameValuePair("MOBILE_CODE", messageLogin.getSms_code().trim()));
			paramsList.add(new NameValuePair("PROD_NO", "4217"));
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

			// webRequest.setAdditionalHeader("Accept", "application/xml,
			// text/xml, */*; q=0.01");
			webRequest.setAdditionalHeader("Referer",
					"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			// webRequest.setAdditionalHeader("X-Requested-With",
			// "XMLHttpRequest");
			webRequest.setAdditionalHeader("Host", "yn.189.cn");
			webRequest.setAdditionalHeader("Pragma", "no-cache");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");

			webRequest.setRequestParameters(paramsList);

			page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		
			if (page.getWebResponse().getContentAsString().indexOf("2") == -1||page.getWebResponse().getContentAsString().indexOf("-2")!=-1) {
				webParamTelecom.setErrormessage("随机验证码错误");
				webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
				return webParamTelecom;
			}
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			return webParamTelecom;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			webParamTelecom.setErrormessage("网络连接错误");
			webParamTelecom.setErrormessage(e.getMessage());
			return webParamTelecom;
		}

	}

	public static WebParamTelecom<Object> getphonecode(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebParamTelecom<Object> webParamTelecom = new WebParamTelecom<>();
		try {

			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = taskMobile.getClient(taskMobile.getCookies());

			String url = "http://yn.189.cn/public/postValidCode.jsp";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("NUM", messageLogin.getName().trim()));
			paramsList.add(new NameValuePair("AREA_CODE", taskMobile.getAreacode().trim()));
			paramsList.add(new NameValuePair("LOGIN_TYPE", "21"));
			paramsList.add(new NameValuePair("OPER_TYPE", "CR0"));
			paramsList.add(new NameValuePair("RAND_TYPE", "004"));

			webClient.setJavaScriptTimeout(20000);
			webClient.getOptions().setTimeout(20000); // 15->60
			webClient.waitForBackgroundJavaScript(10000);
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

			webRequest.setAdditionalHeader("Referer",
					"http://yn.189.cn/service/jt/bill/qry_mainjt.jsp?SERV_NO=SHQD1&fastcode=01941229&cityCode=yn");
			webRequest.setAdditionalHeader("Host", "yn.189.cn");
			webRequest.setAdditionalHeader("Pragma", "no-cache");
			webRequest.setAdditionalHeader("Origin", "http://yn.189.cn");
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
			if (page.getWebResponse().getContentAsString().indexOf("0") == -1) {
				webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
				webParamTelecom.setWebClient(webClient);
				webParamTelecom.setErrormessage("短信发送失败,电信接口波动,请稍后重试");
				return webParamTelecom;
			}
			if (page.getWebResponse().getContentAsString().indexOf("您今天内获取取随机密码已超出限制") != -1) {
				webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
				webParamTelecom.setWebClient(webClient);
				webParamTelecom.setErrormessage("-196910 对不起，您今天内获取取随机密码已超出限制，您的号码已被锁定！请明天再试！ (错误日志序号：112023657)");
				return webParamTelecom;
			}
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			return webParamTelecom;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			webParamTelecom.setErrormessage(e.getMessage());
			return webParamTelecom;
		}

	}

}
