package app.crawler.unicom.htmlunit;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.module.htmlunit.WebCrawler;

import app.bean.UnicomChangePasswordResult;
import app.commontracerlog.TracerLog;
import app.service.LoginAndGetService;

@Component
@Service
public class ChangePassword {
	
	@Autowired
	private TracerLog tracerLog;


	public static final Logger log = LoggerFactory.getLogger(ChangePassword.class);
	
	static LoginAndGetService loginAndGetService = new LoginAndGetService();

	public  TaskMobile getPhoneCode(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		try {
			String url = taskMobile.getNexturl();
			log.info("nexturl:" + url);
			tracerLog.addTag("unicomcrawler changepassword", taskMobile.getTaskid());

			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = loginAndGetService.addcookie(webClient, taskMobile);
			HtmlPage htmlpage = loginAndGetService.getHtml(url, webClient);
			HtmlElement button = (HtmlElement) htmlpage.getFirstByXPath("//span[@id='accessCodeBtn']");
			htmlpage = button.click();

			String cookieString = CommonUnit.transcookieToJson(htmlpage.getWebClient());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getError_code());
			taskMobile.setError_message(null);
			taskMobile.setCookies(cookieString);

			return taskMobile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_ONE.getMessage());
			return taskMobile;
		}
	}

	public  TaskMobile setPhoneCode(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		tracerLog.addTag("unicomcrawler changepassword", "身份验证  添加码");

		taskMobile.setError_message(null);
		taskMobile.setError_code(null);
		taskMobile.setDescription(null);
		
		taskMobile = istrue(result, taskMobile);
		if(taskMobile.getDescription()!=null){
			taskMobile.setError_message(taskMobile.getDescription());
			tracerLog.addTag("unicomcrawler changepassword", "密码类别有问题");
			return taskMobile;
		}
		try {
			String url = taskMobile.getNexturl();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = loginAndGetService.addcookie(webClient, taskMobile);
			HtmlPage htmlpage = loginAndGetService.getHtml(url, webClient);

			HtmlTextInput certNuminput = (HtmlTextInput) htmlpage.getFirstByXPath("//input[@id='certNum']");
			HtmlTextInput verifycodeinput = (HtmlTextInput) htmlpage.getFirstByXPath("//input[@id='mobileMsg']");
			HtmlElement button = (HtmlElement) htmlpage
					.getFirstByXPath("//input[@class='btn-style ml68 next-btn02 mt20']");
			certNuminput.setText(result.getCertNum());
			verifycodeinput.setText(result.getMobileMsg());
			htmlpage = button.click();
			System.out.println(htmlpage.asXml());
			String cookieString = CommonUnit.transcookieToJson(htmlpage.getWebClient());
			if (htmlpage.asXml().indexOf("证件信息不匹配，请重新输入") != -1) {
				tracerLog.addTag("unicomcrawler changepassword", "证件信息不匹配，请重新输入");

				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR_ID.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
				taskMobile.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
				//taskMobile.setCookies(cookieString);
				return taskMobile;
			}
			if (htmlpage.asXml().indexOf("随机码错误,请重新输入") != -1) {
				tracerLog.addTag("unicomcrawler changepassword", "随机码错误,请重新输入");

				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
				taskMobile.setError_code(StatusCodeRec.MESSAGE_PASSWORD_ERROR2.getCode());
				taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription());
				//taskMobile.setCookies(cookieString);
				return taskMobile;
			}
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getError_code());
			taskMobile.setError_message(null);
			taskMobile.setNexturl(htmlpage.getUrl() + "");
			taskMobile.setCookies(cookieString);
			return taskMobile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;

		}

	}

	public  TaskMobile changpasswordoldToNew(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		log.info("==================重置密码=====================");
		tracerLog.addTag("unicomcrawler changepassword", "重置密码");

		try {
			String url = taskMobile.getNexturl();
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			webClient = loginAndGetService.addcookie(webClient, taskMobile);
			HtmlPage htmlpage = loginAndGetService.getHtml(url, webClient);
			HtmlPasswordInput newPasswordinput = htmlpage.getFirstByXPath("//input[@id='newPassword']");
			HtmlPasswordInput newPasswordRepeatinput = htmlpage.getFirstByXPath("//input[@id='newPasswordRepeat']");
			HtmlElement button = (HtmlElement) htmlpage.getFirstByXPath("//input[@id='resetBut']");
			newPasswordinput.setText(result.getNewpassword());
			newPasswordRepeatinput.setText(result.getNewpassword());
			htmlpage = button.click();
			System.out.println(htmlpage.asXml());
			if(htmlpage.asXml().indexOf("密码过于简单,请重新输入")!=-1){
				tracerLog.addTag("unicomcrawler changepassword", "密码过于简单,请重新输入");

				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR4.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR4.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR4.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR4.getError_code());
				taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR4.getDescription());
				return taskMobile;
			}
			String cookieString = CommonUnit.transcookieToJson(htmlpage.getWebClient());
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_SUCCESS.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_SUCCESS.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_SUCCESS.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_SUCCESS.getError_code());
			taskMobile.setError_message(null);
			taskMobile.setCookies(cookieString);
			return taskMobile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR.getError_code());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_PASSWORD_ERROR2.getMessage());
			return taskMobile;
		}
	}

	private  TaskMobile istrue(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		/**
		 * 取得字符串的字节长度
		 */
		String pwd = result.getNewpassword();
		String userName = result.getCertNum();
		if (pwd.length() > 0) {
			pwd = pwd.replace("/^\\s\\s*/", "").replace("/\\s\\s*$/", "");
		}

		if (userName.length() > 0) {
			userName = userName.replace("/^\\s\\s*/", "").replace("/\\s\\s*$/", "");
		}
		/** 有规律数字：ABABAB、ABCABC */
		String reg = "/^(?!(\\d)\\1+$)(\\d{2,3})\\2+$/";
		Pattern p1 = Pattern.compile(reg);
		if (p1.matcher(pwd).matches()) {
			
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhasestatus());

			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getError_code());
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getDescription());
			return taskMobile;
		}
		/** 全一样 */
		reg = "/^(\\d)\\1+$/";
		p1 = Pattern.compile(reg);
		if (p1.matcher(pwd).matches()) {
			
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR2.getDescription());

			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR2.getError_code());
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR2.getDescription());
			return taskMobile;
		}
		char[] pwds = pwd.toCharArray();
		int[] intArr = new int[pwds.length]; // 定义一个长度与上述的字符串数组长度相通的整型数组
		for (int a = 0; a < pwds.length; a++) {
			intArr[a] = Integer.valueOf(pwds[a]); // 然后遍历字符串数组，使用包装类Integer的valueOf方法将字符串转为整型
		}
		
		if( increasing(intArr, 0)){
			
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR3.getDescription());

			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR3.getError_code());
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR3.getDescription());
			return taskMobile;
		}
		
		if( Diminishing(intArr, 0)){
			
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR1.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR3.getDescription());

			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR3.getError_code());
			taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_ERROR3.getDescription());
			return taskMobile;
		}
		return taskMobile;
		
		
	}

	// 递增方法
	public  boolean increasing(int[] arr, int index) {
		if (arr.length <= index) {
			return true;
		}

		if (index == 0) {
			return increasing(arr, index + 1);
		}

		return arr[index - 1] <= arr[index] && increasing(arr, index + 1);
	}

	// 递减方法
	public  boolean Diminishing(int[] arr, int index) {
		if (arr.length <= index) {
			return true;
		}

		if (index == 0) {
			return Diminishing(arr, index + 1);
		}

		return arr[index - 1] >= arr[index] && Diminishing(arr, index + 1);
	}

	public  HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}

	public  Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) {

		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
			if (paramsList != null) {
				webRequest.setRequestParameters(paramsList);
			}
			Page searchPage = webClient.getPage(webRequest);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * public static void main(String[] args) { UnicomChangePasswordResult
	 * result = changpassword(); result.setUsrnum("18618135874");
	 * result.setNewpassword("096572"); result.setCertNum("023312"); result =
	 * changpassword(result); if (result != null) { JFrame f2 = new JFrame();
	 * f2.setSize(1000, 1000); f2.setTitle("验证码"); f2.setVisible(true); String
	 * valicodeStr = JOptionPane.showInputDialog("请输入验证码：");
	 * result.setMobileMsg(valicodeStr); result = changpasswordManId(result);
	 * 
	 * }
	 * 
	 * changpasswordoldToNew(result); }
	 */
}
