package org.common.microservice.bank.cebchina.creditcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class text {


	public static void main(String[] args) throws Exception { 
		for(int i =13;i<14;i++){
		LocalDate today = LocalDate.now();
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(),1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		String monthint = stardate.getMonthValue() + "";
		if(monthint.length()<2){
			monthint = "0" + monthint;
		}
		String month1 = stardate.getYear() + monthint;
		System.out.println(month1);
		}
//		File file = new File("F:\\a.txt"); 
//		String lineTxt = readTxtFile(file);
//		lineTxt = lineTxt.substring(4);
//		//解析数据： 
//		Document doc = Jsoup.parse(lineTxt); 
//		Elements elementsByTag = doc.getElementsByTag("table");
//		Element element = elementsByTag.get(7);
//		Elements elementsByTag2 = element.getElementsByTag("tr");
//		for(int i = 0;i<elementsByTag2.size();i++){
//			if(i==0){
//			String text = elementsByTag2.get(0).getElementsByTag("span").get(0).text();//kahao
//			}
//			if(i>=2){
//				String text = elementsByTag2.get(i).getElementsByTag("td").get(0).text().trim();
//				String text2 = elementsByTag2.get(i).getElementsByTag("td").get(1).text().trim();
//				String text3 = elementsByTag2.get(i).getElementsByTag("td").get(2).text().trim();
//				String text4 = elementsByTag2.get(i).getElementsByTag("td").get(3).text().trim();
//				String text5 = elementsByTag2.get(i).getElementsByTag("td").get(4).text().trim();
//			}
//			
//			
//		}
	}

	public static String readTxtFile(File fileName)throws Exception{  
		String result=null;  
		FileReader fileReader=null; 
		BufferedReader bufferedReader=null;  
		try{  
			fileReader=new FileReader(fileName);  
			bufferedReader=new BufferedReader(fileReader);  
			try{  
				String read=null;  
				while((read=bufferedReader.readLine())!=null){  
					result=result+read;  
				}  
			}catch(Exception e){  
				e.printStackTrace();  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			if(bufferedReader!=null){  
				bufferedReader.close();  
			}  
			if(fileReader!=null){  
				fileReader.close();  
			}  
		}  

		return result;  
	} 
}		



