package app.service.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;

@Component
@Service
public class LoginAndGetAnHuiTongYiCommonService extends HousingBasicService{

	private String baseUrl = "https://sso.ahzwfw.gov.cn/uccp-server/login";
	private WebClient webClient = null;
	
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
		webClient = WebCrawler.getInstance().getNewWebClient();
		Page htmlPage = getHtml(baseUrl, webClient);

		Document doc = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());

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
		paramsList.add(new NameValuePair("username", messageLoginForHousing.getNum().trim()));
		paramsList.add(new NameValuePair("password", messageLoginForHousing.getPassword().trim()));
		paramsList.add(new NameValuePair("random", ""));

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		webRequest.setRequestParameters(paramsList);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage.getWebResponse().getContentAsString().indexOf("用户名或密码不正确") != -1) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
			taskHousing.setError_message("用户名或密码不正确，请重新输入！未注册账号，请前往网站认证，忘记密码，前往网站找回密码");
			taskHousing.setDescription("用户名或密码不正确，请重新输入！未注册账号，请前往网站认证，忘记密码，前往网站找回密码");
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getError_code());
			return taskHousing;
		} else {
			String cookies = CommonUnit.transcookieToJson(webClient);
			taskHousing.setCookies(cookies);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
			return taskHousing;
		}

	}
	
	
//	/**   
//	  *    
//	  * 项目名称：common-microservice-housingfund-sz-anhui-tongyi  
//	  * 所属包名：app.service.common
//	  * 类描述：   登录方法 返回Page 对象  用于自动以登录
//	  * 创建人：hyx 
//	  * 创建时间：2018年5月7日 
//	  * @version 1  
//	  * 返回值    Page
//	  */
//	public Page login2(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception {
//		webClient = WebCrawler.getInstance().getNewWebClient();
//		
//	
//		Page htmlPage = getHtml(baseUrl, webClient);
//		
//		Document doc = Jsoup.parse(htmlPage.getWebResponse().getContentAsString());
//
//		String lt = doc.select("[name=lt]").attr("value");
//		String execution = doc.select("[name=execution]").attr("value");
//		String _eventId = doc.select("[name=_eventId]").attr("value");
//		String loginType = doc.select("[name=loginType]").attr("value");
//		String credentialType = doc.select("[name=credentialType]").attr("value");
//
//		String platform = doc.select("[name=platform]").attr("value");
//		// String ukeyType = doc.select("[name=ukeyType]").attr("value");
//		// String ahcaukey = doc.select("[name=ahcaukey]").attr("value");
//		// String ahcasign = doc.select("[name=ahcasign]").attr("value");
//		String userType = doc.select("[name=userType]").attr("value");
//
//		String url = "https://sso.ahzwfw.gov.cn/uccp-server/login";
//		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
//		paramsList = new ArrayList<NameValuePair>();
//		paramsList.add(new NameValuePair("lt", lt));
//		paramsList.add(new NameValuePair("execution", execution));
//		paramsList.add(new NameValuePair("_eventId", _eventId));
//		paramsList.add(new NameValuePair("platform", platform));
//		paramsList.add(new NameValuePair("loginType", loginType));
//		paramsList.add(new NameValuePair("credentialType", credentialType));
//		paramsList.add(new NameValuePair("userType", userType));
//		paramsList.add(new NameValuePair("username", messageLoginForHousing.getNum().trim()));
//		paramsList.add(new NameValuePair("password", messageLoginForHousing.getPassword().trim()));
//		paramsList.add(new NameValuePair("random", ""));
//
//		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
//		webRequest.setRequestParameters(paramsList);
//
//		webClient.setJavaScriptTimeout(500000);
//		Page searchPage = webClient.getPage(webRequest);
//		
//		
//		return searchPage;
//
//	}

	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);

		webClient.setJavaScriptTimeout(500000);
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;

	}

}
