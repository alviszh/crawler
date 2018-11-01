package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.zhanjiang.HousingFundZhanJiangUserInfo;


public class TestRead1{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\zj.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		Document parse = Jsoup.parse(json);
		Elements elementById = parse.getElementById("MainContent_tabConainer_tabAccInfo").getElementsByTag("tr");
		System.out.println(elementById);
		HousingFundZhanJiangUserInfo h = new HousingFundZhanJiangUserInfo();
		
		
		String name = getNextLabelByKeywordTwo(elementById, "姓名", "th");
		h.setName(name);
		String fundNum = getNextLabelByKeywordTwo(elementById, "公积金账号", "th");
		h.setFundNum(fundNum);
		String openDate = getNextLabelByKeywordTwo(elementById, "开户日期", "th");
		h.setOpenDate(openDate);
		String companyNum = getNextLabelByKeywordTwo(elementById, "单位账号", "th");
		h.setCompanyNum(companyNum);
		String company = getNextLabelByKeywordTwo(elementById, "单位名称", "th");
		h.setCompany(company);
		String status = getNextLabelByKeywordTwo(elementById, "公积金状态", "th");
		h.setStatus(status);
		String cardStatus = getNextLabelByKeywordTwo(elementById, "注册证件类型", "th");
		h.setCardStatus(cardStatus);
		String iDNum = getNextLabelByKeywordTwo(elementById, "注册证件号码", "th");
		h.setIDNum(iDNum);
		String base = getNextLabelByKeywordTwo(elementById, "缴存基数", "th");
		h.setBase(base);
		String companyPay = getNextLabelByKeywordTwo(elementById, "单位月缴额", "th");
		h.setCompanyPay(companyPay);
		String personalPay = getNextLabelByKeywordTwo(elementById, "个人月缴额", "th");
		h.setPersonalPay(personalPay);
		String monthPay = getNextLabelByKeywordTwo(elementById, "财政月缴额", "th");
		h.setMonthPay(monthPay);
		String birth = getNextLabelByKeywordTwo(elementById, "出生年月", "th");
		h.setBirth(birth);
		String sex = getNextLabelByKeywordTwo(elementById, "性别", "th");
		h.setSex(sex);
		String endDate = getNextLabelByKeywordTwo(elementById, "缴存截至年月", "th");
		h.setEndDate(endDate);
		String phone = getNextLabelByKeywordTwo(elementById, "手机号码", "th");
		h.setPhone(phone);
		String addr = getNextLabelByKeywordTwo(elementById, "通讯地址", "th");
		h.setAddr(addr);
		String num = getNextLabelByKeywordTwo(elementById, "固定电话", "th");
		h.setNum(num);
		
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