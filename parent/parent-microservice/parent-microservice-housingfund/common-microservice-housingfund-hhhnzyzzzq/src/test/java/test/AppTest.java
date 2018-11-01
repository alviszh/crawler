package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppTest {

	public static void main(String[] args) {
		// 获取当前的年
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -0);
		String beforeMonth = df.format(c.getTime());
		String[] split = beforeMonth.split("-");
		for (int i = 0; i < split.length; i++) {
			System.out.println(split[i]);
		}
		// 获取当前的前3年
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.YEAR, -3);
		String beforeMonth1 = df1.format(c1.getTime());
		String[] split1 = beforeMonth1.split("-");
		for (int i = 0; i < split1.length; i++) {
			System.out.println(split1[i]);
		}
	}
}
