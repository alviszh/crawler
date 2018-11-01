package Test;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.bouncycastle.util.encoders.Base64Encoder;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;

public class TestGet {

	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
				
				 
		 String str = "23100419870611142X";  
		 String str1="121212";
	     String base64=getBase64(str);  
	     String base641=getBase64(str1);  
	     System.out.println(base64+base641); 
				
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//图片验证码
		String u="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/vericode.jsp?0.20117761121627398";
		Page page4 = webClient.getPage(u);
//		System.out.println(page4.getWebResponse().getContentAsString());
		String u1="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/getvercode.jsp?task=getvercode";
		Page page2 = webClient.getPage(u1);
		String string = page2.getWebResponse().getContentAsString();
		JSONObject fromObject = JSONObject.fromObject(string);
		String string2 = fromObject.getString("vericode");
		System.out.println(string2);
				
		//短信验证码
		String url="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/platform/workflow/sendMessage.jsp?uuid="+System.currentTimeMillis()+"&task=send&trancode=430303&type=socket&message=%3Ccertinum%3E23100419870611142X%3C%2F%3E";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		Page page = webClient.getPage(webRequest);
		System.out.println(page.getWebResponse().getContentAsString());
		
		//登陆
		String u2="http://bh.hrbgjj.org.cn:47598/hrbwsyyt/per.login?hidCertinum="+base64+"&certinum=23100419870611142X&perpwd="+base641+"&vericodesess="+string2+"&vericode="+string2+"&vcode="+"";
		Page page3 = webClient.getPage(u2);
		System.out.println(page3.getWebResponse().getContentAsString());
		
	}
	
	   //加密  
    public static String getBase64(String str){  
        byte[] b=null;  
        String s=null;  
        try {  
            b = str.getBytes("utf-8");  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
        if(b!=null){  
            s=new BASE64Encoder().encode(b);  
        }  
        return s;  
    }
}
