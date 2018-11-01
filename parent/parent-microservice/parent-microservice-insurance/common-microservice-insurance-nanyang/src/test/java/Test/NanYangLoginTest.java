package Test;

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
 * 测试南阳，发现验证登录信息的链接变成了省直相关
 * @author sln
 *
 */
public class NanYangLoginTest {
	public static void main(String[] args) {
		try {
			//先请求链接登录链接，获取图片验证码对象
			String url="http://218.28.196.73:33002/siq/index.jsp?zoneCode=419900";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			HtmlPage hpage = webClient.getPage(webRequest);
			HtmlImage image = hpage.getFirstByXPath("//img[@id='perVaidImg']");   //个人登录方式的图片验证码
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 
			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			//需要先校验图片验证码，图片验证码的校验链接和登录账号密码的验证练级不一样
			url="http://www.hnylbx.com:33002/siq/pages/security/result.jsp?s=0.6206438251875375";
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
//					url="http://218.28.196.73:33002/siq/web/loginWeb.action";
					url="http://www.hnylbx.com:33002/siq/web/szloginWeb.action";
					
					webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					/*requestBody=""
							+ "user.userCode=sweetlu"
							+ "&user.psw=111111"
							+ "&user.zoneCode=411400"
							+ "&user.userType=3";*/
					
					requestBody="user.userCode=hujuhua2017"
							+ "&user.psw=hujuhua2017"
							+ "&user.zoneCode=419900"
							+ "&user.userType=3";
					webRequest.setRequestBody(requestBody);
					page = webClient.getPage(webRequest);    //用Page接收也可以
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
						System.out.println("校验登录信息结果页面html为："+html);
						if(html.contains("success")){
							System.out.println("登录成功");
							//用户信息
							url="http://www.hnylbx.com:33002/siq/web/szpersonHome_seeInsuper.action?insuperType=3";
							webRequest = new WebRequest(new URL(url), HttpMethod.GET);
							hpage = webClient.getPage(webRequest);
							if(null!=hpage){
								html=hpage.getWebResponse().getContentAsString();
								System.out.println("获取的用户信息是："+html);
							
								
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
