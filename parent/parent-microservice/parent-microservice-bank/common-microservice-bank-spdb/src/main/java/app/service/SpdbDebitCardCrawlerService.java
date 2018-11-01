package app.service;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.parser.SpdbDebitCardParser;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.spdb.*;
import com.microservice.dao.repository.crawler.bank.spdb.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zmy on 2017/11/20.
 */
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.basic", "com.microservice.dao.entity.crawler.bank.spdb"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.basic", "com.microservice.dao.repository.crawler.bank.spdb"})
public class SpdbDebitCardCrawlerService {
    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private SpdbDebitCardHtmlRepository spdbDebitCardHtmlRepository;
    @Autowired
    private SpdbDebitCardUserInfoRepository spdbDebitCardUserInfoRepository;
    @Autowired
    private SpdbDebitCardTransFlowRepository spdbDebitCardTransFlowRepository;
    @Autowired
    private SpdbDebitCardRegularRepository spdbDebitCardRegularRepository;
    @Autowired
    private SpdbDebitCardAccountInfoRepository spdbDebitCardAccountInfoRepository;
    @Autowired
    private SpdbDebitCardParser spdbDebitCardParser;
    @Autowired
    private TaskBankStatusService taskBankStatusService;

    /**
     * 采集用户信息
     * @param webClient
     * @param taskBank
     */
    public void getUserInfo(WebClient webClient, TaskBank taskBank) throws Exception{
        tracerLog.output("开始采集用户信息","" + taskBank.getTaskid());
        String url = "https://ebank.spdb.com.cn/nbper/PreSignPubUserInfoModify.do?" +
                "_viewReferer=default,pubsign/SignPubUserInfoModify";
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(false);
        Page page = webClient.getPage(webRequest);
        int statusCode = page.getWebResponse().getStatusCode();
        System.out.println("******statusCode:"+statusCode);

        if (statusCode == 200) {
            String html = page.getWebResponse().getContentAsString();
//            tracerLog.output("账户信息html：","<xmp>"+html+"</xmp>");
//            System.out.println("*****page="+page.getWebResponse().getContentAsString());
            //存储用户信息
            SpdbDebitCardHtml spdbDebitCardHtml = new SpdbDebitCardHtml();
            spdbDebitCardHtml.setTaskid(taskBank.getTaskid());
            spdbDebitCardHtml.setPagenumber(1);
            spdbDebitCardHtml.setHtml(html);
            spdbDebitCardHtml.setUrl(url);
            spdbDebitCardHtml.setType("用户信息源码页已入库");
            System.out.println("spdbDebitCardHtml:"+spdbDebitCardHtml);
            spdbDebitCardHtmlRepository.save(spdbDebitCardHtml);
            tracerLog.output("SpdbDebitCardCrawlerService.getUserinfo", "用户信息源码页已入库" + taskBank.getTaskid());

            //解析用户信息
            SpdbDebitCardUserInfo spdbDebitCardUserInfo = spdbDebitCardParser.userInfoParser(taskBank, html);
            System.out.println("spdbDebitCardUserInfo="+spdbDebitCardUserInfo);
            spdbDebitCardUserInfoRepository.save(spdbDebitCardUserInfo);
            tracerLog.output("SpdbDebitCardCrawlerService.UserInfo.result", "【用户信息】已入库，表名：spdb_debitcard_userinfo" + taskBank.getTaskid());
            taskBankStatusService.updateTaskBankUserinfo(200,"数据采集中，【用户信息】已采集完成",taskBank.getTaskid());

        }

    }

