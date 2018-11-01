package app.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

public class TestCallRecordSplit {
	public static void main(String[] args) {
		String str=";s1[149]=s151;s2['times_int']=\"139\";s2.otherFee=\"0.00\";s2.callStartTime=\"2017/09/01 10:44:31\";s2.roamFee=\"0.00\";s2.longDistaFee=\"0.00\";s2.totalFee=\"0.0\";s2.callAddr=\"\u5357\u660C\";s2.dialing=\"18970922391\";s2.times=\"2\u5206\u949F19\u79D2\";s2.privilege=\"0.00\";s2.called=\"18720907236\";s2.tonghuatype=\"\u56FD\u5185\u901A\u8BDD\";s2.callType=\"\u88AB\u53EB\u547C\u5165\";s3['times_int']=\"132\";s3.otherFee=\"0.00\";s3.callStartTime=\"2017/09/01 16:03:05\";s3.roamFee=\"0.00\";s3.longDistaFee=\"0.00\";s3.totalFee=\"0.0\";s3.callAddr=\"\u5357\u660C\";s3.dialing=\"18970922391\";s3.times=\"2\u5206\u949F12\u79D2\";s3.privilege=\"0.00\";s3.called=\"13437112914\";s3.tonghuatype=\"\u56FD\u5185\u901A\u8BDD\";s3.callType=\"\u88AB\u53EB\u547C\u5165\";s4['times_int']=\"12\";s4.otherFee=\"0.00\";s4.callStartTime=\"2017/09/01 16:13:31\";s4.roamFee=\"0.00\";s4.longDistaFee=\"0.00\";s4.totalFee=\"0.0\";s4.callAddr=\"\u5357\u660C\";s4.dialing=\"18970922391\";s4.times=\"12\u79D2\";s4.privilege=\"0.00\";s4.called=\"079182063079\";s4.tonghuatype=\"\u56FD\u5185\u901A\u8BDD\";s4.callType=\"\u88AB\u53EB\u547C\u5165\";"+
	"s16['page_size']=\"15\";s16['page_index']=\"1\";s16['data_list']=s278;s16['total_count']=\"273\";s16['page_count']=\"19\";s278[0]=s0;s278[1]=s1;s278[2]=s2;s278[3]=s3;s278[4]=s4;s278[5]=s5;s278[6]=s6;s278[7]=s7;s278[8]=s8;s278[9]=s9;s278[10]=s10;s278[11]=s11;s278[12]=s12;s278[13]=s13;s278[14]=s14;dwr.engine._remoteHandleCallback('6','0',{DEVICETYPE:\"10\",SORTINGORDER:\"1\",'AREA_CODE':\"-1\",'TWB_GET_MONTH_DETAIL_BILL':s15,'HIDE_PAGER':\"false\"";
		
		//判断有无数据
		if(str.contains("times_int")){
			System.out.println("有记录可以查询");
		}
		//截取需要的信息
		int i=str.indexOf("['times_int']");
		int j=str.lastIndexOf("callType");
		String jieguo = str.substring(i-3,j+15);
		if(jieguo.startsWith(";")){   //将开头的那个;截取掉
			jieguo=jieguo.substring(1, jieguo.length());
		}
		System.out.println("经过判断的结果是："+jieguo);
		
		//为转换为json格式做前期准备工作
		jieguo=jieguo.replaceAll("\\['", ".");
		jieguo=jieguo.replaceAll("\\']", "");
		jieguo=jieguo.replaceAll(";", ",\"");
		jieguo=jieguo.replaceAll(":", "'");
		jieguo=jieguo.replaceAll("=", "\":");
		jieguo=jieguo.replaceAll("'", ":");
		
		
		System.out.println("替换后的结果为："+jieguo);
		//统计总条数  
		String[] array = jieguo.split("otherFee");
		int count=array.length-1;  
		System.out.println("统计出来的总记录数是："+count);

		//将每条记录的索引值放在list集合中
		List<Integer> list=new ArrayList<Integer>();
		for(int n=0;n<=count+15;n++){    //索引值要比总条数多,故此处加上15，保证可以获取完全
			if(jieguo.contains("s"+n+"")){
				list.add(n);
			}
		}
		
		System.out.println("获取到的list的集合的大小是："+list.size());   //这样做可以可以获取所有的索引值  ，还需要判断一共有多少条数据
		
		
		//转为json串
		jieguo="{\""+jieguo+"}";
		System.out.println("加了中括号之后："+jieguo);
		JSONObject rr = JSONObject.fromObject(jieguo);
//		rr.getString("s"+m+".times_int");
		rr.getString("s2.otherFee");
		System.out.println(rr.getString("s2.otherFee"));
		System.out.println(rr.getString("s"+2+".times_int"));
		
	
				
			
	
		
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
