package app.service;

import java.net.URL;

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
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoBasic;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoHtml;
import com.microservice.dao.entity.crawler.housing.jiaozuo.HousingJiaoZuoPay;
import com.microservice.dao.repository.crawler.housing.jiaozuo.HousingJiaoZuoBasicRepository;
import com.microservice.dao.repository.crawler.housing.jiaozuo.HousingJiaoZuoHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jiaozuo.HousingJiaoZuoPayRepository;

import app.common.WebParam;
import app.parser.HousingFundJiaoZuoParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jiaozuo")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jiaozuo")
public class HousingFundJiaoZuoCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundJiaoZuoParser housingFundJiaozuoParser;
	@Autowired
	private HousingJiaoZuoHtmlRepository htmlRepository;
	@Autowired
	private HousingJiaoZuoBasicRepository basicRepository;
	@Autowired
	private HousingJiaoZuoPayRepository payRepository;
	
	public void loginByIdCard(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing){
		
		try {
			WebParam webParam = housingFundJiaozuoParser.loginByIdCard(messageLoginForHousing,taskHousing);
				if(webParam.getHtml().contains("个人密码错误")){
				tracer.addTag("parser.login.ERROR.PASSWORD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(102);
				save(taskHousing);
			}
			else if(webParam.getHtml().contains("验证码错误")){
				tracer.addTag("parser.login.ERROR.CODE", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(106);
				save(taskHousing);
			}
			else if(webParam.getHtml().contains("缴存信息"))
			{
				tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				taskHousing.setPaymentStatus(200);
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing); 
			}else {
				tracer.addTag("parser.login.ERROR.IDNUM", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUM_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(106);
				save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getBasicInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
		WebParam<HousingJiaoZuoBasic> webParam =housingFundJiaozuoParser.getBasicInfo(messageLoginForHousing,taskHousing);
		
		if(null != webParam){
			tracer.addTag("action.getBasicInfo.SUCCESS", taskHousing.getTaskid());
			updateUserInfoStatusByTaskid("【个人基本信息】已采集完成！", 200, taskHousing.getTaskid());
			HousingJiaoZuoHtml  html = new HousingJiaoZuoHtml();
			html.setHtml(webParam.getHtml());
			html.setTaskid(messageLoginForHousing.getTask_id());
			html.setUrl(webParam.getUrl());
			htmlRepository.save(html);
			basicRepository.save(webParam.getHousingJiaoZuoBasic());
			updateTaskHousing(taskHousing.getTaskid());
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getPayInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
		WebParam<HousingJiaoZuoPay> webParam=housingFundJiaozuoParser.getPayInfo(messageLoginForHousing,taskHousing);
	
		if(null!=webParam){
			tracer.addTag("action.getPayInfo.SUCCESS", taskHousing.getTaskid());
			updatePayStatusByTaskid("【个人缴存信息】已采集完成！", 200, taskHousing.getTaskid());
			HousingJiaoZuoHtml  html = new HousingJiaoZuoHtml();
			html.setHtml(webParam.getHtml());
			html.setUrl(webParam.getUrl());
			htmlRepository.save(html);
			payRepository.save(webParam.getHousingJiaoZuoPay());
			updateTaskHousing(taskHousing.getTaskid());
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
