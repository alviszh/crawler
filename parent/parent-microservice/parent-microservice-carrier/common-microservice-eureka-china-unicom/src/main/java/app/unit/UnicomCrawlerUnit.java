package app.unit;

import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

public class UnicomCrawlerUnit {

	public static TaskMobile istrue(MessageLogin messageLogin, TaskMobile taskMobile) {
		/**
		 * 取得字符串的字节长度
		 */
		String pwd = messageLogin.getPassword();
		String userName = messageLogin.getName();
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
	public static  boolean increasing(int[] arr, int index) {
		if (arr.length <= index) {
			return true;
		}

		if (index == 0) {
			return increasing(arr, index + 1);
		}

		return arr[index - 1] <= arr[index] && increasing(arr, index + 1);
	}

	// 递减方法
	public static  boolean Diminishing(int[] arr, int index) {
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
}
