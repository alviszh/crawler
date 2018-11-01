package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiMessage;

public class TestSMS {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/短信.txt");
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
		TelecomAnhuiMessage telecomAnhuiMessage =null;
		List list = new ArrayList();
		Elements elements2 = doc.getElementsByClass("tabsty").get(0).getElementsByTag("tbody").get(0).getElementsByTag("nobr");
		System.out.println(elements2.text());	
		for (int i = 1; i < elements2.size(); i=i+11) {
			
			for (int j = 0; j < elements2.size(); j++) {
			telecomAnhuiMessage =new TelecomAnhuiMessage();
			telecomAnhuiMessage.setSmsType(elements2.get(i).text());
			telecomAnhuiMessage.setGetType(elements2.get(i+1).text());
			telecomAnhuiMessage.setHisNum(elements2.get(i+2).text());
			telecomAnhuiMessage.setSendTime(elements2.get(i+3).text());
			telecomAnhuiMessage.setMoney(elements2.get(i+4).text());
			}
			list.add(telecomAnhuiMessage);
		}
		System.out.println(list);
	}
}
