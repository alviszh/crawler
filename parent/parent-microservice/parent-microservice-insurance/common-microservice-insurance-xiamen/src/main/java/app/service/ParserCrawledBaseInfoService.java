package app.service;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceXiamenCrawlerResult;
import app.htmlparser.XiamenCrawlerParser;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xiamen.InsuranceXiamenBaseInfo;
import com.microservice.dao.repository.crawler.insurance.xiamen.InsuranceXiamenBaseInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 解析基本信息Service
 * 
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.xiamen" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.xiamen" })
public class ParserCrawledBaseInfoService {

	@Autowired
	private TracerLog tracer;

	@Autowired
	private XiamenInsuranceService xiamenInsuranceService;

	@Autowired
	private XiamenCrawlerParser xiamenCrawlerParser;
	@Autowired
	private InsuranceXiamenBaseInfoRepository insuranceXiamenBaseInfoRepository;

	/**
	 * 解析基本信息
	 * 
	 * @param
	 * @return
	 */
	@Async
	public WebParam<InsuranceXiamenBaseInfo> parserCrawledBaseInfo(Map<String, WebParam<HtmlPage>> resultMap,
			TaskInsurance taskInsurance) {
		HtmlPage baseInfoPage = resultMap.get("baseInfo").getData();
		HtmlPage canbaoInfoPage = resultMap.get("canbaoInfo").getData();
		tracer.addTag("ParserCrawledBaseInfoService.parserCrawledBaseInfo:开始解析基本信息", baseInfoPage.asText());
		// 更新Task表为 基本信息数据解析中

		// 开始解析
		WebParam<InsuranceXiamenBaseInfo> webParam = xiamenCrawlerParser.parserBaseInfo(baseInfoPage, canbaoInfoPage);
		tracer.addTag("ParserCrawledBaseInfoService.parserCrawledBaseInfo:解析完成", webParam.toString());

		// 基本信息 入库
		if (InsuranceXiamenCrawlerResult.SUCCESS.getCode().equals(webParam.getCode()) && webParam.getData() != null) {
			InsuranceXiamenBaseInfo baseInfo = webParam.getData();
			baseInfo.setTaskId(taskInsurance.getTaskid());
			insuranceXiamenBaseInfoRepository.save(baseInfo);

			// 更新Task表为 基本信息数据解析成功
			xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS, 0);
			// TODO: 2017/9/25 待删除
			System.out.println("厦门基本信息解析存储成功！");
		} else {
			// 更新Task表为 基本信息数据解析失败
			xiamenInsuranceService.changeTaskInsuranceStatus4Xiamen(taskInsurance,
					InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_FAILUE, 0);
		}
		return webParam;
	}

}
