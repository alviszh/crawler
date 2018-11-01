package test;

import java.net.URL;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

public class Test1 {

	public static void main(String[] args) throws Exception{
		String url = "http://bsdt.hngjj.net/hnwsyyt/platform/workflow/sendMessage.jsp?uuid=1522378679610&task=send&trancode=149364&type=socket&message=%3Cpubaccnum%3E460102199110021529%3C%2F%3E%3Clogintype%3E1%3C%2F%3E%3Cflag%3E1%3C%2F%3E%3Csmstmplcode%3E%3C%2F%3E";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		System.out.println("sendSMS"+page.getWebResponse().getContentAsString());
		JsonParser parser = new JsonParser();
		JsonObject obj = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
		
		JsonObject data = obj.get("data").getAsJsonObject();
		String asString = data.get("RspCode").getAsString();
		System.out.println(asString);
		
		String message = obj.get("message").getAsString();
		System.out.println(message);
		
		
		login();
	}
	
	public static void login() throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://bsdt.hngjj.net/hnwsyyt/indexPerson.jsp";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "bsdt.hngjj.net");
//		webRequest.setAdditionalHeader("Origin", "http://bsdt.hngjj.net");
//		webRequest.setAdditionalHeader("Referer", "http://bsdt.hngjj.net/hnwsyyt/indexPerson.jsp");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		
//		webRequest.setRequestBody("certinum=460102199110021529&cardno=a&accname=李娜&perpwd=123456&pervcode="+code);
		HtmlPage page = webClient.getPage(webRequest);
//		System.out.println("logined=="+page.asXml());
		
		HtmlTextInput certinum = page.getFirstByXPath("//input[@id='certinum']");
		HtmlTextInput accname = page.getFirstByXPath("//input[@id='accname']");
		HtmlPasswordInput perpwd = page.getFirstByXPath("//input[@name='perpwd']");
		HtmlTextInput pervcode = page.getFirstByXPath("//input[@name='pervcode']");
		HtmlButton submit = page.getFirstByXPath("//button[@type='submit']");
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String code = scanner.next();
		
		certinum.setText("460102199110021529");
		accname.setText("李娜");
		perpwd.setText("123456");
		pervcode.setText(code);
		HtmlPage loginedpage = submit.click();
		System.out.println("loginedpage=="+loginedpage.asXml());
		
		getData(webClient);
	}
	
	public static void getData(WebClient webClient) throws Exception{  
		String url = "http://bsdt.hngjj.net/hnwsyyt/command.summer?uuid=1522392338833";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "bsdt.hngjj.net");
		webRequest.setAdditionalHeader("Origin", "http://bsdt.hngjj.net");
		webRequest.setAdditionalHeader("Referer", "http://bsdt.hngjj.net/hnwsyyt/init.summer?_PROCID=30000001");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		
		webRequest.setRequestBody("%24page=%2Fydpx%2F30000001%2F300001_01.ydpx&_LOANCOUNT=0&_ACCNUM=1872384&_IS=-4886952&_PAGEID=step1&_LOGIP=20180330150550397&isSamePer=false&_PROCID=30000001&_SENDOPERID=460102199110021529&temp_.itemid%5B5%5D=99&temp_.itemid%5B3%5D=03&_BRANCHKIND=0&_SENDDATE=2018-03-30&temp_.itemid%5B1%5D=01&temp_._xh%5B4%5D=4&CURRENT_SYSTEM_DATE=2018-03-30&temp_.itemval%5B4%5D=%E5%A4%96%E5%9B%BD%E4%BA%BA%E6%B0%B8%E4%B9%85%E5%B1%85%E7%95%99%E8%AF%81&_ISCROP=0&_TYPE=init&temp_._xh%5B2%5D=2&_PORCNAME=%E4%B8%AA%E4%BA%BA%E2%80%94%E2%80%94%E4%B8%AA%E4%BA%BA%E5%88%86%E6%88%B7%E6%9F%A5%E8%AF%A2&temp_.itemval%5B2%5D=%E5%86%9B%E5%AE%98%E8%AF%81&temp__rownum=5&_RW=w&_UNITACCNAME=null&_ACCNAME=%E6%9D%8E%E5%A8%9C&_DEPUTYIDCARDNUM=460102199110021529&temp_.itemid%5B4%5D=04&_SENDTIME=2018-03-30&temp_.itemid%5B2%5D=02&temp_._xh%5B5%5D=5&temp_.itemval%5B5%5D=%E5%85%B6%E4%BB%96&temp_._xh%5B3%5D=3&temp_.itemval%5B3%5D=%E6%8A%A4%E7%85%A7&_WITHKEY=0&temp_._xh%5B1%5D=1&_sjhm=&temp_.itemval%5B1%5D=%E8%BA%AB%E4%BB%BD%E8%AF%81&xingming=%E6%9D%8E%E5%A8%9C&zjhm=460102199110021529&zjlx=01&sjhm=&grzhye=&usebal=&xingbie=&DealSeq=0&summarycode=&dwzh=&grzh=&onym=");
		Page page = webClient.getPage(webRequest);
		System.out.println("userinfo=-="+page.getWebResponse().getContentAsString());
	}
	
	public static String decodeUnicode(final String dataStr) {     
        int start = 0;     
        int end = 0;     
        final StringBuffer buffer = new StringBuffer();     
        while (start > -1) {     
            end = dataStr.indexOf("\\u", start + 2);     
            String charStr = "";     
            if (end == -1) {     
                charStr = dataStr.substring(start + 2, dataStr.length());     
            } else {     
                charStr = dataStr.substring(start + 2, end);     
            }     
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。     
            buffer.append(new Character(letter).toString());     
            start = end;     
        }     
        return buffer.toString();     
     } 
}
