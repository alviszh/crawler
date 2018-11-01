package Test;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.sz.shanxi.InsuranceSZShanXiEndowment;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Testlogin {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
				
//		String url="http://sx.msyos.com/pc/htm/index.htm#";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		HtmlPage page = webClient.getPage(url);		
//		
//		HtmlElement id_card1 = (HtmlElement)page.getFirstByXPath("//*[@id='showlogin']");
//		HtmlPage click = id_card1.click();
//		
//		HtmlTextInput id_card2 = (HtmlTextInput)click.getFirstByXPath("//*[@id='username']");
//		id_card2.reset();
//		id_card2.setText("13333576990");
//		
//		HtmlPasswordInput id_account = (HtmlPasswordInput)click.getFirstByXPath("//*[@id='password']");
//		id_account.reset();
//		id_account.setText("891453lili");
		
		
//		HtmlImage img = page.getFirstByXPath("//*[@id='checkImg']");
//		String imageName = "111.jpg";
//		File file = new File("D:\\img\\" + imageName);
//		img.saveAs(file); 
//		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
//		HtmlTextInput h = (HtmlTextInput) page.getFirstByXPath("//*[@id='randCode']");
//		h.setText(inputValue);
		
		
//		HtmlElement firstByXPath = click.getFirstByXPath("//*[@id='loginForm']/div[5]/div/a");
//		HtmlPage page2 = firstByXPath.click();
//		System.out.println(page2.getWebResponse().getContentAsString());
//		WebClient webClient2 = page2.getWebClient();
		
		String md5 = MD5("891453lili");
		String md51 = MD5("891453liliMSYOS");
		System.out.println(md5.toLowerCase());
		System.out.println(md51.toLowerCase());
		String url3="http://sx.msyos.com/user/login.html?username=13333576990&password="+md5.toLowerCase()+"&password_v1="+md51.toLowerCase();
//		WebRequest webRequest = new WebRequest(new URL(url3), HttpMethod.POST);
		System.out.println(url3);
		WebRequest requestSettings = new WebRequest(new URL(url3), HttpMethod.POST);
		Page page4 = webClient.getPage(requestSettings);
		System.out.println(page4.getWebResponse().getContentAsString());
		
		JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
		String token = fromObject.getString("token");
		String url2="http://sx.msyos.com/baseInfo/querySbjfForJson.html?rownum=1000&startrow=1&startDate=2015-04&endDate=2018-04&xzlx=390&token="+token;
		WebRequest webRequest = new WebRequest(new URL(url2),HttpMethod.POST);
		WebClient webClient1 = WebCrawler.getInstance().getNewWebClient();

		Page page3 = webClient1.getPage(webRequest);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("queryList"))
		{
			JSONObject fromObject2 = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
			String string = fromObject2.getString("queryList");
			JSONArray fromObject3 = JSONArray.fromObject(string);
			InsuranceSZShanXiEndowment in =null;
			List<InsuranceSZShanXiEndowment> list = new ArrayList<InsuranceSZShanXiEndowment>();
			WebParam<InsuranceSZShanXiEndowment> webParam = new WebParam<InsuranceSZShanXiEndowment>();
			for (int i = 0; i < fromObject3.size(); i++) {
//				System.out.println(fromObject3.get(i));
				JSONObject fromObject4 = JSONObject.fromObject(fromObject3.get(i));
				String string2 = fromObject4.getString("AAC123");
//				System.out.println(string2);
				in =new InsuranceSZShanXiEndowment();
				in.setType(fromObject4.getString("AAE143"));
				in.setStartDate(fromObject4.getString("AAE041"));
				in.setEndDate(fromObject4.getString("AAE042"));
				in.setPayMoney(fromObject4.getString("AAC123"));
				in.setPersonalPay(fromObject4.getString("BCC006"));
				in.setTaskid("");
				list.add(in);
			}
			System.out.println(list);
		}

	}
	
	
	public static String MD5(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

public static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
}
