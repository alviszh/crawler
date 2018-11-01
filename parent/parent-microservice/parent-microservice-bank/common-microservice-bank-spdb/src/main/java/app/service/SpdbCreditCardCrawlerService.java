package app.service;

import app.commontracerlog.TracerLog;
import app.parser.SpdbCreditCardParser;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.spdb.*;
import com.microservice.dao.repository.crawler.bank.spdb.*;
import com.module.htmlunit.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.spdb"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.spdb"})
public class SpdbCreditCardCrawlerService {
    @Autowired
    private TracerLog tracer;
    @Autowired
    private TaskBankStatusService taskBankStatusService;
    @Autowired
    private SpdbCreditCardHtmlRepository spdbCreditCardHtmlRepository;
    @Autowired
    private SpdbCreditCardInstallmentRepository spdbCreditCardInstallmentRepository;
    @Autowired
    private SpdbCreditCardBillGeneralRepository spdbCreditCardBillGeneralRepository;
    @Autowired
    private SpdbCreditCardBillDetailRepository spdbCreditCardBillDetailRepository;
    @Autowired
    private SpdbCreditCardGeneralInfoRepository spdbCreditCardGeneralInfoRepository;
    @Autowired
    private SpdbCreditCardParser spdbCreditCardParser;


    @Async
    public Future<String> getInstallments(String cookieString, TaskBank taskBank) throws Exception {
        tracer.addTag("开始采集账单分期记录信息","" + taskBank.getTaskid());
        String url = "https://cardsonline.spdbccc.com.cn/icard/queryStmtEppHistory.do?transName=&CardNo=&_viewReferer=defaultError&_locale=zh_CN&SelectedMenuId=menu4_1_1_2" +
                "&ChangeAcctFlag=&htmlType=&_delQuickMenu=ipkguv&_transToken=ptt5cy5l";

        WebClient webClient = addcookie(cookieString);
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(false);
        Page page = webClient.getPage(webRequest);
        int statusCode = page.getWebResponse().getStatusCode();
        System.out.println("******statusCode:"+statusCode);

        if (statusCode == 200) {
            String html = page.getWebResponse().getContentAsString();
            tracer.addTag("账单分期信息html：","<xmp>"+html+"</xmp>");
            //存储账单分期记录源码页
            SpdbCreditCardHtml spdbCreditCardHtml = new SpdbCreditCardHtml();
            spdbCreditCardHtml.setTaskid(taskBank.getTaskid());
            spdbCreditCardHtml.setPagenumber(1);
            spdbCreditCardHtml.setHtml(html);
            spdbCreditCardHtml.setUrl(url);
            spdbCreditCardHtml.setType("账单分期信息源码页已入库");
            System.out.println("spdbCreditCardHtml:"+spdbCreditCardHtml);
            spdbCreditCardHtmlRepository.save(spdbCreditCardHtml);
            tracer.addTag("crawler.getInstallments.html", "账单分期信息源码页已入库" + taskBank.getTaskid());

            //解析账单分期信息
            List<SpdbCreditCardInstallment> spdbCreditCardInstallments = spdbCreditCardParser.installmentsParser(taskBank, html);
            System.out.println("spdbCreditCardInstallments="+spdbCreditCardInstallments);
            spdbCreditCardInstallmentRepository.saveAll(spdbCreditCardInstallments);
            tracer.addTag("action.crawler.Installment.result", "数据采集中，【账单分期】已入库，表名：spdb_creditcard_installment，总共："
                    +(spdbCreditCardInstallments==null?0:spdbCreditCardInstallments.size())+"条，"+ taskBank.getTaskid());

            taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
                    BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(),
                    "数据采集中，【账单分期】已采集完成!",
                    null, false, taskBank.getTaskid());
        }
        return new AsyncResult<String>("200");
    }

    @Async
    public Future<String>  getGeneralInfo(String  cookieString, TaskBank taskBank) throws Exception{
        tracer.addTag("开始采集账户基本信息","" + taskBank.getTaskid());
        String url = "https://cardsonline.spdbccc.com.cn/icard/icard2PerDigest.do";
        String acctInfoUrl = "https://cardsonline.spdbccc.com.cn/icard/queryAcctInfo.do?transName=&CardNo=&_viewReferer=defaultError";
        SpdbCreditCardGeneralInfo spdbCreditCardGeneralInfo = null;

        //第一部分账户信息
        WebClient webClient = addcookie(cookieString);
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
        webClient.getOptions().setJavaScriptEnabled(false);
        Page page = webClient.getPage(webRequest);
        int statusCode = page.getWebResponse().getStatusCode();
        System.out.println("******statusCode:"+statusCode);

        if (statusCode == 200) {
            String html = page.getWebResponse().getContentAsString();
//                tracer.addTag("第一部分账户基本信息html：","<xmp>"+html+"</xmp>");
            //存储账户基本信息源码页
            SpdbCreditCardHtml spdbCreditCardHtml = new SpdbCreditCardHtml();
            spdbCreditCardHtml.setTaskid(taskBank.getTaskid());
            spdbCreditCardHtml.setPagenumber(1);
            spdbCreditCardHtml.setHtml(html);
            spdbCreditCardHtml.setUrl(url);
            spdbCreditCardHtml.setType("第一部分账户基本信息源码页已入库");
//                System.out.println("spdbCreditCardHtml:"+spdbCreditCardHtml);
            spdbCreditCardHtmlRepository.save(spdbCreditCardHtml);
            tracer.addTag("crawler.getGeneralInfo.one.html", "第一部分账户基本信息源码页已入库" + taskBank.getTaskid());

            //解析账户信息
            spdbCreditCardGeneralInfo = spdbCreditCardParser.generalInfoParser(html);
            System.out.println("spdbCreditCardGeneralInfo="+spdbCreditCardGeneralInfo);
        }

        //第二部分账户信息
//            WebClient webClient = addcookie(cookieString);
        WebRequest aiWebRequest = new WebRequest(new URL(acctInfoUrl), HttpMethod.GET);
        Page aiPage = webClient.getPage(aiWebRequest);
        int aiStatusCode = aiPage.getWebResponse().getStatusCode();
        System.out.println("******aiStatusCode:"+aiStatusCode);
        if (aiStatusCode == 200) {
            String html = aiPage.getWebResponse().getContentAsString();
//                tracer.addTag("第二部分账户基本信息html：","<xmp>"+html+"</xmp>");
            //存储账户基本信息源码页
            SpdbCreditCardHtml spdbCreditCardHtml = new SpdbCreditCardHtml();
            spdbCreditCardHtml.setTaskid(taskBank.getTaskid());
            spdbCreditCardHtml.setPagenumber(1);
            spdbCreditCardHtml.setHtml(html);
            spdbCreditCardHtml.setUrl(acctInfoUrl);
            spdbCreditCardHtml.setType("第二部分账户基本信息源码页已入库");
            System.out.println("spdbCreditCardHtml:"+spdbCreditCardHtml);
            spdbCreditCardHtmlRepository.save(spdbCreditCardHtml);
            tracer.addTag("action.crawler.getGeneralInfo.two.html", "第二部分账户基本信息源码页已入库" + taskBank.getTaskid());

            //解析第二部分账户信息
            SpdbCreditCardGeneralInfo acctInfo = spdbCreditCardParser.acctInfoParser(html);
            spdbCreditCardGeneralInfo .setName(acctInfo.getName());
            spdbCreditCardGeneralInfo.setToday(acctInfo.getToday());
            spdbCreditCardGeneralInfo.setAccount(acctInfo.getAccount());
            spdbCreditCardGeneralInfo.setCreditLimit(acctInfo.getCreditLimit());
            spdbCreditCardGeneralInfo.setEssayLimit(acctInfo.getEssayLimit());
            spdbCreditCardGeneralInfo.setCanUseLimit(acctInfo.getCanUseLimit());
            spdbCreditCardGeneralInfo.setMonthlyBillDay(acctInfo.getMonthlyBillDay());
        }

        if (spdbCreditCardGeneralInfo != null) {
            spdbCreditCardGeneralInfo.setTaskid(taskBank.getTaskid());
            spdbCreditCardGeneralInfoRepository.save(spdbCreditCardGeneralInfo);
            tracer.addTag("crawler.GeneralInfo.result", "【账户基本信息】已入库，表名：spdb_creditcard_generalinfo" + taskBank.getTaskid());
            taskBankStatusService.updateTaskBankUserinfo(200,"数据采集中，【账户基本信息】已采集完成",taskBank.getTaskid());
        }
        return new AsyncResult<String>("200");
    }

    @Async
    public Future<String> getBillDetail(String cookieString, TaskBank taskBank) throws Exception{
        tracer.addTag("开始采集账单信息","" + taskBank.getTaskid());
//        String billsMonth = "2017-08"; //账单月份
        String url = "https://cardsonline.spdbccc.com.cn/icard/queryRecentBills.do";
        /*String url = "https://cardsonline.spdbccc.com.cn/icard/queryRecentBills.do?_viewReferer=querybills%2FQueryRecentBills" +
                "&BillsMonthes=%24%7BBillsMonthes%7D&BillsMonth=" + billsMonth;*/
        int billDetailTotal = 0; //统计账单明细总条数
        for (int i = 0; i < 12; i++) {//爬取最近12个月的账单
            try {
                //账单月份
                String billsMonth = getBeforeMonth(i);
                System.out.println("billsMonth:" + billsMonth);

                WebClient webClient = addcookie(cookieString);
                webClient.getOptions().setJavaScriptEnabled(false);
                WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
                webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,**/*//*;q=0.8");
                webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
                webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
                webRequest.setAdditionalHeader("Host", "cardsonline.spdbccc.com.cn");
                webRequest.setAdditionalHeader("Referer", "https://cardsonline.spdbccc.com.cn/icard/preQueryRecentBills.do");

                List<NameValuePair> params = new ArrayList<>();
//                params.add(new NameValuePair("ThirdLevelMenuTransName", "preQueryRecentBills"));
                params.add(new NameValuePair("_viewReferer", "querybills%2FQueryRecentBills"));
                params.add(new NameValuePair("BillsMonthes", "%24%7BBillsMonthes%7D"));
                params.add(new NameValuePair("BillsMonth", billsMonth));
                webRequest.setRequestParameters(params);
                Page page = webClient.getPage(webRequest);
                int statusCode = page.getWebResponse().getStatusCode();
                System.out.println("******statusCode:" + statusCode);

                int pageNum = 1;//页数
                if (statusCode == 200) {
                    String html = page.getWebResponse().getContentAsString();
                    tracer.addTag("账单月份："+billsMonth+",第" + pageNum + "页账单明细html：", "<xmp>" + html + "</xmp>");
                    System.out.println("【账单明细】数据库采集中，账单月份："+billsMonth+",第" + pageNum + "页账单明细");
                    if (html.contains("登录已失效")) {
                        tracer.addTag("【账单明细】数据采集中", "账单月份："+billsMonth+",第" + pageNum + "页，登录已失效，请重新登录！，"
                                + taskBank.getTaskid());
                        taskBankStatusService.updateTaskBankTransflow(404, "数据采集中，登录已失效，请重新登录！", taskBank.getTaskid());

                       /* //释放instance ip ，quit webdriver
                        tracer.addTag("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
                        agentService.releaseInstance(taskBank.getCrawlerHost(), driver);*/
                        return new AsyncResult<String>("404");
                    } else
                    if (!html.contains("该期无账单或账单尚未出")) {
                        //存储第一页【账单明细】源码页
                        SpdbCreditCardHtml spdbccHtml = new SpdbCreditCardHtml();
                        spdbccHtml.setTaskid(taskBank.getTaskid());
                        spdbccHtml.setPagenumber(pageNum);
                        spdbccHtml.setHtml(html);
                        spdbccHtml.setUrl(url);
                        spdbccHtml.setType("账单月份："+billsMonth+",第" + pageNum + "页账单明细源码页已入库");
//                        System.out.println("spdbCreditCardHtml:" + spdbccHtml);
                        spdbCreditCardHtmlRepository.save(spdbccHtml);
                        tracer.addTag("crawler.getBillDetail.html", "账单月份："+billsMonth+",第" + pageNum + "页账单明细源码页已入库" + taskBank.getTaskid());

                        //解析账单明细
                        List<SpdbCreditCardBillDetail> spdbCreditCardBillDetails = spdbCreditCardParser.billDetailParser(taskBank, html, billsMonth);
//                        System.out.println("spdbCreditCardBillDetails=" + spdbCreditCardBillDetails);
                        spdbCreditCardBillDetailRepository.saveAll(spdbCreditCardBillDetails);
                        int size = spdbCreditCardBillDetails==null?0:spdbCreditCardBillDetails.size();
                        tracer.addTag("action.crawler.BillDetail.result，账单月份："+billsMonth, "数据采集中，第" + pageNum +
                                "页【账单明细】已入库，表名：spdb_creditcard_billdetail，当前页"+ size +"条," + taskBank.getTaskid());
                        billDetailTotal += size;

                        tracer.addTag("开始爬取【账单信息】摘要","" + taskBank.getTaskid());
                        //存储【账单信息】摘要源码页
                        SpdbCreditCardHtml spdbCreditCardHtml = new SpdbCreditCardHtml();
                        spdbCreditCardHtml.setTaskid(taskBank.getTaskid());
                        spdbCreditCardHtml.setPagenumber(pageNum);
                        spdbCreditCardHtml.setHtml(html);
                        spdbCreditCardHtml.setUrl(url);
                        spdbCreditCardHtml.setType("账单月份："+billsMonth+",账单信息概要源码页已入库");
                        System.out.println("spdbCreditCardHtml:" + spdbCreditCardHtml);
                        spdbCreditCardHtmlRepository.save(spdbCreditCardHtml);
                        tracer.addTag("action.crawler.getBillGeneral.html", "账单月份："+billsMonth+",账单信息概要源码页已入库" + taskBank.getTaskid());

                        //解析账单信息摘要
                        SpdbCreditCardBillGeneral spdbCreditCardBillGeneral = spdbCreditCardParser.billGeneralParser(taskBank, html, billsMonth);
                        SpdbCreditCardBillDetail spdbCreditCardBillDetail = spdbCreditCardBillDetails.get(0);
                        String cardNum = "";
                        if (spdbCreditCardBillDetail!=null) {
                            cardNum = spdbCreditCardBillDetail.getCardNum();
                        }
                        spdbCreditCardBillGeneral.setCardNum(cardNum);//保存交易卡号后四位
                        System.out.println("spdbCreditCardBillGeneral=" + spdbCreditCardBillGeneral);
                        spdbCreditCardBillGeneralRepository.save(spdbCreditCardBillGeneral);
                        tracer.addTag("action.crawler.getBillGeneral.result", "账单月份："+billsMonth+",数据采集中，【账单信息概要】已入库，" +
                                "表名：spdb_creditcard_billgeneral" + taskBank.getTaskid());

                        Thread.sleep(1000L);

                        //获取【账单明细】下一页的请求参数
                        String detailsHtml = html;
                        Map<String, String> paramParser = spdbCreditCardParser.getParamParser(detailsHtml);
                        String requestBody = getRequestBody(paramParser);
                        System.out.println("===请求参数：" + requestBody);
                        Integer turnoverSign = Integer.parseInt(paramParser.get("turnoverSign"));
                        System.out.println("===turnoverSign=" + turnoverSign); //1 有下一页，0 无下一页

                        //循环获取下一页
                        while (turnoverSign == 1) {
                            pageNum++; //页数

                            long starttimeCode = System.currentTimeMillis();
                            WebClient nextWebClient = addcookie(cookieString);
                            WebRequest nextWebRequest = new WebRequest(new URL(url), HttpMethod.POST);
                            nextWebRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                            nextWebRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
                            nextWebRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
                            nextWebRequest.setAdditionalHeader("Host", "cardsonline.spdbccc.com.cn");
                            nextWebRequest.setAdditionalHeader("Referer", "https://cardsonline.spdbccc.com.cn/icard/queryRecentBills.do");
                            nextWebRequest.setRequestBody(requestBody);
                            Page nextPage = nextWebClient.getPage(nextWebRequest);
                            int nextStatusCode = nextPage.getWebResponse().getStatusCode();
                            System.out.println("******nextStatusCode:" + nextStatusCode);
                            if (nextStatusCode == 200) {
                                String nextHtml = nextPage.getWebResponse().getContentAsString();

                                if (nextHtml.contains("登录已失效")) {
                                    tracer.addTag("crawler.getBillDetail.html", "数据采集中，账单月份："+billsMonth+",第" + pageNum + "页," +
                                            "登录已失效，请重新登录！，"
                                            + taskBank.getTaskid());
                                    taskBankStatusService.updateTaskBankTransflow(404, "数据采集中，登录已失效，请重新登录！", taskBank.getTaskid());

                                   /* //释放instance ip ，quit webdriver
                                    tracer.addTag("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
                                    agentService.releaseInstance(taskBank.getCrawlerHost(), driver);*/
                                    return new AsyncResult<String>("404");
                                }
                                tracer.addTag("账单月份："+billsMonth+",第" + pageNum + "页账单明细html：", "<xmp>" + nextHtml + "</xmp>");
                                detailsHtml = nextHtml;

                                //存储下一页账单明细源码页
                                SpdbCreditCardHtml spdbccHtmlNext = new SpdbCreditCardHtml();
                                spdbccHtmlNext.setTaskid(taskBank.getTaskid());
                                spdbccHtmlNext.setPagenumber(pageNum);
                                spdbccHtmlNext.setHtml(detailsHtml);
                                spdbccHtmlNext.setUrl(url);
                                spdbccHtmlNext.setType("账单月份："+billsMonth+",第" + pageNum + "页账单明细源码页已入库");
                                System.out.println("spdbCreditCardHtml:" + spdbccHtmlNext);
                                spdbCreditCardHtmlRepository.save(spdbccHtmlNext);
                                tracer.addTag("crawler.getBillDetail.html", "账单月份："+billsMonth+",第" + pageNum + "页账单明细源码页已入库" + taskBank.getTaskid());

                                //解析账单明细
                                List<SpdbCreditCardBillDetail> nextSpdbccBillDetails = spdbCreditCardParser.billDetailParser(taskBank, detailsHtml, billsMonth);
                                System.out.println("nextSpdbccBillDetails=" + nextSpdbccBillDetails);
                                spdbCreditCardBillDetailRepository.saveAll(nextSpdbccBillDetails);
                                int sizeNext = nextSpdbccBillDetails==null?0:nextSpdbccBillDetails.size();
                                tracer.addTag("action.crawler.BillDetail.result，账单月份："+billsMonth, "数据采集中，第" + pageNum +
                                        "页【账单明细】已入库，表名：spdb_creditcard_billdetail，当前页"+ sizeNext+"条," + taskBank.getTaskid());
                                billDetailTotal += sizeNext;

                                //获取下一页的请求参数
                                paramParser = spdbCreditCardParser.getParamParser(detailsHtml);
                                requestBody = getRequestBody(paramParser);
                                System.out.println("账单月份："+billsMonth+",第" + pageNum + "页===请求参数：" + requestBody);
//                                tracer.addTag("SpdbccBillDetails.requestBody，账单月份："+billsMonth, "第" + pageNum + "页===请求参数：" + requestBody);
                                turnoverSign = Integer.parseInt(paramParser.get("turnoverSign"));//1：有下一页，2：无
                                System.out.println("===turnoverSign=" + turnoverSign);

                            }
                            long endtimeCode = System.currentTimeMillis();
                            System.out.println("账单月份："+billsMonth+",第" + pageNum + "页爬取所用总时间："+(endtimeCode-starttimeCode)+":ms");
                            Thread.sleep(1000L);
                        }
                    } else {
                        tracer.addTag("账单明细：账单月份："+billsMonth+"第" + pageNum + "页", "该期无账单或账单尚未出，html=="+html);
                    }
                }
                taskBank = taskBankStatusService.changeStatus(BankStatusCode.BANK_CRAWLER_DOING.getPhase(),
                        BankStatusCode.BANK_CRAWLER_DOING.getPhasestatus(),
                        "数据采集中，"+billsMonth+"月份账单明细已采集完成！",
                        null, false, taskBank.getTaskid());

            } catch (Exception e) {
                tracer.addTag("getBillDetail.exception","银行官网系统繁忙");
                tracer.addTag("crawler.getBillDetail.e", "银行官网系统繁忙，账单明细暂不支持数据采集，请稍后再试,"
                        + taskBank.getTaskid() + "  " + e.toString());
                taskBankStatusService.updateTaskBankTransflow(404, "银行官网系统繁忙，流水信息暂不支持数据采集，请稍后再试", taskBank.getTaskid());

                //释放instance ip ，quit webdriver
                /*tracer.addTag("释放instance ip ，quit webdriver:", taskBank.getCrawlerHost());
                agentService.releaseInstance(taskBank.getCrawlerHost(), driver);
                return;*/
            }
            Thread.sleep(5000L); //需要等待3秒以上，不然爬取中会出现登录失效、连接超时的情况
        }
        System.out.println("数据采集中，【账单明细】已采集完成，表名：spdb_creditcard_billdetail，总共"+ billDetailTotal+"条");
        tracer.addTag("crawler.getBillDetail.result","数据采集中，【账单明细】已采集完成，表名：spdb_creditcard_billdetail，总共"+ billDetailTotal+"条，" + taskBank.getTaskid());
        taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，【账单明细】已采集完成", taskBank.getTaskid());
        return new AsyncResult<String>("200");
    }

    public WebClient addcookie(String cookieString) {
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(cookieString);
        Iterator<Cookie> i = cookies.iterator();
        while (i.hasNext()) {
            webClient.getCookieManager().addCookie(i.next());
        }
        return webClient;
    }

    //从当日期开始往前推n月
    public static String getBeforeMonth(int n){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -n);
        String beforeMonth = f.format(c.getTime());
        return beforeMonth;
    }

    //请求参数
    public static String getRequestBody(Map<String, String> paramParser){
        String requestBody = "_viewReferer=querybills%2FQueryRecentBillsRes" +
                "&BillsMonth=" + paramParser.get("billsMonth") +
                "&CurrencyType=" + paramParser.get("currencyType") +
                "&Next=%CF%C2%D2%BB%D2%B3" +
                "&TransJnlNo=" + paramParser.get("transJnlNo") +
                "&ConsumeTime=" + paramParser.get("consumeTime") +
                "&BookedDate=" + paramParser.get("bookedDate") +
                "&RegisterDate=" + paramParser.get("registerDate") +
                "&Command=nextPage" +
                "&TurnoverSign=" + paramParser.get("turnoverSign");
        return requestBody;
    }
}
