package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.jna.webdriver.WebDriverUnit;

import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yueyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yueyang")
public class LoginAndGetService extends HousingBasicService{
	//身份证
	public  Page loginByIDNUM(WebClient webClient, String name, String password) throws Exception {
		String url = "http://yygjj.gov.cn/hfmis_wt/captcha.jpg";
		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.GET);
		Page html = webClient.getPage(webRequest1);
		String path = WebDriverUnit.getPathBySystem();
		String imgagePath= getImagePath(html,path);
		System.out.println(imgagePath);
		String verifycode = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "4004"); 
		System.out.println(verifycode);
		
		String url1 = "http://yygjj.gov.cn/hfmis_wt/login";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "yygjj.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://yygjj.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://yygjj.gov.cn/hfmis_wt/login");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "username="+name+"&password="+password+"&captcha="+verifycode+"&usertype=2&captchaType=9";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
	    System.out.println(searchPage.getWebResponse().getContentAsString());
	    
		return searchPage;
		
	}
	
	/**
	 * 指定图片验证码保存的路径和随机生成的名称，拼接在一起	
	 * 利用IO流保存验证码成功后，将完整路径信息一并返回
	 * 
	 * @param page
	 * @param imagePath
	 * @return
	 * @throws Exception
	 */
	public   String getImagePath(Page page,String imagePath) throws Exception{
		File parentDirFile = new File(imagePath);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
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
	
//	//公积金账号
//	public  Page loginByCO_BRANDED_CARD(WebClient webClient, String name, String password) throws Exception {
//		String url1 = "http://yygjj.gov.cn/logon.do;jsessionid=oTKoDPHt-58s5faew0mduEd31kCbPe2bc-n7B1OwTFS4b7khGu3z!1285593134";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "yygjj.gov.cn");
//		webRequest.setAdditionalHeader("Origin", "http://yygjj.gov.cn");
//		webRequest.setAdditionalHeader("Referer", "http://yygjj.gov.cn/gjjcx.jsp");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		String requestBody = "type=2&spcode="+name+"&sppassword="+password+"&Submit=+";
//		webRequest.setRequestBody(requestBody);
//		Page searchPage = webClient.getPage(webRequest);
////	    System.out.println(searchPage.getWebResponse().getContentAsString());
//		return searchPage;
//		
//	}
//	
//	// 个人账号
//	public  Page loginByACCOUNT_NUM(WebClient webClient, String name, String password) throws Exception {
//		String url1 = "http://yygjj.gov.cn/logon.do;jsessionid=oTKoDPHt-58s5faew0mduEd31kCbPe2bc-n7B1OwTFS4b7khGu3z!1285593134";
//		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
//		webRequest.setAdditionalHeader("Host", "yygjj.gov.cn");
//		webRequest.setAdditionalHeader("Origin", "http://yygjj.gov.cn");
//		webRequest.setAdditionalHeader("Referer", "http://yygjj.gov.cn/gjjcx.jsp");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
//		String requestBody = "type=3&spcode="+name+"&sppassword="+password+"&Submit=+";
//		webRequest.setRequestBody(requestBody);
//		Page searchPage = webClient.getPage(webRequest);
////	    System.out.println(searchPage.getWebResponse().getContentAsString());
//		return searchPage;
//		
//	}
}
