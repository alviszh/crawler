package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

public class test1 {

	private TaskMobileRepository taskMobileRepository;
	
	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "https://gd.189.cn/common/newLogin/newLogin.htm";
		HtmlPage page = getHtml(url, webClient);
		HtmlTextInput account = (HtmlTextInput)page.getFirstByXPath("//input[@id='account']");
		HtmlSelect mobilePassword = (HtmlSelect)page.getFirstByXPath("//select[@id='mobilePassword']");
		HtmlPasswordInput password =(HtmlPasswordInput) page.getFirstByXPath("//input[@id='password']");
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='loginCodeImage']");
		HtmlTextInput loginCodeRand =(HtmlTextInput) page.getFirstByXPath("//input[@id='loginCodeRand']");
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName); 
		image.saveAs(file);

		HtmlElement button = (HtmlElement)page.getFirstByXPath("//input[@id='t_login']");


		account.setText("18126726741");
		password.setText("123457");
		mobilePassword.setTextContent("客户密码");
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		loginCodeRand.setText(inputValue);
		HtmlPage htmlpage = button.click();
		String html1 = htmlpage.getWebResponse().getContentAsString();
		System.out.println("=============================="+html1);
		if (htmlpage.asXml().indexOf("登录失败") != -1) {
			System.out.println("=======失败==============");
		}
		/*String u = "http://www.189.cn/dqmh/my189/initMy189.do?cityCode=gd";
		WebRequest webRequest = new WebRequest(new URL(u),HttpMethod.POST);
		HtmlPage p = webClient.getPage(webRequest);
		int statusCode = page.getWebResponse().getStatusCode();
		WebClient webClientMy189home = null ;
		if(200==statusCode){
			webClientMy189home = page.getWebClient();
		}*/
		//String html = getStatusCode(webClientMy189home);

		/*System.out.println();
		
		
		List<List<String>> list = readExcelWithoutTitle("C:\\Users\\dongty\\Desktop\\tel.xls");
		for (List<String> list2 : list) {
			for (String string : list2) {
				System.out.println(string);
			}
		}
*/
		
		
		//doc.getElementById("cust_sexs_id").val()
		
		
	}

	
	public static String getTaoCan(){
		
		return null;
	}
	
	
	public static String getStatusCode(WebClient webClient) throws Exception{

		//String url = "http://gd.189.cn/service/home/query/jifen.html?in_cmpid=khzy-wdsy-wdjj-jfcx";
		//		List<NameValuePair> paramsList = new ArrayList<>();
		//
		//		paramsList.add(new NameValuePair("a.c", "0"));
		//		paramsList.add(new NameValuePair("a.u", "user"));
		//		paramsList.add(new NameValuePair("a.p", "pass"));
		//		paramsList.add(new NameValuePair("a.s", "ECSS"));
		//		

//		Page page = gethtmlPost(webClient, null, url);
//		String html1 = page.getWebResponse().getContentAsString();
//		Set<Cookie> cookies = page.getEnclosingWindow().getWebClient().getCookieManager().getCookies();
//		for (Cookie cookie : cookies) {
//			System.out.println("========"+cookie.toString());
//		}
		
		
		
		String url9 = "http://gd.189.cn/J/J10133.j";
		List<NameValuePair> paramsList = new ArrayList<>();

		paramsList.add(new NameValuePair("a.c", "0"));
		paramsList.add(new NameValuePair("a.u", "user"));
		paramsList.add(new NameValuePair("a.p", "pass"));
		paramsList.add(new NameValuePair("a.s", "ECSS"));
		Page page2 = gethtmlPost(webClient, paramsList, url9);
		String html2 = page2.getWebResponse().getContentAsString();
//		
//		
//		String url2 = "http://gd.189.cn/common/getLoginUserNameJt.jsp?"; 
//		getHtml(url2, webClient);
//		String url3 = "http://gd.189.cn/common/getIsLogin.jsp?";
//		getHtml(url3, webClient);
//		String url4 = "http://gd.189.cn/common/getCustInfo.jsp?";
//		getHtml(url4, webClient);
//		String url5 = "http://gd.189.cn/common/getIsLogin.jsp";
//		getHtml(url5, webClient);
//		String url6 = "http://gd.189.cn/dis/portal/getSearchHtml.json";
//		getHtml(url6, webClient);
//		String url7 = "http://gd.189.cn/common/getIsLogin.jsp";
//		getHtml(url7, webClient);
//		String url8 = "http://gd.189.cn/mrsServer/mgr/mrs/common/getRecomend.js?_=1505817561373";
//		getHtml(url8, webClient);

		
		return html2;
	}

	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			webRequest.setCharset(Charset.forName("UTF-8"));
			Page searchPage = webClient.getPage(webRequest);
			if (searchPage == null) {
				return null;
			}
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	
	public static WebClient getInitMy189homeWebClient(MessageLogin messageLogin,TaskMobile taskMobile)throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = addcookie(webClient, taskMobile);
		//根据如下链接获取爬取需要的cookie(首先根据登录的cookie获取让中间链接成功获取)
		String url = "http://www.189.cn/dqmh/my189/initMy189.do?cityCode=gd";
		WebRequest webRequest = new WebRequest(new URL(url),HttpMethod.POST);
		HtmlPage page = webClient.getPage(webRequest);
		int statusCode = page.getWebResponse().getStatusCode();
		WebClient webClientMy189home = null ;
		if(200==statusCode){
			webClientMy189home = page.getWebClient();
		}
		return webClientMy189home;
	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(20000);

		webClient.getOptions().setTimeout(20000); // 15->60
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
	public static WebClient addcookie(WebClient webclient, TaskMobile taskMobile) {
		Type founderSetType = new TypeToken<HashSet<CookieJson>>() {
		}.getType();
		Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskMobile.getCookies());
		Iterator<Cookie> i = cookies.iterator();
		while (i.hasNext()) {
			webclient.getCookieManager().addCookie(i.next());
		}

		return webclient;
	}
	
	
	
	
	
	public static WebClient addCookie(WebClient webClient,String cookiepath,String domain) throws Exception{
		List<List<String>> lists =  readExcelWithoutTitle(cookiepath); 
		
		for(List<String> list:lists){
			String key = list.get(0);
			String value = list.get(1);
			System.out.println("domain---->"+domain);
			System.out.println("key---->"+key);
			System.out.println("value---->"+value); 
			webClient.getCookieManager().addCookie(new Cookie(domain, key, value)); 
		}
		
		return webClient;
	}
	
	public static List<List<String>> readExcelWithoutTitle(String filepath) throws Exception{
	    String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
	    InputStream is = null;
	    Workbook wb = null;
	    List<List<String>> sheetList = new ArrayList<List<String>>();//对应sheet页
	    try {
	        is = new FileInputStream(filepath);
	         
	        if (fileType.equals("xls")) {
	            wb = new HSSFWorkbook(is);
	        } else {
	            throw new Exception("读取的不是excel文件");
	        } 
	        int sheetSize = wb.getNumberOfSheets();
	        System.out.println("sheetSize-------"+sheetSize);
	        if(sheetSize>0){
	        	Sheet sheet = wb.getSheetAt(0);  
	            int rowSize = sheet.getLastRowNum() + 1; 
	           // System.out.println("rowSize-------"+rowSize); 
	            for (int j = 0; j < rowSize; j++) {//遍历行
	                Row row = sheet.getRow(j);
	                if (row == null) {//略过空行
	                    continue;
	                }
	                int cellSize = row.getLastCellNum();//行中有多少个单元格，也就是有多少列
	                //System.out.println("cellSize-------"+cellSize);
	                List<String> rowList = new ArrayList<String>();//对应一个数据行
	                for (int k = 0; k < cellSize; k++) {
	                    Cell cell = row.getCell(k);
	                    String value = null;
	                    if (cell != null) {
	                        value = cell.toString();
	                    }
	                   // System.out.println("value-------"+value);
	                    rowList.add(value);
	                }
	                sheetList.add(rowList);
	            } 
		       
		        }
	    } catch (FileNotFoundException e) {
	        throw e;
	    } finally {
	        if (wb != null) {
	            wb.close();
	        }
	        if (is != null) {
	            is.close();
	        }
	    }
	    
	    return sheetList;
	}

	
}
