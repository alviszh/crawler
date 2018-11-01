package Test;

import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		String url="http://cx.bbzfgjj.com/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlRadioButtonInput searchpwd = (HtmlRadioButtonInput)page.getFirstByXPath("//*[@id='RadioButtonList1_1']");
		searchpwd.click();
		Page page3 = searchpwd.click();
		
		Document doc = Jsoup.parse(page3.getWebResponse().getContentAsString());
		Element elementById = doc.getElementById("__VIEWSTATE");
		String val = elementById.val();
		String encode = URLEncoder.encode(val,"UTF-8");
		System.out.println(encode);
		
		Element elementById1 = doc.getElementById("__VIEWSTATEGENERATOR");
		String val1 = elementById1.val();
		System.out.println(val1);
		
		Element elementById2 = doc.getElementById("__EVENTVALIDATION");
		String val2 = elementById2.val();
		String encode2 = URLEncoder.encode(val2,"UTF-8");
		System.out.println(encode2);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//		                                                                   %2FwEPDwUKMTQwMzQ0MjA5NQ9kFgICAw9kFgICBQ8QZGQWAWZkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQxJbWFnZUJ1dHRvbjEFDEltYWdlQnV0dG9uMuKN%2FLFRNUc5Oj5yPBsBjvEeusbZ5vvEEWMNX%2FPjAhgz
		String a="__LASTFOCUS=&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUKMTQwMzQ0MjA5NQ9kFgICAw9kFggCBQ8QZGQWAQIBZAIHDw8WBB4HRW5hYmxlZGgeBFRleHRlZGQCCQ8PFgIfAGdkZAILDw8WBB8AaB8BZWRkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYCBQxJbWFnZUJ1dHRvbjEFDEltYWdlQnV0dG9uMkxBYi6seNaEKg3SSf7w9GXtc0xt9z4VKJtfFL0FrFWc&__VIEWSTATEGENERATOR=BBBC20B8&__EVENTVALIDATION=%2FwEdAAleeVELTurSQiJO17vx1Wy79WzmaW2Ka%2BCcTPIdwbaB%2B1RoR9lNgEd7SOq%2B8Ujsn2eWECZPQRLurjTFVUPTWENWT%2BzJdFl5zbwKWWeR1lWyG6qiF7kcjlfAtoRtHl7DFeqeCN%2FhKhURU8usu6CibpYL6ZACrx5RZnllKSerU%2BIuKmLNV%2B2mZgnOAlNG5DVTg1uEKSG73Ib32dSiuFmhq1LxlLRqIrimPObWMxcSAsamvw%3D%3D&RadioButtonList1=2&TextBox_sfzh=34032219910304561X&ImageButton1.x=38&ImageButton1.y=10";
//		                                                                                                                                                                                                                                                                                                                                                                          %2FwEdAAnca0jJTh7AHHlYddjikaRz9WzmaW2Ka+CcTPIdwbaB+1RoR9lNgEd7SOq+8Ujsn2eWECZPQRLurjTFVUPTWENWT+zJdFl5zbwKWWeR1lWyG6qiF7kcjlfAtoRtHl7DFeqeCN%2FhKhURU8usu6CibpYL6ZACrx5RZnllKSerU+IuKmLNV+2mZgnOAlNG5DVTg1vEbsgnyp27VaDW+B%2FXvt2o3U7SHG0Lys91UoX7RxOwWA%3D%3D
		String r="__LASTFOCUS=&__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE="+encode+"&__VIEWSTATEGENERATOR="+val1+"&__EVENTVALIDATION="+encode2+"&RadioButtonList1=2&TextBox_sfzh=34032219910304561X&ImageButton1.x=51&ImageButton1.y=13";
		webRequest.setRequestBody(a);

		Page page2 = webClient.getPage(webRequest);
		System.out.println(page2.getWebResponse().getContentAsString());
		

		
//		String url11="http://cx.bbzfgjj.com/inputmima.aspx?dwzh=1003748%20&grzh=00161";
//		
//		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='password']");
//		searchpwd.reset();
//		searchpwd.setText("030928");
//		
//		
//        HtmlImage img = page.getFirstByXPath("//*[@id='loginForm']/div[1]/img");
//		
//		String imageName = "111.jpg";
//		File file = new File("D:\\img\\" + imageName);
//		img.saveAs(file); 
//		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
//		
//		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
//		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//input[@name='validateCode']");
//		identifying.reset();
//		identifying.setText(inputValue);
//		
////		HtmlElement button = page.getFirstByXPath("//*[@id='loginForm']/input[3]");
////		HtmlPage page2 = button.click();
//		
//		String url1="http://218.90.206.76:8080/jeesite/servlet/validateCodeServlet?validateCode="+inputValue;
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
//		Page page1 = webClient.getPage(webRequest);
//		String url2="http://218.90.206.76:8080/jeesite/a/login?username=321202199009030928&password=030928&validateCode="+inputValue;
//		WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.POST);
//		Page page2 = webClient.getPage(webRequest2);
//		String url3="http://218.90.206.76:8080/jeesite/a";
//		WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.GET);
//		Page page3 = webClient.getPage(webRequest3);
//		String url4="http://218.90.206.76:8080/jeesite/a/sys/user/index";
//		WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.GET);
//		Page page4 = webClient.getPage(webRequest4);
//		System.out.println(page4.getWebResponse().getContentAsString());
	}
}
