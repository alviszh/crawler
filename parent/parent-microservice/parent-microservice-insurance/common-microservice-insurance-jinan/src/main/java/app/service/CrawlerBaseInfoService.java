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
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanBaseInfo;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanHtml;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanImageInfo;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.jinan.InsuranceJiNanYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanImageInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jinan.InsuranceJiNanYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.jinan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.jinan" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceJiNanHtmlRepository insuranceJiNanHtmlRepository;

	@Autowired
	private InsuranceJiNanBaseInfoRepository insuranceJiNanBaseInfoRepository;

	@Autowired
	private InsuranceJiNanYanglaoInfoRepository insuranceJiNanYanglaoInfoRepository;

	@Autowired
	private InsuranceJiNanYibaoInfoRepository insuranceJiNanYibaoInfoRepository;

	@Autowired
	private InsuranceJiNanShiyeInfoRepository insuranceJiNanShiyeInfoRepository;

	@Autowired
	private InsuranceJiNanShengYuInfoRepository insuranceJiNanShengYuInfoRepository;

	@Autowired
	private InsuranceJiNanGongShangInfoRepository insuranceJiNanGongShangInfoRepository;
	
	@Autowired
	private InsuranceJiNanImageInfoRepository insuranceJiNanImageInfoRepository;

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
		
		List<InsuranceJiNanImageInfo> findByTaskid = insuranceJiNanImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://60.216.99.138/hsp/hspUser.do?method=fwdQueryPerInfo&_random=0.9923023044754902&__usersession_uuid="+requestParameter+"&_laneID=4024d650-8dd7-4796-8c2a-b9e61e53783a";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceJiNanHtml insuranceJiNanHtml = new InsuranceJiNanHtml();
			insuranceJiNanHtml.setHtml(contentAsString);
			insuranceJiNanHtml.setPageCount(1);
			insuranceJiNanHtml.setTaskid(parameter.getTaskId());
			insuranceJiNanHtml.setType("基本信息");
			insuranceJiNanHtml.setUrl(loginurl);
			insuranceJiNanHtmlRepository.save(insuranceJiNanHtml);

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

				InsuranceJiNanBaseInfo insuranceJiNanBaseInfo = new InsuranceJiNanBaseInfo();
				insuranceJiNanBaseInfo.setTaskid(parameter.getTaskId());
				insuranceJiNanBaseInfo.setName(name);
				insuranceJiNanBaseInfo.setCardId(cardId);
				insuranceJiNanBaseInfo.setSex(sex);
				insuranceJiNanBaseInfo.setBirthDate(birthDate);
				insuranceJiNanBaseInfo.setHkxz(hkxz);
				insuranceJiNanBaseInfo.setCulture(culture);
				insuranceJiNanBaseInfo.setMaritalstatus(maritalstatus);
				insuranceJiNanBaseInfo.setCommunicationaddr(communicationaddr);
				insuranceJiNanBaseInfo.setHomeaddr(homeaddr);
				insuranceJiNanBaseInfo.setNation(nation);
				insuranceJiNanBaseInfo.setLinkman(linkman);
				insuranceJiNanBaseInfo.setRegisterphone(registerphone);
				insuranceJiNanBaseInfo.setAdministrative(administrative);
				insuranceJiNanBaseInfo.setPostalservice(postalservice);
				insuranceJiNanBaseInfo.setPermanentaddr(permanentaddr);
				insuranceJiNanBaseInfoRepository.save(insuranceJiNanBaseInfo);

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
		
		List<InsuranceJiNanImageInfo> findByTaskid = insuranceJiNanImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://60.216.99.138/hsp/siAd.do?method=queryAgedPayHis&year="+year+"&_random=0.020817278671616757&__usersession_uuid="+requestParameter+"&_laneID=b44434e8-3006-4a8d-bb4f-25bd96502463";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取养老数据成功！");
				
				InsuranceJiNanHtml insuranceJiNanHtml = new InsuranceJiNanHtml();
				insuranceJiNanHtml.setHtml(contentAsString);
				insuranceJiNanHtml.setPageCount(1);
				insuranceJiNanHtml.setTaskid(parameter.getTaskId());
				insuranceJiNanHtml.setType("养老保险");
				insuranceJiNanHtml.setUrl(loginurl);
				insuranceJiNanHtmlRepository.save(insuranceJiNanHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 4) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 1).val();
					System.out.println(pay_cardinal);
					// 记账额
					String charge = inputs.get(i + 2).val();
					System.out.println(charge);
					// 发生日期
					String date = inputs.get(i + 3).val();
					System.out.println(date);
					InsuranceJiNanYanglaoInfo insuranceJiNanYanglaoInfo = new InsuranceJiNanYanglaoInfo();
					insuranceJiNanYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceJiNanYanglaoInfo.setYearMonth(yearMonth);
					insuranceJiNanYanglaoInfo.setPay_cardinal(pay_cardinal);
					insuranceJiNanYanglaoInfo.setCharge(charge);
					insuranceJiNanYanglaoInfo.setDate(date);
					insuranceJiNanYanglaoInfoRepository.save(insuranceJiNanYanglaoInfo);
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
		
		List<InsuranceJiNanImageInfo> findByTaskid = insuranceJiNanImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://60.216.99.138/hsp/siMedi.do?method=queryMediPayHis&year="+year+"&_random=0.5796482551428432&__usersession_uuid="+requestParameter+"&_laneID=b44434e8-3006-4a8d-bb4f-25bd96502463";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取医疗数据成功！");
				
				InsuranceJiNanHtml insuranceJiNanHtml = new InsuranceJiNanHtml();
				insuranceJiNanHtml.setHtml(contentAsString);
				insuranceJiNanHtml.setPageCount(1);
				insuranceJiNanHtml.setTaskid(parameter.getTaskId());
				insuranceJiNanHtml.setType("医疗保险");
				insuranceJiNanHtml.setUrl(loginurl);
				insuranceJiNanHtmlRepository.save(insuranceJiNanHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 6) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 1).val();
					System.out.println(pay_cardinal);
					// 记账额
					String charge = inputs.get(i + 2).val();
					System.out.println(charge);
					// 门诊统筹扣减额
					String mztckje = inputs.get(i + 3).val();
					System.out.println(mztckje);
					// 大额扣减金额
					String dekjje = inputs.get(i + 4).val();
					System.out.println(dekjje);
					// 发生日期
					String date = inputs.get(i + 5).val();
					System.out.println(date);
					InsuranceJiNanYibaoInfo insuranceJiNanYibaoInfo = new InsuranceJiNanYibaoInfo();
					insuranceJiNanYibaoInfo.setTaskid(parameter.getTaskId());
					insuranceJiNanYibaoInfo.setYearMonth(yearMonth);
					insuranceJiNanYibaoInfo.setPay_cardinal(pay_cardinal);
					insuranceJiNanYibaoInfo.setCharge(charge);
					insuranceJiNanYibaoInfo.setMztckje(mztckje);
					insuranceJiNanYibaoInfo.setDekjje(dekjje);
					insuranceJiNanYibaoInfo.setDate(date);
					insuranceJiNanYibaoInfoRepository.save(insuranceJiNanYibaoInfo);
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
		
		List<InsuranceJiNanImageInfo> findByTaskid = insuranceJiNanImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://60.216.99.138/hsp/siLost.do?method=queryLostPayHis&year="+year+"&_random=0.11177959598291998&__usersession_uuid="+requestParameter+"&_laneID=b44434e8-3006-4a8d-bb4f-25bd96502463";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取失业数据成功！");
				
				InsuranceJiNanHtml insuranceJiNanHtml = new InsuranceJiNanHtml();
				insuranceJiNanHtml.setHtml(contentAsString);
				insuranceJiNanHtml.setPageCount(1);
				insuranceJiNanHtml.setTaskid(parameter.getTaskId());
				insuranceJiNanHtml.setType("失业保险");
				insuranceJiNanHtml.setUrl(loginurl);
				insuranceJiNanHtmlRepository.save(insuranceJiNanHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 1).val();
					System.out.println(pay_cardinal);
					// 个人交
					String personPay = inputs.get(i + 2).val();
					System.out.println(personPay);
					// 缴费日期
					String pay_date = inputs.get(i + 3).val();
					System.out.println(pay_date);
					// 缴费类型 
					String pay_type = inputs.get(i + 4).val();
					System.out.println(pay_type);
					InsuranceJiNanShiyeInfo insuranceJiNanShiyeInfo = new InsuranceJiNanShiyeInfo();
					insuranceJiNanShiyeInfo.setTaskid(parameter.getTaskId());
					insuranceJiNanShiyeInfo.setYearMonth(yearMonth);
					insuranceJiNanShiyeInfo.setPay_cardinal(pay_cardinal);
					insuranceJiNanShiyeInfo.setPersonPay(personPay);
					insuranceJiNanShiyeInfo.setPay_date(pay_date);
					insuranceJiNanShiyeInfo.setPay_type(pay_type);
					insuranceJiNanShiyeInfoRepository.save(insuranceJiNanShiyeInfo);
					
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
		List<InsuranceJiNanImageInfo> findByTaskid = insuranceJiNanImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://60.216.99.138/hsp/siBirth.do?method=queryBirthPayHis&year="+year+"&_random=0.15540882555200253&__usersession_uuid="+requestParameter+"&_laneID=b44434e8-3006-4a8d-bb4f-25bd96502463";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取生育数据成功！");
				
				InsuranceJiNanHtml insuranceJiNanHtml = new InsuranceJiNanHtml();
				insuranceJiNanHtml.setHtml(contentAsString);
				insuranceJiNanHtml.setPageCount(1);
				insuranceJiNanHtml.setTaskid(parameter.getTaskId());
				insuranceJiNanHtml.setType("生育保险");
				insuranceJiNanHtml.setUrl(loginurl);
				insuranceJiNanHtmlRepository.save(insuranceJiNanHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 3) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费日期
					String pay_date = inputs.get(i + 1).val();
					System.out.println(pay_date);
					// 缴费类型
					String pay_type = inputs.get(i + 2).val();
					System.out.println(pay_type);
					InsuranceJiNanShengYuInfo insuranceJiNanShengYuInfo = new InsuranceJiNanShengYuInfo();
					insuranceJiNanShengYuInfo.setTaskid(parameter.getTaskId());
					insuranceJiNanShengYuInfo.setYearMonth(yearMonth);
					insuranceJiNanShengYuInfo.setPay_date(pay_date);
					insuranceJiNanShengYuInfo.setPay_type(pay_type);
					insuranceJiNanShengYuInfoRepository.save(insuranceJiNanShengYuInfo);
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
		List<InsuranceJiNanImageInfo> findByTaskid = insuranceJiNanImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://60.216.99.138/hsp/siHarm.do?method=queryHarmPayHis&year="+year+"&_random=0.31764728004019194&__usersession_uuid="+requestParameter+"&_laneID=b44434e8-3006-4a8d-bb4f-25bd96502463";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取工伤数据成功！");
				
				InsuranceJiNanHtml insuranceJiNanHtml = new InsuranceJiNanHtml();
				insuranceJiNanHtml.setHtml(contentAsString);
				insuranceJiNanHtml.setPageCount(1);
				insuranceJiNanHtml.setTaskid(parameter.getTaskId());
				insuranceJiNanHtml.setType("工伤保险");
				insuranceJiNanHtml.setUrl(loginurl);
				insuranceJiNanHtmlRepository.save(insuranceJiNanHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 3) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费日期
					String pay_date = inputs.get(i + 1).val();
					System.out.println(pay_date);
					// 缴费类型
					String pay_type = inputs.get(i + 2).val();
					System.out.println(pay_type);
					InsuranceJiNanGongShangInfo insuranceJiNanGongShangInfo = new InsuranceJiNanGongShangInfo();
					insuranceJiNanGongShangInfo.setTaskid(parameter.getTaskId());
					insuranceJiNanGongShangInfo.setYearMonth(yearMonth);
					insuranceJiNanGongShangInfo.setPay_date(pay_date);
					insuranceJiNanGongShangInfo.setPay_type(pay_type);
					insuranceJiNanGongShangInfoRepository.save(insuranceJiNanGongShangInfo);
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
