package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONObject;

public class Test5 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\555.html"), "UTF-8");
		if (null != html && html.contains("Balance")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			if (html.contains("Balance")) {
				String balanceAmount = jsonInsurObjs.getString("Balance");
				String specAmount = jsonInsurObjs.getString("specAmount");
				String commonAmount = jsonInsurObjs.getString("commonAmount");
				String realOweAmount = jsonInsurObjs.getString("realOweAmount");
				System.out.println(balanceAmount);
				System.out.println(specAmount);
				System.out.println(commonAmount);
				System.out.println(realOweAmount);
				
			}
	
		}
	}
		

}
