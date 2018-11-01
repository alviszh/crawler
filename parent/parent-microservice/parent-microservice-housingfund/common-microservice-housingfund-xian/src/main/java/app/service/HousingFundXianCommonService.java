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
import com.microservice.dao.entity.crawler.housing.xian.HousingXianBasic;
import com.microservice.dao.entity.crawler.housing.xian.HousingXianDetail;
import com.microservice.dao.entity.crawler.housing.xian.HousingXianHtml;
import com.microservice.dao.repository.crawler.housing.xian.HousingXianBasicRepository;
import com.microservice.dao.repository.crawler.housing.xian.HousingXianDetailRepository;
import com.microservice.dao.repository.crawler.housing.xian.HousingXianHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundXianParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xian")
public class HousingFundXianCommonService extends HousingBasicService{
	@Autowired
	private HousingFundXianParser housingFundSongyuanParser;
	@Autowired
	private HousingXianBasicRepository accountRepository;
	@Autowired
	private HousingXianDetailRepository detailRepository;
	@Autowired
	private HousingXianHtmlRepository xianHtmlRepository;
	
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception {
		WebParam webParam = housingFundSongyuanParser.login(messageLoginForHousing, taskHousing);
		String html=webParam.getPage().getWebResponse().getContentAsString();
		System.out.println(html);
		if(null!=webParam){
			 if(webParam.getPage().getWebResponse().getContentAsString().contains("验证码输入有误")){
				tracer.addTag("action.xian.login.ERROR.CODE", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_CODE_ERROR.getDescription());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setPaymentStatus(104);
				save(taskHousing);
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("此内容仅供职工本人查询使用")){
				tracer.addTag("action.xian.login.SUCCESS", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				taskHousing.setPaymentStatus(200);
				taskHousing.setPassword(taskHousing.getPassword());
				save(taskHousing); 
			}else if(webParam.getPage().getWebResponse().getContentAsString().contains("账号或密码错误")){
				tracer.addTag("action.xian.login.ERROR.PWD", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_PWD_ERROR.getDescription());
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				taskHousing.setPaymentStatus(104);
				taskHousing.setPassword(taskHousing.getPassword());
				save(taskHousing); 
				
			}
		}
	}

	@Async
	public void getBasicInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingXianBasic> webParam =  housingFundSongyuanParser.getAccountInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("action.getAccountInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid("【个人基本信息】已采集完成！", 200, taskHousing.getTaskid());
				HousingXianHtml html = new HousingXianHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				xianHtmlRepository.save(html);
				accountRepository.save(webParam.getHousingXianBasic());
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Async
	public void getDetailInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingXianDetail>  webParam = housingFundSongyuanParser.getDetailInfo(messageLoginForHousing,taskHousing);
			if(null != webParam){
				tracer.addTag("xian.action.getDetail.SUCCESS", taskHousing.getTaskid());
				updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 200, taskHousing.getTaskid());
				HousingXianHtml  html = new HousingXianHtml();
				html.setHtml(webParam.getHtml());
				html.setTaskid(messageLoginForHousing.getTask_id());
				html.setUrl(webParam.getUrl());
				xianHtmlRepository.save(html);
				detailRepository.saveAll(webParam.getList());
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	
}
