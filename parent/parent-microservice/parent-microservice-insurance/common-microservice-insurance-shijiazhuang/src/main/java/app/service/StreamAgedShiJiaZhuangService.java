package app.service;

import java.net.URL;
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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuang;
import com.microservice.dao.entity.crawler.insurance.shijiazhuang.StreamAgedShiJiaZhuang;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.HtmlStoreShiJiaZhuangRepository;
import com.microservice.dao.repository.crawler.insurance.shijiazhuang.StreamAgedShiJiaZhuangRepository;

import app.commontracerlog.TracerLog;
import app.parser.StreamDetailShiJiaZhuangParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shijiazhuang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shijiazhuang"})
public class StreamAgedShiJiaZhuangService {
	
	public static final Logger log = LoggerFactory.getLogger(StreamAgedShiJiaZhuangService.class);
	@Autowired
	private StreamDetailShiJiaZhuangParser streamDetailShiJiaZhuangParser;
	@Autowired
	private StreamAgedShiJiaZhuangRepository streamAgedShiJiaZhuangRepository;
	@Autowired
	private  HtmlStoreShiJiaZhuangRepository htmlStoreShiJiaZhuangRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	/**
	 * @Des 获取养老保险的详细缴费信息
	 * @param insuranceRequestParameters
	 * @param taskInsurance2 
	 * @return 
	 * @throws Exception 
	 */
	@Async
	public Future<String> getStreamAgedDetail(int year,InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) throws Exception {
		tracer.addTag("action.crawler.getPension",insuranceRequestParameters.getTaskId());
		String url= "http://grsbcx.sjz12333.gov.cn/Report-ResultAction.do?reportId=da89388c-5c59-452f-b2bd-8f54effeda33&newReport=true&encode=false&linage=-1&AS_AAE001="+year+"&AS_AAE135="+insuranceRequestParameters.getUsername().trim()+"";
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);		
		requestSettings.setAdditionalHeader("Host", "grsbcx.sjz12333.gov.cn");
	    requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	    requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
	    requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
	    requestSettings.setAdditionalHeader("Connection", "keep-alive");
	    requestSettings.setAdditionalHeader("Referer", "http://grsbcx.sjz12333.gov.cn/si/pages/default.jsp?page=dzd&cs=html"); 
	    requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36"); 
	    requestSettings.setAdditionalHeader("ajaxRequest", "true"); 
	    requestSettings.setAdditionalHeader("Content-Type", "multipart/form-data"); 
	    requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
	    HtmlPage page = webClient.getPage(requestSettings);
		if(null!=page){
	    	String html = page.getWebResponse().getContentAsString();
	    	HtmlStoreShiJiaZhuang htmlStoreShiJiaZhuang = new HtmlStoreShiJiaZhuang();
			htmlStoreShiJiaZhuang.setPageCount(1);
			htmlStoreShiJiaZhuang.setType("养老保险信息源码页");
			htmlStoreShiJiaZhuang.setTaskid(insuranceRequestParameters.getTaskId());
			htmlStoreShiJiaZhuang.setUrl(url);
			htmlStoreShiJiaZhuang.setHtml(html);
			htmlStoreShiJiaZhuangRepository.save(htmlStoreShiJiaZhuang);			
	    	tracer.addTag("action.crawler.getStreamAgedDetail.html","养老保险缴费明细源码页已入库");
	    	StreamAgedShiJiaZhuang streamAgedShiJiaZhuang = streamDetailShiJiaZhuangParser.htmlStreamAgedParser(html,taskInsurance,year);	
	    	if(null != streamAgedShiJiaZhuang){
				streamAgedShiJiaZhuangRepository.save(streamAgedShiJiaZhuang);
				tracer.addTag("action.crawler.getStreamAgedDetail","养老保险详细缴费信息已入库!");
				insuranceService.changeCrawlerStatus("数据采集中，"+year+"年【个人社保-养老保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
						 200, taskInsurance);
			}
	    }
		return new AsyncResult<String>("200");
	}
}
