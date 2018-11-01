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
import com.microservice.dao.entity.crawler.housing.yibin.HousingYibinHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.yibin.HousingYibinHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yibin.HousingYibinUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.unit.HousingFundYibinHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yibin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yibin")
public class HousingYibinService extends HousingBasicService implements ICrawler{

	public static final Logger log = LoggerFactory.getLogger(HousingYibinService.class);
	@Autowired
	private HousingYibinHtmlRepository housingYibinHtmlRepository;
	@Autowired
	private HousingYibinUserInfoRepository housingYibinUserInfoRepository;
	@Autowired
	private HousingFundYibinHtmlunit housingFundYibinHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingYibinService.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {
			webParam = housingFundYibinHtmlunit.login(messageLoginForHousing, taskHousing);
			if(webParam.isLogin){
					tracer.addTag("parser.housing.login.success", "登陆成功");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookies);
					save(taskHousing);		
					if (null !=webParam.getHtml()) {
						HousingYibinHtml housingYibinHtml = new HousingYibinHtml();
						housingYibinHtml.setPageCount(1);
						housingYibinHtml.setHtml(webParam.getHtml());
						housingYibinHtml.setType("userInfo");
						housingYibinHtml.setUrl(webParam.getUrl());
						housingYibinHtml.setTaskid(taskHousing.getTaskid());
						housingYibinHtmlRepository.save(housingYibinHtml);		
					}	
					if (null !=webParam.getUserInfo()) {
						housingYibinUserInfoRepository.save(webParam.getUserInfo());					
						tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
						taskHousing.setUserinfoStatus(200);
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
						taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getError_code());
						save(taskHousing);					
					}else{
						tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
						taskHousing.setUserinfoStatus(201);
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
						taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
						taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getError_code());
						save(taskHousing);
					}
				}else{
					tracer.addTag("parser.housing.login.fail", "登陆失败 "+webParam.alertMsg);
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(webParam.getAlertMsg());
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
		taskHousing=updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}