package app.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tangshan.HousingTangShanPay;
import com.microservice.dao.entity.crawler.housing.tangshan.HousingTangShanUserInfo;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.tangshan.HousingTangShanPayRepository;
import com.microservice.dao.repository.crawler.housing.tangshan.HousingTangShanUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.commontracerlog.TracerLog;
import app.parser.HousingFundTangShanParser;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import net.sf.json.JSONObject;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tangshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tangshan")
public class HousingfundtanshanUnitService extends HousingBasicService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Autowired
	public HousingTangShanUserInfoRepository housingTangShanUserInfoRepository;
	@Autowired
	public HousingFundTangShanParser housingFundTangShanParser;
	@Autowired
	private HousingTangShanPayRepository housingTangShanPayRepository;
	@Value("${loginip}") 
	public String loginip;
	public Object Userinfo(MessageLoginForHousing messageLogin, TaskHousing taskHousing){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		String _csrf = taskHousing.getError_message();
		String url = "http://"+loginip+"/olbh/qryPersonInfo.do?"
				+ "_csrf="+_csrf;

		try {
			Page page = LoginAndGetUnit.gethtmlPost(webClient, null, url);
			String html = page.getWebResponse().getContentAsString();
			if(html==null){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
				save(taskHousing);
				return null;
			}
			HousingTangShanUserInfo userinfo = housingFundTangShanParser.getuserinfo(html);

			if(userinfo==null){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
				save(taskHousing);
				updateUserInfoStatusByTaskid("数据采集中，个人信息已采集完成", 201,taskHousing.getTaskid());
				tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息无数据");
			}
			else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
				save(taskHousing);

				userinfo.setTaskid(taskHousing.getTaskid());
				userinfo.setUserid(messageLogin.getUser_id());
				save(userinfo);
				updateUserInfoStatusByTaskid("数据采集中，个人明细账信息已采集完成", 200,taskHousing.getTaskid());
				tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
			}
			return null;

		}catch (Exception e) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			save(taskHousing);
			updateUserInfoStatusByTaskid("数据采集中，公积金个人明细账信息网页出问题", 404,taskHousing.getTaskid());
			return null;
		}

	}
	public Object PayStatus(MessageLoginForHousing messageLogin, TaskHousing taskHousing){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		try {

			String today = LocalDate.now().toString();
			String url = "http://"+loginip+"/olbh/qryPersonAccDetails.do?"
					+ "_csrf="+taskHousing.getError_message()
					+ "&begDate=2000-01-01"
					+ "&currentPage=1"
					+ "&endDate="+today
					+ "&limits=500";
			Page page = LoginAndGetCommon.gethtmlPost(webClient, null, url);
			String html = page.getWebResponse().getContentAsString();

			if(html.length()<1){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
				taskHousing.setPaymentStatus(201);
				tracer.addTag("parser.crawler.getPayStatus", "数据采集中，用户流水页面修改");
				save(taskHousing);
			}
			List<HousingTangShanPay> list = housingFundTangShanParser.getpay(html,messageLogin.getTask_id());

			if(list!=null){
				housingTangShanPayRepository.saveAll(list);
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
				tracer.addTag("parser.crawler.getPayStatus", "数据采集中，用户流水无数据");
			}
			updateTaskHousing(taskHousing.getTaskid());
			return null;
		} catch (Exception e) {
			// TODO: handle exception.
			updateUserInfoStatusByTaskid("数据采集中，公积金个人明细账网页出问题", 404,taskHousing.getTaskid());
			return null;
		}
	}
	public void save(TaskHousing taskHousing){
		taskHousingRepository.save(taskHousing);
	}
	private void save(HousingTangShanUserInfo result) {
		housingTangShanUserInfoRepository.save(result);
	}

}
