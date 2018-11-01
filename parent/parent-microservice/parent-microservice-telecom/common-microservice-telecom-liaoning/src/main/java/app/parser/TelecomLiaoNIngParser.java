package app.parser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingBalance;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingCallThremResult;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPayThrem;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPhoneBill;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPhoneschemes;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingPointValue;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.liaoning.TelecomLiaoNingUserInfo;
import com.module.htmlunit.WebCrawler;

import app.bean.CallThremBean;
import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.GetCommonunit;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class TelecomLiaoNIngParser {

	@Autowired
	private GetCommonunit loginAndGetCommon;
	@Autowired
	private TracerLog tracer;

	public WebParam getCheckTwoHtml(MessageLogin messageLogin, TaskMobile taskMobile) {
		WebClient webClient = WebCrawler.getInstance().getNewWebClient(); 
		webClient = loginAndGetCommon.addcookie(webClient, taskMobile);

		return null;
	}
	public static HtmlPage getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		HtmlPage searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
	public TelecomLiaoNingUserInfo userinfo_parse(String html) {
		TelecomLiaoNingUserInfo telecomLiaoNingUserInfo = null;
		try {

			if(html!=""){
				telecomLiaoNingUserInfo = new TelecomLiaoNingUserInfo();
				JSONObject object = JSONObject.fromObject(html);
				String username = object.getString("userName");//用户姓名
				String indentCode = object.getString("indentCode");//证件号码
				String userAddress = object.getString("userAddress");//地址
				String acceptDate = object.getString("acceptDate");//入网时间
				String ServingStatusName = object.getString("servingStatusName");//状态
				String email = object.getString("email");//邮箱
				String brandIdDesc = object.getString("brandIdDesc");//品牌

				String productCollection = object.getString("productCollection");
				JSONArray json2 = JSONArray.fromObject(productCollection);
				String string = json2.getString(0);
				String string2 = string.substring(12, string.length()-1);
				JSONObject object3 = JSONObject.fromObject(string2);
				String indentNbrType = object3.getString("IdentityKindDesc");//证件类型
				String custContactPhone = object3.getString("CustContactPhone");//电话号码
				String partyId = object3.getString("CustId");//用户标识码


				telecomLiaoNingUserInfo.setBrandIdDesc(brandIdDesc);
				telecomLiaoNingUserInfo.setIndentNbrType(indentNbrType);
				telecomLiaoNingUserInfo.setPartyId(partyId);
				telecomLiaoNingUserInfo.setIndentCode(indentCode);
				telecomLiaoNingUserInfo.setUserName(username);
				telecomLiaoNingUserInfo.setUserAddress(userAddress);
				telecomLiaoNingUserInfo.setIndentCode(indentCode);
				telecomLiaoNingUserInfo.setAcceptDate(acceptDate);
				telecomLiaoNingUserInfo.setEmail(email);
				telecomLiaoNingUserInfo.setCustContactPhone(custContactPhone);
				telecomLiaoNingUserInfo.setServStatus(ServingStatusName);
				return telecomLiaoNingUserInfo;
			}
		} catch (Exception e) {
			return telecomLiaoNingUserInfo=null;
		}
		return telecomLiaoNingUserInfo=null;
	}


	public TelecomLiaoNingBalance chargesResult_parse(String html) {
		TelecomLiaoNingBalance telecomLiaoNingBalance = null;
		try {

			if(html!=""){
				telecomLiaoNingBalance = new TelecomLiaoNingBalance();
				JSONObject object = JSONObject.fromObject(html);
				String restFee = object.getString("restFee");//余额
				String currNoFavour = object.getString("currNoFavour");//当月消费
				telecomLiaoNingBalance.setBalanceInfo(currNoFavour);
				telecomLiaoNingBalance.setRealTimeChargesInfo(restFee);
				return telecomLiaoNingBalance;
			}

		} catch (Exception e) {
			return telecomLiaoNingBalance=null;
		}
		return telecomLiaoNingBalance=null;
	}
	public List<TelecomLiaoNingPhoneBill> phoneBill_parse(String html, String month) {
		List<TelecomLiaoNingPhoneBill> list = null;
		try {
			String[] strings = html.split("var billJson = ");
			String html2 = strings[1].toString();
			String[] strings2 = html2.split(";//账单信息");
			String json = strings2[0].toString();
			JSONObject object = JSONObject.fromObject(json);
			String s = object.getString("BillQueryResponse");
			JSONObject object1 = JSONObject.fromObject(s);
			String s1 = object1.getString("billInfoGroup");
			String s2 = s1.substring(1, s1.length()-1);
			JSONObject object2 = JSONObject.fromObject(s2);
			String s3 = object2.getString("feeInfo");
			JSONArray object3 = JSONArray.fromObject(s3);
			list = new ArrayList<TelecomLiaoNingPhoneBill>();

			for(int i=0;i<object3.size();i++){
				TelecomLiaoNingPhoneBill t = new TelecomLiaoNingPhoneBill();
				JSONObject object4 = object3.getJSONObject(i);
				String name = object4.getString("name");
				String value = object4.getString("value");
				t.setFeeInfoname(name);
				t.setFeeInfovalue(value);
				t.setDate(month);;
				list.add(t);

			}
			return list;
		} catch (Exception e) {
			return list;
		}


	}
	public List<TelecomLiaoNingCallThremResult> callThrem_parse(String html) {
		List<TelecomLiaoNingCallThremResult> list = null;
		try {
			if (html.indexOf("系统正忙，请稍后再试") != -1) {
				System.out.println("============================系统正忙，请稍后再试=====================================");
				return list;
			}
			System.out.println("11");
			JSONObject object = JSONObject.fromObject(html);
			String call = object.getString("items");
			JSONArray json = JSONArray.fromObject(call);
			System.out.println(json);
			list = new ArrayList<TelecomLiaoNingCallThremResult>();
			for (int i = 0; i < json.size(); i++) {
				String total = json.getJSONObject(i).getString("total");
				String duration = json.getJSONObject(i).getString("duration");
				String feeName = json.getJSONObject(i).getString("feeName");
				String baseFee = json.getJSONObject(i).getString("baseFee");
				String indbDate = json.getJSONObject(i).getString("indbDate");
				String otherFee = json.getJSONObject(i).getString("otherFee");
				String roamType = json.getJSONObject(i).getString("roamType");
				String tollType = json.getJSONObject(i).getString("tollType");
				String callDate = json.getJSONObject(i).getString("callDate");
				String callType = json.getJSONObject(i).getString("callType");
				String cellId = json.getJSONObject(i).getString("cellId");
				String cityName = json.getJSONObject(i).getString("cityName");
				String counterAreaCode = json.getJSONObject(i).getString("counterAreaCode");
				String counterNumber = json.getJSONObject(i).getString("counterNumber");
				String favour = json.getJSONObject(i).getString("favour");
				String queryMonth = json.getJSONObject(i).getString("queryMonth");
				String tollAdd = json.getJSONObject(i).getString("tollAdd");
				String transUseAmont = json.getJSONObject(i).getString("transUseAmont");
				TelecomLiaoNingCallThremResult callThre= new TelecomLiaoNingCallThremResult(total, duration, feeName, baseFee, indbDate, otherFee, roamType,
						tollType, callDate, callType, cellId, cityName, counterAreaCode, counterNumber, favour, queryMonth, tollAdd, transUseAmont, null, null);
				System.out.println("\r总计费用:"+total+"\r时长(秒):"+duration+"\r费用类型:"+feeName+"\r基本费:"+baseFee+
						"\r其他费用:"+otherFee+"\r呼叫时间:"+callDate+"\r呼叫类型:"+callType+"\r对方区号:"+counterAreaCode+"\r对方号码:"+counterNumber
						+"\r优惠费:"+favour+"\r长途费:"+tollAdd);
				list.add(callThre);
			}
			return list;
		} catch (Exception e) {
			return list;
		}

	}
	public List<TelecomLiaoNingPayThrem> payThrem_parse(String html) {
		List<TelecomLiaoNingPayThrem> list = null;
		try {
			if (html.indexOf("系统正忙，请稍后再试") != -1) {
				System.out.println("============================系统正忙，请稍后再试=====================================");
				return list;
			}
			JSONObject object = JSONObject.fromObject(html);
			String s = object.getString("items");
			JSONArray object2 = JSONArray.fromObject(s);
			list = new ArrayList<TelecomLiaoNingPayThrem>();
			for(int i = 0 ; i < object2.size() ; i ++){
				TelecomLiaoNingPayThrem t = new TelecomLiaoNingPayThrem();
				JSONObject json = object2.getJSONObject(i);
				String service_id = json.getString("service_id");//帐号
				String payWayName = json.getString("payWayName");//付费方式
				String payKindName = json.getString("payKindName");//付费银行
				String pay_fee = json.getString("pay_fee");//金额
				String payTime = json.getString("payTime");//缴费时间
				t.setPay_fee(pay_fee);
				t.setPayKindName(payKindName);
				t.setPayTime(payTime);
				t.setPayWayName(payWayName);
				t.setService_id(service_id);
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			return list=null;
		}

	}
	public List<TelecomLiaoNingSMSThremResult> SMSThrem_parse(String html) {
		List<TelecomLiaoNingSMSThremResult> list = null;
		try {
			if (html.indexOf("系统正忙，请稍后再试") != -1) {
				System.out.println("============================系统正忙，请稍后再试=====================================");
				return list;
			}

			JSONObject object = JSONObject.fromObject(html);
			String SMS = object.getString("items");
			JSONArray json = JSONArray.fromObject(SMS);
			list = new ArrayList<TelecomLiaoNingSMSThremResult>();
			for (int i=0;i<json.size();i++) {
				TelecomLiaoNingSMSThremResult t = new TelecomLiaoNingSMSThremResult();
				JSONObject object2 = json.getJSONObject(i);
				String callPhone = object2.getString("callPhone");
				String beginDate = object2.getString("beginDate");
				String callType = object2.getString("callType");
				String feeKind = object2.getString("feeKind");
				String fee = object2.getString("fee");
				String total = object2.getString("total");
				String infoFee = object2.getString("infoFee");
				String monthFee = object2.getString("monthFee");
				t.setCallPhone(callPhone);
				t.setBeginDate(beginDate);
				if(callType.equals("上行")){
					t.setCallType("发信");
				} 
				if(callType.equals("下行")){
					t.setCallType("收信");
				}
				t.setFee(fee);
				t.setFeeKind(feeKind);
				t.setTotal(total);
				t.setInfoFee(infoFee);
				t.setMonthFee(monthFee);
				System.out.println("\r对方号码:"+callPhone+"\r发送时间:"+beginDate+"\r话单类型:"+callType+"\r短信类型:"+feeKind+
						"\r通信费:"+fee+"\r合计(元):"+total+"\r信息费:"+infoFee+"\r包月费:"+monthFee);
				list.add(t);
			}
			return list;
		} catch (Exception e) {
			return list;
		}

	}
	public List<TelecomLiaoNingPhoneschemes> phoneschemesResult_parse(String html) {
		List<TelecomLiaoNingPhoneschemes> list = null;
		try {
			JSONArray object = JSONArray.fromObject(html);

			list = new ArrayList<TelecomLiaoNingPhoneschemes>();

			for(int i = 0 ; i < object.size(); i++){
				TelecomLiaoNingPhoneschemes t = new TelecomLiaoNingPhoneschemes();
				JSONObject object2 = object.getJSONObject(i);
				String offerSpecName = object2.getString("businessName");//业务名称
				String offerDescription = object2.getString("offerDescription");//业务说明
				String startDt = object2.getString("startDt");//开通时间
				String endDt = object2.getString("endDt");//到期时间
				t.setEndDt(endDt);
				t.setOfferDescription(offerDescription);
				t.setStartDt(startDt);
				t.setOfferSpecName(offerSpecName);
				list.add(t);
			}
		} catch (Exception e) {
			return list;
		}
		return list;
	}
	public TelecomLiaoNingPointValue pointValue_parse(String html) {
		TelecomLiaoNingPointValue pc = null;
		try {
			JSONObject object = JSONObject.fromObject(html);
			String pointValue = object.getString("pointValue");//积分
			String consumPoint = object.getString("consumPoint");//消费积分
			pc = new TelecomLiaoNingPointValue();

			pc.setPointValue(pointValue);
			pc.setConsumPoint(consumPoint);
		} catch (Exception e) {
			return pc;
		}
		return pc;
	}

}
