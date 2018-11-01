package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.baidu.aip.ocr.AipOcr;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoAccount;
import com.microservice.dao.entity.crawler.housing.tongliao.HousingFundTongLiaoUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundTongLiaoParser {
	@Value("${spring.APP_ID.name}")
	String APP_ID;
	@Value("${spring.API_KEY.name}")
	String API_KEY;
	@Value("${spring.SECRET_KEY.name}")
	String SECRET_KEY;
	@Value("${filesavepath}")
	String fileSavePath;
	@Value("${templateSign}")
	String templateSign;
	// 设置APPID/AK/SK
	// public static final String APP_ID = "10715647";
	// public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
	// public static final String SECRET_KEY =
	// "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	protected TaskHousingRepository taskHousingRepository;

	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url = "http://www.tlzfgjj.com:8889/wt-web/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");
		elementById.reset();
		elementById.setText(messageLoginForHousing.getNum());

		String encryptedPhone = encryptedPhone(messageLoginForHousing.getPassword());
		System.out.println(encryptedPhone);

		HtmlImage img = page.getFirstByXPath("//*[@id='captcha_img']");
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1005");
		String requestBody = "username=" + messageLoginForHousing.getNum() + "&password=" + encryptedPhone
				+ "&force=force&captcha=" + verifycode;
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

		webRequest.setRequestBody(requestBody);
		Page page2 = webClient.getPage(webRequest);
		Thread.sleep(1000);
		System.out.println(page2.getWebResponse().getContentAsString());
		WebParam webParam = new WebParam();
		if (page2.getWebResponse().getContentAsString().contains("退出服务大厅")) {
			webParam.setHtml(page2.getWebResponse().getContentAsString());
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
			return webParam;
		}
		return null;
	}

	// 加密
	public String encryptedPhone(String phonenum) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = this.readResource("zhumadian.js", Charsets.UTF_8);
		// System.out.println(path);
		// FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path);
		final Invocable invocable = (Invocable) engine;
		Object data = invocable.invokeFunction("encryptedString", phonenum);
		return data.toString();
	}

	public String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	// 个人信息
	public WebParam<HousingFundTongLiaoUserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		String url3 = "http://www.tlzfgjj.com:8889/wt-web/jcr/jcrkhxxcx_mh.service";
		WebRequest requestSettings1 = new WebRequest(new URL(url3), HttpMethod.POST);
		String requestBody = "ffbm=01&ywfl=01&ywlb=99&cxlx=01";
		requestSettings1.setRequestBody(requestBody);
		Page page4 = webClient.getPage(requestSettings1);
		System.out.println(page4.getWebResponse().getContentAsString());
		if (page4.getWebResponse().getContentAsString().contains("results")) {
			JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
			String string = fromObject.getString("results");
			JSONArray fromObject2 = JSONArray.fromObject(string);
			HousingFundTongLiaoUserInfo h = new HousingFundTongLiaoUserInfo();
			List<HousingFundTongLiaoUserInfo> list = new ArrayList<HousingFundTongLiaoUserInfo>();
			WebParam<HousingFundTongLiaoUserInfo> webParam = new WebParam<HousingFundTongLiaoUserInfo>();
			String num = null;
			for (int i = 0; i < fromObject2.size(); i++) {
				h = new HousingFundTongLiaoUserInfo();
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
				if (taskHousing.getCrawlerHost() != null) {
					taskHousing.getCrawlerHost().replaceAll(taskHousing.getCrawlerHost(), "");
				}
				num = taskHousing.getCrawlerHost() + "," + h.getCardNum();
				System.out.println(list);
				taskHousing.setCrawlerHost(num);
				taskHousingRepository.save(taskHousing);
			}
			webParam.setList(list);
			webParam.setHtml(page4.getWebResponse().getContentAsString());
			webParam.setUrl(url3);

			return webParam;
		}
		return null;
	}

	// 流水
	public WebParam<HousingFundTongLiaoAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing,
			TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebParam<HousingFundTongLiaoAccount> webParam = new WebParam<HousingFundTongLiaoAccount>();
		HousingFundTongLiaoAccount h = null;
		List<HousingFundTongLiaoAccount> list = new ArrayList<HousingFundTongLiaoAccount>();
		// 从数据库获取账户账号
		String crawlerHost = taskHousing.getCrawlerHost();
		String[] split = crawlerHost.split(",");
		for (int j = 0; j < split.length; j++) {
//			 //获取数据个数 count
//			 String
//			 urlpay1="http://www.tlzfgjj.com:8889/wt-web/jcr/jcrxxcxzhmxcx_.service";
//			 String reString =
//			 "ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq="+getDateBefore("yyyy-MM-dd",
//			 2)+"&jsrq="+getTime("yyyy-MM-dd")+"&grxx="+split[j]+"&fontSize=13px&pageNum=1&pageSize=10";
//			 System.out.println(reString);
//			 WebRequest webRequest2 = new WebRequest(new URL(urlpay1),
//			 HttpMethod.POST);
//			 webRequest2.setRequestBody(reString);
//			 Page page = webClient.getPage(webRequest2);
//			 System.out.println(page.getWebResponse().getContentAsString());
//			 JSONObject fromObject1 =
//			 JSONObject.fromObject(page.getWebResponse().getContentAsString());
//			 //总数
//			 String totalcount = fromObject1.getString("totalcount");
//			 int count = Integer.parseInt(totalcount);
//			 //循环页数 获取数据 只取每页10条的
//			 for (int k = 1; k <= count/10; k++) {
//			 double random = Math.random();
			String urlpay = "http://www.tlzfgjj.com:8889/wt-web/jcr/jcrxxcxzhmxcx.service?ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq="+ getDateBefore("yyyy-MM-dd", 2) + "&jsrq=" + getTime("yyyy-MM-dd") + "&grxx="+ split[j].toString();
			WebRequest webRequest2 = new WebRequest(new URL(urlpay), HttpMethod.GET);
			webRequest2.setCharset(Charset.forName("UTF-8"));
			Page page4 = webClient.getPage(webRequest2);
//			 Thread.sleep(2000);
//			 //百度解析图片
//			 String imagePath = getImagePath(page4);
//			 // 初始化一个AipOcr
//			 AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
//			 // 可选：设置网络连接参数
//			 client.setConnectionTimeoutInMillis(2000);
//			 client.setSocketTimeoutInMillis(60000);
//			 String sample = sample(client, imagePath);
//			 System.out.println(sample);
//			 if(sample.contains("ret"))
//			 {
//			 JSONObject fromObject = JSONObject.fromObject(sample);
//			 String string = fromObject.getString("data");
//			 JSONObject fromObject3 = JSONObject.fromObject(string);
//			 String string2 = fromObject3.getString("ret");
//			 JSONArray fromObject2 = JSONArray.fromObject(string2);
//			 for (int i = 0; i < fromObject2.size(); i=i+7) {
//			 h = new HousingFundTongLiaoAccount();
//			 h.setDatea(JSONObject.fromObject(fromObject2.get(i)).getString("word"));
//			 h.setType(JSONObject.fromObject(fromObject2.get(i+1)).getString("word"));
//			 h.setMoney(JSONObject.fromObject(fromObject2.get(i+2)).getString("word"));
//			 h.setInterest(JSONObject.fromObject(fromObject2.get(i+3)).getString("word"));
//			 h.setFee(JSONObject.fromObject(fromObject2.get(i+4)).getString("word"));
//			 h.setReason(JSONObject.fromObject(fromObject2.get(i+5)).getString("word"));
//			 h.setGetType(JSONObject.fromObject(fromObject2.get(i+6)).getString("word"));
//			 h.setTaskid(taskHousing.getTaskid());
//			 System.out.println(h);
//			 list.add(h);
//			 }
//			 System.out.println(list);
//			 webParam.setList(list);
//			 webParam.setHtml(sample);
//			 webParam.setUrl(urlpay);
//			 }
//			 }
//			 }
			System.out.println(page4.getWebResponse().getContentAsString());
			if(page4.getWebResponse().getContentAsString().contains("true"))
			{
				JSONObject fromObject = JSONObject.fromObject(page4.getWebResponse().getContentAsString());
				String string = fromObject.getString("results");
				JSONArray fromObject3 = JSONArray.fromObject(string);
				for (int i = 0; i < fromObject3.size(); i++) {
					JSONObject fromObject2 = JSONObject.fromObject(fromObject3.get(i));
					// System.out.println(fromObject2);
					h = new HousingFundTongLiaoAccount();
					h.setDatea(fromObject2.getString("jzrq"));
					h.setType(fromObject2.getString("gjhtqywlx"));
					h.setMoney(fromObject2.getString("fse"));
					h.setInterest(fromObject2.getString("fslxe"));
					h.setFee(fromObject2.getString("grzhye"));
					h.setReason(fromObject2.getString("tqyy"));
					h.setGetType(fromObject2.getString("tqfs"));
					h.setTaskid(messageLoginForHousing.getTask_id());
					list.add(h);
				}
				 webParam.setList(list);
				 webParam.setHtml(page4.getWebResponse().getContentAsString());
				 webParam.setUrl(urlpay);
			}
		}
		return webParam;

	}

	public static String sample(AipOcr client, String image) {
		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
		// options.put("Content-Type", "application/x-www-form-urlencoded");
		// options.put("image", "true");
		// options.put("templateSign", "421bef7004708216699e2f955d1f2d43");

		options.put("detect_direction", "true");
		options.put("probability", "true");
		org.json.JSONObject res = client.custom(image, "templateSign", options);
		System.out.println(res.toString(2));
		return res.toString(2);
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
		String path = "fileSavePath";
		// if
		// (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase())
		// != -1) {
		// path = System.getProperty("user.dir") + "/verifyCodeImage/";
		// } else {
		// path = System.getProperty("user.home") + "/verifyCodeImage/";
		// }
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		// String imageName = "image.jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
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
