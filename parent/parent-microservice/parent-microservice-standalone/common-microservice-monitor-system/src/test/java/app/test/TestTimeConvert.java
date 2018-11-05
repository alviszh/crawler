package app.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTimeConvert {
	public static void main(String[] args) {
		String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s="1541139809915";
        long lt = new Long(s);
        Date date = new Date(lt+8 * 60 * 60 * 1000);
        res = simpleDateFormat.format(date);
        System.out.println(res);
	}
}
