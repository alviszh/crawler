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
import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangBaseInfo;
import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangHtml;
import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.ziyang.InsuranceziyangYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.ziyang.InsuranceziyangBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.ziyang.InsuranceziyangGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.ziyang.InsuranceziyangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.ziyang.InsuranceziyangShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.ziyang.InsuranceziyangShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.ziyang.InsuranceziyangYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.ziyang.InsuranceziyangYibaoInfoRepository;
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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.ziyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.ziyang" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceziyangHtmlRepository insuranceziyangHtmlRepository;

	@Autowired
	private InsuranceziyangBaseInfoRepository insuranceziyangBaseInfoRepository;

	@Autowired
	private InsuranceziyangYanglaoInfoRepository insuranceziyangYanglaoInfoRepository;

	@Autowired
	private InsuranceziyangYibaoInfoRepository insuranceziyangYibaoInfoRepository;

	@Autowired
	private InsuranceziyangShiyeInfoRepository insuranceziyangShiyeInfoRepository;

	@Autowired
	private InsuranceziyangShengYuInfoRepository insuranceziyangShengYuInfoRepository;

	@Autowired
	private InsuranceziyangGongShangInfoRepository insuranceziyangGongShangInfoRepository;

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
			String loginurl = "http://online.zysrsj.gov.cn/117372/Q2001.jhtml";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.GET);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsuranceziyangHtml insuranceziyangHtml = new InsuranceziyangHtml();
			insuranceziyangHtml.setHtml(contentAsString);
			insuranceziyangHtml.setPageCount(1);
			insuranceziyangHtml.setTaskid(parameter.getTaskId());
			insuranceziyangHtml.setType("基本信息");
			insuranceziyangHtml.setUrl(loginurl);
			insuranceziyangHtmlRepository.save(insuranceziyangHtml);

			if (!contentAsString.contains("个人信息维护")) {
				System.out.println("获取基本信息数据失败！");
			} else {
				System.out.println("获取基本信息数据成功！");
				// 姓名
				String[] split = contentAsString.split("'aac003','");
				String[] split2 = split[1].split("'");
				String name = split2[0].trim();
				System.out.println("姓名："+name);
				// 证件类型
				String[] split1 = contentAsString.split("'aac058_','");
				String[] split21 = split1[1].split("'");
				String zjlx = split21[0].trim();
				System.out.println("证件类型："+zjlx);
				// 身份证号
				String[] split11 = contentAsString.split("'aac002','");
				String[] split211 = split11[1].split("'");
				String sfzh = split211[0].trim();
				System.out.println("身份证号："+sfzh);
				// 性别
				String[] split111 = contentAsString.split("'aac004_','");
				String[] split2111 = split111[1].split("'");
				String sex = split2111[0].trim();
				System.out.println("性别："+sex);
				// 民族
				String[] split1111 = contentAsString.split("'aac005_','");
				String[] split21111 = split1111[1].split("'");
				String mz = split21111[0].trim();
				System.out.println("民族："+mz);
				// 年龄
				String[] split11111 = contentAsString.split("'age','");
				String[] split211111 = split11111[1].split("'");
				String age = split211111[0].trim();
				System.out.println("年龄："+age);
				// 出生日期
				String[] split111111 = contentAsString.split("'aac006','");
				String[] split2111111 = split111111[1].split("'");
				String birth = split2111111[0].trim();
				System.out.println("出生日期："+birth);
				// 参工时间
				String[] split1111111 = contentAsString.split("'aac007','");
				String[] split21111111 = split1111111[1].split("'");
				String cgsj = split21111111[0].trim();
				System.out.println("参工时间："+cgsj);
				// 养老退休状态
				String[] split11111111 = contentAsString.split("'aac084_','");
				String[] split211111111 = split11111111[1].split("'");
				String yangtxzt = split211111111[0].trim();
				System.out.println(" 养老退休状态："+yangtxzt);
				//医疗退休状态
				String[] split111111111 = contentAsString.split("'aac066_','");
				String[] split2111111111 = split111111111[1].split("'");
				String yiltxzt = split2111111111[0].trim();
				System.out.println("医疗退休状态："+yiltxzt);
				//生存状态
				String[] split1111111111 = contentAsString.split("'aac060_','");
				String[] split21111111111 = split1111111111[1].split("'");
				String sczt = split21111111111[0].trim();
				System.out.println("生存状态："+sczt);
				//户口性质
				String[] split11111111111 = contentAsString.split("'aac009_','");
				String[] split211111111111 = split11111111111[1].split("'");
				String hkxz = split211111111111[0].trim();
				System.out.println("户口性质："+hkxz);
				//地址
				String[] split111111111111 = contentAsString.split("'aac009_','");
				String[] split2111111111111 = split111111111111[1].split("'");
				String addr = split2111111111111[0].trim();
				System.out.println("地址："+addr);
				
				InsuranceziyangBaseInfo insuranceziyangBaseInfo = new InsuranceziyangBaseInfo();
				insuranceziyangBaseInfo.setTaskid(parameter.getTaskId());
				insuranceziyangBaseInfo.setAddr(addr);
				insuranceziyangBaseInfo.setHkxz(hkxz);
				insuranceziyangBaseInfo.setSczt(sczt);
				insuranceziyangBaseInfo.setYiltxzt(yiltxzt);
				insuranceziyangBaseInfo.setYangtxzt(yangtxzt);
				insuranceziyangBaseInfo.setCgsj(cgsj);
				insuranceziyangBaseInfo.setBirth(birth);
				insuranceziyangBaseInfo.setAge(age);
				insuranceziyangBaseInfo.setMz(mz);
				insuranceziyangBaseInfo.setSex(sex);
				insuranceziyangBaseInfo.setSfzh(sfzh);
				insuranceziyangBaseInfo.setZjlx(zjlx);
				insuranceziyangBaseInfo.setName(name);
				insuranceziyangBaseInfoRepository.save(insuranceziyangBaseInfo);

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
			String loginurl = "http://online.zysrsj.gov.cn/115090/Q2005.jspx";
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

				InsuranceziyangHtml insuranceziyangHtml = new InsuranceziyangHtml();
				insuranceziyangHtml.setHtml(contentAsString);
				insuranceziyangHtml.setPageCount(1);
				insuranceziyangHtml.setTaskid(parameter.getTaskId());
				insuranceziyangHtml.setType("养老保险");
				insuranceziyangHtml.setUrl(loginurl);
				insuranceziyangHtmlRepository.save(insuranceziyangHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceziyangYanglaoInfo insuranceziyangYanglaoInfo = new InsuranceziyangYanglaoInfo();
					insuranceziyangYanglaoInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceziyangYanglaoInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceziyangYanglaoInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceziyangYanglaoInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceziyangYanglaoInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceziyangYanglaoInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceziyangYanglaoInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceziyangYanglaoInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceziyangYanglaoInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceziyangYanglaoInfo.setHrgzje(hrgzje);

					insuranceziyangYanglaoInfoRepository.save(insuranceziyangYanglaoInfo);
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
			String loginurl = "http://online.zysrsj.gov.cn/115090/Q2005.jspx";
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

				InsuranceziyangHtml insuranceziyangHtml = new InsuranceziyangHtml();
				insuranceziyangHtml.setHtml(contentAsString);
				insuranceziyangHtml.setPageCount(1);
				insuranceziyangHtml.setTaskid(parameter.getTaskId());
				insuranceziyangHtml.setType("医疗保险");
				insuranceziyangHtml.setUrl(loginurl);
				insuranceziyangHtmlRepository.save(insuranceziyangHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceziyangYibaoInfo insuranceziyangYibaoInfo = new InsuranceziyangYibaoInfo();
					insuranceziyangYibaoInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceziyangYibaoInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceziyangYibaoInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceziyangYibaoInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceziyangYibaoInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceziyangYibaoInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceziyangYibaoInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceziyangYibaoInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceziyangYibaoInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceziyangYibaoInfo.setHrgzje(hrgzje);

					insuranceziyangYibaoInfoRepository.save(insuranceziyangYibaoInfo);
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
			String loginurl = "http://online.zysrsj.gov.cn/115090/Q2005.jspx";
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

				InsuranceziyangHtml insuranceziyangHtml = new InsuranceziyangHtml();
				insuranceziyangHtml.setHtml(contentAsString);
				insuranceziyangHtml.setPageCount(1);
				insuranceziyangHtml.setTaskid(parameter.getTaskId());
				insuranceziyangHtml.setType("失业保险");
				insuranceziyangHtml.setUrl(loginurl);
				insuranceziyangHtmlRepository.save(insuranceziyangHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceziyangShiyeInfo insuranceziyangShiyeInfo = new InsuranceziyangShiyeInfo();
					insuranceziyangShiyeInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceziyangShiyeInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceziyangShiyeInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceziyangShiyeInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceziyangShiyeInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceziyangShiyeInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceziyangShiyeInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceziyangShiyeInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceziyangShiyeInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceziyangShiyeInfo.setHrgzje(hrgzje);

					insuranceziyangShiyeInfoRepository.save(insuranceziyangShiyeInfo);
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
			String loginurl = "http://online.zysrsj.gov.cn/115090/Q2005.jspx";
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

				InsuranceziyangHtml insuranceziyangHtml = new InsuranceziyangHtml();
				insuranceziyangHtml.setHtml(contentAsString);
				insuranceziyangHtml.setPageCount(1);
				insuranceziyangHtml.setTaskid(parameter.getTaskId());
				insuranceziyangHtml.setType("生育保险");
				insuranceziyangHtml.setUrl(loginurl);
				insuranceziyangHtmlRepository.save(insuranceziyangHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceziyangShengYuInfo insuranceziyangShengYuInfo = new InsuranceziyangShengYuInfo();
					insuranceziyangShengYuInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceziyangShengYuInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceziyangShengYuInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceziyangShengYuInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceziyangShengYuInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceziyangShengYuInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceziyangShengYuInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceziyangShengYuInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceziyangShengYuInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceziyangShengYuInfo.setHrgzje(hrgzje);

					insuranceziyangShengYuInfoRepository.save(insuranceziyangShengYuInfo);
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
			String loginurl = "http://online.zysrsj.gov.cn/115090/Q2005.jspx";
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

				InsuranceziyangHtml insuranceziyangHtml = new InsuranceziyangHtml();
				insuranceziyangHtml.setHtml(contentAsString);
				insuranceziyangHtml.setPageCount(1);
				insuranceziyangHtml.setTaskid(parameter.getTaskId());
				insuranceziyangHtml.setType("工伤保险");
				insuranceziyangHtml.setUrl(loginurl);
				insuranceziyangHtmlRepository.save(insuranceziyangHtml);

				String lists = json.getString("lists").trim();
				JSONObject json2 = JSONObject.fromObject(lists);
				String resultset = json2.getString("resultset").trim();
				JSONObject json3 = JSONObject.fromObject(resultset);
				String list = json3.getString("list").trim();
				JSONArray array = JSONArray.fromObject(list);
				for (int i = 0; i < array.size(); i++) {
					String array2 = array.get(i).toString();
					JSONObject json4 = JSONObject.fromObject(array2);
					InsuranceziyangGongShangInfo insuranceziyangGongShangInfo = new InsuranceziyangGongShangInfo();
					insuranceziyangGongShangInfo.setTaskid(parameter.getTaskId());
					// 单位名称
					String dwmc = json4.getString("aab004").trim();
					System.out.println("单位名称：" + dwmc);
					insuranceziyangGongShangInfo.setDwmc(dwmc);

					// 缴费年月
					String month = json4.getString("aae003").trim();
					System.out.println("缴费年月：" + month);
					insuranceziyangGongShangInfo.setMonth(month);

					// 费款所属年月
					String fkssny = json4.getString("aae002").trim();
					System.out.println("费款所属年月：" + fkssny);
					insuranceziyangGongShangInfo.setFkssny(fkssny);

					// 缴费基数
					String jfjs = json4.getString("aae180").trim();
					System.out.println("缴费基数：" + jfjs);
					insuranceziyangGongShangInfo.setJfjs(jfjs);

					// 单位缴费
					String dwjf = json4.getString("dwjf").trim();
					System.out.println("单位缴费：" + dwjf);
					insuranceziyangGongShangInfo.setDwjf(dwjf);

					// 个人缴费
					String grjf = json4.getString("grjf").trim();
					System.out.println("个人缴费：" + grjf);
					insuranceziyangGongShangInfo.setGrjf(grjf);

					// 缴费状态
					String jfzt = json4.getString("aae078_").trim();
					System.out.println("缴费状态：" + jfzt);
					insuranceziyangGongShangInfo.setJfzt(jfzt);

					// 参保地
					String cbd = json4.getString("aaa027_").trim();
					System.out.println("参保地：" + cbd);
					insuranceziyangGongShangInfo.setCbd(cbd);

					// 划入个账金额
					String hrgzje = json4.getString("hrgz").trim();
					System.out.println("划入个账金额：" + hrgzje);
					insuranceziyangGongShangInfo.setHrgzje(hrgzje);

					insuranceziyangGongShangInfoRepository.save(insuranceziyangGongShangInfo);
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
