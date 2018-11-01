package Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Testt {

	public static void main(String[] args) throws Exception {
		String dateBefore = getDateBefore("yyyy", 3);
		System.out.println(dateBefore);
		
		String dateBefore1 = getDateBefore1("yyyy");
		System.out.println(dateBefore1);
	}
	
	public static String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
//		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	public static String getDateBefore1(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
}
