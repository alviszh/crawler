package app.htmlparser;

import app.domain.WebParam;
import app.enums.InsuranceZhengzhouCrawlerResult;
import app.service.ChaoJiYingOcrService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 郑州社保爬取
 */
@Component
public class ZhengzhouCrawler {
	
	public static final Logger log = LoggerFactory.getLogger(ZhengzhouCrawler.class);

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	/** 郑州社保登录URL */
	public static final String LOGIN_URL = "http://218.28.166.74:8080/zzsbonline/login.jsp";
	/** 郑州社保POST请求的URL */
	public static final String POST_URL = "http://218.28.166.74:8080/zzsbonline/usersAction!userLogin";
	/** 郑州社保首页*/
	public static final String Login_Action = "http://218.28.166.74:8080/zzsbonline/loginAction";
	public static final String BASE_INFO ="http://218.28.166.74:8080/zzsbonline/personAction!GetPersonList";

	/**
	 * 登录 爬取
	 * @param
	 * @throws Exception
	 */
	public WebParam<HtmlPage> crawlerLogin(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebParam<HtmlPage> webParam = new WebParam<>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//loginUrl
		WebRequest webRequestLoginUrl = new WebRequest(new URL(LOGIN_URL), HttpMethod.GET);
		webRequestLoginUrl.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequestLoginUrl.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequestLoginUrl.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequestLoginUrl.setAdditionalHeader("Host", "218.28.166.74:8080");
		webRequestLoginUrl.setAdditionalHeader("Referer", "http://218.28.166.74:8080/zzsbonline/login.jsp");

		HtmlPage searchPage = webClient.getPage(webRequestLoginUrl);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效
		int status = searchPage.getWebResponse().getStatusCode();
		if(status==200){
			HtmlImage image = searchPage.getFirstByXPath("//*[@id=\"codeImg\"]");
			// 超级鹰解析验证码
			String code = chaoJiYingOcrService.getVerifycode(image, "1004");
            // 发送post 请求登录
			HtmlPage loginPage = sendPostRequest(webClient,POST_URL,insuranceRequestParameters,code);
			String responseJson = loginPage.getWebResponse().getContentAsString();
			if(StringUtils.isNotBlank(responseJson)){
                JSONObject jsonObject = JSON.parseObject(responseJson);
                if(jsonObject.getString("msgbox").contains("用户成功登录")){
                    // 访问首页
                    WebRequest webRequest2 = new WebRequest(new URL(Login_Action),HttpMethod.GET);
                    //登陆后页面
                    HtmlPage loginActionPage = webClient.getPage(webRequest2);
                    String html = loginActionPage.getWebResponse().getContentAsString();
					if(html.contains("欢迎您！")){
						String token = loginActionPage.getHtmlElementById("token").getAttribute("value");
						webParam.setToken(token);
						webParam.setData(loginActionPage);
						webParam.setCode("0000");
						return webParam;
					}else {
						webParam.setData(loginActionPage);
						webParam.setCode("9999");
						return webParam;
					}
                }else if(jsonObject.getString("msgbox").contains("用户名或密码不正确")){
					webParam.setData(null);
					webParam.setCode("1005");
                    return webParam;
                }else if (jsonObject.getString("msgbox").contains("无效的验证码")){
					webParam.setData(null);
					webParam.setCode("1002");
					return webParam;
				}else {
					webParam.setData(null);
					webParam.setCode("9999");
					return webParam;
				}
			}else {
				webParam.setData(null);
				webParam.setCode("9999");
				return webParam;
			}
		}
		webParam.setData(null);
		webParam.setCode("9999");
		return webParam;
	}

	/**
	 *
	 * @param webClient
	 * @param url
	 * @param insuranceRequestParameters 获取用户名及密码
	 * @param code 验证码
	 * @return
	 */
	public HtmlPage sendPostRequest(WebClient webClient,String url,InsuranceRequestParameters insuranceRequestParameters,String code){
		HtmlPage inPage = null;
		try {
			WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.POST);
//			webRequest.setCharset("UTF-8");
			Map<String,String> headers = new HashMap<>();
			headers.put("Accept","application/json, text/javascript, */*; q=0.01");
			headers.put("Accept-Encoding","gzip, deflate");
			headers.put("Accept-Language","zh-CN,zh;q=0.8");
			headers.put("Connection","keep-alive");
			headers.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			headers.put("Host","218.28.166.74:8080");
			headers.put("Origin","http://218.28.166.74:8080");
			headers.put("Referer","http://218.28.166.74:8080/zzsbonline/login.jsp");
			headers.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			headers.put("X-Requested-With","XMLHttpRequest");
			webRequest.setAdditionalHeaders(headers);

			List<NameValuePair> params = new ArrayList<>();
			params.add(new NameValuePair("cardid",insuranceRequestParameters.getUsername()));
			params.add(new NameValuePair("password",MD5Util.getMD5(insuranceRequestParameters.getPassword()).toLowerCase()));
			params.add(new NameValuePair("vcode",code));
			webRequest.setRequestParameters(params);
			inPage = webClient.getPage(webRequest);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inPage;
	}


	/**
	 * 爬取郑州社保个人信息
	 * @param cookies
	 * @return
	 */
	public WebParam<String> crawlerBaseInfo(Set<Cookie> cookies,String token){
		WebParam<String> webParam = new WebParam<>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for(Cookie cookie : cookies){
			webClient.getCookieManager().addCookie(cookie);
		}
		HtmlPage inPage = null;
		String responseJson = null;
		try {
			WebRequest webRequest = new WebRequest(new URL(BASE_INFO),HttpMethod.POST);
			//设置请求头
			webRequest.setAdditionalHeader("Accept","application/json, text/javascript, */*; q=0.01");
			webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection","keep-alive");
			webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			webRequest.setAdditionalHeader("Host","218.28.166.74:8080");
			webRequest.setAdditionalHeader("Origin","http://218.28.166.74:8080");
			webRequest.setAdditionalHeader("Referer","http://218.28.166.74:8080/zzsbonline/searchinfoAction");
			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/");
			webRequest.setAdditionalHeader("X-Requested-With","XMLHttpRequest");
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new NameValuePair("idno",""));
			nameValuePairs.add(new NameValuePair("token",token));
			webRequest.setRequestParameters(nameValuePairs);
			inPage = webClient.getPage(webRequest);
			//查询结果 json格式
			responseJson = inPage.getWebResponse().getContentAsString();
			if(StringUtils.isNotBlank(responseJson)&&(responseJson.contains("查询失败，系统出现异常"))){
				//查询异常情况
				webParam.setData(null);
				webParam.setCode(InsuranceZhengzhouCrawlerResult.EXCEPTION.getCode());
				return webParam;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			webParam.setData(null);
			webParam.setCode(InsuranceZhengzhouCrawlerResult.EXCEPTION.getCode());
			return webParam;
		} catch (IOException e) {
			e.printStackTrace();
			webParam.setData(null);
			webParam.setCode(InsuranceZhengzhouCrawlerResult.EXCEPTION.getCode());
			return webParam;
		}
		//正常情况
		webParam.setData(responseJson);
		webParam.setCode(InsuranceZhengzhouCrawlerResult.SUCCESS.getCode());
		return webParam;
	}
}
