package test;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class YuezhangdanTest {

	public static void main(String[] args) {
		SimpleDateFormat dateFormat221 = new SimpleDateFormat("yyyyMM");
		Calendar calendar221 = Calendar.getInstance();
		calendar221.setTime(new Date());
		List<String> list221 = new ArrayList<String>();
		for (int j = 0; j <= 4; j++) {
			list221.add(dateFormat221.format(calendar221.getTime()));
			calendar221.set(Calendar.MONTH, calendar221.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list221.size(); i++) {
			String monthdate = list221.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
			System.out.println(i+1);
//			telecomCommonService.crawlerPaymsg(messageLogin, year, i);
		}
	}

}
