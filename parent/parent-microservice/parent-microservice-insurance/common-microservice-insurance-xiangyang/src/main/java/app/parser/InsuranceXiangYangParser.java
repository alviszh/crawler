package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangGongShang;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangShengYu;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangShiYe;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangUserInfo;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangYangLao;
import com.microservice.dao.entity.crawler.insurance.xiangyang.InsuranceXiangYangYiLiao;

import app.commontracerlog.TracerLog;

@Component
public class InsuranceXiangYangParser {

	@Autowired
	private TracerLog tracer;

	public InsuranceXiangYangUserInfo getUserinfo(String html, String taskid) {
		InsuranceXiangYangUserInfo user = null;
		try {
			Document doc = Jsoup.parse(html);
			String text = doc.select("td:contains(社保证号码)+td").first().text();//社保证号码
			String text2 = doc.select("td:contains(姓名)+td").first().text();//姓名
			String text3 = doc.select("td:contains(身份证号码)+td").first().text();//身份证号码
			String text4 = doc.select("td:contains(性别)+td").first().text();//性别
			String text5 = doc.select("td:contains(企业养老保险)+td").first().text();//企业养老保险
			String text6 = doc.select("td:contains(机关事业养老保险)+td").first().text();//机关事业养老保险
			String text7 = doc.select("td:contains(基本医疗保险)+td").first().text();//基本医疗保险
			String text8 = doc.select("td:contains(基本医疗保险(仅住院))+td").first().text();//基本医疗保险(仅住院)
			String text9 = doc.select("td:contains(工伤保险)+td").first().text();//工伤保险
			String text10 = doc.select("td:contains(生育保险)+td").first().text();//生育保险
			String text11 = doc.select("td:contains(失业保险)+td").first().text();//失业保险
			String text12 = doc.select("td:contains(公务员医疗补助保险)+td").first().text();//公务员医疗补助保险
			String text13 = doc.select("td:contains(大额救助保险)+td").first().text();//大额救助保险
			String text14 = doc.select("td:contains(离休干部医疗保险)+td").first().text();//离休干部医疗保险
			String text15 = doc.select("td:contains(低保对象医疗保险)+td").first().text();//低保对象医疗保险
			String text16 = doc.select("td:contains(农民工综合保险)+td").first().text();//农民工综合保险
			String text17 = doc.select("td:contains(单位名称)+td").first().text();//单位名称
			System.out.println(text17+text+text2+text3+text4+text5+text6+text7+text8+text9+text10+text11+text12+text13+text14+text15+text16);
			user = new InsuranceXiangYangUserInfo(taskid,text,text2,text3,text4,text5,text6,text7,text8,text9,text10,text11,text12,text13,text14,text15,text16,text17);
		} catch (Exception e) {
			tracer.addTag("action.xiangyang.getuserinfo", "抓取失败:"+e.getMessage());
			return user;
		}
		return user;
	}

	public List<InsuranceXiangYangYangLao> getyanglaopay(String html, String taskId) {
		List<InsuranceXiangYangYangLao> list = null;
		try {
			Document doc = Jsoup.parse(html);
			list = new ArrayList<InsuranceXiangYangYangLao>();
			Element element = doc.getElementById("zxbs_2008_ta6").getElementsByTag("table").get(2);
			Elements elementsByTag = element.getElementsByTag("tr");
			for (int i = 3; i < elementsByTag.size()-1; i++) {
				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
				System.out.println(string+string2+string3+string4+string5+string6);
				InsuranceXiangYangYangLao yanglao = new InsuranceXiangYangYangLao(taskId,string,string2,string3,string4,string5,string6);
				list.add(yanglao);
			}
		} catch (Exception e) {
			tracer.addTag("action.xiangyang.getyanglaopay", "抓取失败:"+e.getMessage());
			return list;
		}
		return list;
	}

