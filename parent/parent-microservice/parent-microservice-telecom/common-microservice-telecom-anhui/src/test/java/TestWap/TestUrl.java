package TestWap;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestUrl {

	public static void main(String[] args) throws Exception {
        for (int i = 5; i < 100; i=i+5) {
			System.out.println(i);
		}
	}
	
	public static String getFirstDay(String fmt,int i) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -i);  
        c.set(Calendar.DAY_OF_MONTH, 1);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}
	
	//获取最后天
		public static String getLastDay(String fmt,int i) throws Exception{
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -i+1);   
	        c.set(Calendar.DAY_OF_MONTH, 0);
			Date m = c.getTime();
			String mon = format.format(m);
			return mon;
		}
}
