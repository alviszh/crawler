package app.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestStr {
	
	public static void main(String[] args) {
//		  String str = "showDetail('abd93563da127facf7428cd25f78e61e','6222600910027086121')";
//		  String[] strs = str.split(",");
//		  
//		 String aa = strs[0].substring(strs[0].indexOf("'")+1, strs[0].length()-1);
//		 
//		 System.out.println(aa);
//		 System.out.println(strs[1]);
//		 
//		 String bb = strs[1].replaceAll("'", "").replace(")", "");
//		 System.out.println(bb);
		 
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		 Calendar cal = Calendar.getInstance();
		 cal.add(Calendar.YEAR, -2);
		 cal.add(Calendar.DAY_OF_MONTH, +2);
		 System.out.println("***********************");
		 System.out.println(sdf.format(cal.getTime()));
		  
	}

}
