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
import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangBaseInfo;
import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangHtml;
import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.mianyang.InsurancemianyangYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.mianyang.InsurancemianyangBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.mianyang.InsurancemianyangGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.mianyang.InsurancemianyangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.mianyang.InsurancemianyangYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.mianyang.InsurancemianyangYibaoInfoRepository;
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
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.mianyang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.mianyang" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private InsurancemianyangHtmlRepository insurancemianyangHtmlRepository;

	@Autowired
	private InsurancemianyangBaseInfoRepository insurancemianyangBaseInfoRepository;

	@Autowired
	private InsurancemianyangYanglaoInfoRepository insurancemianyangYanglaoInfoRepository;

	@Autowired
	private InsurancemianyangYibaoInfoRepository insurancemianyangYibaoInfoRepository;

	@Autowired
	private InsurancemianyangGongShangInfoRepository insurancemianyangGongShangInfoRepository;

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
			String loginurl = "http://rsjapp.my.gov.cn/mycx/queryAction!query.do";
			WebRequest webRequestlogin;
			String body = "dto%5B'pageNo'%5D=1&dto%5B'funNo'%5D=Smyp01001&dto%5B'counts'%5D=";
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(body);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------基本信息----------------------------------------------");
			System.out.println("基本信息-------" + contentAsString);

			InsurancemianyangHtml insurancemianyangHtml = new InsurancemianyangHtml();
			insurancemianyangHtml.setHtml(contentAsString);
			insurancemianyangHtml.setPageCount(1);
			insurancemianyangHtml.setTaskid(parameter.getTaskId());
			insurancemianyangHtml.setType("基本信息");
			insurancemianyangHtml.setUrl(loginurl);
			insurancemianyangHtmlRepository.save(insurancemianyangHtml);

			if (!contentAsString.contains("\"success\":true")) {
				System.out.println("获取基本信息数据失败！");
			} else {
				System.out.println("获取基本信息数据成功！");
				JSONObject jsonobject = JSONObject.fromObject(contentAsString);
				String lists = jsonobject.getString("lists").trim();
				JSONObject jsonobject2 = JSONObject.fromObject(lists);
				String domainList = jsonobject2.getString("domainList").trim();
				JSONObject jsonobject3 = JSONObject.fromObject(domainList);
				String list = jsonobject3.getString("list").trim();
				JSONArray jsonarray = JSONArray.fromObject(list);
				String string = jsonarray.get(0).toString();
				JSONObject jsonobject4 = JSONObject.fromObject(string);
				//个人编号
				String grbh = jsonobject4.getString("aac001").trim();
				System.out.println("个人编号："+grbh);
				//身份证号
				String sfzh = jsonobject4.getString("aac002").trim();
				System.out.println("身份证号："+sfzh);
				//姓名
				String name = jsonobject4.getString("aac003").trim();
				System.out.println("姓名："+name);
				//出生年月
				String birth = jsonobject4.getString("aac006").trim();
				System.out.println("出生年月："+birth);
				//参工时间
				String cgsj = jsonobject4.getString("aac007").trim();
				System.out.println("参工时间："+cgsj);
				InsurancemianyangBaseInfo insurancemianyangBaseInfo = new InsurancemianyangBaseInfo();
				insurancemianyangBaseInfo.setTaskid(parameter.getTaskId());
				insurancemianyangBaseInfo.setGrbh(grbh);
				insurancemianyangBaseInfo.setSfzh(sfzh);
				insurancemianyangBaseInfo.setName(name);
				insurancemianyangBaseInfo.setBirth(birth);
				insurancemianyangBaseInfo.setCgsj(cgsj);
				insurancemianyangBaseInfoRepository.save(insurancemianyangBaseInfo);

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
			String loginurl = "http://rsjapp.my.gov.cn/mycx/queryAction!query.do";
			String body = "dto%5B'pageNo'%5D=1&dto%5B'funNo'%5D=Smyi02005&dto%5B'counts'%5D=1000";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(body);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------养老保险信息----------------------------------------------");
			System.out.println("养老保险信息-------" + contentAsString);

			InsurancemianyangHtml insurancemianyangHtml = new InsurancemianyangHtml();
			insurancemianyangHtml.setHtml(contentAsString);
			insurancemianyangHtml.setPageCount(1);
			insurancemianyangHtml.setTaskid(parameter.getTaskId());
			insurancemianyangHtml.setType("养老");
			insurancemianyangHtml.setUrl(loginurl);
			insurancemianyangHtmlRepository.save(insurancemianyangHtml);
			
			if (contentAsString.contains("\"success\":true")) {
				System.out.println("获取养老数据成功！");

				JSONObject jsonobject = JSONObject.fromObject(contentAsString);
				String lists = jsonobject.getString("lists").trim();
				JSONObject jsonobject2 = JSONObject.fromObject(lists);
				String domainList = jsonobject2.getString("domainList").trim();
				JSONObject jsonobject3 = JSONObject.fromObject(domainList);
				String list = jsonobject3.getString("list").trim();
				JSONArray jsonarray = JSONArray.fromObject(list);
				for (int i = 0; i < jsonarray.size(); i++) {
					String string = jsonarray.get(i).toString();
					JSONObject jsonobject4 = JSONObject.fromObject(string);
					//个人编号
					String grbh = jsonobject4.getString("aac001").trim();
					System.out.println("个人编号:"+grbh);
					//姓名
					String name = jsonobject4.getString("aac003").trim();
					System.out.println("姓名:"+name);
					//单位编号
					String dwbh = jsonobject4.getString("aab001").trim();
					System.out.println("单位编号:"+dwbh);
					//单位名称
					String dwmc = jsonobject4.getString("aae044").trim();
					System.out.println("单位名称:"+dwmc);
					//所属期号
					String ssqh = jsonobject4.getString("aae002").trim();
					System.out.println("所属期号:"+ssqh);
					//个人应缴
					String gryj = jsonobject4.getString("gryj").trim();
					System.out.println("个人应缴:"+gryj);
					//单位应缴
					String dwyj = jsonobject4.getString("dwyj").trim();
					System.out.println("单位应缴:"+dwyj);
					//个人实缴
					String grsj = jsonobject4.getString("grsj").trim();
					System.out.println("个人实缴:"+grsj);
					//单位实缴
					String dwsj = jsonobject4.getString("dwsj").trim();
					System.out.println("单位实缴:"+dwsj);
					//应缴合计
					String yjhj = jsonobject4.getString("yj").trim();
					System.out.println("应缴合计:"+yjhj);
					//实缴合计
					String sjhj = jsonobject4.getString("sj").trim();
					System.out.println("实缴合计:"+sjhj);
					
					InsurancemianyangYanglaoInfo insurancemianyangYanglaoInfo = new InsurancemianyangYanglaoInfo();
					insurancemianyangYanglaoInfo.setTaskid(parameter.getTaskId());
					insurancemianyangYanglaoInfo.setSjhj(sjhj);
					insurancemianyangYanglaoInfo.setYjhj(yjhj);
					insurancemianyangYanglaoInfo.setDwsj(dwsj);
					insurancemianyangYanglaoInfo.setGrsj(grsj);
					insurancemianyangYanglaoInfo.setDwyj(dwyj);
					insurancemianyangYanglaoInfo.setGryj(gryj);
					insurancemianyangYanglaoInfo.setSsqh(ssqh);
					insurancemianyangYanglaoInfo.setDwmc(dwmc);
					insurancemianyangYanglaoInfo.setDwbh(dwbh);
					insurancemianyangYanglaoInfo.setName(name);
					insurancemianyangYanglaoInfo.setGrbh(grbh);
					insurancemianyangYanglaoInfoRepository.save(insurancemianyangYanglaoInfo);
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
			String loginurl = "http://rsjapp.my.gov.cn/mycx/queryAction!query.do";
			String body = "dto%5B'pageNo'%5D=1&dto%5B'funNo'%5D=Smyk01001&dto%5B'counts'%5D=1000";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(body);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------医疗保险信息----------------------------------------------");
			System.out.println("医疗保险信息-------" + contentAsString);
			InsurancemianyangHtml insurancemianyangHtml = new InsurancemianyangHtml();
			insurancemianyangHtml.setHtml(contentAsString);
			insurancemianyangHtml.setPageCount(1);
			insurancemianyangHtml.setTaskid(parameter.getTaskId());
			insurancemianyangHtml.setType("医疗保险");
			insurancemianyangHtml.setUrl(loginurl);
			insurancemianyangHtmlRepository.save(insurancemianyangHtml);

			if (contentAsString.contains("\"success\":true")) {
				System.out.println("获取医疗数据成功！");

				JSONObject jsonobject = JSONObject.fromObject(contentAsString);
				String lists = jsonobject.getString("lists").trim();
				JSONObject jsonobject2 = JSONObject.fromObject(lists);
				String domainList = jsonobject2.getString("domainList").trim();
				JSONObject jsonobject3 = JSONObject.fromObject(domainList);
				String list = jsonobject3.getString("list").trim();
				JSONArray jsonarray = JSONArray.fromObject(list);
				for (int i = 0; i < jsonarray.size(); i++) {
					String string = jsonarray.get(i).toString();
					JSONObject jsonobject4 = JSONObject.fromObject(string);
					//个人编号
					String grbh = jsonobject4.getString("aac001").trim();
					System.out.println("个人编号:"+grbh);
					//姓名
					String name = jsonobject4.getString("aac003").trim();
					System.out.println("姓名:"+name);
					//单位编号
					String dwbh = jsonobject4.getString("aab001").trim();
					System.out.println("单位编号:"+dwbh);
					//单位名称
					String dwmc = jsonobject4.getString("aae044").trim();
					System.out.println("单位名称:"+dwmc);
					//所属期号
					String ssqh = jsonobject4.getString("aae002").trim();
					System.out.println("所属期号:"+ssqh);
					//个人应缴
					String gryj = jsonobject4.getString("gryj").trim();
					System.out.println("个人应缴:"+gryj);
					//单位应缴
					String dwyj = jsonobject4.getString("dwyj").trim();
					System.out.println("单位应缴:"+dwyj);
					//个人实缴
					String grsj = jsonobject4.getString("grsj").trim();
					System.out.println("个人实缴:"+grsj);
					//单位实缴
					String dwsj = jsonobject4.getString("dwsj").trim();
					System.out.println("单位实缴:"+dwsj);
					//应缴合计
					String yjhj = jsonobject4.getString("yj").trim();
					System.out.println("应缴合计:"+yjhj);
					//实缴合计
					String sjhj = jsonobject4.getString("sj").trim();
					System.out.println("实缴合计:"+sjhj);
					InsurancemianyangYibaoInfo insurancemianyangYibaoInfo = new InsurancemianyangYibaoInfo();
					insurancemianyangYibaoInfo.setTaskid(parameter.getTaskId());
					insurancemianyangYibaoInfo.setSjhj(sjhj);
					insurancemianyangYibaoInfo.setYjhj(yjhj);
					insurancemianyangYibaoInfo.setDwsj(dwsj);
					insurancemianyangYibaoInfo.setGrsj(grsj);
					insurancemianyangYibaoInfo.setDwyj(dwyj);
					insurancemianyangYibaoInfo.setGryj(gryj);
					insurancemianyangYibaoInfo.setSsqh(ssqh);
					insurancemianyangYibaoInfo.setDwmc(dwmc);
					insurancemianyangYibaoInfo.setDwbh(dwbh);
					insurancemianyangYibaoInfo.setName(name);
					insurancemianyangYibaoInfo.setGrbh(grbh);
					insurancemianyangYibaoInfoRepository.save(insurancemianyangYibaoInfo);

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
			String loginurl = "http://rsjapp.my.gov.cn/mycx/queryAction!query.do";
			String body = "dto%5B'pageNo'%5D=1&dto%5B'funNo'%5D=Smyl01002&dto%5B'counts'%5D=1000";
			WebRequest webRequestlogin;
			webRequestlogin = new WebRequest(new URL(loginurl), HttpMethod.POST);
			webRequestlogin.setRequestBody(body);
			Page pagelogin = webClient.getPage(webRequestlogin);
			String contentAsString = pagelogin.getWebResponse().getContentAsString();
			System.out.println(
					"------------------------------------------工伤保险信息----------------------------------------------");
			System.out.println("工伤保险信息-------" + contentAsString);
			InsurancemianyangHtml insurancemianyangHtml = new InsurancemianyangHtml();
			insurancemianyangHtml.setHtml(contentAsString);
			insurancemianyangHtml.setPageCount(1);
			insurancemianyangHtml.setTaskid(parameter.getTaskId());
			insurancemianyangHtml.setType("工伤保险");
			insurancemianyangHtml.setUrl(loginurl);
			insurancemianyangHtmlRepository.save(insurancemianyangHtml);

			if (contentAsString.contains("\"success\":true")) {
				System.out.println("获取工伤数据成功！");

				JSONObject jsonobject = JSONObject.fromObject(contentAsString);
				String lists = jsonobject.getString("lists").trim();
				JSONObject jsonobject2 = JSONObject.fromObject(lists);
				String domainList = jsonobject2.getString("domainList").trim();
				JSONObject jsonobject3 = JSONObject.fromObject(domainList);
				String list = jsonobject3.getString("list").trim();
				JSONArray jsonarray = JSONArray.fromObject(list);
				for (int i = 0; i < jsonarray.size(); i++) {
					String string = jsonarray.get(i).toString();
					JSONObject jsonobject4 = JSONObject.fromObject(string);
					//个人编号
					String grbh = jsonobject4.getString("aac001").trim();
					System.out.println("个人编号:"+grbh);
					//姓名
					String name = jsonobject4.getString("aac003").trim();
					System.out.println("姓名:"+name);
					//单位编号
					String dwbh = jsonobject4.getString("aab001").trim();
					System.out.println("单位编号:"+dwbh);
					//单位名称
					String dwmc = jsonobject4.getString("aae044").trim();
					System.out.println("单位名称:"+dwmc);
					//所属期号
					String ssqh = jsonobject4.getString("aae002").trim();
					System.out.println("所属期号:"+ssqh);
					//个人应缴
					String gryj = jsonobject4.getString("gryj").trim();
					System.out.println("个人应缴:"+gryj);
					//单位应缴
					String dwyj = jsonobject4.getString("dwyj").trim();
					System.out.println("单位应缴:"+dwyj);
					//个人实缴
					String grsj = jsonobject4.getString("grsj").trim();
					System.out.println("个人实缴:"+grsj);
					//单位实缴
					String dwsj = jsonobject4.getString("dwsj").trim();
					System.out.println("单位实缴:"+dwsj);
					//应缴合计
					String yjhj = jsonobject4.getString("yj").trim();
					System.out.println("应缴合计:"+yjhj);
					//实缴合计
					String sjhj = jsonobject4.getString("sj").trim();
					System.out.println("实缴合计:"+sjhj);
					InsurancemianyangGongShangInfo insurancemianyangGongShangInfo = new InsurancemianyangGongShangInfo();
					insurancemianyangGongShangInfo.setTaskid(parameter.getTaskId());
					insurancemianyangGongShangInfo.setSjhj(sjhj);
					insurancemianyangGongShangInfo.setYjhj(yjhj);
					insurancemianyangGongShangInfo.setDwsj(dwsj);
					insurancemianyangGongShangInfo.setGrsj(grsj);
					insurancemianyangGongShangInfo.setDwyj(dwyj);
					insurancemianyangGongShangInfo.setGryj(gryj);
					insurancemianyangGongShangInfo.setSsqh(ssqh);
					insurancemianyangGongShangInfo.setDwmc(dwmc);
					insurancemianyangGongShangInfo.setDwbh(dwbh);
					insurancemianyangGongShangInfo.setName(name);
					insurancemianyangGongShangInfo.setGrbh(grbh);
					insurancemianyangGongShangInfoRepository.save(insurancemianyangGongShangInfo);

				}
				// 更新Task表为工伤信息数据爬取成功
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getPhasestatus());
				taskInsurance.setGongshangStatus(200);
				taskInsurance.setShengyuStatus(200);
				taskInsurance.setShiyeStatus(200);
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
