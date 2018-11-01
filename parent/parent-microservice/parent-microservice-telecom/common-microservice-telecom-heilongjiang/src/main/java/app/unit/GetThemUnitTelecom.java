package app.unit;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.crawler.telecom.htmlunit.LoginAndGet;

public class GetThemUnitTelecom {
	public  static final Logger log = LoggerFactory.getLogger(GetThemUnitTelecom.class);
	// 爬取结果集并存入数据库
	public static String getPayMsgStatusThem(MessageLogin messageLogin, TaskMobile taskMobile) {
		log.info("====================爬取缴费详单===========================");

		LocalDate nowdate = LocalDate.now();

		String endmonth = nowdate.getMonthValue() + "";
		String startmonth = nowdate.plusMonths(-5).getMonthValue() + "";
		if (startmonth.length() < 2) {
			startmonth = "0" + startmonth;
		}
		if (endmonth.length() < 2) {
			endmonth = "0" + endmonth;
		}

		return LoginAndGet.getPayMsgStatusThem(messageLogin, taskMobile, nowdate.plusMonths(-5) + "" + startmonth,
				nowdate.getYear() + "" + endmonth);

	}

	// 爬取结果集并存入数据库
	public static String getCustomThem(MessageLogin messageLogin, TaskMobile taskMobile, int i) {

		LocalDate nowdate = LocalDate.now();

		String nowdatestring = nowdate.plusMonths(-i).getMonthValue() + "";
		if (nowdatestring.length() < 2) {
			nowdatestring = "0" + nowdatestring;
		}

		return LoginAndGet.getCustomThem(messageLogin, taskMobile, nowdate.plusMonths(-i).getYear() + nowdatestring);

	}

	// 爬取通话并存入数据库
	public static String getCallThemHtml(MessageLogin messageLogin, TaskMobile taskMobile, String phonenum, String date,
			int seledType,int selectype, int page) {
		try {
			String html = LoginAndGet.getCallThemHtml(messageLogin, taskMobile, messageLogin.getName(),date,seledType,selectype, page);
			return html;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

}
