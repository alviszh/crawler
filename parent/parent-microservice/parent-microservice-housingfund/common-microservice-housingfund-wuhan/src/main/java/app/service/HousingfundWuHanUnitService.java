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
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuhan.HousingWuHanHtml;
import com.microservice.dao.entity.crawler.housing.wuhan.HousingWuHanPayStatus;
import com.microservice.dao.entity.crawler.housing.wuhan.HousingWuHanUserInfo;
import com.microservice.dao.repository.crawler.housing.wuhan.HousingWuHanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.wuhan.HousingWuHanPayStatusRepository;
import com.microservice.dao.repository.crawler.housing.wuhan.HousingWuHanUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.service.common.HousingBasicService;
import app.service.common.LoginAndGetCommon;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuhan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuhan")
public class HousingfundWuHanUnitService extends HousingBasicService{

	@Autowired
	private HousingWuHanUserInfoRepository hanUserInfoRepository;
	@Autowired
	private HousingWuHanPayStatusRepository housingWuHanPayStatusRepository;
	@Autowired
	private HousingWuHanHtmlRepository housingWuHanHtmlRepository;
	@Async
	public Object getUserinfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {

			WebClient webClient = WebCrawler.getInstance().getWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> j = set.iterator();
			while(j.hasNext()){
				webClient.getCookieManager().addCookie(j.next());
			}
			String url = "https://whgjj.hkbchina.com/portal/GJJAcctInfoQry.do?AcctNo=123";
			Page page = LoginAndGetCommon.getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();
			HousingWuHanHtml housingWuHanHtml1 = new HousingWuHanHtml();
			housingWuHanHtml1.setUrl(url);
			housingWuHanHtml1.setHtml(html);
			housingWuHanHtml1.setPagenumber(1);
			housingWuHanHtml1.setType("武汉公积金个人信息页面");
			housingWuHanHtmlRepository.save(housingWuHanHtml1);
			if(null!=html){

				try {
					JSONObject object = JSONObject.fromObject(html);
					HousingWuHanUserInfo housingWuHanUserInfo = null;
					if(object!=null){
						String UnitName = object.getString("UnitName");//存缴单位
						String SalaryNum = object.getString("SalaryNum");//缴存基数
						String UserName = object.getString("UserName");//姓名
						String LastPayMouth = object.getString("LastPayMouth");//上笔存缴日期
						String AcctState = object.getString("AcctState");//状态
						String UnitAcctNo = object.getString("UnitAcctNo");//单位帐号
						String Balance = object.getString("Balance");//余额
						String MonthPaySum = object.getString("MonthPaySum");//月缴额
						String AcctNo = object.getString("AcctNo");//公积金帐号
						String _MCHTimestamp = object.getString("_MCHTimestamp");//数据截至日期

						housingWuHanUserInfo = new HousingWuHanUserInfo(taskHousing.getTaskid(), messageLoginForHousing.getUser_id(), 
								UnitName, SalaryNum, UserName, LastPayMouth, AcctState, UnitAcctNo, Balance, MonthPaySum, AcctNo, AcctNo);
						
					}
					save(housingWuHanUserInfo);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_SUCESS.getCode());
					updateUserInfoStatusByTaskid("数据采集中，个人信息已采集完成", 200,taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", "数据采集中，用户基本信息已采集完成");
					updateTaskHousing(taskHousing.getTaskid());
					return null;
				} catch (Exception e) {
					HousingWuHanHtml housingWuHanHtml = new HousingWuHanHtml();
					housingWuHanHtml.setUrl(url);
					housingWuHanHtml.setHtml(html);
					housingWuHanHtml.setPagenumber(1);
					housingWuHanHtml.setType("武汉公积金个人信息错误页面");
					housingWuHanHtmlRepository.save(housingWuHanHtml);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
					updateUserInfoStatusByTaskid("数据采集中，个人信息网页出问题", 500,taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", e.toString());
					updateTaskHousing(taskHousing.getTaskid());
					return null;
				}
			}else{
				HousingWuHanHtml housingWuHanHtml = new HousingWuHanHtml();
				housingWuHanHtml.setUrl(url);
				housingWuHanHtml.setHtml(html);
				housingWuHanHtml.setPagenumber(1);
				housingWuHanHtml.setType("武汉公积金个人信息错误页面");
				housingWuHanHtmlRepository.save(housingWuHanHtml);
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
				updateUserInfoStatusByTaskid("数据采集中，个人信息已采集完成，无数据", 201,taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
				return null;
			}
		} catch (Exception e) {
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getDescription());
			taskHousing.setError_code(StatusCodeRec.CRAWLER_UserMsg_ERROR.getCode());
			updateUserInfoStatusByTaskid("数据采集中，个人信息网页出问题", 500,taskHousing.getTaskid());
			tracer.addTag("parser.crawler.getUserInfo", e.toString());
			updateTaskHousing(taskHousing.getTaskid());
			return null;
		}
	}
	@Async
	public Object getPayStatus(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {

		try {

			WebClient webClient = WebCrawler.getInstance().getWebClient();
			String cookies = taskHousing.getCookies();
			Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
			Iterator<Cookie> j = set.iterator();
			while(j.hasNext()){
				webClient.getCookieManager().addCookie(j.next());
			}
			String date = new Date().toLocaleString().substring(0,9);
			String url = "https://whgjj.hkbchina.com/portal/GJJAcctHisTrsQry.do?"
					+ "BeginDate=2000-01-01"
					+ "&EndDate="+date;
			Page page = LoginAndGetCommon.getHtml(url, webClient);

			String html = page.getWebResponse().getContentAsString();
			HousingWuHanHtml housingWuHanHtml = new HousingWuHanHtml();
			housingWuHanHtml.setUrl(url);
			housingWuHanHtml.setHtml(html);
			housingWuHanHtml.setPagenumber(1);
			housingWuHanHtml.setType("武汉公积金流水信息页面");
			housingWuHanHtmlRepository.save(housingWuHanHtml);
			if(null!=html){
				try {
					JSONObject object = JSONObject.fromObject(html);
					String List = object.getString("List");
					JSONArray object2 = JSONArray.fromObject(List);
					if(object2.size()>0){
						HousingWuHanPayStatus housingWuHanPayStatus = new HousingWuHanPayStatus();
						List<HousingWuHanPayStatus> list =new ArrayList<HousingWuHanPayStatus>();
						for (int i = 0; i < object2.size(); i++) {
							
							JSONObject object3 = object2.getJSONObject(i);
							String TransDate = object3.getString("TransDate");//时间
							String SummaryCode = object3.getString("SummaryCode");//交易码
							String DebitAmount = object3.getString("DebitAmount");//借方发生额
							String Balance = object3.getString("Balance");//余额
							String CreditAmount = object3.getString("CreditAmount");//贷方发生额
							housingWuHanPayStatus = new HousingWuHanPayStatus(messageLoginForHousing.getTask_id(), messageLoginForHousing.getUser_id(),
									TransDate, SummaryCode, DebitAmount, Balance, CreditAmount);
							list.add(housingWuHanPayStatus);
						}
						housingWuHanPayStatusRepository.saveAll(list);
						taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhase());
						taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getPhasestatus());
						taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_SUCCESS.getDescription());
						taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getCode());
						taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_SUCESS.getMessage());
						updatePayStatusByTaskid("数据采集中，用户缴费信息爬取完毕", 200, taskHousing.getTaskid());
						tracer.addTag("parser.crawler.getUserInfo", "数据完成，用户缴费信息");
						updateTaskHousing(taskHousing.getTaskid());
						return null;
					}
					//无数据
					HousingWuHanHtml housingWuHanHtml1 = new HousingWuHanHtml();
					housingWuHanHtml1.setUrl(url);
					housingWuHanHtml1.setHtml(html);
					housingWuHanHtml1.setPagenumber(1);
					housingWuHanHtml1.setType("武汉公积金流水信息错误页面---->无数据");
					housingWuHanHtmlRepository.save(housingWuHanHtml1);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
					taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getMessage());
					save(taskHousing);
					updatePayStatusByTaskid("数据采集中，用户缴费信息爬取完成，无数据", 201, taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", "用户缴费信息爬取完成，无数据");
					updateTaskHousing(taskHousing.getTaskid());
					return null;
					
				} catch (Exception e) {
					// TODO: handle exception
					HousingWuHanHtml housingWuHanHtml1 = new HousingWuHanHtml();
					housingWuHanHtml1.setUrl(url);
					housingWuHanHtml1.setHtml(html);
					housingWuHanHtml1.setPagenumber(1);
					housingWuHanHtml1.setType("武汉公积金流水信息错误页面---->网页有变动");
					housingWuHanHtmlRepository.save(housingWuHanHtml1);
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
					taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
					taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getMessage());
					save(taskHousing);
					updatePayStatusByTaskid("数据采集中，用户缴费信息网页有问题", 404, taskHousing.getTaskid());
					tracer.addTag("parser.crawler.getUserInfo", e.toString());
					updateTaskHousing(taskHousing.getTaskid());
					return null;
				}
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
				taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
				taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getMessage());
				save(taskHousing);
				updatePayStatusByTaskid("数据采集中，用户缴费信息网页有问题", 404, taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
				return null;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_ERROR.getDescription());
			taskHousing.setError_code(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getCode());
			taskHousing.setError_message(StatusCodeRec.CRAWLER_PayMsgStatus_ERROR.getMessage());
			save(taskHousing);
			updatePayStatusByTaskid("数据采集中，用户缴费信息网页有问题", 404, taskHousing.getTaskid());
			tracer.addTag("parser.crawler.getUserInfo", e.toString());
			updateTaskHousing(taskHousing.getTaskid());
			return null;
		}
	}
	public void save(HousingWuHanUserInfo taskHousing){
		hanUserInfoRepository.save(taskHousing);
	}

}
