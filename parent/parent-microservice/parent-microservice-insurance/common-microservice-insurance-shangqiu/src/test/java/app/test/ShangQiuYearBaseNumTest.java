package app.test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
/**
 * @author sln
 *
 */
public class ShangQiuYearBaseNumTest {
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
							//历年缴费基数信息
							url="http://218.28.196.73:33002/siq/web/staff_queryYpwilist.action";
							webRequest = new WebRequest(new URL(url), HttpMethod.POST);
							Page tPage = webClient.getPage(webRequest);
							if(null!=tPage){
								html=tPage.getWebResponse().getContentAsString();
								
								
								html.replaceAll("<\\/td>", "");
								
								StringBuilder sb = new StringBuilder();
								sb.append("<table>");
								sb.append(html);
								sb.append("</table>");
								Document doc = Jsoup.parse(sb.toString());
								
//								String text = doc.getElementsByTag("tbody").get(0).getElementsByTag("tr").get(0)
//								.getElementsByTag("td").get(0).text();
								
								List<String> yearList=new ArrayList<String>();    //yearList大小是多少，就从baseNumList中取多少个数据
								List<String> baseNumList=new ArrayList<String>();
								
								Elements elementsByTag = doc.getElementsByTag("td");
								String data=null;
								if(elementsByTag.size()>0){   //有数据可供采集
									for (Element element : elementsByTag) {
										data=element.text();
										if(data.length()>6){  //说明得到的不是<\/td>替换之后的空值
											if(data.contains("年")){   //保存数据的时候不要"年"字
												yearList.add(data.substring(0, 4));
											}else{
												baseNumList.add(data.substring(0,data.indexOf("<")));
											}
										}
									}
								}
								
								
								System.out.println("yearList集合中的数据是"+yearList.toString());
								System.out.println("baseNumList集合中的数据是"+baseNumList.toString());
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
