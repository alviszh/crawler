/**
 * 
 */
package app.test;

/**
 * @author sln
 * @date 2018年10月9日下午6:25:34
 * @Description: 
 */
public class ContainTest {
	public static void main(String[] args) {
		String str="欢迎您使用中国电信网上营业厅查询话费清单。短信随机码编号：QD[1002611090]，短信随机码是：334221";
		String substring = str.substring(str.length()-6, str.length());
		System.out.println(substring);
		
	}
}
