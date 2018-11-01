package app.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

public class TestsmsRecordSplit {
	public static void main(String[] args) {
		String str="var s0={};var s1=[];var s2={};s0.ret=\"0\";s0.totalFee=\"0.10\";s0.acctName=\"\u664F\u56FD\u5E86\";s0.totalMonthFee=\"0.1\";s0.rowNum=\"1\";s0.cdmaMsgDetails=s1;s1[0]=s2;s2.fee=\"0.10\";s2.corresType=\"\";s2.dialing=\"18970922391\";s2.sendTime=\"2017/08/06 16:26:12\";s2.called=\"15270946749\";s2.roamState=\"\";dwr.engine._remoteHandleCallback('2','0',{'PAGE_SIZE':\"100\",'_msg':null,IP:\"123.126.87.171\",msg:null,'PAGE_INDEX':\"1\",'DETAILS_INFO':s0,flag:\"0\",'VALID_CODE':\"937494\",METHOD:\"QRY_DETAILS_BY_LOGIN_NBR\",MONTH:\"201708\",'DICT_CALL_TYPE':\"dwr\",'QUERY_TYPE':\"8\",CODE:\"0\"});";
		
		//判断有无数据
		if(str.contains("sendTime")){
			System.out.println("有记录可以查询");
		}
		//截取需要的信息
		int i=str.indexOf("fee");
		int j=str.lastIndexOf("called");
		String jieguo = str.substring(i-3,j+20);
		if(jieguo.startsWith(";")){   //将开头的那个;截取掉
			jieguo=jieguo.substring(1, jieguo.length());
		}
		System.out.println("经过判断的结果是："+jieguo);
		
		
		//统计总条数  
		String[] array = jieguo.split("called");
		int count=array.length-1;  
		System.out.println("统计出来的总记录数是："+count);

		//将每条记录的索引值放在list集合中
		List<Integer> list=new ArrayList<Integer>();
		for(int n=0;n<=count+5;n++){    //索引值要比总条数多,故此处加上15，保证可以获取完全
			if(jieguo.contains("s"+n+"")){
				list.add(n);
			}
		}
		
		System.out.println("获取到的list的集合的大小是："+list.size());   //这样做可以可以获取所有的索引值  ，还需要判断一共有多少条数据
		
		
		//转为json串
		jieguo="{"+jieguo+"}";
		System.out.println("加了中括号之后："+jieguo);
		JSONObject rr = JSONObject.fromObject(jieguo);
//		rr.getString("s"+m+".times_int");
		
		
		for (Integer index : list) {
			System.out.println(rr.getString("s"+index+".called"));
		}
	
		
	
				
			
	
		
//		String a=rr.getString("s"+i+".fee");
//		System.out.println(a);
		
		
//		
//		String jieguo1=jieguo.substring(0,jieguo.lastIndexOf(";"));  //切割掉最后一个；和s3
//		System.out.println("剩余的数据为："+jieguo1);
//		
//		String[] split = jieguo1.split(";");
//		for (String string : split) {
//			System.out.println("截取的键值对是："+string);
//		}
//		
//		jieguo1=jieguo1.replaceAll("\"", "");
//		
//		count(jieguo1,"fee");
//		
//		
//		
//		
//		
//		//========================================================
//		jieguo="{"+jieguo+"}";
//		jieguo.replaceAll("s", "\"s\"");
//		jieguo.replaceAll("e=", "e=\"\"");
//		System.out.println("换成json串的数据是："+jieguo);
//		
////		System.out.println("=================="+getCharacterPosition(jieguo1));
//		
//		
//	}
//	
//	
//	
//	public static void count(String string, String str) {
//		String[] array = string.split(str);
//		if (array != null)
//		{	
//		System.out.println("方法二 ---> 个数" + (array.length - 1));}
//	}
	
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
	
}
