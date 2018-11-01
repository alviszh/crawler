package app.service;

import java.security.Security;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeLogin;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.beijing.beijingcent.HousingBeiJingCenterBasicPayBean;
import com.microservice.dao.entity.crawler.housing.beijing.beijingcent.HousingBeiJingCenterPayBean;
import com.microservice.dao.repository.crawler.housing.beijing.beijingcent.HousingBasicCenterUserBeanRepository;
import com.microservice.dao.repository.crawler.housing.beijing.beijingcent.HousingBeiJingCenterBasicPayBeanRepository;
import com.microservice.dao.repository.crawler.housing.beijing.beijingcent.HousingBeiJingCenterPayBeanRepository;

import app.bean.BeiJingCenterBean;
import app.bean.BeijingCenterPayRootBean;
import app.bean.BeijingCenterUserRootBean;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.HousingBJParse;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.beijing.beijingcent")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.beijing.beijingcent")
public class HousingBeijingCentCrawlerService extends HousingBasicService {

	@Autowired
	private TracerLog tracerLog;

	@Autowired
	private HousingBasicCenterUserBeanRepository housingBasicCenterUserBeanRepository;

	@Autowired
	private HousingBeiJingCenterBasicPayBeanRepository housingBeiJingCenterBasicPayBeanRepository;

	@Autowired
	private HousingBeiJingCenterPayBeanRepository housingBeiJingCenterPayBeanRepository;

