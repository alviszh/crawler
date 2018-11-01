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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiBaseInfo;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiGongshangInfo;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiHtml;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiImageInfo;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiShengyuInfo;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiGongshangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiImageInfoRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiShengyuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.yantai.InsuranceYantaiYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.yantai" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.yantai" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceYantaiBaseInfoRepository insuranceYantaiBaseInfoRepository;
	@Autowired
	private InsuranceYantaiGongshangInfoRepository insuranceYantaiGongshangInfoRepository;
	@Autowired
	private InsuranceYantaiHtmlRepository insuranceYantaiHtmlRepository;
	@Autowired
	private InsuranceYantaiImageInfoRepository insuranceYantaiImageInfoRepository;
	@Autowired
	private InsuranceYantaiShengyuInfoRepository insuranceYantaiShengyuInfoRepository;
	@Autowired
	private InsuranceYantaiShiyeInfoRepository insuranceYantaiShiyeInfoRepository;
	@Autowired
	private InsuranceYantaiYanglaoInfoRepository insuranceYantaiYanglaoInfoRepository;
	@Autowired
	private InsuranceYantaiYibaoInfoRepository insuranceYantaiYibaoInfoRepository;

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

		List<InsuranceYantaiImageInfo> findByTaskid = insuranceYantaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String __usersession_uuid = findByTaskid.get(0).getRequestParameter().trim();

		try {
			String loginurl = "http://ytrsj.gov.cn:8081/hsp/hspUser.do";
			WebRequest requestSettings = new WebRequest(new URL(loginurl), HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "fwdQueryPerInfo"));
			requestSettings.getRequestParameters().add(new NameValuePair(
					"crypto_4f453d15abc17b6bc2c1eafed8b7497b1148cc85-24ec-4b8f-a5d6-9a2e5a414e05",
					"crypto_6cbe8616793922c38c92cc46fa92eeacf9ed7ffc94b1c79be9acf29dc1dcd156a00bc940-42c3-4106-90eb-fc3b736c809e"));
			requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", __usersession_uuid));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("_laneID", "af26b486-9e5c-4534-8990-ae267b9303ac"));
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceYantaiHtml insuranceYanTaiHtml = new InsuranceYantaiHtml();
			insuranceYanTaiHtml.setHtml(contentAsString);
			insuranceYanTaiHtml.setPageCount(1);
			insuranceYanTaiHtml.setTaskid(parameter.getTaskId());
			insuranceYanTaiHtml.setType("基本信息");
			insuranceYanTaiHtml.setUrl(loginurl);
			insuranceYantaiHtmlRepository.save(insuranceYanTaiHtml);

			Document doc = Jsoup.parse(contentAsString);
			Element elementById = doc.getElementById("xm");
			if (null == elementById && "".equals(elementById)) {
				System.out.println("获取基本信息数据失败！");
			} else {
				System.out.println("获取基本信息数据成功！");
				// 身份证
				String cardId = doc.getElementById("sfzhm").val();
				System.out.println("身份证-----" + cardId);
				// 姓名
				String name = elementById.val();
				System.out.println("姓名-----" + name);
				// 性别
				String sex = doc.getElementById("xbmc").val();
				System.out.println("性别-----" + sex);
				// 出生日期
				String birthDate = doc.getElementById("csrq").val();
				System.out.println("出生日期-----" + birthDate);
				// 民族
				String nation = doc.getElementById("mzmc").val();
				System.out.println("民族-----" + nation);
				// 单位编号
				String dwbh = doc.getElementById("dwbh").val();
				System.out.println("单位编号-----" + dwbh);
				// 单位名称
				String dwmc = doc.getElementById("dwmc").val();
				System.out.println("单位名称-----" + dwmc);
				// 职工类别
				String zglb = doc.getElementById("zglb").val();
				System.out.println("职工类别-----" + zglb);
				// 投保类别
				String tblb = doc.getElementById("tblbmc").val();
				System.out.println("投保类别-----" + tblb);
				// 通讯地址
				String communicationaddr = doc.getElementById("txdz").val();
				System.out.println("通讯地址-----" + communicationaddr);
				// 联系人
				String linkman = doc.getElementById("lxr").val();
				System.out.println("联系人-----" + linkman);
				// 手机号码
				String phone = doc.getElementById("sjhm").val();
				System.out.println("手机号码-----" + phone);
				// 离退休日期
				String ltxrq = doc.getElementById("ltxrq").val();
				System.out.println("离退休日期-----" + ltxrq);
				// 离退休类别
				String ltxlb = doc.getElementById("ltxlbmc").val();
				System.out.println("离退休类别-----" + ltxlb);

				InsuranceYantaiBaseInfo insuranceYanTaiBaseInfo = new InsuranceYantaiBaseInfo();
				insuranceYanTaiBaseInfo.setTaskid(parameter.getTaskId());
				insuranceYanTaiBaseInfo.setName(name);
				insuranceYanTaiBaseInfo.setCardId(cardId);
				insuranceYanTaiBaseInfo.setSex(sex);
				insuranceYanTaiBaseInfo.setBirthDate(birthDate);
				insuranceYanTaiBaseInfo.setLtxlb(ltxlb);
				insuranceYanTaiBaseInfo.setLtxrq(ltxrq);
				insuranceYanTaiBaseInfo.setPhone(phone);
				insuranceYanTaiBaseInfo.setLinkman(linkman);
				insuranceYanTaiBaseInfo.setCommunicationaddr(communicationaddr);
				insuranceYanTaiBaseInfo.setTblb(tblb);
				insuranceYanTaiBaseInfo.setZglb(zglb);
				insuranceYanTaiBaseInfo.setDwmc(dwmc);
				insuranceYanTaiBaseInfo.setDwbh(dwbh);
				insuranceYanTaiBaseInfo.setNation(nation);

				insuranceYantaiBaseInfoRepository.save(insuranceYanTaiBaseInfo);

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

		List<InsuranceYantaiImageInfo> findByTaskid = insuranceYantaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String __usersession_uuid = findByTaskid.get(0).getRequestParameter().trim();

		try {
			String loginurl = "http://ytrsj.gov.cn:8081/hsp/siAd.do";
			WebRequest requestSettings = new WebRequest(new URL(loginurl), HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "queryAgedPayHis"));
			requestSettings.getRequestParameters().add(new NameValuePair(
					"crypto_007562a8203b1676eba5533bf1864235e31e6cbe-af74-469a-8db3-68bbdd4ca06b",
					"crypto_2fe77d45434587148c70265d78d0dfb305928383cd3cf21f3db2e60ab11c893aa69676e0-b819-4e87-ba14-d93d0aaa707c"));
			requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", __usersession_uuid));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("_laneID", "af26b486-9e5c-4534-8990-ae267b9303ac"));
			HtmlPage pagelogin = webClient.getPage(requestSettings);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			InsuranceYantaiHtml insuranceYanTaiHtml = new InsuranceYantaiHtml();
			insuranceYanTaiHtml.setHtml(contentAsString);
			insuranceYanTaiHtml.setPageCount(1);
			insuranceYanTaiHtml.setTaskid(parameter.getTaskId());
			insuranceYanTaiHtml.setType("养老保险");
			insuranceYanTaiHtml.setUrl(loginurl);
			insuranceYantaiHtmlRepository.save(insuranceYanTaiHtml);

			if (contentAsString.contains("name=\"agedpayhis\"")) {
				System.out.println("获取养老数据成功！");

				Document doc = Jsoup.parse(contentAsString);
				Elements elementById = doc.getElementsByAttributeValue("name", "agedpayhis");
				Elements elementsByAttribute = elementById.get(0).getElementsByTag("tr");
				for (int i = 1; i < elementsByAttribute.size(); i++) {
					Element element = elementsByAttribute.get(i);
					Elements elementsByTag = element.getElementsByTag("td");
					// 险种
					String name = elementsByTag.get(0).getElementsByTag("input").val();
					System.out.println("险种-----" + name);
					// 年月
					String yearMonth = elementsByTag.get(1).getElementsByTag("input").val();
					System.out.println("年月-----" + yearMonth);
					// 缴费基数
					String pay_cardinal = elementsByTag.get(2).getElementsByTag("input").val();
					System.out.println("缴费基数-----" + pay_cardinal);
					// 个人交
					String personal_pay = elementsByTag.get(4).getElementsByTag("input").val();
					System.out.println("个人交-----" + personal_pay);
					// 单位编号
					String unit_no = elementsByTag.get(5).getElementsByTag("input").val();
					System.out.println("单位编号-----" + unit_no);
					// 单位名称
					String unit_name = elementsByTag.get(6).getElementsByTag("input").val();
					System.out.println("单位名称-----" + unit_name);

					InsuranceYantaiYanglaoInfo insuranceYantaiYanglaoInfo = new InsuranceYantaiYanglaoInfo();
					insuranceYantaiYanglaoInfo.setTaskid(parameter.getTaskId());
					insuranceYantaiYanglaoInfo.setName(name);
					insuranceYantaiYanglaoInfo.setYearMonth(yearMonth);
					insuranceYantaiYanglaoInfo.setPayCardinal(pay_cardinal);
					insuranceYantaiYanglaoInfo.setPersonalPay(personal_pay);
					insuranceYantaiYanglaoInfo.setUnitNo(unit_no);
					insuranceYantaiYanglaoInfo.setUnitName(unit_name);

					insuranceYantaiYanglaoInfoRepository.save(insuranceYantaiYanglaoInfo);

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

		List<InsuranceYantaiImageInfo> findByTaskid = insuranceYantaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			String loginurl = "http://ytrsj.gov.cn:8081/hsp/siMedi.do";
			WebRequest requestSettings = new WebRequest(new URL(loginurl), HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "queryMediPayHis"));
			requestSettings.getRequestParameters().add(new NameValuePair("year", "2018"));
			requestSettings.getRequestParameters().add(new NameValuePair(
					"crypto_007562a8203b1676eba5533bf1864235e31e6cbe-af74-469a-8db3-68bbdd4ca06b",
					"crypto_2fe77d45434587148c70265d78d0dfb305928383cd3cf21f3db2e60ab11c893aa69676e0-b819-4e87-ba14-d93d0aaa707c"));
			requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", requestParameter));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("_laneID", "af26b486-9e5c-4534-8990-ae267b9303ac"));
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);
			InsuranceYantaiHtml insuranceYanTaiHtml = new InsuranceYantaiHtml();
			insuranceYanTaiHtml.setHtml(contentAsString);
			insuranceYanTaiHtml.setPageCount(1);
			insuranceYanTaiHtml.setTaskid(parameter.getTaskId());
			insuranceYanTaiHtml.setType("医疗保险");
			insuranceYanTaiHtml.setUrl(loginurl);
			insuranceYantaiHtmlRepository.save(insuranceYanTaiHtml);

			if (contentAsString.contains("class=\"defaultTableClass\"")) {
				System.out.println("获取医疗数据成功！");

				Document doc = Jsoup.parse(contentAsString);
				Elements elementById = doc.getElementsByAttributeValue("class", "defaultTableClass");
				Elements elementsByAttribute = elementById.get(0).getElementsByTag("tr");
				for (int i = 1; i < elementsByAttribute.size(); i++) {
					Element element = elementsByAttribute.get(i);
					Elements elementsByTag = element.getElementsByTag("td");
					// 险种
					String name = elementsByTag.get(0).getElementsByTag("input").val();
					System.out.println("险种-----" + name);
					// 年月
					String yearMonth = elementsByTag.get(1).getElementsByTag("input").val();
					System.out.println("年月-----" + yearMonth);
					// 缴费基数
					String pay_cardinal = elementsByTag.get(2).getElementsByTag("input").val();
					System.out.println("缴费基数-----" + pay_cardinal);
					// 个人交
					String personal_pay = elementsByTag.get(4).getElementsByTag("input").val();
					System.out.println("个人交-----" + personal_pay);
					// 单位编号
					String unit_no = elementsByTag.get(5).getElementsByTag("input").val();
					System.out.println("单位编号-----" + unit_no);
					// 单位名称
					String unit_name = elementsByTag.get(6).getElementsByTag("input").val();
					System.out.println("单位名称-----" + unit_name);

					InsuranceYantaiYibaoInfo insuranceYantaiYibaoInfo = new InsuranceYantaiYibaoInfo();
					insuranceYantaiYibaoInfo.setTaskid(parameter.getTaskId());
					insuranceYantaiYibaoInfo.setName(name);
					insuranceYantaiYibaoInfo.setYearMonth(yearMonth);
					insuranceYantaiYibaoInfo.setPayCardinal(pay_cardinal);
					insuranceYantaiYibaoInfo.setPersonalPay(personal_pay);
					insuranceYantaiYibaoInfo.setUnitNo(unit_no);
					insuranceYantaiYibaoInfo.setUnitName(unit_name);

					insuranceYantaiYibaoInfoRepository.save(insuranceYantaiYibaoInfo);

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

		List<InsuranceYantaiImageInfo> findByTaskid = insuranceYantaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();

		try {

			String loginurl = "http://ytrsj.gov.cn:8081/hsp/siLost.do";
			WebRequest requestSettings = new WebRequest(new URL(loginurl), HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "queryLostPayHis"));
			requestSettings.getRequestParameters().add(new NameValuePair("year", "2018"));
			requestSettings.getRequestParameters().add(new NameValuePair(
					"crypto_007562a8203b1676eba5533bf1864235e31e6cbe-af74-469a-8db3-68bbdd4ca06b",
					"crypto_2fe77d45434587148c70265d78d0dfb305928383cd3cf21f3db2e60ab11c893aa69676e0-b819-4e87-ba14-d93d0aaa707c"));
			requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", requestParameter));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("_laneID", "af26b486-9e5c-4534-8990-ae267b9303ac"));
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			InsuranceYantaiHtml insuranceYanTaiHtml = new InsuranceYantaiHtml();
			insuranceYanTaiHtml.setHtml(contentAsString);
			insuranceYanTaiHtml.setPageCount(1);
			insuranceYanTaiHtml.setTaskid(parameter.getTaskId());
			insuranceYanTaiHtml.setType("失业保险");
			insuranceYanTaiHtml.setUrl(loginurl);
			insuranceYantaiHtmlRepository.save(insuranceYanTaiHtml);

			if (contentAsString.contains("class=\"defaultTableClass\"")) {
				System.out.println("获取失业数据成功！");

				Document doc = Jsoup.parse(contentAsString);
				Elements elementById = doc.getElementsByAttributeValue("class", "defaultTableClass");
				Elements elementsByAttribute = elementById.get(0).getElementsByTag("tr");
				for (int i = 1; i < elementsByAttribute.size(); i++) {
					Element element = elementsByAttribute.get(i);
					Elements elementsByTag = element.getElementsByTag("td");
					// 险种
					String name = elementsByTag.get(0).getElementsByTag("input").val();
					System.out.println("险种-----" + name);
					// 年月
					String yearMonth = elementsByTag.get(1).getElementsByTag("input").val();
					System.out.println("年月-----" + yearMonth);
					// 缴费基数
					String pay_cardinal = elementsByTag.get(2).getElementsByTag("input").val();
					System.out.println("缴费基数-----" + pay_cardinal);
					// 个人交
					String personal_pay = elementsByTag.get(4).getElementsByTag("input").val();
					System.out.println("个人交-----" + personal_pay);
					// 单位编号
					String unit_no = elementsByTag.get(5).getElementsByTag("input").val();
					System.out.println("单位编号-----" + unit_no);
					// 单位名称
					String unit_name = elementsByTag.get(6).getElementsByTag("input").val();
					System.out.println("单位名称-----" + unit_name);

					InsuranceYantaiShiyeInfo insuranceYantaiShiyeInfo = new InsuranceYantaiShiyeInfo();
					insuranceYantaiShiyeInfo.setTaskid(parameter.getTaskId());
					insuranceYantaiShiyeInfo.setName(name);
					insuranceYantaiShiyeInfo.setYearMonth(yearMonth);
					insuranceYantaiShiyeInfo.setPayCardinal(pay_cardinal);
					insuranceYantaiShiyeInfo.setPersonalPay(personal_pay);
					insuranceYantaiShiyeInfo.setUnitNo(unit_no);
					insuranceYantaiShiyeInfo.setUnitName(unit_name);

					insuranceYantaiShiyeInfoRepository.save(insuranceYantaiShiyeInfo);

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
		List<InsuranceYantaiImageInfo> findByTaskid = insuranceYantaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {

			String loginurl = "http://ytrsj.gov.cn:8081/hsp/siBirth.do";
			WebRequest requestSettings = new WebRequest(new URL(loginurl), HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "queryBirthPayHis"));
			requestSettings.getRequestParameters().add(new NameValuePair("year", "2018"));
			requestSettings.getRequestParameters().add(new NameValuePair(
					"crypto_007562a8203b1676eba5533bf1864235e31e6cbe-af74-469a-8db3-68bbdd4ca06b",
					"crypto_2fe77d45434587148c70265d78d0dfb305928383cd3cf21f3db2e60ab11c893aa69676e0-b819-4e87-ba14-d93d0aaa707c"));
			requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", requestParameter));
			requestSettings.getRequestParameters()
					.add(new NameValuePair("_laneID", "af26b486-9e5c-4534-8990-ae267b9303ac"));
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();

			InsuranceYantaiHtml insuranceYanTaiHtml = new InsuranceYantaiHtml();
			insuranceYanTaiHtml.setHtml(contentAsString);
			insuranceYanTaiHtml.setPageCount(1);
			insuranceYanTaiHtml.setTaskid(parameter.getTaskId());
			insuranceYanTaiHtml.setType("生育保险");
			insuranceYanTaiHtml.setUrl(loginurl);
			insuranceYantaiHtmlRepository.save(insuranceYanTaiHtml);

			if (contentAsString.contains("class=\"defaultTableClass\"")) {
				System.out.println("获取生育数据成功！");

				Document doc = Jsoup.parse(contentAsString);
				Elements elementById = doc.getElementsByAttributeValue("class", "defaultTableClass");
				Elements elementsByAttribute = elementById.get(0).getElementsByTag("tr");
				for (int i = 1; i < elementsByAttribute.size(); i++) {
					Element element = elementsByAttribute.get(i);
					Elements elementsByTag = element.getElementsByTag("td");
					// 险种
					String name = elementsByTag.get(0).getElementsByTag("input").val();
					System.out.println("险种-----" + name);
					// 年月
					String yearMonth = elementsByTag.get(1).getElementsByTag("input").val();
					System.out.println("年月-----" + yearMonth);
					// 缴费基数
					String pay_cardinal = elementsByTag.get(2).getElementsByTag("input").val();
					System.out.println("缴费基数-----" + pay_cardinal);
					// 个人交
					String personal_pay = elementsByTag.get(4).getElementsByTag("input").val();
					System.out.println("个人交-----" + personal_pay);
					// 单位编号
					String unit_no = elementsByTag.get(5).getElementsByTag("input").val();
					System.out.println("单位编号-----" + unit_no);
					// 单位名称
					String unit_name = elementsByTag.get(6).getElementsByTag("input").val();
					System.out.println("单位名称-----" + unit_name);

					InsuranceYantaiShengyuInfo insuranceYantaiShengyuInfo = new InsuranceYantaiShengyuInfo();
					insuranceYantaiShengyuInfo.setTaskid(parameter.getTaskId());
					insuranceYantaiShengyuInfo.setName(name);
					insuranceYantaiShengyuInfo.setYearMonth(yearMonth);
					insuranceYantaiShengyuInfo.setPayCardinal(pay_cardinal);
					insuranceYantaiShengyuInfo.setPersonalPay(personal_pay);
					insuranceYantaiShengyuInfo.setUnitNo(unit_no);
					insuranceYantaiShengyuInfo.setUnitName(unit_name);

					insuranceYantaiShengyuInfoRepository.save(insuranceYantaiShengyuInfo);

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
		List<InsuranceYantaiImageInfo> findByTaskid = insuranceYantaiImageInfoRepository
				.findByTaskid(parameter.getTaskId().trim());
		String requestParameter = findByTaskid.get(0).getRequestParameter().trim();
		try {
			
			String loginurl = "http://ytrsj.gov.cn:8081/hsp/siHarm.do";
			WebRequest requestSettings = new WebRequest(new URL(loginurl),
					HttpMethod.POST);
			requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
			requestSettings.getRequestParameters().add(new NameValuePair("method", "queryHarmPayHis"));
			requestSettings.getRequestParameters().add(new NameValuePair("year", "2018"));
			requestSettings.getRequestParameters().add(new NameValuePair("crypto_007562a8203b1676eba5533bf1864235e31e6cbe-af74-469a-8db3-68bbdd4ca06b", "crypto_2fe77d45434587148c70265d78d0dfb305928383cd3cf21f3db2e60ab11c893aa69676e0-b819-4e87-ba14-d93d0aaa707c"));
			requestSettings.getRequestParameters().add(new NameValuePair("__usersession_uuid", requestParameter));
			requestSettings.getRequestParameters().add(new NameValuePair("_laneID", "af26b486-9e5c-4534-8990-ae267b9303ac"));
			HtmlPage loginedPage = webClient.getPage(requestSettings);
			String contentAsString = loginedPage.getWebResponse().getContentAsString();
			
			InsuranceYantaiHtml insuranceYanTaiHtml = new InsuranceYantaiHtml();
			insuranceYanTaiHtml.setHtml(contentAsString);
			insuranceYanTaiHtml.setPageCount(1);
			insuranceYanTaiHtml.setTaskid(parameter.getTaskId());
			insuranceYanTaiHtml.setType("工伤保险");
			insuranceYanTaiHtml.setUrl(loginurl);
			insuranceYantaiHtmlRepository.save(insuranceYanTaiHtml);

			if (contentAsString.contains("class=\"defaultTableClass\"")) {
				System.out.println("获取工伤数据成功！");

				Document doc = Jsoup.parse(contentAsString);
				Elements elementById = doc.getElementsByAttributeValue("class","defaultTableClass");
				Elements elementsByAttribute = elementById.get(0).getElementsByTag("tr");
				for (int i = 1; i < elementsByAttribute.size(); i++) {
					Element element = elementsByAttribute.get(i);
					Elements elementsByTag = element.getElementsByTag("td");
					//险种
					String name = elementsByTag.get(0).getElementsByTag("input").val();
					System.out.println("险种-----"+name);
					//年月
					String yearMonth = elementsByTag.get(1).getElementsByTag("input").val();
					System.out.println("年月-----"+yearMonth);
					//缴费基数
					String pay_cardinal = elementsByTag.get(2).getElementsByTag("input").val();
					System.out.println("缴费基数-----"+pay_cardinal);
					//个人交
					String personal_pay = elementsByTag.get(4).getElementsByTag("input").val();
					System.out.println("个人交-----"+personal_pay);
					//单位编号
					String unit_no = elementsByTag.get(5).getElementsByTag("input").val();
					System.out.println("单位编号-----"+unit_no);
					//单位名称
					String unit_name = elementsByTag.get(6).getElementsByTag("input").val();
					System.out.println("单位名称-----"+unit_name);
					
					
					InsuranceYantaiGongshangInfo insuranceYantaiGongshangInfo = new InsuranceYantaiGongshangInfo();
					insuranceYantaiGongshangInfo.setTaskid(parameter.getTaskId());
					insuranceYantaiGongshangInfo.setName(name);
					insuranceYantaiGongshangInfo.setYearMonth(yearMonth);
					insuranceYantaiGongshangInfo.setPayCardinal(pay_cardinal);
					insuranceYantaiGongshangInfo.setPersonalPay(personal_pay);
					insuranceYantaiGongshangInfo.setUnitNo(unit_no);
					insuranceYantaiGongshangInfo.setUnitName(unit_name);

					insuranceYantaiGongshangInfoRepository.save(insuranceYantaiGongshangInfo);
					
					
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
