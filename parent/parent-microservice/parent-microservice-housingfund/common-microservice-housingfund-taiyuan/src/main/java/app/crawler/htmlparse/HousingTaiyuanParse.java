package app.crawler.htmlparse;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.taiyuan.HousingTaiyuanMsg;
import com.microservice.dao.entity.crawler.housing.taiyuan.HousingTaiyuanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@Component
public class HousingTaiyuanParse {

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

		tracer.addTag("HousingTaiyuanParse.login", taskHousing.getTaskid());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		try {
			String url = "http://www.tygjj.com/wt-web/grlogin";

			HtmlPage searchPage = getHtmlPage(webClient, url, null);
			if (null != searchPage) {
				HtmlImage image = searchPage.getFirstByXPath("//img[@id='captcha_img']");
				String code = "";
				try {
					code = chaoJiYingOcrService.getVerifycode(image, "1902");
				} catch (Exception e) {
					tracer.addTag("HousingTaiyuanParse.login.code.ERROR", taskHousing.getTaskid() + "-----ERROR:" + e);
					e.printStackTrace();
				}

				HtmlTextInput inputUserName = (HtmlTextInput) searchPage.querySelector("input[id='username']");
				if (inputUserName == null) {
					throw new Exception("username input text can not found :input[id='username']");
				} else {
					inputUserName.reset();
					inputUserName.setText(messageLoginForHousing.getNum());
				}
				HtmlPasswordInput inputpassword = (HtmlPasswordInput) searchPage
						.querySelector("input[id='in_password']");
				if (inputpassword == null) {
					throw new Exception("inputcertNo input text can not found :input[id='in_password]");
				} else {
					inputpassword.reset();
					inputpassword.setText(messageLoginForHousing.getPassword());
				}
				HtmlTextInput inputuserjym = (HtmlTextInput) searchPage.querySelector("input[id='captcha']");
				if (inputuserjym == null) {
					throw new Exception("code input text can not found : input[id='captcha']");
				} else {
					inputuserjym.reset();
					inputuserjym.setText(code);
				}
				HtmlButtonInput loginButton = (HtmlButtonInput) searchPage.querySelector("input[id='gr_login']");
				if (loginButton == null) {
					throw new Exception("login button can not found : null");
				} else {
					Page page = loginButton.click();
					String contentAsString = page.getWebResponse().getContentAsString();
					String cookies = CommonUnit.transcookieToJson(webClient);
					webParam.setCookies(cookies);
					webParam.setHtml(contentAsString);
					return webParam;
				}
			}
		} catch (Exception e) {
			tracer.addTag("HousingTaiyuanParse.login:",
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
	 * 用户信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam getUserInfo(TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingTaiyuanParse.getUserInfo", taskHousing.getTaskid());

		WebParam webParam = new WebParam();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://www.tygjj.com/wt-web/jcr/jcrkhxxcx_mh.service";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("ffbm", "01"));
			paramsList.add(new NameValuePair("ywfl", "01"));
			paramsList.add(new NameValuePair("ywlb", "99"));
			paramsList.add(new NameValuePair("cxlx", "01"));

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingTaiyuanParse.getUserInfo---用户信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingTaiyuanUserInfo housingTaiyuanUserInfo = htmlUserInfoParser(taskHousing, html);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setHousingTaiyuanUserInfo(housingTaiyuanUserInfo);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingTaiyuanParse.getUserInfo---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
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
	public HousingTaiyuanUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {

		tracer.addTag("HousingTaiyuanParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(),
				"<xmp>" + html + "</xmp>");

		try {
			JSONObject jsonObj = JSONObject.fromObject(html);
			JSONObject jsonObject = jsonObj.getJSONObject("data");
			// 姓名
			String xingming = jsonObject.getString("xingming");
			// 出生年月
			String csny = jsonObject.getString("csny");
			// 性别
			String xingbie = jsonObject.getString("xingbie");
			// 证件类型
			String zjlx = jsonObject.getString("zjlx");
			// 证件号码
			String zjhm = jsonObject.getString("zjhm");
			// 手机号码
			String sjhm = jsonObject.getString("sjhm");
			// 固定电话号码
			String gddhhm = jsonObject.getString("gddhhm");
			// 邮政编码
			String yzbm = jsonObject.getString("yzbm");
			// 家庭月收入
			String jtysr = jsonObject.getString("jtysr");
			// 家庭住址
			String jtzz = jsonObject.getString("jtzz");
			// 婚姻状况
			String hyzk = jsonObject.getString("hyzk");
			// 贷款情况
			String dkqk = jsonObject.getString("dkqk");
			// 账户账号
			String grzh = jsonObject.getString("grzh");
			// 账户状态
			String grzhzt = jsonObject.getString("grzhzt");
			// 账户余额
			String grzhye = jsonObject.getString("grzhye");
			// 开户日期
			String djrq = jsonObject.getString("djrq");
			// 单位名称
			String dwmc = jsonObject.getString("dwmc");
			// 缴存比例
			String jcbl = jsonObject.getString("jcbl");
			// 个人缴存基数
			String grjcjs = jsonObject.getString("grjcjs");
			// 月缴存额
			String yjce = jsonObject.getString("yjce");
			// 个人存款账户开户银行名称
			String grckzhkhyhmc = jsonObject.getString("grckzhkhyhmc");
			// 个人存款账户号码
			String grckzhhm = jsonObject.getString("grckzhhm");
			HousingTaiyuanUserInfo housingTaiyuanUserInfo = new HousingTaiyuanUserInfo(taskHousing.getTaskid(),
					xingming, csny, xingbie, zjlx, zjhm, sjhm, gddhhm, yzbm, jtysr, jtzz, hyzk, dkqk, grzh, grzhzt,
					grzhye, djrq, dwmc, jcbl, grjcjs, yjce, grckzhkhyhmc, grckzhhm);
			return housingTaiyuanUserInfo;
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
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {
		tracer.addTag("HousingTaiyuanParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

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
		tracer.addTag("HousingTaiyuanParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingTaiyuanParse.getPage---taskid:",
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
	public WebParam<HousingTaiyuanMsg> getMsg(TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingTaiyuanParse.getMsg", taskHousing.getTaskid());

		WebParam<HousingTaiyuanMsg> webParam = new WebParam<HousingTaiyuanMsg>();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://www.tygjj.com/wt-web/jcrMsg/xx";

			Page page = getPage(webClient, taskHousing, url, HttpMethod.GET, null, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingTaiyuanParse.getMsg---缴费信息" + taskHousing.getTaskid(), "<xmp>" + html + "</xmp>");

				List<HousingTaiyuanMsg> list = htmlPayParser(html, taskHousing);
				webParam.setPage(page);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingTaiyuanParse.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解析缴费信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<HousingTaiyuanMsg> htmlPayParser(String html, TaskHousing taskHousing) {

		
		List<HousingTaiyuanMsg> list = new ArrayList<>();
		try {
			HousingTaiyuanMsg housingTaiyuanMsg = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String results = jsonObj.getString("results");
			Object obj = new JSONTokener(results).nextValue();
			/**
			 *  {
	            "id": 1002479676696,
	            "ywmc": "汇(补)缴业务分配入账",
	            "blqd": "柜面",
	            "tssj": "2017-10-27 16:31:48",
	            "xxnr": "尊敬的公积金缴存职工:您的住房公积金账户(21900468455)于本日27-OCT-17汇缴261.00元.【太原公积金中心】",
	            "xxczy": null,
	            "ago": "9天前"
	        	},
			 */
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 消息ID
				String msgId = jsonObject.getString("id");
				// 业务名称
				String ywmc = jsonObject.getString("ywmc");
				// 消息内容
				String xxnr = jsonObject.getString("xxnr");
				// 页面展示推送时间
				String ago = jsonObject.getString("ago");
				// 推送渠道
				String blqd = jsonObject.getString("blqd");
				// 接口json字段，感觉像推送时间
				String tssj = jsonObject.getString("tssj");
				// 不知道啥.....
				String xxczy = jsonObject.getString("xxczy");

				housingTaiyuanMsg = new HousingTaiyuanMsg(taskHousing.getTaskid(), msgId, ywmc, xxnr, ago, blqd, tssj,
						xxczy);
				list.add(housingTaiyuanMsg);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 消息ID
					String msgId = jsonObject.getString("id");
					// 业务名称
					String ywmc = jsonObject.getString("ywmc");
					// 消息内容
					String xxnr = jsonObject.getString("xxnr");
					// 页面展示推送时间
					String ago = jsonObject.getString("ago");
					// 推送渠道
					String blqd = jsonObject.getString("blqd");
					// 接口json字段，感觉像推送时间
					String tssj = jsonObject.getString("tssj");
					// 不知道啥.....
					String xxczy = jsonObject.getString("xxczy");

					housingTaiyuanMsg = new HousingTaiyuanMsg(taskHousing.getTaskid(), msgId, ywmc, xxnr, ago, blqd,
							tssj, xxczy);
					list.add(housingTaiyuanMsg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingTaiyuanParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}
		return list;

	}

}
