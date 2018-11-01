package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chifeng.HousingFundChiFengAccount;
import com.microservice.dao.entity.crawler.housing.chifeng.HousingFundChiFengUserInfo;
import com.microservice.dao.entity.crawler.housing.zhumadian.HousingFundZhuMaDianAccount;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundChiFengParser {
	
	@Value("${spring.APP_ID.name}") 
	String APP_ID;
	@Value("${spring.API_KEY.name}") 
	String API_KEY;
	@Value("${spring.SECRET_KEY.name}") 
	String SECRET_KEY;
	@Value("${filesavepath}") 
	String fileSavePath;
	// 设置APPID/AK/SK
//	public static final String APP_ID = "10715647";
//	public static final String API_KEY = "85Yh1jbkPVjTVAa0SWcvGqlC";
//	public static final String SECRET_KEY = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	
	//登陆
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String url="http://cfszfgjj.com/wt-web-gr/grlogin";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = webClient.getPage(url);	
		HtmlTextInput elementById = (HtmlTextInput) page.getFirstByXPath("//input[@id='username']");
		elementById.reset();
		elementById.setText(messageLoginForHousing.getNum());
		
		HtmlPasswordInput elementById1 = (HtmlPasswordInput) page.getFirstByXPath("//input[@id='in_password']");
		elementById1.reset();
		elementById1.setText(messageLoginForHousing.getPassword());
		
		
		HtmlImage img = page.getFirstByXPath("//*[@id='captcha_img']");
		
		String verifycode = chaoJiYingOcrService.getVerifycode(img, "1902");
		HtmlTextInput yzm = (HtmlTextInput)page.getFirstByXPath("//input[@id='captcha']");
		yzm.reset();
		yzm.setText(verifycode);
		
		HtmlElement button = page.getFirstByXPath("//*[@id='gr_login']");
		HtmlPage page2 = button.click();
		Thread.sleep(1000);
		WebParam webParam = new WebParam();
		webParam.setHtml(page2.getWebResponse().getContentAsString());
//		System.out.println(page2.getWebResponse().getContentAsString());
		if(page2.getWebResponse().getContentAsString().contains("个人业务"))
		{
			
			webParam.setHtmlPage(page2);
			webParam.setUrl(url);
			webParam.setWebClient(webClient);
		}
		return webParam;
	}

	
	//个人信息
	public WebParam<HousingFundChiFengUserInfo> crawlerUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}

		double random = Math.random();
		// 输出信息
		System.out.println("This is a random for double:" + random);
		String url3 = "http://cfszfgjj.com/wt-web/person/jbxx?random=" + random;
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
		WebParam<HousingFundChiFengUserInfo> webParam = new WebParam<HousingFundChiFengUserInfo>();
		if(sample.contains("职工姓名"))
		{
			JSONObject fromObject = JSONObject.fromObject(sample);
			System.out.println(fromObject);
			String string = fromObject.getString("words_result");
			System.out.println(string);
			JSONArray fromObject2 = JSONArray.fromObject(string);
			HousingFundChiFengUserInfo h = new HousingFundChiFengUserInfo();
			
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
			
			webParam.setHousingFundChiFengUserInfo(h);
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
	
	
	//百度api
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
	
	
	//流水
	public WebParam<HousingFundChiFengAccount> crawlerAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		String cookies = taskHousing.getCookies();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(cookies);
		for (Cookie cookie : cookies1) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebParam<HousingFundChiFengAccount> webParam = new WebParam<HousingFundChiFengAccount>();
		String dateBefore = getDateBefore("yyyy-MM-dd", 3);
		System.out.println(dateBefore);

		String time = getTime("yyyy-MM-dd");
		System.out.println(time);
		String urlAc = "http://cfszfgjj.com/wt-web/personal/jcmxlist";
		WebRequest requestSettings2 = new WebRequest(new URL(urlAc), HttpMethod.POST);
		String requestBody = "UserId=1&beginDate=" + dateBefore + "&endDate=" + time+ "&userId=1&pageNum=1&pageSize=1000";
		requestSettings2.setRequestBody(requestBody);
		Page page3 = webClient.getPage(requestSettings2);
		System.out.println(page3.getWebResponse().getContentAsString());
		String contentAsString = page3.getWebResponse().getContentAsString();
		HousingFundChiFengAccount h = null;
		List<HousingFundChiFengAccount> list = new ArrayList<HousingFundChiFengAccount>();
		if(contentAsString.contains("results"))
		{
			JSONObject fromObject = JSONObject.fromObject(contentAsString);
			String string = fromObject.getString("results");
			JSONArray fromObject2 = JSONArray.fromObject(string);
			for (int i = 0; i < fromObject2.size(); i++) {
				h = new HousingFundChiFengAccount();
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

}
