package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouHtml;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouInsuranceBasic;
import com.microservice.dao.entity.crawler.insurance.wenzhou.InsuranceWenzhouPensionPaywater;
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
public class InsuranceWenzhouBasicInfoService {
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
    private InsuranceWenzhouStatusRepository insuranceWenzhouStatusRepository;
    @Autowired
    private InsuranceWenzhouPensionPaywaterRepository insuranceWenzhouPensionPaywaterRepository;
    @Autowired
    private InsuranceWenzhouUserinfoRepository insuranceWenzhouUserinfoRepository;
    @Autowired
    private TracerLog tracer;

    /**
     * 爬取基本养老公用信息
     * @param cookies
     * @throws Exception
     */
    @Async
    public void getJibenYanglaoBasicUserInfo(Set<Cookie> cookies,String taskId,AtomicInteger atomicInteger) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_grjbxx&_t="+System.currentTimeMillis();
        UnexpectedPage page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("基本养老用户信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setTaskId(taskId);
            basic.setInsuranceType("基本养老保险");
            list.add(basic);
//            System.out.println(basic);
        }

        tracer.addTag("wenzhou-getBasicPensionFlow",
                taskId + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("基本养老保险-个人信息");
        insuranceWenzhouHtml.setTaskId(taskId);
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouInsuranceBasicRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 基本养老保个人信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", taskId+"基本养老保个人信息，无数据");
        }
        changeCrawlerStatus(taskId,"pension",atomicInteger);
    }
    //获取城乡养老基本信息
    @Async
    public void getChengxiangYanglaoBasicUserInfo(Set<Cookie> cookies,String taskId,AtomicInteger atomicInteger) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_cjylxx&_t="+System.currentTimeMillis();
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("城乡养老基本信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceStatus(bean.getString("aac008"));
            basic.setInsurancePayStatus(bean.getString("aac031"));
            basic.setTaskId(taskId);
            basic.setInsuranceType("城乡养老保险");
            list.add(basic);
        }

        tracer.addTag("wenzhou-getBasicPensionFlow",
                taskId + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("城乡养老保险-个人信息");
        insuranceWenzhouHtml.setTaskId(taskId);
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouInsuranceBasicRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 城乡养老保个人信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", taskId+"城乡养老保个人信息，无数据");
        }
        changeCrawlerStatus(taskId,"pension",atomicInteger);
    }

    //获取基本医疗信息
    @Async
    public void getJibenYiliaoBasicUserInfo(Set<Cookie> cookies,String taskId,AtomicInteger atomicInteger) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_ybxxcx&_t="+System.currentTimeMillis();
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("基本医疗信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setInsuranceNum(bean.getString("aac001"));
            basic.setUserName(bean.getString("aac003"));
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setWorkStatus(bean.getString("ryxz"));
            basic.setComment("{\"医疗类型\":\""+bean.getString("mdtype")+"\",\"门诊类型\":\""+bean.getString("dmtype") +"\"}");
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setCompanyNum(bean.getString("aab001"));
            basic.setTaskId(taskId);
            basic.setInsuranceType("基本医疗保险");
            list.add(basic);
//            System.out.println(basic);
        }

        tracer.addTag("wenzhou-getBasicPensionFlow",
                taskId + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("基本医疗保险-个人信息");
        insuranceWenzhouHtml.setTaskId(taskId);
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouInsuranceBasicRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 基本医疗保险个人信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 基本医疗保险保个人信息，无数据");
        }
        changeCrawlerStatus(taskId,"medical",atomicInteger);
    }

    //获取城乡医疗信息
    @Async
    public void getChengxiangYiliaoBasicUserInfo(Set<Cookie> cookies,String taskId,AtomicInteger atomicInteger) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_cjybxx&_t" + timeMilions;
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("城乡医疗用户信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setUserName(bean.getString("aac003"));
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setInsuranceNum(bean.getString("aac001"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("城乡医疗保险");
            basic.setTaskId(taskId);
            list.add(basic);
        }

        tracer.addTag("wenzhou-getBasicPensionFlow",
                taskId + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("城乡医疗保险-个人信息");
        insuranceWenzhouHtml.setTaskId(taskId);
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouInsuranceBasicRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 城乡医疗保险个人信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 城乡医疗保险保个人信息，无数据");
        }
        changeCrawlerStatus(taskId,"medical",atomicInteger);
    }

    //获取工伤基本信息

    public WebParam getGongshangBasicUserInfo(Set<Cookie> cookies,String taskId) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_gssyxx&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("工伤保险信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("idcard"));
            basic.setUserName(bean.getString("name"));
            basic.setInsuranceCompanyName(bean.getString("corp_name"));
            basic.setInsuranceStatus(bean.getString("indi_join_sta"));
            basic.setInsuranceType("工伤保险");
            basic.setTaskId(taskId);
            list.add(basic);
        }
        webParam.setUrl(url);
        webParam.setHtml(html);
        webParam.setCode(page.getWebResponse().getStatusCode());
        webParam.setPg(page);
        webParam.setInsuranceBasic(list);
//        System.out.println("webparam ====>>>>"+webParam);
        return webParam;
    }

    //获取生育基本信息

    public WebParam getBirthBasicUserInfo(Set<Cookie> cookies,String taskId) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_shengyxx&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("生育保险信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("生育保险");
            basic.setTaskId(taskId);
            list.add(basic);
        }
        webParam.setUrl(url);
        webParam.setHtml(html);
        webParam.setCode(page.getWebResponse().getStatusCode());
        webParam.setPg(page);
        webParam.setInsuranceBasic(list);
        return webParam;
    }

    //获取失业保险基本信息

    public WebParam getShiyeBasicUserInfo(Set<Cookie> cookies,String taskId) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_syjbxx&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("失业保险信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("失业保险");
            basic.setTaskId(taskId);
            list.add(basic);
        }
        webParam.setUrl(url);
        webParam.setHtml(html);
        webParam.setCode(page.getWebResponse().getStatusCode());
        webParam.setPg(page);
        webParam.setInsuranceBasic(list);
        return webParam;
    }
    //获取机关养老保险基本信息
    @Async
    public void getJiguanYanglaoBasicUserInfo(Set<Cookie> cookies,String taskId,AtomicInteger atomicInteger) throws Exception {
        List<InsuranceWenzhouInsuranceBasic> list = new ArrayList<InsuranceWenzhouInsuranceBasic>();
        WebParam webParam= new WebParam();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/XXXLTgyYTktYzhiOGU2YmYzN2Zi?dataset=sbcx_sheng$sbcx_grjbxx_jg&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("机关养老保险信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouInsuranceBasic basic = new InsuranceWenzhouInsuranceBasic();
            basic.setSocialInsuranceNum(bean.getString("aac002"));
            basic.setUserName(bean.getString("aac003"));
            basic.setInsuranceCompanyName(bean.getString("aab004"));
            basic.setInsuranceStatus(bean.getString("aac031"));
            basic.setInsuranceType("机关养老保险");
            basic.setTaskId(taskId);
            list.add(basic);
        }

        tracer.addTag("wenzhou-getBasicPensionFlow",
                taskId + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("机关养老保险-个人信息");
        insuranceWenzhouHtml.setTaskId(taskId);
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouInsuranceBasicRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 机关养老保个人信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", taskId+" 机关养老保个人信息，无数据");
        }
        changeCrawlerStatus(taskId,"pension",atomicInteger);
    }

    /**
     * 获取基本养老流水
     * @param cookies,reqParam
     * @return
     * @throws Exception
     */
    public void getJibenYanglaoFlow(Set<Cookie> cookies , InsuranceRequestParameters reqParam, AtomicInteger atomicInteger) throws Exception {
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(reqParam.getTaskId());
        List<InsuranceWenzhouPensionPaywater> list = new ArrayList<InsuranceWenzhouPensionPaywater>();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);

        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_yljf&order=asc&limit=120&offset=0&pageNo=1&pageSize=120&_=" + timeMilions + "&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        int statusCode = page.getWebResponse().getStatusCode();
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("获取基本养老流水信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouPensionPaywater water = new InsuranceWenzhouPensionPaywater();
            water.setPayBase(bean.getString("jfjs"));
            water.setPayCompany(bean.getString("aab004"));
            water.setPayMonth(bean.getString("aae002"));
            water.setPersonalPayMoney(bean.getString("gryj"));
            water.setTransferStatus(bean.getString("aae111"));
            water.setTaskId(reqParam.getTaskId());
            water.setInsuranceType("基本养老保险");
            list.add(water);
        }


        tracer.addTag("wenzhou-getBasicPensionFlow",
                reqParam.getTaskId() + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("基本养老缴费流水");
        insuranceWenzhouHtml.setTaskId(reqParam.getTaskId());
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouPensionPaywaterRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", reqParam.getTaskId()+" 基本养老保险缴费流水信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", reqParam.getTaskId()+"基本养老保险缴费流水信息，无数据");
        }
        changeCrawlerStatus(reqParam.getTaskId(),"pension",atomicInteger);
    }
    /**
     * 获取城乡养老流水
     * @param cookies,reqParam
     * @return
     * @throws Exception
     */
    public void getChengxiangYanglaoFlow(Set<Cookie> cookies ,InsuranceRequestParameters reqParam,AtomicInteger atomicInteger) throws Exception {
        WebParam webParam= new WebParam();
        List<InsuranceWenzhouPensionPaywater> list = new ArrayList<InsuranceWenzhouPensionPaywater>();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);

        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NmJlLWEzMjgtMTE1OTEyYTQxMzJk?dataset=sbcx_sheng$sbcx_cjyljf&order=asc&limit=120&offset=0&pageNo=1&pageSize=120&_=" + timeMilions + "&_t=" + timeMilions;
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("获取城乡养老流水信息--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouPensionPaywater water = new InsuranceWenzhouPensionPaywater();
            water.setPayBase(bean.getString("jfjs"));
            water.setPayCompany(bean.getString("aab004"));
            water.setPayMonth(bean.getString("aae002"));
            water.setPersonalPayMoney(bean.getString("gryj"));
            water.setTransferStatus(bean.getString("aae111"));
            water.setTaskId(reqParam.getTaskId());
            water.setInsuranceType("城乡养老保险");
            list.add(water);
        }

        tracer.addTag("wenzhou-getBasicPensionFlow",
                reqParam.getTaskId() + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("城乡养老缴费流水");
        insuranceWenzhouHtml.setTaskId(reqParam.getTaskId());
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouPensionPaywaterRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", reqParam.getTaskId()+" 城乡养老保险缴费流水信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", reqParam.getTaskId()+"城乡养老保险缴费流水信息，无数据");
        }
        changeCrawlerStatus(reqParam.getTaskId(),"pension",atomicInteger);

    }

    /**
     * 获取机关养老流水
     * @param cookies,reqParam
     * @return
     * @throws Exception
     */
    public void getJiguanYanglaoFlow(Set<Cookie> cookies ,InsuranceRequestParameters reqParam,AtomicInteger atomicInteger) throws Exception {
        WebParam webParam= new WebParam();
        List<InsuranceWenzhouPensionPaywater> list = new ArrayList<InsuranceWenzhouPensionPaywater>();
        WebClient webClient = insuranceWenzhouParser.getWebClient(cookies);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(false);

        long timeMilions = System.currentTimeMillis();
        String url = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/XXXLTgyYTktYzhiOGU2YmYzN2Zi?dataset=sbcx_sheng$sbcx_yljf_jg&order=asc&limit=120&offset=0&pageNo=1&pageSize=120&_t=" + timeMilions + "&_=" + timeMilions;
        Page page = webClient.getPage(url);
        String html = page.getWebResponse().getContentAsString();
//        System.out.println("获取机关养老流水--->>>" + html);
        JSONObject json = JSON.parseObject(html);
        JSONArray jsonArray = json.getJSONArray("data");
        for(JSONObject bean:jsonArray.toJavaList(JSONObject.class)){
            InsuranceWenzhouPensionPaywater water = new InsuranceWenzhouPensionPaywater();
            water.setPayMonth(bean.getString("aae002"));
            water.setPayBase(bean.getString("jfjs"));
            water.setPersonalPayMoney(bean.getString("gryj"));
            water.setPayType(bean.getString("aae140"));
            water.setInsuranceType(bean.getString("aae111"));
            water.setTaskId(reqParam.getTaskId());
            water.setInsuranceType("机关养老保险");
//            System.out.println(water);
        }

        tracer.addTag("wenzhou-getJiguanPensionFlow",
                reqParam.getTaskId() + "  <xmp>" + html + "</xmp>");
        InsuranceWenzhouHtml insuranceWenzhouHtml = new InsuranceWenzhouHtml();
        insuranceWenzhouHtml.setPageCount(1);
        insuranceWenzhouHtml.setType("机关养老缴费流水");
        insuranceWenzhouHtml.setTaskId(reqParam.getTaskId());
        insuranceWenzhouHtml.setUrl(url);
        insuranceWenzhouHtml.setHtml(html);
        insuranceWenzhouHtmlRepository.save(insuranceWenzhouHtml);

        if (list.size()>0) {
            insuranceWenzhouPensionPaywaterRepository.saveAll(list);
            tracer.addTag("wenzhou-getPensionInfo", reqParam.getTaskId()+" 机关养老保险缴费流水信息已入库");
        }else{
            tracer.addTag("wenzhou-getPensionInfo", reqParam.getTaskId()+" 机关养老保险缴费流水信息，无数据");
        }
        changeCrawlerStatus(reqParam.getTaskId(),"pension",atomicInteger);
    }

    /**
     * @Des 更新task表（养老信息入库）
     * @param taskInsurance
     * @param code
     * @return
     */
    public synchronized void  changeCrawlerStatus(String taskId,String statusType,AtomicInteger atomicInteger) {
        int count = atomicInteger.get() + 1;

        if("pension".equals(statusType)){
            if( count < 6 ) {

                atomicInteger.incrementAndGet();
            }else{
                TaskInsurance taskInsurance  = taskInsuranceRepository.findByTaskid(taskId);
                insuranceService.changeCrawlerStatus( InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase()
                        , 200, taskInsurance);
            }
        }else if("medical".equals(statusType)){
            if( count < 2 ) {

                atomicInteger.incrementAndGet();
            }else{
                TaskInsurance taskInsurance  = taskInsuranceRepository.findByTaskid(taskId);
                insuranceService.changeCrawlerStatus( InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),
                        InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase()
                        , 200, taskInsurance);
            }
        }
        insuranceService.changeCrawlerStatusSuccess(taskId);
    }

}
