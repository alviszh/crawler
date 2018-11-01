package app.service.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln
 * @date: 2017年11月06日
 */
@Component
public class HxbChinaHelperService {
	public static final Logger log = LoggerFactory.getLogger(HxbChinaHelperService.class);

	public WebClient addcookie(TaskBank taskBank) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskBank.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}

	public static String getPresentDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateNowStr = sdf.format(d);
		return dateNowStr;
	}

	// 从当前月开始往前推i个月
	public static String getBeforeMonth(int i) {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -i);
		String beforeMonth = f.format(c.getTime());
		return beforeMonth;
	}

	public static void main(String[] args) {
		Calendar cale = Calendar.getInstance();
		// 获取当月第一天和最后一天
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String firstday, lastday;
		// 获取前月的第一天
		cale = Calendar.getInstance();
		cale.add(Calendar.MONTH, 0);
		cale.set(Calendar.DAY_OF_MONTH, 1);
		firstday = format.format(cale.getTime());
		// 获取前月的最后一天
		cale = Calendar.getInstance();
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH, 0);
		lastday = format.format(cale.getTime());
		System.out.println("本月第一天和最后一天分别是 ： " + firstday + " and " + lastday);

		String thisyear = lastday.substring(0, 4);
		String thismonth = lastday.substring(4, 6);
		String thismonthfirstday = firstday.substring(6, 8);
		String thismonthtoday = lastday.substring(6, 8);

		System.out.println(thisyear);
		System.out.println(thismonth);
		System.out.println(thismonthfirstday);
		System.out.println(thismonthtoday);
		for (int i = 0; i < 24; i++) {
			String beforeMonth = getBeforeMonth(i);
			System.out.println(beforeMonth);
		}
	}

	// 杀死进程
	public static void killProcess() {
		Runtime runTime = Runtime.getRuntime();
		try {
			runTime.exec("TASKKILL /F /IM iexplore.exe");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
