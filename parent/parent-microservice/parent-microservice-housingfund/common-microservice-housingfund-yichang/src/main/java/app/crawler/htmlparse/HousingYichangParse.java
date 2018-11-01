package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.yichang.HousingYichangPay;
import com.microservice.dao.entity.crawler.housing.yichang.HousingYichangUserInfo;

import app.commontracerlog.TracerLog;

@Component
public class HousingYichangParse {

	@Autowired
	private TracerLog tracer;

	// 公积金明细
	public List<HousingYichangPay> getHousingPay(String taskId, String html) {
		try {
			html = html.toLowerCase();
			List<HousingYichangPay> list = new ArrayList<HousingYichangPay>();
			Document doc = Jsoup.parse(html);
			Elements ds = doc.getElementsByTag("ds");
			for (Element element : ds) {
				String date = element.getElementsByTag("TRANSDATE").text();
				String money = element.getElementsByTag("AMT").text();
				String business_type = element.getElementsByTag("SUMMARYDESC").text();

				HousingYichangPay housingYichangPay = new HousingYichangPay(taskId, date, money, business_type);
				list.add(housingYichangPay);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("HousingYichangParse.getHousingPay.error", e.getMessage());
		}
		return null;
	}

	// 公积金用户信息 201
	public HousingYichangUserInfo getHousingUserInfo(String taskId, String html) {
		try {
			html = html.toLowerCase();
			Document doc = Jsoup.parse(html);
			String gr_num = doc.getElementsByTag("USERID").text();
			String name = doc.getElementsByTag("USERNAME").text();
			String company = doc.getElementsByTag("DWMC").text();
			String total = doc.getElementsByTag("PRICEACCOUNT").text();
			String monthly_payment = doc.getElementsByTag("PRICECOST").text();
			String year_month = doc.getElementsByTag("LASTPAYDATE").text();
			String dw_monthly_pay = doc.getElementsByTag("DWYJCE").text();
			String gr_Monthly_Pay = doc.getElementsByTag("GRYJCE").text();

			HousingYichangUserInfo housingYichangUserInfo = new HousingYichangUserInfo(taskId, gr_num, name, company,
					total, monthly_payment, year_month, dw_monthly_pay, gr_Monthly_Pay);
			return housingYichangUserInfo;
		} catch (Exception e) {
			tracer.addTag("HousingYichangParse.getHousingUserInfo.error", e.getMessage());
		}
		return null;
	}

}
