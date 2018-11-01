package app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.mobile.json.StatusCodeRec;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wenzhou.HousingWenZhouHtml;
import com.microservice.dao.entity.crawler.housing.wenzhou.HousingWenZhouPayStatus;
import com.microservice.dao.entity.crawler.housing.wenzhou.HousingWenZhouUserInfo;
import com.microservice.dao.repository.crawler.housing.wenzhou.HousingWenZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.wenzhou.HousingWenZhouPayStatusRepository;
import com.microservice.dao.repository.crawler.housing.wenzhou.HousingWenZhouUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.bean.WebParamHousing;
import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wenzhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wenzhou")
public class HousingfundWenZhouService extends HousingBasicService{
	@Autowired
	private HousingWenZhouUserInfoRepository housingWenZhouUserInfoRepository;
	@Autowired
	private HousingWenZhouPayStatusRepository housingWenZhouPayStatusRepository;
	@Autowired
	private HousingWenZhouHtmlRepository housingWenZhouHtmlRepository;
	public Object getPhoneCode(MessageLoginForHousing messageLogin, TaskHousing taskHousing) throws Exception {

		WebParamHousing webParamHousing = null;
		String messageLoginJson = gs.toJson(messageLogin);
		taskHousing.setLoginMessageJson(messageLoginJson);
		System.out.println(messageLogin.toString());
		tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_SEND_CODE_DONING.getDescription());
		//发送验证码状态更新
		save(taskHousing);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.wzgjj.gov.cn:8088//Controller/USER/ws_info.ashx";
		webClient.addRequestHeader("Host", "www.wzgjj.gov.cn:8088");
		webClient.addRequestHeader("Origin", "http://www.wzgjj.gov.cn:8088");
		webClient.addRequestHeader("Referer", "http://www.wzgjj.gov.cn:8088/");
		List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		paramsList = new ArrayList<NameValuePair>();
		paramsList.add(new NameValuePair("name", messageLogin.getNum()));
		paramsList.add(new NameValuePair("password", ""));
		paramsList.add(new NameValuePair("usertype", "10"));
		Page page = LoginAndGetCommon.gethtmlPost(webClient, paramsList, url);
		//webParamHousing.setWebClient(webClient);
		String html = page.getWebResponse().getContentAsString();
		String cookieString = CommonUnit.transcookieToJson(webClient);
		try {
			JSONObject object = JSONObject.fromObject(html);
			String s = object.getString("msg");
			if(s.equals("短信验证码已发送!")!=false){
				taskHousing.setCookies(cookieString);
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.MESSAGE_CODE_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.MESSAGE_CODE_SUCESS.getMessage());
				taskHousing.setLogintype("IDNUM");
				save(taskHousing);
				return null;
			}
			System.out.println("身份证输入错误");
			taskHousing.setCookies(cookieString);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR.getDescription());
			taskHousing.setError_code(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getCode());
			taskHousing.setError_message(StatusCodeRec.MESSAGE_CODE_ERROR_THREE.getMessage());
			save(taskHousing);
			return null;
		} catch (Exception e) {
			tracer.addTag("获取验证码错误", e.toString());
			taskHousing.setCookies(cookieString);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_ERROR.getDescription());
			save(taskHousing);
			return null;
		}


	}

	public Object setPhoneCode(MessageLoginForHousing messageLogin, TaskHousing taskHousing) throws Exception {
		tracer.addTag("parser.crawler.taskid",taskHousing.getTaskid());

		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_WAIT_CODE_DONING.getDescription());
		save(taskHousing);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> j = set.iterator();
		while(j.hasNext()){
			webClient.getCookieManager().addCookie(j.next());
		}
		String url = "http://www.wzgjj.gov.cn:8088/";
		HtmlPage page = (HtmlPage) LoginAndGetCommon.getHtml(url, webClient);
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
		HousingWenZhouHtml housingWenZhouHtml = new HousingWenZhouHtml();
		housingWenZhouHtml.setUrl(url);
		housingWenZhouHtml.setHtml(html);
		housingWenZhouHtml.setPagenumber(1);
		housingWenZhouHtml.setType("温州公积金登录页面");
		save(housingWenZhouHtml);
		HtmlTextInput cardno = (HtmlTextInput)page.getFirstByXPath("//input[@id='card_no']");//身份证
		HtmlTextInput login = (HtmlTextInput)page.getFirstByXPath("//input[@id='yzm_login']");//短信框
		HtmlButtonInput btn =(HtmlButtonInput) page.getFirstByXPath("//input[@id='reg_btn']");


		cardno.setText(messageLogin.getNum());
		login.setText(messageLogin.getSms_code());
		Page page2 = btn.click();
		String cookieString = CommonUnit.transcookieToJson(webClient);
		String url2="http://www.wzgjj.gov.cn:8088//Index.aspx";
		HtmlPage page3 =(HtmlPage)LoginAndGetCommon.getHtml(url2, webClient);
		String html3 = page3.getWebResponse().getContentAsString();
		System.out.println(html.equals(html3));
		
		if(html3.indexOf("退出系统")!=-1){
			taskHousing.setCookies(cookieString);
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
			save(taskHousing);
			return null;
		};
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_ID_VERIFIC_ERROR.getDescription());
		save(taskHousing);
		return null;
	}
