package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.CookieJson;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

public class test2 {

	public static void main(String[] args) throws Exception {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://wapah.189.cn/ua/loginout.shtml";
		HtmlPage page = getHtml(url, webClient);
		String html = page.getWebResponse().getContentAsString();
		HtmlTextInput account = (HtmlTextInput)page.getFirstByXPath("//input[@id='userphone']");//帐号
		//HtmlPasswordInput password =(HtmlPasswordInput) page.getFirstByXPath("//input[@class='password']");//密码
		
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='gcode']");//验证码图片
		HtmlTextInput loginCodeRand =(HtmlTextInput) page.getFirstByXPath("//input[@id='randomCode']");//输入验证码
		String imageName = "111.jpg";
		File file = new File("F:\\img\\" + imageName); 
		image.saveAs(file);
		
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
