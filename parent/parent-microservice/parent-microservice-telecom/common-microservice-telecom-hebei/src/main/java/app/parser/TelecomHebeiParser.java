package app.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiAccount;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiCallRec;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiMsg;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiPackage;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiPayfee;
import com.microservice.dao.entity.crawler.telecom.hebei.TelecomHebeiUserInfo;

import app.commontracerlog.TracerLog;

@Component
public class TelecomHebeiParser {
	
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 解析用户信息
	 * @param doc
	 * @param taskid
	 * @return
	 */
	public TelecomHebeiUserInfo parserUserinfo(Document doc, String taskid) {
		
		TelecomHebeiUserInfo telecomHebeiUserInfo = new TelecomHebeiUserInfo();
		
		String name = parserField(doc,"th:contains(客户名称)");
		String starLevel = parserField(doc,"th:contains(客户星级)");
		String city = parserField(doc,"th:contains(所属城市)");
		String serviceStatus = parserField(doc,"th:contains(服务状态)");
		String customerLevel = parserField(doc,"th:contains(客户级别)");
		String certificateType = parserField(doc,"th:contains(证件类型)");
		String certificateNum = parserField(doc,"th:contains(证件号码)");
		String address = parserField(doc,"th:contains(客户地址)");
		
		telecomHebeiUserInfo.setAddress(address);
		telecomHebeiUserInfo.setCertificateNum(certificateNum);
		telecomHebeiUserInfo.setCertificateType(certificateType);
		telecomHebeiUserInfo.setCity(city);
		telecomHebeiUserInfo.setCustomerLevel(customerLevel);
		telecomHebeiUserInfo.setName(name);
		telecomHebeiUserInfo.setServiceStatus(serviceStatus);
		telecomHebeiUserInfo.setStarLevel(starLevel);
		telecomHebeiUserInfo.setTaskid(taskid);
		
		tracer.addTag("crawler.telecom.hebei.userinfo", telecomHebeiUserInfo.toString());
		
		return telecomHebeiUserInfo;
	}
	
	
	/**
	 * @Des 根据select选择器，抽取元素
	 * @param doc
	 * @param rule
	 * @return
	 */
	public String parserField(Element doc, String rule){
		Elements es = doc.select(rule);
		if(null != es && es.size()>0){
			Element element = es.first();
			Element nextElement = element.nextElementSibling();
			if(null != nextElement){
				return nextElement.text();
			}
		}
		return null;
		
	}
	
	/**
	 * @Des 根据select选择器，抽取元素
	 * @param table
	 * @param rule
	 * @param i
	 * @param id
	 * @return
	 */
	public String parserField(Element table, String rule, int i, String id){
		Elements es = table.select(rule);
		if(null != es && es.size()>0){
			Element element = es.get(i).nextElementSibling();
			if(null != element){
				element = element.getElementById(id);
				return element.text();			
			}
		}
		return null;
		
	}


	/**
	 * @Des 解析套餐详情
	 * @param doc
	 * @param taskid
	 * @return
	 */
	public TelecomHebeiPackage parserPackage(Document doc, String taskid, String mobile) {
		
		TelecomHebeiPackage telecomHebeiPackage = new TelecomHebeiPackage();
		
		Element table = doc.getElementById("tab_prodinfo_"+mobile);
		if(null != table){
			String productNum = parserField(table,"td:contains(产品号)");
			String productName = parserField(table,"td:contains(产品名称)");
			String packageName = parserField(table,"td:contains(产品套餐)");
			String status = parserField(table,"td:contains(状态)");
			String netTime = parserField(table,"td:contains(入网时间)");
			String packageDetail = parserField(table,"td:contains(套餐详细介绍)");
			
			telecomHebeiPackage.setNetTime(netTime);
			telecomHebeiPackage.setPackageDetail(packageDetail);
			telecomHebeiPackage.setPackageName(packageName);
			telecomHebeiPackage.setProductName(productName);
			telecomHebeiPackage.setProductNum(productNum);
			telecomHebeiPackage.setStatus(status);
			telecomHebeiPackage.setTaskid(taskid);
			
			tracer.addTag("crawler.telecom.hebei.package", telecomHebeiPackage.toString());			
			return telecomHebeiPackage;
		}else{
			return null;
		}
		
	}


