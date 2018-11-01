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

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiPay;

public class TestPay {
public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/支付.txt");
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
		Elements elements = doc.getElementsByTag("tr");
		List list = new ArrayList();
		for (int i = 0; i < elements.size(); i++) {
			
			Elements elements2 = elements.get(i).getElementsByTag("td");
			TelecomAnhuiPay telecomAnhuiPay = new TelecomAnhuiPay();
			for (int j = 0; j < elements2.size(); j=j+6) {
			telecomAnhuiPay.setStatus(elements.get(j).text());
			telecomAnhuiPay.setPayTime(elements2.get(j+1).text());
			telecomAnhuiPay.setOutMoney(elements2.get(j+2).text());
			telecomAnhuiPay.setGetMoney(elements2.get(j+3).text());
			telecomAnhuiPay.setBanlance(elements2.get(j+4).text());
			telecomAnhuiPay.setTypePay(elements2.get(j+5).text());
			list.add(telecomAnhuiPay);
			}
		}
		System.out.println(list);

    }
}