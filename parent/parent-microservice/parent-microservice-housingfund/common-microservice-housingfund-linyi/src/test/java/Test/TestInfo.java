package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.housing.linyi.HousingFundLinYiUserInfo;

public class TestInfo {

	public static void main(String[] args) {

		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/新建文本文档.txt");
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
//		System.out.println(doc);
		Elements elementsByTag = doc.getElementsByTag("tr").get(2).getElementsByTag("div");
		HousingFundLinYiUserInfo h = new HousingFundLinYiUserInfo();
		System.out.println(elementsByTag.text());
			h.setIdNum(elementsByTag.get(0).text());
			h.setFundNum(elementsByTag.get(1).text());
			h.setMonthPay(elementsByTag.get(2).text().substring(0, elementsByTag.get(2).text().length()-2));
			h.setFee(elementsByTag.get(3).text().substring(0, elementsByTag.get(3).text().length()-2));
			h.setPayDate(elementsByTag.get(4).text().substring(0, elementsByTag.get(4).text().length()-2));
		System.out.println(h);
	}
}
