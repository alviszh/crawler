package Test;

import java.io.File;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouHtml;
import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestLogin {

	@Autowired
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	
	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		String url="http://gjj.zhuzhou.gov.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='gjjQueryForm']/input[1]");
		searchpwd.setText("陈艳");
		
		HtmlTextInput searchpwd1 = (HtmlTextInput)page.getFirstByXPath("//*[@id='gjjQueryForm']/input[2]");
		searchpwd1.setText("430321198911102302");
		
        HtmlImage img = page.getFirstByXPath("//*[@id='verifyImg']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='gjjQueryForm']/input[3]");
		identifying.reset();
		identifying.setText(inputValue); 
		
		HtmlElement button = page.getFirstByXPath("//*[@id='gjjQueryForm']/input[4]");
		HtmlPage page2 = button.click();
		String url3="http://gjj.zhuzhou.gov.cn/apiadapter/appapi00102.json?bodyCardNumber=430321198911102302&fullName=%E9%99%88%E8%89%B3&verifyCode="+inputValue;
		Page page4 = webClient.getPage(url3);
		System.out.println(page4.getWebResponse().getContentAsString());
		if(page4.getWebResponse().getContentAsString().contains("成功"))
		{
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("result");
			JSONArray fromObject2 = JSONArray.fromObject(string);
			HousingFundZhuZhouUserInfo h = new HousingFundZhuZhouUserInfo();
			
		}

	}
}
