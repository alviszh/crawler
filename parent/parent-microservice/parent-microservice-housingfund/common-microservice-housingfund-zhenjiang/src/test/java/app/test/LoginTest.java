package app.test;

import java.io.File;
import java.net.URL;

import javax.swing.JOptionPane;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 * @date: 2018年3月14日 下午2:27:17 
 */
public class LoginTest {
	public static void main(String[] args) throws Exception { 
		String loginUrl="http://www.zjzfjj.com.cn/searchPersonLogon.do";
		WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient();	
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage hPage = webClient.getPage(webRequest);
		if(null!=hPage){
			//身份证号登录
			HtmlImage image = hPage.getFirstByXPath("//img[@src='jcaptcha?onlynum=true']"); 
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 
			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			//如下验证登录信息
			String requestBody="select=2&spcode=MzIxMTgzMTk5MDA2MTkwMzM5&sppassword=MTIzNDU2&rand="+code.trim()+"";
			webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
//			String requestBody="select=2&spcode=321183199006190339&sppassword=123456&rand="+code.trim()+"";
			webRequest.setRequestBody(requestBody);
			Page logonPage=webClient.getPage(webRequest);
			if(null!=logonPage){
				String html=logonPage.getWebResponse().getContentAsString();
				System.out.println("模拟点击登陆后获取的页面是："+html);
				if(html.contains("职工查询")){
					System.out.println("恭喜您登录成功！");
					String url="http://www.zjzfjj.com.cn/searchGrye.do";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page = webClient.getPage(webRequest);
					html=page.getWebResponse().getContentAsString();
					System.out.println("获取用户基本信息响应的页面是："+html);
				}else{
					if(html.contains("您输入的验证有误")){
						System.out.println("您输入的验证码有误");
					}else if(html.contains("请认真核实您输入的职工帐号或身份证号码及密码")){
						System.out.println("请认真核实您输入的职工帐号或身份证号码及密码");
					}else{
						System.out.println("出现了其他登录错误信息");
					}
				}
			}
		}
	}
}
