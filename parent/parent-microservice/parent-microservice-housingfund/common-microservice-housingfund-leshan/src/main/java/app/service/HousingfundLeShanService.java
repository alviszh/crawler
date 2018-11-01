package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.commontracerlog.TracerLog;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;
import app.service.common.aop.ISms;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.leshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.leshan")
public class HousingfundLeShanService extends HousingBasicService implements ICrawlerLogin,ISms{

	@Autowired
	private TracerLog tracer;

	@Autowired
	public TaskHousingRepository taskHousingRepository;

	@Autowired
	private HousingfundLeShanUnitService housingfundLeShanUnitService;

	WebParamHousing webParamHousing ;
	@Override
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {
			webParamHousing = new WebParamHousing();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String url = "http://wangt.lszfgjj.gov.cn:7009/netface/login.do";
			HtmlPage page2 = getHtml(url, webClient);
			HtmlPage page = page2.getElementById("gr").click();
			String html = page.getWebResponse().getContentAsString();
			System.out.println(html);
			HtmlTextInput username = (HtmlTextInput) page.getElementById("username");
			HtmlPasswordInput password = (HtmlPasswordInput) page.getElementById("password");
			HtmlButton obtainBtn = (HtmlButton) page.getElementById("obtainBtn");
			username.setText(messageLoginForHousing.getNum());
			password.setText(messageLoginForHousing.getPassword());
			//			webClient.addRequestHeader("Accept", "application/json, text/javascript, */*; q=0.01");
			//			webClient.addRequestHeader("Host", "wangt.lszfgjj.gov.cn:7009");
			//			webClient.addRequestHeader("Origin", "http://wangt.lszfgjj.gov.cn:7009");
			//			webClient.addRequestHeader("Referer", "http://wangt.lszfgjj.gov.cn:7009/netface/login.do");
			//			webClient.addRequestHeader("X-Requested-With", "XMLHttpRequest");
			//			String url2 = "http://wangt.lszfgjj.gov.cn:7009/netface/reg!getDynCode.do?"
			//					+ "dto%5B%27idCard%27%5D="+messageLoginForHousing.getNum()
			//					+ "&dto%5B%27aType%27%5D=4";
			//			Page gethtmlPost = gethtmlPost(webClient, null, url2);
			//			String contentAsString = gethtmlPost.getWebResponse().getContentAsString();
			//			String trim = JSONObject.fromObject(contentAsString).getString("msg").trim();
			//			System.out.println(trim);
			HtmlPage click = obtainBtn.click();
			//String contentAsString = click.getWebResponse().getContentAsString();
			if(click.asText().contains("编号不能为空")){
				System.out.println("编号不能为空");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription("账号不能为空");
				taskHousing.setPassword(messageLoginForHousing.getPassword());
				taskHousingRepository.save(taskHousing);
			}else if(click.asText().contains("个人基本信息不存在!")){
				System.out.println("个人基本信息不存在!");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhasestatus());
				taskHousing.setDescription("查询不到该帐号信息，请确定是否输入正确。");
				taskHousing.setPassword(messageLoginForHousing.getPassword());
				taskHousingRepository.save(taskHousing);
			}else{
				System.out.println("验证码已发往您的手机,请注意查收");
				webParamHousing.setPage(page);
				webParamHousing.setWebClient(webClient);
				Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies();
				String cookie = CommonUnit.transcookieToJson2(cookies);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getDescription());
				//taskHousing.setCookies(cookie);
				taskHousing.setPassword(messageLoginForHousing.getPassword());
				taskHousingRepository.save(taskHousing);
			}
			//			}else{
			//				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR1.getPhase());
			//				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR1.getPhasestatus());
			//				taskHousing.setDescription(trim);
			//				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR1.getError_code());
			//				save(taskHousing);
			//			}

		} catch (Exception e) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getError_code());
			taskHousing.setError_message("发送短信验证码网页发送变化");
			save(taskHousing);
		}
		
		return taskHousing;
	}

	@Override
	public TaskHousing verifySms(MessageLoginForHousing messageLogin) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLogin.getTask_id());
		try {
			HtmlPage page = (HtmlPage) webParamHousing.getPage();
			WebClient webClient = webParamHousing.getWebClient();
			HtmlTextInput dynCode = (HtmlTextInput)page.getElementById("dynCode");
			dynCode.setText(messageLogin.getSms_code());
			HtmlAnchor loginBtn = page.getFirstByXPath("//a[@class='loginBtn']");
			Page click = loginBtn.click();
			Thread.sleep(2000);
			String trim = click.getWebResponse().getContentAsString();
			System.out.println(trim);
			String url = "http://wangt.lszfgjj.gov.cn:7009/netface/index!nav.do?menuid=114637";
			Page html = getHtml(url, webClient);
			String contentAsString = html.getWebResponse().getContentAsString();
			System.out.println(contentAsString);
			if(contentAsString.indexOf("乐山市住房公积金管理中心-网上业务大厅单位版")!=-1){
				System.out.println("登录，验证短信成功");
				Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies();
				String cookie = CommonUnit.transcookieToJson2(cookies);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setCookies(cookie);
				taskHousingRepository.save(taskHousing);
			}else{
				System.out.println("登录失败");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
				taskHousing.setDescription("对不起，您的短信验证码或密码输入错误，请重试！");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
				save(taskHousing);
			}
		} catch (Exception e) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
			save(taskHousing);
		}
		return taskHousing;

	}

	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public static Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}

	@Override
	public TaskHousing getAllData(MessageLoginForHousing messageLogin) {
		TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLogin.getTask_id());

		housingfundLeShanUnitService.Userinfo(messageLogin,taskHousing);

		housingfundLeShanUnitService.PayStatus(messageLogin,taskHousing);
		
		updateTaskHousing(taskHousing.getTaskid());
		TaskHousing taskHousing2 = taskHousingRepository.findByTaskid(messageLogin.getTask_id());
		return taskHousing2;
	}



	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TaskHousing sendSms(MessageLoginForHousing messageLoginForHousing) {
		//TaskHousing taskHousing = taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		return null;
	}





}
