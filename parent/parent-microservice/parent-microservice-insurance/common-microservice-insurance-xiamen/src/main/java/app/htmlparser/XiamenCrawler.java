package app.htmlparser;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceXiamenCrawlerResult;
import app.enums.InsuranceXiamenCrawlerType;
import app.service.ChaoJiYingOcrService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenDetailsInfo;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenHtml;
import com.microservice.dao.repository.crawler.insurance.xiamen.InsuranceXiamenHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.xiamen.XiamenFiveInsuranceDetailsRepository;
import com.module.htmlunit.WebCrawler;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 厦门社保爬取
 */
@Component
public class XiamenCrawler {

	public static final Logger log = LoggerFactory.getLogger(XiamenCrawler.class);

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;

	/** 厦门社保登录URL */
	public static final String LOGIN_URL = "https://app.xmhrss.gov.cn/login.xhtml";
	/** 厦门社保首页*/
	public static final String HOME_URL= "https://app.xmhrss.gov.cn/UCenter/index.xhtml";
	public static final String Login_Action = "https://app.xmhrss.gov.cn/UCenter/index.xhtml";
	public static final String POST_URL="https://app.xmhrss.gov.cn/login_dowith.xhtml";

	/**个人基本信息URL*/
	public static final String BASE_INFO ="https://app.xmhrss.gov.cn/UCenter/index_grjbxx.xhtml";

	@Autowired
	private InsuranceXiamenHtmlRepository insuranceXiamenHtmlRepository;
	@Autowired
	private XiamenCrawlerParser xiamenCrawlerParser;

	 @Autowired
	    private TracerLog tracer;

