package app.service;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.lishui.HousingLiShuiDetailAccount;
import com.microservice.dao.entity.crawler.housing.lishui.HousingLiShuiHtml;
import com.microservice.dao.entity.crawler.housing.lishui.HousingLiShuiUserInfo;
import com.microservice.dao.repository.crawler.housing.lishui.HousingLiShuiDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.lishui.HousingLiShuiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.lishui.HousingLiShuiUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingFundLiShuiParser;
import app.service.common.HousingBasicService;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.lishui"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.lishui"})
public class HousingFundLiShuiCrawlerService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFundLiShuiCrawlerService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingFundLiShuiParser housingFundLiShuiParser;
	@Autowired
	private HousingLiShuiHtmlRepository housingLiShuiHtmlRepository;
	@Autowired
	private HousingLiShuiDetailAccountRepository housingLiShuiDetailAccountRepository;
	@Autowired
	private HousingLiShuiUserInfoRepository housingLiShuiUserInfoRepository;
	public WebClient addcookie(TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskHousing.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}
		return webClient;
	}
	@Async
	public void getAllInfo(TaskHousing taskHousing){
		try {
			WebClient webClient = addcookie(taskHousing);
			webClient.getOptions().setJavaScriptEnabled(false);
			String url="http://gjj.e0578.com/hdcy_1.aspx";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page= webClient.getPage(webRequest); 
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				HousingLiShuiHtml housingLiShuiHtml=new HousingLiShuiHtml();
	 			housingLiShuiHtml.setHtml(html);
	 			housingLiShuiHtml.setPagenumber(1);
	 			housingLiShuiHtml.setTaskid(taskHousing.getTaskid());
	 			housingLiShuiHtml.setType("用户信息和缴费信息源码页");
	 			housingLiShuiHtml.setUrl(url);
	 			housingLiShuiHtmlRepository.save(housingLiShuiHtml);
	 			//解析用户信息
	 			HousingLiShuiUserInfo housingLiShuiUserInfo= housingFundLiShuiParser.userInfoParser(html,taskHousing);
				if(null!=housingLiShuiUserInfo){
					housingLiShuiUserInfoRepository.save(housingLiShuiUserInfo);
					updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",200,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
				}else{
					updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
					tracer.addTag("action.crawler.getUserInfo", "数据采集中，用户基本信息暂无可采集数据");
					updateTaskHousing(taskHousing.getTaskid());
				}
	 			//解析缴费信息
	 			List<HousingLiShuiDetailAccount> list = housingFundLiShuiParser.detailAccountParser(html,taskHousing);
	 			if(null!=list && list.size()>0){
	 				housingLiShuiDetailAccountRepository.saveAll(list);
	 				updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成", 200,taskHousing.getTaskid());
	 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息已采集完成");
	 			}else{
	 				updatePayStatusByTaskid("数据采集中，个人明细账信息暂无可采集数据",201,taskHousing.getTaskid() );
	 				tracer.addTag("action.crawler.getDetailAccount", "数据采集中，个人明细账信息暂无可采集数据");
	 			}
	 			//更新最终的爬取状态
	 			updateTaskHousing(taskHousing.getTaskid().trim());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getAllInfo.e", e.toString());
			updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成",201,taskHousing.getTaskid());
			updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成", 201,taskHousing.getTaskid());
			updateTaskHousing(taskHousing.getTaskid().trim());
		}
	}
}
