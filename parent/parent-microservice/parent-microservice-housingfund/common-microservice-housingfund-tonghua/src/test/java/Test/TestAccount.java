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

import com.microservice.dao.entity.crawler.housing.tonghua.HousingFundTongHuaAccount;

public class TestAccount {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/2th.txt");
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
		HousingFundTongHuaAccount h =null;
		List<HousingFundTongHuaAccount> list = new ArrayList<HousingFundTongHuaAccount>();
//		System.out.println(doc);
		Elements elementsByTag = doc.getElementsByTag("form").get(1).getElementsByTag("tbody");
//		System.out.println(elementsByTag);
		for (int i = 0; i < elementsByTag.size(); i++) {
			Elements elementsByTag2 = elementsByTag.get(i).getElementsByTag("td");
			//System.out.println(elementsByTag2);
			for (int j = 0; j < elementsByTag2.size(); j=j+7) {
				h = new HousingFundTongHuaAccount();
				h.setDatea(elementsByTag2.get(j+1).text());
				h.setDescr(elementsByTag2.get(j+2).text());
				h.setPayDatea(elementsByTag2.get(j+3).text());
				h.setGetMoney(elementsByTag2.get(j+4).text());
				h.setCostMoney(elementsByTag2.get(j+5).text());
				h.setFee(elementsByTag2.get(j+6).text());
				list.add(h);
			}
			
		}
		System.out.println(list);
	}
}
