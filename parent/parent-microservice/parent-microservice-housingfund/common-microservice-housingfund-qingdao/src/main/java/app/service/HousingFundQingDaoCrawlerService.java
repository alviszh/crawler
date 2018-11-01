package app.service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Future;

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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoChargeInfo;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoCompInfo;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoDetailAccount;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoHtml;
import com.microservice.dao.entity.crawler.housing.qingdao.HousingQingDaoUserInfo;
import com.microservice.dao.repository.crawler.housing.qingdao.HousingQingDaoChargeInfoRepository;
import com.microservice.dao.repository.crawler.housing.qingdao.HousingQingDaoCompInfoRepository;
import com.microservice.dao.repository.crawler.housing.qingdao.HousingQingDaoDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.qingdao.HousingQingDaoHtmlRepository;
import com.microservice.dao.repository.crawler.housing.qingdao.HousingQingDaoUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundQingDaoParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;

/**
 * @description:
 * @author: sln 
 * @date: 2017年10月25日 上午11:32:39 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.qingdao"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.qingdao"})
public class HousingFundQingDaoCrawlerService extends HousingBasicService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundQingDaoParser housingFundQingDaoParser;
	@Autowired
	private HousingQingDaoUserInfoRepository housingQingDaoUserInfoRepository;
	@Autowired
	private HousingQingDaoHtmlRepository housingQingDaoHtmlRepository;
	@Autowired
	private HousingQingDaoDetailAccountRepository housingQingDaoDetailAccountRepository;
	@Autowired
	private HousingQingDaoChargeInfoRepository housingQingDaoChargeInfoRepository;
	@Autowired
	private HousingQingDaoCompInfoRepository housingQingDaoCompInfoRepository;
	@Autowired
	private  HousingFundHelperService housingFundHelperService;
	@Value("${loginip}") 
	public String loginip;
	//获取用户信息
	@Async
	public Future<String> getUserInfo(TaskHousing taskHousing) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		String url = "http://"+loginip+"/Controller/GR/gjcx/gjjzlcx.ashx"; 
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
 		Page hPage = webClient.getPage(webRequest);  
 		if(null!=hPage){
 			String html = hPage.getWebResponse().getContentAsString(); 
 			HousingQingDaoHtml housingQingDaoHtml=new HousingQingDaoHtml();
 			housingQingDaoHtml.setHtml(html);
 			housingQingDaoHtml.setPagenumber(1);
 			housingQingDaoHtml.setTaskid(taskHousing.getTaskid());
 			housingQingDaoHtml.setType("用户基本信息源码页");
 			housingQingDaoHtml.setUrl(url);
 			housingQingDaoHtmlRepository.save(housingQingDaoHtml);
 			tracer.addTag("action.crawler.getUserInfo.html", "用户基本信息源码页已经入库");
 			//解析用户信息
 			HousingQingDaoUserInfo housingQingDaoUserInfo=housingFundQingDaoParser.userInfoParser(html,taskHousing);
 			if(null!=housingQingDaoUserInfo){
 				housingQingDaoUserInfoRepository.save(housingQingDaoUserInfo);
 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
				
 			}else{
				updateUserInfoStatusByTaskid("数据采集中，用户基本信息暂无可采集数据",201,taskHousing.getTaskid());
				tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
			}
 		}
		return new AsyncResult<String>("200");
	}
	//获取明细账信息
	@Async
	public Future<String> getDetailAccount(TaskHousing taskHousing) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		String transDateEnd=HousingFundHelperService.getPresentDate();
		//获取从2000年开始的数据，故将一页的数据设置为300
		String url = "http://"+loginip+"/Controller/GR/gjcx/gjjmx.ashx?transDateBegin=2000-01-01&transDateEnd="+transDateEnd+"&page=1&rows=300&sort=mxbc&order=desc"; 
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
 		Page hPage = webClient.getPage(webRequest);  
 		if(null!=hPage){
 			String html = hPage.getWebResponse().getContentAsString(); 
 			HousingQingDaoHtml housingQingDaoHtml=new HousingQingDaoHtml();
 			housingQingDaoHtml.setHtml(html);
 			housingQingDaoHtml.setPagenumber(1);
 			housingQingDaoHtml.setTaskid(taskHousing.getTaskid());
 			housingQingDaoHtml.setType("2000年至今公积金明细账信息源码页");
 			housingQingDaoHtml.setUrl(url);
 			housingQingDaoHtmlRepository.save(housingQingDaoHtml);
 			tracer.addTag("action.crawler.detailAccount.html", "2000年至今公积金明细账信息源码页已经入库");
 			//解析用户信息
 			List<HousingQingDaoDetailAccount> list=housingFundQingDaoParser.detailAccountParser(html,taskHousing);
 			if(null!=list && list.size()>0){
 				housingQingDaoDetailAccountRepository.saveAll(list);
 				updatePayStatusByTaskid("数据采集中,2000年至今公积金明细账信息已采集完成", 200,taskHousing.getTaskid());
 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2000年至今公积金明细账信息已采集完成");
 			}else{
 				updatePayStatusByTaskid("数据采集中，2000年至今公积金明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2000年至今公积金明细账信息暂无可采集数据");
 			}
 		}else{
			updatePayStatusByTaskid("数据采集中，2000年至今公积金明细账信息已采集完成",201,taskHousing.getTaskid() );
			tracer.addTag("action.crawler.getDetailAccount", "数据采集中，2000年至今公积金明细账信息暂无可采集数据");
 		}
		return new AsyncResult<String>("200");
	}
	//获取单位信息
	@Async
	public Future<String> getCompInfo(TaskHousing taskHousing) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		String url = "http://"+loginip+"/Controller/GR/gjcx/dwjbxx.ashx"; 
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
 		Page hPage = webClient.getPage(webRequest);  
 		if(null!=hPage){
 			String html = hPage.getWebResponse().getContentAsString(); 
 			HousingQingDaoHtml housingQingDaoHtml=new HousingQingDaoHtml();
 			housingQingDaoHtml.setHtml(html);
 			housingQingDaoHtml.setPagenumber(1);
 			housingQingDaoHtml.setTaskid(taskHousing.getTaskid());
 			housingQingDaoHtml.setType("单位基本信息源码页");
 			housingQingDaoHtml.setUrl(url);
 			housingQingDaoHtmlRepository.save(housingQingDaoHtml);
 			tracer.addTag("action.crawler.getCompInfo.html", "单位基本信息源码页已经入库");
 			//解析单位信息
 			HousingQingDaoCompInfo housingQingDaoCompInfo=housingFundQingDaoParser.compInfoParser(html,taskHousing);
 			if(null!=housingQingDaoCompInfo){
 				housingQingDaoCompInfoRepository.save(housingQingDaoCompInfo);
				tracer.addTag("action.crawler.getCompInfo", "数据采集中，单位基本信息已采集完成");
 			}else{
				tracer.addTag("action.crawler.getCompInfo", "数据采集中，单位基本信息暂无可采集数据");
			}
 		}
		return new AsyncResult<String>("200");
	}
	//获取缴费信息
	@Async
	public Future<String> getChargeInfo(TaskHousing taskHousing) throws Exception {
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		String transDateEnd=HousingFundHelperService.getPresentDate();
		//获取从2000年开始的数据，故将一页的数据设置为300
		String url = "http://"+loginip+"/Controller/GR/gjcx/gjcx.ashx?"; 
 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);   
 		String requestBody="dt=1508987007862&m=grjcmx&start=2000-01-01&end="+transDateEnd+"&page=1&rows=300&sort=csrq&order=desc";
 		webRequest.setRequestBody(requestBody);
 		Page hPage = webClient.getPage(webRequest);  
 		if(null!=hPage){
 			String html = hPage.getWebResponse().getContentAsString(); 
 			HousingQingDaoHtml housingQingDaoHtml=new HousingQingDaoHtml();
 			housingQingDaoHtml.setHtml(html);
 			housingQingDaoHtml.setPagenumber(1);
 			housingQingDaoHtml.setTaskid(taskHousing.getTaskid());
 			housingQingDaoHtml.setType("2000年至今公积金缴存信息源码页");
 			housingQingDaoHtml.setUrl(url);
 			housingQingDaoHtmlRepository.save(housingQingDaoHtml);
 			tracer.addTag("action.crawler.getChargeInfo.html", "2000年至今公积金缴存信息源码页已经入库");
 			List<HousingQingDaoChargeInfo> list=housingFundQingDaoParser.chargeInfoParser(html,taskHousing);
 			if(null!=list && list.size()>0){
 				housingQingDaoChargeInfoRepository.saveAll(list);
 				updatePayStatusByTaskid("数据采集中,2000年至今公积金缴存信息已采集完成",200,taskHousing.getTaskid() );
 				tracer.addTag("action.crawler.getChargeInfo", "数据采集中，2000年至今公积金缴存信息已采集完成");
 			}else{
 				tracer.addTag("action.crawler.getChargeInfo", "数据采集中，2000年至今公积金缴存信息暂无可采集数据");
 				updatePayStatusByTaskid("数据采集中，2000年至今公积金缴存信息暂无可采集数据",201,taskHousing.getTaskid() );
 			}
 		}else{
 			updatePayStatusByTaskid("数据采集中，2000年至今公积金缴存信息暂无可采集数据",201,taskHousing.getTaskid() );
			tracer.addTag("action.crawler.getChargeInfo", "数据采集中，2000年至今公积金缴存信息暂无可采集数据");
 		}
		return new AsyncResult<String>("200");
	}

}
