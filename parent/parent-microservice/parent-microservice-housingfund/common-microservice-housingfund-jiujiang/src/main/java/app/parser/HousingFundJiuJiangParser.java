package app.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.jiujiang.HousingJiuJiangUserInfo;

import app.commontracerlog.TracerLog;
@Component
public class HousingFundJiuJiangParser {
	@Autowired
	private TracerLog tracer;
	public HousingJiuJiangUserInfo crawler(String html) {
		HousingJiuJiangUserInfo userinfo = null;
		try {
			Document document = Jsoup.parse(html);
			String string = document.getElementsByTag("td").get(1).text().trim();//姓      名
			String string2 = document.getElementsByTag("td").get(3).text().trim();//身份证号
			String string3 = document.getElementsByTag("td").get(5).text().trim();//单位名称
			String string4 = document.getElementsByTag("td").get(7).text().trim();//公积金账号
			String string5 = document.getElementsByTag("td").get(9).text().trim();//缴交标准（元/月）
			String string6 = document.getElementsByTag("td").get(11).text().trim();//缴存月份
			String string7 = document.getElementsByTag("td").get(13).text().trim();//账户金额（元）
			System.out.println(string+string2+string3+string4+string5+string6+string7);
			userinfo = new HousingJiuJiangUserInfo(null,string,string2,string3,string4,string5,string6,string7);
		} catch (Exception e) {
			tracer.addTag("解析数据","解析数据错误："+ e.toString());
			return userinfo;
		}
		return userinfo;
	}

}
