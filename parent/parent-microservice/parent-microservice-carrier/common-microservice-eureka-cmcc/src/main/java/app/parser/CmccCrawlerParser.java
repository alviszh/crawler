package app.parser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan; 
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import com.crawler.cmcc.domain.json.CallRecordBean;
import com.crawler.cmcc.domain.json.PayMsgBean;
import com.crawler.cmcc.domain.json.UserMessageJsonBean;
import com.crawler.cmcc.domain.json.VerifyBean;
import com.crawler.cmcc.domain.json.WebCmccParam;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.cmcc.CmccUserInfo;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.cmcc.CmccRemedyParameterRepository;
import com.microservice.dao.entity.crawler.cmcc.CmccCheckMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccPayMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccRemedyParameter;
import com.microservice.dao.entity.crawler.cmcc.CmccSMSMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserCallResult;
import com.module.htmlunit.WebCrawler;
import app.bean.CookieBean;
import app.bean.PageBean;
import app.commontracerlog.TracerLog;
import app.service.AsyncCmccGetDetailService;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.cmcc", "com.microservice.dao.entity.crawler.mobile" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.cmcc",
		"com.microservice.dao.repository.crawler.mobile" })
public class CmccCrawlerParser {

	public static final Logger log = LoggerFactory.getLogger(CmccLoginParser.class);
	private Gson gson = new Gson();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmccRemedyParameterRepository cmccRemedyParameterRepository;
	@Autowired
	private AsyncCmccGetDetailService asyncCmccGetDetailService;

	/**
	 * @Description: 发送第二次验证短信随机码
	 * @param cookie
	 * @param messageLogin
	 * @return String
	 * @throws Exception
	 */
	public String sendVerifySMS(String cookie, MessageLogin messageLogin) {

		tracer.addTag("CmccCrawlerParser sendVerifySMS", messageLogin.getTask_id());

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		List<CookieBean> cookies = transferJsonToMap(cookie);
		for (CookieBean cookieBean : cookies) {
			webClient.getCookieManager()
					.addCookie(new Cookie(cookieBean.getDomain(), cookieBean.getKey(), cookieBean.getValue()));
		}
		String html = "";
		String url = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/" + messageLogin.getName();
		try {
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Host", "shop.10086.cn");

			requestSettings.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());
			requestSettings.setAdditionalHeader("Accept", "*/*");
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());

