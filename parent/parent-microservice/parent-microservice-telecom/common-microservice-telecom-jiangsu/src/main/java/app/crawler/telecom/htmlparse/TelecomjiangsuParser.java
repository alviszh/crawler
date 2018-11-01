package app.crawler.telecom.htmlparse;

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

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBalance;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBill;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBillSum;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBusiness;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuCallRecord;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuMessage;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuPay;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.exceptiondetail.EUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@Component
public class TelecomjiangsuParser {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	@Autowired
	private EUtils eutils;

	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, String taskid, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {

		tracer.addTag("XuexinTaskService.getPage" + url, "---taskId:" + taskid);
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
		tracer.addTag("XuexinTaskService.statusCode" + statusCode, url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	public HtmlPage getHtml(String url, WebClient webClient, TaskMobile taskMobile) throws Exception {
		tracer.addTag("TelecomJiangsuParser.getHtml---url:" + url + " ", taskMobile.getTaskid());

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("TelecomJiangsuParser.getHtml.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());
		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("TelecomJiangsuParser.getHtml---url:" + url + "---taskid:" + taskMobile.getTaskid(),
					"<xmp>" + html + "</xmp>");
			return searchPage;
		}
		return null;
	}

	public WebClient addcookie(TaskMobile taskMobile) {

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		return webClient;
	}

	/**
	 * 获取手机验证码
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public String getphonecode(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {

		try {
			WebClient webClient = addcookie(taskMobile);

			// String url1 =
			// "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/queryInfo/myBill.jsp";
			// HtmlPage htmlpage = getHtml(url1, webClient, taskMobile);
			String url1 = "http://www.189.cn/dqmh/frontLinkSkip.do?method=skip&shopId=10011&toStUrl=http://js.189.cn/service/bill?tabFlag=billing4";
			Page page1 = getPage(webClient, taskMobile.getTaskid(), url1, HttpMethod.GET, null, null, null, null);

			String url = "http://js.189.cn/queryValidateSecondPwdAction.action";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("inventoryVo.accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("inventoryVo.productId", "2000004"));
			paramsList.add(new NameValuePair("inventoryVo.action", "generate"));
			paramsList.add(new NameValuePair("inventoryVo.inputRandomPwd", ""));

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			String json = page.getWebResponse().getContentAsString();

			tracer.addTag("TelecomjiangsuParser.getphonecode---验证码返回数据:" + taskMobile.getTaskid() + "---json:", json);

			JSONObject jsonObj = JSONObject.fromObject(json);
			if (jsonObj.has("TSR_MSG")) {
				String errorCode = jsonObj.getString("TSR_MSG");
				if ("成功".equals(errorCode)) {
					String cookieString = CommonUnit.transcookieToJson(webClient);
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
					taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
					taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
					taskMobile.setCookies(cookieString);
					// 发送验证码状态更新
					taskMobileRepository.save(taskMobile);
					return "SUCCESS";
				}
			}

		} catch (Exception e) {
			tracer.addTag("TelecomjiangsuParser.getphonecode---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return "false";

	}

	/**
	 * 验证验证码
	 * 
	 * @param taskMobile
	 * @param messageLogin
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public String verificationcode(TaskMobile taskMobile, MessageLogin messageLogin) throws Exception {

		tracer.addTag("TelecomjiangsuParser.verificationcode", taskMobile.getTaskid());

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/queryValidateSecondPwdAction.action";

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("inventoryVo.accNbr", messageLogin.getName()));
			paramsList.add(new NameValuePair("inventoryVo.productId", "2000004"));
			paramsList.add(new NameValuePair("inventoryVo.action", "check"));
			paramsList.add(new NameValuePair("inventoryVo.inputRandomPwd", messageLogin.getSms_code()));

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomjiangsuParser.verificationcode 验证验证码" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				if (html.contains("TSR_MSG")) {
					JSONObject jsonObj = JSONObject.fromObject(html);
					if (jsonObj.has("TSR_MSG")) {
						String msg = jsonObj.getString("TSR_MSG");
						if ("成功".equals(msg)) {
							String cookieString = CommonUnit.transcookieToJson(webClient);
							taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
							taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
							taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
							taskMobile.setCookies(cookieString);
							taskMobileRepository.save(taskMobile);
							return "SUCCESS";
						}
					}
				}

			}
		} catch (Exception e) {
			tracer.addTag("TelecomjiangsuParser.verificationcode---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return "false";
	}

	/**
	 * 账单
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuBillSum> getBillSum(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth)
			throws Exception {

		tracer.addTag("TelecomJiangsuParser.getBill", taskMobile.getTaskid());

		WebParam<TelecomJiangsuBillSum> webParam = new WebParam<TelecomJiangsuBillSum>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/nservice/billQuery/billQuery";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("zhangqi", yearmonth));
			paramsList.add(new NameValuePair("style", "1"));

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getBill---账单" + taskMobile.getTaskid(), "<xmp>" + html + "</xmp>");
				List<TelecomJiangsuBillSum> list = htmluBillSumParser(html, taskMobile, yearmonth);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getBill---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析账单合计
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuBillSum> htmluBillSumParser(String html, TaskMobile taskMobile, String yearmonth) {

		List<TelecomJiangsuBillSum> list = new ArrayList<TelecomJiangsuBillSum>();
		try {
			if(!html.contains("查询出错")){
				JSONObject jsonObj = JSONObject.fromObject(html);

				// 客户名称
				String user_name = jsonObj.getString("userName");
				// 费用提示
				String fee_new = jsonObj.getString("feeNew");
				// 本期存入金额
				String into_fee = jsonObj.getString("intoFee");
				// 本期返还金额
				String re_fee = jsonObj.getString("reFee");
				// 本期末可用积分
				String ben_use_score = jsonObj.getString("benUseScore");
				// 上期末可用积分
				String last_use_score = jsonObj.getString("lastUseScore");
				// 当期使用积分
				String use_score = jsonObj.getString("useScore");
				// 本期新增积分
				String add_score = jsonObj.getString("addScore");

				TelecomJiangsuBillSum telecomJiangsuBillSum = new TelecomJiangsuBillSum(taskMobile.getTaskid(), user_name,
						yearmonth, fee_new, into_fee, re_fee, ben_use_score, last_use_score, use_score, add_score);
				list.add(telecomJiangsuBillSum);
				return list;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlUserInfoParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
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
	 * 在用业务情况
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuBusiness> getBusiness(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomJiangsuParser.getComboUse", taskMobile.getTaskid());

		WebParam<TelecomJiangsuBusiness> webParam = new WebParam<TelecomJiangsuBusiness>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/nservice/businessHandleIndex/myBusiness";

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.GET, null, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getBusiness---在用业务情况" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomJiangsuBusiness> list = htmlBusinessParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getBusiness---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析在用业务情况
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuBusiness> htmlBusinessParser(String html, TaskMobile taskMobile) {

		List<TelecomJiangsuBusiness> list = new ArrayList<TelecomJiangsuBusiness>();
		try {
			
			TelecomJiangsuBusiness telecomJiangsuBusiness = null;
			
			String myBusinessArr = "var myBusinessArr=";
			int i = html.indexOf(myBusinessArr);
			if (i > 0) {
				html = html.substring(i + myBusinessArr.length());
				String myBusinessArrSort = "}];";
				int j = html.indexOf(myBusinessArrSort);
				if (j > 0) {
					html = html.substring(0,j+2);
				}
			}

			Object obj = new JSONTokener(html).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 业务名称
				String offerSpecName = jsonObject.getString("offerSpecName");
				// 订购时间
				String startDt = jsonObject.getString("startDt");
				// 资费
				String fee = jsonObject.getString("bssPrice");
				
				telecomJiangsuBusiness = new TelecomJiangsuBusiness(taskMobile.getTaskid(), offerSpecName, startDt,
						fee);
				list.add(telecomJiangsuBusiness);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 业务名称
					String offerSpecName = jsonObject.getString("offerSpecName");
					// 订购时间
					String startDt = jsonObject.getString("startDt");
					// 资费
					String fee = jsonObject.getString("bssPrice");
					
					telecomJiangsuBusiness = new TelecomJiangsuBusiness(taskMobile.getTaskid(), offerSpecName, startDt,
							fee);
					list.add(telecomJiangsuBusiness);
					
				}
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlBusinessParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 在用业务情况Two
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuBusiness> getBusinessTwo(TaskMobile taskMobile, MessageLogin messageLogin)
			throws Exception {

		tracer.addTag("TelecomJiangsuParser.getBusinessTwo", taskMobile.getTaskid());

		WebParam<TelecomJiangsuBusiness> webParam = new WebParam<TelecomJiangsuBusiness>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/ywbl/ywbl_queryPackageInfoList.action?accNbr=" + messageLogin.getName()
					+ "&userType=4";

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.GET, null, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getBusinessTwo---在用业务情况" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomJiangsuBusiness> list = htmlBusinessTwoParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getBusinessTwo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析在用业务情况Two
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuBusiness> htmlBusinessTwoParser(String html, TaskMobile taskMobile) {

		List<TelecomJiangsuBusiness> list = new ArrayList<TelecomJiangsuBusiness>();
		try {

			TelecomJiangsuBusiness telecomJiangsuBusiness = null;
			Object obj = new JSONTokener(html).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 业务名称
				String offerSpecName = jsonObject.getString("offerSpecName");
				// 订购时间
				String startDt = jsonObject.getString("startDt");
				// 资费
				String referPrice = jsonObject.getString("referPrice");
				telecomJiangsuBusiness = new TelecomJiangsuBusiness(taskMobile.getTaskid(), offerSpecName, startDt,
						referPrice);
				list.add(telecomJiangsuBusiness);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 业务名称
					String offerSpecName = jsonObject.getString("offerSpecName");
					// 订购时间
					String startDt = jsonObject.getString("startDt");
					// 资费
					String referPrice = jsonObject.getString("referPrice");
					telecomJiangsuBusiness = new TelecomJiangsuBusiness(taskMobile.getTaskid(), offerSpecName, startDt,
							referPrice);
					list.add(telecomJiangsuBusiness);
				}
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlBusinessTwoParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 用户信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuUserInfo> getUserInfo(TaskMobile taskMobile)
			throws Exception {

		tracer.addTag("TelecomJiangsuParser.getComboUse", taskMobile.getTaskid());

		WebParam<TelecomJiangsuUserInfo> webParam = new WebParam<TelecomJiangsuUserInfo>();

		try {
			WebClient webClient = addcookie(taskMobile);
			
			//积分
			String scores = "";
			//余额
			String balance = "";

			String url = "http://js.189.cn/nservice/selfInfomation/myInfomation";

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.GET, null, null, null, null);
			
			String getscores = "http://js.189.cn/nservice/login/getScores";
			Page pagescores = getPage(webClient, taskMobile.getTaskid(), getscores, HttpMethod.POST, null, null, null, null);
			String getbalance = "http://js.189.cn/nservice/login/getBalance";
			Page pagebalance = getPage(webClient, taskMobile.getTaskid(), getbalance, HttpMethod.POST, null, null, null, null);

			if (null != pagescores) {
				scores = pagescores.getWebResponse().getContentAsString();
			}
			if (null != pagebalance) {
				balance = pagebalance.getWebResponse().getContentAsString();
			}
			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getUserInfo---用户信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomJiangsuUserInfo> list = htmlUserInfoParser(html, taskMobile,scores,balance);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getUserInfo---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
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
	private List<TelecomJiangsuUserInfo> htmlUserInfoParser(String html, TaskMobile taskMobile,String scores,String balance) {

		List<TelecomJiangsuUserInfo> list = new ArrayList<TelecomJiangsuUserInfo>();
		try {
			
			Document doc = Jsoup.parse(html);
			// 登录账户
			String productId = getchildByKeyword(doc, "登录账户");
			// 用户姓名
			String userName = getchildByKeyword(doc, "用户姓名");
			// 所在地区
			String areaCode = getchildByKeyword(doc, "所在地区");
			// 客户姓名
			String customer_name = getNextLabelByKeyword(doc, "客户姓名");
			// 证件类型
			String document_type = getNextLabelByKeyword(doc, "证件类型");
			// 证件号码
			String document_code = getNextLabelByKeyword(doc, "证件号码");
			// 通讯地址
			String userAddress = getNextLabelByKeyword(doc, "通讯地址");
			// 通讯编码
			String zipCode = getNextLabelByKeyword(doc, "通讯编码");
			// 联系人
			String contactPerson = getNextLabelByKeyword(doc, "联系人");
			// 联系电话
			String contactPhone = getNextLabelByKeyword(doc, "联系电话");
			// 电子邮件
			String email = getNextLabelByKeyword(doc, "电子邮件");

			TelecomJiangsuUserInfo telecomJiangsuUserInfo = new TelecomJiangsuUserInfo(taskMobile.getTaskid(), productId, userName,
					areaCode, customer_name, document_type, document_code, userAddress, zipCode, contactPerson,
					contactPhone, email, balance, scores);
			list.add(telecomJiangsuUserInfo);
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlUserInfoParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 账户明细
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuBill> getBill(TaskMobile taskMobile,
			String yearmonth) throws Exception {

		tracer.addTag("TelecomJiangsuParser.getBill", taskMobile.getTaskid());

		WebParam<TelecomJiangsuBill> webParam = new WebParam<TelecomJiangsuBill>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/nservice/billQuery/consumptionQuery";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("zhangqi", yearmonth));
			paramsList.add(new NameValuePair("style", "1"));

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getBill---账单明细" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomJiangsuBill> list = htmlBillParser(html, taskMobile,yearmonth);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getBill---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}
	
	/**
	 * 解析账户明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuBill> htmlBillParser(String html, TaskMobile taskMobile,String yearmonth) {

		List<TelecomJiangsuBill> list = new ArrayList<TelecomJiangsuBill>();
		try {
			
			if(!html.contains("您本月没产生费用")){
				JSONObject jsonObj = JSONObject.fromObject(html);
				//本期费用合计
				String dcc_charge = jsonObj.getString("dccCharge");
				//本期已付费用
				String dcc_bal_owe_used = jsonObj.getString("dccBalOweUsed");
				// 本期应付费用
				String dcc_owe_charge = jsonObj.getString("dccOweCharge");
				String consumptionList = jsonObj.getString("consumptionList");
				list = getdccBillList(list, consumptionList, dcc_charge, dcc_bal_owe_used, dcc_owe_charge, yearmonth, taskMobile.getTaskid(), 0);
			}
			
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlComboUseParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}
	
	/**
	 * 循环解析  dccBillList
	 * @param listBill
	 * @param json
	 * @param dcc_charge
	 * @param dcc_bal_owe_used
	 * @param dcc_owe_charge
	 * @param yearmonth
	 * @param taskid
	 * @param i
	 * @return
	 * @throws Exception
	 */
	private List<TelecomJiangsuBill> getdccBillList(List<TelecomJiangsuBill> listBill, String json, String dcc_charge,
			String dcc_bal_owe_used, String dcc_owe_charge, String yearmonth, String taskid, int i)
			throws Exception {
		
		i++;
		TelecomJiangsuBill TelecomJiangsuBill = null;
		JSONArray jsonArray = JSONArray.fromObject(json);
		for (Object object : jsonArray) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			// 计费名目
			String itemName = jsonObject.getString("dccBillItemName");
			// 金额
			String itemCharge = jsonObject.getString("dccBillFee");
			TelecomJiangsuBill = new TelecomJiangsuBill(taskid, dcc_charge, dcc_bal_owe_used, dcc_owe_charge, itemName,
					itemCharge, yearmonth, i+"");
			listBill.add(TelecomJiangsuBill);
			// dccBillList
			String dccBillList = jsonObject.getString("dccBillList");
			getdccBillList(listBill, dccBillList, dcc_charge, dcc_bal_owe_used, dcc_owe_charge, yearmonth,taskid, i);
		}
		return listBill;
	}

	/**
	 * 充值缴费
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuPay> getPay(TaskMobile taskMobile, MessageLogin messageLogin, String yearmonth)
			throws Exception {

		tracer.addTag("TelecomJiangsuParser.getPay", taskMobile.getTaskid());

		WebParam<TelecomJiangsuPay> webParam = new WebParam<TelecomJiangsuPay>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/nservice/pay/querypaylist";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("dccBillingCycle", yearmonth));
			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getPay---充值缴费" + taskMobile.getTaskid(), "<xmp>" + html + "</xmp>");
				List<TelecomJiangsuPay> list = htmlPayParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getPay---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析充值缴费
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuPay> htmlPayParser(String html, TaskMobile taskMobile) {

		List<TelecomJiangsuPay> list = new ArrayList<TelecomJiangsuPay>();
		try {
			TelecomJiangsuPay telecomJiangsuPay = null;
			JSONObject jsonObj = JSONObject.fromObject(html);
			
			JSONObject bodys = jsonObj.getJSONObject("bodys");
			
			if(bodys.has("dccControl101list")){
				String items = bodys.getString("dccControl101list");

				Object obj = new JSONTokener(items).nextValue();
				if (obj instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) obj;
					// 流水号
					String dccPaySerialNbr = jsonObject.getString("dccPaySerialNbr");
					// 被充值号码
					String dccPaymentNbr = jsonObject.getString("dccPaymentNbr");
					// 入帐时间
					String dccPaidTime = jsonObject.getString("dccPaidTime");
					/**
					 * 交费渠道 0-----------营业厅 1-----------网厅
					 * 2-----------欢GO客户端 3-----------翼支付 4-----------第三方支付
					 * 5-----------自助缴费 6-----------银行 7-----------其它
					 */
					String dccChargeSourceId = jsonObject.getString("dccChargeSourceId");
					/**
					 * 交费方式 11------------现金 12------------支票
					 * 14------------代缴 15------------充值
					 * 16------------套餐促销费用 17------------托收
					 * 18------------空中充值 19------------银行卡
					 * 20------------充值卡
					 */
					String dccPaymentMethod = jsonObject.getString("dccPaymentMethod");
					// 入帐金额（元）
					String dccPaymentAmount = jsonObject.getString("dccPaymentAmount");
					/**
					 * 使用范围 0-----------------通用 1-----------------专用
					 * 2-----------------用户 3-----------------用户帐目组
					 * 4-----------------帐户帐目组
					 */
					String tBalanceTypeId = jsonObject.getString("tBalanceTypeId");

					telecomJiangsuPay = new TelecomJiangsuPay(taskMobile.getTaskid(), dccPaySerialNbr,
							dccPaymentNbr, dccPaidTime, dccChargeSourceId, dccPaymentMethod, dccPaymentAmount,
							tBalanceTypeId);
					list.add(telecomJiangsuPay);

				} else if (obj instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) obj;
					for (Object object : jsonArray) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						// 流水号
						String dccPaySerialNbr = jsonObject.getString("dccPaySerialNbr");
						// 被充值号码
						String dccPaymentNbr = jsonObject.getString("dccPaymentNbr");
						// 入帐时间
						String dccPaidTime = jsonObject.getString("dccPaidTime");
						/**
						 * 交费渠道 0-----------营业厅 1-----------网厅
						 * 2-----------欢GO客户端 3-----------翼支付 4-----------第三方支付
						 * 5-----------自助缴费 6-----------银行 7-----------其它
						 */
						String dccChargeSourceId = jsonObject.getString("dccChargeSourceId");
						/**
						 * 交费方式 11------------现金 12------------支票
						 * 14------------代缴 15------------充值
						 * 16------------套餐促销费用 17------------托收
						 * 18------------空中充值 19------------银行卡
						 * 20------------充值卡
						 */
						String dccPaymentMethod = jsonObject.getString("dccPaymentMethod");
						// 入帐金额（元）
						String dccPaymentAmount = jsonObject.getString("dccPaymentAmount");
						/**
						 * 使用范围 0-----------------通用 1-----------------专用
						 * 2-----------------用户 3-----------------用户帐目组
						 * 4-----------------帐户帐目组
						 */
						String tBalanceTypeId = jsonObject.getString("tBalanceTypeId");

