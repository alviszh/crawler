package test;


/**
 * @description:
 * @author: sln 
 * @date: 2017年11月16日 下午4:55:21 
 */
public class LuanMa {
	public static void main(String[] args) throws Exception {
//		String par="ä¸»å¡";  //主卡
		String par="é™„å¡";  //
		
		String outStr = new String(par.getBytes("iso-8859-1"),"UTF-8"); 
		System.out.println(outStr);
	}
}
