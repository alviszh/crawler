package testNanchang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
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
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;
import com.module.htmlunit.WebCrawler;

public class TestLogin {

	public static void main(String[] args) {
		parser();
	}
	
	
	public static void loginNanchang() throws Exception{
		String loginUrl = "http://www.ncgjj.com.cn:8081/wt-web/login";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		
	}
	
	
	public static void login(int i){
		String loginUrl = "http://118.190.12.70/search/AccumulationFund.aspx";
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
			HtmlPage loginPage1 = webClient.getPage(webRequest);
			HtmlImage vc1 = loginPage1.getFirstByXPath("//img[@style='vc']");
			if(null != vc1){
				HtmlPage loginPage = (HtmlPage) vc1.click();
				if(loginPage.asXml().contains("请同时输入账号和身份证号并点击")){
					HtmlImage vc = loginPage.getFirstByXPath("//img[@id='vc']");
					HtmlImage loginbtn = loginPage.getFirstByXPath("//img[@style=' cursor:hand; padding-top:5px;']");
					HtmlTextInput t1 = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='t1']");
					HtmlTextInput t2 = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='t2']");
					HtmlTextInput t3 = (HtmlTextInput) loginPage.getFirstByXPath("//input[@id='t3']");
					
					File file = new File("E:\\Codeimg\\zibo.jpg");
					vc.saveAs(file);
					
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(System.in);
					String input = scanner.next();
					
					t1.setText("402101596");
					t2.setText("37120219930228001X");
					t3.setText(input);
					HtmlPage loginedPage = (HtmlPage) loginbtn.click();
					
					String resPath = "E:\\crawler\\housingfund\\nanchang\\zibo.txt";
					savefile(resPath,loginedPage.asXml());
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void parser(){
		String txt = null;
		try {
            String encoding="utf-8";
            File file = new File("E:\\crawler\\housingfund\\nanchang\\zibo.txt");
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
		Elements divs = document.select(".tbarea");
		if(null != divs && divs.size() > 2){
			Element div = divs.get(1);
			String text = div.text()+" ";
			System.out.println(text);
			int i = text.indexOf("缴至年月： ")+6;
			int o = text.indexOf("姓名： ")+4;
			int p = text.indexOf("余额： ")+4;
			
			String payDate = text.substring(i, text.indexOf(" ", i));
			String name = text.substring(o, text.indexOf(" ", o));
			String balance = text.substring(p, text.indexOf(" ", p));
			System.out.println("+"+payDate+"+");
			System.out.println("-"+name+"-");
			System.out.println("*"+balance+"*");
			/*String[] strings = text.split(" ");
			
			for (String string : strings) {
				System.out.println(string+"+++++++++++");
			}*/
		}
	}
	
	public static void savefile(String filePath, String fileTxt) throws Exception{
		File fp=new File(filePath);
        PrintWriter pfp= new PrintWriter(fp);
        pfp.print(fileTxt);
        pfp.close();
	}
}
