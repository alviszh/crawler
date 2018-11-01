package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jiyuan.HousingJiyuanHtml;
import com.microservice.dao.entity.crawler.housing.jiyuan.HousingJiyuanUserInfo;
import com.microservice.dao.repository.crawler.housing.jiyuan.HousingJiyuanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jiyuan.HousingJiyuanUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

@Component
public class HousingFundJiyuanParser {

	@Autowired
	private TracerLog tracer;
	
	@Autowired
	private HousingJiyuanHtmlRepository housingJiyuanHtmlRepository;
	
	@Autowired
	private HousingJiyuanUserInfoRepository housingJiyuanUserInfoRepository;

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
			String url = "http://search.jygjj.com/items/selZf.action";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			String loginType = "xm"; 
			if(StatusCodeLogin.ACCOUNT_NUM.equals(messageLoginForHousing.getLogintype())){
				loginType = "bh";
			}
			paramsList.add(new NameValuePair("opt", loginType));
			paramsList.add(new NameValuePair("grq_xm", messageLoginForHousing.getNum()));
			paramsList.add(new NameValuePair("grq_sfzh", messageLoginForHousing.getPassword()));
			paramsList.add(new NameValuePair("button2", "查询"));

			Map<String, String> map = new HashMap<String, String>();
			map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			map.put("Accept-Encoding", "gzip, deflate");
			map.put("Accept-Language", "zh-CN,zh;q=0.9");
			map.put("Cache-Control", "max-age=0");
			map.put("Connection", "keep-alive");
			map.put("Content-Type", "application/x-www-form-urlencoded");
			map.put("Host", "search.jygjj.com");
			map.put("Origin", "http://www.jygjj.com");
			map.put("Referer", "http://www.jygjj.com/");
			map.put("Upgrade-Insecure-Requests", "1");
			map.put("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");
			
			Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList,  "UTF-8", null, map);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag(
						"HousingFundJiyuanParser.getInfo信息html" + messageLoginForHousing.getTask_id(),
						"<xmp>" + html + "</xmp>");
				HousingJiyuanHtml housingJiyuanHtml = new HousingJiyuanHtml(messageLoginForHousing.getTask_id(), "housing_jiyuan_userinfo", "1", url, html);
				housingJiyuanHtmlRepository.save(housingJiyuanHtml);
				return html;
			}
		} catch (Exception e) {
			tracer.addTag("HousingFundJiyuanParser.getInfo---ERROR:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	public HousingJiyuanUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {

		tracer.addTag("HousingTaiyuanParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(),
				"<xmp>" + html + "</xmp>");

		try {
			Document doc = Jsoup.parse(html);
			// 管理部
			String management = getNextLabelByKeyword(doc, "管理部");
			// 单位编号
			String company_num = getNextLabelByKeyword(doc, "单位编号");
			// 个人编号
			String personal_num = getNextLabelByKeyword(doc, "个人编号");
			// 个人姓名
			String name = getNextLabelByKeyword(doc, "个人姓名");
			// 单位名称
			String company_name = getNextLabelByKeyword(doc, "单位名称");
			// 性别
			String sex = getNextLabelByKeyword(doc, "性别");
			// 身份证号
			String idnumber = getNextLabelByKeyword(doc, "身份证号");
			// 手机号码
			String phone = getNextLabelByKeyword(doc, "手机号码");
			// 工资额
			String wages = getNextLabelByKeyword(doc, "工资额");
			// 缴至月份
			String to_pay_month = getNextLabelByKeyword(doc, "缴至月份");
			// 单位月缴额
			String company_month_pay = getNextLabelByKeyword(doc, "单位月缴额");
			// 个人月缴额
			String personal_month_pay = getNextLabelByKeyword(doc, "个人月缴额");
			// 月缴额
			String month_pay = getNextLabelByKeyword(doc, "月缴额");
			
			int sum = 0;
			int lastIndexOf = html.lastIndexOf("var preYue");
			int lastIndexOf2 = html.lastIndexOf("$('#currYue')");
			if (lastIndexOf > 0 && lastIndexOf2 > 0) {
				html = html.substring(lastIndexOf,lastIndexOf2);
				 Pattern pattern = Pattern.compile("\\d{1,}");
			        Matcher matcher = pattern.matcher(html);
			        while (matcher.find()) {
			        	sum = sum+Integer.valueOf(matcher.group());
			        }
			}
			// 当前余额
			String balance = ""+sum;
			// 开户日期
			String opening_date = getNextLabelByKeyword(doc, "开户日期");
			// 个人状态
			String state = getNextLabelByKeyword(doc, "个人状态");
			// 个人贷款担保标记
			String personal_loan_guarantee_mark = getNextLabelByKeyword(doc, "个人贷款担保标记");
			// 截止时间
			String deadline = getNextLabelByKeyword(doc, "截止时间");
			
			HousingJiyuanUserInfo housingJiyuanUserInfo = new HousingJiyuanUserInfo(taskHousing.getTaskid(), management, company_num,
					personal_num, name, company_name, sex, idnumber, phone, wages, to_pay_month, company_month_pay,
					personal_month_pay, month_pay, balance, opening_date, state, personal_loan_guarantee_mark, deadline);
			housingJiyuanUserInfoRepository.save(housingJiyuanUserInfo);
			return housingJiyuanUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingTaiyuanParse.htmlUserInfoParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return null;

	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public String getNextLabelByKeyword(Document document, String keyword){
		Elements es = document.select("td:contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
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
		tracer.addTag("HousingFundJiyuanParser.getPage.url:" + url,
				"statusCode:" + statusCode + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}
	
	
	
	
	
	

}
