package app.service.common;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTelInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;

import app.commontracerlog.TracerLog;
import app.domain.TempBean;
import app.service.ChaoJiYingOcrService;
import net.sf.json.JSONObject;
/**
 * 江西二次登录的登录按钮可以用验证图片验证码和短信服务密码的方式来代替
 * @author sln
 *
 */
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.jiangxi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.jiangxi")
public class LoginGetAndSetService{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private  RSAUtils rsa;
	@Autowired 
	private LoginAndGetCommonService loginAndGetCommonService;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	
	@Value("${exponent}") 
	public String exponent;
	
	@Value("${modulus}") 
	public String modulus;
	
	@Value("${filesavepath}") 
	public String fileSavePath;
	
	private static Integer captchaErrorCount=0;   //验证码识别错误次数计数器
	
	//===========================二次登录时需要的短信验证码发送和接收接口  start======================================
	//
	/**
	 * 江西电信二次登录
	 * @param mobileLogin 
	 * @param mobileLogin
	 * @param taskMobile 
	 * @return
	 * @throws Exception 
	 */
	public TempBean login(MessageLogin messageLogin, TempBean tempBean, TaskMobile taskMobile) throws Exception {
		WebClient webClient =loginAndGetCommonService.addcookie(taskMobile);
		String url = "http://jx.189.cn/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url=http://jx.189.cn/2017/fee.jsp";
		HtmlPage html =loginAndGetCommonService.getHtml(url, webClient);
		if(html!=null){
			
			HtmlImage image = html.getFirstByXPath("//img[@id='form_verify_img']");
			String code = chaoJiYingOcrService.getVerifycode(image, "1902");
			System.out.println("期初识别出来的验证码是："+code);
			
			//HtmlTextInput phonenum = (HtmlTextInput) html.getFirstByXPath("//input[@id='form_user_no']");
			//改版后
			HtmlTelInput  phonenum = (HtmlTelInput) html.getFirstByXPath("//input[@id='form_user_no']");
			HtmlTextInput verifyCode = (HtmlTextInput)html.getFirstByXPath("//input[@id='form_verify_code']");
			verifyCode.setText(code);
			phonenum.setText(messageLogin.getName());
			//===================验证图片验证码=======================
			url="http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
		    WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.POST);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new NameValuePair("callCount", "1"));  
			params.add(new NameValuePair("page", "/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url=http://jx.189.cn/2017/fee.jsp"));
			params.add(new NameValuePair("httpSessionId", ""));
			params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
			params.add(new NameValuePair("c0-scriptName", "Service"));
			params.add(new NameValuePair("c0-methodName", "excute"));
			params.add(new NameValuePair("c0-id", "0"));   //参数1
			params.add(new NameValuePair("c0-param0", "WB_TEST_VALIDCODE"));   //参数2
			params.add(new NameValuePair("c0-param1", "boolean:false"));
			params.add(new NameValuePair("c0-e1", "string:"+code+""));
			params.add(new NameValuePair("c0-param2", "Object_Object:{valid_code_input:reference:c0-e1}"));
			params.add(new NameValuePair("batchId", "21"));
			
