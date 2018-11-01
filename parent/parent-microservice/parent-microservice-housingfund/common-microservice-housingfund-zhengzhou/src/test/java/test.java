
public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "单位信息：北京易才人力资源顾问有限公司驻郑州办事处";
		String account = str.substring(str.indexOf("：")+1);
		System.out.println(account);
	}

}
