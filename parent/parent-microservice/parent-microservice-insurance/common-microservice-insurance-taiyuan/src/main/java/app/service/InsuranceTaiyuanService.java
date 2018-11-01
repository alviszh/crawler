package app.service;

import static com.crawler.insurance.json.InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS;
import static com.crawler.insurance.json.InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanFirst;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanHtml;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanResidentWater;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanStaffWater;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanUserinfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.taiyuan.InsuranceTaiyuanFirstRepository;
import com.microservice.dao.repository.crawler.insurance.taiyuan.InsuranceTaiyuanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.taiyuan.InsuranceTaiyuanResidentWaterRepository;
import com.microservice.dao.repository.crawler.insurance.taiyuan.InsuranceTaiyuanStaffWaterRepository;
import com.microservice.dao.repository.crawler.insurance.taiyuan.InsuranceTaiyuanUserinfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceTaiyuanParser;

/**
 * Created by Mu on 2017/9/18.
 */
@SuppressWarnings("all")
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.taiyuan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.taiyuan"})
public class InsuranceTaiyuanService {
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private InsuranceTaiyuanParser insuranceTaiyuanParser;
    @Autowired
    private InsuranceTaiyuanUserinfoRepository insuranceTaiyuanUserinfoRepository;
    @Autowired
    private InsuranceTaiyuanFirstRepository insuranceTaiyuanFirstRepository;
    @Autowired
    private InsuranceTaiyuanResidentWaterRepository insuranceTaiyuanResidentWaterRepository;
    @Autowired
    private InsuranceTaiyuanStaffWaterRepository insuranceTaiyuanStaffWaterRepository;
    @Autowired
    private InsuranceTaiyuanHtmlRepository insuranceTaiyuanHtmlRepository;

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

