package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.changzhou.HousingChangZhouBasicRows;
import com.microservice.dao.entity.crawler.housing.changzhou.HousingChangZhouTranRows;
import com.microservice.dao.repository.crawler.housing.changzhou.HousingChangZhouBasicRowsRepository;
import com.microservice.dao.repository.crawler.housing.changzhou.HousingChangZhouTranRowsRepository;

import app.bean.BasicJsonRootBean;
import app.bean.TranJsonRootBean;
import app.bean.WebParamHousing;
import app.bean.error.ErrorException;
import app.htmlparse.HousingCZParse;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawlerLogin;

/**
 * 
 * 项目名称：common-microservice-housingfund-changzhou 类名称：HousingChangZhouService
 * 类描述： 创建人：Administrator 创建时间：2018年2月28日 上午10:11:02
 * 
 * @version
 */
@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.changzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.changzhou")
public class HousingChangZhouService extends HousingBasicService implements ICrawlerLogin{

	public static final Logger log = LoggerFactory.getLogger(HousingChangZhouService.class);

	@Autowired
	private LoginAndGetService loginAndGetService;

	@Autowired
	private HousingChangZhouBasicRowsRepository housingChangZhouBasicRowsRepository;

	@Autowired
	private HousingChangZhouTranRowsRepository housingChangZhouTranRowsRepository;
	
	private WebParamHousing<?> webParamHousing = new WebParamHousing<>();

	@Override
	@Retryable(value = ErrorException.class, maxAttempts = 3)
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {

		tracer.addTag("taskid", messageLoginForHousing.getTask_id());
		tracer.addTag("auth", messageLoginForHousing.getNum());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			webParamHousing = loginAndGetService.login(messageLoginForHousing.getNum(),
					messageLoginForHousing.getPassword());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			webParamHousing.setErrormessage("网络连接超时");

		}
		System.out.println(webParamHousing.getErrormessage());
		if (webParamHousing.getErrormessage() != null) {
			if (webParamHousing.getErrormessage().indexOf("验证码") != -1) {
				throw new ErrorException("验证码验证失败");
			}
			System.out.println("登录出现问题");

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
			taskHousing.setDescription(webParamHousing.getErrormessage());
			taskHousing.setError_message(webParamHousing.getErrormessage());
			save(taskHousing);
			tracer.addTag("parser.login.Errormessage", webParamHousing.getErrormessage());

			return taskHousing;
		}
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());

		taskHousing.setError_code(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getCode());
		taskHousing.setError_message(StatusCodeRec.MESSAGE_LOGIN_SUCESS.getMessage());
		taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
		save(taskHousing);
		return taskHousing;

	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {

		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());

		WebClient webClient = webParamHousing.getWebClient();

		// 获取用户公积金id
		String mannum = null;
		try {
			mannum = loginAndGetService.getWorkerAccount(webClient);
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ERROR.getPhasestatus());
			taskHousing.setUserinfoStatus(404);
			taskHousing.setPaymentStatus(404);
			taskHousing.setDescription("获取关键字超时");
			save(taskHousing);
			tracer.addTag("parser.login.Errormessage", "获取关键字超时");
			taskHousing = findTaskHousing(taskHousing.getTaskid());
			save(taskHousing);
			updateTaskHousing(taskHousing.getTaskid());
			return null;
		}

		try {
			webParamHousing = loginAndGetService.getBasic(webClient, mannum);
			BasicJsonRootBean jsonObject = HousingCZParse
					.basic_parse(webParamHousing.getPage().getWebResponse().getContentAsString());

			List<HousingChangZhouBasicRows> results = jsonObject.getDataset().getRows();
			for (HousingChangZhouBasicRows result : results) {
				result.setTaskid(taskHousing.getTaskid());
				result.setUserid(messageLoginForHousing.getUser_id());
				housingChangZhouBasicRowsRepository.save(result);
			}

			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
			taskHousing.setUserinfoStatus(200);
			save(taskHousing);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
			taskHousing.setUserinfoStatus(404);
			taskHousing.setDescription("基本信息连接超时");
			save(taskHousing);
			tracer.addTag("parser.login.Errormessage", webParamHousing.getErrormessage());

		}

		LocalDate date = LocalDate.now();
		List<HousingChangZhouTranRows> resultstran = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			LocalDate searchdate = date.plusYears(-i);

			try {
				webParamHousing = loginAndGetService.getTranFlow(webClient, mannum, searchdate.getYear() + "");

				TranJsonRootBean jsonObjecttran = HousingCZParse
						.tran_parse(webParamHousing.getPage().getWebResponse().getContentAsString());

				List<HousingChangZhouTranRows> resultslist = jsonObjecttran.getDataset().getRows();

				for (HousingChangZhouTranRows result : resultslist) {
					result.setTaskid(taskHousing.getTaskid());
					result.setUserid(messageLoginForHousing.getUser_id());
					resultstran.add(result);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				tracer.addTag("parser.login.Errormessage : " + searchdate, "缴费信息" + searchdate + "连接超时");
			}

		}

		if (resultstran.size() < 0) {
			taskHousing = findTaskHousing(taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
			taskHousing.setPaymentStatus(404);
			taskHousing.setDescription("缴费信息连接超时");
			save(taskHousing);
		} else {
			taskHousing = findTaskHousing(taskHousing.getTaskid());
			housingChangZhouTranRowsRepository.saveAll(resultstran);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
			taskHousing.setPaymentStatus(200);
			taskHousing.setDescription("缴费信息抓取成功");
			save(taskHousing);
		}
		taskHousing = findTaskHousing(taskHousing.getTaskid());
		updateTaskHousing(taskHousing.getTaskid());
		return null;

	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}
