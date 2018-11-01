package app.crawler.htmlparse;

import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanMsg;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanPay;
import com.microservice.dao.entity.crawler.housing.jiuquan.HousingJiuquanUserInfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.HousingJiuquanInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@Component
public class HousingJiuquanParse {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	@Autowired
	private HousingJiuquanInfoService housingJiuquanInfoService;
	
	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {

		tracer.addTag("HousingJiuquanParse.login", taskHousing.getTaskid());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		try {
			String url = "http://60.164.171.196:8010/wt-web/grlogin";

			HtmlPage searchPage = getHtmlPage(webClient, url, null);
			if (null != searchPage) {
				HtmlImage image = searchPage.getFirstByXPath("//img[@id='captcha_img']");
				String code = "";
				try {
					code = chaoJiYingOcrService.getVerifycode(image, "1902");
				} catch (Exception e) {
					tracer.addTag("HousingJiuquanParse.login.code.ERROR", taskHousing.getTaskid() + "-----ERROR:" + e);
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
					searchPage = loginButton.click();
					String contentAsString = searchPage.getWebResponse().getContentAsString();
					if (contentAsString.contains("个人网厅登录页")) {
							HtmlImage image2 = searchPage.getFirstByXPath("//img[@id='captcha_img']");
							String code2 = "";
							try {
								code2 = chaoJiYingOcrService.getVerifycode(image2, "1902");
							} catch (Exception e) {
								tracer.addTag("HousingJiuquanParse.login.code.ERROR", taskHousing.getTaskid() + "-----ERROR:" + e);
								e.printStackTrace();
							}
							HtmlTextInput inputforce_captcha = (HtmlTextInput) searchPage.querySelector("input[id='force_captcha']");
							if (inputforce_captcha == null) {
								throw new Exception("code input text can not found :" + "input[id='force_captcha']");
							} else {
								inputforce_captcha.reset();
								inputforce_captcha.setText(code2);
							}
							HtmlButton loginButton2 = (HtmlButton) searchPage.querySelector("#qzdl");
							searchPage = loginButton2.click();
							contentAsString = searchPage.getWebResponse().getContentAsString();
					}
					String cookies = CommonUnit.transcookieToJson(webClient);
					webParam.setCookies(cookies);
					webParam.setHtml(contentAsString);
					return webParam;
				}
			}
		} catch (Exception e) {
			tracer.addTag("HousingJiuquanParse.login:",
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

		tracer.addTag("HousingJiuquanParse.getUserInfo", taskHousing.getTaskid());

		WebParam webParam = new WebParam();

		try {
			WebClient webClient = addcookie(taskHousing);
			
			String url = "http://60.164.171.196:8010/wt-web/jcr/jcrkhxxcx_mh.service";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("ffbm", "01"));
			paramsList.add(new NameValuePair("ywfl", "01"));
			paramsList.add(new NameValuePair("ywlb", "99"));
			paramsList.add(new NameValuePair("cxlx", "01"));

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingJiuquanParse.getUserInfo---用户信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingJiuquanUserInfo housingJiuquanUserInfo = htmlUserInfoParser(taskHousing, html);
				housingJiuquanInfoService.getPay(taskHousing,housingJiuquanUserInfo);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setHousingJiuquanUserInfo(housingJiuquanUserInfo);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingJiuquanParse.getUserInfo---ERROR:",
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
	public HousingJiuquanUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {

		tracer.addTag("HousingJiuquanParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(),
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
			HousingJiuquanUserInfo housingJiuquanUserInfo = new HousingJiuquanUserInfo(taskHousing.getTaskid(),
					xingming, csny, xingbie, zjlx, zjhm, sjhm, gddhhm, yzbm, jtysr, jtzz, hyzk, dkqk, grzh, grzhzt,
					grzhye, djrq, dwmc, jcbl, grjcjs, yjce, grckzhkhyhmc, grckzhhm);
			return housingJiuquanUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingJiuquanParse.htmlUserInfoParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
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
	public WebParam<HousingJiuquanPay> getPay(TaskHousing taskHousing,HousingJiuquanUserInfo housingJiuquanUserInfo) throws Exception {

		tracer.addTag("HousingJiuquanParse.getPay", taskHousing.getTaskid());

		WebParam<HousingJiuquanPay> webParam = new WebParam<HousingJiuquanPay>();

		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://60.164.171.196:8010/wt-web/jcr/jcrxxcxzhmxcx.service";
			
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			String ksrq = getDateBefore("yyyy-MM-dd", -120, 0);
			String jsrq = getDateBefore("yyyy-MM-dd", 0, 0);

			paramsList.add(new NameValuePair("ffbm", "01"));
			paramsList.add(new NameValuePair("ywfl", "01"));
			paramsList.add(new NameValuePair("ywlb", "99"));
			paramsList.add(new NameValuePair("blqd", "wt_02"));
			paramsList.add(new NameValuePair("ksrq", ksrq));
			paramsList.add(new NameValuePair("jsrq", jsrq));
			paramsList.add(new NameValuePair("grxx", housingJiuquanUserInfo.getGrzh()));
			paramsList.add(new NameValuePair("fontSize", "13px"));
			paramsList.add(new NameValuePair("pageNum", "1"));
			paramsList.add(new NameValuePair("pageSize", "100000"));
//			totalcount:16
//			pages:2
//			random:0.40861747789186653

			Page page = getPage(webClient, taskHousing, url, HttpMethod.GET, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingJiuquanParse.getPay---缴费信息" + taskHousing.getTaskid(), "<xmp>" + html + "</xmp>");

				List<HousingJiuquanPay> list = htmlPayParser(html, taskHousing);
				webParam.setPage(page);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingJiuquanParse.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
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
	private List<HousingJiuquanPay> htmlPayParser(String html, TaskHousing taskHousing) {

		
		List<HousingJiuquanPay> list = new ArrayList<HousingJiuquanPay>();
		try {
			HousingJiuquanPay housingJiuquanPay = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String results = jsonObj.getString("results");
			Object obj = new JSONTokener(results).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 记账日期
				String jzrq = jsonObject.getString("jzrq");
				// 归集和提取业务类型
				String gjhtqywlx = jsonObject.getString("gjhtqywlx");
				// 发生额
				String fse = jsonObject.getString("fse");
				// 发生利息额
				String fslxe = jsonObject.getString("fslxe");
				// 个人余额
				String yue = jsonObject.getString("yue");
				// 提取原因
				String tqyy = jsonObject.getString("tqyy");
				// 提取方式
				String tqfs = jsonObject.getString("tqfs");

				housingJiuquanPay = new HousingJiuquanPay(taskHousing.getTaskid(), jzrq, gjhtqywlx, fse, fslxe, yue, tqyy, tqfs);
				list.add(housingJiuquanPay);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 记账日期
					String jzrq = jsonObject.getString("jzrq");
					// 归集和提取业务类型
					String gjhtqywlx = jsonObject.getString("gjhtqywlx");
					// 发生额
					String fse = jsonObject.getString("fse");
					// 发生利息额
					String fslxe = jsonObject.getString("fslxe");
					// 个人余额
					String yue = jsonObject.getString("yue");
					// 提取原因
					String tqyy = jsonObject.getString("tqyy");
					// 提取方式
					String tqfs = jsonObject.getString("tqfs");

					housingJiuquanPay = new HousingJiuquanPay(taskHousing.getTaskid(), jzrq, gjhtqywlx, fse, fslxe, yue, tqyy, tqfs);
					list.add(housingJiuquanPay);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingJiuquanParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}
		return list;

	}
	

	/**
	 * 缴费信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<HousingJiuquanMsg> getMsg(TaskHousing taskHousing) throws Exception {

		tracer.addTag("HousingJiuquanParse.getMsg", taskHousing.getTaskid());

		WebParam<HousingJiuquanMsg> webParam = new WebParam<HousingJiuquanMsg>();

		try {
			WebClient webClient = addcookie(taskHousing);
			
			String url = "http://60.164.171.196:8010/wt-web/jcrMsg/xx";

			Page page = getPage(webClient, taskHousing, url, HttpMethod.GET, null, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingJiuquanParse.getMsg---缴费信息" + taskHousing.getTaskid(), "<xmp>" + html + "</xmp>");

				List<HousingJiuquanMsg> list = htmlMsgParser(html, taskHousing);
				webParam.setPage(page);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingJiuquanParse.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
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
	private List<HousingJiuquanMsg> htmlMsgParser(String html, TaskHousing taskHousing) {

		
		List<HousingJiuquanMsg> list = new ArrayList<>();
		try {
			HousingJiuquanMsg housingJiuquanMsg = null;

			JSONObject jsonObj = JSONObject.fromObject(html);
			String results = jsonObj.getString("results");
			Object obj = new JSONTokener(results).nextValue();
			/**
			 *  {
            "id": 1002045331979,
            "ywmc": "汇补缴业务补缴复核",
            "blqd": "柜面",
            "tssj": "2017-12-20 17:44:19",
            "xxnr": "尊敬的程海元，您的201712至201712补缴的公积金已到账。",
            "xxczy": null,
            "ago": "14天前"
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

				housingJiuquanMsg = new HousingJiuquanMsg(taskHousing.getTaskid(), msgId, ywmc, xxnr, ago, blqd, tssj,
						xxczy);
				list.add(housingJiuquanMsg);

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

					housingJiuquanMsg = new HousingJiuquanMsg(taskHousing.getTaskid(), msgId, ywmc, xxnr, ago, blqd,
							tssj, xxczy);
					list.add(housingJiuquanMsg);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingJiuquanParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}
		return list;

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
		tracer.addTag("HousingJiuquanParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

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
		tracer.addTag("HousingJiuquanParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingJiuquanParse.getPage---taskid:",
					taskHousing.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}
	
	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int monthCount, int dateCount) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, monthCount);
		c.add(Calendar.DATE, dateCount);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

}
