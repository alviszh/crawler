package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.tangshan.HousingTangShanPayRepository;

import app.bean.WebParamHousing;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.tangshan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.tangshan")
public class LoginAndGetService {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private HousingTangShanPayRepository housingTangShanUnitRepository;
	@Autowired
	public TaskHousingRepository taskHousingRepository;
	@Value("${loginip}") 
	public String loginip;
	WebParamHousing webParamHousing ;
	public WebParamHousing login(WebClient webClient,MessageLoginForHousing messageLogin, TaskHousing taskHousing) throws Exception {
		String url = "http://"+loginip+"/olbh/index";
		webParamHousing = new WebParamHousing();
		HtmlPage page = (HtmlPage)LoginAndGetUnit.getHtml(url, webClient);
		/**
		 * 获取参数
		 */
		String contentAsString = page.getWebResponse().getContentAsString();
		Elements elementsByTag = Jsoup.parse(contentAsString).getElementsByTag("head").get(0).getElementsByTag("script");
		String string = elementsByTag.get(0).toString();
		String splitData = splitData(string,"var csrfVal = \"","\";");
		System.out.println(splitData);//参数
		/**
		 * 发送短信
		 */
		HtmlTextInput idNumber = (HtmlTextInput)page.getFirstByXPath("//input[@id='idNumber']");//身份证号
		HtmlTextInput cardno = (HtmlTextInput)page.getFirstByXPath("//input[@id='cardno']");//联名卡号
		HtmlPasswordInput password =(HtmlPasswordInput) page.getFirstByXPath("//input[@id='password']");//密码
		HtmlTextInput verifyCode = (HtmlTextInput)page.getFirstByXPath("//input[@id='verifyCode']");//验证码
		HtmlImage image = (HtmlImage) page.getFirstByXPath("//img[@id='person_pic']");//图片验证码
		String img = chaoJiYingOcrService.getVerifycode(image, "1902");//图片
		idNumber.setText(messageLogin.getNum());
		cardno.setText(messageLogin.getHosingFundNumber());
		password.setText(messageLogin.getPassword());
		verifyCode.setText(img);
		HtmlButton sendBtnText = (HtmlButton) page.getElementById("sendBtnText");//发送短信
		HtmlPage page4 = sendBtnText.click();
		webParamHousing.setWebClient(webClient);
		//发送完毕
		String error = "";
		if(page4.asText().contains("请输入合法的证件号码")){
			error = "请输入合法的证件号码";
		}else if(page4.asText().contains("请输入验证码")){
			error = "请输入验证码";
		}else if(page4.asText().contains("验证码输入错误")){
			error = "服务繁忙，请重试";
			taskHousing.setError_message("验证码输入错误");
		}else if(page4.asText().contains("[卡号]不能为空！")){
			error = "[卡号]不能为空！";
		}else if(page4.asText().contains("[密码]不能为空！")){
			error = "[密码]不能为空！";
		}else if(page4.asText().contains("证件号码、卡号、密码不匹配！")){
			error = "证件号码、卡号、密码不匹配！";
		}else if(page4.asText().contains("[密码]不能为空！")){
			error = "[密码]不能为空！";
		}else if(page4.asText().contains("Session invalid")){
			error = "Session invalid";
		}else if(page4.asText().contains("请输入联名卡号")){
			error = "请输入联名卡号";
		}else if(page4.asText().contains("今日短信发送次数已达到5次，请明天再试！")){
			error = "今日短信发送次数已达到5次，请明天再试！";
		}
		System.out.println(error);
		if(error.length()>0){
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getPhasestatus());
			taskHousing.setDescription(error);
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_THREE.getError_code());
			save(taskHousing);
			return null;
		}
		if(page4.asText().contains("验证码已发送到末位号码")){
			System.out.println("成功");
			Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies();
			String cookie = CommonUnit.transcookieToJson2(cookies);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getDescription());
			taskHousing.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
			taskHousing.setError_message(splitData);
			taskHousing.setCookies(cookie);
			taskHousingRepository.save(taskHousing);
			webParamHousing.setPage(page4);

		}else{
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_ERROR.getError_code());
			save(taskHousing);
		}

		return webParamHousing;
	}

	public WebParamHousing setPhonecode(MessageLoginForHousing messageLogin, TaskHousing taskHousing){
		HtmlPage page = (HtmlPage) webParamHousing.getPage();
		WebClient webClient = webParamHousing.getWebClient();
		try {
			HtmlTextInput imgver = (HtmlTextInput)page.getFirstByXPath("//input[@name='dxyzm']");
			imgver.setText(messageLogin.getSms_code());
			HtmlButton submit = page.getFirstByXPath("//button[@class='btn btn-default btn_login']");
			HtmlPage page2 = submit.click();

			if(page2.asText().contains("账号或密码错误")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
				taskHousing.setDescription("账号或密码错误");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
				save(taskHousing);
				return null;
			}else if(page2.asText().contains("登录失败")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
				taskHousing.setDescription("登录失败");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
				save(taskHousing);
				return null;
			}else if(page2.asText().contains("验证码已经过期，请重新生成")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
				taskHousing.setDescription("验证码已经过期，请重新登录");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
				save(taskHousing);
				return null;
			}else if(page2.asText().contains("当前账号未注册")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
				taskHousing.setDescription("当前账号未注册");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
				save(taskHousing);
				return null;
			}else if(page2.asText().contains("请输入短信验证码")){
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
				taskHousing.setDescription("请输入短信验证码");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
				save(taskHousing);
				return null;
			}
			else{
				String url = "http://"+loginip+"/olbh/pub/home";
				Page page3 = LoginAndGetUnit.getHtml(url, webClient);
				String html = page3.getWebResponse().getContentAsString();
				if(html.indexOf("退    出")!=-1){
					Set<Cookie> cookies = page.getWebClient().getCookieManager().getCookies();
					String cookie = CommonUnit.transcookieToJson2(cookies);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_PASSWORD_SUCCESS.getDescription());
					taskHousing.setCookies(cookie);
					taskHousingRepository.save(taskHousing);
				}else{
					System.out.println("登录失败");
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getDescription());
					taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
					save(taskHousing);
					return null;
				}
			}

			return webParamHousing;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getDescription());
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR1.getError_code());
			save(taskHousing);
			return null;
		} 
	}

	public void save(TaskHousing taskHousing){
		taskHousingRepository.save(taskHousing);
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
	public static String splitData(String str, String strStart, String strEnd) {  
		int i = str.indexOf(strStart); 
		int j = str.indexOf(strEnd, i); 
		String tempStr=str.substring(i+strStart.length(), j); 
        return tempStr;  
	}
}
