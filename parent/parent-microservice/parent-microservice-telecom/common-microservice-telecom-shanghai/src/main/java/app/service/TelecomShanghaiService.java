package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.StatusCodeEnum;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.mobile.MobileDataErrRec;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiAccount;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiCallRec;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiMsg;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiPayfee;
import com.microservice.dao.entity.crawler.telecom.shanghai.TelecomShanghaiUserInfo;
import com.microservice.dao.repository.crawler.mobile.MobileDataErrRecRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.shanghai.TelecomShanghaiAccountRepository;
import com.microservice.dao.repository.crawler.telecom.shanghai.TelecomShanghaiCallRecRepository;
import com.microservice.dao.repository.crawler.telecom.shanghai.TelecomShanghaiMsgRepository;
import com.microservice.dao.repository.crawler.telecom.shanghai.TelecomShanghaiPayfeeRepository;
import com.microservice.dao.repository.crawler.telecom.shanghai.TelecomShanghaiUserInfoRepository;
import app.bean.DataBean;
import app.bean.ResultBean;
import app.commontracerlog.TracerLog;
import app.parser.TelecomShanghaiParser;
import app.service.aop.ISmsTwice;
import app.unit.TeleComCommonUnit;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.telecom.shanghai","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.telecom.shanghai","com.microservice.dao.repository.crawler.mobile"})
public class TelecomShanghaiService implements ISmsTwice{
	
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomShanghaiParser telecomShanghaiParser;
	@Autowired
	private TelecomShanghaiUserInfoRepository telecomShanghaiUserInfoRepository;
	@Autowired
	private CrawlerStatusMobileService crawlerStatusMobileService;
	@Autowired
	private TelecomShanghaiPayfeeRepository telecomShanghaiPayfeeRepository;
	@Autowired
	private TelecomShanghaiAccountRepository telecomShanghaiAccountRepository;
	@Autowired
	private TelecomShanghaiCallRecRepository telecomShanghaiCallRecRepository;
	@Autowired
	private TelecomShanghaiMsgRepository telecomShanghaiMsgRepository;
	@Autowired
	private MobileDataErrRecRepository mobileDataErrRecRepository;
	@Autowired
	private TracerLog tracer;

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
		taskMobile = taskMobileRepository.save(taskMobile);
		return taskMobile;
	}

	/**
	 * @Des 发送短信
	 * @param messageLogin
	 * @param taskMobile 
	 * @return
	 * @throws Exception 
	 */
	@Async
	public TaskMobile sendSmsTwice(MessageLogin messageLogin){
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String baseUrl = "http://service.sh.189.cn/service/account";
		Page basePage = null;
		try {
			basePage = TeleComCommonUnit.getHtml(baseUrl, webClient);
		} catch (Exception e) {
		}
		String chargeFzxh = getParam(basePage);
		tracer.addTag("crawler.telecom.shanghai.sendSms.chargeFzxh", chargeFzxh);
		
		String smsUrl = "http://service.sh.189.cn/service/service/authority/query/billdetail/sendCode.do?flag=1&devNo="+messageLogin.getName()+"&dateType=&moPingType=LOCAL&startDate=&endDate=";
		Page page = null;
		try {
			page = TeleComCommonUnit.getHtml(smsUrl, webClient);
		} catch (Exception e) {
		}
		tracer.addTag("crawler.telecom.shanghai.sendSms.page", page.getWebResponse().getContentAsString());
		
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		DataBean<ResultBean> dataBean = gson.fromJson(page.getWebResponse().getContentAsString(), DataBean.class);
		
		if("0".equals(dataBean.getCODE())){
			taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhase(),StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_WAIT_CODE_SUCCESS.getDescription(),messageLogin.getTask_id());
			//更新cookie
			String cookies = CommonUnit.transcookieToJson(webClient);
			taskMobileRepository.updateCookiesAndParamByTaskid(messageLogin.getTask_id(),cookies,chargeFzxh);
			
			tracer.addTag("crawler.telecom.shanghai.sendSms.cookie", "update the cookie");
		}else{
			taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhase(),StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_SEND_CODE_ERROR.getDescription(),messageLogin.getTask_id());
		}
		return taskMobile;

	}
	
	
	/**
	 * @Des 校验短信是否正确
	 * @param messageLogin
	 * @param taskMobile
	 * @throws Exception 
	 */
	@Async
	public TaskMobile verifySmsTwice(MessageLogin messageLogin) {
		
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		String validateUrl = "http://service.sh.189.cn/service/service/authority/query/billdetail/validate.do?input_code="+messageLogin.getSms_code()+"&selDevid="+messageLogin.getName()+"&flag=nocw&checkCode=%E9%AA%8C%E8%AF%81%E7%A0%81";
		Page page = null;
		try {
			page = TeleComCommonUnit.getHtml(validateUrl, webClient);
		} catch (Exception e) {
		
		}		
		tracer.addTag("crawler.telecom.shanghai.validate.page", page.getWebResponse().getContentAsString());
		
		Gson gson = new Gson();
		@SuppressWarnings("rawtypes")
		DataBean dataBean = gson.fromJson(page.getWebResponse().getContentAsString(), DataBean.class);
		String code = dataBean.getCODE();
		if("0".equals(code)){
			//校验成功
			taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhase(),StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getPhasestatus(),
					StatusCodeEnum.TASKMOBILE_PASSWORD_SUCCESS.getDescription(),messageLogin.getTask_id());
			//更新cookie
			String cookies = CommonUnit.transcookieToJson(webClient);
			taskMobileRepository.updateCookiesByTaskid(messageLogin.getName(), cookies);
		}else{
			//校验失败
			taskMobile = changeTaskMobileStauts(StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhase(),StatusCodeEnum.TASKMOBILE_WAIT_CODE_ERROR1.getPhasestatus(),
					"验证码已失效！或验证码不正确！",messageLogin.getTask_id());
		}
		return taskMobile;
	}
	
	/**
	 * @Des 获取个人信息
	 * @param messageLogin
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.shanghai.getUserInfo.start", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		
		String userUrl ="http://service.sh.189.cn/service/my/basicinfo.do";
		Page page = TeleComCommonUnit.getHtml(userUrl, webClient);
		tracer.addTag("crawler.telecom.shanghai.getUserInfo.html", page.getWebResponse().getContentAsString());
		
		TelecomShanghaiUserInfo data = telecomShanghaiParser.parserUserinfo(page,messageLogin.getTask_id());
		if(null != data){
			telecomShanghaiUserInfoRepository.save(data);			
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 200, "【用户信息】采集完成！");	
			taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 201, "无亲情套餐！");
			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());		
			tracer.addTag("crawler.telecom.shanghai.getUserInfo.success", messageLogin.getTask_id());
		}else{
			tracer.addTag("crawler.telecom.shanghai.userinfo.null", taskMobile.getTaskid());
			taskMobileRepository.updateUserMsgStatus(taskMobile.getTaskid(), 404, "【用户信息】采集超时！");	
			taskMobileRepository.updateFamilyMsgStatus(taskMobile.getTaskid(), 404, "无亲情套餐！");
			taskMobileRepository.updateIntegralMsgStatus(taskMobile.getTaskid(), 201, "无积分详情！");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
	}
	
	/**
	 * @Des 获取缴费信息
	 * @param messageLogin
	 */
	@Async
	public void getPayfee(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.shanghai.getPayfee.start", messageLogin.getTask_id());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		
		//获取当前时间半年前
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -6);
		Date m6 = c.getTime();
		String mon6 = sdf.format(m6);
		
		String payfeeUrl = "http://service.sh.189.cn/service/service/authority/query/rechargePage.do?begin=0&end=100&"
				+ "time=0.2491801649205223&channel_wt=1&total=on&payment_no="+messageLogin.getName()+"&exchange_date="
				+ "&beginDate="+mon6+"&endDate="+sdf.format(new Date())+"&exchangeType=&channelf=1&prodType=4&chargeFzxh="+taskMobile.getNexturl();
		Page page = TeleComCommonUnit.getHtml(payfeeUrl, webClient);
		tracer.addTag("crawler.telecom.shanghai.getPayfee.html", page.getWebResponse().getContentAsString());
		
		List<TelecomShanghaiPayfee> list = telecomShanghaiParser.parserPayfee(page,messageLogin.getTask_id());
		if(null != list && list.size()>0){
			telecomShanghaiPayfeeRepository.saveAll(list);
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 200, "【缴费信息】采集完成！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());		
		}else{
			taskMobileRepository.updatePayMsgStatus(messageLogin.getTask_id(), 404, "【缴费信息】采集超时！");			
			crawlerStatusMobileService.updateTaskMobile(messageLogin.getTask_id());
		}
	}
	
	/**
	 * @Des 获取账户信息
	 * @param messageLogin
	 */
	@Async
	public void getAccount(MessageLogin messageLogin) throws Exception {
		
		tracer.addTag("crawler.telecom.shanghai.getAccount.start", messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		
		String accountUrl = "http://service.sh.189.cn/service/service/authority/queryInfo/getMsgByDeviceId.do";
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new NameValuePair("DeviceId",messageLogin.getName()));
		Page page = TeleComCommonUnit.gethtmlPost(webClient, list, accountUrl);
		tracer.addTag("crawler.telecom.shanghai.getAccount.html", page.getWebResponse().getContentAsString());
		
		TelecomShanghaiAccount telecomShanghaiAccount = telecomShanghaiParser.parserAccount(page,messageLogin.getTask_id());
		if(null == telecomShanghaiAccount){
			taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "【账户信息】采集超时！");		
			taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "【套餐信息】采集超时！");
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}
		telecomShanghaiAccountRepository.save(telecomShanghaiAccount);
		taskMobileRepository.updateAccountMsgStatus(taskMobile.getTaskid(), 200, "【账户信息】采集完成！");		
		taskMobileRepository.updateBusinessMsgStatus(taskMobile.getTaskid(), 200, "【套餐信息】采集完成！");
		crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
	}
	
	/**
	 * @Des 获取通话记录
	 * @param messageLogin
	 * @param mon
	 */
	@Async
	public void getCallRec(MessageLogin messageLogin, String mon){
		
		tracer.addTag("crawler.telecom.shanghai.getCallRec.start."+mon, messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		
		String callUrl = "http://service.sh.189.cn/service/service/authority/query/billdetailQuery.do?begin=0&end=100&flag=1&devNo="+messageLogin.getName()+"&dateType=his"
				+ "&bill_type=SCP&moPingType=LOCAL&queryDate="+mon+"&startDate=&endDate=";
		Page page = null;
		try {
			page = TeleComCommonUnit.getHtml(callUrl, webClient);
		} catch (Exception e) {
			MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", mon,
					taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), 1);
			mobileDataErrRecRepository.save(dataErrRec);
		}
		tracer.addTag("crawler.telecom.shanghai.getCallRec.html."+mon, page.getWebResponse().getContentAsString());
		String html = page.getWebResponse().getContentAsString();
		
		if(html.contains("ME10001")){
			taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 201, "数据采集成功！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			List<TelecomShanghaiCallRec> list = null;
			try {
				list = telecomShanghaiParser.parserCallRec(page,messageLogin.getTask_id());
			} catch (Exception e) {
				MobileDataErrRec dataErrRec = new MobileDataErrRec(taskMobile.getTaskid(), "通话记录", mon,
						taskMobile.getCarrier(), taskMobile.getCity(), "INCOMPLETE", e.getMessage(), 1);
				mobileDataErrRecRepository.save(dataErrRec);
			}
			if(null != list && list.size()>0){
				telecomShanghaiCallRecRepository.saveAll(list);
				taskMobileRepository.updateCallRecordStatus(taskMobile.getTaskid(), 200, "数据采集成功！");			
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}
		}
	}
	
	/**
	 * @Des 获取短信信息
	 * @param messageLogin
	 * @param mon
	 * @throws Exception
	 */
	@Async
	public void getMsgRec(MessageLogin messageLogin, String mon) throws Exception {
		
		tracer.addTag("crawler.telecom.shanghai.getMsgRec.start."+mon, messageLogin.getTask_id());
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());		
		WebClient webClient = taskMobile.getClient(taskMobile.getCookies());
		
		String msgUrl = "http://service.sh.189.cn/service/service/authority/query/billdetailQuery.do?begin=0&end=100&flag=1&devNo="+messageLogin.getName()+"&dateType=his&bill_type=SMSC"
				+ "&moPingType=LOCAL&queryDate="+mon+"&startDate=&endDate=";
		Page page = TeleComCommonUnit.getHtml(msgUrl, webClient);
		String html = page.getWebResponse().getContentAsString();
		tracer.addTag("crawler.telecom.shanghai.getMsgRec.html."+mon, page.getWebResponse().getContentAsString());
		
		if(html.contains("ME10001")){
			taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 201, "数据采集成功！");			
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
		}else{
			List<TelecomShanghaiMsg> list = telecomShanghaiParser.parserMsgRec(page,messageLogin.getTask_id());
			if(null != list && list.size()>0){
				telecomShanghaiMsgRepository.saveAll(list);
				taskMobileRepository.updateSMSRecordStatus(taskMobile.getTaskid(), 200, "数据采集成功！");			
				crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			}			
		}
	}
	
	/**
	 * @Des 获取参数
	 * @param page
	 * @return
	 */
	public String getParam(Page page) {
		Document doc =Jsoup.parse(page.getWebResponse().getContentAsString());
		String chargeFzxh = doc.getElementById("defaultFzxh").attr("value");
		return chargeFzxh;
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
