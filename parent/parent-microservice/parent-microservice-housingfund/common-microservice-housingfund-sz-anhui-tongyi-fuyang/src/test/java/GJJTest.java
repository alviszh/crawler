
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

import app.bean.PayRootBean;
import app.htmlparse.HousingFuYangParse;

public class GJJTest {

	private static String baseUrl = "https://sso.ahzwfw.gov.cn/uccp-server/login";

	private static WebClient webClient = null;

	public static void main(String[] args) throws Exception {
		webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage htmlPage = (HtmlPage) getHtml(baseUrl, webClient);
		// System.out.println("=================================================");
		// System.out.println(htmlPage.asXml());

		Document doc = Jsoup.parse(htmlPage.asXml());

		String lt = doc.select("[name=lt]").attr("value");
		String execution = doc.select("[name=execution]").attr("value");
		String _eventId = doc.select("[name=_eventId]").attr("value");
		String loginType = doc.select("[name=loginType]").attr("value");
		String credentialType = doc.select("[name=credentialType]").attr("value");

		String platform = doc.select("[name=platform]").attr("value");
		// String ukeyType = doc.select("[name=ukeyType]").attr("value");
		// String ahcaukey = doc.select("[name=ahcaukey]").attr("value");
		// String ahcasign = doc.select("[name=ahcasign]").attr("value");
		String userType = doc.select("[name=userType]").attr("value");

		String url = "https://sso.ahzwfw.gov.cn/uccp-server/login";
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("lt", lt));
		paramsList.add(new NameValuePair("execution", execution));
		paramsList.add(new NameValuePair("_eventId", _eventId));
		paramsList.add(new NameValuePair("platform", platform));
		paramsList.add(new NameValuePair("loginType", loginType));
		paramsList.add(new NameValuePair("credentialType", credentialType));
		paramsList.add(new NameValuePair("userType", userType));
		paramsList.add(new NameValuePair("username", "15856785648"));
		paramsList.add(new NameValuePair("password", "nk123456"));
		
//		paramsList.add(new NameValuePair("password", "y15856785648"));
		// paramsList.add(new NameValuePair("username", "zhaojuan0623"));
		// paramsList.add(new NameValuePair("password", "y15856785647"));
		paramsList.add(new NameValuePair("random", ""));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);

		
		System.out.println(searchPage.getWebResponse().getContentAsString());
		if (searchPage.getWebResponse().getContentAsString().indexOf("用户名或密码不正确") != -1) {
			System.out.println("=========false=============");
			
			
		} else {
//			System.out.println("=========true=============");
//
//			Thread.sleep(5000);
//			url = "http://wx.gjj.fy.cn/hfmis_wt/personal/";
//			
//			webClient.getPage(webRequest);
// 
//			String persionid = getPersionId(webClient);
//			
//			if(persionid == null){
//				
//			}else{
//				Page userinfoPage  =  getPay(webClient, persionid);
//				
//				PayRootBean bean = HousingFuYangParse.payParse(userinfoPage.getWebResponse().getContentAsString(), null);
//
//				
//				System.out.println("=========================="+bean.getDataset().getRows().size());
//				
////				Page userinfoPage  =  getPesionInfo(webClient, persionid);
////				
////				HousingFundSzAnHuiTongYiFuYangRootBean bean = HousingFuYangParse.basicusereParse(userinfoPage.getWebResponse().getContentAsString(), null);
////				
////				System.out.println(bean.toString());
//			}
//			
//			
		}

	}

	public static Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

	public static String getPersionId(WebClient webClient) throws Exception {
		String url = "http://wx.gjj.fy.cn/hfmis_wt/personal/jbxx";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		// webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);

		//
//		System.out.println("=======================");
//		System.out.println(searchPage.getWebResponse().getContentAsString());
		Pattern p = Pattern.compile("[\\d]{9}"); // 得到字符串中的数字
		Matcher m = p.matcher(searchPage.getWebResponse().getContentAsString());
		if (m.find()) {
			System.out.println("========="+m.group());
			
			return m.group();
		}
		return null;
	}
	
	public static Page getPesionInfo(WebClient webClient,String persionid) throws Exception{
		String url = "http://wx.gjj.fy.cn/hfmis_wt/common/zhfw/invoke/020101";
		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("grzh", persionid.trim()));
		// paramsList.add(new NameValuePair("username", "zhaojuan0623"));
		// paramsList.add(new NameValuePair("password", "y15856785647"));
		paramsList.add(new NameValuePair("random", ""));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		
//		System.out.println("==============getPesionInfo================");
//		
//		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}
	
	public static Page getPay(WebClient webClient,String persionid) throws Exception{
		String url = "http://wx.gjj.fy.cn/hfmis_wt/common/zhfw/invoke/020102";
		
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("filterscount", "0"));
		paramsList.add(new NameValuePair("groupscount", "0"));
		paramsList.add(new NameValuePair("pagenum", "0"));
		paramsList.add(new NameValuePair("pagesize", "1000"));
		paramsList.add(new NameValuePair("recordstartindex", "0"));
		paramsList.add(new NameValuePair("recordendindex", "20"));
		paramsList.add(new NameValuePair("grzh", persionid.trim()));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		
		System.out.println("==============getPesionInfo================");
		
		System.out.println(searchPage.getWebResponse().getContentAsString());
		
		return searchPage;
	}

}
