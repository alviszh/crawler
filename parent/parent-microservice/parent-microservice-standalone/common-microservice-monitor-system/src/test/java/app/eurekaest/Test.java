package app.eurekaest;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		String str="0=eurekaserver\n   1=housingfund-qiqihaer\n  2=housingfund-xian\n   3=housingfund-dongguan";
		String[] split = str.split("\n");
		for (String string : split) {
			//获取所有的微服务
			list.add(string.split("=")[1]);
		}
		System.out.println(list.size());
	}
}
