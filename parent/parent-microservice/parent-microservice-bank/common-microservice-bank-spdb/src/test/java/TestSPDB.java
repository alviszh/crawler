import com.csvreader.CsvReader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.bank.spdb.SpdbDebitCardTransFlow;
import com.module.htmlunit.WebCrawler;
import com.module.jna.winio.User32;
import com.module.jna.winio.VKMapping;
import com.module.jna.winio.VirtualKeyBoard;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.xvolks.jnative.exceptions.NativeException;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by zmy on 2017/10/31.
 */
public class TestSPDB {
    static String driverPath = "G:\\IE\\32\\IEDriverServer.exe";

    public static void main(String[] args) throws Exception {
        /*DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
        //ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);


        System.out.println("launching IE browser");
        System.setProperty("webdriver.ie.driver", driverPath);

        WebDriver driver = new InternetExplorerDriver(ieCapabilities);

        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        String baseUrl = "https://ebank.spdb.com.cn/nbper/popInnerLogin.do?Reserve=";
        driver.get(baseUrl);
//        driver.get(baseUrl);
        String currentUrl = driver.getCurrentUrl();
        System.out.println("currentUrl--11--" + currentUrl);
        Thread.sleep(1000L);
        //driver.findElement(By.id("username")).sendKeys("6222030200002485674");
        VirtualKeyBoard.KeyPressEx("420106198410028419", 50);//
        //Thread.sleep(1000L);
        InputTab(); //
        //Thread.sleep(1000L);
        VirtualKeyBoard.KeyPressEx("369258", 50);//

        //输入回车登录
        InputEnter();//

        Thread.sleep(2000);
        String currentUrl2 = driver.getCurrentUrl();
        System.out.println("currentUrl--22--" + currentUrl2);
        String htmlsource2 = driver.getPageSource();
        System.out.println("htmlsource--22--" + htmlsource2);
        savefile("G:\\spdblogined.txt", driver.getPageSource());

        Set<Cookie> cookies =  driver.manage().getCookies();
        WebClient webClient = WebCrawler.getInstance().getWebClient();//
        for(Cookie cookie:cookies){
            System.out.println(cookie.getName()+"---------------"+cookie.getValue());
            webClient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie("ebank.spdb.com.cn",cookie.getName(),cookie.getValue()));
        }*/

        //定期存款
       /* String preQueryHistoryUrl = "https://ebank.spdb.com.cn/nbper/QueryNoticeWeekDetail.do?AcctNo=6217920600042951&SeqNo=0002";
        WebRequest preQHWebRequest = new WebRequest(new URL(preQueryHistoryUrl), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(false);
        Page preQHPage = webClient.getPage(preQHWebRequest);
        int preQHStatusCode = preQHPage.getWebResponse().getStatusCode();
        if (preQHStatusCode == 200) {
            String preQHHtml = preQHPage.getWebResponse().getContentAsString();
            System.out.println("=================preQHHtml=" + preQHHtml);

            //class="swiper-slide"
            Document doc = Jsoup.parse(preQHHtml);
            String text = doc.select("td:contains(户名)+td").first().text();
            System.out.println("acctNo:"+text);
        }*/



        //获取卡号
       /* String preQueryHistoryUrl = "https://ebank.spdb.com.cn/nbper/QueryHistoryLeft.do";
        WebRequest preQHWebRequest = new WebRequest(new URL(preQueryHistoryUrl), HttpMethod.GET);
        webClient.getOptions().setJavaScriptEnabled(false);
        Page preQHPage = webClient.getPage(preQHWebRequest);
        int preQHStatusCode = preQHPage.getWebResponse().getStatusCode();
        if (preQHStatusCode == 200) {
            String preQHHtml = preQHPage.getWebResponse().getContentAsString();
            System.out.println("=================preQHHtml=" + preQHHtml);

            //class="swiper-slide"
            Document doc = Jsoup.parse(preQHHtml);
            String acctNo = doc.select("div[class=swiper-slide]").get(0).attr("acctNo");//卡号
            System.out.println("acctNo:"+acctNo);
        }*/


        //获取交易记录
//        String queryHistoryUrl = "https://ebank.spdb.com.cn/nbper/QueryHistory.json";
//        WebRequest webRequest = new WebRequest(new URL(queryHistoryUrl), HttpMethod.POST);
//        webRequest.setAdditionalHeader("Accept","application/json, */*; q=0.01");
//        webRequest.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8");
//        webRequest.setAdditionalHeader("Content-Type","application/json;charset=UTF-8");
//        webRequest.setAdditionalHeader("Host","ebank.spdb.com.cn");
//        webRequest.setAdditionalHeader("Referer","https://ebank.spdb.com.cn/nbper/QueryHistoryCard.do?AcctNo=_encry_7C8D66B3D23F3C9CD7A9EE3265B3D18702BC4D0DF994C4C272853896BE4F89B3E1AC72AD898A3305E93F344BDB59764E3492F00DB8E471556419E952EEE63299F101799243A41D075AB54187A3C28C43135551D3C3BFFD70A0CCA9FD313C5414&acctType=1&overdraftStatus=2&wisdomCardStatus=2&SelectFlag=3&selectedMenu=menu1_1_4");

        /*List<String> acctNoList =  new ArrayList<>();
        acctNoList.add("6217920600042951");
        System.out.println("卡号集合：acctNoList=" + acctNoList);
        String requestBody = "";
        for (String acctNo : acctNoList) {

            requestBody = getRequestBody(acctNo, 1);
            System.out.println("请求参数是：" + requestBody);

            webRequest.setRequestBody(requestBody);
            Page page = webClient.getPage(webRequest);
            int statusCode = page.getWebResponse().getStatusCode();
            String json = page.getWebResponse().getContentAsString();
            System.out.println("获取交易明细：statusCode=" + statusCode);
            System.out.println("json==" + json);


            List<SpdbDebitCardTransFlow> transFlowList = new ArrayList<>();
            JSONObject jsonObject =  JSONObject.fromObject(json);
            JSONArray loopResults = jsonObject.getJSONArray("LoopResult");
            String  errmsg = jsonObject.getString("errmsg");
            System.out.println(loopResults.size());
            System.out.println(errmsg);
        }*/

        //获取交易记录（下载）
        /*String url = "https://ebank.spdb.com.cn/nbper/DownloadHistory.do?AcctNo=6217920600042951&BeginDate=20160601" +
                "&EndDate=20180601&CurrencyType=0&AcctKind=0001&CurrencyNo=01";
        WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
        Page page = webClient.getPage(webRequest);
        InputStream contentAsStream = page.getWebResponse().getContentAsStream();*/
        //获取CSV文件的存储路径
        String csvFilePath = getCsvFilePath("xls1", "62179206000429512");
        //将流水下载保存为CSV文件
//        saveCsv(contentAsStream, csvFilePath);
        //读取CSV文件中的流水信息
        List<SpdbDebitCardTransFlow> transFlows = readxls(csvFilePath, "spdb", "6217920600042951");
        for (SpdbDebitCardTransFlow flow : transFlows) {
            System.out.println("======================="+flow.getNo());
            System.out.println("============"+flow);
        }
        System.out.println(transFlows.size());
    }


