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
import com.microservice.dao.entity.crawler.housing.jian.HousingJiAnPay;
import com.microservice.dao.entity.crawler.housing.jian.HousingJiAnUserInfo;
import com.microservice.dao.repository.crawler.housing.jian.HousingJiAnPayRepository;
import com.microservice.dao.repository.crawler.housing.jian.HousingJiAnUserinfoRepository;

import app.crawler.htmlparse.HousingJAParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jian")
public class HousingJiAnService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingJiAnService.class);
	
	@Autowired
	public HousingJiAnUserinfoRepository housingJiAnUserInfoRepository;
	@Autowired
	public HousingJiAnPayRepository housingJiAnPayRepository;
	
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "http://218.64.84.34:8089/gongjijin/jbxx.action?id=18053055";
		String urlpay = "http://218.64.84.34:8089/gongjijin/jcmx.action";
		Page page = null;
		//基本信息
		try {
			page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("个人信息html", html);
		System.out.println(html);
		HousingJiAnUserInfo userinfo = HousingJAParse.userinfo_parse(html);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingJiAnUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		//缴存明细
		try {
			page = loginAndGetCommon.getHtml(urlpay, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String html1 = page.getWebResponse().getContentAsString();
		tracer.addTag("缴费信息html", html1);
		System.out.println(html1);	
		List<HousingJiAnPay> listresult = HousingJAParse.paydetails_parse(html1,taskHousing);
		if(listresult==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			
			housingJiAnPayRepository.saveAll(listresult);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
			
		
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		return null;
	}

}
