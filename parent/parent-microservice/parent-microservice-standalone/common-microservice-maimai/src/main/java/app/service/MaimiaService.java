package app.service;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.maimai.json.MaimaiJsonBean;
import com.crawler.maimai.json.MaimaiStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.maimai.TaskMaimai;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.maimai.TaskMaimaiRepository;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;

@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.maimai" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.maimai" })
public class MaimiaService implements ICrawlerLogin{
	@Autowired
	private TaskMaimaiRepository taskMaimaiRepository;
	@Autowired
	private MaimiaHtmlService maimiaHtmlService;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
    private TaskStandaloneRepository taskStandaloneRepository;
	
	private Gson gson = new Gson();
	@Async
	@Override
	public String login(PbccrcJsonBean pbccrcJsonBean) {
		// TODO Auto-generated method stub
		TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		TaskMaimai taskMaimai = taskMaimaiRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		if (taskMaimai == null) {
			taskMaimai = new TaskMaimai();
		}
		taskMaimai.setLoginInfo(gson.toJson(pbccrcJsonBean));
		taskMaimai.setTaskid(pbccrcJsonBean.getMapping_id());
		tracerLog.qryKeyValue("Maimai login", pbccrcJsonBean.getMapping_id());
		String url = "https://acc.maimai.cn/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Page searchPage = null;
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			webRequest.setAdditionalHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("accept-encoding", "gzip, deflate, br");
			webRequest.setAdditionalHeader("accept-language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("cache-control", "max-age=0");
			webRequest.setAdditionalHeader("content-type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("origin", "https://acc.maimai.cn");
			webRequest.setAdditionalHeader("referer", "https://acc.maimai.cn/login");
			webRequest.setAdditionalHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			
			String requestBody = "m="+pbccrcJsonBean.getUsername()+"&p="+pbccrcJsonBean.getPassword()+"&to=";
			webRequest.setRequestBody(requestBody);
			searchPage = webClient.getPage(webRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskStandalone.setPhase(MaimaiStatusCode.MAIMAI_LOGIN_EXCEPTION.getPhase());
			taskStandalone.setPhase_status(MaimaiStatusCode.MAIMAI_LOGIN_EXCEPTION.getPhasestatus());
			taskStandalone.setDescription(MaimaiStatusCode.MAIMAI_LOGIN_EXCEPTION.getDescription());
			taskStandaloneRepository.save(taskStandalone);
		}
		tracerLog.addTag("登陆", searchPage.toString());
		if(searchPage!=null){
			String html = searchPage.getWebResponse().getContentAsString();
			tracerLog.addTag("登陆HTML", html);
			System.out.println(html);
			if(html.contains("JSON.parse")){
				System.out.println("登陆成功");
				tracerLog.addTag("登陆状态", "登陆成功");
				String cookies = CommonUnit.transcookieToJson(webClient);
				System.out.println("cookies"+cookies);
				taskStandalone.setPhase(MaimaiStatusCode.MAIMAI_LOGIN_SUCCESS.getPhase());
				taskStandalone.setPhase_status(MaimaiStatusCode.MAIMAI_LOGIN_SUCCESS.getPhasestatus());
				taskStandalone.setDescription(MaimaiStatusCode.MAIMAI_LOGIN_SUCCESS.getDescription());
//				taskMaimai.setError_code(MaimaiStatusCode.MAIMAI_LOGIN_SUCCESS.getError_code());
				
				taskStandaloneRepository.save(taskStandalone);
				
				taskMaimai.setCookies(cookies);
				taskMaimaiRepository.save(taskMaimai);
			}else{
				tracerLog.addTag("登陆状态", "帐号或密码不正确。忘记密码可登陆脉脉APP找回。");
				System.out.println("帐号或密码不正确。忘记密码可登陆脉脉APP找回。");
				taskStandalone.setPhase(MaimaiStatusCode.MAIMAI_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskStandalone.setPhase_status(MaimaiStatusCode.MAIMAI_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskStandalone.setDescription(MaimaiStatusCode.MAIMAI_LOGIN_IDNUMORPWD_ERROR.getDescription());
//				taskMaimai.setError_code(MaimaiStatusCode.MAIMAI_LOGIN_IDNUMORPWD_ERROR.getError_code());
				taskStandaloneRepository.save(taskStandalone);
			}
		}
		return null;
	}
	

	@Async
	@Override
	public String getAllData(PbccrcJsonBean pbccrcJsonBean) {
		// TODO Auto-generated method stub
		String url = "https://maimai.cn/contact/visit_history?jsononly=1&limit=9";
		TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		TaskMaimai taskMaimai = taskMaimaiRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		tracerLog.qryKeyValue("Maimai crawler", pbccrcJsonBean.getMapping_id());
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			Set<Cookie> cookies1 = CommonUnit.transferJsonToSet(taskMaimai.getCookies());
			for (Cookie cookie : cookies1) {
				webClient.getCookieManager().addCookie(cookie);
			}
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page searchPage = webClient.getPage(webRequest);
			String html = searchPage.getWebResponse().getContentAsString();
			tracerLog.addTag("获取tokn", html);
			Future<String> future = maimiaHtmlService.getResult(pbccrcJsonBean, taskMaimai ,taskStandalone, webClient,html);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskStandalone.setPhase(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getPhase());
			taskStandalone.setPhase_status(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getPhasestatus());
			taskStandalone.setDescription(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getDescription());
//			taskMaimai.setError_code(MaimaiStatusCode.MAIMAI_AGENT_ERROR.getError_code());
			taskStandaloneRepository.save(taskStandalone);
		}
		return null;
	}


	
}
