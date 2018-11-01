package app.service.common;

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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

@Component
public class HousingFundHelperService {
	public static void main(String[] args) {
		String threeYearAgoDate = getThreeYearAgoDate();
		System.out.println(threeYearAgoDate);
	}
	public static String getPresentDate(){
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
	}
	public static String getThreeYearAgoDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -3);
        String threeYearAgo=sdf.format(c.getTime());
		return threeYearAgo;
	}
	public WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	
//=======================将登录密码RSA加密       start========================
	public static String encryptedInfo(String needEncryptedContent) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("nanchong.js", Charsets.UTF_8);   //指定用于加密的js文件
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		//加密(参数1是js中的方法名)
		Object data = invocable.invokeFunction("encryptedString",needEncryptedContent);  
		return data.toString(); 
	}
	public static String readResource(final String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
	}
//=======================将登录密码RSA加密       end========================
	
	
//==================如下代码，用于存储pdf文件， 读取用pdf转化的txt文本文件====== start ==========
	
	//根据git上指定的保存文件的路径，来保存图片验证码或者是pdf文件
	public static String getFileSavePath(String path) {
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String fileName = UUID.randomUUID().toString() + ".pdf";
		File saveFile = new File(path + "/" + fileName);
		saveFile.setReadable(true); //
		saveFile.setWritable(true, false);
		String saveFilePath = saveFile.toString();
		return saveFilePath;
	}
	//将返回的pdf文件进行存储
	public static void savePdf(InputStream inputStream, String filePath) throws Exception{ 
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
	
	//将保存成功的pdf文件进行读取，在同样的路径下以同样的名字存为txt文本文件
	public static String readFdfToTxt(String file) throws Exception {    
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
	//将转化之后的txt文本文件进行读取
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
	
//	==================如上代码，用于存储pdf文件， 读取用pdf转化的txt文本文件====== end ==========
}
