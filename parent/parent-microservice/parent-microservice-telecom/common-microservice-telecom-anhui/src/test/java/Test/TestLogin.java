package Test;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestLogin {

	public static void main(String[] args) throws Exception {
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		String url="http://ah.189.cn/sso/VImage.servlet?random=0.8871965497595795";
//		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
//		String code = chaoJiYingOcrService.getVerifycode(url,cookies, "1004");
//		
//		//验证验证码
//		String codeurl="http://ah.189.cn/sso/ValidateRandom?validCode="+code;
//		Page page = webClient.getPage(codeurl);
//		String url="http://ah.189.cn/sso/login";
//		
//		HtmlPage page = webClient.getPage(url);
//		HtmlDivision firstByXPath = page.getFirstByXPath("//div[@class='bd_box']");
//		HtmlDivision submit = page.getFirstByXPath("//div[@class='submit']");
//		
//		HtmlDivision bd = firstByXPath.getFirstByXPath("//div[@class='bd']");
//		HtmlTextInput serviceNbr = (HtmlTextInput) bd.getFirstByXPath("//input[@name='serviceNbr']");
//		HtmlTextInput passWord = (HtmlTextInput) bd.getFirstByXPath("//input[@name='showPwd']");
//		
//		HtmlImage image = page.getFirstByXPath("//img[@id='vImg']");
//		
//		DomElement loginbtn = submit.getFirstElementChild();
//		
//		File file = new File("D:\\img\\anhui.jpg");
//		image.saveAs(file);
//		
//		HtmlTextInput validCode = firstByXPath.getFirstByXPath("//input[@id='validCode']");
//		
//		serviceNbr.setText("17718194181");
//		passWord.setText("119110");
//		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
//		validCode.setText(inputValue);
//		HtmlPage logined = loginbtn.click();
//		
//		System.out.println(logined.getBaseURI());
//		
//		String path = "D:\\img\\anhuiLogined.txt";
//		savefile(path, logined.asXml());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT+0800' ", Locale.US); 
		String string = sdf.format(date);
		String string2 = string.replaceAll(" ", "%20");
		System.out.println(string2);
		
		String decode = URLDecoder.decode("%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4","GBK");
		System.out.println(decode);
		
		String encode = URLEncoder.encode(",", "GBK");
		System.out.println(encode);
		//Thu  Oct  12 2017  10:05:46  GMT+0800  (%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4)
	}
	
	// 通过URL获得HTMLpage
		public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
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
