//package app.service;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import com.crawler.bank.json.BankJsonBean;
//import com.crawler.bank.json.BankStatusCode;
//import com.gargoylesoftware.htmlunit.HttpMethod;
//import com.gargoylesoftware.htmlunit.Page;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebRequest;
//import com.gargoylesoftware.htmlunit.html.HtmlElement;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
//import com.microservice.dao.entity.crawler.telecom.phone.bank.BankAddressa;
//import com.microservice.dao.repository.crawler.telecom.phone.bank.BankAddressRepository;
//import com.module.htmlunit.WebCrawler;
//import com.module.jna.webdriver.WebDriverUnit;
//
//@Component
//@Service
//@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.phone.bank")
//@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.phone.bank")
//public class BankAddress {
//	public WebDriver driver;
//	@Autowired
//	private ChaoJiYingOcrService chaoJiYingOcrService;
//	@Autowired
//	private BankAddressRepository bankAddressRepository;
//	public String getBank() throws Exception{
//		String url = "http://www.dianhua.cn/citylist";
//		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
//		webClient.getOptions().setJavaScriptEnabled(false);
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//		HtmlPage searchPage1= webClient.getPage(webRequest);
//		String html = searchPage1.getWebResponse().getContentAsString();
//		Document doc = Jsoup.parse(html);
//		Elements ele1 = doc.select("dl.clearfix > dd > a");
//		if(ele1.size()>0){
//			WebDriver webDriver = openloginCmbChina(); 
////			List<String> list = new ArrayList<String>();
//			for(int j = 243;j < ele1.size();j++){
//				System.out.println(j);
//				String url1 = ele1.get(j).attr("href").trim();
//				for(int i = 1;i <= 10;i++){
//					String urls = null;
//					urls = "http://www.dianhua.cn"+url1+"/c397"+"/p"+i;
//					
//					webDriver.get(urls);
////					WebRequest webRequest1 = new WebRequest(new URL(urls), HttpMethod.GET);
////					HtmlPage searchPage2= webClient.getPage(webRequest1);
////					String html1 = searchPage2.getWebResponse().getContentAsString();
//					String html1 = webDriver.getPageSource();
//					if(html1.contains("请输入验证码继续访问")){
//						System.out.println("请输入验证码继续访问");
////						String codeurl = "http://www.dianhua.cn/auth/captcha.php?r=0.8713416072111273";
////						WebRequest webRequest2 = new WebRequest(new URL(codeurl), HttpMethod.GET);
////						Page html2 = webClient.getPage(webRequest2);
////						String path = getPathBySystem();
////						String imgagePath= getImagePath(html2,path);
////						System.out.println(imgagePath);
////						String verifycode = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "5000"); 
////						System.out.println(verifycode);
//						
////						HtmlTextInput aa = searchPage2.getFirstByXPath("/html/body/div[1]/div[3]/div/div[2]/form/p/input[1]");
////						aa.setText(verifycode);
////						HtmlElement button = searchPage2.getFirstByXPath("/html/body/div[1]/div[3]/div/div[2]/form/p/input[2]");
////						searchPage2 = button.click();
////						String urli = "http://www.dianhua.cn/auth/code.php?code="+verifycode+"";
////						WebRequest webRequest4 = new WebRequest(new URL(urli), HttpMethod.GET);
////						HtmlPage searchPage4= webClient.getPage(webRequest4);
////						WebRequest webRequest3 = new WebRequest(new URL(urls), HttpMethod.GET);
////						HtmlPage searchPage3= webClient.getPage(webRequest3);
////						html1 = searchPage3.getWebResponse().getContentAsString();
////						System.out.println(html1);
////						if(html1.contains("该网页无法正常运作")){
////							System.out.println("该网页无法正常运作");
////							
////
////						}	
//						Thread.sleep(50000);
//						html1 = webDriver.getPageSource();
//						if(!html1.contains("该网页无法正常运作")||!html1.contains("请输入验证码继续访问")){
//							System.out.println("1111");
//						}	
//						
//					}
//					Document doc1 = Jsoup.parse(html1);
//					Elements ele2 = doc1.select("div.c_right_list > dl");
//					if(ele2.size()>0){
//						for(int k = 2;k<=ele2.size()+1;k++){
//							Elements ele3 = doc1.select("div.c_right_list:nth-child("+k+") > dl  h5");
//							Elements ele4 = doc1.select("div.c_right_list:nth-child("+k+") > dl   div");
//							Elements ele5 = doc1.select("div.c_right_list:nth-child("+k+") > dl   dt._right p");
//							String name = ele3.text().trim();
//							String phone = ele4.get(0).text().trim();
//							String regEx = "[a-zA-Z\u4e00-\u9fa5]";      
////					        String str = "中文fdas313afasfs42342 ";      
//					        Pattern p = Pattern.compile(regEx);      
//					        Matcher m = p.matcher(phone);      //p.matcher()只适合做
//					        String repickStr = m.replaceAll("");
//							String address = ele4.get(1).text().trim();
//							String city = null;
//							if(ele5.size()>1){
//								city = ele5.get(1).text().trim();
//							}else{
//								city = ele5.text().trim();
//							}
//							
//							BankAddressa bank = new BankAddressa();
//							bank.setName(name);
//							bank.setPhone(repickStr);
//							bank.setAddress(address);
//							bank.setCty(city);
//							bankAddressRepository.save(bank);
//						}
//						if(ele2.size()!=10){
//							break;
//						}
//					}		
//					
//				}
//			}
//		}
//		return null;
//	}
//	
//	
//    public WebDriver openloginCmbChina()throws Exception{ 
//		
//		//driver.manage().window().maximize();
//		
//			System.out.println("launching chrome browser");
//			System.setProperty("webdriver.chrome.driver", "D:\\zhaohui\\chromedriver.exe");
//			try {
//			
//			ChromeOptions chromeOptions = new ChromeOptions();
//			chromeOptions.addArguments("disable-gpu"); 
//
//			driver = new ChromeDriver(chromeOptions);
//			 
//			driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
//			driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
//			
//			
//		    driver.manage().window().maximize();
//				
//				return driver;
//			} catch (Exception e) {
//				System.out.println("网络超时");
//				
//			}
//		
////		tracerLog.addTag("WebDriverChromeService loginCmbChina Msg", "兴业银行登陆页加载已完成,当前页面句柄"+driver.getWindowHandle());
//		return null;
//		
//	} 
//	
//	
//	public  String getPathBySystem() {
//
//		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
//			String path = System.getProperty("user.dir") + "/snapshot/";
//			System.out.println("path:"+path);
//			return path;
//		} else {
//			String path = System.getProperty("user.home") + "/snapshot/";
//			System.out.println("path:"+path);
//			return path;
//		}
//
//	}
//	
//	public File getImageLocalPath() {
//		File parentDirFile = new File("D:/home/img");
//		parentDirFile.setReadable(true); //
//		parentDirFile.setWritable(true); //
//
//		if (!parentDirFile.exists()) {
//			parentDirFile.mkdirs();
//		}
//
//		// String imageName = uuid + ".png";
//		String imageName = UUID.randomUUID().toString() + ".png";
//
//		File codeImageFile = new File("D:/home/img" + "/" + imageName);
//		codeImageFile.setReadable(true); //
//		codeImageFile.setWritable(true); //
//
//		return codeImageFile;
//
//	}
//	
//	/**
//	 * 指定图片验证码保存的路径和随机生成的名称，拼接在一起	
//	 * 利用IO流保存验证码成功后，将完整路径信息一并返回
//	 * 
//	 * @param page
//	 * @param imagePath
//	 * @return
//	 * @throws Exception
//	 */
//	public  String getImagePath(Page page,String imagePath) throws Exception{
//		File parentDirFile = new File(imagePath);
//		parentDirFile.setReadable(true);
//		parentDirFile.setWritable(true); 
//		if (!parentDirFile.exists()) {
//			System.out.println("==========创建文件夹==========");
//			parentDirFile.mkdirs();
//		}
//		String imageName = UUID.randomUUID().toString() + ".png";
//		File codeImageFile = new File(imagePath + "/" + imageName);
//		codeImageFile.setReadable(true); 
//		codeImageFile.setWritable(true, false);
//		////////////////////////////////////////
//		
//		String imgagePath = codeImageFile.getAbsolutePath(); 
//		InputStream inputStream = page.getWebResponse().getContentAsStream();
//		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath))); 
//		if (inputStream != null && outputStream != null) {  
//	        int temp = 0;  
//	        while ((temp = inputStream.read()) != -1) {    // 开始拷贝  
//	        	outputStream.write(temp);   // 边读边写  
//	        }  
//	        outputStream.close();  
//	        inputStream.close();   // 关闭输入输出流  
//	    }
//		return imgagePath;
//	}
//
//}
