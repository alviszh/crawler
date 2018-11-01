import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class test {

	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		LocalDate stardate = LocalDate.of(today.getYear(), today.getMonth(), 1);
		String monthint = stardate.getMonthValue() + "";
		if (monthint.length() < 2) {
			monthint = "0" + monthint;
		}
		String month =  stardate.getYear() + monthint;
		System.out.println(month);
	}
	
}
