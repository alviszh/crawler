package app.test;

import java.net.URL;
import java.util.logging.Level;


import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

public class LoginTest {
	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			/*String url="http://www.hrsszj.gov.cn/PublicServicePlatform/index.action";
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(webRequest); 
			
			HtmlImage image = page.getFirstByXPath("//img[@id='logon_user']"); 
			String imageName = "111.jpg"; 
			File file = new File("D:\\img\\"+imageName); 
			image.saveAs(file); 	*/
			
		/*	HtmlTextInput loginIdNum = (HtmlTextInput)page.getFirstByXPath("//input[@name='certNo']"); 
			HtmlTextInput loginName = (HtmlTextInput)page.getFirstByXPath("//input[@name='personAcctNo']"); 
			HtmlPasswordInput loginPassword = (HtmlPasswordInput)page.getFirstByXPath("//input[@name='password']"); 
			HtmlTextInput validateCode = (HtmlTextInput)page.getFirstByXPath("//input[@name='veriCode']"); 
			HtmlButton submitbt = (HtmlButton)page.getFirstByXPath("//input[@id='sub']"); 
			loginIdNum.setText("330501198911050823");
			loginName.setText("3305070625839"); 
			loginPassword.setText("050823"); 	
			String inputValue = JOptionPane.showInputDialog("请输入验证码……"); 
			validateCode.setText(inputValue); 	
			page= submitbt.click();    //用模拟点击的方式，此处报空指针错误
		*/
			//验证登录信息的链接：
//			String code = JOptionPane.showInputDialog("请输入验证码……"); 
			WebClient webClient = WebCrawler.getInstance().getWebClient(); 
			String url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/user/userLogin.action";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//			String requestBody="userVO.logName=15206101555&userVO.password=anhuizhu7k&userVO.verifyCode1="+code.trim()+""; 
//			String requestBody="userVO.logName=15206101555&userVO.password=anhuizhu7k&userVO.verifyCode1=1111";   //验证码输入错误也能登录成功
			String requestBody="userVO.logName=15206101555&userVO.password=anhuizhu7k";  //省去验证码也能登录成功（最终决定用这个）
			webRequest.setRequestBody(requestBody);
			Page loginResultPage=webClient.getPage(webRequest);
			if(null!=loginResultPage){
				String html = loginResultPage.getWebResponse().getContentAsString();
				System.out.println("模拟点击登录响应的信息是："+html);
				if(html.contains("idcard")){    //若是登录成功，响应的数据是个人基本信息
					System.out.println("登录成功");
					/*url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/grsb/grsbData.action?_=1519813036284";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page1 = webClient.getPage(webRequest);
					if(null!=page1){
						String contentAsString = page1.getWebResponse().getContentAsString();
						System.out.println("获取到的个人信息是："+contentAsString);
					}*/
					//必须请求此前提页面
					url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/shbx/toPersonPayList.action";
					webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page = webClient.getPage(webRequest);
					if(null!=page){
						html=page.getWebResponse().getContentAsString();
//						System.out.println("获取的缴费信息的内容的前提页面是："+html);
						url="http://www.hrsszj.gov.cn/PublicServicePlatform/business/shbx/personPayList.action";   
						webRequest = new WebRequest(new URL(url), HttpMethod.POST);
						requestBody=""
//								+ "sEcho=2"
								+ "&iColumns=9"
								+ "&sColumns="
								+ "&iDisplayStart=0"
								+ "&iDisplayLength=10"
								+ "&mDataProp_0=aae044"
								+ "&mDataProp_1=aae003"
								+ "&mDataProp_2=aae140"
								+ "&mDataProp_3=aae115"
								+ "&mDataProp_4=aae180"
								+ "&mDataProp_5=aae020"
								+ "&mDataProp_6=aae022"
								+ "&mDataProp_7=aae078"
								+ "&mDataProp_8=aae079"
								+ "&sSearch=undefined"
								+ "&bRegex=false"
								+ "&sSearch_0="
								+ "&bRegex_0=false"
								+ "&bSearchable_0=true"
								+ "&sSearch_1="
								+ "&bRegex_1=false"
								+ "&bSearchable_1=true"
								+ "&sSearch_2="
								+ "&bRegex_2=false"
								+ "&bSearchable_2=true"
								+ "&sSearch_3="
								+ "&bRegex_3=false"
								+ "&bSearchable_3=true"
								+ "&sSearch_4="
								+ "&bRegex_4=false"
								+ "&bSearchable_4=true"
								+ "&sSearch_5="
								+ "&bRegex_5=false"
								+ "&bSearchable_5=true"
								+ "&sSearch_6="
								+ "&bRegex_6=false"
								+ "&bSearchable_6=true"
								+ "&sSearch_7="
								+ "&bRegex_7=false"
								+ "&bSearchable_7=true"
								+ "&sSearch_8="
								+ "&bRegex_8=false"
								+ "&bSearchable_8=true"
								+ "&aae140="
								+ "&aaa115="
								+ "&aae078="
//								+ "&aae041=200001"    //没有查询区间，也能返回总记录数据
//								+ "&aae042=201802"
								;
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							System.out.println("获取的缴费信息的内容是："+html);
						}
					}
				}else{
					System.out.println("出现了登录错误信息："+html);
					
				}
			}
		} catch (Exception e) {
			System.out.println("打印出来的异常信息是："+e.toString());
		}
	}
	
}
