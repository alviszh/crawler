package app.test;

import java.util.ArrayList;
import java.util.List;

import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiSMSRecord;

import net.sf.json.JSONObject;

public class TestJiaoyan {
	public static void main(String[] args) {
//		String html="s203['times_int']=\"37\";s203.otherFee=\"0.00\";s203.callStartTime=\"2017/09/25 19:51:26\";s203.roamFee=\"0.00\";s203.longDistaFee=\"0.00\";s203.totalFee=\"0.0\";s203.callAddr=\"\u5357\u660C\";s203.dialing=\"18970922391\";s203.times=\"37\u79D2\";s203.privilege=\"0.00\";s203.called=\"18679154623\";s203.tonghuatype=\"\u56FD\u5185\u901A\u8BDD\";s203.callType=\"\u88AB\u53EB\u547C\u5165\";dwr.engine._remoteHandleCallback('3','0',{'PAGE_SIZE':\"100\",'_msg':null,IP:\"123.126.87.167\",msg:null,'PAGE_INDEX':\"1\",'DETAILS_INFO':s0,flag:\"0\",'VALID_CODE':\"426848\",METHOD:\"QRY_DETAILS_BY_LOGIN_NBR\",MONTH:\"201709\",'DICT_CALL_TYPE':\"dwr\",'QUERY_TYPE':\"7\",CODE:\"0\"});";
//		int i=html.indexOf("METHOD");
//		int j=html.lastIndexOf("}");
//		html=html.substring(i,j+1);
//		System.out.println(html);
//		html="{"+html;
//		System.out.println(html);
//		JSONObject json=JSONObject.fromObject(html);
//		String code = json.getString("CODE");
//		System.out.println(json.getString("CODE"));
//		if(code.equals("0")){
//			System.out.println("短信验证码校验OK");
//		}
		String html="throw 'allowScriptTagRemoting is false.';var s0={};var s1=[];var s2={};s0.ret=\"0\";s0.totalFee=\"0.10\";s0.acctName=\"\u664F\u56FD\u5E86\";s0.totalMonthFee=\"0.1\";s0.rowNum=\"1\";s0.cdmaMsgDetails=s1;s1[0]=s2;s2.fee=\"0.10\";s2.corresType=\"\";s2.dialing=\"18970922391\";s2.sendTime=\"2017/08/06 16:26:12\";s2.called=\"15270946749\";s2.roamState=\"\";dwr.engine._remoteHandleCallback('2','0',{'PAGE_SIZE':\"100\",'_msg':null,IP:\"123.126.87.167\",msg:null,'PAGE_INDEX':\"1\",'DETAILS_INFO':s0,flag:\"0\",'VALID_CODE':\"426848\",METHOD:\"QRY_DETAILS_BY_LOGIN_NBR\",MONTH:\"201708\",'DICT_CALL_TYPE':\"dwr\",'QUERY_TYPE':\"8\",CODE:\"0\"});";
		if(html.contains("sendTime")){
			//截取需要的信息
			int i=html.indexOf("fee");
			int j=html.lastIndexOf("called");
			String jieguo = html.substring(i-3,j+20);
			jieguo=jieguo.replaceAll("\\s*", "");   //放在此处替换，才能起作用
			if(jieguo.startsWith(";")){   //将开头的那个;截取掉
				jieguo=jieguo.substring(1, jieguo.length());
			}
			//统计总条数  
			String[] array = jieguo.split("called");
			int count=array.length-1;  
			//将每条记录的索引值放在list集合中
			List<Integer> indexList=new ArrayList<Integer>();
			for(int n=0;n<=count+5;n++){    //索引值要比总条数多,故此处加上5，保证可以获取完全
				if(jieguo.contains("s"+n+".called")){
					indexList.add(n);
				}
			}
			System.out.println(indexList);
			
			//转为json串
			jieguo="{"+jieguo+"}";
			JSONObject json = JSONObject.fromObject(jieguo);
			for (Integer index : indexList) {
				System.out.println(json.getString("s"+index+".called"));
				System.out.println(json.getString("s"+index+".sendTime"));
				System.out.println(json.getString("s"+index+".fee"));
				
			}
		}
	}
}
