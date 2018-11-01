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
import com.microservice.dao.entity.crawler.housing.songyuan.HousingSongYuanAccount;
import com.microservice.dao.entity.crawler.housing.songyuan.HousingSongYuanHtml;
import com.microservice.dao.repository.crawler.housing.songyuan.HousingSongyuanAccountRepository;
import com.microservice.dao.repository.crawler.housing.songyuan.HousingSongyuanDetailRepository;
import com.microservice.dao.repository.crawler.housing.songyuan.HousingSongyuanHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundSongyuanParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.songyuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.songyuan")
public class HousingFundSongyuanCommonService extends HousingBasicService{
	@Autowired
	private HousingFundSongyuanParser housingFundSongyuanParser;
	@Autowired
	private HousingSongyuanAccountRepository accountRepository;
	@Autowired
	private HousingSongyuanDetailRepository detailRepository;
	@Autowired
	private HousingSongyuanHtmlRepository htmlRepository;
	
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception {
		WebParam webParam = housingFundSongyuanParser.login(messageLoginForHousing, taskHousing);
		if(null!=webParam){
			if(webParam.getPage().getWebResponse().getContentAsString().contains("请输入正确的身份证号")){
				tracer.addTag("action.songyuan.login.ERROR.NUM", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("验证码错误")){
				tracer.addTag("action.anshan.login.ERROR.NAMEORCARD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("单位代码")){
				tracer.addTag("action.songyuan.login.SUCCESS", taskHousing.getTaskid());
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


	public void getAccountInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingSongYuanAccount> webParam =  housingFundSongyuanParser.getAccountInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("action.getAccountInfo.SUCCESS", taskHousing.getTaskid());
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription(), 200,
						taskHousing.getTaskid());
				HousingSongYuanHtml html = new HousingSongYuanHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				accountRepository.saveAll(webParam.getList());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	public void getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam  webParam = housingFundSongyuanParser.getDetailInfo(messageLoginForHousing,taskHousing);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
