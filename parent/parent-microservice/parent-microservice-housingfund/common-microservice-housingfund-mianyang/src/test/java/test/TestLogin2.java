package test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.module.htmlunit.WebCrawler;

public class TestLogin2 {

	public static void main(String[] args) throws Exception {

		org.apache.commons.logging.LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		String loginUrl= "http://www.cxzfgjj.com/gjj/gjj_cx.aspx";
		HtmlPage page2 =  getHtml(loginUrl,webClient);
		webClient.getOptions().setJavaScriptEnabled(true);  
		webClient.waitForBackgroundJavaScript(20000);
	  
		System.out.println(page2.asXml());
	  
		HtmlImage image =page2.getFirstByXPath("//img[(@id='imgCode')]");
		
		String imageName = UUID.randomUUID() + ".jpg";
		File file = new File("D:\\img\\"+imageName);
		image.saveAs(file);
	

		HtmlTextInput username = (HtmlTextInput)page2.getFirstByXPath("//input[@id='TextBox1']");
		HtmlTextInput name = (HtmlTextInput)page2.getFirstByXPath("//input[@id='TextBox2']");	
		
		HtmlPasswordInput password = (HtmlPasswordInput)page2.getFirstByXPath("//input[@id='TextBox3']");	
		HtmlTextInput code = (HtmlTextInput)page2.getFirstByXPath("//input[@id='TextBox4']");		
		HtmlElement button = (HtmlElement)page2.getFirstByXPath("//a[@onclick='gjjcx(1);']");
		
		System.out.println("username   :"+username.asXml());
		System.out.println("password   :"+password.asXml());	
		System.out.println("button   :"+button.asXml());
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		code.setText(input);
		username.setText("532301199009052122");
		name.setText("赵昕晨");
		password.setText("a199219b");
			
		HtmlPage loggedPage = button.click();
		System.out.println("登陆成功后的页面  ====》》"+loggedPage.asXml());
		Set<Cookie> cookies = loggedPage.getWebClient().getCookieManager().getCookies();
		for(Cookie cookie:cookies){
			System.out.println("登录成功后的cookie     ==》"+cookie.getName()+" : "+cookie.getValue());
		}
		
		String html=loggedPage.getWebResponse().getContentAsString();
		Document doc = Jsoup.parse(html, "utf-8"); 
		
		Element el=doc.getElementById("__VIEWSTATEGENERATOR");
		String __VIEWSTATEGENERATOR=el.attr("value");
		
		Element el_2=doc.getElementById("__EVENTVALIDATION");
		String __EVENTVALIDATION=el_2.attr("value");
		
		
		Element el_3=doc.getElementById("__VIEWSTATE");
		String __VIEWSTATE=el_3.attr("value");
		
		    String loginUrl4 = "http://www.cxzfgjj.com/gjj/gjj_cx.aspx";		                     
			WebRequest webRequest4 = new WebRequest(new URL(loginUrl4), HttpMethod.POST);	
	//		String requestBody="ScriptManager1=UpdatePanel2%7CButton1&__EVENTTARGET=&__EVENTARGUMENT=&TextBox1=532301199009052122&TextBox2=%E8%B5%B5%E6%98%95%E6%99%A8&TextBox3=a199219b&TextBox4="+input+"&TextBox5=&TextBox7=&TextBox8=&TextBox6=&bs=4&key=1&pkey=0&ekey=0&uid=0&xzh=0&lid=0&pid=0&lkl=&lts_f=&kk1=&kk2=&__VIEWSTATE=%2FwEPDwUKMTI5NTk3MTY5MQ9kFgICAw9kFgwCAQ8PFgIeBFRleHQFwQEmbmJzcDsmbmJzcDs8c3BhbiBjbGFzcz0nd3pfZm9udCc%2BPGI%2B5b2T5YmN5L2N572u77yaPC9iPjxhIGhyZWY9J2h0dHA6Ly93d3cuY3h6Zmdqai5jb20vZGVmYXVsdC5hc3B4JyAgY2xhc3M9J2Ffd3onPualmumbhOW3nuS9j%2BaIv%2BWFrOenr%2BmHkeeuoeeQhuS4reW%2FgzwvYT4gPj4g5Liq5Lq65L2P5oi%2F5YWs56ev6YeR5L2Z6aKd5p%2Bl6K%2BiZGQCBQ9kFgJmD2QWAgIDDw8WAh8ABbYBPGltZyBzcmM9J2ltYWdlcy5hc3B4P3NpZD0wLjc2MjgwNDcnICBpZD0naW1nQ29kZScgYWx0PSfnnIvkuI3muIXvvJ%2FmjaLkuIDlvKDjgILjgILjgIInIHN0eWxlPSdjdXJzb3I6cG9pbnRlcjsgdmVydGljYWwtYWxpZ246bWlkZGxlOyBwYWRkaW5nLWJvdHRvbTo0cHg7JyBvbmNsaWNrPSdnZXRWSW1nKHRoaXMpOycgLz5kZAIGD2QWAmYPZBYCAgEPDxYCHwAFzAE8YSBocmVmPScjJyBvbmNsaWNrID0nZ2pqY3goMSk7JyBjbGFzcyA9J2RqX2x5Jz7lvIDlp4vmn6Xor6I8L2E%2BJm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7ICAgPGEgaHJlZj0nIycgb25jbGljaz0nZ2pqX3BzdygxKTsnIGNsYXNzID0nZGpfbHknPuaJvuWbnuWvhueggTwvYT5kZAIJD2QWAmYPZBYCAgEPDxYCHwAFQjxici8%2BPHNwYW4gc3R5bGU9J2NvbG9yOnJlZCc%2B6aqM6K%2BB56CB6ZSZ6K%2Bv77yM6K%2B35qOA5p%2Bl77yBPC9zcGFuPmRkAgoPZBYCZg9kFgICAQ8PFgIfAAXPCzxkaXYgYWxpZ249J2NlbnRlcicgc3R5bGU9J3BhZGRpbmc6MHB4IDEwcHggMHB4IDEwcHgnPjx0YWJsZSBzdHlsZT0id2lkdGg6IDU2MHB4OyBmb250LXNpemU6IDEycHg7IiBjZWxsc3BhY2luZz0iMCIgY2VsbHBhZGRpbmc9IjAiPjxicj48dHI%2BPHRkIGFsaWduPSJjZW50ZXIiIGhlaWdodD0iMzAiID48c3BhbiBzdHlsZT0iZm9udC1zaXplOjE0cHg7Y29sb3I6I0ZGMDAwMCI%2BPGI%2B5qWa6ZuE5bee5Liq5Lq65L2P5oi%2F5YWs56ev6YeR5L2Z6aKd5p%2Bl6K%2Bi6aG755%2BlPC9iPjwvc3Bhbj48L3RkPjwvdHI%2BPGJyPjx0cj48dGQgYWxpZ249ImNlbnRlciIgPjxpbWcgc3JjPSJpbWFnZXMvbGluZV8xLnBuZyIgd2lkdGg9IjU1MCIgYWx0PSIiLz48L3RkPjwvdHI%2BPGJyPjx0cj48dGQ%2BPGRpdiBzdHlsZT0ibGluZS1oZWlnaHQ6MjJweDtjb2xvcjp0ZWFsIiBhbGlnbj0ibGVmdCI%2BPGJyPjxiPuS4gOOAgeafpeivouaWueazle%2B8mjwvYj48YnIvPuOAgOOAgDHjgIHor7fovpPlhaXkvaDnmoQxOOS9jeaIluiAhTE15L2N6Lqr5Lu96K%2BB5Y%2B377ybPGJyLz7jgIDjgIAy44CB6K%2B36L6T5YWl5oKo55qE5aeT5ZCN77yI5Lit6Ze05LiN6IO955WZ56m65qC877yJ77ybPGJyLz7jgIDjgIAz44CB5a%2BG56CBKOWIneWni%2BWvhueggTEyMzQ1Nu%2B8jOWmguaenOacquWPmOWKqOWPr%2BS7peS4jei%2Bk%2BWFpSnvvJs8YnIvPuOAgOOAgDTjgIHpqozor4HnoIHkuI3ljLrliIblpKflsI%2FlhpnjgII8YnIvPjxiPuS6jOOAgeazqOaEj%2BS6i%2Bmhue%2B8mjwvYj48YnIvPuOAgOOAgDHjgIHlhaznp6%2Fph5HmlbDmja7mr4%2FmnIjmm7TmlrDkuIDmrKHvvIjlnKjmr4%2FmnIjnmoQxNeWPt%2BS5i%2BWJjeabtOaWsO%2B8ie%2B8mzxici8%2B44CA44CAMuOAgeWmguaenOaCqOaYr%2BesrOS4gOasoeafpeivou%2B8jOWIneWni%2BWvhueggeS4ujEyMzQ1Nu%2B8jOS4uuS%2FneivgeaCqOeahOS%2FoeaBr%2BWuieWFqO%2B8jOafpeivouaIkOWKn%2BWQjuivt%2BWwveW%2Fq%2BS%2FruaUueWvhuegge%2B8mzxici8%2B44CA44CAM%2BOAgeW%2FmOiusOWvhueggeaIluiAheS4jeiDveato%2Behruafpeivou%2B8jOivt%2Bi3n%2BaIkeS7rOiBlOezu%2B%2B8jOmCrueuse%2B8mjxBIGhyZWY9Im1haWx0bzpjeF9qc0AxNjMuY29tIj5jeF9qc0AxNjMuY29tPC9BPu%2B8m%2BeUteivne%2B8mjA4NzgtODk4MzM0OeOAgjwvRk9OVD48YnIvPuOAgOOAgDTjgIHlpoLmnpzmmoLml7bmnKrph4fpm4bmlrDouqvku73or4Hlj7fnmoTor7fnlKjogIHmlrnms5Xmn6Xor6I6IDxhIGhyZWY9Imdqal9jeF9vbGQuYXNweD9icz0xMDAwIiB0YXJnZXQ9Il9ibGFuayIgY2xhc3MgPSJkal9seSI%2B6ICB6LSm5Y%2B35p%2Bl6K%2BiPC9hPjwvUD48YnI%2BPFAgYWxpZ249Y2VudGVyPjxGT05UIGNvbG9yPSMwMDAwMDAgc2l6ZT0yPu%2B8iOacrOezu%2Be7n%2BWFseaIkOWKn%2Bafpeivou%2B8mjxGT05UIGNvbG9yPSM4MDAwODA%2BMTc4NTU1MDwvRk9OVD7kurrmrKHvvIk8L0ZPTlQ%2BPC9QPjxici8%2BPGJyPjwvZGl2Pjxicj48L3RkPjwvdHI%2BPGJyPjwvdGFibGU%2BPC9kaXY%2BZGQCCw8PFgIfAAWOCjxkaXYgY2xhc3M9J2Zvb3RfYm94IHdyYXAxMTAwJz48ZGl2IGNsYXNzPSdmb290X2JveDIgZGlzcGxheSBsZWZ0Jz48cD7kuLvlip7vvJrmpZrpm4Tlt57kvY%2FmiL%2Flhaznp6%2Fph5HnrqHnkIbkuK3lv4Mg54mI5p2D5omA5pyJJmNvcHk7IFvmgLvorr%2Fpl67ph4%2FvvJo5NjIwODIzXSA8L3A%2BDQo8cD7mib%2Flip4m566h55CG77ya5qWa6ZuE5bee5L2P5oi%2F5YWs56ev6YeR566h55CG5Lit5b%2BDIOaKgOacr%2BaUr%2BaMge%2B8mualmumbhOW3nueCueWHu%2Be9kee7nOaKgOacr%2BacjeWKoeaciemZkOWFrOWPuDwvcD4NCjxwPueuoeeQhuWRmOmCrueuse%2B8mmN4X2pzQDE2My5jb20g55S16K%2Bd77yaMDg3OC0zMzY5MzU1KOS4reW%2Fg%2BWKnikgMDg3OC04OTgwMzU4KOeCueWHu%2BWFrOWPuCkgPC9wPg0KPHA%2BaWNw5aSH5qGI5Y%2B377ya5ruHSUNQ5aSHMTcwMDQ2NTDlj7ctMSA8L3A%2BDQo8cD4JCSAJPGRpdiBzdHlsZT0id2lkdGg6MzAwcHg7bWFyZ2luOjAgYXV0bzsgcGFkZGluZzo1cHggMDsiPg0KCQkgCQk8YSB0YXJnZXQ9Il9ibGFuayIgaHJlZj0iaHR0cDovL3d3dy5iZWlhbi5nb3YuY24vcG9ydGFsL3JlZ2lzdGVyU3lzdGVtSW5mbz9yZWNvcmRjb2RlPTUzMjMwMTAyMDAwMjk2IiBzdHlsZT0iZGlzcGxheTppbmxpbmUtYmxvY2s7dGV4dC1kZWNvcmF0aW9uOm5vbmU7aGVpZ2h0OjIwcHg7bGluZS1oZWlnaHQ6MjBweDsiPjxpbWcgc3JjPSJodHRwOi8vcW4uY3gzNjMuY29tL2EucG5nIiBzdHlsZT0iZmxvYXQ6bGVmdDsiLz48cCBzdHlsZT0iZmxvYXQ6bGVmdDtoZWlnaHQ6MjBweDtsaW5lLWhlaWdodDoyMHB4O21hcmdpbjogMHB4IDBweCAwcHggNXB4OyBjb2xvcjojZmZmOyI%2B5ruH5YWs572R5a6J5aSHIDUzMjMwMTAyMDAwMjk25Y%2B3PC9wPjwvYT4NCgkJIAk8L2Rpdj48L3A%2BDQo8cD48c2NyaXB0IGlkPSJfaml1Y3VvXyIgc2l0ZWNvZGU9NTMyMzAwMDAxMCBzcmM9aHR0cDovL3B1Y2hhLmthaXB1eXVuLmNuL2V4cG9zdXJlL2ppdWN1by5qcz48L3NjcmlwdD48L3A%2BDQoNCjxkaXY%2BDQo8c3BhbiBzdHlsZT0ibWFyZ2luOjBweCAyMHB4OyI%2BPHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiPmRvY3VtZW50LndyaXRlKHVuZXNjYXBlKCIlM0NzcGFuIGlkPV9pZGVDb25hYyAlM0UlM0Mvc3BhbiUzRSUzQ3NjcmlwdCBzcmM9aHR0cDovL2Rjcy5jb25hYy5jbi9qcy8yNS8zNzcvMDAwMC82MDIzODU0MC9DQTI1Mzc3MDAwMDYwMjM4NTQwMDAwMi5qcyB0eXBlPXRleHQvamF2YXNjcmlwdCUzRSUzQy9zY3JpcHQlM0UiKSk7PC9zY3JpcHQ%2BPC9zcGFuPg0KPC9kaXY%2BDQo8L2Rpdj48L2Rpdj5kZGSXBJVBMgbytu%2BIGojBWCOZMnTGU8gAUKSYoq4Gecd7Dg%3D%3D&__VIEWSTATEGENERATOR=707E8F83&__EVENTVALIDATION=%2FwEWGAKjhL6%2FCQLs0e58AuzRgtgJAuzR2qEIAuzR%2FrYIAuzRxsYPAoznisYGArursYYIAtaUz5sCAuKA9NcJAuGCq8AFAsyCq8AFAsGZyZUMAoqrn5cCArqZyZUMAsaZyZUMAuLP0usKAuuPkdgMAvqX%2F9AFApWBneYPAuzRsusGAuzR9tkMAuzRirUFAr7v3u8M6IcDXPkPa7uUCuleQSSF%2BtiZWdCQKFioQqBOEFK931A%3D&__ASYNCPOST=true&Button1=Button";
			
			String requestBody="ScriptManager1=UpdatePanel2%7CButton1&__EVENTTARGET=&__EVENTARGUMENT=&TextBox1=532301199009052122&TextBox2=%E8%B5%B5%E6%98%95%E6%99%A8&TextBox3=a199219b&TextBox4="+input+"&TextBox5=&TextBox7=&TextBox8=&TextBox6=&bs=4&key=1&pkey=0&ekey=0&uid=0&xzh=0&lid=0&pid=0&lkl=&lts_f=&kk1=&kk2=&__VIEWSTATE=%2FwEPDwUKMTI5NTk3MTY5MQ9kFgICAw9kFgwCAQ8PFgIeBFRleHQFwQEmbmJzcDsmbmJzcDs8c3BhbiBjbGFzcz0nd3pfZm9udCc%2BPGI%2B5b2T5YmN5L2N572u77yaPC9iPjxhIGhyZWY9J2h0dHA6Ly93d3cuY3h6Zmdqai5jb20vZGVmYXVsdC5hc3B4JyAgY2xhc3M9J2Ffd3onPualmumbhOW3nuS9j%2BaIv%2BWFrOenr%2BmHkeeuoeeQhuS4reW%2FgzwvYT4gPj4g5Liq5Lq65L2P5oi%2F5YWs56ev6YeR5L2Z6aKd5p%2Bl6K%2BiZGQCBQ9kFgJmD2QWAgIDDw8WAh8ABbYBPGltZyBzcmM9J2ltYWdlcy5hc3B4P3NpZD0wLjc2MjgwNDcnICBpZD0naW1nQ29kZScgYWx0PSfnnIvkuI3muIXvvJ%2FmjaLkuIDlvKDjgILjgILjgIInIHN0eWxlPSdjdXJzb3I6cG9pbnRlcjsgdmVydGljYWwtYWxpZ246bWlkZGxlOyBwYWRkaW5nLWJvdHRvbTo0cHg7JyBvbmNsaWNrPSdnZXRWSW1nKHRoaXMpOycgLz5kZAIGD2QWAmYPZBYCAgEPDxYCHwAFzAE8YSBocmVmPScjJyBvbmNsaWNrID0nZ2pqY3goMSk7JyBjbGFzcyA9J2RqX2x5Jz7lvIDlp4vmn6Xor6I8L2E%2BJm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7Jm5ic3A7ICAgPGEgaHJlZj0nIycgb25jbGljaz0nZ2pqX3BzdygxKTsnIGNsYXNzID0nZGpfbHknPuaJvuWbnuWvhueggTwvYT5kZAIJD2QWAmYPZBYCAgEPDxYCHwAFQjxici8%2BPHNwYW4gc3R5bGU9J2NvbG9yOnJlZCc%2B6aqM6K%2BB56CB6ZSZ6K%2Bv77yM6K%2B35qOA5p%2Bl77yBPC9zcGFuPmRkAgoPZBYCZg9kFgICAQ8PFgIfAAXPCzxkaXYgYWxpZ249J2NlbnRlcicgc3R5bGU9J3BhZGRpbmc6MHB4IDEwcHggMHB4IDEwcHgnPjx0YWJsZSBzdHlsZT0id2lkdGg6IDU2MHB4OyBmb250LXNpemU6IDEycHg7IiBjZWxsc3BhY2luZz0iMCIgY2VsbHBhZGRpbmc9IjAiPjxicj48dHI%2BPHRkIGFsaWduPSJjZW50ZXIiIGhlaWdodD0iMzAiID48c3BhbiBzdHlsZT0iZm9udC1zaXplOjE0cHg7Y29sb3I6I0ZGMDAwMCI%2BPGI%2B5qWa6ZuE5bee5Liq5Lq65L2P5oi%2F5YWs56ev6YeR5L2Z6aKd5p%2Bl6K%2Bi6aG755%2BlPC9iPjwvc3Bhbj48L3RkPjwvdHI%2BPGJyPjx0cj48dGQgYWxpZ249ImNlbnRlciIgPjxpbWcgc3JjPSJpbWFnZXMvbGluZV8xLnBuZyIgd2lkdGg9IjU1MCIgYWx0PSIiLz48L3RkPjwvdHI%2BPGJyPjx0cj48dGQ%2BPGRpdiBzdHlsZT0ibGluZS1oZWlnaHQ6MjJweDtjb2xvcjp0ZWFsIiBhbGlnbj0ibGVmdCI%2BPGJyPjxiPuS4gOOAgeafpeivouaWueazle%2B8mjwvYj48YnIvPuOAgOOAgDHjgIHor7fovpPlhaXkvaDnmoQxOOS9jeaIluiAhTE15L2N6Lqr5Lu96K%2BB5Y%2B377ybPGJyLz7jgIDjgIAy44CB6K%2B36L6T5YWl5oKo55qE5aeT5ZCN77yI5Lit6Ze05LiN6IO955WZ56m65qC877yJ77ybPGJyLz7jgIDjgIAz44CB5a%2BG56CBKOWIneWni%2BWvhueggTEyMzQ1Nu%2B8jOWmguaenOacquWPmOWKqOWPr%2BS7peS4jei%2Bk%2BWFpSnvvJs8YnIvPuOAgOOAgDTjgIHpqozor4HnoIHkuI3ljLrliIblpKflsI%2FlhpnjgII8YnIvPjxiPuS6jOOAgeazqOaEj%2BS6i%2Bmhue%2B8mjwvYj48YnIvPuOAgOOAgDHjgIHlhaznp6%2Fph5HmlbDmja7mr4%2FmnIjmm7TmlrDkuIDmrKHvvIjlnKjmr4%2FmnIjnmoQxNeWPt%2BS5i%2BWJjeabtOaWsO%2B8ie%2B8mzxici8%2B44CA44CAMuOAgeWmguaenOaCqOaYr%2BesrOS4gOasoeafpeivou%2B8jOWIneWni%2BWvhueggeS4ujEyMzQ1Nu%2B8jOS4uuS%2FneivgeaCqOeahOS%2FoeaBr%2BWuieWFqO%2B8jOafpeivouaIkOWKn%2BWQjuivt%2BWwveW%2Fq%2BS%2FruaUueWvhuegge%2B8mzxici8%2B44CA44CAM%2BOAgeW%2FmOiusOWvhueggeaIluiAheS4jeiDveato%2Behruafpeivou%2B8jOivt%2Bi3n%2BaIkeS7rOiBlOezu%2B%2B8jOmCrueuse%2B8mjxBIGhyZWY9Im1haWx0bzpjeF9qc0AxNjMuY29tIj5jeF9qc0AxNjMuY29tPC9BPu%2B8m%2BeUteivne%2B8mjA4NzgtODk4MzM0OeOAgjwvRk9OVD48YnIvPuOAgOOAgDTjgIHlpoLmnpzmmoLml7bmnKrph4fpm4bmlrDouqvku73or4Hlj7fnmoTor7fnlKjogIHmlrnms5Xmn6Xor6I6IDxhIGhyZWY9Imdqal9jeF9vbGQuYXNweD9icz0xMDAwIiB0YXJnZXQ9Il9ibGFuayIgY2xhc3MgPSJkal9seSI%2B6ICB6LSm5Y%2B35p%2Bl6K%2BiPC9hPjwvUD48YnI%2BPFAgYWxpZ249Y2VudGVyPjxGT05UIGNvbG9yPSMwMDAwMDAgc2l6ZT0yPu%2B8iOacrOezu%2Be7n%2BWFseaIkOWKn%2Bafpeivou%2B8mjxGT05UIGNvbG9yPSM4MDAwODA%2BMTc4NTU1MDwvRk9OVD7kurrmrKHvvIk8L0ZPTlQ%2BPC9QPjxici8%2BPGJyPjwvZGl2Pjxicj48L3RkPjwvdHI%2BPGJyPjwvdGFibGU%2BPC9kaXY%2BZGQCCw8PFgIfAAWOCjxkaXYgY2xhc3M9J2Zvb3RfYm94IHdyYXAxMTAwJz48ZGl2IGNsYXNzPSdmb290X2JveDIgZGlzcGxheSBsZWZ0Jz48cD7kuLvlip7vvJrmpZrpm4Tlt57kvY%2FmiL%2Flhaznp6%2Fph5HnrqHnkIbkuK3lv4Mg54mI5p2D5omA5pyJJmNvcHk7IFvmgLvorr%2Fpl67ph4%2FvvJo5NjIwODIzXSA8L3A%2BDQo8cD7mib%2Flip4m566h55CG77ya5qWa6ZuE5bee5L2P5oi%2F5YWs56ev6YeR566h55CG5Lit5b%2BDIOaKgOacr%2BaUr%2BaMge%2B8mualmumbhOW3nueCueWHu%2Be9kee7nOaKgOacr%2BacjeWKoeaciemZkOWFrOWPuDwvcD4NCjxwPueuoeeQhuWRmOmCrueuse%2B8mmN4X2pzQDE2My5jb20g55S16K%2Bd77yaMDg3OC0zMzY5MzU1KOS4reW%2Fg%2BWKnikgMDg3OC04OTgwMzU4KOeCueWHu%2BWFrOWPuCkgPC9wPg0KPHA%2BaWNw5aSH5qGI5Y%2B377ya5ruHSUNQ5aSHMTcwMDQ2NTDlj7ctMSA8L3A%2BDQo8cD4JCSAJPGRpdiBzdHlsZT0id2lkdGg6MzAwcHg7bWFyZ2luOjAgYXV0bzsgcGFkZGluZzo1cHggMDsiPg0KCQkgCQk8YSB0YXJnZXQ9Il9ibGFuayIgaHJlZj0iaHR0cDovL3d3dy5iZWlhbi5nb3YuY24vcG9ydGFsL3JlZ2lzdGVyU3lzdGVtSW5mbz9yZWNvcmRjb2RlPTUzMjMwMTAyMDAwMjk2IiBzdHlsZT0iZGlzcGxheTppbmxpbmUtYmxvY2s7dGV4dC1kZWNvcmF0aW9uOm5vbmU7aGVpZ2h0OjIwcHg7bGluZS1oZWlnaHQ6MjBweDsiPjxpbWcgc3JjPSJodHRwOi8vcW4uY3gzNjMuY29tL2EucG5nIiBzdHlsZT0iZmxvYXQ6bGVmdDsiLz48cCBzdHlsZT0iZmxvYXQ6bGVmdDtoZWlnaHQ6MjBweDtsaW5lLWhlaWdodDoyMHB4O21hcmdpbjogMHB4IDBweCAwcHggNXB4OyBjb2xvcjojZmZmOyI%2B5ruH5YWs572R5a6J5aSHIDUzMjMwMTAyMDAwMjk25Y%2B3PC9wPjwvYT4NCgkJIAk8L2Rpdj48L3A%2BDQo8cD48c2NyaXB0IGlkPSJfaml1Y3VvXyIgc2l0ZWNvZGU9NTMyMzAwMDAxMCBzcmM9aHR0cDovL3B1Y2hhLmthaXB1eXVuLmNuL2V4cG9zdXJlL2ppdWN1by5qcz48L3NjcmlwdD48L3A%2BDQoNCjxkaXY%2BDQo8c3BhbiBzdHlsZT0ibWFyZ2luOjBweCAyMHB4OyI%2BPHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiPmRvY3VtZW50LndyaXRlKHVuZXNjYXBlKCIlM0NzcGFuIGlkPV9pZGVDb25hYyAlM0UlM0Mvc3BhbiUzRSUzQ3NjcmlwdCBzcmM9aHR0cDovL2Rjcy5jb25hYy5jbi9qcy8yNS8zNzcvMDAwMC82MDIzODU0MC9DQTI1Mzc3MDAwMDYwMjM4NTQwMDAwMi5qcyB0eXBlPXRleHQvamF2YXNjcmlwdCUzRSUzQy9zY3JpcHQlM0UiKSk7PC9zY3JpcHQ%2BPC9zcGFuPg0KPC9kaXY%2BDQo8L2Rpdj48L2Rpdj5kZGSXBJVBMgbytu%2BIGojBWCOZMnTGU8gAUKSYoq4Gecd7Dg%3D%3D&__VIEWSTATEGENERATOR=707E8F83&__EVENTVALIDATION=%2FwEWGAKjhL6%2FCQLs0e58AuzRgtgJAuzR2qEIAuzR%2FrYIAuzRxsYPAoznisYGArursYYIAtaUz5sCAuKA9NcJAuGCq8AFAsyCq8AFAsGZyZUMAoqrn5cCArqZyZUMAsaZyZUMAuLP0usKAuuPkdgMAvqX%2F9AFApWBneYPAuzRsusGAuzR9tkMAuzRirUFAr7v3u8M6IcDXPkPa7uUCuleQSSF%2BtiZWdCQKFioQqBOEFK931A%3D&__ASYNCPOST=true&Button1=Button";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("ScriptManager1", "UpdatePanel2|Button1"));
			paramsList.add(new NameValuePair("__EVENTTARGET", ""));
			paramsList.add(new NameValuePair("__EVENTARGUMENT", ""));
			paramsList.add(new NameValuePair("TextBox1", "532301199009052122"));
			paramsList.add(new NameValuePair("TextBox2", "赵昕晨"));
			paramsList.add(new NameValuePair("TextBox3", "a199219b"));
			paramsList.add(new NameValuePair("TextBox4", input));
			paramsList.add(new NameValuePair("TextBox5", ""));
			paramsList.add(new NameValuePair("TextBox7", ""));
			paramsList.add(new NameValuePair("TextBox8", ""));
			paramsList.add(new NameValuePair("TextBox6", ""));
			
