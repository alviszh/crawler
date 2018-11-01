package TestChi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.baidu.aip.ocr.AipOcr;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONObject;


  

public class TestImg {
	    /** 
	     * @param args 
	     */  
	
	//设置APPID/AK/SK
		public static final String APP_ID = "10715647";
		public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
		public static final String SECRET_KEY = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";
		
		
	    public static void main(String[] args) throws Exception {  
	    	String url="http://www.zmdgjj.com/wt-web/login";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			HtmlPage page = webClient.getPage(webRequest);
			if(page!=null){
				String html=page.getWebResponse().getContentAsString();
				Document doc = Jsoup.parse(html);
				Element e1 = doc.getElementById("modulus");
				String val1 = e1.val();
				
				Element e2 = doc.getElementById("exponent");
				String val2 = e2.val();
				System.out.println("获取到的加密参数是："+val1+"--"+val2);
				
				
				
				
				////////////////////////////////////////////////////////////////////
				
			  	double random = Math.random();
			  	
			  	
				HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='username']");
				id_card.reset();
				id_card.setText("412821198712040212");

				String url1="http://www.zmdgjj.com/wt-web/zgzhLoad?username=412821198712040212";
				
				WebRequest  requestSettings = new WebRequest(new URL(url1), HttpMethod.POST);
				requestSettings.setCharset(Charset.forName("UTF-8"));
				requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
				requestSettings.setAdditionalHeader("Cache-Control", "no-cache, no-store, max-age=0");
				requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
				requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
				requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
				requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
				requestSettings.setAdditionalHeader("Host", "www.zmdgjj.com");
				requestSettings.setAdditionalHeader("Origin", "http://www.zmdgjj.com");
				requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
				requestSettings.setAdditionalHeader("Referer", "http://www.zmdgjj.com/wt-web/logout");
				Page pageq = webClient.getPage(requestSettings);
				System.out.println(pageq.getWebResponse().getContentAsString());
				
				String string = pageq.getWebResponse().getContentAsString();
				JSONObject fromObject = JSONObject.fromObject(string);
				String string2 = fromObject.getString("userList");
				JSONObject fromObject2 = JSONObject.fromObject(string2.substring(1, string2.length()-1));
				String string3 = fromObject2.getString("zgzh");
				System.out.println(string3);
				
				String params="111111";
				String str = encryptedPhone(params);
				System.out.println(str);
				String urllogin="http://www.zmdgjj.com/wt-web/login";
				webRequest = new WebRequest(new URL(urllogin), HttpMethod.POST);
				String requestBody="username=412821198712040212&a001="+string3+"&password="+str+"&modulus="+val1+"&exponent="+val2;
				webRequest.setRequestBody(requestBody);
				Page page2 = webClient.getPage(webRequest);
			    if(null!=page2){
			    	html=page2.getWebResponse().getContentAsString();
			    	System.out.println("登录验证结果是："+html);
			    }
			    
			    //个人信息
			    String url5="http://www.zmdgjj.com/wt-web/person/jbxx?random"+random;
				WebRequest  requestSettings2 = new WebRequest(new URL(url5), HttpMethod.GET);
				Page page5 = webClient.getPage(requestSettings2);	
				
				
				//流水
				String urlAc = "http://www.zmdgjj.com/wt-web/personal/jcmxlist?UserId=1&beginDate=2017-01-01&endDate=2018-01-01&userId=1&pageNum=1&pageSize=10";
				Page page3 = webClient.getPage(urlAc);
				
				
			    InputStream contentAsStream = page5.getWebResponse().getContentAsStream(); 
			    String urlimg = "D:\\img\\" + UUID.randomUUID().toString() + ".png";
			    save(contentAsStream, urlimg);
			    
			    String imagePath = getImagePath(page3);
				// 初始化一个AipOcr
				AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
				// 可选：设置网络连接参数
				client.setConnectionTimeoutInMillis(2000);
				client.setSocketTimeoutInMillis(60000);
				String sample = sample(client, imagePath);
				System.out.println(sample);
			    
			}
			
			
			
			
			
	
		    
	    }
	    
	    public static String sample(AipOcr client,String image) {
			// 传入可选参数调用接口
			HashMap<String, String> options = new HashMap<String, String>();
//			options.put("Content-Type", "application/x-www-form-urlencoded");
//			options.put("image", "true");
//			options.put("templateSign", "421bef7004708216699e2f955d1f2d43");
			
			options.put("detect_direction", "true");
//			options.put("probability", "true");
			org.json.JSONObject res = client.basicAccurateGeneral(image, options);
			System.out.println(res.toString(2));
			return res.toString(2);
		}
	    
	    
	    public static void save(InputStream inputStream, String filePath) throws Exception { 

	    	OutputStream outputStream = new FileOutputStream(filePath); 

	    	int bytesWritten = 0; 
	    	int byteCount = 0; 

	    	byte[] bytes = new byte[1024]; 

	    	while ((byteCount = inputStream.read(bytes)) != -1) { 
	    	outputStream.write(bytes, 0, byteCount); 

	    	} 
	    	inputStream.close(); 
	    	outputStream.close(); 
	    	}
	    
	    //cookie--string
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
	    
	    
	    //下载图片1-------------------------------------------------------------------------------------------------------------
	    public static byte[] readInputStream(InputStream inStream) throws Exception{  
	    	ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	    	//创建一个Buffer字符串  
	    	byte[] buffer = new byte[1024];  
	    	//每次读取的字符串长度，如果为-1，代表全部读取完毕  
	    	int len = 0;  
	    	//使用一个输入流从buffer里把数据读取出来  
	    	while( (len=inStream.read(buffer)) != -1 ){  
	    		//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度  
	    		outStream.write(buffer, 0, len);  
	    	}  
	    	//关闭输入流  
	    	inStream.close();  
	    	//把outStream里的数据写入内存  
	    	return outStream.toByteArray();  
	    }  
	    
	    //下载图片2------------------------------------------------------------------------------------------------------------
	    private static String getImg() {
	    	
	    	double random = Math.random();
			String url = "http://www.zmdgjj.com/wt-web/person/jbxx?random=0.28882884498505623";
			
			Connection con = Jsoup.connect(url).header("Accept","image/webp,image/*,*/*;q=0.8")
						.header("Accept-Encoding", "gzip, deflate, sdch")
						.header("Accept-Language", "zh-CN,zh;q=0.8")
						.header("Connection", "keep-alive")
						.header("Host", "www.zmdgjj.com")
						.header("Referer", "http://www.zmdgjj.com/wt-web/home")
						.header("Cookie", "JSESSIONID=15D33239D94E6DAB92DF16FFEF97EB16; token=134be99b1db04300ab255393e91f0d02; __guid=4384053.501987537427966460.1514962833198.1316; monitor_count=4")
						.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			
			try {
				Response response = con.ignoreContentType(true).execute();
//				String imageName = UUID.randomUUID() + ".png";
//				File file = new File("C:\\Users\\Administrator\\Desktop\\"+imageName);
				File file = new File("D:\\img\\123.png");
				String imgagePath = file.getAbsolutePath();
				FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));
				
				out.write(response.bodyAsBytes()); 
				out.close();
				return imgagePath;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	    
	    //加密
	    public static String encryptedPhone(String phonenum) throws Exception{    
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			String path = readResource("zhumadian.js", Charsets.UTF_8);
			//System.out.println(path);
			//FileReader reader1 = new FileReader(path); // 执行指定脚本
			engine.eval(path); 
			final Invocable invocable = (Invocable) engine;  
			Object data = invocable.invokeFunction("encryptedString",phonenum);
			return data.toString(); 
		}
	    public static String readResource(final String fileName, Charset charset) throws IOException {
	        return Resources.toString(Resources.getResource(fileName), charset);
	    }
	    
	    
	    //下载图片3----------------------------------------------------------------------------------------------
	    //利用IO流保存验证码成功后，返回验证码图片保存路径 
	    public static String getImagePath(Page page) throws Exception{ 
	    File imageFile = getImageCustomPath(); 
	    String imgagePath = imageFile.getAbsolutePath(); 
	    InputStream inputStream = page.getWebResponse().getContentAsStream(); 
	    FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
	    if (inputStream != null && outputStream != null) { 
	    int temp = 0; 
	    while ((temp = inputStream.read()) != -1) { // 开始拷贝 
	    outputStream.write(temp); // 边读边写 
	    } 
	    outputStream.close(); 
	    inputStream.close(); // 关闭输入输出流 
	    } 
	    return imgagePath; 
	    } 
	    //创建验证码图片保存路径 
	    public static File getImageCustomPath() { 
	    String path="D:\\img\\"; 
	    // if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) { 
	    // path = System.getProperty("user.dir") + "/verifyCodeImage/"; 
	    // } else { 
	    // path = System.getProperty("user.home") + "/verifyCodeImage/"; 
	    // } 
	    File parentDirFile = new File(path); 
	    parentDirFile.setReadable(true); // 
	    parentDirFile.setWritable(true); // 
	    if (!parentDirFile.exists()) { 
	    System.out.println("==========创建文件夹=========="); 
	    parentDirFile.mkdirs(); 
	    } 
	    // String imageName = UUID.randomUUID().toString() + ".jpg"; 
	    String imageName = "image.jpg"; 
	    File codeImageFile = new File(path + "/" + imageName); 
	    codeImageFile.setReadable(true); // 
	    codeImageFile.setWritable(true, false); // 
	    return codeImageFile; 
	    }
	} 
