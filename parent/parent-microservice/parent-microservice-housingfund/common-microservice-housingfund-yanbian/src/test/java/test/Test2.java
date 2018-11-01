package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Test2 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		System.out.println(html);
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(html); // 创建JsonObject对
		String success = object.get("success").getAsString();
		if ("true".equals(success)) {
			JsonObject data = object.get("data").getAsJsonObject();
			System.out.println(data);
			String username=data.get("姓名").getAsString();
			System.out.println(username);
			String idnum =data.get("身份证号码").getAsString();
			System.out.println(idnum);
			
			String accountNum=data.get("个人账号").getAsString();
			System.out.println(accountNum);
			String companyCode=data.get("单位代码").getAsString();
			System.out.println(companyCode);
			String sex=data.get("性别").getAsString();
			System.out.println(sex);
			
			String lastmonthpay=data.get("上年月平均工资额").getAsString();
			System.out.println(lastmonthpay);
			String monthpay=data.get("月应缴额").getAsString();
			System.out.println(monthpay);
			String balance=data.get("公积金余额").getAsString();
			System.out.println(balance);
			 String companyPay=data.get("单位缴存额").getAsString();			 
			 System.out.println(companyPay);
			 String personPay=data.get("个人缴存额").getAsString();
			 System.out.println(personPay);
			 String companyRatio=data.get("单位缴存比例").getAsString();
			 System.out.println(companyRatio);
			 String personRatio=data.get("个人缴存比例").getAsString();
			 System.out.println(personRatio);
			 String depositSituation=data.get("缴存情况").getAsString();
			 System.out.println(depositSituation);
			 String companyDepositSituation=data.get("单位缴存情况2").getAsString();
			 System.out.println(companyDepositSituation);
//			 String telephone=data.get("单位缴存情况2").getAsString();
//			 System.out.println(depositSituation);
			String openTime=data.get("开户时间").getAsString();
			 System.out.println(openTime);
				String lastPaytime=data.get("最后缴交时间").getAsString();
				 System.out.println(lastPaytime);
			String maxDefaultperiod=data.get("最大连续违约期数").getAsString();
			 System.out.println(maxDefaultperiod);
			String area=data.get("地区名").getAsString();
			 System.out.println(area);
			String companyName=data.get("单位名称").getAsString();
			 System.out.println(companyName);
		}
		
	}

}
