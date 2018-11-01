package app.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: sln 
 * @date: 2017年12月14日 上午9:58:04 
 */
public class Test {
	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		list.add("2");
		list.add("1");
		list.add("");
		list.add("3");
		list.add("");
		list.add("5");
		for (String string : list) {
			System.out.println(string);
			if(string.equals("")){
				break;
			}
		}
	}
}
