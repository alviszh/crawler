package test.guiyang;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {

	public static void main(String[] args) {
		// 获取当前的年
		SimpleDateFormat df4 = new SimpleDateFormat("yyyyMM");
		Calendar c4 = Calendar.getInstance();
		c4.add(Calendar.YEAR, -0);
		String beforeMonth4 = df4.format(c4.getTime());
		System.out.println(beforeMonth4);
		// 获取当前的前3年
		SimpleDateFormat df14 = new SimpleDateFormat("yyyyMM");
		Calendar c14 = Calendar.getInstance();
		c14.add(Calendar.YEAR, -10);
		String beforeMonth14 = df14.format(c14.getTime());
		System.out.println(beforeMonth14);
	}

}
