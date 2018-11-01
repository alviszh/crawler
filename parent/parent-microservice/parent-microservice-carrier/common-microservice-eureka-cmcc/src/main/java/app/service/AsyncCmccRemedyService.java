package app.service;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan; 
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.crawler.cmcc.domain.json.CallRecordBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.cmcc.CmccRemedyParameter;
import com.microservice.dao.entity.crawler.cmcc.CmccSMSMsgResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserCallResult;
import com.microservice.dao.entity.crawler.cmcc.CmccUserInfo;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.cmcc.CmccRemedyParameterRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccSMSMsgResultRepository;
import com.microservice.dao.repository.crawler.cmcc.CmccUserCallResultRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.unit.CmccUnit;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.cmcc","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(
		basePackages={"com.microservice.dao.repository.crawler.cmcc","com.microservice.dao.repository.crawler.mobile"})
public class AsyncCmccRemedyService {

	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CmccUserCallResultRepository cmccUserCallResultRepository;
	@Autowired
	private CmccSMSMsgResultRepository cmccSMSMsgResultRepository;
	@Autowired
	private CmccRemedyParameterRepository cmccRemedyParameterRepository;
	
	/**
	 * 补救方法
	 * @param taskid
	 */
	@Async
	public void getRemedy(String taskid) {
		tracer.addTag("getRemedy", "补救开始");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskid);
		Set<Cookie> cookies = CmccUnit.transferJsonToSet(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		for(Cookie cookie : cookies){
			webClient.getCookieManager().addCookie(cookie);
		}
		List<CmccRemedyParameter> list = cmccRemedyParameterRepository.findByTaskId(taskid);
		tracer.addTag("补救条数", list.size()+"");
		if(null != list && list.size()>0){
			for(CmccRemedyParameter parameter : list){
				//通话记录
				if(parameter.getType().equals("02")){
					String url = parameter.getUrl();
					tracer.addTag(url, "通话记录补救的url");
					try {
						String html = getData(webClient,url);
						tracer.addTag(html,"通话记录源码");
						String json = html.substring(html.indexOf("(")+1, html.length()-1);
						Type userListType = new TypeToken<CallRecordBean<List<CmccUserCallResult>>>(){}.getType();					
						CallRecordBean<List<CmccUserCallResult>> callRecordBean = new Gson().fromJson(json,userListType);
						if("000000".equals(callRecordBean.getRetCode())){
							List<CmccUserCallResult> results = callRecordBean.data;
							List<CmccUserCallResult> cmccUserCallResults = new ArrayList<CmccUserCallResult>();	
							for(CmccUserCallResult cmccUserCallResult:results){
								cmccUserCallResult.setTaskId(taskid);
								cmccUserCallResult.setMobileNum(taskMobile.getPhonenum());
								cmccUserCallResult.setStartYear(parameter.getYear());
								cmccUserCallResults.add(cmccUserCallResult);
							}
							cmccUserCallResultRepository.saveAll(cmccUserCallResults);
							tracer.addTag(url, "通话记录补救成功");
							
							parameter.setCount(cmccUserCallResults.size());
							cmccRemedyParameterRepository.save(parameter);
							
						}
					} catch (Exception e) {
						tracer.addTag("getRemedy.call", "error");
						tracer.addTag("通话记录补救报错", e.getMessage());
					}
				}else{
					String url = parameter.getUrl();
					tracer.addTag(url, "短信记录补救的url");
					try {
						String html = getData(webClient,url);
						tracer.addTag(html,"短信记录源码");
						String json = html.substring(html.indexOf("(")+1, html.length()-1);
						Type userListType = new TypeToken<CallRecordBean<List<CmccSMSMsgResult>>>(){}.getType();						
						CallRecordBean<List<CmccSMSMsgResult>> smsRecordBean = new Gson().fromJson(json,userListType);
						if("000000".equals(smsRecordBean.getRetCode())){
							List<CmccSMSMsgResult> results = smsRecordBean.data;
							List<CmccSMSMsgResult> cmccSMSMsgResults = new ArrayList<CmccSMSMsgResult>();
							for(CmccSMSMsgResult cmccSMSMsgResult:results){
								cmccSMSMsgResult.setTaskId(taskid);
								cmccSMSMsgResult.setMobileNum(taskMobile.getPhonenum());
								cmccSMSMsgResult.setStartYear(parameter.getYear());
								cmccSMSMsgResults.add(cmccSMSMsgResult);
							}
							cmccSMSMsgResultRepository.saveAll(cmccSMSMsgResults);
							tracer.addTag(url, "短信记录补救成功");
							
							parameter.setCount(cmccSMSMsgResults.size());
							cmccRemedyParameterRepository.save(parameter);
							
						}
					} catch (Exception e) {
						tracer.addTag("getRemedy.sms", "error");
						tracer.addTag("短信记录补救报错", e.getMessage());
					}
				}
			}
		}else{
			tracer.addTag("并不需要补救", "done");
		}
		
	}
	
	
	
	/**
	 * 获取数据
	 * @param webClient
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private String getData(WebClient webClient, String url) throws Exception {
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.GET); 
		requestSettings.setAdditionalHeader("Referer", "http://shop.10086.cn/i/?f=home&welcome="+System.currentTimeMillis());
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Host", "shop.10086.cn");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		
		Page page = webClient.getPage(requestSettings);
		String html = page.getWebResponse().getContentAsString();
		return html;
	}
	
	
}
