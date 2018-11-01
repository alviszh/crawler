package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.liaocheng.InsuranceLiaochengHtml;
import com.microservice.dao.entity.crawler.insurance.liaocheng.InsuranceLiaochengMedicalInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.liaocheng.InsuranceLiaochengHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.liaocheng.InsuranceLiaochengMedicalInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.InsuranceService;

@Component
public class InsuranceLiaochengParser {

	@Autowired
	private TracerLog tracer;  

	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private InsuranceLiaochengHtmlRepository insuranceLiaochengHtmlRepository;

	@Autowired
	private InsuranceLiaochengMedicalInfoRepository insuranceLiaochengMedicalInfoRepository;

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;

	/**
	 * 获取个人医疗保险缴费记录查询结果
	 * 
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception
	 */
	public String getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

		tracer.addTag("parser.crawler.getMedicalInfo", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			String url = "http://www.lcsylbx.cn/search/yiliao_Result.asp";
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			List<NameValuePair> paramsList1 = new ArrayList<NameValuePair>();
			// 城镇职工参保人员请输入您医保证上的参保号码，15位的请在后面添加000补足18位。
			paramsList1.add(new NameValuePair("keyword", insuranceRequestParameters.getUsername()));
			paramsList1.add(new NameValuePair("B1", " 提 交 "));

			Map<String, String> map = new HashMap<String, String>();
			map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			map.put("Accept-Encoding", "gzip, deflate");
			map.put("Accept-Language", "zh-CN,zh;q=0.9");
			map.put("Cache-Control", "max-age=0");
			map.put("Connection", "keep-alive");
			map.put("Content-Type", "application/x-www-form-urlencoded");
			map.put("Host", "www.lcsylbx.cn");
			map.put("Origin", "http://www.lcsylbx.cn");
			map.put("Referer", "http://www.lcsylbx.cn/search_yiliao.asp");
			map.put("Upgrade-Insecure-Requests", "1");
			map.put("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");

			Page page = getPage(webClient, null, url, HttpMethod.POST, paramsList1, "GBK", null, map);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				String msg = WebCrawler.getAlertMsg();
				tracer.addTag(
						"InsuranceLiaochengParser.getMedicalInfo 个人信息:html" + insuranceRequestParameters.getTaskId(),
						"<xmp>" + html + "</xmp>");
				if (msg != null && msg.length() > 0) {
					tracer.addTag(
							"InsuranceLiaochengParser.getMedicalInfo 弹框信息" + insuranceRequestParameters.getTaskId(),
							msg);
					return msg;
				}
				Document doc = Jsoup.parse(html);
				String personal_number = insuranceService.getNextLabelByKeyword(doc, "个人编号");
				String pay_base = insuranceService.getNextLabelByKeyword(doc, "月缴费基数");
				String month_money = insuranceService.getNextLabelByKeyword(doc, "月划入账户金额");
				String sum_money = insuranceService.getNextLabelByKeyword(doc, "累积划入账户金额");
				String yearmoney_pay = insuranceService.getNextLabelByKeyword(doc, "缴费年月");

				InsuranceLiaochengMedicalInfo insuranceLiaochengMedicalInfo = new InsuranceLiaochengMedicalInfo(
						insuranceRequestParameters.getTaskId(), personal_number, pay_base, month_money, sum_money,
						yearmoney_pay);
				insuranceLiaochengMedicalInfoRepository.save(insuranceLiaochengMedicalInfo);
				InsuranceLiaochengHtml insuranceLiaochengHtml = new InsuranceLiaochengHtml(
						insuranceRequestParameters.getTaskId(), "insurance_liaocheng_medicalinfo", "1", url, html);
				insuranceLiaochengHtmlRepository.save(insuranceLiaochengHtml);
				tracer.addTag("InsuranceLiaochengService.getMedicalInfo", "信息已入库!");
				//个人信息
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				// 养老
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				// 生育
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				// 工商
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				// 医疗
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				// 失业
				insuranceService.changeCrawlerStatus(
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
				insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
				return "SUCCESS";
			}
		} catch (Exception e) {
			tracer.addTag("InsuranceLiaochengParser.getMedicalInfo---ERROR:",
					insuranceRequestParameters.getTaskId() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过url获取 Page
	 * 
	 * @param webClient
	 * @param taskid
	 * @param url
	 * @param type
	 * @param paramsList
	 * @param code
	 * @param body
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, String taskid, String url, HttpMethod type, List<NameValuePair> paramsList,
			String code, String body, Map<String, String> map) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);

		if (null != map) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				webRequest.setAdditionalHeader(entry.getKey(), entry.getValue());
			}
		}
		if (null != body && !"".equals(body)) {
			webRequest.setRequestBody(body);
		}
		if (null != code && !"".equals(code)) {
			webRequest.setCharset(Charset.forName(code));
		}
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracer.addTag("InsuranceLiaochengParser.getPage.url:" + url,
				"statusCode:" + statusCode + "---taskId:" + taskid);
		if (200 == statusCode) {
			return searchPage;
		}
		return null;
	}

}
