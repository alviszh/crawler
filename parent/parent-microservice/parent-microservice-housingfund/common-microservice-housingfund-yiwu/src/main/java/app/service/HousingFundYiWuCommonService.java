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
import com.microservice.dao.entity.crawler.housing.yiwu.HousingYiWuBasic;
import com.microservice.dao.entity.crawler.housing.yiwu.HousingYiWuDetail;
import com.microservice.dao.entity.crawler.housing.yiwu.HousingYiWuHtml;
import com.microservice.dao.repository.crawler.housing.yiwu.HousingYiWuBasicRepository;
import com.microservice.dao.repository.crawler.housing.yiwu.HousingYiWuDetailRepository;
import com.microservice.dao.repository.crawler.housing.yiwu.HousingYiWuHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundYiWuParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yiwu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yiwu")
public class HousingFundYiWuCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundYiWuParser housingFundYiwuParser;
	@Autowired
	private HousingYiWuHtmlRepository htmlRepository;
	@Autowired
	private HousingYiWuBasicRepository basicRepository;
	@Autowired
	private HousingYiWuDetailRepository detailRepository;
	
	public void loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){
		try {
			WebParam webParam = housingFundYiwuParser.loginByIdCard(messageLoginForHousing,taskHousing);
				if(webParam.getHtml().contains("登录失败，请重试")){
				tracer.addTag("parser.login.ERROR.NUMORPASSWORD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(102);
				save(taskHousing);
			}
			else if(webParam.getHtml().contains("请输入正确的验证码")){
				tracer.addTag("parser.login.ERROR.CODE", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(106);
				save(taskHousing);
			}
			else if(webParam.getHtml().contains("个人公积金信息查询")){
				tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				taskHousing.setPaymentStatus(200);
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getBasicInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingYiWuBasic> webParam =housingFundYiwuParser.getBasicInfo(messageLoginForHousing,taskHousing);
		
			if(null != webParam){
				tracer.addTag("action.getBasicInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【个人基本信息】已采集完成！", 200, taskHousing.getTaskid());
				HousingYiWuHtml  html = new HousingYiWuHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				basicRepository.save(webParam.getHousingYiWuBasic());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
		WebParam<HousingYiWuDetail> webParam=housingFundYiwuParser.getPayInfo(messageLoginForHousing,taskHousing);
	
		if(null!=webParam){
			tracer.addTag("action.getPayInfo.SUCCESS", taskHousing.getTaskid());
			updatePayStatusByTaskid("【个人公积金明细】已采集完成！", 200, taskHousing.getTaskid());
			HousingYiWuHtml  html = new HousingYiWuHtml();
			html.setHtml(webParam.getHtml());
			html.setUrl(webParam.getUrl());
			htmlRepository.save(html);
			detailRepository.saveAll(webParam.getList());
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
