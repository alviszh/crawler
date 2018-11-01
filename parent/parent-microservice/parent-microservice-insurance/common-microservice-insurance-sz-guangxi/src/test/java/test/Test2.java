package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test2 {

	public static void main(String[] args) throws Exception {
	
		for (int i = 0; i < 12; i++) {             
			System.out.println(getDateBefore("yyyyMM",i)); //12
		}
		System.out.println("============");
		for (int i = 12; i < 24; i++) {
			System.out.println(getDateBefore("yyyyMM",i));//24
		}
		System.out.println("============");
		for (int i = 24; i < 36; i++) {
			System.out.println(getDateBefore("yyyyMM",i));//36
		}
		System.out.println("============");
		for (int i = 36; i < 48; i++) {
			System.out.println(getDateBefore("yyyyMM",i));//48
		}
		System.out.println("============");
		for (int i = 48; i < 60; i++) {
			System.out.println(getDateBefore("yyyyMM",i)); //60
		}
	}
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -i);
        Date m = c.getTime();
        String mon = format.format(m);
        return mon;
	}
}
