package app.service;

import java.util.ArrayList;
import java.util.List;

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
import com.microservice.dao.repository.crawler.housing.jinhua.HousingJinHuaUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.unit.HousingFundJinhuaHtmlunit;


@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jinhua")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jinhua")
public class HousingJinhuaCrawlerService extends HousingBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(HousingJinhuaCrawlerService.class);
	@Autowired
	private HousingJinhuaService housingJinhuaService;
	@Autowired
	private HousingFundJinhuaHtmlunit housingFundJinhuaHtmlunit;
	@Autowired
	private HousingJinHuaUserInfoRepository housingJinHuaUserInfoRepository;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	private List<String> urlList = new ArrayList<>();
	@Override
	public TaskHousing login( MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("parser.HousingJinhuaService.login.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {
			int count=0;
			webParam = housingFundJinhuaHtmlunit.login(messageLoginForHousing, taskHousing,count);
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
					urlList=webParam.getUrlList();
					if (null !=webParam.getUserInfos()) {
						housingJinHuaUserInfoRepository.saveAll(webParam.getUserInfos());		
						tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfos());
						updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
								200 , taskHousing.getTaskid());
					}else{
						tracer.addTag("parser.housing.crawler.getUserInfo", "用户信息为空");
						updateUserInfoStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
								201 , taskHousing.getTaskid());
					}
				}else if(webParam.getText().contains("用户名、密码是否正确")||webParam.getText().contains("密码错误")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 用户名、密码是否正确");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription("用户名、密码是否正确");
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}else if(webParam.getText().contains("验证码不正确")){
					tracer.addTag("parser.housing.login.fail", "登陆失败 验证码不正确");
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
		tracer.addTag("parser.HousingJinhuaService.getAllData.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		housingJinhuaService.getPaydetails(messageLoginForHousing,urlList);
		return null;
	}
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}