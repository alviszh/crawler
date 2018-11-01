package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestParser {
	
	public static void main(String[] args) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://login.189.cn/login";
		HtmlPage html = getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='txtAccount']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='txtPassword']");
		HtmlElement button = (HtmlElement) html.getFirstByXPath("//a[@id='loginbtn']");
		username.setText("18004413282");
		passwordInput.setText("122333");

//		HtmlPage htmlpage = button.click();
//		System.out.println(htmlpage.asXml());
		Page htmlPage2 = getHtml("http://www.189.cn/dqmh/my189/initMy189home.do?fastcode=00710597", webClient);
		Thread.sleep(2000);
		
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/bill/cumulationInfoQueryFra.parser?requestFlag=asynchronism&shijian="), HttpMethod.GET);
		Page htmlPage3 = webClient.getPage(webRequest);
		

//		savefile("E:\\crawler\\telecomJilin\\taocan.txt", htmlPage3.getWebResponse().getContentAsString());
//		getShenfen(webClient);
		
		/*String result = java.net.URLEncoder.encode("王焕", "UTF-8");
		
		System.out.println(result);*/
		
	}
	

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	
	//通过身份验证1和身份验证2
	public static Page getShenfen(WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL("http://jl.189.cn/service/bill/toDetailBillFra.parser?fastcode=00710602&cityCode=jl"), HttpMethod.GET);
		HtmlPage yanzhengPage1 = webClient.getPage(webRequest);
		Thread.sleep(3000);
		HtmlTextInput idNum = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='certCode']");
		HtmlTextInput name = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='cust_name']");
		HtmlTextInput validateInput = (HtmlTextInput) yanzhengPage1.getFirstByXPath("//input[@id='vCode2']");
		HtmlElement submitbt = (HtmlElement)yanzhengPage1.getFirstByXPath("//a[@class='btn-1']");
		HtmlImage validateImage = yanzhengPage1.getFirstByXPath("//img[@id='vImgCode2']");
		
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("E:\\Codeimg\\"+imageName);
		validateImage.saveAs(file);
		//输入图片验证码1
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		idNum.setText("220102198908196169");
		name.setText("苗金玲");
		validateInput.setText(input);
		HtmlPage yanzhengPage2 = submitbt.click();
		if(null != yanzhengPage2){
//				Thread.sleep(3000);
//				HtmlTextInput validateInput2 = (HtmlTextInput) yanzhengPage2.getFirstByXPath("//input[@id='vCode']");
//				HtmlTextInput msgCodeInput = (HtmlTextInput) yanzhengPage2.getFirstByXPath("//input[@id='sRandomCode']");
//				HtmlElement submitbt2 = (HtmlElement)yanzhengPage2.getFirstByXPath("//a[@class='btn-1']");
			HtmlImage validateImage2 = yanzhengPage2.getFirstByXPath("//img[@id='vImgCode']");
			
			//输入图片验证码2
			validateImage2.saveAs(file);
			@SuppressWarnings("resource")
			Scanner scanner2 = new Scanner(System.in);
			String input2 = scanner2.next();
//				validateInput2.setText(input2);
			
			//输入短信验证码
//			@SuppressWarnings("resource")
//			Scanner scanner3 = new Scanner(System.in);
//			String input3 = scanner3.next();
			
//				msgCodeInput.setText(input3);
//				HtmlPage page = submitbt2.click();
			
			WebRequest webRequest2 = new WebRequest(new URL("http://jl.189.cn/service/bill/doDetailBillFra.parser"), HttpMethod.POST);
			webRequest2.setRequestParameters(new ArrayList<NameValuePair>());
			webRequest2.getRequestParameters().add(new NameValuePair("sRandomCode", "376223"));
			webRequest2.getRequestParameters().add(new NameValuePair("randCode", input2));
			Page shenfen = webClient.getPage(webRequest2);
			
			savefile("E:\\crawler\\telecomJilin\\shenfenInfo.txt", shenfen.getWebResponse().getContentAsString());
			
			WebRequest webRequestt = new WebRequest(new URL("http://jl.189.cn/service/bill/billDetailQueryFra.parser"), HttpMethod.POST);
			webRequestt.setRequestParameters(new ArrayList<NameValuePair>());
			webRequestt.getRequestParameters().add(new NameValuePair("billDetailValidate", "true"));
			webRequestt.getRequestParameters().add(new NameValuePair("billDetailType", "5"));
			webRequestt.getRequestParameters().add(new NameValuePair("startTime", "2017-03-01"));
			webRequestt.getRequestParameters().add(new NameValuePair("endTime", "2017-08-31"));
			webRequestt.getRequestParameters().add(new NameValuePair("pagingInfo.currentPage", "0"));
			webRequestt.getRequestParameters().add(new NameValuePair("contactID", "201709011545367398068"));
			HtmlPage searchPage = webClient.getPage(webRequestt);
	        
			savefile("E:\\crawler\\telecomJilin\\duanxin.txt", searchPage.getWebResponse().getContentAsString());
			
			
			return searchPage;
		}
		return null;
	}
	
	
	//获取客户资料页面信息
	public static void parserUserInfo() throws Exception {
		String txt = null;
		try {
            String encoding="Unicode";
            File file = new File("E:\\crawler\\telecomJilin\\userInfo.txt");
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
		Document document = Jsoup.parse(txt);
		String name = getNextLabelByKeyword(document, "th", "客户名称");
		String type = getNextLabelByKeyword(document, "th", "客户类型");
		String idType = getNextLabelByKeyword(document, "th", "证件类型");
		String idNum = getNextLabelByKeyword(document, "th", "证件号码");
		String address = document.select("#custAddress_new").val();
		String contactTel = document.select("#custPhone_new").val();
		
		System.out.println(name+"-/-"+type+"-/-"+idType+"-/-"+idNum+"-/-"+address+"-/-"+contactTel);
		
		
//		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSSSSSS");
//      Calendar c = Calendar.getInstance();
//      
//      c.setTime(new Date());
//      c.add(Calendar.MONTH, -0);
//      Date m5 = c.getTime();
//      String mon5 = format.format(m5);
//      System.out.println("过去六个月："+mon5);
	}
	/**
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 * @param document
	 * @param keyword
	 * @return
	 */
	public static String getNextLabelByKeyword(Document document, String tag, String keyword){
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
	public static String getMsgByKeyword(Document document, String keyword) {
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
