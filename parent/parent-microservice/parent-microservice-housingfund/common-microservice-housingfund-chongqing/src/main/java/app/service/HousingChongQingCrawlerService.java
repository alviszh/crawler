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
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.unit.HousingFundChongQingHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chongqing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chongqing")
public class HousingChongQingCrawlerService extends HousingBasicService implements ICrawlerLogin{
	@Autowired
	private HousingChongQingService housingChongQingService;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private HousingFundChongQingHtmlunit  housingFundChongQingHtmlunit;
	@Autowired
	private TracerLog tracer;	
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		tracer.addTag("parser.HousingChongQingService.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {
			int count=0;
			webParam = housingFundChongQingHtmlunit.login(messageLoginForHousing,count);
			if(null != webParam){
				if(webParam.getHtmlPage().asXml().contains("会员中心")){
					tracer.addTag("parser.HousingChongQingService.login.success", "登陆成功");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookies);
					save(taskHousing);
					webParam.setLogin(true);				
				}else if(webParam.getHtmlPage().asXml().contains("验证码错误")){
					tracer.addTag("parser.HousingChongQingService.login.fail", "登陆失败 验证码错误");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					save(taskHousing);				
				}else if(webParam.getHtmlPage().asXml().contains("用户名或者密码错误")){
					tracer.addTag("parser.HousingChongQingService.login.fail", "登陆失败 用户名或者密码错误");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					save(taskHousing);				
				}
			}else{
				tracer.addTag("parser.HousingChongQingService.login.fail2", "登录时页面发生异常");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("登录异常，请您稍后重试。");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				save(taskHousing);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.HousingChongQingService.login.error", e.toString());
			tracer.addTag("parser.HousingChongQingService.login.fail3", "登录异常");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录异常，请您稍后重试。");
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
			save(taskHousing);
		}		
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("getAllData", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingChongQingService.getUserInfo(messageLoginForHousing);
		housingChongQingService.getAccountInfo( messageLoginForHousing);
		housingChongQingService.getPaydetails(messageLoginForHousing);
		taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		tracer.addTag("getAllData.end", taskHousing.toString());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {

		return null;
	}

	
}