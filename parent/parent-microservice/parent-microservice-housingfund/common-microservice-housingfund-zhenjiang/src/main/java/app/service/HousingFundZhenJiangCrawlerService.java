package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhenjiang.HousingZhenJiangDetailAccount;
import com.microservice.dao.entity.crawler.housing.zhenjiang.HousingZhenJiangHtml;
import com.microservice.dao.entity.crawler.housing.zhenjiang.HousingZhenJiangUserInfo;
import com.microservice.dao.repository.crawler.housing.zhenjiang.HousingZhenJiangDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.zhenjiang.HousingZhenJiangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.zhenjiang.HousingZhenJiangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundZhenJiangParser;
import app.service.common.HousingBasicService;
import app.service.common.HousingFundHelperService;


/**
 * @author: sln 
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.zhenjiang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.zhenjiang"})
public class HousingFundZhenJiangCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundZhenJiangCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundZhenJiangParser housingFundZhenJiangParser;
	@Autowired
	private HousingZhenJiangHtmlRepository housingZhenJiangHtmlRepository;
	@Autowired
	private HousingZhenJiangDetailAccountRepository housingZhenJiangDetailAccountRepository;
	@Autowired
	private HousingZhenJiangUserInfoRepository housingZhenJiangUserInfoRepository;
	@Autowired
	private HousingFundHelperService housingFundHelperService;
	@Async
	public void getUserInfo(TaskHousing taskHousing){
		try {
			WebClient webClient=housingFundHelperService.addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url = "http://www.zjzfjj.com.cn/searchGrye.do"; 
	 		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
	 		Page hPage = webClient.getPage(webRequest);  
	 		if(null!=hPage){
	 			String html = hPage.getWebResponse().getContentAsString(); 
	 			HousingZhenJiangHtml housingZhenJiangHtml=new HousingZhenJiangHtml();
	 			housingZhenJiangHtml.setHtml(html);
	 			housingZhenJiangHtml.setPagenumber(1);
	 			housingZhenJiangHtml.setTaskid(taskHousing.getTaskid());
	 			housingZhenJiangHtml.setType("用户基本信息源码页");
	 			housingZhenJiangHtml.setUrl(url);
	 			housingZhenJiangHtmlRepository.save(housingZhenJiangHtml);
	 			tracer.addTag("action.crawler.getUserInfo.html", "用户基本信息源码页已经入库");
	 			//解析用户信息
	 			HousingZhenJiangUserInfo housingZhenJiangUserInfo=housingFundZhenJiangParser.userInfoParser(html,taskHousing);
	 			if(null!=housingZhenJiangUserInfo){
	 				housingZhenJiangUserInfoRepository.save(housingZhenJiangUserInfo);
	 				updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
					try {
						getDetailAccount(taskHousing);
					} catch (Exception e) {
						tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid()+"  "+e.toString());
						updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",200,taskHousing.getTaskid() );
					}
	 			}
	 			updateTaskHousing(taskHousing.getTaskid());
	 		}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getUserInfo.e", taskHousing.getTaskid()+"  "+e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
			updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成",201,taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid());
		}
	
	}
	public void getDetailAccount(TaskHousing taskHousing) throws Exception {
		//提供的账号中有三年的数据可供查询，可以通过改变年份来查询数据，此处根据下拉框提供的所有年份来查询（option）
		WebClient webClient=housingFundHelperService.addcookie(taskHousing);
		webClient.getOptions().setJavaScriptEnabled(false);
		String year = HousingFundHelperService.getYear(0); //根据当前年份的响应页面，获取下拉框的年份选项
		String url="http://www.zjzfjj.com.cn/searchGrmx.do?year="+year+"";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
		Page page = webClient.getPage(webRequest);
 		if(null!=page){
 			List<String> yearList=new ArrayList<String>();
 			String html=page.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(html);
			Elements elementsByTag = doc.getElementsByTag("select").get(0).getElementsByTag("option");
			if(null!=elementsByTag){
				tracer.addTag("获取的要查询明细账年份的option集合是：", elementsByTag.toString());
				for (Element element : elementsByTag) {
					yearList.add(element.text());
				}
				if(yearList!=null && yearList.size()>0){
					for (String qryYear : yearList) {
						url="http://www.zjzfjj.com.cn/searchGrmx.do?year="+qryYear+"";
						webRequest = new WebRequest(new URL(url), HttpMethod.GET);   
						page = webClient.getPage(webRequest);
						if(null!=page){
							html=page.getWebResponse().getContentAsString();
							HousingZhenJiangHtml housingZhenJiangHtml=new HousingZhenJiangHtml();
				 			housingZhenJiangHtml.setHtml(html);
				 			housingZhenJiangHtml.setPagenumber(1);
				 			housingZhenJiangHtml.setTaskid(taskHousing.getTaskid());
				 			housingZhenJiangHtml.setType(""+qryYear+"年个人明细账源码页");
				 			housingZhenJiangHtml.setUrl(url);
				 			housingZhenJiangHtmlRepository.save(housingZhenJiangHtml);
				 			List<HousingZhenJiangDetailAccount> list = housingFundZhenJiangParser.detailAccountParser(html,taskHousing);
				 			if(null!=list && list.size()>0){
				 				housingZhenJiangDetailAccountRepository.saveAll(list);
				 				updatePayStatusByTaskid("数据采集中，"+qryYear+"年个人明细账信息已采集完成", 200,taskHousing.getTaskid());
				 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+qryYear+"年个人明细账信息已采集完成");
				 			}else{
				 				updatePayStatusByTaskid("数据采集中，"+qryYear+"年个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
				 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，"+qryYear+"年个人明细账信息暂无可采集数据");
				 			}
						}
					}
				}
			}else{
				//未查询到满足个人条件的信息
				updatePayStatusByTaskid("数据采集中，个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息暂无可采集数据");
			}
 		}
	}
}
