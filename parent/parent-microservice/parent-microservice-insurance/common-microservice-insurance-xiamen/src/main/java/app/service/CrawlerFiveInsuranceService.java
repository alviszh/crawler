package app.service;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.htmlparser.XiamenCrawler;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * 爬取养老保险 Service
 * 
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.xiamen" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.xiamen" })
public class CrawlerFiveInsuranceService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private XiamenCrawler xiamenCrawler;

	@Autowired
	private XiamenInsuranceService xiamenInsuranceService;
	@Autowired
	private CrawlerFiveInsuranceSummaryService crawlerFiveInsuranceSummaryService;

	/**
	 * 爬取五险汇总页面
	 * 
	 * @param parameter
	 * @param taskInsurance
	 * @return
	 */
	@Async
	public void crawlerFiveInsurance(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			HtmlPage inHomePage, Map<String, WebParam<HtmlPage>> resultMap) {
		tracer.addTag("CrawlerFiveInsuranceService.crawlerFiveInsurance:开始爬取五险汇总页面", parameter.toString());
		/**
		 * 爬取数据思路： step1：获取五险查询页面 step2：点击step1 中的查询按钮，得到查询结果页面 step3: 获取总页数
		 * step4：对step2 中分页的页面并发执行step5： 扩展： 异步拿到每个页面，执行step5 step5：解析页面信息->
		 * 基本信息+详细信息（详细信息包括：每条数据的点击情况！）
		 */
		// step1：获取五险查询页面
		HtmlPage fiveInsuranceSearchPage = xiamenCrawler.inFiveInsuranceSearchPage(inHomePage);
		// step2: 点击step1 中的查询按钮，得到查询结果页面
		final HtmlPage findAllFiveInsurance = xiamenCrawler.findAllFiveInsurance(fiveInsuranceSearchPage);
		// step3：获取总页数
		HtmlElement pageSizeElement = findAllFiveInsurance
				.querySelector("#pageUtiler > tbody > tr > td.tgray2 > span:nth-child(2)");
		Integer pageSize = Integer.parseInt(pageSizeElement.asText());
		try {
			int lastPageSize = getLastPageCounts(taskInsurance, pageSize);
			// 计算总保险数量(20：每一页的条数)
			int count = (pageSize - 1) * 20 + lastPageSize;
			// step4: 获取五险汇总页
			for (int i = 1; i <= pageSize; i++) {
				// 异步获取五险汇总分页,页面
				tracer.addTag("CrawlerFiveInsuranceService.crawlerFiveInsurance", "五险汇总" + i + "页爬取中...");
				System.out.println("五险汇总" + i + "页爬取中...");
				crawlerFiveInsuranceSummaryService.getFiveInsuranceSummaryPage(taskInsurance, i, count);
			}
		} catch (IOException e) {
			// 获取最后一页保险条数失败
			tracer.addTag("CrawlerFiveInsuranceService.crawlerFiveInsurance", "获取最后一页保险条数失败" + e);
			xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
					InsuranceStatusCode.INSURANCE_CRAWLER_FIVE_FAILUE, 0);
			e.printStackTrace();
		}

	}

	/**
	 * 获取最后一页中，五险的条数。
	 */
	private Integer getLastPageCounts(TaskInsurance taskInsurance, int pageNum) throws IOException {
		Integer listSize = null;
		String url = "https://app.xmhrss.gov.cn/UCenter/sbjfxxcx.xhtml?xzdm00=&pageNo=";
		url = url + pageNum;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		for (Cookie cookie : cookies) {
			webClient.getCookieManager().addCookie(cookie);
		}
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webRequest.setAdditionalHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding:gzip", "deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "app.xmhrss.gov.cn");
		webRequest.setAdditionalHeader("Referer", "https://app.xmhrss.gov.cn/UCenter/sbjfxxcx.xhtml?xzdm00=&pageNo=1");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
		HtmlPage htmlPage = webClient.getPage(webRequest);
		webClient.waitForBackgroundJavaScript(10000); // 该方法在getPage()方法之后调用才能生效
		int status = htmlPage.getWebResponse().getStatusCode();
		if (status == 200) {
			String html = htmlPage.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			Elements trs = doc.select("table.tab5 > tbody > tr");
			listSize = trs.size();
		}
		return listSize;
	}
}
