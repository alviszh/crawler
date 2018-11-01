package TestWap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiPay;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Testpay {
	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/wappay.txt");
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
		JSONObject fromObject = JSONObject.fromObject(txt.substring(4));
		String string = fromObject.getString("ResultMsg");
		JSONObject fromObject2 = JSONObject.fromObject(string);
		String string2 = fromObject2.getString("payLog");
		JSONArray fromObject3 = JSONArray.fromObject(string2);
		System.out.println(fromObject3);
		TelecomAnhuiPay t = new TelecomAnhuiPay();
		for (int i = 0; i < fromObject3.size(); i++) {
			JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(i));
			t.setPayTime(fromObject4.getString("payDate"));
			t.setStatus(fromObject4.getString("payMethodName"));
			t.setGetMoney(fromObject4.getString("codeValue"));
			t.setOutMoney(fromObject4.getString("payAmountD"));
		}
	}
}
