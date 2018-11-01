package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.module.htmlunit.WebCrawler;

import app.service.ChaoJiYingOcrService;
import test.entity.Item;

public class Test3 {
	
	public static void main(String[] args) throws Exception{
		String txt = null;
		try {
            String encoding="Unicode";
            File file = new File("E:\\crawler\\telecomshandong\\henan.txt");
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
		int s = txt.indexOf("doQuery('");
		String substring = txt.substring(s,txt.indexOf(";", s));
		int i = substring.indexOf(",'");
		String substring2 = substring.substring(i+2, substring.indexOf(",'", i+1)-1);
		System.out.println(substring2);
		
		/*Document doc = Jsoup.parse(txt);
		String planName = doc.select("[style=color:#000;]").text();
		String starLevel = doc.select("#starLevel").text();
		
		System.out.println(planName+"-+-+-+-"+starLevel);*/
		
	}
	
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){
		Elements es = document.select(tag+":contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
	}
	
	/**
	 * @Des 通过关键字获取具体的标签内容
	 * @param html
	 * @param keyword
	 * @return
	 */
	public String getMsgByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains("+keyword+")");
		if(null != es && es.size()>0){
			Element element = es.first();
			return element.text();
		}
		return null;
	}
	
	//将String保存到本地
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
	
}
