package app.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiCallRecord;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;
//用这个测试类测通话和短信    外加处理字符串
public class TestCallRecordOldSuccess {
	public static void main(String[] args) { 
		try{
			String loginurl = "http://login.189.cn/web/login";
			WebClient webclientlogin = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			HtmlPage pagelogin = webclientlogin.getPage(webRequestlogin);
			Thread.sleep(2000);
			// 获取对应的输入框
			HtmlImage image = pagelogin.getFirstByXPath("//*[@id='imgCaptcha']");
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 	
			HtmlTextInput username = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtAccount']");
			HtmlTextInput yanzhengma = (HtmlTextInput) pagelogin.getFirstByXPath("//input[@id='txtCaptcha']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) pagelogin
					.getFirstByXPath("//input[@id='txtPassword']");
			HtmlElement button = (HtmlElement) pagelogin.getFirstByXPath("//a[@id='loginbtn']");
			username.setText("13317954690");
			passwordInput.setText("935861");
			
			String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
			yanzhengma.setText(inputValue); 	
			HtmlPage htmlpage = button.click();
			webclientlogin = htmlpage.getWebClient();
			String asXml = htmlpage.asXml();
			System.out.println("点击登录后的页面是：------------" + asXml);
			if (asXml.indexOf("登录失败") != -1) {
				System.out.println("登录失败！");
			} else {
				System.out.println("登录成功！");	
				
				String wdzlurl0 = "http://www.189.cn/dqmh/ssoLink.do?method=skip&platNo=10015&toStUrl=http://jx.189.cn/SsoAgent?returnPage=yecx";
				
				webclientlogin.getOptions().setJavaScriptEnabled(false);
				WebRequest webRequestwdzl0 = new WebRequest(new URL(wdzlurl0), HttpMethod.GET);
				HtmlPage wdzl0 = webclientlogin.getPage(webRequestwdzl0);
				
				
				System.out.println("初始化请求页面是："+wdzl0.asXml());
				webclientlogin = wdzl0.getWebClient();
				
				
				
				
				
				System.out.println("=====================111111111111==============================");
				String wdzlurl22 = "http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
				webclientlogin.getOptions().setJavaScriptEnabled(false);
				System.out.println("第一次请求验证码：============================");
				WebRequest webRequestwdzl22 = new WebRequest(new URL(wdzlurl22), HttpMethod.POST);
				  List<NameValuePair> params22 = new ArrayList<NameValuePair>();
				   params22.add(new NameValuePair("callCount", "1"));  
				   params22.add(new NameValuePair("page", "/service/bill/customerbill/index.jsp?bill=balance"));
				   params22.add(new NameValuePair("httpSessionId", ""));
				   params22.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC933862"));
				   params22.add(new NameValuePair("c0-scriptName", "Service"));
				   params22.add(new NameValuePair("c0-methodName", "excute"));
				   params22.add(new NameValuePair("c0-id", "0"));   //参数1
				   params22.add(new NameValuePair("c0-param0", "string:TWB_GET_MONTH_DETAIL_BILL_NEW"));   //参数2
				   params22.add(new NameValuePair("c0-param1", "boolean:false"));
				   params22.add(new NameValuePair("c0-e1", "string:myPage"));
				   params22.add(new NameValuePair("c0-e2", "string:myPage_table"));
				   params22.add(new NameValuePair("c0-e3", "string:TWB_GET_MONTH_DETAIL_BILL_NEW"));
				   params22.add(new NameValuePair("c0-e4", "boolean:false"));
				   params22.add(new NameValuePair("c0-e5", "string:15"));
				   params22.add(new NameValuePair("c0-e6", "string:1"));
				   params22.add(new NameValuePair("c0-e7", "null:null"));
				   params22.add(new NameValuePair("c0-e8", "boolean:false"));
				   params22.add(new NameValuePair("c0-e9", "null:null"));
				   params22.add(new NameValuePair("c0-e10", "string:-1"));
				   params22.add(new NameValuePair("c0-e11", "string:18970922391"));
				   params22.add(new NameValuePair("c0-e12", "string:0"));
				   params22.add(new NameValuePair("c0-e13", "string:201709"));
				   params22.add(new NameValuePair("c0-e14", "string:7"));   //发现类型8查的是短信  7是通话
				   params22.add(new NameValuePair("c0-e15", "string:10"));
				   params22.add(new NameValuePair("c0-e16", "string:1"));
				   params22.add(new NameValuePair("c0-e17", "string:1"));
				   params22.add(new NameValuePair("c0-e18", "string:"));
				   params22.add(new NameValuePair("c0-e19", "string:"));
				   params22.add(new NameValuePair("c0-e20", "string:yes"));
				   params22.add(new NameValuePair("c0-param2", "Object_Object:{div_id:reference:c0-e1, table_id:reference:c0-e2, func_id:reference:c0-e3, is_sql:reference:c0-e4, page_size:reference:c0-e5, page_index:reference:c0-e6, exp_excel:reference:c0-e7, hide_pager:reference:c0-e8, class_name:reference:c0-e9, area_code:reference:c0-e10, acc_nbr:reference:c0-e11, service_type:reference:c0-e12, inYearMonth:reference:c0-e13, queryContent:reference:c0-e14, deviceType:reference:c0-e15, sortingOrder:reference:c0-e16, write_order:reference:c0-e17, call_type:reference:c0-e18, search_date:reference:c0-e19, need_check_session:reference:c0-e20}"));
				   params22.add(new NameValuePair("batchId", "5"));
				   
				   webRequestwdzl22.setRequestParameters(params22);
				   webRequestwdzl22.setAdditionalHeader("Accept", "*/*");    
				   webRequestwdzl22.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
				   webRequestwdzl22.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
				   webRequestwdzl22.setAdditionalHeader("Connection", "keep-alive");      
//					webRequestwdzl2.setAdditionalHeader("Content-Length", "1166"); 
					webRequestwdzl22.setAdditionalHeader("Content-Type", "text/plain"); 
					webRequestwdzl22.setAdditionalHeader("Host", "jx.189.cn"); 
					webRequestwdzl22.setAdditionalHeader("Origin", "http://jx.189.cn");   
//					webRequestwdzl22.setAdditionalHeader("Referer", "http://jx.189.cn/2017/details.jsp"); 
					webRequestwdzl22.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
				HtmlPage wdzl22 = webclientlogin.getPage(webRequestwdzl22);
				webclientlogin=wdzl22.getWebClient();
				String html = wdzl22.getWebResponse().getContentAsString();
				System.out.println("请求发送验证码获取页面响应是："+html);
				
				List<TelecomJiangxiCallRecord> list=new ArrayList<TelecomJiangxiCallRecord>();
				TelecomJiangxiCallRecord telecomJiangxiCallRecord=null;
				if(html.contains("['times_int']")){  //包含说明至少有一条通话记录，否则没有通话记录
					//截取需要的信息
					int i=html.indexOf("['times_int']");
					int j=html.lastIndexOf("callType");
					String jieguo = html.substring(i-3,j+35);
					if(jieguo.startsWith(";")){   //将开头的那个;截取掉
						jieguo=jieguo.substring(1, jieguo.length());
					}
					//为转换为json格式做前期准备工作
					jieguo=jieguo.replaceAll("\\s*", "");
					jieguo=jieguo.replaceAll("\\[\\'", ".");
					jieguo=jieguo.replaceAll("\\'\\]", "");
					jieguo=jieguo.replaceAll(";", ",\"");
					jieguo=jieguo.replaceAll(":", "'");
					jieguo=jieguo.replaceAll("=", "\":");
					jieguo=jieguo.replaceAll("'", ":");
					//统计总条数  
					String[] array = jieguo.split("otherFee");
					int count=array.length-1;  
					//将每条记录的索引值放在list集合中
					List<Integer> indexList=new ArrayList<Integer>();
					for(int n=0;n<=count+15;n++){    //索引值要比总条数多,故此处加上15，保证可以获取完全
						if(jieguo.contains("s"+n+".otherFee")){  //此处需要加上后边的那个类型名称，不然会出现没有15，有151，但是indexList中存储了15的情况
							indexList.add(n);
						}
					}
					
					System.out.println("索引的集合是："+indexList);
					//转为json串
					jieguo="{\""+jieguo.trim()+"}";
//					jieguo="{"+jieguo+"}";
					
//					jieguo.replaceAll(" ", "");
//					jieguo.replaceAll("\n", "");
//					jieguo.replaceAll("\r\n", "");
					System.out.println("获取的json字符串是：");
					System.out.println(jieguo);
					JSONObject jsob = JSONObject.fromObject(jieguo);
					for (Integer index : indexList) {
						telecomJiangxiCallRecord=new TelecomJiangxiCallRecord();
						telecomJiangxiCallRecord.setBasicallcost(jsob.getString("s"+index+".otherFee"));
						telecomJiangxiCallRecord.setCallstartime(jsob.getString("s"+index+".callStartTime"));
						telecomJiangxiCallRecord.setCalltype(jsob.getString("s"+index+".callType"));
						telecomJiangxiCallRecord.setContactaddr(jsob.getString("s"+index+".callAddr"));
						telecomJiangxiCallRecord.setContactime(jsob.getString("s"+index+".times"));
						telecomJiangxiCallRecord.setContactype(jsob.getString("s"+index+".tonghuatype"));
						telecomJiangxiCallRecord.setLongwaycost(jsob.getString("s"+index+".longDistaFee"));
						telecomJiangxiCallRecord.setOthernum(jsob.getString("s"+index+".called"));
						telecomJiangxiCallRecord.setTotalcharge(jsob.getString("s"+index+".totalFee"));
						list.add(telecomJiangxiCallRecord);
					}
					
					System.out.println(list.toString());
					
				}
				
				
				
//				String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
//				String validCode=inputValue;
//				
//				
//				
//				  String url3="http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
//				   WebRequest webRequestwdzl3 = new WebRequest(new URL(url3), HttpMethod.POST);
////					String payload="callCount=1&page=/service/bill/qryPayHistory.jsp&httpSessionId=&scriptSessionId=AA0CBE9FB90164F9E0E55CF74FCC9338294&c0-scriptName=Service&c0-methodName=excute&c0-id=0&c0-param0=string:TWB_GET_RECENT_BILL&c0-param1=boolean:false&c0-e1=string:myPage&c0-e2=string:myPage_table&c0-e3=string:TWB_GET_RECENT_BILL&c0-e4=boolean:false&c0-e5=string:15&c0-e6=string:1&c0-e7=null:null&c0-e8=boolean:false&c0-e9=null:null&c0-e10=string:-1&c0-e11=string:18970922391&c0-e12=string:80000045&c0-e13=string:A&c0-e14=string:-1&c0-e15=string:10&c0-e16=string:201708&c0-e17=string:1&c0-e18=string:1&c0-e19=string:yes&c0-param2=Object_Object:{div_id:reference:c0-e1, table_id:reference:c0-e2, func_id:reference:c0-e3, is_sql:reference:c0-e4, page_size:reference:c0-e5, page_index:reference:c0-e6, exp_excel:reference:c0-e7, hide_pager:reference:c0-e8, class_name:reference:c0-e9, area_code:reference:c0-e10, acc_nbr:reference:c0-e11, product_id:reference:c0-e12, payment_mode:reference:c0-e13, service_type:reference:c0-e14, deviceType:reference:c0-e15, inYearMonth:reference:c0-e16, nOption:reference:c0-e17, write_order:reference:c0-e18, need_check_session:reference:c0-e19}&batchId=2";
//					
//				   
//				   
//				   List<NameValuePair> params3 = new ArrayList<NameValuePair>();
//				   params3.add(new NameValuePair("callCount", "1"));  
//				   params3.add(new NameValuePair("page", "/2017/details.jsp"));
//				   params3.add(new NameValuePair("httpSessionId", ""));
//				   params3.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338823"));
//				   params3.add(new NameValuePair("c0-scriptName", "Service"));
//				   params3.add(new NameValuePair("c0-methodName", "excute"));
//				   params3.add(new NameValuePair("c0-id", "0"));   //参数1
//				   params3.add(new NameValuePair("c0-param0", "string:DETAILS_SERVICE"));   //参数2
//				   params3.add(new NameValuePair("c0-param1", "boolean:false"));
//				   params3.add(new NameValuePair("c0-e1", "string:201709"));
//				   params3.add(new NameValuePair("c0-e2", "string:7"));
//				   params3.add(new NameValuePair("c0-e3", "string:"+validCode+""));
//				   params3.add(new NameValuePair("c0-e4", "string:QRY_DETAILS_BY_LOGIN_NBR"));
//				
//				   params3.add(new NameValuePair("c0-param2", "Object_Object:{month:reference:c0-e1, query_type:reference:c0-e2, valid_code:reference:c0-e3, method:reference:c0-e4}"));
//				   params3.add(new NameValuePair("batchId", "3"));
//				   
//				    webRequestwdzl3.setRequestParameters(params3);
////					webRequestwdzl2.setRequestBody(payload);
//				    webRequestwdzl3.setAdditionalHeader("Accept", "*/*");    
//					webRequestwdzl3.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
//					webRequestwdzl3.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
//					webRequestwdzl3.setAdditionalHeader("Connection", "keep-alive");      
////					webRequestwdzl2.setAdditionalHeader("Content-Length", "1166"); 
//					webRequestwdzl3.setAdditionalHeader("Content-Type", "text/plain"); 
//					webRequestwdzl3.setAdditionalHeader("Host", "jx.189.cn"); 
//					webRequestwdzl3.setAdditionalHeader("Origin", "http://jx.189.cn");   
//					webRequestwdzl3.setAdditionalHeader("Referer", "http://jx.189.cn/2017/details.jsp"); 
//					webRequestwdzl3.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
////					webRequestwdzl2.setAdditionalHeader("Cookie", "lvid=8df5b37ee7ed60bbf117544226e69922; nvid=1; svid=ED13D30298944A80; bdshare_firstime=1505543664093; td_cookie=18446744072833764339; cn_1260051947_dplus=%7B%22distinct_id%22%3A%20%2215e99048e866bf-0d03549370adfb-3a3e5f04-100200-15e99048e87bbf%22%2C%22sp%22%3A%20%7B%22%24_sessionid%22%3A%200%2C%22%24_sessionTime%22%3A%201505805770%2C%22%24dp%22%3A%200%2C%22%24_sessionPVTime%22%3A%201505805770%7D%7D; UM_distinctid=15e99048e866bf-0d03549370adfb-3a3e5f04-100200-15e99048e87bbf; _pk_id.345.9df4=b864ac02a89790c3.1505557507.3.1505993125.1505702001.; flag=2; trkintaid=jt-sy-hxfw-01-; NETJSESSIONID1=QjJnZGwVfnWy5xmL0Kzv3dBnjlqPTgTxFjtvShFzhTS1BxzYvyQn!-521135661; aactgsh111220=18970922391; userId=201%7C149214171; isLogin=logined; .ybtj.189.cn=F5AD2371E8E4ECB33C7F8BDB588A9982; loginStatus=logined; trkHmCity=0; trkHmLinks=0; s_cc=true; _pk_ref.345.1592=%5B%22%22%2C%22%22%2C1506046028%2C%22http%3A%2F%2Fwww.189.cn%2Fjx%2Fservice%2F%22%2C%220%22%5D; trkHmCitycode=0; trkHmCoords=0; trkHmPageName=0; trkHmClickCoords=0; s_fid=47B4FE5DB9F1D9B4-04B8830129446249; trkId=D2B48367-143E-4A2F-B230-45B9B81AB69E; s_sq=%5B%5BB%5D%5D; Hm_lvt_4ae12616aa0a873fc63cbdccf4a2e47a=1506046028,1506046041,1506046192,1506046707; Hm_lpvt_4ae12616aa0a873fc63cbdccf4a2e47a=1506046707; cityCode=jx; SHOPID_COOKIEID=10015; _pk_id.345.1592=75f8c33fbe73d696.1505803963.19.1506046707.1506044222.; _pk_ses.345.1592=*"); 
////					webRequestwdzl2.setCharset(Charset.forName("UTF-8"));
//					HtmlPage wdzl3 = webclientlogin.getPage(webRequestwdzl3);
//					String zuizhognyanzhegn = wdzl3.getWebResponse().getContentAsString();
//					System.out.println("获取的经过验证后的页面是："+zuizhognyanzhegn);
//				
			}
		}catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}
}
