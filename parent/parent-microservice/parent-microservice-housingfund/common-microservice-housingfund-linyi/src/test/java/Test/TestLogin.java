package Test;

import java.io.File;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

public class TestLogin extends AbstractChaoJiYingHandler{
	private static final String LEN_MIN = "0";
	private static final String TIME_ADD = "0";
	private static final String STR_DEBUG = "a";
	public static void main(String[] args)  throws Exception {
		String url="http://www.lyzfgjj.gov.cn/abc/login.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
//		HtmlSelect select = (HtmlSelect) page.getElementById("selet");
//        HtmlOption option = select.getOptionByText("身份证号");
//        System.out.println(option+"----------------------------------===========================");
//        option.click();
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@name='zfz']");
		id_card.reset();
		id_card.setText("530326198811294920");
		
		HtmlPasswordInput searchpwd = (HtmlPasswordInput)page.getFirstByXPath("//input[@name='zh']");
		searchpwd.reset();
		searchpwd.setText("294920");
		
//		HtmlImage img = page.getFirstByXPath("//img[@id='image']");
//		
//		String imageName = "111.jpg";
//		File file = new File("D:\\img\\" + imageName);
//		img.saveAs(file); 
//		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
//		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@id='verify']");
//		yzm.reset();
//		yzm.setText(inputValue);
		
		HtmlElement button = page.getFirstByXPath("//input[@class='loginbtn']");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
		//System.out.println(page2.getWebResponse().getContentAsString());
		WebClient webClient2 = page2.getWebClient();
		
		
		String url2="http://www.lyzfgjj.gov.cn/abc/index.asp";
		HtmlPage page3 = webClient2.getPage(url2);
		
		HtmlImage image = page3.getFirstByXPath("//img[@src='GetCode.asp']"); 
		File file = new File("D:\\img\\111.jpg" ); 
		image.saveAs(file);
		String ocrCode = getOcrCode(file.getAbsolutePath());
		
		
		HtmlTextInput elementById = (HtmlTextInput)page3.getElementByName("zh");
		elementById.setText("294920");
		System.out.println(ocrCode);
		
		HtmlTextInput pwd = (HtmlTextInput)page3.getElementByName("verifycode");
		pwd.setText(ocrCode);
		System.out.println(ocrCode);
		
		HtmlElement elementByName = page3.getElementByName("submit2");
		Page page5 = elementByName.click();
		
		System.out.println(page5.getWebResponse().getContentAsString());
//		String urlInfo="http://www.lyzfgjj.gov.cn/abc/grcx.asp?zfz=530326198811294920&zh=294920&x=grye&verifycode="+ocrCode+"&submit2=%B2%E9%D1%AF";//		查询GBK
//		HtmlPage page4 = webClient2.getPage(urlInfo);
//		System.out.println(page4.getWebResponse().getContentAsString());
		
		
		
	}
	
	private static String getOcrCode(String imgFilePath)  {
		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, imgFilePath);
		System.out.println("chaoJiYingResult ====>>"+chaoJiYingResult);
		Gson gson = new GsonBuilder().create();
		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
		return code;
	}
}
