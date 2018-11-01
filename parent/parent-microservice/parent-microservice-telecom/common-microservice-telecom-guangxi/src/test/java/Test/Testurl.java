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

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBill;
import com.microservice.dao.entity.crawler.telecom.guangxi.TelecomGuangxiBill;

public class Testurl {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/gx.txt");
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
		if(doc.text().contains("本项小计"))
		{
			int size = doc.getElementsByClass("divC").get(1).getElementsByTag("table").get(0).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").size();
			if(size==6 | size==4 | size==8)
			{
				Elements class1 = doc.getElementById("divFee").getElementsByClass("divC").get(0).getElementsByTag("table").get(0).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").get(2).getElementsByTag("td");
				String mealMoney = getNextLabelByKeywordTwo(class1,"套餐月基本费", "div");
				String sumMoney = class1.get(0).getElementsByTag("span").text();
				
				Elements eles1 = class1.select("[style= float:left; text-align: left;]");
				List<TelecomGuangxiBill> list = new ArrayList<TelecomGuangxiBill>();
				TelecomGuangxiBill telecomGuangxiBill = new TelecomGuangxiBill();
				for (Element element : eles1) {
					String serviceName = element.text().trim().replace(" ", "");
					Element nextElement = element.nextElementSibling();
					String fee = "";
					
					telecomGuangxiBill.setMealMoney(mealMoney);
					telecomGuangxiBill.setSumMoney(sumMoney);
					if (null != nextElement) {
						fee = nextElement.text();
					}
					
					if(serviceName.contains("国内短信费"))
					{
						telecomGuangxiBill.setMessageMoney(fee);
					}
					else if(serviceName.contains("优惠费用"))
					{
						telecomGuangxiBill.setFavourable(fee);
					}
					else if(serviceName.contains("国内通话费"))
					{
						telecomGuangxiBill.setLandMoney(fee);
					}
					else if(serviceName.contains("4G包月流量包费"))
					{
						telecomGuangxiBill.setFourMoney(fee);
					}
					else if(serviceName.contains("无线宽带费"))
					{
						telecomGuangxiBill.setNetMoney(fee);
					}
					else if(serviceName.contains("红包返还"))
					{
						telecomGuangxiBill.setRedMoney(fee);
					}
					
				}
				list.add(telecomGuangxiBill);
				System.out.println(list+"=====");
			}
			
		}
		
		
		
		
		
		
		
		
		
		
		
		Elements elementsByTag = doc.getElementsByClass("divC").get(1).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(2).getElementsByTag("div");
		System.out.println(elementsByTag.text());
		
		TelecomGuangxiBill telecomGuangxiBill=null;
		List list = new ArrayList();
		for (Element element : elementsByTag) {
			if(element.toString().contains("bold"))
			{
			}
			else
			{
				
				if(element.text().contains("国内通话费"))
				{
				}
				else if(element.text().contains("."))
				{
				}
			}
			
		}
		//System.out.println(doc.getElementsByClass("divC").get(1).getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr").get(2).getElementsByTag("div"));
	}

	public static String getNextLabelByKeywordTwo(Elements element, String keyword, String tag) {
		Elements es = element.select(tag+":contains(" + keyword + ")");
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
