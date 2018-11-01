package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2018年1月10日 下午4:17:08 
 */
public class Test {
	public static void main(String[] args) {
		String str="<\\/td>123";
		str=str.replace("<\\/td>", "");
		System.out.println(str.length());
		System.out.println(str);
	}
}
