package test1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test1 {

	public static void main(String[] args) throws Exception {
		String string = getDateBefore5();
		System.out.println(string);
	}
	
	public static String getDateBefore(int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -i);
        Date y = c.getTime();
        String year1 = format.format(y);
       
        c.setTime(new Date());
        c.add(Calendar.YEAR, -(i-1));
        Date y2 = c.getTime();
        String year2 = format.format(y2);
        return year1+"-"+year2;
	}
	
	//获取 距今 五年前 的后一天 的时间
	public static String getDateBefore5() throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -5);
        c.add(Calendar.DAY_OF_YEAR, +1);
        Date y = c.getTime();
        String year1 = format.format(y);
       
        return year1;
	}
}
