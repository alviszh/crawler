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
import com.microservice.dao.entity.crawler.housing.chuxiong.HousingChuXiongHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.chuxiong.HousingChuXiongHtmlRepository;
import com.microservice.dao.repository.crawler.housing.chuxiong.HousingChuXiongPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.chuxiong.HousingChuXiongUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.unit.HousingFundChuXiongHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chuxiong")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chuxiong")
public class HousingChuXiongCrawlerService extends HousingBasicService implements ICrawler{

	public static final Logger log = LoggerFactory.getLogger(HousingChuXiongCrawlerService.class);
	@Autowired
	private HousingChuXiongHtmlRepository housingChuXiongHtmlRepository;
	@Autowired
	private HousingChuXiongPaydetailsRepository housingChuXiongPaydetailsRepository;
	@Autowired
	private HousingChuXiongUserInfoRepository housingChuXiongUserInfoRepository;
	@Autowired
	private HousingFundChuXiongHtmlunit housingFundChuXiongHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Override
	public TaskHousing getAllData( MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingChuXiongService.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam = new WebParam();
		try {
			webParam = housingFundChuXiongHtmlunit.login(messageLoginForHousing, taskHousing);
			if(null != webParam){
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
						HousingChuXiongHtml housingChuXiongHtml = new HousingChuXiongHtml();
						housingChuXiongHtml.setPageCount(1);
						housingChuXiongHtml.setHtml(webParam.getHtml());
						housingChuXiongHtml.setType("userInfo");
						housingChuXiongHtml.setUrl(webParam.getUrl());
						housingChuXiongHtml.setTaskid(taskHousing.getTaskid());
						housingChuXiongHtmlRepository.save(housingChuXiongHtml);		
					}			
					if (null !=webParam.getUserInfo()) {
						housingChuXiongUserInfoRepository.save(webParam.getUserInfo());					
						tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
						taskHousing.setUserinfoStatus(200);
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
						taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getError_code());
						save(taskHousing);
						updateTaskHousing(taskHousing.getTaskid());
					}else{
						tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
						taskHousing.setUserinfoStatus(201);
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
						taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription());
						taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getError_code());
						save(taskHousing);
						updateTaskHousing(taskHousing.getTaskid());
					}
					if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
						housingChuXiongPaydetailsRepository.saveAll(webParam.getPaydetails());
						tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
						taskHousing.setPaymentStatus(200);
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription("数据采集中，【流水信息】采集成功");
						taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getError_code());
						save(taskHousing);
						updateTaskHousing(taskHousing.getTaskid());
					}else{
						tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");
						taskHousing.setPaymentStatus(201);
						taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_CRAWLER_READ.getPhase());
						taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getPhasestatus());
						taskHousing.setDescription("数据采集中，【流水信息】采集成功");
						taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_ERROR.getError_code());
						save(taskHousing);
						updateTaskHousing(taskHousing.getTaskid());
					}					
					return taskHousing;
				}else if(webParam.getText().contains("身份证不能为空") || webParam.getText().contains("身份证错误")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 账户错误");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("输入的账号错误");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}else if(webParam.getText().contains("请认真检查是否姓名错误")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 密码错误或者用户名错误");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("密码错误或者用户名错误");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}else if(webParam.getText().contains("验证码不能为空") || webParam.getText().contains("验证码错误")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 验证码错误错误");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("输入的验证码错误");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}else{
					tracer.addTag("parser.housing.login.fail", "登陆失败");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("登陆失败");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
					return taskHousing;
				}
			}else{
				tracer.addTag("parser.housing.login.fail2", "登录时页面发生异常");
				taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
				taskHousing.setDescription("登录异常，请您稍后重试。");
				taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
				save(taskHousing);
				return taskHousing;
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
			return taskHousing;
		}		
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}