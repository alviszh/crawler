package org.common.microservice.eureka.china.telecom;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.unicom.UnicomNoteResult;
import com.module.htmlunit.WebCrawler;

import app.bean.UnicomRoot;
import app.bean.UnicomUserResultRoot;

public class yidongtest {

	public static void test() {

		try {
			WebClient webClient = WebCrawler.getInstance().getWebClient();
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(true);

			String url = "https://login.10086.cn/login.html";
			HtmlPage html = getHtml(url, webClient);
			HtmlTextInput username = (HtmlTextInput) html.getFirstByXPath("//input[@id='p_name']");
			HtmlPasswordInput passwordInput = (HtmlPasswordInput) html.getFirstByXPath("//input[@id='p_pwd']");
			HtmlElement button = (HtmlElement) html.getFirstByXPath("//input[@id='submit_bt']");
			username.setText("15210072522");
			passwordInput.setText("001314");

			HtmlPage htmlpage = button.click();
			System.out.println(htmlpage.asXml());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public static void test2() throws IOException {
		String targetUrl = "https://login.10086.cn/login.html?channelID=12003&backUrl=http://shop.10086.cn/i/";
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest request = new WebRequest(new URL(targetUrl), HttpMethod.GET);
		// request.setAdditionalHeader('User-Agent', 'Mozilla/5.0 (Windows NT
		// 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)
		// Chrome/58.0.3029.110 Safari/537.36');
		// request.setAdditionalHeader('Referer',
		// 'http://shop.10086.cn/i/?f=home');
		// request.setAdditionalHeader('Accept',
		// 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8');
		// request.setAdditionalHeader('Accept-Encoding', 'gzip, deflate, sdch,
		// br');
		// request.setAdditionalHeader('Accept-Language', 'zh-CN,zh;q=0.8');
		// request.setAdditionalHeader('Cache-Control', 'max-age=0');
		// request.setAdditionalHeader('Connection', 'keep-alive');
		// request.setAdditionalHeader('Host', 'login.10086.cn');
		// request.setAdditionalHeader('If-Modified-Since', 'Tue, 06 Jun 2017
		// 14:52:29 GMT');
		// request.setAdditionalHeader('If-None-Match', '5936c1ad-44d9');
		// request.setAdditionalHeader('Upgrade-Insecure-Requests', '1');
		// request.setAdditionalHeader('Cookie',
		// 'FSSBBIl1UgzbN7N443T=1iuvi1ah4bhrG1kgn9CgKy3cRse6bTuC26y2DWyU.DwhYTyJZbugenIl40PBSqtufLiEWP84CuopgI36TO2m4s6lwAtHiS0Pw67QmLKaIVubD8pZUHNi90vZXwI1kkJg8EgSE_EU89jpAp4LuRyKuHSI18zkETzw7.J_5sGrnt9u1lG;
		// FSSBBIl1UgzbN7N443S=UYvHIkeVtMCjsiagaHQaqcJPqTZQnUdQNA3ao72sgI1WU1nRYbENDJ2yxO1.vDH6;
		// cmccssotoken=015fd62c93b2496ea0685a509e63f60e@.10086.cn;
		// userinfokey=%7b%22loginType%22%3a%2201%22%2c%22provinceName%22%3a%22100%22%2c%22pwdType%22%3a%2201%22%2c%22userName%22%3a%2215210072522%22%7d;
		// verifyCode=88899ac77e1bdfa802e9a67ed71f246c197309c1;
		// freelogin_userlogout=; loginName=15210072522; c=null;
		// jsessionid-echd-cpt-cmcc-jt=4277DFAAA3B953CB21703703277B639E;
		// WT_FPC=id=2c8a61a83d38e9a27111498546581774:lv=1498552386976:ss=1498552116343;
		// CmLocation=100|100; CmProvid=bj; CaptchaCode=JPEEwC');
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(true);
		HtmlPage login = webClient.getPage(request);
		// System.out.println(login.asXml());

		HtmlTextInput mobileInput = (HtmlTextInput) login.getFirstByXPath("//input[@id='p_name']");
		HtmlPasswordInput passwordInput = (HtmlPasswordInput) login.getFirstByXPath("//input[@id='p_pwd']");
		HtmlElement button = (HtmlElement) login.getFirstByXPath("//input[@id='submit_bt']");
		mobileInput.setText("15210072522");
		// login.setFocusedElement(mobileInput);
		passwordInput.setText("001314");
		HtmlPage index = button.click();

		System.out.println(index.asXml());

		/*
		 * HtmlAnchor randomButton = (HtmlAnchor
		 * )index.getFirstByXPath('//a[@id='getSMSPwd']'); HtmlPasswordInput
		 * randomPWDInput =
		 * (HtmlPasswordInput)index.getFirstByXPath('//input[@id='sms_pwd']');
		 * System.out.println(randomButton.asXml());
		 * System.out.println(randomPWDInput.asXml()); randomButton.click();
		 * 
		 * @SuppressWarnings('resource') Scanner scanner = new
		 * Scanner(System.in); String code = scanner.next();
		 * 
		 * randomPWDInput.setText(code); mobileInput.setText('15210072522');
		 * passwordInput.setText('001314'); System.out.println(index.asXml());
		 * HtmlPage aaa = button.click(); System.out.println(aaa.asXml());
		 */
	}

	public static void test4(){
		String json = "{'querynowdate':'2017年07月13日','mmsCount':1,'totalfee':'0.00','endDate':'2017-05-31','pageMap':{'result':[{'amount':'0.00','fee':'0.10','smsdate':'2017-05-30','smstime':'12:19:02','businesstype':'01','othernum':'17080763669','smstype':'1','otherarea':'','homearea':'010','deratefee':''},{'amount':'0.00','fee':'0.10','smsdate':'2017-05-30','smstime':'12:19:02','businesstype':'01','othernum':'17080763669','smstype':'1','otherarea':'','homearea':'010','deratefee':''}],'totalCount':1,'pageNo':1,'pages':[{'pageNo':1,'curr':true}],'pageSize':20,'totalPages':1},'isSuccess':true,'beginDate':'2017-05-01','userInfo':{'packageName':'4G-66元后付费基本套餐A','status':'开通','provincecode':'011','usernumber':'18600914623','expireTime':'1499921226475','nettype':'02','areaCode':'','certnum':'4311****142X','opendate':'20131229152358','citycode':'110','paytype':'2','productId':'18600914623','custName':'曾满意','brand':'9','productType':'01','packageID':'99002147','currentID':'18600914623','customid':'7015020946308155','custlvl':'四星用户','loginType':'01','nickName':'186****4623','subscrbstat':'开通','laststatdate':'','brand_name':'沃','is_wo':'2','is_20':false,'is_36':false,'certtype':'02','certaddr':'湖南省祁阳县茅竹镇茶园村4组','loginCustid':'7015020946308155','verifyState':'','lastLoginTime':'2017-07-13 10:44:46','defaultFlag':'00','isINUser':'0000','mapExtraParam_rls':'03','custsex':'1'}}";
		Gson gs = new Gson();
		UnicomRoot jsonObject = gs.fromJson(json, UnicomRoot.class);
		
		//List<UnicomNoteResult> lists = gs.fromJson(jsonObject.getPageMap().getResult().toString(), new TypeToken<List<UnicomNoteResult>>(){}.getType());
		String json2 = gs.toJson(jsonObject.getPageMap().getResult());
		
		//json2 = "{"+json2+"}";
		System.out.println(json2);
		//List<Object> lists = jsonObject.getPageMap().getResult();
		List<UnicomNoteResult> lists = gs.fromJson(json2,  new TypeToken<List<UnicomNoteResult>>(){}.getType());
		
		UnicomUserResultRoot rootresult = new UnicomUserResultRoot();
		rootresult.setResult(lists);
		List<UnicomNoteResult> lists2 = rootresult.getResult();
		for(UnicomNoteResult result:lists2){
			/*UnicomNoteResult i=(UnicomNoteResult)result;//拆箱
			System.out.println(result.toString());*/
			System.out.println(result.toString());
		}
		
	}
	public static void main(String[] args) throws IOException {
		test4();
	}
}
