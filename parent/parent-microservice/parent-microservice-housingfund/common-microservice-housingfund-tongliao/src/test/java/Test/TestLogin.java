package Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.baidu.aip.ocr.AipOcr;
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

	//设置APPID/AK/SK
		public static final String APP_ID = "10715647";
		public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
		public static final String SECRET_KEY = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";
			
			
		public static void main(String[] args) throws Exception{
			String url="http://www.tlzfgjj.com:8889/wt-web/grlogin";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			HtmlPage page = webClient.getPage(url);	
			
			
			Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
			Element e1 = doc.getElementById("modulus");
			String val1 = e1.val();

			Element e2 = doc.getElementById("exponent");
			String val2 = e2.val();
			System.out.println(val1 + "--" + val2);
			
			String encryptedPhone = encryptedPhone("111111");
			System.out.println(encryptedPhone);
			//094543cff52a8c99acde4530c6738963
			HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");
			elementById.reset();
			elementById.setText("152321199002106671");
			
			HtmlPasswordInput elementById1 = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='in_password']");
			elementById1.reset();
			elementById1.setText("111111");
			
			
			HtmlImage img = page.getFirstByXPath("//*[@id='captcha_img']");
			String imageName = "111.jpg";
			File file = new File("D:\\img\\" + imageName);
			img.saveAs(file); 
			String inputValue = JOptionPane.showInputDialog("请输入验证码……");
			
			
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

			String requestBody="username=152321199002106671&password=094543cff52a8c99acde4530c6738963&force=force&captcha="+inputValue;
			webRequest.setRequestBody(requestBody);
			Page page2 = webClient.getPage(webRequest);
			Thread.sleep(1000);
			System.out.println(page2.getWebResponse().getContentAsString());
			if(page2.getWebResponse().getContentAsString().contains("退出服务大厅"))
			{//302941
//			String urlpay="http://www.tlzfgjj.com:8889/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2016-08-06&jsrq=2018-08-06&grxx=249383";
			String urlpay="http://www.tlzfgjj.com:8889/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2014-01-01&jsrq=2018-08-06&grxx=249383";
			WebRequest webRequest2 = new WebRequest(new URL(urlpay), HttpMethod.GET);
//			String reString  = "ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2016-01-01&jsrq=2018-08-06&grxx=249383";
//			webRequest2.setRequestBody(reString);
			Page page4 = webClient.getPage(webRequest2);
			System.out.println(page4.getWebResponse().getContentAsString());
			net.sf.json.JSONObject fromObject = net.sf.json.JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("totalcount");
			
		    String urlpay2="http://www.tlzfgjj.com:8889/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2014-01-01&jsrq=2018-01-24&grxx=249383";            
			Page page3 = webClient.getPage(urlpay2);
			System.out.println(page3.getWebResponse().getContentAsString());
			if(page3.getWebResponse().getContentAsString().contains("true"))
			{
				net.sf.json.JSONObject fromObject2 = net.sf.json.JSONObject.fromObject(page3.getWebResponse().getContentAsString());
				String string2 = fromObject2.getString("results");
				
			}
			
			
			
			
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
			options.put("probability", "true");
			JSONObject res = client.custom(image, "1d0a371444e8fda1006618447577d14f", options);
			System.out.println(res.toString(2));
			return res.toString(2);
		}
		
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
			String path="C:\\Users\\Administrator\\Desktop\\"; 
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
