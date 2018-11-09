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
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangBaseInfo;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangHtml;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.weifang.InsuranceWeiFangYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.weifang.InsuranceWeiFangYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.weifang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.weifang" })
public class CrawlerBaseInfoService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceWeiFangHtmlRepository insuranceWeiFangHtmlRepository;
	@Autowired
	private InsuranceWeiFangBaseInfoRepository insuranceWeiFangBaseInfoRepository;
	@Autowired
	private InsuranceWeiFangYanglaoInfoRepository insuranceWeiFangYanglaoInfoRepository;
	@Autowired
	private InsuranceWeiFangYibaoInfoRepository insuranceWeiFangYibaoInfoRepository;
	@Autowired
	private InsuranceWeiFangShiyeInfoRepository insuranceWeiFangShiyeInfoRepository;
	@Autowired
	private InsuranceWeiFangShengYuInfoRepository insuranceWeiFangShengYuInfoRepository;
	@Autowired
	private InsuranceWeiFangGongShangInfoRepository insuranceWeiFangGongShangInfoRepository;
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
	public String crawlerBaseInfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,String sessionid) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析基本信息", parameter.toString());

		// 基本信息界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		
		try {
			String loginurl = "https://www.sdwfhrss.gov.cn/hsp/hspUser.do?method=fwdQueryPerInfo&_random=0.9546814274827082&__usersession_uuid="+sessionid+"&_laneID=2a681c73-f296-45d3-8790-9d0a82204523";						
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
			insuranceWeiFangHtml.setHtml(contentAsString);
			insuranceWeiFangHtml.setPageCount(1);
			insuranceWeiFangHtml.setTaskid(parameter.getTaskId());
			insuranceWeiFangHtml.setType("基本信息");
			insuranceWeiFangHtml.setUrl(loginurl);
			insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);

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

				InsuranceWeiFangBaseInfo insuranceWeiFangBaseInfo = new InsuranceWeiFangBaseInfo();
				insuranceWeiFangBaseInfo.setTaskid(parameter.getTaskId());
				insuranceWeiFangBaseInfo.setName(name);
				insuranceWeiFangBaseInfo.setCardId(cardId);
				insuranceWeiFangBaseInfo.setSex(sex);
				insuranceWeiFangBaseInfo.setBirthDate(birthDate);
				insuranceWeiFangBaseInfo.setHkxz(hkxz);
				insuranceWeiFangBaseInfo.setCulture(culture);
				insuranceWeiFangBaseInfo.setMaritalstatus(maritalstatus);
				insuranceWeiFangBaseInfo.setCommunicationaddr(communicationaddr);
				insuranceWeiFangBaseInfo.setHomeaddr(homeaddr);
				insuranceWeiFangBaseInfo.setNation(nation);
				insuranceWeiFangBaseInfo.setLinkman(linkman);
				insuranceWeiFangBaseInfo.setRegisterphone(registerphone);
				insuranceWeiFangBaseInfo.setAdministrative(administrative);
				insuranceWeiFangBaseInfo.setPostalservice(postalservice);
				insuranceWeiFangBaseInfo.setPermanentaddr(permanentaddr);
				insuranceWeiFangBaseInfoRepository.save(insuranceWeiFangBaseInfo);

				// 更新Task表为 基本信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhasestatus());
				taskInsurance.setUserInfoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取基本信息数据异常！");
			tracer.addTag("CrawlerBaseInfoService.Exception:基本信息爬取解析完成", e.getMessage());
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
	public String crawlerAgedInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance, String year,String sessionid) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析养老保险", parameter.toString());
		// 养老保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		
		try {
			String loginurl = "https://www.sdwfhrss.gov.cn/hsp/siAd.do?method=queryAgedPayHis&year="+year+"&_random=0.015180274914302627&__usersession_uuid="+sessionid+"&_laneID=73faa9ef-5707-47d2-b76f-43365ddefe55";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取养老数据成功！");
				
				InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
				insuranceWeiFangHtml.setHtml(contentAsString);
				insuranceWeiFangHtml.setPageCount(1);
				insuranceWeiFangHtml.setTaskid(parameter.getTaskId());
				insuranceWeiFangHtml.setType("养老保险");
				insuranceWeiFangHtml.setUrl(loginurl);
				insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位缴费基数
					String unint_cardinal = inputs.get(i + 1).val();
					System.out.println(unint_cardinal);
					// 单位缴费额
					String unint_charge = inputs.get(i + 2).val();
					System.out.println(unint_charge);
					// 个人缴费基数
					String persion_cardinal = inputs.get(i + 3).val();
					System.out.println(persion_cardinal);
					// 个人缴费额
					String persion_charge = inputs.get(i + 4).val();
					System.out.println(persion_charge);
				
					InsuranceWeiFangYanglaoInfo insuranceWeiFangYanglaoInfo = new InsuranceWeiFangYanglaoInfo();
					insuranceWeiFangYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceWeiFangYanglaoInfo.setYearMonth(yearMonth);
					insuranceWeiFangYanglaoInfo.setUnint_cardinal(unint_cardinal);
					insuranceWeiFangYanglaoInfo.setUnint_charge(unint_charge);
					insuranceWeiFangYanglaoInfo.setPersion_cardinal(persion_cardinal);
					insuranceWeiFangYanglaoInfo.setPersion_charge(persion_charge);
					insuranceWeiFangYanglaoInfoRepository.save(insuranceWeiFangYanglaoInfo);
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
			e.printStackTrace();
			tracer.addTag("crawlerAgedInsurance.Exception:基本信息爬取解析完成", e.getMessage());
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
			String year,String sessionid) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析医疗保险", parameter.toString());
		// 医疗保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String loginurl = "https://www.sdwfhrss.gov.cn/hsp/siMedi.do?method=queryMediPayHis&year="+year+"&_random=0.42587617123193966&__usersession_uuid="+sessionid+"&_laneID=73faa9ef-5707-47d2-b76f-43365ddefe55";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取医疗数据成功！");
				
				InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
				insuranceWeiFangHtml.setHtml(contentAsString);
				insuranceWeiFangHtml.setPageCount(1);
				insuranceWeiFangHtml.setTaskid(parameter.getTaskId());
				insuranceWeiFangHtml.setType("医疗保险");
				insuranceWeiFangHtml.setUrl(loginurl);
				insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 6) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);					
					// 险种标志
					String type = inputs.get(i + 1).val();
					System.out.println(type);
					// 单位缴费基数
					String unint_cardinal = inputs.get(i + 2).val();
					System.out.println(unint_cardinal);
					// 单位缴费额
					String unint_charge = inputs.get(i + 3).val();
					System.out.println(unint_charge);
					// 个人缴费基数
					String persion_cardinal = inputs.get(i + 4).val();
					System.out.println(persion_cardinal);
					// 个人缴费额
					String persion_charge = inputs.get(i + 5).val();
					System.out.println(persion_charge);
					InsuranceWeiFangYibaoInfo insuranceWeiFangYibaoInfo = new InsuranceWeiFangYibaoInfo();
					insuranceWeiFangYibaoInfo.setTaskid(parameter.getTaskId());
					insuranceWeiFangYibaoInfo.setType(type);
					insuranceWeiFangYibaoInfo.setUnint_cardinal(unint_cardinal);
					insuranceWeiFangYibaoInfo.setUnint_charge(unint_charge);
					insuranceWeiFangYibaoInfo.setPersion_cardinal(persion_cardinal);
					insuranceWeiFangYibaoInfo.setPersion_charge(persion_charge);
					insuranceWeiFangYibaoInfoRepository.save(insuranceWeiFangYibaoInfo);
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
			String year,String sessionid) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析失业保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			String loginurl = "https://www.sdwfhrss.gov.cn/hsp/siLost.do?method=queryLostPayHis&year="+year+"&_random=0.6522879063686793&__usersession_uuid="+sessionid+"&_laneID=73faa9ef-5707-47d2-b76f-43365ddefe55";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取失业数据成功！");
				
				InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
				insuranceWeiFangHtml.setHtml(contentAsString);
				insuranceWeiFangHtml.setPageCount(1);
				insuranceWeiFangHtml.setTaskid(parameter.getTaskId());
				insuranceWeiFangHtml.setType("失业保险");
				insuranceWeiFangHtml.setUrl(loginurl);
				insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 5) {
					// 缴费年月
					String yearMonth = inputs.get(i).val();
					System.out.println(yearMonth);
					// 单位名称
					String company_name = inputs.get(i + 1).val();
					System.out.println(company_name);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 单位交费额
					String company_pay = inputs.get(i + 3).val();
					System.out.println(company_pay);
					// 个人交费额
					String persion_pay = inputs.get(i + 4).val();
					System.out.println(persion_pay);
				
					InsuranceWeiFangShiyeInfo insuranceWeiFangShiyeInfo = new InsuranceWeiFangShiyeInfo();
					insuranceWeiFangShiyeInfo.setTaskid(parameter.getTaskId());
					insuranceWeiFangShiyeInfo.setYearMonth(yearMonth);
					insuranceWeiFangShiyeInfo.setCompany_name(company_name);
					insuranceWeiFangShiyeInfo.setPay_cardinal(pay_cardinal);
					insuranceWeiFangShiyeInfo.setCompany_pay(company_pay);	
					insuranceWeiFangShiyeInfo.setPersion_pay(persion_pay);
					insuranceWeiFangShiyeInfoRepository.save(insuranceWeiFangShiyeInfo);				
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
			String year,String sessionid) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析生育保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			String loginurl = "https://www.sdwfhrss.gov.cn/hsp/siBirth.do?method=queryBirthPayHis&year="+year+"&_random=0.15540882555200253&__usersession_uuid="+sessionid+"&_laneID=73faa9ef-5707-47d2-b76f-43365ddefe55";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取生育数据成功！");
				
				InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
				insuranceWeiFangHtml.setHtml(contentAsString);
				insuranceWeiFangHtml.setPageCount(1);
				insuranceWeiFangHtml.setTaskid(parameter.getTaskId());
				insuranceWeiFangHtml.setType("生育保险");
				insuranceWeiFangHtml.setUrl(loginurl);
				insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 4) {
					// 缴费年月
					String year_month = inputs.get(i).val();
					System.out.println(year_month);
					// 缴费日期
					String company_name = inputs.get(i + 1).val();
					System.out.println(company_name);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 缴费金额
					String pay_charge = inputs.get(i + 3).val();
					System.out.println(pay_charge);
					InsuranceWeiFangShengYuInfo insuranceWeiFangShengYuInfo = new InsuranceWeiFangShengYuInfo();
					insuranceWeiFangShengYuInfo.setTaskid(parameter.getTaskId());
					insuranceWeiFangShengYuInfo.setYear_month(year_month);
					insuranceWeiFangShengYuInfo.setCompany_name(company_name);
					insuranceWeiFangShengYuInfo.setPay_cardinal(pay_cardinal);
					insuranceWeiFangShengYuInfo.setPay_charge(pay_charge);
					insuranceWeiFangShengYuInfoRepository.save(insuranceWeiFangShengYuInfo);
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
			String year,String sessionid) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析工伤保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
	
		try {
			String loginurl = "https://www.sdwfhrss.gov.cn/hsp/siHarm.do?method=queryHarmPayHis&year="+year+"&_random=0.593444099359282&__usersession_uuid="+sessionid+"&_laneID=73faa9ef-5707-47d2-b76f-43365ddefe55";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("type=\"text\"")) {
				System.out.println("获取工伤数据成功！");
				
				InsuranceWeiFangHtml insuranceWeiFangHtml = new InsuranceWeiFangHtml();
				insuranceWeiFangHtml.setHtml(contentAsString);
				insuranceWeiFangHtml.setPageCount(1);
				insuranceWeiFangHtml.setTaskid(parameter.getTaskId());
				insuranceWeiFangHtml.setType("工伤保险");
				insuranceWeiFangHtml.setUrl(loginurl);
				insuranceWeiFangHtmlRepository.save(insuranceWeiFangHtml);

				Document doc = Jsoup.parse(contentAsString);
				Elements inputs = doc.getElementsByAttributeValue("type", "text");
				for (int i = 0; i < inputs.size(); i += 4) {
					// 缴费年月
					String year_month = inputs.get(i).val();
					System.out.println(year_month);
					// 公司名称
					String company_name = inputs.get(i + 1).val();
					System.out.println(company_name);
					// 缴费基数
					String pay_cardinal = inputs.get(i + 2).val();
					System.out.println(pay_cardinal);
					// 缴费金额
					String pay_charge = inputs.get(i + 3).val();
					System.out.println(pay_charge);
					InsuranceWeiFangGongShangInfo insuranceWeiFangGongShangInfo = new InsuranceWeiFangGongShangInfo();
					insuranceWeiFangGongShangInfo.setTaskid(parameter.getTaskId());
					insuranceWeiFangGongShangInfo.setYear_month(year_month);
					insuranceWeiFangGongShangInfo.setCompany_name(company_name);
					insuranceWeiFangGongShangInfo.setPay_cardinal(pay_cardinal);
					insuranceWeiFangGongShangInfo.setPay_charge(pay_charge);
					insuranceWeiFangGongShangInfoRepository.save(insuranceWeiFangGongShangInfo);
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
