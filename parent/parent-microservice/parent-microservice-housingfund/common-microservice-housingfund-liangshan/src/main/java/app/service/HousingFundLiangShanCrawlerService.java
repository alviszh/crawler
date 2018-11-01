package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.liangshan.HousingLiangShanDetailAccount;
import com.microservice.dao.entity.crawler.housing.liangshan.HousingLiangShanHtml;
import com.microservice.dao.entity.crawler.housing.liangshan.HousingLiangShanUserInfo;
import com.microservice.dao.repository.crawler.housing.liangshan.HousingLiangShanDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.liangshan.HousingLiangShanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.liangshan.HousingLiangShanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundLiangShanParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.liangshan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.liangshan"})
public class HousingFundLiangShanCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundLiangShanCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundLiangShanParser housingFundLiangShanParser;
	@Autowired
	private HousingLiangShanHtmlRepository housingLiangShanHtmlRepository;
	@Autowired
	private HousingLiangShanDetailAccountRepository housingLiangShanDetailAccountRepository;
	@Autowired
	private HousingLiangShanUserInfoRepository housingLiangShanUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Value("${loginhost}") 
	public String loginHost;
	@Async
	public Future<String> getUserInfo(TaskHousing taskHousing) {
		try {
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String html1="";
			String html2="";
			//先获取第一部分用户信息
			String url1 = "http://"+loginHost+"/ispobs/Forms/SysFiles/Sys_MainDesk.aspx"; 
	 		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.GET);   
	 		HtmlPage hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			html1= hPage.getWebResponse().getContentAsString(); 
	 			HousingLiangShanHtml housingLiangShanHtml=new HousingLiangShanHtml();
	 			housingLiangShanHtml.setHtml(html1);
	 			housingLiangShanHtml.setPagenumber(1);
	 			housingLiangShanHtml.setTaskid(taskHousing.getTaskid());
	 			housingLiangShanHtml.setType("第一部分用户基本信息源码页)");
	 			housingLiangShanHtml.setUrl(url1);
	 			housingLiangShanHtmlRepository.save(housingLiangShanHtml);
	 		}
	 		String url2="http://"+loginHost+"/ispobs/Forms/CX/CX_GRZHJBXXCX.aspx";
	 		webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
	 		hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			html2= hPage.getWebResponse().getContentAsString(); 
	 			HousingLiangShanHtml housingLiangShanHtml=new HousingLiangShanHtml();
	 			housingLiangShanHtml.setHtml(html2);
	 			housingLiangShanHtml.setPagenumber(1);
	 			housingLiangShanHtml.setTaskid(taskHousing.getTaskid());
	 			housingLiangShanHtml.setType("第二部分用户基本信息源码页)");
	 			housingLiangShanHtml.setUrl(url2);
	 			housingLiangShanHtmlRepository.save(housingLiangShanHtml);
	 		}
			HousingLiangShanUserInfo housingLiangShanUserInfo= housingFundLiangShanParser.userInfoParser(html1,html2,taskHousing);
			if(null!=housingLiangShanUserInfo){
				housingLiangShanUserInfoRepository.save(housingLiangShanUserInfo);
				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
			}else{
				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息暂无可采集数据");
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");
	}
	@Async
	public Future<String> getDetailAccount(TaskHousing taskHousing){
		//该网站不涉及分页，给出查询时间范围，该范围内的所有记录都会出来
		try {
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://"+loginHost+"/ispobs/Forms/CX/CX_GRZHMXCX.aspx";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			String requestBody=""
					+ "ctl00%24SDSM=ctl00%24CP1%24Main%7Cctl00%24CP1%24btnSearch"
					+ "&__EVENTTARGET="
					+ "&__EVENTARGUMENT="
					+ "&__VIEWSTATE=%2FwEPDwUKMTYzNTg1NjEyNw9kFgJmD2QWAgIDD2QWAgIFD2QWAgIBD2QWAmYPZBYCAgcPPCsADQEADxYEHgtfIURhdGFCb3VuZGceC18hSXRlbUNvdW50AgFkFgJmD2QWBmYPZBYOZg8PZBYCHgVjbGFzcwUIR3JpZEhlYWRkAgEPD2QWAh8CBQhHcmlkSGVhZGQCAg8PZBYCHwIFCEdyaWRIZWFkZAIDDw9kFgIfAgUIR3JpZEhlYWRkAgQPD2QWAh8CBQhHcmlkSGVhZGQCBQ8PZBYCHwIFCEdyaWRIZWFkZAIGDw9kFgIfAgUIR3JpZEhlYWRkAgEPD2QWAh4Hb25jbGlja2QWDmYPDxYCHgRUZXh0BQoyMDE4LTAxLTE3ZGQCAQ8PFgIfBAUG5rGH57y0ZGQCAg8PFgIfBAUGMTM4LjAwZGQCAw8PFgIfBAUEMC4wMGRkAgQPDxYCHwQFBDAuMDBkZAIFDw8WAh8EBQgzLDUyMC4yOGRkAgYPDxYCHwQFV%2Ba4oOmBk%2Badpea6kO%2B8mijnvZHljoUpIOaxh%2Be8tO%2B8muWNleS9jee8tOWtmCvkuKrkurrnvLTlrZgyMDE35bm0MTLmnIgg6IezIDIwMTflubQxMuaciGRkAgIPDxYCHgdWaXNpYmxlaGQWCAICDw8WAh8EBQQwLjAwZGQCAw8PFgIfBAUEMC4wMGRkAgQPDxYCHwQFBDAuMDBkZAIFDw8WAh8EBQQwLjAwZGQYAQUaY3RsMDAkQ1AxJEdyaWREYXRhR1JaSE1YQ1gPPCsACgEIAgFkOGxN43KGg%2FFsiP3TG%2Bos5w4gpzI%3D"
					+ "&__VIEWSTATEGENERATOR=1D5E2139"
					+ "&__EVENTVALIDATION=%2FwEWBQLfiJ%2B1BgKd5ZjVAwKd5ZTUAwKLs%2FuXDQLP2oasDWywTGjb%2BVzlX5vcdcTMHjEgQ8TY"
					+ "&ctl00%24CP1%24TxtKSRQ=2000-01-01"  //从2000年开始爬取
					+ "&ctl00%24CP1%24TxtJSRQ="+HousingFundHelperService.getPresentDate().trim()+""
					+ "&GridDataGRZHMXCX_GrdData=%2C1"
					+ "&ctl00%24_systabindexString="
					+ "&__ASYNCPOST=true"
					+ "&ctl00%24CP1%24btnSearch=%E6%A3%80%E7%B4%A2";
			webRequest.setRequestBody(requestBody);
			Page page = webClient.getPage(webRequest); 
			if(null!=page){
				String html = page.getWebResponse().getContentAsString(); 
	 			HousingLiangShanHtml housingLiangShanHtml=new HousingLiangShanHtml();
	 			housingLiangShanHtml.setHtml(html);
	 			housingLiangShanHtml.setPagenumber(1);
	 			housingLiangShanHtml.setTaskid(taskHousing.getTaskid());
	 			housingLiangShanHtml.setType("个人明细账源码页");
	 			housingLiangShanHtml.setUrl(url);
	 			housingLiangShanHtmlRepository.save(housingLiangShanHtml);
	 			List<HousingLiangShanDetailAccount> list = housingFundLiangShanParser.detailAccountParser(html,taskHousing);
	 			if(null!=list && list.size()>0){
	 				housingLiangShanDetailAccountRepository.saveAll(list);
	 				updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成", 200,taskHousing.getTaskid());
	 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息已采集完成");
	 			}else{
	 				updatePayStatusByTaskid("数据采集中，个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
	 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息暂无可采集数据");
	 			}
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getDetailAccount.e", e.toString());
			updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid());
		}
		return new AsyncResult<String>("200");
	}
}
