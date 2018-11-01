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

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiScore;

public class TestScore {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/anpoint.txt");
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
		Element element = doc.getElementsByTag("tbody").get(1);
		Elements elements = element.getElementsByTag("td");
		List list = new ArrayList();
		TelecomAnhuiScore telecomAnhuiScore=null;
		for (int i = 0; i < elements.size();i=i+3) {
			for (int j = 0; j < elements.size(); j++) {
				telecomAnhuiScore = new TelecomAnhuiScore();
				telecomAnhuiScore.setMonth(elements.get(i).text());
				telecomAnhuiScore.setStatus(elements.get(i+1).text());
				telecomAnhuiScore.setNewScore(elements.get(i+2).text());
			}
			list.add(telecomAnhuiScore);
		}
		System.out.println(list);
		
	}
}
