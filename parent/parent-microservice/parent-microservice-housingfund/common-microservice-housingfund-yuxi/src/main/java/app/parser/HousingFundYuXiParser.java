package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.yuxi.HousingYuXiPay;
import com.microservice.dao.entity.crawler.housing.yuxi.HousingYuXiUserinfo;

import app.commontracerlog.TracerLog;
@Component
public class HousingFundYuXiParser {
	@Autowired
	private TracerLog tracer;
	
	public HousingYuXiUserinfo getuserinfo(String html, String taskid) {
		HousingYuXiUserinfo user = null;
		try {
		Document doc = Jsoup.parse(html);
		Element elementsByTag = doc.getElementsByTag("table").get(6);
		String text = elementsByTag.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text();
		System.out.println(text);
		String[] split = text.split(" ");
		
		String[] split1 = split[0].split(":");
		String string = split1[1];//姓名
		
		String[] split2 = split[1].split(":");
		String string2 = split2[1];//单位
		
		String[] split3= split[2].split(":");
		String string3 = split3[1];//月缴交额
		
		String[] split4= split[3].split(":");
		String string4 = split4[1];//帐户余额
		
		String[] split5= split[4].split(":");
		String string5 = split5[1];//账号
		
		System.out.println("姓名:"+string+"\r单位:"+string2
				+"\r月缴:"+string3+"\r账户余额:"+string4+"\r帐号："+string5);
		user = new HousingYuXiUserinfo(taskid,string,string2,string3,string4,string5);
		} catch (Exception e) {
			tracer.addTag("解析数据","解析数据错误："+ e.toString());
			return user;
		}
		return user;
	}

	public List<HousingYuXiPay> getpay(String html, String task_id) {
		List<HousingYuXiPay> list = null;
		try {
			Document doc = Jsoup.parse(html);
			Element elementsByTag = doc.getElementsByTag("table").get(6);
			String text = elementsByTag.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text();
			System.out.println(text);
			String[] split = text.split(" ");
			String[] split5= split[4].split(":");
			String string5 = split5[1];//账号
			list = new ArrayList<HousingYuXiPay>();
			Elements tag = doc.getElementsByClass("9size").get(0).getElementsByTag("tr");
			for (int i = 2; i < tag.size(); i++) {
				String tex = tag.get(i).getElementsByTag("td").get(0).text();//日期
				String text1 = tag.get(i).getElementsByTag("td").get(1).text();//摘要
				String text2 = tag.get(i).getElementsByTag("td").get(2).text();//收入
				String text3 = tag.get(i).getElementsByTag("td").get(3).text();//支出
				String text4 = tag.get(i).getElementsByTag("td").get(4).text();//余额
				System.out.println("日期："+tex+"\r摘要:"+text1
						+"\r收入："+text2+"\r支出："+text3+"\r余额:"+text4+"\r");
				HousingYuXiPay pay = new HousingYuXiPay(task_id,tex,text1,text2,text3,text4,string5);
				list.add(pay);
			}
		} catch (Exception e) {
			tracer.addTag("解析数据","解析数据错误："+ e.toString());
			return list;
		}
		return list;
	}

}
