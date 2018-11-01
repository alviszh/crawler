package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\qqq.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		Element fixBand1 = doc.getElementById("fixBand1");
		System.out.println("///////////////////////////////////////账单周期部分//////////////////////////////////");
		Element fixBand75 = fixBand1.getElementById("fixBand75");
		Element fixBand2_zd = fixBand75.getElementById("fixBand2");
		//////////////////////////////////////////////////////////////////////账单周期部分------账单周期
		Element fixBand5 = fixBand2_zd.getElementById("fixBand5");
		Elements fonts = fixBand5.getElementsByTag("font");
		String period = fonts.get(1).text().toString();
		System.out.println("账单周期---" + period);
		//////////////////////////////////////////////////////////////////////账单周期部分------剩下的部分
		Element fixBand7 = fixBand2_zd.getElementById("fixBand7");
		Elements fonts2 = fixBand7.getElementsByTag("font");
		// 卡号
		String cardnumber = fonts2.get(0).text().toString();
		System.out.println("卡号---" + cardnumber);
		// 本期应还总额
		String newbalance = fonts2.get(1).text().toString();
		System.out.println("本期应还总额---" + newbalance);
		// 最低还款额
		String minpayment = fonts2.get(2).text().toString();
		System.out.println("最低还款额---" + minpayment);
		// 最后还款日
		String paymentdate = fonts2.get(3).text().toString();
		System.out.println("最后还款日---" + paymentdate);
		// 清算货币
		String currency = fonts2.get(4).text().toString();
		System.out.println("清算货币---" + currency);
		// 户口消费额度
		String creditlimit = fonts2.get(5).text().toString();
		System.out.println("户口消费额度---" + creditlimit);
		
		System.out.println("///////////////////////////////////////积分部分//////////////////////////////////");
		Element fixBand71 = fixBand1.getElementById("fixBand71");
		Element fixBand7_jf = fixBand71.getElementById("fixBand7");
		Elements fonts3 = fixBand7_jf.getElementsByTag("font");
		// 积分类型
		String integraltype = fonts3.get(1).text().toString();
		System.out.println("积分类型---" + integraltype);
		// 上期余额
		String periodyue = fonts3.get(2).text().toString();
		System.out.println("上期余额---" + periodyue);
		// 本期新增
		String add = fonts3.get(3).text().toString();
		System.out.println("本期新增---" + add);
		// 本期扣减
		String subtract = fonts3.get(4).text().toString();
		System.out.println("本期扣减---" + subtract);
		// 本期余额
		String yue = fonts3.get(5).text().toString();
		System.out.println("本期余额---" + yue);
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

}
