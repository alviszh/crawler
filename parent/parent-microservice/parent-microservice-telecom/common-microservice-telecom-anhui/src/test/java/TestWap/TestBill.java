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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TestBill{
	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/bill.txt");
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
		JSONObject fromObject = JSONObject.fromObject(text);
		System.out.println(fromObject);//resultMessage
		String string = fromObject.getString("resultMessage");
		System.out.println(string);//ContractRoot
		JSONObject fromObject2 = JSONObject.fromObject(string);
		System.out.println(fromObject2);
		String string2 = fromObject2.getString("ContractRoot");
		System.out.println(string2);
		JSONObject fromObject3 = JSONObject.fromObject(string2);
		String string3 = fromObject3.getString("SvcCont");
		System.out.println(string3);
		JSONObject fromObject4 = JSONObject.fromObject(string3);
		String string4 = fromObject4.getString("SOO");
		System.out.println(string4);
		JSONArray fromObject5 = JSONArray.fromObject(string4);
	    Object object = fromObject5.get(1);
		System.out.println(object);
		JSONObject fromObject6 = JSONObject.fromObject(object);
		String string5 = fromObject6.getString("INVOICE_ITEM_INFO");
		
		System.out.println(string5);
		JSONArray fromObject7 = JSONArray.fromObject(string5);
		TelecomAnhuiBill t = null;
		List<TelecomAnhuiBill> list = new ArrayList<TelecomAnhuiBill>();
		for (int i = 0; i < fromObject7.size(); i++) {
			
			t = new TelecomAnhuiBill(); 
			JSONObject fromObject8 = JSONObject.fromObject(fromObject7.get(i));
			String INVOICE_NAME = fromObject8.getString("INVOICE_NAME");
			String CHARGE_S = fromObject8.getString("CHARGE_S");
			String FORMAT_ITEM_STR = fromObject8.getString("FORMAT_ITEM_STR");
			t.setBillName(INVOICE_NAME);
			t.setMoney(CHARGE_S);
			t.setDesce(FORMAT_ITEM_STR);
			t.setMonth("");
			list.add(t);
		}
		System.out.println(list);
	}
	
	
	
	public static String txt2String(File file) { 
		StringBuilder result = new StringBuilder(); 
		try { 
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); 
		String s = null; 
		while ((s = br.readLine()) != null) { 
		result.append(System.lineSeparator() + s); 
		} 
		br.close(); 
		} catch (Exception e) { 
		e.printStackTrace(); 
		} 
		return result.toString(); 
		}

		
}
