package app.parser;

import app.commontracerlog.TracerLog;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.beijingbank.BeijingBankDebitCardAccount;
import com.microservice.dao.entity.crawler.bank.beijingbank.BeijingBankDebitCardTransFlow;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zmy on 2018/3/15.
 */
@Component
public class BeijingBankDebitCardParser {
    @Autowired
    private TracerLog tracerLog;

    /**
     * 解析错误信息
     * @param html
     * @return
     */
    public String getErrInfo(String html) {
        String errInfo = "登录异常，请重试";
        Document doc = Jsoup.parse(html);
        Elements divs = doc.select("div[class=info_result_ditail]");
        if (divs!=null && divs.size() > 0) {
            Element div = divs.get(0);
            Elements tables = div.select("TABLE");
            System.out.println("tables=========="+tables);
            if (tables != null && tables.size() > 0) {
                Element table = tables.get(0);
                Elements trs = table.select("tr");
                if (trs != null) {
                    Elements tds = trs.get(0).select("td");
                    errInfo = tds.get(1).text();
                }
            } else {
                errInfo = div.text();//验证码错误
            }
        }
        return errInfo;
    }

    /**
     * 解析账户信息
     * @param taskBank
     * @param html
     * @return
     */
    public List<BeijingBankDebitCardAccount> accountInfoParser(TaskBank taskBank, String html) {
        List<BeijingBankDebitCardAccount> result = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("td:contains(账/卡号)+td");
        if (elements != null && elements.size() > 0) {
            String acctNo = elements.first().text();
            String bankName = document.select("td:contains(开户行)+td").first().text();

            Elements tbodys = document.select("tbody#accInfo");
            if (tbodys != null) {
                Elements trs = tbodys.select("tr");
                if (trs != null && trs.size() > 0) {
                    for (int i = 0; i < trs.size(); i++) {
                        Elements tds = trs.get(i).select("td");
                        BeijingBankDebitCardAccount account = new BeijingBankDebitCardAccount();
                        account.setTaskid(taskBank.getTaskid());
                        account.setAcctNo(acctNo);
                        account.setBankName(bankName);
                        account.setPointsAccount(tds.get(0).text());
                        account.setCurrency(tds.get(1).text());
                        account.setProductName(tds.get(2).text());
                        account.setDepositTerm(tds.get(3).text());
                        account.setDueDate(tds.get(4).text());
                        account.setStrikeRate(tds.get(5).text());
                        account.setBalances(tds.get(6).text());
                        account.setCanUseBalance(tds.get(7).text());
                        String href = tds.get(8).select("a").attr("href");
                        account.setHref(href);
                        System.out.println("href===="+ href);
                        result.add(account);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获取账户明细的请求参数
     * @param href
     * @param html
     * @return
     */
    public String getAccountBody(String href, String html){
        Document document = Jsoup.parse(html);
        String accountBody = "";
        Elements forms = document.select("form[name=querybaseform]");
//        System.out.println("form===="+forms.html());
        if (forms != null && forms.size() > 0) {
            Element form =forms.get(0);
            String sessionId = form.select("input[name=dse_sessionId]").get(0).val();
            String operationName = form.select("input[name=dse_operationName]").get(0).val();
            String pageId = form.select("input[name=dse_pageId]").get(0).val();
            String tranCode = form.select("input[name=tranCode]").get(0).val();
            String topNum = form.select("input[name=topNum]").get(0).val();
            String tranFlag = form.select("input[name=tranFlag]").get(0).val();
            String accountNo = form.select("input[name=accountNo]").get(0).val();
            String addFlag = form.select("input[name=addFlag]").get(0).val();
            String account = form.select("input[name=account]").get(0).val();

            String subAccount = "";
            String currType = "";
            String saveType = "";
            String tranStep = "";

            if (href != null && !"".equals(href)) {
                if (href.contains("'")) {
                    String[] split = href.split("'");
                    String paramstr = split[1];
                    if (paramstr.contains("|")) {
                        String[] params = paramstr.split("\\|");
                        subAccount = params[0];
                        currType = params[1];
                        saveType = params[2];
                        if (saveType.equals("04") && params[3].equals("B")) {
                            tranStep = "1";
                        } else {
                            tranStep = "0";
                        }
                    }
                }
            }

            accountBody = "dse_sessionId=" + sessionId +
                    "&dse_parentContextName=" +
                    "&dse_operationName=" + operationName +
                    "&dse_pageId=" + pageId +
//                    "&dse_processorState=" +
//                    "&dse_processorId=" +
                    "&tranCode=" + tranCode +
                    "&topNum=" + topNum +
                    "&tranFlag=" + tranFlag +
                    "&currType=" + currType +
                    "&saveType=" + saveType +
                    "&accountNo=" + accountNo +
                    "&subAccount=" + subAccount +
                    "&sDate=&eDate=" +
                    "&addFlag=" + addFlag +
                    "&account=" + account +
                    "&tranStep=" + tranStep; //0:普通的信息查看 1：滚滚利账户查看，需查询开户日期
        }
        return accountBody;
    }

    public BeijingBankDebitCardAccount accountInfoDetailParser(String html) {
        BeijingBankDebitCardAccount account = new BeijingBankDebitCardAccount();
        Document document = Jsoup.parse(html.toString().replace("&nbsp;", ""));
        Elements countent = document.select("div[class=info_content]");
        if (countent != null && countent.size() > 0) {
            Element element = countent.get(0);
            System.out.println(element.html());
            System.out.println("=====text==" +element.text());
            String text = element.text();
            if (text.contains("户名")) {
                String username = subStringTxt(text, "户名：", "基本信息").trim();
                System.out.println("户名是：" + username);
                account.setUsername(username);
            }

            String baseRate = element.select("td:contains(基准利率)+td").first().text();
            String openDate = element.select("td:contains(开户日期)+td").first().text();
            String currencyType = element.select("td:contains(汇钞标志)+td").first().text();
            String accountStatus = element.select("td:contains(账户状态)+td").first().text();
            String medicare= "";
            Elements medicareEles = element.select("td:contains(已关联医保存折)+td");
            if (medicareEles != null && medicareEles.size() > 0) {
                medicare = medicareEles.first().text();
            }
            String withhold = "";
            Elements withholdEles = element.select("td:contains(代发代扣)+td");
            if (withholdEles != null && withholdEles.size() > 0) {
                withhold = withholdEles.first().text();
            }
            String archivedWay = "";
            Elements archivedWayEles = element.select("td:contains(转存方式)+td");
            if (archivedWayEles != null && archivedWayEles.size() > 0) {
                archivedWay = archivedWayEles.first().text();
            }
            account.setBaseRate(baseRate);
            account.setOpenDate(openDate);
            account.setCurrencyType(currencyType);
            account.setAccountStatus(accountStatus);
            account.setMedicare(medicare);
            account.setWithhold(withhold);
            account.setArchivedWay(archivedWay);
        }
        return account;
    }

    public Map<String, String> getTransFlowBase(String html,String formName, String sessionId){
        tracerLog.addTag("BeijingBankDebitCardParser.getTransFlowBase","dse_operationName=" + formName);
        Map<String, String> result = new HashMap<>();
        Document document = Jsoup.parse(html);
        String accountNo = document.select("td:contains(账/卡号)+td").first().text();
        System.out.println("getTransFlowBase.accountNo:"+accountNo);
        result.put("accountNo", accountNo);
        if (formName.equals("queryform")) {
            Elements forms = document.select("form[name=queryform]");
            if (forms != null && forms.size() > 0) {
                Element form = forms.get(0);
                String dse_parentContextName = form.select("input[name=dse_parentContextName]").get(0).val();
                String operationName = form.select("input[name=dse_operationName]").get(0).val();
                String pageId = form.select("input[name=dse_pageId]").get(0).val();
                String tranCode = form.select("input[name=tranCode]").get(0).val();
                String topNum = form.select("input[name=topNum]").get(0).val();
                String tranFlag = form.select("input[name=tranFlag]").get(0).val();
                String beginPos = form.select("input[name=beginPos]").get(0).val();
                String showFlg = form.select("input[name=showFlg]").get(0).val();
                result.put("sessionId", sessionId);
                result.put("dse_parentContextName", dse_parentContextName);
                result.put("operationName", operationName);
                result.put("pageId", pageId);
                result.put("tranCode", tranCode);
                result.put("topNum", topNum);
                result.put("tranFlag", tranFlag);
                result.put("beginPos", beginPos);
                result.put("showFlg", showFlg);
            }
        } else if (formName.equals("selectAccMsgForm")){
            Elements forms = document.select("form[name=selectAccMsgForm]");
            if (forms != null && forms.size() > 0) {
                Element form = forms.get(0);
                String tranFlag = form.select("input[name=tranFlag]").get(0).val();
                result.put("tranFlag", tranFlag);
            }
        }
        return result;
    }

    /**
     * 查询交易明细的请求正文（分账户）
     * @param acctMsgHtml
     * @param params
     * @return
     */
    public List<String> getTransflowBody(String acctMsgHtml, Map<String, String> params) {
        List<String> bodys = new ArrayList<>();
        Document acctMsgDoc = Jsoup.parse(acctMsgHtml);
        String body = "";
        if (params != null && params.size() > 0) {
            String sessionId = params.get("sessionId");
            String dse_parentContextName = params.get("dse_parentContextName");
            String operationName = params.get("operationName");
            String pageId = params.get("pageId");
            String tranCode = params.get("tranCode");
            String topNum = params.get("topNum");
            String tranFlag = params.get("tranFlag");
            String beginPos = params.get("beginPos");
            String showFlg = params.get("showFlg");
            String accountNo = params.get("accountNo");

            String sDate = getBeforeYear(1); //开始日期
            String eDate = getBeforeYear(0); //结束日期

            Elements elements = acctMsgDoc.getElementsByTag("acctMsg");
            Elements acctmsgEle = elements.get(0).getElementsByTag("acctmsg");
            for (int i = 1; i < acctmsgEle.size(); i++) {
                Element element = acctmsgEle.get(i);
                String subaccno = element.getElementsByTag("subaccno").get(0).text();
                String savetype = element.getElementsByTag("savetype").get(0).text();
                String currtype = element.getElementsByTag("currtype").get(0).text();

                body = "dse_sessionId=" + sessionId +
                        "&dse_parentContextName=" + dse_parentContextName +
                        "&dse_operationName=" + operationName +
                        "&dse_pageId=" + pageId +
                        "&tranCode=" + tranCode +
                        "&tranFlag=" + tranFlag +
                        "&topNum=" + topNum +
                        "&currType=" + currtype +
                        "&saveType=" + savetype +
                        "&accountNo=" + accountNo +
                        "&subAccount=" + subaccno +
                        "&sDate=" + sDate +
                        "&eDate=" + eDate +
                        "&beginPos=" + beginPos +
//                "&openOrgCode=08901" +
                        "&showFlg=" + showFlg;
                bodys.add(body);
            }
        }
        return bodys;
    }

    /**
     * 解析交易明细
     * @param html
     * @return
     */
    public List<BeijingBankDebitCardTransFlow> transflowDetailParser(TaskBank taskBank, String html)  {
        List<BeijingBankDebitCardTransFlow> result = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        String acctNo = doc.select("td:contains(网银账户)+td").first().text();
        String subAccNo = doc.select("td:contains(分账号)+td").first().text();
        String bankName = doc.select("td:contains(开户行)+td").first().text();
        System.out.println(acctNo+"====="+subAccNo+"========" +bankName);

        Element table = doc.select("tr[class=form_th]").parents().get(0);
        System.out.println("parents=====" + table.html());
        Elements trs = table.select("tr");
        if (trs != null && trs.size() > 1) {
            for (int i = 1; i < trs.size(); i++ ) {
                BeijingBankDebitCardTransFlow transFlow = new BeijingBankDebitCardTransFlow();
                Elements td = trs.get(i).select("td");
                transFlow.setTaskid(taskBank.getTaskid());
                transFlow.setAcctNo(acctNo);
                transFlow.setPointsAccount(subAccNo);
                transFlow.setBankName(bankName);
                transFlow.setNum(td.get(0).text());
                transFlow.setTransTime(td.get(1).text());
                transFlow.setSummary(td.get(2).text());
                transFlow.setTransType(td.get(3).text());
                transFlow.setTransAmount(td.get(4).text());
                transFlow.setBalance(td.get(5).text());
                transFlow.setTransPlace(td.get(6).text());
                result.add(transFlow);
            }
        }
        return result;
    }

    //读取xls数据
    public static List<BeijingBankDebitCardTransFlow> readxls(String xlsFilePath, String taskid, String subAccount,
                                                              String acctNo, String openOrgBank) throws Exception{
        File file = new File(xlsFilePath);
        List<BeijingBankDebitCardTransFlow> transFlows = new ArrayList<BeijingBankDebitCardTransFlow>();
        InputStream is = new FileInputStream(file);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        // 获取每一个工作薄
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 获取当前工作薄的每一行
            for (int rowNum = 4; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                System.out.println("===========================");
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    //创建一个BeijingBankDebitCardTransFlow对象
                    BeijingBankDebitCardTransFlow transFlow = new BeijingBankDebitCardTransFlow();
                    short lastCellNum = hssfRow.getLastCellNum();
                    System.out.println("lastCellNum:"+lastCellNum);
                    if (lastCellNum==7) { //6列
                        transFlow.setTaskid(taskid);
                        transFlow.setAcctNo(acctNo);
                        transFlow.setPointsAccount(subAccount); //分账户
                        transFlow.setBankName(openOrgBank);
                        transFlow.setNum(getValue(hssfRow.getCell(0)));
                        transFlow.setTransTime(getValue(hssfRow.getCell(1)));
                        transFlow.setSummary(getValue(hssfRow.getCell(2)));
                        transFlow.setTransType(getValue(hssfRow.getCell(3)));
                        transFlow.setTransAmount(getValue(hssfRow.getCell(4)));
                        transFlow.setBalance(getValue(hssfRow.getCell(5)));
                        transFlow.setTransPlace(getValue(hssfRow.getCell(6)));
                        transFlows.add(transFlow);
                    }
                }
            }
        }
        return transFlows;
    }

    // 转换数据格式
    private static  String getValue(Cell cell) {
        Object cellValue = "";
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = cell.getNumericCellValue();
                break;
            case BLANK:
                cellValue = "";
                break;
            default:
                break;
        }
        return String.valueOf(cellValue);
    }

    /**
     * 获取下一页的请求参数
     * @param html
     * @return
     */
    public String getNextTransflowBody(String html, int beginPos) {
        Document document = Jsoup.parse(html);
        //请求参数
        String nextBody = "";
        Elements forms = document.select("form[name=form2]");
        if (forms != null && forms.size() > 0) {
            Element form = forms.get(0);
            String sessionId = form.select("input[name=dse_sessionId]").get(0).val();
            String dse_parentContextName = form.select("input[name=dse_parentContextName]").get(0).val();
            String operationName = "queryHistoryDetailOp";
            String pageId = form.select("input[name=dse_pageId]").get(0).val();
            String dse_processorState = form.select("input[name=dse_processorState]").get(0).val();
            String dse_processorId = form.select("input[name=dse_processorId]").get(0).val();
            String tranCode = form.select("input[name=tranCode]").get(0).val();
            String topNum = form.select("input[name=topNum]").get(0).val();
            String tranFlag = form.select("input[name=tranFlag]").get(0).val();
            String currType = form.select("input[name=currType]").get(0).val();
            String saveType = form.select("input[name=saveType]").get(0).val();
            String openOrgCode = form.select("input[name=openOrgCode]").get(0).val();
            String subAccount = form.select("input[name=subAccount]").get(0).val();
            String accountNo = form.select("input[name=accountNo]").get(0).val();
            String sDate = form.select("input[name=sDate]").get(0).val();
            String eDate = form.select("input[name=eDate]").get(0).val();

            nextBody = "dse_sessionId=" + sessionId +
                    "&dse_parentContextName=" + dse_parentContextName +
                    "&dse_operationName=" + operationName +
                    "&dse_pageId=" + pageId +
                    "&dse_processorState=" + dse_processorState +
                    "&dse_processorId=" + dse_processorId +
                    "&tranCode=" + tranCode +
                    "&tranFlag=" + tranFlag +
                    "&topNum=" + topNum +
                    "&currType=" + currType +
                    "&saveType=" + saveType +
                    "&accountNo=" + accountNo +
                    "&sDate=" + sDate +
                    "&eDate=" + eDate +
                    "&beginPos=" + beginPos +
                    "&subAccount=" + subAccount +
                    "&openOrgCode=" + openOrgCode;
        }
        return nextBody;
    }

    //获取下载交易明细xls文件的请求参数
    public String getDownloadBody(String html, String sessionId) {
        Document document = Jsoup.parse(html);
        //请求参数
        String body = "";
        Elements forms = document.select("form[name=downLoadForm]");
        if (forms != null && forms.size() > 0) {
            Element form = forms.get(0);
//            String sessionId = form.select("input[name=dse_sessionId]").get(0).val();
            String dse_parentContextName = form.select("input[name=dse_parentContextName]").get(0).val();
            String operationName = "queryHistoryDetailOp";
            String pageId = form.select("input[name=dse_pageId]").get(0).val();
            String dse_processorState = form.select("input[name=dse_processorState]").get(0).val();
            String dse_processorId = form.select("input[name=dse_processorId]").get(0).val();
            String tranCode = form.select("input[name=tranCode]").get(0).val();
            String openOrgBank = form.select("input[name=openOrgBank]").get(0).val();
            String accountNo = form.select("input[name=accountNo]").get(0).val();
            String showFlg = form.select("input[name=showFlg]").get(0).val();

            body = "dse_sessionId=" + sessionId +
                    "&dse_parentContextName=" + dse_parentContextName +
                    "&dse_operationName=" + operationName +
                    "&dse_pageId=" + pageId +
                    "&dse_processorState=" + dse_processorState +
                    "&dse_processorId=" + dse_processorId +
                    "&accountNo=" + accountNo + //卡号
                    "&openOrgBank=" + openOrgBank + //开户行
                    "&tranCode=" + tranCode +
                    "&showFlg=" + showFlg ;
        }

        return body;
    }
    /**
     * 获取交易明细分页的总页数
     * @param html
     * @return
     */
    public int getPageCount(String html) {
        Document document = Jsoup.parse(html);
        int pageCount = 0;
        //总页数
        Elements fonts = document.select("font[class=pageText]");
        if (fonts != null && fonts.size() > 0) {
            String pageText = fonts.get(0).text();
            System.out.println("**********pageText=" + pageText);
            String count = subStringTxt(pageText, "/共", "页】");
//            String count = pageText.substring(pageText.indexOf("/共")+ "/共".length(), pageText.lastIndexOf("页】"));
            pageCount = Integer.parseInt(count);
        }
        return pageCount;
    }

    /*public static String subStringTxt(String originalStr,String strStart,String strEnd){
        String subResult = originalStr.substring(originalStr.lastIndexOf(strStart)+strStart.length(),originalStr.indexOf(strEnd));
        return subResult;
    }*/
    public static String subStringTxt(String text, String start, String end){
        String s = text.substring(text.lastIndexOf(start) + start.length());
        String result = s.substring(0, s.indexOf(end));
        return result;
    }

    //从当日期开始往前推n年
    public static String getBeforeYear(int n){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -n);
        String beforeYear = f.format(c.getTime());
        return beforeYear;
    }
}
