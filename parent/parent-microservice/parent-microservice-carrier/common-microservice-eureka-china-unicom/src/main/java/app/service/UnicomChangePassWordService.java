package app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.UnicomChangePasswordResult;
import app.commontracerlog.TracerLog;
import app.crawler.unicom.htmlunit.ChangePassword;
import app.unit.Root;

@Component
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.unicom")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.unicom")
public class UnicomChangePassWordService {

	public static final String KEY = "秘钥就是不告诉你，自己猜去吧哈哈";

	public static final Logger log = LoggerFactory.getLogger(UnicomService.class);

	@Autowired
	private TaskMobileRepository taskMobileRepository;

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private  ChangePassword changePassword;

	@Async
	public void passwordlongin(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		tracerLog.addTag("passwordlongin", taskMobile.getTaskid());

		log.info("=============输入账号步骤================");
		taskMobile = changpasswordlogin(result, taskMobile, 0);
		// 数据爬取状态存储
		save(taskMobile);

	}

	public TaskMobile changpasswordlogin(UnicomChangePasswordResult result, TaskMobile taskMobile, int i) {
		tracerLog.addTag("changpasswordlogin", taskMobile.getTaskid());

		String url = "https://uac.10010.com/cust/resetpwd/inputName";
		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getError_code());
		save(taskMobile);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		log.info("==================输入账号步骤1=====================");
		try {
			HtmlPage htmlpage = changePassword.getHtml(url, webClient);
			HtmlImage htmlimage = (HtmlImage) htmlpage.getElementById("verifyCodeImg");

			String valicodeStr = null;
			try {
				i++;
				valicodeStr = chaoJiYingOcrService.getVerifycode(htmlimage, "5000");
			} catch (Exception e) {
				tracerLog.addTag("passwordlongin 获取图片", e.getMessage());

				log.info("============获取图片报错===============");
				Thread.sleep(5000);

				if (i > 3) {
					return null;
				}
				return changpasswordlogin(result, taskMobile, i);
			}

			List<NameValuePair> paramsList = new ArrayList<>();

			url = "https://uac.10010.com/cust/reset_wocc/resetpassInputName";
			paramsList = new ArrayList<>();
			paramsList.add(new NameValuePair("resetpassBean.loginName", result.getUsrnum()));
			paramsList.add(new NameValuePair("verifyCode", valicodeStr));
			paramsList.add(new NameValuePair("resetpassBean.userType", "00"));
			paramsList.add(new NameValuePair("resetpassBean.cityCode", ""));
			Page javaScriptPage = changePassword.gethtmlPost(webClient, paramsList, url);

			log.info("========================" + javaScriptPage.getWebResponse().getContentAsString());
			Root root = new Gson().fromJson(javaScriptPage.getWebResponse().getContentAsString(), new TypeToken<Root>() {
			}.getType());
			String nexturl = "https://uac.10010.com/cust/resetpwd/checkMsg?sec=" + root.getSecState();
			if (htmlimage.asXml().indexOf("24小时内发送5次信息") != -1) {
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR2.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR2.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR2.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR2.getError_code());
				taskMobile.setError_message(null);
				return taskMobile;
			}
			if (javaScriptPage.getWebResponse().getContentAsString().indexOf("验证码错误") != -1) {
				Thread.sleep(5000);
				tracerLog.addTag("passwordlongin 验证码","验证码错误");

				log.info("============验证码错误===============");
				if (i < 3) {
					taskMobile = changpasswordlogin(result, taskMobile, i);
				} else {
					String cookieString = CommonUnit.transcookieToJson(webClient);
					taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getPhase());
					taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getPhasestatus());
					taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getDescription());
					taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getError_code());
					taskMobile.setError_message(null);
					taskMobile.setCookies(cookieString);
					return taskMobile;
				}

			}
			if(javaScriptPage.getWebResponse().getContentAsString().indexOf("24小时内输错3次随机码") != -1){
				Thread.sleep(5000);
				tracerLog.addTag("passwordlongin","24小时内输错3次随机码");

				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR5.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR5.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR5.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR5.getError_code());
				taskMobile.setError_message(null);
				taskMobile.setCookies(cookieString);
				return taskMobile;

			}
			if (javaScriptPage.getWebResponse().getContentAsString().indexOf("系统繁忙") != -1) {
				log.info("============系统繁忙===============");
				tracerLog.addTag("passwordlongin","系统繁忙");

				Thread.sleep(5000);

				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR4.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR4.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR4.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR4.getError_code());
				taskMobile.setError_message(null);
				taskMobile.setCookies(cookieString);
				return taskMobile;

			}
			if (javaScriptPage.getWebResponse().getContentAsString().indexOf("操作成功") != -1) {
				tracerLog.addTag("passwordlongin","操作成功");

				String cookieString = CommonUnit.transcookieToJson(webClient);
				taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhase());
				taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getPhasestatus());
				taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getDescription());
				taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS1.getError_code());
				taskMobile.setError_message(null);
				taskMobile.setCookies(cookieString);
				taskMobile.setNexturl(nexturl);
				return taskMobile;
			}

			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getDescription());
			taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR3.getError_code());
			taskMobile.setError_message(null);
			return taskMobile;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhase());
			taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getPhasestatus());
			taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_ERROR.getDescription());

			taskMobile.setError_code(StatusCodeRec.MESSAGE_PASSWORD_ERROR.getCode());
			taskMobile.setError_message(StatusCodeRec.MESSAGE_PASSWORD_ERROR.getMessage());
			return taskMobile;
		}

	}

	@Async
	public void getPhoneCode(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		tracerLog.addTag("getPhoneCode", taskMobile.getTaskid());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhasestatus());
		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getError_code());
		taskMobile.setError_message(null);
		taskMobile = changePassword.getPhoneCode(result, taskMobile);
		// 获取验证码状态存储
		save(taskMobile);
	}

	@Async
	public void setPhoneCode(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		
		tracerLog.addTag("setPhoneCode", taskMobile.getTaskid());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getPhasestatus());

		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getError_code());
		taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_DONING.getDescription());
		save(taskMobile);
		taskMobile = changePassword.setPhoneCode(result, taskMobile);
		// 身份验证状态存储
		save(taskMobile);
	}

	@Async
	public void passwordchange(UnicomChangePasswordResult result, TaskMobile taskMobile) {
		tracerLog.addTag("passwordchange", taskMobile.getTaskid());

		taskMobile.setPhase(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getPhase());
		taskMobile.setPhase_status(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getPhasestatus());

		taskMobile.setDescription(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getDescription());
		taskMobile.setError_code(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getError_code());
		taskMobile.setError_message(StatusCodeEnum.TASKMOBILE_PASSWORD_CHANGE_DONING.getDescription());
		save(taskMobile);
		taskMobile = changePassword.changpasswordoldToNew(result, taskMobile);
		save(taskMobile);
	}

	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	private void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
}