package test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class Test {

	public static void main(String[] args) throws IOException {
//		String json = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\test.html"),"UTF-8");
//		//FileUtils.writeStringToFile(new File("C:\\Users\\lenovo\\Desktop\\test.txt"), "UTF-8");
//				
//		JSONArray jsonArray = JSONArray.fromObject(json);  
//		JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));
//		System.out.println(list1ArrayObjs.getString("PageIndex"));
//		System.out.println(list1ArrayObjs.getString("maxPage"));######
		//JSONArray listInfoArray = JSONArray.fromObject(listInfoStr);  

		for(int i=0;i<1;i++){
//			LocalDate today = LocalDate.now();
//			// 本月的第一天
//			LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
//			LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
//			if (i == 0) {
//				enddate = today;
//			}
//			String monthint = stardate.getMonthValue() + "";
//			if (monthint.length() < 2) {
//				monthint = "0" + monthint;
//			}
//			String month = stardate.getYear() + "年" + monthint + "月";
//			System.out.println(month);
		}
	
		

		
		//[{telephone: "099118999891510", queryDate: "201707", markType: 0, deviceType: "25"}]
		String telephone="099118999891510";
	 	String queryDate="201707";
		String markType="0";
		String deviceType="25";
		//String data= "[{telephone:\""+telephone+"\",queryDate:\""+queryDate+"\",markType:"+markType+",deviceType:\""+deviceType+"\"}]";	 	
			String data = "[{\"telephone\":\""+telephone+"\",\"queryDate\":\""+queryDate+"\",\"markType\":"+markType+",\"deviceType\":\""+deviceType+"\"}]";
      System.out.println(data);
 	}

}
