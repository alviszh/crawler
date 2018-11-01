package app.parser;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangHtml;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangMedical;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangPension;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangUserInfo;
import com.microservice.dao.entity.crawler.insurance.nanchang.InsuranceNanchangUserList;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;


@Component
public class InsuranceNanchangParser {
	
	@Autowired
	private TracerLog tracer;

    /**
     * @return
     * @throws Exception
     * @Des 登录
     * //     * @param page
     * //     * @param code
     */
    @SuppressWarnings("rawtypes")
    public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
        WebParam webParam = new WebParam();
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        WebRequest webRequest = new WebRequest(new URL("http://hrss.nc.gov.cn/login.jsp"), HttpMethod.POST);
        webRequest.setCharset(Charset.forName("UTF-8"));
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Origin", "http://hrss.nc.gov.cn");
        headers.put("Referer", "http://hrss.nc.gov.cn/login.jsp?returnUrl=http://hrss.nc.gov.cn/insure/query.jsp?locale=zh_CN&message=true&location=http%3A%2F%2Fhrss.nc.gov.cn%2Finsure%2Fquery.jsp%3Flocale%3Dzh_CN&res_system=%2Fskin%2Fgrand&locale=zh_CN&base=&_start_time=1505960174145&skin=skin%2Fgrand%2Fshebao%2Fred");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36");
        webRequest.setAdditionalHeaders(headers);
        List<NameValuePair> requestParameters = new ArrayList<>();
        requestParameters.add(new NameValuePair("username", insuranceRequestParameters.getUsername()));
        requestParameters.add(new NameValuePair("password", insuranceRequestParameters.getPassword()));
        requestParameters.add(new NameValuePair("passwordstrength", ""));
        requestParameters.add(new NameValuePair("returnUrl", "http://hrss.nc.gov.cn/insure/query.jsp?locale=zh_CN"));
        webRequest.setRequestParameters(requestParameters);

        HtmlPage htmlPage = webClient.getPage(webRequest);
        Thread.sleep(2000);
        String alertMsg = WebCrawler.getAlertMsg();
        System.out.println(alertMsg);
        String cookies = CommonUnit.transcookieToJson(htmlPage.getWebClient());
//      System.out.println(cookies);

