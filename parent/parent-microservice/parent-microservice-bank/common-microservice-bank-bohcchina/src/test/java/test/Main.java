package test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// 字符串时间集合
		List<String> list = new ArrayList<String>();

		for (int k = 0; k < 12; k += 3) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, -k);
			String beforeMonth2 = df.format(c.getTime());
			list.add(beforeMonth2);

			Calendar c1 = Calendar.getInstance();
			int j1 = k + 3;
			c1.add(Calendar.MONTH, -j1);
			String beforeMonth1 = df.format(c1.getTime());
			String specifiedDayBefore = getSpecifiedDayBefore(beforeMonth1);
			if (k == 9) {
				Calendar c11 = Calendar.getInstance();
				c11.add(Calendar.YEAR, -1);
				String beforeMonth21 = df.format(c11.getTime());
				String specifiedDayBefore2 = getSpecifiedDayAfter(beforeMonth21);
				list.add(specifiedDayBefore2);
			} else {
				list.add(specifiedDayBefore);
			}
		}
		System.out.println(list.toString());
		for (int k = 0; k < 4; k++) {
			String BeginDate = "";
			String EndDate = "";
			if (k == 0) {
				// 2018-03-01----------2018-06-01
				BeginDate = list.get(2).toString().trim();
				EndDate = list.get(0).toString().trim();
			}
			if (k == 1) {
				// 2017-12-01----------2018-02-28
				BeginDate = list.get(4).toString().trim();
				EndDate = list.get(1).toString().trim();
			}
			if (k == 2) {
				// 2017-09-01----------2017-11-30
				BeginDate = list.get(6).toString().trim();
				EndDate = list.get(3).toString().trim();
			}
			if (k == 3) {
				// 2017-08-31----------2017-06-02
				BeginDate = list.get(7).toString().trim();
				EndDate = list.get(5).toString().trim();
			}
			System.out.println(BeginDate+"---------------"+EndDate);
		}
	}

	/**
	 * 获得指定日期的前一天
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

}
