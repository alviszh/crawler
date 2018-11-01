package Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.crawler.telecom.anhui.TelecomAnhuiBusiness;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestBusiness {

	public static void main(String[] args) {
		String txt = null;
		try {
            String encoding="UTF-8";
            File file = new File("C:/Users/Administrator/Desktop/业务.txt");
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
		JSONObject fromObject = JSONObject.fromObject(txt.substring(5));
		List list = new ArrayList();
	     String string = fromObject.getString("mainMealList");
	     JSONArray fromObject2 = JSONArray.fromObject(string);
	     for (Object object : fromObject2) {
			JSONObject fromObject3 = JSONObject.fromObject(object);
			TelecomAnhuiBusiness telecomAnhuiBusiness = new TelecomAnhuiBusiness();
			telecomAnhuiBusiness.setName(fromObject3.getString("name"));
			telecomAnhuiBusiness.setStatus(fromObject3.getString("handStatus"));
			telecomAnhuiBusiness.setDoTime(fromObject3.getString("createDate"));
			telecomAnhuiBusiness.setDescr(fromObject3.getString("descr"));
			telecomAnhuiBusiness.setStartTime(fromObject3.getString("effDate"));
			telecomAnhuiBusiness.setEndTime(fromObject3.getString("expDate"));
			list.add(telecomAnhuiBusiness);
		}
		System.out.println(list);
	}
}
