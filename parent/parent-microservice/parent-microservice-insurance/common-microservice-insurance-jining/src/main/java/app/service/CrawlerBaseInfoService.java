package app.service;

import java.net.URL;
import java.util.List;
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
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingBaseInfo;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingHtml;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingImageInfo;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingImageInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jining.InsuranceJiNingYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.jining" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.jining" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceJiNingHtmlRepository insuranceJiNingHtmlRepository;

	@Autowired
	private InsuranceJiNingBaseInfoRepository insuranceJiNingBaseInfoRepository;

	@Autowired
	private InsuranceJiNingYanglaoInfoRepository insuranceJiNingYanglaoInfoRepository;

	@Autowired
	private InsuranceJiNingYibaoInfoRepository insuranceJiNingYibaoInfoRepository;

	@Autowired
	private InsuranceJiNingShiyeInfoRepository insuranceJiNingShiyeInfoRepository;

	@Autowired
	private InsuranceJiNingShengYuInfoRepository insuranceJiNingShengYuInfoRepository;

	@Autowired
	private InsuranceJiNingGongShangInfoRepository insuranceJiNingGongShangInfoRepository;
	
	@Autowired
	private InsuranceJiNingImageInfoRepository insuranceJiNingImageInfoRepository;

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
		
		List<InsuranceJiNingImageInfo> findByTaskid = insuranceJiNingImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://60.211.255.251:8082/hsp/hspUser.do?method=fwdQueryPerInfo&_random=0.9923023044754902&__usersession_uuid="+requestParameter+"&_laneID=4623e26e-375e-41f2-9c46-735180eee48c";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceJiNingHtml insuranceJiNingHtml = new InsuranceJiNingHtml();
			insuranceJiNingHtml.setHtml(contentAsString);
			insuranceJiNingHtml.setPageCount(1);
			insuranceJiNingHtml.setTaskid(parameter.getTaskId());
			insuranceJiNingHtml.setType("基本信息");
			insuranceJiNingHtml.setUrl(loginurl);
			insuranceJiNingHtmlRepository.save(insuranceJiNingHtml);

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
				// 民族
				String nation = doc.getElementById("mzmc").val();
				System.out.println(nation);
				// 婚姻状况
				String maritalstatus = doc.getElementById("hyzkmc").val();
				System.out.println(maritalstatus);
				// 文化程度
				String culture = doc.getElementById("whcdmc").val();
				System.out.println(culture);
				// 户口性质
				String hkxz = doc.getElementById("hkxzmc").val();
				System.out.println(hkxz);
				// 行政职务
				String administrative = doc.getElementById("xzzwmc").val();
				System.out.println(administrative);
				// 联系人
				String linkman = doc.getElementById("lxr").val();
				System.out.println(linkman);
				// 注册电话
				String registerphone = doc.getElementById("lxdh").val();
				System.out.println(registerphone);
				// 邮政编码
				String postalservice = doc.getElementById("yzbm").val();
				System.out.println(postalservice);
				// 家庭住址
				String homeaddr = doc.getElementById("jtzz").val();
				System.out.println(homeaddr);
				// 通讯地址
				String communicationaddr = doc.getElementById("txdz").val();
				System.out.println(communicationaddr);
				// 户口所在地
				String permanentaddr = doc.getElementById("hkszd").val();
				System.out.println(permanentaddr);

				InsuranceJiNingBaseInfo insuranceJiNingBaseInfo = new InsuranceJiNingBaseInfo();
				insuranceJiNingBaseInfo.setTaskid(parameter.getTaskId());
				insuranceJiNingBaseInfo.setName(name);
				insuranceJiNingBaseInfo.setCardId(cardId);
				insuranceJiNingBaseInfo.setSex(sex);
				insuranceJiNingBaseInfo.setBirthDate(birthDate);
				insuranceJiNingBaseInfo.setHkxz(hkxz);
				insuranceJiNingBaseInfo.setCulture(culture);
				insuranceJiNingBaseInfo.setMaritalstatus(maritalstatus);
				insuranceJiNingBaseInfo.setCommunicationaddr(communicationaddr);
				insuranceJiNingBaseInfo.setHomeaddr(homeaddr);
				insuranceJiNingBaseInfo.setNation(nation);
				insuranceJiNingBaseInfo.setLinkman(linkman);
				insuranceJiNingBaseInfo.setRegisterphone(registerphone);
				insuranceJiNingBaseInfo.setAdministrative(administrative);
				insuranceJiNingBaseInfo.setPostalservice(postalservice);
				insuranceJiNingBaseInfo.setPermanentaddr(permanentaddr);
				insuranceJiNingBaseInfoRepository.save(insuranceJiNingBaseInfo);

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
		
		List<InsuranceJiNingImageInfo> findByTaskid = insuranceJiNingImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://60.211.255.251:8082/hsp/siAd.do?method=queryAgedPayHis&nd="+year+"&_random=0.5384723663293376&__usersession_uuid="+requestParameter+"&_laneID=49c1a38d-1ac4-48b0-ac73-1b753454d34f";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取养老数据成功！");
				
				InsuranceJiNingHtml insuranceJiNingHtml = new InsuranceJiNingHtml();
				insuranceJiNingHtml.setHtml(contentAsString);
				insuranceJiNingHtml.setPageCount(1);
				insuranceJiNingHtml.setTaskid(parameter.getTaskId());
				insuranceJiNingHtml.setType("养老保险");
				insuranceJiNingHtml.setUrl(loginurl);
				insuranceJiNingHtmlRepository.save(insuranceJiNingHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位缴费基数
					String company_pay_cardinal = inputs.get(i + 1).val();
					System.out.println(company_pay_cardinal);
					// 个人缴费基数
					String person_pay_cardinal = inputs.get(i + 2).val();
					System.out.println(person_pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personPay = inputs.get(i + 4).val();
					System.out.println(personPay);
					InsuranceJiNingYanglaoInfo insuranceJiNingYanglaoInfo = new InsuranceJiNingYanglaoInfo();
					insuranceJiNingYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceJiNingYanglaoInfo.setYearMonth(yearMonth);
					insuranceJiNingYanglaoInfo.setCompany_pay_cardinal(company_pay_cardinal);
					insuranceJiNingYanglaoInfo.setPerson_pay_cardinal(person_pay_cardinal);
					insuranceJiNingYanglaoInfo.setCompanyPay(companyPay);
					insuranceJiNingYanglaoInfo.setPersonPay(personPay);
					insuranceJiNingYanglaoInfoRepository.save(insuranceJiNingYanglaoInfo);
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
		
		List<InsuranceJiNingImageInfo> findByTaskid = insuranceJiNingImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://60.211.255.251:8082/hsp/siMedi.do?method=queryMediPayHis&year="+year+"&_random=0.1445080871482043&__usersession_uuid="+requestParameter+"&_laneID=49c1a38d-1ac4-48b0-ac73-1b753454d34f";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取医疗数据成功！");
				
				InsuranceJiNingHtml insuranceJiNingHtml = new InsuranceJiNingHtml();
				insuranceJiNingHtml.setHtml(contentAsString);
				insuranceJiNingHtml.setPageCount(1);
				insuranceJiNingHtml.setTaskid(parameter.getTaskId());
				insuranceJiNingHtml.setType("医疗保险");
				insuranceJiNingHtml.setUrl(loginurl);
				insuranceJiNingHtmlRepository.save(insuranceJiNingHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位缴费基数
					String company_pay_cardinal = inputs.get(i + 1).val();
					System.out.println(company_pay_cardinal);
					// 个人缴费基数
					String person_pay_cardinal = inputs.get(i + 2).val();
					System.out.println(person_pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personPay = inputs.get(i + 4).val();
					System.out.println(personPay);
					InsuranceJiNingYibaoInfo insuranceJiNingYibaoInfo = new InsuranceJiNingYibaoInfo();
					insuranceJiNingYibaoInfo.setTaskid(parameter.getTaskId());
					insuranceJiNingYibaoInfo.setYearMonth(yearMonth);
					insuranceJiNingYibaoInfo.setCompany_pay_cardinal(company_pay_cardinal);
					insuranceJiNingYibaoInfo.setPerson_pay_cardinal(person_pay_cardinal);
					insuranceJiNingYibaoInfo.setCompanyPay(companyPay);
					insuranceJiNingYibaoInfo.setPersonPay(personPay);
					insuranceJiNingYibaoInfoRepository.save(insuranceJiNingYibaoInfo);
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
		
		List<InsuranceJiNingImageInfo> findByTaskid = insuranceJiNingImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://60.211.255.251:8082/hsp/siLost.do?method=queryLostPayHis&year="+year+"&_random=0.4735873576283034&__usersession_uuid="+requestParameter+"&_laneID=49c1a38d-1ac4-48b0-ac73-1b753454d34f";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取失业数据成功！");
				
				InsuranceJiNingHtml insuranceJiNingHtml = new InsuranceJiNingHtml();
				insuranceJiNingHtml.setHtml(contentAsString);
				insuranceJiNingHtml.setPageCount(1);
				insuranceJiNingHtml.setTaskid(parameter.getTaskId());
				insuranceJiNingHtml.setType("失业保险");
				insuranceJiNingHtml.setUrl(loginurl);
				insuranceJiNingHtmlRepository.save(insuranceJiNingHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 4) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 1).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 2).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personPay = inputs.get(i + 3).val();
					System.out.println(personPay);
					InsuranceJiNingShiyeInfo insuranceJiNingShiyeInfo = new InsuranceJiNingShiyeInfo();
					insuranceJiNingShiyeInfo.setTaskid(parameter.getTaskId());
					insuranceJiNingShiyeInfo.setYearMonth(yearMonth);
					insuranceJiNingShiyeInfo.setPay_cardinal(pay_cardinal);
					insuranceJiNingShiyeInfo.setCompanyPay(companyPay);
					insuranceJiNingShiyeInfo.setPersonPay(personPay);
					insuranceJiNingShiyeInfoRepository.save(insuranceJiNingShiyeInfo);
					
				}

				// 更新Task表为 失业信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhasestatus());
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
		List<InsuranceJiNingImageInfo> findByTaskid = insuranceJiNingImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://60.211.255.251:8082/hsp/siBirth.do?method=queryBirthPayHis&year="+year+"&_random=0.3530401408238357&__usersession_uuid="+requestParameter+"&_laneID=49c1a38d-1ac4-48b0-ac73-1b753454d34f";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取生育数据成功！");
				
				InsuranceJiNingHtml insuranceJiNingHtml = new InsuranceJiNingHtml();
				insuranceJiNingHtml.setHtml(contentAsString);
				insuranceJiNingHtml.setPageCount(1);
				insuranceJiNingHtml.setTaskid(parameter.getTaskId());
				insuranceJiNingHtml.setType("生育保险");
				insuranceJiNingHtml.setUrl(loginurl);
				insuranceJiNingHtmlRepository.save(insuranceJiNingHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 3) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 1).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 2).val();
					System.out.println(companyPay);
					InsuranceJiNingShengYuInfo insuranceJiNingShengYuInfo = new InsuranceJiNingShengYuInfo();
					insuranceJiNingShengYuInfo.setTaskid(parameter.getTaskId());
					insuranceJiNingShengYuInfo.setYearMonth(yearMonth);
					insuranceJiNingShengYuInfo.setPay_cardinal(pay_cardinal);
					insuranceJiNingShengYuInfo.setCompany_pay(companyPay);
					insuranceJiNingShengYuInfoRepository.save(insuranceJiNingShengYuInfo);
				}

				// 更新Task表为生育信息数据爬取成功
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
		List<InsuranceJiNingImageInfo> findByTaskid = insuranceJiNingImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://60.211.255.251:8082/hsp/siHarm.do?method=queryHarmPayHis&year="+year+"&_random=0.7988163917474771&__usersession_uuid="+requestParameter+"&_laneID=49c1a38d-1ac4-48b0-ac73-1b753454d34f";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取工伤数据成功！");
				
				InsuranceJiNingHtml insuranceJiNingHtml = new InsuranceJiNingHtml();
				insuranceJiNingHtml.setHtml(contentAsString);
				insuranceJiNingHtml.setPageCount(1);
				insuranceJiNingHtml.setTaskid(parameter.getTaskId());
				insuranceJiNingHtml.setType("工伤保险");
				insuranceJiNingHtml.setUrl(loginurl);
				insuranceJiNingHtmlRepository.save(insuranceJiNingHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 3) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 1).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 2).val();
					System.out.println(companyPay);
					InsuranceJiNingGongShangInfo insuranceJiNingGongShangInfo = new InsuranceJiNingGongShangInfo();
					insuranceJiNingGongShangInfo.setTaskid(parameter.getTaskId());
					insuranceJiNingGongShangInfo.setYearMonth(yearMonth);
					insuranceJiNingGongShangInfo.setPay_cardinal(pay_cardinal);
					insuranceJiNingGongShangInfo.setCompany_pay(companyPay);
					insuranceJiNingGongShangInfoRepository.save(insuranceJiNingGongShangInfo);
				}

				// 更新Task表为工伤信息数据爬取成功
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
