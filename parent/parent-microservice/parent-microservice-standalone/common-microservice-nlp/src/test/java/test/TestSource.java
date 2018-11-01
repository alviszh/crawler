package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestSource {
	
	public static void main(String[] args) {
		
		String[] strs = {"发布者: 陈剑锐 | 原作者: 陈尔冬 来自: 读懂新金融 | 发布时间: 2018-4-16 12:09 |浏览量：208",
				"发布时间: 2018-4-11 11:11 |浏览量：872 | 评论: 4",
				"发布：股城理财","大河客户端","2018-04-12 00:00:00      中国经济导报","钛媒体APP 评分：7分"
				,"2018年04月19日 12:04  来源：重庆网络广播电视台-财经新闻 ","2018/1/22 15:44:31  文章来源：新华社  作者：佚名 ",
				"中国证监会 www.csrc.gov.cn 时间：2018-03-30 来源：证监会"
				};
				
		//来自:\s{0,1}[\u4e00-\u9fa5]{1,9}|来源：\s{0,1}[\u4e00-\u9fa5]{1,9}|发布：\s{0,1}[\u4e00-\u9fa5]{1,9}
		String regex1 = "来自(：|:)\\s{0,1}[\u4e00-\u9fa5]{1,9}|来源(：|:)\\s{0,1}[\u4e00-\u9fa5]{1,9}|发布(：|:)\\s{0,1}[\u4e00-\u9fa5]{1,9}";
		//[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}			//2018-04-18 17:04
		String regex2 = "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}(:[0-9]{1,2})*";
		//评分：7分
		String regex3 = "评分：[0-9]{1,2}分";
		
		for(String str : strs){
			Matcher m1 = Pattern.compile(regex1).matcher(str);
			if(m1.find()){
				System.out.println(str+"---------"+m1.group());
			}else if(str.contains("|")){
				System.out.println(str+"---------"+"没有来源");
			}else{
				String aa = str.replaceAll(regex2, "");
				String finalStr = aa.replaceAll(regex3, "").trim();
				System.out.println(str+"---------"+finalStr);
			}
		}
		
	}

}
