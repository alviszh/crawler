package Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

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
		String url="http://wangting.qzsgjj.com/wt-web/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		Thread.sleep(2000);
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element e1 = doc.getElementById("modulus");
		String val1 = e1.val();

		Element e2 = doc.getElementById("exponent");
		String val2 = e2.val();
		System.out.println(val1 + "--" + val2);
		
		String encryptedPhone = encryptedPhone("123456",val1);
		  System.out.println(encryptedPhone);
		//30d215aa498c49866af498cb803fc27e 5db1f4bb0882227bd04b444599390b70
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");
		elementById.reset();
		elementById.setText("450121199206201847");
		
		HtmlPasswordInput elementById1 = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='in_password']");
		elementById1.reset();
		elementById1.setText("123456");
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='captcha_img']");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

		String requestBody="grloginDxyz=0&username=450121199206201847&password="+encryptedPhone+"&force=force&captcha="+inputValue;
		webRequest.setRequestBody(requestBody);
		Page page2 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("您在中心登记有"))
		{
			String userUrl="http://wangting.qzsgjj.com/wt-web/jcr/jcrkhxxcx_mh.service";
			WebRequest webRequest2 = new WebRequest(new URL(userUrl), HttpMethod.POST);
			String reString="ffbm=01&ywfl=01&ywlb=99&cxlx=01";
			webRequest2.setRequestBody(reString);
			Page page3 = webClient.getPage(webRequest2);
			System.out.println(page3.getWebResponse().getContentAsString());
			
//			String userUrl="http://wangting.qzsgjj.com/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2016-01-01&jsrq=2018-01-25&grxx=0172941&pageNum=1&page=1&startPage=0&pageSize=100&size=10&_=1516862372044";
//			Page page3 = webClient.getPage(userUrl);
//			System.out.println(page3.getWebResponse().getContentAsString());
		}
	}
	
	
	// 加密
		public static  String encryptedPhone(String phonenum,String mo) throws Exception {
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			String path = readResource("zhumadian2.js", Charsets.UTF_8);
			// System.out.println(path);
			// FileReader reader1 = new FileReader(path); // 执行指定脚本
			engine.eval(path);
			final Invocable invocable = (Invocable) engine;
			Object data = invocable.invokeFunction("encryptedString", phonenum,mo);
			return data.toString();
		}
	
		public static String readResource(final String fileName, Charset charset) throws IOException {
			return Resources.toString(Resources.getResource(fileName), charset);
		}
}