        tracer.addTag("InsuranceTaiyuan-Service.login", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        WebParam webParam = null;
        if (null != taskInsurance) {

            webParam = insuranceTaiyuanParser.login(insuranceRequestParameters);
            if (null == webParam) {
                tracer.addTag("InsuranceTaiyuan-Service.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
                return taskInsurance;
            } else {
                String html = webParam.getHtml();
                tracer.addTag("InsuranceTaiyuan-service.login",
                        insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
                Integer code=webParam.getCode();
                if (1001 == code) {
                    tracer.addTag(insuranceRequestParameters.getTaskId(),"登陆成功");
                    InsuranceTaiyuanHtml tyHtml = new InsuranceTaiyuanHtml();
                    tyHtml.setHtml(html);
                    tyHtml.setPageCount(1);
                    tyHtml.setTaskId(taskInsurance.getTaskid());
                    tyHtml.setType("login");
                    tyHtml.setUrl(webParam.getUrl());
                    insuranceTaiyuanHtmlRepository.save(tyHtml);
                    taskInsurance =  changeLoginStatusSuccess(taskInsurance, webParam,"");
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
                    tracer.addTag("InsuranceTaiyuan-Service.login" + insuranceRequestParameters.getTaskId(),"登录失败次数" + loginCount);
                }else{
                    tracer.addTag("InsuranceTaiyuan-Service.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
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
        WebParam webParam = insuranceTaiyuanParser.getUserInfo(cookies,insuranceRequestParameters);
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getUserInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceTaiyuanHtml tyHtml = new InsuranceTaiyuanHtml();
            tyHtml.setPageCount(1);
            tyHtml.setType("用户个人信息");
            tyHtml.setTaskId(insuranceRequestParameters.getTaskId());
            tyHtml.setUrl(webParam.getUrl());
            tyHtml.setHtml(html);
            insuranceTaiyuanHtmlRepository.save(tyHtml);
            List<InsuranceTaiyuanUserinfo> userInfo=webParam.getUserinfos();
            if (null !=userInfo ) {
                insuranceTaiyuanUserinfoRepository.saveAll(userInfo);
                tracer.addTag("taiyuan-getUserinfo", "社保个人信息已入库");
                insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
            }else{
                tracer.addTag("getUserinfo", "太远社保个人信息未爬取到，无数据");
                insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 201);
            }
        }


        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }

    /**
     * 爬取职工首次缴费信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getStaffFirst(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());

        WebParam webParam = insuranceTaiyuanParser.getStaffFirst(cookies,insuranceRequestParameters);
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getStaffFirstInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceTaiyuanHtml tyHtml = new InsuranceTaiyuanHtml();
            tyHtml.setPageCount(1);
            tyHtml.setType("职工首次缴费信息");
            tyHtml.setTaskId(insuranceRequestParameters.getTaskId());
            tyHtml.setUrl(webParam.getUrl());
            tyHtml.setHtml(html);
            insuranceTaiyuanHtmlRepository.save(tyHtml);
            List<InsuranceTaiyuanFirst> firsts=webParam.getFirsts();
            if (null != firsts ) {
                insuranceTaiyuanFirstRepository.saveAll(firsts);
                tracer.addTag("taiyuan-getFirsts", "职工首次缴费信息已入库");
            }else{
                tracer.addTag("taiyuan-getFirsts", "太原职工首次缴费信息未爬取到，无数据");
            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }
    /**
     * 爬取居民首次缴费信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getJuminFirst(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());

        WebParam webParam = insuranceTaiyuanParser.getJuminFirst(cookies,insuranceRequestParameters);
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getJuminFirst",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceTaiyuanHtml tyHtml = new InsuranceTaiyuanHtml();
            tyHtml.setPageCount(1);
            tyHtml.setType("居民首次缴费信息");
            tyHtml.setTaskId(insuranceRequestParameters.getTaskId());
            tyHtml.setUrl(webParam.getUrl());
            tyHtml.setHtml(html);
            insuranceTaiyuanHtmlRepository.save(tyHtml);
            List<InsuranceTaiyuanFirst> firsts=webParam.getFirsts();
            if (null != firsts ) {
                insuranceTaiyuanFirstRepository.saveAll(firsts);
                tracer.addTag("taiyuan-getJuminFirst", "太远居民首次缴费信息已入库");
            }else{
                tracer.addTag("taiyuan-getJuminFirst", "太远居民首次缴费信息未爬取到，无数据");
            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }

    /**
     * 爬取职工缴费流水信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getStaffPayInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {


        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());

        WebParam webParam = insuranceTaiyuanParser.getStaffPayInfo(cookies,insuranceRequestParameters);
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getStaffPayInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceTaiyuanHtml tyHtml = new InsuranceTaiyuanHtml();
            tyHtml.setPageCount(1);
            tyHtml.setType("职工缴费流水信息");
            tyHtml.setTaskId(insuranceRequestParameters.getTaskId());
            tyHtml.setUrl(webParam.getUrl());
            tyHtml.setHtml(html);
            insuranceTaiyuanHtmlRepository.save(tyHtml);
            List<InsuranceTaiyuanStaffWater> staffWaters=webParam.getStaffWaters();
            if (null != staffWaters && staffWaters.size()>0) {

                insuranceTaiyuanStaffWaterRepository.saveAll(staffWaters);

                tracer.addTag("taiyuan-getStaffWaters", "太原职工缴费流水信息已入库");
            }else{
                tracer.addTag("taiyuan-getStaffWaters", "太原职工缴费流水信息未爬取到，无数据");
            }
        }
        tracer.addTag("parser.crawler.getPension", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.getMedical", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.getInjury", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.getBear", insuranceRequestParameters.getTaskId());
        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());
        insuranceService.changeCrawlerStatus("数据采集中，工伤保险数据爬取完毕",
                "CRAWLER_GONGSHANG_MSG", 200, taskInsurance);
        insuranceService.changeCrawlerStatus("数据采集中，医疗保险数据爬取完毕",
                "CRAWLER_YILIAO_MSG", 200, taskInsurance);
        insuranceService.changeCrawlerStatus("数据采集中，生育保险数据爬取完毕",
                "CRAWLER_SHENGYU_MSG", 200, taskInsurance);
        insuranceService.changeCrawlerStatus("失业保险无数据",
                INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 201, taskInsurance);
        insuranceService.changeCrawlerStatus("养老保险无数据",
                INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);

        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }
    /**
     * 爬取居民缴费流水信息
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getJuminPayInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        tracer.addTag("parser.crawler.getUnemployment", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());

        WebParam webParam = insuranceTaiyuanParser.getJuminPayInfo(cookies,insuranceRequestParameters);
        if(null != webParam){
            String html=webParam.getHtml();
            tracer.addTag("getStaffPayInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
            InsuranceTaiyuanHtml tyHtml = new InsuranceTaiyuanHtml();
            tyHtml.setPageCount(1);
            tyHtml.setType("居民缴费流水信息");
            tyHtml.setTaskId(insuranceRequestParameters.getTaskId());
            tyHtml.setUrl(webParam.getUrl());
            tyHtml.setHtml(html);
            insuranceTaiyuanHtmlRepository.save(tyHtml);
            List<InsuranceTaiyuanResidentWater> residentWaters=webParam.getResidentWaters();
            if (null != residentWaters && residentWaters.size()>0) {
                insuranceTaiyuanResidentWaterRepository.saveAll(residentWaters);
                tracer.addTag("taiyuan-getResidentWaters", "太原居民缴费流水信息已入库");
            }else{
                tracer.addTag("taiyuan-getResidentWaters", "太原居民缴费流水信息未爬取到，无数据");
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
