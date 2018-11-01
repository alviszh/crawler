package Test;

import java.awt.Robot;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.http.client.utils.URLEncodedUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.xvolks.jnative.exceptions.NativeException;

import com.baidu.aip.ocr.AipOcr;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangMedical;
import com.microservice.dao.entity.crawler.insurance.sz.heilongjiang.InsuranceSZHeiLongJiangUserInfo;
import com.module.ddxoft.VK;
import com.module.htmlunit.WebCrawler;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestLogin {
	//设置APPID/AK/SK
		public static final String APP_ID = "10715647";
		public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
		public static final String SECRET_KEY = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";
		
		
		 static String driverPath = "D:\\IEDriverServer_Win32\\IEDriverServer.exe";
			
			private static final String LEN_MIN = "0";
			private static final String TIME_ADD = "0";
			private static final String STR_DEBUG = "a";

			private static Robot robot;
			public static void main(String[] args) throws Exception {
				DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();

				System.setProperty("webdriver.ie.driver", driverPath);

				WebDriver driver = new InternetExplorerDriver();
				 
				driver = new InternetExplorerDriver(ieCapabilities);

				// 设置超时时间界面加载和js加载
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
				driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
				String baseUrl = "https://www.renshenet.org.cn/sionline/loginControler";
				driver.get(baseUrl);
				driver.manage().window().maximize();
				String pageSource = driver.getPageSource();
				//System.out.println(pageSource);
				
				WebElement username = driver.findElement(By.name("iptUserId"));
				Thread.sleep(1000);
				username.click();
				Thread.sleep(1000);
				username.clear();
				Thread.sleep(1000);
				username.sendKeys("230404199203030543");
				Thread.sleep(1000);
				driver.findElement(By.className("button")).click();
				
				String password = "wangluqsklmyt16";
//				VirtualKeyBoard.KeyPressEx(password,500);
				WebElement passwordEncoder = driver.findElement(By.id("passwordEncoder"));
				passwordEncoder.click();
				VK.KeyPress(password);

				//driver.findElement(By.className("button")).click();
				
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(60, TimeUnit.SECONDS).pollingEvery(2, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);  
				WebElement eleTriggerFunc = wait.until(new Function<WebDriver, WebElement>() {  
		             public WebElement apply(WebDriver driver) {  
		               return driver.findElement(By.className("button")); 
		             }  
				}); 
				eleTriggerFunc.click();
				
				Thread.sleep(50000);
				String html = driver.getPageSource();
				System.out.println(html);
				if (html.contains("我的首页")) {
					
					System.out.println("登录成功！");
					//System.out.println(driver.getPageSource());
					
					Set<org.openqa.selenium.Cookie> cookies = driver.manage().getCookies();
					WebClient webClient = WebCrawler.getInstance().getWebClient();
					for(org.openqa.selenium.Cookie cookie : cookies){
						Cookie cookieWebClient = new Cookie("www.renshenet.org.cn", cookie.getName(), cookie.getValue());
						System.out.println(cookieWebClient);
						webClient.getCookieManager().addCookie(cookieWebClient);
					}
					
					String accountUrl="https://www.renshenet.org.cn/sionline/search01/accountBalance.html";
					WebRequest webRequest = new WebRequest(new URL(accountUrl), HttpMethod.POST);
					String reString = "ywdm=search01&parm=accountBalance";
					webRequest.setRequestBody(reString);
					Page page7 = webClient.getPage(webRequest);
					System.out.println(page7.getWebResponse().getContentAsString());
					
					
					String url1="https://www.renshenet.org.cn/sionline/search01/init00.html";
					Page page2 = webClient.getPage(url1);
					String url2="https://www.renshenet.org.cn/sionline/search01/init.html?parm=baseInfo";
					Page page3 = webClient.getPage(url2);
					String usrUrl="https://www.renshenet.org.cn/sionline/search01/baseInfo.html?ywdm=search01&parm=baseInfo";
					Thread.sleep(2000);
					
					
					WebRequest  requestSettings = new WebRequest(new URL(usrUrl), HttpMethod.POST);
					requestSettings.setCharset(Charset.forName("UTF-8"));
					requestSettings.setAdditionalHeader("Accept", "*/*");
					requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
					requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
					requestSettings.setAdditionalHeader("Accept", "*/*");
					requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
					requestSettings.setAdditionalHeader("Accept-Language", "zh-CN");
					requestSettings.setAdditionalHeader("Host", "www.renshenet.org.cn");
					requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
					requestSettings.setAdditionalHeader("Referer", "https://www.renshenet.org.cn/sionline/search01/init.html?parm=baseInfo");
					
					Page page = webClient.getPage(requestSettings);
					
					Thread.sleep(2000);
					//System.out.println(page.getWebResponse().getContentAsString());
					String string = page.getWebResponse().getContentAsString();
					
					InsuranceSZHeiLongJiangUserInfo i = new InsuranceSZHeiLongJiangUserInfo();
					JSONObject fromObject = JSONObject.fromObject(string);
					i.setCompanyNum(fromObject.getString("aac001"));
					//i.setCompany(fromObject.getString("aab004"));
					i.setPersonalNum(fromObject.getString("aac001"));
					i.setName(fromObject.getString("aac003"));
					i.setIdCard(fromObject.getString("aac002"));
					i.setSex(fromObject.getString("aac004"));
					i.setNational(fromObject.getString("aac004"));
					i.setBirth(fromObject.getString("aac006"));
					i.setBirthday(fromObject.getString("aac006Dossier"));
					i.setCulture(fromObject.getString("aac011"));
					i.setMarry(fromObject.getString("aac017"));
					i.setPersonal(fromObject.getString("aac012"));
					i.setJoinDate(fromObject.getString("aac007"));
					i.setHouseHold(fromObject.getString("aac009"));
					i.setInsuranceStatus(fromObject.getString("aac008"));
					i.setWorkStatus(fromObject.getString("aac016"));
					i.setProfession(fromObject.getString("aac014"));
					i.setUseWork(fromObject.getString("aac013"));
					i.setSpecial(fromObject.getString("aac019"));
					i.setChangeFloor(fromObject.getString("bae905"));
					i.setAdministration(fromObject.getString("aac020"));
					i.setPhone(fromObject.getString("aae005"));
					i.setAddr(fromObject.getString("aae006"));
					i.setCode(fromObject.getString("aae007"));
					i.setHomeland(fromObject.getString("aac010"));
					i.setCountryFloor(fromObject.getString("aac015"));
					i.setJudgeTime(fromObject.getString("bae904"));
					i.setSign(fromObject.getString("mergedMark"));
					i.setWorkerNum(fromObject.getString("bae903"));
					
					System.out.println(i);
					
					String urlinit="https://www.renshenet.org.cn/sionline/search01/init03.html";
					Page page5 = webClient.getPage(urlinit);
					String urlwage="https://www.renshenet.org.cn/sionline/search01/init.html?parm=wagesInfo";
					Page page6 = webClient.getPage(urlwage);
//					String urldetail="https://www.renshenet.org.cn/sionline/search01/init.html?parm=wagesInfo";
//					Page page7 = webClient.getPage(urldetail);
					
					
					String ylbxurl="https://www.renshenet.org.cn/sionline/search01/accountRecord.html?ywdm=search01&parm=accountRecord";
//					Page page8 = webClient.getPage(ylbxurl);
//					System.out.println(page8.getWebResponse().getContentAsString());
//					String urlyi="https://www.renshenet.org.cn/sionline/search01/accountDetails.html?ywdm=search01&parm=accountDetails";
                    WebRequest  requestSettings1 = new WebRequest(new URL(ylbxurl), HttpMethod.POST);
                    
                    requestSettings.setCharset(Charset.forName("UTF-8"));
					requestSettings1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
					requestSettings1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
					requestSettings1.setAdditionalHeader("Accept", "*/*");
					requestSettings1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
					requestSettings1.setAdditionalHeader("Accept-Language", "zh-CN");
					requestSettings1.setAdditionalHeader("Host", "www.renshenet.org.cn");
					requestSettings1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
					requestSettings1.setAdditionalHeader("Referer", "https://www.renshenet.org.cn/sionline/search01/init.html?parm=accountDetails");
					Page page4 = webClient.getPage(requestSettings1);
//					System.out.println(page4.getWebResponse().getContentAsString());
					Document doc = Jsoup.parse(page4.getWebResponse().getContentAsString());
					Element elementById = doc.getElementById("MRVIEWER_DATA");
					System.out.println(elementById.text());
					String encode = URLEncoder.encode(elementById.text(), "GBK");
					
					// 初始化一个AipOcr
					AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
					// 可选：设置网络连接参数
					client.setConnectionTimeoutInMillis(2000);
					client.setSocketTimeoutInMillis(60000);
					String sample = sample(client, encode);
					System.out.println(sample);
					
					
					
					
					
					
					
					
					
					
					String strings = page4.getWebResponse().getContentAsString();
			    	JSONObject fromObjectt = JSONObject.fromObject(strings);
			    	String string2 = fromObjectt.getString("pageQuery");
			    	JSONArray fromObject2 = JSONArray.fromObject(string2);
			    	
			    	List<InsuranceSZHeiLongJiangMedical> list = new ArrayList<InsuranceSZHeiLongJiangMedical>();
			    	for (int j = 0; j < fromObject2.size(); j++) {
			    		InsuranceSZHeiLongJiangMedical insuranceSZHeiLongJiangMedical = new InsuranceSZHeiLongJiangMedical();
						JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(j));
						insuranceSZHeiLongJiangMedical.setPayDate(fromObject3.getString("col1"));
						insuranceSZHeiLongJiangMedical.setPayType(fromObject3.getString("col2"));
						insuranceSZHeiLongJiangMedical.setPayBase(fromObject3.getString("col3"));
						insuranceSZHeiLongJiangMedical.setMustSum(fromObject3.getString("col4"));
						insuranceSZHeiLongJiangMedical.setMustPersonal(fromObject3.getString("col5"));
						insuranceSZHeiLongJiangMedical.setMustCompany(fromObject3.getString("col6"));
						insuranceSZHeiLongJiangMedical.setFactSum(fromObject3.getString("col7"));
						insuranceSZHeiLongJiangMedical.setFactPersonal(fromObject3.getString("col8"));
						insuranceSZHeiLongJiangMedical.setFactCompany(fromObject3.getString("col9"));
						insuranceSZHeiLongJiangMedical.setPayStatus(fromObject3.getString("col10"));
						insuranceSZHeiLongJiangMedical.setPeopleStatus(fromObject3.getString("col11"));
						list.add(insuranceSZHeiLongJiangMedical);
					}
			    	System.out.println(list);
				} else {
					System.out.println("登录失败！");
				}
}
			
			public static void InputTab() throws IllegalAccessException, NativeException, Exception { 
				Thread.sleep(1000L); 
				if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) { 
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab")); 
				} 
				} 
			
			public static void InputEnter() throws IllegalAccessException, NativeException, Exception { 
				Thread.sleep(1000L); 
				if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) { 
				VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter")); 
				} 
				} 
			
			public static String sample(AipOcr client,String image) {
				// 传入可选参数调用接口
				HashMap<String, String> options = new HashMap<String, String>();
//				options.put("Content-Type", "application/x-www-form-urlencoded");
//				options.put("image", "true");
//				options.put("templateSign", "421bef7004708216699e2f955d1f2d43");
				
				options.put("detect_direction", "true");
//				options.put("probability", "true");
				org.json.JSONObject res = client.basicGeneral(image, options);
				System.out.println(res.toString(2));
				return res.toString(2);
			}
}