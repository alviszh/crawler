package app.service;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceShenyangUnemployeedParser;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shenyang.*;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shenyang.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mu on 2017/9/18.
 */
@SuppressWarnings("all")
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shenyang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shenyang"})
public class InsuranceShenyangUnemployeedService {
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceShenyangUnemployeedParser insuranceShenyangUnemployeedParser;
    @Autowired
    private InsuranceShenyangHtmlRepository insuranceShenyangHtmlRepository;

    @Autowired
    private InsuranceShenyangUnemployeedRepository insuranceShenyangUnemployeedRepository;

    @Autowired
    private TracerLog tracer;
    @Autowired
    private InsuranceShenyangPensionService insuranceShenyangPensionService;

    /**
     * 更改登陆状态-----正在登陆中
     * @param insuranceRequestParameters
     * @return
     */
    public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
        return taskInsurance;
    }






    /**
     * 登陆
     * @param insuranceRequestParameters
     * @param loginCount
     * @return
     * @throws Exception
     */
    @Async
    @SuppressWarnings("all")
    public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters, int loginCount, AtomicInteger status,AtomicInteger loginNum) throws Exception{
//        System.out.println("\n\n失业保险第"+loginCount+"次登陆");
        Long start = System.currentTimeMillis();
        String loginType = insuranceRequestParameters.getLoginType();
        tracer.addTag("InsuranceShenyang-unemployeedService.login", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
//        System.out.println("沈阳失业保险登陆！！！");
        if (null != taskInsurance) {
            WebParam webParam = insuranceShenyangUnemployeedParser.login(insuranceRequestParameters);
            if (null == webParam) {
                tracer.addTag("InsuranceShenyang-unemployeedService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                taskInsurance = insuranceShenyangPensionService.changeLoginStatus(taskInsurance,null,"unemployee","timeout",webParam,loginType,status,loginNum);
                return taskInsurance;
            } else {
                String html = webParam.getHtml();
                tracer.addTag("InsuranceShenyang-unemployeedService.login",
                        insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
                Integer code=webParam.getCode();
                if (1001 == code) {
                    tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
                    InsuranceShenyangHtml syhtml = new InsuranceShenyangHtml();
                    InsuranceShenyangHtml shenyangHtml = new InsuranceShenyangHtml();
                    shenyangHtml.setHtml(html);
                    shenyangHtml.setPageCount(1);
                    shenyangHtml.setTaskId(taskInsurance.getTaskid());
                    shenyangHtml.setType("unemployeed");
                    shenyangHtml.setUrl(webParam.getUrl());
                    insuranceShenyangHtmlRepository.save(shenyangHtml);
                    taskInsurance =  insuranceShenyangPensionService.changeLoginStatus(taskInsurance,webParam.getPage(),"unemployee","success",webParam,loginType,status,loginNum);
                    Long end = System.currentTimeMillis();
                    System.out.println("失业保险登陆耗时:"+(end-start));
                    return taskInsurance;
                }else if(1002 == code){
                    tracer.addTag(insuranceRequestParameters.getTaskId(),"号码校验位错");
                    taskInsurance = insuranceShenyangPensionService.changeLoginStatus(taskInsurance,null,"unemployee","accounterror",webParam,loginType,status,loginNum);
                    return taskInsurance;
                }else if(1003 == code){
                    if (loginCount < 4) {
                        if(status.get() != 4)
                            login(insuranceRequestParameters, ++loginCount,status,loginNum);
                    }else{
                        tracer.addTag(insuranceRequestParameters.getTaskId(),"超级鹰识别超过三次");
                        taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
                        return taskInsurance;
                    }
                    tracer.addTag("InsuranceShenyang-unemployeedService.login" + insuranceRequestParameters.getTaskId(),"登录失败次数" + loginCount);
                }else{
                    tracer.addTag("InsuranceShenyang-unemployeedService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                    taskInsurance = insuranceShenyangPensionService.changeLoginStatus(taskInsurance,null,"unemployee","timeout",webParam,loginType,status,loginNum);
                    return taskInsurance;
                }
            }
        }
        return null;
    }
    /**
     * @Des 更新task表（doing 正在采集）
     * @param insuranceRequestParameters
     */
    public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
        return taskInsurance;
    }

    /**
     * 爬取用户数据
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getUnemployeedInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
        List<InsuranceShenyangHtml> insuranceShenyangHtml = insuranceShenyangHtmlRepository.findByTaskIdAndType(insuranceRequestParameters.getTaskId(),"unemployeed");
        WebParam webParam = null;
        if(insuranceShenyangHtml.size()>0){
//            System.out.println("service开始");
            tracer.addTag("shenyang-unemployeedcrawler-getunemployeedinfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + insuranceShenyangHtml.get(0).getHtml() + "</xmp>");
            List<InsuranceShenyangUnemployeedInfo> unemployeedInfos = insuranceShenyangUnemployeedParser.getUnemployeedInfo(insuranceShenyangHtml.get(0),cookies);
            insuranceShenyangUnemployeedRepository.saveAll(unemployeedInfos);
            insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),200,taskInsurance);
        }else {
            insuranceService.changeCrawlerStatus("失业保险数据爬取完毕，无数据",
                    InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
        }
        insuranceService.changeCrawlerStatusSuccess(insuranceRequestParameters.getTaskId());
    }
}
