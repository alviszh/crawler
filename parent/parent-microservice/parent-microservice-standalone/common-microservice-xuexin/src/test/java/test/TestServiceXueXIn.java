package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.jiyuan.HousingJiyuanUserInfo;
import com.microservice.dao.entity.crawler.telecom.jiangsu.TelecomJiangsuBillSum;
import com.microservice.dao.entity.crawler.xuexin.XuexinEducationInfo;
import com.microservice.dao.entity.crawler.xuexin.XuexinSchoolInfo;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import test.ocr.Base64Util;
import test.ocr.HttpUtil;
import test.ocr.Test;

public class TestServiceXueXIn {

	// 通用文字识别（高精度版）
	public static String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";

	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

//			loginlt(webClient);
			// xlcx(webClient);
			// saveImg(webClient);
			// getxjjson();
			// getxljson();
			// getxjurl();
			// getxlurl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static String getImagePath(Page page,String imagePath) throws Exception{ 
		File parentDirFile = new File(imagePath); 
		parentDirFile.setReadable(true); 
		parentDirFile.setWritable(true); 
		if (!parentDirFile.exists()) { 
		System.out.println("==========创建文件夹=========="); 
		parentDirFile.mkdirs(); 
		} 
		String imageName = 111 + ".jpg"; 
		File codeImageFile = new File(imagePath + "/" + imageName); 
		codeImageFile.setReadable(true); 
		codeImageFile.setWritable(true, false); 
		//////////////////////////////////////// 

		String imgagePath = codeImageFile.getAbsolutePath(); 
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
	
	
	
	
	public static WebClient loginlt(WebClient webClient) throws Exception {

//		String url = "https://account.chsi.com.cn/passport/login?service=https%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check";
		 String url = "https://account.chsi.com.cn/passport/login";

		Page page1 = getPage(webClient, null, url, HttpMethod.GET, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();

		System.out.println("html-------------" + html);
		Document doc = Jsoup.parse(html);
		String lt = doc.getElementsByAttributeValue("name", "lt").val();
		System.out.println("lt------------" + lt);

		ChaoJiYingOcrService ocrService = new ChaoJiYingOcrService();
		String urlImg = "https://account.chsi.com.cn/passport/captcha.image";
		Page pageImg = getPage(webClient, null, urlImg, HttpMethod.GET, null, null, null, null);
		InputStream contentAsStream = pageImg.getWebResponse().getContentAsStream();
		File codeImageFile = ocrService.getImageLocalPath();
		String imgagePath = codeImageFile.getAbsolutePath();
		save(contentAsStream, imgagePath);
		String verifycode = ocrService.callChaoJiYingService(imgagePath, "6004");
		System.out.println("qqq===="+verifycode);
//		String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", "664767895@qq.com"));
		paramsList.add(new NameValuePair("password", "15903148170"));
		paramsList.add(new NameValuePair("captcha", verifycode));
		paramsList.add(new NameValuePair("lt", lt));
		paramsList.add(new NameValuePair("_eventId", "submit"));
		paramsList.add(new NameValuePair("submit", "登  录"));

		Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);
		String htm = page.getWebResponse().getContentAsString();
		System.out.println("htm-------------" + htm);

		if (htm.contains("登录成功")) {
			System.out.println("登录成功");
		} else {
			System.out.println("失败	");
		}

		return webClient;

	}
	

	public static WebClient logintwo(WebClient webClient) throws Exception {

//		String url = "https://account.chsi.com.cn/passport/login?service=https%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check";
		 String url = "https://account.chsi.com.cn/passport/login";

		Page page1 = getPage(webClient, null, url, HttpMethod.GET, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();

		System.out.println("html-------------" + html);
		Document doc = Jsoup.parse(html);
		String lt = doc.getElementsByAttributeValue("name", "lt").val();
		System.out.println("lt------------" + lt);

		String urlImg = "https://account.chsi.com.cn/passport/captcha.image";
		Page pageImg = getPage(webClient, null, urlImg, HttpMethod.GET, null, null, null, null);
		InputStream contentAsStream = pageImg.getWebResponse().getContentAsStream();
		String urlimg = "D:\\img\\xxxx.jpg";
		save(contentAsStream, urlimg);
		String inputuserjymtemp = JOptionPane.showInputDialog("请输入验证码……");

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("username", "664767895@qq.com"));
		paramsList.add(new NameValuePair("password", "15903148170"));
		paramsList.add(new NameValuePair("captcha", inputuserjymtemp));
		paramsList.add(new NameValuePair("lt", lt));
		paramsList.add(new NameValuePair("_eventId", "submit"));
		paramsList.add(new NameValuePair("submit", "登  录"));

		Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);
		String htm = page.getWebResponse().getContentAsString();
		System.out.println("htm-------------" + htm);

