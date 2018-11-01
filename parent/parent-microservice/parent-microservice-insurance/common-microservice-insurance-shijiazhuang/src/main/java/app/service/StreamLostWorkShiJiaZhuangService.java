package app.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamLostWorkShiJiaZhuang;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuangRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.StreamLostWorkShiJiaZhuangRepository;

import app.commontracerlog.TracerLog;
import app.parser.CommonRequestBody;
import app.parser.StreamDetailShiJiaZhuangParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shijiazhuang"})
public class StreamLostWorkShiJiaZhuangService {
	public static final Logger log = LoggerFactory.getLogger(StreamLostWorkShiJiaZhuangService.class);

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private StreamDetailShiJiaZhuangParser streamDetailShiJiaZhuangParser;
	@Autowired
	private StreamLostWorkShiJiaZhuangRepository streamLostWorkShiJiaZhuangRepository;
	@Autowired
	private  HtmlStoreShiJiaZhuangRepository htmlStoreShiJiaZhuangRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CommonRequestBody commonRequestBody;
	
	/**
	 * @Des 获取失业保险的详细缴费信息
	 * @param insuranceRequestParameters
	 * @param taskInsurance2 
	 * @return 
	 * @throws Exception 
	 */
	@Async
	public Future<String> getStreamLostWorkDetail(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance2) throws Exception {
		tracer.addTag("action.crawler.getUnemployment",insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		String url = "http://grsbcx.sjz12333.gov.cn/ria_grid.do?method=query";
		String requestpayload="{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{searchStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"searchStore\",pageNumber:1,pageSize:20,recordCount:0,context:{\"BUSINESS_ID\": \"UOA017\", \"BUSINESS_REQUEST_ID\": \"REQ-OA-M-013-01\", \"CUSTOMVPDPARA\": \"\"},statementName:\"si.treatment.ggfw.syjf\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"userNameCut\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"UOA017\", \"BUSINESS_REQUEST_ID\": \"REQ-OA-M-013-01\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
		HtmlPage page = commonRequestBody.getMedicalOrLostWorkPage(url, requestpayload, insuranceRequestParameters.getUsername().trim(), cookies);
		if(null!=page){
	    	String html = page.getWebResponse().getContentAsString();
	    	HtmlStoreShiJiaZhuang htmlStoreShiJiaZhuang = new HtmlStoreShiJiaZhuang();
			htmlStoreShiJiaZhuang.setPageCount(1);
			htmlStoreShiJiaZhuang.setType("失业保险信息源码页");
			htmlStoreShiJiaZhuang.setTaskid(taskInsurance.getTaskid());
			htmlStoreShiJiaZhuang.setUrl(url);
			htmlStoreShiJiaZhuang.setHtml(html);
			htmlStoreShiJiaZhuangRepository.save(htmlStoreShiJiaZhuang);
			tracer.addTag("action.crawler.getStreamLostWorkDetail.html","失业险详细缴费信息源码页已入库!");			
	    	List<StreamLostWorkShiJiaZhuang> list = streamDetailShiJiaZhuangParser.htmlStreamLostWorkParser(html,taskInsurance);	
	    	if(null!=list && list.size()>0){
	    		streamLostWorkShiJiaZhuangRepository.saveAll(list);
				tracer.addTag("action.crawler.getStreamLostWorkDetail","失业险详细缴费信息已入库");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
						 200, taskInsurance);
	    	}else{
	    		tracer.addTag("action.crawler.getStreamLostWorkDetail","失业险详细缴费信息无数据可供采集");
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
						 201, taskInsurance);
	    	}
	    }
		return new AsyncResult<String>("200");			
	}
}
