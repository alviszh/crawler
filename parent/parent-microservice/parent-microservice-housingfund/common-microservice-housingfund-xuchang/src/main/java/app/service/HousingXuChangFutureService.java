package app.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xuchang.HousingXuChangUserInfo;
import com.microservice.dao.repository.crawler.housing.xuchang.HousingXuChangUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.crawler.htmlparse.HousingXCParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xuchang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xuchang")
public class HousingXuChangFutureService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingXuChangFutureService.class);
	
	@Autowired
	private HousingXuChangUserInfoRepository housingXuChangUserInfoRepository;
	public  Object crawler(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		String url1 = "http://www.xcgjj.com/EpointWebBuilder/zNJSAction.action?cmd=getDkxx&sfzid="+messageLoginForHousing.getNum().trim()+"";
		Page page = null;
		WebRequest webRequest;
		try {
			webRequest = new WebRequest(new URL(url1), HttpMethod.GET);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			page = webClient.getPage(webRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆异常", e.getMessage());
			save(taskHousing);
			return null;
		}
//		String url = "http://www.xcgjj.com/searchdao.html?sfzid="+messageLoginForHousing.getNum().trim()+"";
//		HtmlPage htmlpage = null;
//		try {
//			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
//			webClient.setJavaScriptTimeout(50000); 
//			webClient.getOptions().setTimeout(50000); // 15->60 
//			htmlpage = webClient.getPage(webRequest);
//			//System.out.println(htmlpage.asXml());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhase());
//			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getPhasestatus());
//			taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGINTWO_ERROR.getDescription());
//
//			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
//			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
//			//taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("parser.login.auth", e.getMessage());
//			save(taskHousing);
//			return null;
//		}

        //System.out.println(htmlpage.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("DWDM")){
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
			System.out.println("111111");
			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
			//taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			
			
			HousingXuChangUserInfo userinfo = HousingXCParse.userinfo_parse(page.getWebResponse().getContentAsString());
			userinfo.setTaskid(taskHousing.getTaskid());
			housingXuChangUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
			return taskHousing;
		}else{
			System.out.println("222222");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
			taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
			save(taskHousing);
			return taskHousing;
		}
		
	}
}