	public TaskHousing getBasic(BeiJingCenterBean beiJingCenterBean, WebClient webClient, TaskHousing taskHousing)
			throws Exception {
		String url = "https://grwsyw.bjgjj.gov.cn/ish/flow/command/home/homeurl/CMD_GRXX/"
				+ beiJingCenterBean.get_POOLKEY().trim();

		tracerLog.addTag("getBasic url", url);

		webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/home?_r=20a12aa8cd7944b8c47189146521");
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		Page page = LoginAndGetCommon.getHtml(url, webClient);

		BeijingCenterUserRootBean beijingCenterUserRootBean = HousingBJParse
				.beijingcenter_basic_parse(page.getWebResponse().getContentAsString());

		housingBasicCenterUserBeanRepository.save(beijingCenterUserRootBean.getData());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
		taskHousing.setUserinfoStatus(200);
		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getError_code());
		taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
		return taskHousing;

	}

	public TaskHousing getBasicPay(BeiJingCenterBean beiJingCenterBean, WebClient webClient, TaskHousing taskHousing)
			throws Exception {
		String url = "https://grwsyw.bjgjj.gov.cn/ish/flow/menu/PPLGRZH0101";
		tracerLog.addTag("getBasicPay url", url);

		webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/home?_r=20a12aa8cd7944b8c47189146521");
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		Page page = LoginAndGetCommon.getHtml(url, webClient);

		// System.out.println("============中国============" +

		List<HousingBeiJingCenterBasicPayBean> result = HousingBJParse
				.beijingcenter_basicpay_parse(page.getWebResponse().getContentAsString());

		housingBeiJingCenterBasicPayBeanRepository.saveAll(result);
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
		taskHousing.setUserinfoStatus(200);

		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getError_code());
		taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());

		return taskHousing;

	}

	public TaskHousing getPay(BeiJingCenterBean beiJingCenterBean, WebClient webClient, TaskHousing taskHousing)
			throws Exception {

		LocalDate date = LocalDate.now();

		LocalDate begindate = LocalDate.now().plusYears(-2);

		String url = "https://grwsyw.bjgjj.gov.cn/ish/ydpx/parsepage?" + "ksrq=" + begindate.toString() + "&ywlx="
				+ "&zzrq=" + date.toString() + "&jbqd=" + "&%24page=PPLPageGRZH_0102_01.ydpx" + "&_POOLKEY="
				+ beiJingCenterBean.get_POOLKEY() + "&list_id=ywxxlsb" + "&dataset_id=gjjywxx" + "&list_page_no=1"
				+ "&gjjywxx_pagesize=100" + "&gjjywxx_order_by=1";

		tracerLog.addTag("getPay url", url);

		webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/home?_r=20a12aa8cd7944b8c47189146521");
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		Page page = LoginAndGetCommon.getHtml(url, webClient);

		BeijingCenterPayRootBean beijingCenterPayRootBean = HousingBJParse
				.beijingcenter_payroot_parse(page.getWebResponse().getContentAsString());

		// System.out.println("-=============-" +
		// beijingCenterPayRootBean.getHtml());
		List<HousingBeiJingCenterPayBean> result = HousingBJParse
				.beijingcenter_pay_parse(beijingCenterPayRootBean.getHtml());
		housingBeiJingCenterPayBeanRepository.saveAll(result);
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
		taskHousing.setPaymentStatus(200);

		taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getError_code());
		taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
		return taskHousing;

	}

	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		Security.setProperty("jdk.tls.disabledAlgorithms", "SSLv3, DH keySize < 768");
		WebClient webClient = new WebClient(BrowserVersion.CHROME);

		webClient.getOptions().setUseInsecureSSL(true);

		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setTimeout(20000); // 15->60
		webClient.waitForBackgroundJavaScript(20000); // 5s
		String url = "https://grwsyw.bjgjj.gov.cn/ish/login";
		try {

			webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
			webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
			webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/");
			webClient.addRequestHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
			webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webClient.addRequestHeader("Cache-Control", "max-age=0");
			webClient.addRequestHeader("Connection", "keep-alive");

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("mm", messageLoginForHousing.getPassword()));
			paramsList.add(new NameValuePair("logintype", "card"));
			if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getIDNUM())) {
				// 身份证登录
				tracerLog.addTag("身份证登录", messageLoginForHousing.toString());
				paramsList.add(new NameValuePair("yzfs", "1"));// 1为身份证号登录，2为个人登记号登录，3为军官证登录，4为护照登录，5为联名卡登录

			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getACCOUNT_NUM())) {
				// 根据个人登记号登录
				tracerLog.addTag("个人登记号登录", messageLoginForHousing.toString());
				paramsList.add(new NameValuePair("yzfs", "2"));// 1为身份证号登录，2为个人登记号登录，3为军官证登录，4为护照登录，5为联名卡登录

			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getOFFICER_CARD())) {
				// 根据军官证号登录
				tracerLog.addTag("军官证号登录", messageLoginForHousing.toString());
				paramsList.add(new NameValuePair("yzfs", "3"));// 1为身份证号登录，2为个人登记号登录，3为军官证登录，4为护照登录，5为联名卡登录

			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getPASSPORT())) {
				// 根据护照号登录
				tracerLog.addTag("护照号登录", messageLoginForHousing.toString());
				paramsList.add(new NameValuePair("yzfs", "4"));// 1为身份证号登录，2为个人登记号登录，3为军官证登录，4为护照登录，5为联名卡登录

			} else if (messageLoginForHousing.getLogintype().trim().contains(StatusCodeLogin.getCO_BRANDED_CARD())) {
				// 根据联名卡号登录
				tracerLog.addTag("联名卡号登录", messageLoginForHousing.toString());
				paramsList.add(new NameValuePair("yzfs", "5"));// 1为身份证号登录，2为个人登记号登录，3为军官证登录，4为护照登录，5为联名卡登录

			}
			paramsList.add(new NameValuePair("hm", messageLoginForHousing.getNum()));
			paramsList.add(new NameValuePair("xyjmdzd", "mmMD5".trim()));
			Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			// System.out.println("==html==" +
			// page.getWebResponse().getContentAsString());

			String html = page.getWebResponse().getContentAsString();

			tracerLog.addTag("login html", html);

			if (page.getWebResponse().getStatusCode() != 200) {
				Document doc = Jsoup.parse(html);

				String errorinfo = doc.select("div.info").text();

				taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription(errorinfo);

				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setError_message(errorinfo);
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
			}
			

			taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

			String cookieString = CommonUnit.transcookieToJson(page.getEnclosingWindow().getWebClient());
			taskHousing.setCookies(cookieString);
			save(taskHousing);
			// String rgex = "poolSelect = (.*?)\\;";
			// Pattern pattern = Pattern.compile(rgex);// 匹配的模式
			// Matcher m = pattern.matcher(html);
			// String value = "";
			// while (m.find()) {
			// value = m.group(1);
			// }
			//
			// BeiJingCenterBean beiJingCenterBean =
			// HousingBJParse.beijingcenter_need_parse(value);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return taskHousing;
	}

	public static void main(String[] args) {
		LocalDate date = LocalDate.now();

		System.out.println(date.toString());
	}
}
