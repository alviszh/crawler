package TestWap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBill;
import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBusiness;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class TestBill2 {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/bill2.txt");
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
		String substring = txt.substring(5);
		System.out.println(substring);
		JSONObject jsonObj = JSONObject.fromObject(substring);
		String payLog = jsonObj.getString("ResultMesg");
		Object obj = new JSONTokener(payLog).nextValue();
		TelecomAnhuiBill t = null;
		List<TelecomAnhuiBusiness> list = new ArrayList<TelecomAnhuiBusiness>();
		if (obj instanceof JSONObject) 
		{
			t = new TelecomAnhuiBill();
			JSONObject jsonObject = (JSONObject) obj;
			String SIXTH_LEVEL = jsonObject.getString("BillDetail");
			JSONObject fromObject = JSONObject.fromObject(SIXTH_LEVEL);
			String string = fromObject.getString("ContractRoot");
			
			JSONObject fromObject2 = JSONObject.fromObject(string);
			String string2 = fromObject2.getString("SvcCont");
			
			JSONObject fromObject3 = JSONObject.fromObject(string2);
			String string3 = fromObject3.getString("SOO");
			
			JSONArray fromObject4 = JSONArray.fromObject(string3);
			Object object = fromObject4.get(4);
			JSONObject fromObject5 = JSONObject.fromObject(object);
			String string4 = fromObject5.getString("FM_FEE_INFO");
			
			JSONArray fromObject6 = JSONArray.fromObject(string4);
			for (int i = 0; i < fromObject6.size(); i++) {
				if(fromObject6.get(i).toString().contains("小计")){
					JSONObject fromObject7 = JSONObject.fromObject(fromObject6.get(i));
					String string5 = fromObject7.getString("SIXTH_LEVEL");
					System.out.println(string5);
				}
				
			}
			
			
		} 
		else if (obj instanceof JSONArray) 
		{
			JSONArray jsonArray = (JSONArray) obj;
			for (Object object : jsonArray) {
				JSONObject jsonObject = JSONObject.fromObject(object);
				String SIXTH_LEVEL = jsonObject.getString("BillDetail");
				JSONObject fromObject = JSONObject.fromObject(SIXTH_LEVEL);
				String string = fromObject.getString("ContractRoot");
				
				JSONObject fromObject2 = JSONObject.fromObject(string);
				String string2 = fromObject2.getString("SvcCont");
				JSONObject fromObject3 = JSONObject.fromObject(string2);
				String string3 = fromObject3.getString("SOO");
				JSONArray fromObject4 = JSONArray.fromObject(string3);
				Object object1 = fromObject4.get(4);
				
				
				JSONObject fromObject5 = JSONObject.fromObject(object1);
				String string4 = fromObject5.getString("FM_FEE_INFO");
				System.out.println(string4);
				
			}
		}
	}
}
