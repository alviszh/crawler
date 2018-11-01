package app.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouAccountDetail;
import com.microservice.dao.entity.crawler.housing.suzhou.HousingSuzhouHtml;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouAccount;
import com.microservice.dao.repository.crawler.housing.suzhou.HousingSuzhouAccountRepository;
import com.microservice.dao.repository.crawler.housing.suzhou.HousingSuzhouBasicRepository;
import com.microservice.dao.repository.crawler.housing.suzhou.HousingSuzhouHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundSuzhouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.suzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.suzhou")
public class HousingFundSuzhouCommonService extends HousingBasicService{

	@Autowired
	private HousingSuzhouBasicRepository housingSuzhouBasicRepository;
	@Autowired
	private HousingSuzhouAccountRepository housingSuzhouAccountRepository;
	@Autowired
	private HousingFundSuzhouParser housingFundSuzhouParser;
	@Autowired
	private HousingSuzhouHtmlRepository htmlSuzhouRepository;
	
	public void loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){
		
		try {
			WebParam webParam = housingFundSuzhouParser.loginByIdCard(messageLoginForHousing,taskHousing);
				if(webParam.getHtml().contains("公积金账号与证件不匹配")){
				tracer.addTag("action.login.ERROR.PASSWORD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getDescription());
				taskHousing.setPaymentStatus(102);
				save(taskHousing);
			}
			else if(webParam.getHtml().contains("验证码输入错误，请重新登录")){
				tracer.addTag("action.login.ERROR.IDNUM", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_PASSWORD_ERROR.getDescription());
				taskHousing.setPaymentStatus(106);
				save(taskHousing);
			}
			else if(webParam.getHtml().contains("欢迎您")){
				tracer.addTag("action.login.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setPassword(webParam.getUrl());
				taskHousing.setCookies(cookieString);
				save(taskHousing); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Async
	public void getAccountInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingXuzhouAccount> webParam = housingFundSuzhouParser.getBasicInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("action.getAccountInfo.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPaymentStatus(200);
				HousingSuzhouHtml HousingSuzhouHtml = new HousingSuzhouHtml();
				HousingSuzhouHtml.setHtml(webParam.getHtml());
				HousingSuzhouHtml.setPagenumber(1);
				HousingSuzhouHtml.setTaskid(messageLoginForHousing.getTask_id());
				HousingSuzhouHtml.setUrl(webParam.getUrl());
				htmlSuzhouRepository.save(HousingSuzhouHtml);
				housingSuzhouBasicRepository.save(webParam.getHousingSuzhouAccount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Async
	public void getDetail(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingSuzhouAccountDetail> webParam = housingFundSuzhouParser.getDetailInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("action.getAccountInfo.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPaymentStatus(200);
				HousingSuzhouHtml housingXuzhouHtml = new HousingSuzhouHtml();
				housingXuzhouHtml.setHtml(webParam.getHtml());
				housingXuzhouHtml.setPagenumber(1);
				housingXuzhouHtml.setTaskid(messageLoginForHousing.getTask_id());
				housingXuzhouHtml.setUrl(webParam.getUrl());
				htmlSuzhouRepository.save(housingXuzhouHtml);
				housingSuzhouAccountRepository.saveAll(webParam.getList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
