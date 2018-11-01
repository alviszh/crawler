package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.guilin.HousingGuiLinPay;

import app.commontracerlog.TracerLog;


@Component
public class HousingFundGuiLinParser {
	@Autowired
	private TracerLog tracer;

	public List<HousingGuiLinPay> getpay(String task_id, String html) {
		List<HousingGuiLinPay> list = null;
		try {
			list = new ArrayList<HousingGuiLinPay>();
			Document doc = Jsoup.parse(html);
			String text = doc.getElementById("ctl00_ContentPlaceHolder1_Panel3").text();
			String[] split = text.split(" ");
			String[] split2 = split[0].split("：");
			String name = split2[1].trim();//姓名
			System.out.println(name);
			String[] split3 = split[1].split("：");
			String idcard = split3[1].trim();//身份证号码
			System.out.println(idcard);
			Elements elementsByTag = doc.getElementById("ctl00_ContentPlaceHolder1_GridView1").getElementsByTag("tr");
			for (int i = 1; i < elementsByTag.size(); i++) {
				
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(0).text();//记账日期
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(1).text();//摘要
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(2).text();//减少金额
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(3).text();//增加金额
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(4).text();//累计金额
				System.out.println("身份证："+idcard+"\r姓名："+name+"\r记账日期:"+text2+"\r摘要:"+text3+"\r减少金额："+text4+"\r增加金额:"+text5+"\r累计金额:"+text6+"\r");
				HousingGuiLinPay pay = new HousingGuiLinPay(
						task_id,text2,text3,text4,text5,text6,name,idcard);
				list.add(pay);
			}

		} catch (Exception e) {
			tracer.addTag("解析数据","解析数据错误："+ e.toString());
			return list;
		}
		return list;
	}


}
