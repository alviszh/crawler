package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongBaseInfo;
import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongHtml;
import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.nantong.InsuranceNanTongBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.nantong.InsuranceNanTongGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.nantong.InsuranceNanTongHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.nantong.InsuranceNanTongShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.nantong.InsuranceNanTongShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.nantong.InsuranceNanTongYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.nantong.InsuranceNanTongYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.nantong" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.nantong" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceNanTongHtmlRepository insuranceNanTongHtmlRepository;

	@Autowired
	private InsuranceNanTongBaseInfoRepository insuranceNanTongBaseInfoRepository;

	@Autowired
	private InsuranceNanTongYanglaoInfoRepository insuranceNanTongYanglaoInfoRepository;

	@Autowired
	private InsuranceNanTongYibaoInfoRepository insuranceNanTongYibaoInfoRepository;

	@Autowired
	private InsuranceNanTongShiyeInfoRepository insuranceNanTongShiyeInfoRepository;

	@Autowired
	private InsuranceNanTongShengYuInfoRepository insuranceNanTongShengYuInfoRepository;

	@Autowired
	private InsuranceNanTongGongShangInfoRepository insuranceNanTongGongShangInfoRepository;

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
			String loginurl = "http://www.jsnt.lss.gov.cn:1002/query/person/personINFO_query.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceNanTongHtml insuranceNanTongHtml = new InsuranceNanTongHtml();
			insuranceNanTongHtml.setHtml(contentAsString);
			insuranceNanTongHtml.setPageCount(1);
			insuranceNanTongHtml.setTaskid(parameter.getTaskId());
			insuranceNanTongHtml.setType("基本信息");
			insuranceNanTongHtml.setUrl(loginurl);
			insuranceNanTongHtmlRepository.save(insuranceNanTongHtml);

			Document doc = Jsoup.parse(contentAsString);
			// 个人编号
			Element AAC001 = doc.getElementById("AAC001");
			String grbh = AAC001.val();
			System.out.println("个人编号：" + grbh);
			// 身份证号码
			Element AAE135 = doc.getElementById("AAE135");
			String sfzhm = AAE135.val();
			System.out.println(" 身份证号码：" + sfzhm);
			//姓名
			Element AAC003 = doc.getElementById("AAC003");
			String xm = AAC003.val();
			System.out.println("姓名：" + xm);
			//性别
			String xb =	doc.getElementById("AAC004").getElementsByAttribute("selected").get(0).val(); 
			System.out.println("性别：" + xb);
			//民族
			String mz =	doc.getElementById("AAC005").getElementsByAttribute("selected").get(0).val(); 
			System.out.println("民族：" + mz);
			//出生日期
			Element AAC006 = doc.getElementById("AAC006");
			String csrq = AAC006.val();
			System.out.println("出生日期：" + csrq);
			String gwybs ="";
			try{
				//公务员标识
				gwybs =	doc.getElementById("BAC059").getElementsByAttribute("selected").get(0).val(); 
				System.out.println("公务员标识：" + gwybs);
			}catch (Exception e) {
				System.out.println("为空！没有被选择！");
				gwybs = "";
			}
			//社保卡号
			Element AAZ500 = doc.getElementById("AAZ500");
			String sbkh = AAZ500.val();
			System.out.println("社保卡号：" + sbkh);
			//离退休日期
			Element AIC162 = doc.getElementById("AIC162");
			String ltxrq = AIC162.val();
			System.out.println("离退休日期：" + ltxrq);
			//人员状态
			String ryzt =	doc.getElementById("AAC084").getElementsByAttribute("selected").get(0).val(); 
			System.out.println("人员状态：" + ryzt);
			//单位编号
			Element AAB001 = doc.getElementById("AAB001");
			String dwbh = AAB001.val();
			System.out.println("单位编号：" + dwbh);
			//单位名称
			Element AAB004 = doc.getElementById("AAB004");
			String dwmc = AAB004.val();
			System.out.println("单位名称：" + dwmc);

			InsuranceNanTongBaseInfo insuranceNanTongBaseInfo = new InsuranceNanTongBaseInfo();
			insuranceNanTongBaseInfo.setTaskid(parameter.getTaskId());
			insuranceNanTongBaseInfo.setDwmc(dwmc);
			insuranceNanTongBaseInfo.setDwbh(dwbh);
			insuranceNanTongBaseInfo.setRyzt(ryzt);
			insuranceNanTongBaseInfo.setLtxrq(ltxrq);
			insuranceNanTongBaseInfo.setSbkh(sbkh);
			insuranceNanTongBaseInfo.setGwybs(gwybs);
			insuranceNanTongBaseInfo.setCsrq(csrq);
			insuranceNanTongBaseInfo.setMz(mz);
			insuranceNanTongBaseInfo.setXb(xb);
			insuranceNanTongBaseInfo.setXm(xm);
			insuranceNanTongBaseInfo.setSfzhm(sfzhm);
			insuranceNanTongBaseInfo.setGrbh(grbh);
			insuranceNanTongBaseInfoRepository.save(insuranceNanTongBaseInfo);

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
	public String crawlerAgedInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析养老保险", parameter.toString());
		// 养老保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
			System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

			SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyyMM");
			Calendar calendar222 = Calendar.getInstance();
			calendar222.setTime(new Date());
			calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
			System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));

			String loginurl = "http://www.jsnt.lss.gov.cn:1002/query/person/personJFJL_result.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_b", dateFormat222.format(calendar222.getTime())));
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_e", dateFormat22.format(calendar22.getTime())));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae140", "11"));
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);
			if (contentAsString.contains("class=\"grid\"")) {
				System.out.println("获取养老数据成功！");

				InsuranceNanTongHtml insuranceNanTongHtml = new InsuranceNanTongHtml();
				insuranceNanTongHtml.setHtml(contentAsString);
				insuranceNanTongHtml.setPageCount(1);
				insuranceNanTongHtml.setTaskid(parameter.getTaskId());
				insuranceNanTongHtml.setType("养老保险");
				insuranceNanTongHtml.setUrl(loginurl);
				insuranceNanTongHtmlRepository.save(insuranceNanTongHtml);

				Document doc = Jsoup.parse(contentAsString);
				Element element = doc.getElementsByClass("grid").get(0);
				Elements trs = element.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					for (int j = 1; j < tds.size(); j+=8) {
						//对应期
						String dyq = tds.get(j).text();
						System.out.println("对应期------"+dyq);
						//缴费单位名称
						String jfdwmc = tds.get(j+1).text();
						System.out.println("缴费单位名称------"+jfdwmc);
						//险种类型
						String xzlx = tds.get(j+2).text();
						System.out.println("险种类型------"+xzlx);
						//缴费对象
						String jfdx = tds.get(j+3).text();
						System.out.println("缴费对象------"+jfdx);
						//缴费基数
						String jfjs = tds.get(j+4).text();
						System.out.println("缴费基数------"+jfjs);
						//缴纳金额
						String jnje = tds.get(j+5).text();
						System.out.println("缴纳金额------"+jnje);
						//缴费标志
						String jfbz = tds.get(j+6).text();
						System.out.println("缴费标志------"+jfbz);
						InsuranceNanTongYanglaoInfo insuranceNanTongYanglaoInfo = new InsuranceNanTongYanglaoInfo();
						insuranceNanTongYanglaoInfo.setTaskid(parameter.getTaskId());
						insuranceNanTongYanglaoInfo.setJfbz(jfbz);
						insuranceNanTongYanglaoInfo.setJnje(jnje);
						insuranceNanTongYanglaoInfo.setJfjs(jfjs);
						insuranceNanTongYanglaoInfo.setJfdx(jfdx);
						insuranceNanTongYanglaoInfo.setXzlx(xzlx);
						insuranceNanTongYanglaoInfo.setJfdwmc(jfdwmc);
						insuranceNanTongYanglaoInfo.setDyq(dyq);
						insuranceNanTongYanglaoInfoRepository.save(insuranceNanTongYanglaoInfo);
					}
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
	public String crawlerMedicalInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析医疗保险", parameter.toString());
		// 医疗保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
			System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

			SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyyMM");
			Calendar calendar222 = Calendar.getInstance();
			calendar222.setTime(new Date());
			calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
			System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));

			String loginurl = "http://www.jsnt.lss.gov.cn:1002/query/person/personJFJL_result.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_b", dateFormat222.format(calendar222.getTime())));
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_e", dateFormat22.format(calendar22.getTime())));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae140", "31"));
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("class=\"grid\"")) {
				System.out.println("获取医疗数据成功！");

				InsuranceNanTongHtml insuranceNanTongHtml = new InsuranceNanTongHtml();
				insuranceNanTongHtml.setHtml(contentAsString);
				insuranceNanTongHtml.setPageCount(1);
				insuranceNanTongHtml.setTaskid(parameter.getTaskId());
				insuranceNanTongHtml.setType("医疗保险");
				insuranceNanTongHtml.setUrl(loginurl);
				insuranceNanTongHtmlRepository.save(insuranceNanTongHtml);

				Document doc = Jsoup.parse(contentAsString);
				Element element = doc.getElementsByClass("grid").get(0);
				Elements trs = element.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					for (int j = 1; j < tds.size(); j+=8) {
						//对应期
						String dyq = tds.get(j).text();
						System.out.println("对应期------"+dyq);
						//缴费单位名称
						String jfdwmc = tds.get(j+1).text();
						System.out.println("缴费单位名称------"+jfdwmc);
						//险种类型
						String xzlx = tds.get(j+2).text();
						System.out.println("险种类型------"+xzlx);
						//缴费对象
						String jfdx = tds.get(j+3).text();
						System.out.println("缴费对象------"+jfdx);
						//缴费基数
						String jfjs = tds.get(j+4).text();
						System.out.println("缴费基数------"+jfjs);
						//缴纳金额
						String jnje = tds.get(j+5).text();
						System.out.println("缴纳金额------"+jnje);
						//缴费标志
						String jfbz = tds.get(j+6).text();
						System.out.println("缴费标志------"+jfbz);
						InsuranceNanTongYibaoInfo insuranceNanTongYibaoInfo = new InsuranceNanTongYibaoInfo();
						insuranceNanTongYibaoInfo.setTaskid(parameter.getTaskId());
						insuranceNanTongYibaoInfo.setJfbs(jfbz);
						insuranceNanTongYibaoInfo.setJnje(jnje);
						insuranceNanTongYibaoInfo.setJfjs(jfjs);
						insuranceNanTongYibaoInfo.setJfdx(jfdx);
						insuranceNanTongYibaoInfo.setXzlx(xzlx);
						insuranceNanTongYibaoInfo.setJfdwmc(jfdwmc);
						insuranceNanTongYibaoInfo.setDyq(dyq);
						insuranceNanTongYibaoInfoRepository.save(insuranceNanTongYibaoInfo);
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
	public String crawlerUnemploymentInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析失业保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}

		try {
			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
			System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

			SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyyMM");
			Calendar calendar222 = Calendar.getInstance();
			calendar222.setTime(new Date());
			calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
			System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));

			String loginurl = "http://www.jsnt.lss.gov.cn:1002/query/person/personJFJL_result.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_b", dateFormat222.format(calendar222.getTime())));
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_e", dateFormat22.format(calendar22.getTime())));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae140", "21"));
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);

			if (contentAsString.contains("class=\"grid\"")) {
				System.out.println("获取失业数据成功！");

				InsuranceNanTongHtml insuranceNanTongHtml = new InsuranceNanTongHtml();
				insuranceNanTongHtml.setHtml(contentAsString);
				insuranceNanTongHtml.setPageCount(1);
				insuranceNanTongHtml.setTaskid(parameter.getTaskId());
				insuranceNanTongHtml.setType("失业保险");
				insuranceNanTongHtml.setUrl(loginurl);
				insuranceNanTongHtmlRepository.save(insuranceNanTongHtml);

				Document doc = Jsoup.parse(contentAsString);
				Element element = doc.getElementsByClass("grid").get(0);
				Elements trs = element.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					for (int j = 1; j < tds.size(); j+=8) {
						//对应期
						String dyq = tds.get(j).text();
						System.out.println("对应期------"+dyq);
						//缴费单位名称
						String jfdwmc = tds.get(j+1).text();
						System.out.println("缴费单位名称------"+jfdwmc);
						//险种类型
						String xzlx = tds.get(j+2).text();
						System.out.println("险种类型------"+xzlx);
						//缴费对象
						String jfdx = tds.get(j+3).text();
						System.out.println("缴费对象------"+jfdx);
						//缴费基数
						String jfjs = tds.get(j+4).text();
						System.out.println("缴费基数------"+jfjs);
						//缴纳金额
						String jnje = tds.get(j+5).text();
						System.out.println("缴纳金额------"+jnje);
						//缴费标志
						String jfbz = tds.get(j+6).text();
						System.out.println("缴费标志------"+jfbz);
						InsuranceNanTongShiyeInfo insuranceNanTongShiyeInfo = new InsuranceNanTongShiyeInfo();
						insuranceNanTongShiyeInfo.setTaskid(parameter.getTaskId());
						insuranceNanTongShiyeInfo.setJfbs(jfbz);
						insuranceNanTongShiyeInfo.setJnje(jnje);
						insuranceNanTongShiyeInfo.setJfjs(jfjs);
						insuranceNanTongShiyeInfo.setJfdx(jfdx);
						insuranceNanTongShiyeInfo.setXzlx(xzlx);
						insuranceNanTongShiyeInfo.setJfdwmc(jfdwmc);
						insuranceNanTongShiyeInfo.setDyq(dyq);
						insuranceNanTongShiyeInfoRepository.save(insuranceNanTongShiyeInfo);
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
	public String crawlerShengyuInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析生育保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
			System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

			SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyyMM");
			Calendar calendar222 = Calendar.getInstance();
			calendar222.setTime(new Date());
			calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
			System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));

			String loginurl = "http://www.jsnt.lss.gov.cn:1002/query/person/personJFJL_result.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_b", dateFormat222.format(calendar222.getTime())));
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_e", dateFormat22.format(calendar22.getTime())));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae140", "51"));
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("class=\"grid\"")) {
				System.out.println("获取生育数据成功！");

				InsuranceNanTongHtml insuranceNanTongHtml = new InsuranceNanTongHtml();
				insuranceNanTongHtml.setHtml(contentAsString);
				insuranceNanTongHtml.setPageCount(1);
				insuranceNanTongHtml.setTaskid(parameter.getTaskId());
				insuranceNanTongHtml.setType("生育保险");
				insuranceNanTongHtml.setUrl(loginurl);
				insuranceNanTongHtmlRepository.save(insuranceNanTongHtml);

				Document doc = Jsoup.parse(contentAsString);
				Element element = doc.getElementsByClass("grid").get(0);
				Elements trs = element.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					for (int j = 1; j < tds.size(); j+=8) {
						//对应期
						String dyq = tds.get(j).text();
						System.out.println("对应期------"+dyq);
						//缴费单位名称
						String jfdwmc = tds.get(j+1).text();
						System.out.println("缴费单位名称------"+jfdwmc);
						//险种类型
						String xzlx = tds.get(j+2).text();
						System.out.println("险种类型------"+xzlx);
						//缴费对象
						String jfdx = tds.get(j+3).text();
						System.out.println("缴费对象------"+jfdx);
						//缴费基数
						String jfjs = tds.get(j+4).text();
						System.out.println("缴费基数------"+jfjs);
						//缴纳金额
						String jnje = tds.get(j+5).text();
						System.out.println("缴纳金额------"+jnje);
						//缴费标志
						String jfbz = tds.get(j+6).text();
						System.out.println("缴费标志------"+jfbz);
						InsuranceNanTongShengYuInfo insuranceNanTongShengYuInfo = new InsuranceNanTongShengYuInfo();
						insuranceNanTongShengYuInfo.setTaskid(parameter.getTaskId());
						insuranceNanTongShengYuInfo.setJfbs(jfbz);
						insuranceNanTongShengYuInfo.setJnje(jnje);
						insuranceNanTongShengYuInfo.setJfjs(jfjs);
						insuranceNanTongShengYuInfo.setJfdx(jfdx);
						insuranceNanTongShengYuInfo.setXzlx(xzlx);
						insuranceNanTongShengYuInfo.setJfdwmc(jfdwmc);
						insuranceNanTongShengYuInfo.setDyq(dyq);
						insuranceNanTongShengYuInfoRepository.save(insuranceNanTongShengYuInfo);
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
	public String crawlerGongshangInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析工伤保险", parameter.toString());
		// 失业保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {

			SimpleDateFormat dateFormat22 = new SimpleDateFormat("yyyyMM");
			Calendar calendar22 = Calendar.getInstance();
			calendar22.setTime(new Date());
			calendar22.set(Calendar.MONTH, calendar22.get(Calendar.MONTH));
			System.out.println("当前年月---" + dateFormat22.format(calendar22.getTime()));

			SimpleDateFormat dateFormat222 = new SimpleDateFormat("yyyyMM");
			Calendar calendar222 = Calendar.getInstance();
			calendar222.setTime(new Date());
			calendar222.set(Calendar.MONTH, calendar222.get(Calendar.MONTH) - 36);
			System.out.println("三年前---" + dateFormat222.format(calendar222.getTime()));

			String loginurl = "http://www.jsnt.lss.gov.cn:1002/query/person/personJFJL_result.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_b", dateFormat222.format(calendar222.getTime())));
			webRequestlogin.getRequestParameters()
					.add(new NameValuePair("aae002_e", dateFormat22.format(calendar22.getTime())));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae140", "41"));
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("class=\"grid\"")) {
				System.out.println("获取工伤数据成功！");

				InsuranceNanTongHtml insuranceNanTongHtml = new InsuranceNanTongHtml();
				insuranceNanTongHtml.setHtml(contentAsString);
				insuranceNanTongHtml.setPageCount(1);
				insuranceNanTongHtml.setTaskid(parameter.getTaskId());
				insuranceNanTongHtml.setType("工伤保险");
				insuranceNanTongHtml.setUrl(loginurl);
				insuranceNanTongHtmlRepository.save(insuranceNanTongHtml);

				Document doc = Jsoup.parse(contentAsString);
				Element element = doc.getElementsByClass("grid").get(0);
				Elements trs = element.getElementsByTag("tr");
				for (int i = 1; i < trs.size(); i++) {
					Element tr = trs.get(i);
					Elements tds = tr.getElementsByTag("td");
					for (int j = 1; j < tds.size(); j+=8) {
						//对应期
						String dyq = tds.get(j).text();
						System.out.println("对应期------"+dyq);
						//缴费单位名称
						String jfdwmc = tds.get(j+1).text();
						System.out.println("缴费单位名称------"+jfdwmc);
						//险种类型
						String xzlx = tds.get(j+2).text();
						System.out.println("险种类型------"+xzlx);
						//缴费对象
						String jfdx = tds.get(j+3).text();
						System.out.println("缴费对象------"+jfdx);
						//缴费基数
						String jfjs = tds.get(j+4).text();
						System.out.println("缴费基数------"+jfjs);
						//缴纳金额
						String jnje = tds.get(j+5).text();
						System.out.println("缴纳金额------"+jnje);
						//缴费标志
						String jfbz = tds.get(j+6).text();
						System.out.println("缴费标志------"+jfbz);
						InsuranceNanTongGongShangInfo insuranceNanTongGongShangInfo = new InsuranceNanTongGongShangInfo();
						insuranceNanTongGongShangInfo.setTaskid(parameter.getTaskId());
						insuranceNanTongGongShangInfo.setJfbs(jfbz);
						insuranceNanTongGongShangInfo.setJnje(jnje);
						insuranceNanTongGongShangInfo.setJfjs(jfjs);
						insuranceNanTongGongShangInfo.setJfdx(jfdx);
						insuranceNanTongGongShangInfo.setXzlx(xzlx);
						insuranceNanTongGongShangInfo.setJfdwmc(jfdwmc);
						insuranceNanTongGongShangInfo.setDyq(dyq);
						insuranceNanTongGongShangInfoRepository.save(insuranceNanTongGongShangInfo);
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
			}

		} catch (Exception e) {
			System.out.println("获取工伤保险信息数据异常！");
		}

		return "";
	}
}