			webRequest.setRequestParameters(params);
			webRequest.setAdditionalHeader("Accept", "*/*");    
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
			webRequest.setAdditionalHeader("Connection", "keep-alive");      
			webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
			webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
			webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
			webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url=http://jx.189.cn/2017/fee.jsp"); 
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
			HtmlPage hPage = webClient.getPage(webRequest);
			String htmlImage=hPage.getWebResponse().getContentAsString();
			System.out.println("获取的验证图片验证码的响应页面是："+htmlImage);
			tracer.addTag("获取的验证图片验证码的响应页面是：", taskMobile.getTaskid()+"====>"+htmlImage);
			int a=htmlImage.indexOf("{");
			int b=htmlImage.lastIndexOf("}");
			htmlImage=htmlImage.substring(a,b+1);
			JSONObject rr = JSONObject.fromObject(htmlImage);
			String testValue = rr.getString("WB_TEST_VALIDCODE");
			System.out.println("获取的验证图片验证码正确性的value值是："+testValue);
			tracer.addTag("获取的代表图片验证码正确性的value值是：", testValue);
			if(testValue.equals("OK")){   //OK是正确的,NO是错误的
				System.out.println("图片验证码识别正确,接下来调用短信发送接口");
				tracer.addTag("图片验证码识别正确，接下来调用短信发送接口", taskMobile.getTaskid());
				//调用发送短信验证码接口
				tempBean = getloginphonecode(messageLogin, taskMobile);
				if(null!=tempBean && null!=tempBean.getErrormessage()){
					String errormessage = tempBean.getErrormessage();
					if(errormessage.contains("失败")){
						//获取手机验证码失败
						tempBean.setStatusCodeRec(StatusCodeRec.MESSAGE_CODE_ERROR_ONE);
						tempBean.setHtmlpage(null);  //在短信验证码验证失败的情况下，存储null
					}else{
						//获取手机验证码成功
						tempBean.setStatusCodeRec(StatusCodeRec.MESSAGE_CODE_SUCESS);
						tempBean.setHtmlpage(hPage);  //在短信验证码验证失败的情况下，存储验证图片验证码的页面
					}
				}
			}else{
				captchaErrorCount++;
				if(captchaErrorCount>3){
					tracer.addTag("二次登录时，图片验证码识别错误次数过多","故结束识别，认为二次登录所需短信验证码发送失败，因江西电信二次登录发送短信验证码需要以“图片验证码识别正确”为前提");
					tempBean.setStatusCodeRec(StatusCodeRec.MOBILE_VERIFY_IMG_THR_ERROR);
					tempBean.setHtmlpage(null);  //如果识别了三次验证码还是失败，就设置为null,以此判断最终的发送验证码的状态是失败
					captchaErrorCount=0;
					return tempBean;
				}else{
					System.out.println("发送二次登录所需短信前，图片验证码识别错误，这是第"+captchaErrorCount+"次重新调用图片验证码识别的方法");
					tracer.addTag("图片验证码识别重试"+captchaErrorCount, "发送二次登录所需短信前，图片验证码识别错误，这是第"+captchaErrorCount+"次重新调用图片验证码识别的方法");
					login(messageLogin,tempBean,taskMobile);
			    }
			}
		}else{  //通过后期测试发现，有时候官网二次登录的页面会挂掉,导致二次登录所需短信验证码发送失败，故此处也需要处理
			tracer.addTag("获取二次登录短信验证码的官网页面暂时无法响应","通过后期测试发现，有时候官网二次登录的页面会挂掉,导致二次登录所需短信验证码发送失败，故此处也需要处理");
			tempBean.setStatusCodeRec(StatusCodeRec.MESSAGE_CODE_ERROR_ONE);
			tempBean.setHtmlpage(null);  //在短信验证码验证失败的情况下，存储null
		}
		return tempBean;
	}
	//=================================================================================
	public TempBean getloginphonecode(MessageLogin messageLogin, TaskMobile taskMobile) {
		TempBean tempBean  = new TempBean();
		try {
			WebClient webClient =loginAndGetCommonService.addcookie(taskMobile);
			String url = "http://jx.189.cn/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url=http://jx.189.cn/2017/fee.jsp";
			HtmlPage hPage =loginAndGetCommonService.getHtml(url, webClient);
			if(hPage!=null){
				webClient = hPage.getWebClient();
				url = "http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new NameValuePair("callCount", "1"));  
				params.add(new NameValuePair("page", "/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url=http://jx.189.cn/2017/fee.jsp"));
				params.add(new NameValuePair("httpSessionId", ""));
				params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
				params.add(new NameValuePair("c0-scriptName", "Service"));
				params.add(new NameValuePair("c0-methodName", "excute"));
				params.add(new NameValuePair("c0-id", "0"));   //参数1
				params.add(new NameValuePair("c0-param0", "string:SEND_LOGIN_RANDOM_PWD"));   //参数2
				params.add(new NameValuePair("c0-param1", "boolean:false"));
				params.add(new NameValuePair("c0-e1", "string:"+messageLogin.getName()+""));
				params.add(new NameValuePair("c0-e2", "string:CR0"));
				params.add(new NameValuePair("c0-e3", "string:001"));
				params.add(new NameValuePair("c0-e4", "string:no"));
				params.add(new NameValuePair("c0-param2", "Object_Object:{RECV_NUM:reference:c0-e1, SMS_OPERTYPE:reference:c0-e2, RAND_TYPE:reference:c0-e3, need_val:reference:c0-e4}"));
				params.add(new NameValuePair("batchId", "6"));
				
				webRequest.setRequestParameters(params);
				webRequest.setAdditionalHeader("Accept", "*/*");    
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
				webRequest.setAdditionalHeader("Connection", "keep-alive");      
				webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
				webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
				webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
				webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url=http://jx.189.cn/2017/fee.jsp"); 
				webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
				HtmlPage page = webClient.getPage(webRequest);
				String html = page.getWebResponse().getContentAsString();
				System.out.println("获取的发送二次登录所需要的验证码的页面是："+html);
				tracer.addTag("action.twologin.getloginphonecode.html","获取二次登录所需短信验证码的页面是："+html);  //此处保留获取验证码的返回页面，原因见下面注释
				if(html.contains("RECV_NUM")){   //用这种方式不太保险，但是还没有发送失败的情况，在开发的过程中
					System.out.println("二次登录所需短信验证码发送成功");
					tempBean.setErrormessage("二次登录所需短信验证码发送成功");
					//存储发送验证码后返回的页面，用作登录中最后判断是否发送成功的标志
					//发送成功，就在登录那里显示发送成功
					tempBean.setPage(page);   
					tracer.addTag("action.twologin.getloginphonecode", "二次登录所需短信验证码发送成功");
					//将验证码发送成功的cookie更新到taskMobile表
					String cookieString = CommonUnit.transcookieToJson(page.getWebClient());
					taskMobile.setCookies(cookieString);
					save(taskMobile);
					return tempBean;
				}else{
					tempBean.setErrormessage("二次登录所需短信验证码发送失败");
					System.out.println("二次登录所需短信验证码发送失败");
					tracer.addTag("action.twologin.getloginphonecode", "二次登录所需短信验证码发送失败");
					return null;
				}
			}
		} catch (Exception e) {
			if (taskMobile.getTrianNum() > 2) {
				return null;
			}
			tempBean = getloginphonecode(messageLogin,taskMobile);
			return tempBean;
		}
		return tempBean;
	}

	//=============================验证登录需要的短信验证码==================================
	/**
	 * 验证登录所需要的短信验证码，如果验证可以通过，就将验证登录通过的cookie作为最后登录成功的cookie
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	public  TempBean setloginphonecode(MessageLogin messageLogin, TaskMobile taskMobile){
		TempBean tempBean = new TempBean();
		try {
			String encryptedPhoneNum=rsa.encryptedPhone(messageLogin.getName(),exponent,modulus);
			String encryptedServicePwd=rsa.encryptedPhone(messageLogin.getSms_code(),exponent,modulus);   //期初总是错误，是因为加密的应该是短信随机码，而不是服务密码
			WebClient webClient =loginAndGetCommonService.addcookie(taskMobile);
			String url = "http://jx.189.cn/dwr/call/plaincall/Service.excute.dwr";
			WebRequest webRequest= new WebRequest(new URL(url), HttpMethod.POST);
			String smsCode = messageLogin.getSms_code().trim();
			if(null!=smsCode){
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new NameValuePair("callCount", "1"));  
				params.add(new NameValuePair("page", "/public/v4/logon/loginPop.jsp?from_sc=service_login&ret_url=http://jx.189.cn/2017/fee.jsp"));
				params.add(new NameValuePair("httpSessionId", ""));
				params.add(new NameValuePair("scriptSessionId", "AA0CBE9FB90164F9E0E55CF74FCC9338180"));
				params.add(new NameValuePair("c0-scriptName", "Service"));
				params.add(new NameValuePair("c0-methodName", "excute"));
				params.add(new NameValuePair("c0-id", "0"));   //参数1
				params.add(new NameValuePair("c0-param0", "string:MWB_WT_USERLOGIN"));   //参数2
				params.add(new NameValuePair("c0-param1", "boolean:false"));
				params.add(new NameValuePair("c0-e1", "string:22"));
				params.add(new NameValuePair("c0-e2", "string:80000045"));
				params.add(new NameValuePair("c0-e3", "string:"+encryptedServicePwd+""));
			    params.add(new NameValuePair("c0-e4", "string:"+encryptedPhoneNum+""));
				params.add(new NameValuePair("c0-e5","string:"+taskMobile.getAreacode().trim()+"" ));   //注意区域码
				params.add(new NameValuePair("c0-e6","string:1"));
				params.add(new NameValuePair("c0-param2", "Object_Object:{LOGIN_TYPE:reference:c0-e1, LOGIN_PRODUCT_ID:reference:c0-e2, LOGIN_PASSWD:reference:c0-e3, LOGIN_NAME:reference:c0-e4, AREA_CODE:reference:c0-e5, MY_CHECK_FLAG:reference:c0-e6}"));
				params.add(new NameValuePair("batchId", "5"));
				
				webRequest.setRequestParameters(params);
				webRequest.setAdditionalHeader("Accept", "*/*");    
				webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");      
				webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");      
				webRequest.setAdditionalHeader("Connection", "keep-alive");      
				webRequest.setAdditionalHeader("Content-Type", "text/plain"); 
				webRequest.setAdditionalHeader("Host", "jx.189.cn"); 
				webRequest.setAdditionalHeader("Origin", "http://jx.189.cn");   
				webRequest.setAdditionalHeader("Referer", "http://jx.189.cn/public/v4/logon/loginPop.jsp?from_sc=http://jx.189.cn/2017/fee.jsp"); 
				webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"); 
				//获取验证验证码成功的页面
				HtmlPage hPage = webClient.getPage(webRequest);
				String html=hPage.getWebResponse().getContentAsString();
				System.out.println("获取的验证短信验证码的页面是："+html);
				tracer.addTag("action.twologin.setloginphonecode.html","获取的验证短信验证码的页面是："+html);
				//根据验证码校验的返回状态值来判断是否校验成功
				if(html.contains("_msg':null")){
					tracer.addTag("二次登录短信验证码验证成功", taskMobile.getTaskid());
					System.out.println("二次登录短信验证码验证成功");
					WebClient webClientSuccess=hPage.getWebClient(); 
					tempBean.setWebClient(webClientSuccess);
				}else{
					int i=html.indexOf("{");
					int j=html.lastIndexOf("}");
					html=html.substring(i,j+1);
					JSONObject json=JSONObject.fromObject(html);
					String verifyMsg = json.getString("_msg");
					System.out.println("获取的验证短信码信息的结果是："+verifyMsg);
					tracer.addTag("获取的验证短信码信息的结果是：", taskMobile.getTaskid()+"===>"+verifyMsg);
					if(verifyMsg.contains("\u5BF9\u4E0D\u8D77\uFF0C\u60A8\u5DF2\u7ECF\u8FDE\u7EED3\u6B21\u8F93\u9519\u5BC6\u7801\uFF0C\u6B64\u7528\u6237\u4ECA\u65E5\u5185\u5DF2\u4E0D\u5141\u8BB8\u767B\u5F55\uFF0C\u8BF7\u660E\u65E5\u518D\u8BD5\uFF01")){   //
						System.out.println("短信服务密码连续输入错误的次数超过三次，请明日再试");
						tempBean.setErrormessage("短信服务密码连续输入错误的次数超过三次，请明日再试");
						tracer.addTag("action.twologin.setloginphonecode", "短信服务密码连续输入错误的次数超过三次，请明日再试");
					}else if(verifyMsg.contains("\u5931\u8D25\u539F\u56E0\uFF1A\u60A8\u8F93\u5165\u7684\u77ED\u4FE1\u5BC6\u7801\u9519\u8BEF\uFF01")){   
						System.out.println("您输入的短信密码错误");
						tempBean.setErrormessage("您输入的短信密码错误");
						tracer.addTag("action.twologin.setloginphonecode", "您输入的短信密码错误");
					}else if(verifyMsg.contains("\u5931\u8D25\u539F\u56E0\uFF1A\u767B\u5F55\u5931\u8D25\uFF0C\u4E0D\u5B58\u5728\u6B64\u7528\u6237\uFF01")){   
						System.out.println("登录失败，不存在此用户！");
						tempBean.setErrormessage("登录失败，不存在此用户");
						tracer.addTag("action.twologin.setloginphonecode", "登录失败，不存在此用户！");
					}else if(verifyMsg.contains("\u5931\u8D25\u539F\u56E0\uFF1A\uFF01\u60A8\u8F93\u5165\u7684\u77ED\u4FE1\u5BC6\u7801\u5DF2\u5931\u6548\uFF01\u8BF7\u91CD\u65B0\u83B7\u53D6\u77ED\u4FE1\u5BC6\u7801\uFF01")){
						System.out.println("失败原因：！您输入的短信密码已失效！请重新获取短信密码！");
						tempBean.setErrormessage("失败原因：！您输入的短信密码已失效！请重新获取短信密码！");
						tracer.addTag("action.twologin.setloginphonecode", "失败原因：！您输入的短信密码已失效！请重新获取短信密码！");
					}else{
						tempBean.setErrormessage("未知错误！");
						tracer.addTag("action.twologin.setloginphonecode", "未知错误！   "+tempBean.getErrormessage());
						tracer.addTag("二次登录短信验证码验证过程中出现未知错误，响应的错误页面是：", tempBean.getHtml());
					}
				}
				tempBean.setHtml(html);  //存储验证验证码后返回的页面
				return tempBean;
			}
		} catch (Exception e) {
			System.out.println("获取的异常信息是："+e.toString());
			tempBean.setErrormessage("未知错误！");
			tracer.addTag("action.twologin.setloginphonecode", "未知错误！");
			return tempBean;
		}
		return tempBean;
	}
	public void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}
	public TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}
}
