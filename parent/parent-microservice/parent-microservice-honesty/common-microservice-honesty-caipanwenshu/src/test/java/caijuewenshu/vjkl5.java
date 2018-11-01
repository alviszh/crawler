package caijuewenshu;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class vjkl5 {

	public static void main(String[] args) {
//		String vjkl5 = "5a1dd9bff348dba014e1801a93e845a23864de5d";
//		double random = Math.random();
//		
//		random = random + 1;
//		
//		random = random * 0x10000;
//		
//		String dou_str = Double.toString(random).substring(1);
//		System.out.println(random);
	//	(((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
		
		//本月的第一天
		String plusDays = LocalDate.now().plusDays(-10).toString();
		
		System.out.println(plusDays);
	}
}
