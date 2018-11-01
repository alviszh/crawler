package app.service;

import app.parser.InsuranceZhoushanParser;
import app.util.MD5Utils;
import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.dandong.InsuranceDandongUserInfo;
import com.microservice.dao.entity.crawler.insurance.zhoushan.*;
import com.microservice.dao.repository.crawler.insurance.dandong.InsuranceDandongUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.zhoushan.*;
import com.module.htmlunit.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.insurance.basic", "com.microservice.dao.entity.crawler.insurance.zhoushan"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.insurance.basic", "com.microservice.dao.repository.crawler.insurance.zhoushan"})

public class InsuranceZhoushanService extends InsuranceService {

    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;

    @Autowired
    private InsuranceZhoushanUserInfoRepository userInfoRepository;

    @Autowired
    private InsuranceZhoushanEndowmentRepository endowmentRepository;


    @Autowired
    private InsuranceZhoushanMaternityRepository maternityRepository;


    @Autowired
    private InsuranceZhoushanMedicalRepository medicalRepository;


    @Autowired
    private InsuranceZhoushanUnemploymentRepository unemploymentRepository;


    @Autowired
    private InsuranceZhoushanEmploymentInjuryRepository employmentInjuryRepository;


    private WebClient webClient;

    private String getVerifycodeByChaojiying(WebClient webClient) {
        InsuranceZhoushanParser zhoushanParser = new InsuranceZhoushanParser();
        HtmlPage page = null;
        try {
            WebRequest webRequest = new WebRequest(new URL("http://122.226.8.130:9090/zswsbs/LogonDialog.jsp"), HttpMethod.GET);
            //添加请求头
            setAdditionalHeader(webRequest);
            page = webClient.getPage(webRequest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Open Dandong Insurance Index Page Error!");
        }
        HtmlImage yzm = null;
        try {
            yzm = zhoushanParser.parseImg(page);
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
        return verifyCode;
    }

    private boolean checkVerifyCode(WebClient webClient, String verifycode, String userId, String cookie) throws MalformedURLException, UnsupportedEncodingException {

        WebRequest webRequest = new WebRequest(new URL("http://122.226.8.130:9090/zswsbs/register/RegisterServlet"));
        List<NameValuePair> list = new ArrayList();
        String value = "{\"iscode\":\"" + userId + "\",\"verifycode\":\"" + verifycode + "\"}";
        //String encode = URLEncoder.encode(value, "GBK");
        NameValuePair nameValuePair = new NameValuePair("inParams", value);
        list.add(nameValuePair);
        webRequest.setRequestParameters(list);
        //webRequest.
        webRequest.setAdditionalHeader("Cookie", cookie);
        try {
            TextPage page = webClient.getPage(webRequest);

            String content = page.getContent();
            return !"true".equals(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param webClient
     * @param insuranceRequestParameters
     * @param cookie
     * @param verifyCode
     * @throws MalformedURLException
     */
    private String loginAction(WebClient webClient, InsuranceRequestParameters insuranceRequestParameters, String cookie, String verifyCode) throws IOException {
        WebRequest webRequest = new WebRequest(new URL("http://122.226.8.130:9090/zswsbs/logonAction.do"));
        List<NameValuePair> list = new ArrayList();
        list.add(new NameValuePair("username", insuranceRequestParameters.getUserIDNum()));
        list.add(new NameValuePair("tmpuname", java.net.URLEncoder.encode("身份证号或单位编码", "GBK")));
        list.add(new NameValuePair("passwd", MD5Utils.lowercaseMD5(insuranceRequestParameters.getPassword())));
        list.add(new NameValuePair("tmp", java.net.URLEncoder.encode("初始密码为000000", "GBK")));
        list.add(new NameValuePair("verifycode", verifyCode));
        list.add(new NameValuePair("submitbtn.x", "32"));
        list.add(new NameValuePair("submitbtn.y", "9"));
        webRequest.setRequestParameters(list);
        webRequest.setAdditionalHeader("Cookie", cookie);
        HtmlPage page = webClient.getPage(webRequest);
        //System.out.println(page.asXml());
        return page.asXml();
    }

    public String login(InsuranceRequestParameters insuranceRequestParameters) throws MalformedURLException {
        boolean b = true;
        String cookies;
        String verifycode;
        do {
            StringBuffer cookieBuffer = new StringBuffer();
            webClient = WebCrawler.getInstance().getNewWebClient();
            verifycode = getVerifycodeByChaojiying(webClient);

            Set<Cookie> cookieSet = webClient.getCookieManager().getCookies();

            for (Cookie cookie : cookieSet) {
                cookieBuffer.append(cookie);
            }
            try {
                b = checkVerifyCode(webClient, verifycode, insuranceRequestParameters.getUserIDNum(), cookieBuffer.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Pattern pattern = Pattern.compile("(JSESSIONID=.+?);");
            Matcher matcher = pattern.matcher(cookieBuffer.toString());
            if (matcher.find()) {
                cookies = matcher.group(1);
            } else {
                cookies = cookieBuffer.toString();
            }

        } while (b);

        try {
            String loginActionHtml = loginAction(webClient, insuranceRequestParameters, cookies, verifycode);
            if (loginActionHtml.length() == 230 && loginActionHtml.indexOf("网上办事大厅") != -1) {
                return cookies;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public TaskInsurance getData(TaskInsurance taskInsurance, InsuranceRequestParameters insuranceRequestParameters, String cookie) {

        InsuranceZhoushanParser<InsuranceZhoushanBasicBean> parser = new InsuranceZhoushanParser<>();
        parser.setTaskid(insuranceRequestParameters.getTaskId());
        //养老保险
        try {
            List<InsuranceZhoushanEndowment> endowmentList = endowmentInsurance(webClient, cookie, insuranceRequestParameters.getUserIDNum(), parser);
            if (null != endowmentList) {
                endowmentRepository.saveAll(endowmentList);
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
        List<InsuranceZhoushanMedical> medicalList = null;
        try {
            medicalList = medicalInsurance(webClient, cookie, insuranceRequestParameters.getUserIDNum(), parser);
            if (null != medicalList) {
                medicalRepository.saveAll(medicalList);
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
            List<InsuranceZhoushanEmploymentInjury> employmentInjuryList = employmentInjuryInsurance(webClient, cookie, insuranceRequestParameters.getUserIDNum(), parser);
            if (null != employmentInjuryList) {
                employmentInjuryRepository.saveAll(employmentInjuryList);
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
            List<InsuranceZhoushanMaternity> maternityList = maternityInsurance(webClient, cookie, insuranceRequestParameters.getUserIDNum(), parser);
            if (null != maternityList) {
                maternityRepository.saveAll(maternityList);
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
            List<InsuranceZhoushanUnemployment> unemploymentList = unemploymentInsurance(webClient, cookie, insuranceRequestParameters.getUserIDNum(), parser);
            if (null != unemploymentList) {
                unemploymentRepository.saveAll(unemploymentList);
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
            List<InsuranceZhoushanUserInfo> userInfoList = userInfo(webClient, cookie, insuranceRequestParameters.getUserIDNum(), parser);
            if (null != userInfoList) {
                userInfoRepository.saveAll(userInfoList);
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
     * 养老保险
     *
     * @param webClient
     * @param cookie
     */
    private List<InsuranceZhoushanEndowment> endowmentInsurance(WebClient webClient, String cookie, String userIDNum, InsuranceZhoushanParser zhoushanParser) {

        try {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(new NameValuePair("method", "doAction"));// method	doAction
            nameValuePairList.add(new NameValuePair("divForStrust(div_1)", "[{\"iscode\":\"\",\"psname\":\"\",\"year\":\"\"}]"));//            divForStrust(div_1)	[{"iscode":"","psname":"","year":""}]
            nameValuePairList.add(new NameValuePair("divForStrust(div_2)", "[]"));//            divForStrust(div_2)	[]
            nameValuePairList.add(new NameValuePair("divForStrust(div_3)", "[]"));//            divForStrust(div_3)	[]
            nameValuePairList.add(new NameValuePair("divForStrust(commParams)", "[{\"currentEvent\":\"init\",\"currentOpseno\":null,\"currentLoginName\":\"" + userIDNum + "\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(MDParams)", "[{\"active\":\"\",\"auflag\":\"0\",\"baseLocation\":\"/pages/commAction.do?method=geren.Zzrycx\",\"bdyy\":\"\",\"cprate\":\"\",\"description\":\"养老保险缴费查询\",\"firstopen\":\"0\",\"focanclose\":\"\",\"functioncode\":\"\",\"functiondesc\":\"\",\"functionid\":\"0004\",\"location\":\"/pages/commAction.do?method=geren.Zzrycx\",\"log\":\"0\",\"method\":\"geren.Zzrycx\",\"opctrl\":\"001\",\"orderno\":1,\"owner\":\"\",\"param1\":\"\",\"param2\":\"\",\"param3\":\"\",\"param4\":\"\",\"parent\":\"0031\",\"proc\":\"\",\"prsource\":\"\",\"publicflag\":\"0\",\"rbflag\":\"1\",\"repid\":\"/geren/zzrycx.cpt\",\"rpflag\":\"0\",\"sysid\":\"00\",\"title\":\"养老保险缴费查询\",\"type\":\"0\",\"uptype\":\"\",\"vsclass\":\"\",\"ywlx\":\"\",\"zyxx\":\"{}\"}]"));
            nameValuePairList.add(new NameValuePair("divForStrust(types_div_1)", "[{\"iscode\":\"string\",\"psname\":\"string\",\"year\":\"number\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(labels_div_1)", "[{\"iscode\":\"公民身份号码：\",\"psname\":\"姓名：\",\"year\":\"输入要查询的年度：\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_1)", "[{\"iscode\":\"R\",\"psname\":\"R\",\"year\":\"R\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(types_div_3)", "[{\"grzh\":\"number\",\"cpname\":\"string\",\"sacode\":\"string\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(labels_div_3)", "[{\"grzh\":\"个人账户累计存储额\",\"cpname\":\"所在单位\",\"sacode\":\"参保地\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_3)", "[{\"grzh\":\"D\",\"cpname\":\"D\",\"sacode\":\"D\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(types_div_2)", "[{\"ysym\":\"number\",\"acym\":\"number\",\"ftym\":\"number\",\"rewage\":\"number\",\"arcpfd\":\"number\",\"arpsfd\":\"number\",\"reflg\":\"string\",\"cpname\":\"string\",\"sacode\":\"string\",\"pdcode\":\"string\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(labels_div_2)", "[{\"ysym\":\"应缴年月\",\"acym\":\"实缴年月\",\"ftym\":\"到账年月\",\"rewage\":\"缴费基数\",\"arcpfd\":\"单位缴纳\",\"arpsfd\":\"个人缴纳\",\"reflg\":\"类型\",\"cpname\":\"所在单位\",\"sacode\":\"参保地\",\"pdcode\":\"险种\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_2)", "[{\"ysym\":\"D\",\"acym\":\"D\",\"ftym\":\"D\",\"rewage\":\"D\",\"arcpfd\":\"D\",\"arpsfd\":\"D\",\"reflg\":\"D\",\"cpname\":\"D\",\"sacode\":\"D\",\"pdcode\":\"D\"}]"));//
            nameValuePairList.add(new NameValuePair("divForStrust(divpagetypes)", "[{\"div_1\":\"2\",\"div_3\":\"0\",\"div_2\":\"1\"}]"));

            String commData = commAction(webClient, nameValuePairList, cookie);

            String querySQL = null;
            try {
                //System.out.println(commData);
                querySQL = zhoushanParser.parseQuerySQL(commData);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String dataJson = pageQueryAction(webClient, cookie, querySQL);
            //System.out.println(dataJson);
            List<InsuranceZhoushanEndowment> endowmentList = zhoushanParser.parseInsurance(dataJson, InsuranceZhoushanEndowment.class);
            //System.out.println(endowment);
            return endowmentList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 医疗保险
     *
     * @param webClient
     * @param cookie
     * @return
     */
    private List<InsuranceZhoushanMedical> medicalInsurance(WebClient webClient, String cookie, String userIDNum, InsuranceZhoushanParser zhoushanParser) throws IOException {
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new NameValuePair("method", "doAction"));// method	doAction

        nameValuePairList.add(new NameValuePair("divForStrust(div_1)", "[{\"iscode\":\"\",\"psname\":\"\",\"year\":\"\"}]"));//            divForStrust(div_1)	[{"iscode":"","psname":"","year":""}]
        nameValuePairList.add(new NameValuePair("divForStrust(div_2)", "[]"));//            divForStrust(div_3)	[]
        nameValuePairList.add(new NameValuePair("divForStrust(commParams)", "[{\"currentEvent\":\"init\",\"currentOpseno\":null,\"currentLoginName\":\"" + userIDNum + "\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(MDParams)", "[{\"active\":\"\",\"auflag\":\"0\",\"baseLocation\":\"/pages/commAction.do?method=geren.Ckgrjfqk_yb\",\"bdyy\":\"\",\"cprate\":\"\",\"description\":\"医保缴费信息\",\"firstopen\":\"0\",\"focanclose\":\"\",\"functioncode\":\"\",\"functiondesc\":\"\",\"functionid\":\"0035\",\"location\":\"/pages/commAction.do?method=geren.Ckgrjfqk_yb\",\"log\":\"0\",\"method\":\"geren.Ckgrjfqk_yb\",\"opctrl\":\"001\",\"orderno\":2,\"owner\":\"\",\"param1\":\"\",\"param2\":\"\",\"param3\":\"\",\"param4\":\"\",\"parent\":\"0032\",\"proc\":\"\",\"prsource\":\"\",\"publicflag\":\"0\",\"rbflag\":\"1\",\"repid\":\"/geren/ckgrjfqk_yb.cpt\",\"rpflag\":\"0\",\"sysid\":\"00\",\"title\":\"医保缴费信息\",\"type\":\"0\",\"uptype\":\"\",\"vsclass\":\"\",\"ywlx\":\"\",\"zyxx\":\"{}\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_1)", "[{\"iscode\":\"string\",\"psname\":\"string\",\"year\":\"number\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_1)", "[{\"iscode\":\"公民身份号码\",\"psname\":\"姓名：\",\"year\":\"输入要查询的年度：\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_1)", "[{\"iscode\":\"R\",\"psname\":\"R\",\"year\":\"R\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_2)", "[{\"ysym\":\"number\",\"acym\":\"number\",\"ftym\":\"number\",\"rewage\":\"number\",\"arcpfd\":\"number\",\"arpsfd\":\"number\",\"arfd05\":\"number\",\"arfd07\":\"number\",\"reflg\":\"string\",\"arcpinfd\":\"number\",\"arpsinfd\":\"number\",\"arinfd05\":\"number\",\"cpname\":\"string\",\"sacode\":\"string\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_2)", "[{\"ysym\":\"应缴年月\",\"acym\":\"实缴年月\",\"ftym\":\"到账年月\",\"rewage\":\"缴费基数\",\"arcpfd\":\"单位金额\",\"arpsfd\":\"个人金额\",\"arfd05\":\"医疗补助\",\"arfd07\":\"医疗救助\",\"reflg\":\"类型\",\"arcpinfd\":\"单位划入\",\"arpsinfd\":\"个人划入\",\"arinfd05\":\"补助金划入\",\"cpname\":\"所在单位\",\"sacode\":\"参保地\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_2)", "[{\"ysym\":\"D\",\"acym\":\"D\",\"ftym\":\"D\",\"rewage\":\"D\",\"arcpfd\":\"D\",\"arpsfd\":\"D\",\"arfd05\":\"D\",\"arfd07\":\"D\",\"reflg\":\"D\",\"arcpinfd\":\"D\",\"arpsinfd\":\"D\",\"arinfd05\":\"D\",\"cpname\":\"D\",\"sacode\":\"D\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(divpagetypes)", "[{\"div_1\":\"2\",\"div_2\":\"1\"}]"));

        String commData = commAction(webClient, nameValuePairList, cookie);

        String querySQL = null;
        try {
            //System.out.println(commData);
            querySQL = zhoushanParser.parseQuerySQL(commData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dataJson = pageQueryAction(webClient, cookie, querySQL);
        //System.out.println(dataJson);
        List<InsuranceZhoushanMedical> medicalList = zhoushanParser.parseInsurance(dataJson, InsuranceZhoushanMedical.class);
        return medicalList;
    }

    /**
     * 工伤保险
     *
     * @param webClient
     * @param cookie
     * @return
     */
    private List<InsuranceZhoushanEmploymentInjury> employmentInjuryInsurance(WebClient webClient, String cookie, String userIDNum, InsuranceZhoushanParser zhoushanParser) throws IOException {
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new NameValuePair("method", "doAction"));// method	doAction

        nameValuePairList.add(new NameValuePair("divForStrust(div_1)", "[{\"iscode\":\"\",\"psname\":\"\",\"year\":\"\"}]"));//            divForStrust(div_1)	[{"iscode":"","psname":"","year":""}]
        nameValuePairList.add(new NameValuePair("divForStrust(div_2)", "[]"));
        nameValuePairList.add(new NameValuePair("divForStrust(commParams)", "[{\"currentEvent\":\"init\",\"currentOpseno\":null,\"currentLoginName\":\"" + userIDNum + "\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(MDParams)", "[{\"active\":\"\",\"auflag\":\"0\",\"baseLocation\":\"/pages/commAction.do?method=geren.Ckgrjfqk_gs\",\"bdyy\":\"\",\"cprate\":\"\",\"description\":\"工伤缴费信息\",\"firstopen\":\"0\",\"focanclose\":\"\",\"functioncode\":\"\",\"functiondesc\":\"\",\"functionid\":\"0038\",\"location\":\"/pages/commAction.do?method=geren.Ckgrjfqk_gs\",\"log\":\"0\",\"method\":\"geren.Ckgrjfqk_gs\",\"opctrl\":\"001\",\"orderno\":2,\"owner\":\"\",\"param1\":\"\",\"param2\":\"\",\"param3\":\"\",\"param4\":\"\",\"parent\":\"0037\",\"proc\":\"\",\"prsource\":\"\",\"publicflag\":\"0\",\"rbflag\":\"1\",\"repid\":\"/geren/ckgrjfqk_gs.cpt\",\"rpflag\":\"0\",\"sysid\":\"00\",\"title\":\"工伤缴费信息\",\"type\":\"0\",\"uptype\":\"\",\"vsclass\":\"\",\"ywlx\":\"\",\"zyxx\":\"{}\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_1)", "[{\"iscode\":\"string\",\"psname\":\"string\",\"year\":\"number\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_1)", "[{\"iscode\":\"公民身份号码\",\"psname\":\"姓名：\",\"year\":\"输入要查询的年度：\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_1)", "[{\"iscode\":\"R\",\"psname\":\"R\",\"year\":\"R\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_2)", "[{\"ysym\":\"number\",\"acym\":\"number\",\"ftym\":\"number\",\"rewage\":\"number\",\"arcpfd\":\"number\",\"arpsfd\":\"number\",\"arfd05\":\"number\",\"arfd07\":\"number\",\"reflg\":\"string\",\"arcpinfd\":\"number\",\"arpsinfd\":\"number\",\"arinfd05\":\"number\",\"cpname\":\"string\",\"sacode\":\"string\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_2)", "[{\"ysym\":\"应缴年月\",\"acym\":\"实缴年月\",\"ftym\":\"到账年月\",\"rewage\":\"缴费基数\",\"arcpfd\":\"单位金额\",\"arpsfd\":\"个人金额\",\"arfd05\":\"医疗补助\",\"arfd07\":\"医疗救助\",\"reflg\":\"类型\",\"arcpinfd\":\"单位划入\",\"arpsinfd\":\"个人划入\",\"arinfd05\":\"补助金划入\",\"cpname\":\"所在单位\",\"sacode\":\"参保地\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_2)", "[{\"ysym\":\"D\",\"acym\":\"D\",\"ftym\":\"D\",\"rewage\":\"D\",\"arcpfd\":\"D\",\"arpsfd\":\"D\",\"arfd05\":\"D\",\"arfd07\":\"D\",\"reflg\":\"D\",\"arcpinfd\":\"D\",\"arpsinfd\":\"D\",\"arinfd05\":\"D\",\"cpname\":\"D\",\"sacode\":\"D\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(divpagetypes)", "[{\"div_1\":\"2\",\"div_2\":\"1\"}]"));

        String commData = commAction(webClient, nameValuePairList, cookie);

        String querySQL = null;
        try {
            //System.out.println(commData);
            querySQL = zhoushanParser.parseQuerySQL(commData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dataJson = pageQueryAction(webClient, cookie, querySQL);
        // System.out.println(dataJson);
        List<InsuranceZhoushanEmploymentInjury> list = zhoushanParser.parseInsurance(dataJson, InsuranceZhoushanEmploymentInjury.class);
        return list;
    }

    /**
     * 生育保险
     *
     * @param webClient
     * @param cookie
     * @return
     */
    private List<InsuranceZhoushanMaternity> maternityInsurance(WebClient webClient, String cookie, String userIDNum, InsuranceZhoushanParser zhoushanParser) throws IOException {
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new NameValuePair("method", "doAction"));// method	doAction

        nameValuePairList.add(new NameValuePair("divForStrust(div_1)", "[{\"iscode\":\"\",\"psname\":\"\",\"year\":\"\"}]"));//            divForStrust(div_1)	[{"iscode":"","psname":"","year":""}]
        nameValuePairList.add(new NameValuePair("divForStrust(div_2)", "[]"));
        nameValuePairList.add(new NameValuePair("divForStrust(commParams)", "[{\"currentEvent\":\"init\",\"currentOpseno\":null,\"currentLoginName\":\"" + userIDNum + "\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(MDParams)", "[{\"active\":\"\",\"auflag\":\"0\",\"baseLocation\":\"/pages/commAction.do?method=geren.Ckgrjfqk_sy\",\"bdyy\":\"\",\"cprate\":\"\",\"description\":\"生育缴费信息\",\"firstopen\":\"0\",\"focanclose\":\"\",\"functioncode\":\"\",\"functiondesc\":\"\",\"functionid\":\"0042\",\"location\":\"/pages/commAction.do?method=geren.Ckgrjfqk_sy\",\"log\":\"0\",\"method\":\"geren.Ckgrjfqk_sy\",\"opctrl\":\"001\",\"orderno\":2,\"owner\":\"\",\"param1\":\"\",\"param2\":\"\",\"param3\":\"\",\"param4\":\"\",\"parent\":\"0040\",\"proc\":\"\",\"prsource\":\"\",\"publicflag\":\"0\",\"rbflag\":\"1\",\"repid\":\"/geren/ckgrjfqk_sy.cpt\",\"rpflag\":\"0\",\"sysid\":\"00\",\"title\":\"生育缴费信息\",\"type\":\"0\",\"uptype\":\"\",\"vsclass\":\"\",\"ywlx\":\"\",\"zyxx\":\"{}\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_1)", "[{\"iscode\":\"string\",\"psname\":\"string\",\"year\":\"number\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_1)", "[{\"iscode\":\"公民身份号码\",\"psname\":\"姓名：\",\"year\":\"输入要查询的年度：\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_1)", "[{\"iscode\":\"R\",\"psname\":\"R\",\"year\":\"R\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_2)", "[{\"ysym\":\"number\",\"acym\":\"number\",\"ftym\":\"number\",\"rewage\":\"number\",\"arcpfd\":\"number\",\"arpsfd\":\"number\",\"arfd05\":\"number\",\"arfd07\":\"number\",\"reflg\":\"string\",\"arcpinfd\":\"number\",\"arpsinfd\":\"number\",\"arinfd05\":\"number\",\"cpname\":\"string\",\"sacode\":\"string\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_2)", "[{\"ysym\":\"应缴年月\",\"acym\":\"实缴年月\",\"ftym\":\"到账年月\",\"rewage\":\"缴费基数\",\"arcpfd\":\"单位金额\",\"arpsfd\":\"个人金额\",\"arfd05\":\"医疗补助\",\"arfd07\":\"医疗救助\",\"reflg\":\"类型\",\"arcpinfd\":\"单位划入\",\"arpsinfd\":\"个人划入\",\"arinfd05\":\"补助金划入\",\"cpname\":\"所在单位\",\"sacode\":\"参保地\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_2)", "[{\"ysym\":\"D\",\"acym\":\"D\",\"ftym\":\"D\",\"rewage\":\"D\",\"arcpfd\":\"D\",\"arpsfd\":\"D\",\"arfd05\":\"D\",\"arfd07\":\"D\",\"reflg\":\"D\",\"arcpinfd\":\"D\",\"arpsinfd\":\"D\",\"arinfd05\":\"D\",\"cpname\":\"D\",\"sacode\":\"D\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(divpagetypes)", "[{\"div_1\":\"2\",\"div_2\":\"1\"}]"));

        String commData = commAction(webClient, nameValuePairList, cookie);

        String querySQL = null;
        try {
            //System.out.println(commData);
            querySQL = zhoushanParser.parseQuerySQL(commData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dataJson = pageQueryAction(webClient, cookie, querySQL);
        //System.out.println(dataJson);
        List<InsuranceZhoushanMaternity> list = zhoushanParser.parseInsurance(dataJson, InsuranceZhoushanMaternity.class);
        return list;
    }

    /**
     * 失业保险
     *
     * @param webClient
     * @param cookie
     * @return
     */
    private List<InsuranceZhoushanUnemployment> unemploymentInsurance(WebClient webClient, String cookie, String userIDNum, InsuranceZhoushanParser zhoushanParser) throws IOException {
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new NameValuePair("method", "doAction"));// method	doAction

        nameValuePairList.add(new NameValuePair("divForStrust(div_1)", "[{\"iscode\":\"\",\"psname\":\"\",\"year\":\"\"}]"));//            divForStrust(div_1)	[{"iscode":"","psname":"","year":""}]
        nameValuePairList.add(new NameValuePair("divForStrust(div_2)", "[]"));
        nameValuePairList.add(new NameValuePair("divForStrust(commParams)", "[{\"currentEvent\":\"init\",\"currentOpseno\":null,\"currentLoginName\":\"" + userIDNum + "\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(MDParams)", "[{\"active\":\"\",\"auflag\":\"0\",\"baseLocation\":\"/pages/commAction.do?method=geren.Ckgrjfqksy\",\"bdyy\":\"\",\"cprate\":\"\",\"description\":\"失业缴费信息\",\"firstopen\":\"0\",\"focanclose\":\"\",\"functioncode\":\"\",\"functiondesc\":\"\",\"functionid\":\"004111\",\"location\":\"/pages/commAction.do?method=geren.Ckgrjfqksy\",\"log\":\"0\",\"method\":\"geren.Ckgrjfqksy\",\"opctrl\":\"001\",\"orderno\":2,\"owner\":\"\",\"param1\":\"\",\"param2\":\"\",\"param3\":\"\",\"param4\":\"\",\"parent\":\"00411\",\"proc\":\"\",\"prsource\":\"\",\"publicflag\":\"0\",\"rbflag\":\"1\",\"repid\":\"/geren/ckgrjfqksy.cpt\",\"rpflag\":\"0\",\"sysid\":\"00\",\"title\":\"失业缴费信息\",\"type\":\"0\",\"uptype\":\"\",\"vsclass\":\"\",\"ywlx\":\"\",\"zyxx\":\"{}\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_1)", "[{\"iscode\":\"string\",\"psname\":\"string\",\"year\":\"number\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_1)", "[{\"iscode\":\"公民身份号码\",\"psname\":\"姓名：\",\"year\":\"输入要查询的年度：\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_1)", "[{\"iscode\":\"R\",\"psname\":\"R\",\"year\":\"R\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_2)", "[{\"ysym\":\"number\",\"acym\":\"number\",\"ftym\":\"number\",\"rewage\":\"number\",\"arcpfd\":\"number\",\"arpsfd\":\"number\",\"arfd05\":\"number\",\"arfd07\":\"number\",\"reflg\":\"string\",\"arcpinfd\":\"number\",\"arpsinfd\":\"number\",\"arinfd05\":\"number\",\"cpname\":\"string\",\"sacode\":\"string\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_2)", "[{\"ysym\":\"应缴年月\",\"acym\":\"实缴年月\",\"ftym\":\"到账年月\",\"rewage\":\"缴费基数\",\"arcpfd\":\"单位金额\",\"arpsfd\":\"个人金额\",\"arfd05\":\"医疗补助\",\"arfd07\":\"医疗救助\",\"reflg\":\"类型\",\"arcpinfd\":\"单位划入\",\"arpsinfd\":\"个人划入\",\"arinfd05\":\"补助金划入\",\"cpname\":\"所在单位\",\"sacode\":\"参保地\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_2)", "[{\"ysym\":\"D\",\"acym\":\"D\",\"ftym\":\"D\",\"rewage\":\"D\",\"arcpfd\":\"D\",\"arpsfd\":\"D\",\"arfd05\":\"D\",\"arfd07\":\"D\",\"reflg\":\"D\",\"arcpinfd\":\"D\",\"arpsinfd\":\"D\",\"arinfd05\":\"D\",\"cpname\":\"D\",\"sacode\":\"D\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(divpagetypes)", "[{\"div_1\":\"2\",\"div_2\":\"1\"}]"));


        String commData = commAction(webClient, nameValuePairList, cookie);

        String querySQL = null;
        try {
            // System.out.println(commData);
            querySQL = zhoushanParser.parseQuerySQL(commData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dataJson = pageQueryAction(webClient, cookie, querySQL);

        //System.out.println(dataJson);
        List<InsuranceZhoushanUnemployment> list = zhoushanParser.parseInsurance(dataJson, InsuranceZhoushanUnemployment.class);
        return list;
    }

    /**
     * 用户基本信息
     *
     * @param webClient
     * @param cookie
     * @return
     */
    private List<InsuranceZhoushanUserInfo> userInfo(WebClient webClient, String cookie, String userIDNum, InsuranceZhoushanParser zhoushanParser) throws IOException {
        List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
        nameValuePairList.add(new NameValuePair("method", "doAction"));// method	doAction
        nameValuePairList.add(new NameValuePair("divForStrust(div_1)", "[{\"iscode\":\"\",\"psname\":\"\",\"bdatee\":\"\",\"tel\":\"\",\"mtel\":\"\",\"zip\":\"\",\"address\":\"\",\"psseno\":\"\",\"cpseno\":\"\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(div_3)", "[{\"sb_tel\":\"\",\"sb_mtel\":\"\",\"sb_zip\":\"\",\"sb_address\":\"\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(div_2)", "[]"));
        nameValuePairList.add(new NameValuePair("divForStrust(commParams)", "[{\"currentEvent\":\"init\",\"currentOpseno\":null,\"currentLoginName\":\"" + userIDNum + "\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(MDParams)", "[{\"active\":\"\",\"auflag\":\"1\",\"baseLocation\":\"/pages/commAction.do?method=geren.Zzrycxjbxg\",\"bdyy\":\"\",\"cprate\":\"\",\"description\":\"人员基本信息修改\",\"firstopen\":\"0\",\"focanclose\":\"\",\"functioncode\":\"\",\"functiondesc\":\"\",\"functionid\":\"0050\",\"location\":\"/pages/commAction.do?method=geren.Zzrycxjbxg\",\"log\":\"0\",\"method\":\"geren.Zzrycxjbxg\",\"opctrl\":\"001\",\"orderno\":1,\"owner\":\"\",\"param1\":\"\",\"param2\":\"\",\"param3\":\"\",\"param4\":\"\",\"parent\":\"0048\",\"proc\":\"\",\"prsource\":\"\",\"publicflag\":\"0\",\"rbflag\":\"1\",\"repid\":\"/geren/zzrycxjbxg.cpt\",\"rpflag\":\"0\",\"sysid\":\"00\",\"title\":\"人员基本信息修改\",\"type\":\"0\",\"uptype\":\"\",\"vsclass\":\"\",\"ywlx\":\"01\",\"zyxx\":\"{'姓名':'div_1.psname','身份证':'div_1.iscode'}\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_1)", "[{\"iscode\":\"string\",\"psname\":\"string\",\"bdatee\":\"string\",\"tel\":\"string\",\"mtel\":\"string\",\"zip\":\"string\",\"address\":\"string\",\"psseno\":\"number\",\"cpseno\":\"number\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_1)", "[{\"iscode\":\"公民身份号码\",\"psname\":\"姓名\",\"bdatee\":\"出生年月\",\"tel\":\"联系电话\",\"mtel\":\"手机号码\",\"zip\":\"邮编\",\"address\":\"联系地址\",\"psseno\":\"\",\"cpseno\":\"\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_1)", "[{\"iscode\":\"R\",\"psname\":\"R\",\"bdatee\":\"R\",\"tel\":\"R\",\"mtel\":\"R\",\"zip\":\"R\",\"address\":\"R\",\"psseno\":\"H\",\"cpseno\":\"H\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_3)", "[{\"sb_tel\":\"string\",\"sb_mtel\":\"string\",\"sb_zip\":\"string\",\"sb_address\":\"string\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_3)", "[{\"sb_tel\":\"联系电话\",\"sb_mtel\":\"手机号码\",\"sb_zip\":\"邮编\",\"sb_address\":\"联系地址\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_3)", "[{\"sb_tel\":\"R\",\"sb_mtel\":\"R\",\"sb_zip\":\"R\",\"sb_address\":\"R\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(types_div_2)", "[{\"cpname\":\"string\",\"rylb\":\"string\",\"psstatus\":\"string\",\"sacode\":\"string\",\"ylbs\":\"string\",\"ybbs\":\"string\",\"gsbs\":\"string\",\"ngsybs\":\"string\",\"sybs\":\"string\",\"psseno\":\"number\",\"cpseno\":\"number\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(labels_div_2)", "[{\"cpname\":\"所在单位\",\"rylb\":\"人员类别\",\"psstatus\":\"人员状态\",\"sacode\":\"参保地\",\"ylbs\":\"养老保险\",\"ybbs\":\"医疗保险\",\"gsbs\":\"工伤保险\",\"ngsybs\":\"生育险种\",\"sybs\":\"失业保险\",\"psseno\":\"\",\"cpseno\":\"\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(REDH_div_2)", "[{\"cpname\":\"D\",\"rylb\":\"D\",\"psstatus\":\"D\",\"sacode\":\"D\",\"ylbs\":\"D\",\"ybbs\":\"D\",\"gsbs\":\"D\",\"ngsybs\":\"D\",\"sybs\":\"D\",\"psseno\":\"H\",\"cpseno\":\"H\"}]"));
        nameValuePairList.add(new NameValuePair("divForStrust(divpagetypes)", "[{\"div_1\":\"2\",\"div_2\":\"0\",\"div_3\":\"2\"}]"));

        String commData = commAction(webClient, nameValuePairList, cookie);


        //System.out.println(commData);
        List<InsuranceZhoushanUserInfo> userInfoList = zhoushanParser.parseUserInfo(commData);
        return userInfoList;
    }

    private String pageQueryAction(WebClient webClient, String cookie, String querySQL) throws IOException {
        WebRequest webRequest = new WebRequest(new URL("http://122.226.8.130:9090/zswsbs/common/pageQueryAction.do?method=query"));
        setAdditionalHeader(webRequest);
        List<NameValuePair> requestParameters = new ArrayList<NameValuePair>();
        requestParameters.add(new NameValuePair("method", "query"));
        requestParameters.add(new NameValuePair("div", "div_2"));
        requestParameters.add(new NameValuePair("querySQL", querySQL));
        requestParameters.add(new NameValuePair("sqlType", "SQL"));
        requestParameters.add(new NameValuePair("rowstatus", "U"));
        requestParameters.add(new NameValuePair("limit", "240"));

        webRequest.setRequestParameters(requestParameters);
        webRequest.setAdditionalHeader("Cookie", cookie);
        TextPage page = webClient.getPage(webRequest);
        return page.getContent();
    }

    private String commAction(WebClient webClient, List<NameValuePair> requestParameters, String cookie) throws IOException {
        WebRequest webRequest = new WebRequest(new URL("http://122.226.8.130:9090/zswsbs/pages/comm/commAction.do?method=doAction"));
        setAdditionalHeader(webRequest);
        webRequest.setRequestParameters(requestParameters);
        webRequest.setAdditionalHeader("Cookie", cookie);
        Page page = webClient.getPage(webRequest);
        return page.getWebResponse().getContentAsString();
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
