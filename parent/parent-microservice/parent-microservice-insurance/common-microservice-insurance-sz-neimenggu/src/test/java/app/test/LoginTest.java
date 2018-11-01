 package app.test;
/**
 * @description:   内蒙古自治区的验证码输入错误，有时候也能登录成功，但是为了保险起见，还是让其识别验证码
 * @author: sln 
 * @date: 2017年12月8日 上午11:17:09 
 */

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
public class LoginTest {
	public static void main(String[] args) {
		try {
			String url="http://login.12333k.cn/Cas/login?service=http%3A%2F%2Fcard.12333k.cn%2Fsiweb%2Flogin.do%3Fmethod%3Dbegin";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000);  //网站本身原因，登录页面的加载需要费些时间
			if(null!=hPage){
				String html=hPage.asXml();
				Document doc = Jsoup.parse(html);  //获取隐藏域的三个参数
//				String lt = doc.getElementsByTag("input[name='lt']").get(0).val();
				String lt =doc.getElementsByAttributeValue("name", "lt").get(0).val();
				System.out.println("获取到的lt是："+lt);
				String eventId =doc.getElementsByAttributeValue("name", "_eventId").get(0).val();
				System.out.println("获取到的lt是："+eventId);
				String captchaId =doc.getElementsByAttributeValue("name", "captchaId").get(0).val();
				System.out.println("获取到的lt是："+captchaId);
				
				HtmlImage image = hPage.getFirstByXPath("//img[@id='jcaptcha']");
				String imageName = "111.jpg"; 
				File file = new File("D:\\img\\"+imageName); 
				image.saveAs(file); 	
//				HtmlTextInput username = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='username']"); 
//				HtmlPasswordInput password = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@id='password']");
//				HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='checkCode']");
//				HtmlImage button = (HtmlImage)hPage.getFirstByXPath("//img[@id='loginButton']");			
//				username.setText("152801199007175029");
//				password.setText("199007175029");
//				String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
//				validateCode.setText(inputValue); 	
//				Page page = button.click();
				//用验证链接验证登录信息的正确性
				url="http://login.12333k.cn/Cas/login?service=http%3A%2F%2Fcard.12333k.cn%2Fsiweb%2Flogin.do%3Fmethod%3Dbegin";
				String requestBody=""
						+ "username=150428199203260120"
						+ "&password=nrty19920326"
//						+ "&checkCode="+inputValue+""
						+ "&checkCode=1234"
						+ "&lt="+lt+""
						+ "&_eventId="+eventId+""
						+ "&captchaId="+captchaId+"";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					System.out.println("验证登录返回的结果是："+html);
					if(html.contains("http://www.12333k.cn/ecdomain/framework/nmsbksite/jhhkokbbfoinbbobjplikhoddigkkkal.jsp")){
						System.out.println("登录成功，已经跳转到登录首页面");
						//爬取参保信息(有时候这个页面反应慢，无法加载出来，就会报错)
						url="http://app.12333s.cn/nmQuery/person/insuranceList.html";
						webClient.getOptions().setTimeout(20000);
						webRequest = new WebRequest(new URL(url), HttpMethod.GET);
						webRequest.setCharset(Charset.forName("GBK"));  //这个需要加上
						page=webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的参保信息的html是："+html);
						}
						
						
						//尝试获取用户基本信息
						url="http://card.12333k.cn/siweb/rpc.do?method=doQuery";
						requestBody="{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"\",pageNumber:1,pageSize:1,recordCount:0,rowSetName:\"nmcard.ccweb_ac01\"}},parameters:{\"synCount\":\"true\"}}}";
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						webRequest.setAdditionalHeader("Host", "card.12333k.cn");
						webRequest.setAdditionalHeader("Origin", "http://card.12333k.cn");
						webRequest.setAdditionalHeader("Referer", "http://card.12333k.cn/siweb/empmodify.do");
						webRequest.setAdditionalHeader("Content-Type", "application/json");   //这是必须加的请求头信息，否则无法返回数据
						webRequest.setRequestBody(requestBody);
						page=webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的用户基本信息的html是："+html);
						}
						
						
						//获取缴费信息(参数信息必须拼接在url后边，如果用webRequest.setRequestBody(requestBody)的方式，即使改变页码也无法获取指定页码的数据，都是获取的第一页的数据)
						//必须指定citycode,不然只能获取默认城市的数据（没必要指定查询区间范围，因为默认第一页返回的信息中已经说了共有多少条数据）
						//根据第一页获取到所有的citycode（参保地，用list集合存储）和总页码等参数
						url="http://app.12333s.cn/nmQuery/person/paySI_query.html?"
								+ "pageNo=1"
								+ "&pageSize="
//								+ "&aae003=200110"
//								+ "&baz001=201802"
								+ "&aae140=null";
//								+ "&citycode=150400";
//						url="http://app.12333s.cn/nmQuery/person/paySI_query.html";
//						requestBody=""
//								+ "pageNo=6"
//								+ "&pageSize="
//								+ "&aae003=200110"   //查询起始日期
//								+ "&baz001=201802"   //查询终止日期
//								+ "&aae140=null"    //为null代表的是任意类型的保险
//								+ "&citycode=150400";
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//						webRequest.setRequestBody(requestBody);
						page=webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的缴费信息的html是："+html);
							doc = Jsoup.parse(html);
							String text = doc.getElementById("citycode").text();
							System.out.println("获取的参保地点是："+text);
							String val = doc.getElementById("citycode").getElementsByTag("option").get(0).val();
							System.out.println("获取的参保地点的代号是："+val);
							
							String text2 = doc.getElementById("cont_1").getElementsByClass("tableDateBk").get(0).getElementsByTag("tr").get(0)
									.getElementsByTag("td").get(0).getElementsByTag("table").get(0).getElementsByTag("tr").get(0).getElementsByTag("td").get(2).text();
							System.out.println("获取的总页码是："+text2);
							String totalPage = text2.split("共")[1].split("页")[0];
							System.out.println("获取的总页数是："+totalPage);
							
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("出现了异常"+e.toString());
		}
	}
}
