package app.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.sf.json.JSONObject;

public class JSONTest {
	public static void main(String[] args) {
//		String aa="{s0.fee=\"100.00\";s0.time=\"20170516 06:54:53\";s0.addr=\"网厅\";s0.mode=\"UVC充值\";s1.fee=\"100.00\";s1.time=\"20170506 07:09:59\";s1.addr=\"网厅\";s1.mode=\"银行联网代收\"}";
//		int i=0;
//		JSONObject rr = JSONObject.fromObject(aa);
//		String a=rr.getString("s"+i+".fee");
//		System.out.println(a);
//		String aa="{\"s4.callStartTime\":\"2017/09/01 16'13'31\"}";
//		int i=0;
//		JSONObject rr = JSONObject.fromObject(aa);
//		String a=rr.getString("s4.callStartTime");
//		System.out.println(a);
		
//		Calendar a=Calendar.getInstance();
//		System.out.println(a.get(Calendar.YEAR));//得到年
//		
//		System.out.println(Calendar.getInstance().get(Calendar.YEAR));
		
//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
//		Calendar calendar = Calendar.getInstance();	
//		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
//		String nowYearMonth =dateFormat.format(calendar.getTime());
		
		
//		String haha="throw 'allowScriptTagRemoting is false.';//#DWR-INSERT//#DWR-REPLYvar s0=[];var s1={};s0[0]=s1;s1.reqSerial=\"982084479746\";s1.paymentAmount=\"1000\";s1.payChannelId=\"0\";s1.stateDate=\"20171011083304\";s1.balanceTypeName=\"\u4E13\u7528\u4F59\u989D\";s1.balanceTypeFlag=\"1\";s1.accNbrDetail=\"13317954690\";s1.paymentMethodName=\"\u5957\u9910\u4FC3\u9500\uFF08\u9884\u5B58\u3001\u8FD4\u8FD8\u3001\u8D60\u9001\uFF09\u8D39\u7528\";s1.payChannelName=\"\u8425\u4E1A\u5385\";s1.paymentMethod=\"16\";dwr.engine._remoteHandleCallback('7','0',{'QRY_TIME':\"2017-10-20 13:56:41\",flag:\"0\",'_msg':null,'PAGE_SIZE':\"100\",IP:\"123.126.87.169\",'PAYMENT_RECORD':s0,METHOD:\"QRY_PAYMENT_IN_MONTH_BY_LOGIN_NBR\",'DICT_CALL_TYPE':\"dwr\",MONTH:\"201710\",msg:null,'PAGE_INDEX':\"1\"});";
//		int a=haha.indexOf("reqSerial");
//		int b=haha.lastIndexOf("dwr.engine");
//		haha=haha.substring(a-3,b);
//		System.out.println(haha);
//		haha="{"+haha+"}";
//		String[] array = haha.split("reqSerial");
//		int count=array.length - 1;
//		System.out.println(count);
//		JSONObject rr = JSONObject.fromObject(haha);
//		
//		System.out.println(rr.getString("s1.reqSerial"));
		String haha="throw 'allowScriptTagRemoting is false.';//#DWR-INSERT//#DWR-REPLYdwr.engine._remoteHandleCallback('4','0',{flag:\"0\",'_msg':null,'PAGE_SIZE':\"100\",IP:\"123.126.87.169\",'CODE_DESC':\"\u672A\u67E5\u8BE2\u5230\u60A8\u7684\u9A8C\u8BC1\u53F7\u7801\u4FE1\u606F\",METHOD:\"SEND_SMS_CODE\",'DICT_CALL_TYPE':\"dwr\",msg:null,'PAGE_INDEX':\"1\",CODE:\"-9\"});";
		int a=haha.indexOf("{");
		int b=haha.lastIndexOf("}");
		haha=haha.substring(a,b+1);
		System.out.println(haha);
		JSONObject rr = JSONObject.fromObject(haha);
//		
		System.out.println(rr.getString("CODE_DESC"));
	}
	
}
