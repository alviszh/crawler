package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.aip.ocr.AipOcr;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianAccount;
import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianUserInfo;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.common.HousingBasicService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundZhuMaDianParser {
	@Value("${spring.APP_ID.name}") 
	String APP_ID;
	@Value("${spring.API_KEY.name}") 
	String API_KEY;
	@Value("${spring.SECRET_KEY.name}") 
	String SECRET_KEY;
	@Value("${filesavepath}") 
	String fileSavePath;

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingBasicService housingBasicService;
	
	// 设置APPID/AK/SK
//	public static final String APP_ID = "10715647";
//	public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
//	public static final String SECRET_KEY = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";

	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String img="http://www.zmdgjj.com:8090/wt-web/captcha";
		Page page6 = webClient.getPage(img);
		String imagePath = housingBasicService.getImagePath(page6, fileSavePath);
		
		String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1004"); 
		
		String urllogin="http://www.zmdgjj.com:8090/wt-web/login";
		WebRequest webRequest = new WebRequest(new URL(urllogin), HttpMethod.POST);
		
		Page page = webClient.getPage(urllogin);
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Element e1 = doc.getElementById("modulus");
		String val1 = e1.val();
		
		String params="111111";
		String str = encryptedPhone(params,val1);
		System.out.println(str);
		
		String a  ="username=412821198712040212&password="+str+"&captcha="+code+"&logintype=1";
		webRequest.setRequestBody(a);
	    Page page2 = webClient.getPage(webRequest);
	    System.out.println(page2.getWebResponse().getContentAsString());
		String a1="http://www.zmdgjj.com:8090/wt-web/home?logintype=1";
		WebRequest webRequest2 = new WebRequest(new URL(a1), HttpMethod.GET);
		Page page1 = webClient.getPage(webRequest2);
		System.out.println(page1.getWebResponse().getContentAsString());
		WebParam webparam = new WebParam();
		webparam.setHtml(page1.getWebResponse().getContentAsString());
		webparam.setUrl(urllogin);
		webparam.setWebClient(webClient);
		return webparam;
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

	// 缴存流水
	public WebParam<HousingFundZhuMaDianAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebParam<HousingFundZhuMaDianAccount> webParam = new WebParam<HousingFundZhuMaDianAccount>();
		String dateBefore = getDateBefore("yyyy-MM-dd", 3);
		System.out.println(dateBefore);

		String time = getTime("yyyy-MM-dd");
		System.out.println(time);
		String urlAc = "http://www.zmdgjj.com/wt-web/personal/jcmxlist";
		WebRequest requestSettings2 = new WebRequest(new URL(urlAc), HttpMethod.POST);
		String requestBody = "UserId=1&beginDate=" + dateBefore + "&endDate=" + time+ "&userId=1&pageNum=1&pageSize=1000";
		requestSettings2.setRequestBody(requestBody);
		Page page3 = webClient.getPage(requestSettings2);
		System.out.println(page3.getWebResponse().getContentAsString());
		String contentAsString = page3.getWebResponse().getContentAsString();
		HousingFundZhuMaDianAccount h = null;
		List<HousingFundZhuMaDianAccount> list = new ArrayList<HousingFundZhuMaDianAccount>();
		if(contentAsString.contains("results"))
		{
			JSONObject fromObject = JSONObject.fromObject(contentAsString);
			String string = fromObject.getString("results");
			JSONArray fromObject2 = JSONArray.fromObject(string);
			for (int i = 0; i < fromObject2.size(); i++) {
				h = new HousingFundZhuMaDianAccount();
				JSONObject fromObject3 = JSONObject.fromObject(fromObject2.get(i));
				h.setAccountNum(fromObject3.getString("ywlsh"));
				h.setDatea(fromObject3.getString("rq"));
				h.setGetMoney(fromObject3.getString("jfje"));
				h.setSetMoney(fromObject3.getString("dfje"));
				h.setFee(fromObject3.getString("ye"));
				h.setDescr(fromObject3.getString("zy"));
				h.setTaskid(taskHousing.getTaskid());
				list.add(h);
			}
			webParam.setHtml(page3.getWebResponse().getContentAsString());
			webParam.setUrl(urlAc);
			webParam.setList(list);
			return webParam;
		}
		return null;
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

	// 个人信息
	public WebParam<HousingFundZhuMaDianUserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)
			throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}

		double random = Math.random();
		// 输出信息
		System.out.println("This is a random for double:" + random);
		String url3 = "http://www.zmdgjj.com/wt-web/person/jbxx?random=" + random;
		WebRequest requestSettings1 = new WebRequest(new URL(url3), HttpMethod.GET);
		Page page4 = webClient.getPage(requestSettings1);


		String imagePath = getImagePath(page4);

		// 初始化一个AipOcr
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);
		String sample = sample(client, imagePath);
		System.out.println(sample);
		WebParam<HousingFundZhuMaDianUserInfo> webParam = new WebParam<HousingFundZhuMaDianUserInfo>();
		if(sample.contains("职工姓名"))
		{
			JSONObject fromObject = JSONObject.fromObject(sample);
			System.out.println(fromObject);
			String string = fromObject.getString("words_result");
			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			HousingFundZhuMaDianUserInfo h = new HousingFundZhuMaDianUserInfo();
			
			h.setCompanyNum(delete(JSONObject.fromObject(fromObject2.get(0)).getString("words").substring(4)));
			h.setCompany(delete(JSONObject.fromObject(fromObject2.get(1)).getString("words").substring(4)));
			h.setPersonalNum(delete(JSONObject.fromObject(fromObject2.get(2)).getString("words").substring(4)));
			h.setName(delete(JSONObject.fromObject(fromObject2.get(3)).getString("words").substring(4)));
			h.setStatus(delete(JSONObject.fromObject(fromObject2.get(4)).getString("words").substring(4)));
			h.setCardNum(delete(JSONObject.fromObject(fromObject2.get(5)).getString("words").substring(4)));
			h.setPhone(delete(JSONObject.fromObject(fromObject2.get(6)).getString("words").substring(4)));
			h.setAddr(delete(JSONObject.fromObject(fromObject2.get(7)).getString("words").substring(4)));
			h.setBank(delete(JSONObject.fromObject(fromObject2.get(8)).getString("words").substring(4)));
			h.setBankNum(delete(JSONObject.fromObject(fromObject2.get(9)).getString("words").substring(4)));
			h.setOpenDate(delete(JSONObject.fromObject(fromObject2.get(10)).getString("words").substring(4)));
			h.setTaskid(taskHousing.getTaskid());
			
			webParam.setHousingFundZhuMaDianUserInfo(h);
			webParam.setHtml(sample);
			webParam.setUrl(url3);
			return webParam;
		}
		return null;
	}
	//去杂
	public String delete(String a)
	{
		if(a.contains(":"))
		{
			return a.replace(":", "");
		}
		return a;
	}
	
	
	//百度API
	public String sample(AipOcr client, String image) {
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();

		options.put("detect_direction", "true");
		options.put("probability", "true");
		org.json.JSONObject res = client.basicAccurateGeneral(image, options);
		System.out.println(res.toString(2));
		return res.toString(2);
	}


	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public String getImagePath(Page page) throws Exception {
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
	public File getImageCustomPath() {
		String path = "fileSavePath";
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
//		String imageName = "image.jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}
}
