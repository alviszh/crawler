/**
 * 
 */
package testLogin;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.module.htmlunit.WebCrawler;

/**
 * @author Administrator
 *
 */
public class Test1 {

	private static Set<Cookie> cookies;
	
	public static final String URl = "http://www.fssi.gov.cn/";
	
	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		HtmlPage html = getHtml(URl, webClient, 1);
		
//		String loginPath = "E:\\crawler\\foshan\\login.txt";
//		savefile(loginPath,html.asXml());
        
        HtmlTextInput loginName = (HtmlTextInput)html.getFirstByXPath("//input[@id='sfzhForm1']");
		HtmlPasswordInput loginPassword = (HtmlPasswordInput)html.getFirstByXPath("//input[@id='pswForm1']");
		HtmlElement submitbt = (HtmlElement)html.getFirstByXPath("//input[@value='提 交']");
		
		loginName.setText("445381199210170422");
		loginPassword.setText("lsm123456");
		HtmlPage checkPage = submitbt.click();
		
		String checkPath = "E:\\crawler\\foshan\\loginERRORCheck.txt";
		savefile(checkPath,checkPage.asXml());
		
		
		HtmlTextInput validateCode = (HtmlTextInput)checkPage.getFirstByXPath("//input[@name='imagecheck']");
		
		HtmlImage image = checkPage.getFirstByXPath("//img[@height='20']");
		
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("E:\\Codeimg\\"+imageName);
		image.saveAs(file);
		
		HtmlElement submit = (HtmlElement)checkPage.getFirstByXPath("//a[@class='buttonLink']");
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		validateCode.setText(input);
		
		submit.click();
		
//		String resPath = "E:\\crawler\\foshan\\loginRes.txt";
//		savefile(resPath,resPage.asXml());
		
//		HtmlPage personalInfoPage = getHtml("http://61.142.213.86/grwssb/action/MainAction?ActionType=grcx_grjbzlcx&flag=true", webClient, 0);
		//http://61.142.213.86/grwssb/action/MainAction?menuid=grcx_ylbxjfcx&ActionType=grcx_ylbxjfcx&flag=true
		HtmlPage pensionPage = getHtml("http://61.142.213.86/grwssb/parser/MainAction?menuid=grcx_ylbxjfcx&ActionType=grcx_ylbxjfcx&flag=true", webClient, 0);
		
		String resPath = "E:\\crawler\\foshan\\pensionInfo.txt";
		savefile(resPath,pensionPage.asXml());
		
	}
	
	
	//参数type来区别请求类型，1为post，其他为get
	public static HtmlPage getHtml(String url,WebClient webClient,int type) throws Exception{
//			WebClient webClient = WebCrawler.getInstance().getWebClient();
//			webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webRequest = new WebRequest(new URL(url), type==1 ? HttpMethod.POST : HttpMethod.GET);
		
		cookies = webClient.getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("搜索："+cookie.getName()+":"+cookie.getValue());
		}
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
		
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
}
