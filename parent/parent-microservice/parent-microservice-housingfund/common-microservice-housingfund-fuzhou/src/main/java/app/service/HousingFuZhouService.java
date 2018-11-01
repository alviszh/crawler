package app.service;

import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.fuzhou.HousingFuZhouPay;
import com.microservice.dao.entity.crawler.housing.fuzhou.HousingFuZhouUserinfo;
import com.microservice.dao.repository.crawler.housing.fuzhou.HousingFuZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.fuzhou.HousingFuZhouUserInfoRepository;

import app.crawler.htmlparse.HousingFZParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.fuzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.fuzhou")
public class HousingFuZhouService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingFuZhouService.class);
	
	@Autowired
	public HousingFuZhouUserInfoRepository housingFuZhouUserInfoRepository;
	@Autowired
	public HousingFuZhouPayRepository housingFuZhouPayRepository;
	
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String url = "http://www.fzgjj.com.cn:7002/fznetface/member/index.do";
		String urlInfo = "http://www.fzgjj.com.cn:7002/fznetface/member/depositInfo.do?_r=0.6604527050228082";
		String urlpay = "http://www.fzgjj.com.cn:7002/fznetface/member/marshalAccInfo.do?_r=0.719069604666619";
		Page page = null;
		//基本信息
		try {
			page = loginAndGetCommon.getHtml(url, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
		try {
			page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html1 = page.getWebResponse().getContentAsString();
		tracer.addTag("个人信息html", html);
		tracer.addTag("个人信息html", html1);
		System.out.println(html1);
		HousingFuZhouUserinfo userinfo = HousingFZParse.userinfo_parse(html,html1);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingFuZhouUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		//缴存明细
		try {
			page = loginAndGetCommon.getHtml(urlpay, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html2 = page.getWebResponse().getContentAsString();
		tracer.addTag("缴费信息html", html2);
		System.out.println(html2);
		List<HousingFuZhouPay> listresult = HousingFZParse.paydetails_parse(html2,taskHousing);
		if(listresult==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			
			housingFuZhouPayRepository.saveAll(listresult);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
			
		
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}
}
