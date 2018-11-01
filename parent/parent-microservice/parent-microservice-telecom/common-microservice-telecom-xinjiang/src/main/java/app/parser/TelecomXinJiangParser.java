package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangAddvalueItem;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPayMonths;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangPointRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangProductInfo;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangRealtimeFee;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangRechargeRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangSmsRecord;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangUserInfo;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecord;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class TelecomXinJiangParser {
  public TelecomXinjiangUserInfo htmlUserInfoParser(String html) {
	    TelecomXinjiangUserInfo userInfo = new TelecomXinjiangUserInfo();
		if (null != html && html.contains("custInfo") ) {
			JSONArray jsonArray = JSONArray.fromObject(html);
			String userInfoStr = jsonArray.getString(0);
			if (null !=userInfoStr) {
				JSONObject jsonInsurObjs = JSONObject.fromObject(userInfoStr);		
				if (null != jsonInsurObjs && jsonInsurObjs.toString().contains("custInfo")) {
					String userInfoString = jsonInsurObjs.getString("custInfo");
					JSONObject userObject = JSONObject.fromObject(userInfoString);
					String username = userObject.getString("custName");
					String certType = userObject.getString("custType");
					String certNumber = userObject.getString("certNumber");
					String contactPhone = userObject.getString("contactPhone");
					String contactAddress = userObject.getString("contactAddrecs");
					String contactEmail = userObject.getString("custEmail");
					String postCod = userObject.getString("postCode");
					String createDate = userObject.getString("createDate");
					userInfo.setUsername(username);
					userInfo.setCertType(certType);
					userInfo.setCertNumber(certNumber);
					userInfo.setContactPhone(contactPhone);
					userInfo.setContactAddress(contactAddress);
					userInfo.setContactEmail(contactEmail);
					userInfo.setPostCod(postCod);
					userInfo.setCreateDate(createDate);
				}				
			}			
		}
		return userInfo;
	}

	public TelecomXinjiangProductInfo htmlProductInfoParser(String html) {
		TelecomXinjiangProductInfo productInfo = new TelecomXinjiangProductInfo();
		if (null != html && html.contains("prodList")) {
			JSONArray jsonArray = JSONArray.fromObject(html);  
			String jsonInsur = jsonArray.getString(0);			
			JSONObject jsonInsurObjs = JSONObject.fromObject(jsonInsur);
			String proStr=jsonInsurObjs.getString("prodList");
			JSONArray jsonArray2 = JSONArray.fromObject(proStr);  
			JSONObject productObj = JSONObject.fromObject(jsonArray2.get(0));
			String areaCode=productObj.getString("areaCode");
			String proNumber=productObj.getString("serialNum");
			String proAddress=productObj.getString("installAddress");
			String proType=productObj.getString("prodName");
			String proAccount=productObj.getString("accountNum");
			
			productInfo.setArea(areaCode);
			productInfo.setProNumber(proNumber);
			productInfo.setProAccount(proAccount);
			productInfo.setProAddress(proAddress);
			productInfo.setProType(proType);
		}
		return productInfo;
	}
	
	public List<TelecomXinjiangRealtimeFee> htmlRealtimeFeeParser(String html,TaskMobile taskMobile) {
		List<TelecomXinjiangRealtimeFee> realtimeFees = new ArrayList<TelecomXinjiangRealtimeFee>();
		if (null != html && html.contains("list1")) {
			JSONArray jsonArray = JSONArray.fromObject(html);  
			String jsonInsur = jsonArray.getString(0);			
			JSONObject jsonInsurObjs = JSONObject.fromObject(jsonInsur);
			String realtimeFeeStr=jsonInsurObjs.getString("list1");
			JSONArray jsonArrayReal = JSONArray.fromObject(realtimeFeeStr);
			for(int i=0;i<jsonArrayReal.size();i++){			
			   JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArrayReal.get(i));
			   String feeName=list1ArrayObjs.getString("zfmc");
			   String amount=list1ArrayObjs.getString("fylx");
			    TelecomXinjiangRealtimeFee realtimeFee=new TelecomXinjiangRealtimeFee();
				realtimeFee.setFeeName(feeName);
				realtimeFee.setAmount(amount);
				realtimeFee.setType("1");
				realtimeFee.setTaskid(taskMobile.getTaskid());
				realtimeFees.add(realtimeFee);
			}
			String accountFeeStr=jsonInsurObjs.getString("list");
			JSONArray jsonArrayAccount = JSONArray.fromObject(accountFeeStr);
			for(int i=0;i<jsonArrayAccount.size();i++){			
			   JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArrayAccount.get(i));
			   String feeName=list1ArrayObjs.getString("zfmc");
			   String amount=list1ArrayObjs.getString("fylx");
			    TelecomXinjiangRealtimeFee realtimeFee=new TelecomXinjiangRealtimeFee();
				realtimeFee.setFeeName(feeName);
				realtimeFee.setAmount(amount);
				realtimeFee.setType("0");
				realtimeFee.setTaskid(taskMobile.getTaskid());
				realtimeFees.add(realtimeFee);
				}		
		}	
		return realtimeFees;
	}
	
	public List<TelecomXinjiangRechargeRecord> htmlRechargeRecordsParser(String html,TaskMobile taskMobile) {
		List<TelecomXinjiangRechargeRecord> rechargeRecords = new ArrayList<TelecomXinjiangRechargeRecord>();
		if (null != html && html.contains("payLiushu")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String msgStr = jsonInsurObjs.getString("msg");		
		    if (null !=msgStr) {
		    	JSONArray jsonArray = JSONArray.fromObject(msgStr);
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject jsonArrayObjs = JSONObject.fromObject(jsonArray.get(i));
					String serialNumber = jsonArrayObjs.getString("payLiushu");
					String accountTime = jsonArrayObjs.getString("payTime");
					String accountAmount = jsonArrayObjs.getString("payMoney");
					String accountWay = jsonArrayObjs.getString("payPlace");
					String accountType = jsonArrayObjs.getString("payType");
					String accountScope = jsonArrayObjs.getString("payPeople");
					TelecomXinjiangRechargeRecord RechargeRecord = new TelecomXinjiangRechargeRecord();
					RechargeRecord.setSerialNumber(serialNumber);
					RechargeRecord.setAccountAmount(accountAmount);
					RechargeRecord.setAccountTime(accountTime);
					RechargeRecord.setAccountType(accountType);
					RechargeRecord.setAccountWay(accountWay);
					RechargeRecord.setAccountScope(accountScope);
					RechargeRecord.setTaskid(taskMobile.getTaskid());
					rechargeRecords.add(RechargeRecord);
				}
			}
		}
		return rechargeRecords;
	}
	public List<TelecomXinjiangVoiceRecord> htmlVoiceRecordsParser(String html,TaskMobile taskMobile) {
		List<TelecomXinjiangVoiceRecord> voiceRecords = new ArrayList<TelecomXinjiangVoiceRecord>();
		if (null != html && html.contains("result")) {
			JSONArray jsonArray = JSONArray.fromObject(html);  
			JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));			
			String result=list1ArrayObjs.getString("result");
		    if("1".equals(result)){
		    	String listInfoStr=list1ArrayObjs.getString("listInfo");
				JSONArray listInfoArray = JSONArray.fromObject(listInfoStr);  
				for(int i=0;i<listInfoArray.size();i++){
					JSONObject listArrayObjs = JSONObject.fromObject(listInfoArray.get(i));
					String startTime=listArrayObjs.getString("table1");
					String talkTime=listArrayObjs.getString("table2");
					String dataReduce=listArrayObjs.getString("table3");
					String type=listArrayObjs.getString("table4");
					String calledNumber=listArrayObjs.getString("table5");
					String callPalce=listArrayObjs.getString("table6");
					String callType=listArrayObjs.getString("table7");
					String basicFee=listArrayObjs.getString("table8");
					String longFee=listArrayObjs.getString("table9");
					String otherFee=listArrayObjs.getString("table10");
					String benefit=listArrayObjs.getString("table11");
					String total=listArrayObjs.getString("titles");
					TelecomXinjiangVoiceRecord voiceRecord=new TelecomXinjiangVoiceRecord();
					voiceRecord.setStartTime(startTime);
					voiceRecord.setTalkTime(talkTime);
					voiceRecord.setDataReduce(dataReduce);
					voiceRecord.setType(type);
					voiceRecord.setCallType(callType);
					voiceRecord.setCalledNumber(calledNumber);
					voiceRecord.setCallPalce(callPalce);
					voiceRecord.setBasicFee(basicFee);
					voiceRecord.setLongFee(longFee);
					voiceRecord.setOtherFee(otherFee);
					voiceRecord.setBenefit(benefit);
					voiceRecord.setTotal(total);
					voiceRecord.setTaskid(taskMobile.getTaskid());
					voiceRecords.add(voiceRecord);
				}
		    }
		}
	   return voiceRecords;
	}
	
	public List<TelecomXinjiangPointRecord>  htmlPointParser(String html,TaskMobile taskMobile) {
		List<TelecomXinjiangPointRecord> points = new  ArrayList<TelecomXinjiangPointRecord>();
		if (null != html && html.contains("resList")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String resList = jsonInsurObjs.getString("resList");
			JSONArray jsonArray = JSONArray.fromObject(resList);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONArray jsonArrayObjs = JSONArray.fromObject(jsonArray.get(i));
				List<String> strs = new ArrayList<String>();
				for (Object object : jsonArrayObjs) {
					String str = object.toString();
					strs.add(str);
				}
				TelecomXinjiangPointRecord point = new TelecomXinjiangPointRecord();
				point.setAccountDate(strs.get(0));
				point.setCostPoint(strs.get(1));
				point.setRewardPoints(strs.get(2));
				point.setTotal(strs.get(3));
				point.setTaskid(taskMobile.getTaskid());
				points.add(point);
			}

		}
	   return points;
	}
	
	public List<TelecomXinjiangSmsRecord> htmlSmsRecordsParser(String html,TaskMobile taskMobile) {
		List<TelecomXinjiangSmsRecord> smsRecords = new ArrayList<TelecomXinjiangSmsRecord>();
		if (null != html && html.contains("result")) {
			JSONArray jsonArray = JSONArray.fromObject(html);  
			JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));
			String result=list1ArrayObjs.getString("result");
			if ("1".equals(result)) {
				String listInfoStr=list1ArrayObjs.getString("listInfo");
				JSONArray listInfoArray = JSONArray.fromObject(listInfoStr);  
				for(int i=0;i<listInfoArray.size();i++){
					JSONObject listArrayObjs = JSONObject.fromObject(listInfoArray.get(i));
					String smsType=listArrayObjs.getString("table1");
					String smsNumber=listArrayObjs.getString("table2");
					String sendTime=listArrayObjs.getString("table3");
					String longStatus=listArrayObjs.getString("table4");
					String totalFee=listArrayObjs.getString("table5");
					String dataReduce=listArrayObjs.getString("table6");
					
					TelecomXinjiangSmsRecord smsRecord =new TelecomXinjiangSmsRecord();
					smsRecord.setSmsType(smsType);		
					smsRecord.setSmsNumber(smsNumber);
					smsRecord.setSendTime(sendTime);
					smsRecord.setLongStatus(longStatus);
					smsRecord.setTotalFee(totalFee);
					smsRecord.setDataReduce(dataReduce);
					smsRecord.setTaskid(taskMobile.getTaskid());
					smsRecords.add(smsRecord);
				}
			}		
		}
	   return smsRecords;
	}
	
	public List<TelecomXinjiangAddvalueItem> htmladdvalueItemsParser(String html,TaskMobile taskMobile) {
		List<TelecomXinjiangAddvalueItem> addvalueItems = new ArrayList<TelecomXinjiangAddvalueItem>();
		if (null != html && html.contains("result")) {
			JSONArray jsonArray = JSONArray.fromObject(html);  
			JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(0));
			String result=list1ArrayObjs.getString("result");
			if ("1".equals(result)) {
				String list1InfoStr=list1ArrayObjs.getString("list1");
				String listInfoStr=list1ArrayObjs.getString("list");
				JSONArray list1InfoArray = JSONArray.fromObject(list1InfoStr);  
				JSONArray listInfoArray = JSONArray.fromObject(listInfoStr);  
				for(int i=0;i<list1InfoArray.size();i++){
					JSONObject listArrayObjs = JSONObject.fromObject(list1InfoArray.get(i));
					String serviceName=listArrayObjs.getString("zfmc");
					String serviceFee=listArrayObjs.getString("fylx");
					String effectiveDate=listArrayObjs.getString("sxsjj");
					String failureDate=listArrayObjs.getString("sxsj");				
					TelecomXinjiangAddvalueItem addvalueItem=new TelecomXinjiangAddvalueItem();
					addvalueItem.setServiceName(serviceName);
					addvalueItem.setServiceFee(serviceFee);
					addvalueItem.setEffectiveDate(effectiveDate);
					addvalueItem.setFailureDate(failureDate);
					addvalueItem.setType("1");
					addvalueItem.setTaskid(taskMobile.getTaskid());
					addvalueItems.add(addvalueItem);
				}
				for(int i=0;i<listInfoArray.size();i++){
					JSONObject listArrayObjs = JSONObject.fromObject(listInfoArray.get(i));
					String serviceName=listArrayObjs.getString("zfmc");
					String serviceFee=listArrayObjs.getString("fylx");
					String effectiveDate=listArrayObjs.getString("sxsjj");
					String failureDate=listArrayObjs.getString("sxsj");			
					TelecomXinjiangAddvalueItem addvalueItem=new TelecomXinjiangAddvalueItem();
					addvalueItem.setServiceName(serviceName);
					addvalueItem.setServiceFee(serviceFee);
					addvalueItem.setEffectiveDate(effectiveDate);
					addvalueItem.setFailureDate(failureDate);
					addvalueItem.setType("0");
					addvalueItem.setTaskid(taskMobile.getTaskid());
					addvalueItems.add(addvalueItem);
				}
			}
		}
	   return addvalueItems;
	}
	public List<TelecomXinjiangPayMonths> htmlPayMonthsParser(String html,String month,TaskMobile taskMobile) {
		List<TelecomXinjiangPayMonths> payMonths = new ArrayList<TelecomXinjiangPayMonths>();
		if (null != html && html.contains("two")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
		 	String twoStr=jsonInsurObjs.getString("two");
			  JSONArray jsonArray = JSONArray.fromObject(twoStr);
		    	for (int i = 0; i < jsonArray.size(); i++) {
		    		JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(i));
		    		String itemValue= list1ArrayObjs.getString("name");
		    		String itemName= list1ArrayObjs.getString("value");
		    		TelecomXinjiangPayMonths  payMonth=new TelecomXinjiangPayMonths();
		    		payMonth.setItemName(itemName);
		    		payMonth.setItemValue(itemValue);
		    		payMonth.setMonth(month);
		    		payMonth.setTaskid(taskMobile.getTaskid());
		    		payMonths.add(payMonth);
		    	}
		}
	   return payMonths;
	}
}
