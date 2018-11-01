package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.bank.citicchina.CiticChinaDebitCardRegular;

public class TestRegualr {
	public static void main(String[] args) {
		String txt = null;
		try {
	        String encoding="UTF-8";
	        File file = new File("C:/Users/Administrator/Desktop/123.txt");
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
		//System.out.println(doc);
		Elements elementsByClass = doc.getElementsByClass("bg_white_color");
		//System.out.println(elementsByClass);
		Element element = elementsByClass.get(0);
		Elements elementsByTag2 = element.getElementsByTag("tr");
		//System.out.println(elementsByTag2.size());
		Elements elementsByTag = element.getElementsByTag("td");
		//System.out.println(elementsByTag);
		CiticChinaDebitCardRegular c = new CiticChinaDebitCardRegular();
		for (int i = 0; i < elementsByTag2.size(); i++) {
			c.setType(elementsByTag.get(i).text()+"");
			c.setCurrency(elementsByTag.get(i+1).text()+"");
			c.setRatio(elementsByTag.get(i+2).text()+"");
			c.setTime(elementsByTag.get(i+3).text()+"");
			c.setDoRatio(elementsByTag.get(i+4).text()+"");
			c.setStartDate(elementsByTag.get(i+5).text()+"");
			c.setEndDate(elementsByTag.get(i+6).text()+"");
			c.setUseFee(elementsByTag.get(i+7).text()+"");
			c.setAccountFee(elementsByTag.get(i+8).text()+"");
			c.setStatus(elementsByTag.get(i+9).text()+"");
		}
//		c.setType(elementsByTag.get(0).text()+"");
//		c.setCurrency(elementsByTag.get(1).text()+"");
//		c.setRatio(elementsByTag.get(2).text()+"");
//		c.setTime(elementsByTag.get(3).text()+"");
//		c.setDoRatio(elementsByTag.get(4).text()+"");
//		c.setStartDate(elementsByTag.get(5).text()+"");
//		c.setEndDate(elementsByTag.get(6).text()+"");
//		c.setUseFee(elementsByTag.get(7).text()+"");
//		c.setAccountFee(elementsByTag.get(8).text()+"");
//		c.setStatus(elementsByTag.get(9).text()+"");
		System.out.println(c);
		
		
	}
	
}