						telecomJiangsuPay = new TelecomJiangsuPay(taskMobile.getTaskid(), dccPaySerialNbr,
								dccPaymentNbr, dccPaidTime, dccChargeSourceId, dccPaymentMethod, dccPaymentAmount,
								tBalanceTypeId);
						list.add(telecomJiangsuPay);

					}
				}
			}
			
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlPayParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 余额变动明细
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuBalance> getBalanceRecord(TaskMobile taskMobile,String yearmonth) throws Exception {

		tracer.addTag("TelecomJiangsuParser.getBalanceRecord", taskMobile.getTaskid());

		WebParam<TelecomJiangsuBalance> webParam = new WebParam<TelecomJiangsuBalance>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/nservice/arrears/balanceDetail";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("zhangqi", yearmonth));
			paramsList.add(new NameValuePair("type", "0"));
			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getBalanceRecord---余额变动明细" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomJiangsuBalance> list = htmlBalanceParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getBalanceRecord---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析余额变动明细
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuBalance> htmlBalanceParser(String html, TaskMobile taskMobile) {

		List<TelecomJiangsuBalance> list = new ArrayList<TelecomJiangsuBalance>();
		try {
			TelecomJiangsuBalance telecomJiangsuBalance = null;
			JSONObject jsonObj = JSONObject.fromObject(html);
			if(jsonObj.has("arrBalance")){
				String items = jsonObj.getString("arrBalance");

				Object obj = new JSONTokener(items).nextValue();
				if (obj instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) obj;
					// 时间 状态时间（变动时间）
					String dccStateDate = jsonObject.getString("dccStateDate");
					// 时间 状态时间（变动时间）
					String dccStateTime = jsonObject.getString("dccStateTime");
					
					// 余额类型 余额类型标识0：通用余额 1：专用余额2：用户级3：用户帐目组级4：帐户帐目组级
					String aocUnit = jsonObject.getString("aocUnit");
					// 入帐（元） 本期入帐
					String dccPaymentAmount = jsonObject.getString("dccPaymentAmount");
					// 支出（元） 本期支出
					String dccBalUsedAmount = jsonObject.getString("dccBalUsedAmount");
					/**
					 * 变动类型 
					 * 0-------现金充值
					 * 1-------预存返还
					 * 2-------赠费
					 * 3-------退费
					 * 4-------积分兑换
					 * 5-------话费支出"
					 * 6-------代收费
					 * 7-------余额失效
					 * 8-------其它支出
					 * 9-------转坏帐
					 */
					String dccBalUnitTypeId = jsonObject.getString("dccBalUnitTypeId");
					// 余额（元）
					String balanceAmount = jsonObject.getString("balanceAmount");
					// 变动号码 
					String dccCounts = jsonObject.getString("userNumber");

					telecomJiangsuBalance = new TelecomJiangsuBalance(taskMobile.getTaskid(), dccStateDate+dccStateTime, aocUnit,
							dccPaymentAmount, dccBalUsedAmount, dccBalUnitTypeId, balanceAmount, dccCounts);
					list.add(telecomJiangsuBalance);

				} else if (obj instanceof JSONArray) {
					JSONArray jsonArray = (JSONArray) obj;
					for (Object object : jsonArray) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						// 时间 状态时间（变动时间）
						String dccStateDate = jsonObject.getString("dccStateDate");
						// 时间 状态时间（变动时间）
						String dccStateTime = jsonObject.getString("dccStateTime");
						
						// 余额类型 余额类型标识0：通用余额 1：专用余额2：用户级3：用户帐目组级4：帐户帐目组级
						String aocUnit = jsonObject.getString("aocUnit");
						// 入帐（元） 本期入帐
						String dccPaymentAmount = jsonObject.getString("dccPaymentAmount");
						// 支出（元） 本期支出
						String dccBalUsedAmount = jsonObject.getString("dccBalUsedAmount");
						/**
						 * 变动类型 
						 * 0-------现金充值
						 * 1-------预存返还
						 * 2-------赠费
						 * 3-------退费
						 * 4-------积分兑换
						 * 5-------话费支出"
						 * 6-------代收费
						 * 7-------余额失效
						 * 8-------其它支出
						 * 9-------转坏帐
						 */
						String dccBalUnitTypeId = jsonObject.getString("dccBalUnitTypeId");
						// 余额（元）
						String balanceAmount = jsonObject.getString("balanceAmount");
						// 变动号码 
						String dccCounts = jsonObject.getString("userNumber");

						telecomJiangsuBalance = new TelecomJiangsuBalance(taskMobile.getTaskid(), dccStateDate+dccStateTime, aocUnit,
								dccPaymentAmount, dccBalUsedAmount, dccBalUnitTypeId, balanceAmount, dccCounts);
						list.add(telecomJiangsuBalance);
					}
				}
			}

		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlBalanceParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 通话详单
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuCallRecord> getCallRecord(TaskMobile taskMobile,
			String begDate, String endDate) throws Exception {

		tracer.addTag("TelecomJiangsuParser.getCallRecord", taskMobile.getTaskid());

		WebParam<TelecomJiangsuCallRecord> webParam = new WebParam<TelecomJiangsuCallRecord>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/nservice/listQuery/queryList";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("listType", "1"));
			paramsList.add(new NameValuePair("stTime", begDate));
			paramsList.add(new NameValuePair("endTime", endDate));

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getCallRecord---通话详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomJiangsuCallRecord> list = htmlCallRecordParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getCallRecord---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析通话详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuCallRecord> htmlCallRecordParser(String html, TaskMobile taskMobile) {

		List<TelecomJiangsuCallRecord> list = new ArrayList<TelecomJiangsuCallRecord>();
		try {
			TelecomJiangsuCallRecord telecomJiangsuCallRecord = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			String items = jsonObj.getString("respMsg");

			Object obj = new JSONTokener(items).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 对方号码
				String accNbr1 = jsonObject.getString("nbr");
				// 日期
				String startDateNew = jsonObject.getString("startDateNew");
				// 呼叫类型
				String ticketTypeNew = jsonObject.getString("ticketTypeNew");
				// 通话类型
				String durationType = jsonObject.getString("durationType");
				// 通话地区
				String areaCode = jsonObject.getString("areaCode");
				// 通话开始时间（时分秒）
				String startTimeNew = jsonObject.getString("startTimeNew");
				// 通话时长（时分秒）
				String durationCh = jsonObject.getString("duartionCh");
				// 通话时间（时分秒）
				String startTime = jsonObject.getString("startTime");
				// 金额（元）
				String ticketChargeCh = jsonObject.getString("ticketChargeCh");

				telecomJiangsuCallRecord = new TelecomJiangsuCallRecord(taskMobile.getTaskid(), accNbr1, startDateNew,
						ticketTypeNew, durationType, areaCode, startTimeNew, durationCh, startTime, ticketChargeCh);
				list.add(telecomJiangsuCallRecord);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object1 : jsonArray) {
					JSONArray jsonArray2 = JSONArray.fromObject(object1);
					for (Object object : jsonArray2) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						// 对方号码
						String accNbr1 = jsonObject.getString("nbr");
						// 日期
						String startDateNew = jsonObject.getString("startDateNew");
						// 呼叫类型
						String ticketTypeNew = jsonObject.getString("ticketTypeNew");
						// 通话类型
						String durationType = jsonObject.getString("durationType");
						// 通话地区
						String areaCode = jsonObject.getString("areaCode");
						// 通话开始时间（时分秒）
						String startTimeNew = jsonObject.getString("startTimeNew");
						// 通话时长（时分秒）
						String durationCh = jsonObject.getString("duartionCh");
						// 通话时间（时分秒）
						String startTime = jsonObject.getString("startTime");
						// 金额（元）
						String ticketChargeCh = jsonObject.getString("ticketChargeCh");

						telecomJiangsuCallRecord = new TelecomJiangsuCallRecord(taskMobile.getTaskid(), accNbr1, startDateNew,
								ticketTypeNew, durationType, areaCode, startTimeNew, durationCh, startTime, ticketChargeCh);
						list.add(telecomJiangsuCallRecord);
					}
				}
				
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlCallRecordParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	/**
	 * 短信详单
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParam<TelecomJiangsuMessage> getMessage(TaskMobile taskMobile,String begDate,
			String endDate) throws Exception {

		tracer.addTag("TelecomJiangsuParser.getMessage", taskMobile.getTaskid());

		WebParam<TelecomJiangsuMessage> webParam = new WebParam<TelecomJiangsuMessage>();

		try {
			WebClient webClient = addcookie(taskMobile);

			String url = "http://js.189.cn/nservice/listQuery/queryList";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("listType", "2"));
			paramsList.add(new NameValuePair("stTime", begDate));
			paramsList.add(new NameValuePair("endTime", endDate));

			Page page = getPage(webClient, taskMobile.getTaskid(), url, HttpMethod.POST, paramsList, null, null, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("TelecomJiangsuParser.getMessage---短信详单" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				List<TelecomJiangsuMessage> list = htmlMessageParser(html, taskMobile);
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setList(list);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				webClient.close();
				return webParam;
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.getMessage---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}
		return null;
	}

	/**
	 * 解析短信详单
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	private List<TelecomJiangsuMessage> htmlMessageParser(String html, TaskMobile taskMobile) {

		List<TelecomJiangsuMessage> list = new ArrayList<TelecomJiangsuMessage>();
		try {
			TelecomJiangsuMessage telecomJiangsuMessage = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			String items = jsonObj.getString("respMsg");

			Object obj = new JSONTokener(items).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 对方号码
				String accNbr1 = jsonObject.getString("nbr");
				// 业务类型
				String ticketType = jsonObject.getString("ticketType");
				// 发送日期（年月日）
				String startDateNew = jsonObject.getString("startDateNew");
				// 发送开始时间（时分秒）
				String startTimeNew = jsonObject.getString("startTimeNew");
				// 金额（元）
				String ticketChargeCh = jsonObject.getString("ticketChargeCh");
				// 产品名称
				String productName = jsonObject.getString("productName");

				telecomJiangsuMessage = new TelecomJiangsuMessage(taskMobile.getTaskid(), accNbr1, ticketType,
						startDateNew, startTimeNew, ticketChargeCh, productName);
				list.add(telecomJiangsuMessage);

			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				
				for (Object object1 : jsonArray) {
					JSONArray jsonArray2 = JSONArray.fromObject(object1);
					for (Object object : jsonArray2) {
						JSONObject jsonObject = JSONObject.fromObject(object);
						// 对方号码
						String accNbr1 = jsonObject.getString("nbr");
						// 业务类型
						String ticketType = jsonObject.getString("ticketType");
						// 发送日期（年月日）
						String startDateNew = jsonObject.getString("startDateNew");
						// 发送开始时间（时分秒）
						String startTimeNew = jsonObject.getString("startTimeNew");
						// 金额（元）
						String ticketChargeCh = jsonObject.getString("ticketChargeCh");
						// 产品名称
						String productName = jsonObject.getString("productName");
						telecomJiangsuMessage = new TelecomJiangsuMessage(taskMobile.getTaskid(), accNbr1, ticketType,
								startDateNew, startTimeNew, ticketChargeCh, productName);
						list.add(telecomJiangsuMessage);

					}
				}
				
				
			}
		} catch (Exception e) {
			tracer.addTag("TelecomJiangsuParser.htmlMessageParser---ERROR---"+ taskMobile.getTaskid() ,eutils.getEDetail(e));
		}

		return list;

	}

	public static String getchildByKeyword(Document document, String keyword) {
		Elements es = document.select("p:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.child(0);
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("th:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}

}
