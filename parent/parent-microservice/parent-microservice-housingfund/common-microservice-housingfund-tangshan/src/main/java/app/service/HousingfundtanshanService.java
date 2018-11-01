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
import com.crawler.mobile.json.MessageResult;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;
import app.service.common.aop.ISms;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tangshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tangshan")
public class HousingfundtanshanService extends HousingBasicService implements ICrawlerLogin,ISms{
	@Autowired
	private LoginAndGetService loginAndGetService;

	@Autowired
	private HousingfundtanshanUnitService housingfundtanshanUnitService;

	private MessageResult messageresult;
	
	@Override
	public TaskHousing sendSms(MessageLoginForHousing messageLogin) {
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebParamHousing webParamHousing = null;
		String messageLoginJson = gs.toJson(messageLogin);
		taskHousing.setLoginMessageJson(messageLoginJson);
		System.out.println(messageLogin.toString());

		try {
			WebParamHousing login = loginAndGetService.login(webClient,messageLogin,taskHousing);
			taskHousing.setPassword(messageLogin.getPassword().trim());
			save(taskHousing);
			System.out.println("登录Cookie原始："+webClient.getCookieManager().getCookies().toString());
			return taskHousing;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
			taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_ERROR_FOURE.getMessage());
			taskHousing.setPassword(messageLogin.getPassword().trim());
			tracer.addTag("parser.login.auth", e.getMessage());
			save(taskHousing);
			return taskHousing;
		}

	}

	@Override
	public TaskHousing verifySms(MessageLoginForHousing messageLogin) {
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid",taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getDescription());
		save(taskHousing);

		WebParamHousing webParamHousing = loginAndGetService.setPhonecode(messageLogin,taskHousing);

		if(webParamHousing==null){
			System.out.println("验证短信失败");
			return null;
		}
		String cookieString = CommonUnit.transcookieToJson(webParamHousing.getWebClient());
		taskHousing.setCookies(cookieString);
		// 手机验证码验证成功状态更新
		save(taskHousing);
		System.out.println("手机验证码验证成功");
		return null;
	}
	
	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLogin) {
		TaskHousing taskHousing = findTaskHousing(messageLogin.getTask_id());
		tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		//爬取数据状态更新
		save(taskHousing);

		housingfundtanshanUnitService.Userinfo(messageLogin,taskHousing);
		
		housingfundtanshanUnitService.PayStatus(messageLogin,taskHousing);
		
		taskHousing = updateTaskHousing(taskHousing.getTaskid());
		return taskHousing;
	}

	
	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
