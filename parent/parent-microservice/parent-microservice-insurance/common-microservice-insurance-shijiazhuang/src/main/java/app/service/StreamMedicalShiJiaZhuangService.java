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
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamMedicalShiJiaZhuang;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuangRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.StreamMedicalShiJiaZhuangRepository;

import app.commontracerlog.TracerLog;
import app.parser.CommonRequestBody;
import app.parser.StreamDetailShiJiaZhuangParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shijiazhuang"})
public class StreamMedicalShiJiaZhuangService {
	public static final Logger log = LoggerFactory.getLogger(StreamMedicalShiJiaZhuangService.class);

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private StreamDetailShiJiaZhuangParser streamDetailShiJiaZhuangParser;
	@Autowired
	private StreamMedicalShiJiaZhuangRepository streamMedicalShiJiaZhuangRepository;
	@Autowired
	private  HtmlStoreShiJiaZhuangRepository htmlStoreShiJiaZhuangRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CommonRequestBody commonRequestBody;
	
	/**
	 * @Des 获取医疗保险的详细缴费信息
	 * @param insuranceRequestParameters
	 * @param taskInsurance2 
	 * @return 
	 * @throws Exception 
	 */
	@Async
	public Future<String> getStreamMedicalDetail(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("action.crawler.getMedical",insuranceRequestParameters.getTaskId());
		taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		String url = "http://grsbcx.sjz12333.gov.cn/ria_grid.do?method=query";
		String requestpayload="{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{searchStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"searchStore\",pageNumber:1,pageSize:20,recordCount:0,context:{\"BUSINESS_ID\": \"UOA017\", \"BUSINESS_REQUEST_ID\": \"REQ-OA-M-013-01\", \"CUSTOMVPDPARA\": \"\"},statementName:\"si.treatment.ggfw.yljf\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"userNameCut\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"UOA017\", \"BUSINESS_REQUEST_ID\": \"REQ-OA-M-013-01\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
		HtmlPage page = commonRequestBody.getMedicalOrLostWorkPage(url, requestpayload, insuranceRequestParameters.getUsername().trim(), cookies);
//		获取医疗保险缴费详情页面     第一页
		if(null!=page){
	    	String html = page.getWebResponse().getContentAsString();
	    	HtmlStoreShiJiaZhuang htmlStoreShiJiaZhuang = new HtmlStoreShiJiaZhuang();
			htmlStoreShiJiaZhuang.setPageCount(1);
			htmlStoreShiJiaZhuang.setType("医疗保险信息第一页源码页");
			htmlStoreShiJiaZhuang.setTaskid(insuranceRequestParameters.getTaskId());
			htmlStoreShiJiaZhuang.setUrl(url);
			htmlStoreShiJiaZhuang.setHtml(html);
			htmlStoreShiJiaZhuangRepository.save(htmlStoreShiJiaZhuang);
			tracer.addTag("action.crawler.getStreamMedicalDetail.html.1","第一页医疗险信息源码页已入库!");	
	    	List<StreamMedicalShiJiaZhuang> streamMedicalListOne =streamDetailShiJiaZhuangParser.htmlMedicalParser(html,taskInsurance);	
	    	if(null != streamMedicalListOne){
				streamMedicalShiJiaZhuangRepository.saveAll(streamMedicalListOne);
				tracer.addTag("action.crawler.getStreamMedicalDetail","第一页医疗保险信息已入库!");	
			}
	    }
//		获取医疗保险缴费详情页面     第二页
		url = "http://grsbcx.sjz12333.gov.cn/ria_grid.do?method=query";
		requestpayload="{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{searchStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"searchStore\",pageNumber:2,pageSize:20,recordCount:36,context:{\"BUSINESS_ID\": \"UOA017\", \"BUSINESS_REQUEST_ID\": \"REQ-OA-M-013-01\", \"CUSTOMVPDPARA\": \"\"},metaData:{\"AC43_AAB001\": {\"label\": \"AC43_AAB001\", \"dataType\": 12, \"precision\": 40}, \"AC43_AAC001\": {\"label\": \"AC43_AAC001\", \"dataType\": 12, \"precision\": 40},\"AC43_AAE003\": {\"label\": \"AC43_AAE003\", \"dataType\": 12, \"precision\": 12}, \"AC43_AAE140\": {\"label\": \"AC43_AAE140\", \"dataType\": 12, \"precision\": 64}, \"AC43_AAE002\": {\"label\": \"AC43_AAE002\", \"dataType\": 12, \"precision\": 12}, \"AC43_AAC040\": {\"label\": \"AC43_AAC040\", \"dataType\": 2, \"precision\": 16}, \"AC43_AAE018\": {\"label\": \"AC43_AAE018\", \"dataType\": 2, \"precision\": 16}, \"AC43_AAE020\": {\"label\": \"AC43_AAE020\", \"dataType\": 2, \"precision\": 16}, \"AC43_AAE021\": {\"label\": \"AC43_AAE021\",\"dataType\": 2, \"precision\": 16}, \"AC43_AAE022\": {\"label\": \"AC43_AAE022\", \"dataType\": 2, \"precision\": 0}, \"ITE\": {\"label\": \"ITE\", \"dataType\": 1, \"precision\": 1},\"AAE136\": {\"label\": \"AAE136\", \"dataType\": 12, \"precision\": 10}},statementName:\"si.treatment.ggfw.yljf\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"userNameCut\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"UOA017\", \"BUSINESS_REQUEST_ID\": \"REQ-OA-M-013-01\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
		page = commonRequestBody.getMedicalOrLostWorkPage(url, requestpayload, insuranceRequestParameters.getUsername().trim(), cookies);
		if(null!=page){
	    	String html = page.getWebResponse().getContentAsString();
	    	HtmlStoreShiJiaZhuang htmlStoreShiJiaZhuang = new HtmlStoreShiJiaZhuang();
			htmlStoreShiJiaZhuang.setPageCount(1);
			htmlStoreShiJiaZhuang.setType("医疗保险信息第二页源码页");
			htmlStoreShiJiaZhuang.setTaskid(taskInsurance.getTaskid());
			htmlStoreShiJiaZhuang.setUrl(url);
			htmlStoreShiJiaZhuang.setHtml(html);
			htmlStoreShiJiaZhuangRepository.save(htmlStoreShiJiaZhuang);
			tracer.addTag("action.crawler.getStreamMedicalDetail.html.2","第二页医疗险信息源码页已入库!");	
	    	List<StreamMedicalShiJiaZhuang> streamMedicalListTwo =streamDetailShiJiaZhuangParser.htmlMedicalParser(html,taskInsurance);	
	    	if(null != streamMedicalListTwo){
				streamMedicalShiJiaZhuangRepository.saveAll(streamMedicalListTwo);
				tracer.addTag("getStreamMedicalDetail ==>","第一页医疗保险信息已入库!");	
				tracer.addTag("action.crawler.getStreamMedicalDetail","第二页医疗险信息源码页已入库!");	
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
						InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
						 200, taskInsurance);
			}
		}
		return new AsyncResult<String>("200");
	}
}
