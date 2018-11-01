package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppTest {
	public static void main(String[] args) {
		SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyy");
		Calendar calendar22 = Calendar.getInstance();
		calendar22.setTime(new Date());
		List<String> list22 = new ArrayList<String>();
		for (int j = 0; j <= 3; j++) {
			list22.add(dateFormat22.format(calendar22.getTime()));
			calendar22.set(Calendar.YEAR, calendar22.get(Calendar.YEAR) - 1);
		}
		for (int k = 0; k < list22.size(); k++) {
			String monthdate = list22.get(k);
			String year = Integer.parseInt(monthdate)+"";
			System.out.println(year);
		}
	}
}