	@Autowired
	private XiamenFiveInsuranceDetailsRepository xiamenFiveInsuranceDetailsRepository;
	/**
	 * 登录 爬取
	 * @param
	 * @throws Exception
	 */
	public WebParam<HtmlPage> crawlerLogin(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		tracer.addTag("XiamenCrawler.crawlerLogin","登陆发送POST请求...");
		WebParam<HtmlPage> webParam = new WebParam<>();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		//loginUrl
		WebRequest webRequestLoginUrl = new WebRequest(new URL(LOGIN_URL), HttpMethod.GET);
		webRequestLoginUrl.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequestLoginUrl.setAdditionalHeader("Connection", "keep-alive");
		webRequestLoginUrl.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequestLoginUrl.setAdditionalHeader("Host", "app.xmhrss.gov.cn");
		webRequestLoginUrl.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");

		HtmlPage searchPage = webClient.getPage(webRequestLoginUrl);
		webClient.waitForBackgroundJavaScript(10000); //该方法在getPage()方法之后调用才能生效
		int status = searchPage.getWebResponse().getStatusCode();
		if(status==200){
			HtmlImage image = searchPage.getFirstByXPath("//*[@id=\"vcodeImg\"]");
			// 超级鹰解析验证码
			String code = chaoJiYingOcrService.getVerifycode(image, "1004");

			// 发送POST请求登录
			WebRequest webRequest = new WebRequest(new URL(POST_URL),HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept","application/json, text/javascript, */*; q=0.01");
			webRequest.setAdditionalHeader("Connection","keep-alive");
			webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			webRequest.setAdditionalHeader("Host","app.xmhrss.gov.cn");
			webRequest.setAdditionalHeader("Origin","https://app.xmhrss.gov.cn");
			webRequest.setAdditionalHeader("Referer","https://app.xmhrss.gov.cn/login.xhtml?returnUrl=https://app.xmhrss.gov.cn:443/UCenter/logout.xhtml");
			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With","XMLHttpRequest");
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new NameValuePair("id0000",insuranceRequestParameters.getUsername()));
			nameValuePairs.add(new NameValuePair("userpwd",insuranceRequestParameters.getPassword()));
			nameValuePairs.add(new NameValuePair("validateCode",code));
			webRequest.setRequestParameters(nameValuePairs);
			HtmlPage loginPage = webClient.getPage(webRequest);
			String response = loginPage.getWebResponse().getContentAsString();
			if(StringUtils.isNotBlank(response)){
				JSONObject jsonObject = JSON.parseObject(response);
				if(jsonObject.getBoolean("result")){
					WebRequest webRequest2 = new WebRequest(new URL(Login_Action),HttpMethod.GET);
					//登陆后页面
					HtmlPage loginActionPage = webClient.getPage(webRequest2);
					String html = loginActionPage.getWebResponse().getContentAsString();
					if(html.contains("当前用户")){
						//success
						log.info("登录成功！");
						webParam.setData(loginActionPage);
						webParam.setCode("0000");
						return webParam;
					}else {
						webParam.setData(loginActionPage);
						webParam.setCode("9999");
						return webParam;
					}
				}else {
					String error = jsonObject.getString("msg");
					if("验证码错误！".equals(error)){
						webParam.setData(null);
						webParam.setCode("1002");
						return webParam;
					}else if("社会保障号或密码错误".equals(error)){
						webParam.setData(null);
						webParam.setCode("1005");
						return webParam;
					}else {
						webParam.setData(null);
						webParam.setCode("9999");
						return webParam;
					}
				}
			}
		}
		webParam.setData(null);
		webParam.setCode("9999");
		return webParam;
	}

	/**
	 * 进入主页
	 * @throws IOException
	 * @throws FailingHttpStatusCodeException
	 *
	 */
	public HtmlPage inHomePage(InsuranceRequestParameters parameter,Set<Cookie> cookies){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for(Cookie cookie : cookies){
			webClient.getCookieManager().addCookie(cookie);
		}
		HtmlPage inPage = null;
		try {
			WebRequest webRequest = new WebRequest(new URL(HOME_URL), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "app.xmhrss.gov.cn");
			webRequest.setAdditionalHeader("Referer", "https://app.xmhrss.gov.cn/login.xhtml?returnUrl=https://app.xmhrss.gov.cn:443/UCenter/logout.xhtml");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			inPage = webClient.getPage(webRequest);
			webClient.waitForBackgroundJavaScriptStartingBefore(10000);
			webClient.waitForBackgroundJavaScript(10000);

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
	 * 爬取厦门社保个人信息
	 * @param
	 * @return
	 */
	public WebParam<HtmlPage> crawlerBaseInfo(HtmlPage inHomePage) throws IOException {
		WebParam<HtmlPage> webParam = new WebParam<>();
		HtmlPage baseInfoPage = null;
		HtmlElement baseInfoElement = (HtmlElement) inHomePage.getFirstByXPath("//*[@id=\"div_gis_1\"]/div[1]/div[1]/h3/a");
		baseInfoPage = baseInfoElement.click();
		webParam.setData(baseInfoPage);
		webParam.setCode(InsuranceXiamenCrawlerResult.SUCCESS.getCode());
		return webParam;
	}

	/**
	 * 基本信息之 参保情况
	 * @param
	 * @param inHomePage
	 * @return
	 */
	public WebParam<HtmlPage> crawlerBaseCanbaoInfo(HtmlPage inHomePage) throws IOException {
		WebParam<HtmlPage> webParam = new WebParam<>();
		HtmlPage baseInfoPage = null;
		HtmlElement baseInfoElement = (HtmlElement) inHomePage.getFirstByXPath("//*[@id=\"div_gis_1\"]/div[2]/div[1]/h3/a");
		baseInfoPage = baseInfoElement.click();
		throw new IOException();
//		webParam.setData(baseInfoPage);
//		webParam.setCode(InsuranceXiamenCrawlerResult.SUCCESS.getCode());
//		return webParam;
	}

	/**
	 * 爬取厦门五险查询页面
	 * @param inHomePage
	 * @return
	 */
	public HtmlPage inFiveInsuranceSearchPage(HtmlPage inHomePage){
		HtmlPage baseInfoPage = null;
		try{
			HtmlElement baseInfoElement = (HtmlElement) inHomePage.getFirstByXPath("//*[@id=\"div_gis_4\"]/div[1]/div[1]/h3/a");
			baseInfoPage = baseInfoElement.click();
		}catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		return baseInfoPage;
	}

	/**
	 * 点击查询五险按钮
	 * @param inHomePage
	 * @return
	 */
	public HtmlPage findAllFiveInsurance(HtmlPage inHomePage){
		HtmlPage baseInfoPage = null;
		try{
			HtmlElement baseInfoElement = (HtmlElement) inHomePage.getFirstByXPath("//input[@class='btn']");
			baseInfoPage = baseInfoElement.click();
		}catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		return baseInfoPage;
	}

	/**
	 * 获取险种详情页
	 * @param inInsurancePage 分页
	 * @param pageNum 当前页数
	 * @param index 当前条数
	 * @param taskInsurance 任务实例
	 * @param retry 失败重试次数
	 */
	@Async
	public void detailedInsurance(HtmlPage inInsurancePage,int pageNum,int index,TaskInsurance taskInsurance,int retry){
		WebParam<HtmlPage> webParam = new WebParam<>();
		HtmlPage baseInfoPage = null;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for(Cookie cookie :cookies){
			webClient.getCookieManager().addCookie(cookie);
		}
		try{
			Document doc = Jsoup.parse(inInsurancePage.getWebResponse().getContentAsString());
			String href = doc.select("table.tab5 > tbody > tr:nth-child("+index+") > td:nth-child(1) > a").attr("href");
			String url = "https://app.xmhrss.gov.cn/UCenter/"+href;
			WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding:gzip","deflate, br");
			webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection","keep-alive");
			webRequest.setAdditionalHeader("Host","app.xmhrss.gov.cn");
			webRequest.setAdditionalHeader("Referer","https://app.xmhrss.gov.cn/UCenter/sbjfxxcx.xhtml?xzdm00=&zmlx00=&qsnyue=&jznyue=");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
			webRequest.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			baseInfoPage = webClient.getPage(webRequest);
			int status = baseInfoPage.getWebResponse().getStatusCode();
			if(status==200){
				System.out.println("获取五险详情第："+pageNum+"页，第："+index+"条成功: webclient:"+webClient.hashCode()+" baseInfoPage:"+baseInfoPage.hashCode());
				webParam.setData(baseInfoPage);
				webParam.setCode(InsuranceXiamenCrawlerResult.SUCCESS.getCode());
				// 保存html
				InsuranceXiamenHtml insuranceShenzhenHtml = new InsuranceXiamenHtml();
				insuranceShenzhenHtml.setHtml(baseInfoPage.asText());
				insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
				insuranceShenzhenHtml.setType(InsuranceXiamenCrawlerType.BASE_INFO.getCode());
				insuranceShenzhenHtml.setUrl(baseInfoPage.getUrl().toString());
				insuranceXiamenHtmlRepository.save(insuranceShenzhenHtml);
				//开始解析
				WebParam<InsuranceXiamenDetailsInfo> detailsInfo = xiamenCrawlerParser.parserFiveInsuranceDetails(baseInfoPage);
				tracer.addTag("ParserCrawledFiveInsuranceService.parserFiveInsuranceSummary:解析完成", detailsInfo.getData().toString());
				// 五险详细信息入库
				if(InsuranceXiamenCrawlerResult.SUCCESS.getCode().equals(detailsInfo.getCode()) && detailsInfo.getData() != null){
					InsuranceXiamenDetailsInfo insuranceDetailsInfos = detailsInfo.getData();
					insuranceDetailsInfos.setTaskId(taskInsurance.getTaskid());
					xiamenFiveInsuranceDetailsRepository.save(insuranceDetailsInfos);
				}
			}else {
				System.out.println("获取五险详情第："+pageNum+"页，第："+index+"条失败: webclient:"+webClient.hashCode());
			}
		}catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch(Exception e){
			tracer.addTag("XiamenCrawler.detailedInsurance 获取险种详情失败!重试第"+retry+"次。",""+e);
			if(retry < 4){
				detailedInsurance(inInsurancePage,pageNum,index,taskInsurance,++retry);
			}else{
				System.out.println("失败4次！！！！！！");
				e.printStackTrace();
			}
		}
	}
}
