package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test {

	public static void main(String[] args) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -0);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
			// 获取当前的上一年
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -3);
			String beforeMonth1 = df1.format(c1.getTime());
			System.out.println(beforeMonth1);

	}

}
