package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.huangshan.HousinghuangshanBase;
import com.microservice.dao.entity.crawler.housing.huangshan.HousinghuangshanHtml;
import com.microservice.dao.entity.crawler.housing.huangshan.HousinghuangshanPay;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.huangshan.HousinghuangshanBaseRepository;
import com.microservice.dao.repository.crawler.housing.huangshan.HousinghuangshanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.huangshan.HousinghuangshanPayRepository;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.huangshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.huangshan")
public class HousingFundhuangshanService extends AbstractChaoJiYingHandler implements ICrawlerLogin{
	/** 烟台公积金登录的URL */
	public static final Logger log = LoggerFactory.getLogger(HousingFundhuangshanService.class);
	@Autowired
	public ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	public HousinghuangshanHtmlRepository housinghuangshanHtmlRepository;
	@Autowired
	public HousinghuangshanBaseRepository housinghuangshanBaseRepository;
	@Autowired
	public HousinghuangshanPayRepository housinghuangshanPayRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingBasicService housingBasicService;
	// 登录业务层
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			String loginUrl = "http://www.hsgjjw.com/";
			WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage hPage = webClient.getPage(webRequest);
			Thread.sleep(2000); // 网站本身原因，登录页面的加载需要费些时间
			if (null != hPage) {
				// 此处请求图片验证码链接
				String vericodeUrl = "http://www.hsgjjw.com/vericode.jsp";
				webRequest = new WebRequest(new URL(vericodeUrl), HttpMethod.GET);
				Page page = webClient.getPage(webRequest);
				String imagePath = getImagePath(page);
				String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1006");
				HtmlTextInput loginName = (HtmlTextInput) hPage.getFirstByXPath("//input[@id='certinum']");
				HtmlPasswordInput loginPassword = (HtmlPasswordInput) hPage.getFirstByXPath("//*[@id=\"tabs-1\"]/form/div[2]/div/input");
				HtmlTextInput validateCode = (HtmlTextInput) hPage.getFirstByXPath("//*[@id=\"tabs-1\"]/form/div[3]/div/input");
				HtmlElement submitbt = (HtmlElement) hPage.getFirstByXPath("//*[@id=\"tabs-1\"]/form/div[4]/div/button");
				loginName.setText("341004198812070030");
				loginPassword.setText("123456");
				validateCode.setText(code);
				HtmlPage logonPage = submitbt.click();

				String contentAsString2 = logonPage.getWebResponse().getContentAsString();

				System.out.println(contentAsString2);

				if (contentAsString2.contains("退出")) {
					System.out.println("登陆成功！");
					String cookies = CommonUnit.transcookieToJson(webClient);
					taskHousing.setCookies(cookies);
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
					taskHousingRepository.save(taskHousing);
				} else {
					System.out.println("登陆失败！异常错误！");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR2.getError_code());
					taskHousingRepository.save(taskHousing);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return taskHousing;
	}

	// 爬取数据的业务层
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			// 登录请求的入参
			String jbxx1 = "http://www.hsgjjw.com/init.summer?_PROCID=70000013";
			WebRequest requestSettings = new WebRequest(new URL(jbxx1), HttpMethod.GET);
			Page page = webClient.getPage(requestSettings);

			String contentAsString = page.getWebResponse().getContentAsString();

