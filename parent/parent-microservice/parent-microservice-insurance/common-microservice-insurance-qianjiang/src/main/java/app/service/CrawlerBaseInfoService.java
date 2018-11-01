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
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.qianjiang.InsuranceqianjiangBaseInfo;
import com.microservice.dao.entity.crawler.insurance.qianjiang.InsuranceqianjiangHtml;
import com.microservice.dao.entity.crawler.insurance.qianjiang.InsuranceqianjiangInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.qianjiang.InsuranceqianjiangBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.qianjiang.InsuranceqianjiangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.qianjiang.InsuranceqianjiangInfoRepository;
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
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceqianjiangHtmlRepository insuranceqianjiangHtmlRepository;
	@Autowired
	private InsuranceqianjiangBaseInfoRepository insuranceqianjiangBaseInfoRepository;
	@Autowired
	private InsuranceqianjiangInfoRepository insuranceqianjiangInfoRepository;
	/**
	 * 爬取解析五险信息
	 * 
	 * @param parameter
	 * @param cookies
	 * @param pid
	 * @return
	 */
	public String getAllData(InsuranceRequestParameters parameter) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析五险信息", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		// 五险信息界面通过请求建立连接
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try{
			
			// 中间过滤的请求
			String url2 = "http://219.138.153.190:806/personageDemand/Index";
			WebRequest webRequest2 = new WebRequest(new URL(url2), HttpMethod.GET);
			webClient.getPage(webRequest2);
			
			tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析基本信息", parameter.toString());
			
			// 基本信息的请求
			String url3 = "http://219.138.153.190:806/personageDemand/grcbjfzmyb";
			WebRequest webRequest3 = new WebRequest(new URL(url3), HttpMethod.GET);
			HtmlPage html3 = webClient.getPage(webRequest3);
			String contentAsString3 = html3.getWebResponse().getContentAsString();
			
			InsuranceqianjiangHtml insuranceqianjiangHtml = new InsuranceqianjiangHtml();
			insuranceqianjiangHtml.setHtml(contentAsString3);
			insuranceqianjiangHtml.setPageCount(1);
			insuranceqianjiangHtml.setTaskid(parameter.getTaskId());
			insuranceqianjiangHtml.setType("五险信息");
			insuranceqianjiangHtml.setUrl(url3);
			insuranceqianjiangHtmlRepository.save(insuranceqianjiangHtml);
			
			Document doc = Jsoup.parse(contentAsString3);
			Element table = doc.getElementsByTag("table").get(0);
			Elements trs = table.getElementsByTag("tr");
			Elements tds0 = trs.get(0).getElementsByTag("td");
			// 姓名
			String xm = tds0.get(1).text();
			System.out.println("姓名：" + xm);
			// 身份证号码
			String sfzhm = tds0.get(3).text();
			System.out.println("身份证号码：" + sfzhm);
			Elements tds1 = trs.get(1).getElementsByTag("td");
			// 参保状态
			String cbzt = tds1.get(1).text();
			System.out.println("参保状态：" + cbzt);
			// 社保卡号
			String sbkh = tds1.get(3).text();
			System.out.println("社保卡号：" + sbkh);
			
			
			InsuranceqianjiangBaseInfo insuranceqianjiangBaseInfo = new InsuranceqianjiangBaseInfo();
			insuranceqianjiangBaseInfo.setTaskid(parameter.getTaskId());
			insuranceqianjiangBaseInfo.setXm(xm);
			insuranceqianjiangBaseInfo.setSfzhm(sfzhm);
			insuranceqianjiangBaseInfo.setCbzt(cbzt);
			insuranceqianjiangBaseInfo.setSbkh(sbkh);
			insuranceqianjiangBaseInfoRepository.save(insuranceqianjiangBaseInfo);

			// 更新Task表为 基本信息数据爬取成功
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhasestatus());
			taskInsurance.setUserInfoStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			
			tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取解析五险信息", parameter.toString());
			// 五险信息的请求
			String url4 = "http://219.138.153.190:806/personageDemand/PayCostRecordQuery?page=1&count=500&lx=0";
			WebRequest webRequest4 = new WebRequest(new URL(url4), HttpMethod.GET);
			HtmlPage html4= webClient.getPage(webRequest4);
			String contentAsString4 = html4.getWebResponse().getContentAsString();
			System.out.println(contentAsString4);
			
			InsuranceqianjiangHtml insuranceqianjiangHtml1 = new InsuranceqianjiangHtml();
			insuranceqianjiangHtml1.setHtml(contentAsString4);
			insuranceqianjiangHtml1.setPageCount(1);
			insuranceqianjiangHtml1.setTaskid(parameter.getTaskId());
			insuranceqianjiangHtml1.setType("五险信息");
			insuranceqianjiangHtml1.setUrl(url4);
			insuranceqianjiangHtmlRepository.save(insuranceqianjiangHtml1);
			
			Document doc2 = Jsoup.parse(contentAsString4);
			String DataJson = doc2.getElementById("DataJson").val();
			JSONObject object = JSONObject.fromObject(DataJson);
			String rows = object.getString("rows").trim();
			JSONArray array = JSONArray.fromObject(rows);
			for (int i = 0; i < array.size(); i++) {
				String string = array.get(i).toString();
				JSONObject object2 = JSONObject.fromObject(string);
				//单位名称
				String dwmc = object2.getString("DWMC").trim();
				System.out.println("单位名称"+dwmc);
				//险种类型
				String xzlx = object2.getString("XZLX").trim();
				System.out.println("险种类型"+xzlx);
				//缴费状态
				String jfzt = object2.getString("YJLX").trim();
				System.out.println("缴费状态"+jfzt);
				//缴费年月
				String jfny = object2.getString("JFNY").trim();
				System.out.println("缴费年月"+jfny);
				//缴费基数
				String jfjs = object2.getString("JFJS").trim();
				System.out.println("缴费基数"+jfjs);
				// 缴费金额
				String jfje = object2.getString("GRJFJE").trim();
				System.out.println("缴费金额"+jfje);
				System.out.println("======================");
				
				InsuranceqianjiangInfo insuranceqianjiangInfo = new InsuranceqianjiangInfo();
				insuranceqianjiangInfo.setTaskid(parameter.getTaskId());
				insuranceqianjiangInfo.setJfje(jfje);
				insuranceqianjiangInfo.setJfjs(jfjs);
				insuranceqianjiangInfo.setJfny(jfny);
				insuranceqianjiangInfo.setJfzt(jfzt);
				insuranceqianjiangInfo.setXzlx(xzlx);
				insuranceqianjiangInfo.setDwmc(dwmc);
				insuranceqianjiangInfoRepository.save(insuranceqianjiangInfo);
			}
			
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getPhasestatus());
			taskInsurance.setYanglaoStatus(200);
			taskInsurance.setYiliaoStatus(200);
			taskInsurance.setShengyuStatus(200);
			taskInsurance.setShiyeStatus(200);
			taskInsurance.setGongshangStatus(200);
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
			
		} catch (Exception e) {
			System.out.println("获取五险信息数据异常！");
		}
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:五险信息爬取解析完成", "");
		return "";
	}

}
