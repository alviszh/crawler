package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanEndowment;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanMedical;
import com.microservice.dao.entity.crawler.insurance.sz.yunnan.InsuranceSZYunNanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
@Component
public class InsuranceSZYunNanParser {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	
	//医疗登陆     name医疗编号  username是身份证   http://www.yn12333.gov.cn/Index.aspx?cid=49
//	                                http://www.yn12333.gov.cn/Index.aspx?cid=54
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String url="http://www.yn12333.gov.cn/Index.aspx?cid=54";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		HtmlPage page = webClient.getPage(url);		
		
		 HtmlSelect h = (HtmlSelect) page.getElementById("ddl_originlist");
		 h.getOptionByText(insuranceRequestParameters.getCity()).click(); 
//		 HtmlPage click = h.getOptionByValue("5304").click();
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("//input[@id='txtSFZH']");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getName());
		
		HtmlPasswordInput id_account = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='txtAge']");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getUsername().substring(8, 14));
		
		
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='yzm']/img");
		
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		
		HtmlTextInput identifying = (HtmlTextInput)page.getFirstByXPath("//*[@id='txtYzm']");
		identifying.reset();
		identifying.setText(verifycode);
		
		HtmlElement button = page.getFirstByXPath("//*[@id='check']");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("医保个人账户权益信息"))
		{
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	//养老登陆    password是养老 编号  username是身份证号
	public WebParam EndowmentLogin(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		String url="http://www.yn12333.gov.cn/Index.aspx?cid=49";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		HtmlPage page = webClient.getPage(url);		
		
		HtmlTextInput id_card = (HtmlTextInput)page.getFirstByXPath("/html/body/div[1]/div[4]/div[2]/ul/li[1]/input");
		id_card.reset();
		id_card.setText(insuranceRequestParameters.getUsername());
		
		HtmlTextInput id_account = (HtmlTextInput)page.getFirstByXPath("/html/body/div[1]/div[4]/div[2]/ul/li[2]/input");
		id_account.reset();
		id_account.setText(insuranceRequestParameters.getPassword());
		
		HtmlSelect h = (HtmlSelect) page.getFirstByXPath("//*[@id='ddl_originlist']");
		h.getOptionByText(insuranceRequestParameters.getCity()).click(); 
		HtmlElement button = page.getFirstByXPath("//*[@id='check']");
		Page page2 = button.click();
		
		String url2="http://www.yn12333.gov.cn/Insurance/ProvideCare/personInfo.aspx";
		Page page3 = webClient.getPage(url2);
		Thread.sleep(1000);
		System.out.println(page3.getWebResponse().getContentAsString());
		if(page3.getWebResponse().getContentAsString().contains("养老保险个人账户权益信息"))
		{
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}
	
	
	public WebParam<InsuranceSZYunNanMedical> crawlerMedical(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception{
		String cookies = taskInsurance.getCookies();
		if(cookies.contains("personInfo"))
		{
			Document doc = Jsoup.parse(cookies);
			Elements elementById = doc.getElementById("personInfo").getElementsByTag("tbody").get(0).getElementsByTag("td");
			System.out.println(elementById);
			InsuranceSZYunNanMedical  i = null;
			List<InsuranceSZYunNanMedical> list = new ArrayList<InsuranceSZYunNanMedical>();
			for (int j = 0; j < elementById.size(); j=j+7) {
				i = new InsuranceSZYunNanMedical();
				i.setDatea(elementById.get(j).text());
				i.setDateaIn(elementById.get(j+1).text());
				i.setBase(elementById.get(j+2).text());
				i.setPersonal(elementById.get(j+3).text());
				i.setCompany(elementById.get(j+4).text());
				i.setSum(elementById.get(j+5).text());
				i.setLastDatea(elementById.get(j+6).text());
				i.setTaskid(taskInsurance.getTaskid());
				list.add(i);
			}
			System.out.println(list);
			WebParam<InsuranceSZYunNanMedical> webParam = new WebParam<InsuranceSZYunNanMedical>();
			webParam.setHtml(cookies);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	public WebParam<InsuranceSZYunNanUserInfo> crawlerUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		String cookies = taskInsurance.getCookies();
		if(cookies.contains("content"))
		{
			Document doc = Jsoup.parse(cookies);
			InsuranceSZYunNanUserInfo i = new InsuranceSZYunNanUserInfo();
			String ybkhTxt = doc.getElementsByClass("heade").get(0).getElementById("ybkhTxt").val();
			i.setPersonalNum(ybkhTxt);
			String CblxText = doc.getElementsByClass("heade").get(0).getElementById("CblxText").val();
			i.setType(CblxText);
			String xmTxt = doc.getElementsByClass("content").get(0).getElementById("xmTxt").val();
			i.setName(xmTxt);
			String xbTxt = doc.getElementsByClass("content").get(0).getElementById("xbTxt").val();
			i.setSex(xbTxt);
			String csnyTxt = doc.getElementsByClass("content").get(0).getElementById("csnyTxt").val();
			i.setBirthday(csnyTxt);
			String sfgwyTxt = doc.getElementsByClass("content").get(0).getElementById("sfgwyTxt").val();
			i.setYn(sfgwyTxt);
			String sfylzgryTxt = doc.getElementsByClass("content").get(0).getElementById("sfyyzgryTxt").val();
			i.setMedical(sfylzgryTxt);
			String sznlTxt = doc.getElementsByClass("content").get(0).getElementById("sznlTxt").val();
			i.setYears(sznlTxt);
			
			String dwmcTxt = doc.getElementsByClass("content").get(0).getElementById("dwmcTxt").val();
			i.setCompany(dwmcTxt);
			String cbztTxt = doc.getElementsByClass("content").get(0).getElementById("cbztText").val();
			i.setStatus(cbztTxt);
			String zhyeTxt = doc.getElementsByClass("content").get(0).getElementById("zhyeTxt").val();
			i.setFee(zhyeTxt);
			
			String czzsrTxt = doc.getElementsByClass("content").get(0).getElementById("zhzsrTxt").val();
			i.setSum(czzsrTxt);
			String zhzzcTxt = doc.getElementsByClass("content").get(0).getElementById("zhzzcTxt").val();
			i.setSumPay(zhzzcTxt);
			i.setTaskid(taskInsurance.getTaskid());
			
			WebParam<InsuranceSZYunNanUserInfo> webParam = new WebParam<InsuranceSZYunNanUserInfo>();
			webParam.setHtml(cookies);
			webParam.setInsuranceSZYunNanUserInfo(i);
			return webParam;
		}
		return null;
		
	}

	
	//养老
	public WebParam<InsuranceSZYunNanEndowment> crawlerEndowment(InsuranceRequestParameters insuranceRequestParameters,
			TaskInsurance taskInsurance) {
		String cookies = taskInsurance.getTesthtml();
		if(cookies.contains("personInfo"))
		{
			Document doc = Jsoup.parse(cookies);
			Elements elementById = doc.getElementById("personInfo").getElementsByTag("tbody").get(0).getElementsByTag("td");
			System.out.println(elementById);
			InsuranceSZYunNanEndowment  i = null;
			List<InsuranceSZYunNanEndowment> list = new ArrayList<InsuranceSZYunNanEndowment>();
			for (int j = 0; j < elementById.size(); j=j+7) {
				i = new InsuranceSZYunNanEndowment();
				i.setDateaIn(elementById.get(j).text());
				i.setDatea(elementById.get(j).text());
				i.setType(elementById.get(j).text());
				i.setBase(elementById.get(j+2).text());
				i.setPersonal(elementById.get(j+3).text());
				i.setPersonalDate(elementById.get(j+3).text());
				i.setCompany(elementById.get(j+4).text());
				i.setTaskid(taskInsurance.getTaskid());
				list.add(i);
			}
			System.out.println(list);
			WebParam<InsuranceSZYunNanEndowment> webParam = new WebParam<InsuranceSZYunNanEndowment>();
			webParam.setHtml(cookies);
			webParam.setList(list);
			return webParam;
		}
		return null;
	}

	

}
