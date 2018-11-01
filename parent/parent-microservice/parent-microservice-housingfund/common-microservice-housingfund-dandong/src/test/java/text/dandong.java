package text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.service.common.LoginAndGetCommon;

public class dandong{

	public static void main(String[] args) throws Exception {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url = "https://223.100.185.98/wt-web/logout";

		HtmlPage page = getHtml(url, webClient);

		HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
		HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("in_password");//密码
		HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码

		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");


		username.setText("210604196304021092");
		pass.setText("111111");
		captcha.setText(inputValue);

		HtmlButton login = (HtmlButton) page.getElementById("gr_login");
		Page page2 = login.click();

		String html = page2.getWebResponse().getContentAsString();

		System.out.println(html);
		if(html.indexOf("加载中 ...")!=-1){
			
			
//			String url3 = "https://223.100.185.98/wt-web/person/dzmx?lsnd=%E5%BD%93%E5%89%8D%E5%B9%B4%E5%BA%A6";
//			Page page5 = LoginAndGetCommon.getHtml(url3, webClient);
//			InputStream contentAsStream3 = page5.getWebResponse().getContentAsStream();
//			save1(contentAsStream3, "f:\\file\\a.pdf");
//			readFdf("f:\\file\\a.pdf");
//			File file1 = new File("f:\\file\\a.txt");
//			String readTxtFile = readTxtFile(file1);
//			System.out.println("读取出来的文件内容是："+"\r\n"+readTxtFile);  
			
			
			//		String url2 = "https://223.100.185.98/wt-web/person/bgcx";// https://223.100.185.98/wt-web/person/bgcx
			//		webClient.addRequestHeader("Host", "223.100.185.98");
			//		webClient.addRequestHeader("Origin", "http://223.100.185.98");
			//		webClient.addRequestHeader("Referer", "https://223.100.185.98/wt-web/home?logintype=1");
			//		Page page3 = gethtmlPost(webClient, null, url2);
			//		String html2 = page3.getWebResponse().getContentAsString();//个人信息(OK)
			//		System.out.println(html2);




			//		String url5  = "https://223.100.185.98/wt-web/person/jcqqxx";
			//		Page page5 = getHtml1(url5, webClient);
			//		InputStream contentAsStream = page5.getWebResponse().getContentAsStream();
			//		//String c= "C:\\Users\\Administrator\\git\\strong-auth-crawler\\parent\\parent-microservice\\parent-microservice-housingfund\\common-microservice-housingfund-cangzhou\\abc.txt";
			//		save1(contentAsStream, "f:\\abc.pdf");
			//		File file1 = new File("f:\\abc.pdf");
			//		String readTxtFile = text.login2.readTxtFile(file1);
			//		System.out.println("读取出来的文件内容是："+"\r\n"+readTxtFile); 
			/**
			 * 借款信息
//		String url4 = "https://223.100.185.98/wt-web/person/dkhkcx";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("htbh", "20140100567"));
//		paramsList.add(new NameValuePair("beginDate", "2000-01-01"));
//		paramsList.add(new NameValuePair("endDate", "2017-12-11"));
//		paramsList.add(new NameValuePair("userId", "1"));
//		paramsList.add(new NameValuePair("pageNum", "1"));
//		paramsList.add(new NameValuePair("pageSize", "100"));
//		
//		Page page4 = gethtmlPost(webClient, paramsList, url4);
//		String html4 = page4.getWebResponse().getContentAsString();//信息(OK)
//		System.out.println(html4);

			 */



			//		String url3 = "https://223.100.185.98/wt-web/person/jcmx?yhkh=2017-07-01";
			//		text.login2.saveUrlAs(url3, "f:\\a.pdf", "GET");
			//		text.login2.readFdf("f:\\b.pdf");
			//		File file1 = new File("f:\\b.txt");
			//		String readTxtFile = text.login2.readTxtFile(file1);
			//		System.out.println("读取出来的文件内容是："+"\r\n"+readTxtFile);  


			String url1="https://223.100.185.98/wt-web/person/jcmx?yhkh=2000-01-01,2018-06-30";
			Page page3 = getHtml1(url1, webClient);
			InputStream contentAsStream = page3.getWebResponse().getContentAsStream();
			save1(contentAsStream, "f:\\file\\a.pdf");
			readFdf("f:\\file\\a.pdf");
			File file1 = new File("f:\\file\\a.txt");
			String readTxtFile = readTxtFile(file1);
			System.out.println("读取出来的文件内容是："+"\r\n"+readTxtFile);  

			String[] split = readTxtFile.split("\n");
			for(int i=3;i<split.length-2;i++){
				String liushuihao = null;
				String jiefan = null;
				String daifan = null;
				String yu = null;
				String zai1 = null;
				String ri = null;
				
				String string = split[i];
				String[] split2 = string.split(" ");
				if(split2.length>5){
					if(split2.length!=7){
					liushuihao = split2[0].substring(0, 20);//业务流水号
					jiefan = split2[2];//借方金额
					daifan = split2[3];//贷方金额
					yu = split2[4];//余额
					zai1 = split2[5];//摘要
					if(split2.length>6){
						zai1 = zai1 + split2[split2.length-1].toString();
					}
					String[] split3 = split2[1].split("-");
					String string2 = split3[1];
					ri = string2+ "-" + split3[2].toString();//日期
					}
				}
				if(split2.length<6){
					if(split2.length!=4&&split2.length!=5){
					liushuihao = split2[0].substring(0, 20);//业务流水号
					jiefan = split2[1];//借方金额
					daifan = split2[2];//贷方金额
					yu = split2[3];//余额
					zai1 = split2[4];//摘要
					String[] split3 = split2[0].split("-");
					String string2 = split3[1];
					ri = string2+ "-" + split3[2].toString();//日期
					}
					if(split2.length==4){
						//[11006170101_002658462010-06-17, 0.00, 550.00, 11,568.69补缴201001至201005公积金]
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String[] split3 = split2[0].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
						jiefan = split2[1];//借方金额
						daifan = split2[2];//贷方金额
						String string3 = split2[3];
						String[] split4 = string3.split("\\.");
						String substring = split4[1].substring(0, 2);
						yu = split4[0] + "."+substring;
						zai1 = split4[1].substring(2, split4[1].length()-1);
					}
					if(split2.length==5&&split2[0].length()<27){
						//[11605050101_0045219520, 6-05-05, 1,540.68, 0.00, 56,417.36部分提取(逐月提取公积金]	
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String[] split3 = split2[1].split("-");
						String string2 = split3[1];
						ri = string2+ "-" + split3[2].toString();//日期
						jiefan = split2[2];
						daifan = split2[3];
						String string3 = split2[4];
						String[] split4 = string3.split("\\.");
						String substring = split4[1].substring(0, 2);
						yu = split4[0] + "."+substring;
						zai1 = split4[1].substring(2, split4[1].length()-1);
						
					}
					if(split2.length==5&&split2[0].length()>27){
						liushuihao = split2[0].substring(0, 20);//业务流水号
						String substring = split2[0].substring(20, split2[0].length());
						String[] split3 = substring.split("-");
						ri = split3[1]+ "-" + split3[2].toString();//日期
						jiefan = split2[1];
						daifan = split2[2];
						yu=split2[3];
						zai1=split2[4];
					}
				}
				//10903170101_000508922 0 -03-17 0.00 604.00 31,125.33 汇缴200902公积金

				if(split2.length==7){
					liushuihao = split2[0].substring(0, 20);//业务流水号
					String[] split3 = split2[2].split("-");
					String string2 = split3[1];
					ri = string2+ "-" + split3[2].toString();//日期
					jiefan = split2[3];//借方金额
					daifan = split2[4];//贷方金额
					yu = split2[5];//余额
					zai1 = split2[6];//摘要
				}
				
				System.out.println(">>>"+i+".读取出来的文件内容是："
				+"\r\n业务流水号:"+liushuihao+"\n 日期:"+ri+"\n借方金额:"+jiefan+"\n贷方金额:"+daifan+"\n余额:"+yu+"\n摘要:"+zai1);
			}
		}
		else{
			System.out.println("登录失败");
		}
	}

	public static void save1(InputStream inputStream, String filePath) throws Exception{ 

		OutputStream outputStream = new FileOutputStream(filePath); 

		int bytesWritten = 0; 
		int byteCount = 0; 

		byte[] bytes = new byte[1024]; 

		while ((byteCount = inputStream.read(bytes)) != -1) 
		{ 
			outputStream.write(bytes, 0, byteCount); 

		} 
		inputStream.close(); 
		outputStream.close(); 
	} 


	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page getHtml1(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "223.100.185.98");
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:56.0) Gecko/20100101 Firefox/56.0");
		webRequest.setAdditionalHeader("Connection","keep-alive");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}

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

	public static void readFdf(String file) throws Exception {    
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
}
