package app.test;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

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
		 
//		 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		 Calendar cal = Calendar.getInstance();
//		 cal.add(Calendar.YEAR, -2);
//		 cal.add(Calendar.DAY_OF_MONTH, +2);
//		 System.out.println("***********************");
//		 System.out.println(sdf.format(cal.getTime()));
		
		
		String str = "&pageUpList%5B0%5D.%24startRow_62="
				+ "&pageUpList%5B0%5D.%24startRow_83="
				+ "&pageUpList%5B0%5D.%24bizProNo=20180528000001487892"
				+ "&pageUpList%5B0%5D.%24nextPageLastDayBal=335.99"
				+ "&pageUpList%5B0%5D.%24endFlag=";
		
		
		for(int i = 2 ; i < 5 ; i++){
			String change = "pageUpList%5B"+(i-1)+"%5D";
			String param = str.replaceAll("pageUpList%5B\\d%5D", change);
			
			str+=param;
			System.out.println("当i = "+i+" 时 str为："+str);
		}
		  
	}

}
