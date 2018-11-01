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
import com.microservice.dao.entity.crawler.housing.huaibei.HousingHuaiBeiBase;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;

@Component
public class HousingFundHuaiBeiParser {
	@Autowired
	private TracerLog tracer;

	public WebParam getUserInfo(String html, TaskHousing taskHousing) {
		WebParam webParam = new WebParam();
		
		Document document = Jsoup.parse(html);
		
		String dwgjj = getNextLabelByKeyword(document, "td", "单位公积金银行账户");
		String dwmc = getNextLabelByKeyword(document, "td", "单位名称");
		String grgjj = getNextLabelByKeyword(document, "td", "个人公积金账号");
		String grxm  = getNextLabelByKeyword(document, "td", "个人姓名");
		String sfz = getNextLabelByKeyword(document, "td", "身份证");
		String dwjj = getNextLabelByKeyword(document, "td", "单位缴交");
		String grjj   =getNextLabelByKeyword(document, "td", "个人缴交");
		String btjj = getNextLabelByKeyword(document, "td", "补贴缴交");
		String ye = getNextLabelByKeyword(document, "td", "余额");
		String xcjfrq = getNextLabelByKeyword(document, "td", "下次缴费日期");
		String grzt = getNextLabelByKeyword(document, "td", "个人状态");
		String gze = getNextLabelByKeyword(document, "td", "工资额");
		String bcjfrq = getNextLabelByKeyword(document, "td", "本次缴费日期");
		
		HousingHuaiBeiBase base = new HousingHuaiBeiBase();
		base.setCompanyBankAccount(dwgjj);
		base.setCompanyName(dwmc);
		base.setPerHousingNum(grgjj);
		base.setName(grxm);
		base.setCompanyPay(dwjj);
		base.setPerPay(grjj);
		base.setSubsidyPay(btjj);
		base.setBalance(ye);
		base.setNextPayTime(xcjfrq);
		base.setPerStatus(grzt);
		base.setWages(gze);
		base.setThisPayTime(bcjfrq);
		
		base.setTaskid(taskHousing.getTaskid());
		webParam.setHtml(html);
		webParam.setHuaiBeiBase(base);
		return webParam;
	}
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Element document, String tag, String keyword){
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



	public WebParam<HousingHuaiBeiBase> crawler(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception {
		String url = "http://www.hbzfgjj.cn/index.php?c=gjjcx&m=detail&account="+messageLoginForHousing.getNum()+"&md=acbb9451661e3f03915d7b0f2ea0e540";
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.POST); 
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.setCharset(Charset.forName("UTF-8"));
		HtmlPage page2 = (HtmlPage)webClient.getPage(requestSettings);
		if(page2.asXml().contains("下次缴费日期")){
			WebParam<HousingHuaiBeiBase> webParam = getUserInfo(page2.asXml(),taskHousing);
			tracer.addTag("HousingHuaiBeiParser.getUserInfo 个人公积金信息" + messageLoginForHousing.getTask_id(),
					"<xmp>" + page2.asXml() + "</xmp>");
			return webParam;
		}
		return null;
	}
}