			requestSettings.getRequestParameters().add(new NameValuePair("callback",
					"jQuery183008660428427514177_" + String.valueOf(System.currentTimeMillis())));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("_", String.valueOf(System.currentTimeMillis())));

			Page page = webClient.getPage(requestSettings);
			html = page.getWebResponse().getContentAsString();

			tracer.addTag("sendVerifySMS 二次验证短信发送", html);
			String json = html.substring(html.indexOf("(") + 1, html.length() - 1);

			VerifyBean verifyBean = gson.fromJson(json, VerifyBean.class);

			return verifyBean.getRetCode();

		} catch (Exception e) {
			tracer.addTag("发送第二次短信失败", e.getMessage());
			tracer.addTag("sendVerifySMS 二次验证短信发送", html);
			return null;
		}
	}

	private List<CookieBean> transferJsonToMap(String cookie) {
		Gson gson = new Gson();
		Type cookieType = new TypeToken<List<CookieBean>>() {
		}.getType();
		List<CookieBean> cookies = gson.fromJson(cookie, cookieType);
		return cookies;
	}

	/**
	 * @Description: 验证图片验证码
	 * @param code
	 * @param cookies
	 * @param mobileNum
	 * @return
	 */
	public Boolean preCheckCaptcha(String code, Set<Cookie> cookies, String mobileNum) {

		tracer.addTag("CmccCrawlerParser preCheckCaptcha", code);

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		String url = "http://shop.10086.cn/i/v1/res/precheck/" + mobileNum + "?captchaVal=" + code + "&_="
				+ System.currentTimeMillis();
		try {

			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
			requestSettings.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());
			requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			requestSettings.setAdditionalHeader("Cache-Control", "no-store, must-revalidate");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("expires", "0");
			requestSettings.setAdditionalHeader("If-Modified-Since", "0");
			requestSettings.setAdditionalHeader("pragma", "no-cache");
			requestSettings.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

			Page page = webClient.getPage(requestSettings);
			String html = page.getWebResponse().getContentAsString();

			VerifyBean verifyBean = gson.fromJson(html, VerifyBean.class);
			tracer.addTag("preCheckCaptcha 图片验证结果", verifyBean.toString());
			if ("000000".equals(verifyBean.getRetCode())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @Description: 二次验证
	 * @param messageLogin
	 * @param cookies
	 * @param codeOCR
	 * @return WebCmccParam
	 */
	public WebCmccParam secondAttestation(MessageLogin messageLogin, Set<Cookie> cookies, String codeOCR) {

		tracer.addTag("CmccCrawlerParser secondAttestation", messageLogin.getTask_id());

		WebCmccParam webCmccParam = new WebCmccParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		String html = "";
		String url = "https://shop.10086.cn/i/v1/fee/detailbilltempidentjsonp/" + messageLogin.getName()
				+ "?callback=jQuery183020907438300533676_" + System.currentTimeMillis() + "&pwdTempSerCode="
				+ dealWithPassword(messageLogin.getPassword()) + "&pwdTempRandCode="
				+ dealWithPassword(messageLogin.getSms_code()) + "&captchaVal=" + codeOCR + "&_="
				+ System.currentTimeMillis();
		try {
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());
			requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
			requestSettings.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

			Page page = webClient.getPage(requestSettings);
			html = page.getWebResponse().getContentAsString();
			tracer.addTag("secondAttestation 二次验证结果", html);
			String json = html.substring(html.indexOf("(") + 1, html.length() - 1);
			VerifyBean verifyBean = gson.fromJson(json, VerifyBean.class);
			tracer.addTag("二次验证json解析后的结果", verifyBean.getRetCode());
			webCmccParam.setCode_second(verifyBean.getRetCode());
			webCmccParam.setWebClient(webClient);
			return webCmccParam;

		} catch (Exception e) {
			tracer.addTag("二次验证出现错误", e.getMessage());
			tracer.addTag("secondAttestation 二次验证结果", html);
			return null;
		}

	}

	/**
	 * @Description: 二次验证中服务密码和随机密码加密
	 * @param password
	 * @return String
	 */
	public String dealWithPassword(String password) {
		byte[] encodeBase64 = null;
		try {
			encodeBase64 = Base64.encodeBase64(password.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new String(encodeBase64);
	}

	/**
	 * @Description: 获取用户信息
	 * @param messageLogin
	 * @param cookies
	 * @return Map<CmccUserInfo,Integer>
	 */
	@SuppressWarnings("unused")
	public WebCmccParam getUserMessage(MessageLogin messageLogin, Set<Cookie> cookies) {

		tracer.addTag("CmccCrawlerParser getUserMessage", messageLogin.getTask_id());
		WebCmccParam webCmccParam = new WebCmccParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		CmccUserInfo cmccUserInfo = new CmccUserInfo();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String url = "http://shop.10086.cn/i/v1/cust/mergecust/" + messageLogin.getName() + "?_="
					+ System.currentTimeMillis();
			WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);

			request.setAdditionalHeader("Host", "shop.10086.cn");
			request.setAdditionalHeader("pragma", "no-cache");
			request.setAdditionalHeader("expires", "0");
			request.setAdditionalHeader("If-Modified-Since", "0");
			request.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());

			UnexpectedPage page = webClient.getPage(request);
			int code = page.getWebResponse().getStatusCode();
			if (200 == code) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("getUserMessage 个人信息源码", html);
				if (html.contains("\"custInfoQryOut\": []")) {
					webCmccParam.setCode(403);
					return webCmccParam;
				}
				UserMessageJsonBean userMessageJsonBean = gson.fromJson(html, UserMessageJsonBean.class);
				if ("500003".equals(userMessageJsonBean.getRetCode())) {
					webCmccParam.setCode(403);
					return webCmccParam;
				}
				cmccUserInfo = userMessageJsonBean.getData().getCustInfoQryOut();
				cmccUserInfo.setTaskId(messageLogin.getTask_id());

				webCmccParam.setCmccUserInfo(cmccUserInfo);
				webCmccParam.setCode(code);
			} else {
				webCmccParam.setCode(code);
			}

		} catch (Exception e) {
			tracer.addTag("CmccCrawlerParser getUserMessage", e.getMessage());
			webCmccParam.setCode(403);
			return webCmccParam;
		}
		// 该用户没有用户信息
		if (null == cmccUserInfo) {
			webCmccParam.setCode(403);
			return webCmccParam;
		}
		webCmccParam = getPointValue(webCmccParam, messageLogin, webClient);

		webCmccParam = getFeeReal(webCmccParam, messageLogin, webClient);

		webCmccParam = getBusiPlan(webCmccParam, messageLogin, webClient);

		return webCmccParam;
	}

	/**
	 * @Description: 获取账户套餐信息
	 * @param webCmccParam
	 * @param messageLogin
	 * @param webClient
	 * @return
	 */
	private WebCmccParam getBusiPlan(WebCmccParam webCmccParam, MessageLogin messageLogin, WebClient webClient) {

		CmccUserInfo cmccUserInfo = webCmccParam.getCmccUserInfo();
		try {
			String url = "http://shop.10086.cn/i/v1/busi/plan/" + messageLogin.getName() + "?_="
					+ System.currentTimeMillis();
			WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);

			request.setAdditionalHeader("Host", "shop.10086.cn");
			request.setAdditionalHeader("pragma", "no-cache");
			request.setAdditionalHeader("expires", "0");
			request.setAdditionalHeader("If-Modified-Since", "0");
			request.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			request.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			request.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());

			Page page = webClient.getPage(request);
			int code = page.getWebResponse().getStatusCode();
			if (200 == code) {
				String html = page.getWebResponse().getContentAsString();
				if (html.contains("[]")) {
					webCmccParam.setBusiPlanStatus(201);
					return webCmccParam;
				}
				tracer.addTag("getBusiPlan 账户套餐", html);
				UserMessageJsonBean userMessageJsonBean = gson.fromJson(html, UserMessageJsonBean.class);
				if ("500003".equals(userMessageJsonBean.getRetCode())) {
					webCmccParam.setBusiPlanStatus(403);
					return webCmccParam;
				}
				cmccUserInfo.setBrandName(userMessageJsonBean.getData().getBrandName());
				cmccUserInfo.setCurPlanId(userMessageJsonBean.getData().getCurPlanId());
				cmccUserInfo.setCurPlanName(userMessageJsonBean.getData().getCurPlanName());
				cmccUserInfo.setNextPlanId(userMessageJsonBean.getData().getNextPlanId());
				cmccUserInfo.setNextPlanName(userMessageJsonBean.getData().getNextPlanName());

				webCmccParam.setCmccUserInfo(cmccUserInfo);
				webCmccParam.setBusiPlanStatus(code);
				return webCmccParam;
			}
		} catch (Exception e) {
			webCmccParam.setBusiPlanStatus(404);
			return webCmccParam;
		}
		webCmccParam.setBusiPlanStatus(404);
		return webCmccParam;
	}

	/**
	 * @Description: 获取账户可用余额
	 * @param webCmccParam
	 * @param messageLogin
	 * @param webClient
	 * @return
	 */
	private WebCmccParam getFeeReal(WebCmccParam webCmccParam, MessageLogin messageLogin, WebClient webClient) {

		CmccUserInfo cmccUserInfo = webCmccParam.getCmccUserInfo();
		try {
			String url = "http://shop.10086.cn/i/v1/fee/real/" + messageLogin.getName() + "?_="
					+ System.currentTimeMillis();
			WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);

			request.setAdditionalHeader("Host", "shop.10086.cn");
			request.setAdditionalHeader("pragma", "no-cache");
			request.setAdditionalHeader("expires", "0");
			request.setAdditionalHeader("If-Modified-Since", "0");
			request.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			request.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			request.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());

			Page page = webClient.getPage(request);
			int code = page.getWebResponse().getStatusCode();
			if (200 == code) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("getFeeReal 账户可用余额", html);
				if (html.contains("[]")) {
					webCmccParam.setAccountMsgStatus(201);
					return webCmccParam;
				}
				UserMessageJsonBean userMessageJsonBean = gson.fromJson(html, UserMessageJsonBean.class);
				if ("500003".equals(userMessageJsonBean.getRetCode())) {
					webCmccParam.setAccountMsgStatus(403);
					return webCmccParam;
				}
				String curFee = userMessageJsonBean.getData().getCurFee();
				String curFeeTotal = userMessageJsonBean.getData().getCurFeeTotal();
				String realFee = userMessageJsonBean.getData().getRealFee();

				cmccUserInfo.setCurFee(curFee);
				cmccUserInfo.setCurFeeTotal(curFeeTotal);
				cmccUserInfo.setRealFee(realFee);
				webCmccParam.setCmccUserInfo(cmccUserInfo);
				webCmccParam.setAccountMsgStatus(code);
				return webCmccParam;
			}
		} catch (Exception e) {
			webCmccParam.setAccountMsgStatus(404);
			return webCmccParam;
		}
		webCmccParam.setAccountMsgStatus(404);
		return webCmccParam;
	}

	/**
	 * @Description: 获取积分情况
	 * @param webCmccParam
	 * @param messageLogin
	 * @param cookies
	 * @return
	 */
	private WebCmccParam getPointValue(WebCmccParam webCmccParam, MessageLogin messageLogin, WebClient webClient) {

		CmccUserInfo cmccUserInfo = webCmccParam.getCmccUserInfo();
		if (null == cmccUserInfo) {
			webCmccParam.setPointValueCode(403);
			return webCmccParam;
		}
		try {
			String url = "http://shop.10086.cn/i/v1/point/sum/" + messageLogin.getName() + "?_="
					+ System.currentTimeMillis();
			WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);

			request.setAdditionalHeader("Host", "shop.10086.cn");
			request.setAdditionalHeader("pragma", "no-cache");
			request.setAdditionalHeader("expires", "0");
			request.setAdditionalHeader("If-Modified-Since", "0");
			request.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			request.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			request.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());

			Page page = webClient.getPage(request);
			int code = page.getWebResponse().getStatusCode();
			if (200 == code) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("getPointValue 账户积分情况", html);
				if (html.contains("[]")) {
					webCmccParam.setPointValueCode(201);
					return webCmccParam;
				} else {
					UserMessageJsonBean userMessageJsonBean = gson.fromJson(html, UserMessageJsonBean.class);
					if ("500003".equals(userMessageJsonBean.getRetCode())) {
						webCmccParam.setPointValueCode(403);
						return webCmccParam;
					}
					String pointValue = userMessageJsonBean.getData().getPointValue();
					cmccUserInfo.setPointValue(pointValue);
					webCmccParam.setCmccUserInfo(cmccUserInfo);
					webCmccParam.setPointValueCode(code);
					return webCmccParam;
				}
			}

		} catch (Exception e) {
			webCmccParam.setPointValueCode(404);
			return webCmccParam;
		}

		webCmccParam.setPointValueCode(404);
		return webCmccParam;
	}

	/**
	 * @Description: 获取用户通话记录
	 * @param messageLogin
	 * @param cookies
	 * @return Map<List<CmccUserCallResult>,Integer>
	 */
	public WebCmccParam getMobileRecord(MessageLogin messageLogin, Set<Cookie> cookies,TaskMobile taskMobile) {

		tracer.addTag("CmccCrawlerParser getMobileRecord", messageLogin.getTask_id());

		WebCmccParam webCmccParam = new WebCmccParam();
		List<CmccUserCallResult> cmccUserCallResults = new ArrayList<CmccUserCallResult>();
		// 爬取通话详单
		List<PageBean> pageBeans = getData(cookies, messageLogin.getName(), "02",taskMobile);
		Integer code = null;
		if (null != pageBeans) {
			for (PageBean pageBean : pageBeans) {
				code = pageBean.getPages().getWebResponse().getStatusCode();
				if (200 == code) {
					String html = pageBean.getPages().getWebResponse().getContentAsString();
					String json = html.substring(html.indexOf("(") + 1, html.length() - 1);
					// tracer.addTag("getMobileRecord 第"+i+"个通话信息",json);
					try {
						Type userListType = new TypeToken<CallRecordBean<List<CmccUserCallResult>>>() {
						}.getType();
						
						CallRecordBean<List<CmccUserCallResult>> callRecordBean = gson.fromJson(json, userListType);

						if ("000000".equals(callRecordBean.getRetCode())) {
							List<CmccUserCallResult> results = callRecordBean.data;
							// 查看如果当月数据超过200条后，url变化
							// if(results != null && 200 == results.size()){
							// tracer.addTag("通话记录超过200条", "true");
							// getCall(cookies,messageLogin.getName(),pageBean.getStartYear());
							// }

							for (CmccUserCallResult cmccUserCallResult : results) {
								cmccUserCallResult.setTaskId(messageLogin.getTask_id());
								cmccUserCallResult.setMobileNum(messageLogin.getName());
								cmccUserCallResult.setStartYear(pageBean.getStartYear().substring(0, 4));
								cmccUserCallResults.add(cmccUserCallResult);
							}
						} else
						// 当月没有通话记录
						if ("2039".equals(callRecordBean.getRetCode())) {
							continue;
						} else
						// 登录失效
						if ("500003".equals(callRecordBean.getRetCode())) {
							continue;
						} else
						// 临时身份凭证不存在
						if ("520001".equals(callRecordBean.getRetCode())) {
							continue;
						} else {
							addRemedy(messageLogin.getTask_id(), "02", pageBean.getPages().getUrl().toString(),
									pageBean.getStartYear().substring(0, 4));
							tracer.addTag("通话记录二次补救", pageBean.getPages().getUrl().toString());
						}
					} catch (Exception e) {
						tracer.addTag("usercall", "error");
						tracer.addTag("移动通话记录出错", e.getMessage());
						continue;
					}

				} else {
					tracer.addTag("通话记录返回的状态吗", code + "");
					addRemedy(messageLogin.getTask_id(), "02", pageBean.getPages().getUrl().toString(),
							pageBean.getStartYear().substring(0, 4));
					tracer.addTag("通话记录二次补救", pageBean.getPages().getUrl().toString());
				}
			}
			webCmccParam.setResults(cmccUserCallResults);
			webCmccParam.setCode(code);
			return webCmccParam;

		}

		return null;
	}

	/**
	 * //查看如果当月数据超过200条后，url变化
	 * 
	 * @param cookies
	 * @param name
	 * @param startYear
	 */
	// private void getCall(Set<Cookie> cookies, String name, String startYear)
	// {
	//
	// WebClient webClient = WebCrawler.getInstance().getNewWebClient();
	// for(Cookie cookie : cookies){
	// webClient.getCookieManager().addCookie(cookie);
	// }
	//
	// try{
	// /*https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/15210072522?callback=jQuery18305462428283535743_1524809846286&curCuror=1&step=100&qryMonth=201803&billType=03&_=1524810136321*/
	// String url =
	// "https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/"+name+"?callback=jQuery1830007205199076461_"+System.currentTimeMillis()+"&curCuror=201&step=1000&qryMonth="+startYear+"&billType=02&_="+System.currentTimeMillis();
	// WebRequest requestSettings = new WebRequest(new URL(url),
	// HttpMethod.GET);
	// requestSettings.setAdditionalHeader("Referer",
	// "http://shop.10086.cn/i/?f=home&welcome="+System.currentTimeMillis());
	// requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate,
	// sdch, br");
	// requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	// requestSettings.setAdditionalHeader("Connection", "keep-alive");
	// requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
	// requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows
	// NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)
	// Chrome/58.0.3029.110 Safari/537.36");
	//
	// Page page = webClient.getPage(requestSettings);
	// String html = page.getWebResponse().getContentAsString();
	// tracer.addTag("200", "true");
	// tracer.addTag(startYear+"超过200数据", "<xmp>"+html+"</xmp>");
	//
	// }catch(Exception e){
	// tracer.addTag("CmccCrawlerParser getData",e.getMessage());
	// }
	// }

	/**
	 * @Description: 获取用户短信信息
	 * @param messageLogin
	 * @param cookies
	 * @return
	 */
	public WebCmccParam getSMSMsg(MessageLogin messageLogin, Set<Cookie> cookies, TaskMobile taskMobile) {

		tracer.addTag("CmccCrawlerParser getSMSMsg", messageLogin.getTask_id());

		WebCmccParam webCmccParam = new WebCmccParam();
		List<CmccSMSMsgResult> cmccSMSMsgResults = new ArrayList<CmccSMSMsgResult>();
		List<PageBean> pages = getData(cookies, messageLogin.getName(), "03", taskMobile);

		Integer code = null;
		if (null != pages && pages.size() > 0) {
			int i = 1;
			for (PageBean page : pages) {
				code = page.getPages().getWebResponse().getStatusCode();
				if (200 == code) {
					if (page.getPages().getWebResponse().getContentAsString().contains("您选择时间段没有详单记录哦")) {
						continue;
					}
					try {
						String html = page.getPages().getWebResponse().getContentAsString();
						String json = html.substring(html.indexOf("(") + 1, html.length() - 1);
						// tracer.addTag("getSMSMsg 第"+i+"个短信信息",json);
						Type userListType = new TypeToken<CallRecordBean<List<CmccSMSMsgResult>>>() {
						}.getType();

						CallRecordBean<List<CmccSMSMsgResult>> callRecordBean = gson.fromJson(json, userListType);
						if ("000000".equals(callRecordBean.getRetCode())) {
							List<CmccSMSMsgResult> results = callRecordBean.data;
							for (CmccSMSMsgResult cmccSMSMsgResult : results) {
								// System.out.println("短信记录具体数据
								// ===============》》"+cmccSMSMsgResult.toString());
								cmccSMSMsgResult.setTaskId(messageLogin.getTask_id());
								cmccSMSMsgResult.setMobileNum(messageLogin.getName());
								cmccSMSMsgResult.setStartYear(page.getStartYear().substring(0, 4));
								cmccSMSMsgResults.add(cmccSMSMsgResult);
							}
						} else if ("520001".equals(callRecordBean.getRetCode())) {
							webCmccParam.setCode(403);
							return webCmccParam;
						} else {
							tracer.addTag("短信二次补救", page.getPages().getUrl().toString());
							addRemedy(messageLogin.getTask_id(), "03", page.getPages().getUrl().toString(),
									page.getStartYear().substring(0, 4));

						}

					} catch (Exception e) {
						tracer.addTag("爬取短信当中出错" + i, e.getMessage());
						continue;
					}
					i++;
				} else {
					tracer.addTag("短信记录返回的状态吗", code + "");
					addRemedy(messageLogin.getTask_id(), "03", page.getPages().getUrl().toString(),
							page.getStartYear().substring(0, 4));
					tracer.addTag("短信二次补救", page.getPages().getUrl().toString());
				}
			}
			webCmccParam.setCmccSMSMsgResults(cmccSMSMsgResults);
			webCmccParam.setCode(200);
			return webCmccParam;
		} else {
			webCmccParam.setCode(404);
			return webCmccParam;
		}
	}

	public Future<List<PageBean>> aysncData(int i, String mobileNum, String billType, WebClient webClient,
			TaskMobile taskMobile) {
		tracer.addTag("aysncData开始执行[i]"+i+"：", System.currentTimeMillis() + "");
		if(i!=0){
			try {
				Thread.sleep(1100L); // 流量控制
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} 
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - i);
		Date date = curr.getTime();
		String qryMonth = sdf.format(date);
		Future<List<PageBean>> future = asyncCmccGetDetailService.asyncRetryableGetData(qryMonth, mobileNum, billType,
				webClient,taskMobile);
		return future;
	}

	public List<PageBean> getData(Set<Cookie> cookies, String mobileNum, String billType,TaskMobile taskMobile){
		System.out.println("-----getData----billType "+billType+"--------mobileNum "+mobileNum);
		tracer.addTag("CmccCrawlerParserGetData",mobileNum);
		
		List<PageBean> pages = new ArrayList<PageBean>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();		
		webClient.getOptions().setJavaScriptEnabled(false);
		for(Cookie cookie : cookies){ 
			webClient.getCookieManager().addCookie(cookie);
		}
		
		Future<List<PageBean>> data1 = aysncData(0,mobileNum,billType,webClient,taskMobile);
		Future<List<PageBean>> data2 = aysncData(1,mobileNum,billType,webClient,taskMobile);
		Future<List<PageBean>> data3 = aysncData(2,mobileNum,billType,webClient,taskMobile);
		Future<List<PageBean>> data4 = aysncData(3,mobileNum,billType,webClient,taskMobile);
		Future<List<PageBean>> data5 = aysncData(4,mobileNum,billType,webClient,taskMobile);
		Future<List<PageBean>> data6 = aysncData(5,mobileNum,billType,webClient,taskMobile);
		
		while(true){
			if(data1.isDone() && data2.isDone() && data3.isDone() && data4.isDone() && data5.isDone() && data6.isDone()){
				tracer.addTag("六个月数据", "异步获取数据方法已完成"+mobileNum);
				/*try {
					List<PageBean> list1 = data1.get();
					List<PageBean> list2 = data2.get();
					List<PageBean> list3 = data3.get();
					List<PageBean> list4 = data4.get();
					List<PageBean> list5 = data5.get();
					List<PageBean> list6 = data6.get();
					
					pages.addAll(list1);
					pages.addAll(list2);
					pages.addAll(list3);
					pages.addAll(list4);
					pages.addAll(list5);
					pages.addAll(list6);
					return pages;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}		*/	
				List<PageBean> list1 = null;
				try {
					list1 = data1.get();
				} catch (Exception e) {
					tracer.addTag("mobileNum:"+mobileNum+" billType:"+billType+" data1.exception", e.toString());
					e.printStackTrace();
				}
				if(list1!=null){
					pages.addAll(list1);
				}
				
				List<PageBean> list2 = null;
				try {
					list2 = data2.get();
				} catch (Exception e) {
					tracer.addTag("mobileNum:"+mobileNum+" billType:"+billType+" data2.exception", e.toString());
					e.printStackTrace();
				}
				if(list2!=null){
					pages.addAll(list2);
				}
				
				List<PageBean> list3 = null;
				try {
					list3 = data3.get();
				} catch (Exception e) {
					tracer.addTag("mobileNum:"+mobileNum+" billType:"+billType+" data3.exception", e.toString());
					e.printStackTrace();
				}
				if(list3!=null){
					pages.addAll(list3);
				}
				
				List<PageBean> list4 = null;
				try {
					list4 = data4.get();
				} catch (Exception e) {
					tracer.addTag("mobileNum:"+mobileNum+" billType:"+billType+" data4.exception", e.toString());
					e.printStackTrace();
				}
				if(list4!=null){
					pages.addAll(list4);
				}
				
				List<PageBean> list5 = null;
				try {
					list5 = data5.get();
				} catch (Exception e) {
					tracer.addTag("mobileNum:"+mobileNum+" billType:"+billType+" data5.exception", e.toString());
					e.printStackTrace();
				}
				if(list5!=null){
					pages.addAll(list5);
				}
				
				List<PageBean> list6 = null;
				try {
					list6 = data6.get();
				} catch (Exception e) {
					tracer.addTag("mobileNum:"+mobileNum+" billType:"+billType+" data6.exception", e.toString());
					e.printStackTrace();
				}
				if(list6!=null){
					pages.addAll(list6);
				}
				
				
				return pages;
				
			}
			
		}
		
		
		
		
//		Calendar curr = Calendar.getInstance();
//		for(int i=0;i<6;i++){
//			try {
//				Thread.sleep(1000L);						//流量控制
//			} catch (InterruptedException e1) {
//				e1.printStackTrace();
//			}
//			if(i==0){
//				curr.set(Calendar.MONTH,curr.get(Calendar.MONTH));
//			}else{
//				curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)-1);			
//			}
//			Date date=curr.getTime();
//			String qryMonth = sdf.format(date);
//			
//			Future<List<PageBean>> future1 = asyncCmccGetDetailService.asyncRetryableGetData(qryMonth, mobileNum, billType, webClient);
//			while(true){
//				if(future1.isDone()){
//					tracer.addTag(qryMonth, "异步获取数据方法已完成");
//					try {
//						List<PageBean> list = future1.get();
//						pages.addAll(list);
//						break;
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					} catch (ExecutionException e) {
//						e.printStackTrace();
//					}			
//				}
//				
//			}
//
////			int m = 201;
////			for(int y = 1;y < m; y += 200){				
////				try {
////					tracer.addTag(qryMonth+"-for", "m "+m+"  y "+y); 
////					Thread.sleep(200L);						//流量控制
////				} catch (InterruptedException e1) {
////					e1.printStackTrace();
////				}
////				//https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/15210072522?callback=jQuery18305462428283535743_1524809846286&curCuror=1&step=100&qryMonth=201803&billType=03&_=1524810136321
////				String url = "https://shop.10086.cn/i/v1/fee/detailbillinfojsonp/"+mobileNum+"?callback=jQuery1830007205199076461_"+System.currentTimeMillis()+"&curCuror="+y+"&step=1000&qryMonth="+qryMonth+"&billType="+billType+"&_="+randTimeMillis;;
////				Page page = null;
////				try{ 
////					WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
////					requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome="+welcomeTimeMillis);
////					requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
////					requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
////					requestSettings.setAdditionalHeader("Connection", "keep-alive");
////					requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
////					requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
////					long startTime = System.currentTimeMillis();
////					page = webClient.getPage(requestSettings);
////					long endTime = System.currentTimeMillis();
////					String html = page.getWebResponse().getContentAsString();
////					int statusCode = page.getWebResponse().getStatusCode();
////					if(html!=null&&html.contains("jQuery")){
////						int subStart = html.indexOf("({"); 
////						html = html.substring(subStart+1, html.length()-1);
////					}
////					tracer.addTag(qryMonth+" y:"+y+" m:"+m, html);
////					tracer.addTag(qryMonth+" y:"+y+" m:"+m +" 耗时 "+(endTime-startTime)+":ms","[StatusCode] "+statusCode+" [URL] "+url);
////					
////					//第一页或最后一页有 totalNum by meidi 20180510
////					CallRecordBean<List> callRecordBean = gson.fromJson(html,CallRecordBean.class);
////					
////					if(callRecordBean!=null){ 
////						if(callRecordBean.getTotalNum()!=null){ // 第一页或最后一页有 totalNum 的情况 by meidi 20170510
////							int totalNum = callRecordBean.getTotalNum();
////							tracer.addTag(qryMonth+" y:"+y+" m:"+m+" 总条数", totalNum+"");
////							if(totalNum>=200){
////								m = totalNum;
////							}
////							tracer.addTag(qryMonth+"-Total-m-max", m+"");
////							PageBean pageBean = new PageBean();
////							pageBean.setPages(page);
////							pageBean.setStartYear(qryMonth);
////							pages.add(pageBean);  
////						}else if(callRecordBean.getData()!=null){ // 没有 totalNum 但条数是200 by meidi 20170510
////							int size = callRecordBean.getData().size();
////							tracer.addTag(qryMonth+" y:"+y+" m:"+m +"-noTotal-RealSize", size+"");
////							if(size>=200){
////								m+=200;
////							}
////							tracer.addTag(qryMonth+"-noTotal-m-max", m+"");
////							PageBean pageBean = new PageBean();
////							pageBean.setPages(page);
////							pageBean.setStartYear(qryMonth);
////							pages.add(pageBean);  
////						}else{
////							tracer.addTag(qryMonth+" y:"+y+" m:"+m +" TODO HTML", html);
////							tracer.addTag(qryMonth+" y:"+y+" m:"+m +" TODO URL", "[StatusCode] "+statusCode+" [URL] "+url);
////						} 
////					}else{
////						tracer.addTag(qryMonth+" y:"+y+" m:"+m+" callRecordBeanNull", "callRecordBean is null");
////						tracer.addTag(qryMonth+" y:"+y+" m:"+m+" callRecordBeanNull HTML", html);
////						tracer.addTag(qryMonth+" y:"+y+" m:"+m+" callRecordBeanNull URL", "[StatusCode] "+statusCode+" [URL] "+url);
////					}
////					/*
////					if(callRecordBean!=null&&callRecordBean.getTotalNum()!=null){
////					}else if(callRecordBean!=null&&callRecordBean.getData()!=null){//现在
////						
////					}else if(html.contains("您选择时间段没有详单记录")){
////						break;
////					}else if(html.contains("特殊时期不受理详单查询业务")){ //{"data": [], "retCode": "3019",  "retMsg": "特殊时期不受理详单查询业务，感谢您使用中国移动网上商城", "sOperTime": "20180510200754"}
////						break;
////					}else if(html.contains("临时身份凭证不存在")){ 
////						break;
////					}else if(html.contains("500003")){		//{"retCode":"500003","retMsg":"not login.but must login.sso flag."}
////						break; 
////					}else if(html.contains("999999")){ //详单查询异常
////						PageBean pageBean = new PageBean();
////						pageBean.setPages(page);
////						pageBean.setStartYear(qryMonth);
////						pages.add(pageBean);
////					}else if(StringUtils.isBlank(html)){ //空白页
////						PageBean pageBean = new PageBean();
////						pageBean.setPages(page);
////						pageBean.setStartYear(qryMonth);
////						pages.add(pageBean);
////					}else if(html.contains("9700")){ //流量控制
////						PageBean pageBean = new PageBean();
////						pageBean.setPages(page);
////						pageBean.setStartYear(qryMonth);
////						pages.add(pageBean);
////					}else{
////						PageBean pageBean = new PageBean();
////						pageBean.setPages(page);
////						pageBean.setStartYear(qryMonth);
////						pages.add(pageBean);
////					}*/
//////					if(!html.contains("000000")){
//////						break;
//////					}
////					
//////					PageBean pageBean = new PageBean();
//////					pageBean.setPages(page);
//////					pageBean.setStartYear(sdf.format(date));
//////					pages.add(pageBean);
////					
////					//现在第一页就有 totalNum by meidi 20170510
////					//m+=200; 
////					//现在第一页就有 totalNum by meidi 20170510
////					
//////					tracer.addTag(sdf.format(date)+"当前的m值:"+m, String.valueOf(m));
////				}catch(Exception e){
////					tracer.addTag("CmccCrawlerParserException","未知的异常");//固定key value 以便zipkin查询
////					tracer.addTag("CmccCrawlerParserException.getUrl",url);//  
////					tracer.addTag("CmccCrawlerParserException.getMessage",e.getMessage());//一般页面404的时候会出现Json解析的异常，因为此时返回的不是json，而是html 404源码
////					//continue outer;
////				}
////			}
//		}
		
	}

	/**
	 * @Description: 获取用户每月消费账单
	 * @param messageLogin
	 * @param cookies
	 * @return
	 */
	public WebCmccParam getCheckMsg(MessageLogin messageLogin, Set<Cookie> cookies) {

		tracer.addTag("CmccCrawlerParser getCheckMsg", messageLogin.getTask_id());
		try {
			Thread.sleep(600L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		WebCmccParam webCmccParam = new WebCmccParam();
		List<CmccCheckMsgResult> list = new ArrayList<CmccCheckMsgResult>();

		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		Calendar curr = Calendar.getInstance();
		curr.set(Calendar.MONTH, curr.get(Calendar.MONTH));
		Date endDate = curr.getTime();
		curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 5);
		Date startDate = curr.getTime();
		try {
			String url = "http://shop.10086.cn/i/v1/fee/billinfo/" + messageLogin.getName() + "?bgnMonth="
					+ sdf.format(endDate) + "&endMonth=" + startDate + "&_=" + System.currentTimeMillis();
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());
			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			requestSettings.setAdditionalHeader("pragma", "no-cache");
			requestSettings.setAdditionalHeader("expires", "0");
			requestSettings.setAdditionalHeader("Cache-Control", "no-store, must-revalidate");
			requestSettings.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

			Page page = webClient.getPage(requestSettings);

			int code = page.getWebResponse().getStatusCode();
			if (200 == code) {
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("getCheckMsg 用户每月消费账单", html);

				Type userListType = new TypeToken<CallRecordBean<List<CmccCheckMsgResult>>>() {
				}.getType();

				CallRecordBean<List<CmccCheckMsgResult>> callRecordBean = gson.fromJson(html, userListType);
				if ("000000".equals(callRecordBean.getRetCode())) {
					List<CmccCheckMsgResult> cmccCheckMsgResults = callRecordBean.data;
					for (CmccCheckMsgResult cmccCheckMsgResult : cmccCheckMsgResults) {
						cmccCheckMsgResult.setTaskId(messageLogin.getTask_id());
						System.out.println(cmccCheckMsgResult.toString());
						list.add(cmccCheckMsgResult);

					}
				}
				if ("500003".equals(callRecordBean.getRetCode())) {
					webCmccParam.setCode(403);
					return webCmccParam;
				}
			}

			webCmccParam.setCmccCheckMsgResults(list);
			webCmccParam.setCode(code);
			return webCmccParam;
		} catch (Exception e) {
			tracer.addTag("CmccCrawlerParser getCheckMsg", e.getMessage());
			e.printStackTrace();
		}
		webCmccParam.setCode(404);
		return webCmccParam;
	}

	/**
	 * @Description: 获取用户缴费记录（一年）
	 * @param messageLogin
	 * @param cookies
	 * @return WebCmccParam
	 */
	public WebCmccParam getPayMsg(MessageLogin messageLogin, Set<Cookie> cookies) {
		tracer.addTag("CmccCrawlerParser getPayMsg", messageLogin.getTask_id());
		WebCmccParam webCmccParam = new WebCmccParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar curr = Calendar.getInstance();
		Date endDate = curr.getTime();
		curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) - 1);
		Date startDate = curr.getTime();

		try {
			String url = "http://shop.10086.cn/i/v1/cust/his/" + messageLogin.getName() + "?startTime="
					+ sdf.format(startDate) + "&endTime=" + sdf.format(endDate) + "&_=" + System.currentTimeMillis();
			WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
			requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
			requestSettings.setAdditionalHeader("Referer",
					"http://shop.10086.cn/i/?f=home&welcome=" + System.currentTimeMillis());
			requestSettings.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			requestSettings.setAdditionalHeader("Cache-Control", "no-store, must-revalidate");
			requestSettings.setAdditionalHeader("Connection", "keep-alive");
			requestSettings.setAdditionalHeader("expires", "0");
			requestSettings.setAdditionalHeader("If-Modified-Since", "0");
			requestSettings.setAdditionalHeader("pragma", "no-cache");
			requestSettings.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

			Page page = webClient.getPage(requestSettings);
			int code = page.getWebResponse().getStatusCode();
			if (200 == code) {

				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("getPayMsg 用户缴费记录（一年）", html);
				PayMsgBean payMsgBean = gson.fromJson(html, PayMsgBean.class);
				if ("500003".equals(payMsgBean.getRetCode())) {
					webCmccParam.setCode(403);
					return webCmccParam;
				}

				if ("000000".equals(payMsgBean.getRetCode())) {
					List<CmccPayMsgResult> results = payMsgBean.getData();
					if (null != results && results.size() > 0) {
						for (CmccPayMsgResult cmccPayMsgResult : results) {
							cmccPayMsgResult.setTaskId(messageLogin.getTask_id());
						}

						webCmccParam.setCmccPayMsgResults(results);
						webCmccParam.setPayMsgStatus(code);
						return webCmccParam;
					} else {
						webCmccParam.setPayMsgStatus(201);
						return webCmccParam;
					}
				} else {
					webCmccParam.setPayMsgStatus(201);
					return webCmccParam;
				}
			} else {
				webCmccParam.setPayMsgStatus(code);
				return webCmccParam;
			}

		} catch (Exception e) {
			tracer.addTag("CmccCrawlerParser getPayMsg", e.getMessage());
			webCmccParam.setPayMsgStatus(404);
			return webCmccParam;
		}
	}

	/**
	 * 用于请求补救措施
	 * 
	 * @param task_id
	 * @param type
	 *            类型
	 * @param url
	 */
	public void addRemedy(String task_id, String type, String url, String year) {
		CmccRemedyParameter parameter = new CmccRemedyParameter();
		parameter.setType(type);
		parameter.setUrl(url);
		parameter.setTaskId(task_id);
		parameter.setYear(year);
		cmccRemedyParameterRepository.save(parameter);
		tracer.addTag("补救的url已入库", url);

	}

}
