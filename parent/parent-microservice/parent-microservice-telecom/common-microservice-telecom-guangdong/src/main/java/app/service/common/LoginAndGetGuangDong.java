package app.service.common;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import net.sf.json.JSONObject;
@Component
public class LoginAndGetGuangDong {

	public static WebParamTelecom getphonecode(MessageLogin messageLogin, TaskMobile taskMobile){

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = TelecomCommonUnit.addcookie(webClient, taskMobile);
		try {
			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

			String url = "http://gd.189.cn/volidate/validateSendMsg.action";
			taskMobile.setTrianNum(0);
			//number=18126726741&latnId=020&typeCode=LIST_QRY
			String value = null;
			Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("LATN_CODE_COOKIE")){
					value = cookie.getValue();
				}
			}

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("number", messageLogin.getName()));
			paramsList.add(new NameValuePair("latnId", value));
			paramsList.add(new NameValuePair("typeCode", "LIST_QRY"));
			Page page = TelecomCommonUnit.gethtmlPost(webClient, paramsList, url);


			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
			String url2 = "http://gd.189.cn/volidate/insertSendSmg.action";
			List<NameValuePair> paramsList2 = new ArrayList<NameValuePair>();
			paramsList2 = new ArrayList<NameValuePair>();
			paramsList2.add(new NameValuePair("validaterResult", "0"));
			paramsList2.add(new NameValuePair("resultmsg", "允许发送短信"));
			paramsList2.add(new NameValuePair("YXBS", "LIST_QRY"));
			paramsList2.add(new NameValuePair("number", messageLogin.getName()));
			paramsList2.add(new NameValuePair("latnId", value));
			Page page2 = TelecomCommonUnit.gethtmlPost(webClient, paramsList2, url2);


			WebParamTelecom  webParamTelecom = new WebParamTelecom();
			webParamTelecom.setHtml(page2.getWebResponse().getContentAsString());
			webParamTelecom.setWebClient(webClient);
			System.out.println("111");
			System.out.println(page.getWebResponse().getContentAsString());
			return webParamTelecom;
		} catch (Exception e) {

			taskMobile.setTrianNum(taskMobile.getTrianNum() + 1);
			if (taskMobile.getTrianNum() > 2) {
				return null;
			}
			return getphonecode(messageLogin, taskMobile);
		}

	}


	public static WebParamTelecom setphonecode(MessageLogin messageLogin, TaskMobile taskMobile){
		try {
			String str = messageLogin.getSms_code();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = TelecomCommonUnit.addcookie(webClient, taskMobile);
			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
			//日期
			LocalDate today = LocalDate.now();
			String today1 = today.toString();//2017-10-12
			String[] split = today1.split("-");
			String to="";
			for (String string : split) {
				to+=string;
			}
			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1);
			LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth());

			String stardate1 = stardate.toString();
			String[] split2 = stardate1.split("-");
			String kai = "";
			for (String string : split2) {
				kai+=string;//20171001
			}

			String enddate1 = enddate.toString();
			String[] split3 = enddate1.split("-");
			String end="";
			for (String string : split3) {
				end+=string;//20171031
			}

			String monthint = stardate.getMonthValue() + "";
			if(monthint.length()<2){
				monthint = "0" + monthint;
			}
			String month1 = stardate.getYear() + monthint;//201710

			String url2 = "http://gd.189.cn/J/J10008.j";
			List<NameValuePair> paramsList = new ArrayList<>();
			paramsList.add(new NameValuePair("a.c", "0"));
			paramsList.add(new NameValuePair("a.u", "user"));
			paramsList.add(new NameValuePair("a.p", "pass"));
			paramsList.add(new NameValuePair("a.s", "ECSS"));
			paramsList.add(new NameValuePair("d.d01", "call"));
			paramsList.add(new NameValuePair("d.d02", month1));
			paramsList.add(new NameValuePair("d.d03", to));
			paramsList.add(new NameValuePair("d.d04", to));
			paramsList.add(new NameValuePair("d.d05", "20"));
			paramsList.add(new NameValuePair("d.d06", "1"));
			paramsList.add(new NameValuePair("&d.d08", "1"));
			Page page2 = TelecomCommonUnit.gethtmlPost(webClient, paramsList, url2);
			String html2 = page2.getWebResponse().getContentAsString();
			System.out.println(html2);

			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webClient.addRequestHeader("Host", "gd.189.cn");
			webClient.addRequestHeader("Origin", "http://gd.189.cn");
			webClient.addRequestHeader("Referer", "http://gd.189.cn/service/home/query/xd_index.html");
			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");

			String url = "http://gd.189.cn/J/J10009.j";
			List<NameValuePair> paramsList2 = new ArrayList<>();
			paramsList2.add(new NameValuePair("a.c", "0"));
			paramsList2.add(new NameValuePair("a.u", "user"));
			paramsList2.add(new NameValuePair("a.p", "pass"));
			paramsList2.add(new NameValuePair("a.s", "ECSS"));
			paramsList2.add(new NameValuePair("c.n", "语音清单"));
			paramsList2.add(new NameValuePair("c.t", "02"));
			paramsList2.add(new NameValuePair("c.i", "02-005-04"));
			paramsList2.add(new NameValuePair("d.d01", "call"));
			paramsList2.add(new NameValuePair("d.d02", month1));
			paramsList2.add(new NameValuePair("d.d03", to));
			paramsList2.add(new NameValuePair("d.d04", to));
			paramsList2.add(new NameValuePair("d.d05", "20"));
			paramsList2.add(new NameValuePair("d.d06", "1"));
			paramsList2.add(new NameValuePair("d.d07", str));
			paramsList2.add(new NameValuePair("d.d08", "1"));


			Page page = TelecomCommonUnit.gethtmlPost(webClient, paramsList2, url);
			WebParamTelecom  webParamTelecom = new WebParamTelecom();
			String string = page.getWebResponse().getContentAsString();
			System.out.println(string);
			String string2 = JSONObject.fromObject(string).getString("r");
			if(html2.indexOf("未选择当前查询号码")!=-1&&string.indexOf("非法操作，错误调用顺序")!=-1){
				webParamTelecom.setHtml("用户未进行实名登记!");
				return webParamTelecom;
			}else if(string2.equals(null)){
				webParamTelecom.setHtml("验证码不正确");
				return webParamTelecom;
			}
				webParamTelecom.setHtml(page.getWebResponse().getContentAsString());
				webParamTelecom.setWebClient(webClient);
			
				return webParamTelecom;
			
		} catch (Exception e) {
			taskMobile.setTrianNum(taskMobile.getTrianNum() + 1);
			if (taskMobile.getTrianNum() > 2) {
				return null;
			}
			return setphonecode(messageLogin, taskMobile);
		}

	}
}
