package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\777.html"),"UTF-8");
		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String listStr = list1ArrayObjs.getString("dataset");
		JSONObject dateObjs = JSONObject.fromObject(listStr);
		String dataStr=dateObjs.getString("rows");
		System.out.println(dataStr);
		JSONArray listArray = JSONArray.fromObject(dataStr);
		for (int i = 0; i < listArray.size(); i++) {
			 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
			 String month=listArrayObjs.getString("HJNY");
			 System.out.println(month);
		}
	}

}
