package app.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import app.commontracerlog.TracerLog;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.cangzhou.HousingCangZhouUserInfo;

@Component
public class HousingFundCangZhouParser {
	@Autowired
	private TracerLog tracer;

	public HousingCangZhouUserInfo crawler(String html2) {
		HousingCangZhouUserInfo housingCangZhouUserInfo = null;
		try {
		Document doc = Jsoup.parse(html2);
		housingCangZhouUserInfo = new HousingCangZhouUserInfo();
		Element element = doc.getElementsByClass("box").get(0);
		String text = element.getElementsByClass("ul_search").get(0).getElementsByTag("li").get(0).text();//姓名
		housingCangZhouUserInfo.setUsername(text.substring(5, text.length()).trim());
		String text2 = element.getElementsByClass("ul_search").get(0).getElementsByTag("li").get(1).text();//身份证
		housingCangZhouUserInfo.setIdcard(text2.substring(5, text2.length()).trim());
		String text3 = element.getElementsByClass("ul_search").get(1).getElementsByTag("li").get(0).text();//每月交缴额
		housingCangZhouUserInfo.setPaymoney(text3.substring(6, text3.length()).trim());
		String text4 = element.getElementsByClass("ul_search").get(1).getElementsByTag("li").get(1).text();//余额
		housingCangZhouUserInfo.setBalance(text4.substring(3,text4.length()).trim());
		String text5 = element.getElementsByClass("ul_search").get(1).getElementsByTag("li").get(2).text();//日期
		housingCangZhouUserInfo.setXdate(text5.substring(3,text5.length()).trim());
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("解析数据","解析数据错误："+ e.toString());
			return housingCangZhouUserInfo;
		}
		return housingCangZhouUserInfo;
	}
}
