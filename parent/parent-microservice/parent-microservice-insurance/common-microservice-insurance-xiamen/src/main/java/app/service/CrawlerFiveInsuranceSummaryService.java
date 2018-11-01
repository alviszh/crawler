package app.service;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceXiamenCrawlerResult;
import app.enums.InsuranceXiamenCrawlerType;
import app.htmlparser.XiamenCrawlerParser;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenHtml;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenPaymentSummaryInfo;
import com.microservice.dao.repository.crawler.insurance.xiamen.InsuranceXiamenHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.xiamen.XiamenFiveInsuranceSummaryRepository;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Created by kaixu on 2017/9/28.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.xiamen" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.xiamen" })
public class CrawlerFiveInsuranceSummaryService {
	@Autowired
	private TracerLog tracer;

	@Autowired
	private XiamenInsuranceService xiamenInsuranceService;

	@Autowired
	private InsuranceXiamenHtmlRepository insuranceXiamenHtmlRepository;
	@Autowired
	private XiamenFiveInsuranceSummaryRepository xiamenFiveInsuranceSummaryRepository;

	@Autowired
	private XiamenCrawlerParser xiamenCrawlerParser;
	@Autowired
	private CrawlerFiveInsuranceDetailsService crawlerFiveInsuranceDetailsService;

	/**
	 * 获取五险汇总页 发送http get 请求方式
	 * 
	 * @param taskInsurance
	 * @param pageNum
	 */
	@Async
	public void getFiveInsuranceSummaryPage(TaskInsurance taskInsurance, Integer pageNum, int count) {
		WebParam<HtmlPage> webParam = new WebParam<>();
		String url = "https://app.xmhrss.gov.cn/UCenter/sbjfxxcx.xhtml?xzdm00=&pageNo=";
		url = url + pageNum;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding:gzip", "deflate, br");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "app.xmhrss.gov.cn");
			webRequest.setAdditionalHeader("Referer",
					"https://app.xmhrss.gov.cn/UCenter/sbjfxxcx.xhtml?xzdm00=&pageNo=1");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
			HtmlPage htmlPage = webClient.getPage(webRequest);
			webClient.waitForBackgroundJavaScript(10000); // 该方法在getPage()方法之后调用才能生效
			int status = htmlPage.getWebResponse().getStatusCode();
			if (status == 200) {
				tracer.addTag("CrawlerFiveInsuranceSummaryService.getFiveInsuranceSummaryPage", "五险汇总第" + pageNum
						+ "页 webclient:" + webClient.hashCode() + "=========webParam:" + webParam.hashCode());
				System.out.println("五险汇总第" + pageNum + "页获取成功！");
				// 保存html信息
				InsuranceXiamenHtml insuranceShenzhenHtml = new InsuranceXiamenHtml();
				insuranceShenzhenHtml.setHtml(htmlPage.asText());
				insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
				insuranceShenzhenHtml.setType(InsuranceXiamenCrawlerType.BASE_INFO.getCode());
				insuranceShenzhenHtml.setUrl(htmlPage.getUrl().toString());
				insuranceXiamenHtmlRepository.save(insuranceShenzhenHtml);
				// 保存五险汇总页
				tracer.addTag("CrawlerFiveInsuranceSummaryService.getFiveInsuranceSummaryPage:解析完成,调用解析五险汇总",
						webParam.toString());
				// 调用解析五险汇总
				parserCrawledFiveInsuranceInfo(htmlPage, taskInsurance, count, pageNum);
				// 获取每页保险详细信息页面
				String html = htmlPage.getWebResponse().getContentAsString();
				Document doc = Jsoup.parse(html);
				Elements trs = doc.select("table.tab5 > tbody > tr");
				int listSize = trs.size();
				for (int j = 1; j <= listSize; j++) {
					System.out.println("五险详情---第：" + pageNum + "页。第：" + j + "条数据");
					tracer.addTag("CrawlerFiveInsuranceSummaryService.getFiveInsuranceSummaryPage",
							"五险详情---第：" + pageNum + "页。第：" + j + "条数据");
					crawlerFiveInsuranceDetailsService.detailedInsurance(htmlPage, pageNum, j, taskInsurance, 1, count);
				}
			} else {
				System.out.println("五险汇总第" + pageNum + "页获取失败！");
			}
		} catch (Exception e) {
			System.out.println("五险汇总第" + pageNum + "页获取失败！");
			xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
					InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_FAILUE, count);
			e.printStackTrace();
		}
	}

	/**
	 * 解析五险汇总页面
	 * 
	 * @param
	 * @return
	 */
	@Async
	private void parserCrawledFiveInsuranceInfo(HtmlPage htmlPage, TaskInsurance taskInsurance, int count,
			int pageNum) {
		// 更新Task表为 五险信息数据解析中
		tracer.addTag("ParserCrawledFiveInsuranceService.parserCrawledFiveInsuranceInfo:开始解析五险信息", htmlPage.asText());
		// 开始解析
		WebParam<List<InsuranceXiamenPaymentSummaryInfo>> fiveSummaryInfo = xiamenCrawlerParser
				.parserFiveInsuranceSummary(htmlPage, pageNum);
		tracer.addTag("ParserCrawledFiveInsuranceService.parserFiveInsuranceSummary:解析完成", fiveSummaryInfo.toString());
		// 五险汇总信息入库
		if (InsuranceXiamenCrawlerResult.SUCCESS.getCode().equals(fiveSummaryInfo.getCode())
				&& fiveSummaryInfo.getData() != null) {
			List<InsuranceXiamenPaymentSummaryInfo> insurancePaymentSummaryInfos = fiveSummaryInfo.getData();
			for (InsuranceXiamenPaymentSummaryInfo insurancePaymentSummaryInfo : insurancePaymentSummaryInfos) {
				insurancePaymentSummaryInfo.setTaskId(taskInsurance.getTaskid());
				xiamenFiveInsuranceSummaryRepository.save(insurancePaymentSummaryInfo);
			}
			xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_FIVE_SUCCESS, count);
		} else {
			// 更新Task表为 五险汇总信息数据解析失败
			System.out.println("更新Task表为 五险汇总信息数据解析失败");
			xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_FIVE_FAILUE, count);
		}
	}

}
