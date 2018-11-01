package app.service;

import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouBaseInfo;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouHtml;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.dezhou.InsuranceDeZhouYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.dezhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.dezhou" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceDeZhouHtmlRepository insuranceDeZhouHtmlRepository;

	@Autowired
	private InsuranceDeZhouBaseInfoRepository insuranceDeZhouBaseInfoRepository;

	@Autowired
	private InsuranceDeZhouYanglaoInfoRepository insuranceDeZhouYanglaoInfoRepository;

	@Autowired
	private InsuranceDeZhouYibaoInfoRepository insuranceDeZhouYibaoInfoRepository;

	@Autowired
	private InsuranceDeZhouShiyeInfoRepository insuranceDeZhouShiyeInfoRepository;

	@Autowired
	private InsuranceDeZhouShengYuInfoRepository insuranceDeZhouShengYuInfoRepository;

	@Autowired
	private InsuranceDeZhouGongShangInfoRepository insuranceDeGongShangYuInfoRepository;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;

	/**
	 * 爬取解析基本信息
	 * 
	 * @param parameter
	 * @param cookies
	 * @param pid
	 * @return
	 */
	public String crawlerBaseInfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析基本信息", parameter.toString());

		// 基本信息界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String loginurl = "http://222.133.0.220:9988/hso/hsoPer.do?method=QueryPersonBaseInfo&__logon_ticket=null";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceDeZhouHtml insuranceDeZhouHtml = new InsuranceDeZhouHtml();
			insuranceDeZhouHtml.setHtml(contentAsString);
			insuranceDeZhouHtml.setPageCount(1);
			insuranceDeZhouHtml.setTaskid(parameter.getTaskId());
			insuranceDeZhouHtml.setType("1");
			insuranceDeZhouHtml.setUrl(loginurl);
			insuranceDeZhouHtmlRepository.save(insuranceDeZhouHtml);

			Document doc = Jsoup.parse(contentAsString);
			Element elementById = doc.getElementById("xm");
			if (null == elementById && "".equals(elementById)) {
				System.out.println("获取基本信息数据失败！");
			} else {
				System.out.println("获取基本信息数据成功！");
				// 姓名
				String name = elementById.val();
				System.out.println(name);
				// 身份证
				String cardId = doc.getElementById("sfzhm").val();
				System.out.println(cardId);
				// 性别
				String sex = doc.getElementById("xbmc").val();
				System.out.println(sex);
				// 出生日期
				String birthDate = doc.getElementById("csrq").val();
				System.out.println(birthDate);
				// 户口性质
				String hkxz = doc.getElementById("hkxzmc").val();
				System.out.println(hkxz);
				// 文化程度
				String culture = doc.getElementById("whcdmc").val();
				System.out.println(culture);
				// 婚姻状况
				String maritalstatus = doc.getElementById("hyzkmc").val();
				System.out.println(maritalstatus);
				// 通讯地址
				String communicationaddr = doc.getElementById("txdz").val();
				System.out.println(communicationaddr);
				// 家庭住址
				String homeaddr = doc.getElementById("jtzz").val();
				System.out.println(homeaddr);
				// 民族
				String nation = doc.getElementById("mzmc").val();
				System.out.println(nation);
				// 联系人
				String linkman = doc.getElementById("lxr").val();
				System.out.println(linkman);
				// 联系电话
				String callphone = doc.getElementById("lxdh").val();
				System.out.println(callphone);
				// 行政职务
				String administrative = doc.getElementById("xzzwmc").val();
				System.out.println(administrative);
				// 邮政编码
				String postalservice = doc.getElementById("yzbm").val();
				System.out.println(postalservice);
				// 户口所在地
				String permanentaddr = doc.getElementById("hkszd").val();
				System.out.println(permanentaddr);

				InsuranceDeZhouBaseInfo insuranceDeZhouBaseInfo = new InsuranceDeZhouBaseInfo();
				insuranceDeZhouBaseInfo.setTaskid(parameter.getTaskId());
				insuranceDeZhouBaseInfo.setName(name);
				insuranceDeZhouBaseInfo.setCardId(cardId);
				insuranceDeZhouBaseInfo.setSex(sex);
				insuranceDeZhouBaseInfo.setBirthDate(birthDate);
				insuranceDeZhouBaseInfo.setHkxz(hkxz);
				insuranceDeZhouBaseInfo.setCulture(culture);
				insuranceDeZhouBaseInfo.setMaritalstatus(maritalstatus);
				insuranceDeZhouBaseInfo.setCommunicationaddr(communicationaddr);
				insuranceDeZhouBaseInfo.setHomeaddr(homeaddr);
				insuranceDeZhouBaseInfo.setNation(nation);
				insuranceDeZhouBaseInfo.setLinkman(linkman);
				insuranceDeZhouBaseInfo.setCallphone(callphone);
				insuranceDeZhouBaseInfo.setAdministrative(administrative);
				insuranceDeZhouBaseInfo.setPostalservice(postalservice);
				insuranceDeZhouBaseInfo.setPermanentaddr(permanentaddr);
				insuranceDeZhouBaseInfoRepository.save(insuranceDeZhouBaseInfo);

				// 更新Task表为 基本信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhasestatus());
				taskInsurance.setUserInfoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			System.out.println("获取基本信息数据异常！");
		}
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:基本信息爬取解析完成", "");
		return "";
	}

	/**
	 * 爬取解析养老保险
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public String crawlerAgedInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance, String year) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析养老保险", parameter.toString());
		// 养老保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String loginurl = "http://222.133.0.220:9988/hso/persi.do?method=queryZgYanglaozh&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""+year+"\"/></p>&__logon_ticket=null";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取养老数据成功！");

				InsuranceDeZhouHtml insuranceDeZhouHtml = new InsuranceDeZhouHtml();
				insuranceDeZhouHtml.setHtml(contentAsString);
				insuranceDeZhouHtml.setPageCount(1);
				insuranceDeZhouHtml.setTaskid(parameter.getTaskId());
				insuranceDeZhouHtml.setType("1");
				insuranceDeZhouHtml.setUrl(loginurl);
				insuranceDeZhouHtmlRepository.save(insuranceDeZhouHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位名称
					String companyName = inputs.get(i + 1).val();
					System.out.println(companyName);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personalPay = inputs.get(i + 4).val();
					System.out.println(personalPay);

					InsuranceDeZhouYanglaoInfo insuranceDeZhouYanglaoInfo = new InsuranceDeZhouYanglaoInfo();
					insuranceDeZhouYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceDeZhouYanglaoInfo.setYearMonth(yearMonth);
					insuranceDeZhouYanglaoInfo.setCompanyName(companyName);
					insuranceDeZhouYanglaoInfo.setPay_cardinal(pay_cardinal);
					insuranceDeZhouYanglaoInfo.setCompanyPay(companyPay);
					insuranceDeZhouYanglaoInfo.setPersonPay(personalPay);
					insuranceDeZhouYanglaoInfoRepository.save(insuranceDeZhouYanglaoInfo);

				}

				// 更新Task表为 养老信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhasestatus());
				taskInsurance.setYanglaoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取养老数据失败或没有数据！");
			}

		} catch (Exception e) {
			System.out.println("获取养老保险信息数据异常！");
		}
		return "";
	}

	/**
	 * 爬取解析医疗保险
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public String crawlerMedicalInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			String year) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析医疗保险", parameter.toString());
		// 医疗保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String loginurl = "http://222.133.0.220:9988/hso/persi.do?method=queryMediAccount&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""+year+"\"/></p>&__logon_ticket=null";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取医疗数据成功！");

				InsuranceDeZhouHtml insuranceDeZhouHtml = new InsuranceDeZhouHtml();
				insuranceDeZhouHtml.setHtml(contentAsString);
				insuranceDeZhouHtml.setPageCount(1);
				insuranceDeZhouHtml.setTaskid(parameter.getTaskId());
				insuranceDeZhouHtml.setType("1");
				insuranceDeZhouHtml.setUrl(loginurl);
				insuranceDeZhouHtmlRepository.save(insuranceDeZhouHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 单位名称
					String companyName = inputs.get(i).val();
					System.out.println(companyName);
					// 缴费年月
					String yearMonth = inputs.get(i + 1).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 个人缴费额
					String personalPay = inputs.get(i + 3).val();
					System.out.println(personalPay);
					// 单位缴费额
					String companyPay = inputs.get(i + 4).val();
					System.out.println(companyPay);
					InsuranceDeZhouYibaoInfo insuranceDeZhouYibaoInfo = new InsuranceDeZhouYibaoInfo();
					insuranceDeZhouYibaoInfo.setTaskid(parameter.getTaskId());
					insuranceDeZhouYibaoInfo.setCompanyName(companyName);
					insuranceDeZhouYibaoInfo.setYearMonth(yearMonth);
					insuranceDeZhouYibaoInfo.setPay_cardinal(pay_cardinal);
					insuranceDeZhouYibaoInfo.setPersonPay(personalPay);
					insuranceDeZhouYibaoInfo.setCompanyPay(companyPay);
					insuranceDeZhouYibaoInfoRepository.save(insuranceDeZhouYibaoInfo);
				}

				// 更新Task表为 医疗信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhasestatus());
				taskInsurance.setYiliaoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取医疗数据失败或没有数据！");
			}

		} catch (Exception e) {
			System.out.println("获取医疗保险信息数据异常！");
		}
		return "";
	}

	/**
	 * 爬取解析失业保险
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public String crawlerUnemploymentInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			String year) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析失业保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String loginurl = "http://222.133.0.220:9988/hso/persi.do?method=queryZgShiyejf&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""+year+"\"/></p>&__logon_ticket=null";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取失业数据成功！");

				InsuranceDeZhouHtml insuranceDeZhouHtml = new InsuranceDeZhouHtml();
				insuranceDeZhouHtml.setHtml(contentAsString);
				insuranceDeZhouHtml.setPageCount(1);
				insuranceDeZhouHtml.setTaskid(parameter.getTaskId());
				insuranceDeZhouHtml.setType("1");
				insuranceDeZhouHtml.setUrl(loginurl);
				insuranceDeZhouHtmlRepository.save(insuranceDeZhouHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位名称
					String companyName = inputs.get(i + 1).val();
					System.out.println(companyName);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personalPay = inputs.get(i + 4).val();
					System.out.println(personalPay);
					InsuranceDeZhouShiyeInfo insuranceDeZhouShiyeInfo = new InsuranceDeZhouShiyeInfo();
					insuranceDeZhouShiyeInfo.setTaskid(parameter.getTaskId());
					insuranceDeZhouShiyeInfo.setYearMonth(yearMonth);
					insuranceDeZhouShiyeInfo.setCompanyName(companyName);
					insuranceDeZhouShiyeInfo.setPay_cardinal(pay_cardinal);
					insuranceDeZhouShiyeInfo.setPersonPay(personalPay);
					insuranceDeZhouShiyeInfo.setCompanyPay(companyPay);
					insuranceDeZhouShiyeInfoRepository.save(insuranceDeZhouShiyeInfo);

				}

				// 更新Task表为 失业信息数据爬取成功
				taskInsurance
						.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhase());
				taskInsurance
						.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhasestatus());
				taskInsurance.setShiyeStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取失业数据失败或没有数据！");
			}

		} catch (Exception e) {
			System.out.println("获取失业保险信息数据异常！");
		}
		return "";
	}

	/**
	 * 爬取解析生育保险
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public String crawlerShengyuInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			String year) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析生育保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			String loginurl = "http://222.133.0.220:9988/hso/persi.do?method=queryZgShengyujf&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""+year+"\"/></p>&__logon_ticket=null";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取生育数据成功！");

				InsuranceDeZhouHtml insuranceDeZhouHtml = new InsuranceDeZhouHtml();
				insuranceDeZhouHtml.setHtml(contentAsString);
				insuranceDeZhouHtml.setPageCount(1);
				insuranceDeZhouHtml.setTaskid(parameter.getTaskId());
				insuranceDeZhouHtml.setType("1");
				insuranceDeZhouHtml.setUrl(loginurl);
				insuranceDeZhouHtmlRepository.save(insuranceDeZhouHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 4) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位名称
					String companyName = inputs.get(i + 1).val();
					System.out.println(companyName);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					InsuranceDeZhouShengYuInfo insuranceDeZhouShengYuInfo = new InsuranceDeZhouShengYuInfo();
					insuranceDeZhouShengYuInfo.setTaskid(parameter.getTaskId());
					insuranceDeZhouShengYuInfo.setYearMonth(yearMonth);
					insuranceDeZhouShengYuInfo.setPay_cardinal(pay_cardinal);
					insuranceDeZhouShengYuInfo.setCompanyName(companyName);
					insuranceDeZhouShengYuInfo.setCompanyPay(companyPay);
					insuranceDeZhouShengYuInfoRepository.save(insuranceDeZhouShengYuInfo);
				}

				// 更新Task表为 生育信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhasestatus());
				taskInsurance.setShengyuStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取生育数据失败或没有数据！");
			}

		} catch (Exception e) {
			System.out.println("获取生育保险信息数据异常！");
		}

		return "";
	}

	/**
	 * 爬取解析工伤保险
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	public String crawlerGongshangInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			String year) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析工伤保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			String loginurl = "http://222.133.0.220:9988/hso/persi.do?method=queryZgGsjf&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""+year+"\"/></p>&__logon_ticket=null";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取工伤数据成功！");

				InsuranceDeZhouHtml insuranceDeZhouHtml = new InsuranceDeZhouHtml();
				insuranceDeZhouHtml.setHtml(contentAsString);
				insuranceDeZhouHtml.setPageCount(1);
				insuranceDeZhouHtml.setTaskid(parameter.getTaskId());
				insuranceDeZhouHtml.setType("1");
				insuranceDeZhouHtml.setUrl(loginurl);
				insuranceDeZhouHtmlRepository.save(insuranceDeZhouHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 4) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位名称
					String companyName = inputs.get(i + 1).val();
					System.out.println(companyName);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					InsuranceDeZhouGongShangInfo insuranceDeZhouGongShangInfo = new InsuranceDeZhouGongShangInfo();
					insuranceDeZhouGongShangInfo.setTaskid(parameter.getTaskId());
					insuranceDeZhouGongShangInfo.setYearMonth(yearMonth);
					insuranceDeZhouGongShangInfo.setCompanyName(companyName);
					insuranceDeZhouGongShangInfo.setPay_cardinal(pay_cardinal);
					insuranceDeZhouGongShangInfo.setCompanyPay(companyPay);
					insuranceDeGongShangYuInfoRepository.save(insuranceDeZhouGongShangInfo);
				}

				// 更新Task表为 工伤信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhasestatus());
				taskInsurance.setGongshangStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取工伤数据失败或没有数据！");
			}

		} catch (Exception e) {
			System.out.println("获取工伤保险信息数据异常！");
		}

		return "";
	}
}
