package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.guangzhou.HousingGuangzhouDetailAccount;
import com.microservice.dao.entity.crawler.housing.guangzhou.HousingGuangzhouHtml;
import com.microservice.dao.entity.crawler.housing.guangzhou.HousingGuangzhouUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.guangzhou.HousingGuangzhouDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.guangzhou.HousingGuangzhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.guangzhou.HousingGuangzhouUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundGuangZhouParser;
import app.service.common.HousingFundHelperService;


/**
 * @description: 广州公积金信息爬取service
 * @author: sln 
 * @date: 2017年9月29日 上午9:52:28 
 */
@Component
public class HousingFundGuangZhouCrawlerService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundGuangZhouCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundGuangZhouParser housingFundGuangZhouParser;
	@Autowired
	private HousingGuangzhouHtmlRepository housingGuangzhouHtmlRepository;
	@Autowired
	private HousingGuangzhouDetailAccountRepository housingGuangzhouDetailAccountRepository;
	@Autowired
	private HousingGuangzhouUserInfoRepository housingGuangzhouUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Autowired
	private TaskHousingRepository taskHousingRepository;
	@Async
	public Future<String> getUserInfo(TaskHousing taskHousing) throws Exception {
		//获取爬取数据需要的cookie
		WebClient webClient = housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String url = "https://gzgjj.gov.cn/wsywgr/TQAction!getMapInfo.action"; 
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
 		webRequest.setAdditionalHeader("Host", "gzgjj.gov.cn");
 		webRequest.setAdditionalHeader("Referer", "https://gzgjj.gov.cn/wsywgr/index.jsp");
 		Page page = webClient.getPage(webRequest);  
 		if(null!=page){
 			String html = page.getWebResponse().getContentAsString(); 
 			HousingGuangzhouHtml housingGuangzhouHtml=new HousingGuangzhouHtml();
 			housingGuangzhouHtml.setHtml(html);
 			housingGuangzhouHtml.setPagenumber(1);
 			housingGuangzhouHtml.setTaskid(taskHousing.getTaskid());
 			housingGuangzhouHtml.setType("用户基本信息源码页");
 			housingGuangzhouHtml.setUrl(url);
 			housingGuangzhouHtmlRepository.save(housingGuangzhouHtml);
 			HousingGuangzhouUserInfo housingGuangzhouUserInfo= housingFundGuangZhouParser.userInfoParser(html,taskHousing);
 			if(null!=housingGuangzhouUserInfo){
 				housingGuangzhouUserInfoRepository.save(housingGuangzhouUserInfo);
 				taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
 			}else{
 				taskHousingRepository.updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
 				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息暂无可采集数据");
 			}
 		}
		return new AsyncResult<String>("200");
		
	}
	@Async
	public Future<String> getDetailAccount(TaskHousing taskHousing) throws Exception {
		//获取爬取数据需要的cookie
		WebClient webClient = housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		//从2000年开始获取数据
		String presentDate = HousingFundHelperService.getPresentDate();
		//把每一页显示的数据设置为了100条
		String url = "https://gzgjj.gov.cn/wsywgr/TQAction!getDetailInfo.action?struts.token.name=token&token=JZAVYH1Q21ZNA7WGKA2VGWBSKWOICQKX&start=20000101&end="+presentDate+"&pageNo=1&dwdjh=&pageCount=100"; 
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
 		webRequest.setAdditionalHeader("Host", "gzgjj.gov.cn");
 		webRequest.setAdditionalHeader("Referer", "https://gzgjj.gov.cn/wsywgr/index.jsp");
 		Page page = webClient.getPage(webRequest);  
 		if(null!=page){
 			String html = page.getWebResponse().getContentAsString(); 
 			HousingGuangzhouHtml housingGuangzhouHtml=new HousingGuangzhouHtml();
 			housingGuangzhouHtml.setHtml(html);
 			housingGuangzhouHtml.setPagenumber(1);
 			housingGuangzhouHtml.setTaskid(taskHousing.getTaskid());
 			housingGuangzhouHtml.setType("个人明细账源码页");
 			housingGuangzhouHtml.setUrl(url);
 			housingGuangzhouHtmlRepository.save(housingGuangzhouHtml);
 			List<HousingGuangzhouDetailAccount> list = housingFundGuangZhouParser.detailAccountParser(html,taskHousing,presentDate);
 			if(null!=list && list.size()>0){
 				housingGuangzhouDetailAccountRepository.saveAll(list);
 				taskHousingRepository.updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成", 200,taskHousing.getTaskid());
 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息已采集完成");
 			}else{
 				taskHousingRepository.updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid() );
 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息暂无可采集数据");
 			}
 		}
		return new AsyncResult<String>("200");
	}
}
