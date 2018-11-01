package app.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetTime
{
    public static void main(String[] args)
    {
//        Calendar calendar = Calendar.getInstance(Locale.CHINA);
//        Date date = calendar.getTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateString = dateFormat.format(date);
//        System.out.println(dateString);
    	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		//某月的第一天
		calendar.add(Calendar.MONTH, -5);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(format.format(calendar.getTime()));
    }
  //某个月的第一天
   
//  	public static String getFirstMonthdate(int i){
//  		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
//  		Calendar calendar=Calendar.getInstance();
//  		//某月的第一天
//  		calendar.add(Calendar.MONTH, -i);
//  		calendar.set(Calendar.DAY_OF_MONTH, 1);
//  		return format.format(calendar.getTime());
//  	}
}