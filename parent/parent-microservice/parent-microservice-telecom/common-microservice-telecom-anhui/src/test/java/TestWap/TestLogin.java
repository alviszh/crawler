package TestWap;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.common.base.Charsets;
import com.module.htmlunit.WebCrawler;
import com.google.common.io.Resources;

public class TestLogin {

	public static void main(String[] args) throws Exception {
		//登陆界面
				String url = "http://wapah.189.cn/ua/toLogin.shtml";
				WebClient webClient = WebCrawler.getInstance().getNewWebClient();
				HtmlPage page = webClient.getPage(url);
				
				
				//加密的动态val
				//HtmlElement module = page.getFirstByXPath("//input[@id='module']");
				Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
				Element elementById = doc.getElementById("module");
				String nodeValue = elementById.val();
				
				HtmlImage image = page.getFirstByXPath("//img[@id='gcode']");
				//String code = chaoJiYingOcrService.getVerifycode(image, "1005");
				String imageName = "111.jpg";
				File file = new File("D:\\img\\" + imageName);
				image.saveAs(file); 
				//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
				
				String inputValue = JOptionPane.showInputDialog("请输入验证码……");
				//加密参数
				TestLogin tl = new TestLogin();
				String userphone = tl.encryptedPhoneTwo("18134539679",nodeValue);
				String khmm =tl.encryptedPhoneTwo("131415",nodeValue);
				String data ="{\"userphone\":\""+userphone+"\",\"check_userphone\":\"18134539679\",\"logintype\":\"1\",\"khmm\":\""+khmm+"\",\"sjmm\":\"\",\"randomCode\":\""+inputValue+"\"}";
		        String encode = URLEncoder.encode(data, "GBK");
				//String url1 ="http://wapah.189.cn/ua/login.shtml?tt=Thu%20Oct%2012%202017%2010:05:46%20GMT+0800%20(%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4)?formData="+data;
				
				//时间参数
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss 'GMT+0800' ", Locale.US); 
				String string = sdf.format(date);
				String string2 = string.replaceAll(" ", "%20");
				System.out.println(string2);
				
				                                                                                                       //中国标准时间↓
				WebRequest requestSettings = new WebRequest(new URL("http://wapah.189.cn/ua/login.shtml?tt="+string2+"(%D6%D0%B9%FA%B1%EA%D7%BC%CA%B1%BC%E4)"), HttpMethod.POST);
				requestSettings.setAdditionalHeader("Accept","application/json, text/javascript, */*; q=0.01");
				requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				requestSettings.setAdditionalHeader("Connection", "keep-alive");
				requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
				requestSettings.setAdditionalHeader("Origin", "http://wapah.189.cn");
				requestSettings.setAdditionalHeader("Host", "wapah.189.cn");
				requestSettings.setAdditionalHeader("Referer","http://wapah.189.cn/ua/toLogin.shtml");
				requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				requestSettings.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36");
				requestSettings.setRequestBody("formData="+encode);
				
				Page page2 = webClient.getPage(requestSettings);
				String urluser="";
	}
	
	//加密js2
		public  String encryptedPhoneTwo(String phonenum,String nodeValue) throws Exception {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			String path = this.readResource("telecom2.js", Charsets.UTF_8);
			// System.out.println(path);
			// FileReader reader1 = new FileReader(path); // 执行指定脚本
			engine.eval(path);
			final Invocable invocable = (Invocable) engine;
			Object data = invocable.invokeFunction("encryptedString", phonenum,nodeValue);
			return data.toString();
		}
		
		public String readResource(final String fileName, Charset charset) throws IOException {
	        return Resources.toString(Resources.getResource(fileName), charset);
	    }
}
