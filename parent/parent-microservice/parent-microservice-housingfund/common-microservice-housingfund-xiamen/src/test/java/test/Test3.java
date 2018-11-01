package test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Test3 {

	public static void main(String[] args) throws IOException {
		String html = FileUtils.readFileToString(new File("C:\\Users\\lenovo\\Desktop\\111.html"),"UTF-8");
		System.out.println(html);
	
		JSONArray listArray=JSONArray.fromObject(html);  
//		{"drawReason":"0","centDealDate":"20170928","acctType":"01","creditAmt":150,"bankAcctDate":"2017年09月27日",
//			"acDetailpId":436171034,"voucherNo":"050101000822","fixWeit":1600703.85,"centDetailKmh":"2010101",
//			"debitAmt":0,"bankSerno":25892,"creditSaveAmt":150,"debitFixAmt":0,"bankSumy":"汇缴","custAcct":"10036589067",
//			"bal":4835.49,"compAcct":"5300010205000701617","saveBal":450,"saveWeit":137700,
//			"bankSumyCode":"001","balaFlag":"1","acctKind":"1","centSumy":"汇缴","centAddFlag":"1","bankCode":"0501",
//			"voucherType":"","fixBal":4385.49,"debitSaveAmt":0,"centSumyCode":"001","subCent":"01","creditFixAmt":0,"cd":"2"}
		for (int i = 0; i < listArray.size(); i++) {
			 JSONObject listArrayObjs = JSONObject.fromObject(listArray.get(i));
			 String bankAcctDate=listArrayObjs.getString("MSG");
			 System.out.println(bankAcctDate);
		
		}
		
		
	}

}
