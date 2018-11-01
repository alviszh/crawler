package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import com.microservice.dao.entity.crawler.housing.luohe.HousingLuoheHtml;
import com.microservice.dao.entity.crawler.housing.luohe.HousingLuoheUserInfo;
import com.microservice.dao.repository.crawler.housing.luohe.HousingLuoheHtmlRepository;
import com.microservice.dao.repository.crawler.housing.luohe.HousingLuoheUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundLuoheParser {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private HousingLuoheHtmlRepository housingLuoheHtmlRepository;

	@Autowired
	private HousingLuoheUserInfoRepository housingLuoheUserInfoRepository;

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
			String url = "http://61.163.209.58:8089/lhgjj_ws_client/GjjcxAction.do";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("GRXM", messageLoginForHousing.getUsername()));
			paramsList.add(new NameValuePair("GRBH", messageLoginForHousing.getHosingFundNumber()));
			paramsList.add(new NameValuePair("SFZH", messageLoginForHousing.getNum()));

			Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, "UTF-8", null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingFundLuoheParser.getInfo信息html" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				HousingLuoheHtml housingLuoheHtml = new HousingLuoheHtml(messageLoginForHousing.getTask_id(),
						"housing_luohe_userinfo", "1", url, html);
				housingLuoheHtmlRepository.save(housingLuoheHtml);
				return html;
			}
		} catch (Exception e) {
			tracer.addTag("HousingFundLuoheParser.getInfo---ERROR:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return "";
	}

	public void htmlUserInfoParser(String taskid, String html) {

		tracer.addTag("HousingTaiyuanParse.htmlUserInfoParser---info:" + taskid, "<xmp>" + html + "</xmp>");
		try {
			JSONObject jsonObj = JSONObject.fromObject(html);
			String ctn = jsonObj.getString("ctn");
			JSONArray jsonArray = JSONArray.fromObject(ctn);
			JSONObject obj = jsonArray.getJSONObject(0);

			String name = obj.getString("GRXM");// 姓名
			String base = obj.getString("GZE");// 工资基数
			String monthly_pay = obj.getString("YJCE");// 月缴额
			String balance = obj.getString("JCYE");// 缴存余额
			String open_date = obj.getString("KHRQ");// 开户日期
			String pay_year = obj.getString("JZYF");// 缴至年月
			String state = obj.getString("GRZT");// 账户状态
			HousingLuoheUserInfo housingLuoheUserInfo = new HousingLuoheUserInfo(taskid, name, base, monthly_pay,
					balance, open_date, pay_year, state);
			housingLuoheUserInfoRepository.save(housingLuoheUserInfo);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingTaiyuanParse.htmlUserInfoParser---ERROR:", taskid + "---ERROR:" + e.toString());
		}

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
		tracer.addTag("HousingFundLuoheParser.getPage.url:" + url, "statusCode:" + statusCode + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

}
