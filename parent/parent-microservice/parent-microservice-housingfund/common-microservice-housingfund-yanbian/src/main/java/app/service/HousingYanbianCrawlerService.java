package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yanbian.HousingYanbianHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.yanbian.HousingYanbianHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yanbian.HousingYanbianPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.yanbian.HousingYanbianUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.unit.HousingFundYanbianHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yanbian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yanbian")
public class HousingYanbianCrawlerService extends HousingBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(HousingYanbianCrawlerService.class);
	@Autowired
	private HousingYanbianHtmlRepository housingYanbianHtmlRepository;
	@Autowired
	private HousingYanbianPaydetailsRepository housingYanbianPaydetailsRepository;
	@Autowired
	private HousingYanbianUserInfoRepository housingYanbianUserInfoRepository;
	@Autowired
	private HousingFundYanbianHtmlunit housingFundYanbianHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private HousingYanbianService  housingYanbianService;
	@Autowired
	private TracerLog tracer;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {		
		tracer.addTag("action.HousingYanbianService.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {		
			webParam = housingFundYanbianHtmlunit.login(messageLoginForHousing, taskHousing);
			if(null != webParam){
				if(webParam.isLogin){
					tracer.addTag("action.HousingYanbianService.login.success", "登陆成功");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookies);
					save(taskHousing);		
					return taskHousing;
				}else if(webParam.getHtmlPage().asXml().contains("登陆没有成功请核对身份证号码和密码")){
					tracer.addTag("action.HousingYanbianService.login.fail", "登陆失败 账户错误");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}else{
					tracer.addTag("action.HousingYanbianService.login.fail", "登陆失败");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}
			}else{
				tracer.addTag("action.HousingYanbianService.login.fail2", "登录时页面发生异常");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("登录异常，请您稍后重试。");
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
				save(taskHousing);
				return taskHousing;
			}			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("action.HousingYanbianService.login.error", e.toString());
			tracer.addTag("action.HousingYanbianService.login.fail3", "登录异常");
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录异常，请您稍后重试。");
			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
			save(taskHousing);
			return taskHousing;
		}
	}
	
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingYanbianService.getUserInfo(messageLoginForHousing);
		housingYanbianService.getPaydetails(messageLoginForHousing);
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}