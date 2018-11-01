package app.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.crawler.bank.json.BankStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.module.htmlunit.WebCrawler;

import app.common.CommonUnitForSpdb;
import app.commontracerlog.TracerLog;

@Component
public class SpdbChinaserviceLogin {
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private TaskBankStatusService taskBankStatusService; 
	@Autowired
	private CommonUnitForSpdb commonUnitForSpdb;
	@Autowired
	private TracerLog tracerLog;
	@Retryable(value={RuntimeException.class},maxAttempts=3,backoff = @Backoff(delay = 1500l,multiplier = 1.5))
	//maxAttempts表示重试次数，multiplier即指定延迟倍数，比如delay=5000l,multiplier=2,则第一次重试为5秒，第二次为10秒，第三次为20秒
	//backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为1000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为1秒，第二次为1.5秒，第三次为2.25秒。
	public String login(BankJsonBean bankJsonBean ,String  idTpyeValue) throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		// 图片请求
		String loginurl = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/toLogin.action";
		WebRequest webRequest1 = new WebRequest(new URL(loginurl), HttpMethod.GET);
		HtmlPage html1 = webClient.getPage(webRequest1);
		
		String codeurl = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/validateCode";
		WebRequest webRequest = new WebRequest(new URL(codeurl), HttpMethod.GET);
		Page html = webClient.getPage(webRequest);
		String path = commonUnitForSpdb.getPathBySystem();
		String imgagePath=commonUnitForSpdb.getImagePath(html,path);
		System.out.println(imgagePath);
		String verifycode = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "1004"); 
		System.out.println(verifycode);

		String urlLoginSms = "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/sendLoginSms.action";
		WebRequest webRequest2 = new WebRequest(new URL(urlLoginSms), HttpMethod.POST);
		webRequest2.setAdditionalHeader("Accept", "*/*");
		webRequest2.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest2.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
		webRequest2.setAdditionalHeader("Connection", "keep-alive");
		webRequest2.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		webRequest2.setAdditionalHeader("Host", "ebill.spdbccc.com.cn");
		webRequest2.setAdditionalHeader("Origin", "https://ebill.spdbccc.com.cn");
		webRequest2.setAdditionalHeader("Referer", "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/toLogin.action");
		webRequest2.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		webRequest2.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		String requestBody = "idNo="+bankJsonBean.getLoginName()+"&idType="+idTpyeValue+"&yzmM="+verifycode+"";
		webRequest2.setRequestBody(requestBody);
		Page html3 = webClient.getPage(webRequest2);
		String html2 = html3.getWebResponse().getContentAsString();
		System.out.println(html2);
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		if(html2.contains("yzmerror")){
			tracerLog.addTag(verifycode, "图片验证码错误，触发retry机制");
			throw new RuntimeException("请输入正确的附加码");
		}else if(html2.contains("true")){
			System.out.println("短信发送成功");
			tracerLog.addTag("短信发送成功", html2);
//			taskBankRepository.save(taskBank);
			taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhase(), 
					BankStatusCode.BANK_WAIT_CODE_SUCCESS.getPhasestatus(),
					"短信验证码已发送到您的手机，请注意查收", 
					BankStatusCode.BANK_WAIT_CODE_SUCCESS.getError_code(),false, bankJsonBean.getTaskid());
			String cookieString = CommonUnit.transcookieToJson(webClient);
			System.out.println("cookieString"+cookieString);
			taskBank.setCookies(cookieString);
			taskBank.setParam(verifycode);
			taskBankRepository.save(taskBank);
			return html2;
//			System.out.print("输入");
//			  Scanner scan = new Scanner(System.in);
//			  String read = scan.nextLine();
//			  System.out.println("输入数据："+read); 
			
			
		}else if(html2.contains("yzm60s")){
			taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(), 
					BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					"短信验证码发送失败,短信验证码60秒内不允许重复发送！",
					BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(),false, bankJsonBean.getTaskid());
			tracerLog.addTag("短信验证码发送失败,短信验证码60秒内不允许重复发送！", html2);
			System.out.println("短信验证码发送失败,短信验证码60秒内不允许重复发送！");
			return html2;
		}else if(html2.contains("false")){
			tracerLog.addTag("短信验证码发送失败,请检查证件号码输入是否错误！", html2);
			taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(), 
					BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					"短信验证码发送失败,请检查证件号码输入是否错误！",
					BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(),false, bankJsonBean.getTaskid());
			System.out.println("短信验证码发送失败,请检查证件号码输入是否错误！");
			return html2;
		}else{
			tracerLog.addTag("短信发送失败", html2);
			taskBank=taskBankStatusService.changeStatus(BankStatusCode.BANK_SEND_CODE_ERROR.getPhase(), 
					BankStatusCode.BANK_SEND_CODE_ERROR.getPhasestatus(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getDescription(),
					BankStatusCode.BANK_SEND_CODE_ERROR.getError_code(),false, bankJsonBean.getTaskid());
			System.out.println("短信发送失败");
			return html2;
		}
		
		
	}
//	@Async
//	public String saveCode(TaskBank taskBank,BankJsonBean bankJsonBean,String idTpyeValue) throws Exception{
//		
//		
//		return null;
//	}
	
	
}
