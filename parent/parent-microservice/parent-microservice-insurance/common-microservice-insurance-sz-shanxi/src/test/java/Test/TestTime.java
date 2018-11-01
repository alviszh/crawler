package Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestTime {

	public static void main(String[] args) throws Exception {
		String dateBefore = getDateBefore("yyyy-MM");
		System.out.println(dateBefore);
	}
	
	
	
	
	
	public static String getDateBefore(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
}