    /**
     * 采集流水信息、定期存款信息、账户信息
     * @param webClient
     * @param taskBank
     * @throws Exception
     */
    public void getTransFlow(WebClient webClient, TaskBank taskBank) throws Exception{
        tracerLog.output("开始采集流水信息","" + taskBank.getTaskid());
        //银行交易明细查询页面（获取爬取流水信息的请求参数）
        String preQueryHistoryUrl = "https://ebank.spdb.com.cn/nbper/QueryHistoryLeft.do";
        WebRequest preQHWebRequest = new WebRequest(new URL(preQueryHistoryUrl), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(false);
        Page preQHPage = webClient.getPage(preQHWebRequest);
        int preQHStatusCode = preQHPage.getWebResponse().getStatusCode();
        if (preQHStatusCode == 200) {
            String preQHHtml = preQHPage.getWebResponse().getContentAsString();
            tracerLog.output("SpdbDebitCardCrawlerService.getTransFlow.getparam.html", "获取银行交易明细查询页面," + taskBank.getTaskid());
//            System.out.println("=================preQHHtml="+preQHHtml);
            List<String> acctNoList = spdbDebitCardParser.getAcctNosParser(preQHHtml);
            tracerLog.output("银行卡数量", "size="+ (acctNoList==null?0:acctNoList.size()));
            System.out.println("卡号集合：acctNoList=" + acctNoList == null ? 0 : acctNoList.size());
            String requestBody = "";
            for (String acctNo : acctNoList) {
                //点击查询的url请求
                String queryHistoryUrl = "https://ebank.spdb.com.cn/nbper/QueryHistory.json";
                WebRequest webRequest = new WebRequest(new URL(queryHistoryUrl), HttpMethod.POST);
                webRequest.setAdditionalHeader("Accept","application/json, */*; q=0.01");
                webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
                webRequest.setAdditionalHeader("Content-Type","application/json;charset=UTF-8");
                webRequest.setAdditionalHeader("Host","ebank.spdb.com.cn");
                webRequest.setAdditionalHeader("Referer","https://ebank.spdb.com.cn/nbper/QueryHistoryCard.do?AcctNo=_encry_7C8D66B3D23F3C9CD7A9EE3265B3D18702BC4D0DF994C4C272853896BE4F89B3E1AC72AD898A3305E93F344BDB59764E3492F00DB8E471556419E952EEE63299F101799243A41D075AB54187A3C28C43135551D3C3BFFD70A0CCA9FD313C5414&acctType=1&overdraftStatus=2&wisdomCardStatus=2&SelectFlag=3&selectedMenu=menu1_1_4");

                int pageNum = 1;//页数
                int beginNum = 1;
                List<SpdbDebitCardTransFlow> spdbDebitCardTransFlows = new ArrayList<>();
                boolean flag = true;
                while (flag) {
                    tracerLog.output("循环获取流水信息页","flag:"+flag + "，起始行beginNum=" + beginNum + ",taskid="+ taskBank.getTaskid());

                    requestBody = getRequestBody(acctNo, beginNum);
                    tracerLog.output("请求参数是：", requestBody);
                    System.out.println("请求参数是：" + requestBody);

                    webRequest.setRequestBody(requestBody);
                    Page page = webClient.getPage(webRequest);
                    int statusCode = page.getWebResponse().getStatusCode();

                    //流水查询结果
                    if (statusCode == 200) {
                        String html = page.getWebResponse().getContentAsString();
//                        tracerLog.output("第"+pageNum+"页银行流水记录html：","<xmp>"/+html+"</xmp>");
//                    System.out.println("*****page="+page.getWebResponse().getContentAsString());

                        JSONObject jsonObject =  JSONObject.fromObject(html);
                        String  errmsg = jsonObject.getString("errmsg");
                        if (errmsg.equals("")) {
                            //存储用户信息 （仅提供2年内的交易明细）
                            SpdbDebitCardHtml spdbDebitCardHtml = new SpdbDebitCardHtml();
                            spdbDebitCardHtml.setTaskid(taskBank.getTaskid());
                            spdbDebitCardHtml.setPagenumber(pageNum);
                            spdbDebitCardHtml.setHtml(html);
                            spdbDebitCardHtml.setUrl(queryHistoryUrl);
                            spdbDebitCardHtml.setType("第"+pageNum+"页银行流水信息源码页已入库");
                            spdbDebitCardHtmlRepository.save(spdbDebitCardHtml);
                            tracerLog.output("SpdbDebitCardCrawlerService.getTransFlow.page.html", "第"+pageNum+"页银行流水信息源码页已入库" + taskBank.getTaskid());

                            //解析银行流水信息
                            List<SpdbDebitCardTransFlow> transFlowList = spdbDebitCardParser.transFlowParser(taskBank, html, acctNo);
                            if (transFlowList.size() > 0) {
                                spdbDebitCardTransFlows.addAll(transFlowList);
                            } else {
                                flag = false;
                                System.out.println("第"+pageNum+"页银行流水信息，无记录");
                                tracerLog.output("crawler.TransFlow.result", "第"+pageNum+"页银行流水信息，无记录");
                            }

                        } else {
                            tracerLog.output("crawler.TransFlow.result", "数据采集中，【流水信息】：" + errmsg+"，"+taskBank.getTaskid());
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                    beginNum  = beginNum + 20;
                    pageNum++;
                }
                //所有银行流水信息
                if (spdbDebitCardTransFlows.size() > 0) {
                    System.out.println("spdbDebitCardTransFlows=" + spdbDebitCardTransFlows.size());
                    spdbDebitCardTransFlowRepository.saveAll(spdbDebitCardTransFlows);
                    tracerLog.output("crawler.TransFlow.result", "数据采集中，【流水信息】已入库，表名：spdb_debitcard_transflow，总计："
                            +spdbDebitCardTransFlows.size()+"条");
                    taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，【流水信息】已采集完成", taskBank.getTaskid());
                } else {
                    tracerLog.output("crawler.TransFlow.result", "数据采集中，【流水信息】无数据，表名：spdb_debitcard_transflow");
                    taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，【流水信息】无数据", taskBank.getTaskid());
                }

                //整存整取定期存款
                getRegular(webClient, taskBank, acctNo);

                //爬取账户信息
                getAccountInfo(webClient, taskBank, acctNo);
            }

        }
    }

    public void getTransFlowXLS(WebClient webClient, TaskBank taskBank) throws Exception{
        tracerLog.output("开始采集流水信息","" + taskBank.getTaskid());
        //银行交易明细查询页面（获取爬取流水信息的请求参数）
        String preQueryHistoryUrl = "https://ebank.spdb.com.cn/nbper/QueryHistoryLeft.do";
        WebRequest preQHWebRequest = new WebRequest(new URL(preQueryHistoryUrl), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(false);
        Page preQHPage = webClient.getPage(preQHWebRequest);
        int preQHStatusCode = preQHPage.getWebResponse().getStatusCode();
        if (preQHStatusCode == 200) {
            String preQHHtml = preQHPage.getWebResponse().getContentAsString();
            tracerLog.output("SpdbDebitCardCrawlerService.getTransFlow.getparam.html", "获取银行交易明细查询页面," + taskBank.getTaskid());
//            System.out.println("=================preQHHtml="+preQHHtml);
            List<String> acctNoList = spdbDebitCardParser.getAcctNosParser(preQHHtml);
            tracerLog.output("银行卡数量", "size="+ (acctNoList==null?0:acctNoList.size()));
            System.out.println("卡号集合：acctNoList=" + acctNoList == null ? 0 : acctNoList.size());
            System.out.println("acctNoList:"+acctNoList);
            int size = 0;//流水入库计数器
            for (String acctNo : acctNoList) {
                System.out.println("acctNoList===acctNo"+acctNo);
                WebParam webParam = spdbDebitCardParser.getTransflow(webClient, taskBank, acctNo);

                SpdbDebitCardHtml debitCardHtml = new SpdbDebitCardHtml();
                debitCardHtml.setUrl(webParam.getUrl());
                debitCardHtml.setPagenumber(1);
                debitCardHtml.setType("transflow");
                debitCardHtml.setHtml(webParam.getHtml());			//将银行流水的xls文件作为页面源码入库
                debitCardHtml.setTaskid(taskBank.getTaskid());
                spdbDebitCardHtmlRepository.save(debitCardHtml);

                if(null != webParam.getList()){
                    spdbDebitCardTransFlowRepository.saveAll(webParam.getList());
                    tracerLog.output("crawler.bank.crawler.getTransflow.success."+ acctNo, "卡号"+ acctNo +"的【流水信息】已入库，表名：spdb_debitcard_transflow，总计："
                            + webParam.getList().size()+"条");
                    size++;
                } else {
                    tracerLog.output("crawler.bank.crawler.getTransflow.success."+ acctNo, "卡号"+ acctNo +"的【流水信息】无数据");
                }

                //整存整取定期存款
                getRegular(webClient, taskBank, acctNo);

                //爬取账户信息
                getAccountInfo(webClient, taskBank, acctNo);
            }

            //所有银行流水信息
            if (size > 0) {
                tracerLog.output("crawler.TransFlow.result", "数据采集中，【流水信息】已入库，表名：spdb_debitcard_transflow");
                taskBankStatusService.updateTaskBankTransflow(200, "数据采集中，【流水信息】已采集完成", taskBank.getTaskid());
            } else {
                tracerLog.output("crawler.TransFlow.result", "数据采集中，【流水信息】无数据，表名：spdb_debitcard_transflow");
                taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，【流水信息】无数据", taskBank.getTaskid());
            }
        }
    }

    /**
     * 采集定期存款
     * @param webClient
     * @param taskBank
     * @throws Exception
     */
    public void getRegular(WebClient webClient, TaskBank taskBank, String acctNo) throws Exception{
        tracerLog.output("开始采集定期存款信息","taskid=" + taskBank.getTaskid());
        String regularUrl = "https://ebank.spdb.com.cn/nbper/BeforeTransferFixToCurrent.do";

        WebRequest webRequest = new WebRequest(new URL(regularUrl), HttpMethod.POST);
        webRequest.setAdditionalHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
        webRequest.setAdditionalHeader("Content-Type","application/x-www-form-urlencoded");
        webRequest.setAdditionalHeader("Host","ebank.spdb.com.cn");
        webRequest.setAdditionalHeader("Referer","https://ebank.spdb.com.cn/nbper/PreBeforeTransferFixToCurrent.do?selectedSubMenu=menu2_1_3_3&selectedMenu=menu2_1_3&_viewReferer=default,deposit/fixed/BeforeTransferFixToCurrent");
        webRequest.setAdditionalHeader("Upgrade-Insecure-Requests","1");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new NameValuePair("_viewReferer","default,deposit/fixed/BeforeTransferFixToCurrent"));
        params.add(new NameValuePair("AcctNo",acctNo));
        webRequest.setRequestParameters(params);
        Page page = webClient.getPage(webRequest);
        int statusCode = page.getWebResponse().getStatusCode();
        //定期存款记录
        if (statusCode == 200) {
            String html = page.getWebResponse().getContentAsString();
//            tracerLog.output("定期存款记录html：","<xmp>"+html+"</xmp>");

            if (!html.contains("无整存整取定期存款")) {
                //定期存款信息
                SpdbDebitCardHtml spdbDebitCardHtml = new SpdbDebitCardHtml();
                spdbDebitCardHtml.setTaskid(taskBank.getTaskid());
                spdbDebitCardHtml.setPagenumber(1);
                spdbDebitCardHtml.setHtml(html);
                spdbDebitCardHtml.setUrl(regularUrl);
                spdbDebitCardHtml.setType("银行定期存款信息源码页已入库");
                spdbDebitCardHtmlRepository.save(spdbDebitCardHtml);
                tracerLog.output("action.crawler.getRegular.html", "银行定期存款信息源码页已入库" + taskBank.getTaskid());

                //解析银行定期存款信息
                List<SpdbDebitCardRegular> spdbDebitCardRegulars = spdbDebitCardParser.regularParser(taskBank, html, acctNo);
                System.out.println("spdbDebitCardRegulars=" + spdbDebitCardRegulars);
                if (spdbDebitCardRegulars.size() > 0) {
                    for (SpdbDebitCardRegular spdbDebitCardRegular : spdbDebitCardRegulars) {
                        String seqNo = spdbDebitCardRegular.getSn();
                        SpdbDebitCardRegular otherRegular = getOtherRegular(webClient, taskBank, acctNo, seqNo);
                        spdbDebitCardRegular.setUserName(otherRegular.getUserName());
                        spdbDebitCardRegular.setUserNo(otherRegular.getUserNo());
                        spdbDebitCardRegular.setAccountType(otherRegular.getAccountType());
                        spdbDebitCardRegular.setSaveTerm(otherRegular.getSaveTerm());
                        spdbDebitCardRegular.setRate(otherRegular.getRate());
                        spdbDebitCardRegular.setStartData(otherRegular.getStartData());
                    }

                    spdbDebitCardRegularRepository.saveAll(spdbDebitCardRegulars);
                    tracerLog.output("action.crawler.regular.result", "数据采集中，【定期存款信息】已入库，表名：spdb_debitcard_regular，总计："
                            + spdbDebitCardRegulars.size() +"条，"+ taskBank.getTaskid());
                } else {
                    tracerLog.output("action.crawler.regular.result", "数据采集中，【定期存款信息】无数据，表名：spdb_debitcard_regular"
                            + taskBank.getTaskid());
                }
            } else {
                tracerLog.output("action.crawler.regular.result", "数据采集中，【定期存款信息】：您该卡、折中无整存整取定期存款"
                        + taskBank.getTaskid());
            }
        }


    }

    /**
     * 爬取银行流水参数
     * @param acctNo
     * @return
     */
    public String getRequestBody(String acctNo, int beginNum){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String toDate = f.format(new Date());
        String beginDate = spdbDebitCardParser.getBeforeYear(2);
        String endDate = toDate;
        String requestBody = "{\"ClickMenu\":\"QueryHistory\"" +
                ",\"SelectFlag\":\"3\"" + //1是在账户详情页面跳转2是交易明细延期查询3是本页面发的交易
                ",\"AcctNo\":\""+acctNo+"\"" + //卡号
                ",\"BeginDate\":\""+beginDate+"\",\"EndDate\":\""+endDate+"\"" + //查询日期
//                ",\"EndDate1\":\"20180201\"" +
                ",\"QueryTrsType\":\"0\"" + //0全部交易明细，1海外交易明细
                ",\"AcctKind\":\"0001\"" + //账户种类: 0001普通活期
                ",\"AcctType\":\"1\"" +
//                ",\"OverdraftStatus\":\"2\"" +
//                ",\"WisdomCardStatus\":\"2\"" +
                ",\"FastSelect\":\"5\"" + //5 按日期查询
//                ",\"InputType\":\"0\",\"InputType1\":\"0\",\"HuoqiShow\":\"1\"" +
                ",\"CurrencyNo\":\"01\",\"CurrencyType\":\"0\"" + //01 人民币 //0 钞, 1 汇
                ",\"LoanType\":\"000000\"" + //贷款类型：000000 全部，112801 融资周转贷款，999999 经营支用贷款
                ",\"LoanStatus\":\"2\"" +  //贷款状态：2 全部，0 未结清，1 结清
//                ",\"BeginDateText\":\"20160202\",\"EndDateText\":\"20180201\"" +
                ",\"BeginNumber\":\""+beginNum+"\"}";
        return requestBody;
    }

    /**
     * 采集账户信息（卡信息）
     * @param webClient
     * @param taskBank
     * @throws Exception
     */
    public void getAccountInfo(WebClient webClient, TaskBank taskBank,String acctNo) throws Exception{
        tracerLog.output("开始采集账户信息","taskid=" + taskBank.getTaskid());
        String url = "https://ebank.spdb.com.cn/nbper/NewQueryBalance.do?AcctNo=" + acctNo;
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(true);
        Page page = webClient.getPage(webRequest);
        int statusCode = page.getWebResponse().getStatusCode();
        System.out.println("******statusCode:"+statusCode);

        if (statusCode == 200) {
            String html = page.getWebResponse().getContentAsString();
//            tracerLog.output("账户信息html：","<xmp>"+html+"</xmp>");
            //存储账户信息
            SpdbDebitCardHtml spdbDebitCardHtml = new SpdbDebitCardHtml();
            spdbDebitCardHtml.setTaskid(taskBank.getTaskid());
            spdbDebitCardHtml.setPagenumber(1);
            spdbDebitCardHtml.setHtml(html);
            spdbDebitCardHtml.setUrl(url);
            spdbDebitCardHtml.setType("账户信息源码页已入库");
            System.out.println("spdbDebitCardHtml:"+spdbDebitCardHtml);
            spdbDebitCardHtmlRepository.save(spdbDebitCardHtml);
            tracerLog.output("action.crawler.getUserinfo.html", "账户信息源码页已入库" + taskBank.getTaskid());

            //解析账户信息
            List<SpdbDebitCardAccountInfo> accountInfos = spdbDebitCardParser.accountInfoParser(taskBank, html, acctNo);
            System.out.println("accountInfos="+ accountInfos);
            if (accountInfos.size() > 0) {
                //获取凭证（卡）的可用余额
                /*for (SpdbDebitCardAccountInfo accountInfo : accountInfos) {
                    String voucherNum = accountInfo.getAcctNo();
                    System.out.println("==========voucherNum="+voucherNum);
                    String canUseBalance = getCanUseBalance(webClient, taskBank, voucherNum);
                    accountInfo.setCanUseBalance(canUseBalance);
                }*/
                spdbDebitCardAccountInfoRepository.saveAll(accountInfos);
                tracerLog.output("action.crawler.accountInfo.result", "数据采集中，【账户信息】已入库，表名：spdb_debitcard_accountinfo，总共："
                        +accountInfos.size()+"条，" + taskBank.getTaskid());
            } else {
                tracerLog.output("action.crawler.accountInfo.result", "数据采集中，【账户信息】无数据" + taskBank.getTaskid());
            }

        }

    }

    /**
     * 获取凭证（卡）的可用余额（账户信息）
     * @param webClient
     * @param taskBank
     * @param acctNo
     * @throws Exception
     */
    public String getCanUseBalance(WebClient webClient, TaskBank taskBank, String acctNo) throws Exception{
        tracerLog.output("action.crawler.getAvailableBalance.start", "开始获取银行卡的可用余额,task=" + taskBank.getTaskid());
        String regularUrl = "https://ebank.spdb.com.cn/nbper/QueryCanUseBalance4.json";

        WebRequest webRequest = new WebRequest(new URL(regularUrl), HttpMethod.POST);
        webRequest.setAdditionalHeader("Accept","application/json, */*; q=0.01");
        webRequest.setAdditionalHeader("Accept-Encoding","gzip, deflate, br");
        webRequest.setAdditionalHeader("Content-Type","application/json;charset=UTF-8");
        webRequest.setAdditionalHeader("Host","ebank.spdb.com.cn");
        webRequest.setAdditionalHeader("Referer","https://ebank.spdb.com.cn/nbper/PreQueryBalance.do?_viewReferer=default,account/QueryBalance&selectedMenu=menu2_1_9");
        String params = "{\"_viewReferer\":\"default,account/QueryBalance\",\"_SysTokenName\":\"ezonui\"," +
                "\"transName\":\"\",\"RadomNo\":\"\"," +
                "\"Flag\":\"\",\"CreditAcctno\":\"\"," +
                "\"AcctNo\":\""+acctNo+"\",\"AcctType\":\"\"," +
                "\"AcctNickName\":\"\",\"FinanceAcctNo\":\"\",\"PayeeAcctNo\":\"\",\"PayAgreeNo\":\"\"," +
                "\"PayerAcctNo\":\"\",\"PayerBankName\":\"\",\"PayerName\":\"\",\"PayeeAccountType\":\"\"," +
                "\"PymtAcctType\":\"\",\"isBondHas\":\"\",\"isFundHas\":\"0\",\"isFinancHas\":\"0\"}";
        System.out.println("===============params="+params);
        webRequest.setRequestBody(params);
        Page page = webClient.getPage(webRequest);
        int statusCode = page.getWebResponse().getStatusCode();

        //获取可用余额
        String canUseBalance = "";
        if (statusCode == 200) {
            String jsonStr = page.getWebResponse().getContentAsString();
            tracerLog.output("获取可用余额Json：", jsonStr);
            if (jsonStr != null && !"".equals(jsonStr)) {
                JSONObject object = JSONObject.fromObject(jsonStr);
                canUseBalance = object.getString("CanUseBalance");
                System.out.println("canUseBalance=" + canUseBalance);
            }
        }

        return canUseBalance;
    }


    /**
     * 获取整存整取的详细信息
     * @param webClient
     * @param taskBank
     * @param acctNo
     * @param seqNo
     * @return
     * @throws Exception
     */
    public SpdbDebitCardRegular  getOtherRegular(WebClient webClient, TaskBank taskBank,String acctNo, String seqNo) throws Exception{
        SpdbDebitCardRegular spdbDebitCardRegular = null;
        String url = "https://ebank.spdb.com.cn/nbper/QueryNoticeWeekDetail.do?AcctNo=" + acctNo + "&SeqNo=" + seqNo;
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(true);
        Page page = webClient.getPage(webRequest);
        int statusCode = page.getWebResponse().getStatusCode();
        System.out.println("******statusCode:"+statusCode);

        if (statusCode == 200) {
            String html = page.getWebResponse().getContentAsString();
            tracerLog.output("账户信息html：","<xmp>"+html+"</xmp>");

            //解析整存整取
            spdbDebitCardRegular = spdbDebitCardParser.otherRegularParser(html);
            System.out.println("spdbDebitCardRegular="+ spdbDebitCardRegular);

        } else {
            tracerLog.output("action.crawler.otherRegularParser", "数据采集中，整存整取其他部分未完成" + taskBank.getTaskid());
        }
        return spdbDebitCardRegular;
    }
}
