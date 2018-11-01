package app.service;

import app.commontracerlog.TracerLog;
import app.parser.BeijingBankDebitCardParser;
import app.service.aop.ICrawlerLogin;
import com.crawler.bank.json.BankJsonBean;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.beijingbank.BeijingBankDebitCardAccount;
import com.microservice.dao.entity.crawler.bank.beijingbank.BeijingBankDebitCardHtml;
import com.microservice.dao.entity.crawler.bank.beijingbank.BeijingBankDebitCardTransFlow;
import com.microservice.dao.repository.crawler.bank.beijingbank.BeijingBankDebitCardAccountRepository;
import com.microservice.dao.repository.crawler.bank.beijingbank.BeijingBankDebitCardHtmlRepository;
import com.microservice.dao.repository.crawler.bank.beijingbank.BeijingBankDebitCardTransFlowRepository;
import com.module.htmlunit.WebCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zmy on 2018/3/15.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.beijingbank"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.beijingbank"})
public class BeijingBankDebitCardCrawlerService{

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private BeijingBankDebitCardAccountRepository beijingBankDebitCardAccountRepository;
    @Autowired
    private BeijingBankDebitCardHtmlRepository beijingBankDebitCardHtmlRepository;
    @Autowired
    private BeijingBankDebitCardTransFlowRepository beijingBankDebitCardTransFlowRepository;
    @Autowired
    private BeijingBankDebitCardParser beijingBankDebitCardParser;
    @Autowired
    private TaskBankStatusService taskBankStatusService;

    String url = "https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans";

