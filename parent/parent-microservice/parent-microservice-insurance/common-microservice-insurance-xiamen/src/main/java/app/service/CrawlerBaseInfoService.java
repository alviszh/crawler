package app.service;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceXiamenCrawlerResult;
import app.enums.InsuranceXiamenCrawlerType;
import app.htmlparser.XiamenCrawler;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenHtml;
import com.microservice.dao.repository.crawler.insurance.xiamen.InsuranceXiamenHtmlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 爬取基本信息Service
 * 
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.xiamen" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.xiamen" })
public class CrawlerBaseInfoService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private XiamenInsuranceService xiamenInsuranceService;
	@Autowired
	private XiamenCrawler xiamenCrawler;
	@Autowired
	private CrawlerFiveInsuranceService crawlerFiveInsuranceService;

	@Autowired
	private InsuranceXiamenHtmlRepository insuranceXiamenHtmlRepository;
	@Autowired
	private ParserCrawledBaseInfoService parserCrawledBaseInfoService;

	/**
	 * 爬取基本信息
	 * 
	 * @param parameter
	 * @return
	 */
	public WebParam<HtmlPage> crawlerBaseInfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			HtmlPage inHomePage, Set<Cookie> cookies, Map<String, WebParam<HtmlPage>> resultMap) {
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:开始爬取厦门用户基本信息", parameter.toString());
		// 更新Task表为 基本信息数据采集中
		// 开始爬取(个人资料)
		WebParam<HtmlPage> personalInfo = null;
		// 爬取参保情况
		WebParam<HtmlPage> canbaoInfo = null;
		try {
			personalInfo = xiamenCrawler.crawlerBaseInfo(inHomePage);
			canbaoInfo = xiamenCrawler.crawlerBaseCanbaoInfo(inHomePage);
			tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:厦门用户个人资料爬取完成", personalInfo.toString());
			tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:厦门用户参保情况爬取完成", canbaoInfo.toString());
			// Html入库
			if (InsuranceXiamenCrawlerResult.SUCCESS.getCode().equals(personalInfo.getCode())
					&& personalInfo.getData() != null
					&& InsuranceXiamenCrawlerResult.SUCCESS.getCode().equals(canbaoInfo.getCode())
					&& canbaoInfo.getData() != null) {
				HtmlPage htmlPage = personalInfo.getData();
				HtmlPage canbaoPage = canbaoInfo.getData();
				tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:厦门用户基本信息(个人基本信息)HTML入库中", htmlPage.asText());
				tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:厦门用户基本信息(参保信息)HTML入库中", canbaoPage.asText());
				InsuranceXiamenHtml insuranceShenzhenHtml = new InsuranceXiamenHtml();
				insuranceShenzhenHtml.setHtml(htmlPage.asText());
				insuranceShenzhenHtml.setTaskId(taskInsurance.getTaskid());
				insuranceShenzhenHtml.setType(InsuranceXiamenCrawlerType.BASE_INFO.getCode());
				insuranceShenzhenHtml.setUrl(XiamenCrawler.BASE_INFO);
				insuranceXiamenHtmlRepository.save(insuranceShenzhenHtml);
				insuranceShenzhenHtml.setHtml(canbaoPage.asText());
				insuranceXiamenHtmlRepository.save(insuranceShenzhenHtml);
				// 更新Task表为 基本信息数据采集成功
				xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
						InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS, 0);
			} else {
				// 更新Task表为 基本信息数据采集失败
				xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
						InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE, 0);
			}
			// 保存爬取结果,
			resultMap.put("baseInfo", personalInfo);
			resultMap.put("canbaoInfo", canbaoInfo);
			// 解析个人基本信息
			parserCrawledBaseInfoService.parserCrawledBaseInfo(resultMap, taskInsurance);
		} catch (IOException e) {
			tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo 获取厦门个人信息失败! ", "" + e);
			xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE, 0);
		}
		tracer.addTag("CrawlerBaseInfoService.crawlerBaseInfo:爬取完成,调用爬取五险", personalInfo.toString());
		// 调用爬取 五险
		// 更新Task表为 保险数据采集中
//		xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
//				InsuranceStatusCode.INSURANCE_CRAWLER_AGED_DOING, 0);
//		xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
//				InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_DOING, 0);
//		xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
//				InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_DOING, 0);
//		xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
//				InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_DOING, 0);
//		xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
//				InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_DOING, 0);
		crawlerFiveInsuranceService.crawlerFiveInsurance(parameter, taskInsurance, inHomePage, resultMap);
		return personalInfo;
	}

}
