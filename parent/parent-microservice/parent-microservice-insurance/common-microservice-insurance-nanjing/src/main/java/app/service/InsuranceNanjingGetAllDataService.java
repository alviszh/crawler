package app.service;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingAllChargeInfo;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingHtml;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanjingAllChargeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanjingHtmlRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceNanjingParser;
import net.sf.json.JSONObject;

/**
 * @description:  社保数据爬取
 * @author: sln 
 * @date: 2017年9月26日 下午6:24:47 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.nanjing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.nanjing"})
public class InsuranceNanjingGetAllDataService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceNanjingParser insuranceNanjingParser;
	@Autowired
	private InsuranceNanjingHtmlRepository insuranceNanjingHtmlRepository;
	@Autowired
	private InsuranceNanjingAllChargeInfoRepository insuranceNanjingAllChargeInfoRepository;
	
	
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public void getPension(TaskInsurance taskInsurance,WebClient webClient){
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYljfByAjax";
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				//先获取的是默认页面，即第一页
				String html = page.getWebResponse().getContentAsString();
				InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
				insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNanjingHtml.setType("养老保险信息源码页1");
				insuranceNanjingHtml.setPageCount(1);
				insuranceNanjingHtml.setUrl(url);
				insuranceNanjingHtml.setHtml(html);
				insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getPension", "养老保险信息无数据可供采集");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){  //总页数
					List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"养老保险");
					if(null != list && list.size()>0){
						insuranceNanjingAllChargeInfoRepository.saveAll(list);
					}
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					//默认页已经爬取完毕，从第二页开始爬取
					for(int i=2;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYljfByAjax";
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("养老保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"养老保险");
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
							}
						}
					}
					tracer.addTag("action.crawler.getPension", "【个人社保-养老保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}else{  //有时候响应的是空字符串
					tracer.addTag("养老保险信息爬取过程中页面响应为空", "接下来进行请求重试");
					throw new RuntimeException("养老保险信息爬取过程中出现异常，接下来进行重试");
				}
			}
		} catch (Exception e){
			tracer.addTag("养老保险信息爬取过程中出现异常", "接下来进行重试"+e.toString());
			throw new RuntimeException("养老保险信息爬取过程中出现异常，接下来进行重试");
		}
		
	}
	
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public void getMedical(TaskInsurance taskInsurance,WebClient webClient)   {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYbjfByAjax";
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(45000);  //经常是医疗信息超时
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
				insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNanjingHtml.setType("医疗保险信息源码页1");
				insuranceNanjingHtml.setPageCount(1);
				insuranceNanjingHtml.setUrl(url);
				insuranceNanjingHtml.setHtml(html);
				insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getMedical", "医疗保险信息无数据可供采集");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){
					List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"医疗保险");
					if(null != list && list.size()>0){
						insuranceNanjingAllChargeInfoRepository.saveAll(list);
					}
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=2;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYbjfByAjax";
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("医疗保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"医疗保险");
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
							}
						}
					}
					tracer.addTag("action.crawler.getMedical", "【个人社保-医疗保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}else{  //有时候响应的是空字符串
					tracer.addTag("医疗保险信息爬取过程中页面响应为空", "接下来进行请求重试");
					throw new RuntimeException("医疗保险信息爬取过程中出现异常，接下来进行重试");
				}
			}
		} catch (Exception e) {
			tracer.addTag("医疗保险信息爬取过程中出现异常", "接下来进行重试"+e.toString());
			throw new RuntimeException("医疗保险信息爬取过程中出现异常，接下来进行重试");
		}
	}
	
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public void getUnemployment(TaskInsurance taskInsurance,WebClient webClient)  {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSybxjfByAjax";
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
				insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNanjingHtml.setType("失业保险信息源码页1");
				insuranceNanjingHtml.setPageCount(1);
				insuranceNanjingHtml.setUrl(url);
				insuranceNanjingHtml.setHtml(html);
				insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getUnemployment", "失业保险信息无数据可供采集");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){
					List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"失业保险");
					if(null != list && list.size()>0){
						insuranceNanjingAllChargeInfoRepository.saveAll(list);
					}
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=2;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSybxjfByAjax";
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("失业保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"失业保险");
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
							}
						}
					}
					tracer.addTag("action.crawler.getUnemployment", "【个人社保-失业保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}else{  //有时候响应的是空字符串
					tracer.addTag("失业保险信息爬取过程中页面响应为空", "接下来进行请求重试");
					throw new RuntimeException("失业保险信息爬取过程中出现异常，接下来进行重试");
				}
			}
		} catch (Exception e) {
			tracer.addTag("失业保险信息爬取过程中出现异常", "接下来进行重试"+e.toString());
			throw new RuntimeException("失业保险信息爬取过程中出现异常，接下来进行重试");
		}
		
	}
	
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public void getBear(TaskInsurance taskInsurance,WebClient webClient)  {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSyjfByAjax";
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
				insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNanjingHtml.setType("生育保险信息源码页1");
				insuranceNanjingHtml.setPageCount(1);
				insuranceNanjingHtml.setUrl(url);
				insuranceNanjingHtml.setHtml(html);
				insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getBear", "生育保险信息无数据可供采集");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){
					List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"生育保险");
					if(null != list && list.size()>0){
						insuranceNanjingAllChargeInfoRepository.saveAll(list);
					}
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=2;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSyjfByAjax";
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("生育保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"生育保险");
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
							}
						}
					}
					tracer.addTag("action.crawler.getBear", "【个人社保-生育保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
							 200, taskInsurance);
				}else{  //有时候响应的是空字符串
					tracer.addTag("生育保险信息爬取过程中页面响应为空", "接下来进行请求重试");
					throw new RuntimeException("生育保险信息爬取过程中出现异常，接下来进行重试");
				}
			}
		} catch (Exception e) {
			tracer.addTag("生育保险信息爬取过程中出现异常", "接下来进行重试"+e.toString());
			throw new RuntimeException("生育保险信息爬取过程中出现异常，接下来进行重试");
		}
	}
	
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public void getInjury(TaskInsurance taskInsurance,WebClient webClient)  {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getGsjfByAjax";
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
				insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNanjingHtml.setType("工伤保险信息源码页1");
				insuranceNanjingHtml.setPageCount(1);
				insuranceNanjingHtml.setUrl(url);
				insuranceNanjingHtml.setHtml(html);
				insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getInjury", "工伤保险信息无数据可供采集");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
							201, taskInsurance);
				}else if(html.contains("total")){
					List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"工伤保险");
					if(null != list && list.size()>0){
						insuranceNanjingAllChargeInfoRepository.saveAll(list);
					}
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=1;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getGsjfByAjax";
//						webClient = taskInsurance.getClient(cookies);
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("工伤保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							list = insuranceNanjingParser.insuranceParser(taskInsurance,html,"工伤保险");
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
							}
						}
					}
					tracer.addTag("action.crawler.getInjury", "【个人社保-工伤保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
							200, taskInsurance);
				}else{  //有时候响应的是空字符串
					tracer.addTag("工伤保险信息爬取过程中页面响应为空", "接下来进行请求重试");
					throw new RuntimeException("工伤保险信息爬取过程中出现异常，接下来进行重试");
				}
			}
		} catch (Exception e) {
			tracer.addTag("工伤保险信息爬取过程中出现异常", "接下来进行重试"+e.toString());
			throw new RuntimeException("工伤保险信息爬取过程中出现异常，接下来进行重试");
		}
	}
	//请求头必须添加，否则无法获取正常响应的页面
	public WebRequest getWebRequest(String url) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "*/*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest.setAdditionalHeader("Host", "m.mynj.cn:11096");
		webRequest.setAdditionalHeader("Origin", "https://m.mynj.cn:11096");
		webRequest.setAdditionalHeader("Referer", "https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=showDetail");
		webRequest.setAdditionalHeader("Request-Type", "Ajax");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		return webRequest;
	}
}
