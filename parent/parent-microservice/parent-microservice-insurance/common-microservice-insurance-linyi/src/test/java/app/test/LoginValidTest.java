package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2017年12月8日 上午11:17:09 
 */

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
/**
 * 如下登录方式可以达到登录成功且能准确提示错误信息的目的
 * @author sln
 *
 */
public class LoginValidTest {
	public static void main(String[] args) {
		try {
			String url="http://60.213.43.44/loginvalidate.html";    
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody="type=1"
					+ "&account=530326198811294920"
					+ "&password=zhq19881129";    //如下四个参数没有也可以登录成功
//					+ "&captcha=fxbc"    //该网站验证码输入错误也能登录成功
//					+ "&yzm=%E8%AF%B7%E8%BE%93%E5%85%A5%E8%81%94%E7%B3%BB%E5%87%BD%E5%8F%8A%E5%87%AD%E8%AF%81%E7%BC%96%E5%8F%B7"
//					+ "&tab2_type=%E8%AF%B7%E9%80%89%E6%8B%A9%E4%B8%9A%E5%8A%A1%E7%B1%BB%E5%9E%8B"
//					+ "&input3=%E8%AF%B7%E8%BE%93%E5%85%A5%E9%AA%8C%E8%AF%81%E7%A0%81";
			webRequest.setRequestBody(requestBody);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				System.out.println("登录页面html为："+html);
				if(html.contains("success")){
					System.out.println("登录成功！");
					//获取用户信息
//					url="http://60.213.43.44/person/personInfo.html";
//					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//					page = webClient.getPage(webRequest);
//					if(null!=page){
//						html=page.getWebResponse().getContentAsString();
//						System.out.println("获取的用户信息页面是："+html);
//					}
					
					System.out.println("============================================");
					
					//获取个人参保信息
					url="http://60.213.43.44/person/personCBInfo.html";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					page = webClient.getPage(webRequest);
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						System.out.println("获取的个人参保信息页面是："+html);
						
						Document doc = Jsoup.parse(html);
						//获取参保信息的总记录数
						String val = doc.getElementById("queryform1").getElementsByTag("table").get(0).getElementsByClass("page_bottom").get(0).getElementsByClass("page_num").get(0).getElementsByTag("input").val();
						System.out.println("获取的记录总数是："+val);
						
//						Elements elementsByTag =doc.getElementById("queryform1").getElementsByTag("table").get(0).getElementById("queryResult").getElementsByTag("tr").get(0).getElementsByTag("td").get(0).getElementsByClass("grid").get(0).getElementsByTag("tr");
//						Elements elementsByTag =doc.getElementById("queryform1").getElementsByTag("table").get(0).getElementById("queryResult").getElementsByClass("grid").get(0).getElementsByTag("tr");
						//如上两种写法也可以
						Elements elementsByTag =doc.getElementById("queryform1").getElementById("queryResult").getElementsByClass("grid").get(0).getElementsByTag("tr");
						for (Element element : elementsByTag) {
							System.out.println("===========");
							System.out.println(element.text());
						}

						
					}
					
					System.out.println("============================================");
					
					//获取个人缴费明细
					//每页10条记录 | 共283条记录 | 当前1/29页
					url="http://60.213.43.44/person/personJFJSInfo_result.html";     //不加请求参数，直接这样请求，默认获取的是第一页的数据，通过这个默认请求，得到总页数
					requestBody=""      
							+ "pageNo=1"
//							+ "&aae002bg="    //通过测试发现，这三个参数不要也可以
//							+ "&aae002ed="
//							+ "&aae140="
							+ "&datanum=10";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						System.out.println("获取的个人缴费明细信息页面是："+html);
						
						
						Document doc = Jsoup.parse(html);
						String text = doc.getElementById("queryform1").getElementsByTag("table").get(0).getElementsByClass("page_bottom").get(0).getElementsByClass("page_num").get(0).text();
						System.out.println("获取到的页码总数提示信息是："+text);
						String[] split = text.split("/");
						String totalPageCount=split[1];
						totalPageCount=totalPageCount.substring(0, totalPageCount.length()-1);
						System.out.println("最终获取到的总页数是："+totalPageCount);
					}
				}else if(html.contains("success1")){   //盘锦市特有
					System.out.println("登录成功，温馨提示：该单位已经欠缴两个月,请及时补缴,以免影响登录！！");
				}else{    //各种登录失败的情况
					if(html.contains("wrongaccount")){
						System.out.println("登录失败，错误原因：用户名不存在");
					}else if(html.contains("wrongpass")){
						System.out.println("登录失败，错误原因：密码和用户名不匹配");
					}else if(html.contains("allstop")){
						System.out.println("登录失败，错误原因：该人员已终止");
					}else if(html.contains("captchawrong")){
						System.out.println("登录失败，错误原因：验证码错误,请重新填写！");
					}else if(html.contains("captchaexpire")){
						System.out.println("登录失败，错误原因：验证码过期,请重新填写！");
					}else if(html.contains("isLocked")){
						System.out.println("登录失败，错误原因：该账号已被冻结，请联系管理员！");
					}else if(html.contains("isLocked1")){   //盘锦市特有
						System.out.println("登录失败，错误原因：该单位已经欠缴三个月,在补全欠费之前不能登录账户！");
					}else{   //其他登录错误原因(决定提示用户系统繁忙)
						System.out.println("登录失败，错误原因：系统繁忙，请稍后再试！");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("出现了异常"+e.toString());
		}
	}
}
