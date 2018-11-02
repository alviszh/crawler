package app.service.common;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.common.TelecomStarlevel;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamTelecom;
import app.commontracerlog.TracerLog;
import app.crawler.telecom.htmlparse.TelecomParseCommon;
import net.sf.json.JSONObject;

@Component
@Service
public class LoginAndGetService {

	public final Logger log = LoggerFactory.getLogger(LoginAndGetService.class);
	@Autowired
	private TracerLog tracerLog;


	// 获取北京用户积分 话费余额
	public  String getPointsAndCharges(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		String url = "http://www.189.cn/dqmh/order/getHuaFei.do";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient = LoginAndGetCommon.addcookie(webClient, taskMobile);

		Page page = LoginAndGetCommon.gethtmlPost(webClient, null, url);

		return page.getWebResponse().getContentAsString();
	}

	/**
	 * 用户星级服务信息
	 * 
	 * @param taskMobile
	 * @return
	 * @throws Exception
	 */
	public WebParamTelecom<TelecomStarlevel> getStarlevel(TaskMobile taskMobile,int k) throws Exception {

		tracerLog.addTag("TelecomShanxiParser.getStarlevel", taskMobile.getTaskid());

		Thread.sleep(2000);

		String transactionId = "";
		String token = "";
		String mobile = "";
		String sign = "";
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = LoginAndGetCommon.addcookie(webClient, taskMobile);
			try {
				// 跳转星级服务
				String url = "http://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=93510&toStUrl=http://xjfw.189.cn/tykfh5/modules/starService/medalWall/indexPC.html?intaid=jt-sy-hxfw-01-";
				HtmlPage htmlT = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
				webClient = htmlT.getWebClient();

			} catch (Exception e) {
				e.printStackTrace();
				tracerLog.addTag("urlT---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}
			try {
				// 通过此接口获取手机号与token
				String url1 = "http://xjfw.189.cn/tykf-itr-services/services/login/bySessionId";
				Page html = getPage(webClient, taskMobile, url1, null);
				String json = html.getWebResponse().getContentAsString();

				tracerLog.addTag("TelecomShanxiParser.getStarlevel---用户星级服务token与mobile信息" + taskMobile.getTaskid(),
						"<xmp>" + json + "</xmp>");

				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				int radomInt = new Random().nextInt(999999);
				transactionId = "1000010017" + df.format(new Date()) + radomInt;
				JSONObject jsonObj = JSONObject.fromObject(json);
				token = jsonObj.getString("token");
				mobile = jsonObj.getString("mobile");

			} catch (Exception e) {
				e.printStackTrace();
				tracerLog.addTag("SessionId---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}

			try {
				/**
				 * 通过 transactionId , token 与 mobile 获取签名
				 */
				String urlData = "http://xjfw.189.cn/tykf-itr-services/services/dispatch.jsp?&dispatchUrl=ClientUni/clientuni/services/user/getSign?reqParam={\"transactionId\":\""
						+ transactionId + "\",\"channelCode\":\"H5002018\",\"token\":\"" + token + "\",\"type\":2}";
				Page page = getPage(webClient, taskMobile, urlData, HttpMethod.POST);
				String json = page.getWebResponse().getContentAsString();

				tracerLog.addTag("getStarlevel---用户星级服务签名信息" + taskMobile.getTaskid(),
						"<xmp>" + json + "</xmp>");

				JSONObject jsonObj2 = JSONObject.fromObject(json);
				sign = jsonObj2.getString("sign");

			} catch (Exception e) {
				e.printStackTrace();
				tracerLog.addTag("sign---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			}

			String prvince = URLEncoder.encode(URLEncoder.encode(taskMobile.getProvince().trim(),"utf8"),"utf8");
			String urlStarlevel = "http://xjfw.189.cn/tykf-itr-services/services/dispatch.jsp?&dispatchUrl=ClientUni/clientuni/services/starLevel/custStarLevelQuery?reqParam={\"transactionId\":\""
					+ transactionId + "\",\"clientNbr\":\"" + mobile
					+ "\",\"channelCode\":\"H5002018\",\"deviceType\":\"1\",\"prvince\":\"+"+prvince+"\",\"sign\":\""
					+ sign + "\"}";

			Page page = getPage(webClient, taskMobile, urlStarlevel, null);

			if (null != page) {
				String html = page.getWebResponse().getContentAsString();
				tracerLog.addTag("TelecomShanxiParser.getStarlevel---用户星级服务信息" + taskMobile.getTaskid(),
						"<xmp>" + html + "</xmp>");
				WebParamTelecom<TelecomStarlevel> webParam  = TelecomParseCommon.starlevel_Parser(html, taskMobile);
				if(webParam.getErrormessage()!=null){
					if(k<3){
						k++;
						System.out.println("============================第"+k+"次请求==============");
						webParam  = getStarlevel(taskMobile, k);
					}
				}
				webParam.setPage(page);
				webParam.setHtml(html);
				webParam.setUrl(page.getUrl().toString());
				webParam.setCode(page.getWebResponse().getStatusCode());
				return webParam;
			}
		} catch (Exception e) {
			tracerLog.addTag("TelecomShanxiParser.getStarlevel---ERROR:", taskMobile.getTaskid() + "---ERROR:" + e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过url获取 Page
	 * 
	 * @param taskMobile
	 * @param url
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public Page getPage(WebClient webClient, TaskMobile taskMobile, String url, HttpMethod type) throws Exception {
		tracerLog.addTag("TelecomShanxiParser.getPage---url:", url + "taskId:" + taskMobile.getTaskid());
		webClient.getOptions().setTimeout(20000); // 15->60
		WebRequest webRequest = new WebRequest(new URL(url), null != type ? type : HttpMethod.GET);
		Page searchPage = webClient.getPage(webRequest);
		int statusCode = searchPage.getWebResponse().getStatusCode();
		tracerLog.addTag("TelecomShanxiParser.getPage.statusCode:" + statusCode, "---taskid:" + taskMobile.getTaskid());

		if (200 == statusCode) {
			String html = searchPage.getWebResponse().getContentAsString();
			tracerLog.addTag("TelecomShanxiParser.getPage---taskid:",
					taskMobile.getTaskid() + "---url:" + url + "<xmp>" + html + "</xmp>");
			return searchPage;
		}

		return null;
	}

}
