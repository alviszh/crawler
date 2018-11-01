package app.service;

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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhangzhou.HousingZhangZhouAccount;
import com.microservice.dao.entity.crawler.housing.zhangzhou.HousingZhangZhouDetail;
import com.microservice.dao.entity.crawler.housing.zhangzhou.HousingZhangZhouHtml;
import com.microservice.dao.repository.crawler.housing.zhangzhou.HousingZhangZhouAccountRepository;
import com.microservice.dao.repository.crawler.housing.zhangzhou.HousingZhangZhouDetailRepository;
import com.microservice.dao.repository.crawler.housing.zhangzhou.HousingZhangZhouHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundZhangzhouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.zhangzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.zhangzhou")
public class HousingFundZhangzhouCommonService extends HousingBasicService{
	@Autowired
	private HousingFundZhangzhouParser housingFundZhangzhouParser;
	@Autowired
	private HousingZhangZhouAccountRepository accountRepository;
	@Autowired
	private HousingZhangZhouDetailRepository detailRepository;
	@Autowired
	private HousingZhangZhouHtmlRepository htmlRepository;
	
	
	
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception {
		WebParam webParam = housingFundZhangzhouParser.login(messageLoginForHousing, taskHousing);
		if(null!=webParam){
			if(webParam.getPage().getWebResponse().getContentAsString().contains("该用户不存在")){
				tracer.addTag("action.zhangzhou.login.ERROR.FUND", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CARD_FUND_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("验证有错误")){
				tracer.addTag("action.zhangzhou.login.ERROR.CODE", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("个人账户明细")){
				tracer.addTag("action.zhangzhou.login.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				taskHousing.setPaymentStatus(200);
				taskHousing.setPassword(taskHousing.getPassword());
				save(taskHousing); 
			}
		}
	}


	public void getBasicInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingZhangZhouAccount> webParam =  housingFundZhangzhouParser.getAccountInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("action.getAccountInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【个人账户信息】已采集完成！", 200, taskHousing.getTaskid());
				HousingZhangZhouHtml html = new HousingZhangZhouHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				accountRepository.save(webParam.getHousingZhangzhouAccount());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	public void getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingZhangZhouDetail>  webParam = housingFundZhangzhouParser.getDetailInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("zhangzhou.action.getDetail.SUCCESS", taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 200, taskHousing.getTaskid());
				HousingZhangZhouHtml  html = new HousingZhangZhouHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				detailRepository.saveAll(webParam.getList());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
