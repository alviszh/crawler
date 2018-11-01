package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test1 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\test2.html"),"UTF-8");
		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String codeStr = list1ArrayObjs.getString("code");
		if ("0".endsWith(codeStr)) {
			String listStr = list1ArrayObjs.getString("dataset");			
			JSONObject dateObjs = JSONObject.fromObject(listStr);
			String dataStr=dateObjs.getString("rows");
			System.out.println(dataStr);
			JSONArray listArray = JSONArray.fromObject(dataStr);
			JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(0));
			String  name=listArrayObjs.getString("DWMC");
			System.out.println(name);
		}
		
	}

}