    //获取存储CSV文件的路径
    public static String getCsvFilePath(String taskid, String cardNum) {
        //获取存放流水csv文件的路径
        String path = System.getProperty("user.dir")+"\\file\\";
        File parentDirFile = new File(path);
        parentDirFile.setReadable(true);
        parentDirFile.setWritable(true);
        if (!parentDirFile.exists()) {
            parentDirFile.mkdirs();
        }

        String csvPath = path+""+taskid+"_"+ cardNum +".xls";
        System.out.println("csvPath:" + csvPath);
        return csvPath;
    }

    public static void saveCsv(InputStream inputStream, String path) throws Exception{
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


    public static List<SpdbDebitCardTransFlow> readCSV(String csvFilePath, String taskid, String acctNo) throws Exception{
        File file = new File(csvFilePath);
        List<SpdbDebitCardTransFlow> transFlows = new ArrayList<SpdbDebitCardTransFlow>();
        // 判断文件是否存在
        if(file.exists()){
            // 用来保存数据
            ArrayList<String[]> csvFileList = new ArrayList<String[]>();
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("gb2312"));
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
                System.out.println(reader.getRawRecord());
                csvFileList.add(reader.getValues());
            }
            reader.close();
            //判断是否为空数据
            if(csvFileList.size() > 5){
                // 遍历读取的CSV文件
                for (int row = 2; row < csvFileList.size()-2; row++) {
                    //创建一个SpdbDebitCardTransFlow对象
                    SpdbDebitCardTransFlow transFlow = new SpdbDebitCardTransFlow();
                    List<String> txt = new ArrayList<String>();
                    //获取第row行的长度
                    int length = csvFileList.get(row).length;
                    // 取得第row行第i列的数据
                    for (int i = 0; i < length; i++) {
                        String cell = csvFileList.get(row)[i].trim();
                        txt.add(cell);
                    }
                    transFlow.setTaskid(taskid);
                    transFlow.setNo(txt.get(0));
                    transFlow.setTransTime(txt.get(1));
                    transFlow.setSummary(txt.get(2));
                    //获取正负号字段值（区分存入、取出）
                    String crDtIndicator = txt.get(3).trim();
                    String depositAmount = "";
                    String takeAmount = "";
                    if (crDtIndicator.equals("1")) {
                        depositAmount = txt.get(4);
                    } else if (crDtIndicator.equals("0")) {
                        takeAmount = txt.get(4);
                    }
                    transFlow.setDepositAmount(depositAmount);
                    transFlow.setTakeAmount(takeAmount);
                    transFlow.setBalance(txt.get(5));
                    transFlow.setAcctNo(acctNo);
                    transFlows.add(transFlow);
                }
            }
        }
        return transFlows;
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
                    /*for (Iterator<Cell> cit = (Iterator<Cell>)hssfRow.iterator(); cit.hasNext(); ) {
                        Cell cell = cit.next();
                        System.out.println("==="+getValue(cell));
                    }*/
//                    System.out.println("rowNum::"+rowNum1);

