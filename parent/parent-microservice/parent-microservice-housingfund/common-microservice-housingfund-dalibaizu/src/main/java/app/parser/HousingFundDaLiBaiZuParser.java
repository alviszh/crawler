package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.dalibaizu.HousingDaLiBaiZuDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;

@Component
public class HousingFundDaLiBaiZuParser {
	@Autowired
	private TracerLog tracer;
	
	
	

	public WebParam<HousingDaLiBaiZuDetail> getDetailInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception {

		String result = java.net.URLEncoder.encode(messageLoginForHousing.getUsername(), "gb2312");
		String url = "http://60.160.91.191:81/gjj/Gjj_check.asp?xm="+result+"&zh="+messageLoginForHousing.getNum()+"&pass="+messageLoginForHousing.getPassword();
		WebParam webParam= new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		HtmlPage page = webClient.getPage(requestSettings);
			
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setPage(page);
		webParam.setWebClient(webClient);
		if(webParam.getPage().getWebResponse().getContentAsString().contains("修改密码")){
			String html = page.getWebResponse().getContentAsString();
			List<HousingDaLiBaiZuDetail> infoList = new ArrayList<HousingDaLiBaiZuDetail>();
			tracer.addTag("HousingDalibaizuParser.getDetailInfo 个人公积金明细" + messageLoginForHousing.getTask_id(),
					"<xmp>" + html + "</xmp>");
			infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
			if(null!=infoList){
				webParam.setList(infoList);
				webParam.setUrl(url);
				webParam.setHtml(html);
				return webParam;
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("输入信息不正确")) {
				tracer.addTag("HousingDalibaizuParser.getDetailInfo 输入的信息不正确！" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
			}
		}
		return webParam;
	}



	private List<HousingDaLiBaiZuDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing,List<HousingDaLiBaiZuDetail> infoList) {
		Document doc = Jsoup.parse(html);
		Element baseInfo = doc.getElementsByAttributeValue("cellpadding","2").first();
		Elements trs = baseInfo.select("tr");
		for (int i = 1; i < trs.size(); i++) {
				Elements tds = trs.get(i).select("td");
				HousingDaLiBaiZuDetail detail = new HousingDaLiBaiZuDetail();
				detail.setTime(tds.get(0).text());
				detail.setUnitName(tds.get(1).text());
				detail.setRemark(tds.get(2).text());
				detail.setDebit(tds.get(3).text());
				detail.setCredit(tds.get(4).text());
				detail.setLastBalance(tds.get(5).text());
				detail.setThisBalance(tds.get(6).text());
				detail.setTotalBalance(tds.get(7).text());
				detail.setTaskid(messageLoginForHousing.getTask_id());
				infoList.add(detail);
		}
		return infoList;
	}

}
