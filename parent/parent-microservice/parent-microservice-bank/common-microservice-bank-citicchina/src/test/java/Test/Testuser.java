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

public class Testuser {
	public static void main(String[] args) {
		String txt = null;
		try {
	        String encoding="UTF-8";
	        File file = new File("C:/Users/Administrator/Desktop/资料.txt");
	        if(file.isFile() && file.exists()){ //判断文件是否存在
	            InputStreamReader read = new InputStreamReader(
	            new FileInputStream(file),encoding);//考虑到编码格式
	            BufferedReader bufferedReader = new BufferedReader(read);
	            String lineTxt = null;
	            while((lineTxt = bufferedReader.readLine()) != null){
	                txt += lineTxt;
	            }
	           // System.out.println(txt);
	            read.close();
	        }else{
	        	System.out.println("找不到指定的文件");
	        }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		
		Document doc = Jsoup.parse(txt);
		//System.out.println(doc.toString());
		if(doc.toString().contains("获取账单信息成功"))
		{
			Element element1 = doc.getElementsByTag("billprofile").get(0);
			System.out.println(element1);
			String string1 = element1.toString();
			int cur_amtNum = string1.indexOf("cur_amt");
			int min_payNum = string1.indexOf("min_pay");			
			int cashNum = string1.indexOf("cash");
			int stmt_date_textNum = string1.indexOf("stmt_date_text");
			int dte_pymt_dueNum = string1.indexOf("dte_pymt_due");
			int purchaseNum = string1.indexOf("purchase");
			
			int curr_codeNum = string1.indexOf("curr_code");
			int pre_hpayNum = string1.indexOf("pre_hpay");
			int stmt_dateNum = string1.indexOf("stmt_date");
			String cur_amt = string1.substring(cur_amtNum, curr_codeNum).substring(8);
			System.out.println(cur_amt);
			String min_pay = string1.substring(min_payNum, pre_hpayNum).substring(8);
			System.out.println(min_pay);
			String cash = string1.substring(cashNum, cur_amtNum).substring(5);
			System.out.println(cash);
			String stmt_date_text = string1.substring(stmt_date_textNum).substring(15,29);
			System.out.println(stmt_date_text);
			String dte_pymt_due = string1.substring(dte_pymt_dueNum, min_payNum).substring(13);
			System.out.println(dte_pymt_due);
			String purchase = string1.substring(purchaseNum, stmt_dateNum).substring(9);
			System.out.println(purchase);
			
			
			Elements elementsByTag = doc.getElementsByTag("billdetaillist");
			System.out.println(elementsByTag);
			CiticChinaCreditCardAccount c = null;
			List list = new ArrayList();
			for (Element element : elementsByTag) {
				String string = element.toString();
				c = new CiticChinaCreditCardAccount();
				int post_amtNum = string.indexOf("post_amt");
				int post_currNum = string.indexOf("post_curr");
				int post_dateNum = string.indexOf("post_date");
				int tram_codeNum = string.indexOf("tram_code");
				int tran_currNum = string.indexOf("tran_curr");
				int tran_dateNum = string.indexOf("tran_date");
				int tran_descNum = string.indexOf("tran_desc");
				int tran_amtNum = string.indexOf("tran_amt");
				String post_amt = string.substring(post_amtNum, post_currNum).substring(9);
				String post_curr = string.substring(post_currNum, post_dateNum).substring(10);
				String post_date = string.substring(post_dateNum, tram_codeNum).substring(10);
				//String tram_code = string.substring(tram_codeNum, tran_amtNum).substring(10);
				String tran_curr = string.substring(tran_currNum, tran_dateNum).substring(10);
				String tran_date = string.substring(tran_dateNum, tran_descNum).substring(10);
				String tran_amt = string.substring(tran_amtNum,tran_currNum).substring(9);
				String tran_desc = string.substring(tran_descNum).substring(10).replace("/>", "");
				System.out.println(post_amt+"/"+post_curr);
				System.out.println(post_date);
				//System.out.println(tram_code);
				System.out.println(tran_date);
				System.out.println(tran_desc);
				System.out.println(tran_amt+"/"+tran_curr);
				
				c.setMoneyStatus(post_amt);
				c.setDescription(tran_desc);
				c.setMoneyStatus(post_amt+"/"+post_curr);
				c.setDatea(post_date);
				c.setSum(tran_amt+"/"+tran_curr);
				c.setGetDatea(tran_date);
//				
//				c.setRepay(cur_amt);
//				c.setLowstRepay(min_pay);
//				c.setAlreadyMoney(cash);
//				c.setNotRepay(purchase);
//				c.setThisDatea(stmt_date_text);
//				c.setRepayDatea(dte_pymt_due);
//				list.add(c);
			}
			System.out.println(list);
		}
			
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
