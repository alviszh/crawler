package app.service;

import app.bean.MyProps;
import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceHeFeiParser;
import app.service.aop.InsuranceLogin;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.hefei"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.hefei"})
public class InsuranceHeFeiService implements InsuranceLogin {
    public static final Logger log = LoggerFactory.getLogger(InsuranceHeFeiService.class);

    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceHeFeiParser insuranceHeFeiParser;
    @Autowired
    private AsyncHeFeiGetAllDataService asyncHeFeiGetAllDataService;
    @Autowired
    private TracerLog tracer;
    @Autowired
    private MyProps myProps;

    private static int errorCount=0;

    @Async
    @Override
    public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
        tracer.qryKeyValue("taskid", insuranceRequestParameters.getTaskId());
        String[] ips = myProps.getIps();
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        if(null != taskInsurance){
            insuranceService.changeLoginStatusDoing(taskInsurance);
//            String ip = ips[1];
            for (String ip : ips) {//连接超时需要循环下一个ip登录
                WebParam webParam = null;
                try {
                    webParam = insuranceHeFeiParser.login(insuranceRequestParameters, ip);
                }catch (Exception e) {
                    tracer.addTag("登录解析失败",e.getMessage());
                }
                System.out.println(webParam);
                System.out.println(webParam.getCode());
                if (null == webParam) {
                    tracer.qryKeyValue("InsuranceHeFeiService===>login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                    taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                    return taskInsurance;
                } else {

                    String alertMsg = webParam.getAlertMsg();
                    if ("".equals(alertMsg) || alertMsg == null) {
                        tracer.qryKeyValue(insuranceRequestParameters.getTaskId(), "登陆成功");
                        taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
                        return taskInsurance;

                    } else if (alertMsg.contains("输入有误")) { //姓名、身份证号码不正确
                        taskInsurance = insuranceService.changeLoginStatusIdnumNotExistError(taskInsurance);
                        return taskInsurance;

                    } else if (alertMsg.contains("密码错误")) {
                        taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
                        return taskInsurance;

                    } else if (alertMsg.contains("验证码错误")) {
                        errorCount++;
                        tracer.addTag(insuranceRequestParameters.getTaskId() + "超级鹰对登录验证码解析错误的次数为：", errorCount + "次");
                        if (errorCount < 3) {
                            tracer.addTag("InsuranceHeFeiService.login", "检验码失败" + errorCount + "次，重新执行登录方法");
                            login(insuranceRequestParameters);
                        } else {
                            errorCount = 0;
                            taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
                            return taskInsurance;
                        }
                        return taskInsurance;
                    } /*else {
                        tracer.addTag("InsuranceHeFeiService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                        taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                        return taskInsurance;
                    }*/
                }
            }
            tracer.addTag("InsuranceHeFeiService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
            taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
            return taskInsurance;
        }
        return null;
    }

    @Async
    @Override
    public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
//		insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
        try {
            //爬取个人基本信息
            asyncHeFeiGetAllDataService.getUserInfo(taskInsurance, myProps.getIps());
            //爬取五险
            asyncHeFeiGetAllDataService.getAllInsurData(taskInsurance, myProps.getIps());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            tracer.addTag("爬取异常",e.toString());
            taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription());
            taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase());
            taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhasestatus());
            taskInsurance = taskInsuranceRepository.save(taskInsurance);
        }
        return taskInsurance;
    }

    @Override
    public TaskInsurance getAllDataDone(String taskId) {
        return null;
    }
}
