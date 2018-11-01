package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardAccount;
import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaCreditCardBill;


public class TestRead2{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\1.txt"); 
		String json = txt2String(file);
//		System.out.println(json);
		CiticChinaCreditCardAccount c = null;
		Document doc = Jsoup.parse(json);
		Elements elementsByTag = doc.getElementsByTag("billDetailList");
//		System.out.println(elementsByTag);
		List<CiticChinaCreditCardAccount> list = new ArrayList<CiticChinaCreditCardAccount>();
		for (int i = 0; i < elementsByTag.size(); i++) {
			c = new CiticChinaCreditCardAccount();
			Element element = elementsByTag.get(i);
			String string = element.toString();
			int tran_desc = string.indexOf("tran_desc");
//			System.out.println(tran_desc);
			int tran_date = string.indexOf("tran_date");
//			System.out.println(tran_date);
			int tran_curr = string.indexOf("tran_curr");
//			System.out.println(tran_curr);
			int tran_amt = string.indexOf("tran_amt");
//			System.out.println(tran_amt);
			int post_date = string.indexOf("post_date");
//			System.out.println(post_date);
			int post_curr = string.indexOf("post_curr");
//			System.out.println(post_curr);
			int rs_card_nbr = string.indexOf("rs_card_nbr");
//			System.out.println(rs_card_nbr);
			int post_amt = string.indexOf("post_amt");
//			System.out.println(post_amt);
			int tran_code = string.indexOf("tran_code");
//			System.out.println(tran_code);
			
			
			String tran_desc1 = string.substring(tran_desc, tran_date);
			String tran_date1 = string.substring(tran_date, tran_curr);
			String tran_curr1 = string.substring(tran_curr, tran_code);
			String tran_amt1 = string.substring(tran_amt, rs_card_nbr);
			String post_date1 = string.substring(post_date, post_curr);
			String post_curr1 = string.substring(post_curr, post_amt);
			System.out.println(tran_desc1.substring(11, tran_desc1.length()-2));
			System.out.println(tran_date1.substring(11, tran_date1.length()-2));
			System.out.println(tran_curr1.substring(11, tran_curr1.length()-2));
			System.out.println(tran_amt1.substring(10, tran_amt1.length()-2));
			System.out.println(post_date1.substring(11, post_date1.length()-2));
			System.out.println(post_curr1.substring(11, post_curr1.length()-2));
			
			c.setDescription(tran_desc1.substring(11, tran_desc1.length()-2));
			c.setDatea(tran_date1.substring(11, tran_date1.length()-2));
			c.setGetDatea(post_date1.substring(11, post_date1.length()-2));
			c.setIdCard("");
			c.setAccountType("未出账单");
			c.setMoneyStatus(tran_curr1.substring(11, tran_curr1.length()-2));
			c.setSum(tran_amt1.substring(10, tran_amt1.length()-2));
			c.setTaskid("");
			list.add(c);
		}
		System.out.println(list);
		
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