        if (htmlPage.getWebResponse().getStatusCode() == 200 && !alertMsg.contains("用户名或者密码错误")) {

            HtmlElement descifr = htmlPage.getHtmlElementById("win");//切换到iframe
            String src = descifr.getAttribute("src");
            WebRequest homeRequest = new WebRequest(new URL(src), HttpMethod.GET);
            HtmlPage homehPage = webClient.getPage(homeRequest);

//           System.out.println("homehPage--------" + homehPage.getWebResponse().getContentAsString());


            webParam.setUrl(src);
            webParam.setCode(homehPage.getWebResponse().getStatusCode());
            webParam.setPage(homehPage);
            return webParam;
        }
        if (htmlPage.getWebResponse().getStatusCode() == 200 && alertMsg.contains("用户名或者密码错误")){

            webParam.setUrl("http://hrss.nc.gov.cn/login.jsp");
            webParam.setCode(htmlPage.getWebResponse().getStatusCode());
            webParam.setPage(htmlPage);
            return webParam;
        }
        return null;
    }


    /**
     * 判定是否登录成功
     *
     * @param webClient
     * @return
     * @throws Exception
     */
    public HtmlPage getloginInfo(WebClient webClient) throws Exception {
        String urlData = "http://hrss.nc.gov.cn/insure/query.jsp?locale=zh_CN";
        tracer.addTag("InsuranceNanchangParser.login.getloginInfo", urlData);
        WebRequest webRequest = new WebRequest(new URL(urlData), HttpMethod.GET);
        HtmlPage searchPage = webClient.getPage(webRequest);
        webClient.waitForBackgroundJavaScript(30000);
        return searchPage;
    }

    /**
     *
     * @param taskInsurance
     * @param url
     * @return
     */

    public WebParam getUserList(TaskInsurance taskInsurance, String url) {

        WebParam<InsuranceNanchangUserList> webParam = new WebParam<>();
        HtmlPage page = null;

        try {
            page = getHtmlPage(taskInsurance, url);
        } catch (Exception e) {
                try {
                    page = getHtmlPage(taskInsurance, url);
                } catch (Exception e1){
                    e1.printStackTrace();
                }

            tracer.addTag("InsuranceNanchangParser.getUserList---ERROR:" + url + " ", taskInsurance.getTaskid() + "---ERROR:" + e);
            e.printStackTrace();
        }
            if (null != page) {
                String html = page.getWebResponse().getContentAsString();
                tracer.addTag("InsuranceNanchangParser.getUserList 社保个人信息列表" + taskInsurance.getTaskid(),
                        "<xmp>" + html + "</xmp>");
                List<InsuranceNanchangUserList> userlist = parserList(html, taskInsurance);

                webParam.setHtml(html);
                webParam.setList(userlist);
                return webParam;
            } else {
                tracer.addTag("InsuranceChengduiParser.getUserList---返回社保个人信息列表页面失败:" + url + " ", taskInsurance.getTaskid());
                return null;
            }

    }

    /**
     * 解析个人信息
     * @param html
     * @param taskInsurance
     * @return
     */
    public List<InsuranceNanchangUserList> parserList(String html, TaskInsurance taskInsurance) {
        List<InsuranceNanchangUserList> list = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements link1 = doc.select("tr");
        for (Element link : link1) {
            if (!(link.select("td > a").isEmpty())) {
                Elements elements = link.getElementsByTag("td");
                String taskid = taskInsurance.getTaskid();
                /**
                 * 个人编号
                 */
                String personalNumber = elements.get(0).text();

                /**
                 * 身份证号
                 */
                String idNum = elements.get(1).text();
                /**
                 * 姓名
                 */
                String name = elements.get(2).text();
                /**
                 * 出生日期
                 */
                String birthdate = elements.get(3).text();
                String urlinfor = elements.select("a").attr("href");
//                System.out.println(taskid + personalNumber + idNum + name + birthdate + urlinfor);
                //    InsuranceNanchangUserList insuranceNanchangUserList=new InsuranceNanchangUserList(taskid,personalNumber,idNum,name,birthdate,urlinfor);
                list.add(new InsuranceNanchangUserList(taskid, personalNumber, idNum, name, birthdate, urlinfor));
            }
        }
        return list;
    }

    /**
     * 通过url获取 HtmlPage
     *
     * @param taskInsurance
     * @param url
     * @return
     * @throws Exception
     */
    public HtmlPage getHtmlPage(TaskInsurance taskInsurance, String url)
            throws Exception {
        tracer.addTag("InsuranceChengduiParser.getHtmlPage---url:" + url + " ", taskInsurance.getTaskid());
        WebClient webClient = WebCrawler.getInstance().getWebClient();

        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        HtmlPage searchPage = webClient.getPage(webRequest);
        int statusCode = searchPage.getWebResponse().getStatusCode();
        if (200 == statusCode) {
            String html = searchPage.getWebResponse().getContentAsString();
            tracer.addTag("InsuranceChengduiParser.getHtmlPage---url:" + url + "---taskid:" + taskInsurance.getTaskid(),
                    "<xmp>" + html + "</xmp>");
            if (html.contains("Request forbidden by administrative rules")) {
                tracer.addTag("InsuranceChengduiParser.getHtmlPage---返回HtmlPage失败:" + url + " ", taskInsurance.getTaskid());
                return null;
            }
            return searchPage;
        } else {
            tracer.addTag("InsuranceChengduiParser.getHtmlPage---返回HtmlPage失败:" + url + " ", taskInsurance.getTaskid());
            return null;
        }
    }

    /**
     * 个人信息
     * @param taskInsurance
     * @param lists
     * @return
     */
    public WebParam getUserdetinfo(TaskInsurance taskInsurance, List<String> lists) {
        WebParam webParam = new WebParam();
        List<InsuranceNanchangHtml> listhtml= new ArrayList<>();
        List<InsuranceNanchangUserInfo> listuser = new ArrayList<>();
        String urlbase = "http://218.204.132.6:9090";
        try {
            for (String list : lists) {
                HtmlPage page=null;
                String url = urlbase + list;
//            System.out.println("URL===============================" + url);
                Thread.sleep(1000);
                page = getHtmlPage(taskInsurance, url);
                if (page != null) {
                    String html = page.getWebResponse().getContentAsString();
                    listhtml.add(new InsuranceNanchangHtml(taskInsurance.getTaskid(),page.getContentType(),1,url,html));
                    listuser.add(parseruserinfo(taskInsurance, html));
                }
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceChengduiParser.getUserdetinfo---个人信息页面打开失败:" , taskInsurance.getTaskid());
            e.printStackTrace();
            tracer.addTag("InsuranceChengduiParser.getUserdetinfo---个人信息获取失败:" , taskInsurance.getTaskid());
            return null;
        }
        webParam.setListhtml(listhtml);
        webParam.setList(listuser);
        return webParam;
    }


    /**
     * 解析个人信息
     * @param taskInsurance
     * @param html
     * @return
     */
    private InsuranceNanchangUserInfo parseruserinfo(TaskInsurance taskInsurance, String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("tr");

        /**
         * uuid 前端通过uuid访问状态结果
         */
        String taskid = taskInsurance.getTaskid();
        /**
         * 近两年详细信息的url
         */
        String urldetinfo = elements.get(0).select("a").attr("href");
        /**
         * 个人编号
         */
        String personalNumber = elements.get(0).select("td.fcontent").first().text();
        /**
         * 姓名
         */
        String name = elements.get(2).select("td.fcontent").first().text();
        /**
         * 身份证号
         */
        String idNum = elements.get(1).select("td.fcontent").first().text();
        if (elements.size() > 3) {
            /**
             * 医疗保险单位编号
             */
            String medicalUnitNumber = elements.get(3).select("td.fcontent").first().text();
            /**
             * 医保卡号
             */
            String healthCardNumber = elements.get(4).select("td.fcontent").first().text();
            /**
             * 医疗保险单位名称
             */
            String nameMedicalInsUnit = elements.get(5).select("td.fcontent").first().text();
            /**
             * 医疗保险参保状态
             */
            String healthInsStatus = elements.get(6).select("td.fcontent").first().text();
            /**
             *医疗保险人员状态			例：在职待遇
             */
            String personnelStatus = elements.get(7).select("td.fcontent").first().text();
            /**
             * 医疗保险账户状态
             */
            String healthInsAccStatus = elements.get(8).select("td.fcontent").first().text();
            /**
             * 医疗保险账户余额
             */
            String medicalBalance = elements.get(9).select("td.fcontent").first().text();
            /**
             * 养老保险单位编号
             */
            String olderUnitNumber = elements.get(10).select("td.fcontent").first().text();
            /**
             * 养老保险单位名称
             */
            String olderUnitName = elements.get(11).select("td.fcontent").first().text();
            /**
             * 养老保险参保状态
             */
            String olderInsStatus = elements.get(12).select("td.fcontent").first().text();
            /**
             * 养老保险退休状态
             */
            String olderInsRetiresStatus = elements.get(13).select("td.fcontent").first().text();
            /**
             * 养老保险缴费基数
             */
            String olderInsExpBase = elements.get(14).select("td.fcontent").first().text();
            /**
             * 养老保险账户金额
             */
            String olderInsAccAmount = elements.get(15).select("td.fcontent").first().text();
            /**
             * 养老保险缴费月数
             */
            String olderInsMonthNumber = elements.get(16).select("td.fcontent").first().text();
            InsuranceNanchangUserInfo insuranceNanchangUserInfo = new InsuranceNanchangUserInfo(taskid, personalNumber, name, idNum, medicalUnitNumber, healthCardNumber,
                    nameMedicalInsUnit, healthInsStatus, personnelStatus, healthInsAccStatus, medicalBalance, olderUnitNumber, olderUnitName, olderInsStatus, olderInsRetiresStatus, olderInsExpBase, olderInsAccAmount, olderInsMonthNumber, urldetinfo);

            //   InsuranceNanchangUserInfo insuranceNanchangUserInfo=new InsuranceNanchangUserInfo();
            return insuranceNanchangUserInfo;
        } else {
            InsuranceNanchangUserInfo insuranceNanchangUserInfo = new InsuranceNanchangUserInfo();
            insuranceNanchangUserInfo.setTaskid(taskid);
            insuranceNanchangUserInfo.setIdNum(idNum);
            insuranceNanchangUserInfo.setPersonalNumber(personalNumber);
            insuranceNanchangUserInfo.setName(name);
            insuranceNanchangUserInfo.setUrldetinfo(urldetinfo);
            return insuranceNanchangUserInfo;
        }
    }



    /**
     * 返回医疗信息
     * @param taskInsurance
     * @param url
     * @return
     */
    public WebParam getwebparaMedical(TaskInsurance taskInsurance, String url) {
        List<InsuranceNanchangMedical> lists = new ArrayList<>();
        List<InsuranceNanchangHtml> listhtml = new ArrayList<>();
        for (int i = 1; i < 20; i++) {
            String  url1 = "http://218.204.132.6:9090"+url + "&pageNo=" + i;
            HtmlPage htmlPage = null;
            try {
                Thread.sleep(1000);
                htmlPage = getHtmlPage(taskInsurance, url1);
            } catch (Exception e) {
                htmlPage = null;
                e.printStackTrace();
            }
            if (htmlPage != null) {
                listhtml.add(new InsuranceNanchangHtml(taskInsurance.getTaskid(),htmlPage.getContentType(),i,url1,htmlPage.getWebResponse().getContentAsString()));

                List<InsuranceNanchangMedical> list = parsermedical(taskInsurance, htmlPage.getWebResponse().getContentAsString());
                if (!list.isEmpty()) {
                    lists.addAll(list);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        if (lists.isEmpty()) {

            return null;
        }else {
            WebParam webParam = new WebParam();
            webParam.setListhtml(listhtml);
            webParam.setList(lists);
            return webParam;
        }
    }



    /**
     * 返回养老信息
     * @param taskInsurance
     * @param url
     * @return
     */
    public WebParam getwebparaPension(TaskInsurance taskInsurance, String url){
        List<InsuranceNanchangPension> lists = new ArrayList<>();
        List<InsuranceNanchangHtml> listhtml = new ArrayList<>();
        WebParam webParam = new WebParam();
        try {
            for (int i = 1; i < 10; i++) {
                String url1 = "http://218.204.132.6:9090"+url + "&pageNo=" + i;

                Thread.sleep(1000);

                HtmlPage  htmlPage = getHtmlPage(taskInsurance, url1);
                if (htmlPage!=null){
                    listhtml.add(new InsuranceNanchangHtml(taskInsurance.getTaskid(),htmlPage.getContentType(),i,url1,htmlPage.getWebResponse().getContentAsString()));

                    List<InsuranceNanchangPension> list = parserpension(taskInsurance, htmlPage.getWebResponse().getContentAsString());
                    if (!list.isEmpty()) {
                        lists.addAll(list);
                    } else {
                        break;
                    }
                }else {
                    lists.clear();
                    break;
                }
            }
            if (!lists.isEmpty()){
                webParam.setListhtml(listhtml);
                webParam.setList(lists);
            }
        } catch (Exception e) {
         //   tracer.addTag("InsuranceNanchangParser.getwebparaPension---养老信息页面提取失败", url1);
            e.printStackTrace();
        }
        return webParam;
    }

    /**
     * 解析养老底层页面
     * @param taskInsurance
     * @param html
     * @return
     */
    private List<InsuranceNanchangPension> parserpension(TaskInsurance taskInsurance, String html) {
        Document doc = Jsoup.parse(html);

        List<InsuranceNanchangPension> list = new ArrayList<InsuranceNanchangPension>();
        Elements elements = doc.getElementsByTag("tr");

        if (elements.size() > 2) {

            for (int i = 1; i < elements.size()-1; i++) {


                Elements elements1 = elements.get(i).select("td");
//                System.out.println("elements1.text==============="+elements1.text());
//                System.out.println("elements1.size()==============="+elements1.size());

                String taskid = taskInsurance.getTaskid();
                /**
                 * 个人编号
                 */
                String personalNumber = elements1.get(0).text();
                /**
                 * 费款所属期
                 */
                String feePeriod = elements1.get(1).text();
                /**
                 * 缴费类型
                 */
                String paymentType = elements1.get(2).text();
                /**
                 * 是否欠费
                 */
                String onBills = elements1.get(3).text();
                /**
                 * 缴费基数
                 */
                String paymentBase = elements1.get(4).text();
                /**
                 * 单位缴纳金额
                 */
                String unitPayment = elements1.get(5).text();
                /**
                 * 个人缴纳金额
                 */
                String personalPayment = elements1.get(6).text();
                /**
                 * 总金额
                 */
                String totalMon = elements1.get(7).text();
//                System.out.println(taskid+personalNumber+feePeriod+paymentType+onBills+paymentBase+unitPayment+personalPayment+totalMon);
                InsuranceNanchangPension insuranceNanchangPension = new InsuranceNanchangPension(taskid, personalNumber, feePeriod, paymentType, onBills, paymentBase, unitPayment, personalPayment, totalMon);
                list.add(insuranceNanchangPension);
            }
        }
        return list;
    }

    /*
        * 解析医疗底层页面
        *
        *
        * */
    private List<InsuranceNanchangMedical> parsermedical(TaskInsurance taskInsurance, String html) {
        Document doc = Jsoup.parse(html);
        List<InsuranceNanchangMedical> list = new ArrayList<InsuranceNanchangMedical>();
        Elements elements = doc.getElementsByTag("tr");
        if (elements.size() > 2) {
            for (int i = 1; i < elements.size()-1; i++) {
                Elements elements1 = elements.get(i).select("td");
                String taskid = taskInsurance.getTaskid();
                /**
                 * 个人编号
                 */
                String personalNumber = elements1.get(0).text();
                /**
                 * 费款所属期
                 */
                String feePeriod = elements1.get(1).text();
                /**
                 * 缴费类型
                 */
                String paymentType = elements1.get(2).text();
                /**
                 * 是否欠费
                 */
                String onBills = elements1.get(3).text();
                /**
                 * 缴费基数
                 */
                String paymentBase = elements1.get(4).text();
                /**
                 * 单位缴纳金额
                 */
                String unitPayment = elements1.get(5).text();
                /**
                 * 个人缴纳金额
                 */
                String personalPayment = elements1.get(6).text();
                /**
                 * 总金额
                 */
                String totalMon = elements1.get(7).text();
                InsuranceNanchangMedical insuranceNanchangMedical = new InsuranceNanchangMedical(taskid, personalNumber, feePeriod, paymentType, onBills, paymentBase, unitPayment, personalPayment, totalMon);
                list.add(insuranceNanchangMedical);
            }

        }
        return list;
    }

    /**
     * 解析底层列表信息，返回各个社保明细的url 目前仅有医疗
     * @param taskInsurance
     * @param url
     * @return
     */
    public List<String> getlisturl(TaskInsurance taskInsurance, String url) {
        List<String> list = new ArrayList<>();
        String url1 = "http://218.204.132.6:9090"+url;
        HtmlPage html = null;
        try {
            html = getHtmlPage(taskInsurance, url1);
            if (html != null) {
                Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());
                Element element = doc.select("ul.lsjl").first();
                Elements elements = element.select("li > a");
                for (Element ele : elements) {
                    list.add(ele.attr("href"));
                }
                return list;
            }else {
                return null;
            }
        } catch (Exception e) {
            tracer.addTag("InsuranceNanchangParser.getlisturl---解析底层列表信息（医疗......url）提取失败", url1);
            e.printStackTrace();
            return null;
        }

    }


}
