package app.service;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.enums.InsuranceXiamenCrawlerResult;
import app.htmlparser.XiamenCrawler;
import app.service.aop.InsuranceLogin;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kaixu on 2017/9/19.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.xiamen" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.xiamen" })
public class InsuranceXiamenService implements InsuranceLogin {
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private XiamenCrawler xiamenCrawler;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	@Autowired
	private TracerLog tracer;

	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceXiamenService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		// 登录认证
		if (taskInsurance != null) {
			WebParam<HtmlPage> webParam = null;
			try {
				// 进行登录状态的判断
				webParam = xiamenCrawler.crawlerLogin(insuranceRequestParameters);
				String resultCode = webParam.getCode();
				if (InsuranceXiamenCrawlerResult.SUCCESS.getCode().equals(resultCode)) {
					tracer.addTag("InsuranceXiamenService.login:登录成功", webParam.getData().asXml());
					insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getData());
				} else {
					if (InsuranceXiamenCrawlerResult.USER_OR_PASSWORD_ERROR.getCode().equals(resultCode)) {
						tracer.addTag("InsuranceZhengzhouService.login:用户名/密码错误", "");
						insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					}
					if (InsuranceXiamenCrawlerResult.IMAGE_ERROR.getCode().equals(resultCode)) {
						tracer.addTag("InsuranceZhengzhouService.login:图片验证码错误", "");

						insuranceService.changeLoginStatusPwdError(taskInsurance);
					}
					if (InsuranceXiamenCrawlerResult.EXCEPTION.getCode().equals(resultCode)) {
						tracer.addTag("InsuranceZhengzhouService.login:登录异常",
								webParam.getData().asXml());
						insuranceService.changeLoginStatusTimeOut(taskInsurance);
					}
					if (InsuranceXiamenCrawlerResult.TIMEOUT.getCode().equals(resultCode)) {
						tracer.addTag("InsuranceZhengzhouService.login:连接超时",
								webParam.getData().asXml());
						insuranceService.changeLoginStatusTimeOut(taskInsurance);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("InsuranceZhengzhouService 连接超时", "ERROR:" + e);
				insuranceService.changeLoginStatusTimeOut(taskInsurance);
			}
		}
		return taskInsurance;
	}

	/**
	 * 更新task表（doing 正在登录状态）
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}

	/**
	 * 获取TaskInsurance
	 * 
	 * @param parameter
	 * @return
	 */
	public TaskInsurance getTaskInsurance(InsuranceRequestParameters parameter) {
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		return taskInsurance;
	}

	/**
	 * 不存在TaskInsurance时,初始化错误信息
	 * 
	 * @return
	 */
	public TaskInsurance initNotExistTaskInsurance() {
		TaskInsurance taskInsurance = new TaskInsurance();
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getPhasestatus());
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getDescription());
		taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getError_code());
		return taskInsurance;
	}

	/**
	 * 爬取指定账号的厦门社保信息
	 * 
	 * @param parameter
	 * @return
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceZhengzhouService.crawler:开始执行爬取", insuranceRequestParameters.toString());
		tracer.addTag("InsuranceZhengzhouService.crawler:爬取厦门社保个人信息", insuranceRequestParameters.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		// 爬取数据
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		HtmlPage inHomePage = xiamenCrawler.inHomePage(insuranceRequestParameters, cookies);
		tracer.addTag("InsuranceShenzhenService.crawler:厦门社保主页爬取完成,准备爬取基本信息", inHomePage.asText());
		Map<String, WebParam<HtmlPage>> resultMap = new ConcurrentHashMap<>();
		// 开始爬取
		crawlerBaseInfoService.crawlerBaseInfo(insuranceRequestParameters, taskInsurance, inHomePage, cookies, resultMap);
		return taskInsurance;
	}


	@Override
	public TaskInsurance getAllDataDone(String insuranceRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
