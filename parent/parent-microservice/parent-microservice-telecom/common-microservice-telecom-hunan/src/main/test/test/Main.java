package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
		Calendar calendar22 = Calendar.getInstance();
		calendar22.setTime(new Date());
		List<String> list22 = new ArrayList<String>();
		for (int j = 0; j <= 5; j++) {
			list22.add(dateFormat22.format(calendar22.getTime()));
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH) - 1);
		}
		for (int i = 0; i < list22.size(); i++) {
			String monthdate = list22.get(i);
			int year = Integer.parseInt(monthdate);
			System.out.println(year);
		}
	}
}
