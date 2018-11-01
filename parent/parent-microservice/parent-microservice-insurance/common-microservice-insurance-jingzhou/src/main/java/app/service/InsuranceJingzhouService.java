package app.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsuranceJingzhouBasicBean;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouBaseInfo;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouShiyeInfo;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouYanglaoInfo;
import com.microservice.dao.entity.crawler.insurance.jingzhou.InsurancejingzhouYibaoInfo;
import com.microservice.dao.repository.crawler.insurance.jingzhou.InsurancejingzhouBaseInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jingzhou.InsurancejingzhouGongShangInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jingzhou.InsurancejingzhouShengYuInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jingzhou.InsurancejingzhouShiyeInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jingzhou.InsurancejingzhouYanglaoInfoRepository;
import com.microservice.dao.repository.crawler.insurance.jingzhou.InsurancejingzhouYibaoInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.parser.InsuranceJingzhouParser;
import app.util.ImageUtils;
import app.util.WebClientUtil;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.insurance.basic", "com.microservice.dao.entity.crawler.insurance.jingzhou"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.insurance.basic", "com.microservice.dao.repository.crawler.insurance.jingzhou"})

public class InsuranceJingzhouService extends InsuranceService {

    @Autowired
    private InsurancejingzhouBaseInfoRepository baseInfoRepository;

    @Autowired
    private InsurancejingzhouYanglaoInfoRepository yanglaoInfoRepository;

    @Autowired
    private InsurancejingzhouYibaoInfoRepository yibaoInfoRepository;

    @Autowired
    private InsurancejingzhouShengYuInfoRepository shengYuInfoRepository;

    @Autowired
    private InsurancejingzhouGongShangInfoRepository gongShangInfoRepository;

    @Autowired
    private InsurancejingzhouShiyeInfoRepository shiyeInfoRepository;

    private WebClient webClient;

    public String[] login(InsuranceRequestParameters insuranceRequestParameters) throws IOException {
        String[] resultArray = new String[2];
        InsuranceJingzhouParser parser = new InsuranceJingzhouParser();
        String cookie = null;
        String imgGapLoginPageText = null;
        do {
            webClient = WebCrawler.getInstance().getNewWebClient();

            //请求登录首页 获取Cookie
            WebRequest request = new WebRequest(new URL("http://58.54.132.3:7080/jzwssb/login.jsp"));
            request.setHttpMethod(HttpMethod.GET);
            WebClientUtil.setAdditionalHeader(request, null);
            webClient.getPage(request);

            cookie = parser.getCookies(webClient.getCookieManager());

            //获取图片ID
            request = new WebRequest(new URL("http://58.54.132.3:7080/jzwssb/SlideParamImgServlet"));
            request.setHttpMethod(HttpMethod.POST);
            WebClientUtil.setAdditionalHeader(request, cookie);
            TextPage textPage = webClient.getPage(request);
            String imageID = textPage.getContent();

            String url = "http://58.54.132.3:7080/jzwssb/SlideBackGroundImgServlet?ticket=" + imageID;
            request = new WebRequest(new URL(url));
            request.setHttpMethod(HttpMethod.GET);
            WebClientUtil.setAdditionalHeader(request, cookie);
            Page parentImage = webClient.getPage(request);
            File parentImageFile = ImageUtils.saveImg(parentImage);


            url = "http://58.54.132.3:7080/jzwssb/SlideDragImgServlet?ticket=" + imageID;
            request = new WebRequest(new URL(url));
            request.setHttpMethod(HttpMethod.GET);
            WebClientUtil.setAdditionalHeader(request, cookie);
            Page childImage = webClient.getPage(request);
            File childImageFile = ImageUtils.saveImg(childImage);


            int offset = ImageUtils.getMoveOffset(parentImageFile, childImageFile);
            if (offset == Integer.MIN_VALUE) {
                throw new RuntimeException("LOGIN ERROR !!!");
            }

            // 请求滑动图片接口
            request = new WebRequest(new URL("http://58.54.132.3:7080/jzwssb/SlideVerifyServlet"));
            request.setHttpMethod(HttpMethod.POST);
            Random random = new Random();
            int slidetime = random.nextInt(1000);
            slidetime += 1000;
            List<NameValuePair> list = new ArrayList();
            list.add(new NameValuePair("slidetime", String.valueOf(slidetime)));
            list.add(new NameValuePair("slidewith", String.valueOf(offset)));
            request.setRequestParameters(list);
            WebClientUtil.setAdditionalHeader(request, cookie);
            imgGapLoginPageText = webClient.getPage(request).getWebResponse().getContentAsString();


            System.out.println("pageContent : " + imgGapLoginPageText);
            System.out.println("offset : " + offset);
            System.out.println("slidetime : " + slidetime);

        } while (!"{\"success\":true}".equals(imgGapLoginPageText));

        resultArray[0] = cookie;

        WebRequest request = new WebRequest(new URL("http://58.54.132.3:7080/jzwssb/formLoginCheckAction.do?"));
        request.setHttpMethod(HttpMethod.POST);
        List<NameValuePair> list = new ArrayList();
        list = new ArrayList();
        list.add(new NameValuePair("j_password", insuranceRequestParameters.getPassword()));
        list.add(new NameValuePair("j_username", insuranceRequestParameters.getUserIDNum()));
        request.setRequestParameters(list);
        WebClientUtil.setAdditionalHeader(request, cookie);
        request.setAdditionalHeader("Referer", "http://58.54.132.3:7080/jzwssb/login.jsp");
        Page page = webClient.getPage(request);

        String statusCode = parser.checkLoginStatus(page.getWebResponse().getContentAsString());
        if (statusCode.startsWith("SUCCESS___")) {
            resultArray[1] = statusCode.replaceAll("SUCCESS___", "");
        } else {
            throw new RuntimeException(statusCode);
        }
        return resultArray;
    }

