package test;

import java.util.Calendar;

public class Test {

	public static void main(String[] args){
		
	     System.out.println(System.currentTimeMillis());System.currentTimeMillis();
	 	Calendar calendar = Calendar.getInstance();
		for(int i=0;i<3;i++){
			String year = String.valueOf(calendar.get(Calendar.YEAR)-i);
			System.out.println(year);
		}
	}
}
