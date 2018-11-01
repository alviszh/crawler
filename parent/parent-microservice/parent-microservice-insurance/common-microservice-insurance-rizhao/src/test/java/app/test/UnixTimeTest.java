package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年12月26日 上午11:25:48 
 */
public class UnixTimeTest {
	public static void main(String[] args) {
//		String timeStamp2Date = TimeStamp2Date("685209600", "yyyy-MM-dd HH:mm:ss");
		String timeStamp2Date = TimeStamp2Date("1422720000000", "yyyy-MM-dd");
		System.out.println("unix时间戳转换为北京时间是："+timeStamp2Date);
	}
	public static String TimeStamp2Date(String timestampString, String formats){    
//		  Long timestamp = Long.parseLong(timestampString)*1000;      日照社保中的生日时间戳已经*1000了
		  Long timestamp = Long.parseLong(timestampString);    
		  String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));    
		  return date;    
	} 
}
