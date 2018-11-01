package app.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouHtml;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouInsuranceBasic;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouStatus;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouUserinfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.wenzhou.InsuranceWenzhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.wenzhou.InsuranceWenzhouInsuranceBasicRepository;
import com.microservice.dao.repository.crawler.insurance.wenzhou.InsuranceWenzhouPensionPaywaterRepository;
import com.microservice.dao.repository.crawler.insurance.wenzhou.InsuranceWenzhouStatusRepository;
import com.microservice.dao.repository.crawler.insurance.wenzhou.InsuranceWenzhouUserinfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceWenzhouParser;

/**
 * Created by Mu on 2017/9/18.
 */
@SuppressWarnings("all")
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.wenzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.wenzhou"})
public class InsuranceWenzhouService {
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceWenzhouParser insuranceWenzhouParser;
    @Autowired
    private InsuranceWenzhouHtmlRepository insuranceWenzhouHtmlRepository;
    @Autowired
    private InsuranceWenzhouInsuranceBasicRepository insuranceWenzhouInsuranceBasicRepository;
    @Autowired
    private InsuranceWenzhouStatusRepository  insuranceWenzhouStatusRepository;
    @Autowired
    private InsuranceWenzhouPensionPaywaterRepository insuranceWenzhouPensionPaywaterRepository;
    @Autowired
    private InsuranceWenzhouUserinfoRepository insuranceWenzhouUserinfoRepository;
    @Autowired
    InsuranceWenzhouBasicInfoService insuranceWenzhouBasicInfoService;
    @Autowired
    private TracerLog tracer;


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
     * @Des 更新task表（doing 正在采集）
     * @param insuranceRequestParameters
     */
    public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
        TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
        return taskInsurance;
    }

    /**
     * 登陆   后续需加入根据类型判断登陆方式
     * @param insuranceRequestParameters
     * @param loginCount
     * @return
     * @throws Exception
     */
    @Async
    @SuppressWarnings("all")
    public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters, int loginCount) throws Exception{

        tracer.addTag("InsuranceWenzhou-Service.login", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        String loginType = insuranceRequestParameters.getLoginType();

        WebParam webParam = null;
        if (null != taskInsurance) {

//            if("IDNUM".equals(loginType))
//                webParam = insuranceWenzhouParser.login(insuranceRequestParameters);
            webParam = insuranceWenzhouParser.login(insuranceRequestParameters);
            if (null == webParam) {
                tracer.addTag("InsuranceWenzhou-Service.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                return taskInsurance;
            } else {
                String html = webParam.getHtml();
                tracer.addTag("InsuranceWenzhou-service.login",
                        insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
                Integer code=webParam.getCode();
                if (1001 == code) {
                    tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
                    InsuranceWenzhouHtml wzhtml = new InsuranceWenzhouHtml();
                    wzhtml.setHtml(html);
                    wzhtml.setPageCount(1);
                    wzhtml.setTaskId(taskInsurance.getTaskid());
                    wzhtml.setType("userInfo");
                    wzhtml.setUrl(webParam.getUrl());
                    insuranceWenzhouHtmlRepository.save(wzhtml);

                    taskInsurance =  changeLoginStatusSuccess(taskInsurance, webParam,loginType);

                    return taskInsurance;
                }else if(1002 == code){
                    tracer.addTag(insuranceRequestParameters.getTaskId(),"用户名、密码是否正确？请重试");
                    taskInsurance = insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
                    return taskInsurance;
                }else if(1003 == code){
                    tracer.addTag(insuranceRequestParameters.getTaskId(),"密码错误,请重新输入");
                    taskInsurance = insuranceService.changeLoginStatusPwdError(taskInsurance);
                    return taskInsurance;
                }else if(1004 == code){
                    if (loginCount < 4) {
                        login(insuranceRequestParameters, ++loginCount);
                    }else{
                        tracer.addTag(insuranceRequestParameters.getTaskId(),"超级鹰识别超过三次");
                        taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
                        return taskInsurance;
                    }
                    tracer.addTag("InsuranceWenzhou-Service.login" + insuranceRequestParameters.getTaskId(),"登录失败次数" + loginCount);
                }else{
                    tracer.addTag("InsuranceWenzhou-Service.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                    taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                    return taskInsurance;
                }
            }
        }
        return null;
    }

    /**
     * 爬取用户数据
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getUserinfo", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
        @SuppressWarnings("rawtypes")
        WebParam webParam = insuranceWenzhouParser.getUserInfo(cookies,insuranceRequestParameters);
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getUserInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
            insuranceWenzhouHtml.setPageCount(1);
            insuranceWenzhouHtml.setType("用户个人信息");
            insuranceWenzhouHtml.setTaskId(insuranceRequestParameters.getTaskId());
            insuranceWenzhouHtml.setUrl(webParam.getUrl());
            insuranceWenzhouHtml.setHtml(html);
            insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);
            InsuranceWenzhouUserinfo userInfo=webParam.getUserInfo();
            if (null !=userInfo) {
                insuranceWenzhouUserinfoRepository.save(userInfo);
                tracer.addTag("wenzhou-getUserinfo", "社保个人信息已入库");
                insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
            }else{
                tracer.addTag("getUserinfo", "温州社保个人信息未爬取到，无数据");
                insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 201);
            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }
    /**
     * 爬取养老保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getPensionInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getPension", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
        InsuranceWenzhouStatus insuranceWenzhouStatus = new InsuranceWenzhouStatus();
        insuranceWenzhouStatus.setCount(0);
        insuranceWenzhouStatus.setType("pension");
        insuranceWenzhouStatus.setTaskId(insuranceRequestParameters.getTaskId());
        insuranceWenzhouStatusRepository.save(insuranceWenzhouStatus);
        //获取基本养老，城乡养老，机关养老信息
        AtomicInteger atomicInteger = new AtomicInteger(0);
        insuranceWenzhouBasicInfoService.getJibenYanglaoFlow(cookies,insuranceRequestParameters,atomicInteger);
        insuranceWenzhouBasicInfoService.getJiguanYanglaoFlow(cookies,insuranceRequestParameters,atomicInteger);
        insuranceWenzhouBasicInfoService.getChengxiangYanglaoFlow(cookies,insuranceRequestParameters,atomicInteger);
        insuranceWenzhouBasicInfoService.getChengxiangYanglaoBasicUserInfo(cookies,insuranceRequestParameters.getTaskId(),atomicInteger);
        insuranceWenzhouBasicInfoService.getJibenYanglaoBasicUserInfo(cookies,insuranceRequestParameters.getTaskId(),atomicInteger);
        insuranceWenzhouBasicInfoService.getJiguanYanglaoBasicUserInfo(cookies,insuranceRequestParameters.getTaskId(),atomicInteger);
    }

    /**
     * 爬取医疗保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getMedicalInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getMedical", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
        InsuranceWenzhouStatus insuranceWenzhouStatus = new InsuranceWenzhouStatus();
        insuranceWenzhouStatus.setCount(0);
        insuranceWenzhouStatus.setType("medical");
        insuranceWenzhouStatus.setTaskId(insuranceRequestParameters.getTaskId());
        insuranceWenzhouStatusRepository.save(insuranceWenzhouStatus);
        //获取基本养老，城乡养老，机关养老信息
        AtomicInteger atomicInteger = new AtomicInteger(0);
        insuranceWenzhouBasicInfoService.getJibenYiliaoBasicUserInfo(cookies,taskInsurance.getTaskid(),atomicInteger);
        insuranceWenzhouBasicInfoService.getChengxiangYiliaoBasicUserInfo(cookies,taskInsurance.getTaskid(),atomicInteger);
    }

    /**
     * 爬取工伤保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getGongshangInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getInjury", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());

        WebParam webParam = insuranceWenzhouBasicInfoService.getGongshangBasicUserInfo(cookies,taskInsurance.getTaskid());
//        System.out.println("webparam---->"+webParam);
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getGongshangInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
            insuranceWenzhouHtml.setPageCount(1);
            insuranceWenzhouHtml.setType("工伤保险");
            insuranceWenzhouHtml.setTaskId(insuranceRequestParameters.getTaskId());
            insuranceWenzhouHtml.setUrl(webParam.getUrl());
            insuranceWenzhouHtml.setHtml(html);
            insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);
            List<InsuranceWenzhouInsuranceBasic> gongShangInfo=webParam.getInsuranceBasic();
            if (null != gongShangInfo && gongShangInfo.size() > 0 ) {
                insuranceWenzhouInsuranceBasicRepository.saveAll(gongShangInfo);
                tracer.addTag("wenzhou-getUserinfo", "工伤保险信息已入库");
                insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 200, taskInsurance);

            }else{
                tracer.addTag("getUserinfo", "温州社保工伤保险信息未爬取到，无数据");
//                System.out.println("工商信息---入库");
                insuranceService.changeCrawlerStatus("温州社保工伤保险信息未爬取到，无数据",
                        InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 201, taskInsurance);

            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }
    /**
     * 爬取生育保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getBirthInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getBear", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());

        WebParam webParam = insuranceWenzhouBasicInfoService.getBirthBasicUserInfo(cookies,taskInsurance.getTaskid());
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getBirthInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
            insuranceWenzhouHtml.setPageCount(1);
            insuranceWenzhouHtml.setType("生育保险");
            insuranceWenzhouHtml.setTaskId(insuranceRequestParameters.getTaskId());
            insuranceWenzhouHtml.setUrl(webParam.getUrl());
            insuranceWenzhouHtml.setHtml(html);
            insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);
            List<InsuranceWenzhouInsuranceBasic> gongShangInfo=webParam.getInsuranceBasic();
            if (null != gongShangInfo && gongShangInfo.size() > 0 ) {
                insuranceWenzhouInsuranceBasicRepository.saveAll(gongShangInfo);
                tracer.addTag("wenzhou-getUserinfo", "生育保险信息已入库");
                insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 200, taskInsurance);
            }else{
                tracer.addTag("getUserinfo", "温州社保生育保险信息未爬取到，无数据");
                insuranceService.changeCrawlerStatus("温州社保生育保险信息未爬取到，无数据",
                        InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 201, taskInsurance);
            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }

    /**
     * 爬取失业保险
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getShiyeInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());

        WebParam webParam = insuranceWenzhouBasicInfoService.getShiyeBasicUserInfo(cookies,taskInsurance.getTaskid());
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getBirthInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
            insuranceWenzhouHtml.setPageCount(1);
            insuranceWenzhouHtml.setType("失业保险");
            insuranceWenzhouHtml.setTaskId(insuranceRequestParameters.getTaskId());
            insuranceWenzhouHtml.setUrl(webParam.getUrl());
            insuranceWenzhouHtml.setHtml(html);
            insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);
            List<InsuranceWenzhouInsuranceBasic> gongShangInfo=webParam.getInsuranceBasic();
            if (null != gongShangInfo && gongShangInfo.size() > 0 ) {
                insuranceWenzhouInsuranceBasicRepository.saveAll(gongShangInfo);
                tracer.addTag("wenzhou-getUserinfo", "失业保险信息已入库");
                insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 200, taskInsurance);
            }else{
                tracer.addTag("getUserinfo", "温州社保失业保险信息未爬取到，无数据");
                insuranceService.changeCrawlerStatus("温州社保失业保险信息未爬取到，无数据",
                        InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }

    /**
     * @Des 更新task表（success 登陆成功,cookie入库）
     * @param taskInsurance
     * @param htmlPage
     * @return
     */
    public TaskInsurance changeLoginStatusSuccess(TaskInsurance taskInsurance, WebParam webParam,String loginType) {

        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getDescription());
        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhase());
        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_SUCCESS.getPhasestatus());

        Map<String,String> map = new HashMap<String,String>();
        map.put("loginType",loginType);
        map.put("username",webParam.getUserName());
        map.put("password",webParam.getPassWord());
        String testHtml = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);

        taskInsurance.setTesthtml(testHtml);

        String cookies = CommonUnit.transcookieToJson(webParam.getPage().getWebClient());
        taskInsurance.setCookies(cookies);

        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
}
