package app.service;

import java.net.URL;
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
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.baotou.HousingBaoTouUserinfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.baotou.HousingBaoTouUserInfoRepository;

import app.crawler.htmlparse.HousingBTParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.baotou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.baotou")
public class HousingBaoTouService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingBaoTouService.class);
	@Autowired
	private HousingBaoTouUserInfoRepository housingBaoTouUserInfoRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "http://www.btgjj.com/tools/submit_ajax.ashx?action=user_gjj_person&site=main";
		Page page = null;
		//基本信息
		try {
			WebRequest webRequest = new WebRequest(new URL(urlInfo), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
//			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest.setAdditionalHeader("Host", "www.btgjj.com");
			webRequest.setAdditionalHeader("Origin", "http://www.btgjj.com");
			webRequest.setAdditionalHeader("Referer", "http://www.btgjj.com/user/gjj/person.html");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			page = webClient.getPage(webRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
		html = html.substring(html.indexOf("<"), html.lastIndexOf(">")+1);
		html = "<html><head></head><body><table>"+html+"</table></body></html>";
		System.out.println(html);
		tracer.addTag("parser.crawler.user.page", "<xmp>"+html+"</xmp>");
		HousingBaoTouUserinfo userinfo = HousingBTParse.userinfo_parse(html);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingBaoTouUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		taskHousing.setPaymentStatus(0);
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}

}