    /**
     * 获取账户信息（活期、整存整取）
     * @param taskBank
     * @param sessionId
     * @param accountNo
     * @throws Exception
     */
    public void getAccountInfo(TaskBank taskBank, String sessionId, String accountNo){
        tracerLog.addTag("开始采集账户信息（活期、整存整取）","" + taskBank.getTaskid());
        try {
            String body = "dse_sessionId=" + sessionId +
                    "&dse_parentContextName=" +
                    "&dse_operationName=queryAccountBalanceOp" +
                    "&dse_pageId=9" +
//                "&dse_processorState=" +
//                "&dse_processorId=" +
                    "&tranCode=Pb00501" +
                    "&tranFlag=0" +
//                "&topNum=010100" +
//                "&currType=" +
//                "&saveType=" +
                    "&accountNo=" + accountNo;
//                + "&subAccount=&sDate=&eDate=&Alais=";
            Page page = getPage(url, body, taskBank);
            int statusCode = page.getWebResponse().getStatusCode();
            if (statusCode == 200) {
                String html = page.getWebResponse().getContentAsString();
//            System.out.println("=================html=" + html);

                //存储账户信息
                BeijingBankDebitCardHtml beijingBankDebitCardHtml = new BeijingBankDebitCardHtml();
                beijingBankDebitCardHtml.setTaskid(taskBank.getTaskid());
                beijingBankDebitCardHtml.setPagenumber(1);
                beijingBankDebitCardHtml.setHtml(html);
                beijingBankDebitCardHtml.setUrl(url);
                beijingBankDebitCardHtml.setType("账户信息源码页已入库");
                beijingBankDebitCardHtml.setBody(body);
//            System.out.println("beijingBankDebitCardHtml:"+ beijingBankDebitCardHtml);
                beijingBankDebitCardHtmlRepository.save(beijingBankDebitCardHtml);
                tracerLog.addTag("crawler.accountInfo.html", "账户信息源码页已入库" + taskBank.getTaskid());

                //解析账户信息
                List<BeijingBankDebitCardAccount> accountInfos = beijingBankDebitCardParser.accountInfoParser(taskBank, html);
                System.out.println("accountInfos="+ accountInfos);
                if (accountInfos.size() > 0) {
                    //获取账户的详情信息
                    for (BeijingBankDebitCardAccount account : accountInfos) {
                        Thread.sleep(5000);
                        String href = account.getHref();
                        String accountBody = beijingBankDebitCardParser.getAccountBody(href, html);
                        tracerLog.addTag("账户详细信息body", accountBody);
                        System.out.println("账户详细信息body=" + accountBody);
                        System.out.println("href" + href);
                        Page accountPage = getPage(url, accountBody, taskBank);
                        System.out.println("=========账户详细信息：" + accountPage.getWebResponse().getContentAsString());
                        //解析账户详情
                        String accountHtml = accountPage.getWebResponse().getContentAsString();
                        BeijingBankDebitCardAccount accountInfoDetail = beijingBankDebitCardParser.accountInfoDetailParser(accountHtml);
                        account.setUsername(accountInfoDetail.getUsername());
                        account.setBaseRate(accountInfoDetail.getBaseRate());
                        account.setOpenDate(accountInfoDetail.getOpenDate());
                        account.setCurrencyType(accountInfoDetail.getCurrencyType());
                        account.setAccountStatus(accountInfoDetail.getAccountStatus());
                        account.setMedicare(accountInfoDetail.getMedicare());
                        account.setWithhold(accountInfoDetail.getWithhold());
                        account.setArchivedWay(accountInfoDetail.getArchivedWay());
                    }
                    beijingBankDebitCardAccountRepository.saveAll(accountInfos);
                    tracerLog.addTag("crawler.accountInfo.result", "数据采集中，【账户信息】已入库，表名：beijingbank_debitcard_account，总共"
                            + accountInfos.size()+"条，"+ taskBank.getTaskid());
                    taskBankStatusService.updateTaskBankUserinfo(200,"数据采集中，【账户信息】已采集完成",taskBank.getTaskid());
                } else {
                    tracerLog.addTag("crawler.accountInfo.result", "数据采集中，【账户信息】无数据，表名：beijingbank_debitcard_account"
                            + taskBank.getTaskid());
                    taskBankStatusService.updateTaskBankUserinfo(201,"数据采集中，【账户信息】无数据",taskBank.getTaskid());
                }
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String strs = sw.toString();
            System.out.println(""+strs);
            tracerLog.addTag("crawler.getAccountInfo.exception","用户信息爬取失败");
            tracerLog.addTag("crawler.getAccountInfo.e", taskBank.getTaskid() + ", " + strs);
            taskBankStatusService.updateTaskBankUserinfo(404,"银行官网系统繁忙，用户基本信息暂不支持数据采集，请稍后再试",taskBank.getTaskid());
            taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
        }
    }

    //下载、读取交易明细
    public void getTransflowXls(WebClient webClient, TaskBank taskBank, String sessionId) {
        tracerLog.addTag("开始采集交易明细记录","" + taskBank.getTaskid());
        try {
            //获取卡交易明细的请求参数
            String body = "dse_sessionId=" + sessionId +
                    "&dse_parentContextName=" +
                    "&dse_operationName=tranDispatchOp" +
                    "&dse_pageId=8&menuCode=PB020103";
            Page page = getPage(url, body, taskBank);
            int statusCode = page.getWebResponse().getStatusCode();
            if (statusCode == 200) {
                String html = page.getWebResponse().getContentAsString();
//            System.out.println("=================html=" + html);
                Map<String, String> selectAccMsgFormParam = beijingBankDebitCardParser.getTransFlowBase(html,"selectAccMsgForm", sessionId);
                System.out.println("selectAccMsgFormParamMap="+ selectAccMsgFormParam);
                String accountNo = selectAccMsgFormParam.get("accountNo"); //卡号
                String tranFlag = selectAccMsgFormParam.get("tranFlag");
                Thread.sleep(500);

                //分账户列表
                String url1 = "https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans?dse_sessionId=" + sessionId +
                        "&dse_parentContextName=" +
                        "&dse_operationName=selectAccMsgOp" +
                        "&dse_pageId=11" +
//                "&dse_processorState=&dse_processorId=" +
                        "&accountNo=" + accountNo +
                        "&tranFlag=" + tranFlag;
                System.out.println("获取分账户列表，url："+url1);
                WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
                Page result = webClient.getPage(webRequest);
                String acctMsgHtml = result.getWebResponse().getContentAsString();
                Map<String, String> queryformParam = beijingBankDebitCardParser.getTransFlowBase(html,"queryform", sessionId);
                List<String> transflowBodys = beijingBankDebitCardParser.getTransflowBody(acctMsgHtml, queryformParam);//获取请求正文

                //分账号：下载，读取交易明细
                int size = 0;//流水入库计数器
                for (String transflowBody : transflowBodys) {
                    Thread.sleep(3000);
                    //获取分账号：
                    String subAccount = beijingBankDebitCardParser.subStringTxt(transflowBody, "&subAccount=", "&");
                    System.out.println("transflowBody=" + transflowBody +"\n 分账号："+ subAccount);
                    tracerLog.addTag("交易明细-分账号：", subAccount);
                    Page detailPage = getPage(url, transflowBody, taskBank); //获取明细页
                    int detailStatusCode = detailPage.getWebResponse().getStatusCode();
                    if (detailStatusCode == 200) {
                        String detailHtml = detailPage.getWebResponse().getContentAsString();
//                    System.out.println("====明细===" + detailHtml);
                        tracerLog.addTag("交易明细-页面",detailHtml);

                        String downloadBody = beijingBankDebitCardParser.getDownloadBody(detailHtml, sessionId);
                        //获取卡号：
                        String acctNo = beijingBankDebitCardParser.subStringTxt(downloadBody, "&accountNo=", "&");
                        //获取开户行：
                        String openOrgBank = beijingBankDebitCardParser.subStringTxt(downloadBody, "&openOrgBank=", "&");
                        Page downloadPage = getPage(url, downloadBody, taskBank); //获取交易明细第一页
                        InputStream contentAsStream = downloadPage.getWebResponse().getContentAsStream();
                        //获取xls文件的存储路径
                        String xlsFilePath = getXlsFilePath(taskBank.getTaskid(), taskBank.getLoginName()+"_"+subAccount);
                        //将流水下载保存为xls文件
                        saveXls(contentAsStream, xlsFilePath);
                        //保存下载的源码页
                        BeijingBankDebitCardHtml debitCardHtml = new BeijingBankDebitCardHtml();
                        debitCardHtml.setUrl(url);
                        debitCardHtml.setPagenumber(1);
                        debitCardHtml.setType("transflow");
                        debitCardHtml.setHtml(xlsFilePath);			//将银行流水的xls文件作为页面源码入库
                        debitCardHtml.setTaskid(taskBank.getTaskid());
                        beijingBankDebitCardHtmlRepository.save(debitCardHtml);

                        //读取CSV文件中的流水信息
                        List<BeijingBankDebitCardTransFlow> transFlows = beijingBankDebitCardParser.readxls(xlsFilePath, taskBank.getTaskid(),
                                subAccount, acctNo, openOrgBank);
                        tracerLog.addTag("crawler.bank.parser.getTransflow.list", transFlows.toString());
                        if(null != transFlows && transFlows.size() > 0){
                            beijingBankDebitCardTransFlowRepository.saveAll(transFlows);
                            tracerLog.addTag("数据采集中，分账号: "+subAccount, "【交易明细】已入库，表名：beijingbank_debitcard_transflow，当前分账户"
                                    + transFlows.size() +"条，" + taskBank.getTaskid());
                            size++;
                        } else {
                            tracerLog.addTag("crawler.transFlow.result", "数据采集中，【交易明细】无记录，表名：beijingbank_debitcard_transflow" + taskBank.getTaskid());
                        }
                    }
                }

                //所有银行流水信息
                if (size > 0) {
                    taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，【交易明细】已采集完成", taskBank.getTaskid());
                } else {
                    taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，【交易明细】无数据", taskBank.getTaskid());
                }
            }
        }catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            String strs = sw.toString();
            System.out.println(""+strs);
            tracerLog.addTag("crawler.transFlow.exception","流水信息爬取失败");
            tracerLog.addTag("crawler.transFlow.e", taskBank.getTaskid() + ", " + strs);
            taskBankStatusService.updateTaskBankTransflow(404, "银行官网系统繁忙，流水信息暂不支持数据采集，请稍后再试", taskBank.getTaskid());
            taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
        }
    }

