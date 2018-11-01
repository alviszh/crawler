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
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouBaseInfo;
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouHtml;
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouImageInfo;
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.binzhou.InsuranceBinZhouBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.binzhou.InsuranceBinZhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.binzhou.InsuranceBinZhouImageInfoRepository;
import com.microservice.dao.repository.crawler.insurance.binzhou.InsuranceBinZhouShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.binzhou.InsuranceBinZhouYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.binzhou.InsuranceBinZhouYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongbin
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.binzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.binzhou" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceBinZhouHtmlRepository insuranceBinZhouHtmlRepository;

	@Autowired
	private InsuranceBinZhouBaseInfoRepository insuranceBinZhouBaseInfoRepository;

	@Autowired
	private InsuranceBinZhouYanglaoInfoRepository insuranceBinZhouYanglaoInfoRepository;

	@Autowired
	private InsuranceBinZhouYibaoInfoRepository insuranceBinZhouYibaoInfoRepository;

	@Autowired
	private InsuranceBinZhouShiyeInfoRepository insuranceBinZhouShiyeInfoRepository;

	@Autowired
	private InsuranceBinZhouImageInfoRepository insuranceBinZhouImageInfoRepository;
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

		List<InsuranceBinZhouImageInfo> findByTaskid = insuranceBinZhouImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://222.134.45.172:8002/hsp/hspUser.do?method=fwdQueryPerInfo&_random=0.33937379572851367&__usersession_uuid="
					+ requestParameter + "&_laneID=69dfbe80-2da5-4893-8d4e-8b3deaa39126";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println("基本信息-------" + contentAsString);

			InsuranceBinZhouHtml insuranceBinZhouHtml = new InsuranceBinZhouHtml();
			insuranceBinZhouHtml.setHtml(contentAsString);
			insuranceBinZhouHtml.setPageCount(1);
			insuranceBinZhouHtml.setTaskid(parameter.getTaskId());
			insuranceBinZhouHtml.setType("1");
			insuranceBinZhouHtml.setUrl(loginurl);
			insuranceBinZhouHtmlRepository.save(insuranceBinZhouHtml);

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
				// 单位名称
				String unitaddr = doc.getElementById("dwmc").val();
				System.out.println(unitaddr);

				InsuranceBinZhouBaseInfo insuranceBinZhouBaseInfo = new InsuranceBinZhouBaseInfo();
				insuranceBinZhouBaseInfo.setTaskid(parameter.getTaskId());
				insuranceBinZhouBaseInfo.setName(name);
				insuranceBinZhouBaseInfo.setCardId(cardId);
				insuranceBinZhouBaseInfo.setSex(sex);
				insuranceBinZhouBaseInfo.setBirthDate(birthDate);
				insuranceBinZhouBaseInfo.setNation(nation);
				insuranceBinZhouBaseInfo.setMaritalstatus(maritalstatus);
				insuranceBinZhouBaseInfo.setCulture(culture);
				insuranceBinZhouBaseInfo.setHkxz(hkxz);
				insuranceBinZhouBaseInfo.setAdministrative(administrative);
				insuranceBinZhouBaseInfo.setLinkman(linkman);
				insuranceBinZhouBaseInfo.setRegisterphone(registerphone);
				insuranceBinZhouBaseInfo.setPostalservice(postalservice);
				insuranceBinZhouBaseInfo.setHomeaddr(homeaddr);
				insuranceBinZhouBaseInfo.setCommunicationaddr(communicationaddr);
				insuranceBinZhouBaseInfo.setPermanentaddr(permanentaddr);
				insuranceBinZhouBaseInfo.setUnitaddr(unitaddr);
				insuranceBinZhouBaseInfoRepository.save(insuranceBinZhouBaseInfo);
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

		List<InsuranceBinZhouImageInfo> findByTaskid = insuranceBinZhouImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();

		try {
			String loginurl = "http://222.134.45.172:8002/hsp/siAd.do?method=queryAgedPayHis&nd=" + year
					+ "&_random=0.19695410333063923&__usersession_uuid=" + requestParameter
					+ "&_laneID=40e7022f-edc4-4acc-b02c-a06990773736";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println("养老保险信息-------" + contentAsString);
			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取养老数据成功！");
				InsuranceBinZhouHtml insuranceBinZhouHtml = new InsuranceBinZhouHtml();
				insuranceBinZhouHtml.setHtml(contentAsString);
				insuranceBinZhouHtml.setPageCount(1);
				insuranceBinZhouHtml.setTaskid(parameter.getTaskId());
				insuranceBinZhouHtml.setType("1");
				insuranceBinZhouHtml.setUrl(loginurl);
				insuranceBinZhouHtmlRepository.save(insuranceBinZhouHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位缴费基数
					String unit_pay_cardinal = inputs.get(i + 1).val();
					System.out.println(unit_pay_cardinal);
					// 个人缴费基数
					String person_pay_cardinal = inputs.get(i + 2).val();
					System.out.println(person_pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personalPay = inputs.get(i + 4).val();
					System.out.println(personalPay);
					InsuranceBinZhouYanglaoInfo insuranceBinZhouYanglaoInfo = new InsuranceBinZhouYanglaoInfo();
					insuranceBinZhouYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceBinZhouYanglaoInfo.setYearMonth(yearMonth);
					insuranceBinZhouYanglaoInfo.setUnit_pay_cardinal(unit_pay_cardinal);
					insuranceBinZhouYanglaoInfo.setPerson_pay_cardinal(person_pay_cardinal);
					insuranceBinZhouYanglaoInfo.setPersonalPay(personalPay);
					insuranceBinZhouYanglaoInfo.setCompanyPay(companyPay);
					insuranceBinZhouYanglaoInfoRepository.save(insuranceBinZhouYanglaoInfo);
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
			System.out.println("获取养老数据异常！");
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
		List<InsuranceBinZhouImageInfo> findByTaskid = insuranceBinZhouImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();

		try {
			String loginurl = "http://222.134.45.172:8002/hsp/siMedi.do?method=queryMediPayHis&year=" + year
					+ "&_random=0.12064872512468461&__usersession_uuid=" + requestParameter
					+ "&_laneID=70400d99-5831-4997-80fc-c7220f1fd604";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println("医疗保险信息-------" + contentAsString);
			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取医疗数据成功！");

				InsuranceBinZhouHtml insuranceBinZhouHtml = new InsuranceBinZhouHtml();
				insuranceBinZhouHtml.setHtml(contentAsString);
				insuranceBinZhouHtml.setPageCount(1);
				insuranceBinZhouHtml.setTaskid(parameter.getTaskId());
				insuranceBinZhouHtml.setType("1");
				insuranceBinZhouHtml.setUrl(loginurl);
				insuranceBinZhouHtmlRepository.save(insuranceBinZhouHtml);
				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位缴费基数
					String unit_pay_cardinal = inputs.get(i + 1).val();
					System.out.println(unit_pay_cardinal);
					// 个人缴费基数
					String person_pay_cardinal = inputs.get(i + 2).val();
					System.out.println(person_pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personalPay = inputs.get(i + 4).val();
					System.out.println(personalPay);
					InsuranceBinZhouYibaoInfo insuranceBinZhouYibaoInfo = new InsuranceBinZhouYibaoInfo();
					insuranceBinZhouYibaoInfo.setTaskid(parameter.getTaskId());
					insuranceBinZhouYibaoInfo.setYearMonth(yearMonth);
					insuranceBinZhouYibaoInfo.setUnit_pay_cardinal(unit_pay_cardinal);
					insuranceBinZhouYibaoInfo.setPerson_pay_cardinal(person_pay_cardinal);
					insuranceBinZhouYibaoInfo.setPersonalPay(personalPay);
					insuranceBinZhouYibaoInfo.setCompanyPay(companyPay);
					insuranceBinZhouYibaoInfoRepository.save(insuranceBinZhouYibaoInfo);
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
			System.out.println("获取医疗数据异常！");
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
		
		List<InsuranceBinZhouImageInfo> findByTaskid = insuranceBinZhouImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();

		try {
			String loginurl = "http://222.134.45.172:8002/hsp/siLost.do?method=queryLostPayHis&year="+year+"&_random=0.09854995135877886&__usersession_uuid="+requestParameter+"&_laneID=70400d99-5831-4997-80fc-c7220f1fd604";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println("失业保险信息-------" + contentAsString);
			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取失业数据成功！");
				InsuranceBinZhouHtml insuranceBinZhouHtml = new InsuranceBinZhouHtml();
				insuranceBinZhouHtml.setHtml(contentAsString);
				insuranceBinZhouHtml.setPageCount(1);
				insuranceBinZhouHtml.setTaskid(parameter.getTaskId());
				insuranceBinZhouHtml.setType("1");
				insuranceBinZhouHtml.setUrl(loginurl);
				insuranceBinZhouHtmlRepository.save(insuranceBinZhouHtml);
				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 4) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 缴费基数
					String payCardinal = inputs.get(i + 1).val();
					System.out.println(payCardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 2).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personalPay = inputs.get(i + 3).val();
					System.out.println(personalPay);
					InsuranceBinZhouShiyeInfo insuranceBinZhouShiyeInfo = new InsuranceBinZhouShiyeInfo();
					insuranceBinZhouShiyeInfo.setTaskid(parameter.getTaskId());
					insuranceBinZhouShiyeInfo.setYearMonth(yearMonth);
					insuranceBinZhouShiyeInfo.setPayCardinal(payCardinal);
					insuranceBinZhouShiyeInfo.setPersonalPay(personalPay);
					insuranceBinZhouShiyeInfo.setCompanyPay(companyPay);
					insuranceBinZhouShiyeInfoRepository.save(insuranceBinZhouShiyeInfo);
				}

				// 更新Task表为 失业信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhasestatus());
				taskInsurance.setShiyeStatus(200);
				taskInsurance.setGongshangStatus(200);
				taskInsurance.setShengyuStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else {
				System.out.println("获取失业数据失败或没有数据！");
			}

		} catch (Exception e) {
			System.out.println("获取失业数据异常！");
		}
		
		return "";
	}
}
