package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangBaseInfo;
import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangHtml;
import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.fuyang.InsurancefuyangYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.fuyang.InsurancefuyangBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.fuyang.InsurancefuyangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.fuyang.InsurancefuyangYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.fuyang.InsurancefuyangYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.fuyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.fuyang" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsurancefuyangHtmlRepository insurancefuyangHtmlRepository;

	@Autowired
	private InsurancefuyangBaseInfoRepository insurancefuyangBaseInfoRepository;

	@Autowired
	private InsurancefuyangYanglaoInfoRepository insurancefuyangYanglaoInfoRepository;

	@Autowired
	private InsurancefuyangYibaoInfoRepository insurancefuyangYibaoInfoRepository;

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
			String loginurl = "http://218.23.30.50:8003/person/personBaseInfo.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsurancefuyangHtml insurancefuyangHtml = new InsurancefuyangHtml();
			insurancefuyangHtml.setHtml(contentAsString);
			insurancefuyangHtml.setPageCount(1);
			insurancefuyangHtml.setTaskid(parameter.getTaskId());
			insurancefuyangHtml.setType("基本信息");
			insurancefuyangHtml.setUrl(loginurl);
			insurancefuyangHtmlRepository.save(insurancefuyangHtml);

			Document doc = Jsoup.parse(contentAsString);
			// 个人编号
			String grbh = doc.getElementById("aac001").val().trim();
			System.out.println("个人编号：" + grbh);
			// 姓名
			String xm = doc.getElementById("aac003").val().trim();
			System.out.println("姓名：" + xm);
			// 性别
			String xb = doc.getElementById("aac004").getElementsByAttribute("selected").get(0).text();
			System.out.println("性别：" + xb);
			// 身份证号
			String sfzh = doc.getElementById("aac002").val().trim();
			System.out.println("身份证号：" + sfzh);
			// 出生年月
			String csny = doc.getElementById("aac006").val().trim();
			System.out.println("出生年月：" + csny);
			// 民族
			String mz = doc.getElementById("aac005").getElementsByAttribute("selected").get(0).text();
			System.out.println("民族：" + mz);
			// 参加工作时间
			String cjgzsj = doc.getElementById("aac007").val().trim();
			System.out.println("参加工作时间：" + cjgzsj);
			// 人员状态
			String ryzt = doc.getElementById("aac008").getElementsByAttribute("selected").get(0).text();
			System.out.println("人员状态：" + ryzt);
			// 个人身份
			String grsf = doc.getElementById("aac012").getElementsByAttribute("selected").get(0).text();
			System.out.println("个人身份：" + grsf);
			// 医疗待遇类别
			String yldylb = doc.getElementById("akc021").getElementsByAttribute("selected").get(0).text();
			System.out.println("医疗待遇类别：" + yldylb);
			// 医保卡号
			String ybkh = doc.getElementById("akc020").val().trim();
			System.out.println("医保卡号：" + ybkh);
			// 退休时间
			String txsj = doc.getElementById("aic162").val().trim();
			System.out.println("退休时间：" + txsj);
			InsurancefuyangBaseInfo insurancefuyangBaseInfo = new InsurancefuyangBaseInfo();
			insurancefuyangBaseInfo.setTaskid(parameter.getTaskId());
			insurancefuyangBaseInfo.setTxsj(txsj);
			insurancefuyangBaseInfo.setYbkh(ybkh);
			insurancefuyangBaseInfo.setYldylb(yldylb);
			insurancefuyangBaseInfo.setGrsf(grsf);
			insurancefuyangBaseInfo.setRyzt(ryzt);
			insurancefuyangBaseInfo.setCjgzsj(cjgzsj);
			insurancefuyangBaseInfo.setMz(mz);
			insurancefuyangBaseInfo.setCsny(csny);
			insurancefuyangBaseInfo.setSfzh(sfzh);
			insurancefuyangBaseInfo.setXb(xb);
			insurancefuyangBaseInfo.setXm(xm);
			insurancefuyangBaseInfo.setGrbh(grbh);
			insurancefuyangBaseInfoRepository.save(insurancefuyangBaseInfo);

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

			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -0);
			String beforeMonth = df.format(c.getTime());
			// 获取当前的前3年
			SimpleDateFormat df1 = new SimpleDateFormat("yyyyMM");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -3);
			String beforeMonth1 = df1.format(c1.getTime());

			String loginurl = "http://218.23.30.50:8003/person/personaccount_YLao_result.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters().add(new NameValuePair("pageNo", "1"));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae003", beforeMonth1));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae003e", beforeMonth));
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();

			Document doc = Jsoup.parse(contentAsString);
			String text = doc.getElementsByClass("page_num").get(0).text();
			String[] split = text.split("/");
			String[] split2 = split[1].split("页");
			String pages = split2[0].trim();
			int intpages = Integer.parseInt(pages);
			for (int i = 1; i <= intpages; i++) {
				// 获取当前的年
				SimpleDateFormat df4 = new SimpleDateFormat("yyyyMM");
				Calendar c4 = Calendar.getInstance();
				c4.add(Calendar.YEAR, -0);
				String beforeMonth4 = df4.format(c4.getTime());
				// 获取当前的前3年
				SimpleDateFormat df14 = new SimpleDateFormat("yyyyMM");
				Calendar c14 = Calendar.getInstance();
				c14.add(Calendar.YEAR, -3);
				String beforeMonth14 = df14.format(c14.getTime());

				String loginurl4 = "http://218.23.30.50:8003/person/personaccount_YLao_result.html";
				WebRequest webRequestlogin4;
				webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.POST);
				webRequestlogin4.setRequestParameters(new ArrayList<NameValuePair>());
				webRequestlogin4.getRequestParameters().add(new NameValuePair("pageNo", i + ""));
				webRequestlogin4.getRequestParameters().add(new NameValuePair("aae003", beforeMonth14));
				webRequestlogin4.getRequestParameters().add(new NameValuePair("aae003e", beforeMonth4));
				Page pagelogin4 = webClient.getPage(webRequestlogin4);
				String contentAsString4 = pagelogin4.getWebResponse().getContentAsString();
				System.out.println(
						"------------------------------------------养老保险信息----------------------------------------------");
				System.out.println("养老保险信息-------" + contentAsString4);
				if (contentAsString4.contains("class=\"grid\"")) {
					System.out.println("获取养老数据成功！");
					InsurancefuyangHtml insurancefuyangHtml = new InsurancefuyangHtml();
					insurancefuyangHtml.setHtml(contentAsString4);
					insurancefuyangHtml.setPageCount(1);
					insurancefuyangHtml.setTaskid(parameter.getTaskId());
					insurancefuyangHtml.setType("养老保险");
					insurancefuyangHtml.setUrl(loginurl4);
					insurancefuyangHtmlRepository.save(insurancefuyangHtml);

					Document doc2 = Jsoup.parse(contentAsString);
					Element table = doc2.getElementsByClass("grid").get(0);
					Elements trs = table.getElementsByTag("tr");
					for (int j = 1; j < trs.size(); j++) {
						Elements tds = trs.get(j).getElementsByTag("td");
						for (int k = 0; k < tds.size(); k += 6) {
							// 记帐年月
							String jzny = tds.get(k).text();
							System.out.println("记帐年月" + jzny);
							// 对应费款所属期
							String dyfkssq = tds.get(k + 1).text();
							System.out.println("对应费款所属期" + dyfkssq);
							// 缴费基数
							String jfjs = tds.get(k + 2).text();
							System.out.println("缴费基数" + jfjs);
							// 个人缴纳划入账户金额
							String grjnhrzhye = tds.get(k + 3).text();
							System.out.println("个人缴纳划入账户金额" + grjnhrzhye);
							// 单位划拨金额
							String dwhbje = tds.get(k + 4).text();
							System.out.println("单位划拨金额" + dwhbje);
							// 注销标志
							String zxbz = tds.get(k + 5).text();
							System.out.println("注销标志" + zxbz);
							InsurancefuyangYanglaoInfo insurancefuyangYanglaoInfo = new InsurancefuyangYanglaoInfo();
							insurancefuyangYanglaoInfo.setTaskid(parameter.getTaskId());
							insurancefuyangYanglaoInfo.setJzny(jzny);
							insurancefuyangYanglaoInfo.setDyfkssq(dyfkssq);
							insurancefuyangYanglaoInfo.setJfjs(jfjs);
							insurancefuyangYanglaoInfo.setGrjnhrzhye(grjnhrzhye);
							insurancefuyangYanglaoInfo.setDwhbje(dwhbje);
							insurancefuyangYanglaoInfo.setZxbz(zxbz);
							insurancefuyangYanglaoInfoRepository.save(insurancefuyangYanglaoInfo);
						}
					}
					// 更新Task表为 养老信息数据爬取成功
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhasestatus());
					taskInsurance.setYanglaoStatus(200);
					taskInsurance.setShiyeStatus(200);
					taskInsurance.setShengyuStatus(200);
					taskInsurance.setGongshangStatus(200);
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				} else {
					System.out.println("获取养老数据失败或没有数据！");
				}
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

			// 获取当前的年
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, -0);
			String beforeMonth = df.format(c.getTime());
			// 获取当前的前3年
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c1 = Calendar.getInstance();
			c1.add(Calendar.YEAR, -3);
			String beforeMonth1 = df1.format(c1.getTime());

			String loginurl = "http://218.23.30.50:8003/person/personaccount_YiL_result.html";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestlogin.getRequestParameters().add(new NameValuePair("pageNo", "1"));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae036", beforeMonth1));
			webRequestlogin.getRequestParameters().add(new NameValuePair("aae036e", beforeMonth));
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();

			Document doc = Jsoup.parse(contentAsString);
			String text = doc.getElementsByClass("page_num").get(0).text();
			String[] split = text.split("/");
			String[] split2 = split[1].split("页");
			String pages = split2[0].trim();
			int intpages = Integer.parseInt(pages);
			for (int i = 1; i <= intpages; i++) {
				// 获取当前的年
				SimpleDateFormat df4 = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c4 = Calendar.getInstance();
				c4.add(Calendar.YEAR, -0);
				String beforeMonth4 = df4.format(c4.getTime());
				// 获取当前的前3年
				SimpleDateFormat df14 = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c14 = Calendar.getInstance();
				c14.add(Calendar.YEAR, -3);
				String beforeMonth14 = df14.format(c14.getTime());

				String loginurl4 = "http://218.23.30.50:8003/person/personaccount_YiL_result.html";
				WebRequest webRequestlogin4;
				webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.POST);
				webRequestlogin4.setRequestParameters(new ArrayList<NameValuePair>());
				webRequestlogin4.getRequestParameters().add(new NameValuePair("pageNo", i + ""));
				webRequestlogin4.getRequestParameters().add(new NameValuePair("aae036", beforeMonth14));
				webRequestlogin4.getRequestParameters().add(new NameValuePair("aae036e", beforeMonth4));
				Page pagelogin4 = webClient.getPage(webRequestlogin4);
				String contentAsString4 = pagelogin4.getWebResponse().getContentAsString();
				System.out.println(
						"------------------------------------------医疗保险信息----------------------------------------------");
				System.out.println("医疗保险信息-------" + contentAsString);
				if (contentAsString.contains("class=\"grid\"")) {
					System.out.println("获取医疗数据成功！");
					InsurancefuyangHtml insurancefuyangHtml = new InsurancefuyangHtml();
					insurancefuyangHtml.setHtml(contentAsString4);
					insurancefuyangHtml.setPageCount(1);
					insurancefuyangHtml.setTaskid(parameter.getTaskId());
					insurancefuyangHtml.setType("医疗保险");
					insurancefuyangHtml.setUrl(loginurl4);
					insurancefuyangHtmlRepository.save(insurancefuyangHtml);
					
					Document doc2 = Jsoup.parse(contentAsString);
					Element table = doc2.getElementsByClass("grid").get(0);
					Elements trs = table.getElementsByTag("tr");
					for (int j = 1; j < trs.size(); j++) {
						Elements tds = trs.get(j).getElementsByTag("td");
						for (int k = 0; k < tds.size(); k += 7) {
							// 个人编号 
							String grbh = tds.get(k).text();
							System.out.println("个人编号 " + grbh);
							//  日期
							String rq = tds.get(k + 1).text();
							System.out.println(" 日期" + rq);
							//  摘要
							String zy = tds.get(k + 2).text();
							System.out.println(" 摘要" + zy);
							// 收入
							String sr = tds.get(k + 3).text();
							System.out.println("收入" + sr);
							// 支出
							String zc = tds.get(k + 4).text();
							System.out.println("支出" + zc);
							// 变更金额
							String bgje = tds.get(k + 5).text();
							System.out.println("变更金额" + bgje);
							// 余额
							String ye = tds.get(k + 6).text();
							System.out.println("余额" + ye);
							InsurancefuyangYibaoInfo insurancefuyangYibaoInfo = new InsurancefuyangYibaoInfo();
							insurancefuyangYibaoInfo.setTaskid(parameter.getTaskId());
							insurancefuyangYibaoInfo.setYe(ye);
							insurancefuyangYibaoInfo.setBgje(bgje);
							insurancefuyangYibaoInfo.setZc(zc);
							insurancefuyangYibaoInfo.setSr(sr);
							insurancefuyangYibaoInfo.setZy(zy);
							insurancefuyangYibaoInfo.setRq(rq);
							insurancefuyangYibaoInfo.setGrbh(grbh);
							insurancefuyangYibaoInfoRepository.save(insurancefuyangYibaoInfo);
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
			}
		} catch (Exception e) {
			System.out.println("获取医疗保险信息数据异常！");
		}
		return "";
	}

}