	public List<InsuranceXiangYangYiLiao> getyiliaopay(String html, String taskId) {
		List<InsuranceXiangYangYiLiao> list = null;
		try {
			Document doc = Jsoup.parse(html);
			list = new ArrayList<InsuranceXiangYangYiLiao>();
			Element element = doc.getElementById("zxbs_2008_ta5").getElementsByTag("table").get(4);
			Elements elementsByTag = element.getElementsByTag("tr");
			for (int i = 2; i < elementsByTag.size()-1; i++) {
				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
				System.out.println(string+string2+string3+string4+string5+string6);
				InsuranceXiangYangYiLiao yiliao = new InsuranceXiangYangYiLiao(taskId,string,string2,string3,string4,string5,string6);
				list.add(yiliao);
			}
		} catch (Exception e) {
			tracer.addTag("action.xiangyang.getyiliaopay", "抓取失败:"+e.getMessage());
			return list;
		}
		return list;
	}

	public List<InsuranceXiangYangShiYe> getshiyepay(String html, String taskId) {
		List<InsuranceXiangYangShiYe> list = null;
		try {
			Document doc = Jsoup.parse(html);
			list = new ArrayList<InsuranceXiangYangShiYe>();
			Element element = doc.getElementById("zxbs_2008_ta7").getElementsByTag("table").get(2);
			Elements elementsByTag = element.getElementsByTag("tr");
			for (int i = 3; i < elementsByTag.size(); i++) {
				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
				System.out.println(string+string2+string3+string4+string5+string6);
				InsuranceXiangYangShiYe shiye = new InsuranceXiangYangShiYe(taskId,string,string2,string3,string4,string5,string6);
				list.add(shiye);
			}
		} catch (Exception e) {
			tracer.addTag("action.xiangyang.getshiyepay", "抓取失败:"+e.getMessage());
			return list;
		}
		return list;
	}

	public List<InsuranceXiangYangGongShang> getgongshangpay(String html, String taskId) {
		List<InsuranceXiangYangGongShang> list = null;
		try {
			Document doc = Jsoup.parse(html);
			list = new ArrayList<InsuranceXiangYangGongShang>();
			Element element = doc.getElementById("zxbs_2008_ta8").getElementsByTag("table").get(2);
			Elements elementsByTag = element.getElementsByTag("tr");
			for (int i = 3; i < elementsByTag.size(); i++) {
				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
				System.out.println(string+string2+string3+string4+string5+string6);
				InsuranceXiangYangGongShang gongshang = new InsuranceXiangYangGongShang(taskId,string,string2,string3,string4,string5,string6);
				list.add(gongshang);
			}
		} catch (Exception e) {
			tracer.addTag("action.xiangyang.getgongshangpay", "抓取失败:"+e.getMessage());
			return list;
		}
		return list;
	}

	public List<InsuranceXiangYangShengYu> getshengyupay(String html, String taskId) {
		List<InsuranceXiangYangShengYu> list = null;
		try {
			Document doc = Jsoup.parse(html);
			list = new ArrayList<InsuranceXiangYangShengYu>();
			Element element = doc.getElementById("zxbs_2008_ta9").getElementsByTag("table").get(2);
			Elements elementsByTag = element.getElementsByTag("tr");
			for (int i = 3; i < elementsByTag.size(); i++) {
				String string = elementsByTag.get(i).getElementsByTag("td").get(0).text().trim();//年月
				String string2 = elementsByTag.get(i).getElementsByTag("td").get(1).text().trim();//险种
				String string3 = elementsByTag.get(i).getElementsByTag("td").get(2).text().trim();//缴费类别
				String string4 = elementsByTag.get(i).getElementsByTag("td").get(3).text().trim();//缴费基数
				String string5 = elementsByTag.get(i).getElementsByTag("td").get(4).text().trim();//应缴金额
				String string6 = elementsByTag.get(i).getElementsByTag("td").get(5).text().trim();//单位名称
				System.out.println(string+string2+string3+string4+string5+string6);
				InsuranceXiangYangShengYu shengyu = new InsuranceXiangYangShengYu(taskId,string,string2,string3,string4,string5,string6);
				list.add(shengyu);
			}
		} catch (Exception e) {
			tracer.addTag("action.xiangyang.getshengyupay", "抓取失败:"+e.getMessage());
			return list;
		}
		return list;
	}

}
