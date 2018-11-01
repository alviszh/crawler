package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapPaymonths;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapPointrecord;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapRecharges;
import com.microservice.dao.entity.crawler.telecom.hubei.wap.TelecomHubeiWapUserinfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class TelecomHubeiWapParser {	
	// 解析用户信息
	public TelecomHubeiWapUserinfo htmlUserInfoParser(String html, TaskMobile taskMobile) {
		TelecomHubeiWapUserinfo userinfo = new TelecomHubeiWapUserinfo();
		if (null != html && html.contains("serv")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String dataStr=jsonInsurObjs.getString("data");
			if (null !=dataStr) {			
				JSONObject JSONObject_data = JSONObject.fromObject(dataStr);
				String servStr=JSONObject_data.getString("serv");						
				JSONObject JSONObject_serv = JSONObject.fromObject(servStr);
				//解析用户信息
				String username=JSONObject_serv.getString("uSERNAME");
				String telephone=JSONObject_serv.getString("aCCNBR");				
				String address=JSONObject_serv.getString("aDDRESSDETAIL");
				//String identityType=JSONObject_serv.getString("aCCNBR");
				String identityNumber=JSONObject_serv.getString("cERTIFICATENO");
				String createDate=JSONObject_serv.getString("cOMPLETEDATE");		
				userinfo.setUsername(username);
				userinfo.setAddress(address);
				userinfo.setTelephone(telephone);
				userinfo.setIdentityType("身份证");
				userinfo.setAddress(address);
				userinfo.setIdentityNumber(identityNumber);			
				userinfo.setCreateDate(createDate);
				userinfo.setTaskid(taskMobile.getTaskid());
			}
		}
		return userinfo;
	}  
	
     //解析月账单信息
	public List<TelecomHubeiWapPaymonths> htmlPaymonthParser(String html, TaskMobile taskMobile, String month) {
		List<TelecomHubeiWapPaymonths> paymonths = new ArrayList<TelecomHubeiWapPaymonths>();
		if (null != html && html.contains("itemInforList")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String csbRStr=jsonInsurObjs.getString("csbR");
			JSONObject csbRObjs = JSONObject.fromObject(csbRStr);			
			String erviceInforStr=csbRObjs.getString("serviceInformationUniform");
			JSONObject billInforObjs = JSONObject.fromObject(erviceInforStr);	
			String billInforStr=billInforObjs.getString("billInforForUniform");		
			JSONObject itemInforListObjs = JSONObject.fromObject(billInforStr);	
			String itemInforListStr=itemInforListObjs.getString("itemInforList");
			JSONArray jsonArray = JSONArray.fromObject(itemInforListStr);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonArrayObjs = JSONObject.fromObject(jsonArray.get(i));
				String itemName = jsonArrayObjs.getString("charge_type_Name");
				String amount = jsonArrayObjs.getString("charge");
				TelecomHubeiWapPaymonths paymonth=new TelecomHubeiWapPaymonths();
				paymonth.setCycle(month);
				paymonth.setItemName(itemName);
				paymonth.setAmount(amount);
				paymonth.setTaskid(taskMobile.getTaskid());
				paymonths.add(paymonth);
			}
		}
		return paymonths;
	}
   
     //解析充值信息
	public List<TelecomHubeiWapRecharges> htmlRechargeRecordsParser(String html,String month,TaskMobile taskMobile) {
		List<TelecomHubeiWapRecharges> recharges = new ArrayList<TelecomHubeiWapRecharges>();
		if (null != html && html.contains("paymentRecordList")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String csbRStr=jsonInsurObjs.getString("csbR");			
			JSONObject csbRObjs = JSONObject.fromObject(csbRStr);
			String serInforStr=csbRObjs.getString("serInfor");			
			JSONObject serInforObjs = JSONObject.fromObject(serInforStr);
			String paymentRecordListStr=serInforObjs.getString("paymentRecordList");
			JSONArray jsonArray = JSONArray.fromObject(paymentRecordListStr);  
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(i));				
				String paymentTime = list1ArrayObjs.getString("stateDate");// 缴费时间			
				String payChannelId = list1ArrayObjs.getString("payChannelId");	// 充值渠道及方式		
				String paymentAmount = list1ArrayObjs.getString("payment_Amount");
				String paymentRange = list1ArrayObjs.getString("accNbrDetail");	// 使用范围		
				TelecomHubeiWapRecharges  recharge=new TelecomHubeiWapRecharges();
				recharge.setCycle(month);
				recharge.setPayChannelId(payChannelId);
				recharge.setPaymentTime(paymentTime);
				recharge.setPaymentAmount(paymentAmount);
				recharge.setPaymentRange(paymentRange);
				recharge.setTaskid(taskMobile.getTaskid());
				recharges.add(recharge);
			}
		}
		return recharges;
	}	
   //解析积分信息
	public TelecomHubeiWapPointrecord htmlPointRecordParser(String html,TaskMobile taskMobile) {
		TelecomHubeiWapPointrecord pointrecord = new TelecomHubeiWapPointrecord();
		if (null != html && html.contains("pointNow")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Element nowPointStr = doc.getElementById("pointNow");
			Element addPointMonthStr = doc.getElementById("addPointMonth");
			Element yearFailPointStr = doc.getElementById("yearFailPoint");
			Element changePointMonthStr = doc.getElementById("changePointMonth");
	    	String nowPoint=nowPointStr.text();
	    	String addPointMonth=addPointMonthStr.text();
	    	String yearFailPoint=yearFailPointStr.text();
	    	String changePointMonth=changePointMonthStr.text();
	    	pointrecord.setNowPoint(nowPoint);
	    	pointrecord.setAddPointMonth(addPointMonth);
	    	pointrecord.setYearFailPoint(yearFailPoint);
	    	pointrecord.setChangePointMonth(changePointMonth);
	    	pointrecord.setTaskid(taskMobile.getTaskid());
		}
		return pointrecord;
	}
   //解析通话详单信息
	public List<TelecomHubeiWapCallrecords> htmlCallrecordsParser(String html, String month,TaskMobile taskMobile) {
		List<TelecomHubeiWapCallrecords> callrecords = new ArrayList<TelecomHubeiWapCallrecords>();
		if (null != html && html.contains("billDetail")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String billDetailStr=jsonInsurObjs.getString("billDetail");		
			if (billDetailStr.contains("result")) {
				JSONObject resultObjs = JSONObject.fromObject(billDetailStr);
				String resultStr=resultObjs.getString("result");	
				resultStr=resultStr.replace("[\"", "");
				resultStr=resultStr.replace("\"]", "");
				String[] params = resultStr.split("\",\"");
				for (int i = 0; i < params.length; i++) {						
					String[] paramsStr = params[i].split(",");				
					for (int j = 0; j < paramsStr.length; j++) {
						String startDate=paramsStr[2];// 开始时间
						String calledArea=paramsStr[1];	// 呼叫地点			
						 String calledType=paramsStr[5];// 呼叫类型
						 String calledNum=paramsStr[0];//对方号码
						 String duration=paramsStr[3];// 通话时长
						 String type=paramsStr[9];// 漫游类型
						 String feeTotal=paramsStr[10];;// 费用
						 TelecomHubeiWapCallrecords callrecord=new TelecomHubeiWapCallrecords(); 
						 callrecord.setCycle(month);
						 callrecord.setTaskid(taskMobile.getTaskid());
						 callrecord.setStartDate(startDate);
						 callrecord.setCalledArea(calledArea);
						 callrecord.setCalledType(calledType);
						 callrecord.setCalledNum(calledNum);
						 callrecord.setDuration(duration);
						 callrecord.setType(type);
						 callrecord.setFeeTotal(feeTotal);
						 callrecords.add(callrecord);
					}
				}
			}		
		}
		return callrecords;
	}
   
}
