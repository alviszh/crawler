package TestWap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBill;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiScore;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestScore {

		public static void main(String[] args) {
			String txt = null;
			try {
	            String encoding="UTF-8";
	            File file = new File("C:/Users/Administrator/Desktop/score.txt");
	            if(file.isFile() && file.exists()){ //判断文件是否存在
	                InputStreamReader read = new InputStreamReader(
	                new FileInputStream(file),encoding);//考虑到编码格式
	                BufferedReader bufferedReader = new BufferedReader(read);
	                String lineTxt = null;
	                while((lineTxt = bufferedReader.readLine()) != null){
	                    txt += lineTxt;
	                }
	                System.out.println(txt);
	                read.close();
	            }else{
	            	System.out.println("找不到指定的文件");
	            }
			}catch (Exception e) {
				System.out.println("读取文件内容出错");
				e.printStackTrace();
			}
			Document doc = Jsoup.parse(txt.substring(5));
			System.out.println(doc.text());
			String text = doc.text();
			TelecomAnhuiScore t = new TelecomAnhuiScore();
			if(doc.text().contains("成功"))
			{
				JSONObject fromObject = JSONObject.fromObject(text);
				String string = fromObject.getString("svcContArray");
				System.out.println(string);
				JSONArray fromObject2 = JSONArray.fromObject(string);
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(0));
				String month = fromObject3.getString("Month");
				String PointTypeName = fromObject3.getString("PointTypeName");
				String PointValue = fromObject3.getString("PointValue");
				t.setMonth(month);
				t.setStatus(PointTypeName);
				t.setNewScore(PointValue);
				t.setTaskid("");
				System.out.println(t);
			}
	}
}
