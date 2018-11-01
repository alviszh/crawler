package app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

/**
 * @description:改变了验证码请求方式（用的是模拟点的方式）但是要注意的是，鄂尔多斯公积金在断点调试的时候不能正确响应数据，直接运行才可以
 * @author: sln 
 * @date: 2018年1月22日 上午11:30:04 
 */
public class HtmlUnitSuccess {
	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String url="http://www.ordosgjj.com:8088/(S(iou4ov45shntcl45smbomwij))/login.aspx";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			/**
			 * getNewWebClient start
			 */
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			/**
			 * getNewWebClient end
			 */
//			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			
			/**
			 * 获取登录时中间参数 start
			 */
			String path = hPage.getUrl().getPath(); 
			path = path.substring(1,path.lastIndexOf("/"));
			System.out.println(path);
			/**
			 * 获取登录时中间参数 end
			 */
			if(null!=hPage){
				int statusCode = hPage.getWebResponse().getStatusCode();
				if(500!=statusCode){
					System.out.println("网站可以正常使用");
					
					/**
					 * 删除 用不上这么费劲 start
					 */
					//请求验证码图片
//					url="http://www.ordosgjj.com:8088/(S(xnuxzwihw4wn1czuax1urrnp))/ValidateCode.aspx";
//					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//					Page page=webClient.getPage(webRequest);
//					if(page!=null){
//						getImagePath(page);
//					}
					/**
					 * 删除 用不上这么费劲 end
					 */
					
					/**
					 * 图片验证码 start(有时候登录的时候无法将验证码图片加载出来)
					 * 
					 * 之前刷不出来图片验证码的时候，模拟点击的是页面上图片验证码的位置，实际上应该模拟点击页面源代码验证码生成的那个a标签链接
					 */
					HtmlAnchor validatecode = (HtmlAnchor)hPage.getFirstByXPath("//*[@id=\"form1\"]/dl/dd[3]/a");  
					hPage = validatecode.click();
					HtmlImage image = hPage.getFirstByXPath("//*[@id='ImageCheck']");
					String imageName = "111.jpg";
					File file = new File("D:\\img\\" + imageName);
					try {
						image.saveAs(file);
					} catch (Exception e) {
						System.out.println("图片有误");
					}
					/**
					 * 图片验证码  end
					 */
					
					HtmlTextInput loginName = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='txtUsername']"); 
					HtmlPasswordInput loginPassword = (HtmlPasswordInput)hPage.getFirstByXPath("//input[@id='txtUserpass']"); 
					HtmlTextInput validateCode = (HtmlTextInput)hPage.getFirstByXPath("//input[@id='yzm']"); 
					HtmlSubmitInput submitbt = (HtmlSubmitInput)hPage.getFirstByXPath("//input[@id='Button1']"); 
				//	System.out.println(submitbt.asXml());   //登录		
					loginName.setText("mcx0242"); 
					loginPassword.setText("mcx0242"); 	
					String code = JOptionPane.showInputDialog("请输入验证码……"); 
					validateCode.setText(code); 	
					hPage = submitbt.click(); 
					if(null!=hPage){
						statusCode = hPage.getWebResponse().getStatusCode();
						if(200==statusCode){
							String html=hPage.asXml();
							System.out.println("模拟点击登陆后获取的页面是："+html);
							/**
							 * 获取弹框内容,你也可以按照自己的方式写 start
							 */
							String msg=WebCrawler.getAlertMsg();
							System.out.println("login:msg------------------------------------------"+msg);
							/**
							 * 获取弹框内容,你也可以按照自己的方式写 end
							 */
							if("登陆成功！".equals(msg)){
								String url111 = "http://www.ordosgjj.com:8088/"+path+"/PersonalAccumulationMoney/Personalinformation.aspx";
								webRequest = new WebRequest(new URL(url111), HttpMethod.GET);
								Page page= webClient.getPage(webRequest);
								if(page!=null){
									html=page.getWebResponse().getContentAsString();
									System.out.println("获取的用户信息的页面是："+html);
									
									Document doc = Jsoup.parse(html);
									String text = doc.getElementById("GridView1").text();
									System.out.println("获取的table中的信息是："+text);
									
									text = doc.getElementById("GridView1").getElementsByTag("tr").text();
									System.out.println("获取的tr中的信息是："+text);
									
									//获取如下属性中的内容，作为
									String attr = doc.getElementById("GridView1").getElementsByTag("tr").get(1).getElementsByTag("td").get(6).getElementsByTag("a").get(0).attr("href");
									System.out.println("获取的链接参数是："+attr);
								}
								
								
							}
							//正式写登录代码的时候，此处需要加tracer日志，用于记录调研时候没出现登录错误的结果
							else if(html.contains("alert")){
								if(html.contains("验证码输入错误")){
									System.out.println("验证码输入错误");
								}else if(html.contains("密码错误")){
									System.out.println("密码错误");
								}else if(html.contains("用户名不存在，请重新输入")){
									System.out.println("用户名不存在，请重新输入");
								}else if(html.contains("数据库连接失败")){
									System.out.println("数据库连接失败");
								}else{
									System.out.println("出现了其他登录错误，模拟点击后的页面是："+html);
								}
							}
							else{
//								
								}
							}
						}else{
							System.out.println("响应的状态码不是200，请重新登录");
						}
				}else{
					System.out.println("网站升级维护中");
					String msg=hPage.getWebResponse().getStatusMessage();   //获取得到的网站信息Internal Server Error
					System.out.println("网站升级维护中，响应的信息是："+msg);
				}
			}
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}
	//利用IO流保存验证码成功后，返回验证码图片保存路径
		public static String getImagePath(Page page) throws Exception{
			File imageFile = getImageCustomPath();
			String imgagePath = imageFile.getAbsolutePath(); 
			InputStream inputStream = page.getWebResponse().getContentAsStream();
			FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
			if (inputStream != null && outputStream != null) {  
		        int temp = 0;  
		        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
		        	outputStream.write(temp);   // 边读边写  
		        }  
		        outputStream.close();  
		        inputStream.close();   // 关闭输入输出流  
		    }
			return imgagePath;
		}
		//创建验证码图片保存路径
		public static File getImageCustomPath() {
			String path="D:\\img\\";
//			if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
//				path = System.getProperty("user.dir") + "/verifyCodeImage/";
//			} else {
//				path = System.getProperty("user.home") + "/verifyCodeImage/";
//			}
			File parentDirFile = new File(path);
			parentDirFile.setReadable(true); //
			parentDirFile.setWritable(true); //
			if (!parentDirFile.exists()) {
				System.out.println("==========创建文件夹==========");
				parentDirFile.mkdirs();
			}
//			String imageName = UUID.randomUUID().toString() + ".jpg";
			String imageName = "image.jpg";
			File codeImageFile = new File(path + "/" + imageName);
			codeImageFile.setReadable(true); //
			codeImageFile.setWritable(true, false); //
			return codeImageFile;
		}
}
