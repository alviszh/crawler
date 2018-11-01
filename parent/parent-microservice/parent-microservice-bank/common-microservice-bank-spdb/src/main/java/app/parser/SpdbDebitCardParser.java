package app.parser;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardAccountInfo;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardRegular;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardTransFlow;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardUserInfo;
import com.module.htmlunit.WebCrawler;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class SpdbDebitCardParser {

    @Autowired
    private TracerLog tracerLog;

    public SpdbDebitCardUserInfo userInfoParser(TaskBank taskBank, String html) {
        Document doc = Jsoup.parse(html);
        String masterId = doc.select("input[name=MasterId]").get(0).val();//客户号
        String name = doc.select("input[name=Name]").get(0).val();//客户姓名
        String address = doc.select("input#Address").val();//地址
        String zipCode = doc.select("input#ZipCode").val();//邮政编码
        String telephoneNo = doc.select("input#TelephoneNo").val();//联系手机号
        String fixedTel = doc.select("input#FixedTel").val();//固定电话
        String rsrvNo = doc.select("input#RsrvNo").val();//备用手机号

        SpdbDebitCardUserInfo spdbDebitCardUserInfo = new SpdbDebitCardUserInfo();
        spdbDebitCardUserInfo.setTaskid(taskBank.getTaskid());
        spdbDebitCardUserInfo.setCustomerNo(masterId);
        spdbDebitCardUserInfo.setCustomerName(name);
        spdbDebitCardUserInfo.setAddress(address);
        spdbDebitCardUserInfo.setPostalCode(zipCode);
        spdbDebitCardUserInfo.setTelephoneNo(telephoneNo);
        spdbDebitCardUserInfo.setFixedTel(fixedTel);
        spdbDebitCardUserInfo.setRsrvNo(rsrvNo);
        return spdbDebitCardUserInfo;
    }
    public static  List<SpdbDebitCardTransFlow> transFlowParser(TaskBank taskBank, String json, String acctNo){
        List<SpdbDebitCardTransFlow> transFlowList = new ArrayList<>();
        JSONObject jsonObject =  JSONObject.fromObject(json);
        JSONArray loopResults = jsonObject.getJSONArray("LoopResult");

        for (int i = 0; i < loopResults.size(); i++) {
            JSONObject loopResult = loopResults.getJSONObject(i);
            SpdbDebitCardTransFlow transFlow = new SpdbDebitCardTransFlow();
            transFlow.setTaskid(taskBank.getTaskid());
            transFlow.setNo(loopResult.getString("SeqNo"));
            transFlow.setTransTime(loopResult.getString("OccursDate"));
            transFlow.setSummary(loopResult.getString("TxType"));
            //获取正负号字段值（区分存入、取出）
            String crDtIndicator = loopResult.getString("CrDtIndicator").trim();
            String depositAmount = "";
            String takeAmount = "";
            if (crDtIndicator.equals("1")) {
                depositAmount = loopResult.getString("TxAmount").trim();
            } else if (crDtIndicator.equals("0")) {
                takeAmount = loopResult.getString("TxAmount").trim();
            }
            transFlow.setDepositAmount(depositAmount);
            transFlow.setTakeAmount(takeAmount);
            transFlow.setBalance(loopResult.getString("Balance").trim());
            transFlow.setAcctNo(acctNo);
            transFlowList.add(transFlow);
        }
        return transFlowList;
    }
    /*public List<SpdbDebitCardTransFlow> transFlowParser(TaskBank taskBank, String html, String acctNo) {
        List<SpdbDebitCardTransFlow> transFlowList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table[class=table]");
        if (tables != null) {
            Element table = tables.get(0);
            Elements trs = table.select("tr");
            if (trs != null) {
                for (int i = 2; i < trs.size(); i++) {
                    SpdbDebitCardTransFlow transFlow = new SpdbDebitCardTransFlow();
                    transFlow.setTaskid(taskBank.getTaskid());
                    Elements tds=trs.get(i).select("td");
                    transFlow.setNo(tds.get(0).text());
                    transFlow.setTransTime(tds.get(1).text());
                    transFlow.setSummary(tds.get(2).text());
                    transFlow.setDepositAmount(tds.get(3).text());
                    transFlow.setTakeAmount(tds.get(4).text());
                    transFlow.setBalance(tds.get(5).text());
                    transFlow.setAcctNo(acctNo);
                    transFlowList.add(transFlow);
                }
            }
        }
        return transFlowList;
    }*/

    public List<SpdbDebitCardRegular> regularParser(TaskBank taskBank, String html, String acctNo) {
        List<SpdbDebitCardRegular> regularList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table[class=table]");
        if (tables != null && tables.size() > 0) {
            Element table = tables.get(0);
            Elements trs = table.select("tr");
            if (trs != null && trs.size() > 0) {
                for (int i = 2; i < trs.size(); i++) {
                    Elements tds=trs.get(i).select("td");
                    if (tds.size() == 8) {
                        SpdbDebitCardRegular regular = new SpdbDebitCardRegular();
                        regular.setTaskid(taskBank.getTaskid());
                        regular.setSavingsType(tds.get(1).select("a").text());
                        regular.setSn(tds.get(2).text());
                        regular.setCurrency(tds.get(3).text());
                        regular.setCurrencyType(tds.get(4).text());
                        regular.setBalance(tds.get(5).text());
                        regular.setDueDate(tds.get(6).text());
                        regular.setAccountStatus(tds.get(7).text());
                        regular.setAcctNo(acctNo);
                        regular.setTaskid(taskBank.getTaskid());
                        regularList.add(regular);
                    }
                }
            }

        }

        return regularList;
    }

    /**
     * 获取卡号集合
     * @param html
     * @return
     */
    public List<String> getAcctNosParser(String html) {
        Document doc = Jsoup.parse(html);
        List<String> acctNoList = null;
        /*Elements options = doc.select("select#AcctNo").select("option");
        for (Element option : options) {
            String acctNo = option.val();
            System.out.println("====acctNo="+acctNo);
            acctNoList = new ArrayList<>();
            acctNoList.add(acctNo);
        }*/
        Elements elements = doc.select("div[class=swiper-slide]");
        for (Element element : elements) {
            String acctNo = element.attr("acctNo");
            if (acctNo != null && !"".equals(acctNo)) {
                System.out.println("====acctNo=" + acctNo);
                acctNoList = new ArrayList<>();
                acctNoList.add(acctNo);
            }
        }
        return acctNoList;
    }

    public List<SpdbDebitCardAccountInfo> accountInfoParser(TaskBank taskBank, String html, String acctNo) {
        List<SpdbDebitCardAccountInfo> accountInfoList = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements tables = doc.select("table[class=table]");
        if (tables != null && tables.size() > 0) {
            Element table = tables.get(0);//活期余额
            Elements trs = table.select("tr");
            if (trs != null && trs.size() > 0) {
                for (int i = 2; i < trs.size(); i++) {
                    String html1 = trs.html();
//                    System.out.println("==============html1="+html1);
                    Elements tds=trs.get(i).select("td");
                    if (tds.size() == 6) {
                        SpdbDebitCardAccountInfo accountInfo = new SpdbDebitCardAccountInfo();
                        accountInfo.setVoucherKind(tds.get(0).select("div").text());
//                        String href = tds.get(1).select("a").attr("href");
//                        System.out.println("++++++href=" + href);
                        /*String acctNo = "";
                        if (href.contains("'")) {
                            acctNo = href.split("'")[1];
                        }*/
                        accountInfo.setCurrency(tds.get(1).select("div").text());
                        accountInfo.setCurrencyType(tds.get(2).select("div").text());
                        accountInfo.setCurrentBalance(tds.get(3).select("div").text());
                        accountInfo.setCanUseBalance(tds.get(4).select("div").text());
                        accountInfo.setAccountStatus(tds.get(5).select("div").text());

                        accountInfo.setAcctNo(acctNo);
                        accountInfo.setTaskid(taskBank.getTaskid());
                        accountInfoList.add(accountInfo);
                    }
                }
            }

        }

        return accountInfoList;
    }

    public SpdbDebitCardRegular otherRegularParser(String html) {
        Document doc = Jsoup.parse(html);
        String userName = doc.select("td:contains(户名)+td").first().text();
        String userNo = doc.select("td:contains(客户号)+td").first().text();
        String accountType = doc.select("td:contains(账号类型)+td").first().text();
        String saveTerm = doc.select("td:contains(存期)+td").first().text();
        String rate = doc.select("td:contains(利率)+td").first().text();
        String startData = doc.select("td:contains(起息日期)+td").first().text();

        SpdbDebitCardRegular spdbDebitCardRegular = new SpdbDebitCardRegular();
        spdbDebitCardRegular.setUserName(userName);
        spdbDebitCardRegular.setUserNo(userNo);
        spdbDebitCardRegular.setAccountType(accountType);
        spdbDebitCardRegular.setSaveTerm(saveTerm);
        spdbDebitCardRegular.setRate(rate);
        spdbDebitCardRegular.setStartData(startData);
        return spdbDebitCardRegular;
    }

    public WebParam getTransflow(WebClient webClient, TaskBank taskBank, String acctNo) throws Exception{
        tracerLog.output("crawler.bank.parser.getTransflow.taskid", taskBank.getTaskid());
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String endDate = f.format(new Date());
        String beginDate = getBeforeYear(2);
        WebParam webParam = new WebParam();
        String url = "https://ebank.spdb.com.cn/nbper/DownloadHistory.do?AcctNo=" + acctNo +
                "&BeginDate=" + beginDate +
                "&EndDate=" + endDate +
                "&CurrencyType=0" + //0 钞, 1 汇
                "&AcctKind=0001" + //账户种类: 0001普通活期
                "&CurrencyNo=01"; //01 人民币
        tracerLog.output("crawler.bank.parser.getTransflow.url", url);
        webParam.setUrl(url);

        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        Page page = webClient.getPage(webRequest);
        InputStream contentAsStream = page.getWebResponse().getContentAsStream();
        //获取CSV文件的存储路径
        String csvFilePath = getXlsFilePath(taskBank.getTaskid(), acctNo);
        tracerLog.output("crawler.bank.parser.getTransflow.download.path", csvFilePath);
        webParam.setHtml(csvFilePath);
        //将流水下载保存为xls文件
        saveXls(contentAsStream, csvFilePath);
        //读取CSV文件中的流水信息
        List<SpdbDebitCardTransFlow> transFlows = readxls(csvFilePath, taskBank.getTaskid(), acctNo);
        tracerLog.output("crawler.bank.parser.getTransflow.list", transFlows.toString());
        if(null != transFlows && transFlows.size() > 0){
            webParam.setList(transFlows);
        }
        return webParam;
    }

    //获取存储xls文件的路径
    public String getXlsFilePath(String taskid, String accNo) {
        //获取存放流水xls文件的路径
        String path = System.getProperty("user.dir")+"\\file\\";
        tracerLog.output("crawler.bank.parser.getXlsFilePath.path", path);
        File parentDirFile = new File(path);
        parentDirFile.setReadable(true);
        parentDirFile.setWritable(true);
        if (!parentDirFile.exists()) {
            parentDirFile.mkdirs();
        }

        String xlsPath = path+""+taskid+"_"+ accNo +".xls";
        tracerLog.output("crawler.bank.parser.getXlsFilePath.xlsPath", xlsPath);
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

    public static List<SpdbDebitCardTransFlow> readxls(String csvFilePath, String taskid, String acctNo) throws Exception{
        File file = new File(csvFilePath);
        List<SpdbDebitCardTransFlow> transFlows = new ArrayList<SpdbDebitCardTransFlow>();
        InputStream is = new FileInputStream(file);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        // 获取每一个工作薄
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 获取当前工作薄的每一行
            for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                System.out.println("===========================");
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    //创建一个SpdbDebitCardTransFlow对象
                    SpdbDebitCardTransFlow transFlow = new SpdbDebitCardTransFlow();
                    short lastCellNum = hssfRow.getLastCellNum();
                    System.out.println("lastCellNum:"+lastCellNum);
                    if (lastCellNum==6) { //6列
                        transFlow.setTaskid(taskid);
                        transFlow.setNo(getValue(hssfRow.getCell(0)));
                        transFlow.setTransTime(getValue(hssfRow.getCell(1)));
                        transFlow.setSummary(getValue(hssfRow.getCell(2)));
                        String depositAmount = getValue(hssfRow.getCell(3));
                        String takeAmount = getValue(hssfRow.getCell(4));
                        transFlow.setDepositAmount(depositAmount);
                        transFlow.setTakeAmount(takeAmount);
                        transFlow.setBalance(getValue(hssfRow.getCell(5)));
                        transFlow.setAcctNo(acctNo);
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
    //从当日期开始往前推n年
    public static String getBeforeYear(int n){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -n);
        String beforeYear = f.format(c.getTime());
        return beforeYear;
    }
}
