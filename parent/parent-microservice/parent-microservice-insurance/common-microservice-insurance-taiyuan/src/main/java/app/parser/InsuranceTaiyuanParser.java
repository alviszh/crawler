package app.parser;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanFirst;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanResidentWater;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanStaffWater;
import com.microservice.dao.entity.crawler.insurance.taiyuan.InsuranceTaiyuanUserinfo;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;

/**
 * 登陆，获取用户信息，获取流水信息
 * Created by Mu on 2017/9/25.
 */
@Component
@SuppressWarnings("all")
public class InsuranceTaiyuanParser {
    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private TracerLog tracer;

    /**
     * 个人用户登陆，获取登陆后页面
     * @param reqPamam
     * @return
     */
    @SuppressWarnings("rawtypes")
    public WebParam login(InsuranceRequestParameters reqParam) throws Exception{
        Long start = System.currentTimeMillis();
        WebParam webParam = new WebParam();
        String loginUrl = "http://www.tyyb.gov.cn/login.jsp";
        WebRequest requestSetting = new WebRequest(new URL(loginUrl), HttpMethod.GET);

        WebClient webClient =getWebClient(null);
        webClient.getOptions().setJavaScriptEnabled(false);//重定向不可以关闭

        HtmlPage htmlPage = webClient.getPage(requestSetting);
        webClient.waitForBackgroundJavaScript(10000);
        HtmlImage image = htmlPage.getFirstByXPath("//img[@id='codeimg']");
        String yzm = chaoJiYingOcrService.getVerifycode(image,"1005");
        String loginUrl2 = "http://www.tyyb.gov.cn/j_spring_security_check?r=%27+Math.random()";

        String reqBody = "j_password="+reqParam.getPassword()+"&j_username="+reqParam.getUsername()+"&j_userid=" + URLEncoder.encode(reqParam.getName()) + "&checkCode=" + yzm;

        WebRequest requestSettings = new WebRequest(new URL(loginUrl2), HttpMethod.POST);

        requestSettings.setRequestBody(reqBody);
//        System.out.println("请求参数-->>" + reqBody);
        requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");


        TextPage pa = webClient.getPage(requestSettings);
        webClient.waitForBackgroundJavaScript(10000);
        // 等待JS驱动dom完成获得还原后的网页
//        System.out.println(pa.getContent());
        JSONObject resultJson = JSON.parseObject(pa.getContent());

        if("true".equals(resultJson.getString("success"))){
//            System.out.println("登陆成功！！");
            Long end = System.currentTimeMillis();
            System.out.println("登陆耗时"+(end-start));
            //获取登陆成功后结果
            HtmlPage page = webClient.getPage("http://www.tyyb.gov.cn/indexAction.do");
            String html = page.getWebResponse().getContentAsString();
            String alertMsg = WebCrawler.getAlertMsg();
            webParam.setAlertMsg(pa.getContent());
            webParam.setCode(page.getWebResponse().getStatusCode());
            webParam.setUrl("http://www.tyyb.gov.cn/indexAction.do");
            webParam.setPage(page);
            webParam.setHtml(html);
            webParam.setUserName(reqParam.getUsername());
            webParam.setPassWord(reqParam.getPassword());
            tracer.addTag("InsuranceTaiyuan-Parser-login","<xmp>"+html+"</xmp>");
            webParam.setCode(1001);
            return webParam;
        }else if(resultJson.getString("msg").contains("请填写正确的身份信息")){
                webParam.setCode(1002);
                return webParam;
        }else if(resultJson.getString("msg").contains("密码不对")){
            webParam.setCode(1003);
            return webParam;
        }else if(resultJson.getString("msg").contains("验证码输入错误")||yzm.length()>5){
            webParam.setCode(1004);
            return webParam;
        }else{
            webParam.setCode(1005);
            return webParam;
        }
    }

    /**
     * 获取用户信息
     * @param reqParam
     * @param cookies
     * @return
     * @throws Exception
     */
    public WebParam getUserInfo(Set<Cookie> cookies,InsuranceRequestParameters reqParam) throws Exception {
        WebParam webParam= new WebParam();
        WebClient webClient = getWebClient(cookies);
        String url = "http://www.tyyb.gov.cn/query/queryAction!queryBaseInfo.do";
        WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);
        List<InsuranceTaiyuanUserinfo> list = new ArrayList<InsuranceTaiyuanUserinfo>();
        TextPage userPage = webClient.getPage(request);

