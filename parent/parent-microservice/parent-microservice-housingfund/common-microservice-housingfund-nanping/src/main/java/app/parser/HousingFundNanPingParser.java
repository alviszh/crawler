package app.parser;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.nanping.HousingNanPingUserInfo;

import app.commontracerlog.TracerLog;


@Component
public class HousingFundNanPingParser {
	@Autowired
	private TracerLog tracer;

	public HousingNanPingUserInfo crawler(String html2) {
		HousingNanPingUserInfo user = null;
		try {
			Elements elementsByTag = Jsoup.parse(html2).getElementById("ctl00_cphMain_dgMain").getElementsByTag("td");
			String string = elementsByTag.get(1).text().trim();//帐号
			String string2 = elementsByTag.get(3).text().trim();//身份证
			String string3 = elementsByTag.get(5).text().trim();//姓名
			String string4 = elementsByTag.get(7).text().trim();//工作单位
			String string5 = elementsByTag.get(9).text().trim();//余额
			String string6 = elementsByTag.get(11).text().trim();//开户银行
			String string7 = elementsByTag.get(13).text().trim();//截至日期
			System.out.println(string+string2+string3+string4+string5+string6+string7);
			user = new HousingNanPingUserInfo(null,string,string2,string3,string4,string5,string6,string7);
		} catch (Exception e) {
			tracer.addTag("解析数据","解析数据错误："+ e.toString());
			return user;
		}
		return user;
	}
}
