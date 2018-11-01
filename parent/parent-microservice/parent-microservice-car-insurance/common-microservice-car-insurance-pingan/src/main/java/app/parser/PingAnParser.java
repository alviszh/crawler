package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.entity.crawler.car.insurance.pingan.PingAnBasicInformation;
import com.microservice.dao.entity.crawler.car.insurance.pingan.PingAnPayInfo;
import com.microservice.dao.entity.crawler.car.insurance.pingan.PingAnUserInfo;

import app.commontracerlog.TracerLog;

@Component
public class PingAnParser {
	@Autowired
	private TracerLog tracer;

	public List<PingAnBasicInformation> getBasicInformation(String html, TaskCarInsurance taskCarInsurance) {
		List<PingAnBasicInformation> list = new ArrayList<>();
		if(html.indexOf("您的车架号为")!=-1){
			Document document = Jsoup.parse(html);
			String text = document.getElementsByClass("m_t8").first().text();
			Element element = document.getElementsByClass("ti_b").first();
			Elements elementsByTag = element.getElementsByTag("tr");

			for (int i = 1; i < elementsByTag.size(); i++) {
				PingAnBasicInformation pingAnBasicInformation = new PingAnBasicInformation();
				String text2 = elementsByTag.get(i).getElementsByTag("td").get(1).text();
				String text3 = elementsByTag.get(i).getElementsByTag("td").get(2).text();
				String text4 = elementsByTag.get(i).getElementsByTag("td").get(3).text();
				String text5 = elementsByTag.get(i).getElementsByTag("td").get(4).text();
				String text6 = elementsByTag.get(i).getElementsByTag("td").get(5).text();
				pingAnBasicInformation.setPolicyNumber(text2);
				pingAnBasicInformation.setInsuranceInception(text3);
				pingAnBasicInformation.setInsuranceEnddate(text4);
				pingAnBasicInformation.setVehicleType(text5);
				pingAnBasicInformation.setInsuranceName(text6);
				pingAnBasicInformation.setPolicyNumber(text);
				pingAnBasicInformation.setTaskid(taskCarInsurance.getTaskid());
				list.add(pingAnBasicInformation);
			}
		}else{
			tracer.addTag("首页面元素有变动", html);
			list = null;
		}
		return list;
	}

	public PingAnUserInfo getUserInfo(String html, TaskCarInsurance taskCarInsurance) {
		PingAnUserInfo pingAnUserInfo = null;
		if(html.indexOf("车险承保理赔信息查询")!=-1){
			Document document = Jsoup.parse(html);
			//第一张表
			Element element = document.getElementsByTag("table").first();
			String text = element.getElementsByTag("td").get(0).text();//被保险人
			String text1 = element.getElementsByTag("td").get(1).text();//车牌号
			String text2 = element.getElementsByTag("td").get(2).text();//投保人
			String text3 = element.getElementsByTag("td").get(3).text();//保险期限
			String text4 = element.getElementsByTag("td").get(4).text();//发动机号
			String text5 = element.getElementsByTag("td").get(5).text();//车架号
			String text6 = element.getElementsByTag("td").get(6).text();//核定载质量
			String text7 = element.getElementsByTag("td").get(7).text();//核定座位
			String text8 = element.getElementsByTag("td").get(8).text();//车辆类型
			String text9 = element.getElementsByTag("td").get(9).text();//使用性质
			String text10 = element.getElementsByTag("td").get(10).text();//标准保费
			String text11 = element.getElementsByTag("td").get(11).text();//保单状态
			String text12 = element.getElementsByTag("td").get(12).text();//业务来源
			String text13 = element.getElementsByTag("td").get(13).text();//业务来源细分
			String text14 = element.getElementsByTag("td").get(14).text();//代理人
			String text15 = element.getElementsByTag("td").get(15).text();//代理人代码
			String text16 = element.getElementsByTag("td").get(16).text();//支付方式
			String text17 = element.getElementsByTag("td").get(17).text();//支付对象
			String text18 = element.getElementsByTag("td").get(18).text();//应缴保费金额
			String text19 = element.getElementsByTag("td").get(19).text();//已缴保费金额
			String text20 = element.getElementsByTag("td").get(20).text();//欠缴保费金额
			String text21 = element.getElementsByTag("td").get(21).text();//缴费方式
			String text22 = element.getElementsByTag("td").get(22).text();//缴费日期
			//第二张表
			Element element2 = document.getElementsByTag("table").get(1);
			String text23 = element2.getElementsByTag("td").get(1).text();//浮动比率
			//第四张表
			Element element3 = document.getElementsByTag("table").get(3);
			String text24 = element3.getElementsByTag("p").get(2).text();//车船税打印码：
			
			pingAnUserInfo = new PingAnUserInfo(taskCarInsurance.getTaskid(),text,text1,text2,text3,text4
					,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17,text18,text19,text20,text21,text22,text23,text24);
		}else{
			tracer.addTag("首页面元素有变动", html);
			pingAnUserInfo = null;
		}
		return pingAnUserInfo;
	}

	public List<PingAnPayInfo> getPayInfo(String html,
			TaskCarInsurance taskCarInsurance) {
		List<PingAnPayInfo> list = new ArrayList<>();
		Document document = Jsoup.parse(html);
		Element element = document.getElementsByTag("table").get(2);
		Elements elementsByTag = element.getElementsByTag("tr");
		for (int i = 1; i < elementsByTag.size()-1; i++) {
			PingAnPayInfo pingAnPayInfo = new PingAnPayInfo();
			String text = elementsByTag.get(i).getElementsByTag("td").get(1).text();
			String text2 = elementsByTag.get(i).getElementsByTag("td").get(2).text();
			String text3 = elementsByTag.get(i).getElementsByTag("td").get(3).text();
			String text4 = elementsByTag.get(i).getElementsByTag("td").get(4).text();
			pingAnPayInfo.setTaskid(taskCarInsurance.getTaskid());
			pingAnPayInfo.setInsuranceType(text);
			pingAnPayInfo.setInsuranceMoney(text2);
			pingAnPayInfo.setPremiumMoney(text3);
			pingAnPayInfo.setCoefficient(text4);
			list.add(pingAnPayInfo);
		}
		PingAnPayInfo pingAnPayInfo2 = new PingAnPayInfo();
		int size = elementsByTag.size();
		String text5 = elementsByTag.get(elementsByTag.size()-1).getElementsByTag("td").get(0).text();
		String text6 = elementsByTag.get(elementsByTag.size()-1).getElementsByTag("td").get(1).text();
		String text7 = elementsByTag.get(elementsByTag.size()-1).getElementsByTag("td").get(2).text();
		String text8 = elementsByTag.get(elementsByTag.size()-1).getElementsByTag("td").get(3).text();
		pingAnPayInfo2.setTaskid(taskCarInsurance.getTaskid());
		pingAnPayInfo2.setInsuranceType(text5);
		pingAnPayInfo2.setInsuranceMoney(text6);
		pingAnPayInfo2.setPremiumMoney(text7);
		pingAnPayInfo2.setCoefficient(text8);
		list.add(pingAnPayInfo2);
		return list;
	}

}
