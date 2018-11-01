package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanJingParams;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingAllChargeInfo;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingHtml;
import com.microservice.dao.entity.crawler.insurance.nanjing.InsuranceNanjingUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanJingParamsRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanjingAllChargeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanjingHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.nanjing.InsuranceNanjingUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceNanjingParser;
import net.sf.json.JSONObject;

/**
 * @description:  社保数据爬取
 * @author: sln 
 * @date: 2017年9月26日 下午6:24:47 
 * 
 * 弃用该类，原因见另一个爬取类的注释
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.nanjing"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.nanjing"})
public class AsyncNanjingGetAllDataService {
	public static final Logger log = LoggerFactory.getLogger(AsyncNanjingGetAllDataService.class);
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
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceNanjingUserInfoRepository insuranceNanjingUserInfoRepository;
	@Autowired
	private InsuranceNanJingParamsRepository nanJingParamsRepository;
	
    //maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
    //backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。

	@Async
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public Future<String> getPension(TaskInsurance taskInsurance){
		try {
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYljfByAjax";
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				//先获取的是默认页面，即第一页
				String html = page.getWebResponse().getContentAsString();
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getPension", "【个人社保-养老保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){  //总页数
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=1;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYljfByAjax";
						webClient = taskInsurance.getClient(taskInsurance.getCookies());
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("养老保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							tracer.addTag("action.crawler.getPension.page"+i, "养老保险信息源码页已入库");
							String webType="养老保险";
							List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,webType);
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
								tracer.addTag("action.crawler.getPension"+i, "养老保险信息已入库");
							}else{
								tracer.addTag("action.crawler.getPension"+i, "【个人社保-养老保险信息】已采集完成！");
							}
						}
					}
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
		return new AsyncResult<String>("200");
	}
	@Async
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public Future<String> getMedical(TaskInsurance taskInsurance)   {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYbjfByAjax";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(45000);  //经常是医疗信息超时
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getMedical", "【个人社保-医疗保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=1;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getYbjfByAjax";
						webClient = taskInsurance.getClient(taskInsurance.getCookies());
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("医疗保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							tracer.addTag("action.crawler.getMedical.page"+i, "医疗保险信息源码页已入库");
							String webType="医疗保险";
							List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,webType);
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
								tracer.addTag("action.crawler.getMedical"+i, "医疗保险信息已入库");
							}else{
								tracer.addTag("action.crawler.getMedical"+i, "【个人社保-医疗保险信息】已采集完成！");
							}
						}
					}
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
		
		return new AsyncResult<String>("200");
	}
	@Async
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public Future<String> getUnemployment(TaskInsurance taskInsurance)  {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSybxjfByAjax";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getUnemployment", "【个人社保-失业保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=1;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSybxjfByAjax";
						webClient = taskInsurance.getClient(taskInsurance.getCookies());
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("失业保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							tracer.addTag("action.crawler.getUnemployment.page"+i, "失业保险信息源码页已入库");
							String webType="失业保险";
							List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,webType);
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
								tracer.addTag("action.crawler.getUnemployment"+i, "失业保险信息已入库");
							}else{
								tracer.addTag("action.crawler.getUnemployment"+i, "【个人社保-失业保险信息】已采集完成！");
							}
						}
					}
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
		return new AsyncResult<String>("200");
	}
	@Async
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public Future<String> getBear(TaskInsurance taskInsurance)  {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSyjfByAjax";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getBear", "【个人社保-生育保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
							 201, taskInsurance);
				}else if(html.contains("total")){
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=1;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getSyjfByAjax";
						webClient = taskInsurance.getClient(taskInsurance.getCookies());
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("生育保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							tracer.addTag("action.crawler.getBear.page"+i, "生育保险信息源码页已入库");
							String webType="生育保险";
							List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,webType);
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
								tracer.addTag("action.crawler.getBear"+i, "生育保险信息已入库");
							}else{
								tracer.addTag("action.crawler.getBear"+i, "【个人社保-生育保险信息】已采集完成！");
							}
						}
					}
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
		return new AsyncResult<String>("200");
	}
	@Async
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public Future<String> getInjury(TaskInsurance taskInsurance)  {
		try {
			String url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getGsjfByAjax";
			WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
			WebRequest webRequest = getWebRequest(url);
			webClient.getOptions().setTimeout(30000);
			String requestBody="page=1&bbb=123B&btime=&etime=";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				if(html.contains("null")){ //没有数据，响应{"data":null,"detailMessage":"","mainMessage":"处理成功提示信息","messageCode":"0"}
					tracer.addTag("action.crawler.getInjury", "【个人社保-工伤保险信息】已采集完成！");
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
							201, taskInsurance);
				}else if(html.contains("total")){
					String total = JSONObject.fromObject(html).getJSONArray("data").getJSONObject(0).getString("total");//获取总页数
					int totalPage = Integer.parseInt(total);
					for(int i=1;i<=totalPage;i++){
						url="https://m.mynj.cn:11096/njwsbs/singlelevyandpay.do?method=getGsjfByAjax";
						webClient = taskInsurance.getClient(taskInsurance.getCookies());
						webRequest = getWebRequest(url);
						requestBody="page="+i+"&bbb=123B&btime=&etime=";
						webRequest.setRequestBody(requestBody);
						page = webClient.getPage(webRequest);
						if(null!=page){
							html = page.getWebResponse().getContentAsString();
							InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
							insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
							insuranceNanjingHtml.setType("工伤保险信息源码页"+i);
							insuranceNanjingHtml.setPageCount(i);
							insuranceNanjingHtml.setUrl(url);
							insuranceNanjingHtml.setHtml(html);
							insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
							tracer.addTag("action.crawler.getInjury.page"+i, "工伤保险信息源码页已入库");
							String webType="工伤保险";
							List<InsuranceNanjingAllChargeInfo> list = insuranceNanjingParser.insuranceParser(taskInsurance,html,webType);
							if(null != list && list.size()>0){
								insuranceNanjingAllChargeInfoRepository.saveAll(list);
								tracer.addTag("action.crawler.getInjury"+i, "工伤保险信息已入库");
							}else{
								tracer.addTag("action.crawler.getInjury"+i, "【个人社保-工伤保险信息】已采集完成！");
							}
						}
					}
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
		return new AsyncResult<String>("200");
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
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 5000,multiplier = 1))
	public TaskInsurance getUserInfo(TaskInsurance taskInsurance) {
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		webClient.getOptions().setJavaScriptEnabled(false);
		try {
			InsuranceNanJingParams params = nanJingParamsRepository.findTopByTaskidOrderByCreatetimeDesc(taskInsurance.getTaskid());
			String token = null;
			if(null!=params){
				token = params.getToken();
			}
			String url="https://m.mynj.cn:11096/njwsbs/index.do?method=show&token="+token+"";   //请求连接中拼接着token
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html = page.getWebResponse().getContentAsString();
				//存储用户信息源码页
				InsuranceNanjingHtml insuranceNanjingHtml = new InsuranceNanjingHtml();
				insuranceNanjingHtml.setTaskid(taskInsurance.getTaskid());
				insuranceNanjingHtml.setType("userInfo用户信息源码页");
				insuranceNanjingHtml.setPageCount(1);
				insuranceNanjingHtml.setUrl(url);
				insuranceNanjingHtml.setHtml(html);
				insuranceNanjingHtmlRepository.save(insuranceNanjingHtml);
				tracer.addTag("action.crawler.getUserinfo.html", "个人信息源码页已入库");
				if(html.contains("上次登录时间")){  //包含这个字段，说明正确获取了用户登录信息
					String cookies = CommonUnit.transcookieToJson(webClient);
					taskInsurance.setCookies(cookies);
					//将保存后的taskInsurance进行响应，存储用户信息页面正常响应之后的cookie,用户信息页面正确响应之后获取的cookie用于爬取
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
					//获取用户信息解析返回值
					InsuranceNanjingUserInfo insuranceNanjingUserInfo=insuranceNanjingParser.userInfoParser(taskInsurance,html);
					if(null != insuranceNanjingUserInfo){
						insuranceNanjingUserInfoRepository.save(insuranceNanjingUserInfo);
						tracer.addTag("action.crawler.getUserinfo", "个人信息已入库"+taskInsurance.getTaskid());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
								InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
								 200, taskInsurance);
					}
				}else{
					tracer.addTag("用户基本信息爬取过程中未响应预期页面", "接下来进行请求重试");
					throw new RuntimeException("用户基本信息爬取过程中未响应预期页面，接下来进行重试");
				}
			}
		} catch (Exception e) {
			tracer.addTag("解析用户信息的过程中出现异常：", e.toString());
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					 201, taskInsurance);
		}
		return taskInsurance;
	}
	
	
	
}
