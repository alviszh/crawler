package app.service;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.zhejiang.TelecomZhejiangCallRec;
import com.microservice.dao.entity.crawler.telecom.zhejiang.TelecomZhejiangMsg;
import com.microservice.dao.entity.crawler.telecom.zhejiang.TelecomZhejiangPayfee;
import com.microservice.dao.entity.crawler.telecom.zhejiang.TelecomZhejiangUserInfo;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.zhejiang.TelecomZhejiangCallRecRepository;
import com.microservice.dao.repository.crawler.telecom.zhejiang.TelecomZhejiangMsgRepository;
import com.microservice.dao.repository.crawler.telecom.zhejiang.TelecomZhejiangPayfeeRepository;
import com.microservice.dao.repository.crawler.telecom.zhejiang.TelecomZhejiangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.TelecomZhejiangParser;
import app.service.aop.ICrawler;
import app.service.aop.ISmsTwice;
import app.service.aop.impl.CrawlerImpl;
import app.unit.TeleComCommonUnit;

@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.zhejiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.zhejiang")
public class TelecomZhejiangService implements ISmsTwice{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomZhejiangParser telecomZhejiangParser;
	@Autowired
	private TelecomZhejiangUserInfoRepository telecomZhejiangUserInfoRepository;
	@Autowired
	private TelecomZhejiangPayfeeRepository telecomZhejiangPayfeeRepository;
	@Autowired
	private TelecomZhejiangCallRecRepository telecomZhejiangCallRecRepository;
	@Autowired
	private TelecomZhejiangMsgRepository telecomZhejiangMsgRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private CrawlerImpl crawlerImpl;
	
