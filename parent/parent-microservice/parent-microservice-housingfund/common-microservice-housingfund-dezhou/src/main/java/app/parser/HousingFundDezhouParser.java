package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.dezhou.HousingDeZhouDepositInformation;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingFundDezhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	
	
	private WebParam<HousingDeZhouDepositInformation> htmlParserAccount(String html, MessageLoginForHousing messageLoginForHousing) {
		WebParam webParam = new WebParam();
		Document documentHtml = Jsoup.parse(html);
		String dwzh= getNextLabelByKeyword(documentHtml,"单位帐号","td");
		String mc  = getNextLabelByKeyword(documentHtml,"名称","td");
		String grzh= getNextLabelByKeyword(documentHtml,"个人帐号：","td");
		String xm  = getNextLabelByKeyword(documentHtml,"姓名","td");
		String sfz = getNextLabelByKeyword(documentHtml,"身份证","td");
		String xb  = getNextLabelByKeyword(documentHtml,"性别","td");
		String jzrq= getNextLabelByKeyword(documentHtml,"缴至日期","td");
		String gzjs= getNextLabelByKeyword(documentHtml,"工资基数","td");
		String jje = getNextLabelByKeyword(documentHtml,"缴交额","td");
		String grye= getNextLabelByKeyword(documentHtml,"个人余额","td");
		String zt  = getNextLabelByKeyword(documentHtml,"状态","td");
		String dwjl= getNextLabelByKeyword(documentHtml,"单位缴率","td");
		String grjl= getNextLabelByKeyword(documentHtml,"个人缴率","td");
		String lxfs= getNextLabelByKeyword(documentHtml,"联系方式","td");
		HousingDeZhouDepositInformation depositInformation = new HousingDeZhouDepositInformation();
		depositInformation.setCompanyNum(dwzh);
		depositInformation.setCompanyName(mc);
		depositInformation.setPersonNum(grzh);
		depositInformation.setName(xm);
		depositInformation.setIdNum(sfz);
		depositInformation.setSex(xb);
		depositInformation.setTime(jzrq);
		depositInformation.setWageBase(gzjs);
		depositInformation.setPayNum(jje);
		depositInformation.setBalance(grye);
		depositInformation.setState(zt);
		depositInformation.setCompanyPayRate(dwjl);
		depositInformation.setPersonPayRate(grjl);
		depositInformation.setPhoneNum(lxfs);
		depositInformation.setTaskid(messageLoginForHousing.getTask_id());
		webParam.setHousingDezhouDepositInfo(depositInformation);
		webParam.setHtml(html);
		return webParam;
	}
	
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
		Elements es = document.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.text(); 
			} 
		} 
		return null; 
	}
	public WebParam<HousingDeZhouDepositInformation> getDetailInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception {
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest  requestSettings = new WebRequest(new URL("http://www.dzgjj.com:8081/gjj/index.php?action=jc"), HttpMethod.POST); 
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("name",messageLoginForHousing.getUsername()));
		requestSettings.getRequestParameters().add(new NameValuePair("card",messageLoginForHousing.getNum()));
		requestSettings.getRequestParameters().add(new NameValuePair("password", messageLoginForHousing.getPassword()));
		requestSettings.setCharset(Charset.forName("UTF-8"));
		HtmlPage page = webClient.getPage(requestSettings);
//		
		String html = page.getWebResponse().getContentAsString();
		if(html.contains("联系方式")){
			WebParam<HousingDeZhouDepositInformation> detail = htmlParserAccount(html,messageLoginForHousing);
			return detail;
		}
		return null;
	}

}
