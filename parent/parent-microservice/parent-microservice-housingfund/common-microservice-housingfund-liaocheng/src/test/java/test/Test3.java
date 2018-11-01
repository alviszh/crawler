package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test3 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\444.html"),"UTF-8");
		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String listStr = list1ArrayObjs.getString("data");
		
		JSONArray listArray = JSONArray.fromObject(listStr);
		for (int i = 0; i < listArray.size(); i++) {
			 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
			 String month=listArrayObjs.getString("ace020");
			 System.out.println(month);
		}
	}

}
