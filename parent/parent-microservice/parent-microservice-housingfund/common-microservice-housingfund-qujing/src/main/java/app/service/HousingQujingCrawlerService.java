package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;

import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.unit.HousingFundQujingHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.qujing")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.qujing")
public class HousingQujingCrawlerService extends HousingBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(HousingQujingCrawlerService.class);
	@Autowired
	private HousingQujingService housingQujingService;
	@Autowired
	private HousingFundQujingHtmlunit housingFundQujingHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingQujingService.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());		
		try {
			int count=0;
			WebParam webParam = housingFundQujingHtmlunit.login(messageLoginForHousing, taskHousing,count);
			if(null != webParam){
				if(webParam.getText().contains("登陆成功")){
					tracer.addTag("parser.housing.login.success", "登陆成功");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookies);
					save(taskHousing);				
				}else if(webParam.getText().contains("个人公积金帐号不能为空") || webParam.getText().contains("公积金账号位数不正确")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 个人公积金帐号错误或者为空");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("个人公积金帐号错误或者为空");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}else if(webParam.getText().contains("个人身份证号不能为空")||webParam.getText().contains("身份证号码位数不对")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 身份证号为空或者身份证号位数不对");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("身份证号为空或者身份证号位数不对");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}else if(webParam.getText().contains("验证码错误")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 验证码错误错误");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("输入的验证码错误");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}else{
					tracer.addTag("parser.housing.login.fail", "登陆失败");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("登陆失败");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}
			}else{
				tracer.addTag("parser.housing.login.fail2", "登录时页面发生异常");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("登录异常，请您稍后重试。");
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
				save(taskHousing);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.housing.login.error", e.toString());
			tracer.addTag("parser.housing.login.fail3", "登录异常");
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录异常，请您稍后重试。");
			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
			save(taskHousing);
		}		
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingQujingService.getUserInfo(messageLoginForHousing, taskHousing);
		housingQujingService.getPaydetails(messageLoginForHousing, taskHousing);
		taskHousing =taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}