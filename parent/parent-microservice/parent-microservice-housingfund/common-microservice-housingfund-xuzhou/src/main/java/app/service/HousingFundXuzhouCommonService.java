package app.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouAccount;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouDetail;
import com.microservice.dao.entity.crawler.housing.xuzhou.HousingXuzhouHtml;
import com.microservice.dao.repository.crawler.housing.xuzhou.HousingXuzhouAccountRepository;
import com.microservice.dao.repository.crawler.housing.xuzhou.HousingXuzhouDetailRepository;
import com.microservice.dao.repository.crawler.housing.xuzhou.HousingXuzhouHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundXuzhouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xuzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xuzhou")
public class HousingFundXuzhouCommonService extends HousingBasicService{

	@Autowired
	private HousingXuzhouDetailRepository housingXuzhouDetailRepository;
	@Autowired
	private HousingXuzhouAccountRepository housingXuzhouAccountRepository;
	@Autowired
	private HousingXuzhouHtmlRepository housingXuzhouHtmlRepository;
	@Autowired
	private HousingFundXuzhouService housingFundNingboService;
	@Autowired
	private HousingFundXuzhouParser housingFundXuzhouParser;
	
	public void loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){
		
		try {
			WebParam webParam = housingFundXuzhouParser.loginByIdCard(messageLoginForHousing,taskHousing);
			URL url  = webParam.getPage().getUrl();
			String s = String.valueOf(url);
			String a = s.substring(s.indexOf("p="));
			String html = webParam.getPage().getWebResponse().getContentAsString();
//			if(null ==html)
//			{
//				tracer.addTag("parser.login.ERROR", taskHousing.getTaskid());
//				taskHousing.setPhase(HousingFundStatusCode.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
//				taskHousing.setPhase_status(HousingFundStatusCode.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
//				taskHousing.setDescription(HousingFundStatusCode.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
//				taskHousing.setError_code(HousingFundStatusCode.HOUSING_LOGIN_IDNUMORPWD_ERROR.getError_code());
//				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
//				taskHousing.setPaymentStatus(104);
//				save(taskHousing); 
//			}	
//			else 
				if(webParam.getPage().getWebResponse().getContentAsString().contains("您的公积金账号查询不到账单信息")){
				tracer.addTag("parser.login.ERROR.PASSWORD", taskHousing.getTaskid());
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(102);
				save(taskHousing);
			}
			else if(webParam.getPage().getWebResponse().getContentAsString().contains("您输入的身份证号有误")){
				tracer.addTag("parser.login.ERROR.IDNUM", taskHousing.getTaskid());
				taskHousing.setPhase(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(106);
				save(taskHousing);
			}
			else if(webParam.getPage().getWebResponse().getContentAsString().contains("所在单位"))
			{
				tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				taskHousing.setPaymentStatus(200);
				taskHousing.setPassword(a);
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
			WebParam<HousingXuzhouAccount> webParam = housingFundXuzhouParser.getAccountInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("parser.getAccountInfo.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPaymentStatus(200);
				HousingXuzhouHtml housingXuzhouHtml = new HousingXuzhouHtml();
				housingXuzhouHtml.setHtml(webParam.getHtml());
				housingXuzhouHtml.setPagenumber(1);
				housingXuzhouHtml.setTaskid(messageLoginForHousing.getTask_id());
				housingXuzhouHtml.setUrl(webParam.getUrl());
				housingXuzhouHtmlRepository.save(housingXuzhouHtml);
				housingXuzhouAccountRepository.save(webParam.getHousingXuzhouAccount());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Async
	public void getDetail(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingXuzhouDetail> webParam = housingFundXuzhouParser.getDetailInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("parser.getAccountInfo.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPaymentStatus(200);
				HousingXuzhouHtml housingXuzhouHtml = new HousingXuzhouHtml();
				housingXuzhouHtml.setHtml(webParam.getHtml());
				housingXuzhouHtml.setPagenumber(1);
				housingXuzhouHtml.setTaskid(messageLoginForHousing.getTask_id());
				housingXuzhouHtml.setUrl(webParam.getUrl());
				housingXuzhouHtmlRepository.save(housingXuzhouHtml);
				housingXuzhouDetailRepository.saveAll(webParam.getList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