		if (htm.contains("登录")) {
			System.out.println("失败");
		} else {
			System.out.println("成功");
		}

		return webClient;

	}

	/**
	 * 学历页面 https://my.chsi.com.cn/archive/gdjy/xl/show.action
	 * attr------------https://my.chsi.com.cn/archive/gdjy/photo/show.action?pid=951ad88ec3d090c1c9dbfcc903ca944e
	 * attr------------https://my.chsi.com.cn/archive/gdjy/photo/show.action?pid=9d571ad080d837851c7d9e45ed44f2a2
	 */
	public static void getxlurl() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\xlhtml.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("xjxx-img");
		for (Element element : elementsByClass) {
			String attr = element.attr("src");
			System.out.println("attr------------" + attr);
		}
	}

	/**
	 * 学籍页面 https://my.chsi.com.cn/archive/gdjy/xj/show.action
	 * attr------------https://my.chsi.com.cn/archive/gdjy/photo/show.action?pid=396f1a3bea02967f882f855776ea8b8f
	 * attr------------https://my.chsi.com.cn/archive/gdjy/photo/show.action?pid=e5083af35ef56004920a4f03977cbc45
	 */
	public static void getxjurl() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\xjhtml.txt");
		String json = txt2String(file);
		Document doc = Jsoup.parse(json);
		Elements elementsByClass = doc.getElementsByClass("xjxx-img");
		for (Element element : elementsByClass) {
			String attr = element.attr("src");
			System.out.println("attr------------" + attr);
		}
	}

	public static void getxljson() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\xljson.txt");
		String xmlStr = txt2String(file);
		JSONObject jsonObj = JSONObject.fromObject(xmlStr);
		String payLog = jsonObj.getString("words_result");
		JSONArray jsonArray = JSONArray.fromObject(payLog);
		String taskid = "";
		String name = getValue(jsonArray, 0, "姓名");
		String gender = getValue(jsonArray, 1, "性别");
		String birthday = getValue(jsonArray, 2, "出生日期");
		String entrance_date = getValue(jsonArray, 3, "入学日期");
		String graduation_date = getValue(jsonArray, 4, "毕(结)业日期");
		String school_name = getValue(jsonArray, 5, "学校名称");
		String major = getValue(jsonArray, 6, "专业");
		String education_category = getValue(jsonArray, 7, "学历类别");
		String length_schooling = getValue(jsonArray, 8, "学制");
		String study_form = getValue(jsonArray, 9, "学习形式");
		String level = getValue(jsonArray, 10, "层次");
		String graduation = getValue(jsonArray, 11, "毕(结)业");
		String dean = getValue(jsonArray, 12, "校(院)长姓名");
		String certificate_num = getValue(jsonArray, 13, "证书编号");

		XuexinEducationInfo xuexinEducationInfo = new XuexinEducationInfo(taskid, name, gender, birthday, entrance_date,
				graduation_date, school_name, major, education_category, length_schooling, study_form, level,
				graduation, dean, certificate_num);
		JSONObject json = JSONObject.fromObject(xuexinEducationInfo);
		System.out.println(json);
	}

	public static void getxjjson() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\ceshi.txt");
		String xmlStr = txt2String(file);
		JSONObject jsonObj = JSONObject.fromObject(xmlStr);
		String payLog = jsonObj.getString("words_result");
		JSONArray jsonArray = JSONArray.fromObject(payLog);
		String taskid = "";
		String name = getValue(jsonArray, 0, "姓名");
		String gender = getValue(jsonArray, 1, "性别");
		String birthday = getValue(jsonArray, 2, "出生日期");
		String nation = getValue(jsonArray, 3, "民族");
		String idnumber = getValue(jsonArray, 4, "证件号码");
		String school_name = getValue(jsonArray, 5, "学校名称");
		String level = getValue(jsonArray, 6, "层次");
		String major = getValue(jsonArray, 7, "专业");
		String length_schooling = getValue(jsonArray, 8, "学制");
		String education_category = getValue(jsonArray, 9, "学历类别");
		String study_form = getValue(jsonArray, 10, "学习形式");
		String branch = getValue(jsonArray, 11, "分院");
		String system_place = getValue(jsonArray, 12, "系(所、函授站)");
		String class_place = getValue(jsonArray, 13, "班级");
		String study_num = getValue(jsonArray, 14, "学号");
		String entrance_date = getValue(jsonArray, 15, "入学日期");
		String leave_school_date = getValue(jsonArray, 16, "离校日期");
		String status = getValue(jsonArray, 17, "学籍状态");

		XuexinSchoolInfo xuexinSchoolInfo = new XuexinSchoolInfo(taskid, name, gender, birthday, nation, idnumber,
				school_name, level, major, length_schooling, education_category, study_form, branch, system_place,
				class_place, study_num, entrance_date, leave_school_date, status);
		JSONObject json = JSONObject.fromObject(xuexinSchoolInfo);
		System.out.println(json);
	}

	public static String getValue(JSONArray jsonArray, int i, String key) {
		String string = JSONObject.fromObject(jsonArray.get(i)).getString("words");
		if (string.contains(key)) {
			string = string.replace(key, "");
		}
		if (string.contains(":")) {
			// string = string.replace(":", "");
			string = string.substring(string.lastIndexOf(":") + 1);
		}
		return string;
	}

	public static String getValue(JSONArray jsonArray, int i) {
		String string = JSONObject.fromObject(jsonArray.get(i)).getString("words");
		if (string.contains(":")) {
			string = string.substring(string.lastIndexOf(":") + 1);
		}
		return string;
	}

	public static void getlt() {
		File file = new File("C:\\Users\\Administrator\\Desktop\\loginLt.txt");
		String json = txt2String(file);

		Document doc = Jsoup.parse(json);
		String lt = doc.getElementsByAttributeValue("name", "lt").val();
		System.out.println("lt------------" + lt);
	}

	/**
	 * 验证查询学历
	 */
	public static WebClient xlcx(WebClient webClient) throws Exception {
		String url = "http://www.chsi.com.cn/xlcx/bg.do?vcode=720951786148&trnd=&srcid=archive";
		// List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		// paramsList.add(new NameValuePair("GRXM", ""));
		// paramsList.add(new NameValuePair("GRBH", ""));
		// paramsList.add(new NameValuePair("SFZH", ""));

		Map<String, String> map = new HashMap<String, String>();

		// map.put("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// map.put("Accept-Encoding", "gzip, deflate");
		// map.put("Accept-Language", "zh-CN,zh;q=0.9");
		// map.put("Cache-Control", "max-age=0");
		// map.put("Connection", "keep-alive");
		// map.put("Content-Type", "application/x-www-form-urlencoded");
		// map.put("Host", "my.chsi.com.cn");
		// map.put("Upgrade-Insecure-Requests", "1");
		// map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84
		// Safari/537.36");

		map.put("User-Agent",
				"Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Mobile Safari/537.36");
		// map.put("Cookie",
		// "__utmz=65168252.1516097416.2.2.utmcsr=baidu|utmccn=(organic)|utmcmd=organic;
		// acw_tc=AQAAAAfL3wvA0gAAo1d+e8WS7zeGRXsS;
		// _ga=GA1.3.208352660.1516003270; _gid=GA1.3.434547048.1516241885;
		// __utmc=65168252; JSESSIONID=EC7D744C08A8FC1EF6198465F8B90BA8;
		// __utma=65168252.208352660.1516003270.1516244000.1516257352.6;
		// __utmt=1; __utmb=65168252.13.10.1516257352");

		Page page1 = getPage(webClient, null, url, HttpMethod.GET, null, null, null, map);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("xlcx---html-------------" + html);

		return webClient;

	}

	/**
	 * 学信档案
	 */
	public static WebClient xxda(WebClient webClient) throws Exception {
		String url = "https://my.chsi.com.cn/archive/bab/xj/show.action?trnd=";
		Page page1 = getPage(webClient, null, url, HttpMethod.GET, null, null, null, null);
		String html = page1.getWebResponse().getContentAsString();
		System.out.println("html-------------" + html);
		return webClient;
	}

	/**
	 * 保存图片
	 */
	public static WebClient saveImg(WebClient webClient) throws Exception {
		String uri = "https://my.chsi.com.cn/archive/gdjy/photo/show.action?pid=5b02480955698743f174aaec2b2f0fbb";
		String url = "https://my.chsi.com.cn/archive/gdjy/photo/show.action?pid=b1eaec739a0eb314b86024dc9d84606b";
		// List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		// paramsList.add(new NameValuePair("GRXM", ""));
		// paramsList.add(new NameValuePair("GRBH", ""));
		// paramsList.add(new NameValuePair("SFZH", ""));

		Map<String, String> map = new HashMap<String, String>();

		// map.put("Accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// map.put("Accept-Encoding", "gzip, deflate, br");
		// map.put("Accept-Language", "zh-CN,zh;q=0.9");
		// map.put("Cache-Control", "max-age=0");
		// map.put("Connection", "keep-alive");
		// map.put("Content-Type", "application/x-www-form-urlencoded");
		// map.put("Host", "my.chsi.com.cn");
		// map.put("Referer",
		// "https://my.chsi.com.cn/archive/bab/xl/show.action?trnd=1516241907306");
		// map.put("Upgrade-Insecure-Requests", "1");
		// map.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64)
		// AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84
		// Safari/537.36");

		map.put("Cookie",
				"JSESSIONID=D6586655444E99001ABF26E5595E5220; _ga=GA1.3.208352660.1516003270; __utmz=65168252.1516327798.8.4.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utmz=228727808.1516865329.17.4.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=65168252.208352660.1516003270.1517212116.1517292370.14; __utma=228727808.596196258.1516003280.1517388635.1517451669.26; __utmc=228727808; __utmt=1; __utmb=228727808.3.10.1517451669");

		Page page1 = getPage(webClient, null, url, HttpMethod.POST, null, null, "", map);
		InputStream contentAsStream = page1.getWebResponse().getContentAsStream();
		// String html = page1.getWebResponse().getContentAsString();
		// String urlimg = "D:\\img\\xl.png";
		// save(contentAsStream, urlimg);
		// System.out.println("html-------------" + html);

		byte[] byt = new byte[contentAsStream.available()];
		contentAsStream.read(byt);
		String token = Test.getAuth();
		String imgStr = Base64Util.encode(byt);
		String param = "image=" + URLEncoder.encode(imgStr, "UTF-8");

		/**
		 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
		 */
		String result = HttpUtil.post(otherHost, token, param);
		System.out.println(result);

		return webClient;

	}

	public static void save(InputStream inputStream, String filePath) throws Exception {

		OutputStream outputStream = new FileOutputStream(filePath);
		int byteCount = 0;
		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, byteCount);

		}
		inputStream.close();
		outputStream.close();
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
	public static Page getPage(WebClient webClient, String taskid, String url, HttpMethod type,
			List<NameValuePair> paramsList, String code, String body, Map<String, String> map) throws Exception {
		// tracerLog.output("CmbChinaService.getPage---url:", url + "---taskId:"
		// + taskid);
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
		// tracerLog.output("CmbChinaService.getPage.statusCode:" + statusCode,
		// url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

	public static String txt2String(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			// BufferedReader br = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = br.readLine()) != null) {
				result.append(System.lineSeparator() + s);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
