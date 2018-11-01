package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Balance;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3BalanceDetail;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3BalanceSum;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Business;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3CallRecord;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3ChargeInfo;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3CurrentSituation;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3FamilyCall;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3Integra;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3MonthBill;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3SMSRecord;
import com.microservice.dao.entity.crawler.telecom.shanxi3.TelecomShanxi3UserInfo;

import app.domain.WebParam;
import app.service.common.CalendarParamService;
import app.service.common.CodeTypeEnum;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @Description  陕西电信相关信息的具体解析
 * @author sln
 * @date 2017年8月21日 下午6:18:02
 */
@Component
public class TelecomShanXi3Parser {
	/**
	 * 解析积分信息页面
	 * @param html
	 * @param taskMobile
	 * @param yearmonth
	 * @return
	 */
	public List<TelecomShanxi3Integra> integraParser(String html, TaskMobile taskMobile,String yearmonth) {
		
		List<TelecomShanxi3Integra> list=new ArrayList<TelecomShanxi3Integra>();
		TelecomShanxi3Integra telecomShanxi3Integra=null;
		JSONArray arrObj = JSONArray.fromObject(html);
		JSONObject jsonObj = JSONObject.fromObject(arrObj.get(0));
		String pointType=jsonObj.getString("pointType");
		//根据获取的json串中代表积分类型的数字来找到对应具体的消费类型
		String integraTypeName = CodeTypeEnum.getIntegraTypeName(pointType);
		String pointValue=jsonObj.getString("pointValue");
		telecomShanxi3Integra=new TelecomShanxi3Integra();
		telecomShanxi3Integra.setTaskid(taskMobile.getTaskid());
		telecomShanxi3Integra.setIntegra(pointValue);
		telecomShanxi3Integra.setType(integraTypeName);
		telecomShanxi3Integra.setYearmonth(yearmonth);
		list.add(telecomShanxi3Integra);
		return list;
	}
	/**
	 * 解析亲情号信息页面
	 * @param html
	 * @param taskMobile
	 * @param firstMonthdate
	 * @param lastMonthdate
	 * @return
	 */
	public List<TelecomShanxi3FamilyCall> familyCallParser(String html, TaskMobile taskMobile, String firstMonthdate,String lastMonthdate) {
		Document doc=Jsoup.parse(html);
		TelecomShanxi3FamilyCall telecomShanxi3FamilyCall=null;
		List<TelecomShanxi3FamilyCall> list=new ArrayList<TelecomShanxi3FamilyCall>();
		Elements elementsByTag = doc.getElementsByTag("tr");
		int size = elementsByTag.size();
		for(int i=1;i<size;i++){
			String text = elementsByTag.get(i).text();
			String[] split = text.split(" ");
			telecomShanxi3FamilyCall=new TelecomShanxi3FamilyCall();
			telecomShanxi3FamilyCall.setOwnnum(split[0]);
			telecomShanxi3FamilyCall.setFamilynum(split[1]);
			telecomShanxi3FamilyCall.setOriginatingcalltime(split[2]);
			telecomShanxi3FamilyCall.setTerminatingcalltime(split[3]);
			telecomShanxi3FamilyCall.setTotalcalltime(split[4]);
			telecomShanxi3FamilyCall.setBelongmonth(firstMonthdate+"至"+lastMonthdate);
			telecomShanxi3FamilyCall.setTaskid(taskMobile.getTaskid());
			list.add(telecomShanxi3FamilyCall);
		}
		return list;
	}
	/**
	 * 解析通话记录页面
	 * @param html
	 * @param firstMonthdate
	 * @param lastMonthdate
	 * @return
	 */
	public List<TelecomShanxi3CallRecord> callRecordParser(TaskMobile taskMobile,String html) {
		Document doc=Jsoup.parse(html);
		TelecomShanxi3CallRecord telecomShanxi3CallRecord=null;
		List<TelecomShanxi3CallRecord> list=new ArrayList<TelecomShanxi3CallRecord>();
		Elements elementsByTag = doc.getElementsByTag("tr");
		int size = elementsByTag.size();
		for(int i=1;i<size;i++){
			String text = elementsByTag.get(i).text();
			String[] split = text.split(" ");
			telecomShanxi3CallRecord=new TelecomShanxi3CallRecord();
			if(split.length>9){
				telecomShanxi3CallRecord.setConvertedflow(split[9]);  //折算流量在页面上不是空值
			}else{
				telecomShanxi3CallRecord.setConvertedflow("");  //折算流量在页面上是空值，故此处设置为空
			}
			telecomShanxi3CallRecord.setCalladdress(split[3]);
			telecomShanxi3CallRecord.setCalltype(split[4]);
			telecomShanxi3CallRecord.setCosttime(split[6]);
			telecomShanxi3CallRecord.setLinktype(split[7]);
			telecomShanxi3CallRecord.setTaskid(taskMobile.getTaskid());
			telecomShanxi3CallRecord.setOthernum(split[5]);
			telecomShanxi3CallRecord.setSortnum(split[0]);
			telecomShanxi3CallRecord.setStarttime(split[1]+" "+split[2]);  //用空格切割导致日期和时间分离，故组合
			telecomShanxi3CallRecord.setTotalcharge(split[8]);
			telecomShanxi3CallRecord.setTotalexpenses(doc.getElementsByTag("strong").text());
			list.add(telecomShanxi3CallRecord);
		}
		return list;
	}
	/**
	 * 短信记录的爬取
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public List<TelecomShanxi3SMSRecord> SMSRecordParser(TaskMobile taskMobile, String html) {
		Document doc=Jsoup.parse(html);
		TelecomShanxi3SMSRecord telecomShanxi3SMSRecord=null;
		List<TelecomShanxi3SMSRecord> list=new ArrayList<TelecomShanxi3SMSRecord>();
		Elements elementsByTag = doc.getElementsByTag("tr");
		int size = elementsByTag.size();
		for(int i=1;i<size;i++){
			String text = elementsByTag.get(i).text();
			String[] split = text.split(" ");
			telecomShanxi3SMSRecord=new TelecomShanxi3SMSRecord();
			if(split.length>6){
				telecomShanxi3SMSRecord.setConvertedflow(split[5]);  //折算流量在页面上不是空值
				telecomShanxi3SMSRecord.setRoamstatus(split[6]);
			}else{
				telecomShanxi3SMSRecord.setConvertedflow("");  //折算流量在页面上是空值，故此处设置为空
				telecomShanxi3SMSRecord.setRoamstatus(split[5]);
			}
			telecomShanxi3SMSRecord.setCommunicationtype(split[0]);
			telecomShanxi3SMSRecord.setOthernum(split[1]);
			telecomShanxi3SMSRecord.setSendtime(split[2]+" "+split[3]);
			telecomShanxi3SMSRecord.setTotalcost(split[4]);
			telecomShanxi3SMSRecord.setTotalexpenses(doc.getElementsByTag("strong").text());
			telecomShanxi3SMSRecord.setTaskid(taskMobile.getTaskid());
			list.add(telecomShanxi3SMSRecord);
		}
		return list;
	}
	/**
	 * 业务信息解析
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public List<TelecomShanxi3Business> businessParser(TaskMobile taskMobile, String html) {
		Document doc=Jsoup.parse(html);
		TelecomShanxi3Business telecomShanxi3Business=null;
		List<TelecomShanxi3Business> list=new ArrayList<TelecomShanxi3Business>();
		Elements select = doc.select("tbody#tb_thead");
		for (Element element : select) {
			telecomShanxi3Business=new TelecomShanxi3Business();
			telecomShanxi3Business.setBusinessname(element.getElementsByTag("tr").get(0).getElementsByTag("td").get(0).text());
			telecomShanxi3Business.setAccessnum(element.getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text());
			telecomShanxi3Business.setServicearea(element.getElementsByTag("tr").get(0).getElementsByTag("td").get(2).text());
			telecomShanxi3Business.setStatus(element.getElementsByTag("tr").get(0).getElementsByTag("td").get(3).text());
			telecomShanxi3Business.setAccountname(element.getElementsByTag("tr").get(0).getElementsByTag("td").get(4).text());
			telecomShanxi3Business.setFlowcarrystatus(element.getElementsByTag("tr").get(0).getElementsByTag("td").get(5).text());
			telecomShanxi3Business.setBuytime(element.getElementsByTag("tr").get(0).getElementsByTag("td").get(6).text());
			telecomShanxi3Business.setTaskid(taskMobile.getTaskid());
			list.add(telecomShanxi3Business);
		}
		return list;
	}
	
	public WebParam<TelecomShanxi3CurrentSituation> currentSituationParser(TaskMobile taskMobile, String htmlHuaFei,String htmlTaoCan) {
		WebParam<TelecomShanxi3CurrentSituation> webParam=new WebParam<TelecomShanxi3CurrentSituation>();
		TelecomShanxi3CurrentSituation telecomShanxi3CurrentSituation=new TelecomShanxi3CurrentSituation();
		//解析话费信息
		telecomShanxi3CurrentSituation.setThismonthcharge(JSONObject.fromObject(htmlHuaFei).getJSONObject("obj").getString("month_charge"));
		telecomShanxi3CurrentSituation.setTotalintegra(JSONObject.fromObject(htmlHuaFei).getJSONObject("obj").getString("myjifen"));
		//解析套餐信息
		telecomShanxi3CurrentSituation.setRemainflow(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonFlow").getJSONObject("Surplus").getString("value"));
		telecomShanxi3CurrentSituation.setRemainsms(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonMessage").getJSONObject("Surplus").getString("value"));
		telecomShanxi3CurrentSituation.setRemainvoice(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonVoice").getJSONObject("Surplus").getString("value"));
		telecomShanxi3CurrentSituation.setTaskid(taskMobile.getTaskid());
		webParam.setTelecomShanxi3CurrentSituation(telecomShanxi3CurrentSituation);
		return webParam;
	}
	/**
	 * 解析用户信息
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public WebParam<TelecomShanxi3UserInfo> userInfoParser(TaskMobile taskMobile, String html) {
		WebParam<TelecomShanxi3UserInfo> webParam=new WebParam<TelecomShanxi3UserInfo>();
		Document doc=Jsoup.parse(html);
		TelecomShanxi3UserInfo telecomShanxi3UserInfo=new TelecomShanxi3UserInfo();
		telecomShanxi3UserInfo.setCity(doc.select("th:contains(所在地市)+td").first().text());
		telecomShanxi3UserInfo.setContactnum(doc.getElementById("contactNum").val());
		telecomShanxi3UserInfo.setCusname(doc.select("th:contains(客户名称)+td").first().text());
		telecomShanxi3UserInfo.setDetailaddress(doc.getElementById("contactAddress_bak").val());
		telecomShanxi3UserInfo.setEmail(doc.select("th:contains(Email)+td").first().text());
		telecomShanxi3UserInfo.setPostalcode(doc.select("th:contains(邮政编码)+td").first().text());
		
		telecomShanxi3UserInfo.setOpenbank(doc.select("th:contains(开户银行)+td").first().text());
		telecomShanxi3UserInfo.setCompaddress(doc.getElementById("companyAddress").val());
		telecomShanxi3UserInfo.setCompcontactnum(doc.select("th:contains(联系电话)+td").first().text());
		telecomShanxi3UserInfo.setTaxnum(doc.select("th:contains(纳税登记号)+td").first().text());
		telecomShanxi3UserInfo.setTaxaccountnum(doc.getElementById("companyAddress").text());
		telecomShanxi3UserInfo.setIsgeneraltax(doc.select("th:contains(是否一般纳税)+td").first().text());
		
		telecomShanxi3UserInfo.setTaskid(taskMobile.getTaskid());
	
		webParam.setTelecomShanxi3UserInfo(telecomShanxi3UserInfo);
		return webParam;
	}
	/**
	 * 解析充值记录信息      要注意有的数据保留两位小数
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public List<TelecomShanxi3ChargeInfo> chargeInfoParser(TaskMobile taskMobile, String html,String month) {
		List<TelecomShanxi3ChargeInfo> list=new ArrayList<TelecomShanxi3ChargeInfo>();
		TelecomShanxi3ChargeInfo telecomShanxi3ChargeInfo=null;
		if(!html.contains("[]")){  //说明有数据
			JSONObject jsonObj = JSONObject.fromObject(html);
			JSONArray jsonArray = jsonObj.getJSONArray("paymentRecordInfos");
			int size = jsonArray.size();  
			for(int i=0;i<size;i++){
				telecomShanxi3ChargeInfo=new TelecomShanxi3ChargeInfo();
				telecomShanxi3ChargeInfo.setChargechanal(CodeTypeEnum.getChargeChannelName(jsonArray.getJSONObject(i).getString("payChannelId")));
				telecomShanxi3ChargeInfo.setChargemoney((Double.parseDouble(jsonArray.getJSONObject(i).getString("paymentAmount"))/100)+"");
				telecomShanxi3ChargeInfo.setChargetime(jsonArray.getJSONObject(i).getString("stateDate").substring(0,8));
				telecomShanxi3ChargeInfo.setChargeway(CodeTypeEnum.getChargeWayName(jsonArray.getJSONObject(i).getString("paymentMethod")));
				telecomShanxi3ChargeInfo.setFlownum(jsonArray.getJSONObject(i).getString("reqSerial"));
				telecomShanxi3ChargeInfo.setUsescope("账户通用");
				telecomShanxi3ChargeInfo.setTaskid(taskMobile.getTaskid());
				telecomShanxi3ChargeInfo.setQuerycycle(month);
				list.add(telecomShanxi3ChargeInfo);
			}
		}
		return list;
	}
	//解析月账单
	public List<TelecomShanxi3MonthBill> getMonthBill(MessageLogin messageLogin,TaskMobile taskMobile, String html,String yearmonth) {
		List<TelecomShanxi3MonthBill> list=new ArrayList<TelecomShanxi3MonthBill>();
		List<String> tempList=null;;
		TelecomShanxi3MonthBill telecomShanxi3MonthBill=null;
		String phoneNum=messageLogin.getName();
		Elements eles = Jsoup.parse(html).select("tr:contains(天翼手机:"+phoneNum+")"); 
		Element ele1 = eles.get(0).nextElementSibling(); 
		if(ele1.text().contains("套餐月基本费")){ 
			String text = ele1.toString();
			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			sb.append(text);
			sb.append("</table>");
			Document doc1=Jsoup.parse(sb.toString());
			Elements elementsByTag = doc1.getElementsByTag("td");
			int size = elementsByTag.size();
			tempList=new ArrayList<String>();
			for(int i=1;i<size;i++){	
				tempList.add(elementsByTag.get(i).text());
			}
			telecomShanxi3MonthBill=new TelecomShanxi3MonthBill();
			telecomShanxi3MonthBill.setBeforediscount(tempList.get(0));
			telecomShanxi3MonthBill.setDiscount(tempList.get(1));
			telecomShanxi3MonthBill.setTotalcost(tempList.get(2));
			telecomShanxi3MonthBill.setPrintdate(CalendarParamService.getPresentDate());
			telecomShanxi3MonthBill.setTaskid(taskMobile.getTaskid());
			telecomShanxi3MonthBill.setCountmonth(yearmonth);
			telecomShanxi3MonthBill.setPhonenum(phoneNum);
			telecomShanxi3MonthBill.setExpensename("套餐月基本费");
			list.add(telecomShanxi3MonthBill);
		} 
		Element ele2 = ele1.nextElementSibling(); 
		if(ele2.text().contains("来电显示费")){ 
			String text = ele2.toString();
			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			sb.append(text);
			sb.append("</table>");
			Document doc2=Jsoup.parse(sb.toString());
			Elements elementsByTag = doc2.getElementsByTag("td");
			int size = elementsByTag.size();
			tempList=new ArrayList<String>();
			for(int i=1;i<size;i++){	
				tempList.add(elementsByTag.get(i).text());
			}
			telecomShanxi3MonthBill=new TelecomShanxi3MonthBill();
			telecomShanxi3MonthBill.setBeforediscount(tempList.get(0));
			telecomShanxi3MonthBill.setDiscount(tempList.get(1));
			telecomShanxi3MonthBill.setTotalcost(tempList.get(2));
			telecomShanxi3MonthBill.setPrintdate(CalendarParamService.getPresentDate());
			telecomShanxi3MonthBill.setTaskid(taskMobile.getTaskid());
			telecomShanxi3MonthBill.setCountmonth(yearmonth);
			telecomShanxi3MonthBill.setPhonenum(phoneNum);
			telecomShanxi3MonthBill.setExpensename("来电显示费");
			list.add(telecomShanxi3MonthBill);
		} 
		
		Element ele3 = ele2.nextElementSibling(); 
		if(ele3.text().contains("代收费")){ 
			String text = ele3.toString();
			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			sb.append(text);
			sb.append("</table>");
			Document doc3=Jsoup.parse(sb.toString());
			Elements elementsByTag = doc3.getElementsByTag("td");
			int size = elementsByTag.size();
			tempList=new ArrayList<String>();
			for(int i=1;i<size;i++){	
				tempList.add(elementsByTag.get(i).text());
			}
			telecomShanxi3MonthBill=new TelecomShanxi3MonthBill();
			telecomShanxi3MonthBill.setBeforediscount(tempList.get(0));
			telecomShanxi3MonthBill.setDiscount(tempList.get(1));
			telecomShanxi3MonthBill.setTotalcost(tempList.get(2));
			telecomShanxi3MonthBill.setPrintdate(CalendarParamService.getPresentDate());
			telecomShanxi3MonthBill.setTaskid(taskMobile.getTaskid());
			telecomShanxi3MonthBill.setCountmonth(yearmonth);
			telecomShanxi3MonthBill.setPhonenum(phoneNum);
			telecomShanxi3MonthBill.setExpensename("代收费");
			list.add(telecomShanxi3MonthBill);
		} 
		Element ele4 = ele3.nextElementSibling(); 
		if(ele4.text().contains("短信服务费")){ 
			String text = ele4.toString();
			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			sb.append(text);
			sb.append("</table>");
			Document doc4=Jsoup.parse(sb.toString());
			Elements elementsByTag = doc4.getElementsByTag("td");
			int size = elementsByTag.size();
			tempList=new ArrayList<String>();
			for(int i=1;i<size;i++){	
				tempList.add(elementsByTag.get(i).text());
			}
			telecomShanxi3MonthBill=new TelecomShanxi3MonthBill();
			telecomShanxi3MonthBill.setBeforediscount(tempList.get(0));
			telecomShanxi3MonthBill.setDiscount(tempList.get(1));
			telecomShanxi3MonthBill.setTotalcost(tempList.get(2));
			telecomShanxi3MonthBill.setPrintdate(CalendarParamService.getPresentDate());
			telecomShanxi3MonthBill.setTaskid(taskMobile.getTaskid());
			telecomShanxi3MonthBill.setCountmonth(yearmonth);
			telecomShanxi3MonthBill.setPhonenum(phoneNum);
			telecomShanxi3MonthBill.setExpensename("短信服务费");
			list.add(telecomShanxi3MonthBill);
		}
		return list;
	}
	
	//可用余额明细信息   解析
	public List<TelecomShanxi3BalanceDetail> balanceDetailParser(TaskMobile taskMobile, String html,String month) {
		List<TelecomShanxi3BalanceDetail> list=new ArrayList<TelecomShanxi3BalanceDetail>();
		TelecomShanxi3BalanceDetail telecomShanxi3BalanceDetail=null;
		if(!html.contains("[]")){  //说明有数据
//			String substring = html.substring(1, html.length()-1);
			JSONObject jsonObj = JSONObject.fromObject(html);
			JSONArray jsonArray = jsonObj.getJSONArray("BalanceRecordDetailInfos");
			int size = jsonArray.size();  
			for(int i=0;i<size;i++){
				telecomShanxi3BalanceDetail=new TelecomShanxi3BalanceDetail();
				telecomShanxi3BalanceDetail.setMonth(jsonArray.getJSONObject(i).getString("billingCycle"));
				telecomShanxi3BalanceDetail.setBalancetype(CodeTypeEnum.getBalanceTypeName(jsonArray.getJSONObject(i).getString("balanceTypeFlag")));
				telecomShanxi3BalanceDetail.setBalanceout((Double.parseDouble(jsonArray.getJSONObject(i).getString("balanceOut"))/100)+"");
				telecomShanxi3BalanceDetail.setBalance((Double.parseDouble(jsonArray.getJSONObject(i).getString("balanceEnd"))/100)+"");
				telecomShanxi3BalanceDetail.setBalancein((Double.parseDouble(jsonArray.getJSONObject(i).getString("balanceIn"))/100)+"");
				telecomShanxi3BalanceDetail.setChangetype(CodeTypeEnum.getBalanceChangeName(jsonArray.getJSONObject(i).getString("balanceChangeType")));
				telecomShanxi3BalanceDetail.setUsescope("账户通用");
				telecomShanxi3BalanceDetail.setQuerycycle(month);
				telecomShanxi3BalanceDetail.setTaskid(taskMobile.getTaskid());
				list.add(telecomShanxi3BalanceDetail);
			}
		}
		return list;
	}
	//可用余额汇总信息  解析
	public List<TelecomShanxi3BalanceSum> balanceSumParser(TaskMobile taskMobile, String html,String month) {
		List<TelecomShanxi3BalanceSum> list=new ArrayList<TelecomShanxi3BalanceSum>();
		TelecomShanxi3BalanceSum telecomShanxi3BalanceSum=null;
		if(!html.contains("[]")){  //说明有数据
//			String substring = html.substring(1, html.length()-1);
			JSONObject jsonObj = JSONObject.fromObject(html);
			JSONArray jsonArray = jsonObj.getJSONArray("balanceRecordInfos");
			int size = jsonArray.size();  
			for(int i=0;i<size;i++){
				telecomShanxi3BalanceSum=new TelecomShanxi3BalanceSum();
				telecomShanxi3BalanceSum.setBalancetype(CodeTypeEnum.getBalanceTypeName(jsonArray.getJSONObject(i).getString("balanceTypeFlag")));
				telecomShanxi3BalanceSum.setLastbalance((Double.parseDouble(jsonArray.getJSONObject(i).getString("balanceBegin"))/100)+"");
				telecomShanxi3BalanceSum.setQuerycycle(jsonArray.getJSONObject(i).getString("billingCycle"));
				telecomShanxi3BalanceSum.setTaskid(taskMobile.getTaskid());
				telecomShanxi3BalanceSum.setThisbalance((Double.parseDouble(jsonArray.getJSONObject(i).getString("balanceEnd"))/100)+"");
				telecomShanxi3BalanceSum.setThisbalancein((Double.parseDouble(jsonArray.getJSONObject(i).getString("balanceIn"))/100)+"");
				telecomShanxi3BalanceSum.setThisbalanceout((Double.parseDouble(jsonArray.getJSONObject(i).getString("balanceOut"))/100)+"");
				telecomShanxi3BalanceSum.setTaskid(taskMobile.getTaskid());
				list.add(telecomShanxi3BalanceSum);
			}
		}
		return list;
	}
	public List<TelecomShanxi3Balance> balanceParser(TaskMobile taskMobile, String html) {
		List<String> list0=new ArrayList<String>();
		List<TelecomShanxi3Balance> list=new ArrayList<TelecomShanxi3Balance>();
		TelecomShanxi3Balance telecomShanxi3Balance=new TelecomShanxi3Balance();
		JSONObject jsonObj = JSONObject.fromObject(html);
		JSONArray jsonArray = jsonObj.getJSONObject("balance").getJSONArray("balanceInformations");
		String totalBalance = (Double.parseDouble(jsonObj.getJSONObject("balance").getString("totalBalanceAvailable"))/100)+"";
		int size = jsonArray.size();
		for(int i=0;i<size;i++){
			list0.add(i,jsonArray.getJSONObject(i).getString("balance"));
		}
		telecomShanxi3Balance.setCommonbalance((Double.parseDouble(list0.get(0))/100)+"");
		telecomShanxi3Balance.setSpecialbalance((Double.parseDouble(list0.get(1))/100)+"");
		telecomShanxi3Balance.setTaskid(taskMobile.getTaskid());
		telecomShanxi3Balance.setTotalbalance(totalBalance);
		list.add(telecomShanxi3Balance);
		return list;
	}
	
}
