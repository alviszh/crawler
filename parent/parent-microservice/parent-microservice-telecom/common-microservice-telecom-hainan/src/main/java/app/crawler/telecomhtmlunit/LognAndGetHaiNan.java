package app.crawler.telecomhtmlunit;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.TelecomHaiNanUserIdBean;
import app.bean.WebParamTelecom;
import app.service.common.LoginAndGetCommon;
import app.unit.TeleComCommonUnit;

@Component
@Service
public class LognAndGetHaiNan {

	public static final Logger log = LoggerFactory.getLogger(LognAndGetHaiNan.class);

	// 获取账单信息
	public static String getBill(MessageLogin messageLogin, TaskMobile taskMobile, String stardate) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		String url = "http://www.hi.189.cn/webgo/thesame/billing?objectNum=" + messageLogin.getName().trim()
				+ "&queryMonth=" + stardate.trim();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		return page.getWebResponse().getContentAsString();
	}

	// 获取用户缴费信息
	public static String getpayResult(MessageLogin messageLogin, TaskMobile taskMobile, String stardate)
			throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

		String url = "http://www.hi.189.cn/webgo/thesame/rechargeRecord?" + "objectNum=" + messageLogin.getName().trim()
				+ "&queryMonth=" + stardate.trim();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return page.getWebResponse().getContentAsString();
	}

	// 获取积分信息
	public static String getintegraChangeResult(MessageLogin messageLogin, TaskMobile taskMobile, String stardate)
			throws Exception, IOException {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		String url = "http://www.hi.189.cn/BUFFALO/buffalo/IntegralQueryAjax";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");
		webRequest.setRequestBody("<buffalo-call>" + "<method>integralHistoryQuery</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>MONTH</string>" + "<string>" + stardate.trim()
				+ "</string>" + "</map>" + "</buffalo-call>");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		return page.getWebResponse().getContentAsString();

	}

	// 获取通话详单
	public static String getPhoneBill(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, MessageLogin messageLogin,
			TaskMobile taskMobile, String stardate) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

		String url = "http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Referer",
				"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		webRequest.setRequestBody("<buffalo-call>" + "<method>queryDetailBill</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>PRODNUM</string>" + "<string>"
				+ telecomHaiNanUserIdBean.getCanshu().trim() + "</string>" + "<string>CITYCODE</string>"
				+ "<string>0898</string>" + "<string>QRYDATE</string>" + "<string>" + stardate.trim() + "</string>"
				+ "<string>TYPE</string>" + "<string>8</string>" + "<string>PRODUCTID</string>" + "<string>50</string>"
				+ "<string>CODE</string>" + "<string>" + messageLogin.getSms_code().trim() + "</string>"
				+ "<string>USERID</string>" + "<string>" + telecomHaiNanUserIdBean.getUserid().trim() + "</string>"
				+ "</map>" + "</buffalo-call>");
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return page.getWebResponse().getContentAsString();
	}

	// 获取短信详单
	public static String getSMSBill(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, MessageLogin messageLogin,
			TaskMobile taskMobile, String stardate) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);
		String url = "http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Referer",
				"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		webRequest.setRequestBody("<buffalo-call>" + "<method>queryDetailBill</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>PRODNUM</string>" + "<string>"
				+ telecomHaiNanUserIdBean.getCanshu().trim() + "</string>" + "<string>CITYCODE</string>"
				+ "<string>0898</string>" + "<string>QRYDATE</string>" + "<string>" + stardate.trim() + "</string>"
				+ "<string>TYPE</string>" + "<string>6</string>" + "<string>PRODUCTID</string>" + "<string>50</string>"
				+ "<string>CODE</string>" + "<string>" + messageLogin.getSms_code().trim() + "</string>"
				+ "<string>USERID</string>" + "<string>" + telecomHaiNanUserIdBean.getUserid().trim() + "</string>"
				+ "</map>" + "</buffalo-call>");
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

		String url = "http://www.hi.189.cn/service/account/service_kh_xx.jsp?fastcode=0212&cityCode=hi";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		return page.getWebResponse().getContentAsString();
	}

	/**
	 * 爬取余额
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getBalance(MessageLogin messageLogin, TaskMobile taskMobile, String stardate)
			throws Exception {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String url = "http://www.hi.189.cn/webgo/thesame/balanceChanges?objectNum=" + messageLogin.getName().trim()
				+ "&queryMonth=" + stardate.trim();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		return page.getWebResponse().getContentAsString();
	}

	/**
	 * 爬取套餐
	 * 
	 * @param telecomHaiNanUserIdBean
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public static String getBussiness(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean, MessageLogin messageLogin,
			TaskMobile taskMobile) throws Exception {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String url = "http://www.hi.189.cn/ServiceOrderAjax.do?method=setEsuringIn&number="
				+ telecomHaiNanUserIdBean.getCanshu() + "&phonetype=" + telecomHaiNanUserIdBean.getWeizhiphonetype();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return page.getWebResponse().getContentAsString();
	}

	/**
	 * 获取参数
	 * 
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	public static String readyGetUserId(MessageLogin messageLogin, TaskMobile taskMobile)
			throws Exception {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
//		String url = "http://www.189.cn/login/index.do";
//
//		LoginAndGetCommon.getHtml(url, webClient);
//		url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02091577";
//		getHtml(url, webClient);
		String url = "http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi";
		Page page = getHtml(url, webClient);

//		url = "http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi";
//		// webClient.getOptions().setJavaScriptEnabled(false);
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);
		return page.getWebResponse().getContentAsString();

	}

	public static WebParamTelecom<?> getPhonecode(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		WebParamTelecom<?> webParamTelecom = new WebParamTelecom<>();

		try {
			webClient.getOptions().setJavaScriptEnabled(false);

			String url = "http://www.189.cn/login/index.do";

			LoginAndGetCommon.getHtml(url, webClient);
			url = "http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=02091577";
			getHtml(url, webClient);
			url = "http://hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi";
			Page html4 = getHtml(url, webClient);

			System.out.println("=========================================");
			System.out.println(html4.getWebResponse().getContentAsString());
			System.out.println("=========================================");

			url = "http://www.hi.189.cn/BUFFALO/buffalo/CommonAjaxService";

			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
			webRequest.setAdditionalHeader("Referer",
					"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
			webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
			webRequest.setAdditionalHeader("Pragma", "no-cache");
			webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
			webRequest.setAdditionalHeader("Accept", "*/*");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Cache-Control", "no-cache");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			// webRequest.setAdditionalHeader("Content-Length", "160");
			webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

			webRequest.setRequestBody("<buffalo-call>" + "<method>getSmsCode</method>" + "<map>"
					+ "<type>java.util.HashMap</type>" + "<string>PHONENUM</string>" + "<string>"
					+ messageLogin.getName().trim() + "</string>" + "<string>PRODUCTID</string>" + "<string>50</string>"
					+ "<string>RTYPE</string>" + "<string>QD</string>" + "</map>" + "</buffalo-call>");
			Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

			if (page.getWebResponse().getContentAsString().indexOf("尊敬的客户,您好!短信随机密码已经发到您的联系电话,请查收") == -1) {
				webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
				webParamTelecom.setWebClient(webClient);
				webParamTelecom.setErrormessage("短信发送失败");
				return webParamTelecom;
			}
			if (page.getWebResponse().getContentAsString().indexOf("过多") != -1
					|| page.getWebResponse().getContentAsString().indexOf("频繁") != -1) {
				webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
				webParamTelecom.setWebClient(webClient);
				webParamTelecom.setErrormessage("短信发送失败,发送验证码次数过于频繁");
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

	// 获取通话详单
	public static WebParamTelecom<?> setphonecode(TelecomHaiNanUserIdBean telecomHaiNanUserIdBean,
			MessageLogin messageLogin, TaskMobile taskMobile, String stardate) throws Exception {
		WebParamTelecom<?> webParamTelecom = new WebParamTelecom<>();
		// System.out.println(telecomHaiNanUserIdBean.toString());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TeleComCommonUnit.addcookie(webClient, taskMobile);

		String url = "http://www.hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service";
		// http://hi.189.cn/BUFFALO/buffalo/FeeQueryAjaxV4Service

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Referer", "http://www.hi.189.cn/service/jf/integralHistory.jsp");
		webRequest.setAdditionalHeader("Referer",
				"http://www.hi.189.cn/service/bill/feequery.jsp?TABNAME=xdcx&fastcode=02091577&cityCode=hi");
		webRequest.setAdditionalHeader("Host", "www.hi.189.cn");
		webRequest.setAdditionalHeader("Pragma", "no-cache");
		webRequest.setAdditionalHeader("Origin", "http://www.hi.189.cn");
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Cache-Control", "no-cache");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		// webRequest.setAdditionalHeader("Content-Length", "160");
		webRequest.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");

		String canshuString = "<buffalo-call>" + "<method>queryDetailBill</method>" + "<map>"
				+ "<type>java.util.HashMap</type>" + "<string>PRODNUM</string>" + "<string>"
				+ telecomHaiNanUserIdBean.getCanshu().trim() + "</string>" + "<string>CITYCODE</string>"
				+ "<string>0898</string>" + "<string>QRYDATE</string>" + "<string>" + stardate.trim() + "</string>"
				+ "<string>TYPE</string>" + "<string>8</string>" + "<string>PRODUCTID</string>" + "<string>50</string>"
				+ "<string>CODE</string>" + "<string>" + messageLogin.getSms_code().trim() + "</string>"
				+ "<string>USERID</string>" + "<string>" + telecomHaiNanUserIdBean.getUserid().trim() + "</string>"
				+ "</map>" + "</buffalo-call>";
		webRequest.setRequestBody(canshuString);
		Page page = TeleComCommonUnit.gethtmlWebRequest(webClient, webRequest);

		System.out.println(page.getWebResponse().getContentAsString());
		if (page.getWebResponse().getContentAsString().indexOf("对不起，短信验证码不正确，请重新输入") != -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setErrormessage("对不起，短信验证码不正确，请重新输入");
			return webParamTelecom;

		}
		if (page.getWebResponse().getContentAsString().indexOf("对不起，你的短信验证码已失效，请重新获取") != -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setErrormessage("对不起，你的短信验证码已失效，请重新获取");
			return webParamTelecom;

		}
		if (page.getWebResponse().getContentAsString().indexOf("null") != -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			return webParamTelecom;
		}
		if (page.getWebResponse().getContentAsString().indexOf("呼叫类型") == -1
				&& page.getWebResponse().getContentAsString().indexOf("null") == -1) {
			webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
			webParamTelecom.setErrormessage("对不起，你的短信验证码验证失败");
			return webParamTelecom;
		}
		webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
		return webParamTelecom;
	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept", "*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		webRequest.setAdditionalHeader("Host", "www.189.cn");
		webRequest.setAdditionalHeader("Referer", "http://www.189.cn/html/login/right.html");
		webRequest.setAdditionalHeader("Origin", "http://www.czgjj.com");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webClient.setJavaScriptTimeout(50000);
		webClient.getOptions().setTimeout(50000); // 15->60
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

}
