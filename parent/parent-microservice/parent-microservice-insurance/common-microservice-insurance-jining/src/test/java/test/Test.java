package test;

public abstract class Test {

	public static void main(String[] args) {
		//onLogin('1.0.77','114','mainFrame.jsp?','1','')
		String s = "1.0.77";
		String s2 = "370829199207064927";
		s = s.replace(".", "");
		String hhz;
		hhz = "9853398" + s + "7291166723";
		int len = hhz.length();
		hhz = hhz.substring(6, 9) + s2.substring(4, 7) + hhz.substring(0, 4) + s2.substring(0, 4)
				+ hhz.substring(len - 5) + s2.substring(11, 15) + hhz.substring(3, 7) + s2.substring(11)
				+ hhz.substring(10, 14);
		System.out.println("系统版本处理之后的参数：" + hhz);
		System.out.println("系统版本处理之后的参数：" + "81029198533708667237064339870649277729");

	}

}
