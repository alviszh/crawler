package app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.changchun")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.changchun")
public class LoginAndGetService extends HousingBasicService {

	// 根据身份证号登录
	public WebParamHousing<?> loginByIDCard(WebClient webClient, String name, String password) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		String url = "http://www.cczfgjj.gov.cn/GJJQuery";
		String url_login = "http://www.cczfgjj.gov.cn/grlogin.jhtml";
		HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url_login, webClient);
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='guestbookCaptcha']");
		webClient.addRequestHeader("Host", "www.cczfgjj.gov.cn");
		webClient.addRequestHeader("Origin", "http://www.cczfgjj.gov.cn");
		webClient.addRequestHeader("Referer", "http://www.cczfgjj.gov.cn/grlogin.jhtml");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");
		System.out.println(valicodeStr);

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("tranCode", "142805"));
		paramsList.add(new NameValuePair("task", ""));
		paramsList.add(new NameValuePair("accnum", ""));
		paramsList.add(new NameValuePair("certinum", name));
		paramsList.add(new NameValuePair("pwd", password));
		paramsList.add(new NameValuePair("verify", valicodeStr));
		Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);

		webParamHousing.setPage(page);
		webParamHousing.setStatusCode(page.getWebResponse().getStatusCode());
		webParamHousing.setWebClient(webClient);
		tracer.addTag("公积金登录  loginByIDCard", "<xmp>" + page.getWebResponse().getContentAsString() + "</xmp>");

		return webParamHousing;

	}

	// 根据个人公积金账号登录
	public WebParamHousing<?> loginByIDPersonal(WebClient webClient, String name, String password) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();
		String url = "http://www.cczfgjj.gov.cn/GJJQuery";
		String url_login = "http://www.cczfgjj.gov.cn/grlogin.jhtml";
		HtmlPage htmlPage = (HtmlPage) LoginAndGetCommon.getHtml(url_login, webClient);
		HtmlImage valiCodeImg = htmlPage.getFirstByXPath("//*[@id='guestbookCaptcha']");
		webClient.addRequestHeader("Host", "www.cczfgjj.gov.cn");
		webClient.addRequestHeader("Origin", "http://www.cczfgjj.gov.cn");
		webClient.addRequestHeader("Referer", "http://www.cczfgjj.gov.cn/grlogin.jhtml");
		String valicodeStr = chaoJiYingOcrService.getVerifycode(valiCodeImg, "5000");
		System.out.println(valicodeStr);

		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("tranCode", "142805"));
		paramsList.add(new NameValuePair("task", ""));
		paramsList.add(new NameValuePair("accnum", name));
		paramsList.add(new NameValuePair("certinum", ""));
		paramsList.add(new NameValuePair("pwd", password));
		paramsList.add(new NameValuePair("verify", valicodeStr));
		Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);

		webParamHousing.setPage(page);
		webParamHousing.setStatusCode(page.getWebResponse().getStatusCode());
		webParamHousing.setWebClient(webClient);
		tracer.addTag("公积金登录  loginByIDCard", "<xmp>" + page.getWebResponse().getContentAsString() + "</xmp>");

		return webParamHousing;

	}

	// 抓取用户信息
	@Async
	public Future<WebParamHousing<?>> getAccountInfor(WebClient webClient, Map<String, String> map) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();

		String url = "http://www.cczfgjj.gov.cn/GJJQuery";

		try {
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("tranCode", "112813"));
			paramsList.add(new NameValuePair("task", ""));
			paramsList.add(new NameValuePair("accnum", map.get("gjjaccnum").trim()));
			paramsList.add(new NameValuePair("certinum", map.get("gjjcertinum").trim()));

			Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			System.out.println("==" + page.getWebResponse().getContentAsString());
			tracer.addTag("公积金  getAccountInfor", "<xmp>" + page.getWebResponse().getContentAsString() + "</xmp>");
			webParamHousing.setPage(page);
			webParamHousing.setStatusCode(200);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("公积金  getAccountDetails", e.getMessage());
			webParamHousing.setStatusCode(404);
			webParamHousing.setErrormessage("抓取失败登录失败 连接超时");
		}
		return new AsyncResult<WebParamHousing<?>>(webParamHousing);
	}

	// 抓取用户信息
	@Async
	public Future<WebParamHousing<?>> getAccountDetails(WebClient webClient, Map<String, String> map) throws Exception {
		WebParamHousing<?> webParamHousing = new WebParamHousing<>();

		String url = "http://www.cczfgjj.gov.cn/GJJQuery";
		LocalDate today = LocalDate.now();
		// 本月的第一天
		LocalDate stardate = today.plusYears(-2);
		try {
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("tranCode", "112814"));
			paramsList.add(new NameValuePair("task", "ftp"));
			paramsList.add(new NameValuePair("accnum", map.get("gjjaccnum").trim()));
			paramsList.add(new NameValuePair("begdate", stardate + ""));
			paramsList.add(new NameValuePair("enddate", today + ""));

			Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			System.out.println("==" + page.getWebResponse().getContentAsString());
			tracer.addTag("公积金  getAccountDetails", "<xmp>" + page.getWebResponse().getContentAsString() + "</xmp>");
			webParamHousing.setPage(page);
			webParamHousing.setStatusCode(200);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("公积金  getAccountDetails", e.getMessage());
			webParamHousing.setStatusCode(404);
			webParamHousing.setErrormessage("抓取失败  连接超时");
		}
		return new AsyncResult<WebParamHousing<?>>(webParamHousing);
	}

}