			String[] split = contentAsString.split("var poolSelect = \\{");
			String[] split2 = split[1].split("\\}");
			String trim = split2[0].trim();
			String[] split3 = trim.split(",");
			String base = "";
			for (int i = 0; i < split3.length; i++) {
				String trim2 = split3[i].trim();
				String[] split4 = null;
				if (trim2.contains("\"")) {
					split4 = trim2.split("\"");
				}
				if (trim2.contains("'")) {
					split4 = trim2.split("'");
				}
				base += split4[split4.length - 1] + ",";
			}
			String[] split4 = base.split(",");
			// 基本信息
			String jbxx11 = "http://www.hsgjjw.com/command.summer?uuid=1520577274890";
			WebRequest requestSettings1 = new WebRequest(new URL(jbxx11), HttpMethod.POST);
			requestSettings1.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings1.getRequestParameters().add(new NameValuePair("$page", split4[0]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_RW", split4[1]));
			requestSettings1.getRequestParameters().add(new NameValuePair("temp_.itemid[1]", split4[2]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_BRANCHKIND", split4[3]));
			requestSettings1.getRequestParameters().add(new NameValuePair("temp_.itemval[2]", split4[4]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_ACCNUM", split4[5]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_PAGEID", split4[6]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_DEPUTYIDCARDNUM", split4[7]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_PORCNAME", split4[8]));
			requestSettings1.getRequestParameters().add(new NameValuePair("temp_._xh[2]", split4[9]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_PROCID", split4[10]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_IS", split4[11]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_ISCROP", split4[12]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_SENDDATE", split4[13]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_SENDTIME", split4[14]));
			requestSettings1.getRequestParameters().add(new NameValuePair("temp_.itemid[2]", split4[15]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_TYPE", split4[16]));
			requestSettings1.getRequestParameters().add(new NameValuePair("CURRENT_SYSTEM_DATE", split4[17]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_SENDOPERID", split4[18]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_WITHKEY", split4[19]));
			requestSettings1.getRequestParameters().add(new NameValuePair("isSamePer", split4[20]));
			requestSettings1.getRequestParameters().add(new NameValuePair("temp_.itemval[1]", split4[21]));
			requestSettings1.getRequestParameters().add(new NameValuePair("temp__rownum", split4[22]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_LOGIP", split4[23]));
			requestSettings1.getRequestParameters().add(new NameValuePair("temp_._xh[1]", split4[24]));
			requestSettings1.getRequestParameters().add(new NameValuePair("_UNITACCNAME", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("_ACCNAME", split4[26]));
			requestSettings1.getRequestParameters().add(new NameValuePair("grzh", split4[5]));
			requestSettings1.getRequestParameters().add(new NameValuePair("xingming", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("zjhm", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("grzhzt", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("grzhye", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("khrq", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("unitprop", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("indiprop", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("grjcjs", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("yjce", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("dwyjce", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("gryjce", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("jzny", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("dwzh", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("unitaccname", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("isloanflag", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("sjhm", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("jkhtbh", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("useflag", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("cardno", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("frzflag", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("flag", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("smrzbs", ""));
			requestSettings1.getRequestParameters().add(new NameValuePair("DealSeq", "1"));
			Page page1 = webClient.getPage(requestSettings1);

			String contentAsString1 = page1.getWebResponse().getContentAsString();
			System.out.println(contentAsString1);
			HousinghuangshanHtml housinghuangshanHtml = new HousinghuangshanHtml();
			housinghuangshanHtml.setHtml(contentAsString1);
			housinghuangshanHtml.setTaskid(messageLoginForHousing.getTask_id().trim());
			housinghuangshanHtml.setType("基本信息");
			housinghuangshanHtml.setUrl(jbxx11);
			housinghuangshanHtmlRepository.save(housinghuangshanHtml);

			JSONObject object = JSONObject.fromObject(contentAsString1);
			String data = object.getString("data");
			JSONObject object2 = JSONObject.fromObject(data);
			// 个人账号
			String grzh = object2.getString("_ACCNUM");
			System.out.println("个人账号------" + grzh);
			// 姓名
			String xm = object2.getString("xingming");
			System.out.println("姓名------" + xm);
			// 证件号码
			String zjhm = object2.getString("_DEPUTYIDCARDNUM");
			System.out.println("证件号码------" + zjhm);
//			// 个人账户状态
//			String grzhzt = object2.getString("temp_.itemval[1]");
//			System.out.println("个人账户状态------" + grzhzt);
			// 个人账户余额
			String grzhye = object2.getString("grzhye");
			System.out.println("个人账户余额------" + grzhye);
			// 开户日期
			String khrq = object2.getString("khrq");
			System.out.println("开户日期------" + khrq);
			// 单位缴存比例
			String dwjcbl = object2.getString("unitprop");
			System.out.println("单位缴存比例------" + dwjcbl);
			// 个人缴存比例
			String grjcbl = object2.getString("indiprop");
			System.out.println("个人缴存比例------" + grjcbl);
			// 个人缴存基数
			String grjcjs = object2.getString("grjcjs");
			System.out.println("个人缴存基数------" + grjcjs);
			// 月缴存总额
			String yjcze = object2.getString("dwyjce");
			String yjcze1 = object2.getString("gryjce");
			System.out.println("月缴存总额------" + yjcze+yjcze1);
			// 单位月缴存额
			String dwyjce = object2.getString("dwyjce");
			System.out.println("单位月缴存额------" + dwyjce);
			// 个人月缴存额
			String gryjce = object2.getString("gryjce");
			System.out.println("个人月缴存额------" + gryjce);
			// 缴至年月
			String jzny = object2.getString("jzny");
			System.out.println("缴至年月------" + jzny);
			// 单位账号
			String dwzh = object2.getString("dwzh");
			System.out.println("单位账号------" + dwzh);
			// 单位名称
			String dwmc = object2.getString("unitaccname");
			System.out.println("单位名称------" + dwmc);
			// 是否贷款
			String sfdk = object2.getString("isloanflag");
			if ("1".equals(sfdk)) {
				sfdk = "否";
			}
			if ("0".equals(sfdk)) {
				sfdk = "是";
			}
			System.out.println("是否贷款------" + sfdk);
			// 手机号码
			String sjhm = object2.getString("sjhm");
			System.out.println("手机号码------" + sjhm);
			// 借款合同号
			String jkhth = object2.getString("jkhtbh");
			System.out.println("借款合同号------" + jkhth);
			HousinghuangshanBase housinghuangshanBase = new HousinghuangshanBase();
			housinghuangshanBase.setTaskid(messageLoginForHousing.getTask_id().trim());
			housinghuangshanBase.setJkhth(jkhth);
			housinghuangshanBase.setSjhm(sjhm);
			housinghuangshanBase.setSfdk(sfdk);
			housinghuangshanBase.setDwmc(dwmc);
			housinghuangshanBase.setDwzh(dwzh);
			housinghuangshanBase.setJzny(jzny);
			housinghuangshanBase.setGryjce(gryjce);
			housinghuangshanBase.setDwyjce(dwyjce);
			housinghuangshanBase.setYjcze(yjcze+yjcze1);
			housinghuangshanBase.setGrjcjs(grjcjs);
			housinghuangshanBase.setGrjcbl(grjcbl);
			housinghuangshanBase.setDwjcbl(dwjcbl);
			housinghuangshanBase.setKhrq(khrq);
			housinghuangshanBase.setGrzhye(grzhye);
			housinghuangshanBase.setGrzhzt("正常");
			housinghuangshanBase.setZjhm(zjhm);
			housinghuangshanBase.setXm(xm);
			housinghuangshanBase.setGrzh(grzh);
			housinghuangshanBaseRepository.save(housinghuangshanBase);
			taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
		}

		////////////////////////////////////// 流水信息////////////////////////////////////////
		try

		{
			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
			System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

			SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar222 = Calendar.getInstance();
			calendar222.setTime(new Date());
			calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
			System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));

			// 流水请求入参
			String jbxx1 = "http://www.hsgjjw.com/init.summer?_PROCID=70000002";
			WebRequest requestSettings = new WebRequest(new URL(jbxx1), HttpMethod.GET);
			Page page5 = webClient.getPage(requestSettings);

			String contentAsString = page5.getWebResponse().getContentAsString();

			String[] split0 = contentAsString.split("var poolSelect = \\{");
			String[] split2 = split0[1].split("\\}");
			String trim = split2[0].trim();
			String[] split3 = trim.split(",");
			String base = "";
			for (int i = 0; i < split3.length; i++) {
				String trim2 = split3[i].trim();
				String[] split4 = null;
				if (trim2.contains("\"")) {
					split4 = trim2.split("\"");
				}
				if (trim2.contains("'")) {
					split4 = trim2.split("'");
				}
				base += split4[split4.length - 1] + ",";
			}
			String[] split4 = base.split(",");

			String jbxx112 = "http://www.hsgjjw.com/command.summer?uuid=1521447908376";
			WebRequest requestSettings12 = new WebRequest(new URL(jbxx112), HttpMethod.POST);
			String requestBody=""
					+ "%24page=%2Fydpx%2F70000002%2F700002_01.ydpx"
					+ "&_RW=w"
					+ "&_TYPE=init"
					+ "&_BRANCHKIND=0"
					+ "&_WITHKEY=0"
					+ "&isSamePer=false"
					+ "&_ACCNUM="+split4[8]+""
					+ "&_PAGEID=step1"
					+ "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2"
					+ "&_PROCID="+split4[13]+""
					+ "&_IS=-212620"
					+ "&_ISCROP=0"
					+ "&begdate="+dateFormat222.format(calendar222.getTime())+""
					+ "&enddate="+dateFormat22.format(calendar22.getTime())+""
					+ "&year=2018"
					+ "&accnum="+split4[8]+""
					;
			requestSettings12.setRequestBody(requestBody);
			Page page2 = webClient.getPage(requestSettings12);
			System.out.println("前提："+page2.getWebResponse().getContentAsString());
			// 流水请求
			String jbxx11 = "http://www.hsgjjw.com/dynamictable?uuid=1521447909736";
			WebRequest requestSettings1 = new WebRequest(new URL(jbxx11), HttpMethod.POST);
			requestBody=""
					+ "dynamicTable_id=datalist2"
					+ "&dynamicTable_currentPage=0"
					+ "&dynamicTable_pageSize=500"
					+ "&dynamicTable_nextPage=1"
					+ "&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx"
					+ "&dynamicTable_paging=true"
					+ "&dynamicTable_configSqlCheck=0"
					+ "&errorFilter=1%3D1"
					+ "&begdate="+dateFormat222.format(calendar222.getTime())+""
					+ "&enddate="+dateFormat22.format(calendar22.getTime())+""
					+ "&year=2018"
					+ "&accnum="+split4[8]+""
					+ "&_APPLY=0"
					+ "&_CHANNEL=1"
					+ "&_PROCID="+split4[13]+""
					+ "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AM1zZWxlY3QgaW5zdGFuY2VudW0sIHRvX2NoYXIodHJhbnNk%0AYXRlLCd5eXl5LW1tLWRkJykgdHJhbnNkYXRlLCBncnpoLCB4aW5nbWluZywgeGluZ21pbmcyLCBh%0AbXQxLCBhbXQyLCBiZWd5bSwgZW5keW0sIHJlYXNvbiwgcGF5dm91bnVtLCBmcmVldXNlMSBmcm9t%0AIGRwMDc3IHdoZXJlIGluc3RhbmNlbnVtID0tMjEyNjIwIG9yZGVyIGJ5IHRyYW5zZGF0ZSBkZXNj%0AeA%3D%3D&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABV0AANf%0AUld0AAF3dAAFX1RZUEV0AARpbml0dAALX0JSQU5DSEtJTkR0AAEwdAATQ1VSUkVOVF9TWVNURU1f%0AREFURXQACjIwMTgtMDMtMTl0AAtfU0VORE9QRVJJRHQAEjM0MTAwNDE5ODgxMjA3MDAzMHQACF9X%0ASVRIS0VZcQB%2BAAl0AAlpc1NhbWVQZXJ0AAVmYWxzZXQAB19BQ0NOVU10AAwzNDEwMDkxNTI0NjF0%0AAAdfUEFHRUlEdAAFc3RlcDF0ABBfREVQVVRZSURDQVJETlVNcQB%2BAA10AAlfUE9SQ05BTUV0ABjk%0AuKrkurrmmI7nu4bkv6Hmga%2Fmn6Xor6J0AAtfVU5JVEFDQ05VTXB0AAZfTE9HSVB0ABEyMDE4MDMx%0AOTE2MjM0MDM5N3QAB19QUk9DSUR0AAg3MDAwMDAwMnQADF9VTklUQUNDTkFNRXQAAHQAA19JU3Ny%0AAA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoas%0AlR0LlOCLAgAAeHD%2F%2F%2F%2F%2F%2F%2FzBdHQAB19JU0NST1BxAH4ACXQAB19VU0JLRVlwdAAJX1NFTkREQVRF%0AcQB%2BAAt0AAhfQUNDTkFNRXQACeWui%2BaZk%2BW%2BvXQACV9TRU5EVElNRXQACjIwMTgtMDMtMTl4dAAI%0AQFN5c0RhdGV0AAdAU3lzRGF5dAAJQFN5c01vbnRodAAIQFN5c1RpbWV0AAhAU3lzV2Vla3QACEBT%0AeXNZZWFy";
			requestSettings1.setRequestBody(requestBody);
			Page page1 = webClient.getPage(requestSettings1);
			String contentAsString2 = page1.getWebResponse().getContentAsString();
			System.out.println("流水信息"+contentAsString2);

			HousinghuangshanHtml housinghuangshanHtml1 = new HousinghuangshanHtml();
			housinghuangshanHtml1.setHtml(contentAsString2);
			housinghuangshanHtml1.setTaskid(messageLoginForHousing.getTask_id().trim());
			housinghuangshanHtml1.setType("流水信息");
			housinghuangshanHtml1.setUrl(jbxx11);
			housinghuangshanHtmlRepository.save(housinghuangshanHtml1);

			JSONObject object = JSONObject.fromObject(contentAsString2);
			String data = object.getString("data").trim();
			JSONObject object2 = JSONObject.fromObject(data);
			String data2 = object2.getString("data").trim();
			JSONArray array = JSONArray.fromObject(data2);
			for (int i = 0; i < array.size(); i++) {
				String string = array.get(i).toString();
				JSONObject object3 = JSONObject.fromObject(string);
				// 交易时间
				String jysj = object3.getString("transdate").trim();
				System.out.println("交易时间-----" + jysj);
				// 个人账户
				String grzh = object3.getString("grzh").trim();
				System.out.println("个人账户-----" + grzh);
				// 姓名
				String xm = object3.getString("xingming").trim();
				System.out.println("姓名-----" + xm);
				// 业务类型
				String ywlx = object3.getString("xingming2").trim();
				System.out.println("业务类型-----" + ywlx);
				// 发生额
				String fse = object3.getString("amt1").trim();
				System.out.println("发生额-----" + fse);
				// 余额
				String ye = object3.getString("amt2").trim();
				System.out.println("余额-----" + ye);
				// 开始年月
				String ksny = object3.getString("begym").trim();
				System.out.println("开始年月-----" + ksny);
				// 终止年月
				String zzny = object3.getString("endym").trim();
				System.out.println("终止年月-----" + zzny);
				// 备注
				String bz = object3.getString("reason").trim();
				System.out.println("备注-----" + bz);
				// 入账状态
				String rzzt = object3.getString("payvounum").trim();
				System.out.println("入账状态-----" + rzzt);
				HousinghuangshanPay housinghuangshanPay = new HousinghuangshanPay();
				housinghuangshanPay.setTaskid(messageLoginForHousing.getTask_id().trim());
				housinghuangshanPay.setRzzt(rzzt);
				housinghuangshanPay.setBz(bz);
				housinghuangshanPay.setZzny(zzny);
				housinghuangshanPay.setKsny(ksny);
				housinghuangshanPay.setYe(ye);
				housinghuangshanPay.setFse(fse);
				housinghuangshanPay.setYwlx(ywlx);
				housinghuangshanPay.setXm(xm);
				housinghuangshanPay.setGrzh(grzh);
				housinghuangshanPay.setJyrq(jysj);
				housinghuangshanPayRepository.save(housinghuangshanPay);
			}

			taskHousingRepository.updatePayStatusByTaskid("数据采集中，流水信息已采集完成", 200, taskHousing.getTaskid());

			taskHousing = housingBasicService.updateTaskHousing(taskHousing.getTaskid());

		} catch (Exception e) {
			e.printStackTrace();
		}
return taskHousing;
	}

	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public static String getImagePath(Page page) throws Exception {
		File imageFile = getImageCustomPath();
		String imgagePath = imageFile.getAbsolutePath();
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}

	// 创建验证码图片保存路径
	public static File getImageCustomPath() {
		String path = "";
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			path = System.getProperty("user.dir") + "/verifyCodeImage/";
		} else {
			path = System.getProperty("user.home") + "/verifyCodeImage/";
		}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}
	/**
	 * 将unicode 字符串
	 * 
	 * @param str
	 *            待转字符串
	 * @return 普通字符串
	 */
	public static String revert(String str) {
		str = (str == null ? "" : str);
		if (str.indexOf("\\u") == -1)// 如果不是unicode码则原样返回
			return str;

		StringBuffer sb = new StringBuffer(1000);

		for (int i = 0; i < str.length() - 6;) {
			String strTemp = str.substring(i, i + 6);
			String value = strTemp.substring(2);
			int c = 0;
			for (int j = 0; j < value.length(); j++) {
				char tempChar = value.charAt(j);
				int t = 0;
				switch (tempChar) {
				case 'a':
					t = 10;
					break;
				case 'b':
					t = 11;
					break;
				case 'c':
					t = 12;
					break;
				case 'd':
					t = 13;
					break;
				case 'e':
					t = 14;
					break;
				case 'f':
					t = 15;
					break;
				default:
					t = tempChar - 48;
					break;
				}

				c += t * ((int) Math.pow(16, (value.length() - j - 1)));
			}
			sb.append((char) c);
			i = i + 6;
		}
		return sb.toString();

	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}