    public void getTransflow(WebClient webClient, TaskBank taskBank, String sessionId) throws Exception {
        tracerLog.addTag("开始采集交易明细记录","" + taskBank.getTaskid());
        //获取卡交易明细的请求参数
        String body = "dse_sessionId=" + sessionId +
                "&dse_parentContextName=" +
                "&dse_operationName=tranDispatchOp" +
                "&dse_pageId=8&menuCode=PB020103"; //PB020103查询历史明细
        Page page = getPage(url, body, taskBank);
        int statusCode = page.getWebResponse().getStatusCode();
        if (statusCode == 200) {
            String html = page.getWebResponse().getContentAsString();
//            System.out.println("=================html=" + html);
            Thread.sleep(500);

            //分账户列表
            String url1 = "https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans?dse_sessionId=" + sessionId +
                    "&dse_parentContextName=" +
                    "&dse_operationName=selectAccMsgOp" +
                    "&dse_pageId=11" +
//                "&dse_processorState=&dse_processorId=" +
                    "&accountNo=6214686001329643&tranFlag=0";
            WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
            Page result = webClient.getPage(webRequest);
            String acctMsgHtml = result.getWebResponse().getContentAsString();
            Map<String, String> transFlowBase = beijingBankDebitCardParser.getTransFlowBase(html,"selectAccMsgOp", sessionId);
            List<String> transflowBodys = beijingBankDebitCardParser.getTransflowBody(acctMsgHtml, transFlowBase);//获取请求正文

            //获取交易明细第一页
            Thread.sleep(1000);
            for (String transflowBody : transflowBodys) {
                int pageNum = 1;
                Thread.sleep(3000);
                //获取分账号：
                String subAccount = beijingBankDebitCardParser.subStringTxt(transflowBody, "&subAccount=", "&");
                System.out.println("transflowBody=" + transflowBody +"\n 分账号："+ subAccount);
                tracerLog.addTag("交易明细-分账号：", subAccount);
                Page detailPage = getPage(url, transflowBody, taskBank); //返回明细页
                int detailStatusCode = detailPage.getWebResponse().getStatusCode();
                if (detailStatusCode == 200) {
                    String detailHtml = detailPage.getWebResponse().getContentAsString();
//                    System.out.println("====明细===" + detailHtml);

                    //存储交易明细页面
                    BeijingBankDebitCardHtml beijingBankDebitCardHtml = new BeijingBankDebitCardHtml();
                    beijingBankDebitCardHtml.setTaskid(taskBank.getTaskid());
                    beijingBankDebitCardHtml.setPagenumber(pageNum);
                    beijingBankDebitCardHtml.setHtml(detailHtml);
                    beijingBankDebitCardHtml.setUrl(url);
                    beijingBankDebitCardHtml.setType("分账号:" + subAccount +"，第"+pageNum+"页交易明细源码页已入库");
                    beijingBankDebitCardHtml.setBody(transflowBody);
                    System.out.println("beijingBankDebitCardHtml:" + beijingBankDebitCardHtml);
                    beijingBankDebitCardHtmlRepository.save(beijingBankDebitCardHtml);
                    tracerLog.addTag("crawler.transFlow.html，分账号:" + subAccount, "第"+pageNum+"页交易明细源码页已入库" + taskBank.getTaskid());


                    List<BeijingBankDebitCardTransFlow> transFlows = beijingBankDebitCardParser.transflowDetailParser(taskBank, detailHtml);
                    if (transFlows != null && transFlows.size() > 0) {
                        beijingBankDebitCardTransFlowRepository.saveAll(transFlows);
                        tracerLog.addTag("数据采集中，分账号: "+subAccount, "第" + pageNum + "页【交易明细】已入库，表名：beijingbank_debitcard_transflow，当前页"
                                + transFlows.size() +"条，" + taskBank.getTaskid());

                        //交易明细（下一页）
                        int beginPos = 0;
                        int pageSize = 20;
                        int pageCount = beijingBankDebitCardParser.getPageCount(detailHtml);
                        for (int i = 2; i <= pageCount; i++) {  //从第二页开始
                            pageNum++;
                            beginPos += pageSize;
                        /*String nextBody = "dse_sessionId=" + sessionId +
                                "&dse_parentContextName=&dse_operationName=queryHistoryDetailOp&dse_pageId=12&dse_processorState=" +
                                "&dse_processorId=&tranCode=Pb00501&tranFlag=2&topNum=010300&currType=&saveType=&accountNo=6214686001329643" +
                                "&sDate=20170801&eDate=20180321&beginPos=20&subAccount=001&openOrgCode=00000";*/
                            String nextBody = beijingBankDebitCardParser.getNextTransflowBody(detailHtml, beginPos);
                            System.out.println("nextBody====="+nextBody);
                            Page nextPage = getPage(url, nextBody, taskBank);
                            int nextStatusCode = nextPage.getWebResponse().getStatusCode();
                            if (nextStatusCode == 200) {
                                String nextHtml = nextPage.getWebResponse().getContentAsString();
                                System.out.println("====下一页明细===" + nextHtml);

                                //存储账户信息
                                BeijingBankDebitCardHtml nextBBDCHtml = new BeijingBankDebitCardHtml();
                                nextBBDCHtml.setTaskid(taskBank.getTaskid());
                                nextBBDCHtml.setPagenumber(pageNum);
                                nextBBDCHtml.setHtml(nextHtml);
                                nextBBDCHtml.setUrl(url);
                                nextBBDCHtml.setType("分账号:" + subAccount +"，第" + pageNum + "页交易明细源码页已入库");
                                nextBBDCHtml.setBody(nextBody);
                                System.out.println("beijingBankDebitCardHtml:" + nextBBDCHtml);
                                beijingBankDebitCardHtmlRepository.save(nextBBDCHtml);
                                tracerLog.addTag("crawler.transFlow.html，分账号:" + subAccount, "第"+pageNum+"页交易明细源码页已入库" + taskBank.getTaskid());

                                List<BeijingBankDebitCardTransFlow> nextTransFlows = beijingBankDebitCardParser.transflowDetailParser(taskBank, nextHtml);
                                if (nextTransFlows != null && nextTransFlows.size() > 0) {
                                    beijingBankDebitCardTransFlowRepository.saveAll(nextTransFlows);
                                    tracerLog.addTag("数据采集中，分账号: "+subAccount, "第" + pageNum + "页【交易明细】已入库，表名：beijingbank_debitcard_transflow，当前页"
                                            + nextTransFlows.size() +"条，" + taskBank.getTaskid());
                                }
                            }
                            Thread.sleep(5000);
                        }
                        taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，【交易明细】已采集完成", taskBank.getTaskid());
                    } else {
                        tracerLog.addTag("crawler.transFlow.result", "数据采集中，无【交易明细】记录，表名：beijingbank_debitcard_transflow" + taskBank.getTaskid());
                    }
                }
            }
        }

    }

