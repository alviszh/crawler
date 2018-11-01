package app.service;

import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.BasicUserShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuang;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.BasicUserShiJiaZhuangRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuangRepository;

import app.commontracerlog.TracerLog;
import app.parser.CommonRequestBody;
import app.parser.StreamDetailShiJiaZhuangParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shijiazhuang"})
public class BasicUserShiJiaZhuangService {
	@Autowired
	private BasicUserShiJiaZhuangRepository basicUserShiJiaZhuangRepository;
	@Autowired
	private HtmlStoreShiJiaZhuangRepository htmlStoreShiJiaZhuangRepository;
	@Autowired
	private StreamDetailShiJiaZhuangParser streamDetailShiJiaZhuangParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CommonRequestBody commonRequestBody;
	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @param taskInsurance 
	 * @return 
	 * @throws Exception 
	 */
	@Async
	public Future<String> getUserInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		String url = "http://grsbcx.sjz12333.gov.cn/ria_grid.do?method=query";
		String requestpayload="{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{contentStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"contentStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.content\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"\", \"12\"]}},xzStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"xzStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.xzxx\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"\", \"12\"]}},sbkxxStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"sbkxxStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.sbkxx\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"\", \"12\"]}},grqyjlStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"grqyjlStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.grqyjlyj\",attributes:{\"AAE135\": [\"InsurNumLocal\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"\", \"BUSINESS_REQUEST_ID\": \"\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
		HtmlPage page = commonRequestBody.getUserOrGeneralPage(url, requestpayload, insuranceRequestParameters.getUsername(), cookies);
		if(null!=page){
			String html = page.getWebResponse().getContentAsString();
			HtmlStoreShiJiaZhuang htmlStoreShiJiaZhuang = new HtmlStoreShiJiaZhuang();
			htmlStoreShiJiaZhuang.setPageCount(1);
			htmlStoreShiJiaZhuang.setType("个人信息源码页");
			htmlStoreShiJiaZhuang.setTaskid(insuranceRequestParameters.getTaskId());
			htmlStoreShiJiaZhuang.setUrl(url);
			htmlStoreShiJiaZhuang.setHtml(html);
			htmlStoreShiJiaZhuangRepository.save(htmlStoreShiJiaZhuang);
			tracer.addTag("action.crawler.getUserInfo.html","个人信息源码页已入库!");
	    	BasicUserShiJiaZhuang basicUserShiJiaZhuang = streamDetailShiJiaZhuangParser.htmlUserInfoParser(html,taskInsurance);	
	    	if(null!=basicUserShiJiaZhuang){
				basicUserShiJiaZhuangRepository.save(basicUserShiJiaZhuang);
				tracer.addTag("action.crawler.getUserInfo","个人信息已入库!");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
						 200, taskInsurance);
			}
	    }
    	//没有生育险和工伤险，为了最后更改状态成功，此处设置为定值
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
				 201, taskInsurance);
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
				 201, taskInsurance);
		return new AsyncResult<String>("200");	
	}
}
