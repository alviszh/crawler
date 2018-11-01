package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.StandaloneEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.entity.crawler.xuexin.TaskXuexin;
import com.microservice.dao.entity.crawler.xuexin.XuexinHtml;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import com.microservice.dao.repository.crawler.xuexin.TaskXuexinRepository;
import com.microservice.dao.repository.crawler.xuexin.XuexinHtmlRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.XuexinParser;
import app.service.aop.ICrawlerLogin;
import app.util.ocr.Base64Util;
import app.util.ocr.GetAuthUtil;
import app.util.ocr.HttpUtil;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.xuexin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.xuexin" })
public class XuexinTaskService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(XuexinTaskService.class);

	// API Key
	@Value("${datasource.clientId}")
	public String clientId;

	// Secret Key
	@Value("${datasource.clientSecret}")
	String clientSecret;

	@Autowired
	private TracerLog tracer;

	@Autowired
	protected TaskXuexinRepository taskXuexinRepository;

	@Autowired
	protected XuexinHtmlRepository xuexinHtmlRepository;

	@Autowired
	protected XuexinParser xuexinParser;

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
    private TaskStandaloneRepository taskStandaloneRepository;

	// 通用文字识别（高精度版）
	public String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
	
	private Gson gson = new Gson();

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	public TaskStandalone loginTwo(PbccrcJsonBean pbccrcJsonBean, TaskStandalone taskStandalone)
			throws Exception {

		tracer.addTag("XuexinTaskService.login", pbccrcJsonBean.getMapping_id());
		
		TaskXuexin taskXuexin = taskXuexinRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		if (taskXuexin == null) {
			taskXuexin = new TaskXuexin();
		}
		taskXuexin.setLoginInfo(gson.toJson(pbccrcJsonBean));
		taskXuexin.setTaskid(pbccrcJsonBean.getMapping_id());
		try {
			
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			// String url =
			// "https://account.chsi.com.cn/passport/login?service=https%3A%2F%2Fmy.chsi.com.cn%2Farchive%2Fj_spring_cas_security_check";
			String url = "https://account.chsi.com.cn/passport/login";
			Page page1 = getPage(webClient, pbccrcJsonBean.getMapping_id(), url, HttpMethod.GET, null, null, null,
					null);
			String html = page1.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			String lt = doc.getElementsByAttributeValue("name", "lt").val();
			String execution = doc.getElementsByAttributeValue("name", "execution").val();

			String imgUrl = "https://account.chsi.com.cn/passport/captcha.image";
			Page pageImg = getPage(webClient, pbccrcJsonBean.getMapping_id(), imgUrl, HttpMethod.GET, null, null,
					null, null);
			InputStream contentAsStream = pageImg.getWebResponse().getContentAsStream();
			File codeImageFile = chaoJiYingOcrService.getImageLocalPath();
			String imgagePath = codeImageFile.getAbsolutePath();
			save(contentAsStream, imgagePath);
			String verifycode = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "6004");

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("username", pbccrcJsonBean.getUsername()));
			paramsList.add(new NameValuePair("password", pbccrcJsonBean.getPassword()));
			paramsList.add(new NameValuePair("captcha", verifycode));
			paramsList.add(new NameValuePair("submit", "登  录"));
			paramsList.add(new NameValuePair("lt", lt));
			paramsList.add(new NameValuePair("execution", execution));
			paramsList.add(new NameValuePair("_eventId", "submit"));
			Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList, null, null, null);
			String htm = page.getWebResponse().getContentAsString();
			
			if (htm.contains(pbccrcJsonBean.getUsername())) {
				String cookies = CommonUnit.transcookieToJson(webClient);
				taskStandalone.setPhase(StandaloneEnum.STANDALONE_LOGIN_SUCCESS.getPhase());
				taskStandalone.setPhase_status(StandaloneEnum.STANDALONE_LOGIN_SUCCESS.getPhasestatus());
				taskStandalone.setDescription(StandaloneEnum.STANDALONE_LOGIN_SUCCESS.getDescription());
				taskStandaloneRepository.save(taskStandalone);
				
				taskXuexin.setCookies(cookies);
				taskXuexinRepository.save(taskXuexin);
			} else {
				tracer.addTag("登录失败:", htm);
				taskStandalone.setPhase(StandaloneEnum.STANDALONE_PASSWORD_ERROR.getPhase());
				taskStandalone.setPhase_status(StandaloneEnum.STANDALONE_PASSWORD_ERROR.getPhasestatus());
				taskStandalone.setDescription("您输入的用户名或密码有误");
				taskStandaloneRepository.save(taskStandalone);
				
				taskXuexinRepository.save(taskXuexin);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登录有误:", e.toString());
			taskStandalone.setPhase(StandaloneEnum.STANDALONE_LOGIN_ERROR2.getPhase());
			taskStandalone.setPhase_status(StandaloneEnum.STANDALONE_LOGIN_ERROR2.getPhasestatus());
			taskStandalone.setDescription(StandaloneEnum.STANDALONE_LOGIN_ERROR2.getDescription());
			taskStandaloneRepository.save(taskStandalone);
			
			taskXuexinRepository.save(taskXuexin);
			
		}

		return taskStandalone;
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
	public Page getPage(WebClient webClient, String taskid, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {

		tracer.addTag("XuexinTaskService.getPage" + url, "---taskId:" + taskid);
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
		tracer.addTag("XuexinTaskService.statusCode" + statusCode, url + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
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
	 * 获取数据
	 * 
	 * @param xuexinRequestParameters
	 */
	@Async
	@Override
	public String getAllData(PbccrcJsonBean pbccrcJsonBean) {

		tracer.addTag("parser.crawler.taskid", pbccrcJsonBean.getMapping_id());
		tracer.addTag("parser.crawler.auth", pbccrcJsonBean.getUsername());

		TaskXuexin taskXuexin = taskXuexinRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		int school_status = 200;
		int education_status = 200;
		TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		
		WebClient webClient = addcookie(taskXuexin.getCookies());
		String token = GetAuthUtil.getAuth(clientId, clientSecret);
		String xjurl = "https://my.chsi.com.cn/archive/gdjy/xj/show.action";
		String xlurl = "https://my.chsi.com.cn/archive/gdjy/xl/show.action";
		try {
			Page page = getPage(webClient, taskStandalone.getTaskid(), xjurl, HttpMethod.GET, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			Elements elementsByClass = doc.getElementsByClass("xjxx-img");
			for (Element element : elementsByClass) {
				String attr = element.attr("src");
				String imgvalue = getImgvalue(token, webClient, attr);
				XuexinHtml xuexinHtml = new XuexinHtml(taskStandalone.getTaskid(), "xuexin_school_info", "1", attr,
						imgvalue);
				xuexinHtmlRepository.save(xuexinHtml);
				xuexinParser.getXuexinSchoolInfo(imgvalue, taskStandalone.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("getXuexinSchoolInfo---ERROR:", pbccrcJsonBean.getMapping_id() + "---ERROR:" + e);
			school_status = 500;
		}

		try {
			Page page = getPage(webClient, taskStandalone.getTaskid(), xlurl, HttpMethod.GET, null, null, null, null);
			String html = page.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			Elements elementsByClass = doc.getElementsByClass("xjxx-img");
			for (Element element : elementsByClass) {
				String attr = element.attr("src");
				String imgvalue = getImgvalue(token, webClient, attr);
				XuexinHtml xuexinHtml = new XuexinHtml(taskStandalone.getTaskid(), "xuexin_education_info", "1", attr,
						imgvalue);
				xuexinHtmlRepository.save(xuexinHtml);
				xuexinParser.getXuexinEducationInfo(imgvalue, taskStandalone.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("getXuexinSchoolInfo---ERROR:", pbccrcJsonBean.getMapping_id() + "---ERROR:" + e);
			education_status = 500;
		}
		taskStandalone.setPhase(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getPhase());
		taskStandalone.setPhase_status(StandaloneEnum.STANDALONE_CRAWLER_SUCCESS.getPhasestatus());
		taskStandalone.setDescription("数据采集成功");
		taskStandalone.setFinished(true);
		taskStandaloneRepository.save(taskStandalone);
		//保存爬取学籍学历状态信息码
		taskXuexin.setSchool_status(school_status);
		taskXuexin.setEducation_status(education_status);
		taskXuexinRepository.save(taskXuexin);
		return null;

	}

	public String getImgvalue(String token, WebClient webClient, String url) throws Exception {
		try {
			Page page1 = getPage(webClient, null, url, HttpMethod.POST, null, null, "", null);
			InputStream contentAsStream = page1.getWebResponse().getContentAsStream();
			byte[] byt = new byte[contentAsStream.available()];
			contentAsStream.read(byt);
			String imgStr = Base64Util.encode(byt);
			String param = "image=" + URLEncoder.encode(imgStr, "UTF-8");
			String result = HttpUtil.post(otherHost, token, param);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("getImgvalue---ERROR:", url + "---ERROR:" + e);
		}
		return "";

	}

	public WebClient addcookie(String cookieString) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}


	@Async
	@Override
	public String login(PbccrcJsonBean pbccrcJsonBean) {
		 TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		try {
			loginTwo(pbccrcJsonBean,taskStandalone);
		} catch (Exception e) {
			tracer.addTag("XuexinTaskController.login:" , taskStandalone.getTaskid()+"---ERROR:"+e);
			e.printStackTrace();
		}
		return null;
	}

}
