
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.beijing.beijingcent.HousingBeiJingCenterBasicPayBean;
import com.microservice.dao.entity.crawler.housing.beijing.beijingcent.HousingBeiJingCenterPayBean;

import app.bean.BeiJingCenterBean;
import app.bean.BeijingCenterPayRootBean;
import app.bean.BeijingCenterUserRootBean;
import app.crawler.htmlparse.HousingBJParse;
import app.service.common.LoginAndGetCommon;

public class beijingtest {

	public static void main(String[] args) throws Exception {

		htmlunitTest();

	}

	public static void htmlunitTest() {
		Security.setProperty("jdk.tls.disabledAlgorithms", "SSLv3, DH keySize < 768");
		WebClient webClient = new WebClient(BrowserVersion.CHROME);

		webClient.getOptions().setUseInsecureSSL(true);

		webClient.setRefreshHandler(new ThreadedRefreshHandler());
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setTimeout(20000); // 15->60
		webClient.waitForBackgroundJavaScript(20000); // 5s
		String url = "https://grwsyw.bjgjj.gov.cn/ish/login";
		try {

			webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
			webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
			webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/");
			webClient.addRequestHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
			webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webClient.addRequestHeader("Cache-Control", "max-age=0");
			webClient.addRequestHeader("Connection", "keep-alive");
			// webClient.addRequestHeader("Content-Type",
			// "application/x-www-form-urlencoded");

			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("mm", "096571".trim()));
			paramsList.add(new NameValuePair("logintype", "card"));
			paramsList.add(new NameValuePair("yzfs", "1"));
			paramsList.add(new NameValuePair("hm", "130984199210023312".trim()));
			paramsList.add(new NameValuePair("xyjmdzd", "mmMD5".trim()));
			Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
			// System.out.println("==html==" +
			// page.getWebResponse().getContentAsString());

			String html = page.getWebResponse().getContentAsString();
			String rgex = "poolSelect = (.*?)\\;";
			Pattern pattern = Pattern.compile(rgex);// 匹配的模式
			Matcher m = pattern.matcher(html);
			String value = "";
			while (m.find()) {
				value = m.group(1);
			}

			BeiJingCenterBean beiJingCenterBean = HousingBJParse.beijingcenter_need_parse(value);

			System.out.println(beiJingCenterBean.toString());

			// getBasic(beiJingCenterBean, webClient);
			// getBasicPay(beiJingCenterBean, webClient);
			getPay(beiJingCenterBean, webClient);
			// Set<Cookie> cookies = webClient.getCookieManager().getCookies();

			// Map<String, String> map = new HashMap<>();
			// for (Cookie cookie : cookies) {
			// System.out.println(cookie.toString());
			// /*
			// * if (cookie.getName().indexOf("gjjaccnum") != -1) {
			// * map.put(cookie.getName(), cookie.getValue()); } if
			// * (cookie.getName().indexOf("gjjcertinum") != -1) {
			// * map.put(cookie.getName(), cookie.getValue()); }
			// */
			// }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getBasic(BeiJingCenterBean beiJingCenterBean, WebClient webClient) {
		String url = "https://grwsyw.bjgjj.gov.cn/ish/flow/command/home/homeurl/CMD_GRXX/"
				+ beiJingCenterBean.get_POOLKEY().trim();

		webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/home?_r=20a12aa8cd7944b8c47189146521");
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		try {
			Page page = LoginAndGetCommon.getHtml(url, webClient);

			System.out.println("============中国============" + page.getWebResponse().getContentAsString());

			BeijingCenterUserRootBean beijingCenterUserRootBean = HousingBJParse
					.beijingcenter_basic_parse(page.getWebResponse().getContentAsString());

			System.out.println(beijingCenterUserRootBean.getData().toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getBasicPay(BeiJingCenterBean beiJingCenterBean, WebClient webClient) {
		String url = "https://grwsyw.bjgjj.gov.cn/ish/flow/menu/PPLGRZH0101";

		webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/home?_r=20a12aa8cd7944b8c47189146521");
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		try {
			Page page = LoginAndGetCommon.getHtml(url, webClient);

			// System.out.println("============中国============" +
			// page.getWebResponse().getContentAsString());

			List<HousingBeiJingCenterBasicPayBean> result = HousingBJParse
					.beijingcenter_basicpay_parse(page.getWebResponse().getContentAsString());
			
			System.out.println(result.toString());

			// BeijingCenterRootBean beijingCenterRootBean = HousingBJParse
			// .beijingcenter_basic_parse(page.getWebResponse().getContentAsString());
			//
			// System.out.println(beijingCenterRootBean.getData().toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getPay(BeiJingCenterBean beiJingCenterBean, WebClient webClient) {
		String url = "https://grwsyw.bjgjj.gov.cn/ish/ydpx/parsepage?" + "ksrq=2016-10-03" + "&ywlx="
				+ "&zzrq=2018-08-13" + "&jbqd=" + "&%24page=PPLPageGRZH_0102_01.ydpx" + "&_POOLKEY="
				+ beiJingCenterBean.get_POOLKEY() + "&list_id=ywxxlsb" + "&dataset_id=gjjywxx" + "&list_page_no=1"
				+ "&gjjywxx_pagesize=100" + "&gjjywxx_order_by=1";

		webClient.addRequestHeader("Host", "grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Origin", "https://grwsyw.bjgjj.gov.cn");
		webClient.addRequestHeader("Referer", "https://grwsyw.bjgjj.gov.cn/ish/home?_r=20a12aa8cd7944b8c47189146521");
		webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
		webClient.addRequestHeader("Accept-Encoding", "gzip, deflate, br");
		webClient.addRequestHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webClient.addRequestHeader("Cache-Control", "max-age=0");
		webClient.addRequestHeader("Connection", "keep-alive");
		webClient.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");

		try {
			Page page = LoginAndGetCommon.getHtml(url, webClient);

			BeijingCenterPayRootBean beijingCenterPayRootBean = HousingBJParse
					.beijingcenter_payroot_parse(page.getWebResponse().getContentAsString());

			// System.out.println("-=============-" +
			// beijingCenterPayRootBean.getHtml());
			List<HousingBeiJingCenterPayBean> result = HousingBJParse
					.beijingcenter_pay_parse(beijingCenterPayRootBean.getHtml());
			for (HousingBeiJingCenterPayBean value : result) {
				System.out.println("====" + value.toString());
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
