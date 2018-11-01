package app.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinBaseInfo;
import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinHtml;
import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.haerbin.InsuranceHaerbinShiyeInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.haerbin.InsuranceHaerbinBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.haerbin.InsuranceHaerbinGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.haerbin.InsuranceHaerbinHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.haerbin.InsuranceHaerbinShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.haerbin.InsuranceHaerbinShiyeInfoRepository;
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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.Haerbin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.Haerbin" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsuranceHaerbinHtmlRepository insuranceHaerbinHtmlRepository;

	@Autowired
	private InsuranceHaerbinBaseInfoRepository insuranceHaerbinBaseInfoRepository;
	@Autowired
	private InsuranceHaerbinGongShangInfoRepository insuranceHaerbinGongShangInfoRepository;
	@Autowired
	private InsuranceHaerbinShengYuInfoRepository insuranceHaerbinShengYuInfoRepository;
	@Autowired
	private InsuranceHaerbinShiyeInfoRepository insuranceHaerbinShiyeInfoRepository;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	WebClient webClient = null;
	
	
	/**
	 * 登录业务方法
	 * 
	 * @param parameter
	 */
	public TaskInsurance login(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			String loginurl2 = "http://221.207.175.178:7989/uaa/captcha/img";
			WebRequest webRequest = new WebRequest(new URL(loginurl2), HttpMethod.GET);
			Page page00 = webClient.getPage(webRequest);
			String contentAsString = page00.getWebResponse().getContentAsString();
			JSONObject json = JSONObject.fromObject(contentAsString);
			String id = json.getString("id");
			System.out.println("图片验证码对应的id-----" + id);

			// 获取图片验证码
			String loginurl21 = "http://221.207.175.178:7989/uaa/captcha/img/" + id + "";
			WebRequest webRequest1 = new WebRequest(new URL(loginurl21), HttpMethod.GET);
			Page page001 = webClient.getPage(webRequest1);
			String imagePath = getImagePath(page001);
			String code = chaoJiYingOcrService.callChaoJiYingService(imagePath, "1004");
			System.out.println("识别出来的图片验证码是---------" + code);

			String url = "http://221.207.175.178:7989/uaa/api/person/idandmobile/login";
			// 调用下面的getHtml方法
			WebRequest webRequest11 = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest11.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest11.getRequestParameters().add(new NameValuePair("captchaId", id));
			webRequest11.getRequestParameters().add(new NameValuePair("captchaWord", code));
			webRequest11.getRequestParameters().add(new NameValuePair("password", parameter.getPassword()));
			webRequest11.getRequestParameters().add(new NameValuePair("username", parameter.getUsername()));
			Page html = webClient.getPage(webRequest11);
			String contentAsString2 = html.getWebResponse().getContentAsString();

			if (contentAsString2.contains("<title>社会保障个人网上业务大厅</title>")) {
				System.out.println("登录成功！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else if (contentAsString2.contains("验证码错误")) {
				System.out.println("登录失败...验证码错误");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_CAPTCHA_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			} else if (contentAsString2.contains("用户名或密码错误")) {
				System.out.println("登录失败...用户名或密码错误");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("异常错误!请从新登陆！");
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_EXCEPTION.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		}
		return taskInsurance;
	}

	/**
	 * 爬取解析基本信息
	 * 
	 * @param parameter
	 * @param cookies
	 * @param pid
	 * @return
	 */
	public String crawlerBaseInfo(InsuranceRequestParameters parameter) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析基本信息", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		
		String id = null;
		try {
			// 获取个人编号
			String loginurl1 = "http://221.207.175.178:7989/api/security/user";
			WebRequest webRequestlogin1;
			webRequestlogin1 = new WebRequest(new URL(loginurl1), HttpMethod.GET);
			Page pagelogin1 = webClient.getPage(webRequestlogin1);
			String contentAsString = pagelogin1.getWebResponse().getContentAsString();
			System.out.println("获取个人编号返回的报文-----"+contentAsString);
			JSONObject json = JSONObject.fromObject(contentAsString);
			String associatedPersons = json.getString("associatedPersons");
			JSONArray array = JSONArray.fromObject(associatedPersons);
			JSONObject json2 = JSONObject.fromObject(array.get(0));
			// 个人编号
			id = json2.getString("id");
			System.out.println("个人编号-----" + id);

			String loginurl11 = "http://221.207.175.178:7989/ehrss-si-person/api/rights/person/baseinfo?personId=" + id
					+ "";
			WebRequest webRequestlogin11;
			webRequestlogin11 = new WebRequest(new URL(loginurl11), HttpMethod.GET);
			Page pagelogin11 = webClient.getPage(webRequestlogin11);
			String contentAsString1 = pagelogin11.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString1);

			InsuranceHaerbinHtml insuranceHaerbinHtml = new InsuranceHaerbinHtml();
			insuranceHaerbinHtml.setHtml(contentAsString1);
			insuranceHaerbinHtml.setPageCount(1);
			insuranceHaerbinHtml.setTaskid(parameter.getTaskId());
			insuranceHaerbinHtml.setType("基本信息");
			insuranceHaerbinHtml.setUrl(loginurl11);
			insuranceHaerbinHtmlRepository.save(insuranceHaerbinHtml);

			JSONObject json1 = JSONObject.fromObject(contentAsString1);
			String baseInfoDTO = json1.getString("baseInfoDTO");
			JSONObject json21 = JSONObject.fromObject(baseInfoDTO);

			// 社保卡号
			String sbkh = json21.getString("idSocialensureNumber");
			System.out.println("社保卡号-----" + sbkh);
			// 身份证号
			String sfzh = json21.getString("idNumber");
			System.out.println("身份证号-----" + sfzh);
			// 姓名
			String xm = json21.getString("name");
			System.out.println("姓名-----" + xm);
			// 性别
			String xb = json21.getString("sex");
			System.out.println("性别-----" + xb);
			// 人员状态
			String ryzt = json21.getString("retireStatus");
			System.out.println("人员状态-----" + ryzt);
			// 个人身份
			String grsf = json21.getString("individualStatus");
			System.out.println("个人身份-----" + grsf);
			// 公务员标识
			String gwybs = json21.getString("officialMark");
			System.out.println("公务员标识-----" + gwybs);

			InsuranceHaerbinBaseInfo insuranceHaerbinBaseInfo = new InsuranceHaerbinBaseInfo();
			insuranceHaerbinBaseInfo.setTaskid(parameter.getTaskId());
			insuranceHaerbinBaseInfo.setGwybs(gwybs);
			insuranceHaerbinBaseInfo.setGrsf(grsf);
			insuranceHaerbinBaseInfo.setRyzt(ryzt);
			insuranceHaerbinBaseInfo.setXb(xb);
			insuranceHaerbinBaseInfo.setXm(xm);
			insuranceHaerbinBaseInfo.setSfzh(sfzh);
			insuranceHaerbinBaseInfo.setSbkh(sbkh);
			insuranceHaerbinBaseInfo.setGrbh(id);
			insuranceHaerbinBaseInfoRepository.save(insuranceHaerbinBaseInfo);

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
		return id;
	}

	// 工伤
	public void crawlerGongshangInsurance(InsuranceRequestParameters parameter, String id) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析工伤", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			// 获取当前的年
			SimpleDateFormat df4 = new SimpleDateFormat("yyyyMM");
			Calendar c4 = Calendar.getInstance();
			c4.add(Calendar.YEAR, -0);
			String beforeMonth4 = df4.format(c4.getTime());
			System.out.println(beforeMonth4);
			// 获取当前的前10年
			SimpleDateFormat df14 = new SimpleDateFormat("yyyyMM");
			Calendar c14 = Calendar.getInstance();
			c14.add(Calendar.YEAR, -10);
			String beforeMonth14 = df14.format(c14.getTime());
			System.out.println(beforeMonth14);

			String loginurl4 = "http://221.207.175.178:7989/ehrss-si-person/api/rights/payment/paydetail?endTime="
					+ beforeMonth4 + "&paymentType=&personId=" + id + "&startTime=" + beforeMonth14 + "&type=1";
			WebRequest webRequestlogin4;
			webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.GET);
			Page pagelogin4 = webClient.getPage(webRequestlogin4);
			String contentAsString4 = pagelogin4.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString4);

			InsuranceHaerbinHtml insuranceHaerbinHtml = new InsuranceHaerbinHtml();
			insuranceHaerbinHtml.setHtml(contentAsString4);
			insuranceHaerbinHtml.setPageCount(1);
			insuranceHaerbinHtml.setTaskid(parameter.getTaskId());
			insuranceHaerbinHtml.setType("工伤保险");
			insuranceHaerbinHtml.setUrl(loginurl4);
			insuranceHaerbinHtmlRepository.save(insuranceHaerbinHtml);

			JSONObject json = JSONObject.fromObject(contentAsString4);
			String list = json.getString("list");
			JSONArray array = JSONArray.fromObject(list);
			for (int i = 0; i < array.size(); i++) {
				String object = array.get(i).toString();
				JSONObject json2 = JSONObject.fromObject(object);

				// 缴费属期
				String jfsq = json2.getString("issue");
				System.out.println("缴费属期-----" + jfsq);
				// 对应缴费属期
				String dyjfsq = json2.getString("dissue");
				System.out.println("对应缴费属期-----" + dyjfsq);
				// 单位应缴
				String dwyj = json2.getString("companyPay");
				System.out.println("单位应缴-----" + dwyj);
				// 个人应缴
				String gryj = json2.getString("personPay");
				System.out.println("个人应缴-----" + gryj);
				// 应缴小计
				String yjxj = json2.getString("countPay");
				System.out.println(" 应缴小计-----" + yjxj);
				// 公司名称
				String gsmc = json2.getString("companyName");
				System.out.println("公司名称-----" + gsmc);
				// 险种类型
				String xzlx = json2.getString("type");
				System.out.println("险种类型-----" + xzlx);
				// 缴费基数
				String jfjs = json2.getString("basePay");
				System.out.println("缴费基数-----" + jfjs);
				// 缴费标识
				String jfbs = json2.getString("payFlag");
				System.out.println("缴费标识-----" + jfbs);
				// 缴费类型
				String jflx = json2.getString("payType");
				System.out.println("缴费类型-----" + jflx);
				// 划账日期
				String hzrq = json2.getString("payDate");
				System.out.println("划账日期-----" + hzrq);

				InsuranceHaerbinGongShangInfo insuranceHaerbinGongShangInfo = new InsuranceHaerbinGongShangInfo();
				insuranceHaerbinGongShangInfo.setTaskid(parameter.getTaskId());
				insuranceHaerbinGongShangInfo.setHzrq(hzrq);
				insuranceHaerbinGongShangInfo.setJflx(jflx);
				insuranceHaerbinGongShangInfo.setJfbs(jfbs);
				insuranceHaerbinGongShangInfo.setJfjs(jfjs);
				insuranceHaerbinGongShangInfo.setXzlx(xzlx);
				insuranceHaerbinGongShangInfo.setGsmc(gsmc);
				insuranceHaerbinGongShangInfo.setYjxj(yjxj);
				insuranceHaerbinGongShangInfo.setGryj(gryj);
				insuranceHaerbinGongShangInfo.setDwyj(dwyj);
				insuranceHaerbinGongShangInfo.setDyjfsq(dyjfsq);
				insuranceHaerbinGongShangInfo.setJfsq(jfsq);
				insuranceHaerbinGongShangInfoRepository.save(insuranceHaerbinGongShangInfo);

			}
			// 更新Task表为 工伤信息数据爬取成功
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhasestatus());
			taskInsurance.setGongshangStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		} catch (Exception e) {
			System.out.println("获取工伤保险信息数据异常！");
		}
	}

	// 生育
	public void crawlerShengyuInsurance(InsuranceRequestParameters parameter, String id) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析生育", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			// 获取当前的年
			SimpleDateFormat df4 = new SimpleDateFormat("yyyyMM");
			Calendar c4 = Calendar.getInstance();
			c4.add(Calendar.YEAR, -0);
			String beforeMonth4 = df4.format(c4.getTime());
			System.out.println(beforeMonth4);
			// 获取当前的前10年
			SimpleDateFormat df14 = new SimpleDateFormat("yyyyMM");
			Calendar c14 = Calendar.getInstance();
			c14.add(Calendar.YEAR, -10);
			String beforeMonth14 = df14.format(c14.getTime());
			System.out.println(beforeMonth14);

			String loginurl4 = "http://221.207.175.178:7989/ehrss-si-person/api/rights/payment/paydetail?endTime="
					+ beforeMonth4 + "&paymentType=&personId=" + id + "&startTime=" + beforeMonth14 + "&type=2";
			WebRequest webRequestlogin4;
			webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.GET);
			Page pagelogin4 = webClient.getPage(webRequestlogin4);
			String contentAsString4 = pagelogin4.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------生育保险信息----------------------------------------------");
			System.out.println("生育保险信息-------" + contentAsString4);

			InsuranceHaerbinHtml insuranceHaerbinHtml = new InsuranceHaerbinHtml();
			insuranceHaerbinHtml.setHtml(contentAsString4);
			insuranceHaerbinHtml.setPageCount(1);
			insuranceHaerbinHtml.setTaskid(parameter.getTaskId());
			insuranceHaerbinHtml.setType("生育保险");
			insuranceHaerbinHtml.setUrl(loginurl4);
			insuranceHaerbinHtmlRepository.save(insuranceHaerbinHtml);

			JSONObject json = JSONObject.fromObject(contentAsString4);
			String list = json.getString("list");
			JSONArray array = JSONArray.fromObject(list);
			for (int i = 0; i < array.size(); i++) {
				String object = array.get(i).toString();
				JSONObject json2 = JSONObject.fromObject(object);

				// 缴费属期
				String jfsq = json2.getString("issue");
				System.out.println("缴费属期-----" + jfsq);
				// 对应缴费属期
				String dyjfsq = json2.getString("dissue");
				System.out.println("对应缴费属期-----" + dyjfsq);
				// 单位应缴
				String dwyj = json2.getString("companyPay");
				System.out.println("单位应缴-----" + dwyj);
				// 个人应缴
				String gryj = json2.getString("personPay");
				System.out.println("个人应缴-----" + gryj);
				// 应缴小计
				String yjxj = json2.getString("countPay");
				System.out.println(" 应缴小计-----" + yjxj);
				// 公司名称
				String gsmc = json2.getString("companyName");
				System.out.println("公司名称-----" + gsmc);
				// 险种类型
				String xzlx = json2.getString("type");
				System.out.println("险种类型-----" + xzlx);
				// 缴费基数
				String jfjs = json2.getString("basePay");
				System.out.println("缴费基数-----" + jfjs);
				// 缴费标识
				String jfbs = json2.getString("payFlag");
				System.out.println("缴费标识-----" + jfbs);
				// 缴费类型
				String jflx = json2.getString("payType");
				System.out.println("缴费类型-----" + jflx);
				// 划账日期
				String hzrq = json2.getString("payDate");
				System.out.println("划账日期-----" + hzrq);

				InsuranceHaerbinShengYuInfo insuranceHaerbinShengYuInfo = new InsuranceHaerbinShengYuInfo();
				insuranceHaerbinShengYuInfo.setTaskid(parameter.getTaskId());
				insuranceHaerbinShengYuInfo.setHzrq(hzrq);
				insuranceHaerbinShengYuInfo.setJflx(jflx);
				insuranceHaerbinShengYuInfo.setJfbs(jfbs);
				insuranceHaerbinShengYuInfo.setJfjs(jfjs);
				insuranceHaerbinShengYuInfo.setXzlx(xzlx);
				insuranceHaerbinShengYuInfo.setGsmc(gsmc);
				insuranceHaerbinShengYuInfo.setYjxj(yjxj);
				insuranceHaerbinShengYuInfo.setGryj(gryj);
				insuranceHaerbinShengYuInfo.setDwyj(dwyj);
				insuranceHaerbinShengYuInfo.setDyjfsq(dyjfsq);
				insuranceHaerbinShengYuInfo.setJfsq(jfsq);
				insuranceHaerbinShengYuInfoRepository.save(insuranceHaerbinShengYuInfo);

			}
			// 更新Task表为 生育信息数据爬取成功
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getPhasestatus());
			taskInsurance.setShengyuStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		} catch (Exception e) {
			System.out.println("获取生育保险信息数据异常！");
		}

	}

	// 失业
	public void crawlerShiyeInsurance(InsuranceRequestParameters parameter, String id) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析失业", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		try {
			// 获取当前的年
			SimpleDateFormat df4 = new SimpleDateFormat("yyyyMM");
			Calendar c4 = Calendar.getInstance();
			c4.add(Calendar.YEAR, -0);
			String beforeMonth4 = df4.format(c4.getTime());
			System.out.println(beforeMonth4);
			// 获取当前的前10年
			SimpleDateFormat df14 = new SimpleDateFormat("yyyyMM");
			Calendar c14 = Calendar.getInstance();
			c14.add(Calendar.YEAR, -10);
			String beforeMonth14 = df14.format(c14.getTime());
			System.out.println(beforeMonth14);

			String loginurl4 = "http://221.207.175.178:7989/ehrss-si-person/api/rights/payment/paydetail?endTime="
					+ beforeMonth4 + "&paymentType=&personId=" + id + "&startTime=" + beforeMonth14 + "&type=3";
			WebRequest webRequestlogin4;
			webRequestlogin4 = new WebRequest(new URL(loginurl4), HttpMethod.GET);
			Page pagelogin4 = webClient.getPage(webRequestlogin4);
			String contentAsString4 = pagelogin4.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------失业保险信息----------------------------------------------");
			System.out.println("失业保险信息-------" + contentAsString4);

			InsuranceHaerbinHtml insuranceHaerbinHtml = new InsuranceHaerbinHtml();
			insuranceHaerbinHtml.setHtml(contentAsString4);
			insuranceHaerbinHtml.setPageCount(1);
			insuranceHaerbinHtml.setTaskid(parameter.getTaskId());
			insuranceHaerbinHtml.setType("失业保险");
			insuranceHaerbinHtml.setUrl(loginurl4);
			insuranceHaerbinHtmlRepository.save(insuranceHaerbinHtml);

			JSONObject json = JSONObject.fromObject(contentAsString4);
			String list = json.getString("list");
			JSONArray array = JSONArray.fromObject(list);
			for (int i = 0; i < array.size(); i++) {
				String object = array.get(i).toString();
				JSONObject json2 = JSONObject.fromObject(object);

				// 缴费属期
				String jfsq = json2.getString("issue");
				System.out.println("缴费属期-----" + jfsq);
				// 对应缴费属期
				String dyjfsq = json2.getString("dissue");
				System.out.println("对应缴费属期-----" + dyjfsq);
				// 单位应缴
				String dwyj = json2.getString("companyPay");
				System.out.println("单位应缴-----" + dwyj);
				// 个人应缴
				String gryj = json2.getString("personPay");
				System.out.println("个人应缴-----" + gryj);
				// 应缴小计
				String yjxj = json2.getString("countPay");
				System.out.println(" 应缴小计-----" + yjxj);
				// 公司名称
				String gsmc = json2.getString("companyName");
				System.out.println("公司名称-----" + gsmc);
				// 险种类型
				String xzlx = json2.getString("type");
				System.out.println("险种类型-----" + xzlx);
				// 缴费基数
				String jfjs = json2.getString("basePay");
				System.out.println("缴费基数-----" + jfjs);
				// 缴费标识
				String jfbs = json2.getString("payFlag");
				System.out.println("缴费标识-----" + jfbs);
				// 缴费类型
				String jflx = json2.getString("payType");
				System.out.println("缴费类型-----" + jflx);
				// 划账日期
				String hzrq = json2.getString("payDate");
				System.out.println("划账日期-----" + hzrq);

				InsuranceHaerbinShiyeInfo insuranceHaerbinShiyeInfo = new InsuranceHaerbinShiyeInfo();
				insuranceHaerbinShiyeInfo.setTaskid(parameter.getTaskId());
				insuranceHaerbinShiyeInfo.setHzrq(hzrq);
				insuranceHaerbinShiyeInfo.setJflx(jflx);
				insuranceHaerbinShiyeInfo.setJfbs(jfbs);
				insuranceHaerbinShiyeInfo.setJfjs(jfjs);
				insuranceHaerbinShiyeInfo.setXzlx(xzlx);
				insuranceHaerbinShiyeInfo.setGsmc(gsmc);
				insuranceHaerbinShiyeInfo.setYjxj(yjxj);
				insuranceHaerbinShiyeInfo.setGryj(gryj);
				insuranceHaerbinShiyeInfo.setDwyj(dwyj);
				insuranceHaerbinShiyeInfo.setDyjfsq(dyjfsq);
				insuranceHaerbinShiyeInfo.setJfsq(jfsq);
				insuranceHaerbinShiyeInfoRepository.save(insuranceHaerbinShiyeInfo);

			}
			// 更新Task表为 失业信息数据爬取成功
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getPhasestatus());
			taskInsurance.setShiyeStatus(200);
			taskInsurance.setYiliaoStatus(200);
			taskInsurance.setYanglaoStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		} catch (Exception e) {
			System.out.println("获取失业保险信息数据异常！");
		}

	}

	/**
	 * 获取TaskInsurance
	 * 
	 * @param parameter
	 * @return
	 */
	public TaskInsurance getTaskInsurance(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		return taskInsurance;
	}

	// 将字符串md5加密，返回加密后的字符串
	public String md5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public static String getImagePath(Page page) throws Exception {
		File imageFile = getImageCustomPath();
		String imgagePath = imageFile.getAbsolutePath();
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new java.io.File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}

	// 创建验证码图片保存路径
	public static File getImageCustomPath() {
		String path = "";
		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			path = System.getProperty("user.dir") + "/verifyCodeImage/";
		} else {
			path = System.getProperty("user.home") + "/verifyCodeImage/";
		}
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".jpg";
		File codeImageFile = new File(path + "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true, false); //
		return codeImageFile;
	}
}