	/**
	 * @Des 解析缴费详情
	 * @param doc
	 * @param taskid
	 * @return
	 */
	public List<TelecomHebeiPayfee> parserPayfee(Document doc, String taskid) {
		
		List<TelecomHebeiPayfee> list = new ArrayList<TelecomHebeiPayfee>();
		
		Elements trs = doc.getElementsByClass("c1");
		if(null != trs && trs.size()>0){
			for(Element tr : trs){
				TelecomHebeiPayfee telecomHebeiPayfee = new TelecomHebeiPayfee();
				String phoneNum = tr.child(0).text();
				if(StringUtils.isNotBlank(phoneNum)){
					phoneNum = tr.child(0).text();
					String payType = tr.child(1).text();
					String payBank = tr.child(2).text();
					String transactionAmount = tr.child(3).text();
					String payTime = tr.child(4).text();
					
					telecomHebeiPayfee.setPayBank(payBank);
					telecomHebeiPayfee.setPayTime(payTime);
					telecomHebeiPayfee.setPayType(payType);
					telecomHebeiPayfee.setPhoneNum(phoneNum);
					telecomHebeiPayfee.setTransactionAmount(transactionAmount);
					telecomHebeiPayfee.setTaskid(taskid);
					
					list.add(telecomHebeiPayfee);
					tracer.addTag("crawler.telecom.hebei.payfee", telecomHebeiPayfee.toString());
				}
			}
		}
				 
		return list;
	}


	/**
	 * @Des 解析账单详情
	 * @param doc
	 * @return
	 */
	public TelecomHebeiAccount parserAccount(Document doc, String taskid, String month) {
		TelecomHebeiAccount telecomHebeiAccount = new TelecomHebeiAccount();
		
		Element table = doc.getElementsByClass("tableLeft").first();
		if(null != table){
			String monthlyBasePay = parserField(table,"td:contains(月基本费)",1,"parent_1_5_4");
			String total = parserField(table,"td:contains(本项小计)",0,"parent_1_5_4");
			String communicationPay = parserField(table,"td:contains(语音通信费)",0,"parent_1_5_4");
			String longDistancePay = parserField(table,"td:contains(国内长途通话费)",0,"parent_1_5_4");
			String localPay = parserField(table,"td:contains(本地通话费)",0,"parent_1_5_4");
			String msgPay = parserField(table,"td:contains(短信费)",0,"parent_1_5_4");
			String mmsPay = parserField(table,"td:contains(短信彩信费)",0,"parent_1_5_4");
			String internetPay = parserField(table,"td:contains(手机上网费)",0,"parent_1_5_4");
			
			telecomHebeiAccount.setCommunicationPay(communicationPay);
			telecomHebeiAccount.setLocalPay(localPay);
			telecomHebeiAccount.setLongDistancePay(longDistancePay);
			telecomHebeiAccount.setMonthlyBasePay(monthlyBasePay);
			telecomHebeiAccount.setTotal(total);
			telecomHebeiAccount.setMonth(month);
			telecomHebeiAccount.setTaskid(taskid);
			telecomHebeiAccount.setMmsPay(mmsPay);
			telecomHebeiAccount.setMsgPay(msgPay);
			telecomHebeiAccount.setInternetPay(internetPay);
			
			tracer.addTag("crawler.telecom.hebei.account", telecomHebeiAccount.toString());
			return telecomHebeiAccount;			
		}else{
			return null;
		}
	}


