package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiAccount;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiCallrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPaymonths;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiPointrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiRecharges;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiServices;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiSmsrecords;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiUserinfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class TelecomHubeiParser {	
	// 解析用户信息
	public TelecomHubeiUserinfo htmlUserInfoParser(String html, TaskMobile taskMobile) {
		TelecomHubeiUserinfo userinfo = new TelecomHubeiUserinfo();
		if (null != html && html.contains("yhnc1")) {
			Document doc = Jsoup.parse(html, "utf-8");
			Element userInfotab = doc.getElementById("showTable01");
			if (null !=userInfotab) {
				Elements userInfo_trs = userInfotab.select("tbody").select("tr");					
				String username=userInfo_trs.get(0).select("td").get(1).text();				
				String nickname="";
				if (null !=userInfotab.getElementById("nickname")) {
				   nickname=userInfotab.getElementById("nickname").text();
				}
				String identityType=userInfo_trs.get(1).select("td").get(1).text();
				String identityNumber=userInfotab.getElementById("zjhmli1").text();
				String contactNumber=userInfo_trs.get(2).select("td").get(1).text();
				String emailAddress=userInfotab.getElementById("txdzli1").text();
				String zipcode=userInfotab.getElementById("yzbmli1").text();
				String email=userInfotab.getElementById("emailli1").text();
				String qq=userInfotab.getElementById("qqhmli1").text();
				String weibo=userInfotab.getElementById("wbli1").text();
				String createDate=userInfotab.getElementById("cjrqli1").text();				
				userinfo.setUsername(username);
				userinfo.setNickname(nickname);
				userinfo.setIdentityType(identityType);
				userinfo.setIdentityNumber(identityNumber);
				userinfo.setContactNumber(contactNumber);
				userinfo.setEmailAddress(emailAddress);
				userinfo.setZipcode(zipcode);
				userinfo.setEmail(email);
				userinfo.setQq(qq);
				userinfo.setWeibo(weibo);
				userinfo.setCreateDate(createDate);
				userinfo.setTaskid(taskMobile.getTaskid());
			}
		}
		return userinfo;
	}  
	// 解析账户信息
	public TelecomHubeiAccount htmlAccountInfoParser(String html,TaskMobile taskMobile) {
		TelecomHubeiAccount accountinfo = new TelecomHubeiAccount();
		if (null != html) {		
			String str[]=html.split(",");
			if (str.length==22) {
				String availableAmount=str[2];
				String currentAvailableAmount=str[2];
				String dueTotalAmount=str[2];
				String realtimeAmount=str[2];
				accountinfo.setAvailableAmount(availableAmount);
				accountinfo.setCurrentAvailableAmount(currentAvailableAmount);
				accountinfo.setDueTotalAmount(dueTotalAmount);
				accountinfo.setRealtimeAmount(realtimeAmount);
				accountinfo.setTaskid(taskMobile.getTaskid());
			}
		}
		return accountinfo;
	}  
     //解析月账单信息
	public List<TelecomHubeiPaymonths> htmlPaymonthParser(String html, TaskMobile taskMobile, String month) {
		List<TelecomHubeiPaymonths> paymonths = new ArrayList<TelecomHubeiPaymonths>();
		if (null != html && html.contains("lszd_month") && html.contains("月基本费")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Elements div= doc.getElementsByAttributeValue("align","left");
			Elements eles =div.select("tr:contains(月基本费)"); 
			if (null !=eles && eles.size()>0) {
				Element ele1 = eles.get(0).nextElementSibling(); 
				Elements trs1=ele1.select("tr");
				for (int i = 0; i < trs1.size(); i++) {				
					String itemName=trs1.get(i).select("td").get(0).text();
					String amount=trs1.get(i).select("td").get(1).text();
					TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
					paymonth.setCycle(month);
					paymonth.setItemName(itemName);
					paymonth.setAmount(amount);
					paymonth.setTaskid(taskMobile.getTaskid());
					paymonths.add(paymonth);
				}
			}			
		
			Elements eles2 =div.select("tr:contains(代收费)"); 
		  	if (null !=eles2 && eles2.size() >0) { 
				Element ele12 = eles2.get(0).nextElementSibling(); 
				if (null !=ele12) {
					Elements trs2=ele12.select("tr");
					for (int i = 0; i < trs2.size(); i++) {				
						String itemName=trs2.get(i).select("td").get(0).text();
						String amount=trs2.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}	
			}
							
			Elements eles3 =div.select("tr:contains(语音通信费)"); 
			if (null !=eles3  && eles3.size() >0) {
				Element ele13 = eles3.get(0).nextElementSibling(); 
				if (null !=ele13) {
					Elements trs3=ele13.select("tr");
					for (int i = 0; i < trs3.size(); i++) {				
						String itemName=trs3.get(i).select("td").get(0).text();
						String amount=trs3.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}
			}	
			
			Elements eles4 =div.select("tr:contains(语音通信费)"); 
			if (null !=eles4  && eles4.size() >0) {
				Element eles14 = eles4.get(0).nextElementSibling(); 
				if (null !=eles14) {
					Elements trs4=eles14.select("tr");
					for (int i = 0; i < trs4.size(); i++) {				
						String itemName=trs4.get(i).select("td").get(0).text();
						String amount=trs4.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}
			}		
			Elements eles5 =div.select("tr:contains(短信彩信费)"); 
			if (null !=eles5  && eles5.size() >0) {
				Element eles15 = eles5.get(0).nextElementSibling(); 
				if (null !=eles15) {
					Elements trs5=eles15.select("tr");
					for (int i = 0; i < trs5.size(); i++) {				
						String itemName=trs5.get(i).select("td").get(0).text();
						String amount=trs5.get(i).select("td").get(1).text();
						TelecomHubeiPaymonths  paymonth=new TelecomHubeiPaymonths();
						paymonth.setCycle(month);
						paymonth.setItemName(itemName);
						paymonth.setAmount(amount);
						paymonth.setTaskid(taskMobile.getTaskid());
						paymonths.add(paymonth);
					}	
				}
			}			
		}
		return paymonths;
	}
    //解析订购的增值服务及功能信息
	public List<TelecomHubeiServices> htmlServiceinfosParser(String html, TaskMobile taskMobile) {
		List<TelecomHubeiServices> serviceinfos = new ArrayList<TelecomHubeiServices>();
		if (null != html && html.contains("hovergray")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Elements div= doc.getElementsByAttributeValue("class","main_biao");		
			if (null !=div) {
				Elements  table=div.select("table");				
				if (null !=table) {
					Element	table1=table.get(0);					
					if (null !=table1) {
						Elements  trs=table1.getElementsByAttributeValue("class","hovergray");	
						if (null !=trs) {
							for (int i = 0; i < trs.size(); i++) {			
								Elements tds=trs.get(i).select("td");
								String itemName=tds.get(0).text();
								String amount=tds.get(1).text();
								String startDate=tds.get(2).text();
								String operate=tds.get(3).text();
								TelecomHubeiServices  serviceinfo=new  TelecomHubeiServices();
								serviceinfo.setItemName(itemName);
								serviceinfo.setAmount(amount);
								serviceinfo.setStartDate(startDate);
								serviceinfo.setOperate(operate);
								serviceinfo.setType("1");
								serviceinfo.setTaskid(taskMobile.getTaskid());
								serviceinfos.add(serviceinfo);
							}	
						}						
					}				
					Element table2=table.get(1);
					if (null !=table2) {
						Elements  trs2=table2.getElementsByAttributeValue("class","hovergray");
						if (null !=trs2) {
							for (int i = 0;i < trs2.size(); i++) {
								Elements tds=trs2.get(i).select("td");
								String itemName=tds.get(0).text();
								String startDate=tds.get(1).text();
								String operate=tds.get(2).text();
								TelecomHubeiServices  serviceinfo=new  TelecomHubeiServices();
								serviceinfo.setItemName(itemName);
								serviceinfo.setStartDate(startDate);
								serviceinfo.setOperate(operate);
								serviceinfo.setType("0");
								serviceinfo.setTaskid(taskMobile.getTaskid());
								serviceinfos.add(serviceinfo);
							}
						}						
					}				
				}			
			}		
		}
		return serviceinfos;
	}
     //解析充值信息
	public List<TelecomHubeiRecharges> htmlRechargeRecordsParser(String html,String month,TaskMobile taskMobile) {
		List<TelecomHubeiRecharges> recharges = new ArrayList<TelecomHubeiRecharges>();
		if (null != html && html.contains("resultList")) {
			JSONObject jsonInsurObjs = JSONObject.fromObject(html);
			String resultList=jsonInsurObjs.getString("resultList");
			JSONArray jsonArray = JSONArray.fromObject(resultList);  
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject list1ArrayObjs = JSONObject.fromObject(jsonArray.get(i));
				String reqSerial = list1ArrayObjs.getString("Req_Serial");
				String stateDate = list1ArrayObjs.getString("StateDate");
				String payChannelId = list1ArrayObjs.getString("PayChannelId");
				String paymentMethod = list1ArrayObjs.getString("Payment_Method");
				String paymentAmount = list1ArrayObjs.getString("Payment_Amount");
				String accNbrDetail = list1ArrayObjs.getString("AccNbrDetail");			
				TelecomHubeiRecharges  recharge=new TelecomHubeiRecharges();
				recharge.setCycle(month);
				recharge.setReqSerial(reqSerial);
				recharge.setStateDate(stateDate);
				recharge.setPayChannelId(payChannelId);
				recharge.setPaymentAmount(paymentAmount);
				recharge.setPaymentMethod(paymentMethod);
				recharge.setAccNbrDetail(accNbrDetail);
				recharge.setTaskid(taskMobile.getTaskid());
				recharges.add(recharge);
			}
		}
		return recharges;
	}	
   //解析积分信息
	public List<TelecomHubeiPointrecords> htmlPointRecordsParser(String html,String month,TaskMobile taskMobile) {
		List<TelecomHubeiPointrecords> pointrecords = new ArrayList<TelecomHubeiPointrecords>();
		if (null != html && html.contains("idtable")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Element pontTable= doc.getElementById("idtable");
			if (null !=pontTable) {
				Elements trs=pontTable.select("tr");
				int trs_size=trs.size();
				if (trs_size>1) {
					for (int i = 1; i < trs_size; i++) {
						TelecomHubeiPointrecords  pointrecord=new  TelecomHubeiPointrecords();
						String pointValue=trs.get(i).select("td").get(1).text();
						String startTime=trs.get(i).select("td").get(2).text();
						String remark=trs.get(i).select("td").get(3).text();
						pointrecord.setAvailableAmount(month);
						pointrecord.setTaskid(taskMobile.getTaskid());
						pointrecord.setPointValue(pointValue);
						pointrecord.setStartTime(startTime);
						pointrecord.setRemark(remark);
						pointrecords.add(pointrecord);
					}					
				}
			}		
		}
		return pointrecords;
	}
   //解析通话详单信息
	public List<TelecomHubeiCallrecords> htmlCallrecordsParser(String html, String month,TaskMobile taskMobile) {
		List<TelecomHubeiCallrecords> callrecords = new ArrayList<TelecomHubeiCallrecords>();
		if (null != html && html.contains("xd01")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Element div= doc.getElementById("xd01");	
			if (null !=div) {
				Element table=div.select("table").get(1);			
				Elements trs=table.getElementsByAttributeValue("class","hovergray1");
				if (trs.size()>=3) {
					for (int i = 0; i < trs.size()-2; i++) {
						String startDate=trs.get(i).select("td").get(0).text();
						String calledNum=trs.get(i).select("td").get(1).text();
						String duration=trs.get(i).select("td").get(2).text();
						String calledType=trs.get(i).select("td").get(3).text();
						String type=trs.get(i).select("td").get(4).text();
						String dataNum=trs.get(i).select("td").get(5).text();
						String calledArea=trs.get(i).select("td").get(6).text();					
						String callChangeNum=trs.get(i).select("td").get(7).text();
						String feeType=trs.get(i).select("td").get(8).text();
						String feeTotal=trs.get(i).select("td").get(9).text();
						TelecomHubeiCallrecords callrecord=new TelecomHubeiCallrecords();
						callrecord.setCycle(month);
						callrecord.setStartDate(startDate);
						callrecord.setCalledNum(calledNum);
						callrecord.setDuration(duration);				
						callrecord.setCalledType(calledType);
						callrecord.setType(type);
						callrecord.setDataNum(dataNum);
						callrecord.setCalledArea(calledArea);
						callrecord.setCalledNum(calledNum);
						callrecord.setCallChangeNum(callChangeNum);
						callrecord.setFeeType(feeType);				
						callrecord.setFeeTotal(feeTotal);
						callrecord.setTaskid(taskMobile.getTaskid());
						callrecords.add(callrecord);			
					}
				}	
			}			
		}
		return callrecords;
	}
   //解析短信详单信息
	public List<TelecomHubeiSmsrecords> htmlSmsrecordsParser(String html,String month, TaskMobile taskMobile) {
		List<TelecomHubeiSmsrecords> smsrecords = new ArrayList<TelecomHubeiSmsrecords>();
		if (null != html && html.contains("xd01")) {
			Document doc = Jsoup.parse(html, "utf-8");			
			Element div= doc.getElementById("xd01");	
			Element table=div.select("table").get(1);			
			Elements trs=table.getElementsByAttributeValue("class","hovergray1");
			if (trs.size()>=3) {
				for (int i = 0; i < trs.size()-2; i++) {
					String calledNum=trs.get(i).select("td").get(0).text();
					String sendTime=trs.get(i).select("td").get(1).text();
					String feeType=trs.get(i).select("td").get(2).text();
					String fee=trs.get(i).select("td").get(3).text();
					String dataNum=trs.get(i).select("td").get(4).text();
					TelecomHubeiSmsrecords smsrecord=new TelecomHubeiSmsrecords();
					smsrecord.setCycle(month);			
					smsrecord.setSendTime(sendTime);
					smsrecord.setCalledNum(calledNum);
					smsrecord.setFeeType(feeType);
					smsrecord.setFee(fee);
					smsrecord.setDataNum(dataNum);
					smsrecord.setTaskid(taskMobile.getTaskid());
					smsrecords.add(smsrecord);
				}
			}		
		}
		return smsrecords;
	}
}
