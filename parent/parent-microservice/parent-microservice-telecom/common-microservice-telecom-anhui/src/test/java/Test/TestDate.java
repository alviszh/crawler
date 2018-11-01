package Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TestDate {

public static void main(String[] args) throws Exception {
	//System.out.println(Math.random());
	System.out.println(getFirstDay("yyyy-MM-dd",0)); 
	System.out.println(getLastDay("yyyy-MM-dd", 0));
	
	//UUID uuid=UUID.randomUUID();  
	//System.out.println(uuid);
}
//43f24fa4cb0242a6ac9c54a57ad3fc65bc589ac3e87118a3414f87aea618014ba2f779fa25338bc5775e002a9237a18bcc156977943d2f1c525565bed5823954

//获取第一天
	public static String getFirstDay(String fmt,int i) throws Exception{
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
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
