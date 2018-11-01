package Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;

public class TestLogin {

	
	public static void main(String[] args)  throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String urlimg="https://gt3app9.tax861.gov.cn/Gt3GsWeb/RandomCode";
		WebRequest webRequest1 = new WebRequest(new URL(urlimg), HttpMethod.GET);
		Page page = webClient.getPage(webRequest1);
		String imgagePath=getImagePath(page,"D:\\img");
		ChaoJiYingOcrService chaoJiYingOcrService = new ChaoJiYingOcrService();
		
		String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1902");  
		
		String url="https://gt3app9.tax861.gov.cn/Gt3GsWeb/gsmxwyNo/YhdlAction.action?code=login";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		String a="zjlx=201&zzhm=340602199307040416&xm=%E6%9D%A8%E7%A3%8A&password=yl123456&mmhg=1&yzm="+code+"&oldMm=&newMm=&mmComf=";
		webRequest.setRequestBody(a);
		HtmlPage page1 = webClient.getPage(webRequest);	
//		System.out.println(page1.getWebResponse().getContentAsString());

		String url1="https://gt3app9.tax861.gov.cn/Gt3GsWeb/gsmxwyNo/GrnsxxcxAction.action?code=query&tijiao=grsbxxcx&actionType=query&index=&skssksrqN=2017&skssksrqY=1&skssjsrqN=2018&skssjsrqY=8&sbbmc=";
		WebRequest webRequest11 = new WebRequest(new URL(url1), HttpMethod.POST);

//		String aa="code=querytijiao=grsbxxcx&actionType=query&index=&skssksrqN=2017&skssksrqY=1&skssjsrqN=2018&skssjsrqY=8&sbbmc=";
//		webRequest11.setRequestBody(aa);
		Page page2 = webClient.getPage(webRequest11);
		System.out.println("--------------"+page2.getWebResponse().getContentAsString());
	}
	
	public static  String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = 111 + ".jpg";
		File codeImageFile = new File(imagePath + "/" + imageName);
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false);
		////////////////////////////////////////
		
		String imgagePath = codeImageFile.getAbsolutePath(); 
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
}
