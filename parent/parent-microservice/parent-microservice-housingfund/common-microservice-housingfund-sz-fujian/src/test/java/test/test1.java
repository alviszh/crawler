package test;

import java.util.Base64;
import java.util.Calendar;

public class test1 {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-15;
		int yue = now.get(Calendar.MONTH) +1;
		int ri = now.get(Calendar.DAY_OF_MONTH);
		String yu = null;
		String r = null;
		if (yue>9){
			yu = String.valueOf(yue);
		}else{
			yu = "0"+yue;
		}
		if (ri>9){
			r = String.valueOf(ri);
		}else{
			r = "0"+ri;
		}
		String endTime = String.valueOf(endNian)+"-"+ yu +"-" + r;
		String beginTime = beginNian +"-01-01";
		Base64.Encoder encoder = Base64.getEncoder();
		String text = beginTime;
		String text1 = endTime;
	    byte[] textByte = text.getBytes("UTF-8");
	    byte[] textByte1 = text1.getBytes("UTF-8");
		//编码
		String encodedText = encoder.encodeToString(textByte);
		String encodedText1 = encoder.encodeToString(textByte1);
		System.out.println(encodedText);
		System.out.println(encodedText1);
	}

}
