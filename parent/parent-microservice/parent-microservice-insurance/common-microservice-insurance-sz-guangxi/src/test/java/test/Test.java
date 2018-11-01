package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.insurance.sz.guangxi.InsuranceSZGuangXiUserInfo;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\999.html"), "UTF-8");
		Document doc = Jsoup.parse(html, "UTF-8");
		String username=getNextLabelByKeyword(doc,"姓名");
		System.out.println(username);
		String idnum = getNextLabelByKeyword(doc, "身份证号"); // 证件号码
		String gender = getNextLabelByKeyword(doc, "性别"); // 性别
		String nation = getNextLabelByKeyword(doc, "民族"); // 民族
		String householdAddress = getNextLabelByKeyword(doc, "户籍地址"); // 户籍地址
		String companyname = getNextLabelByKeyword(doc, "单位名称"); // 单位名称
		String firstworkDate = getNextLabelByKeyword(doc, "参加工作日期"); // 参加工作日期
		String identity = getNextLabelByKeyword(doc, "个人身份"); // 个人身份
		String insuranceState = getNextLabelByKeyword(doc, "参保状态"); // 参保状态
		String insuranceNum = getNextLabelByKeyword(doc, "社保卡号"); // 社保卡号
		InsuranceSZGuangXiUserInfo	userInfo = new InsuranceSZGuangXiUserInfo(username, idnum, gender, nation,
				householdAddress, companyname, firstworkDate, identity, insuranceState, insuranceNum, "ttt");
		System.out.println(userInfo.toString());
	}
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("label:contains(" + keyword + ")");
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
