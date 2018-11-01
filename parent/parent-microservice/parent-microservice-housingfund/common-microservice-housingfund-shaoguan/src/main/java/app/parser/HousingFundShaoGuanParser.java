package app.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class HousingFundShaoGuanParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="http://www.sggjj.com:8080/gjjnet/jsp/login.jsp";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		
		HtmlTextInput searchpwd = (HtmlTextInput)page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td[2]/input");
		searchpwd.reset();
		searchpwd.setText(messageLoginForHousing.getUsername());
		
		HtmlPasswordInput searchpwd1 = (HtmlPasswordInput)page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[3]/td[2]/input");
		searchpwd1.reset();
		searchpwd1.setText(messageLoginForHousing.getPassword());
		
        HtmlImage img = page.getFirstByXPath("//*[@id='ValidImage']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[4]/td[2]/input");
		identifying.reset();
		identifying.setText(verifycode);
		HtmlElement button = page.getFirstByXPath("//*[@id='form1']/table/tbody/tr/td/table[2]/tbody/tr/td/table/tbody/tr[5]/td/img[1]");
		HtmlPage page2 = button.click();
		System.out.println(page2.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains(""))
		{
			return webParam;
		}
		return null;
	}

}
