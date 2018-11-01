package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinAccountInfo;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinBalance;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinBusiness;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinCallRecord;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinChargeInfo;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinCurrentSituation;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinIntegra;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinMonthBill;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinSMSRecord;
import com.microservice.dao.entity.crawler.telecom.tianjin.TelecomTianjinUserInfo;

import app.domain.WebParam;
import app.service.common.CalendarParamService;
import app.service.common.CodeTypeEnum;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 * @Description: 天津电信相关信息的具体解析
 * @author sln
 * @date 2017年9月14日
 */
@Component
public class TelecomTianjinParser {
	public static final Logger log = LoggerFactory.getLogger(TelecomTianjinParser.class);
	/**
	 * 用户页面解析
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public WebParam<TelecomTianjinUserInfo> userInfoParser(TaskMobile taskMobile, String html) {
		WebParam<TelecomTianjinUserInfo> webParam=new WebParam<TelecomTianjinUserInfo>();
		Document doc=Jsoup.parse(html);
		TelecomTianjinUserInfo telecomTianjinUserInfo=new TelecomTianjinUserInfo();
		telecomTianjinUserInfo.setTaskid(taskMobile.getTaskid());
		telecomTianjinUserInfo.setAccesstime(doc.select("th:contains(入网时间)+td").first().text());
		telecomTianjinUserInfo.setContactnum(doc.select("th:contains(联系电话)+td").first().text());
		telecomTianjinUserInfo.setCusname(doc.select("th:contains(机主姓名)+td").first().text());
		telecomTianjinUserInfo.setEmail(doc.getElementsByTag("input[id='myemail']").text());
		telecomTianjinUserInfo.setIdnum(doc.select("th:contains(证件号码)+td").first().text());
		telecomTianjinUserInfo.setLinkaddress(doc.select("th:contains(通信地址)+td").first().text());
		telecomTianjinUserInfo.setPostalcode(doc.select("th:contains(邮政编码)+td").first().text());
		webParam.setTelecomTianjinUserInfo(telecomTianjinUserInfo);
		return webParam;
	}
	/**
	 * 解析首页我的现状信息(解析套餐信息的时候，相关信息在哪个实体中偶尔变化，故留存套餐信息中的部分代码)
	 * @param taskMobile
	 * @param htmlHuaFei
	 * @param htmlTaoCan
	 * @return
	 */
	public WebParam<TelecomTianjinCurrentSituation> currentSituationParser(TaskMobile taskMobile, String htmlHuaFei,String htmlTaoCan) {
		WebParam<TelecomTianjinCurrentSituation> webParam=new WebParam<TelecomTianjinCurrentSituation>();
		TelecomTianjinCurrentSituation telecomTianjinCurrentSituation=new TelecomTianjinCurrentSituation();
		//解析话费信息
		telecomTianjinCurrentSituation.setThismonthcharge(JSONObject.fromObject(htmlHuaFei).getJSONObject("obj").getString("month_charge"));
		telecomTianjinCurrentSituation.setTotalintegra(JSONObject.fromObject(htmlHuaFei).getJSONObject("obj").getString("myjifen"));
		//解析套餐信息
		telecomTianjinCurrentSituation.setRemainflow(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonFlow").getJSONObject("Surplus").getString("value"));
		telecomTianjinCurrentSituation.setRemainsms(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonMessage").getJSONObject("Surplus").getString("value"));
		telecomTianjinCurrentSituation.setRemainvoice(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonVoice").getJSONObject("Surplus").getString("value"));
		
		
//		telecomTianjinCurrentSituation.setRemainflow(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("my189datarebean").getString("flowsBalanceAmount"));
//		telecomTianjinCurrentSituation.setRemainsms(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("my189datarebean").getString("msgBalanceAmount"));
//		telecomTianjinCurrentSituation.setRemainvoice(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("my189datarebean").getString("callBalanceAmount"));
		telecomTianjinCurrentSituation.setTaskid(taskMobile.getTaskid());
		webParam.setTelecomTianjinCurrentSituation(telecomTianjinCurrentSituation);
		return webParam;
	}
	/**
	 * 账户信息解析（后期测试发现，用isStarMember这个字段的key值是不是Y或者y不靠谱）
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public WebParam<TelecomTianjinAccountInfo> accountInfoParser(TaskMobile taskMobile, String html) {
//		String isStarMember=JSONObject.fromObject(html).getString("isStarMember");
		String pointDepositRet=JSONObject.fromObject(html).getString("pointDepositRet");
		WebParam<TelecomTianjinAccountInfo> webParam=new WebParam<TelecomTianjinAccountInfo>();
		TelecomTianjinAccountInfo telecomTianjinAccountInfo=new TelecomTianjinAccountInfo();
//		if("Y".equals(isStarMember) || "y".equals(isStarMember)){
		if(!pointDepositRet.equals("null")){
			telecomTianjinAccountInfo.setTaskid(taskMobile.getTaskid());
			telecomTianjinAccountInfo.setMaturityintegra(JSONObject.fromObject(html).getJSONArray("pointDepositRet").getJSONObject(0).getString("year_score1"));
			telecomTianjinAccountInfo.setPhonenum(JSONObject.fromObject(html).getJSONArray("pointDepositRet").getJSONObject(0).getString("custId"));
			telecomTianjinAccountInfo.setStarlevel(CodeTypeEnum.getTjStarlevel((JSONObject.fromObject(html).getString("memberLevel"))));
			telecomTianjinAccountInfo.setUsefulintegra(JSONObject.fromObject(html).getJSONArray("pointDepositRet").getJSONObject(0).getString("usablePoint"));
			telecomTianjinAccountInfo.setStarmultiple(CodeTypeEnum.getStarMultiple((JSONObject.fromObject(html).getString("memberLevel"))));
			webParam.setTelecomTianjinAccountInfo(telecomTianjinAccountInfo);
		}else{   //月初或者月末期间账户相关信息采集不完全
			telecomTianjinAccountInfo.setTaskid(taskMobile.getTaskid());
			telecomTianjinAccountInfo.setStarlevel(CodeTypeEnum.getTjStarlevel((JSONObject.fromObject(html).getString("memberLevel"))));
			telecomTianjinAccountInfo.setStarmultiple(CodeTypeEnum.getStarMultiple((JSONObject.fromObject(html).getString("memberLevel"))));
			webParam.setTelecomTianjinAccountInfo(telecomTianjinAccountInfo);
		}
		return webParam;
	}
	/**
	 * 余额信息解析
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public WebParam<TelecomTianjinBalance> BalanceParser(TaskMobile taskMobile, String html) {
		WebParam<TelecomTianjinBalance> webParam=new WebParam<TelecomTianjinBalance>();
		TelecomTianjinBalance telecomTianjinBalance=new TelecomTianjinBalance();
		telecomTianjinBalance.setTaskid(taskMobile.getTaskid());
		telecomTianjinBalance.setAccountBalance(JSONObject.fromObject(html).getJSONObject("basicAccountBalance").getString("accountBalance"));
		telecomTianjinBalance.setQuerytime(CalendarParamService.getBeijingTime());
		webParam.setTelecomTianjinBalance(telecomTianjinBalance);
		return webParam;
	}
	/**
	 * 月账单解析
	 * @param taskMobile
	 * @param html
	 * @param yearmonth
	 * @return
	 */
	public List<TelecomTianjinMonthBill> getMonthBill(TaskMobile taskMobile, String html, String yearmonth) {
		List<TelecomTianjinMonthBill> list=new ArrayList<TelecomTianjinMonthBill>();
		TelecomTianjinMonthBill telecomTianjinMonthBill=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("billItemList").getJSONObject(0).getJSONArray("acctItems");
		int size = jsonArray.size();
		for(int i=0;i<size;i++){
			telecomTianjinMonthBill=new TelecomTianjinMonthBill();
			telecomTianjinMonthBill.setAccessnum(JSONObject.fromObject(html).getJSONArray("billItemList").getJSONObject(0).getString("accNbr"));
			telecomTianjinMonthBill.setExpensename(jsonArray.getJSONObject(i).getString("acctItemName"));
			telecomTianjinMonthBill.setExpense(jsonArray.getJSONObject(i).getString("acctItemFee"));
			telecomTianjinMonthBill.setQuerymonth(yearmonth);
			telecomTianjinMonthBill.setTaskid(taskMobile.getTaskid());
			telecomTianjinMonthBill.setTotalcost(JSONObject.fromObject(html).getJSONArray("billItemList").getJSONObject(0).getString("billFee"));
			list.add(telecomTianjinMonthBill);
		}
		return list;
	}
	/**
	 * 解析充值记录信息
	 * @param taskMobile
	 * @param html
	 * @param firstMonthdate
	 * @param presentDate
	 * @return
	 */
	public List<TelecomTianjinChargeInfo> chargeInfoParser(TaskMobile taskMobile, String html, String firstMonthdate, String presentDate) {
		List<TelecomTianjinChargeInfo> list=new ArrayList<TelecomTianjinChargeInfo>();
		TelecomTianjinChargeInfo telecomTianjinChargeInfo=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("listPaymentHistory");
		int size = jsonArray.size();
		for(int i=0;i<size;i++){
			telecomTianjinChargeInfo=new TelecomTianjinChargeInfo();
			telecomTianjinChargeInfo.setTaskid(taskMobile.getTaskid());
			telecomTianjinChargeInfo.setChargemoney(jsonArray.getJSONObject(i).getString("fee"));
			telecomTianjinChargeInfo.setChargetime(jsonArray.getJSONObject(i).getString("paymentDate"));
			telecomTianjinChargeInfo.setChargetype(jsonArray.getJSONObject(i).getString("paymentType"));
			telecomTianjinChargeInfo.setEnddate(presentDate);
			telecomTianjinChargeInfo.setFlownum(jsonArray.getJSONObject(i).getString("billingID"));
			telecomTianjinChargeInfo.setStartdate(firstMonthdate);
			list.add(telecomTianjinChargeInfo);
		}
		return list;
	}
	/**
	 * 解析积分信息
	 * @param html
	 * @param taskMobile
	 * @param firstMonthdate
	 * @param presentDate
	 * @return
	 */
	public List<TelecomTianjinIntegra> integraParser(String html, TaskMobile taskMobile, String firstMonthdate,String presentDate) {
		List<TelecomTianjinIntegra> list=new ArrayList<TelecomTianjinIntegra>();
		TelecomTianjinIntegra telecomTianjinIntegra=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("scoreGenerationList");
		int size = jsonArray.size();
		for(int i=0;i<size;i++){
			telecomTianjinIntegra=new TelecomTianjinIntegra();
			telecomTianjinIntegra.setTaskid(taskMobile.getTaskid());
			telecomTianjinIntegra.setIntegra(jsonArray.getJSONObject(i).getString("addBonus"));
			telecomTianjinIntegra.setSenddate(jsonArray.getJSONObject(i).getString("period"));
			telecomTianjinIntegra.setType(jsonArray.getJSONObject(i).getString("constituteTypeName"));
			telecomTianjinIntegra.setEnddate(presentDate);
			telecomTianjinIntegra.setStartdate(firstMonthdate);
			list.add(telecomTianjinIntegra);
		}
		return list;
	}
	/**
	 * 解析通话记录页面
	 * @param taskMobile
	 * @param html
	 * @param lastMonthdate 
	 * @param firstMonthdate 
	 * @return
	 */
	public List<TelecomTianjinCallRecord> callRecordParser(TaskMobile taskMobile, String html, String firstMonthdate, String lastMonthdate) {
		Document doc=Jsoup.parse(html);
		TelecomTianjinCallRecord telecomTianjinCallRecord=null;
		List<TelecomTianjinCallRecord> list=new ArrayList<TelecomTianjinCallRecord>();
//		Elements elementsByTag = doc.getElementById("quer2").getElementsByTag("tbody").get(0).getElementsByTag("tr");	
		//默认页码有table的id,但是分页爬取的时候没有，为了统一，用如下方式	
		Elements elementsByTag = doc.getElementsByClass("ued-table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");		
		int size = elementsByTag.size();
		if(size>1){   //说明除了标题行还有别的数据
			for(int i=1;i<size;i++){
				telecomTianjinCallRecord=new TelecomTianjinCallRecord();
				telecomTianjinCallRecord.setTaskid(taskMobile.getTaskid());
				telecomTianjinCallRecord.setCalladdress(elementsByTag.get(i).getElementsByTag("td").get(3).text());
				telecomTianjinCallRecord.setCallednum(elementsByTag.get(i).getElementsByTag("td").get(1).text());
				telecomTianjinCallRecord.setCallingnum(elementsByTag.get(i).getElementsByTag("td").get(0).text());
				telecomTianjinCallRecord.setCosttime(elementsByTag.get(i).getElementsByTag("td").get(5).text());
				telecomTianjinCallRecord.setDiscount(elementsByTag.get(i).getElementsByTag("td").get(7).text());
				telecomTianjinCallRecord.setEnddate(lastMonthdate);
				telecomTianjinCallRecord.setLinktype(elementsByTag.get(i).getElementsByTag("td").get(2).text());
				telecomTianjinCallRecord.setStartdate(firstMonthdate);
				telecomTianjinCallRecord.setStarttime(elementsByTag.get(i).getElementsByTag("td").get(4).text());
				telecomTianjinCallRecord.setTotalcharge(elementsByTag.get(i).getElementsByTag("td").get(6).text());
				list.add(telecomTianjinCallRecord);
			}
		}else{
			list=null;
		}
		return list;
	}
	/**
	 * 短信详单解析
	 * @param taskMobile
	 * @param html
	 * @param firstMonthdate
	 * @param lastMonthdate
	 * @return
	 */
	public List<TelecomTianjinSMSRecord> SMSRecordParser(TaskMobile taskMobile, String html, String firstMonthdate,String lastMonthdate) {
		Document doc=Jsoup.parse(html);
		TelecomTianjinSMSRecord telecomTianjinSMSRecord=null;
		List<TelecomTianjinSMSRecord> list=new ArrayList<TelecomTianjinSMSRecord>();
		Elements elementsByTag = doc.getElementById("test").getElementsByTag("table").get(0).getElementsByTag("tbody").get(0).getElementsByTag("tr");
		int size = elementsByTag.size();
		if(size>1){   //说明除了标题行还有别的数据
			for(int i=1;i<size;i++){
				telecomTianjinSMSRecord=new TelecomTianjinSMSRecord();
				telecomTianjinSMSRecord.setTaskid(taskMobile.getTaskid());
				telecomTianjinSMSRecord.setDiscount(elementsByTag.get(i).getElementsByTag("td").get(4).text());
				telecomTianjinSMSRecord.setEnddate(lastMonthdate);
				telecomTianjinSMSRecord.setStartdate(firstMonthdate);
				telecomTianjinSMSRecord.setGetnum(elementsByTag.get(i).getElementsByTag("td").get(1).text());
				telecomTianjinSMSRecord.setSendnum(elementsByTag.get(i).getElementsByTag("td").get(0).text());
				telecomTianjinSMSRecord.setSendtime(elementsByTag.get(i).getElementsByTag("td").get(2).text());
				telecomTianjinSMSRecord.setTotalcost(elementsByTag.get(i).getElementsByTag("td").get(3).text());
				list.add(telecomTianjinSMSRecord);
			}
		}else{
			list=null;
		}
		return list;
	}
	/**
	 * 增值业务信息解析
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public List<TelecomTianjinBusiness> businessUIParser(TaskMobile taskMobile, String html) {
		List<TelecomTianjinBusiness> list=new ArrayList<TelecomTianjinBusiness>();
		TelecomTianjinBusiness telecomTianjinBusiness=null;
		JSONArray jsonArray = JSONObject.fromObject(html).getJSONArray("subInfoList");
		int size = jsonArray.size();
		for(int i=0;i<size;i++){
			telecomTianjinBusiness=new TelecomTianjinBusiness();
			telecomTianjinBusiness.setTaskid(taskMobile.getTaskid());
			telecomTianjinBusiness.setBusinessname(jsonArray.getJSONObject(i).getString("idName"));
			telecomTianjinBusiness.setBusinesstype("增值业务");
			telecomTianjinBusiness.setEffectivetime(jsonArray.getJSONObject(i).getString("effectiveTime"));
			telecomTianjinBusiness.setExpireTime(jsonArray.getJSONObject(i).getString("expireTime"));
			list.add(telecomTianjinBusiness);
		}
		return list;
	}
	/**
	 * 附加业务信息解析
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public List<TelecomTianjinBusiness> businessSrvParser(TaskMobile taskMobile, String html) {
		List<TelecomTianjinBusiness> list=new ArrayList<TelecomTianjinBusiness>();
		TelecomTianjinBusiness telecomTianjinBusiness=null;
		//解析主套餐
		JSONObject jsonObject = JSONObject.fromObject(html).getJSONObject("mainpackage");
		telecomTianjinBusiness=new TelecomTianjinBusiness();
		telecomTianjinBusiness.setTaskid(taskMobile.getTaskid());
		telecomTianjinBusiness.setBusinessname(jsonObject.getString("pricingPlanName"));
		telecomTianjinBusiness.setBusinesstype("主套餐");
		telecomTianjinBusiness.setEffectivetime(jsonObject.getString("effTime"));
		telecomTianjinBusiness.setExpireTime(jsonObject.getString("expTime"));
		list.add(telecomTianjinBusiness);
		//解析附属业务第一部分
		JSONArray jsonArray1 = JSONObject.fromObject(html).getJSONObject("info").getJSONArray("list");
		for(int i=1;i<6;i++){
			telecomTianjinBusiness=new TelecomTianjinBusiness();
			telecomTianjinBusiness.setTaskid(taskMobile.getTaskid());
			telecomTianjinBusiness.setBusinessname(jsonArray1.getJSONObject(i).getString("pricingPlanName"));
			telecomTianjinBusiness.setBusinesstype("附属业务");
			telecomTianjinBusiness.setEffectivetime(jsonArray1.getJSONObject(i).getString("effTime"));
			telecomTianjinBusiness.setExpireTime(jsonArray1.getJSONObject(i).getString("expTime"));
			list.add(telecomTianjinBusiness);
		}
		//解析附属业务第二部分
		JSONArray jsonArray2 = JSONObject.fromObject(html).getJSONObject("info").getJSONArray("object");
		for(int i=0;i<24;i++){
			telecomTianjinBusiness=new TelecomTianjinBusiness();
			telecomTianjinBusiness.setTaskid(taskMobile.getTaskid());
			telecomTianjinBusiness.setBusinessname(jsonArray2.getJSONObject(i).getString("attachProdName"));
			telecomTianjinBusiness.setBusinesstype("附属业务");
			telecomTianjinBusiness.setEffectivetime(jsonArray2.getJSONObject(i).getString("effectiveTime"));
			telecomTianjinBusiness.setExpireTime(jsonArray2.getJSONObject(i).getString("expirationTime"));
			list.add(telecomTianjinBusiness);
		}
		return list;
	}
}
