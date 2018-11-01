package app.service;

import java.io.IOException;
import java.util.ArrayList;
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

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chuzhou.HousingChuZhouPay;
import com.microservice.dao.entity.crawler.housing.chuzhou.HousingChuZhouUserinfo;
import com.microservice.dao.repository.crawler.housing.chuzhou.HousingChuZhouPayRepository;
import com.microservice.dao.repository.crawler.housing.chuzhou.HousingChuZhouUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.crawler.htmlparse.HousingCZParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chuzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chuzhou")
public class HousingChuZhouFutureService extends HousingBasicService implements ICrawlerLogin,ICrawler{
	public static final Logger log = LoggerFactory.getLogger(HousingChuZhouFutureService.class);
	
	@Autowired
	private LoginAndGetService loginAndGetService;
	@Autowired
	private HousingChuZhouUserInfoRepository housingChuZhouUserInfoRepository;
	@Autowired
	private HousingChuZhouPayRepository housingChuZhouPayRepository;
	private HtmlPage htmlpage;
//	private WebParam webParam = new WebParam();;
	@Async
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String url = "http://gjjcx.chuzhou.gov.cn/default.aspx";
		htmlpage = null;
//		String htmlsource2 = null;
		try {
			if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getIDNUM())){
				//身份证
				 htmlpage = loginAndGetService.loginByIDNUM(webClient, url,  messageLoginForHousing.getUsername().trim(), messageLoginForHousing.getPassword().trim());
			}else if(messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getACCOUNT_NUM())){
				// 个人账号
				htmlpage = loginAndGetService.loginByACCOUNT_NUM(webClient, url,  messageLoginForHousing.getCountNumber().trim(), messageLoginForHousing.getNum().trim(),messageLoginForHousing.getUsername().trim());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
//			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("登陆异常", e.getMessage());
			save(taskHousing);
			return taskHousing;
		}
		if(htmlpage!=null){
			String html = htmlpage.getWebResponse().getContentAsString();
//			System.out.println("html"+html);
			tracer.addTag("登陆html", html);
//			webParam.setHtmlPage1(htmlpage);
			if(html.contains("摘要")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

				taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
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
//			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("parser.login.auth", null);
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
			String html = htmlpage.getWebResponse().getContentAsString();
			List<String> list = new ArrayList<String>();
			if (messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getIDNUM())) {

				list.add(html);
				Document doc = Jsoup.parse(html);
				Elements ele1 = doc.select("#GridView2 > tbody > tr > td  > a");
				if (ele1.size() > 1) {
					for (int i = 1; i < ele1.size(); i++) {
						HtmlElement button1 = htmlpage
								.getFirstByXPath("//*[@id='GridView2']/tbody/tr[" + (i + 2) + "]/td[6]/a");
						try {
							htmlpage = button1.click();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						html = htmlpage.getWebResponse().getContentAsString();
						list.add(html);
					}
					for (String url_result : list) {
						tracer.addTag("个人信息和流水html", url_result);
						htmlparse(url_result, taskHousing);
					}
				}
			} else if (messageLoginForHousing.getLogintype().equals(StatusCodeLogin.getACCOUNT_NUM())) {
				tracer.addTag("个人信息和流水html", html);
				htmlparse(html, taskHousing);
			} 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR2.getDescription());
//			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//			tracer.addTag("e", "baotougangjijin");
			tracer.addTag("爬取异常", e.getMessage());
			save(taskHousing);
		}
		return taskHousing;
	}
	
	public void htmlparse(String url_result ,TaskHousing taskHousing){
		HousingChuZhouUserinfo userinfo = HousingCZParse.userinfo_parse(url_result);
		if(userinfo==null){
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
		}else{
			userinfo.setTaskid(taskHousing.getTaskid());
			housingChuZhouUserInfoRepository.save(userinfo);
			taskHousing.setUserinfoStatus(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
		}
		
		List<HousingChuZhouPay> listresul = HousingCZParse.paydetails_parse(url_result,taskHousing);
		if(listresul==null){
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		}else{
			
			housingChuZhouPayRepository.saveAll(listresul);
			taskHousing.setPaymentStatus(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
		}
			
			
		
		save(taskHousing);
		updateTaskHousing(taskHousing.getTaskid());
	}

	

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
