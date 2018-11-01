package TestChi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class TestLogin {
	//设置APPID/AK/SK
	public static final String APP_ID = "10715647";
	public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
	public static final String SECRET_KEY = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";
		
		
	public static void main(String[] args) throws Exception{
		String url="http://cfszfgjj.com/wt-web-gr/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");
		elementById.reset();
		elementById.setText("652823198708020822");
		
		HtmlPasswordInput elementById1 = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='password']");
		elementById1.reset();
		elementById1.setText("111111");
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='profile']//form//table[1]//tbody//tr[3]//td[1]//div[2]//img");
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@id='captcha']");
		yzm.reset();
		yzm.setText(inputValue);
		
		HtmlElement button = page.getFirstByXPath("//*[@id='profile']//form//table[1]//tbody//tr[4]//td//div//button");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
		double random = Math.random();
		String url3="http://cfszfgjj.com/wt-web/person/jbxx?random="+random;
		Page page4 = webClient.getPage(url3);
		System.out.println(page4.getWebResponse().getContentAsString());
		
		String url2="http://cfszfgjj.com/wt-web/personal/jcmxlist?UserId=1&beginDate=2016-01-01&endDate=2018-01-22&userId=1&pageNum=1&pageSize=10";
		Page page3 = webClient.getPage(url2);
		Thread.sleep(1000);
		System.out.println(page3.getWebResponse().getContentAsString());
		
		 String imagePath = getImagePath(page4);
		 
		 
		// 初始化一个AipOcr
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		String sample = sample(client, imagePath);
		System.out.println(sample);
	}
	
	public static String sample(AipOcr client,String image) {
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
//		options.put("Content-Type", "application/x-www-form-urlencoded");
//		options.put("image", "true");
//		options.put("templateSign", "421bef7004708216699e2f955d1f2d43");
		
		options.put("detect_direction", "true");
		options.put("probability", "true");
		JSONObject res = client.basicAccurateGeneral(image, options);
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
}
