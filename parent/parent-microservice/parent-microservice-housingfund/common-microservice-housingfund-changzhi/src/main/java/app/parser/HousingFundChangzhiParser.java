package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.changzhi.HousingChangzhiHtml;
import com.microservice.dao.repository.crawler.housing.changzhi.HousingChangzhiHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

@Component
public class HousingFundChangzhiParser {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private HousingChangzhiHtmlRepository housingChangzhiHtmlRepository;
	
	/**
	 * 获取信息 
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public String getInfo(MessageLoginForHousing messageLoginForHousing) throws Exception {

		tracer.addTag("parser.crawler.getInfo", messageLoginForHousing.getTask_id());
		try {
			String url = "http://csfw.changzhi.gov.cn/czcsfw/web/ch/gjijinBalance/getBalanceBySfzh.do?sfzh="+messageLoginForHousing.getNum();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			
			Page page = getPage(webClient, null, url, HttpMethod.GET, null,  "UTF-8", null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag(
						"HousingFundChangzhiParser.getInfo信息html" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				HousingChangzhiHtml housingChangzhiHtml = new HousingChangzhiHtml(messageLoginForHousing.getTask_id(), "housing_Changzhi_userinfo", "1", url, html);
				housingChangzhiHtmlRepository.save(housingChangzhiHtml);
				return html;
			}
		} catch (Exception e) {
			tracer.addTag("HousingFundChangzhiParser.getInfo---ERROR:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return "";
	}
	
	
	/**
	 * 通过url获取 Page
	 * 
	 * @param webClient
	 * @param taskid
	 * @param url
	 * @param type
	 * @param paramsList
	 * @param code
	 * @param body
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, String taskid, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}
		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("HousingFundChangzhiParser.getPage.url:" + url,
				"statusCode:" + statusCode + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}
	
	
	
	
	
	

}
