package app.service;

import java.net.URL;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangBaseInfo;
import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangHtml;
import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.zaozhuang.InsuranceZaoZhuangYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zaozhuang.InsuranceZaoZhuangBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zaozhuang.InsuranceZaoZhuangGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zaozhuang.InsuranceZaoZhuangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.zaozhuang.InsuranceZaoZhuangShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zaozhuang.InsuranceZaoZhuangShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zaozhuang.InsuranceZaoZhuangYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zaozhuang.InsuranceZaoZhuangYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.zaozhuang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.zaozhuang" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceZaoZhuangHtmlRepository insuranceZaoZhuangHtmlRepository;

	@Autowired
	private InsuranceZaoZhuangBaseInfoRepository insuranceZaoZhuangBaseInfoRepository;

	@Autowired
	private InsuranceZaoZhuangYanglaoInfoRepository insuranceZaoZhuangYanglaoInfoRepository;

	@Autowired
	private InsuranceZaoZhuangYibaoInfoRepository insuranceZaoZhuangYibaoInfoRepository;

	@Autowired
	private InsuranceZaoZhuangShiyeInfoRepository insuranceZaoZhuangShiyeInfoRepository;

	@Autowired
	private InsuranceZaoZhuangShengYuInfoRepository insuranceZaoZhuangShengYuInfoRepository;

	@Autowired
	private InsuranceZaoZhuangGongShangInfoRepository insuranceZaoZhuangGongShangInfoRepository;

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
			String loginurl = "http://218.56.155.46:8081/hso/hsoPer.do?method=QueryPersonBaseInfo";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceZaoZhuangHtml insuranceZaoZhuangHtml = new InsuranceZaoZhuangHtml();
			insuranceZaoZhuangHtml.setHtml(contentAsString);
			insuranceZaoZhuangHtml.setPageCount(1);
			insuranceZaoZhuangHtml.setTaskid(parameter.getTaskId());
			insuranceZaoZhuangHtml.setType("1");
			insuranceZaoZhuangHtml.setUrl(loginurl);
			insuranceZaoZhuangHtmlRepository.save(insuranceZaoZhuangHtml);

			Document doc = Jsoup.parse(contentAsString);
			Elements elementsByAttributeValue = doc.getElementsByAttributeValue("type", "text");

			InsuranceZaoZhuangBaseInfo insuranceZaoZhuangBaseInfo = new InsuranceZaoZhuangBaseInfo();
			insuranceZaoZhuangBaseInfo.setTaskid(parameter.getTaskId());
			// 身份证号
			String cardId = elementsByAttributeValue.get(0).val();
			System.out.println(cardId);
			insuranceZaoZhuangBaseInfo.setCardId(cardId);
			// 姓名
			String name = elementsByAttributeValue.get(1).val();
			System.out.println(name);
			insuranceZaoZhuangBaseInfo.setName(name);
			// 性别
			String sex = elementsByAttributeValue.get(2).val();
			System.out.println(sex);
			insuranceZaoZhuangBaseInfo.setSex(sex);
			// 出生日期
			String birthDate = elementsByAttributeValue.get(3).val();
			System.out.println(birthDate);
			insuranceZaoZhuangBaseInfo.setBirthDate(birthDate);
			// 户口性质
			String hkxz = elementsByAttributeValue.get(4).val();
			System.out.println(hkxz);
			insuranceZaoZhuangBaseInfo.setHkxz(hkxz);
			// 文化程度
			String culture = elementsByAttributeValue.get(5).val();
			System.out.println(culture);
			insuranceZaoZhuangBaseInfo.setCulture(culture);
			// 通讯地址
			String communicationaddr = elementsByAttributeValue.get(6).val();
			System.out.println(communicationaddr);
			insuranceZaoZhuangBaseInfo.setCommunicationaddr(communicationaddr);
			// 家庭住址
			String homeaddr = elementsByAttributeValue.get(7).val();
			System.out.println(homeaddr);
			insuranceZaoZhuangBaseInfo.setHomeaddr(homeaddr);
			// 民族
			String nation = elementsByAttributeValue.get(8).val();
			System.out.println(nation);
			insuranceZaoZhuangBaseInfo.setNation(nation);
			// 固定电话
			String registerphone = elementsByAttributeValue.get(9).val();
			System.out.println(registerphone);
			insuranceZaoZhuangBaseInfo.setRegisterphone(registerphone);
			// 邮政编码
			String postalservice = elementsByAttributeValue.get(10).val();
			System.out.println(postalservice);
			insuranceZaoZhuangBaseInfo.setPostalservice(postalservice);
			// 手机号码
			String phone = elementsByAttributeValue.get(11).val();
			System.out.println(phone);
			insuranceZaoZhuangBaseInfo.setPhone(phone);
			// 姓名2
			String xingming = elementsByAttributeValue.get(12).val();
			System.out.println(xingming);
			insuranceZaoZhuangBaseInfo.setXingming(xingming);
			// 身份证号2
			String sfzh = elementsByAttributeValue.get(13).val();
			System.out.println(sfzh);
			insuranceZaoZhuangBaseInfo.setSfzh(sfzh);
			// 性别2
			String xingbie = elementsByAttributeValue.get(14).val();
			System.out.println(xingbie);
			insuranceZaoZhuangBaseInfo.setXingbie(xingbie);
			// 联系电话
			String lxdh = elementsByAttributeValue.get(15).val();
			System.out.println(lxdh);
			insuranceZaoZhuangBaseInfo.setLxdh(lxdh);

			insuranceZaoZhuangBaseInfoRepository.save(insuranceZaoZhuangBaseInfo);

			// 更新Task表为 基本信息数据爬取成功
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhasestatus());
			taskInsurance.setUserInfoStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
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
			String loginurl = "http://218.56.155.46:8081/hso/persi.do?method=queryZgYanglaozh&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""
					+ year + "\"/></p>";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			InsuranceZaoZhuangHtml insuranceZaoZhuangHtml = new InsuranceZaoZhuangHtml();
			insuranceZaoZhuangHtml.setHtml(contentAsString);
			insuranceZaoZhuangHtml.setPageCount(1);
			insuranceZaoZhuangHtml.setTaskid(parameter.getTaskId());
			insuranceZaoZhuangHtml.setType("1");
			insuranceZaoZhuangHtml.setUrl(loginurl);
			insuranceZaoZhuangHtmlRepository.save(insuranceZaoZhuangHtml);

			Document doc = Jsoup.parse(contentAsString);
			Elements tables = doc.getElementsByClass("dataTable");
			for (int j = 1; j < tables.size(); j++) {
				Elements inputs = tables.get(j).getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位名称
					String company = inputs.get(i + 1).val();
					System.out.println(company);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personPay = inputs.get(i + 4).val();
					System.out.println(personPay);
					InsuranceZaoZhuangYanglaoInfo insuranceZaoZhuangYanglaoInfo = new InsuranceZaoZhuangYanglaoInfo();
					insuranceZaoZhuangYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceZaoZhuangYanglaoInfo.setYearMonth(yearMonth);
					insuranceZaoZhuangYanglaoInfo.setCompany(company);
					insuranceZaoZhuangYanglaoInfo.setPay_cardinal(pay_cardinal);
					insuranceZaoZhuangYanglaoInfo.setCompanyPay(companyPay);
					insuranceZaoZhuangYanglaoInfo.setPersonPay(personPay);
					insuranceZaoZhuangYanglaoInfoRepository.save(insuranceZaoZhuangYanglaoInfo);
				}
			}

			// 更新Task表为 养老信息数据爬取成功
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhasestatus());
			taskInsurance.setYanglaoStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);

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
			String loginurl = "http://218.56.155.46:8081/hso/persi.do?method=queryZgMediPay&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""
					+ year + "\"/></p>";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取医疗数据成功！");

				InsuranceZaoZhuangHtml insuranceZaoZhuangHtml = new InsuranceZaoZhuangHtml();
				insuranceZaoZhuangHtml.setHtml(contentAsString);
				insuranceZaoZhuangHtml.setPageCount(1);
				insuranceZaoZhuangHtml.setTaskid(parameter.getTaskId());
				insuranceZaoZhuangHtml.setType("1");
				insuranceZaoZhuangHtml.setUrl(loginurl);
				insuranceZaoZhuangHtmlRepository.save(insuranceZaoZhuangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements tables = doc.getElementsByClass("dataTable");
				for (int j = 0; j < tables.size(); j++) {
					Elements inputs = tables.get(j).getElementsByAttributeValue("type", "text");
					for (int i = 0; i < inputs.size(); i += 7) {
						// 缴费年月
						String yearMonth = inputs.get(i).val();
						System.out.println(yearMonth);
						// 单位名称
						String company = inputs.get(i + 1).val();
						System.out.println(company);
						// 险种标志
						String insurance = inputs.get(i + 2).val();
						System.out.println(insurance);
						// 单位缴费基数
						String company_pay_cardinal = inputs.get(i + 3).val();
						System.out.println(company_pay_cardinal);
						// 单位缴费额
						String companyPay = inputs.get(i + 4).val();
						System.out.println(companyPay);
						// 个人缴费基数
						String person_pay_cardinal = inputs.get(i + 5).val();
						System.out.println(person_pay_cardinal);
						// 个人缴费额
						String personPay = inputs.get(i + 6).val();
						System.out.println(personPay);

						InsuranceZaoZhuangYibaoInfo insuranceZaoZhuangYibaoInfo = new InsuranceZaoZhuangYibaoInfo();
						insuranceZaoZhuangYibaoInfo.setTaskid(parameter.getTaskId());
						insuranceZaoZhuangYibaoInfo.setYearMonth(yearMonth);
						insuranceZaoZhuangYibaoInfo.setCompany(company);
						insuranceZaoZhuangYibaoInfo.setInsurance(insurance);
						insuranceZaoZhuangYibaoInfo.setCompany_pay_cardinal(company_pay_cardinal);
						insuranceZaoZhuangYibaoInfo.setCompanyPay(companyPay);
						insuranceZaoZhuangYibaoInfo.setPerson_pay_cardinal(person_pay_cardinal);
						insuranceZaoZhuangYibaoInfo.setPersonPay(personPay);
						insuranceZaoZhuangYibaoInfoRepository.save(insuranceZaoZhuangYibaoInfo);
					}
				}
				// 更新Task表为 医疗信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhasestatus());
				taskInsurance.setYiliaoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取医疗数据失败或没有数据！");
				// 更新Task表为 医疗信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhasestatus());
				taskInsurance.setYiliaoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
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
			String loginurl = "http://218.56.155.46:8081/hso/persi.do?method=queryZgShiyejf&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""
					+ year + "\"/></p>";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取失业数据成功！");

				InsuranceZaoZhuangHtml insuranceZaoZhuangHtml = new InsuranceZaoZhuangHtml();
				insuranceZaoZhuangHtml.setHtml(contentAsString);
				insuranceZaoZhuangHtml.setPageCount(1);
				insuranceZaoZhuangHtml.setTaskid(parameter.getTaskId());
				insuranceZaoZhuangHtml.setType("1");
				insuranceZaoZhuangHtml.setUrl(loginurl);
				insuranceZaoZhuangHtmlRepository.save(insuranceZaoZhuangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements tables = doc.getElementsByClass("dataTable");
				for (int j = 0; j < tables.size(); j++) {
					Elements inputs = tables.get(j).getElementsByAttributeValue("type", "text");
					for (int i = 0; i < inputs.size(); i += 5) {
						// 缴费年月
						String yearMonth = inputs.get(i).val();
						System.out.println(yearMonth);
						// 单位名称
						String company = inputs.get(i + 1).val();
						System.out.println(company);
						// 缴费基数
						String pay_cardinal = inputs.get(i + 2).val();
						System.out.println(pay_cardinal);
						// 单位缴费额
						String companyPay = inputs.get(i + 3).val();
						System.out.println(companyPay);
						// 个人缴费额
						String personPay = inputs.get(i + 4).val();
						System.out.println(personPay);
						InsuranceZaoZhuangShiyeInfo insuranceZaoZhuangShiyeInfo = new InsuranceZaoZhuangShiyeInfo();
						insuranceZaoZhuangShiyeInfo.setTaskid(parameter.getTaskId());
						insuranceZaoZhuangShiyeInfo.setYearMonth(yearMonth);
						insuranceZaoZhuangShiyeInfo.setCompany(company);
						insuranceZaoZhuangShiyeInfo.setPay_cardinal(pay_cardinal);
						insuranceZaoZhuangShiyeInfo.setCompanyPay(companyPay);
						insuranceZaoZhuangShiyeInfo.setPersonPay(personPay);
						insuranceZaoZhuangShiyeInfoRepository.save(insuranceZaoZhuangShiyeInfo);

					}
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
				// 更新Task表为 失业信息数据爬取成功
				taskInsurance
				.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhase());
				taskInsurance
				.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhasestatus());
				taskInsurance.setShiyeStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
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
			String loginurl = "http://218.56.155.46:8081/hso/persi.do?method=queryZgShengyujf&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""
					+ year + "\"/></p>";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取生育数据成功！");

				InsuranceZaoZhuangHtml insuranceZaoZhuangHtml = new InsuranceZaoZhuangHtml();
				insuranceZaoZhuangHtml.setHtml(contentAsString);
				insuranceZaoZhuangHtml.setPageCount(1);
				insuranceZaoZhuangHtml.setTaskid(parameter.getTaskId());
				insuranceZaoZhuangHtml.setType("1");
				insuranceZaoZhuangHtml.setUrl(loginurl);
				insuranceZaoZhuangHtmlRepository.save(insuranceZaoZhuangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements tables = doc.getElementsByClass("dataTable");
				for (int j = 0; j < tables.size(); j++) {
					Elements inputs = tables.get(j).getElementsByAttributeValue("type", "text");
					for (int i = 0; i < inputs.size(); i += 4) {
						// 缴费年月
						String yearMonth = inputs.get(i).val();
						System.out.println(yearMonth);
						// 单位名称
						String company = inputs.get(i + 1).val();
						System.out.println(company);
						// 缴费基数
						String pay_cardinal = inputs.get(i + 2).val();
						System.out.println(pay_cardinal);
						// 单位缴费额
						String companyPay = inputs.get(i + 3).val();
						System.out.println(companyPay);
						InsuranceZaoZhuangShengYuInfo insuranceZaoZhuangShengYuInfo = new InsuranceZaoZhuangShengYuInfo();
						insuranceZaoZhuangShengYuInfo.setTaskid(parameter.getTaskId());
						insuranceZaoZhuangShengYuInfo.setYearMonth(yearMonth);
						insuranceZaoZhuangShengYuInfo.setCompany(company);
						insuranceZaoZhuangShengYuInfo.setPay_cardinal(pay_cardinal);
						insuranceZaoZhuangShengYuInfo.setCompany_pay(companyPay);
						insuranceZaoZhuangShengYuInfoRepository.save(insuranceZaoZhuangShengYuInfo);
					}
				}
				// 更新Task表为生育信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhasestatus());
				taskInsurance.setShengyuStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取生育数据失败或没有数据！");
				// 更新Task表为生育信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhasestatus());
				taskInsurance.setShengyuStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
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
			String loginurl = "http://218.56.155.46:8081/hso/persi.do?method=queryZgGsjf&_xmlString=<?xml version=\"1.0\" encoding=\"UTF-8\"?><p><s nd=\""
					+ year + "\"/></p>";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取工伤数据成功！");

				InsuranceZaoZhuangHtml insuranceZaoZhuangHtml = new InsuranceZaoZhuangHtml();
				insuranceZaoZhuangHtml.setHtml(contentAsString);
				insuranceZaoZhuangHtml.setPageCount(1);
				insuranceZaoZhuangHtml.setTaskid(parameter.getTaskId());
				insuranceZaoZhuangHtml.setType("1");
				insuranceZaoZhuangHtml.setUrl(loginurl);
				insuranceZaoZhuangHtmlRepository.save(insuranceZaoZhuangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements tables = doc.getElementsByClass("dataTable");
				for (int j = 0; j < tables.size(); j++) {
					Elements inputs = tables.get(j).getElementsByAttributeValue("type", "text");
					for (int i = 0; i < inputs.size(); i += 4) {
						// 缴费年月
						String yearMonth = inputs.get(i).val();
						System.out.println(yearMonth);
						// 单位名称
						String company = inputs.get(i + 1).val();
						System.out.println(company);
						// 缴费基数
						String pay_cardinal = inputs.get(i + 2).val();
						System.out.println(pay_cardinal);
						// 单位缴费额
						String companyPay = inputs.get(i + 3).val();
						System.out.println(companyPay);
						InsuranceZaoZhuangGongShangInfo insuranceZaoZhuangGongShangInfo = new InsuranceZaoZhuangGongShangInfo();
						insuranceZaoZhuangGongShangInfo.setTaskid(parameter.getTaskId());
						insuranceZaoZhuangGongShangInfo.setYearMonth(yearMonth);
						insuranceZaoZhuangGongShangInfo.setCompany(company);
						insuranceZaoZhuangGongShangInfo.setPay_cardinal(pay_cardinal);
						insuranceZaoZhuangGongShangInfo.setCompany_pay(companyPay);
						insuranceZaoZhuangGongShangInfoRepository.save(insuranceZaoZhuangGongShangInfo);
					}
				}
				// 更新Task表为工伤信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhasestatus());
				taskInsurance.setGongshangStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取工伤数据失败或没有数据！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhasestatus());
				taskInsurance.setGongshangStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}

		} catch (Exception e) {
			System.out.println("获取工伤保险信息数据异常！");
		}

		return "";
	}
}
