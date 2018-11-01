package app.crawler.htmlparse;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.sz.hunan.HousingSZHunanHtml;
import com.microservice.dao.entity.crawler.housing.sz.hunan.HousingSZHunanPay;
import com.microservice.dao.entity.crawler.housing.sz.hunan.HousingSZHunanUserInfo;
import com.microservice.dao.repository.crawler.housing.sz.hunan.HousingSZHunanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.sz.hunan.HousingSZHunanPayRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;

@Component
public class HousingSZHunanParse {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private HousingSZHunanPayRepository housingSZHunanPayRepository;

	@Autowired
	private HousingSZHunanHtmlRepository housingSZHunanHtmlRepository;

	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {

		tracer.addTag("HousingSZHunanParse.login", taskHousing.getTaskid());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParam webParam = new WebParam();
		try {
			String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/zfbzsq/login_hidden.jsp";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("password", messageLoginForHousing.getPassword()));
			paramsList.add(new NameValuePair("sfzh", messageLoginForHousing.getNum()));
			paramsList.add(new NameValuePair("cxyd", "当前年度"));
			paramsList.add(new NameValuePair("dbname", "gjjmx9"));
			String loginType = "0";
			if (StatusCodeLogin.SUNSHINE_NUM.equals(messageLoginForHousing.getLogintype())) {
				loginType = "1";
			}
			paramsList.add(new NameValuePair("dlfs", loginType));

			Page searchPage = getPage(webClient, taskHousing, url, null, paramsList, "GBK", null, null);

			if (null != searchPage) {
				String searchPageString = searchPage.getWebResponse().getContentAsString();
				Document doc = Jsoup.parse(searchPageString);
				// 职工账号
				Elements selectzgzh = doc.select("input[name=zgzh]");
				String zgzh = selectzgzh.get(0).val();
				// 身份证号
				Elements selectsfzh = doc.select("input[name=sfzh]");
				String sfzh = selectsfzh.get(0).val();
				// 职工姓名
				Elements selectzgxm = doc.select("input[name=zgxm]");
				String zgxm = selectzgxm.get(0).val();
				// 感觉像“单位编码”
				Elements selectdwbm = doc.select("input[name=dwbm]");
				String dwbm = selectdwbm.get(0).val();
				// 当前状态
				Elements selectzgzt = doc.select("input[name=zgzt]");
				String zgzt = selectzgzt.get(0).val();
				// 弹框
				String alert = WebCrawler.getAlertMsg();
				InfoParam infoParam = new InfoParam(zgzh, sfzh, zgxm, dwbm, zgzt);
				webParam.setInfoParam(infoParam);
				webParam.setText(alert);
				String cookies = CommonUnit.transcookieToJson(webClient);
				webParam.setCookies(cookies);
				webParam.setHtml(searchPageString);
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingSZHunanParse.login:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
			e.printStackTrace();
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
	public WebParam getUserInfo(TaskHousing taskHousing, InfoParam infoParam) throws Exception {

		tracer.addTag("HousingSZHunanParse.getUserInfo", taskHousing.getTaskid());

		WebParam webParam = new WebParam();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/gjjxxcx/gjjxx_cx.jsp";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
			paramsList.add(new NameValuePair("cxyd", "当前年度"));
			paramsList.add(new NameValuePair("zgzt", infoParam.getZgzt()));

			Page page = getPage(webClient, taskHousing, url, null, paramsList, "GBK", null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingSZHunanParse.getUserInfo---用户信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingSZHunanUserInfo housingSZHunanUserInfo = htmlUserInfoParser(taskHousing, html);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setHousingSZHunanUserInfo(housingSZHunanUserInfo);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingSZHunanParse.getUserInfo---ERROR:",
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
	public HousingSZHunanUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {

		tracer.addTag("HousingSZHunanParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(),
				"<xmp>" + html + "</xmp>");

		try {
			Document doc = Jsoup.parse(html);

			String name = getNextLabelByKeyword(doc, "职工姓名");
			String bank = getNextLabelByKeyword(doc, "银行账号");
			String idNum = getNextLabelByKeyword(doc, "身份证号");
			String staffAccount = getNextLabelByKeyword(doc, "职工账号");
			String company = getNextLabelByKeyword(doc, "所在单位");
			String office = getNextLabelByKeyword(doc, "所属办事处");
			String openingDate = getNextLabelByKeyword(doc, "开户日期");
			String state = getNextLabelByKeyword(doc, "当前状态");
			String basemny = getNextLabelByKeyword(doc, "月缴基数");
			String proportion = getNextLabelByKeyword(doc, "个人/单位");// 缴存比例
			String monthlyPay = getNextLabelByKeyword(doc, "月缴金额");
			String yearBalance = getNextLabelByKeyword(doc, "上年余额");
			String companyMonthlyPay = getNextLabelByKeyword(doc, "单位月缴额");
			String yearPay = getNextLabelByKeyword(doc, "本年补缴");
			String psnMonthlyPay = getNextLabelByKeyword(doc, "个人月缴额");
			String yearDraw = getNextLabelByKeyword(doc, "本年支取");
			String yearPayable = getNextLabelByKeyword(doc, "本年缴交");
			String interest = getNextLabelByKeyword(doc, "本年利息");
			String yearInto = getNextLabelByKeyword(doc, "本年转入");
			String balance = getNextLabelByKeyword(doc, "公积金余额");
			String yearMonth = getNextLabelByKeyword(doc, "缴至年月");

			HousingSZHunanUserInfo housingSZHunanUserInfo = new HousingSZHunanUserInfo(taskHousing.getTaskid(), name,
					bank, idNum, staffAccount, company, office, openingDate, state, basemny, proportion, monthlyPay,
					yearBalance, companyMonthlyPay, yearPay, psnMonthlyPay, yearDraw, yearPayable, interest, yearInto,
					balance, yearMonth);
			return housingSZHunanUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingSZHunanParse.htmlUserInfoParser---ERROR:",
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
		Elements es = document.select("tr[class=jtpsoft] td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
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
		tracer.addTag("HousingSZHunanParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

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
		tracer.addTag("HousingSZHunanParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingSZHunanParse.getPage---taskid:",
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
	public WebParam<HousingSZHunanPay> getPay(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			InfoParam infoParam) throws Exception {

		tracer.addTag("HousingSZHunanParse.getPay", taskHousing.getTaskid());

		WebParam<HousingSZHunanPay> webParam = new WebParam<HousingSZHunanPay>();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
			paramsList.add(new NameValuePair("cxyd", "当前年度"));
			paramsList.add(new NameValuePair("zgzt", infoParam.getZgzt()));

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingSZHunanParse.getPay---缴费信息第一次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<String> datalist = new ArrayList<>();
				List<HousingSZHunanPay> list = htmlPayParser(html, taskHousing,"当前年度");

				Document doc = Jsoup.parse(html);
				Elements option = doc.getElementsByTag("option");
				for (Element element : option) {
					String text = element.text();
					if (!"当前年度".equals(text)) {
						datalist.add(text);
					}
				}
				Elements elementsByAttributeValue = doc.getElementsByAttributeValue("name", "totalpages");
				String text = elementsByAttributeValue.get(0).val();
				if ("2".equals(text)) {
					getPayTWO(messageLoginForHousing, taskHousing, infoParam);
				}
				webParam.setPage(page);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setText(text);
				webParam.setDatalist(datalist);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());

				housingSZHunanPayRepository.saveAll(list);
				HousingSZHunanHtml housingSZHunanHtml = new HousingSZHunanHtml(taskHousing.getTaskid(),
						"housing_szhunan_pay", "当前年度第一页", page.getUrl().toString(), html);
				housingSZHunanHtmlRepository.save(housingSZHunanHtml);

				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingSZHunanParse.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
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
	public void getPayTWO(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing, InfoParam infoParam)
			throws Exception {

		tracer.addTag("HousingSZHunanParse.getPayTWO", taskHousing.getTaskid());

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();

			paramsList.add(new NameValuePair("cxydone", "0"));
			paramsList.add(new NameValuePair("cxydtwo", "当前年度"));
			paramsList.add(new NameValuePair("yss", "2"));
			paramsList.add(new NameValuePair("totalpages", "2"));
			paramsList.add(new NameValuePair("cxyd", "当前年度"));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingSZHunanParse.getPayTWO---缴费信息第二次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<HousingSZHunanPay> list = htmlPayParser(html, taskHousing,"当前年度");

				housingSZHunanPayRepository.saveAll(list);
				HousingSZHunanHtml housingSZHunanHtml = new HousingSZHunanHtml(taskHousing.getTaskid(),
						"housing_szhunan_pay", "当前年度第二页", page.getUrl().toString(), html);
				housingSZHunanHtmlRepository.save(housingSZHunanHtml);

			}
		} catch (Exception e) {
			tracer.addTag("HousingSZHunanParse.getPayTWO---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 缴费信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<HousingSZHunanPay> getPaydata(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing, InfoParam infoParam, String data) throws Exception {

		tracer.addTag("HousingSZHunanParse.getPay" + data, taskHousing.getTaskid());

		WebParam<HousingSZHunanPay> webParam = new WebParam<HousingSZHunanPay>();

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("cxydone", data));
			paramsList.add(new NameValuePair("cxydtwo", data));
			paramsList.add(new NameValuePair("yss", "1"));
			paramsList.add(new NameValuePair("totalpages", "1"));
			paramsList.add(new NameValuePair("cxyd", "当前年度"));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingSZHunanParse.getPay" + data + "---缴费信息第一次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<String> datalist = new ArrayList<>();
				List<HousingSZHunanPay> list = htmlPayParser(html, taskHousing,data);

				Document doc = Jsoup.parse(html);
				Elements elementsByAttributeValue = doc.getElementsByAttributeValue("name", "totalpages");
				String text = elementsByAttributeValue.get(0).val();
				if ("2".equals(text)) {
					tracer.addTag("查询月份" + data + "第二页", taskHousing.getTaskid());
					getPaydataTWO(messageLoginForHousing, taskHousing, infoParam, data);
				}
				webParam.setPage(page);
				webParam.setHtml(page.getWebResponse().getContentAsString());
				webParam.setList(list);
				webParam.setText(text);
				webParam.setDatalist(datalist);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());

				housingSZHunanPayRepository.saveAll(list);
				HousingSZHunanHtml housingSZHunanHtml = new HousingSZHunanHtml(taskHousing.getTaskid(),
						"housing_szhunan_pay", data + "第一页", page.getUrl().toString(), html);
				housingSZHunanHtmlRepository.save(housingSZHunanHtml);

				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingSZHunanParse.getPay" + data + "---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
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
	public void getPaydataTWO(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			InfoParam infoParam, String data) throws Exception {

		tracer.addTag("HousingSZHunanParse.getPayTWO" + data, taskHousing.getTaskid());

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://www.xzgjj.com:7001/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("cxydone", data));
			paramsList.add(new NameValuePair("cxydtwo", data));
			paramsList.add(new NameValuePair("yss", "2"));
			paramsList.add(new NameValuePair("totalpages", "2"));
			paramsList.add(new NameValuePair("cxyd", "当前年度"));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));

			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingSZHunanParse.getPayTWO" + data + "---缴费信息第二次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<HousingSZHunanPay> list = htmlPayParser(html, taskHousing,data);

				housingSZHunanPayRepository.saveAll(list);
				HousingSZHunanHtml housingSZHunanHtml = new HousingSZHunanHtml(taskHousing.getTaskid(),
						"housing_szhunan_pay", data + "第二页", page.getUrl().toString(), html);
				housingSZHunanHtmlRepository.save(housingSZHunanHtml);

			}
		} catch (Exception e) {
			tracer.addTag("HousingSZHunanParse.getPayTWO" + data + "---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 解析缴费信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<HousingSZHunanPay> htmlPayParser(String html, TaskHousing taskHousing,String year) {

		List<HousingSZHunanPay> list = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(html);
			HousingSZHunanPay housingSZHunanPay = null;
			Elements tr = doc.getElementsByTag("tr");
			for (Element element : tr) {
				Elements td = element.getElementsByTag("td");
				if (td.size() == 6) {
					String date = td.get(0).text();
					if (!"日期".equals(date)) {
						String debitAmount = td.get(1).text();
						String creditAmount = td.get(2).text();
						String balance = td.get(3).text();
						String lendingDirection = td.get(4).text();
						String summary = td.get(5).text();
						housingSZHunanPay = new HousingSZHunanPay(taskHousing.getTaskid(), date, debitAmount,
								creditAmount, balance, lendingDirection, summary, year);
						list.add(housingSZHunanPay);
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingSZHunanParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return list;

	}

}
