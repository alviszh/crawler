package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.microservice.dao.entity.crawler.housing.liaocheng.HousingLiaoChengPaydetails;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\5555.html"),"UTF-8");
		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String 	codeStr=list1ArrayObjs.getString("success");
		System.out.println(codeStr);
		if ("true".endsWith(codeStr)) {
			String listStr = list1ArrayObjs.getString("lists");
			JSONObject dateObjs = JSONObject.fromObject(listStr);
			System.out.println(dateObjs.toString());
			String dataStr=dateObjs.getString("dataList");
			System.out.println(dataStr);
			
			JSONObject listObjs = JSONObject.fromObject(dataStr);
			String  listObjsStr=listObjs.getString("list");
			JSONArray listArray = JSONArray.fromObject(listObjsStr);
			for (int i = 0; i < listArray.size(); i++) {
				 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
				  String acctime=listArrayObjs.getString("acctime");
				  System.out.println(acctime);
				
			}
		}	
	}

}
