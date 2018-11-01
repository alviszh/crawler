package app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.microservice.dao.entity.crawler.housing.xiamen.HousingXiamenUserInfo;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.xiamen.HousingXiamenUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.unit.HousingFundXiamenHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.xiamen")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.xiamen")
public class HousingXiamenCrawlerService extends HousingBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(HousingXiamenCrawlerService.class);
	@Autowired
	private HousingXiamenService housingXiamenService;
	@Autowired
	private HousingFundXiamenHtmlunit housingFundXiamenHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;	
	@Autowired
	private HousingXiamenUserInfoRepository housingXiamenUserInfoRepository;
	@Autowired
	private TracerLog tracer;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingXiamenService.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());	
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {
			int count=0;
			webParam = housingFundXiamenHtmlunit.login(messageLoginForHousing,count);
			if(null != webParam){
				if(webParam.isLogin){
					tracer.addTag("parser.housing.login.success", "登陆成功");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookies);
					save(taskHousing);		
					return taskHousing;
				}else if(webParam.getHtmlPage().asXml().contains("您的身份证信息未通过系统校验")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 账户错误");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("输入的账号错误");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}else if(webParam.getHtmlPage().asXml().contains("您输入的密码有误")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 密码错误");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("输入的密码错误");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}else if(webParam.getHtmlPage().asXml().contains("您输入的验证码有误")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 验证码错误错误");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("输入的验证码错误");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}
			}else{
				tracer.addTag("parser.housing.login.fail2", "登录时页面发生异常");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("登录异常，请您稍后重试。");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				save(taskHousing);
			}			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.housing.login.error", e.toString());
			tracer.addTag("parser.housing.login.fail3", "登录异常");
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录异常，请您稍后重试。");
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
			save(taskHousing);
			return taskHousing;
		}		
		return taskHousing;
	}
	
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());	
		housingXiamenService.getUserInfo(messageLoginForHousing);
		HousingXiamenUserInfo userInfo=housingXiamenUserInfoRepository.findTopByTaskid(messageLoginForHousing.getTask_id());
		if (null !=userInfo) {
			housingXiamenService.getPaydetails(messageLoginForHousing, userInfo.getAccountnum());
		}
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}