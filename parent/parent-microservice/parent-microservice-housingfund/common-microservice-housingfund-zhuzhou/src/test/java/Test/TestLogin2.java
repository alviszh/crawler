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
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouHtml;
import com.microservice.dao.entity.crawler.housing.zhuzhou.HousingFundZhuZhouUserInfo;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TestLogin2 {

	@Autowired
	private static ChaoJiYingOcrService chaoJiYingOcrService;
	
	public static void main(String[] args) throws Exception {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", 
				"org.apache.commons.logging.impl.NoOpLog"); 
				java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF); 
				java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF); 
		String url="http://zhfw.zzsgjj.com/ish/index.html";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='zdyyhm']");
		searchpwd.setText("430321198911102302");
		
		HtmlPasswordInput searchpwd1 = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='dlmm']");
		searchpwd1.setText("cy19891110");
		
        HtmlImage img = page.getFirstByXPath("//*[@id='yzm_pic']");
		
		String imageName = "111.jpg";
		File file = new File("D:\\img\\" + imageName);
		img.saveAs(file); 
		//String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='yzm']");
		identifying.reset();
		identifying.setText(inputValue); 
		
		HtmlElement button = page.getFirstByXPath("//*[@id='login_tab_0']/form/div/div[6]/button[2]");
		HtmlPage page2 = button.click();
//		System.out.println(page2.getWebResponse().getContentAsString());//您好，欢迎登录本系统
		String url2="http://zhfw.zzsgjj.com/ish/flow/menu/GRXXQRY";
		Page page3 = webClient.getPage(url2);
//		System.out.println(page3.getWebResponse().getContentAsString());//个人基本信息查询
		if(page3.getWebResponse().getContentAsString().contains("_POOLKEY"))
		{
			int indexOf = page3.getWebResponse().getContentAsString().indexOf("_POOLKEY");
			String substring = page3.getWebResponse().getContentAsString().substring(indexOf);
			String substring2 = substring.substring(11,56);
//			System.out.println(substring2);
			String uu="http://zhfw.zzsgjj.com/ish/flow/command/GRMXQRY/step1/grmxqry/"+substring2+"?grzh=0058213805&grxm=%E9%99%88%E8%89%B3&begdate=2017-12-01&enddate=2018-04-13";
			WebRequest webRequest = new WebRequest(new URL(uu), HttpMethod.POST);
			Page page5 = webClient.getPage(webRequest);
			System.out.println(page5.getWebResponse().getContentAsString());
			String u="http://zhfw.zzsgjj.com/ish/ydpx/parsepage?grzh=0058213805&grxm=%E9%99%88%E8%89%B3&begdate=2017-12-01&enddate=2018-04-06&%24page=GRYW_GRZHMXQRY.ydpx&_POOLKEY="+substring2+"&list_id=grmxlist&dataset_id=grmx&list_page_no=1&grmx_order_by=";
			System.out.println(u);
			Page page4 = webClient.getPage(u);
			System.out.println(page4.getWebResponse().getContentAsString());
		}
	

	}
}