				paramsList.add(new NameValuePair("bs", "4"));
			paramsList.add(new NameValuePair("key", "1"));
			paramsList.add(new NameValuePair("pkey", "0"));
			paramsList.add(new NameValuePair("ekey", "0"));
			paramsList.add(new NameValuePair("uid", "0"));
				
			paramsList.add(new NameValuePair("xzh", "0"));
			paramsList.add(new NameValuePair("lid", "0"));
			paramsList.add(new NameValuePair("pid", "0"));
				
			paramsList.add(new NameValuePair("lkl", ""));
			paramsList.add(new NameValuePair("lts_f", ""));
			paramsList.add(new NameValuePair("kk1", ""));
			paramsList.add(new NameValuePair("kk2", ""));
			paramsList.add(new NameValuePair("__VIEWSTATE", __VIEWSTATE));
			
			paramsList.add(new NameValuePair("__VIEWSTATEGENERATOR", __VIEWSTATEGENERATOR));

			paramsList.add(new NameValuePair("__EVENTVALIDATION", __EVENTVALIDATION));
			paramsList.add(new NameValuePair("__ASYNCPOST", "true"));
			paramsList.add(new NameValuePair("Button1", "Button"));
			webRequest4.setAdditionalHeader("Accept", "*/*");
			webRequest4.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest4.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest4.setAdditionalHeader("Connection", "keep-alive");
			webRequest4.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			webRequest4.setAdditionalHeader("Host", "www.cxzfgjj.com");
			webRequest4.setAdditionalHeader("Origin", "http://www.cxzfgjj.com");
			webRequest4.setAdditionalHeader("Referer", "http://www.cxzfgjj.com/gjj/gjj_cx.aspx");
			webRequest4.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36");
			webRequest4.setAdditionalHeader("X-MicrosoftAjax", "Delta=true");
			webRequest4.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
			webRequest4.setRequestParameters(paramsList);
			//webRequest4.setRequestBody(requestBody);
			Page page4 = webClient.getPage(webRequest4);
			System.out.println(page4.getWebResponse().getContentAsString());
		
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(500000);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;

	}
}
