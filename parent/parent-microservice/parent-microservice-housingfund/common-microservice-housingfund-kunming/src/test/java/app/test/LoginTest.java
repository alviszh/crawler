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
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

public class LoginTest {
	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			String url="http://222.172.223.90:8081/kmnbp/index.jsp?flg=1";
			WebRequest  webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();	
			HtmlPage hPage = webClient.getPage(webRequest);
			if(null!=hPage){
				//获取图片验证码
				url="http://222.172.223.90:8081/kmnbp/vericode.jsp";
				webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				if(page!=null){
					getImagePath(page);
				}
				String code = JOptionPane.showInputDialog("请输入验证码……"); 
				url="http://222.172.223.90:8081/kmnbp/per.login";
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				String requestBody=""
						+ "certinum=532323199107020544"
						+ "&unitcode="
						+ "&devcode="
						+ "&pwd=910702"
						+ "&vericode="+code+"";
				webRequest.setRequestBody(requestBody);
				page = webClient.getPage(webRequest);
				if(null!=page){
						String html=page.getWebResponse().getContentAsString();
//						System.out.println("模拟点击登陆后获取的页面是："+html);
						if(html.contains("欢迎您")){
					    	System.out.println("登录成功，欢迎来到首页面");
					    	//获取用户基本信息
					    	url="http://222.172.223.90:8081/kmnbp/init.summer?_PROCID=70000013";
					    	webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					    	page = webClient.getPage(webRequest);
					    	if(page!=null){
					    		html=page.getWebResponse().getContentAsString();
								System.out.println("获取用户信息所用的第一个页面是："+html);
								//获取公积金账号
								Document doc = Jsoup.parse(html);
								String accnum = doc.getElementById("accnum").val();
								
								
								
								url="http://222.172.223.90:8081/kmnbp/command.summer?uuid=1516867227259";
								webRequest = new WebRequest(new URL(url), HttpMethod.POST);
								/*通过类似网站的开发，发现的一个规律是：如果两个请求配合才可以爬取到如下截图的信息，第一个链接返回
								的信息中通常都会带有一部分返回信息，比如本页面中已经红色标识的公积金账号，那么第二个链接中所需要的很多参数中，
								也许只有第一个链接可以返回的信息可以作为其必要的参数（加上一些其他的信息，还有一些需要爬取的信息字段）*/
								
								//如下注释内容为非必要参数，虽然响应回来的数据的个数和官网相比会少一些，但都是非必要信息
								requestBody=""
										+ "%24page=%2Fydpx%2F70000013%2F700013_01.ydpx"
										+ "&_ACCNUM="+accnum.trim()+""
//										+ "&temp_._xh%5B18%5D=18"
//										+ "&temp_.itemval%5B17%5D=%E5%9F%8E%E8%A5%BF"
										+ "&_PAGEID=step1"
//										+ "&temp_._xh%5B14%5D=14"
//										+ "&temp_.itemval%5B13%5D=%E7%9C%81%E7%9B%B4"
//										+ "&temp_.itemid%5B18%5D=00087117"
//										+ "&temp_.itemid%5B9%5D=00087110"
//										+ "&temp_._xh%5B10%5D=10"
//										+ "&temp_.itemid%5B14%5D=00087131"
										+ "&_PROCID=70000013"

										
										
//										+ "&_SENDOPERID=532323199107020544"
										
										

//										+ "&temp_.itemid%5B5%5D=00087106"
//										+ "&temp_.itemid%5B10%5D=00087111"
//										+ "&temp_._xh%5B8%5D=8"
//										+ "&_SENDDATE=2018-01-25"
//										+ "&temp_.itemval%5B8%5D=%E7%9F%B3%E6%9E%97"
//										+ "&temp_.itemid%5B1%5D=00087102"
//										+ "&temp_._xh%5B4%5D=4"
										+ "&_TYPE=init"
//										+ "&temp_.itemval%5B4%5D=%E4%B8%9C%E5%B7%9D"
//										+ "&temp__rownum=18"
//										+ "&temp_.itemval%5B18%5D=%E5%9F%8E%E5%8C%97"
										+ "&_UNITACCNAME="
//										+ "&temp_._xh%5B15%5D=15"
//										+ "&temp_.itemval%5B14%5D=%E9%93%81%E8%B7%AF"


//										+ "&_ACCNAME=%E6%99%AE%E8%8F%8A%E7%8E%89"
										
										

//										+ "&temp_._xh%5B11%5D=11"
//										+ "&temp_.itemval%5B10%5D=%E5%AF%BB%E7%94%B8"
//										+ "&temp_.itemid%5B15%5D=00087114"


										
//										+ "&_DEPUTYIDCARDNUM=532323199107020544"
										
										

//										+ "&temp_.itemid%5B6%5D=00087107"
//										+ "&temp_.itemid%5B11%5D=00087112"
//										+ "&temp_._xh%5B9%5D=9"
//										+ "&_SENDTIME=2018-01-25"
//										+ "&temp_.itemval%5B9%5D=%E5%B5%A9%E6%98%8E"
//										+ "&temp_.itemid%5B2%5D=00087103"
//										+ "&temp_._xh%5B5%5D=5&temp_.itemval%5B5%5D=%E5%AF%8C%E6%B0%91"
//										+ "&temp_._xh%5B1%5D=1&temp_.itemval%5B1%5D=%E4%B8%BB%E5%9F%8E"
//										+ "&temp_._xh%5B16%5D=16&temp_.itemval%5B15%5D=%E5%9F%8E%E4%B8%9C"
										+ "&_IS=-2356775"
//										+ "&_LOGIP=20180125155852041"
//										+ "&temp_._xh%5B12%5D=12"
//										+ "&temp_.itemval%5B11%5D=%E5%AE%9C%E8%89%AF"
										+ "&isSamePer=false"
//										+ "&temp_.itemid%5B16%5D=00087115"
//										+ "&temp_.itemid%5B7%5D=00087108"
//										+ "&temp_.itemid%5B12%5D=00087113"
//										+ "&temp_.itemid%5B3%5D=00087104"
										+ "&_BRANCHKIND=0"
//										+ "&temp_._xh%5B6%5D=6"
//										+ "&temp_.itemval%5B6%5D=%E6%99%8B%E5%AE%81"
//										+ "&CURRENT_SYSTEM_DATE=2018-01-25"
										+ "&_ISCROP=0"
										+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E5%9F%BA%E6%9C%AC%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
//										+ "&temp_._xh%5B2%5D=2"
//										+ "&temp_.itemval%5B2%5D=%E5%AE%89%E5%AE%81"
										+ "&_RW=w"
//										+ "&temp_._xh%5B17%5D=17"
//										+ "&temp_.itemval%5B16%5D=%E5%9F%8E%E5%8D%97"
//										+ "&temp_._xh%5B13%5D=13"
//										+ "&temp_.itemval%5B12%5D=%E7%9F%B3%E6%B2%B9"
//										+ "&temp_.itemid%5B17%5D=00087116"
//										+ "&temp_.itemid%5B8%5D=00087109"
//										+ "&temp_.itemid%5B13%5D=00087121"
//										+ "&temp_.itemid%5B4%5D=00087105"
//										+ "&temp_._xh%5B7%5D=7"
//										+ "&temp_.itemval%5B7%5D=%E7%A6%84%E5%8A%9D"
//										+ "&temp_._xh%5B3%5D=3"
										+ "&_WITHKEY=0"
//										+ "&temp_.itemval%5B3%5D=%E5%91%88%E8%B4%A1"
										+ "&accnum="+accnum.trim()+""
										+ "&accname="
										+ "&certinum="
										+ "&peraccstate=0"
										+ "&balance="
										+ "&lastpaydate="
										+ "&lastdrawdate="
										+ "&basenumber="
										+ "&monpaysum="
										+ "&indiprop="
										+ "&unitprop="
										+ "&unitaccnum="
										+ "&unitaccname="
										+ "&accinstcode="
										+ "&DealSeq=1";
								webRequest.setRequestBody(requestBody);
								webRequest.setAdditionalHeader("Host", "222.172.223.90:8081");
								webRequest.setAdditionalHeader("Origin", "http://222.172.223.90:8081");
								webRequest.setAdditionalHeader("Referer", "http://222.172.223.90:8081/kmnbp/init.summer?_PROCID=70000013");
								page = webClient.getPage(webRequest);
								if(null!=page){
									html=page.getWebResponse().getContentAsString();
									System.out.println("获取用户信息所用的第二个页面是："+html);
								}
								
								
								//爬取用户缴费信息
								url="";
								
								
								
					    	}
					    	
						}else{
							Document doc = Jsoup.parse(html);
							String errorMsg= doc.getElementsByClass("WTLoginError").get(0).text();  //获取页面中可能出现的错误信息
							//页面信息提示中可能存在返回这个字段，截取掉
//								String str="操作失败:进行身份校验时出错:密码错误,请检查! 返回";
							String[] split = errorMsg.split(" ");
							errorMsg=split[0];
							System.out.println("获取的错误信息是："+errorMsg);
							if(errorMsg.contains("您输入的验证码与图片不符")){
									 System.out.println("您输入的验证码与图片不符");
							}else if(errorMsg.contains("密码输入错误")){
								 System.out.println("密码输入错误");
							}else if(errorMsg.contains("无正常账户，请确认输入是否正确")){
								 System.out.println("无正常账户，请确认输入是否正确");
							//其他错误
							}else if(errorMsg.contains("验证码已超时")){
								 System.out.println("验证码已超时");
							}else{
								System.out.println("登录失败，出现了调研时没有遇到的错误："+errorMsg);
							} 
						}
				}else{
					System.out.println("网站升级维护中");
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
