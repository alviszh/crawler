package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouAccount;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouCallrecord;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouPaymonth;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouPoint;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouRecharges;
import com.microservice.dao.entity.crawler.telecom.guizhou.TelecomGuizhouSmsrecord;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomGuizhouBasicParser {
	// 解析账户信息
	public TelecomGuizhouAccount htmlAccountInfoParser(String html,TaskMobile taskMobile) {
		TelecomGuizhouAccount accountInfo = new TelecomGuizhouAccount();
		if (null != html && html.contains("Balance")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			if (html.contains("Balance")) {
				String balanceAmount = jsonInsurObjs.getString("Balance");
				String specAmount = jsonInsurObjs.getString("specAmount");
				String commonAmount = jsonInsurObjs.getString("commonAmount");
				String realOweAmount = jsonInsurObjs.getString("realOweAmount");
				accountInfo.setBalanceAmount(balanceAmount);
				accountInfo.setSpecAmount(specAmount);
				accountInfo.setCommonAmount(commonAmount);
				accountInfo.setRealOweAmount(realOweAmount);
				accountInfo.setTaskid(taskMobile.getTaskid());
			}
		}
		return accountInfo;
	}  
	
    //解析积分信息
	public TelecomGuizhouPoint htmlPointParser(String html, TaskMobile taskMobile) {
		TelecomGuizhouPoint point = new TelecomGuizhouPoint();
		if (null != html && html.contains("details")) {
			Document doc = Jsoup.parse(html, "utf-8");		
			Elements div= doc.getElementsByAttributeValue("class","details");
			if (null !=div && div.toString().contains("details")) {
				Elements tps = div.select("p");
				String status=tps.get(0).text();
				String canuse=tps.get(1).text();
				String expires=tps.get(2).text();
				
				String statusPoint[] = status.split("：");
				String canuseIntegral[] = canuse.split("：");
				String expiresIntegral[] = expires.split("：");
				point.setStatus(statusPoint[1]);
				point.setCanuseIntegral(canuseIntegral[1]);
				point.setExpiresIntegral(expiresIntegral[1]);
				point.setTaskid(taskMobile.getTaskid());
				taskMobile.setTaskid(taskMobile.getTaskid());
			}
		}
		return point;
	}
     //解析月账单信息
	public List<TelecomGuizhouPaymonth> htmlPaymonthParser(String html, TaskMobile taskMobile, String month,
			String phoneNum) {
		List<TelecomGuizhouPaymonth> paymonths = new ArrayList<TelecomGuizhouPaymonth>();
		if (null != html && html.contains("productInfo")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String productInfoStr = jsonInsurObjs.getString("productInfo");
			Document div = Jsoup.parse(productInfoStr, "utf-8");	
			Elements trs= div.getElementsByAttributeValue("class","xuanbg");
		    if (trs.size()>=1) {
		    	for (int i = 0; i < trs.size(); i++) {
		    		Elements tds = trs.get(i).select("td");
		    		String itemName=tds.get(0).text();
		    		String realAmout=tds.get(1).text();
		    		TelecomGuizhouPaymonth paymonth=new TelecomGuizhouPaymonth();
		    		paymonth.setCycle(month);
		    		paymonth.setItemName(itemName);
		    		paymonth.setRealAmout(realAmout);
		    		paymonth.setTaskid(taskMobile.getTaskid());
		    		paymonths.add(paymonth);
				}
			}		
		}
		return paymonths;
	}
     //解析充值信息
	public List<TelecomGuizhouRecharges> htmlRechargeRecordsParser(String html, TaskMobile taskMobile) {
		List<TelecomGuizhouRecharges> recharges = new ArrayList<TelecomGuizhouRecharges>();
		if (null != html && html.contains("records")) {
			Document doc = Jsoup.parse(html, "utf-8");		
			Element div= doc.getElementById("records");	
			if (null !=div) {
				Element table=div.select("table").get(0);			
				Elements trs=table.getElementsByAttributeValue("class","xuanbg");
				if (trs.size()>=1) {
					for (int i = 0; i < trs.size(); i++) {
						String paymentTime=trs.get(i).select("td").get(0).text();
						String paymentAmount=trs.get(i).select("td").get(1).text();
						String paymentChannel=trs.get(i).select("td").get(2).text();
						String paymentType=trs.get(i).select("td").get(3).text();
						String userRange=trs.get(i).select("td").get(4).text();
						TelecomGuizhouRecharges  recharge=new TelecomGuizhouRecharges();
						recharge.setPaymentTime(paymentTime);
						recharge.setPaymentAmount(paymentAmount);
						recharge.setPaymentChannel(paymentChannel);
						recharge.setPaymentType(paymentType);
						recharge.setUserRange(userRange);
						recharge.setTaskid(taskMobile.getTaskid());
						recharges.add(recharge);
					}				
				}
			}	
		}
		return recharges;
	}
   //解析通话详单信息
	public List<TelecomGuizhouCallrecord> htmlCallrecordsParser(String html, TaskMobile taskMobile) {
		List<TelecomGuizhouCallrecord> callrecords = new ArrayList<TelecomGuizhouCallrecord>();
		if (null != html && html.contains("CDMA_CALL_CDR")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String operationStatus = jsonInsurObjs.getString("OperationStatus");
			String records = jsonInsurObjs.getString("CDMA_CALL_CDR");			
			if (null !=records && "0".equals(operationStatus)) {
				if (!records.contains("[{")) {
					records="["+records+"]";
				}			
				JSONArray jsonArray = JSONArray.fromObject(records);
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(i));
					String cycle = list1ArrayObjs.getString("CYCLE");
					String callType = list1ArrayObjs.getString("CALLING_TYPE_NAME");
					String calledArea = list1ArrayObjs.getString("CALLED_AREA");
					String calledNum = list1ArrayObjs.getString("ORG_CALLED_NBR");
					String startDate = list1ArrayObjs.getString("START_DATE");
					String duration = list1ArrayObjs.getString("DURATION");
					String fee1 = list1ArrayObjs.getString("FEE1");
					String fee2 = list1ArrayObjs.getString("FEE2");
					String fee3 = list1ArrayObjs.getString("FEE3");
					String fee4 = list1ArrayObjs.getString("FEE4");
					String fee5 = list1ArrayObjs.getString("FEE5");

					TelecomGuizhouCallrecord callrecord = new TelecomGuizhouCallrecord();
					callrecord.setCycle(cycle);
					callrecord.setCallType(callType);
					callrecord.setCalledArea(calledArea);
					callrecord.setCalledNum(calledNum);
					callrecord.setStartDate(startDate);
					callrecord.setDuration(duration);
					callrecord.setFee1(fee1);
					callrecord.setFee2(fee2);
					callrecord.setFee3(fee3);
					callrecord.setFee4(fee4);
					callrecord.setFee5(fee5);
					callrecord.setTaskid(taskMobile.getTaskid());
					callrecords.add(callrecord);
				}
			}
		}
		return callrecords;
	}
   //解析短信详单信息
	public List<TelecomGuizhouSmsrecord> htmlSmsrecordsParser(String html, TaskMobile taskMobile) {
		List<TelecomGuizhouSmsrecord> smsrecords = new ArrayList<TelecomGuizhouSmsrecord>();
		if (null != html && html.contains("CDMA_SMS_CDR")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String operationStatus = jsonInsurObjs.getString("OperationStatus");
			String records = jsonInsurObjs.getString("CDMA_SMS_CDR");
			if (records !=null && "0".equals(operationStatus)) {
				if (!records.contains("[{")) {
					records="["+records+"]";
				}	
				JSONArray jsonArray = JSONArray.fromObject(records);
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(i));
					String cycle = list1ArrayObjs.getString("CYCLE");
					String name = list1ArrayObjs.getString("NAME");
					String calledArea = list1ArrayObjs.getString("CALLED_AREA");
					String calledNum = list1ArrayObjs.getString("ORG_CALLED_NBR");
					String startDate = list1ArrayObjs.getString("START_DATE");
					String fee = list1ArrayObjs.getString("FEE2");
					TelecomGuizhouSmsrecord smsrecord = new TelecomGuizhouSmsrecord();
					smsrecord.setCycle(cycle);
					smsrecord.setName(name);
					smsrecord.setCalledArea(calledArea);
					smsrecord.setCalledNum(calledNum);
					smsrecord.setStartDate(startDate);
					smsrecord.setFee(fee);
					smsrecord.setTaskid(taskMobile.getTaskid());
					smsrecords.add(smsrecord);
				}
			}
		}
		return smsrecords;
	}
}
