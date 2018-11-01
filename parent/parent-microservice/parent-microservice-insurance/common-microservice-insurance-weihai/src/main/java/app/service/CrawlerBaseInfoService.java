package app.service;

import java.net.URL;
import java.util.ArrayList;
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
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiBaseInfo;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiHtml;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiImageInfo;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiImageInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weihai.InsuranceWeiHaiYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.weihai" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.weihai" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceWeiHaiHtmlRepository insuranceWeiHaiHtmlRepository;

	@Autowired
	private InsuranceWeiHaiBaseInfoRepository insuranceWeiHaiBaseInfoRepository;

	@Autowired
	private InsuranceWeiHaiYanglaoInfoRepository insuranceWeiHaiYanglaoInfoRepository;

	@Autowired
	private InsuranceWeiHaiYibaoInfoRepository insuranceWeiHaiYibaoInfoRepository;

	@Autowired
	private InsuranceWeiHaiShiyeInfoRepository insuranceWeiHaiShiyeInfoRepository;

	@Autowired
	private InsuranceWeiHaiShengYuInfoRepository insuranceWeiHaiShengYuInfoRepository;

	@Autowired
	private InsuranceWeiHaiGongShangInfoRepository insuranceWeiHaiGongShangInfoRepository;
	
	@Autowired
	private InsuranceWeiHaiImageInfoRepository insuranceWeiHaiImageInfoRepository;

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
		
		List<InsuranceWeiHaiImageInfo> findByTaskid = insuranceWeiHaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		System.out.println(requestParameter);
		try {
			String loginurl = "http://202.110.217.69:7001/hsp/hspUser.do";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setAdditionalHeader("Accept", "*/*");
			webRequestlogin.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequestlogin.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequestlogin.setAdditionalHeader("Connection", "keep-alive");
			webRequestlogin.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			webRequestlogin.setAdditionalHeader("Host", "202.110.217.69:7001");
			webRequestlogin.setAdditionalHeader("Origin", "http://202.110.217.69:7001");
			webRequestlogin.setAdditionalHeader("Referer", "http://202.110.217.69:7001/hsp/iframe.jsp?bizurl=hspUser.do%3Fmethod%3DfwdQueryPerInfo%26_random%3D0.3473784356438625&__usersession_uuid="+requestParameter+"");
			webRequestlogin.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			webRequestlogin.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters().add(new NameValuePair("method", "fwdQueryPerInfo"));
			webRequestlogin.getRequestParameters().add(new NameValuePair("_random", "0.7919819053471986"));
			webRequestlogin.getRequestParameters().add(new NameValuePair("_width", "1344"));
			webRequestlogin.getRequestParameters().add(new NameValuePair("_height", "239"));
			webRequestlogin.getRequestParameters().add(new NameValuePair("_random", "0.8587922858610335"));
			webRequestlogin.getRequestParameters().add(new NameValuePair("__usersession_uuid", requestParameter));
			webRequestlogin.getRequestParameters().add(new NameValuePair("_laneID", "4b256615-5cd3-4e8a-a52c-82fda594ef0d"));
			
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceWeiHaiHtml insuranceWeiHaiHtml = new InsuranceWeiHaiHtml();
			insuranceWeiHaiHtml.setHtml(contentAsString);
			insuranceWeiHaiHtml.setPageCount(1);
			insuranceWeiHaiHtml.setTaskid(parameter.getTaskId());
			insuranceWeiHaiHtml.setType("基本信息");
			insuranceWeiHaiHtml.setUrl(loginurl);
			insuranceWeiHaiHtmlRepository.save(insuranceWeiHaiHtml);

			Document doc = Jsoup.parse(contentAsString);
			Element elementById = doc.getElementById("xm");
			System.out.println(elementById);
			if (null == elementById && "".equals(elementById)) {
				System.out.println("获取基本信息数据失败！");
			} else {
				System.out.println("获取基本信息数据成功！");
				// 联系电话
				String lxdh = doc.getElementById("lxdh").val();
				System.out.println(lxdh);
				// 单位名称
				String unitaddr = doc.getElementById("dwmc").val();
				System.out.println(unitaddr);
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
				// 联系人
				String linkman = doc.getElementById("lxr").val();
				System.out.println(linkman);
				// 手机号码
				String registerphone = doc.getElementById("sjhm").val();
				System.out.println(registerphone);
				// 民族
				String nation = doc.getElementById("mzmc").val();
				System.out.println(nation);
				// 行政职务
				String administrative = doc.getElementById("xzzwmc").val();
				System.out.println(administrative);
				// 邮政编码
				String postalservice = doc.getElementById("yzbm").val();
				System.out.println(postalservice);
				// 户口所在地
				String permanentaddr = doc.getElementById("hkszd").val();
				System.out.println(permanentaddr);
				
				InsuranceWeiHaiBaseInfo insuranceWeiHaiBaseInfo = new InsuranceWeiHaiBaseInfo();
				insuranceWeiHaiBaseInfo.setTaskid(parameter.getTaskId());
				insuranceWeiHaiBaseInfo.setName(name);
				insuranceWeiHaiBaseInfo.setCardId(cardId);
				insuranceWeiHaiBaseInfo.setSex(sex);
				insuranceWeiHaiBaseInfo.setBirthDate(birthDate);
				insuranceWeiHaiBaseInfo.setHkxz(hkxz);
				insuranceWeiHaiBaseInfo.setCulture(culture);
				insuranceWeiHaiBaseInfo.setMaritalstatus(maritalstatus);
				insuranceWeiHaiBaseInfo.setCommunicationaddr(communicationaddr);
				insuranceWeiHaiBaseInfo.setHomeaddr(homeaddr);
				insuranceWeiHaiBaseInfo.setNation(nation);
				insuranceWeiHaiBaseInfo.setLinkman(linkman);
				insuranceWeiHaiBaseInfo.setRegisterphone(registerphone);
				insuranceWeiHaiBaseInfo.setAdministrative(administrative);
				insuranceWeiHaiBaseInfo.setPostalservice(postalservice);
				insuranceWeiHaiBaseInfo.setPermanentaddr(permanentaddr);
				insuranceWeiHaiBaseInfo.setUnitaddr(unitaddr);
				insuranceWeiHaiBaseInfo.setLxdh(lxdh);
				insuranceWeiHaiBaseInfoRepository.save(insuranceWeiHaiBaseInfo);

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
		
		List<InsuranceWeiHaiImageInfo> findByTaskid = insuranceWeiHaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://202.110.217.69:7001/hsp/siAd.do?method=queryAgedPayHis&nd="+year+"&_width=1344&_height=629&_random=0.764530426201135&__usersession_uuid="+requestParameter+"&_laneID=00a8d4d5-e1cb-4580-a58d-689e9149cd40";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取养老数据成功！");
				
				InsuranceWeiHaiHtml insuranceWeiHaiHtml = new InsuranceWeiHaiHtml();
				insuranceWeiHaiHtml.setHtml(contentAsString);
				insuranceWeiHaiHtml.setPageCount(1);
				insuranceWeiHaiHtml.setTaskid(parameter.getTaskId());
				insuranceWeiHaiHtml.setType("养老保险");
				insuranceWeiHaiHtml.setUrl(loginurl);
				insuranceWeiHaiHtmlRepository.save(insuranceWeiHaiHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				
				for (int i = 0; i < inputs.size()-4; i += 4) {
					// 缴费年月
					String yearMonth = inputs.get(i + 4).val();
					System.out.println(yearMonth);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 5).val();
					System.out.println(pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 6).val();
					System.out.println(companyPay);
					// 个人缴费额
					String personPay = inputs.get(i + 7).val();
					System.out.println(personPay);
					InsuranceWeiHaiYanglaoInfo insuranceWeiHaiYanglaoInfo = new InsuranceWeiHaiYanglaoInfo();
					insuranceWeiHaiYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceWeiHaiYanglaoInfo.setYearMonth(yearMonth);
					insuranceWeiHaiYanglaoInfo.setPay_cardinal(pay_cardinal);
					insuranceWeiHaiYanglaoInfo.setCompanyPay(companyPay);
					insuranceWeiHaiYanglaoInfo.setPersonPay(personPay);
					insuranceWeiHaiYanglaoInfoRepository.save(insuranceWeiHaiYanglaoInfo);
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
			e.printStackTrace();
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
		
		List<InsuranceWeiHaiImageInfo> findByTaskid = insuranceWeiHaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://202.110.217.69:7001/hsp/siMedi.do?method=queryMediPayHis&nd="+year+"&_width=1344&_height=629&_random=0.7840340425567665&__usersession_uuid="+requestParameter+"&_laneID=4a8b7f61-6bca-4130-b033-4cc8de20e92e";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取医疗数据成功！");
				
				InsuranceWeiHaiHtml insuranceWeiHaiHtml = new InsuranceWeiHaiHtml();
				insuranceWeiHaiHtml.setHtml(contentAsString);
				insuranceWeiHaiHtml.setPageCount(1);
				insuranceWeiHaiHtml.setTaskid(parameter.getTaskId());
				insuranceWeiHaiHtml.setType("医疗保险");
				insuranceWeiHaiHtml.setUrl(loginurl);
				insuranceWeiHaiHtmlRepository.save(insuranceWeiHaiHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 6) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 险种标志
					String insurance = inputs.get(i + 1).val();
					System.out.println(insurance);
					// 单位缴费基数
					String company_pay_cardinal = inputs.get(i + 2).val();
					System.out.println(company_pay_cardinal);
					// 单位缴费额
					String companyPay = inputs.get(i + 3).val();
					System.out.println(companyPay);
					// 个人缴费基数
					String person_pay_cardinal = inputs.get(i + 4).val();
					System.out.println(person_pay_cardinal);
					// 个人缴费额
					String personPay = inputs.get(i + 5).val();
					System.out.println(personPay);
					InsuranceWeiHaiYibaoInfo insuranceWeiHaiYibaoInfo = new InsuranceWeiHaiYibaoInfo();
					insuranceWeiHaiYibaoInfo.setTaskid(parameter.getTaskId());
					insuranceWeiHaiYibaoInfo.setYearMonth(yearMonth);
					insuranceWeiHaiYibaoInfo.setInsurance(insurance);
					insuranceWeiHaiYibaoInfo.setCompany_pay_cardinal(company_pay_cardinal);
					insuranceWeiHaiYibaoInfo.setPerson_pay_cardinal(person_pay_cardinal);
					insuranceWeiHaiYibaoInfo.setCompanyPay(companyPay);
					insuranceWeiHaiYibaoInfo.setPersonPay(personPay);
					insuranceWeiHaiYibaoInfoRepository.save(insuranceWeiHaiYibaoInfo);
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
			e.printStackTrace();
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
		
		List<InsuranceWeiHaiImageInfo> findByTaskid = insuranceWeiHaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		
		try {
			String loginurl = "http://202.110.217.69:7001/hsp/siLost.do?method=queryZgShiyejf&nd="+year+"&_width=1344&_height=629&_random=0.026297706046107594&__usersession_uuid="+requestParameter+"&_laneID=1a3410c7-dc9c-4d3c-b0e8-a96f1547cb2c";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取失业数据成功！");
				
				InsuranceWeiHaiHtml insuranceWeiHaiHtml = new InsuranceWeiHaiHtml();
				insuranceWeiHaiHtml.setHtml(contentAsString);
				insuranceWeiHaiHtml.setPageCount(1);
				insuranceWeiHaiHtml.setTaskid(parameter.getTaskId());
				insuranceWeiHaiHtml.setType("失业保险");
				insuranceWeiHaiHtml.setUrl(loginurl);
				insuranceWeiHaiHtmlRepository.save(insuranceWeiHaiHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
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
					InsuranceWeiHaiShiyeInfo insuranceWeiHaiShiyeInfo = new InsuranceWeiHaiShiyeInfo();
					insuranceWeiHaiShiyeInfo.setTaskid(parameter.getTaskId());
					insuranceWeiHaiShiyeInfo.setYearMonth(yearMonth);
					insuranceWeiHaiShiyeInfo.setCompany(company);
					insuranceWeiHaiShiyeInfo.setPay_cardinal(pay_cardinal);
					insuranceWeiHaiShiyeInfo.setCompanyPay(companyPay);
					insuranceWeiHaiShiyeInfo.setPersonPay(personPay);
					insuranceWeiHaiShiyeInfoRepository.save(insuranceWeiHaiShiyeInfo);
					
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
			e.printStackTrace();
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
		List<InsuranceWeiHaiImageInfo> findByTaskid = insuranceWeiHaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://202.110.217.69:7001/hsp/siBirth.do?method=queryZgShengyujf&nd="+year+"&_width=1344&_height=629&_random=0.6278698829625315&__usersession_uuid="+requestParameter+"&_laneID=d36c933f-eb0c-4776-88a7-2c0515d1e1aa";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取生育数据成功！");
				
				InsuranceWeiHaiHtml insuranceWeiHaiHtml = new InsuranceWeiHaiHtml();
				insuranceWeiHaiHtml.setHtml(contentAsString);
				insuranceWeiHaiHtml.setPageCount(1);
				insuranceWeiHaiHtml.setTaskid(parameter.getTaskId());
				insuranceWeiHaiHtml.setType("生育保险");
				insuranceWeiHaiHtml.setUrl(loginurl);
				insuranceWeiHaiHtmlRepository.save(insuranceWeiHaiHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
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
					InsuranceWeiHaiShengYuInfo insuranceWeiHaiShengYuInfo = new InsuranceWeiHaiShengYuInfo();
					insuranceWeiHaiShengYuInfo.setTaskid(parameter.getTaskId());
					insuranceWeiHaiShengYuInfo.setYearMonth(yearMonth);
					insuranceWeiHaiShengYuInfo.setCompany(company);
					insuranceWeiHaiShengYuInfo.setPay_cardinal(pay_cardinal);
					insuranceWeiHaiShengYuInfo.setCompany_pay(companyPay);
					insuranceWeiHaiShengYuInfoRepository.save(insuranceWeiHaiShengYuInfo);
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
			e.printStackTrace();
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
		List<InsuranceWeiHaiImageInfo> findByTaskid = insuranceWeiHaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://202.110.217.69:7001/hsp/siHarm.do?method=queryZgGsjf&nd="+year+"&_width=1344&_height=629&_random=0.9257481884285841&__usersession_uuid="+requestParameter+"&_laneID=89f6bd8d-a675-4274-bee3-77f4db17b292";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取工伤数据成功！");
				
				InsuranceWeiHaiHtml insuranceWeiHaiHtml = new InsuranceWeiHaiHtml();
				insuranceWeiHaiHtml.setHtml(contentAsString);
				insuranceWeiHaiHtml.setPageCount(1);
				insuranceWeiHaiHtml.setTaskid(parameter.getTaskId());
				insuranceWeiHaiHtml.setType("工伤保险");
				insuranceWeiHaiHtml.setUrl(loginurl);
				insuranceWeiHaiHtmlRepository.save(insuranceWeiHaiHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				
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
					InsuranceWeiHaiGongShangInfo insuranceWeiHaiGongShangInfo = new InsuranceWeiHaiGongShangInfo();
					insuranceWeiHaiGongShangInfo.setTaskid(parameter.getTaskId());
					insuranceWeiHaiGongShangInfo.setYearMonth(yearMonth);
					insuranceWeiHaiGongShangInfo.setCompany(company);
					insuranceWeiHaiGongShangInfo.setPay_cardinal(pay_cardinal);
					insuranceWeiHaiGongShangInfo.setCompany_pay(companyPay);
					insuranceWeiHaiGongShangInfoRepository.save(insuranceWeiHaiGongShangInfo);
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
			e.printStackTrace();
			System.out.println("获取工伤保险信息数据异常！");
		}

		return "";
	}
}
