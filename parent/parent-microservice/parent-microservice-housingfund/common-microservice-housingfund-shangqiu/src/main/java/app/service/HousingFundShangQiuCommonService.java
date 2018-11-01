package app.service;

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
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuBasic;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuDetail;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuHtml;
import com.microservice.dao.repository.crawler.housing.shangqiu.HousingShangqiuBasicRepository;
import com.microservice.dao.repository.crawler.housing.shangqiu.HousingShangqiuDetailRepository;
import com.microservice.dao.repository.crawler.housing.shangqiu.HousingShangqiuHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundShangQiuParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shangqiu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shangqiu")
public class HousingFundShangQiuCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundShangQiuParser housingFundShangqiuParser;
	@Autowired
	private HousingShangqiuHtmlRepository htmlRepository;
	@Autowired
	private HousingShangqiuBasicRepository basicRepository;
	@Autowired
	private HousingShangqiuDetailRepository detailRepository;
	
	
	
	public void loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundShangqiuParser.login(messageLoginForHousing,taskHousing);
			if(webParam.getPage().getWebResponse().getContentAsString().contains("用户名或密码不正确")){
				tracer.addTag("parser.login.ERROR.NUMORPASSWORD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(102);
				save(taskHousing);
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("验证码不正确")){
				tracer.addTag("parser.login.ERROR.CODE", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(106);
				save(taskHousing);
			}else{
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


	@Async
	public void getBasicInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingShangQiuBasic> webParam = housingFundShangqiuParser.getBasicInfo(messageLoginForHousing,taskHousing);
			if(null!=webParam){
				tracer.addTag("action.getBasicInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【个人基本信息】已采集完成！", 200, taskHousing.getTaskid());
				HousingShangQiuHtml html = new HousingShangQiuHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				htmlRepository.save(html);
				basicRepository.save(webParam.getHousingShangQiuBasic());
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Async
	public void getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingShangQiuDetail> webParam = housingFundShangqiuParser.getDetailInfo(messageLoginForHousing,taskHousing);
			tracer.addTag("xian.action.getDetail.SUCCESS", taskHousing.getTaskid());
			updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 200, taskHousing.getTaskid());
			HousingShangQiuHtml  html = new HousingShangQiuHtml();
			html.setHtml(webParam.getHtml());
			html.setTaskid(messageLoginForHousing.getTask_id());
			html.setUrl(webParam.getUrl());
			htmlRepository.save(html);
			detailRepository.saveAll(webParam.getList());
			updateTaskHousing(taskHousing.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
