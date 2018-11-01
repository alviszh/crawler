package app;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.module.htmlunit.WebCrawler;

public class ShanXiTestLogin {
	
	public static void main(String[] args) {
		
		//城镇职工养老保险
		String yanglao = "http://117.36.52.39/sxlssLogin.jsp";
		//城镇职工失业保险
		String shiye = "http://117.36.52.39/sylssLogin.jsp";
		//城镇职工医疗保险
		String yiliao = "http://117.36.52.39/zgylLogin.jsp";
		//城镇职工工伤保险
		String gongshang = "http://117.36.52.39/bjgsLogin.jsp";
		
		getHtml(gongshang);
	}
	
	public static void getHtml(String url){
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			HtmlPage searchPage = (HtmlPage) getHtml(url,webClient);
			//获取身份证号标签
			HtmlTextInput idnum = searchPage.getFirstByXPath("//input[@name='uname']");
			//获取姓名标签
			HtmlTextInput aac003 = searchPage.getFirstByXPath("//input[@name='aac003']");
			//获取输入验证码框的标签
			HtmlTextInput psinput = searchPage.getFirstByXPath("//input[@maxlength='4']");
			//选择城市的select标签
//			HtmlSelect select = searchPage.getFirstByXPath("//select[@name='ylslFlag']");
//			select.getOption(0).setSelected(true);  
			//验证码
			HtmlTextInput checkCode = searchPage.getFirstByXPath("//input[@id='checkCode']");
			String code = checkCode.getValueAttribute();
			System.out.println(".............code  :"+code);
			//获取点击登录按钮
			HtmlInput Icon2 = (HtmlInput)searchPage.getFirstByXPath("//input[@id='Icon2']");
			
			idnum.setText("610321199309203627");
			aac003.setText("吴永芳");
			psinput.setText(code);
			
			HtmlPage loginPage = Icon2.click();
			System.out.println("登录后跳转的页面  ====================="+loginPage.asXml());
			
			
			String personUrl = "http://117.36.52.39/jsp/bjgs/bjgsPersonInfoQuery.jsp";
			HtmlPage personPage = (HtmlPage) getHtml(personUrl,webClient);
			System.out.println("个人信息页面  ====================="+personPage.asXml());
			
			parserHtml(personPage.asXml());

			
			String mingxiUrl = "http://117.36.52.39/bjgsPaymentQuery.do";
			HtmlPage mingxiPage = (HtmlPage) getHtml(mingxiUrl,webClient);
			System.out.println("历年缴费页面  ====================="+mingxiPage.asXml());
			parserMingxi(mingxiPage.asXml());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void parserMingxi(String asXml) {
		Document doc = Jsoup.parse(asXml);
		Element tb1 = doc.getElementById("tb1");
		if(null != tb1){
			Elements trs = tb1.select("tr");
			if(null != trs && trs.size()>0){
				for(Element tr : trs){
					if(tr.select("td").size()>4){
						String year = tr.child(0).text();
						String payType = tr.child(1).text();
						String paymentBase = tr.child(2).text();
						String organizationPayScale = tr.child(3).text();
						String accountDate = tr.child(4).text();
						
						
						System.out.println("缴费年月："+year);
						System.out.println("缴费类型："+payType);
						System.out.println("缴费基数："+paymentBase	);
						System.out.println("单位缴费比例："+organizationPayScale);
						System.out.println("到帐日期："+accountDate);
						System.out.println("*******************************************");
						
					}
				}
			}
		}
	}

	public static Page getHtml(String url,WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}
	
	
	public static void parserHtml(String html){
		Document doc = Jsoup.parse(html);
		
		Element uname = doc.select("td:contains(姓名)").first().nextElementSibling();
		System.out.println("姓名："+uname.text());
		
		Element personStatus = doc.select("td:contains(人员状态)").first().nextElementSibling();
		System.out.println("人员状态："+personStatus.text());
		
		Element payType = doc.select("td:contains(缴费状态)").first().nextElementSibling();
		System.out.println("缴费状态："+payType.text());
		
		Element participationDate = doc.select("td:contains(参保日期)").first().nextElementSibling();
		System.out.println("参保日期："+participationDate.text());
		
		Element organization = doc.select("td:contains(经办机构名称)").first().nextElementSibling();
		System.out.println("经办机构名称："+organization.text());
		
		
		
	}


}
