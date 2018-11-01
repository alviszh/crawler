package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestCrawler extends AbstractChaoJiYingHandler {
	
	static String url = "http://wecare.sinosig.com/common/new_customerservice/html/baodanfuwu/dzbd_index.html";
	
	static WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	private static final String OCR_FILE_PATH = "/home/img";
	private static String uuid = UUID.randomUUID().toString();
	static Gson gson = new GsonBuilder().create();
	static String code = "";
	
	public static void main(String[] args) throws Exception {
		//登录
		login();
	}
	
	public static void login() throws Exception{
		HtmlPage page = (HtmlPage) getHtml(url,webClient);
//		System.out.println(page.getWebResponse().getContentAsString());
		//保单号
		HtmlTextInput insuredNo = page.getFirstByXPath("//input[@id='insuredNo']");
		//身份证号
		HtmlTextInput insuredCardNo = page.getFirstByXPath("//input[@id='insuredCardNo']");
		//验证码
		HtmlTextInput verifycode = page.getFirstByXPath("//input[@id='verifycode']");
		//登录按钮
		HtmlInput land = (HtmlInput)page.getFirstByXPath("//input[@id='land']");
		//验证码
		HtmlImage image = page.getFirstByXPath("//img[@id='cheNum']");
		File codeImageFile = getImageLocalPath();
		
		image.saveAs(codeImageFile);
		
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1005", LEN_MIN, TIME_ADD, STR_DEBUG, codeImageFile.getAbsolutePath());
		System.out.println("超级鹰识别之后的结果： "+chaoJiYingResult);
		code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		System.out.println("code : "+code);
		
//		loginByWebClient();
		
		insuredNo.setText("1155405282018015976");
		insuredCardNo.setText("130406199110233017");
		verifycode.setText(code);
		
		HtmlPage loginPage = land.click();
		writer(loginPage.asXml(),"C:/home/yangguanglogin.txt");
		
		Document doc = Jsoup.parse(loginPage.asXml());
		Element element = doc.getElementById("iframe");
		String src = element.attr("src");
		System.out.println("获取详情页的url："+src);
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage result = (HtmlPage) getHtml("https://epolicy.sinosig.com"+src,webClient);
		writer(result.asXml(),"C:/home/reaule.txt");
	}
	
	private static void loginByWebClient() throws Exception {
		String loginUrl = "https://epolicy.sinosig.com/digitalBill_download/EPolicy_searchforcus.action?"
				+ "callback=jsonp1536307044663&"
				+ "insuredNo=1155405282018015976&"
				+ "insuredCardNo=130406199110233017&"
				+ "isFR=&"
				+ "isBJ=&"
				+ "holderCardNo=&"
				+ "hpNumber=&"
				+ "frameNumber=&"
				+ "isCar=N&"
				+ "verifycode="+code+"&"
				+ "riskFlag=P&"
				+ "flag=chan";
		HtmlPage page = (HtmlPage) getHtml(loginUrl,webClient);
		String welcome = "https://epolicy.sinosig.com/digitalBill_download/EPolicy_search.action";
		HtmlPage welcomePage = (HtmlPage) getHtml(welcome,page.getWebClient());
		writer(welcomePage.asXml(),"C:/home/yangguanglogin.txt");
		
		Document doc = Jsoup.parse(welcomePage.asXml());
		Element element = doc.getElementById("iframe");
		String src = element.attr("src");
		System.out.println("获取详情页的url："+src);
		HtmlPage result = (HtmlPage) getHtml("https://epolicy.sinosig.com"+src,webClient);
		writer(result.asXml(),"C:/home/reaule.txt");
	}

	public static Page getHtml(String url,WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	public static void writer(String page, String path){
		File file = new File(path);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		 byte bt[] = new byte[1024];  
	        bt = page.getBytes();  
	        try {  
	            FileOutputStream in = new FileOutputStream(file);  
	            try {  
	                in.write(bt, 0, bt.length);  
	                in.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        } catch (FileNotFoundException e) {  
	            e.printStackTrace();  
	        }  
	}
	
	
	
	public static File getImageLocalPath(){
		
		File parentDirFile = new File(OCR_FILE_PATH);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		
		String imageName = uuid + ".jpg";
		File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
				
		return codeImageFile;
		
	}

}
