package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\888.html"),"UTF-8");
		//System.out.println(html);
		Document doc = Jsoup.parse(html, "utf-8");
		String 	username = getNextLabelByKeyword(doc, "职工姓名");	
		System.out.println(username);
		String companyNum = getNextLabelByKeyword(doc, "单位账号");
		System.out.println(companyNum);
		String idnum = getNextLabelByKeyword(doc, "身份证号");
		System.out.println(idnum);
		String accountNum = getNextLabelByKeyword(doc, "职工账号");
		System.out.println(accountNum);
		String companyName = getNextLabelByKeyword(doc, "所在单位");
		System.out.println(companyName);
		String staffName = getNextLabelByKeyword(doc, "所属办事处");
		System.out.println(staffName);
		String openTime = getNextLabelByKeyword(doc, "开户日期");
		System.out.println(openTime);
		String accountState = getNextLabelByKeyword(doc, "当前状态");
		System.out.println(accountState);
		String payBase = getNextLabelByKeyword(doc, "月缴基数");
		System.out.println(payBase);
		String payAmount = getNextLabelByKeyword(doc, "月缴金额");
		System.out.println(payAmount);
		String payRatio = getNextLabelByKeyword(doc, "个人/单位");// 缴存比例	
		System.out.println(payRatio);
		String suppleRatio = getNextLabelByKeyword(doc, "补充缴存率");
		System.out.println(suppleRatio);
	}
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("tr[class=jtpsoft] td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}
