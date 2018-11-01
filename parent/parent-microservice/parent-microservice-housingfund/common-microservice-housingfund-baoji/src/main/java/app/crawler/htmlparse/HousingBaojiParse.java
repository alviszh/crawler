package app.crawler.htmlparse;

import java.net.URL;
import java.net.URLEncoder;
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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.baoji.HousingBaojiHtml;
import com.microservice.dao.entity.crawler.housing.baoji.HousingBaojiPay;
import com.microservice.dao.entity.crawler.housing.baoji.HousingBaojiUserInfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.baoji.HousingBaojiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.baoji.HousingBaojiPayRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.InfoParam;
import app.crawler.bean.WebParam;

@Component
public class HousingBaojiParse {
	@Autowired
	private HousingBaojiPayRepository housingBaojiPayRepository;
	@Autowired
	private HousingBaojiHtmlRepository housingBaojiHtmlRepository;
	@Autowired
	private TracerLog tracer;
	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 */
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("HousingBaojiParse.login", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();			
			String loginUrl = "http://61.134.23.147:7004/wscx/zfbzgl/zfbzsq/login_hidden.jsp?pass="+messageLoginForHousing.getPassword()+"&zh="+messageLoginForHousing.getNum();	         
			webParam.setUrl(loginUrl);
			Page searchPage = getPage(webClient, taskHousing, loginUrl, null, null, "GBK", null, null);		
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
				// 弹框
				String alert = WebCrawler.getAlertMsg();
				InfoParam infoParam = new InfoParam(zgzh, sfzh, zgxm, dwbm);
				webParam.setInfoParam(infoParam);
				webParam.setText(alert);
				String cookies = CommonUnit.transcookieToJson(webClient);
				webParam.setCookies(cookies);
				webParam.setHtml(searchPageString);
			}
		} catch (Exception e) {
			tracer.addTag("HousingBaojiParse.login:",
					messageLoginForHousing.getTask_id() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return webParam;
	}

	/**
	 * 用户信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam getUserInfo(TaskHousing taskHousing, InfoParam infoParam) throws Exception {
		tracer.addTag("HousingBaojiParse.getUserInfo", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://61.134.23.147:7004/wscx/zfbzgl/gjjxxcx/gjjxx_cx.jsp";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", URLEncoder.encode(infoParam.getZgxm(), "GBK")));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
			paramsList.add(new NameValuePair("cxyd", URLEncoder.encode("当前年度", "GBK")));
			paramsList.add(new NameValuePair("dbname", "gjjmx9"));
			Page page = getPage(webClient, taskHousing, url, null, paramsList, "GBK", null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingBaojiParse.getUserInfo---用户信息" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				HousingBaojiUserInfo housingBaojiUserInfo = htmlUserInfoParser(taskHousing, html);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setHousingBaojiUserInfo(housingBaojiUserInfo);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingBaojiParse.getUserInfo---ERROR:",
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
	public HousingBaojiUserInfo htmlUserInfoParser(TaskHousing taskHousing, String html) {

		tracer.addTag("HousingBaojiParse.htmlUserInfoParser---info:" + taskHousing.getTaskid(),
				"<xmp>" + html + "</xmp>");
		try {
			Document doc = Jsoup.parse(html);
			String username = getNextLabelByKeyword(doc, "职工姓名");
			String bankNum = getNextLabelByKeyword(doc, "银行账号");
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
			String lastInterest = getNextLabelByKeyword(doc, "上年利息");
			String yearInto = getNextLabelByKeyword(doc, "本年转入");
			String balance = getNextLabelByKeyword(doc, "公积金余额");
			String isHedge = getNextLabelByKeyword(doc, "是否做月对冲");		
			String yearMonth = getNextLabelByKeyword(doc, "缴至年月");
			HousingBaojiUserInfo housingBaojiUserInfo = new HousingBaojiUserInfo(taskHousing.getTaskid(),  username,  bankNum,  idNum,  staffAccount,
					 company,  office,  openingDate,  state,  basemny,  proportion,
					 monthlyPay,  yearBalance,  companyMonthlyPay,  yearPay,  psnMonthlyPay,
					 yearDraw,  yearPayable,  lastInterest,  yearInto,  balance,  yearMonth,
					 isHedge);
			return housingBaojiUserInfo;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingBaojiParse.htmlUserInfoParser---ERROR:",
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
		tracer.addTag("HousingBaojiParse.getPage---url:", url + "---taskId:" + taskHousing.getTaskid());

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
		Thread.sleep(2500);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("HousingBaojiParse.getPage.statusCode:" + statusCode, "---taskid:" + taskHousing.getTaskid());
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("HousingBaojiParse.getPage---taskid:",
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
	public WebParam<HousingBaojiPay> getPay(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,
			InfoParam infoParam) throws Exception {

		tracer.addTag("HousingBaojiParse.getPay", taskHousing.getTaskid());

		WebParam<HousingBaojiPay> webParam = new WebParam<HousingBaojiPay>();

		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://61.134.23.147:7004/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", URLEncoder.encode(infoParam.getZgxm(), "GBK")));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
			paramsList.add(new NameValuePair("cxyd", URLEncoder.encode("当前年度", "GBK")));
			paramsList.add(new NameValuePair("zgzt", null));
			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingBaojiParse.getPay---缴费信息第一次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<String> datalist = new ArrayList<>();
				List<HousingBaojiPay> list = htmlPayParser(html, taskHousing);
				Document doc = Jsoup.parse(html);
				Elements option = doc.getElementsByTag("option");
				for (Element element : option) {
					String text = element.text();
					if (!"当前年度".equals(text) && !(infoParam.getZgzh().equals(text))) {
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
				housingBaojiPayRepository.saveAll(list);
				HousingBaojiHtml housingBaojiHtml = new HousingBaojiHtml(taskHousing.getTaskid(),
						"housing_Baoji_pay", "当前年度第一页", page.getUrl().toString(), html);
				housingBaojiHtmlRepository.save(housingBaojiHtml);

				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingBaojiParse.getPay---ERROR:", taskHousing.getTaskid() + "---ERROR:" + e.toString());
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

		tracer.addTag("HousingBaojiParse.getPayTWO", taskHousing.getTaskid());

		try {
			WebClient webClient = addcookie(taskHousing);

			String url = "http://61.134.23.147:7004/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";

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
				tracer.addTag("HousingBaojiParse.getPayTWO---缴费信息第二次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<HousingBaojiPay> list = htmlPayParser(html, taskHousing);

				housingBaojiPayRepository.saveAll(list);
				HousingBaojiHtml housingBaojiHtml = new HousingBaojiHtml(taskHousing.getTaskid(),
						"housing_Baoji_pay", "当前年度第二页", page.getUrl().toString(), html);
				housingBaojiHtmlRepository.save(housingBaojiHtml);

			}
		} catch (Exception e) {
			tracer.addTag("HousingBaojiParse.getPayTWO---ERROR:",
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
	public WebParam<HousingBaojiPay> getPaydata(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing, InfoParam infoParam, String data) throws Exception {
		tracer.addTag("HousingBaojiParse.getPay" + data, taskHousing.getTaskid());
		WebParam<HousingBaojiPay> webParam = new WebParam<HousingBaojiPay>();
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://61.134.23.147:7004/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("cxydone", data));
			paramsList.add(new NameValuePair("cxydtwo", data));
			paramsList.add(new NameValuePair("yss", "1"));
			paramsList.add(new NameValuePair("totalpages", "1"));
			paramsList.add(new NameValuePair("cxyd", URLEncoder.encode("当前年度", "GBK")));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingBaojiParse.getPay" + data + "---缴费信息第一次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<HousingBaojiPay> list = htmlPayParser(html, taskHousing);
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
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());

				housingBaojiPayRepository.saveAll(list);
				HousingBaojiHtml housingBaojiHtml = new HousingBaojiHtml(taskHousing.getTaskid(),
						"housing_Baoji_pay", data + "第一页", page.getUrl().toString(), html);
				housingBaojiHtmlRepository.save(housingBaojiHtml);
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("HousingBaojiParse.getPay" + data + "---ERROR:",
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
		tracer.addTag("HousingBaojiParse.getPaydataTWO" + data, taskHousing.getTaskid());
		try {
			WebClient webClient = addcookie(taskHousing);
			String url = "http://61.134.23.147:7004/wscx/zfbzgl/gjjmxcx/gjjmx_cx.jsp";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("cxydone", data));
			paramsList.add(new NameValuePair("cxydtwo", data));
			paramsList.add(new NameValuePair("yss", "2"));
			paramsList.add(new NameValuePair("totalpages", "2"));
			paramsList.add(new NameValuePair("cxyd",  URLEncoder.encode("当前年度", "GBK")));
			paramsList.add(new NameValuePair("zgzh", infoParam.getZgzh()));
			paramsList.add(new NameValuePair("sfzh", infoParam.getSfzh()));
			paramsList.add(new NameValuePair("zgxm", infoParam.getZgxm()));
			paramsList.add(new NameValuePair("dwbm", infoParam.getDwbm()));
			Page page = getPage(webClient, taskHousing, url, HttpMethod.POST, paramsList, "GBK", null, null);
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("HousingBaojiParse.getPaydataTWO" + data + "---缴费信息第二次请求" + taskHousing.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<HousingBaojiPay> list = htmlPayParser(html, taskHousing);
				housingBaojiPayRepository.saveAll(list);
				HousingBaojiHtml housingBaojiHtml = new HousingBaojiHtml(taskHousing.getTaskid(),
						"housing_Baoji_pay", data + "第二页", page.getUrl().toString(), html);
				housingBaojiHtmlRepository.save(housingBaojiHtml);
			}
		} catch (Exception e) {
			tracer.addTag("HousingBaojiParse.getPayTWO" + data + "---ERROR:",
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
	private List<HousingBaojiPay> htmlPayParser(String html, TaskHousing taskHousing) {
		List<HousingBaojiPay> list = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(html);
			HousingBaojiPay housingBaojiPay = null;
			Elements tr = doc.getElementsByTag("tr");
			for (Element element : tr) {
				Elements td = element.getElementsByTag("td");
				if (td.size() == 6) {
					String date = td.get(0).text();
					if (!"日期".equals(date)) {
						String debtAmount = td.get(1).text();
						String creditAmount = td.get(2).text();
						String balance = td.get(3).text();
						String lendingdirection = td.get(4).text();
						String summary = td.get(5).text();
						housingBaojiPay = new HousingBaojiPay(taskHousing.getTaskid(), date, debtAmount, creditAmount,
								balance, lendingdirection, summary);
						list.add(housingBaojiPay);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingBaojiParse.htmlPayParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}
		return list;
	}
}
