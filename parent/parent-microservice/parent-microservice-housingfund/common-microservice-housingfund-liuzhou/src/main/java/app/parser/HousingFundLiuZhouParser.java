package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.liuzhou.HousingFundLiuZhouPay;
import com.microservice.dao.entity.crawler.housing.liuzhou.HousingFundLiuZhouUserinfo;

import app.commontracerlog.TracerLog;

@Component
public class HousingFundLiuZhouParser {
	@Autowired
	private TracerLog tracer;

	public HousingFundLiuZhouUserinfo getuserinfo(String html, String taskid) {
		HousingFundLiuZhouUserinfo user = null;
		try {
			Document document = Jsoup.parse(html);
			Elements tag = document.getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = tag.get(0).getElementsByTag("td").get(0).text();//姓名
			String text2 = tag.get(0).getElementsByTag("td").get(1).text();//个人编号
			String text3 = tag.get(0).getElementsByTag("td").get(2).text();//缴存状态
			String text4 = tag.get(1).getElementsByTag("td").get(0).text();//单位账号
			String text5 = tag.get(1).getElementsByTag("td").get(1).text();//身份证
			String text6 = tag.get(2).getElementsByTag("td").get(0).text();//单位名称
			String text7 = tag.get(3).getElementsByTag("td").get(0).text();//所属网点名称
			String text8 = tag.get(4).getElementsByTag("td").get(0).text();//月缴存基数
			String text9 = tag.get(4).getElementsByTag("td").get(1).text();//单位缴存比例(%)
			String text10 = tag.get(4).getElementsByTag("td").get(2).text();//个人缴存比例(%)
			String text11 = tag.get(5).getElementsByTag("td").get(0).text();//单位月缴存额
			String text12 = tag.get(5).getElementsByTag("td").get(1).text();//个人月缴存额
			String text13 = tag.get(5).getElementsByTag("td").get(2).text();//合计月缴存额
			String text14 = tag.get(6).getElementsByTag("td").get(0).text();//累计提取额
			String text15 = tag.get(6).getElementsByTag("td").get(1).text();//账户余额
			String text16 = tag.get(6).getElementsByTag("td").get(2).text();//当前缴至年月
			user = new HousingFundLiuZhouUserinfo(taskid,text,text2,text3,text4,
					text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			tracer.addTag("housingfundliuzhou.getcrawler.getuser", "个人信息解析错误"+e.getMessage());
			return user;
		}
		return user;
	}

	public List<HousingFundLiuZhouPay> getpay(String html2, String taskid) {
		List<HousingFundLiuZhouPay> list = null;
		try {
			list = new ArrayList<HousingFundLiuZhouPay>();
			Document doc = Jsoup.parse(html2);
			Elements byTag = doc.getElementsByTag("table").get(1).getElementsByTag("tr");
		    Element element = doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(0);
			for (int i = 1; i < byTag.size()-1; i++) {
				String text = byTag.get(i).getElementsByTag("td").get(0).text();//业务日期
				String text2 = byTag.get(i).getElementsByTag("td").get(1).text();//流水号
				String text3 = byTag.get(i).getElementsByTag("td").get(2).text();//业务类型
				String text4 = byTag.get(i).getElementsByTag("td").get(3).text();//缴存年月
				String text5 = byTag.get(i).getElementsByTag("td").get(4).text();//增加额
				String text6 = byTag.get(i).getElementsByTag("td").get(5).text();//减少额
				String text7 = byTag.get(i).getElementsByTag("td").get(6).text();//余额
				String text8 = byTag.get(i).getElementsByTag("td").get(7).text();//缴存单位账号
				String text9 = element.getElementsByTag("td").get(0).text();//姓名
				String text10 = element.getElementsByTag("td").get(1).text();//身份证
				HousingFundLiuZhouPay pay = new HousingFundLiuZhouPay(
						taskid,text,text2,text3,text4,text5,text6,text7,text8,text9,text10);
				list.add(pay);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			tracer.addTag("housingfundliuzhou.getcrawler.getpay", "流水信息解析错误"+e.getMessage());
			return list;
		}
		return list;
	}
}