//个人信息
	@Async
	public Object getuserinfo(MessageLoginForHousing messageLogin, TaskHousing taskHousing){
		tracer.addTag("parser.crawler.taskid",taskHousing.getTaskid());

		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		save(taskHousing);
		try {
			WebClient webClient = WebCrawler.getInstance().getNewWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> i = set.iterator();
			while(i.hasNext()){
				webClient.getCookieManager().addCookie(i.next());
			}
			String url3 = "http://www.wzgjj.gov.cn:8088/Controller/menu/allTreeNode.ashx";
			Page page3 = LoginAndGetCommon.getHtml(url3, webClient);
			String string = page3.getWebResponse().getContentAsString();
			if(string.contains("个人归集业务查询")){
				String url4 = "http://www.wzgjj.gov.cn:8088/Controller/DW/ywcx.ashx?m=ws2";
				Page page4 = LoginAndGetCommon.getHtml(url4, webClient);
				String html4 = page4.getWebResponse().getContentAsString();
				HousingWenZhouHtml housingWenZhouHtml = new HousingWenZhouHtml();
				housingWenZhouHtml.setUrl(url4);
				housingWenZhouHtml.setHtml(html4);
				housingWenZhouHtml.setPagenumber(1);
				housingWenZhouHtml.setType("温州公积金个人页面");
				save(housingWenZhouHtml);
				HousingWenZhouUserInfo housingWenZhouUserInfo = null;
				if(html4.contains("gr0")){
					JSONObject object4 = JSONObject.fromObject(html4);
					String gr0 = object4.getString("gr0");//职工帐号
					String gr1 = object4.getString("gr1");//姓名
					String gr2 = object4.getString("gr2");//状态
					String gr3 = object4.getString("gr3");//单位帐号
					String gr4 = object4.getString("gr4");//单位名称
					String gr5 = object4.getString("gr5");//缴存基数
					String gr6 = object4.getString("gr6");//单位缴存比例
					String gr7 = object4.getString("gr7");//个人缴存比例
					String gr8 = object4.getString("gr8");//单位月缴存额
					String gr9 = object4.getString("gr9");//个人月缴存额
					//汇缴金额=单位月缴存额+个人月缴存额
					housingWenZhouUserInfo.setAccount(gr0);
					housingWenZhouUserInfo.setName(gr1);
					housingWenZhouUserInfo.setState(gr2);
					housingWenZhouUserInfo.setUnitaccount(gr3);
					housingWenZhouUserInfo.setUnitname(gr4);
					housingWenZhouUserInfo.setDepositbase(gr5);
					housingWenZhouUserInfo.setUnitdeposit(gr6);
					housingWenZhouUserInfo.setUserdeposit(gr7);
					housingWenZhouUserInfo.setUnitmonthpayment(gr8);
					housingWenZhouUserInfo.setUsermonthpayment(gr9);
				}
				String url5 = "http://www.wzgjj.gov.cn:8088/Controller/DW/ywcx.ashx?m=ws3";
				Page page5 = LoginAndGetCommon.getHtml(url5, webClient);
				String html5 = page5.getWebResponse().getContentAsString();
				if(html5.contains("grye2")){
					JSONObject object5 = JSONObject.fromObject(html5);
					String grye2 = object5.getString("grye2");//余额
					housingWenZhouUserInfo.setYu(grye2);
				}
				if(housingWenZhouUserInfo!=null){
					housingWenZhouUserInfo.setTaskid(messageLogin.getTask_id());
					housingWenZhouUserInfo.setUserid(messageLogin.getUser_id());
					save(housingWenZhouUserInfo);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
					updateUserInfoStatusByTaskid("个人信息已采集完成", 200,taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
					updateTaskHousing(taskHousing.getTaskid());
					return null;
				}
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
				taskHousing.setError_message(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getMessage());
				taskHousing.setCookies(string);
				save(taskHousing);
				updateUserInfoStatusByTaskid("数据采集中，个人明细账信息已采集完成无数据", 201,taskHousing.getTaskid());
				tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成无数据");
				updateTaskHousing(taskHousing.getTaskid());
				return null;
			}else{
				tracer.addTag("parser.crawler.taskid", taskHousing.getTaskid());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
				taskHousing.setError_message(StatusCodeRec.CRAWLER_UserMsg_ERROR.getMessage());
				save(taskHousing);
				updateUserInfoStatusByTaskid("数据采集中，公积金个人明细账信息网页出问题", 404,taskHousing.getTaskid());
				tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息");
				updateTaskHousing(taskHousing.getTaskid());
				return null;
			}

		} catch (Exception e) {
			tracer.addTag("parser.crawler.taskid", e.toString());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR_FIVE.getDescription());
			taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			taskHousing.setError_message(StatusCodeRec.CRAWLER_UserMsg_ERROR.getMessage());
			save(taskHousing);
			updateUserInfoStatusByTaskid("数据采集中，公积金个人明细账信息网页出问题", 404,taskHousing.getTaskid());
			tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息");
			updateTaskHousing(taskHousing.getTaskid());
			return null;
		}

	}

	//缴费信息
	@Async
	public Object PayStatus(MessageLoginForHousing messageLogin, TaskHousing taskHousing) throws Exception {
		tracer.addTag("parser.crawler.taskid",taskHousing.getTaskid());

		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getDescription());
		save(taskHousing);
		String s = new Date().toLocaleString().substring(0, 10);
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		String url3 = "http://www.wzgjj.gov.cn:8088/Controller/menu/allTreeNode.ashx";
		Page page3 = LoginAndGetCommon.getHtml(url3, webClient);
		String string = page3.getWebResponse().getContentAsString();
		if(string.contains("个人归集业务查询")){
		
		String url4 = "http://www.wzgjj.gov.cn:8088/Controller/DW/ywcx.ashx?m=ws8"
				+ "&transDateBegin2=2000-01-01"
				+ "&transDateEnd2="+s;
		Page page4 = LoginAndGetCommon.getHtml(url4, webClient);
		String html = page4.getWebResponse().getContentAsString();
		HousingWenZhouHtml housingWenZhouHtml = new HousingWenZhouHtml();
		housingWenZhouHtml.setUrl(url3);
		housingWenZhouHtml.setHtml(html);
		housingWenZhouHtml.setPagenumber(1);
		housingWenZhouHtml.setType("温州公积金缴费页面");
		save(housingWenZhouHtml);
		
		List<HousingWenZhouPayStatus> list = new ArrayList();
		if(html!=null){
			try {
				JSONObject object = JSONObject.fromObject(html);
				String rows = object.getString("rows");
				JSONArray object2 = JSONArray.fromObject(rows);
				for(int j=0;j<object2.size();j++){
					HousingWenZhouPayStatus housingWenZhouPayStatus = new HousingWenZhouPayStatus();
					JSONObject object3 = object2.getJSONObject(j);
					String grjc0 = object3.getString("grjc0");//记账日期
					String grjc1 = object3.getString("grjc1");//摘要
					String grjc2 = object3.getString("grjc2");//所属年月
					String grjc3 = object3.getString("grjc3");//发生金额
					String grjc4 = object3.getString("grjc4");//状态
					String grjc5 = object3.getString("grjc5");//余额

					housingWenZhouPayStatus.setJzdate(grjc0);
					housingWenZhouPayStatus.setSummary(grjc1);
					housingWenZhouPayStatus.setYears(grjc2);
					housingWenZhouPayStatus.setMoney(grjc3);
					housingWenZhouPayStatus.setState(grjc4);
					housingWenZhouPayStatus.setYu(grjc5);
					list.add(housingWenZhouPayStatus);
				}

				if(list!=null){
					for (HousingWenZhouPayStatus h : list) {
						h.setTaskid(messageLogin.getTask_id());
						h.setUserid(messageLogin.getUser_id());
						save(h);
					}
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
					taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getMessage());
					updatePayStatusByTaskid("用户缴费信息爬取完毕", 200, taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", "数据完成，用户缴费信息");
					updateTaskHousing(taskHousing.getTaskid());
					return null;
				}
			
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
				taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getMessage());
				save(taskHousing);
				updatePayStatusByTaskid("用户缴费信息网页有问题", 404, taskHousing.getTaskid());
				tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户缴费信息网页出问题");
				updateTaskHousing(taskHousing.getTaskid());
				return null;
			
			} catch (Exception e) {
				tracer.addTag("parser.crawler.taskid", e.toString());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
				taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getMessage());
				save(taskHousing);
				updatePayStatusByTaskid("用户缴费信息网页有问题", 404, taskHousing.getTaskid());
				tracer.addTag("parser.crawler.pay", "数据采集中，用户缴费信息网页出问题");
				updateTaskHousing(taskHousing.getTaskid());
				return null;
			}
		}
		}
		taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhase());
		taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getPhasestatus());
		taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription());
		taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
		taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getMessage());
		save(taskHousing);
		updatePayStatusByTaskid("用户缴费信息网页有问题", 404, taskHousing.getTaskid());
		tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户缴费信息网页出问题");
		updateTaskHousing(taskHousing.getTaskid());
		return null;
	}

	public void save(HousingWenZhouUserInfo housingWenZhouUserInfo){
		housingWenZhouUserInfoRepository.save(housingWenZhouUserInfo);
	}
	public void save(HousingWenZhouPayStatus housingWenZhouPayStatus){
		housingWenZhouPayStatusRepository.save(housingWenZhouPayStatus);
	}
	public void save(HousingWenZhouHtml housingWenZhouHtml){
		housingWenZhouHtmlRepository.save(housingWenZhouHtml);
	}
}