        if(userPage.getWebResponse().getStatusCode() == 200) {
            JSONObject userJson = JSON.parseObject(userPage.getContent());
            JSONArray userArray = userJson.getJSONObject("lists").getJSONObject("companyGrid").getJSONArray("list");
            for (JSONObject json : userArray.toJavaList(JSONObject.class)) {
                String gerenBianhao = json.getString("aac001");
                String name = json.getString("aac003");
                String gender = NatureParser.getGender(json.getString("aac004"));
                String idNum = json.getString("aac002");
                String nation = NatureParser.getNation(json.getString("aac005"));
                String birthDay = json.getString("aac006").substring(0, 10);
                String staffStatus = NatureParser.getStaffStaus(json.getString("aac008"));
                String anmelden = json.getString("aac010");
                String gerenShenfen = NatureParser.getShenFen(json.getString("aac012"));
                String address = json.getString("aac025");
                String phoneNum = json.getString("aae005");
                String yonggongStyle = NatureParser.getYongGong(json.getString("aac013"));
                String peasant = NatureParser.getPeasant(json.getString("aac028"));
                InsuranceTaiyuanUserinfo userinfo = new InsuranceTaiyuanUserinfo();
                userinfo.setGerenBianhao(gerenBianhao);
                userinfo.setName(name);
                userinfo.setGender(gender);
                userinfo.setIdNum(idNum);
                userinfo.setNation(nation);
                userinfo.setBirthDay(birthDay);
                userinfo.setStaffStatus(staffStatus);
                userinfo.setAnmelden(anmelden);
                userinfo.setGerenShenfen(gerenShenfen);
                userinfo.setAddress(address);
                userinfo.setPhoneNum(phoneNum);
                userinfo.setYonggongStyle(yonggongStyle);
                userinfo.setPeasant(peasant);
                userinfo.setTaskId(reqParam.getTaskId());
                list.add(userinfo);
            }
            //获取用户信息
            webParam.setUserinfos(list);
        }
        webParam.setPg(userPage);
        webParam.setUrl(url);
        webParam.setCode(userPage.getWebResponse().getStatusCode());
        webParam.setHtml(userPage.getContent());
        return webParam;
    }
    /**
     * 获取职工首次缴费信息
     * @param reqParam
     * @param cookies
     * @return
     * @throws Exception
     */
    public WebParam getStaffFirst(Set<Cookie> cookies,InsuranceRequestParameters reqParam) throws Exception {
        WebParam webParam= new WebParam();
        WebClient webClient = getWebClient(cookies);
        List<InsuranceTaiyuanFirst> list = new ArrayList<InsuranceTaiyuanFirst>();
        //职工首次缴费
        String firstUrl = "http://www.tyyb.gov.cn/query/joinInsuranceAction!joinInsuranceInfo.do";
        String formData = "gridInfo%5B'personGrid_limit'%5D=120&gridInfo%5B'personGrid_start'%5D=0&gridInfo%5B'personGrid_selected'%5D=%5B%5D&gridInfo%5B'personGrid_modified'%5D=%5B%5D&gridInfo%5B'personGrid_removed'%5D=%5B%5D&gridInfo%5B'personGrid_added'%5D=%5B%5D";
        WebRequest request = new WebRequest(new URL(firstUrl), HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.setAdditionalHeader("Referer", "http://www.tyyb.gov.cn/query/joinInsuranceAction.do");
        request.setRequestBody(formData);

        TextPage page = webClient.getPage(request);
//        System.out.println("page---->>>>\n" + page.getContent());

        if(page.getWebResponse().getStatusCode() == 200) {
            JSONObject firstJson = JSON.parseObject(page.getContent());
            JSONArray firstArray = firstJson.getJSONObject("lists").getJSONObject("personGrid").getJSONArray("list");
            for (JSONObject json : firstArray.toJavaList(JSONObject.class)) {
                String personNum = json.getString("aac001");
                String companyNum = json.getString("aab001");
                String companyName = json.getString("aab004");
                String insuranceType = NatureParser.getInsuranceType(json.getString("aae140"));
                String payStatus = NatureParser.getPayStatus(json.getString("aac031"));
                String startTiem = json.getString("aac030");
                String insuranceParticipantType = NatureParser.getWorkerPersonType(json.getString("yac505"));
                InsuranceTaiyuanFirst first = new InsuranceTaiyuanFirst();
                first.setPersonNum(personNum);
                first.setCompanyNum(companyNum);
                first.setCompanyName(companyName);
                first.setInsuranceType(insuranceType);
                first.setPayStatus(payStatus);
                first.setStartTiem(startTiem);
                first.setInsuranceParticipantType(insuranceParticipantType);
                first.setTaskId(reqParam.getTaskId());
                list.add(first);
            }
            //获取用户信息
            webParam.setFirsts(list);
        }
        webParam.setPg(page);
        webParam.setUrl(firstUrl);
        webParam.setCode(page.getWebResponse().getStatusCode());
        webParam.setHtml(page.getContent());
        return webParam;
    }
    /**
     * 获取居民首次缴费信息
     * @param reqParam
     * @param cookies
     * @return
     * @throws Exception
     */
    public WebParam getJuminFirst(Set<Cookie> cookies,InsuranceRequestParameters reqParam) throws Exception {
        WebParam webParam= new WebParam();
        WebClient webClient = getWebClient(cookies);
        List<InsuranceTaiyuanFirst> list = new ArrayList<InsuranceTaiyuanFirst>();
        //职工首次缴费
        String firstUrl = "http://www.tyyb.gov.cn/query/jmJoinInsuranceAction!joinInsuranceInfo.do";
        String formData = "gridInfo%5B'jmpersonGrid_limit'%5D=120&gridInfo%5B'jmpersonGrid_start'%5D=0&gridInfo%5B'jmpersonGrid_selected'%5D=%5B%5D&gridInfo%5B'jmpersonGrid_modified'%5D=%5B%5D&gridInfo%5B'jmpersonGrid_removed'%5D=%5B%5D&gridInfo%5B'jmpersonGrid_added'%5D=%5B%5D";
        WebRequest request = new WebRequest(new URL(firstUrl), HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.setAdditionalHeader("Referer", "http://www.tyyb.gov.cn/query/joinInsuranceAction.do");
        request.setRequestBody(formData);

        TextPage page = webClient.getPage(request);
//        System.out.println("page---->>>>\n" + page.getContent());

        if(page.getWebResponse().getStatusCode() == 200) {
            JSONObject firstJson = JSON.parseObject(page.getContent());
            JSONArray firstArray = firstJson.getJSONObject("lists").getJSONObject("jmpersonGrid").getJSONArray("list");
            for (JSONObject json : firstArray.toJavaList(JSONObject.class)) {
                String personNum = json.getString("aac001");
                String companyNum = json.getString("aab001");
                String companyName = json.getString("aab004");
                String insuranceType = NatureParser.getInsuranceType(json.getString("aae140"));
                String payStatus = NatureParser.getPayStatus(json.getString("aac031"));
                String startTiem = json.getString("aac030");
                String insuranceParticipantType = NatureParser.getJuminPersonType(json.getString("ykc280"));
                InsuranceTaiyuanFirst first = new InsuranceTaiyuanFirst();
                first.setPersonNum(personNum);
                first.setCompanyNum(companyNum);
                first.setCompanyName(companyName);
                first.setInsuranceType(insuranceType);
                first.setPayStatus(payStatus);
                first.setStartTiem(startTiem);
                first.setInsuranceParticipantType(insuranceParticipantType);
                first.setTaskId(reqParam.getTaskId());
                list.add(first);
            }
            //获取用户信息
            webParam.setFirsts(list);
        }
        webParam.setPg(page);
        webParam.setUrl(firstUrl);
        webParam.setCode(page.getWebResponse().getStatusCode());
        webParam.setHtml(page.getContent());
        return webParam;
    }

    /**
     * 获取职工缴费流水信息
     * @param reqParam
     * @param cookies
     * @return
     * @throws Exception
     */
    public WebParam getStaffPayInfo(Set<Cookie> cookies,InsuranceRequestParameters reqParam) throws Exception {
        WebParam webParam= new WebParam();
        WebClient webClient = getWebClient(cookies);
        List<InsuranceTaiyuanStaffWater> list = new ArrayList<InsuranceTaiyuanStaffWater>();
        String requestUrl = "http://www.tyyb.gov.cn/query/insuranceJfyzAction!insuranceJfyzInfo.do";
        //爬取1200条数据（差不多10年）
        String formData = "gridInfo%5B'jfjeGrid_limit'%5D=1200&gridInfo%5B'jfjeGrid_start'%5D=0&gridInfo%5B'jfjeGrid_selected'%5D=%5B%5D&gridInfo%5B'jfjeGrid_modified'%5D=%5B%5D&gridInfo%5B'jfjeGrid_removed'%5D=%5B%5D&gridInfo%5B'jfjeGrid_added'%5D=%5B%5D";
        WebRequest request = new WebRequest(new URL(requestUrl), HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.setAdditionalHeader("Referer", "http://www.tyyb.gov.cn/query/insuranceJfyzAction.do");
        request.setRequestBody(formData);

        TextPage page = webClient.getPage(request);

        if(page.getWebResponse().getStatusCode() == 200) {
            JSONObject firstJson = JSON.parseObject(page.getContent());
            JSONArray firstArray = firstJson.getJSONObject("lists").getJSONObject("jfjeGrid").getJSONArray("list");
            for (JSONObject json : firstArray.toJavaList(JSONObject.class)) {
                String personNum = json.getString("aac001");
                String companyName = json.getString("aab004");
                String insuranceType = NatureParser.getInsuranceType(json.getString("aae140"));
                String payMonth = json.getString("aae003");
                String payType = NatureParser.getPayType(json.getString("aae143"));
                String insurancePeopleType = NatureParser.getWorkerPersonType(json.getString("yac505"));
                String staffStatus = NatureParser.getPeopleStatus(json.getString("aac008"));
                String payBase = json.getString("aae180");
                String personMoney = json.getString("yab157");
                String companyMoney = json.getString("aab212");
                String months = json.getString("yac234");
                InsuranceTaiyuanStaffWater water = new InsuranceTaiyuanStaffWater(personNum,companyName,insuranceType,payMonth,payType,insurancePeopleType,staffStatus,payBase,personMoney,companyMoney,months,"");
                list.add(water);
            }
            //获取用户信息
            webParam.setStaffWaters(list);
        }
        webParam.setPg(page);
        webParam.setUrl(requestUrl);
        webParam.setCode(page.getWebResponse().getStatusCode());
        webParam.setHtml(page.getContent());
        return webParam;
    }

    /**
     * 获取居民缴费流水信息
     * @param reqParam
     * @param cookies
     * @return
     * @throws Exception
     */
    public WebParam getJuminPayInfo(Set<Cookie> cookies,InsuranceRequestParameters reqParam) throws Exception {
        WebParam webParam= new WebParam();
        WebClient webClient = getWebClient(cookies);
        List<InsuranceTaiyuanResidentWater> list = new ArrayList<InsuranceTaiyuanResidentWater>();
        String requestUrl = "http://www.tyyb.gov.cn/query/jmInsuranceJfAction!insuranceJfyzInfo.do";
        //爬取1200条数据（差不多10年）
        String formData = "gridInfo%5B'jmjfjeGrid_limit'%5D=1200&gridInfo%5B'jmjfjeGrid_start'%5D=0&gridInfo%5B'jmjfjeGrid_selected'%5D=%5B%5D&gridInfo%5B'jmjfjeGrid_modified'%5D=%5B%5D&gridInfo%5B'jmjfjeGrid_removed'%5D=%5B%5D&gridInfo%5B'jmjfjeGrid_added'%5D=%5B%5D";
        WebRequest request = new WebRequest(new URL(requestUrl), HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        request.setAdditionalHeader("Referer", "http://www.tyyb.gov.cn/query/jmInsuranceJfAction.do");
        request.setRequestBody(formData);

        TextPage page = webClient.getPage(request);

        if(page.getWebResponse().getStatusCode() == 200) {
            JSONObject firstJson = JSON.parseObject(page.getContent());
            JSONArray firstArray = firstJson.getJSONObject("lists").getJSONObject("jmjfjeGrid").getJSONArray("list");
            for (JSONObject json : firstArray.toJavaList(JSONObject.class)) {
                String personNum = json.getString("aac001");
                String company = json.getString("aab001");
                String payType = NatureParser.getPayType(json.getString("aae143"));
                String payYear = json.getString("aae001");
                String medicalPayAmount = json.getString("yka446");
                String personMoney = json.getString("yka447");
                String govMoney = json.getString("yka448");
                String zhongyangMoney = json.getString("yka449");
                String provinceMoney = json.getString("yka450");
                String cityMoney = json.getString("yka451");
                String countyMoney = json.getString("yka452");
                String canbaoMoney = json.getString("yka501");
                String insuranceType = NatureParser.getInsuranceType(json.getString("aae140"));
                String handleTime = json.getString("aae036");
                InsuranceTaiyuanResidentWater water  = new InsuranceTaiyuanResidentWater(personNum,company,payType,
                        payYear,medicalPayAmount,personMoney,govMoney,zhongyangMoney,provinceMoney,cityMoney,countyMoney,canbaoMoney,insuranceType,handleTime,"");
                list.add(water);
            }
            //获取用户信息
            webParam.setResidentWaters(list);
        }
        webParam.setPg(page);
        webParam.setUrl(requestUrl);
        webParam.setCode(page.getWebResponse().getStatusCode());
        webParam.setHtml(page.getContent());
        return webParam;
    }

    public WebClient getWebClient(Set<Cookie> cookies) {

        WebClient  webClient = new WebClient(BrowserVersion.CHROME);
        webClient.setRefreshHandler(new ThreadedRefreshHandler());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(15000); // 15->60
        webClient.waitForBackgroundJavaScript(10000); // 5s
        try{
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        }catch(Exception e){

        }

        webClient.getOptions().setUseInsecureSSL(true); //
        webClient.getCookieManager().setCookiesEnabled(true);//开启cookie管理


        if(cookies != null)
            for(Cookie cookie:cookies){
                webClient.getCookieManager().addCookie(cookie);
            }
        return webClient;
    }

}
