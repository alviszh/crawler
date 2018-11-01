package test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONObject;

public class Test {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\333.html"),"UTF-8");
		
		if (html.contains("houseFund")) {
			JSONObject jsonObject = JSONObject.fromObject(html);				
			String userAccount = jsonObject.getString("houseFund");
			JSONObject accountObject = JSONObject.fromObject(userAccount);	
			String username=accountObject.getString("NAME");//姓名
			String gender=accountObject.getString("GENDER");//性别
			String idnum=accountObject.getString("IDCARD");// 身份证
			String state=accountObject.getString("ACCOUNTSTATE");//账户状态
			String unitaccount=accountObject.getString("UNITACCOUNT");// 单位公积金账号
			String personaccount=accountObject.getString("PERSONACCOUNT");// 个人公积金账号
			String monthpay=accountObject.getString("MONTHPAY");// 月缴额
			String lastaccountdate=accountObject.getString("LASTACCOUNTDATE");// 最近时间		
			String  creditramount=accountObject.getString("CREDITORAMOUNT");// 最新到账金额
			String  company=accountObject.getString("UNITNAME");//  公司名称
			String acountamount=accountObject.getString("ACCOUNT_BALANCE");	//公积金账户总金额	
			String  foundloan=accountObject.getString("PERSONBANKACCOUNT");//  公积金贷款情况
			
			String userAccount2 = jsonObject.getString("userFoundAccount");
			JSONObject accountObject2 = JSONObject.fromObject(userAccount2);				
			String accountbalance=accountObject2.getString("ACCOUNT_BALANCE");
		    System.out.println(accountbalance);
		}
	} 

}
