/**
 * @description:
 * @author: sln 
 * @date: 2017年12月4日 下午3:57:06 
 */
public class Test {
	public static void main(String[] args) {
		String str="操作失败:进行身份校验时出错:密码错误,请检查!返回";
		String[] split = str.split(" ");
		System.out.println(split[0]);
	}
}
