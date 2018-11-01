package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test4 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\test.html"),"UTF-8");
		JSONObject list1ArrayObjs = JSONObject.fromObject(html);
		String listStr=list1ArrayObjs.getString("list");
		System.out.println(listStr);
         JSONArray listArray=JSONArray.fromObject(listStr);
		 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(0));
		// [{"idNo":"350825199203104128","bankOrgName":"建行湖滨南支行","bal":4835.49,"lastAutoAcctDate":"2017年10月17日",
		 //"compName":"厦门智唯易才人力资源顾问有限公司","acctStatus":"0","custName":"邱*玲","openDate":"2014年12月23日",
		 //"aaa103":"厦门建设银行","custAcct":"10036589067","acPersonId":"233044978"}]

		 String custAcct=listArrayObjs.getString("custAcct");
		 String aaa103=listArrayObjs.getString("aaa103");
		 String custName=listArrayObjs.getString("custName");
		 String bankOrgName=listArrayObjs.getString("bankOrgName");
		 
		 String openDate=listArrayObjs.getString("openDate");
		 String compName=listArrayObjs.getString("compName");
		 String acctStatus=listArrayObjs.getString("acctStatus");
		 String bal=listArrayObjs.getString("bal");
		 System.out.println(custAcct);
		 System.out.println(aaa103);
		 System.out.println(custName);
		 System.out.println(bankOrgName);
		 
		 System.out.println(openDate);
		 System.out.println(compName);
		 System.out.println(acctStatus);
		 System.out.println(bal);
	}

}
