package app.service;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceShenyangPensionParser;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangHtml;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangPaymentDetailsEachyear;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangPaymentPastYears;
import com.microservice.dao.entity.crawler.insurance.shenyang.InsuranceShenyangUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.shenyang.InsuranceShenyangHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.shenyang.InsuranceShenyangPaymentDetailsEachYearRepository;
import com.microservice.dao.repository.crawler.insurance.shenyang.InsuranceShenyangPaymentPastYearsRepository;
import com.microservice.dao.repository.crawler.insurance.shenyang.InsuranceShenyangUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mu on 2017/9/18.
 */
@SuppressWarnings("all")
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.shenyang"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.shenyang"})
public class InsuranceShenyangPensionService {
    @Autowired
    private TaskInsuranceRepository taskInsuranceRepository;
    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private InsuranceShenyangPensionParser insuranceShenyangPensionParser;
    @Autowired
    private InsuranceShenyangHtmlRepository insuranceShenyangHtmlRepository;
    @Autowired
    private InsuranceShenyangUserInfoRepository insuranceShenyangUserInfoRepository;
    @Autowired
    private InsuranceShenyangPaymentPastYearsRepository insuranceShenyangPaymentPastYearsRepository;
    @Autowired
    private InsuranceShenyangPaymentDetailsEachYearRepository insuranceShenyangPaymentDetailsEachYearRepository;
    @Autowired
    private TracerLog tracer;


