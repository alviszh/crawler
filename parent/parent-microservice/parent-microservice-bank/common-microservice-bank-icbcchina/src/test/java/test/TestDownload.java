package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.csvreader.CsvReader;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.bank.icbcchina.IcbcChinaDebitCardTimeDeposit;
import com.module.htmlunit.WebCrawler;

public class TestDownload {

	public static void main(String[] args) throws Exception{
		/*WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId=BYDVHMBFHHIQEVDLJUBEFMFBDBIHIWALAYEUGWGU&YETYPE=0&cardNum=6212260200137433062&acctNum=0200336301009067883&begDate=2017-08-16&endDate=2017-11-16&Tran_flag=2&queryType=4&dse_operationName=per_AccountQueryHisdetailOp";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
//		String string = page.getWebResponse().getContentAsString("gb2312");
//		savefile("C:\\Users\\Administrator\\Desktop\\123.txt", string);
		InputStream contentAsStream = page.getWebResponse().getContentAsStream();
		save1(contentAsStream);
//		WebResponse response = page.getWebResponse();
//		details(webClient);
//		getAccNumList();
		
//		readCSV();*/
		readCSV();
//		getTrans2("EUDIFCDRFUJICUFVGNFUJEFNBMGXDKEZFFFCDDGQ");
//		getTimeDeposit("IHDBIPBZBUEPALGAJOATDIDXESCIFYAKDDHBESIN");
	}
	
	public static void getTrans2(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/includes/mybank.jsp?dse_sessionId="+sessionId;
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String txt = page.getWebResponse().getContentAsString();
		
		int i = txt.indexOf("var cardlistdata =");
		int j = txt.indexOf(";", i);
		System.out.println(i+"|"+j);
		String substring = txt.substring(i+19, j);
		System.out.println(substring);
		
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(substring); // 创建JsonObject对象
		JsonArray accountCardList = object.get("accountCardList").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String cardNum = account.get("cardNum").getAsString();
			String acctNo0 = account.get("acctNo0").getAsString();
			System.out.println("-----------------------------");
			System.out.println(cardNum);
			System.out.println("..............");
			System.out.println(acctNo0);
			
			gettrans2(sessionId, cardNum, acctNo0);
		}
	}
	
	public static void gettrans2(String sessionId, String cardNum, String acctNo0) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/ICBCINBSReqServlet?dse_sessionId="+sessionId+"&YETYPE=0&cardNum="+cardNum+"&acctNum="+acctNo0+"&begDate=2017-08-18&endDate=2017-11-17&Tran_flag=2&queryType=4&dse_operationName=per_AccountQueryHisdetailOp";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		InputStream contentAsStream = page.getWebResponse().getContentAsStream();
		save1(contentAsStream);
	}
	
	
	//获取含有AccNumList的页面
	public static void details(WebClient webClient) throws Exception{
		String url = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/includes/mybank.jsp?dse_sessionId=DUJVEDJLCRHMDSCRBIBXBEEXASJQDHBTHEAYDUIE";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		savefile("C:\\Users\\Administrator\\Desktop\\icbcDetails.txt", page.getWebResponse().getContentAsString());
	}
	
	
	
	
	public static void getAccNumList() throws Exception{
		String txt = null;
		try {
            String encoding="utf-8";
            File file = new File("C:\\Users\\Administrator\\Desktop\\icbcDetails.txt");
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    txt += lineTxt;
                }
//                System.out.println(txt);
                read.close();
            }else{
            	System.out.println("找不到指定的文件");
            }
		}catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		
		int i = txt.indexOf("var cardlistdata =");
		int j = txt.indexOf(";", i);
		System.out.println(i+"|"+j);
		String substring = txt.substring(i+19, j);
		System.out.println(substring);
		
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(substring); // 创建JsonObject对象
		JsonArray accountCardList = object.get("accountCardList").getAsJsonArray();
		for (JsonElement acc : accountCardList) {
			JsonObject account = acc.getAsJsonObject();
			String cardNum = account.get("cardNum").getAsString();
			String acctNo0 = account.get("acctNo0").getAsString();
			System.out.println("-----------------------------");
			System.out.println(cardNum);
			System.out.println("..............");
			System.out.println(acctNo0);
			
		}
	}
	
	public static void getTimeDeposit(String sessionId) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/icbc/newperbank/perbank3/includes/atomService_control.jsp?serviceId=PBL20102102&transData=&dse_sessionId="+sessionId+"&requestChannel=302";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String txt = page.getWebResponse().getContentAsString();
		savefile("C:\\Users\\Administrator\\Desktop\\1712011107.txt",txt);
		Document document = Jsoup.parse(txt);
		Elements eles = document.select(".lstlink");
		for (Element element : eles) {
			String str = element.toString();
			String substring = str.substring(str.indexOf("(")+2, str.indexOf(")")-1);
			System.out.println("参数数据-----》"+substring);
			String[] split = substring.split("','");
			for (String string : split) {
				System.out.println("--->"+string);
			}
			getTimeDeposit2(sessionId, split);
		}
	}
	
	
	public static void getTimeDeposit2(String sessionId,String[] split) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://mybank.icbc.com.cn/servlet/AsynGetDataServlet?SessionId="+sessionId+"&cardNum="+split[0]+"&acctCode="+split[1]+"&acctNum="+split[2]+"&acctType="+split[3]+"&Begin_pos=0&dingFlag=1&NormalOrBooking=0&tranCode=A00008&more=0";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page page = webClient.getPage(webRequest);
		String txt = page.getWebResponse().getContentAsString();
		savefile("C:\\Users\\Administrator\\Desktop\\1712011123.txt",txt);
		System.out.println(txt);
	}
	
	//将String保存到本地
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
	
	public static void save1(InputStream inputStream) throws Exception{

		OutputStream  outputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\icbcdownload.csv");

		int bytesWritten = 0;
		int byteCount = 0;

		byte[] bytes = new byte[1024];

		while ((byteCount = inputStream.read(bytes)) != -1)
		{
//			outputStream.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
			outputStream.write(bytes, 0, byteCount);
//			bytesWritten += byteCount;
		}
		inputStream.close();
		outputStream.close();
    }
	
	public static void readCSV() {
	    try {
	        // 用来保存数据
	        ArrayList<String[]> csvFileList = new ArrayList<String[]>();
	        // 定义一个CSV路径
	        String csvFilePath = "C:\\Users\\Administrator\\Desktop\\25b19dcb-9863-42cc-a229-2dda598c3ce3_6215581502003119096.csv";
	        File file = new File(csvFilePath);
	        boolean b = file.exists();
	        System.out.println(b);
	        // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
	        CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("utf-8"));
	        // 跳过表头 如果需要表头的话，这句可以忽略
	        reader.readHeaders();
	        // 逐行读入除表头的数据
	        while (reader.readRecord()) {
	            System.out.println(reader.getRawRecord()); 
	            csvFileList.add(reader.getValues()); 
	        }
	        reader.close();
	        
	        // 遍历读取的CSV文件
	        for (int row = 3; row < csvFileList.size()-2; row++) {
	            // 取得第row行第0列的数据
	        	int length = csvFileList.get(row).length;
	        	System.out.println("================================");
	        	for (int i = 0; i < length; i++) {
					String cell = csvFileList.get(row)[i].trim();
					System.out.println("------------>"+cell);
				}
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
