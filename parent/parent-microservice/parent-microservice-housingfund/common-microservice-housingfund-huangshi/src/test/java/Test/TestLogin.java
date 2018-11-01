package Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		
		String url="http://online.hsgjj.com:8038/wt-web/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element e1 = doc.getElementById("modulus");
		String val1 = e1.val();

		Element e2 = doc.getElementById("exponent");
		String val2 = e2.val();
		System.out.println(val1 + "--" + val2);
		
		String encryptedPhone = encryptedPhone("880425");
		System.out.println(encryptedPhone);
		//30d215aa498c49866af498cb803fc27e   a49bca71283a0e028313094321135d71
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");
		elementById.reset();
		elementById.setText("420281198804250042");
		
		HtmlPasswordInput elementById1 = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='in_password']");
		elementById1.reset();
		elementById1.setText("880425");
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='captcha_img']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String requestBody="&username=420281198804250042&password=a49bca71283a0e028313094321135d71&force=force&captcha="+inputValue;
//		                     username=420281198804250042&password=a49bca71283a0e028313094321135d71&force=force&captcha=7e19
		webRequest.setRequestBody(requestBody);
		Page page2 = webClient.getPage(webRequest);
		Thread.sleep(1000);
//		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("您确定退出"))
		{
//			String userUrl="http://online.hsgjj.com:8038/wt-web/jcr/jcrkhxxcx_mh.service";
//			WebRequest webRequest2 = new WebRequest(new URL(userUrl), HttpMethod.POST);
//			String reString="ffbm=01&ywfl=01&ywlb=99&cxlx=01";
//			webRequest2.setRequestBody(reString);
//			Page page3 = webClient.getPage(webRequest2);
//			System.out.println(page3.getWebResponse().getContentAsString());
			
//			String userUrl="http://wangting.qzsgjj.com/wt-web/jcr/jcrxxcxzhmxcx.service?   ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2016-01-01&jsrq=2018-01-25&grxx=0172941&pageNum=1&page=1&startPage=0&pageSize=100&size=10&_=1516862372044";
			String uuu="http://online.hsgjj.com:8038/wt-web/jcr/jcrxxcxzhmxcx_.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2015-04-01&jsrq=2018-04-23&grxx=258017901&fontSize=13px&pageNum=1&pageSize=10&totalcount=40&pages=4&random=0.257592513865994";
			WebRequest webRequest3 = new WebRequest(new URL(uuu), HttpMethod.POST);
			webRequest3.setCharset(Charset.forName("UTF-8"));
			Page page4 = webClient.getPage(webRequest3);
			System.out.println(page4.getWebResponse().getContentAsString());
		}
	}
	
	
	// 加密
		public static  String encryptedPhone(String phonenum) throws Exception {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			String path = readResource("zhumadian.js", Charsets.UTF_8);
			// System.out.println(path);
			// FileReader reader1 = new FileReader(path); // 执行指定脚本
			engine.eval(path);
			final Invocable invocable = (Invocable) engine;
			Object data = invocable.invokeFunction("encryptedString", phonenum);
			return data.toString();
		}
	
		public static String readResource(final String fileName, Charset charset) throws IOException {
			return Resources.toString(Resources.getResource(fileName), charset);
		}
}