                   /* HSSFCell one = hssfRow.getCell(0);
                    //读取第一列数据
                    HSSFCell two = hssfRow.getCell(1);
                    //读取第二列数据
                    HSSFCell three = hssfRow.getCell(2);
                    //读取第三列数据
                    HSSFCell d = hssfRow.getCell(3);
                    HSSFCell e = hssfRow.getCell(4);
                    HSSFCell f = hssfRow.getCell(5);
                    //需要转换数据的话直接调用getValue获取字符串
                    System.out.println(getValue(one));
                    System.out.println(getValue(two));
                    System.out.println(getValue(three));
                    System.out.println(getValue(d));
                    System.out.println(getValue(e));
                    System.out.println(getValue(f));*/
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
        /*if (cell.getCellTypeEnum().equals(cell.CELL_TYPE_BOOLEAN)) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }*/
    }

    public static void InputEnter() throws IllegalAccessException, NativeException, Exception {
        Thread.sleep(1000L);
        if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
            VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Enter"));
        }
    }

    public static void InputTab() throws IllegalAccessException, NativeException, Exception {
        Thread.sleep(1000L);
        if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
            VirtualKeyBoard.KeyPress(VKMapping.toScanCode("Tab"));
        }
    }

    public static void Input(String[] accountNum) throws IllegalAccessException, NativeException, Exception {
        Thread.sleep(1000L);
        for (String s : accountNum) {
            if (User32.GetWindowText(User32.GetForegroundWindow()).contains("Internet Explorer")) {
                VirtualKeyBoard.KeyPress(VKMapping.toScanCode(s));
            }
            Thread.sleep(500L);
        }
    }

    //将String保存到本地
    public static void savefile(String filePath, String fileTxt) throws Exception {
        File fp = new File(filePath);
        PrintWriter pfp = new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
    }

    public static String getRequestBody(String acctNo, int beginNum){
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String toDate = f.format(new Date());
        String beginDate = getBeforeYear(2);
        String endDate = toDate;

        /*String requestBody
        = "ClickMenu=QueryHistory" +
                "&SelectFlag=3" +  //1是在账户详情页面跳转2是交易明细延期查询3是本页面发的交易
                "&QueryTrsType=0" + //0全部交易明细，1海外交易明细
                "&AcctKind=0001" +   //账户种类: 0001普通活期
                "&FastSelect=5" +   //5 按日期查询
                "&HuoqiShow=1" +    //1 活期显示
                "&AcctNo=" + acctNo + //卡号
                "&CurrencyNo=01" +  //01 人民币
                "&CurrencyType=0" + //0 钞, 1 汇
                "&LoanType=000000" + //贷款类型：000000 全部，112801 融资周转贷款，999999 经营支用贷款
                "&LoanStatus=2" +   //贷款状态：2 全部，0 未结清，1 结清
                "&BeginDate=" + beginDate +
                "&EndDate=" + endDate +
                "&BeginNumber=" + beginNum; //起始行
//                "&QueryNumber=" + queryNum; //每页行数（固定20行）*/

        String requestBody = "{\"ClickMenu\":\"QueryHistory\"" +
                ",\"SelectFlag\":\"3\"" + //1是在账户详情页面跳转2是交易明细延期查询3是本页面发的交易
                ",\"AcctNo\":\"6217920600042951\"" + //卡号
                ",\"BeginDate\":\"20160206\",\"EndDate\":\"20180205\"" + //查询日期
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
                ",\"BeginNumber\":\"31\"}";
        return requestBody;
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

