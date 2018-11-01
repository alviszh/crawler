package app.unit;



import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.crawler.telecomhtmlunit.LognAndGetYunNanUnit;

public class CommonunitQingHai {

	public static WebClient getWebClientForTeleComQingHai(TaskMobile taskMobile) {
		WebClient webClientlogin = WebCrawler.getInstance().getNewWebClient();
		webClientlogin = TeleComCommonUnit.addcookie(webClientlogin, taskMobile);
		try{
						
			String url = "http://wwwr.189.cn/dqmh/my189/initMy189home.do?fastcode=00900906";
			HtmlPage html3 = (HtmlPage) LognAndGetYunNanUnit.getHtml(url, webClientlogin);

			url = "http://qh.189.cn/service/bill/initQueryTicket.parser?rnd=0.3154301050822139";

			html3 = (HtmlPage) LognAndGetYunNanUnit.getHtml(url, webClientlogin);
			webClientlogin = html3.getWebClient();
			return webClientlogin;
		}catch(Exception e){
			return webClientlogin;
		}
		
		/*WebClient webClient = WebCrawler.getInstance().getNewWebClient();

		Set<Cookie> cookies3 = webClientlogin.getCookieManager().getCookies();
		for (Cookie cookie3 : cookies3) {

			tracer.addTag("登录cookie", cookies3.toString());

			if (cookie3.getName().indexOf("JSESSIONID_PERSONWEB") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}

			if (cookie3.getName().indexOf("trkHmClickCoords") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());
				webClient.getCookieManager().addCookie(cookie3);
			}
			if (cookie3.getName().indexOf("COM.TYDIC.SSO_AUTH_TOKEN") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}

			if (cookie3.getName().indexOf("Hm_lpvt_024e4958b87ba93ed27e4571805fbb5a") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}

			if (cookie3.getName().indexOf("Hm_lpvt_024e4958b87ba93ed27e4571805fbb5a") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}
			if (cookie3.getName().indexOf("WT_FPC") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}
			if (cookie3.getName().indexOf("WT_SS") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}

			if (cookie3.getName().indexOf(".ybtj.189.cn") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}

			if (cookie3.getName().indexOf("WT_si_n") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}

			if (cookie3.getName().indexOf("trkId") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}
			if (cookie3.getName().indexOf("s_fid") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}
			if (cookie3.getName().indexOf("lvid") != -1) {
				tracer.addTag("登录cookie", cookie3.toString());

				webClient.getCookieManager().addCookie(cookie3);
			}

		}*/
		
		
	}
}
