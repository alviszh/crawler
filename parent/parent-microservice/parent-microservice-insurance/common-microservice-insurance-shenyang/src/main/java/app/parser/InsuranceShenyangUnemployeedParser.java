package app.parser;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.shenyang.*;
import com.module.htmlunit.WebCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/18.
 */
@Component
@SuppressWarnings("all")
public class InsuranceShenyangUnemployeedParser {
    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;


    /**
     * 登陆，获取登陆后页面
     * @param reqPamam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public WebParam login(InsuranceRequestParameters reqParam) throws Exception{

        WebParam webParam = new WebParam();
        String loginUrl = "http://218.25.90.164/xxcx/sybxcx.html";
        WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);

        webRequest.setAdditionalHeader("Accept", "application/json, text/javascript, */*; q=0.01");
        webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        webRequest.setAdditionalHeader("Host", "gs.sysb.gov.cn");
        webRequest.setAdditionalHeader("Referer", "http://gs.sysb.gov.cn/sysb/");

        WebClient webClient= WebParam.getWebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage htmlPage = webClient.getPage(webRequest);
        // 等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(10000);
        int status = htmlPage.getWebResponse().getStatusCode();

//        System.out.println("\n\nunemployeedparser-login:"+htmlPage.getWebResponse().getContentAsString());
        if(200 == status){
            HtmlImage image = htmlPage.getFirstByXPath("//img[@src='image.jsp']");
            String code = chaoJiYingOcrService.getVerifycode(image,"1004");
            tracer.qryKeyValue("shenyang-unemployeed-login-verifyCode ==>","code");
            HtmlTextInput sfz = htmlPage.getFirstByXPath("//input[@name='sfz']");
            HtmlTextInput xm  = htmlPage.getFirstByXPath("//input[@name='xm']");

            HtmlTextInput verifyCode = htmlPage.getFirstByXPath("//input[@name='bz2']");
            HtmlElement button = htmlPage.getFirstByXPath("//input[@type='submit']");
            if (button == null) {
                tracer.addTag("InsuranceShenyangUnemployeedParser.login",
                        reqParam.getTaskId() + "login button can not found : null");
                throw new Exception("login button can not found : null");
            }else{
                sfz.setText(reqParam.getUsername());
                xm.setText(reqParam.getPassword());
                verifyCode.setText(code);
//                System.out.println("username="+reqParam.getUsername()+"  password="+reqParam.getPassword()+"  code="+code);
                HtmlPage loginPage = button.click();
                webClient.waitForBackgroundJavaScript(10000);
                String html=loginPage.getWebResponse().getContentAsString();
                //获取登陆成功后结果
                String alertMsg = WebCrawler.getAlertMsg();
                webParam.setAlertMsg(alertMsg);
                webParam.setCode(loginPage.getWebResponse().getStatusCode());
                webParam.setUrl(loginUrl);
                webParam.setPage(loginPage);
                webParam.setHtml(html);
                webParam.setUserName(reqParam.getUsername());
                webParam.setPassWord(reqParam.getPassword());
//                System.out.println("unemployeedparser-webParam:"+webParam);
                tracer.addTag("unemployeedlogin","<xmp>"+html+"</xmp>");

                boolean success = html.contains("失业保险个人账户信息");
                if(success){
                    webParam.setCode(1001);
//                    System.out.println("沈阳失业登陆成功");
                    return webParam;
                }else {
                    if(null != html && (html.contains("号码校验位错"))){
                        webParam.setCode(1002);
                        return webParam;
                    }else if(null != html && (html.contains("验证码错误")||code.length() > 4 )){
                        webParam.setCode(1003);
                        return webParam;
                    }else {
                        webParam.setCode(1005);
                        return webParam;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取失业保险
     * @param html
     * @param cookie
     * @return
     */
    public List<InsuranceShenyangUnemployeedInfo> getUnemployeedInfo(InsuranceShenyangHtml html, Set<Cookie> cookie){
        Document doc = Jsoup.parse(html.getHtml());
        Element table=doc.select("table[width=90%]").first();
        List<InsuranceShenyangUnemployeedInfo> list = new ArrayList<InsuranceShenyangUnemployeedInfo>();
        if(table != null){
            Elements rows = table.select("tr");
            if(rows != null){

                String taskId = html.getTaskId();
                int i = 0;
                for(Element row:rows){
                    if( ++i < 3 || i%2==0 )
                        continue;
                    Elements columns = row.select("td");
                    String idCardNum = columns.get(0).text().trim();
                    String insuranceCode = columns.get(1).text().trim();
                    String name = columns.get(2).text().trim();
                    String payTime = columns.get(3).text().trim();
                    String personalIdentity = columns.get(4).text().trim();
                    String payState = columns.get(5).text().trim();

                    InsuranceShenyangUnemployeedInfo bean = new InsuranceShenyangUnemployeedInfo();
                    bean.setIdCardNum(idCardNum);
                    bean.setInsuranceCode(insuranceCode);
                    bean.setName(name);
                    bean.setPayTime(payTime);
                    bean.setPayState(payState);
                    bean.setPersonalIdentity(personalIdentity);
                    bean.setTaskId(taskId);
                    bean.setInsuranceType("失业保险");
                    list.add(bean);
                }
            }
            tracer.addTag("insuranceshenyang-unemployeedinfo-parser",html.getTaskId() + "<xmp>" + html.getHtml() + "<xmp>");

        }
//        System.out.println("失业detaillist----size="+list.size()+"\n"+list);
        return list;
    }

}
