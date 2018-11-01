package app.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
//		String aa="\u94F6\u884C\u8054\u7F51\u4EE3\u6536";
//		System.out.println("\u94F6\u884C\u8054\u7F51\u4EE3\u6536");
//		String bb=aa;
//		System.out.println(bb);
		
//		['page_size']
//		String str="中华人们共和国";
//		int i=str.indexOf("共和国");
//		System.out.println(i);
		
		//出现的位置： 
//		String str="s5[0]=s0;s5[1]=s1;s0.fee=\"100.00\"53453453425252534535['page_size']";
//		int i=str.indexOf("s0.fee");
//		System.out.println(i);
//		int j=str.indexOf("['page_size']");
//		System.out.println(j);
		String str="s5[0]=s0;s5[1]=s1;s0.fee=\"100.00\";s0.time=\"20170516 06:54:53\";s0.addr=\"\u7F51\u5385\";s0.mode=\"UVC\u5145\u503C\";s1.fee=\"100.00\";s1.time=\"20170506 07:09:59\";s1.addr=\"\u7F51\u5385\";s1.mode=\"\u94F6\u884C\u8054\u7F51\u4EE3\u6536\";s3['page_size']=\"15\";s3['page_index']=\"1\";s3['data_list']=s6;s3['total_count']=\"2\";s3['page_count']=\"1\";";
		int i=str.indexOf("s0.fee");
		System.out.println(i);
		int j=str.indexOf("['page_size']");
		System.out.println(j);
		
		
		
		
		String jieguo = str.substring(i,j);
		System.out.println(jieguo);
		
		String jieguo1=jieguo.substring(0,jieguo.lastIndexOf(";"));  //切割掉最后一个；和s3
		System.out.println("剩余的数据为："+jieguo1);
		
		String[] split = jieguo1.split(";");
		for (String string : split) {
			System.out.println("截取的键值对是："+string);
		}
		
		jieguo1=jieguo1.replaceAll("\"", "");
		
		count(jieguo1,"fee");
		
		
		
		
		
		//========================================================
		jieguo="{"+jieguo+"}";
		jieguo.replaceAll("s", "\"s\"");
		jieguo.replaceAll("e=", "e=\"\"");
		System.out.println("换成json串的数据是："+jieguo);
		
//		System.out.println("=================="+getCharacterPosition(jieguo1));
		
		
	}
	
	
	
	public static void count(String string, String str) {
		String[] array = string.split(str);
		if (array != null)
		{	
		System.out.println("方法二 ---> 个数" + (array.length - 1));}
	}
	
//	public static int getCharacterPosition(String string){
//	    //这里是获取"/"符号的位置
//	    Matcher slashMatcher = Pattern.compile(";").matcher(string);
//	    int mIdx = 0;
//	    while(slashMatcher.find()) {
//	       mIdx++;
//	       //当"/"符号第三次出现的位置
//	       if(mIdx == 3){
//	          break;
//	       }
//	    }
//	    return slashMatcher.start();
//	 }

	
	
}
