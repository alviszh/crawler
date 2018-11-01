

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class panzhihualogin {

	public static void main(String[] args) throws Exception {
		
		String url = "http://www.scpzh.lss.gov.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage page = (HtmlPage) getHtml(url, webClient);
		HtmlTextInput username = (HtmlTextInput)  page.getFirstByXPath("//input[@id='SSNumber']");
		HtmlPasswordInput password = (HtmlPasswordInput)page.getFirstByXPath("//input[@id='SPWD']");
		HtmlTextInput randomnum = (HtmlTextInput) page.getFirstByXPath("//input[@id='Validate']");
		
		HtmlImage image = (HtmlImage)page.getFirstByXPath("//img[@id='_Validate']");//验证码
		String imageName = "111.jpg"; 
		File file = new File("F:\\img\\" + imageName);
		image.saveAs(file);
		String inputValue = JOptionPane.showInputDialog("请输入验证码……");
		
		username.setText("513425199112153216");//513425199112153216
		password.setText("panzhihua888");//panzhihua888
		randomnum.setText(inputValue);
		
		HtmlImageInput login_btn = (HtmlImageInput) page.getElementById("button");
		Page page3 = login_btn.click();
		String asString2 = page3.getWebResponse().getContentAsString();
		System.out.println(asString2);
		if(asString2.indexOf("社保信息-攀枝花市人力资源和社会保障局")!=-1){
			System.out.println("登录成功");
			
			/**
			 * 个人信息
			 */
			/*String urlu = "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp";
			Page page2 = getHtml(urlu, webClient);
			String asString = page2.getWebResponse().getContentAsString();
			System.out.println(asString);
			
			Elements byTag = Jsoup.parse(asString).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//姓名
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//个人编号
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//单位编号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//单位名称
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//身份证号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//出生日期
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//参加工作时间
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//联系电话
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//户口所在地
			
			System.out.println("姓名："+text+"\r个人编号:"+text2+"\r单位编号:"+text3+"\r单位名称:"
					+text4+"\r身份证号:"+text5+"\r出生日期:"+text6+"\r参加工作时间:"+text7+"\r联系电话:"+text8+"\r户口所在地:"+text9);
			*/
			/**
			 * 养老
			 */
		/*	String url3 = "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp?it=2";
			Page page4 = getHtml(url3, webClient);
			String contentAsString = page4.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			
			Elements byTag = Jsoup.parse(contentAsString).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//单位编号
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//单位名称
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//首次参保时间
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//建账时间
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text10 = byTag.get(10).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text11 = byTag.get(11).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			
			String aurl= "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp?it=3";
			Page page2 = getHtml(aurl, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Elements byTag2 = Jsoup.parse(contentAsString2).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text12 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//最末缴费年月
			String text13 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//累计缴费月数
			String text14 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//个人账户金额
			*/
			/***
			 * 医疗
			 */
			/*String ylurl = "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp?it=4";
			Page page2 = getHtml(ylurl, webClient);
			String contentAsString = page2.getWebResponse().getContentAsString();
			
			Elements byTag = Jsoup.parse(contentAsString).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//单位编号
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//单位名称
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//本年缴费月数
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text10 = byTag.get(10).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			String text11 = byTag.get(11).getElementsByTag("td").get(1).text().trim();//异地登记信息
			
			String zurl = "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp?it=5";
			Page page4 = getHtml(zurl, webClient);
			String contentAsString2 = page4.getWebResponse().getContentAsString();
			Elements byTag2 = Jsoup.parse(contentAsString2).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text12 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//本年缴费月数
			String text13 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//最末上账期号
			String text14 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//当前账户余额
*/			
			/**
			 * 失业
			 */
			/*String syurl = "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp?it=6";
			Page page2 = getHtml(syurl, webClient);
			String contentAsString = page2.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			
			Elements byTag = Jsoup.parse(contentAsString).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//农民工标识
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			String text9 = byTag.get(9).getElementsByTag("td").get(1).text().trim();//本次失业享受失业保险待遇月数
			String text10 = byTag.get(10).getElementsByTag("td").get(1).text().trim();//失业保险金剩余月数
*/			
			/**
			 * 工伤
			 */
		/*	String gurl = "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp?it=7";
			Page page2 = getHtml(gurl, webClient);
			String contentAsString = page2.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			Elements byTag = Jsoup.parse(contentAsString).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//缴费类型
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text8 = byTag.get(8).getElementsByTag("td").get(1).text().trim();//当月缴费基数
			
*/		
			/**
			 * 生育
			 */
			String gurl = "http://www.scpzh.lss.gov.cn/SocialSecurityInfo_iframe.jsp?it=8";
			Page page2 = getHtml(gurl, webClient);
			String contentAsString = page2.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			Elements byTag = Jsoup.parse(contentAsString).getElementsByTag("table").get(0).getElementsByTag("tr");
			String text = byTag.get(1).getElementsByTag("td").get(1).text().trim();//个人编号
			String text2 = byTag.get(2).getElementsByTag("td").get(1).text().trim();//姓名
			String text3 = byTag.get(3).getElementsByTag("td").get(1).text().trim();//身份证号
			String text4 = byTag.get(4).getElementsByTag("td").get(1).text().trim();//初次缴费期号
			String text5 = byTag.get(5).getElementsByTag("td").get(1).text().trim();//最末缴费期号
			String text6 = byTag.get(6).getElementsByTag("td").get(1).text().trim();//单位当月缴费标志
			String text7 = byTag.get(7).getElementsByTag("td").get(1).text().trim();//当月缴费基数
		}
		
		
	}
	
	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
