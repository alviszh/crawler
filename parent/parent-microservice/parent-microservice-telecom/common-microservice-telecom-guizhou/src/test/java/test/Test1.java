package test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Test1 {

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 6; i++){
			String month=getDateBefore("yyyyMM",i);
			System.out.println(month);
			String month2=getDateBefore2(i);
			System.out.println(month2);
		}
		
	}
	public static String getDateBefore2(int i) throws Exception {
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1).plusMonths(-i);
		LocalDate enddate = today.with(TemporalAdjusters.lastDayOfMonth()).plusMonths(-i);
		if (i == 0) {
			enddate = today;
		}
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month = stardate.getYear() + "年" + monthint + "月";
        return month;
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
//201804
//201803
//201802
//201801
//201712
//201711


