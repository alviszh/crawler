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
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinAccount;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinHtml;
import com.microservice.dao.entity.crawler.housing.haerbin.HousingFundHaErBinUserInfo;
import com.microservice.dao.repository.crawler.housing.haerbin.HousingHaErBinAccountRepository;
import com.microservice.dao.repository.crawler.housing.haerbin.HousingHaErBinHtmlRepository;
import com.microservice.dao.repository.crawler.housing.haerbin.HousingHaErBinUserInfoRepository;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.HousingFundHaErBinParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.haerbin")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.haerbin")
public class HousingFundHaErBinCommonService extends HousingBasicService{

	@Autowired
	private HousingFundHaErBinParser housingFundHaErBinParser;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private HousingHaErBinHtmlRepository housingHaErBinHtmlRepository;
	@Autowired
	private HousingHaErBinUserInfoRepository housingHaErBinUserInfoRepository;
	@Autowired
	private HousingHaErBinAccountRepository housingHaErBinAccountRepository;
	
	public void login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundHaErBinParser.login(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				if(null == webParam.getHtml())
				{
					tracer.addTag("parser.login.ERROR", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("系统错误"))
				{
					tracer.addTag("parser.login.ERROR.img", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_ONE.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("查询密码不正确，请重新输入"))
				{
					tracer.addTag("parser.login.ERROR.pwd", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription("查询密码不正确，请重新输入");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("个人编码不能为空或不足12位"))
				{
					tracer.addTag("parser.login.ERROR.personalNum", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");				
					taskHousing.setDescription("个人编码不能为空或不足12位");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("身份证号码位数不对"))
				{
					tracer.addTag("parser.login.ERROR.idNum", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
					taskHousing.setPhase_status("FAIL");
					taskHousing.setDescription("身份证号码位数不对");
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					save(taskHousing);
				}
				else if(webParam.getHtml().contains("欢迎您"))
				{
					tracer.addTag("parser.login.SUCCESS", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
					taskHousing.setCookies(cookieString);
					taskHousing.setLogintype(messageLoginForHousing.getLogintype());
					save(taskHousing);
				}
			}
			else
			{
				tracer.addTag("parser.login.ERROR.TIMEOUT1", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription("登陆超时，请核对信息及其网络");
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				save(taskHousing);
			}
		} catch (Exception e) {
			tracer.addTag("parser.login.ERROR.TIMEOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("登陆超时，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			save(taskHousing);
			e.printStackTrace();
		}
		
	}

	//个人信息
	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingFundHaErBinUserInfo> webParam = housingFundHaErBinParser.getUserInfo(messageLoginForHousing,taskHousing);
			if(null != webParam.getHousingHaErBinUserInfo())
			{
				tracer.addTag("parser.getUserInfo.SUCCESS", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 200, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setUserinfoStatus(200);
				HousingFundHaErBinHtml housingFundHaErBinHtml = new HousingFundHaErBinHtml();
				housingFundHaErBinHtml.setHtml(webParam.getHtml());
				housingFundHaErBinHtml.setPagenumber(1);
				housingFundHaErBinHtml.setTaskid(taskHousing.getTaskid());
				housingFundHaErBinHtml.setUrl(webParam.getUrl());
				taskHousing.setWebdriverHandle(webParam.getHousingHaErBinUserInfo().getPersonalNum());
				housingHaErBinHtmlRepository.save(housingFundHaErBinHtml);
				housingHaErBinUserInfoRepository.save(webParam.getHousingHaErBinUserInfo());
				updateTaskHousing(taskHousing.getTaskid()); 
			}
			else
			{
				tracer.addTag("parser.getUserInfo.ERROR1", taskHousing.getTaskid());
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
				taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
				taskHousing.setUserinfoStatus(201);
				updateTaskHousing(taskHousing.getTaskid()); 
			}
		} catch (Exception e) {
			tracer.addTag("parser.getUserInfo.ERROR2", taskHousing.getTaskid());
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription(), 201, taskHousing.getTaskid());
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousing.setUserinfoStatus(201);
			updateTaskHousing(taskHousing.getTaskid()); 
			e.printStackTrace();
		}
		
	}

	public void getAccount(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {int a=0;
			for (int j = 0; j < 3; j++) {
				WebParam<HousingFundHaErBinAccount> webParam = housingFundHaErBinParser.getAccount(messageLoginForHousing,taskHousing,j);
				if(null != webParam)
				{
					if(webParam.getList() != null)
					{
						tracer.addTag("action.crawlerAccount.SUCCESS", taskHousing.getTaskid());
						HousingFundHaErBinHtml h = new HousingFundHaErBinHtml();
						h.setHtml(webParam.getHtml());
						h.setTaskid(taskHousing.getTaskid());
						h.setType("crawlerAccount");
						h.setPagenumber(1);
						h.setUrl(webParam.getUrl());
//						System.out.println(h.toString());
						housingHaErBinHtmlRepository.save(h);
						housingHaErBinAccountRepository.saveAll(webParam.getList());
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
						taskHousing.setPaymentStatus(200);
						taskHousingRepository.save(taskHousing);
						updateTaskHousing(taskHousing.getTaskid());	
						a++;
					}
					else
					{
						tracer.addTag("action.crawlerAccount.ERROR1", taskHousing.getTaskid());
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
						taskHousing.setPaymentStatus(201);
						taskHousingRepository.save(taskHousing);
						updateTaskHousing(taskHousing.getTaskid());	
					}
				}
				else
				{
					tracer.addTag("action.crawlerAccount.ERROR2", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
					taskHousing.setPaymentStatus(201);
					taskHousingRepository.save(taskHousing);
					updateTaskHousing(taskHousing.getTaskid());	
				}
			}
			if(a>0)
			{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(200);
				taskHousingRepository.save(taskHousing);
				updateTaskHousing(taskHousing.getTaskid());
			}
			else
			{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setPaymentStatus(201);
				taskHousingRepository.save(taskHousing);
				updateTaskHousing(taskHousing.getTaskid());
			}
		} catch (Exception e) {
			tracer.addTag("action.crawlerUserInfo.TIMEOUT", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskHousing.setUserinfoStatus(201);
			taskHousingRepository.save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}

		 
	}

	//验证码
	public void getCode(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam webParam = housingFundHaErBinParser.getCode(messageLoginForHousing,taskHousing);
			if(null != webParam)
			{
				
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getDescription(), 200, taskHousing.getTaskid());
				taskHousing.setDescription("短信验证码已发送，请注意查收");
				taskHousing.setPhase("WAIT_CODE");
				taskHousing.setPhase_status("SUCCESS");
				String cookieString = CommonUnit.transcookieToJson(webParam.getWebClient());
				taskHousing.setCookies(cookieString);
				taskHousing.setWebdriverHandle(webParam.getHtml());
				save(taskHousing);
			}
			else
			{
				updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getDescription(), 201, taskHousing.getTaskid());
			}
		} catch (Exception e) {
			updateUserInfoStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getDescription(), 201, taskHousing.getTaskid());
			e.printStackTrace();
		}
	}

}
