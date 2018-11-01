package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouUserInfo;

import net.sf.json.JSONObject;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zhuzhouuser.txt"); 
		String json = txt2String(file);
		System.out.println(json);
		Document doc = Jsoup.parse(json);
		HousingFundZhuZhouUserInfo h = new HousingFundZhuZhouUserInfo();
		h.setPersonalNum(doc.getElementById("grzhye").val());
		h.setStatus(doc.getElementById("frzflag").val());//01身份证02
		h.setCardNum(doc.getElementById("zjhm").val());
		h.setName(doc.getElementById("xingming").val());
		h.setCompanyNum(doc.getElementById("dwzh").val());
		h.setCompany(doc.getElementById("unitaccname").val());
		h.setPhone(doc.getElementById("sjhm").val());
		h.setOpenDate(doc.getElementById("khrq").val());
		h.setStatus(doc.getElementById("grzhzt").val());
		h.setYf(doc.getElementById("frzflag").val());
		h.setYfMoney(doc.getElementById("isloanflag").val());
		h.setEndDate(doc.getElementById("jzny").val());
		h.setPersonalBase(doc.getElementById("grjcjs").val());
		h.setMonthPay(doc.getElementById("indipaysum").val());
		h.setCompanyPay(doc.getElementById("dwyjce").val());
		h.setPersonalMonth(doc.getElementById("gryjce").val());
		h.setCompanyRatio(doc.getElementById("unitprop").val());
		h.setPersonalRatio(doc.getElementById("indiprop").val());
		h.setFee(doc.getElementById("grzhye").val());
		h.setConnectNum(doc.getElementById("cardno").val());
		h.setTaskid("");
		System.out.println(h);
	}
	public static String txt2String(File file) { 
		StringBuilder result = new StringBuilder(); 
		try { 
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); 
		String s = null; 
		while ((s = br.readLine()) != null) { 
		result.append(System.lineSeparator() + s); 
		} 
		br.close(); 
		} catch (Exception e) { 
		e.printStackTrace(); 
		} 
		return result.toString(); 
		}

	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容2
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}