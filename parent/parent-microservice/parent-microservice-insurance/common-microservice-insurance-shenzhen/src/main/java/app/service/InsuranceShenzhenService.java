package app.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.service.aop.InsuranceLogin;

/**
 * 深圳社保爬取Service
 * @author rongshengxu
 *
 */
@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.shenzhen"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.shenzhen"})
public class InsuranceShenzhenService implements InsuranceLogin{
	
	@Autowired 
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private ShenzhenCrawler shenzhenCrawler;
	@Autowired
	private ShenzhenCrawlerParser shenzhenCrawlerParser;
	
	@Autowired
	private CrawlerBaseInfoService crawlerBaseInfoService;
	
	/**
	 * 登录
	 * @param parameter
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters parameter){
		tracer.addTag("InsuranceShenzhenService.login:开始登录", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		//登录认证
		WebParam<HtmlPage> webParam = shenzhenCrawler.loginCrawler(parameter);
		String resultCode = shenzhenCrawlerParser.parserLogin(webParam.getData());
		if(InsuranceShenzhenCrawlerResult.SUCCESS.getCode().equals(resultCode)){
			System.out.println("登录成功！");
			taskInsurance.setPid(webParam.getPid());
			insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getData());
		}else{
			System.out.println("登录失败！");
			if(InsuranceShenzhenCrawlerResult.USER_ERROR.getCode().equals(resultCode)){
				System.out.println("用户名错误！");
				insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
			}
			if(InsuranceShenzhenCrawlerResult.USER_OR_PASSWORD_ERROR.getCode().equals(resultCode)){
				System.out.println("用户名或密码错误！");
				insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
			}
			if(InsuranceShenzhenCrawlerResult.IMAGE_ERROR.getCode().equals(resultCode)){
				System.out.println("图片验证码错误！");
					insuranceService.changeLoginStatusPwdError(taskInsurance);
			}
			if(InsuranceShenzhenCrawlerResult.EXCEPTION.getCode().equals(resultCode)){
				System.out.println("异常！");
				insuranceService.changeLoginStatusTimeOut(taskInsurance);
			}
			if(InsuranceShenzhenCrawlerResult.TIMEOUT.getCode().equals(resultCode)){
				System.out.println("超时！");
				insuranceService.changeLoginStatusTimeOut(taskInsurance);
			}
		}
		return taskInsurance;
	}
	
	/**
	 * 获取TaskInsurance
	 * @param parameter
	 * @return
	 */
	public TaskInsurance getTaskInsurance(InsuranceRequestParameters parameter){
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId()); 
		return taskInsurance;
	}
	
	/**
	 * 不存在TaskInsurance时,初始化错误信息
	 * @return
	 */
	public TaskInsurance initNotExistTaskInsurance(){
		TaskInsurance taskInsurance = new TaskInsurance();
		taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getPhase());
		taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getPhasestatus());
		taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getDescription());
		taskInsurance.setError_code(InsuranceStatusCode.INSURANCE_CRAWLER_CHECK_ERROR.getError_code());
		return taskInsurance;
	}
	
	
	/**
	 * 爬取指定账号的深圳社保信息
	 * @param parameter
	 * @return
	 */
	@Async
	public TaskInsurance getAllData(InsuranceRequestParameters parameter){
		tracer.addTag("InsuranceShenzhenService.crawler:开始执行爬取", parameter.toString());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(parameter.getTaskId());
		//爬取数据
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
		String pid = taskInsurance.getPid();
		HtmlPage inHomePage = shenzhenCrawler.inHomePage(parameter,cookies,pid);
		
		tracer.addTag("InsuranceShenzhenService.crawler:深圳社保主页爬取完成,准备爬取基本信息", inHomePage.asText());
		Map<String,WebParam<HtmlPage>> resultMap = new HashMap<String,WebParam<HtmlPage>>();
		crawlerBaseInfoService.crawlerBaseInfo(parameter, taskInsurance, inHomePage, resultMap);
		
		System.out.println("数据采集完成之后的TaskInsurance-----"+taskInsurance.toString());
		
		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
