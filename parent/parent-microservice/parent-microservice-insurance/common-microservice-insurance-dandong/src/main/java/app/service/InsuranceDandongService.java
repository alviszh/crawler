package app.service;

import app.parser.InsuranceDandongParser;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dandong.InsuranceDandongUserInfo;
import com.microservice.dao.repository.crawler.insurance.dandong.InsuranceDandongUserInfoRepository;
import com.module.htmlunit.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.insurance.basic", "com.microservice.dao.entity.crawler.insurance.dandong"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.insurance.basic", "com.microservice.dao.repository.crawler.insurance.dandong"})

public class InsuranceDandongService extends InsuranceService {

    @Autowired
    private InsuranceDandongParser insuranceDandongParser;

    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;

    @Autowired
    private InsuranceDandongUserInfoRepository insuranceDandongUserInfoRepository;

    public HtmlPage login(InsuranceRequestParameters insuranceRequestParameters, int i) {
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        WebRequest webRequest = null;
        HtmlPage page = null;
        try {
            webRequest = new WebRequest(new URL("http://lndd.hrss.gov.cn/card/kcx.jsp"), HttpMethod.GET);
            //添加请求头
            setAdditionalHeader(webRequest);
            page = webClient.getPage(webRequest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Open Dandong Insurance Index Page Error!");
        }
        HtmlImage yzm = null;
        try {
            yzm = insuranceDandongParser.parseImg(page);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Parser Verification Code Img Error!");
        }
        String verifyCode = null;
        try {
            verifyCode = chaoJiYingOcrService.getVerifycode(yzm, "1902");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Chaojiyi OCR Distinguish Verification Code Error!");
        }

        String userNameURLCode = null;
        try {
            userNameURLCode = java.net.URLEncoder.encode(insuranceRequestParameters.getUsername(), "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String userIDNum = insuranceRequestParameters.getUserIDNum();


        String url = "http://lndd.hrss.gov.cn/sbkcx?card.xm=" + userNameURLCode
                + "&kccx.lx=xzjd&card.sfzh=" + userIDNum
                + "&yzm=" + verifyCode;

        try {
            webRequest = new WebRequest(new URL(url));

            //添加请求头
            setAdditionalHeader(webRequest);
            page = webClient.getPage(webRequest);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Dandong Insurance Login Error!");
        }
        return page;
    }

    public InsuranceDandongUserInfo getData(HtmlPage page) {
        InsuranceDandongUserInfo insuranceDandongUserInfo = null;
        try {
            insuranceDandongUserInfo = insuranceDandongParser.parserUserData(page);
        } catch (Exception e) {
            e.printStackTrace();

            switch (e.getMessage()) {
                case "Verification Code Error!":
                    throw new RuntimeException("Verification Code Input Error!");
                case "Insurance Dandong User Name Error!":
                    throw new RuntimeException("Insurance Dandong User Name Error!");
                case "Insurance Dandong User ID Card Num Error!":
                    throw new RuntimeException("Insurance Dandong User ID Card Num Error!");
            }
        }
        return insuranceDandongUserInfo;
    }

    public void saveUserInfo(InsuranceDandongUserInfo insuranceDandongUserInfo) {
        if (insuranceDandongUserInfo != null) {
            insuranceDandongUserInfoRepository.save(insuranceDandongUserInfo);
        }
    }

    public void saveTaskInsurance(TaskInsurance taskInsurance) {
        taskInsurance.setGongshangStatus(200);
        taskInsurance.setShengyuStatus(200);
        taskInsurance.setShiyeStatus(200);
        taskInsurance.setYiliaoStatus(200);
        taskInsurance.setYanglaoStatus(200);
        taskInsurance.setUserInfoStatus(200);

        taskInsuranceRepository.save(taskInsurance);
    }


    /**
     * webRequest 添加请求头信息
     *
     * @param webRequest
     */
    private void setAdditionalHeader(WebRequest webRequest) {
        webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
        webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
        webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
    }
}
