package app.service;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceQuanzhouParser;
import app.service.aop.InsuranceLogin;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.quanzhou.InsuranceQuanzhouHtml;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.quanzhou.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhangyongjie
 * @create 2017-09-19 13:58
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.quanzhou" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.quanzhou" })
public class InsuranceQuanzhouService implements InsuranceLogin{

    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private TracerLog tracer;

    @Autowired
    private InsuranceQuanzhouParser insuranceQuanzhouParser;

    @Autowired
    private InsuranceQuanzhouUserInfoRepository insuranceQuanzhouUserInfoRepository;

    @Autowired
    private InsuranceQuanzhouHtmlRepository insuranceQuanzhouHtmlRepository;

    @Autowired
    private InsuranceQuanzhouInsuredInfoRepository insuranceQuanzhouInsuredInfoRepository;

    @Autowired
    private InsuranceQuanzhouEmpPensionRepository insuranceQuanzhouEmpPensionRepository;

    @Autowired
    private InsuranceQuanzhouEmpMedicalRepository insuranceQuanzhouEmpMedicalRepository;

    @Autowired
    private InsuranceQuanzhouInjuryRepository insuranceQuanzhouInjuryRepository;

    @Autowired
    private InsuranceQuanzhouBirthRepository insuranceQuanzhouBirthRepository;
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
        tracer.addTag("InsuranceQuanzhouService.login", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        if (null != taskInsurance) {

            try {
                WebParam webParam = insuranceQuanzhouParser.login(insuranceRequestParameters);

                if (null == webParam) {
                    tracer.addTag("InsuranceQuanzhouService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                    taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                    return taskInsurance;
                } else {
                    String html = webParam.getPage().getWebResponse().getContentAsString();
                    tracer.addTag("InsuranceQuanzhouService.login",
                            insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
                    if (html.contains("个人基本信息概览")) {
                        taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
                        return taskInsurance;
                    } else {
                        tracer.addTag("InsuranceQuanzhouService.login" + insuranceRequestParameters.getTaskId(),
                                "登录失败次数" + loginCount);
                        if (loginCount < 4) {
                            login(insuranceRequestParameters, ++loginCount);
                        }else{
                            taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
                            return taskInsurance;
                        }
                    }

                }
            }catch (Exception e){
                tracer.addTag("InsuranceQuanzhouController.login:" , taskInsurance.getTaskid()+"---ERROR:"+e);
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
        tracer.addTag("InsuranceQuanzhouService.getUserinfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getUserinfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getUserInfo(taskInsurance, taskInsurance.getCookies());
        if (null != webParam) {
            insuranceQuanzhouUserInfoRepository.save(webParam.getInsuranceQuanzhouUserInfo());
            tracer.addTag("InsuranceQuanzhouService.getUserInfo 个人信息", "个人信息已入库!");

            insuranceService.changeCrawlerStatus(
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), webParam.getCode(),
                    taskInsurance);

            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_userinfo", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getUserInfo 个人信息源码", "个人信息源码表入库!");

            insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

        }else{
            tracer.addTag("InsuranceQuanzhouService.getUserInfo.webParam个人信息  is null", insuranceRequestParameters.getTaskId());

            insuranceService.changeCrawlerStatus(
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                    InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 500,
                    taskInsurance);

            insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
        }

    }


    /**
     * @Des 获取企业职工基本养老保险的参保信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getEmpPensionInsuredInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getEmpPensionInsuredInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getEmpPensionInsuredInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getEmpPensionInsuredInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {
            insuranceQuanzhouInsuredInfoRepository.save(webParam.getInsuranceQuanzhouInsuredInfo());
            tracer.addTag("InsuranceQuanzhouService.getEmpPensionInsuredInfo 企业职工基本养老保险的参保信息", "企业职工基本养老保险的参保信息已入库!");


            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_insuredinfo", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getEmpPensionInsuredInfo 企业职工基本养老保险的参保信息源码", "企业职工基本养老保险的参保信息源码表入库!");

        }else{
            tracer.addTag("InsuranceQuanzhouService.getEmpPensionInsuredInfo 企业职工基本养老保险的参保信息源码  is null", insuranceRequestParameters.getTaskId());

        }

    }

    /**
     * @Des 获取企业职工基本养老保险的缴费信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getEmpPensionPayInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getEmpPensionPayInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getEmpPensionPayInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getEmpPensionPayInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {


            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_emppension", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getEmpPensionPayInfo 企业职工基本养老保险的缴费信息源码", "企业职工基本养老保险的缴费信息源码表入库!");

            if(webParam.getList()!=null&&webParam.getList().size()>0){
                insuranceQuanzhouEmpPensionRepository.saveAll(webParam.getList());
                tracer.addTag("InsuranceQuanzhouService.getEmpPensionPayInfo 企业职工基本养老保险的缴费信息", "企业职工基本养老保险的缴费信息已入库!");
                insuranceService.changeCrawlerStatus("【个人社保-养老保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),
                        webParam.getCode(), taskInsurance);
                insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
            }else{
                tracer.addTag("InsuranceQuanzhouService.getEmpPensionPayInfo 企业职工基本养老保险的缴费信息 is null", "企业职工基本养老保险的缴费信息为空!");
            }

        }else{
            tracer.addTag("InsuranceQuanzhouService.getEmpPensionPayInfo 企业职工基本养老保险的缴费信息源码  is null", insuranceRequestParameters.getTaskId());
        }
    }


    /**
     * @Des 获取企业职工基本养老保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getEmpPension (InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getEmpPension", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getEmpPension", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getEmpPension(taskInsurance, taskInsurance.getCookies());
        if (null != webParam) {

            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_emppension", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getEmpPension 企业职工基本养老保险源码", "企业职工基本养老保险源码表入库!");

            Map<String,Object> params = webParam.getParams();
            //获取企业职工基本养老保险参保信息
            this.getEmpPensionInsuredInfo(insuranceRequestParameters,params);
            //获取企业职工基本养老保险缴费信息
            this.getEmpPensionPayInfo(insuranceRequestParameters,params);

        } else {
            tracer.addTag("InsuranceQuanzhouService.getEmpPension 企业职工基本养老保险源码  is null", insuranceRequestParameters.getTaskId());

        }

    }


    /**
     * @Des 获取城镇职工基本医疗保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getEmpMedical (InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getEmpMedical", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getEmpMedical", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getEmpMedical(taskInsurance, taskInsurance.getCookies());
        if (null != webParam) {

            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_empmedical", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getEmpMedical 城镇职工基本医疗保险源码", "城镇职工基本医疗保险源码表入库!");

            Map<String,Object> params = webParam.getParams();
            //获取城镇职工基本医疗保险参保信息
            this.getEmpMedicalInsuredInfo(insuranceRequestParameters,params);
            //获取城镇职工基本医疗保险缴费信息
            this.getEmpMedicalPayInfo(insuranceRequestParameters,params);

        } else {
            tracer.addTag("InsuranceQuanzhouService.getEmpMedical 城镇职工基本医疗保险源码  is null", insuranceRequestParameters.getTaskId());

        }

    }


    /**
     * @Des 获取城镇职工基本医疗保险参保信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getEmpMedicalInsuredInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getEmpMedicalInsuredInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getEmpMedicalInsuredInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getEmpMedicalInsuredInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {
            insuranceQuanzhouInsuredInfoRepository.save(webParam.getInsuranceQuanzhouInsuredInfo());
            tracer.addTag("InsuranceQuanzhouService.getEmpMedicalInsuredInfo 城镇职工基本医疗保险参保信息", "城镇职工基本医疗保险参保信息已入库!");


            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_insuredinfo", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getEmpMedicalInsuredInfo 城镇职工基本医疗保险参保信息源码", "城镇职工基本医疗保险参保信息源码表入库!");

        }else{
            tracer.addTag("InsuranceQuanzhouService.getEmpMedicalInsuredInfo 城镇职工基本医疗保险参保信息源码  is null", insuranceRequestParameters.getTaskId());

        }

    }


    /**
     * @Des 获取城镇职工基本医疗保险的缴费信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getEmpMedicalPayInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getEmpMedicalPayInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getEmpMedicalPayInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getEmpMedicalPayInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {


            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_empmedical", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getEmpMedicalPayInfo 城镇职工基本医疗保险的缴费信息源码", "城镇职工基本医疗保险的缴费信息源码表入库!");

            if(webParam.getList()!=null&&webParam.getList().size()>0){
                insuranceQuanzhouEmpMedicalRepository.saveAll(webParam.getList());
                tracer.addTag("InsuranceQuanzhouService.getEmpMedicalPayInfo 城镇职工基本医疗保险的缴费信息", "城镇职工基本医疗保险的缴费信息已入库!");
                insuranceService.changeCrawlerStatus("【个人社保-医疗保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),
                        webParam.getCode(), taskInsurance);
                insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
            }else{
                tracer.addTag("InsuranceQuanzhouService.getEmpMedicalPayInfo 城镇职工基本医疗保险的缴费信息 is null", "城镇职工基本医疗保险的缴费信息为空!");
            }

        }else{
            tracer.addTag("InsuranceQuanzhouService.getEmpMedicalPayInfo 城镇职工基本医疗保险的缴费信息源码  is null", insuranceRequestParameters.getTaskId());
        }
    }


    /**
     * @Des 获取工伤保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getInjury (InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getInjury", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getInjury", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getInjury(taskInsurance, taskInsurance.getCookies());
        if (null != webParam) {

            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_injury", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getInjury 工伤保险源码", "工伤保险源码表入库!");

            Map<String,Object> params = webParam.getParams();
            //获取工伤保险参保信息
            this.getInjutyInsuredInfo(insuranceRequestParameters,params);
            //获取工伤保险缴费信息
            this.getInjuryPayInfo(insuranceRequestParameters,params);
        } else {
            tracer.addTag("InsuranceQuanzhouService.getInjury 工伤保险源码  is null", insuranceRequestParameters.getTaskId());
        }
    }


    /**
     * @Des 获取工伤保险参保信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getInjutyInsuredInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getInjutyInsuredInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getInjutyInsuredInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getInjuryInsuredInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {
            insuranceQuanzhouInsuredInfoRepository.save(webParam.getInsuranceQuanzhouInsuredInfo());
            tracer.addTag("InsuranceQuanzhouService.getInjutyInsuredInfo 工伤保险参保信息", "工伤保险参保信息已入库!");


            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_insuredinfo", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getInjutyInsuredInfo 工伤保险参保信息源码", "工伤保险参保信息源码表入库!");

        }else{
            tracer.addTag("InsuranceQuanzhouService.getInjutyInsuredInfo 工伤保险参保信息源码  is null", insuranceRequestParameters.getTaskId());

        }

    }


    /**
     * @Des 获取工伤保险的缴费信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getInjuryPayInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getInjuryPayInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getInjuryPayInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getInjuryPayInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {
            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_injury", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getInjuryPayInfo 工伤保险的缴费信息源码", "工伤保险的缴费信息源码表入库!");

            if(webParam.getList()!=null&&webParam.getList().size()>0){
                insuranceQuanzhouInjuryRepository.saveAll(webParam.getList());
                tracer.addTag("InsuranceQuanzhouService.getInjuryPayInfo 工伤保险的缴费信息", "工伤保险的缴费信息已入库!");
                insuranceService.changeCrawlerStatus("【个人社保-工伤保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(),
                        webParam.getCode(), taskInsurance);
                insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
            }else{
                tracer.addTag("InsuranceQuanzhouService.getInjuryPayInfo 工伤保险的缴费信息 is null", "工伤保险的缴费信息为空!");
            }

        }else{
            tracer.addTag("InsuranceQuanzhouService.getInjuryPayInfo 工伤保险的缴费信息源码  is null", insuranceRequestParameters.getTaskId());
        }
    }


    /**
     * @Des 获取生育保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getBirth (InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getBirth", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getBirth", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getBirth(taskInsurance, taskInsurance.getCookies());
        if (null != webParam) {

            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_birth", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getBirth 生育保险源码", "生育保险源码表入库!");

            Map<String,Object> params = webParam.getParams();
            //获取工伤保险参保信息
            this.getBirthInsuredInfo(insuranceRequestParameters,params);
            //获取工伤保险缴费信息
            this.getBirthPayInfo(insuranceRequestParameters,params);
        } else {
            tracer.addTag("InsuranceQuanzhouService.getBirth 生育保险源码  is null", insuranceRequestParameters.getTaskId());
        }
    }


    /**
     * @Des 获取生育保险参保信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getBirthInsuredInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getBirthInsuredInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getBirthInsuredInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getBirthInsuredInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {
            insuranceQuanzhouInsuredInfoRepository.save(webParam.getInsuranceQuanzhouInsuredInfo());
            tracer.addTag("InsuranceQuanzhouService.getBirthInsuredInfo 生育保险参保信息", "生育保险参保信息已入库!");


            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_insuredinfo", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getBirthInsuredInfo 生育保险参保信息源码", "生育保险参保信息源码表入库!");

        }else{
            tracer.addTag("InsuranceQuanzhouService.getBirthInsuredInfo 生育保险参保信息源码  is null", insuranceRequestParameters.getTaskId());

        }
    }

    /**
     * @Des 获取生育保险的缴费信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getBirthPayInfo(InsuranceRequestParameters insuranceRequestParameters,Map<String,Object> params) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getBirthPayInfo", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getBirthPayInfo", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceQuanzhouParser.getBirthPayInfo(taskInsurance, taskInsurance.getCookies(),params);
        if (null != webParam) {
            InsuranceQuanzhouHtml insuranceQuanzhouHtml = new InsuranceQuanzhouHtml(insuranceRequestParameters.getTaskId(),
                    "insurance_quanzhou_birth", 1, webParam.getUrl(), webParam.getHtml());
            insuranceQuanzhouHtmlRepository.save(insuranceQuanzhouHtml);
            tracer.addTag("InsuranceQuanzhouService.getBirthPayInfo 生育保险的缴费信息源码", "生育保险的缴费信息源码表入库!");

            if(webParam.getList()!=null&&webParam.getList().size()>0){
                insuranceQuanzhouBirthRepository.saveAll(webParam.getList());
                tracer.addTag("InsuranceQuanzhouService.getBirthPayInfo 生育保险的缴费信息", "生育保险的缴费信息已入库!");
                insuranceService.changeCrawlerStatus("【个人社保-生育保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),
                        webParam.getCode(), taskInsurance);
                insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
            }else{
                tracer.addTag("InsuranceQuanzhouService.getBirthPayInfo 生育保险的缴费信息 is null", "生育保险的缴费信息为空!");
            }

        }else{
            tracer.addTag("InsuranceQuanzhouService.getBirthPayInfo 生育保险的缴费信息源码  is null", insuranceRequestParameters.getTaskId());
        }
    }


    /**
     * @Des 暂无失业保险参保信息和缴费信息，直接修改爬取状态为成功
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getUnemployment (InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("InsuranceQuanzhouService.getUnemployment", insuranceRequestParameters.getTaskId());

        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        insuranceService.changeCrawlerStatus("【个人社保-失业保险】已采集完成！", InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),
                200, taskInsurance);
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());

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

	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		try {
			TaskInsurance taskInsurance = login(insuranceRequestParameters,1);
			return taskInsurance;
		} catch (Exception e) {
			tracer.addTag("Insurancelogin", insuranceRequestParameters.getTaskId()+e.getMessage());
			return null;
		}
		
	}
}