    /**
     * 获取请求页面
     * @param url
     * @param body
     * @return
     * @throws Exception
     */
    public static Page getPage(String url, String body, TaskBank taskBank) throws Exception {
        WebClient webClient = addcookie(taskBank);

        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);

        webRequest.setAdditionalHeader("Accept","text/html, application/xhtml+xml, */*");
        webRequest.setAdditionalHeader("Accept-Language","zh-CN");
        webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
        webRequest.setAdditionalHeader("Host","ebank.bankofbeijing.com.cn");
        webRequest.setAdditionalHeader("Referer","https://ebank.bankofbeijing.com.cn/servlet/BCCBPB.Trans");
        webRequest.setRequestBody(body);
        Page page = webClient.getPage(webRequest);
        return page;
    }

    public static WebClient addcookie(TaskBank taskBank) {
        WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        webClient.getOptions().setJavaScriptEnabled(false);
        Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskBank.getCookies());
        Iterator<Cookie> i = cookies.iterator();
        while (i.hasNext()) {
            webClient.getCookieManager().addCookie(i.next());
        }
        return webClient;
    }

    //获取存储xls文件的路径
    public String getXlsFilePath(String taskid, String accNo) {
        //获取存放流水xls文件的路径
        String path = System.getProperty("user.dir")+"\\file\\";
        tracerLog.addTag("crawler.bank.parser.getXlsFilePath.path", path);
        File parentDirFile = new File(path);
        parentDirFile.setReadable(true);
        parentDirFile.setWritable(true);
        if (!parentDirFile.exists()) {
            parentDirFile.mkdirs();
        }

        String xlsPath = path+""+taskid+"_"+ accNo +".xls";
        tracerLog.addTag("crawler.bank.parser.getXlsFilePath.xlsPath", xlsPath);
        return xlsPath;
    }

    public static void saveXls(InputStream inputStream, String path) throws Exception{
        OutputStream outputStream = new FileOutputStream(path);

        int byteCount = 0;

        byte[] bytes = new byte[1024];

        while ((byteCount = inputStream.read(bytes)) != -1)
        {
            outputStream.write(bytes, 0, byteCount);
        }
        inputStream.close();
        outputStream.close();
    }
}
