package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2018年1月5日 下午4:00:18 
 */
public class LoginTest {
	public static void main(String[] args) {  //爬取医保信息的时候需用到个人编号，如下代码尝试截取
		String str="var aab001= \"2001225216\";var aae041= \"111111\";";
		str= str.substring(str.indexOf("aab001"), str.indexOf("aae041"));
		str=str.substring(str.indexOf("\"")+1, str.lastIndexOf("\""));
		System.out.println(str);
	}
}
