package app.test;
/**
 * @description:
 * @author: sln 
 * @date: 2018年1月16日 下午5:11:31 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;
/**
 * 濮阳公积金登录测试类   webDriver的方式
 * @author sln
 *
 */
public class PuYangLoginTest {
	public static void main(String[] args) {
		try {
			String loginUrl="http://www.pygjj.gov.cn:9998/wt-web/login";
			WebRequest  webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();	
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(1000);  
			if(null!=hPage){
				HtmlImage image = hPage.getFirstByXPath("//img[@src='/wt-web/captcha']"); 
				String imageName = "111.jpg"; 
				File file = new File("D:\\img\\"+imageName); 
				image.saveAs(file); 	
				String code= JOptionPane.showInputDialog("请输入验证码……"); 
				loginUrl="http://www.pygjj.gov.cn:9998/wt-web/login";
				webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
				String requestBody="username=410928199010016042&password=0de0615f7c2e8303010047ebd4b76c90&captcha="+code+"&logintype=1";
				webRequest.setRequestBody(requestBody);
				HtmlPage logonPage = webClient.getPage(webRequest);
				if(null!=logonPage){
					String html=logonPage.asXml();
					System.out.println("模拟点击登陆后获取的页面是："+html);
					Document doc = Jsoup.parse(html);
					if(html.contains("欢迎您")){
						System.out.println("登录成功");
						//将个人基本信息表的pdf保存到本地
						String url="http://www.pygjj.gov.cn:9998/wt-web/grxxpdf/grxx";
						webRequest = new WebRequest(new URL(url), HttpMethod.GET);
						webRequest.setAdditionalHeader("Content-Type", "application/json;charset=UTF-8");
						Page page= webClient.getPage(webRequest);
						if(page!=null){
							InputStream is = page.getWebResponse().getContentAsStream();
							System.out.println("获取的个人基本信息的页面是："+html);
							String path = getImagePath(page);
							System.out.println("返回的保存个人基本信息页面的路径是："+path);
							save(is, path);
							String readFdf5 = readFdf(path);
							System.out.println("转化为txt之后的路径是："+readFdf5);
							File file5 = new File(readFdf5);
							String readTxt = readTxtFile(file5);
							System.out.println("读取出来的文件内容是："+"\r\n"+readTxt);  
							
							
							
							
						}else{
							System.out.println("不能够获取个人信息基本页面");
						}
					}else if(html.contains("in_error")){
						String errorMsg = doc.getElementById("in_error").text();
						if(errorMsg.contains("验证码错误")){
							System.out.println("验证码错误");
						}else if(errorMsg.contains("密码格式不正确")){
							System.out.println("密码格式不正确");
						}else if(errorMsg.contains("个人密码错误")){
							System.out.println("个人密码错误");
						}else if(errorMsg.contains("身份证格式错误")){
							System.out.println("身份证格式错误");
						}else{
							System.out.println("出现了其他登录错误："+errorMsg);
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
		
	}
	
	
//	=====================================================
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
//				if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
//					path = System.getProperty("user.dir") + "/verifyCodeImage/";
//				} else {
//					path = System.getProperty("user.home") + "/verifyCodeImage/";
//				}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
//				String imageName = UUID.randomUUID().toString() + ".jpg";
		String imageName = "grxx.pdf";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}

	
//	=====================================================
	
	
	public static void save(InputStream inputStream, String filePath) throws Exception{ 

		OutputStream outputStream = new FileOutputStream(filePath); 

		int byteCount = 0; 

		byte[] bytes = new byte[1024]; 

		while ((byteCount = inputStream.read(bytes)) != -1) 
		{ 
			outputStream.write(bytes, 0, byteCount); 

		} 
		inputStream.close(); 
		outputStream.close(); 
	} 
	
	
	//读取txt文本文件
	public static String readFdf(String file) throws Exception {    
		// 是否排序    
		boolean sort = false;
		// pdf文件名    
		String pdfFile = file;
		// 输入文本文件名称    
		String textFile = null;
		// 编码方式    
		String encoding = "UTF-8";    
		// 开始提取页数    
		int startPage = 1;    
		// 结束提取页数    
		int endPage = Integer.MAX_VALUE;    
		// 文件输入流，生成文本文件    
		Writer output = null;    
		// 内存中存储的PDF Document    
		PDDocument document = null;    
		try {    
			try {    
				// 首先当作一个URL来装载文件，如果得到异常再从本地文件系统//去装载文件    
				URL url = new URL(pdfFile);    
				//注意参数已不是以前版本中的URL.而是File。    
				document = PDDocument.load(pdfFile);    
				// 获取PDF的文件名    
				String fileName = url.getFile();    
				// 以原来PDF的名称来命名新产生的txt文件    
				if (fileName.length() > 4) {    
					File outputFile = new File(fileName.substring(0, fileName    
							.length() - 4)    
							+ ".txt");
					textFile = outputFile.getName();    
				}    
			} catch (MalformedURLException e) {    
				// 如果作为URL装载得到异常则从文件系统装载    
				//注意参数已不是以前版本中的URL.而是File。    
				document = PDDocument.load(pdfFile);    
				if (pdfFile.length() > 4) {    
					textFile = pdfFile.substring(0, pdfFile.length() - 4)    
							+ ".txt";    
				}    
			}    
			// 文件输入流，写入文件倒textFile    
			output = new OutputStreamWriter(new FileOutputStream(textFile),    
					encoding);    
			// PDFTextStripper来提取文本    
			PDFTextStripper stripper = null;    
			stripper = new PDFTextStripper();    
			// 设置是否排序    
			stripper.setSortByPosition(sort);    
			// 设置起始页    
			stripper.setStartPage(startPage);    
			// 设置结束页    
			stripper.setEndPage(endPage);    
			// 调用PDFTextStripper的writeText提取并输出文本    
			stripper.writeText(document, output);    
			return textFile;
		} finally {    
			if (output != null) {    
				// 关闭输出流    
				output.close();    
			}    
			if (document != null) {    
				// 关闭PDF Document    
				document.close();    
			}    
		}    
	}
	//将pdf读取为txt文件
	public static String readTxtFile(File fileName)throws Exception{  
		String result=null;  
		FileReader fileReader=null;  
		BufferedReader bufferedReader=null;  
		try{  
			fileReader=new FileReader(fileName);  
			bufferedReader=new BufferedReader(fileReader);  
			try{  
				String read=null;  
				while((read=bufferedReader.readLine())!=null){  
					result=result+read+"\r\n";  
				}  
			}catch(Exception e){  
				e.printStackTrace();  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			if(bufferedReader!=null){  
				bufferedReader.close();  
			}  
			if(fileReader!=null){  
				fileReader.close();  
			}  
		}  

		return result;  
	} 
}
