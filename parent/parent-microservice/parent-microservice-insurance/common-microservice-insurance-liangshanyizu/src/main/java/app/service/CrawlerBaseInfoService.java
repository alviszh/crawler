package app.service;

import java.net.URL;
import java.util.Set;

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
import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuBaseInfo;
import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuHtml;
import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.liangshanyizu.InsuranceliangshanyizuYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.liangshanyizu.InsuranceliangshanyizuBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.liangshanyizu.InsuranceliangshanyizuGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.liangshanyizu.InsuranceliangshanyizuHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.liangshanyizu.InsuranceliangshanyizuShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.liangshanyizu.InsuranceliangshanyizuShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.liangshanyizu.InsuranceliangshanyizuYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.liangshanyizu.InsuranceliangshanyizuYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 爬取解析所有信息Service
 * 
 * @author qizhongDe
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.liangshanyizu" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.liangshanyizu" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceliangshanyizuHtmlRepository insuranceliangshanyizuHtmlRepository;

	@Autowired
	private InsuranceliangshanyizuBaseInfoRepository insuranceliangshanyizuBaseInfoRepository;

	@Autowired
	private InsuranceliangshanyizuYanglaoInfoRepository insuranceliangshanyizuYanglaoInfoRepository;

	@Autowired
	private InsuranceliangshanyizuYibaoInfoRepository insuranceliangshanyizuYibaoInfoRepository;

	@Autowired
	private InsuranceliangshanyizuShiyeInfoRepository insuranceliangshanyizuShiyeInfoRepository;

	@Autowired
	private InsuranceliangshanyizuShengYuInfoRepository insuranceliangshanyizuShengYuInfoRepository;

	@Autowired
	private InsuranceliangshanyizuGongShangInfoRepository insuranceliangshanyizuGongShangInfoRepository;

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
			String loginurl = "http://118.122.8.171:8003/lsui/117372/Q2001.jhtml";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceliangshanyizuHtml insuranceliangshanyizuHtml = new InsuranceliangshanyizuHtml();
			insuranceliangshanyizuHtml.setHtml(contentAsString);
			insuranceliangshanyizuHtml.setPageCount(1);
			insuranceliangshanyizuHtml.setTaskid(parameter.getTaskId());
			insuranceliangshanyizuHtml.setType("基本信息");
			insuranceliangshanyizuHtml.setUrl(loginurl);
			insuranceliangshanyizuHtmlRepository.save(insuranceliangshanyizuHtml);

			if (!contentAsString.contains("个人信息维护")) {
				System.out.println("获取基本信息数据失败！");
			} else {
				System.out.println("获取基本信息数据成功！");
				// 姓名
				String[] split = contentAsString.split("'aac003','");
				String[] split2 = split[1].split("'");
				String name = split2[0].trim();
				System.out.println("姓名：" + name);
				// 证件类型
				String[] split1 = contentAsString.split("'aac058_','");
				String[] split21 = split1[1].split("'");
				String zjlx = split21[0].trim();
				System.out.println("证件类型：" + zjlx);
				// 身份证号
				String[] split11 = contentAsString.split("'aac002','");
				String[] split211 = split11[1].split("'");
				String sfzh = split211[0].trim();
				System.out.println("身份证号：" + sfzh);
				// 性别
				String[] split111 = contentAsString.split("'aac004_','");
				String[] split2111 = split111[1].split("'");
				String sex = split2111[0].trim();
				System.out.println("性别：" + sex);
				// 民族
				String[] split1111 = contentAsString.split("'aac005_','");
				String[] split21111 = split1111[1].split("'");
				String mz = split21111[0].trim();
				System.out.println("民族：" + mz);
				// 年龄
				String[] split11111 = contentAsString.split("'age','");
				String[] split211111 = split11111[1].split("'");
				String age = split211111[0].trim();
				System.out.println("年龄：" + age);
				// 出生日期
				String[] split111111 = contentAsString.split("'aac006','");
				String[] split2111111 = split111111[1].split("'");
				String birth = split2111111[0].trim();
				System.out.println("出生日期：" + birth);
				// 参工时间
				String[] split1111111 = contentAsString.split("'aac007','");
				String[] split21111111 = split1111111[1].split("'");
				String cgsj = split21111111[0].trim();
				System.out.println("参工时间：" + cgsj);
				// 养老退休状态
				String[] split11111111 = contentAsString.split("'aac084_','");
				String[] split211111111 = split11111111[1].split("'");
				String yangtxzt = split211111111[0].trim();
				System.out.println(" 养老退休状态：" + yangtxzt);
				// 医疗退休状态
				String[] split111111111 = contentAsString.split("'aac066_','");
				String[] split2111111111 = split111111111[1].split("'");
				String yiltxzt = split2111111111[0].trim();
				System.out.println("医疗退休状态：" + yiltxzt);
				// 生存状态
				String[] split1111111111 = contentAsString.split("'aac060_','");
				String[] split21111111111 = split1111111111[1].split("'");
				String sczt = split21111111111[0].trim();
				System.out.println("生存状态：" + sczt);
				// 户口性质
				String[] split11111111111 = contentAsString.split("'aac009_','");
				String[] split211111111111 = split11111111111[1].split("'");
				String hkxz = split211111111111[0].trim();
				System.out.println("户口性质：" + hkxz);
				// 地址
				String[] split111111111111 = contentAsString.split("'aac009_','");
				String[] split2111111111111 = split111111111111[1].split("'");
				String addr = split2111111111111[0].trim();
				System.out.println("地址：" + addr);

				InsuranceliangshanyizuBaseInfo insuranceliangshanyizuBaseInfo = new InsuranceliangshanyizuBaseInfo();
				insuranceliangshanyizuBaseInfo.setTaskid(parameter.getTaskId());
				insuranceliangshanyizuBaseInfo.setAddr(addr);
				insuranceliangshanyizuBaseInfo.setHkxz(hkxz);
				insuranceliangshanyizuBaseInfo.setSczt(sczt);
				insuranceliangshanyizuBaseInfo.setYiltxzt(yiltxzt);
				insuranceliangshanyizuBaseInfo.setYangtxzt(yangtxzt);
				insuranceliangshanyizuBaseInfo.setCgsj(cgsj);
				insuranceliangshanyizuBaseInfo.setBirth(birth);
				insuranceliangshanyizuBaseInfo.setAge(age);
				insuranceliangshanyizuBaseInfo.setMz(mz);
				insuranceliangshanyizuBaseInfo.setSex(sex);
				insuranceliangshanyizuBaseInfo.setSfzh(sfzh);
				insuranceliangshanyizuBaseInfo.setZjlx(zjlx);
				insuranceliangshanyizuBaseInfo.setName(name);
				insuranceliangshanyizuBaseInfoRepository.save(insuranceliangshanyizuBaseInfo);

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
	public String crawlerAgedInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析养老保险", parameter.toString());
		// 养老保险界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			String loginurl = "http://118.122.8.171:8003/lsui/115090/Q2005.jspx";
			String requestBody = "" + "aae140=110" + "&pageNo=1" + "&pageSize=1000" + "&aae036_s=201001"
					+ "&aae036_e=202801" + "&notkeyflag=0";

			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(requestBody);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);
			if (contentAsString.contains("lists")) {
				JSONObject json = JSONObject.fromObject(contentAsString);
				System.out.println("获取养老数据成功！");

				InsuranceliangshanyizuHtml insuranceliangshanyizuHtml = new InsuranceliangshanyizuHtml();
				insuranceliangshanyizuHtml.setHtml(contentAsString);
				insuranceliangshanyizuHtml.setPageCount(1);
				insuranceliangshanyizuHtml.setTaskid(parameter.getTaskId());
				insuranceliangshanyizuHtml.setType("养老保险");
				insuranceliangshanyizuHtml.setUrl(loginurl);
				insuranceliangshanyizuHtmlRepository.save(insuranceliangshanyizuHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceliangshanyizuYanglaoInfo insuranceliangshanyizuYanglaoInfo = new InsuranceliangshanyizuYanglaoInfo();
					insuranceliangshanyizuYanglaoInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceliangshanyizuYanglaoInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceliangshanyizuYanglaoInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceliangshanyizuYanglaoInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceliangshanyizuYanglaoInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceliangshanyizuYanglaoInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceliangshanyizuYanglaoInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceliangshanyizuYanglaoInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceliangshanyizuYanglaoInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceliangshanyizuYanglaoInfo.setHrgzje(hrgzje);

					insuranceliangshanyizuYanglaoInfoRepository.save(insuranceliangshanyizuYanglaoInfo);
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
			String loginurl = "http://118.122.8.171:8003/lsui/115090/Q2005.jspx";
			String requestBody = "" + "aae140=310" + "&pageNo=1" + "&pageSize=1000" + "&aae036_s=201001"
					+ "&aae036_e=202801" + "&notkeyflag=0";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(requestBody);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);

			if (contentAsString.contains("lists")) {
				JSONObject json = JSONObject.fromObject(contentAsString);
				System.out.println("获取医疗数据成功！");

				InsuranceliangshanyizuHtml insuranceliangshanyizuHtml = new InsuranceliangshanyizuHtml();
				insuranceliangshanyizuHtml.setHtml(contentAsString);
				insuranceliangshanyizuHtml.setPageCount(1);
				insuranceliangshanyizuHtml.setTaskid(parameter.getTaskId());
				insuranceliangshanyizuHtml.setType("医疗保险");
				insuranceliangshanyizuHtml.setUrl(loginurl);
				insuranceliangshanyizuHtmlRepository.save(insuranceliangshanyizuHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceliangshanyizuYibaoInfo insuranceliangshanyizuYibaoInfo = new InsuranceliangshanyizuYibaoInfo();
					insuranceliangshanyizuYibaoInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceliangshanyizuYibaoInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceliangshanyizuYibaoInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceliangshanyizuYibaoInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceliangshanyizuYibaoInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceliangshanyizuYibaoInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceliangshanyizuYibaoInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceliangshanyizuYibaoInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceliangshanyizuYibaoInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceliangshanyizuYibaoInfo.setHrgzje(hrgzje);

					insuranceliangshanyizuYibaoInfoRepository.save(insuranceliangshanyizuYibaoInfo);
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
			String loginurl = "http://118.122.8.171:8003/lsui/115090/Q2005.jspx";
			String requestBody = "" + "aae140=210" + "&pageNo=1" + "&pageSize=1000" + "&aae036_s=201001"
					+ "&aae036_e=202801" + "&notkeyflag=0";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(requestBody);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString);
			if (contentAsString.contains("lists")) {
				JSONObject json = JSONObject.fromObject(contentAsString);
				System.out.println("获取失业数据成功！");

				InsuranceliangshanyizuHtml insuranceliangshanyizuHtml = new InsuranceliangshanyizuHtml();
				insuranceliangshanyizuHtml.setHtml(contentAsString);
				insuranceliangshanyizuHtml.setPageCount(1);
				insuranceliangshanyizuHtml.setTaskid(parameter.getTaskId());
				insuranceliangshanyizuHtml.setType("失业保险");
				insuranceliangshanyizuHtml.setUrl(loginurl);
				insuranceliangshanyizuHtmlRepository.save(insuranceliangshanyizuHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceliangshanyizuShiyeInfo insuranceliangshanyizuShiyeInfo = new InsuranceliangshanyizuShiyeInfo();
					insuranceliangshanyizuShiyeInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceliangshanyizuShiyeInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceliangshanyizuShiyeInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceliangshanyizuShiyeInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceliangshanyizuShiyeInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceliangshanyizuShiyeInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceliangshanyizuShiyeInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceliangshanyizuShiyeInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceliangshanyizuShiyeInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceliangshanyizuShiyeInfo.setHrgzje(hrgzje);

					insuranceliangshanyizuShiyeInfoRepository.save(insuranceliangshanyizuShiyeInfo);
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
			String loginurl = "http://118.122.8.171:8003/lsui/115090/Q2005.jspx";
			String requestBody = "" + "aae140=510" + "&pageNo=1" + "&pageSize=1000" + "&aae036_s=201001"
					+ "&aae036_e=202801" + "&notkeyflag=0";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(requestBody);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString);

			if (contentAsString.contains("lists")) {
				JSONObject json = JSONObject.fromObject(contentAsString);
				System.out.println("获取生育数据成功！");

				InsuranceliangshanyizuHtml insuranceliangshanyizuHtml = new InsuranceliangshanyizuHtml();
				insuranceliangshanyizuHtml.setHtml(contentAsString);
				insuranceliangshanyizuHtml.setPageCount(1);
				insuranceliangshanyizuHtml.setTaskid(parameter.getTaskId());
				insuranceliangshanyizuHtml.setType("生育保险");
				insuranceliangshanyizuHtml.setUrl(loginurl);
				insuranceliangshanyizuHtmlRepository.save(insuranceliangshanyizuHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceliangshanyizuShengYuInfo insuranceliangshanyizuShengYuInfo = new InsuranceliangshanyizuShengYuInfo();
					insuranceliangshanyizuShengYuInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceliangshanyizuShengYuInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceliangshanyizuShengYuInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceliangshanyizuShengYuInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceliangshanyizuShengYuInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceliangshanyizuShengYuInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceliangshanyizuShengYuInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceliangshanyizuShengYuInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceliangshanyizuShengYuInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceliangshanyizuShengYuInfo.setHrgzje(hrgzje);

					insuranceliangshanyizuShengYuInfoRepository.save(insuranceliangshanyizuShengYuInfo);
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
			String loginurl = "http://118.122.8.171:8003/lsui/115090/Q2005.jspx";
			String requestBody = "" + "aae140=410" + "&pageNo=1" + "&pageSize=1000" + "&aae036_s=201001"
					+ "&aae036_e=202801" + "&notkeyflag=0";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(requestBody);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);

			if (contentAsString.contains("lists")) {
				JSONObject json = JSONObject.fromObject(contentAsString);
				System.out.println("获取工伤数据成功！");

				InsuranceliangshanyizuHtml insuranceliangshanyizuHtml = new InsuranceliangshanyizuHtml();
				insuranceliangshanyizuHtml.setHtml(contentAsString);
				insuranceliangshanyizuHtml.setPageCount(1);
				insuranceliangshanyizuHtml.setTaskid(parameter.getTaskId());
				insuranceliangshanyizuHtml.setType("工伤保险");
				insuranceliangshanyizuHtml.setUrl(loginurl);
				insuranceliangshanyizuHtmlRepository.save(insuranceliangshanyizuHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceliangshanyizuGongShangInfo insuranceliangshanyizuGongShangInfo = new InsuranceliangshanyizuGongShangInfo();
					insuranceliangshanyizuGongShangInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceliangshanyizuGongShangInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceliangshanyizuGongShangInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceliangshanyizuGongShangInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceliangshanyizuGongShangInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceliangshanyizuGongShangInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceliangshanyizuGongShangInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceliangshanyizuGongShangInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceliangshanyizuGongShangInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceliangshanyizuGongShangInfo.setHrgzje(hrgzje);

					insuranceliangshanyizuGongShangInfoRepository.save(insuranceliangshanyizuGongShangInfo);
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
