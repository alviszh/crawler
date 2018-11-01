package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.internal.engine.messageinterpolation.FormatterWrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBill;

public class TestBill {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/账单.txt");
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
		//Elements element = doc.select(".titbill");
		TelecomAnhuiBill telecomAnhuiBill =null;
		
//		Elements red = doc.getElementsByClass("titbill").get(1).getElementsByTag("h5");
//		String[] split4 = red.text().split(" ");
//		System.out.println(red.text()+"--");
//		telecomAnhuiBill = new TelecomAnhuiBill();
//		telecomAnhuiBill.setMoney(split4[0]);
//		telecomAnhuiBill.setBillName(split4[1]);
//		System.out.println(telecomAnhuiBill);
		Elements elementsByTag = doc.getElementsByClass("titbill").get(1).getElementsByTag("dl");
		for (Element element : elementsByTag) {
			String text = element.text();
			String[] split3 = text.split(" ");
			telecomAnhuiBill = new TelecomAnhuiBill();
			telecomAnhuiBill.setMoney(split3[0]);
			telecomAnhuiBill.setBillName(split3[1]);
			System.out.println(telecomAnhuiBill);
		}
		//System.out.println(elementsByTag.text());
		System.out.println(doc.getElementsByClass("titbill"));
		
		Elements elementsByTag2 = doc.getElementsByClass("titbill").get(1).getElementsByTag("li");
		System.out.println(elementsByTag2);
		for (Element element : elementsByTag2) {
			String text = element.text();
			String[] split3 = text.split(" ");
			telecomAnhuiBill = new TelecomAnhuiBill();
			telecomAnhuiBill.setMoney(split3[0]);
			telecomAnhuiBill.setBillName(split3[1]);
			if(split3[1].equals("套餐月基本费"))
			{
				System.out.println(telecomAnhuiBill);
				break;
			}
			
		}
		Elements elementsByTag3 = doc.getElementsByClass("titbill").get(1).getElementsByTag("h5");
		System.out.println(elementsByTag3);
		for (Element element : elementsByTag3) {
			if(element.text().contains("."))
			{
				
				String text = element.text();
				String[] split3 = text.split(" ");
				telecomAnhuiBill = new TelecomAnhuiBill();
				
					telecomAnhuiBill.setMoney(split3[0]+"");
					telecomAnhuiBill.setBillName(split3[1]+"");
					System.out.println(telecomAnhuiBill);
			}
			else{
				continue;
			}
			
				
			
		}
//		System.out.println(elementsByTag2.text());
//		System.out.println(elementsByTag2);
//		System.out.println(elementsByTag3);
		
		
//		
//		
//		if(elementsByTag.text().contains("天翼套餐月使用费"))
//		{
//			String string = elementsByTag.html();
//			String[] split = string.split("<label class='s1 fr'>");
//			
//			String[] split2 = split[1].split("</label>");
//			for (String string2 : split2) {
//				System.out.println(string2);
//			}
//		}
//		
//		telecomAnhuiBill = new TelecomAnhuiBill();
//		telecomAnhuiBill.setMealMoney(elements2.get(0).text());
//		telecomAnhuiBill.setSmsMoney(elements2.get(2).text());
//		telecomAnhuiBill.setSumMoney(elements2.get(3).text());
//	
//		//Elements elements = doc.getElementsByTag("label");
//		System.out.println(telecomAnhuiBill);
//		
//		String url="费用项目 金额(元) 使用量项目 套餐包含使用量 实际使用量 "
//				+ "套餐月基本费 299.00 套餐月基本费 套餐超出费用 短信彩信费 5.70 短信费 304.70 "
//				+ "本项小计: 手机:18056292299 "
//				+ "乐享4G299元 国内通用流量[共享] 4096.00 0.00 国内通话分钟数[共享] 1500 267 201505-欢GO促销通用包201505 0元1GB 省内通用流量 1024.00 1024.00 赠送省内流量2GB（12个月） 省内通用流量 2048.00 2048.00 上月结转使用情况 使用量项目 上月结转 本月使用 手机:18056292299 3 乐享4G299元 国内通用流量[共享] 3405.30 1705.32 套餐月基本费 299.00 套餐月基本费 套餐超出费用 手机:18056292299 短信彩信费 5.70 短信费 304.70 本项小计: 手机:18056292299 乐享4G299元 国内通用流量[共享] 4096.00 0.00 国内通话分钟数[共享] 1500 267 201505-欢GO促销通用包201505 0元1GB 省内通用流量 1024.00 1024.00 赠送省内流量2GB（12个月） 省内通用流量 2048.00 2048.00 上月结转使用情况 使用量项目 上月结转 本月使用 手机:18056292299 乐享4G299元 国内通用流量[共享] 3405.30 1705.32";
	}

	public static String getNextLabelByKeywordTwo(Elements element,  String keyword, String tag) {
		Elements es = element.select(tag + ":contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element1 = es.first();
			Element nextElement = element1.previousElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
}
