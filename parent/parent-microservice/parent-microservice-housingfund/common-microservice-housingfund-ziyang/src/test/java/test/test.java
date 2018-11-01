package test;


import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;


public class test extends HousingBasicService{
	
	public static  void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
        String url ="http://sbj.jiangmen.gov.cn/WsSever.aspx?mode=log";
		
		WebRequest webRequest1 = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage page = webClient.getPage(webRequest1);

		HtmlImage valiCodeImg = page.getFirstByXPath("//*[@id='vimg2']");
		System.err.println(valiCodeImg);
		Scanner input = new Scanner(System.in);
		 System.out.print("请输入：");
		 String valicodeStr = input.next();
		System.out.println("===================="+valicodeStr);
		String url1 = "http://sbj.jiangmen.gov.cn/WsSever.aspx?mode=log";
		WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "sbj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Origin", "http://sbj.jiangmen.gov.cn");
		webRequest.setAdditionalHeader("Referer", "http://sbj.jiangmen.gov.cn/WsSever.aspx?mode=log");
		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		String requestBody = "__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwULLTIwMzEzNjU4MTEPZBYCAgMPZBYEAhcPDxYCHghJbWFnZVVybAUaU2hvd0NoZWNrQ29kZS5hc3B4P3R5cGU9enhkZAIhD2QWBAINDw8WAh8ABRtTaG93Q2hlY2tDb2RlLmFzcHg%2FdHlwZT1yZWdkZAIdDw8WAh8ABRxTaG93Q2hlY2tDb2RlLmFzcHg%2FdHlwZT1jb21wZGRkfSzaZZa0bn1xW3jv0kwcHKHeD9k%3D&__VIEWSTATEGENERATOR=3FA4FD3C&__EVENTVALIDATION=%2FwEWFwKfovXYBgL2g8m2DAL0z6nBDAKNk%2FjDAQK9%2B7qcDgL5h8GfAwKv4uKxAgLCi5reAwLb%2B8OvCQLii9jdAwL6hrQbAoylvswLAoDq25wPAoPY6rgGAsbMrrkJAteGsBwCp7zKtAkCp7zOtAkCz4KjqwYClPnEnAgC1KLwnQYC7KW1tQgC4sm40QMSOGORLOpULbDOAjWrJof4R%2BzMfw%3D%3D"
				+ "&txtPSIDCD=&txtPWD=&txtLast6=&txtCommentCode3=&IDNo=NDQwNzgyMTk4NjA0MjAwNjQ2&BtnSummit=%E6%8F%90+%E4%BA%A4&PsCxKl=MTQwMzA4&sbcard=MzU4Njk5&txtCommentCode="+valicodeStr+"&OrganizationNo=&UtCxKl=&DJZNo1=&DJZNo2=&bze=&txtCommentCode2=&hidPscode=440782198604200646&hidCodeCount=1&hidIdcard=440782198604200646";
		webRequest.setRequestBody(requestBody);
		Page searchPage = webClient.getPage(webRequest);
	    System.err.println(searchPage.getWebResponse().getContentAsString());
	}
	
	

}
