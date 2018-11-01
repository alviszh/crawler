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
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamGeneralShiJiaZhuang;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.StreamGeneralShiJiaZhuangRepository;

import app.commontracerlog.TracerLog;
import app.parser.CommonRequestBody;
import app.parser.StreamGeneralShiJiaZhuangParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shijiazhuang"})
public class StreamGeneralShiJiaZhuangService {
public static final Logger log = LoggerFactory.getLogger(StreamGeneralShiJiaZhuangService.class);
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private StreamGeneralShiJiaZhuangParser streamGeneralShiJiaZhuangParser;
	@Autowired
	private StreamGeneralShiJiaZhuangRepository streamGeneralShiJiaZhuangRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CommonRequestBody commonRequestBody;
	
	/**
	 * @Des 获取保险公共信息（由于保险公共信息是和个人信息在同样的返回json串中只是分表存储，
	 * 				故保存个人信息的时候已经保存了html数据，此处不再重复保存）
	 * @param insuranceRequestParameters
	 * @param taskInsurance2 
	 * @return 
	 * @throws Exception 
	 */
	@Async
	public Future<String> getStreamGeneral(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance2) throws Exception {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		String url = "http://grsbcx.sjz12333.gov.cn/ria_grid.do?method=query";
		String requestpayload="{header:{\"code\": -100, \"message\": {\"title\": \"\", \"detail\": \"\"}},body:{dataStores:{contentStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"contentStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.content\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"\", \"12\"]}},xzStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"xzStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.xzxx\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"\", \"12\"]}},sbkxxStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"sbkxxStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.sbkxx\",attributes:{\"AAC002\": [\"InsurNumLocal\", \"12\"], \"AAE135\": [\"\", \"12\"]}},grqyjlStore:{rowSet:{\"primary\":[],\"filter\":[],\"delete\":[]},name:\"grqyjlStore\",pageNumber:1,pageSize:2147483647,recordCount:0,statementName:\"si.treatment.ggfw.grqyjlyj\",attributes:{\"AAE135\": [\"InsurNumLocal\", \"12\"]}}},parameters:{\"BUSINESS_ID\": \"\", \"BUSINESS_REQUEST_ID\": \"\", \"CUSTOMVPDPARA\": \"\", \"PAGE_ID\": \"\"}}}";
		HtmlPage page = commonRequestBody.getUserOrGeneralPage(url, requestpayload, insuranceRequestParameters.getUsername(), cookies);
		if(null!=page){
	    	String html = page.getWebResponse().getContentAsString();
	    	tracer.addTag("action.crawler.getStreamGeneral.html", "公共社保信息json串已经入库");
	    	List<StreamGeneralShiJiaZhuang> list = streamGeneralShiJiaZhuangParser.htmlGeneralParser(html,taskInsurance);			
	    	if(null!=list && list.size()>0){
				streamGeneralShiJiaZhuangRepository.saveAll(list);
				tracer.addTag("action.crawler.getStreamGeneral","石家庄社保流水公共信息已经入库!");
			}
	    }
		return new AsyncResult<String>("200");
	}
}