	/**
	 * @Des 改变task_mobile表当前状态
	 * @param phase
	 * @param phasestatus
	 * @param description
	 * @param taskid
	 */
	public TaskMobile changeTaskMobileStauts(String phase, String phasestatus, String description, String taskid) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(taskid);
		taskMobile.setPhase(phase);
		taskMobile.setPhase_status(phasestatus);
		taskMobile.setDescription(description);
//		taskMobile.setFinished(isTrue);
		taskMobile = taskMobileRepository.save(taskMobile);
		
//		crawlerImpl.getAllDataDone(taskid);
		return taskMobile;
	}

	/**
	 * @Des 登录后到发送短信直接所需的步骤
	 * @param messageLogin
	 * @param taskMobile
	 * @throws Exception 
	 */
	
	@Async
	public void intermediate(MessageLogin messageLogin, TaskMobile taskMobile) throws Exception {
		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String adminUrl = "http://www.189.cn/zj/";
		tracer.addTag("登录成功后到发送短信所需步骤：", "开始！");
		HtmlPage adminPage = (HtmlPage) TeleComCommonUnit.getHtml(adminUrl, webClient);
//		tracer.addTag("crawler.telecom.zhejiang.intermediate.page", "<xmp>"+adminPage.getWebResponse().getContentAsString()+"</xmp>");
		taskMobileRepository.updateDescriptionByTaskid("页面加载中..(1/7)",taskMobile.getTaskid());
		
		HtmlElement button = adminPage.getFirstByXPath("//a[@href='https://www.189.cn/dqmh/ssoLink.do?method=linkTo&platNo=10012&toStUrl=https://zj.189.cn/service/queryorder/']");
		tracer.addTag("crawler.telecom.zhejiang.intermediate.button", "<xmp>"+button.asXml()+"</xmp>");
		HtmlPage clickPage = button.click();	
		taskMobileRepository.updateDescriptionByTaskid("页面加载中..(2/7)",taskMobile.getTaskid());
		
		String baseUrl = "http://www.189.cn/dqmh/my189/initMy189home.do";
		TeleComCommonUnit.getHtml(baseUrl, webClient);
		taskMobileRepository.updateDescriptionByTaskid("页面加载中..(3/7)",taskMobile.getTaskid());
		
		String queryUrl = "http://zj.189.cn/zjpr/service/query/query_order.html?menuFlag=1";
		TeleComCommonUnit.getHtml(queryUrl, webClient);
		taskMobileRepository.updateDescriptionByTaskid("页面加载中..(4/7)",taskMobile.getTaskid());
		
		String realUrl = "http://zj.189.cn/zjpr/user/Userinfodef/groupRealNameVerify.htm";
		TeleComCommonUnit.getHtml(realUrl, webClient);
		taskMobileRepository.updateDescriptionByTaskid("页面加载中..(5/7)",taskMobile.getTaskid());
		
		String serviceUrl = "http://zj.189.cn/bfapp/buffalo/cdrService";
		String servicepayload = "<buffalo-call><method>querycdrasset</method></buffalo-call>";
		String serviceHtml = getHtmlPOST(serviceUrl,null,servicepayload,webClient);
		tracer.addTag("crawler.telecom.intermediate.servicehtml", "<xmp>"+serviceHtml+"</xmp>");
		taskMobileRepository.updateDescriptionByTaskid("页面加载中..(6/7)",taskMobile.getTaskid());
		
		String integration_id = parserParam(serviceHtml,"string:contains(integration_id)");
		String serv_type_id = parserParam(serviceHtml,"string:contains(serv_type_id)");
		
		tracer.addTag("crawler.telecom.zhejiang.intermediate.param", "integration_id ="+integration_id+",  serv_type_id="+serv_type_id);		
		if(StringUtils.isNotBlank(integration_id) && StringUtils.isNotBlank(serv_type_id)){			
			changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_SUCCESS.getPhase(),StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_SUCCESS.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_SUCCESS.getDescription(),messageLogin.getTask_id());			
			//更新cookie
			String cookies = CommonUnit.transcookieToJson(webClient);
			taskMobileRepository.updateCookiesAndParamByTaskid(messageLogin.getTask_id(),cookies,integration_id+","+serv_type_id);			
			tracer.addTag("crawler.telecom.zhejiang.intermediate.cookie", "update the cookie");			
		}else{
			changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getPhase(),StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_CRAWLER_INTERMEDIATE_ERROR.getDescription(),messageLogin.getTask_id());
		}
		webClient.close();
	}
	
	/**
	 * @Des 解析需求字段
	 * @param serviceHtml
	 * @param string
	 * @return
	 */
	public String parserParam(String serviceHtml, String string) throws Exception{
		Document doc = Jsoup.parse(serviceHtml);
		Element e = doc.select(string).first();
		if(null != e){
			Element param = e.nextElementSibling();
			if(null != param){
				return param.text();
			}else{
				return null;
			}		
		}else{
			return null;
		}
	}

	/**
	 * @Des post请求
	 * @param url
	 * @param map
	 * @param payload
	 * @param webClient
	 * @return
	 * @throws Exception
	 */
	public String getHtmlPOST(String url,Map<String,String> map,String payload, WebClient webClient) throws Exception{
		String html = "";
		URL gsurl = new URL(url);
		WebRequest request = new WebRequest(gsurl, HttpMethod.POST);
		
		request.setAdditionalHeader("Host", "zj.189.cn");
		request.setAdditionalHeader("Referer", "http://zj.189.cn/zjpr/service/query/query_order.html?menuFlag=1");
		request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		request.setAdditionalHeader("Pragma", "no-cache");
		request.setAdditionalHeader("Content-Type", "text/xml;charset=UTF-8");
		request.setAdditionalHeader("X-Buffalo-Version", "2.0");
				
		if(null != map){
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : map.entrySet()) {  
				list.add(new NameValuePair(entry.getKey(), entry.getValue()));  
			}  
			request.setRequestParameters(list);			
		}
		
		if(null != payload){
			request.setRequestBody(payload);
		}
			
		Page page = webClient.getPage(request);
		int code = page.getWebResponse().getStatusCode();
		if(code == 200){
			html = page.getWebResponse().getContentAsString();		
		}

		return html;	
	}

	/**
	 * @Des 发送短信
	 * @param messageLogin
	 * @param taskMobile
	 * @throws Exception 
	 */
	@Async
	public TaskMobile sendSmsTwice(MessageLogin messageLogin){
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String sendUrl = "http://zj.189.cn/bfapp/buffalo/VCodeOperation";
		String formload = "<buffalo-call><method>SendVCodeByNbr</method><string>"+messageLogin.getName()+"</string></buffalo-call>";
		
		String html = null;
		try {
			html = getHtmlPOST(sendUrl,null,formload,webClient);
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean isSuccess = telecomZhejiangParser.isSuccess(html);			//判断短信是否发送成功
		if(isSuccess){
			//更改状态
			taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase(),StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription(),messageLogin.getTask_id());
			//更新cookie
			String cookies = CommonUnit.transcookieToJson(webClient);
			taskMobileRepository.updateCookiesByTaskid(messageLogin.getTask_id(), cookies);
			
			tracer.addTag("crawler.telecom.zhejiang.sendSms.cookie", "update the cookie");
		}else{
			taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase(),StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription(),messageLogin.getTask_id());
		}
		webClient.close();
		return taskMobile;
	}

	/**
	 * @Des 获取用户信息
	 * @param messageLogin
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.zhejiang.getUserInfo.start", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		TelecomZhejiangUserInfo telecomZhejiangUserInfo = new TelecomZhejiangUserInfo();
		
		//星级
		String starUrl = "http://zj.189.cn/zjpr/servicenew/queryAccountInfo.htm";
		Page starPage = TeleComCommonUnit.getHtml(starUrl, webClient);
		String starHtml = starPage.getWebResponse().getContentAsString();
		if(starHtml.contains("页面君已经狗带，回归星海")){
			telecomZhejiangUserInfo.setStarLevel("");
		}else{
			tracer.addTag("crawler.telecom.zhejiang.getUserInfo.starpage", starHtml);
			String starLevel = telecomZhejiangParser.getIntegral(starPage.getWebResponse().getContentAsString());
			telecomZhejiangUserInfo.setStarLevel(starLevel);		
		}
		
		//套餐
		String productUrl = "http://zj.189.cn/zjpr/servicenew/gettaocan.htm";
		Page productPage = TeleComCommonUnit.getHtml(productUrl, webClient);
		String productHtml = productPage.getWebResponse().getContentAsString();
		if(productHtml.contains("页面君已经狗带，回归星海")){
			telecomZhejiangUserInfo.setProductName("");			
		}else{
			tracer.addTag("crawler.telecom.zhejiang.getUserInfo.productPage", productPage.getWebResponse().getContentAsString());
			String productName = telecomZhejiangParser.getIntegral(productPage.getWebResponse().getContentAsString());			
			telecomZhejiangUserInfo.setProductName(productName);	
		}
		
		telecomZhejiangUserInfo.setTaskid(messageLogin.getTask_id());
		telecomZhejiangUserInfoRepository.save(telecomZhejiangUserInfo);
		tracer.addTag("crawler.telecom.zhejiang.getUserInfo.success", messageLogin.getTask_id());
		
		taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "【用户信息】采集完成！");	
		taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "无亲情套餐！");
		taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "【套餐信息】采集完成！");
		taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
		taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200,"【账户信息】采集完成！");
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		
		webClient.close();
		
	}

	/**
	 * @Des 获取缴费信息
	 * @param messageLogin
	 * @throws Exception 
	 */
	@Async
	public void getPayRec(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.zhejiang.getPayRec.start", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		webClient.setJavaScriptTimeout(10000);
		
		String payUrl = "http://zj.189.cn/zjpr/service/paym/payment_recharge.html?pgPHS.startDate=6";
		Page payPage = TeleComCommonUnit.getHtml(payUrl, webClient);
		List<TelecomZhejiangPayfee> telecomZhejiangPayfees = telecomZhejiangParser.parserPayRec(payPage.getWebResponse().getContentAsString(),taskMobile);
		
		if(null != telecomZhejiangPayfees && telecomZhejiangPayfees.size()>0){
			telecomZhejiangPayfeeRepository.saveAll(telecomZhejiangPayfees);
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 200, "【缴费信息】采集完成！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
		}else{
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 404, "【缴费信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
		webClient.close();
	}

	/**
	 * @Des 爬取通话记录
	 * @param messageLogin
	 * @param mon
	 * @throws Exception 
	 */
	@Async
	public void getCallRec(MessageLogin messageLogin, String mon) throws Exception {
		
		tracer.addTag("crawler.telecom.zhejiang.getCallRec.start."+mon, messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		webClient.setJavaScriptTimeout(10000);
		String[] params = taskMobile.getNexturl().split(",");
		
		String callUrl = "http://zj.189.cn/zjpr/cdr/getCdrDetail.htm?flag=1&cdrCondition.pagenum=1&cdrCondition.pagesize=500&cdrCondition.productnbr="+messageLogin.getName()+""
				+ "&cdrCondition.areaid=571&cdrCondition.cdrlevel=&cdrCondition.productid="+params[0]+"&cdrCondition.product_servtype="+params[1]+"&cdrCondition.recievenbr=%D2%C6%B6%AF%B5%E7%BB%B0&cdrCondition.cdrmonth="+mon+
				"&cdrCondition.cdrtype=11&cdrCondition.usernameyanzheng="+URLEncoder.encode(taskMobile.getBasicUser().getName(), "gb2312")
				+"&cdrCondition.idyanzheng="+messageLogin.getIdNum()+"&cdrCondition.randpsw="+messageLogin.getSms_code();
		tracer.addTag("用户姓名：", messageLogin.getUsername());
		Page callPage = TeleComCommonUnit.getHtml(callUrl, webClient);
		tracer.addTag("crawler.telecom.zhejiang.getCallRec.html."+mon, callPage.getWebResponse().getContentAsString());
		
		List<TelecomZhejiangCallRec> list = telecomZhejiangParser.parserCall(callPage.getWebResponse().getContentAsString(),messageLogin.getTask_id());
		if(null != list && list.size()>0){
			telecomZhejiangCallRecRepository.saveAll(list);
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "【通讯信息】第"+mon+"月采集完成！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 404, "【通讯信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		webClient.close();
	}

	/**
	 * @Des 获取短信信息
	 * @param messageLogin
	 * @param mon
	 * @throws Exception 
	 */
	@Async
	public void getMsgRec(MessageLogin messageLogin, String mon) throws Exception {
		tracer.addTag("crawler.telecom.zhejiang.getMsgRec.start."+mon, messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		webClient.setJavaScriptTimeout(10000);
		String[] params = taskMobile.getNexturl().split(",");
		
		String msgUrl = "http://zj.189.cn/zjpr/cdr/getCdrDetail.htm?flag=1&cdrCondition.pagenum=1&cdrCondition.pagesize=100&cdrCondition.productnbr="+messageLogin.getName()+
				"&cdrCondition.areaid=571&cdrCondition.cdrlevel=&cdrCondition.productid="+params[0]+"&cdrCondition.product_servtype="+params[1]+
				"&cdrCondition.recievenbr=%D2%C6%B6%AF%B5%E7%BB%B0&cdrCondition.cdrmonth="+mon+"&cdrCondition.cdrtype=21&cdrCondition.usernameyanzheng="+
				URLEncoder.encode(taskMobile.getBasicUser().getName(), "gb2312")
						+ "&cdrCondition.idyanzheng="+messageLogin.getIdNum()+"&cdrCondition.randpsw="+messageLogin.getSms_code();
		
		tracer.addTag("用户姓名：", messageLogin.getUsername());
		Page msgPage = TeleComCommonUnit.getHtml(msgUrl, webClient);
		tracer.addTag("crawler.telecom.zhejiang.getMsgRec.html."+mon, "<xmp>"+msgPage.getWebResponse().getContentAsString()+"</xmp>");
		
		List<TelecomZhejiangMsg> list = telecomZhejiangParser.parserMsg(msgPage.getWebResponse().getContentAsString(),messageLogin.getTask_id());
		if(null != list && list.size()>0){
			telecomZhejiangMsgRepository.saveAll(list);
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "【短信信息】第"+mon+"月采集完成！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 404, "【短信信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		webClient.close();
	}

	/**
	 * @Des 校验短信验证码
	 * @param messageLogin
	 * @param taskMobile
	 */
	@Async
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracer.addTag("crawler.telecom.zhejiang.validate.start", messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String[] params = taskMobile.getNexturl().split(",");
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		Date date = c.getTime();
		String mon = sdf.format(date);
		
		String callUrl = null;
		try {
			callUrl = "http://zj.189.cn/zjpr/cdr/getCdrDetail.htm?flag=1&cdrCondition.pagenum=1&cdrCondition.pagesize=500&cdrCondition.productnbr="+messageLogin.getName()+""
					+ "&cdrCondition.areaid=571&cdrCondition.cdrlevel=&cdrCondition.productid="+params[0]+"&cdrCondition.product_servtype="+params[1]+"&cdrCondition.recievenbr=%D2%C6%B6%AF%B5%E7%BB%B0&cdrCondition.cdrmonth="+mon+
					"&cdrCondition.cdrtype=11&cdrCondition.usernameyanzheng="+URLEncoder.encode(taskMobile.getBasicUser().getName(), "gb2312")+
					"&cdrCondition.idyanzheng="+messageLogin.getIdNum()+"&cdrCondition.randpsw="+messageLogin.getSms_code();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Page callPage = null;
		try {
			callPage = TeleComCommonUnit.getHtml(callUrl, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tracer.addTag("验证的url：", callUrl);
		tracer.addTag("crawler.telecom.zhejiang.validate.html."+mon, "<xmp>"+callPage.getWebResponse().getContentAsString()+"</xmp>");
		
		if(null != callPage){
			String html = callPage.getWebResponse().getContentAsString();
			tracer.addTag("验证的html", "<xmp>"+html+"</xmp>");
			
			if(html.contains("随机验证码输入有误")){
				taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase(),StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus(),
						StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getDescription(),messageLogin.getTask_id());
			}else{
				taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase(),StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus(),
						StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription(),messageLogin.getTask_id());
			}
		}
		
		webClient.close();
		return taskMobile;
	}


	@Override
	public TaskMobile sendSms(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMobile verifySms(MessageLogin messageLogin) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
