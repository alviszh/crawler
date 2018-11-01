package app.service;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xiaogan.HousingXiaoGanPay;
import com.microservice.dao.entity.crawler.housing.xiaogan.HousingXiaoGanUserinfo;
import com.microservice.dao.repository.crawler.housing.xiaogan.HousingXiaoGanPayRepository;
import com.microservice.dao.repository.crawler.housing.xiaogan.HousingXiaoGanUserinfoRepository;

import app.crawler.htmlparse.HousingXGParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xiaogan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xiaogan")
public class HousingXiaoGanService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingXiaoGanService.class);
	
	@Autowired
	public HousingXiaoGanUserinfoRepository housingXiaoGanUserinfoRepository;
	@Autowired
	public HousingXiaoGanPayRepository housingXiaoGanPayRepository;
	
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient ,String html) {
		
		Page page = null;
		//基本信息
		HousingXiaoGanUserinfo userinfo = HousingXGParse.userinfo_parse(html);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingXiaoGanUserinfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		//缴费信息
		
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		for(int i = 0;i<=13;i++){
			int date = endNian-i;
			String urlPay = "http://www.xggjj.com/action/gjjcx";
			try {
				WebRequest webRequest1 = new WebRequest(new URL(urlPay), HttpMethod.POST);
				webRequest1.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			    webRequest1.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			    webRequest1.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			    webRequest1.setAdditionalHeader("Cache-Control", "max-age=0");
			    webRequest1.setAdditionalHeader("Connection", "keep-alive");
			    webRequest1.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			    webRequest1.setAdditionalHeader("Host", "www.xggjj.com");
			    webRequest1.setAdditionalHeader("Origin", "http://www.xggjj.com");
			    webRequest1.setAdditionalHeader("Referer", "http://www.xggjj.com/page/wzzs/wzzs_center_mode/grcxindex.jsp");
			    webRequest1.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			    webRequest1.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			    String requestBody1 = "grid=1001090315&kjny="+date+"&ksny="+(date-1)+"07&jsny="+date+"06&exeType=selGrjbxxKjnydate";
				webRequest1.setRequestBody(requestBody1);
				page = webClient.getPage(webRequest1);
				//page = loginAndGetCommon.getHtml(urlInfo, webClient);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String html1 = page.getWebResponse().getContentAsString();
			System.out.println(html1);
			
			if (!html1.contains("01")){
					break;
			}
			List<HousingXiaoGanPay> listresult = HousingXGParse.paydetails_parse(html1);
			for(HousingXiaoGanPay result : listresult){
				if(result==null){
					taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
					break;
				}else{
					result.setTaskid(taskHousing.getTaskid());
					housingXiaoGanPayRepository.save(result);
					taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
				}
			}
		}
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}

}
