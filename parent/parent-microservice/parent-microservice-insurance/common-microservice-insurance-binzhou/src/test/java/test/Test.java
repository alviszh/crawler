package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Test {

	public static void main(String[] args) {
		// 爬取解析养老保险
		for (int k = 0; k < 3; k++) {
			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -k);
			String beforeMonth = df.format(c.getTime());
			System.out.println(beforeMonth);
		}
	}

}




