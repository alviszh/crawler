package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test3 {
	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		System.out.println(html);
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对
		JsonArray jsonArray = object.get("root").getAsJsonArray();
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject subObject = jsonArray.get(i).getAsJsonObject();
			String areaName=subObject.get("地区名称").getAsString();
			String payTime=subObject.get("年月").getAsString();
			String companyName=subObject.get("单位名称").getAsString();
			String username=subObject.get("姓名").getAsString();
			String idnum=subObject.get("身份证号码").getAsString();
			String accountNum=subObject.get("个人账号").getAsString();
			String monthpay=subObject.get("月缴存额").getAsString();
			String personPay=subObject.get("个人缴存额").getAsString();
			String companyPay=subObject.get("单位缴存额").getAsString();
			System.out.println(payTime);
		}
		
	}

}