	/**
	 * @Des 解析通话记录
	 * @param doc
	 * @param task_id
	 * @param mon
	 * @return
	 */
	public List<TelecomHebeiCallRec> parserCallRec(Document doc, String task_id) {
		
		List<TelecomHebeiCallRec> list = new ArrayList<TelecomHebeiCallRec>();
		Elements trs = doc.getElementsByTag("tr");
		if(null != trs && trs.size()>2){
			for(int i=1;i<trs.size();i++){
				String otherNum = trs.get(i).child(1).text();
				String callType = trs.get(i).child(2).text();
				String callTime = trs.get(i).child(3).text();
				String callDuration = trs.get(i).child(4).text();
				String reducedUse = trs.get(i).child(5).text();
				String feeType = trs.get(i).child(6).text();
				String total = trs.get(i).child(7).text();
				
				TelecomHebeiCallRec telecomHebeiCallRec = new TelecomHebeiCallRec();
//				telecomHebeiCallRec.setBasicFee(basicFee);
				telecomHebeiCallRec.setCallDuration(callDuration);
				telecomHebeiCallRec.setCallTime(callTime);
				telecomHebeiCallRec.setCallType(callType);
//				telecomHebeiCallRec.setDiscountsFee(discountsFee);
				telecomHebeiCallRec.setFeeType(feeType);
//				telecomHebeiCallRec.setLongDistanceFee(longDistanceFee);
//				telecomHebeiCallRec.setOtherFee(otherFee);
				telecomHebeiCallRec.setOtherNum(otherNum);
				telecomHebeiCallRec.setReducedUse(reducedUse);
				telecomHebeiCallRec.setTotal(total);
				telecomHebeiCallRec.setTaskid(task_id);
				
				list.add(telecomHebeiCallRec);
			}
		}
		return list;
	}


	/**
	 * @Des 解析短信信息
	 * @param doc
	 * @param task_id
	 * @param mon
	 * @return
	 */
	public List<TelecomHebeiMsg> parserMsgRec(Document doc, String task_id, String mon) {
		
		List<TelecomHebeiMsg> list = new ArrayList<TelecomHebeiMsg>();
		
		Elements tbodys = doc.getElementsByTag("tbody");
		if(null != tbodys && tbodys.size()>0){
			Elements trs = tbodys.first().select("tr");	
			if(null != trs && trs.size()>0){
				
				Element tr = trs.first();
				TelecomHebeiMsg telecomHebeiMsg = new TelecomHebeiMsg();
				
				String otherNum = tr.child(1).text();
				String msgType = tr.child(2).text();
				String msgTime = tr.child(3).text();
				String correspondence = tr.child(4).text();
				String reducedUse = tr.child(5).text();
				String msgPay = tr.child(6).text();
				String discountsFee = tr.child(7).text();
				String packageCosts = tr.child(8).text();
				String total = tr.child(9).text();
				
				telecomHebeiMsg.setCorrespondence(correspondence);
				telecomHebeiMsg.setDiscountsFee(discountsFee);
				telecomHebeiMsg.setMsgPay(msgPay);
				telecomHebeiMsg.setMsgTime(msgTime);
				telecomHebeiMsg.setMsgType(msgType);
				telecomHebeiMsg.setOtherNum(otherNum);
				telecomHebeiMsg.setPackageCosts(packageCosts);
				telecomHebeiMsg.setReducedUse(reducedUse);
				telecomHebeiMsg.setTotal(total);
				telecomHebeiMsg.setTaskid(task_id);
				
				list.add(telecomHebeiMsg);
			}
		}
		
		return list;
	}


	public String parserCityCode(String contentAsString) {
		System.out.println("获得城市代码页面："+contentAsString);
		String code = null;
		String regex = "doQuery[^,]*,'(.*)'?";
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(contentAsString);
		if(m.find()){
			String cityCode = m.group(0);
			code = cityCode.split(",")[1].replace("'", "");
		}
	
		return code;
	}


}
