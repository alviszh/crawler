package app.service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.leshan.HousingLeShanPay;
import com.microservice.dao.entity.crawler.housing.leshan.HousingLeShanUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.leshan.HousingLeShanPayRepository;
import com.microservice.dao.repository.crawler.housing.leshan.HousingLeShanUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.HousingfundLeShanParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.leshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.leshan")
public class HousingfundLeShanUnitService extends HousingBasicService{

	@Autowired
	private TracerLog tracer;

	@Autowired
	public TaskHousingRepository taskHousingRepository;

	@Autowired
	public HousingfundLeShanParser housingfundLeShanParser;

	@Autowired
	private HousingLeShanUserInfoRepository housingLeShanUserInfoRepository;

	@Autowired
	private HousingLeShanPayRepository housingLeShanPayRepository;
	/***
	 * 个人基本信息
	 * @param messageLogin
	 * @param taskHousing
	 */
	public void Userinfo(MessageLoginForHousing messageLogin, TaskHousing taskHousing) {
		try {
			tracer.addTag("HousingfundLeShanUnitService.Userinfo", taskHousing.getTaskid());
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> i = set.iterator();
			while(i.hasNext()){
				webClient.getCookieManager().addCookie(i.next());
			}
			String url = "http://wangt.lszfgjj.gov.cn:7009/netface/per/queryPerInfo.do";
			Page page = getHtml(url, webClient);
			String html = page.getWebResponse().getContentAsString();
			if(html.indexOf("个人基本信息导出")!=-1){
				HousingLeShanUserInfo user = housingfundLeShanParser.getuserinfo(html,messageLogin.getTask_id());
				if(user==null){
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
					save(taskHousing);
					updateUserInfoStatusByTaskid("数据采集中，个人信息已采集完成", 201,taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息无数据");
				}else{
					housingLeShanUserInfoRepository.save(user);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
					save(taskHousing);
					updateUserInfoStatusByTaskid("数据采集中，个人明细账信息已采集完成", 200,taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
				save(taskHousing);
				updateUserInfoStatusByTaskid("数据采集中，个人明细账信息已采集完成", 500,taskHousing.getTaskid());
				tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
			}
		}catch (Exception e) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			save(taskHousing);
			updateUserInfoStatusByTaskid("数据采集中，公积金个人明细账信息网页出问题", 404,taskHousing.getTaskid());
		}


	}
	/***
	 * 流水
	 * @param messageLogin
	 * @param taskHousing
	 */
	public void PayStatus(MessageLoginForHousing messageLogin, TaskHousing taskHousing) {
		try {
			tracer.addTag("HousingfundLeShanUnitService.PayStatus", taskHousing.getTaskid());
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> i = set.iterator();
			while(i.hasNext()){
				webClient.getCookieManager().addCookie(i.next());
			}
			LocalDate today = LocalDate.now();
			String url = "http://wangt.lszfgjj.gov.cn:7009/netface/per/queryPerDeposit!queryPerByYear.do?"
					+"dto%5B'startdate'%5D=2000-01-01"
					+ "&dto%5B'enddate'%5D="+today
					+ "&gridInfo%5B'dataList_limit'%5D=1000"
					+ "&gridInfo%5B'dataList_start'%5D=0&";

			Page page = gethtmlPost(webClient, null, url);
			String contentAsString = page.getWebResponse().getContentAsString();
			if(contentAsString.indexOf("lists")!=-1){
				List<HousingLeShanPay> list = housingfundLeShanParser.getpaymsg(messageLogin.getTask_id(),contentAsString);
				if(list!=null){
					housingLeShanPayRepository.saveAll(list);
					taskHousing.setPaymentStatus(200);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_AccountMsgStatus_SUCESS.getCode());
					save(taskHousing);
				}else{
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
					taskHousing.setPaymentStatus(201);
					save(taskHousing);
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
				taskHousing.setPaymentStatus(500);
				save(taskHousing);
			}


		} catch (Exception e) {
			tracer.addTag("HousingfundLeShanUnitService.PayStatus.parser", messageLogin.getTask_id());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
			taskHousing.setPaymentStatus(404);
			save(taskHousing);
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
}