    //
    public TaskInsurance getData(TaskInsurance taskInsurance, InsuranceRequestParameters insuranceRequestParameters, String cookie) {

        InsuranceJingzhouParser<InsuranceJingzhouBasicBean> parser = new InsuranceJingzhouParser<>(insuranceRequestParameters.getTaskId());
        try {
            List<InsurancejingzhouYanglaoInfo> endowmentList = getEndowmentInsurance(cookie, insuranceRequestParameters);
            if (null != endowmentList) {
                yanglaoInfoRepository.saveAll(endowmentList);
                taskInsurance.setYanglaoStatus(200);
                taskInsuranceRepository.save(taskInsurance);
            } else {
                taskInsurance.setYanglaoStatus(404);
                taskInsuranceRepository.save(taskInsurance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskInsurance.setYanglaoStatus(404);
            taskInsuranceRepository.save(taskInsurance);
        }

        //医保
        List<InsurancejingzhouYibaoInfo> medicalList = null;
        try {
            medicalList = getMedicalInsurance(cookie, insuranceRequestParameters);
            if (null != medicalList) {
                yibaoInfoRepository.saveAll(medicalList);
                taskInsurance.setYiliaoStatus(200);
                taskInsuranceRepository.save(taskInsurance);
            } else {
                taskInsurance.setGongshangStatus(404);
                taskInsuranceRepository.save(taskInsurance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskInsurance.setGongshangStatus(404);
            taskInsuranceRepository.save(taskInsurance);
        }

        try {
            //工伤保险
            List<InsurancejingzhouGongShangInfo> employmentInjuryList = getEmploymentInjuryInsurance(cookie, insuranceRequestParameters);
            if (null != employmentInjuryList) {
                gongShangInfoRepository.saveAll(employmentInjuryList);
                taskInsurance.setGongshangStatus(200);
                taskInsuranceRepository.save(taskInsurance);
            } else {
                taskInsurance.setGongshangStatus(404);
                taskInsuranceRepository.save(taskInsurance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskInsurance.setGongshangStatus(404);
            taskInsuranceRepository.save(taskInsurance);
        }
        try {
            //生育保险
            List<InsurancejingzhouShengYuInfo> maternityList = getMaternityInsurance(cookie, insuranceRequestParameters);
            if (null != maternityList) {
                shengYuInfoRepository.saveAll(maternityList);
                taskInsurance.setShengyuStatus(200);
                taskInsuranceRepository.save(taskInsurance);
            } else {
                taskInsurance.setShengyuStatus(404);
                taskInsuranceRepository.save(taskInsurance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskInsurance.setShengyuStatus(404);
            taskInsuranceRepository.save(taskInsurance);
        }
        try {
            //失业保险
            List<InsurancejingzhouShiyeInfo> unemploymentList = getUnemploymentInsurance(cookie, insuranceRequestParameters);
            if (null != unemploymentList) {
                shiyeInfoRepository.saveAll(unemploymentList);
                taskInsurance.setShiyeStatus(200);
                taskInsuranceRepository.save(taskInsurance);
            } else {
                taskInsurance.setShiyeStatus(404);
                taskInsuranceRepository.save(taskInsurance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskInsurance.setShiyeStatus(404);
            taskInsuranceRepository.save(taskInsurance);
        }
        try {
            //个人基本信息
            InsurancejingzhouBaseInfo userInfoList = getUserInfo(cookie, insuranceRequestParameters);
            if (null != userInfoList) {
                baseInfoRepository.save(userInfoList);
                taskInsurance.setUserInfoStatus(200);
                taskInsuranceRepository.save(taskInsurance);
            } else {
                taskInsurance.setUserInfoStatus(404);
                taskInsuranceRepository.save(taskInsurance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            taskInsurance.setUserInfoStatus(404);
            taskInsuranceRepository.save(taskInsurance);
        }
        taskInsurance.setFinished(Boolean.TRUE);
        taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getDescription());
        taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhase());
        taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
        taskInsurance = taskInsuranceRepository.save(taskInsurance);
        return taskInsurance;

    }

    /**
     * 个人基本信息
     *
     * @param cookie
     * @return
     */
    private InsurancejingzhouBaseInfo getUserInfo(String cookie, InsuranceRequestParameters requestParameters) throws IOException {
        String url = "http://58.54.132.3:7080/jzwssb/ygba/bajcsjk/query/person/employeeForReCheckAction!query01.do";
        String dataJson = employeeForReCheckAction(webClient, url, cookie, requestParameters.getUserIDNum(), requestParameters.getUsername());
        InsurancejingzhouBaseInfo baseInfo = new InsuranceJingzhouParser<InsurancejingzhouBaseInfo>(requestParameters.getTaskId()).parserUserInfo(dataJson);
        return baseInfo;
    }

    /**
     *
     * @param cookie
     * @return
     */
    private List<InsurancejingzhouYanglaoInfo> getEndowmentInsurance(String cookie, InsuranceRequestParameters insuranceRequestParameters) throws IOException {
        String url = "http://58.54.132.3:7080/jzwssb/ygba/bajcsjk/query/person/employeeForReCheckAction!query03.do";
        String dataJson = employeeForReCheckAction(webClient, url, cookie, insuranceRequestParameters.getUserIDNum(), insuranceRequestParameters.getUsername());
        List<InsurancejingzhouYanglaoInfo> list = new InsuranceJingzhouParser<InsurancejingzhouYanglaoInfo>(insuranceRequestParameters.getTaskId())
                .parseInsurance(dataJson, "3", InsurancejingzhouYanglaoInfo.class);
        System.out.println(list);
        return list;
    }

    /**
     * 医疗缴费明细
     *
     * @param cookie
     * @return
     */
    private List<InsurancejingzhouYibaoInfo> getMedicalInsurance(String cookie, InsuranceRequestParameters insuranceRequestParameters) throws IOException {
        String url = "http://58.54.132.3:7080/jzwssb/ygba/bajcsjk/query/person/employeeForReCheckAction!query04.do";
        String dataJson = employeeForReCheckAction(webClient, url, cookie, insuranceRequestParameters.getUserIDNum(), insuranceRequestParameters.getUsername());
        List<InsurancejingzhouYibaoInfo> list = new InsuranceJingzhouParser<InsurancejingzhouYibaoInfo>(insuranceRequestParameters.getTaskId())
                .parseInsurance(dataJson, "4", InsurancejingzhouYibaoInfo.class);
        System.out.println(list);
        return list;

    }

    /**
     * 失业缴费明细
     *
     * @param cookie
     * @return
     */
    private List<InsurancejingzhouShiyeInfo> getUnemploymentInsurance(String cookie, InsuranceRequestParameters insuranceRequestParameters) throws IOException {
        String url = "http://58.54.132.3:7080/jzwssb/ygba/bajcsjk/query/person/employeeForReCheckAction!query05.do";
        String dataJson = employeeForReCheckAction(webClient, url, cookie, insuranceRequestParameters.getUserIDNum(), insuranceRequestParameters.getUsername());
        List<InsurancejingzhouShiyeInfo> list = new InsuranceJingzhouParser<InsurancejingzhouShiyeInfo>(insuranceRequestParameters.getTaskId())
                .parseInsurance(dataJson, "5", InsurancejingzhouShiyeInfo.class);
        System.out.println(list);
        return list;
    }


    /**
     * 工伤缴费明细
     *
     * @param cookie
     * @return
     */
    private List<InsurancejingzhouGongShangInfo> getEmploymentInjuryInsurance(String cookie, InsuranceRequestParameters insuranceRequestParameters) throws IOException {
        String url = "http://58.54.132.3:7080/jzwssb/ygba/bajcsjk/query/person/employeeForReCheckAction!query06.do";
        String dataJson = employeeForReCheckAction(webClient, url, cookie, insuranceRequestParameters.getUserIDNum(), insuranceRequestParameters.getUsername());
        List<InsurancejingzhouGongShangInfo> list = new InsuranceJingzhouParser<InsurancejingzhouGongShangInfo>(insuranceRequestParameters.getTaskId())
                .parseInsurance(dataJson, "6", InsurancejingzhouGongShangInfo.class);
        System.out.println(list);
        return list;
    }

    /**
     * 生育缴费明细
     *
     * @param cookie
     * @return
     */
    private List<InsurancejingzhouShengYuInfo> getMaternityInsurance(String cookie, InsuranceRequestParameters insuranceRequestParameters) throws IOException {
        String url = "http://58.54.132.3:7080/jzwssb/ygba/bajcsjk/query/person/employeeForReCheckAction!query07.do";
        String dataJson = employeeForReCheckAction(webClient, url, cookie, insuranceRequestParameters.getUserIDNum(), insuranceRequestParameters.getUsername());
        List<InsurancejingzhouShengYuInfo> list = new InsuranceJingzhouParser<InsurancejingzhouShengYuInfo>(insuranceRequestParameters.getTaskId())
                .parseInsurance(dataJson, "7", InsurancejingzhouShengYuInfo.class);
        System.out.println(list);
        return list;
    }


    /**
     * 社保数据查询接口请求
     *
     * @param webClient
     * @param cookie
     * @return
     */
    private String employeeForReCheckAction(WebClient webClient, String url, String cookie, String userIDNum, String userRealName) throws IOException {
        WebRequest webRequest = new WebRequest(new URL(url));
        webRequest.setHttpMethod(HttpMethod.POST);
        List<NameValuePair> list = new ArrayList();
        list.add(new NameValuePair("dto['aac002']", userIDNum));
        list.add(new NameValuePair("dto['aac003_1']", userRealName));
        webRequest.setRequestParameters(list);
        WebClientUtil.setAdditionalHeader(webRequest, cookie);
        Page page = webClient.getPage(webRequest);
        return page.getWebResponse().getContentAsString();
    }
}