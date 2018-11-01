package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.beijing.HousingBeiJingPay;
import com.microservice.dao.entity.crawler.housing.beijing.HousingBeiJingUserinfo;
import com.microservice.dao.repository.crawler.housing.beijing.HousingBeiJingPayRepository;
import com.microservice.dao.repository.crawler.housing.beijing.HousingBeiJingUserinfoRepository;

import app.crawler.htmlparse.HousingBJParse;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.beijing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.beijing")
public class HousingBeiJingUnitService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingBeiJingUnitService.class);
	
	@Autowired
	private HousingBeiJingPayRepository housingBeiJingPayRepository;
	
	@Autowired
	private HousingBeiJingUserinfoRepository housingBeiJingUserinfoRepository;
	
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing,String url, WebClient webClient) {
		
		
		Page page = null;
		try {
			page = LoginAndGetCommon.getHtml(url, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(page.getWebResponse().getContentAsString().indexOf("系统忙，请稍后再进行查询")!=-1){
			taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
			taskHousing.setUserinfoStatus(201);
			taskHousing.setPaymentStatus(201);
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
			return new AsyncResult<String>("系统忙，请稍后再进行查询");
		}
		
		
		HousingBeiJingUserinfo userinfo = HousingBJParse.userinfo_parse(page.getWebResponse().getContentAsString());
		if(userinfo.getName()!=null || !userinfo.getName().isEmpty()){
			userinfo.setUserid(messageLoginForHousing.getUser_id());
			userinfo.setTaskid(taskHousing.getTaskid());
			save(userinfo);
		}
		
		url = getURLforPay(page.getWebResponse().getContentAsString());
		
		try {
			page = LoginAndGetCommon.getHtml(url, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
			taskHousing.setUserinfoStatus(201);
			taskHousing.setPaymentStatus(201);
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
			return null;
		}
		
				
		List<HousingBeiJingPay> listresult = HousingBJParse.pay_parse(page.getWebResponse().getContentAsString());
		
		for(HousingBeiJingPay result : listresult){
			if(result.getBalance()==null ||result.getBalance().isEmpty()){
				continue;
			}
			result.setUserid(messageLoginForHousing.getUser_id());
			result.setTaskid(taskHousing.getTaskid());
			save(result);
		}
		taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		taskHousing.setUserinfoStatus(200);
		taskHousing.setPaymentStatus(200);
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
	
	public  String getURLforPay(String html) {
		Document doc = Jsoup.parse(html);

		String urlString = doc.select("a:contains(查看历史明细账信息)").attr("onclick");

		System.out.println(urlString);
		String url = "http://www.bjgjj.gov.cn/wsyw/wscx/"
				+ urlString.substring(urlString.indexOf("(") + 1, urlString.indexOf(",")).replaceAll("\'", "").trim();
		System.out.println("缴费url == " + url);

		return url;
	}
	
	public  List<String> getURLforUserInfo(String html) {
		List<String> urllist = new ArrayList<>();
		Document doc = Jsoup.parse(html);
		Elements urlas = doc.select("td.style21").select("a");
		for (Element urla : urlas) {
			String urlString = urla.attr("onclick");

			String url = "http://www.bjgjj.gov.cn/wsyw/wscx/" + urlString
					.substring(urlString.indexOf("(") + 1, urlString.indexOf(",")).replaceAll("\"", "").trim();
			System.out.println("====" + url);
			urllist.add(url);
		}
		return urllist;
	}
	
	private void save(HousingBeiJingPay result){
		housingBeiJingPayRepository.save(result);
	}
	private void save(HousingBeiJingUserinfo result){
		housingBeiJingUserinfoRepository.save(result);
	}
	
}