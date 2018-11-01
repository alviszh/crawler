package app.service;

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
import app.domain.WebParam;
import app.enums.InsuranceZhengzhouCrawlerResult;
import app.htmlparser.ZhengzhouCrawler;
import app.service.aop.InsuranceLogin;


/**
 * Created by kaixu on 2017/9/19.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhengzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhengzhou" })
public class InsuranceZhengzhouService implements InsuranceLogin{
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private ZhengzhouCrawler zhengzhouCrawler;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private CrawlerBaseInfoService crawlerBaseInfoService;
    @Autowired
    private TracerLog tracer;

    @Async
    public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
        tracer.addTag("InsuranceZhengzhouService.login",insuranceRequestParameters.getTaskId());
        //登录认证
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        if(taskInsurance!=null) {
            WebParam<HtmlPage> webParam = null;
            try {
                // 进行登录状态的判断
                webParam = zhengzhouCrawler.crawlerLogin(insuranceRequestParameters);
                String resultCode = webParam.getCode();
                int loginCount = 1;
                if(InsuranceZhengzhouCrawlerResult.SUCCESS.getCode().equals(resultCode)){
                    tracer.addTag("InsuranceZhengzhouService.login[第"+loginCount+"次]:登录成功", webParam.getData().asXml());
                    insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getData(),webParam.getToken());
                }else{
                    if(InsuranceZhengzhouCrawlerResult.USER_OR_PASSWORD_ERROR.getCode().equals(resultCode)){
                        tracer.addTag("InsuranceZhengzhouService.login[第"+loginCount+"次]:用户名/密码错误", "用户名/密码错误");
                        insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
                    }
                    if(InsuranceZhengzhouCrawlerResult.IMAGE_ERROR.getCode().equals(resultCode)){
                        tracer.addTag("InsuranceZhengzhouService.login[第"+loginCount+"次]:图片验证码错误","图片验证码错误");
                        if(loginCount < 4){
                            login(insuranceRequestParameters);
                        }else{
                            insuranceService.changeLoginStatusPwdError(taskInsurance);
                        }
                    }
                    if(InsuranceZhengzhouCrawlerResult.EXCEPTION.getCode().equals(resultCode)){
                        tracer.addTag("InsuranceZhengzhouService.login[第"+loginCount+"次]:登录异常", webParam.getData().asXml());
                        insuranceService.changeLoginStatusTimeOut(taskInsurance);
                    }
                    if(InsuranceZhengzhouCrawlerResult.TIMEOUT.getCode().equals(resultCode)){
                        tracer.addTag("InsuranceZhengzhouService.login[第"+loginCount+"次]:连接超时", webParam.getData().asXml());
                        insuranceService.changeLoginStatusTimeOut(taskInsurance);
                    }
                    loginCount++;
                }
            } catch (Exception e) {
                String message = e.getMessage();
                tracer.addTag("InsuranceZhengzhouService 连接超时",message );
                insuranceService.changeLoginStatusTimeOut(taskInsurance);
            }
        }
        return taskInsurance;
    }

    /**
     * 更新task表（doing 正在登录状态）
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
     * 爬取指定账号的郑州社保信息
     * @param parameter
     * @return
     */
    @Async
    public TaskInsurance crawler(InsuranceRequestParameters parameter,TaskInsurance taskInsurance){
        tracer.addTag("InsuranceZhengzhouService.crawler:开始执行爬取", parameter.toString());

        tracer.addTag("InsuranceZhengzhouService.crawler:爬取郑州社保个人信息", parameter.toString());
        //爬取数据
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
        crawlerBaseInfoService.crawlerBaseInfo(parameter,taskInsurance,cookies);
        return taskInsurance;
    }

	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
