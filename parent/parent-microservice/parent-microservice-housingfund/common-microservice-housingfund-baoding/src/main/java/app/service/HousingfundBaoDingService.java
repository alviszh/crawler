package app.service;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.baoding.HousingBaoDingPay;
import com.microservice.dao.entity.crawler.housing.baoding.HousingBaoDingUserInfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.baoding.HousingBaoDingPayRepository;
import com.microservice.dao.repository.crawler.housing.baoding.HousingBaoDingUserInfoRepository;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.Base64Util;
import app.bean.FileUtil;
import app.bean.HttpUtil;
import app.commontracerlog.TracerLog;
import app.parser.HousingfundBaoDingParser;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.baoding"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.baoding"})
public class HousingfundBaoDingService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Autowired
	private HousingfundBaoDingParser housingfundBaoDingParser;
	@Autowired
	private HousingBaoDingUserInfoRepository housingBaoDingUserInfoRepository;
	@Autowired
	private HousingBaoDingPayRepository housingBaoDingPayRepository;
	@Value("${driverPath}")
	public String driverPath;
	@Value("${akpath}")
	String akpath;
	@Value("${skpath}")
	String skpath;
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {

		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		System.out.println(messageLoginForHousing.toString());
		tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_LOADING.getDescription());
		//发送验证码状态更新
		save(taskHousing);

		String url = "http://www.bdgjj.gov.cn/wt-web/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try{
			HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");//身份证
			HtmlPasswordInput pass = (HtmlPasswordInput) page.getElementById("password");//密码
			HtmlTextInput captcha = (HtmlTextInput) page.getElementById("captcha");//验证码
			HtmlImage img = (HtmlImage) page.getFirstByXPath("//img[@src='/wt-web/captcha']");//图片验证码

			String image = chaoJiYingOcrService.getVerifycode(img, "1902");
			username.setText(messageLoginForHousing.getNum());
			pass.setText(messageLoginForHousing.getPassword());
			captcha.setText(image);

			HtmlButton login = (HtmlButton) page.getElementById("gr_login");
			Page page2 = login.click();
			String html = page2.getWebResponse().getContentAsString();
			if(html.indexOf("安全退出")!=-1){
				System.out.println("登录成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setCookies(cookies);
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				save(taskHousing);
				
				String date = new Date().toLocaleString().substring(0, 10);
				String url2 = "http://www.bdgjj.gov.cn/wt-web/personal/jcmxlist?"
						+ "UserId=1&beginDate=2000-01-01&endDate="+date+"&userId=1&pageNum=1&pageSize=500";
				Page page3 = gethtmlPost(webClient, null, url2);
				String ocr1 = page3.getWebResponse().getContentAsString();
				System.out.println(ocr1);

			}else{
				System.out.println("登录失败");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
				save(taskHousing);
			}

		}catch (Exception e) {
			tracer.addTag("parser.login.taskid", e.getMessage());
			System.out.println("登录失败");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
			save(taskHousing);
		}
	}
	/***
	 * 用户信息
	 * @param messageLogin
	 * @param taskHousing
	 * @throws Exception
	 */
	public TaskHousing getcrawler(MessageLoginForHousing messageLogin, TaskHousing taskHousing) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		save(taskHousing);
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		String url = "http://www.bdgjj.gov.cn/wt-web/person/jbxx";
		Page page = LoginAndGetCommon.getHtml(url, webClient);
		InputStream contentAsStream2 = page.getWebResponse().getContentAsStream();
		String urlimg = "F:\\img\\" + UUID.randomUUID().toString() + ".png";
		save(contentAsStream2, urlimg);
		String token = getAuth();
		System.out.println(token);
		if (token == null) {
			System.out.println("token获取失败!请检查是否网络问题或者失效了!");
		} else {
			System.out.println("token获取成功!");
			// 通用识别url
			String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate";
			byte[] imgData = FileUtil.readFileByBytes(urlimg);
			String imgStr = Base64Util.encode(imgData);
			String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
			/**
			 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
			 */
			String result = HttpUtil.post(otherHost, token, params);
			System.out.println(result);

			JSONObject jsonobject = JSONObject.fromObject(result);

			if(result.contains("error_code")){
				String error_code = jsonobject.getString("error_code").trim();
				System.out.println("图片解析失败！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription("找不到链接，网路延迟！");
				taskHousing.setUserinfoStatus(500);
				save(taskHousing);
				if (error_code == "17") {
					System.out.println("每天请求量超限额");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
					taskHousing.setPaymentStatus(500);
					taskHousing.setError_message("每天请求量超限额");
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(500);
					taskHousing.setError_message("发生未知错误");
					save(taskHousing);
				}
			}else{
				System.out.println("图片解析成功！进行数据解析！");
				String words_result = jsonobject.get("words_result").toString();
				HousingBaoDingUserInfo getuserinfo = housingfundBaoDingParser.getuserinfo(words_result);
				if(getuserinfo!=null){
					getuserinfo.setTaskid(messageLogin.getTask_id());
					housingBaoDingUserInfoRepository.save(getuserinfo);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setUserinfoStatus(200);
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
					taskHousing.setUserinfoStatus(201);
					save(taskHousing);
				}
			}
		}
		/***
		 * 流水信息
		 */
		Thread.sleep(5000);
		String date = new Date().toLocaleString().substring(0, 10);
		String url2 = "http://www.bdgjj.gov.cn/wt-web/personal/jcmxlist?"
				+ "UserId=1&beginDate=2000-01-01&endDate="+date+"&userId=1&pageNum=1&pageSize=500";
		Page page3 = gethtmlPost(webClient, null, url2);
		String ocr1 = page3.getWebResponse().getContentAsString();
		if(ocr1.indexOf("")!=-1){
			List<HousingBaoDingPay> getpay = housingfundBaoDingParser.getpay(ocr1,messageLogin);
			if(getpay!=null){
				housingBaoDingPayRepository.saveAll(getpay);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(200);
				save(taskHousing);
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(501);
				save(taskHousing);
			}
		}else{
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
			taskHousing.setPaymentStatus(404);
			save(taskHousing);
		}
		updateTaskHousing(messageLogin.getTask_id());
		return taskHousing;
	}

	public static void save(InputStream inputStream, String filePath) throws Exception{ 

		OutputStream outputStream = new FileOutputStream(filePath); 

		int bytesWritten = 0; 
		int byteCount = 0; 

		byte[] bytes = new byte[1024]; 

		while ((byteCount = inputStream.read(bytes)) != -1) 
		{ 
			outputStream.write(bytes, 0, byteCount); 

		} 
		inputStream.close(); 
		outputStream.close(); 
	} 

	public String getAuth() {

		// API Key
		String clientId = akpath;
		// Secret Key
		String clientSecret = skpath;
		return getAuth(clientId, clientSecret);
	}
	public String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// grant_type为固定参数
				+ "grant_type=client_credentials"
				// API Key
				+ "&client_id=" + ak
				// Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
