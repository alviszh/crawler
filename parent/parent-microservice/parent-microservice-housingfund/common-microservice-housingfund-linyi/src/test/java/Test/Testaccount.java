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

import com.microservice.dao.entity.crawler.housing.linyi.HousingFundLinYiAccount;

public class Testaccount {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/2.txt");
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
		Elements elementsByTag = doc.getElementsByTag("tr");
		HousingFundLinYiAccount h = null;
		List<HousingFundLinYiAccount> list = new ArrayList<HousingFundLinYiAccount>();
		for (int i = 2; i < elementsByTag.size()-1; i++) {
			if(elementsByTag.get(i).getElementsByTag("div").size()==6)
			{
				h = new HousingFundLinYiAccount();
				//System.out.println(elementsByTag.get(i).getElementsByTag("div").get(1).text());
				h.setIdNum(elementsByTag.get(i).getElementsByTag("div").get(0).text());
				h.setFundNum(elementsByTag.get(i).getElementsByTag("div").get(1).text());
				h.setDatea(elementsByTag.get(i).getElementsByTag("div").get(2).text());
				h.setDescc(elementsByTag.get(i).getElementsByTag("div").get(3).text());
				h.setMoney(elementsByTag.get(i).getElementsByTag("div").get(4).text());
				h.setInfo(elementsByTag.get(i).getElementsByTag("div").get(5).text());
			}
			list.add(h);
		}
		System.out.println(list);
	}
}
