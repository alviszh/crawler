package app.service;

import app.domain.HousingFundCommonUnit;
import app.parse.HousingFundBaoShanParse;
import app.service.common.HousingBasicService;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.baoshan.HousingBaoShanDetailAccount;
import com.microservice.dao.entity.crawler.housing.baoshan.HousingBaoShanHtml;
import com.microservice.dao.entity.crawler.housing.baoshan.HousingBaoShanUserInfo;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.baoshan.HousingBaoShanDetailAccountRepository;
import com.microservice.dao.repository.crawler.housing.baoshan.HousingBaoShanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.baoshan.HousingBaoShanUserInfoRepository;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

/**
 * Created by zmy on 2018/5/16.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.housing.baoshan"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.housing.baoshan"})
public class HousingFundBaoShanCrawlerService extends HousingBasicService {
    @Autowired
    private HousingFundCommonUnit housingFundCommonUnit;
    @Autowired
    private HousingFundBaoShanParse housingFundBaoShanParse;
    @Autowired
    private HousingBaoShanHtmlRepository housingBaoShanHtmlRepository;
    @Autowired
    private HousingBaoShanUserInfoRepository housingBaoShanUserInfoRepository;
    @Autowired
    private HousingBaoShanDetailAccountRepository housingBaoShanDetailAccountRepository;

    @Async
    public void getUserInfo(TaskHousing taskHousing) throws Exception {
        WebClient webClient = housingFundCommonUnit.addcookie(taskHousing);
        webClient.getOptions().setJavaScriptEnabled(false);
        String url = "https://www.bsgjj.com/bswsyyt/init.summer?_PROCID=70000001";
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        Page hPage = webClient.getPage(webRequest);
        if(null!=hPage){
            String html = hPage.getWebResponse().getContentAsString();
            Document doc = Jsoup.parse(html);
            String accnum = doc.getElementById("accnum").val();  //通过该链接获取个人账号，作为完整信息获取过程中的参数
            String certinum = doc.getElementById("certinum").val(); //证件号码
            String userLevel = doc.getElementById("userLevel").getElementsByAttribute("selected").val();
            System.out.println("userLevel:"+userLevel);
            url="https://www.bsgjj.com/bswsyyt/command.summer?uuid=1526454098493";
            webRequest = new WebRequest(new URL(url), HttpMethod.POST);
            String requestBody = "%24page=%2Fydpx%2F70000001%2F700001_01.ydpx" +
                    "&_ACCNUM=" + accnum.trim() +
//                    "&_LOGINTYPE=1" +
                    "&_PAGEID=step1" +
//                    "&_USER_LEVEL=2" +
                    "&_PROCID=70000001" +
                    "&_SENDOPERID=" + certinum.trim()+
                    "&_TYPE=init" +
                    "&_DEPUTYIDCARDNUM=" + certinum.trim() +
                    "&_IS=-409090" +
                    "&isSamePer=false" +
                    "&_ISCROP=0" +
                    "&accname=&certinum=" + certinum.trim() +
                    "&marstatus=&occupation=&handset=&email=&msg=&opnaccdate=&indisoicode=" +
                    "&userLevel=" + userLevel +
                    "&famaddr=" +
                    "&accnum=" + accnum.trim()+
                    "&indiaccstate=0&unitaccnum=" +
                    "&accinstcode=08750001" +
                    "&calintyear=";
            webRequest.setRequestBody(requestBody);
            Page page = webClient.getPage(webRequest);
            if(null!=page){
                html = page.getWebResponse().getContentAsString();
                System.out.println("获取的用户基本信息是：" + html);
                tracer.addTag("action.crawler.getUserInfo.html", "获取的用户基本信息的html====>"+html);
                String msg = JSONObject.fromObject(html).getJSONObject("data").getString("msg");
                if (msg.contains("\u4ea4\u6613\u5904\u7406\u6210\u529f")) {//交易处理成功
                    HousingBaoShanHtml housingBaoShanHtml = new HousingBaoShanHtml();
                    housingBaoShanHtml.setHtml(html);
                    housingBaoShanHtml.setPagenumber(1);
                    housingBaoShanHtml.setTaskid(taskHousing.getTaskid());
                    housingBaoShanHtml.setType("用户基本信息源码页");
                    housingBaoShanHtml.setUrl(url);
                    housingBaoShanHtmlRepository.save(housingBaoShanHtml);
                    //解析用户信息
                    HousingBaoShanUserInfo housingBaoShanUserInfo = housingFundBaoShanParse.userInfoParser(html, taskHousing);
                    if (null != housingBaoShanUserInfo) {
                        housingBaoShanUserInfoRepository.save(housingBaoShanUserInfo);
                        updateUserInfoStatusByTaskid("数据采集中，用户基本信息已采集完成", 200, taskHousing.getTaskid());
                        tracer.qryKeyValue("action.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
//                        try {
                            getDetailAccount(taskHousing, housingBaoShanUserInfo);
                            updateTaskHousing(taskHousing.getTaskid());
                       /* } catch (Exception e) {
                            tracer.addTag("action.crawler.getDetailAccount.e", taskHousing.getTaskid() + "  " + e.toString());
                            updatePayStatusByTaskid("数据采集中，个人明细账信息已采集完成", 500, taskHousing.getTaskid());
                        }*/
                    }
                } else {
                    tracer.qryKeyValue("action.crawler.getUserInfo.error", "用户基本信息采集失败"+taskHousing.getTaskid());
                    updatePayStatusByTaskid("数据采集中，用户基本信息采集完成", 404, taskHousing.getTaskid());
                }
            }
        }
    }
    //获取个人明细信息
    private void getDetailAccount(TaskHousing taskHousing, HousingBaoShanUserInfo housingBaoShanUserInfo) throws Exception {
        WebClient webClient = housingFundCommonUnit.addcookie(taskHousing);
        webClient.getOptions().setJavaScriptEnabled(false);
        String url="https://www.bsgjj.com/bswsyyt/command.summer?uuid=1526467667591";
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
        //通过测试，发现只需要accnum即可
        String accnum = housingBaoShanUserInfo.getAccnum().trim();
        String certinum = housingBaoShanUserInfo.getCertiNum().trim();
        String requestBody = "%24page=%2Fydpx%2F70000002%2F700002_01.ydpx" +
                "&_ACCNUM=" + accnum.trim() +
//                "&_RW=w&_LOGINTYPE=1" +
                "&_PAGEID=step1" +
                "&_IS=-409568" +
                "&isSamePer=false" +
                "&_USER_LEVEL=2" +
                "&_PROCID=70000002" +
                "&_SENDOPERID=" + certinum.trim() +
                "&_DEPUTYIDCARDNUM=" + certinum.trim() +
                "&_BRANCHKIND=0" +
                "&_TYPE=init&_ISCROP=0" +
                "&_PORCNAME=%E4%B8%AA%E4%BA%BA%E6%98%8E%E7%BB%86%E4%BF%A1%E6%81%AF%E6%9F%A5%E8%AF%A2" +
                "&_WITHKEY=0" +
                "&begdate="+ housingFundCommonUnit.getTwoYearAgoDate().trim() +
                "&enddate="+ housingFundCommonUnit.getPresentDate().trim() +
//                "&year=2018" +
                "&accnum=" + accnum;
        webRequest.setRequestBody(requestBody);
        Page page = webClient.getPage(webRequest);
        if(null!=page){
            String html=page.getWebResponse().getContentAsString();
            tracer.addTag("确定查询日期范围后，返回数据有无提示的页面是：", html);
            System.out.println("确定查询日期范围后，返回数据有无提示的页面是："+html);
            String returnCode = JSONObject.fromObject(html).getString("returnCode");
            if(returnCode.equals("0")){//请求成功
                url="https://www.bsgjj.com/bswsyyt/dynamictable?uuid=1526467669538";
                webRequest = new WebRequest(new URL(url), HttpMethod.POST);
                requestBody = "dynamicTable_id=datalist2" +
                        "&dynamicTable_currentPage=0" +
                        "&dynamicTable_pageSize=50" +
                        "&dynamicTable_nextPage=1" +
                        "&dynamicTable_page=%2Fydpx%2F70000002%2F700002_01.ydpx" +
                        "&dynamicTable_paging=true" +
                        "&dynamicTable_configSqlCheck=0" +
                        "&errorFilter=1%3D1" +
                        "&begdate=" + housingFundCommonUnit.getTwoYearAgoDate().trim()+
                        "&enddate=" + housingFundCommonUnit.getPresentDate().trim()+
//                        "&year=2018" +
                        "&accnum=" + accnum +
                        "&_APPLY=0&_CHANNEL=1" +
                        "&_PROCID=70000002" +
                        "&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAAK%0Ac3IAJWNvbS55ZHlkLm5icC5lbmdpbmUucHViLkRhdGFMaXN0R2hvc3RCsjhA3j2pwwIAA0wAAmRz%0AdAASTGphdmEvbGFuZy9TdHJpbmc7TAAEbmFtZXEAfgADTAADc3FscQB%2BAAN4cHQAEHdvcmtmbG93%0ALmNmZy54bWx0AAlkYXRhbGlzdDJ0AMpzZWxlY3QgaW5zdGFuY2VudW0sIHRyYW5zZGF0ZSwgcGVv%0AcGxlbnVtLCBmcmVldXNlNCwgdW5pdGFjY25hbWUsIGFtdDQsIGFtdDEsIGFtdDIsIGJhc2VudW1i%0AZXIsIGJlZ2luZGF0ZWMsIGVuZGRhdGVjLCBmcmVldXNlNiBmcm9tIGRwMDc3IHdoZXJlIGluc3Rh%0AbmNlbnVtID0tNDA5NTY4IG9yZGVyIGJ5IHRyYW5zZGF0ZSBkZXNjLCBmcmVldXNlNiBkZXNjeA%3D%3D" +
                        "&_DATAPOOL_=rO0ABXNyABZjb20ueWR5ZC5wb29sLkRhdGFQb29sp4pd0OzirDkCAAZMAAdTWVNEQVRFdAASTGph%0AdmEvbGFuZy9TdHJpbmc7TAAGU1lTREFZcQB%2BAAFMAAhTWVNNT05USHEAfgABTAAHU1lTVElNRXEA%0AfgABTAAHU1lTV0VFS3EAfgABTAAHU1lTWUVBUnEAfgABeHIAEWphdmEudXRpbC5IYXNoTWFwBQfa%0AwcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA%2FQAAAAAAAGHcIAAAAIAAAABd0AAdf%0AQUNDTlVNdAAMNTMzMDAwMTc0MjUzdAADX1JXdAABd3QACl9MT0dJTlRZUEV0AAExdAALX1VOSVRB%0AQ0NOVU1wdAAHX1BBR0VJRHQABXN0ZXAxdAADX0lTc3IADmphdmEubGFuZy5Mb25nO4vkkMyPI98C%0AAAFKAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cP%2F%2F%2F%2F%2F%2F%2BcAgdAAMX1VO%0ASVRBQ0NOQU1FcHQABl9MT0dJUHQAETIwMTgwNTE4MTQzMjI0MDg2dAAIX0FDQ05BTUV0AAbnjovl%0AqJ90AAlpc1NhbWVQZXJ0AAVmYWxzZXQAC19VU0VSX0xFVkVMdAABMnQAB19QUk9DSUR0AAg3MDAw%0AMDAwMnQAC19TRU5ET1BFUklEdAASNTMzMDAxMTk4OTA5MjYwMDIzdAAQX0RFUFVUWUlEQ0FSRE5V%0ATXEAfgAddAAJX1NFTkRUSU1FdAAKMjAxOC0wNS0xOHQACV9TRU5EREFURXQACjIwMTgtMDUtMTh0%0AAAtfQlJBTkNIS0lORHQAATB0ABNDVVJSRU5UX1NZU1RFTV9EQVRFdAAKMjAxOC0wNS0xOHQABV9U%0AWVBFdAAEaW5pdHQAB19JU0NST1BxAH4AJHQACV9QT1JDTkFNRXQAGOS4quS6uuaYjue7huS%2FoeaB%0Ar%2BafpeivonQAB19VU0JLRVlwdAAIX1dJVEhLRVlxAH4AJHh0AAhAU3lzRGF0ZXQAB0BTeXNEYXl0%0AAAlAU3lzTW9udGh0AAhAU3lzVGltZXQACEBTeXNXZWVrdAAIQFN5c1llYXI%3D";
                webRequest.setRequestBody(requestBody);
                page = webClient.getPage(webRequest);
                if(null!=page){
                    html=page.getWebResponse().getContentAsString();
                    tracer.addTag("action.crawler.getDetailAccount.html", "获取的个人明细账的html ====" + html);
                    System.out.println("获取的个人明细账的html是："+html);
                    returnCode = JSONObject.fromObject(html).getString("returnCode");
                    if (returnCode.equals("0")) {
                        HousingBaoShanHtml housingBaoShanHtml = new HousingBaoShanHtml();
                        housingBaoShanHtml.setHtml(html);
                        housingBaoShanHtml.setPagenumber(1);
                        housingBaoShanHtml.setTaskid(taskHousing.getTaskid());
                        housingBaoShanHtml.setType("个人明细账源码页");
                        housingBaoShanHtml.setUrl(url);
                        housingBaoShanHtmlRepository.save(housingBaoShanHtml);
                        List<HousingBaoShanDetailAccount> list = housingFundBaoShanParse.detailAccountParser(html, taskHousing);
                        if (null != list && list.size() > 0) {
                            housingBaoShanDetailAccountRepository.saveAll(list);
                            updatePayStatusByTaskid("数据采集中,个人明细账信息已采集完成", 200, taskHousing.getTaskid());
                            tracer.qryKeyValue("action.crawler.getDetailAccount", "数据采集中,个人明细账信息已采集完成");
                        } else {
                            updatePayStatusByTaskid("数据采集中,个人明细账信息暂无可采集数据", 201, taskHousing.getTaskid());
                            tracer.qryKeyValue("action.crawler.getDetailAccount", "数据采集中,个人明细账信息暂无可采集数据");
                        }
                    } else {
                        tracer.qryKeyValue("action.crawler.getDetailAccount.error", "个人明细账信息采集失败" + taskHousing.getTaskid());
                        updatePayStatusByTaskid("数据采集中，个人明细账信息采集完成", 404, taskHousing.getTaskid());
                    }
                }
            }else{
                //未查询到满足个人条件的信息
                updatePayStatusByTaskid("数据采集中,个人明细账信息已采集完成", 201, taskHousing.getTaskid());
                tracer.qryKeyValue("action.crawler.getDetailAccount", "数据采集中,个人明细账信息暂无可采集数据");
            }
        }
    }
}
