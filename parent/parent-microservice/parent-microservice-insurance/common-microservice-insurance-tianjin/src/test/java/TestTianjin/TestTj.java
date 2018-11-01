package TestTianjin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

public class TestTj {
	public static void main(String[] args) {

		try {
			//验证码图片请求路径
			String  url1 = "http://221.207.175.178:7989/uaa/captcha/img";
			
			//拼接出验证码真正的路径
			String ss = url1+"/"+sendGetRequest(url1).substring(7,39);
			System.err.println(ss);
			
			login("231084198511174027", "851117",ss);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	private static String selector_username = "#username";
	private static String selector_password = "#inputPassword";
	private static String selector_userjym = "#captchaWord";

	public static void login(String name, String password, String ss) throws Exception {

		//隐藏界面路径
		String url = "http://221.207.175.178:7989/uaa/views/person/login.html";
		             
		
		//实际页面路径
	//	String url2 = "http://public.tj.hrss.gov.cn/uaa/personlogin#/personLogin";
		
//		String url3 = "http://public.tj.hrss.gov.cn/uaa/api/person/idandmobile/login";

		WebClient webClient = WebCrawler.getInstance().getWebClient();
		
//		WebClient webClient = new WebClient();
		
		webClient.getOptions().setJavaScriptEnabled(false);
		
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		// 2 禁用Css，可避免自动二次请求CSS进行渲染
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(true);
		// 3 启动客户端重定向
		webClient.getOptions().setRedirectEnabled(true);
		// 4 js运行错误时，是否抛出异常
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		// 5 设置超时
		webClient.getOptions().setTimeout(80000);
		
		
		HtmlPage searchPage = webClient.getPage(url);
		// 等待JS驱动dom完成获得还原后的网页
		webClient.waitForBackgroundJavaScript(2000);
		
		
//		HtmlPage searchPage1 = webClient.getPage(url2);
//		webClient.waitForBackgroundJavaScript(2000);
		
		
		
		//String cookies = CommonUnit.transcookieToJson(searchPage.getWebClient());
		//System.out.println(cookies);
		//WebRequest webRequest = new WebRequest(new URL(url3),HttpMethod.POST);
		//webRequest.toString();
		
		 //从服务器获得图片，保存到本地  
        saveImageToDisk(ss);
        

		//String imageName = "111.jpg";
		//File file = new File("D:\\img\\" + imageName);
		//image.saveAs(file);

		HtmlTextInput inputName = (HtmlTextInput) searchPage.querySelector(selector_username);
		inputName.reset();
		inputName.setText(name);
       
   //     HtmlElement username = (HtmlElement)searchPage.getFirstByXPath("//*[@id='username']");
	    HtmlPasswordInput inputPassword = (HtmlPasswordInput) searchPage.querySelector(selector_password);
		inputPassword.reset();
		inputPassword.setText(password);

		HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector(selector_userjym);
		inputuserjym.reset();
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		inputuserjym.setText(inputValue);
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
		
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Cache-Control", "max-age=0");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("Origin", "http://public.tj.hrss.gov.cn");
		requestSettings.setAdditionalHeader("Host", "221.207.175.178:7989");
		requestSettings.setAdditionalHeader("Referer", "http://221.207.175.178:7989/uaa/personlogin");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("username", "231084198511174027"));
		requestSettings.getRequestParameters().add(new NameValuePair("password", "851117"));
	//	requestSettings.getRequestParameters().add(new NameValuePair("captchaId", id));
	//	requestSettings.getRequestParameters().add(new NameValuePair("captchaWord", code));
		
		HtmlPage page = webClient.getPage(requestSettings);
        System.out.println(page);
	     HtmlElement loginButton = (HtmlElement)searchPage.getFirstByXPath("//button[@type='submit']");
       // HtmlAnchor loginButton = searchPage.getFirstByXPath("//a[@href='http://public.tj.hrss.gov.cn/personlogin/']");
		
		
		if (loginButton == null) {
			throw new Exception("login button can not found : null");
		} else {
			Page a  = loginButton.click();
			System.out.println(a.getWebResponse().getContentAsString());
			String substring = a.toString().replace("/views/person", "");
			System.out.println(substring);
			
			//System.out.println(searchPage.asXml());
			
			
			
			
			
			
			
			
			
		}
	}
	
	
	 public static String sendGetRequest(String getUrl)
	  {
	   StringBuffer sb = new StringBuffer();
	   InputStreamReader isr = null;
	   BufferedReader br = null;
	   try
	   {
	     URL url = new URL(getUrl);
	     URLConnection urlConnection = url.openConnection();
	     urlConnection.setAllowUserInteraction(false);
	     isr = new InputStreamReader(url.openStream());
	     br = new BufferedReader(isr);
	     String line;
	     while ((line = br.readLine()) != null)
	     {
	      sb.append(line);
	     }
	   }
	   catch (Exception e)
	   {
	     e.printStackTrace();
	   }
			return sb.toString();
	  }
	
	
	 
		  
	       
	        //这个函数负责把获取到的InputStream流保存到本地。  
	        public static void saveImageToDisk(String ss) {  
	            InputStream inputStream = null;  
	            inputStream = getInputStream(ss);//调用getInputStream()函数。  
	            byte[] data = new byte[1024];  
	            int len = 0;  
	              
	            FileOutputStream fileOutputStream = null;  
	            try {  
	                fileOutputStream = new FileOutputStream("D:\\test.jpg");//初始化一个FileOutputStream对象。  
	                while ((len = inputStream.read(data))!=-1) {//循环读取inputStream流中的数据，存入文件流fileOutputStream  
	                    fileOutputStream.write(data,0,len);  
	                }  
	                  
	            } catch (FileNotFoundException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }finally{//finally函数，不管有没有异常发生，都要调用这个函数下的代码  
	                if(fileOutputStream!=null){  
	                    try {  
	                        fileOutputStream.close();//记得及时关闭文件流  
	                    } catch (IOException e) {  
	                        // TODO Auto-generated catch block  
	                        e.printStackTrace();  
	                    }  
	                }  
	                if(inputStream!=null){  
	                    try {  
	                        inputStream.close();//关闭输入流  
	                    } catch (IOException e) {  
	                        // TODO Auto-generated catch block  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }  
	              
	              
	              
	        }  
	          
	          
	        public static InputStream getInputStream(String ss){  
	            InputStream inputStream = null;  
	            HttpURLConnection httpURLConnection = null;  
	            try {  
	                URL url = new URL(ss);//创建的URL  
	                if (url!=null) {  
	                    httpURLConnection = (HttpURLConnection) url.openConnection();//打开链接  
	                    httpURLConnection.setConnectTimeout(3000);//设置网络链接超时时间，3秒，链接失败后重新链接  
	                    httpURLConnection.setDoInput(true);//打开输入流  
	                    httpURLConnection.setRequestMethod("GET");//表示本次Http请求是GET方式  
	                    int responseCode = httpURLConnection.getResponseCode();//获取返回码  
	                    if (responseCode == 200) {//成功为200  
	                        //从服务器获得一个输入流  
	                        inputStream = httpURLConnection.getInputStream();  
	                    }  
	                }  
	                  
	                  
	            } catch (MalformedURLException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            } catch (IOException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	            return inputStream;  
	              
	        }  
	        
	        public static String transcookieToJson(WebClient webClient){
	    		Set<Cookie> cookies = webClient.getCookieManager().getCookies(); 

	    		Set<CookieJson> cookiesSet= new HashSet<>();
	    		
	    		for(Cookie cookie:cookies){ 
	    			CookieJson cookiejson = new CookieJson();
	    			cookiejson.setDomain(cookie.getDomain());
	    			cookiejson.setKey(cookie.getName());
	    			cookiejson.setValue(cookie.getValue());
	    			cookiesSet.add(cookiejson); 
	    		} 

	    		String cookieJson = new Gson().toJson(cookiesSet);
	    		return cookieJson;

	    	}
	       
	          
	    } 
	