    /**
     * 更改登陆状态-----正在登陆中
     * @param insuranceRequestParameters
     * @return
     */
    public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters,String medicalNum) {
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getDescription());
        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhase());
        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhasestatus());

        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    public TaskInsurance getTaskInsurance(InsuranceRequestParameters insuranceRequestParameters ) {
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
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
        Long start = System.currentTimeMillis();
        String loginType = insuranceRequestParameters.getLoginType();
        tracer.qryKeyValue("InsuranceShenyang-pensionService.login", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        System.out.println("沈阳养老保险第"+ loginCount +"次登陆");
        if (null != taskInsurance) {
            WebParam webParam = insuranceShenyangPensionParser.login(insuranceRequestParameters);
            if (null == webParam) {
                tracer.addTag("InsuranceShenyang-pensionService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                taskInsurance = changeLoginStatus(taskInsurance,null,"pension","timeout",webParam,loginType,status,loginNum);
                return taskInsurance;
            } else {
                String html = webParam.getHtml();
                tracer.addTag("InsuranceShenyang-pensionService.login",
                        insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
                Integer code=webParam.getCode();
                if (1001 == code) {
                    tracer.qryKeyValue(insuranceRequestParameters.getTaskId(), "养老保险登陆成功");
                    InsuranceShenyangHtml syhtml = new InsuranceShenyangHtml();
                    InsuranceShenyangHtml shenyangHtml = new InsuranceShenyangHtml();
                    shenyangHtml.setHtml(html);
                    shenyangHtml.setPageCount(1);
                    shenyangHtml.setTaskId(taskInsurance.getTaskid());
                    shenyangHtml.setType("pension");
                    shenyangHtml.setUrl(webParam.getUrl());
                    insuranceShenyangHtmlRepository.save(shenyangHtml);
                    //更改状态为养老登陆成功
                    taskInsurance = changeLoginStatus(taskInsurance,webParam.getPage(),"pension","success",webParam,loginType,status,loginNum);
                    Long end = System.currentTimeMillis();
                    System.out.println("\n登陆耗时:"+ (end-start));
                    return taskInsurance;
                }else if(1002 == code){
                    tracer.qryKeyValue(insuranceRequestParameters.getTaskId(), "附加码错误");
                    taskInsurance = changeLoginStatus(taskInsurance,null,"pension","pwderror",webParam,loginType,status,loginNum);
                    return taskInsurance;
                }else if(1003 == code){
                    if (loginCount < 4) {
                        int i = status.get();
                        if(i != 4)
                            login(insuranceRequestParameters, ++loginCount,status,loginNum);
                    }else{
                        tracer.qryKeyValue(insuranceRequestParameters.getTaskId(), "超级鹰识别超过三次");
                        taskInsurance = insuranceService.changeLoginStatusCaptError(taskInsurance);
                        return taskInsurance;
                    }
                    tracer.addTag("InsuranceShenyang-pensionService.login" + insuranceRequestParameters.getTaskId(),"登录失败次数" + loginCount);
                }else if(1004 == code){
                    tracer.addTag(insuranceRequestParameters.getTaskId(),"账户错误");
                    taskInsurance = changeLoginStatus(taskInsurance,null,"pension","accounterror",webParam,loginType,status,loginNum);
                    return taskInsurance;
                }else{
                    tracer.addTag("InsuranceShenyang-pensionService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
                    taskInsurance = changeLoginStatus(taskInsurance,null,"pension","timeout",webParam,loginType,status,loginNum);
                    return taskInsurance;
                }
            }
        }
        return null;
    }
    /**
     * @Des 判断当前状态是否爬取完毕
     * @param insuranceRequestParameters
     * @return
     */
    public boolean isDoing(InsuranceRequestParameters insuranceRequestParameters){
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        if(null == taskInsurance){
            return true;
        }
        if(taskInsurance.getPhase().equals("CRAWLER") && taskInsurance.getPhase_status().equals("DOING")){
            return true;
        }
        taskInsurance.setYanglaoStatus(null);
        taskInsurance.setShengyuStatus(null);
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return false;
    }

    /**
     * @Des 更新task表（doing 正在采集）
     * @param insuranceRequestParameters
     */
    public TaskInsurance changeCrawlerStatusDoing(InsuranceRequestParameters insuranceRequestParameters) {

        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getDescription());
        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhase());
        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_DOING.getPhasestatus());
        taskInsurance.setCity(insuranceRequestParameters.getCity());
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    //判断用户信息，养老，医疗，失业是否全部爬取完毕

    /**
     * 爬取用户数据
     * @param insuranceRequestParameters
     * @throws Exception
     */
    @Async
    public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {

        tracer.qryKeyValue("parser.crawler.getUserinfo", insuranceRequestParameters.getTaskId());

        tracer.qryKeyValue("parser.crawler.getInjury", insuranceRequestParameters.getTaskId());
        tracer.qryKeyValue("parser.crawler.getMedical", insuranceRequestParameters.getTaskId());
        tracer.qryKeyValue("parser.crawler.getBear", insuranceRequestParameters.getTaskId());
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
        insuranceService.changeCrawlerStatus("工商保险数据爬取完毕,无数据",
                "CRAWLER_GONGSHANG_MSG", 201, taskInsurance);
        insuranceService.changeCrawlerStatus("医疗保险数据爬取完毕,无数据",
                "CRAWLER_YILIAO_MSG", 201, taskInsurance);
        insuranceService.changeCrawlerStatus("生育保险数据爬取完毕,无数据",
                "CRAWLER_SHENGYU_MSG", 201, taskInsurance);

        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
        List<InsuranceShenyangHtml> insuranceShenyangHtml = insuranceShenyangHtmlRepository.findByTaskIdAndType(insuranceRequestParameters.getTaskId(),"pension");
        WebParam webParam = null;
        if(insuranceShenyangHtml.size()>0)
            webParam = insuranceShenyangPensionParser.getUserInfo(insuranceShenyangHtml.get(0),cookies);
        if(null != webParam) {
            String html = webParam.getHtml();
            tracer.addTag("shenyang-pensioncrawler-getUserInfo",
                    insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");

            InsuranceShenyangUserInfo shenyangUserInfo = webParam.getUserInfo();
//            System.out.println("shenyangUserInfo=="+shenyangUserInfo);
            if (null != shenyangUserInfo) {
                insuranceShenyangUserInfoRepository.save(shenyangUserInfo);
                tracer.addTag("shenyang-pensioncrawler-getUserInfo", "社保个人信息已入库");
                insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 200, taskInsurance);
            }else{
                tracer.qryKeyValue("shenyang-pensioncrawler-getUserInfo", "沈阳社保个人信息未爬取到，无数据");
                insuranceService.changeCrawlerStatusUserInfo(taskInsurance, 201);
            }
            List<InsuranceShenyangPaymentPastYears> ppys = webParam.getPaymentPastYearsList();
            if(ppys.size()>0) {
                insuranceShenyangPaymentPastYearsRepository.saveAll(ppys);
                for(InsuranceShenyangPaymentPastYears ppy:ppys){
                    getPaymentDetails(ppy,taskInsurance,cookies);
                }
            }
        }
    }

    /**
     * 异步爬取社保流水
     * @param ppy
     * @param taskInsurance
     * @param cookies
     * @throws Exception
     */
    @Async
    public void getPaymentDetails(InsuranceShenyangPaymentPastYears ppy, TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {
        tracer.addTag("parser.crawler.getPension", taskInsurance.getTaskid());
        if(null != ppy){
            WebParam webParam = insuranceShenyangPensionParser.getPaymentDetailsEachYearList(ppy,taskInsurance,cookies);
            String html = webParam.getHtml();
            tracer.addTag("shenyang-pensioncrawler-getPaymentDetails", "<xmp>" + html + "<xmp>" );
            InsuranceShenyangHtml shenyangHtml = new InsuranceShenyangHtml();
            shenyangHtml.setHtml(html);
            shenyangHtml.setPageCount(1);
            shenyangHtml.setTaskId(taskInsurance.getTaskid());
            shenyangHtml.setType("PaymentDetail");
            shenyangHtml.setUrl(webParam.getUrl());
            insuranceShenyangHtmlRepository.save(shenyangHtml);
            List<InsuranceShenyangPaymentDetailsEachyear>  pde = webParam.getPaymentDetailsEachyearList();
            if(pde.size()>0){
                insuranceShenyangPaymentDetailsEachYearRepository.saveAll(pde);
                insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 200, taskInsurance);

                tracer.qryKeyValue("shenyang-pensioncrawler-getPaymentDetails", "社保信息已入库");
            }else{
                insuranceService.changeCrawlerStatus("养老保险数据爬取完毕,无数据",
                        "CRAWLER_YANGLAO_MSG", 201, taskInsurance);
            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
    }

    /**
     * 同步更改登陆状态
     * @param taskInsurance
     * @param htmlPage
     * @param type
     * @param status
     * @return
     */
    public  TaskInsurance changeLoginStatus(TaskInsurance taskInsurance,HtmlPage htmlPage,String type,String status,WebParam webParam,String loginType,AtomicInteger loginStatus,AtomicInteger loginNum){
        if("pension".equals(type) && "success".equals(status)){
            return changeLoginYangLaSuccescc(taskInsurance,htmlPage,webParam,loginType,loginStatus,loginNum);
        }else if("pension".equals(type) && "timeout".equals(status)){
            return changeLoginYangLaoStatusTimeOut(taskInsurance,loginStatus);
        }else if("pension".equals(type) && "pwderror".equals(status)){
            return changeLoginYangLaoPasswordError(taskInsurance,loginStatus);
        }else if("pension".equals(type) && "accounterror".equals(status)){
            return changeLoginYangLaoAccountError(taskInsurance,loginStatus);
        }else if("unemployee".equals(type) && "success".equals(status)){
            return changeLoginShiyeSuccescc(taskInsurance,htmlPage,webParam,loginType,loginStatus,loginNum);
        }else if("unemployee".equals(type) && "timeout".equals(status)){
            return changeLoginShiyeTimeout(taskInsurance,loginStatus);
        }else if("unemployee".equals(type) && "pwderror".equals(status)){
            return changeLoginShiyePwdError(taskInsurance,loginStatus);
        }else if("unemployee".equals(type) && "accounterror".equals(status)){
            return changeLoginShiyeAccountError(taskInsurance,loginStatus);
        }
        return null;
    }

    /**
     * 更改养老保险登陆状态
     * @param taskInsurance
     * @return
     */
    public TaskInsurance changeLoginYangLaoStatusTimeOut(TaskInsurance taskInsurance,AtomicInteger status) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
        taskInsurance.setDescription("养老保险登陆超时");
        taskInsurance.setPhase("LOGIN");
        taskInsurance.setPhase_status("ERROR");
        status.set(4);
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    public TaskInsurance changeLoginYangLaoPasswordError(TaskInsurance taskInsurance,AtomicInteger status) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
        taskInsurance.setDescription("养老保险附加码错误");
        taskInsurance.setPhase("LOGIN");
        taskInsurance.setPhase_status("ERROR");
        status.set(4);
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    public TaskInsurance changeLoginYangLaoAccountError(TaskInsurance taskInsurance,AtomicInteger status) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
        taskInsurance.setDescription("养老保险账号错误!!!");
        taskInsurance.setPhase("LOGIN");
        taskInsurance.setPhase_status("ERROR");
        status.set(4);
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    public TaskInsurance changeLoginYangLaSuccescc(TaskInsurance taskInsurance,HtmlPage htmlPage,WebParam webParam,String loginType,AtomicInteger status,AtomicInteger loginNum) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());

        int i = status.get();
//        System.out.println("this is yanglao "+ i);
        if(i!=4) {
            taskInsurance.setDescription("养老登陆成功!!!");
            taskInsurance.setPhase("LOGIN");
            String cookies = CommonUnit.transcookieToJson(htmlPage.getWebClient());
            taskInsurance.setCookies(cookies);
            status.getAndIncrement();
            taskInsurance.setPhase_status("YANG  LAO  SUCCESS");

            Map<String,String> map = new HashMap<String,String>();
            map.put("loginType",loginType);
            map.put("username",webParam.getUserName());
            map.put("password",webParam.getPassWord());
            String testHtml = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
            JSONObject jsonObject = JSON.parseObject(testHtml);

            JSONArray array = null;
            String before = taskInsurance.getTesthtml();
            if(before == null || before.trim().equals(""))
                array = new JSONArray();
            else
                array = JSON.parseArray(before);

            array.add(jsonObject);
            taskInsurance.setTesthtml(array.toJSONString());
        }
        if(status.get() == loginNum.get())
            taskInsurance.setPhase_status("SUCCESS");
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
//        System.out.println(i +" this is yanglao ---->>>>" + taskInsurance);
        return taskInsurance;
    }



    /**
     * 失业保险账户错误(身份证号码错误)
     * @param taskInsurance
     * @return
     */
    public TaskInsurance changeLoginShiyeAccountError(TaskInsurance taskInsurance ,AtomicInteger status) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
        taskInsurance.setDescription("失业保险账户错误(身份证号码错误)!!!");
        taskInsurance.setPhase("LOGIN");
        taskInsurance.setPhase_status("ERROR");
        status.set(4);
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    public TaskInsurance changeLoginShiyeTimeout(TaskInsurance taskInsurance,AtomicInteger status) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
        taskInsurance.setDescription("失业保险登陆超时!!!");
        taskInsurance.setPhase("LOGIN");
        taskInsurance.setPhase_status("ERROR");
        status.set(4);
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    public TaskInsurance changeLoginShiyePwdError(TaskInsurance taskInsurance,AtomicInteger status) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
        taskInsurance.setDescription("失业保险密码校验错误!!!");
        taskInsurance.setPhase("LOGIN");
        taskInsurance.setPhase_status("ERROR");
        status.set(4);
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;
    }
    public TaskInsurance changeLoginShiyeSuccescc(TaskInsurance taskInsurance,HtmlPage htmlPage,WebParam webParam,String loginType,AtomicInteger status,AtomicInteger loginNum) {
        taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
        int i = status.get();
//        System.out.println("this is shiye " + i);
        if(i!=4) {
            taskInsurance.setDescription("失业保险登陆成功!!!");
            taskInsurance.setPhase("LOGIN");
            String cookies = CommonUnit.transcookieToJson(htmlPage.getWebClient());
            taskInsurance.setCookies(cookies);
            taskInsurance.setPhase_status("SHI  YE  SUCCESS");
            status.getAndIncrement();
            Map<String,String> map = new HashMap<String,String>();
            map.put("loginType",loginType);
            map.put("username",webParam.getUserName());
            map.put("password",webParam.getPassWord());
            String testHtml = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
            JSONObject jsonObject = JSON.parseObject(testHtml);

            JSONArray array = null;
            String before = taskInsurance.getTesthtml();
            if(before == null || before.trim().equals(""))
                array = new JSONArray();
            else
                array = JSON.parseArray(before);

            array.add(jsonObject);
            taskInsurance.setTesthtml(array.toJSONString());

        }
        if(status.get() == loginNum.get()) {
            taskInsurance.setPhase_status("SUCCESS");

        }
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
//        System.out.println( i + "this is shiye ---->>>>" + taskInsurance);
        return taskInsurance;
    }
}
