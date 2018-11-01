package app.crawler.htmlparse;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanPay;
import com.microservice.dao.entity.crawler.housing.dongguan.HousingDongguanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@Component
public class HousingDongguanParse {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {

		tracer.addTag("HousingDongguanParse.login", taskHousing.getTaskid());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		try {
			
			
			String url = "http://wsbs.dggjj.gov.cn/web_psn/websys/pages/psncollquery/psnCollDetail/psnInfoQryOnline1.jsp";
			
			HtmlPage searchPage = getHtmlPage(webClient, url, null);
			if (null != searchPage) {
				HtmlImage image = searchPage.getFirstByXPath("//img[@id='jcaptcha']");
				String code = "";
				try {
					code = chaoJiYingOcrService.getVerifycode(image, "1902");
				} catch (Exception e) {
					tracer.addTag("HousingDongguanParse.login.code.ERROR", taskHousing.getTaskid() + "-----ERROR:" + e);
					e.printStackTrace();
				}
				//公积金账号或提取绑定账号
				HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='psnAcc']");
				if (inputUserName == null) {
					throw new Exception("username input text can not found :input[id='psnAcc']");
				} else {
					inputUserName.reset();
					inputUserName.setText(messageLoginForHousing.getHosingFundNumber());
				}
				//身份证号
				HtmlTextInput inputcertNo = (HtmlTextInput) searchPage.querySelector("input[id='certNo']");
				if (inputcertNo == null) {
					throw new Exception("inputcertNo input text can not found :input[id='certNo']");
				} else {
					inputcertNo.reset();
					inputcertNo.setText(messageLoginForHousing.getNum());
				}
				HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='checkCode']");
				if (inputuserjym == null) {
					throw new Exception("code input text can not found : input[id='checkCode']");
				} else {
					inputuserjym.reset();
					inputuserjym.setText(code);
				}
				HtmlImage loginButton = (HtmlImage) searchPage.querySelector("img[onclick='okFun();']");
				if (loginButton == null) {
					throw new Exception("login button can not found : null");
				} else {
					Page page = loginButton.click();
					String alert = WebCrawler.getAlertMsg();
					String contentAsString = page.getWebResponse().getContentAsString();
					if (contentAsString.contains("公积金账户基本信息")) {
						String urldata = page.getUrl().toString();
						String output = URLDecoder.decode(URLDecoder.decode(urldata, "UTF-8"), "UTF-8");
						webParam.setUrl(output);
					} 
					String cookies = CommonUnit.transcookieToJson(webClient);
					webParam.setText(alert);
					webParam.setCookies(cookies);
					webParam.setHtml(contentAsString);
					return webParam;
				}
			}
		} catch (Exception e) {
			tracer.addTag("HousingDongguanParse.login:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;

	}

	public static HtmlPage getHtmlPage(WebClient webClient, String url, HttpMethod type) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		if (200 == statusCode) {

			return searchPage;
		}

		return null;
	}

	/**
	 * 解析用户信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public HousingDongguanUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {

		tracer.addTag("HousingDongguanParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(), "<xmp>" + html + "</xmp>");

		try {
			Map<String, String> map = new HashMap<>();
			if (html.contains("?")) {
				String jsonData = html.substring(html.lastIndexOf("?") + 1);
				String[] data = jsonData.split("&");
				for (String string : data) {
					String[] split = string.split("=");
					map.put(split[0], split[1]);
				}
				HousingDongguanUserInfo housingDongguanUserInfo = new HousingDongguanUserInfo(taskHousing.getTaskid(), map.get("psnAcc"),
						map.get("psnName"), map.get("certNo"), map.get("orgName"), map.get("psnAccSt"), map.get("bal"),
						map.get("orgEndPayTime"), map.get("pay"), map.get("originalBase"));
				return housingDongguanUserInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingDongguanParse.htmlUserInfoParser---ERROR:",
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
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("div:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.parent().nextElementSibling();
			Elements elspan = nextElement.getElementsByTag("a");
			for (Element element2 : elspan) {
				element2.empty();
			}
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

	public WebClient addcookie(TaskHousing taskHousing) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, TaskHousing taskHousing, String url, HttpMethod type,
			List<NameValuePair> paramsList, Boolean code,String body,Map<String, String> map) throws Exception {
		tracer.addTag("HousingDongguanParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if(null != map){
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
		
		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}
		
		if (code) {
			webRequest.setCharset(Charset.forName("UTF-8"));
		}

		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("HousingDongguanParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingDongguanParse.getPage---taskid:",
					taskHousing.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}


	/**
	 * 缴费信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<HousingDongguanPay> getPay(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingDongguanParse.getPay", taskHousing.getTaskid());

		WebParam<HousingDongguanPay> webParam = new WebParam<HousingDongguanPay>();

		try {
			WebClient webClient = addcookie(taskHousing);
			
			String url = "http://wsbs.dggjj.gov.cn/web_psn/psnloanquery_psnLoanAccount.do?method=psnInfoDetailQry";
			
			String body = "{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{},parameters:{\"psnAcc\":\""+messageLoginForHousing.getHosingFundNumber()+"\"}}}";

			Map<String, String> map = new HashMap<>();
			
			map.put("Content-Type", "multipart/form-data");

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, null, false,body,map);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingDongguanParse.getPay---缴费信息第一次请求" + taskHousing.getTaskid(), html);
				
				String url2 = "http://wsbs.dggjj.gov.cn/web_psn/rpc.do?method=doQuery";

				String body2 = getPayjsonParam(html);
				
				Page page2 = getPage(webClient, taskHousing, url2, HttpMethod.POST, null,false,body2,map);
				if (null != page2) {
					String html2 = page2.getWebResponse().getContentAsString();
					tracer.addTag("HousingDongguanParse.getPay---缴费信息第二次请求" + taskHousing.getTaskid(), html2);
					
					List<HousingDongguanPay> list = htmlPayParser(html2, taskHousing);
					webParam.setPage(page2);
					webParam.setHtml(page2.getWebResponse().getContentAsString());
					webParam.setList(list);
					webParam.setUrl("url---"+page.getUrl().toString()+"url2---"+page.getUrl().toString());
					webParam.setCode(page2.getWebResponse().getStatusCode());
					return webParam;
				}
			}
		} catch (Exception e) {
			tracer.addTag("HousingDongguanParse.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取流水信息请求信息参数
	 * @param json
	 * @return
	 * @throws Exception
	 */
	private static String getPayjsonParam(String json) throws Exception {

		JSONObject jsonObj = JSONObject.fromObject(json);

		JSONObject body = jsonObj.getJSONObject("body");
		
		JSONObject dataStores = body.getJSONObject("dataStores");
		
		JSONObject psnInfoDetailDs = dataStores.getJSONObject("psnInfoDetailDs");
		
		// 名：psnInfoDetailDs
		String name = psnInfoDetailDs.getString("name");
		// 前面应该是公积金账号，后面不知道啥玩意  ：[["0909149484","12"]]
		String conditionValues = psnInfoDetailDs.getString("conditionValues");
		// 不知道啥玩意：websys.psnInfoDetailQry
		String statementName = psnInfoDetailDs.getString("statementName");
		// 不知道啥玩意： ["  PSN_ACC ","12"]
		JSONObject attributes = psnInfoDetailDs.getJSONObject("attributes");
		String psnAcc = attributes.getString("psnAcc");
		// 不知道啥玩意： hafmis
		String pool = psnInfoDetailDs.getString("pool");
		
		// 不知道啥玩意：不知道啥玩意：{"synCount":"true"}
		String parameters = "{\"synCount\":\"true\"}";
		
		String jsonParam = "{header:{\"code\":0,\"message\":{\"title\":\"\",\"detail\":\"\"}},body:{dataStores:{\"psnInfoDetailDs\":{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\""+name+"\",pageNumber:1,pageSize:900000,recordCount:999999,conditionValues:"+conditionValues+",parameters:{},statementName:\""+statementName+"\",attributes:{\"psnAcc\":"+psnAcc+"},pool:\""+pool+"\"}},parameters:{\"synCount\":\"true\"}}}";
		return jsonParam;

	}

	/**
	 * 解析缴费信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<HousingDongguanPay> htmlPayParser(String html, TaskHousing taskHousing) {

		List<HousingDongguanPay> list = new ArrayList<>();
		try {
			HousingDongguanPay housingDongguanPay = null;
			
			JSONObject jsonObj = JSONObject.fromObject(html);
			JSONObject body = jsonObj.getJSONObject("body");
			JSONObject dataStores = body.getJSONObject("dataStores");
			JSONObject psnInfoDetailDs = dataStores.getJSONObject("psnInfoDetailDs");
			JSONObject rowSet = psnInfoDetailDs.getJSONObject("rowSet");
			String primary = rowSet.getString("primary");

			Object obj = new JSONTokener(primary).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 日期
				 String acctDate = jsonObject.getString("ACCT_DATE");
				 // 摘要
				 String absCodeName= jsonObject.getString("ABS_CODE_NAME");
				 // 发生额（元）
				 String ttlAmt= jsonObject.getString("TTL_AMT");
				 // 余额
				 String curBal= jsonObject.getString("CUR_BAL");
				 // 汇缴年月
				 String payYmon= jsonObject.getString("PAY_YMON");
				housingDongguanPay = new HousingDongguanPay(taskHousing.getTaskid(), acctDate, absCodeName, ttlAmt, curBal, payYmon);
				list.add(housingDongguanPay);
				
			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 日期
					 String acctDate = jsonObject.getString("ACCT_DATE");
					 // 摘要
					 String absCodeName= jsonObject.getString("ABS_CODE_NAME");
					 // 发生额（元）
					 String ttlAmt= jsonObject.getString("TTL_AMT");
					 // 余额
					 String curBal= jsonObject.getString("CUR_BAL");
					 // 汇缴年月
					 String payYmon= jsonObject.getString("PAY_YMON");
					housingDongguanPay = new HousingDongguanPay(taskHousing.getTaskid(), acctDate, absCodeName, ttlAmt, curBal, payYmon);
					list.add(housingDongguanPay);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingDongguanParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

}
