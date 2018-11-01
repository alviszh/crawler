package app.service;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xianyang.HousingXianYangUserInfo;
import com.microservice.dao.repository.crawler.housing.xianyang.HousingXianYangUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.crawler.htmlparse.HousingXYParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xianyang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xianyang")
public class HousingXianYangFutureService extends HousingBasicService implements ICrawlerLogin,ICrawler{
	public static final Logger log = LoggerFactory.getLogger(HousingXianYangFutureService.class);
	
	@Autowired
	private HousingXianYangUserInfoRepository housingXianYangUserInfoRepository;
//	public static String html =null;
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://xyzfgjj.xys.gov.cn/chaxun_geren.asp";
		HtmlPage htmlpage = null;
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			htmlpage = webClient.getPage(webRequest);
			HtmlTextInput username = htmlpage.getFirstByXPath("//*[@id='RealName']");
			HtmlTextInput num = htmlpage.getFirstByXPath("//*[@id='UserIDCard']");
			HtmlElement button = htmlpage.getFirstByXPath("//*[@id='button']");
			username.setText(messageLoginForHousing.getUsername());
			num.setText(messageLoginForHousing.getNum());
			htmlpage = button.click();
			//System.out.println(htmlpage.asXml());
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
			return taskHousing;
		}

		if(htmlpage!=null){
			tracer.addTag("登陆html", htmlpage.getWebResponse().getContentAsString());
			if(htmlpage.getWebResponse().getContentAsString().contains("帐户信息")){
				String html = htmlpage.getWebResponse().getContentAsString();
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
				taskHousing.setCookies(html);
				//taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
				
				tracer.addTag("登陆成功", messageLoginForHousing.getTask_id());
				return taskHousing;
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FOURE.getDescription());
				taskHousing.setError_message(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
				save(taskHousing);
				tracer.addTag("登陆失败", messageLoginForHousing.getTask_id());
				return taskHousing;
			}
		}else{
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆html", null);
			save(taskHousing);
			return taskHousing;
		}
		
	}
	
	@Async
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			String html = taskHousing.getCookies();
			HousingXianYangUserInfo userinfo = HousingXYParse.userinfo_parse(html);
			userinfo.setTaskid(taskHousing.getTaskid());
			housingXianYangUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
			taskHousing.setPaymentStatus(0);
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("爬取异常", e.getMessage());
			save(taskHousing);
		}
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
