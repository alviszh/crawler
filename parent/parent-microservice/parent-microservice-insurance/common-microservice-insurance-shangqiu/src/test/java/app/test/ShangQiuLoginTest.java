package app.test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
/**
 * 经过测试类测试，有些信息，例如用户信息，在解析的时候用标签定位的方式，不如用select的方式简单
 * @author sln
 *
 */
public class ShangQiuLoginTest {
	public static void main(String[] args) {
		try {
			//先请求链接登录链接，获取图片验证码对象
			String url="http://218.28.196.73:33002/siq/index.jsp?zoneCode=411400";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			HtmlPage hpage = webClient.getPage(webRequest);
			HtmlImage image = hpage.getFirstByXPath("//img[@id='perVaidImg']");   //个人登录方式的图片验证码
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 
			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			//需要先校验图片验证码，图片验证码的校验链接和登录账号密码的验证练级不一样
			url="http://218.28.196.73:33002/siq/pages/security/result.jsp?s=0.5873911454739567";
			String requestBody="fieldId=perVaidImgText&fieldValue="+code+"";
			webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webClient.getOptions().setJavaScriptEnabled(false);
			Page page=webClient.getPage(webRequest);
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();
				System.out.println("获取的验证图片验证码的结果是："+html);
				if(html.contains("验证码错误")){
					System.out.println("图片验证码识别错误");
				}else{
					url="http://218.28.196.73:33002/siq/web/loginWeb.action";
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					requestBody=""
							+ "user.userCode=sweetlu"
							+ "&user.psw=111111"
							+ "&user.zoneCode=411400"
							+ "&user.userType=3";
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);    //用Page接收也可以
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						System.out.println("校验登录信息结果页面html为："+html);
						if(html.contains("success")){
							System.out.println("登录成功");
							//用户信息
							url="http://218.28.196.73:33002/siq/web/staff_disStaffInfo.action";
							webRequest = new WebRequest(new URL(url), HttpMethod.GET);
							hpage = webClient.getPage(webRequest);
							if(null!=hpage){
								html=hpage.getWebResponse().getContentAsString();
								Document doc = Jsoup.parse(html);
//								String pernum = doc.getElementsByClass("white1000").get(0)
//								.getElementsByTag("tr").get(1).getElementsByTag("td").get(0)
//								.getElementsByTag("table").get(0).getElementsByTag("tr").get(0)
//								.getElementsByTag("td").get(2).text();
//								
//								pernum=pernum.split("：")[1].trim();  //截取的时候，冒号的后边需要有个空格
//								System.out.println("获取的个人编号是："+pernum);
								
								//////////////////////////////////////////////////////
							/*	String unitnum = doc.getElementById("proofTable").getElementsByClass("content1").get(0)
										.getElementsByTag("table").get(0).getElementsByTag("tr").get(3)    //调研的时候发现tr在第二个位置，可是写索引3才可以正确获取到
										.getElementsByTag("td").get(0).text();
								
								System.out.println("获取的原始的单位编号信息是："+unitnum);
								unitnum=unitnum.split(":")[1].trim();
								System.out.println("获取的单位编号是："+unitnum);*/
								
								
								///////////////////////////////////////////////
								//测试获取用户信息
								/*doc.getElementById("proofTable").getElementsByClass("content1").get(0)
										.getElementsByTag("table").get(0).getElementsByTag("tr").get(4)    //调研的时候发现tr在第二个位置，可是写索引3才可以正确获取到
										.getElementsByTag("td").get(0).getElementsByTag("table").get(0)
										.getElementsByTag("tr").get(0);*/
								
								
//								String daiweimingcheng=doc.select("td:contains(单位名称)").first().text();
//								System.out.println("用select的方式获取的单位名称是："+daiweimingcheng);
								
								String unitnum = doc.select("td:contains(单位编号)").first().text();
								System.out.println("用select的方式获取的单位编号是："+unitnum);
								
								unitnum=unitnum.split(":")[1].trim();
								System.out.println("获取的单位编号是："+unitnum);
								
							}
						}else{
							if(html.contains("用户不存在")){
								System.out.println("用户不存在");
							}else if(html.contains("用户名或密码错误")){
								System.out.println("用户名或密码错误");
							}else{
								System.out.println("出现了其他登录错误："+html);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("登录时出现了异常，在catch中更新登录状态为系统繁忙"+e.toString());
		}
	}
}
