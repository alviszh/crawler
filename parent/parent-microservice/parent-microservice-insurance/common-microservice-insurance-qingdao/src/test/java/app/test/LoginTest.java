package app.test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
			String url="http://12333.qingdao.gov.cn/grcx2/pages/login_zg.jsp";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				String html=hPage.asXml();
				HtmlImage image = hPage.getFirstByXPath("//img[@id='validationCode']");
				String imageName = "111.jpg"; 
				File file = new File("D:\\img\\"+imageName); 
				image.saveAs(file); 	
				String code = JOptionPane.showInputDialog("请输入验证码……"); 
//				用如下链接验证相关信息输入是否正确
				//先验证图片验证码是否正确
				url="http://12333.qingdao.gov.cn/grcx2/validationCode/equalsCode.action";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody="checkCode="+code+"";
				webRequest.setRequestBody(requestBody);
				Page page=webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					if(html.contains("success")){
						//图片验证码验证成功的前提下，验证登录信息的正确性
						url="http://12333.qingdao.gov.cn/grcx2/login.action";
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						//en520717
						requestBody="loginname=370202199310305423"
								+ "&password="+DigestUtils.md5Hex("en520717")+""
								+ "&pid=1001&checkCode="+code+"";
						webRequest.setRequestBody(requestBody);
						page=webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
//							System.out.println("登陆成功的页面响应是："+html);
							if(html.contains("个人查询首页")){ //登陆成功之后页面中会包含
								System.out.println("登陆成功");
								//尝试获取养老信息源码页的内容，测试可否通过改变参数，一次性爬取（经过测试，可以一次性爬取）
								try {
									url = "http://12333.qingdao.gov.cn/grcx2/work/f10010302/loadAgedPay.action";
									webRequest = new WebRequest(new URL(url), HttpMethod.POST);
									String requstBody="pageIndex=0&pageSize=300&sortField=&sortOrder=";
									webRequest.setRequestBody(requstBody);
									page = webClient.getPage(webRequest);
									if(null!=page){
										html = page.getWebResponse().getContentAsString();
										System.out.println("获取的养老信息源码页是："+html);
									}
								} catch (Exception e) {
									System.out.println("获取养老信息出现异常："+e.toString());
								}
							}else {
								Document doc = Jsoup.parse(html);
								Elements errObj = doc.select("[color=red]");  //此方法可定位
//								Elements errObj = doc.select("font[color]");  //此方法亦可定位
								if(errObj!=null){
									String errMsg =errObj.text();
									System.out.println("登陆失败，提示的错误信息是："+errMsg);
								}else{
									System.out.println("出现了其他登陆错误信息");
								}
							}
						}
					}else{ 
						System.out.println("图片验证码识别错误");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("出现了异常"+e.toString());
		}
	}
}
