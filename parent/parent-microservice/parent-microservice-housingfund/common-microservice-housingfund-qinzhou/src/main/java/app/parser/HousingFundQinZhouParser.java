package app.parser;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.qinzhou.HousingFundQinZhouAccount;
import com.microservice.dao.entity.crawler.housing.qinzhou.HousingFundQinZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class HousingFundQinZhouParser {

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	protected TaskHousingRepository taskHousingRepository;
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="http://wangting.qzsgjj.com/wt-web/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		Thread.sleep(5000);
		
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element e1 = doc.getElementById("modulus");
		String val1 = e1.val();

		Element e2 = doc.getElementById("exponent");
		String val2 = e2.val();
		System.out.println(val1 + "--" + val2);
		
		String encryptedPhone = encryptedPhone(messageLoginForHousing.getPassword(),val1);
		System.out.println(encryptedPhone);
		//30d215aa498c49866af498cb803fc27e 600c2b2d66ee487049d0077f1ab1c302
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");
		elementById.reset();
		elementById.setText(messageLoginForHousing.getNum());
		
		HtmlPasswordInput elementById1 = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='in_password']");
		elementById1.reset();
		elementById1.setText(messageLoginForHousing.getPassword());
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='captcha_img']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1004");

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

		String requestBody="grloginDxyz=0&username="+messageLoginForHousing.getNum()+"&password="+encryptedPhone+"&force=force&captcha="+verifycode;
		webRequest.setRequestBody(requestBody);
		Page page2 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
		
		WebParam webParam = new WebParam();
		if(page2.getWebResponse().getContentAsString().contains("您在中心登记有"))
		{
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
		}
		return webParam;
	}
	
	// 加密
	public static  String encryptedPhone(String phonenum,String mo) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource("zhumadian2.js", Charsets.UTF_8);
		// System.out.println(path);
		// FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum,mo);
		return data.toString();
	}

	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	
	//个人信息
	public WebParam<HousingFundQinZhouUserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		String userUrl="http://wangting.qzsgjj.com/wt-web/jcr/jcrkhxxcx_mh.service";
		WebRequest webRequest2 = new WebRequest(new URL(userUrl), HttpMethod.POST);
		String reString="ffbm=01&ywfl=01&ywlb=99&cxlx=01";
		webRequest2.setRequestBody(reString);
		Page page3 = webClient.getPage(webRequest2);
		System.out.println(page3.getWebResponse().getContentAsString());
		WebParam<HousingFundQinZhouUserInfo> webParam = new WebParam<HousingFundQinZhouUserInfo>();
		if(page3.getWebResponse().getContentAsString().contains("results"))
		{
			JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
			String string = fromObject.getString("results");
			JSONArray fromObject2 = JSONArray.fromObject(string);
			HousingFundQinZhouUserInfo h = new HousingFundQinZhouUserInfo();
			List<HousingFundQinZhouUserInfo> list = new ArrayList<HousingFundQinZhouUserInfo>();
			String num = null;
			for (int i = 0; i < fromObject2.size(); i++) {
				h = new HousingFundQinZhouUserInfo();
				h.setName(JSONObject.fromObject(fromObject2.get(i)).getString("xingming"));
				h.setBirthday(JSONObject.fromObject(fromObject2.get(i)).getString("csny"));
				h.setSex(JSONObject.fromObject(fromObject2.get(i)).getString("xingbie"));
				h.setCardType(JSONObject.fromObject(fromObject2.get(i)).getString("zjlx"));
				h.setIdcardNum(JSONObject.fromObject(fromObject2.get(i)).getString("zjhm"));
				h.setPhone(JSONObject.fromObject(fromObject2.get(i)).getString("sjhm"));
				h.setNum(JSONObject.fromObject(fromObject2.get(i)).getString("gddhhm"));
				h.setCode(JSONObject.fromObject(fromObject2.get(i)).getString("yzbm"));
				h.setGetMoney(JSONObject.fromObject(fromObject2.get(i)).getString("jtysr"));
				h.setAddr(JSONObject.fromObject(fromObject2.get(i)).getString("jtzz"));
				h.setMarry(JSONObject.fromObject(fromObject2.get(i)).getString("hyzk"));
				h.setLoan(JSONObject.fromObject(fromObject2.get(i)).getString("dkqk"));
				h.setCardNum(JSONObject.fromObject(fromObject2.get(i)).getString("grzh"));
				h.setCardStatus(JSONObject.fromObject(fromObject2.get(i)).getString("grzhzt"));
				h.setFee(JSONObject.fromObject(fromObject2.get(i)).getString("grzhye"));
				h.setOpenDate(JSONObject.fromObject(fromObject2.get(i)).getString("djrq"));
				h.setCompany(JSONObject.fromObject(fromObject2.get(i)).getString("dwmc"));
				h.setPayRatio(JSONObject.fromObject(fromObject2.get(i)).getString("jcbl"));
				h.setPayBase(JSONObject.fromObject(fromObject2.get(i)).getString("grjcjs"));
				h.setMonthPay(JSONObject.fromObject(fromObject2.get(i)).getString("yjce"));
				h.setBank(JSONObject.fromObject(fromObject2.get(i)).getString("grckzhkhyhmc"));
				h.setSetNum(JSONObject.fromObject(fromObject2.get(i)).getString("grckzhhm"));
				h.setTaskid(taskHousing.getTaskid());
				list.add(h);
				if(taskHousing.getCrawlerHost() != null){
					taskHousing.getCrawlerHost().replaceAll(taskHousing.getCrawlerHost(), "");
				}
				num = taskHousing.getCrawlerHost()+","+h.getCardNum();
				System.out.println(list);
				taskHousing.setCrawlerHost(num);
				taskHousingRepository.save(taskHousing);
			}
			webParam.setList(list);
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(userUrl);
		}
		return webParam;
	}

	public WebParam<HousingFundQinZhouAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		
		WebParam<HousingFundQinZhouAccount> webParam = new WebParam<HousingFundQinZhouAccount>();
		HousingFundQinZhouAccount  h = null;
		List<HousingFundQinZhouAccount> list = new ArrayList<HousingFundQinZhouAccount>();
		
		//从数据库获取账户账号
		String crawlerHost = taskHousing.getCrawlerHost();
		String[] split = crawlerHost.split(",");
		for (int j = 0; j < split.length; j++) {
			String userUrl="http://wangting.qzsgjj.com/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq="+getDateBefore("yyyy-MM-dd", 10)+"&jsrq="+getTime("yyyy-MM-dd")+"&grxx="+split[j]+"&pageNum=1&page=1&startPage=0&pageSize=1000&size=10&_="+System.currentTimeMillis();
			WebRequest webRequest = new WebRequest(new URL(userUrl), HttpMethod.GET);
			Page page3 = webClient.getPage(webRequest);
			System.out.println(page3.getWebResponse().getContentAsString());
			if(page3.getWebResponse().getContentAsString().contains("results"))
			{
				JSONObject fromObject = JSONObject.fromObject(page3.getWebResponse().getContentAsString());
				String string = fromObject.getString("results");
				JSONArray fromObject2 = JSONArray.fromObject(string);
				for (int i = 0; i < fromObject2.size(); i++) {
					h = new HousingFundQinZhouAccount();
					JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
					h.setDatea(fromObject3.getString("jzrq"));
					h.setGetType(fromObject3.getString("gjhtqywlx"));
					h.setMoney(fromObject3.getString("fse"));
					h.setInterest(fromObject3.getString("fslxe"));
					h.setReason(fromObject3.getString("tqyy"));
					h.setType(fromObject3.getString("tqfs"));
					h.setTaskid(taskHousing.getTaskid());
					list.add(h);
				}
				webParam.setList(list);
				webParam.setHtml(page3.getWebResponse().getContentAsString());
				webParam.setUrl(userUrl);
			}
		}
		return webParam;
		
		
	}
	
	
	// 当前时间
	public String getTime(String fmt) {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		return hehe;
	}

	/*
	 * @Des 获取当前月 的前i个月的 时间
	 */
	public String getDateBefore(String fmt, int i) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, -i);
		Date m = c.getTime();
		String mon = format.format(m);
		return mon;
	}

}
