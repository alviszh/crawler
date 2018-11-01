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
import com.microservice.dao.entity.crawler.housing.panjin.HousingPanjinHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.panjin.HousingPanjinHtmlRepository;
import com.microservice.dao.repository.crawler.housing.panjin.HousingPanjinPaydetailsRepository;
import com.microservice.dao.repository.crawler.housing.panjin.HousingPanjinUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundPanjinHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.panjin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.panjin")
public class HousingPanjinService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingPanjinService.class);
	@Autowired
	private HousingPanjinHtmlRepository housingPanjinHtmlRepository;
	@Autowired
	private HousingPanjinPaydetailsRepository housingPanjinPaydetailsRepository;
	@Autowired
	private HousingPanjinUserInfoRepository housingPanjinUserInfoRepository;
	@Autowired
	private HousingFundPanjinHtmlunit housingFundPanjinHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	public WebParam login(WebClient webClient, MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("action.HousingPanjinService.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		webParam.setCode(0);
		try {
			int count=0;
			webParam = housingFundPanjinHtmlunit.login(messageLoginForHousing, taskHousing,count);
			if(webParam.getHtmlPage().asXml().contains("基本信息")){
					tracer.addTag("action.HousingPanjinService.login.success", "登陆成功");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_SUCCESS.getError_code());
					String cookies = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookies);
					save(taskHousing);			
					if (null !=webParam.getHtml()) {
						HousingPanjinHtml housingPanjinHtml = new HousingPanjinHtml();
						housingPanjinHtml.setPageCount(1);
						housingPanjinHtml.setHtml(webParam.getHtml());
						housingPanjinHtml.setType("userInfo");
						housingPanjinHtml.setUrl(webParam.getUrl());
						housingPanjinHtml.setTaskid(taskHousing.getTaskid());
						housingPanjinHtmlRepository.save(housingPanjinHtml);		
					}				
					if (null !=webParam.getUserInfo()) {
						housingPanjinUserInfoRepository.save(webParam.getUserInfo());
						tracer.addTag("action.housing.crawler.getUserInfo", "用户信息入库"+webParam.getUserInfo());
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
				}else if(webParam.getHtmlPage().asXml().contains("身份证号长度不对")){
					tracer.addTag("action.HousingPanjinService.login.fail", "登陆失败 账户错误");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}else{
					tracer.addTag("action.HousingPanjinService.login.fail", "登陆失败");
					taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
					taskHousing.setDescription(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
					save(taskHousing);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("action.HousingPanjinService.login.error", e.toString());
			tracer.addTag("action.HousingPanjinService.login.fail3", "登录异常");
			taskHousing.setPhase(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getPhasestatus());
			taskHousing.setDescription("登录异常，请您稍后重试。");
			taskHousing.setError_code(StatusCodeEnum.TASKMOBILE_LOGIN_ERROR.getError_code());
			save(taskHousing);
		}		
		return webParam;
	}
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing,int pageNum){		
		tracer.addTag("获取账户缴存明细信息 获取第"+pageNum+"页", messageLoginForHousing.getTask_id());
		taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {
			int temp = 0;
			for (int i = 1; i <= pageNum; i++) {
				webParam = housingFundPanjinHtmlunit.getPaydetails(messageLoginForHousing, taskHousing, i);
				if (null != webParam.getHtml()) {
					HousingPanjinHtml housingPanjinHtml = new HousingPanjinHtml();
					housingPanjinHtml.setPageCount(pageNum);
					housingPanjinHtml.setHtml(webParam.getHtml());
					housingPanjinHtml.setType("paydetails");
					housingPanjinHtml.setUrl(webParam.getUrl());
					housingPanjinHtml.setTaskid(taskHousing.getTaskid());
					housingPanjinHtmlRepository.save(housingPanjinHtml);
				}
				if (null != webParam.getPaydetails()) {
					housingPanjinPaydetailsRepository.saveAll(webParam.getPaydetails());
					temp++;
					tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息入库" + webParam.getPaydetails());
				}

			}
			if (temp > 0) {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			} else {
				updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(), 201,
						taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			}

		} catch (Exception e) {
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			tracer.addTag("action.housing.crawler.getPaydetails", "获取缴存明细信息失败");
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
}