package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.wuxi.InsuranceWuxiHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.wuxi.InsuranceWuxiHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.wuxi.InsuranceWuxiInsuredInfoRepository;
import com.microservice.dao.repository.crawler.insurance.wuxi.InsuranceWuxiMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.wuxi.InsuranceWuxiUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceWuxiParser;

/**
 * @author zhangyongjie
 * @create 2017-09-22 15:31
 * @Desc
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.wuxi" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.wuxi" })
public class InsuranceWuxiService {

    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private TracerLog tracer;

    @Autowired
    private InsuranceWuxiParser insuranceWuxiParser;

    @Autowired
    private InsuranceWuxiHtmlRepository insuranceWuxiHtmlRepository;

    @Autowired
    private InsuranceWuxiUserInfoRepository insuranceWuxiUserInfoRepository;

    @Autowired
    private InsuranceWuxiInsuredInfoRepository insuranceWuxiInsuredInfoRepository;

    @Autowired
    private InsuranceWuxiMedicalRepository insuranceWuxiMedicalRepository;

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
     * @Des 登录
     * @param insuranceRequestParameters
     * @return TaskInsurance
     * @throws Exception
     */
    @Async
    public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters,int loginCount)
            throws Exception {
        tracer.addTag("InsuranceWuxiService.login", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        if (null != taskInsurance) {
            try{
                WebParam webParam = insuranceWuxiParser.login(insuranceRequestParameters);
                if (null == webParam) {
                    tracer.addTag("InsuranceWuxiService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                    taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                    return taskInsurance;
                } else {
                    //用户名或密码错误
                    if(9999==webParam.getCode()){
                        taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
                        return taskInsurance;
                    }else if(8888==webParam.getCode()){
                        //验证码错误
                        tracer.addTag("InsuranceWuxiService.login" + insuranceRequestParameters.getTaskId(),
                                "登录失败次数" + loginCount);
                        if (loginCount < 4) {
                            login(insuranceRequestParameters, ++loginCount);
                        }else{
                            taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                            return taskInsurance;
                        }
                    }else if(200==webParam.getCode()){
                        String html = webParam.getPage().getWebResponse().getContentAsString();
                        tracer.addTag("InsuranceWuxiService.login",
                                insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
                        taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
                        return taskInsurance;
                    }
                }
            }catch (Exception e){
                tracer.addTag("InsuranceWuxiController.login:" , taskInsurance.getTaskid()+"---ERROR:"+e);
                e.printStackTrace();
                //修改超时
                insuranceService.changeLoginStatusTimeOut(taskInsurance);
            }

        }
        return null;
    }


    /**
     * @Des 更新taskInsurance
     * @param insuranceRequestParameters
     */
    public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
        return taskInsurance;
    }


    /**
     * @Des 获取个人信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceWuxiService.getUserinfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getUserinfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceWuxiParser.getUserInfo(taskInsurance, taskInsurance.getCookies());
        if (null != webParam) {
            insuranceWuxiUserInfoRepository.save(webParam.getInsuranceWuxiUserInfo());
            tracer.addTag("InsuranceWuxiService.getUserInfo 个人信息", "个人信息已入库!");

            insuranceService.changeCrawlerStatus(
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), webParam.getCode(),
                    taskInsurance);

            InsuranceWuxiHtml insuranceWuxiHtml = new InsuranceWuxiHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_wuxi_userinfo", 1, webParam.getUrl(), webParam.getHtml());
            insuranceWuxiHtmlRepository.save(insuranceWuxiHtml);
            tracer.addTag("InsuranceWuxiService.getUserInfo 个人信息源码", "个人信息源码表入库!");

            insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

        }else{
            tracer.addTag("InsuranceWuxiService.getUserInfo.webParam个人信息  is null", insuranceRequestParameters.getTaskId());

            insuranceService.changeCrawlerStatus(
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500,
                    taskInsurance);

            insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
        }

    }


    /**
     * @Des 获取个人参保信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getInsuredInfo(InsuranceRequestParameters insuranceRequestParameters,int currentPageNo) throws Exception {
        tracer.addTag("InsuranceWuxiService.getInsuredInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getInsuredInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        //分页保存参保信息
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceWuxiParser.getInsuredInfo(taskInsurance, taskInsurance.getCookies(),currentPageNo);
        if (null != webParam) {
            if(webParam.getList()!=null&&webParam.getList().size()>0) {
                insuranceWuxiInsuredInfoRepository.saveAll(webParam.getList());
                tracer.addTag("InsuranceWuxiService.getInsuredInfo 第" + currentPageNo + "页参保信息", "参保信息已入库!");

                InsuranceWuxiHtml insuranceWuxiHtml = new InsuranceWuxiHtml(insuranceRequestParameters.getTaskId(),
                        "insurance_wuxi_insuredinfo", 1, webParam.getUrl(), webParam.getHtml());
                insuranceWuxiHtmlRepository.save(insuranceWuxiHtml);
                tracer.addTag("InsuranceWuxiService.getInsuredInfo 第" + currentPageNo + "页参保信息源码", "参保信息源码表入库!");
                this.getInsuredInfo(insuranceRequestParameters, currentPageNo + 1);
            }else{
                tracer.addTag("InsuranceWuxiService.getInsuredInfo.webParam 第"+currentPageNo+"页参保信息  is null", insuranceRequestParameters.getTaskId());
            }
        }else{
            tracer.addTag("InsuranceWuxiService.getInsuredInfo.webParam 第"+currentPageNo+"页参保信息  is null", insuranceRequestParameters.getTaskId());
        }
    }

    /**
     * @Des 获取医疗保险信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getMedical(InsuranceRequestParameters insuranceRequestParameters,int currentPageNo) throws Exception {
        tracer.addTag("InsuranceWuxiService.getMedical", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getMedical", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        //分页保存医疗保险信息
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceWuxiParser.getMedical(taskInsurance, taskInsurance.getCookies(),currentPageNo);
        if (null != webParam) {
            if(webParam.getList()!=null&&webParam.getList().size()>0){
                insuranceWuxiMedicalRepository.saveAll(webParam.getList());
                tracer.addTag("InsuranceWuxiService.getMedical 第"+currentPageNo+"页医疗保险信息", "医疗保险信息已入库!");

                InsuranceWuxiHtml insuranceWuxiHtml = new InsuranceWuxiHtml(insuranceRequestParameters.getTaskId(),
                        "insurance_wuxi_medical", 1, webParam.getUrl(), webParam.getHtml());
                insuranceWuxiHtmlRepository.save(insuranceWuxiHtml);
                tracer.addTag("InsuranceWuxiService.getMedical 第"+currentPageNo+"页医疗保险信息源码", "医疗保险信息源码表入库!");

                this.getMedical(insuranceRequestParameters,currentPageNo+1);
            }else{
                tracer.addTag("InsuranceWuxiService.getMedical.webParam 第"+currentPageNo+"页医疗保险信息  is null", insuranceRequestParameters.getTaskId());

                insuranceService.changeCrawlerStatus("【个人社保-医疗保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),
                        webParam.getCode(), taskInsurance);

                insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
            }

        }else{
            tracer.addTag("InsuranceWuxiService.getMedical.webParam 第"+currentPageNo+"页医疗保险信息  is null", insuranceRequestParameters.getTaskId());
        }
    }


    /**
     * @Des 暂无养老保险信息访问权限，直接修改爬取状态为成功
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getPension(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceWuxiService.getPension", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getPension", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        insuranceService.changeCrawlerStatus("【个人社保-养老保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),
                200, taskInsurance);
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

    }

    /**
     * @Des 暂无工伤保险信息访问权限，直接修改爬取状态为成功
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getInjury(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceWuxiService.getInjury", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getInjury", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        insuranceService.changeCrawlerStatus("【个人社保-工伤保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),
                200, taskInsurance);
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

    }

    /**
     * @Des 暂无生育保险信息访问权限，直接修改爬取状态为成功
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getBirth(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceWuxiService.getBirth", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getBirth", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        insuranceService.changeCrawlerStatus("【个人社保-生育保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),
                200, taskInsurance);
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

    }

    /**
     * @Des 暂无失业保险信息访问权限，直接修改爬取状态为成功
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getUnemployment(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceWuxiService.getUnemployment", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        insuranceService.changeCrawlerStatus("【个人社保-失业保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),
                200, taskInsurance);
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

    }
}
