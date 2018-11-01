package app.service;

import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yichang.HousingYichangHtml;
import com.microservice.dao.entity.crawler.housing.yichang.HousingYichangPay;
import com.microservice.dao.entity.crawler.housing.yichang.HousingYichangUserInfo;
import com.microservice.dao.repository.crawler.housing.yichang.HousingYichangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yichang.HousingYichangPayRepository;
import com.microservice.dao.repository.crawler.housing.yichang.HousingYichangUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HousingYichangParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.util.EncryptionUtils;
import app.util.JsEncryption;
import net.sf.json.JSONObject;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yichang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yichang")
public class HousingYichangService extends HousingBasicService implements ICrawlerLogin{

	@Autowired
	private HousingYichangParse housingYichangParse;

	@Autowired
	private HousingYichangUserInfoRepository housingYichangUserInfoRepository;
	@Autowired
	private HousingYichangPayRepository housingYichangPayRepository;
	@Autowired
	private HousingYichangHtmlRepository housingYichangHtmlRepository;
	

	@Autowired
	private TracerLog tracer;
	
	@Value("${datasource.key}")
	String key;
	
	private String url = "http://61.136.223.44/web2/src/personal/Service.asmx/smcxLoginNew";

	/**
	 * 登录
	 * 
	 * @param messageLoginForHousing
	 * @param taskHousing
	 * @return
	 * @throws Exception
	 */
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("HousingYichangFutureService.login", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		JSONObject jsonObject = JSONObject.fromObject(messageLoginForHousing);
		try {
			String loginUrl = "http://61.136.223.44/Service.asmx/LoginCheck";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String username = messageLoginForHousing.getNum();
			String password = messageLoginForHousing.getPassword();
			password = password.replace("+", "%2B");
			password = password.replace("&", "%26");
			String nLoginPWD = EncryptionUtils.StringToMd5("smejweb" + username.toUpperCase() + "dzzw321")
					.toUpperCase();
			String pwd = EncryptionUtils.StringToMd5(username + password).toUpperCase();
			String nSfzhm = "";
			try {
				nSfzhm = JsEncryption.encrypted(username, key);
				nSfzhm = EncryptionUtils.Base64(nSfzhm);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("nLoginID", "smejweb"));
			paramsList.add(new NameValuePair("nLoginPWD", nLoginPWD));
			paramsList.add(new NameValuePair("nSfzhm", nSfzhm));
			paramsList.add(new NameValuePair("pwd", pwd));
			paramsList.add(new NameValuePair("phoneNO", ""));
			paramsList.add(new NameValuePair("VerifiCode", ""));
			paramsList.add(new NameValuePair("system", ""));
			paramsList.add(new NameValuePair("versions", ""));
			Page page = getPage(webClient, loginUrl, HttpMethod.POST, paramsList, null, null, null);
			String xmlStr = page.getWebResponse().getContentAsString();
			xmlStr = xmlStr.toLowerCase();
			Document doc = Jsoup.parse(xmlStr);
			String msg = doc.getElementsByTag("Error").text();
			if (msg.contains("登录验证成功")) {
				tracer.addTag("登录验证成功:", msg);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
				// String cookies = CommonUnit.transcookieToJson(webClient);
				// 保存登录成功信息获取token
				taskHousing.setCookies(xmlStr);
				taskHousing.setCrawlerHost(nSfzhm);
				taskHousing.setLoginMessageJson(jsonObject.toString());
				save(taskHousing);
				return taskHousing;
			} else if (StringUtils.isNotBlank(msg)) {
				tracer.addTag("登录失败:", msg);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(msg);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				save(taskHousing);
				return taskHousing;
			} else {
				tracer.addTag("HousingYichangFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
				taskHousing.setLoginMessageJson(jsonObject.toString());
				// 登录失败状态存储
				save(taskHousing);
				return taskHousing;
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingYichangFutureService.login", messageLoginForHousing.getTask_id() + "登录页获取超时！");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
			taskHousing.setLoginMessageJson(jsonObject.toString());
			// 登录失败状态存储
			save(taskHousing);
		}
		return taskHousing;
	}

	/**
	 * 用户信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public void getUserInfo(TaskHousing taskHousing,String inType) {
		tracer.addTag("HousingYichangFutureService.getUserInfo", taskHousing.getTaskid());
		try {
			
			String html = getdata(taskHousing, inType);
			// 网页信息
			HousingYichangHtml housingYichangHtml = new HousingYichangHtml(taskHousing.getTaskid(), "公积金用户信息",
					inType, url, html);
			housingYichangHtmlRepository.save(housingYichangHtml);

			HousingYichangUserInfo housingYichangUserInfo = housingYichangParse.getHousingUserInfo(taskHousing.getTaskid(), html);
			//医疗险标注公积金用户信息
			if (housingYichangUserInfo != null) {
				housingYichangUserInfoRepository.save(housingYichangUserInfo);
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				tracer.addTag("HousingYichangFutureService.getUserInfo---用户信息", "用户信息已入库!" + taskHousing.getTaskid());
			} else {
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingYichangFutureService.getUserInfo is null", taskHousing.getTaskid());
			}
			
		} catch (Exception e) {
			tracer.addTag("HousingYichangFutureService.getUserInfo---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			e.printStackTrace();
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	/**
	 * 缴存记录查询
	 * @param taskHousing
	 * @param inType
	 */
	public void getPayRecord(TaskHousing taskHousing,String inType) {
		tracer.addTag("HousingYichangFutureService.getPayRecord", taskHousing.getTaskid());
		try {
			String html = getdata(taskHousing, inType);
			// 网页信息
			HousingYichangHtml housingYichangHtml = new HousingYichangHtml(taskHousing.getTaskid(), "公积金明细",
					inType, url, html);
			housingYichangHtmlRepository.save(housingYichangHtml);

			List<HousingYichangPay> list = housingYichangParse.getHousingPay(taskHousing.getTaskid(), html);
			//工伤险标注公积金明细
			if (list != null) {
				housingYichangPayRepository.saveAll(list);
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				tracer.addTag("HousingYichangFutureService.getPayRecord---缴存记录查询",  taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 404,
						taskHousing.getTaskid());
				tracer.addTag("HousingYichangFutureService.getPayRecord is null", taskHousing.getTaskid());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 500,
					taskHousing.getTaskid());
			tracer.addTag("HousingYichangFutureService.getPayRecord---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		updateTaskHousing(taskHousing.getTaskid());
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
	public Page getPage(WebClient webClient, String url, HttpMethod type, List<NameValuePair> paramsList, String code,
			String body, Map<String, String> map) throws Exception {
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
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}
	
	
	/**
	 * 获取返回数据
	 * @param taskHousing
	 * @param inType
	 * @return
	 */
	public String getdata(TaskHousing taskHousing, String inType) {

		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();

			tracer.addTag("HousingYichangFutureService.getdata-inType"+inType, taskHousing.getTaskid());

			Document doc = Jsoup.parse(taskHousing.getCookies());
			Elements tdValue = doc.getElementsByTag("token");
			String token = tdValue.get(0).text().toUpperCase();
			String sfzhm = taskHousing.getCrawlerHost();
			String time = EncryptionUtils.Base64(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			String loginPWD = EncryptionUtils.StringToMd5(token + time).toUpperCase();

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			// 公积金用户信息 201
			// 公积金明细 202
			// 社保用户信息 101
			// 社保流水 102
			paramsList.add(new NameValuePair("inType", inType));
			paramsList.add(new NameValuePair("BillTime", ""));
			paramsList.add(new NameValuePair("carNO", ""));
			paramsList.add(new NameValuePair("carUnint", ""));
			paramsList.add(new NameValuePair("carVCode", ""));
			paramsList.add(new NameValuePair("loginPhone", ""));
			paramsList.add(new NameValuePair("NOStart", "1"));
			paramsList.add(new NameValuePair("NOEnd", "100000"));
			paramsList.add(new NameValuePair("loginID", "smejweb"));
			paramsList.add(new NameValuePair("loginPWD", loginPWD));
			paramsList.add(new NameValuePair("sfzhm", sfzhm));
			paramsList.add(new NameValuePair("time", time));
			paramsList.add(new NameValuePair("loginType", "1"));

			Page page = getPage(webClient, url, HttpMethod.POST, paramsList, null, null, null);
			String html = page.getWebResponse().getContentAsString();

			return html;
		} catch (Exception e) {
			tracer.addTag("HousingYichangFutureService.getdata-inType"+inType, e.getMessage());
		}
		return null;
	}

	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			// 获取个人信息
			getUserInfo(taskHousing, "201");
			// 缴存记录查询
			getPayRecord(taskHousing, "202");
		} catch (Exception e) {
			tracer.addTag("HousingYichangFutureService.getAllData", e.getMessage());
		}
		return null;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}