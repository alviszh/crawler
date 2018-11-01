package app.controller;

import java.io.IOException;
import java.util.UUID;

import app.client.standalone.StandaloneTaskClient;
import com.crawler.callback.json.OwnerConfig;
import com.crawler.pbccrc.json.TaskStandalone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.domain.json.Result;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.ReportData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.netflix.discovery.EurekaClient;

import app.bean.MessageResult;
import app.client.PbccrcClient;
import app.commontracerlog.TracerLog;

@Controller
@RequestMapping("/h5")
public class LoginController {

	@Value("${spring.profiles.active}")
	String active;

	public static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private PbccrcClient pbccrcClient;

	@Value("${spring.appName}")
	String appName;
	@Autowired
	private EurekaClient eurekaClient;
	@Autowired
	private StandaloneTaskClient standaloneTaskClient;

	// public Application getApplicationByName(String appName){
	// Application app = eurekaClient.getApplication(appName);
	// return app;
	// }

	/**
	 * 获取人行征信报告
	 * @param pbccrcJsonBean
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/pbccrc/loginv1", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody TaskStandalone loginAndGetcreditV1(PbccrcJsonBean pbccrcJsonBean) throws IOException {
		// Application app =getApplicationByName(appName);
		// List<InstanceInfo> instances = app.getInstances();
		System.out.println("pbccrcJsonBean:" + pbccrcJsonBean);
		long startTime = System.currentTimeMillis();
		// 设置任务id
		String mappingId = UUID.randomUUID().toString(); // 唯一标识
		pbccrcJsonBean.setMapping_id(mappingId);
		System.out.println("mappingId="+mappingId);

		//创建task
		pbccrcJsonBean.setServiceName("pbccrc"); //服务名称（人行征信）
		TaskStandalone taskStandalone = standaloneTaskClient.createTask(pbccrcJsonBean);

		String owner = pbccrcJsonBean.getOwner();
		tracerLog.qryKeyValue("mappingId", pbccrcJsonBean.getMapping_id());
		tracerLog.qryKeyValue("taskid", taskStandalone.getTaskid());
		tracerLog.qryKeyValue("owner", owner);
		tracerLog.qryKeyValue("key", pbccrcJsonBean.getKey());
		tracerLog.qryKeyValue("username", pbccrcJsonBean.getUsername());// 账号
		tracerLog.qryKeyValue("password", pbccrcJsonBean.getPassword());// 密码
		tracerLog.qryKeyValue("tradecode", pbccrcJsonBean.getTradecode());// 授权码

		tracerLog.qryKeyValue("LoginController.loginAndGetcreditV1.creditParam",
				"开始获取报告，creditParam=" + pbccrcJsonBean);
		log.info("------------获取报告信息loginv1----------------" + pbccrcJsonBean);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		MessageResult messageResult = new MessageResult();

		String reportDataResultStr = "";
		try {
			String result = pbccrcClient.loginAndGetcreditV(pbccrcJsonBean);
			tracerLog.addTag("loginAndGetcreditV.taskStandalone", result);
			if (result != null) {
				taskStandalone = gson.fromJson(result,
						new TypeToken<TaskStandalone>() {
						}.getType());
			}
		} catch (Exception e) {
			System.out.println("Exception" + e.toString());
			tracerLog.qryKeyValue("Exception", e.toString());
		}

		if (reportDataResultStr != null && !reportDataResultStr.equals("")) {
			Result<ReportData> reportDataResult = gson.fromJson(reportDataResultStr,
					new TypeToken<Result<ReportData>>() {
					}.getType());
			if (reportDataResult != null) {
				ReportData reportData = reportDataResult.getData();
				System.out.println("获取报告结果，reportDataResult=" + reportDataResult);
				String statusCode = reportData.getStatusCode();
				String message = reportData.getMessage();
				tracerLog.qryKeyValue("statusCode", statusCode);
				tracerLog.qryKeyValue("message", message);
				if (message != null && message.contains("查询成功")) {
					String html = reportDataResult.getHtml();
					String reportId = reportData.getReport().getReportBase().getReportId();// 报告编号
					String realName = reportData.getReport().getReportBase().getRealname();// 姓名
					String certificateNum = reportData.getReport().getReportBase().getCertificateNum();// 身份证号码
					// tracerLog.qryKeyValue("html",html); //保存报告结果中的html
					tracerLog.qryKeyValue("reportId", reportId);
					tracerLog.qryKeyValue("realName", realName);
					tracerLog.qryKeyValue("certificateNum", certificateNum);
				}
				messageResult.setStatusCode(reportData.getStatusCode());
				messageResult.setMessage(message);
			} else {
				messageResult.setMessage("报告获取失败！");
				tracerLog.qryKeyValue("message", "报告获取失败");
			}
		}
		tracerLog.qryKeyValue("LoginController.getCredit.taskStandalone",
				taskStandalone + "，key=" + pbccrcJsonBean.getKey());

		long endTime = System.currentTimeMillis();
		tracerLog.qryKeyValue("耗时", (endTime - startTime) + ":ms");

		return taskStandalone;
	}

	/**
	 * 获取人行征信报告-不对接微信公众号 （对接汇金）
	 * 
	 * @param username
	 * @param password
	 * @param tradecode
	 * @param html
	 * @return
	 */
	@RequestMapping(value = "/pbccrc/v1/getCredit", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String login(@RequestParam("username") final String username,
			@RequestParam("password") final String password, @RequestParam("tradecode") String tradecode,
			@RequestParam(name = "html", required = false, defaultValue = "false") boolean html) {
		log.info("------------huijing /pbccrc/v1/getCredit  start----------------" + username);
		tracerLog.qryKeyValue("username", username);// 账号
		tracerLog.qryKeyValue("password", password);// 密码
		tracerLog.qryKeyValue("tradecode", tradecode);// 授权码
		tracerLog.qryKeyValue("是否需要原始html", html + "");// 是否需要原始
		tracerLog.qryKeyValue("LoginController.loginAndGetcreditV1外调",
				"开始获取报告，username=" + username + ",password=" + password + ",tradecode =" + tradecode);

		PbccrcJsonBean pbccrcJsonBean = new PbccrcJsonBean();
		pbccrcJsonBean.setUsername(username);
		pbccrcJsonBean.setPassword(password);
		pbccrcJsonBean.setTradecode(tradecode);
		pbccrcJsonBean.setHtml(html);
		pbccrcJsonBean.setOwner(OwnerConfig.HUIJIN);
		tracerLog.qryKeyValue("owner", pbccrcJsonBean.getOwner());

		// 设置任务id
		String mappingId = UUID.randomUUID().toString(); // 唯一标识
		pbccrcJsonBean.setMapping_id(mappingId);
		tracerLog.qryKeyValue("mappingId", pbccrcJsonBean.getMapping_id());

		//创建task
		pbccrcJsonBean.setServiceName("pbccrc"); //服务名称（人行征信）
		TaskStandalone taskStandalone = standaloneTaskClient.createTask(pbccrcJsonBean);
		tracerLog.addTag("taskStandalone创建完成",taskStandalone.toString());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		log.info("------------huijing   pbccrcClient.loginAndGetcreditV ----------------" + username);
		String result = pbccrcClient.loginAndGetcreditV(pbccrcJsonBean);

		tracerLog.qryKeyValue("获取征信报告结果，result=" , result);
//		log.info("------------获取征信报告结果信息----------------" + result);

		String message = null;
		Result<ReportData> reportDataResult = gson.fromJson(result, new TypeToken<Result<ReportData>>() {
		}.getType());
		if (reportDataResult != null) {
			ReportData reportData = reportDataResult.getData();
			if (reportData != null && !reportData.equals("")) {
				System.out.println("===============reportData="+reportData);
				message = reportData.getMessage();
				String statusCode = reportData.getStatusCode();
				tracerLog.qryKeyValue("statusCode", statusCode);
				tracerLog.qryKeyValue("message", message);
				if (message != null && message.contains("查询成功")) {
					String html1 = reportDataResult.getHtml();
					String reportId = reportDataResult.getData().getReport().getReportBase().getReportId();// 报告编号
					String realName = reportDataResult.getData().getReport().getReportBase().getRealname();// 姓名
					String certificateNum = reportDataResult.getData().getReport().getReportBase().getCertificateNum();// 身份证号码
					// tracerLog.qryKeyValue("html",html1); //保存报告结果中的html
					tracerLog.qryKeyValue("reportId", reportId);
					tracerLog.qryKeyValue("realName", realName);
					tracerLog.qryKeyValue("certificateNum", certificateNum);
				}
			} else {
				tracerLog.qryKeyValue("message", "报告获取失败");
			}
		}
		return result;
	}

	/**
	 * 进入人行征信登录页面
	 * 
	 * @param model
	 * @param themeColor
	 * @param isTopHide
	 * @param key
	 * @param redirectUrl
	 * @param owner
	 * @return
	 */
	@RequestMapping(value = "/pbccrc", method = { RequestMethod.GET, RequestMethod.POST })
	public String auth(Model model,
			@RequestParam(name = "themeColor", required = false, defaultValue = "5bc0de") String themeColor,
			@RequestParam(name = "isTopHide", required = false, defaultValue = "false") boolean isTopHide,
			@RequestParam(name = "key") String key,
			@RequestParam(name = "redirectUrl", required = false) String redirectUrl,
			@RequestParam(name = "owner") String owner) {
		tracerLog.qryKeyValue("key", key);
		tracerLog.qryKeyValue("LoginController.auth", "key=" + key + ",redirectUrl=" + redirectUrl + ",owner=" + owner);
		log.info("------------人行征信的登录页面----------------key=" + key);
		model.addAttribute("themeColor", themeColor);
		String topHide = "block";
		if (isTopHide) {
			topHide = "none";
		}
		model.addAttribute("topHide", topHide);
		model.addAttribute("key", key);
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("owner", owner);
		model.addAttribute("appActive", active);
		log.info("*********** themeColor=" + themeColor + ", isTopHide=" + isTopHide + ",topHide=" + topHide
				+ ", owner=" + owner);
		return "login";
	}
}
