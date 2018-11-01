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
/**
 * 该测试类用于调研缴费信息(测试是不是需以第一个链接为前提)
 * @author sln
 *
 */
public class LoginTest2 {
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
						if(html.contains("欢迎您")){
					    	System.out.println("登录成功，欢迎来到首页面");
							//获取用户缴费信息
					    	url="http://222.172.223.90:8081/kmnbp/command.summer?uuid=1516933677952";
					    	webRequest = new WebRequest(new URL(url), HttpMethod.POST);
					    	requestBody=""
					    			+ "%24page=%2Fydpx%2F70000002%2F700002_01.ydpx"
					    			+ "&_ACCNUM=113115822711"
					    			+ "&_RW=w"
					    			+ "&_PAGEID=step1"
					    			+ "&_IS=-2361238"
//					    			+ "&_UNITACCNAME="
//					    			+ "&_LOGIP=20180126102549856"
//					    			+ "&_ACCNAME=%E6%99%AE%E8%8F%8A%E7%8E%89"
					    			+ "&isSamePer=false"
					    			+ "&_PROCID=70000002"
//					    			+ "&_SENDOPERID=532323199107020544"
//					    			+ "&_DEPUTYIDCARDNUM=532323199107020544"
//					    			+ "&_SENDTIME=2018-01-26"
					    			+ "&_BRANCHKIND=0"
//					    			+ "&_SENDDATE=2018-01-26"
//					    			+ "&CURRENT_SYSTEM_DATE=2018-01-26"
					    			+ "&_TYPE=init"
					    			+ "&_ISCROP=0"
					    			+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
					    			+ "&_WITHKEY=0"
					    			+ "&begdate=2000-01-01"
					    			+ "&enddate=2018-12-31"
//					    			+ "&year=2017"
					    			+ "&accnum=113115822711";
						 	webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");			
					    	webRequest.setRequestBody(requestBody);
//					    	webRequest.setAdditionalHeader("Host", "222.172.223.90:8081");
//							webRequest.setAdditionalHeader("Origin", "http://222.172.223.90:8081");
//							webRequest.setAdditionalHeader("Referer", "http://222.172.223.90:8081/kmnbp/init.summer?_PROCID=70000002");
					    	page = webClient.getPage(webRequest);
							if(null!=page){
								html=page.getWebResponse().getContentAsString();
								System.out.println("获取缴费信息所用的第一个页面是："+html);
								
								url="http://222.172.223.90:8081/kmnbp/dynamictable?uuid=1516933678761";
						    	webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						    	requestBody=""
						    			+ "dynamicTable_id=datalist2"
						    			+ "&dynamicTable_currentPage=0"
						    			+ "&dynamicTable_pageSize=300"
						    			+ "&dynamicTable_nextPage=1"
						    			+ "&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx"
						    			+ "&dynamicTable_paging=true"
						    			+ "&dynamicTable_configSqlCheck=0"
						    			+ "&errorFilter=1%3D1"
						    			+ "&begdate=2000-01-01"
						    			+ "&enddate=2018-12-31"
//						    			+ "&year=2017"
						    			+ "&accnum=113115822711"
						    			+ "&_APPLY=0"
						    			+ "&_CHANNEL=1"
						    			+ "&_PROCID=70000002"
						    			+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AJ1zZWxlY3QgaW5zdGFuY2VudW0sIHRyYW5zZGF0ZSwgdW5p%0AdGFjY251bTEsIGFtdDEsIGFtdDIsIGFtdDMsIGJlZ2luZGF0ZSwgZW5kZGF0ZSxpbnN0Y29kZSxv%0AcGVyIGZyb20gZHAwNzcgd2hlcmUgaW5zdGFuY2VudW0gPS0yMzYxMjM4IG9yZGVyIGJ5IHRyYW5z%0AZGF0ZSBkZXNjeA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AAdf%0AQUNDTlVNdAAMMTEzMTE1ODIyNzExdAADX1JXdAABd3QAC19VTklUQUNDTlVNcHQAB19QQUdFSUR0%0AAAVzdGVwMXQAA19JU3NyAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2%0AYS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F9v4anQADF9VTklUQUNDTkFNRXQAAHQABl9M%0AT0dJUHQAETIwMTgwMTI2MTAyNTQ5ODU2dAAIX0FDQ05BTUV0AAnmma7oj4rnjol0AAlpc1NhbWVQ%0AZXJ0AAVmYWxzZXQAB19QUk9DSUR0AAg3MDAwMDAwMnQAC19TRU5ET1BFUklEdAASNTMyMzIzMTk5%0AMTA3MDIwNTQ0dAAQX0RFUFVUWUlEQ0FSRE5VTXEAfgAadAAJX1NFTkRUSU1FdAAKMjAxOC0wMS0y%0ANnQAC19CUkFOQ0hLSU5EdAABMHQACV9TRU5EREFURXQACjIwMTgtMDEtMjZ0ABNDVVJSRU5UX1NZ%0AU1RFTV9EQVRFcQB%2BACF0AAVfVFlQRXQABGluaXR0AAdfSVNDUk9QcQB%2BAB90AAlfUE9SQ05BTUV0%0AABjkuKrkurrmmI7nu4bkv6Hmga%2Fmn6Xor6J0AAdfVVNCS0VZcHQACF9XSVRIS0VZcQB%2BAB94dAAI%0AQFN5c0RhdGV0AAdAU3lzRGF5dAAJQFN5c01vbnRodAAIQFN5c1RpbWV0AAhAU3lzV2Vla3QACEBT%0AeXNZZWFy";
								webRequest.setRequestBody(requestBody);
								webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//								webRequest.setAdditionalHeader("Host", "222.172.223.90:8081");
//								webRequest.setAdditionalHeader("Origin", "http://222.172.223.90:8081");
//								webRequest.setAdditionalHeader("Referer", "http://222.172.223.90:8081/kmnbp/command.summer?uuid=1516931995669");
								page = webClient.getPage(webRequest);
								if(null!=page){
									html=page.getWebResponse().getContentAsString();
									System.out.println("获取缴费信息所用的第二个页面是："+html);
								}
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
