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
import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengBaseInfo;
import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengHtml;
import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.kaifeng.InsurancekaifengYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.kaifeng.InsurancekaifengBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.kaifeng.InsurancekaifengGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.kaifeng.InsurancekaifengHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.kaifeng.InsurancekaifengYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.kaifeng.InsurancekaifengYibaoInfoRepository;
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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.kaifeng" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.kaifeng" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsurancekaifengHtmlRepository insurancekaifengHtmlRepository;

	@Autowired
	private InsurancekaifengBaseInfoRepository insurancekaifengBaseInfoRepository;

	@Autowired
	private InsurancekaifengYanglaoInfoRepository insurancekaifengYanglaoInfoRepository;

	@Autowired
	private InsurancekaifengYibaoInfoRepository insurancekaifengYibaoInfoRepository;

	@Autowired
	private InsurancekaifengGongShangInfoRepository insurancekaifengGongShangInfoRepository;

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
			String loginurl = "https://gr.kf12333.cn/grpt/zgbx/zgbxJbxxcxAction001.action";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsurancekaifengHtml insurancekaifengHtml = new InsurancekaifengHtml();
			insurancekaifengHtml.setHtml(contentAsString);
			insurancekaifengHtml.setPageCount(1);
			insurancekaifengHtml.setTaskid(parameter.getTaskId());
			insurancekaifengHtml.setType("基本信息");
			insurancekaifengHtml.setUrl(loginurl);
			insurancekaifengHtmlRepository.save(insurancekaifengHtml);

			JSONObject json = JSONObject.fromObject(contentAsString);
            String json_jsxx_data = json.getString("json_jsxx_data").trim().toString();
            JSONObject json3 = JSONObject.fromObject(json_jsxx_data);
            String data = json3.getString("data").trim().toString();
            JSONArray array = JSONArray.fromObject(data);
            String string = array.get(0).toString();
            JSONObject json2 = JSONObject.fromObject(string);
            //个人编号
            String grbh = json2.getString("F002").toString().trim();
            System.out.println("个人编号-----"+grbh);
            //职工姓名
            String zgxm = json2.getString("F004").toString().trim();
            System.out.println("职工姓名-----"+zgxm);
            //性别
            String xb = json2.getString("F005ZH").toString().trim();
            System.out.println("性别-----"+xb);
            //民族
            String mz = json2.getString("F006ZH").toString().trim();
            System.out.println("民族-----"+mz);
            //人员状态
            String ryzt = json2.getString("F009ZH").toString().trim();
            System.out.println("人员状态-----"+ryzt);
            //参加工作时间
            String cjgzsj = json2.getString("F008").toString().trim();
            System.out.println("参加工作时间-----"+cjgzsj);
            //出生日期
            String csrq = json2.getString("F007").toString().trim();
            System.out.println("出生日期-----"+csrq);
            //身份证号码
            String sfzhm = json2.getString("F003").toString().trim();
            System.out.println("身份证号码-----"+sfzhm);
				InsurancekaifengBaseInfo insurancekaifengBaseInfo = new InsurancekaifengBaseInfo();
				insurancekaifengBaseInfo.setTaskid(parameter.getTaskId());
				insurancekaifengBaseInfo.setSfzhm(sfzhm);
				insurancekaifengBaseInfo.setCsrq(csrq);
				insurancekaifengBaseInfo.setCjgzsj(cjgzsj);
				insurancekaifengBaseInfo.setRyzt(ryzt);
				insurancekaifengBaseInfo.setMz(mz);
				insurancekaifengBaseInfo.setXb(xb);
				insurancekaifengBaseInfo.setZgxm(zgxm);
				insurancekaifengBaseInfo.setGrbh(grbh);
				insurancekaifengBaseInfoRepository.save(insurancekaifengBaseInfo);

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
			String loginurl = "https://gr.kf12333.cn/grpt/zgbx/zgbxYlbxjfxxcxAction001.action";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

				InsurancekaifengHtml insurancekaifengHtml = new InsurancekaifengHtml();
				insurancekaifengHtml.setHtml(contentAsString);
				insurancekaifengHtml.setPageCount(1);
				insurancekaifengHtml.setTaskid(parameter.getTaskId());
				insurancekaifengHtml.setType("养老保险");
				insurancekaifengHtml.setUrl(loginurl);
				insurancekaifengHtmlRepository.save(insurancekaifengHtml);
			
				JSONObject json = JSONObject.fromObject(contentAsString);
	            String json_jsxx_data = json.getString("json_jsxx_data").trim().toString();
	            JSONObject json3 = JSONObject.fromObject(json_jsxx_data);
	            String data = json3.getString("data").trim().toString();
            JSONArray array = JSONArray.fromObject(data);
			for (int i = 0; i < array.size(); i++) {
				String string = array.get(i).toString().trim();
				JSONObject json2 = JSONObject.fromObject(string);
				//起止年月
	            String year = json2.getString("F002").toString().trim()+"-"+json2.getString("F003").toString().trim();
	            System.out.println("起止年月-----"+year);
	            //单位编号
	            String dwbh = json2.getString("F004").toString().trim();
	            System.out.println("单位编号-----"+dwbh);
	            //单位名称
	            String dwmc = json2.getString("F004ZH").toString().trim();
	            System.out.println("单位名称-----"+dwmc);
	            //申报工资
	            String sbgz = json2.getString("F005").toString().trim();
	            System.out.println("申报工资-----"+sbgz);
	            //缴费基数
	            String jfjs = json2.getString("F006").toString().trim();
	            System.out.println("缴费基数-----"+jfjs);
	            InsurancekaifengYanglaoInfo insurancekaifengYanglaoInfo = new InsurancekaifengYanglaoInfo();
	            insurancekaifengYanglaoInfo.setTaskid(parameter.getTaskId());
	            insurancekaifengYanglaoInfo.setYear(year);
	            insurancekaifengYanglaoInfo.setDwbh(dwbh);
	            insurancekaifengYanglaoInfo.setDwmc(dwmc);
	            insurancekaifengYanglaoInfo.setSbgz(sbgz);
	            insurancekaifengYanglaoInfo.setJfjs(jfjs);
	            insurancekaifengYanglaoInfoRepository.save(insurancekaifengYanglaoInfo);
			}


				// 更新Task表为 养老信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhasestatus());
				taskInsurance.setYanglaoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			
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
			String loginurl = "https://gr.kf12333.cn/grpt/zgbx/zgbxMlbxjfxxcxAction001.action";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);
				
				InsurancekaifengHtml insurancekaifengHtml = new InsurancekaifengHtml();
				insurancekaifengHtml.setHtml(contentAsString);
				insurancekaifengHtml.setPageCount(1);
				insurancekaifengHtml.setTaskid(parameter.getTaskId());
				insurancekaifengHtml.setType("医疗保险");
				insurancekaifengHtml.setUrl(loginurl);
				insurancekaifengHtmlRepository.save(insurancekaifengHtml);

				JSONObject json = JSONObject.fromObject(contentAsString);
	            String json_jsxx_data = json.getString("json_jsxx_data").trim().toString();
	            JSONObject json3 = JSONObject.fromObject(json_jsxx_data);
	            String data = json3.getString("data").trim().toString();
	            JSONArray array = JSONArray.fromObject(data);
				for (int i = 0; i < array.size(); i++) {
					String string = array.get(i).toString().trim();
					JSONObject json2 = JSONObject.fromObject(string);
					//计划年月
		            String jhyf = json2.getString("F003").toString().trim();
		            System.out.println("计划年月-----"+jhyf);
		            //征缴月份
		            String zjyf = json2.getString("F004").toString().trim();
		            System.out.println("征缴月份-----"+zjyf);
		            //缴费基数 
		            String jfjs = json2.getString("F006").toString().trim();
		            System.out.println("缴费基数 -----"+jfjs);
		            //单位缴费
		            String dwjf = json2.getString("F008").toString().trim();
		            System.out.println("单位缴费-----"+dwjf);
		            //个人缴费
		            String grjf = json2.getString("F009").toString().trim();
		            System.out.println("个人缴费-----"+grjf);
		            //合计
		            String hj = json2.getString("F007").toString().trim();
		            System.out.println("合计-----"+hj);
		            //记入个人部分
		            String jrgrbf = json2.getString("F011").toString().trim();
		            System.out.println("记入个人部分-----"+jrgrbf);
		            //缴费标志
		            String jfbz = json2.getString("F005ZH").toString().trim();
		            System.out.println("缴费标志-----"+jfbz);
		            //到账日期
		            String dzrq = json2.getString("F013").toString().trim();
		            System.out.println("到账日期-----"+dzrq);
		            //单位名称
		            String dwmc = json2.getString("F001ZH").toString().trim();
		            System.out.println("单位名称-----"+dwmc);
				
					InsurancekaifengYibaoInfo insurancekaifengYibaoInfo = new InsurancekaifengYibaoInfo();
					insurancekaifengYibaoInfo.setTaskid(parameter.getTaskId());
					insurancekaifengYibaoInfo.setDwmc(dwmc);
					insurancekaifengYibaoInfo.setDzrq(dzrq);
					insurancekaifengYibaoInfo.setJfbz(jfbz);
					insurancekaifengYibaoInfo.setJrgrbf(jrgrbf);
					insurancekaifengYibaoInfo.setHj(hj);
					insurancekaifengYibaoInfo.setGrjf(grjf);
					insurancekaifengYibaoInfo.setDwjf(dwjf);
					insurancekaifengYibaoInfo.setJfjs(jfjs);
					insurancekaifengYibaoInfo.setZjyf(zjyf);
					insurancekaifengYibaoInfo.setJhyf(jhyf);
					insurancekaifengYibaoInfoRepository.save(insurancekaifengYibaoInfo);
				}

				// 更新Task表为 医疗信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getPhasestatus());
				taskInsurance.setYiliaoStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			
		} catch (Exception e) {
			System.out.println("获取医疗保险信息数据异常！");
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
			String loginurl = "https://gr.kf12333.cn/grpt/zgbx/zgbxGsbxjfxxcxAction002.action";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);
				
				InsurancekaifengHtml insurancekaifengHtml = new InsurancekaifengHtml();
				insurancekaifengHtml.setHtml(contentAsString);
				insurancekaifengHtml.setPageCount(1);
				insurancekaifengHtml.setTaskid(parameter.getTaskId());
				insurancekaifengHtml.setType("工伤保险");
				insurancekaifengHtml.setUrl(loginurl);
				insurancekaifengHtmlRepository.save(insurancekaifengHtml);

				JSONObject json = JSONObject.fromObject(contentAsString);
	            String json_jsxx_data = json.getString("json_jsxx_data").trim().toString();
	            JSONObject json2 = JSONObject.fromObject(json_jsxx_data);
	            String data = json2.getString("data").trim().toString();
	            JSONArray array = JSONArray.fromObject(data);
				for (int i = 0; i < array.size(); i++) {
					String string = array.get(i).toString().trim();
					JSONObject json3 = JSONObject.fromObject(string);
					//起止年月
		            String year = json3.getString("F002").toString().trim()+"-"+json2.getString("F003").toString().trim();
		            System.out.println("起止年月-----"+year);
		            //单位编号
		            String dwbh = json3.getString("F004").toString().trim();
		            System.out.println("单位编号-----"+dwbh);
		            //单位名称
		            String dwmc = json3.getString("F004ZH").toString().trim();
		            System.out.println("单位名称-----"+dwmc);
		            //申报工资
		            String sbgz = json3.getString("F005").toString().trim();
		            System.out.println("申报工资-----"+sbgz);
		            //缴费基数
		            String jfjs = json3.getString("F006").toString().trim();
		            System.out.println("缴费基数-----"+jfjs);
		            InsurancekaifengGongShangInfo insurancekaifengGongShangInfo = new InsurancekaifengGongShangInfo();
		            insurancekaifengGongShangInfo.setTaskid(parameter.getTaskId());
		            insurancekaifengGongShangInfo.setJfjs(jfjs);
		            insurancekaifengGongShangInfo.setSbgz(sbgz);
		            insurancekaifengGongShangInfo.setDwmc(dwmc);
		            insurancekaifengGongShangInfo.setDwbh(dwbh);
		            insurancekaifengGongShangInfo.setYear(year);
		            insurancekaifengGongShangInfoRepository.save(insurancekaifengGongShangInfo);
				}

				// 更新Task表为工伤信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhasestatus());
				taskInsurance.setGongshangStatus(200);
				taskInsurance.setShengyuStatus(200);
				taskInsurance.setShiyeStatus(200);
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			
		} catch (Exception e) {
			System.out.println("获取工伤保险信息数据异常！");
		}

		return "";
	}
}
