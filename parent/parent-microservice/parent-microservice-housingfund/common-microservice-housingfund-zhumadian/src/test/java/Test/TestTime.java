package Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.annotations.GenerationTime;

public class TestTime {

	public static void main(String[] args) throws Exception {
		String dateBefore = getDateBefore("yyyy-MM-dd", 3);
		System.out.println(dateBefore);
		
		String time = getTime("yyyy-MM-dd");
		System.out.println(time);
	}
	
	// 当前时间
		public static String getTime(String fmt) {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);// 可以方便地修改日期格式
			String hehe = dateFormat.format(now);
			return hehe;
		}

		/*
		 * @Des 获取当前月 的前i个月的 时间
		 */
		public static String getDateBefore(String fmt, int i) throws Exception {
			SimpleDateFormat format = new SimpleDateFormat(fmt);
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.YEAR, -i);
			Date m = c.getTime();
			String mon = format.format(m);
			return mon;
		}
}
