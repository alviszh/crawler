package app.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiBalance;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiBusiness;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiCallRecord;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiChargeInfo;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiCurrentSituation;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiIntegra;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiMonthBill;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiSMSRecord;
import com.microservice.dao.entity.crawler.telecom.jiangxi.TelecomJiangxiUserInfo;

import app.domain.WebParam;
import app.service.common.CalendarParamService;
import net.sf.json.JSONObject;


/**
 * 
 * @Description: 江西电信相关信息的具体解析
 * @author sln
 * @date 2017年9月16日
 */
@Component
public class TelecomJiangxiParser {
	public static final Logger log = LoggerFactory.getLogger(TelecomJiangxiParser.class);
	/**
	 * 用户信息解析
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public WebParam<TelecomJiangxiUserInfo> userInfoParser(TaskMobile taskMobile, String html) {
		WebParam<TelecomJiangxiUserInfo> webParam=new WebParam<TelecomJiangxiUserInfo>();
		Document doc=Jsoup.parse(html);
		TelecomJiangxiUserInfo telecomJiangxiUserInfo=new TelecomJiangxiUserInfo();
		telecomJiangxiUserInfo.setTaskid(taskMobile.getTaskid());
		telecomJiangxiUserInfo.setContactname(doc.getElementById("CONTACT_NAME").val());
		telecomJiangxiUserInfo.setContactnum(doc.getElementById("MOBILE_PHONE").val());
		telecomJiangxiUserInfo.setCusaddress(doc.getElementById("CUST_ADDR").text());
		telecomJiangxiUserInfo.setCusname(doc.getElementById("CUST_NAME").val());
		telecomJiangxiUserInfo.setEmail(doc.getElementById("EMAIL_ADDRESS").val());
		telecomJiangxiUserInfo.setIdnum(doc.getElementById("CERT_NUM").val());
//		doc.getElementsByAttributeValue("selected", "selected").get(0).text()    获取证件类型，这样写也可以
		telecomJiangxiUserInfo.setIdtype(doc.getElementsByTag("option").attr("selected", "selected").get(0).text());
		telecomJiangxiUserInfo.setLinkaddress(doc.getElementById("POST_ADDR").text());
		telecomJiangxiUserInfo.setPostalcode(doc.getElementById("POST_CODE").val());
		webParam.setTelecomJiangxiUserInfo(telecomJiangxiUserInfo);
		return webParam;
	}
	/**
	 * 解析积分信息
	 * @param html
	 * @param taskMobile
	 * @param qryMonth 
	 * @return
	 */
	public List<TelecomJiangxiIntegra> integraParser(String html, TaskMobile taskMobile, String qryMonth) {
		List<TelecomJiangxiIntegra>	list=new ArrayList<TelecomJiangxiIntegra>();
		TelecomJiangxiIntegra telecomJiangxiIntegra=new TelecomJiangxiIntegra();
		if(!html.contains("暂无信息")){
			Document doc = Jsoup.parse(html);
			telecomJiangxiIntegra.setBonusintegra(doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(1).text());
			telecomJiangxiIntegra.setCreditintegra(doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(2).getElementsByTag("td").get(1).text());
			telecomJiangxiIntegra.setMonthintegra(doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(0).getElementsByTag("td").get(1).text());
			telecomJiangxiIntegra.setMultipintegra(doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(2).getElementsByTag("td").get(3).text());
			telecomJiangxiIntegra.setNetworkintegra(doc.getElementsByTag("table").get(0).getElementsByTag("tr").get(1).getElementsByTag("td").get(3).text());
			telecomJiangxiIntegra.setQrymonth(qryMonth);
			telecomJiangxiIntegra.setTaskid(taskMobile.getTaskid());
			list.add(telecomJiangxiIntegra);
		}
		return list;
	}
	/**
	 * 解析业务信息
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public List<TelecomJiangxiBusiness> businessParser(TaskMobile taskMobile, String html) {
		HashSet<String> hset=new HashSet<String>();
		List<TelecomJiangxiBusiness> list=new ArrayList<TelecomJiangxiBusiness>();
		Document doc = Jsoup.parse(html);
		TelecomJiangxiBusiness TelecomJiangxiBusiness=null;
		Elements elementsByTag = doc.getElementById("commbus").getElementsByTag("tbody").get(0).getElementsByTag("tr");
		int size = elementsByTag.size();
		for(int i=1;i<size-1;i++){   //业务去重
			hset.add(elementsByTag.get(i).getElementsByTag("td").get(0).text());
		}
		for(String businessName:hset){
			TelecomJiangxiBusiness=new TelecomJiangxiBusiness();
			TelecomJiangxiBusiness.setBusinessname(businessName);
			TelecomJiangxiBusiness.setTaskid(taskMobile.getTaskid());
			list.add(TelecomJiangxiBusiness);
		}
		return list;
	}
	/**
	 * 解析首页我的现状信息
	 * @param taskMobile
	 * @param htmlHuaFei
	 * @param htmlTaoCan
	 * @return
	 */
	public WebParam<TelecomJiangxiCurrentSituation> currentSituationParser(TaskMobile taskMobile, String htmlHuaFei,String htmlTaoCan) {
		WebParam<TelecomJiangxiCurrentSituation> webParam=new WebParam<TelecomJiangxiCurrentSituation>();
		TelecomJiangxiCurrentSituation telecomJiangxiCurrentSituation=new TelecomJiangxiCurrentSituation();
		//解析话费信息
		telecomJiangxiCurrentSituation.setThismonthcharge(JSONObject.fromObject(htmlHuaFei).getJSONObject("obj").getString("month_charge"));
		telecomJiangxiCurrentSituation.setTotalintegra(JSONObject.fromObject(htmlHuaFei).getJSONObject("obj").getString("myjifen"));
		//解析套餐信息
		telecomJiangxiCurrentSituation.setRemainflow(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonFlow").getJSONObject("Surplus").getString("value"));
		telecomJiangxiCurrentSituation.setRemainsms(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonMessage").getJSONObject("Surplus").getString("value"));
		telecomJiangxiCurrentSituation.setRemainvoice(JSONObject.fromObject(htmlTaoCan).getJSONObject("obj").getJSONObject("userresourcequeryfor189home").getJSONObject("commonVoice").getJSONObject("Surplus").getString("value"));
		
		telecomJiangxiCurrentSituation.setTaskid(taskMobile.getTaskid());
		webParam.setTelecomJiangxiCurrentSituation(telecomJiangxiCurrentSituation);
		return webParam;
	}
	/**
	 * 解析月账单
	 * @param messageLogin 
	 * @param taskMobile
	 * @param html
	 * @param yearmonth
	 * @return
	 */
	public List<TelecomJiangxiMonthBill> getMonthBill(MessageLogin messageLogin, TaskMobile taskMobile, String html, String yearmonth) {
		List<TelecomJiangxiMonthBill> list=new ArrayList<TelecomJiangxiMonthBill>();
		Document doc = Jsoup.parse(html);
		TelecomJiangxiMonthBill telecomJiangxiMonthBill = null;
//		账单打印时间
		String printDate=CalendarParamService.getBeijingTime();
		Elements kindFee = doc.getElementsByAttributeValue("name", "kindFee");
		float sum=0;
		for (Element element : kindFee) {
			sum+=Double.parseDouble(element.val());
		}
		for (Element element : kindFee) {
			telecomJiangxiMonthBill=new TelecomJiangxiMonthBill();
			telecomJiangxiMonthBill.setBelongmonth(yearmonth);
			telecomJiangxiMonthBill.setFeetype(element.attr("kind"));
			telecomJiangxiMonthBill.setFee(element.val());
			telecomJiangxiMonthBill.setAccessnum(messageLogin.getName());
			telecomJiangxiMonthBill.setPrinttime(printDate);
			telecomJiangxiMonthBill.setTaskid(taskMobile.getTaskid());
			telecomJiangxiMonthBill.setMonthtotalcost(sum);
			list.add(telecomJiangxiMonthBill);
		}
		return list;
	}
	public List<TelecomJiangxiChargeInfo> chargeInfoParser(TaskMobile taskMobile, String html, String qryMonth) {
		List<TelecomJiangxiChargeInfo> list=new ArrayList<TelecomJiangxiChargeInfo>();
		TelecomJiangxiChargeInfo telecomJiangxiChargeInfo=null;
		if(html.contains("reqSerial")){  //包含说明至少有一条充值记录，否则没有充值记录
			//截取需要的字符串  相关记录
			int i=html.indexOf("reqSerial");
			int j=html.indexOf("dwr.engine");
			String record = html.substring(i-3,j);
			//添加{}，为转化为json对象做准备
			record="{"+record+"}";
			String[] array = record.split("reqSerial");
			if (array != null){	
				//统计公有几条数据
				int count=array.length - 1;
				JSONObject jsob = JSONObject.fromObject(record);
				for(int n=1;n<=count;n++){
					telecomJiangxiChargeInfo=new TelecomJiangxiChargeInfo();
					telecomJiangxiChargeInfo.setChargeaddr(jsob.getString("s"+n+".payChannelName"));
					telecomJiangxiChargeInfo.setChargemoney(jsob.getString("s"+n+".paymentAmount"));
					telecomJiangxiChargeInfo.setChargetime(jsob.getString("s"+n+".stateDate"));
					telecomJiangxiChargeInfo.setQrymonth(qryMonth);
					telecomJiangxiChargeInfo.setChargeway(jsob.getString("s"+n+".paymentMethodName"));
					telecomJiangxiChargeInfo.setTaskid(taskMobile.getTaskid());
					list.add(telecomJiangxiChargeInfo);
				}
			}
		}
		return list;
	}
	/**
	 * 解析余额信息
	 * @param taskMobile
	 * @param html
	 * @return
	 */
	public WebParam<TelecomJiangxiBalance> BalanceParser(TaskMobile taskMobile, String html) {
		WebParam<TelecomJiangxiBalance> webParam=new WebParam<TelecomJiangxiBalance>();
		TelecomJiangxiBalance telecomJiangxiBalance=new TelecomJiangxiBalance();
		int i=html.indexOf("{");
		int j=html.indexOf("}");
		String record = html.substring(i,j+1);
		JSONObject jsob = JSONObject.fromObject(record);
		telecomJiangxiBalance.setTaskid(taskMobile.getTaskid());
		telecomJiangxiBalance.setBalance(jsob.getString("BALANCE"));
		telecomJiangxiBalance.setQuerytime(CalendarParamService.getBeijingTime());
		webParam.setTelecomJiangxiBalance(telecomJiangxiBalance);
		return webParam;
	}
	/**
	 * 解析通话记录
	 * @param html
	 * @param taskMobile
	 * @param qryMonth
	 * @return
	 * 
	 * 改版记录：如下为改版前	 
	 * (2018年7月6日之后通话响应的源码格式变了，故修改如下相关解析代码)
	 */
/*	public List<TelecomJiangxiCallRecord> callRecordParser(String html, TaskMobile taskMobile, String qryMonth) {
		List<TelecomJiangxiCallRecord> list=new ArrayList<TelecomJiangxiCallRecord>();
		TelecomJiangxiCallRecord telecomJiangxiCallRecord=null;
		if(html.contains("['times_int']")){  //包含说明至少有一条通话记录，否则没有通话记录
			//截取需要的信息
			int i=html.indexOf("['times_int']");
			int j=html.lastIndexOf("callType");
			String jieguo = html.substring(i-3,j+35);
			if(jieguo.startsWith(";")){   //将开头的那个;截取掉
				jieguo=jieguo.substring(1, jieguo.length());
			}
			//为转换为json格式做前期准备工作
			jieguo=jieguo.replaceAll("\\s*", "");   //放在此处替换，才能起作用
			jieguo=jieguo.replaceAll("\\['", ".");
			jieguo=jieguo.replaceAll("\\']", "");
			jieguo=jieguo.replaceAll(";", ",\"");
			jieguo=jieguo.replaceAll(":", "'");
			jieguo=jieguo.replaceAll("=", "\":");
			jieguo=jieguo.replaceAll("'", ":");
			//统计总条数  
			String[] array = jieguo.split("otherFee");
			int count=array.length-1;  
			//将每条记录的索引值放在list集合中
			List<Integer> indexList=new ArrayList<Integer>();
			for(int n=0;n<=count+15;n++){    //索引值要比总条数多,故此处加上15，保证可以获取完全
				if(jieguo.contains("s"+n+".otherFee")){  //此处需要加上后边的那个类型名称，不然会出现没有15，有151，但是indexList中存储了15的情况
					indexList.add(n);
				}
			}
			//转为json串
			jieguo="{\""+jieguo+"}";
			JSONObject jsob = JSONObject.fromObject(jieguo);
			for (Integer index : indexList) {
				telecomJiangxiCallRecord=new TelecomJiangxiCallRecord();
				telecomJiangxiCallRecord.setBasicallcost(jsob.getString("s"+index+".otherFee"));
				telecomJiangxiCallRecord.setCallstartime(jsob.getString("s"+index+".callStartTime"));
				telecomJiangxiCallRecord.setCalltype(jsob.getString("s"+index+".callType"));
				telecomJiangxiCallRecord.setContactaddr(jsob.getString("s"+index+".callAddr"));
				telecomJiangxiCallRecord.setContactime(jsob.getString("s"+index+".times"));
				telecomJiangxiCallRecord.setContactype(jsob.getString("s"+index+".tonghuatype"));
				telecomJiangxiCallRecord.setLongwaycost(jsob.getString("s"+index+".longDistaFee"));
				telecomJiangxiCallRecord.setOthernum(jsob.getString("s"+index+".called"));
				telecomJiangxiCallRecord.setQrymonth(qryMonth);
				telecomJiangxiCallRecord.setTotalcharge(jsob.getString("s"+index+".totalFee"));
				telecomJiangxiCallRecord.setTaskid(taskMobile.getTaskid());
				list.add(telecomJiangxiCallRecord);
			}
		}
		return list;
	}*/
	/**
	 * 改版后的通话记录解析方式
	 * @param html
	 * @param taskMobile
	 * @param qryMonth
	 * @return
	 */
	public List<TelecomJiangxiCallRecord> callRecordParser(String html, TaskMobile taskMobile, String qryMonth) throws Exception {
		List<TelecomJiangxiCallRecord> list=new ArrayList<TelecomJiangxiCallRecord>();
		TelecomJiangxiCallRecord telecomJiangxiCallRecord=null;
		if(html.contains("dialing")){  //包含说明至少有一条通话记录，否则没有通话记录
			//截取需要的信息
			int i=html.indexOf("dialing");
//			int j=html.lastIndexOf("privilege");
//			String jieguo = html.substring(i-3,j+16);  //截取到所需记录的最后一个；之前(用这两行代码也可以，但是不能保证结尾处都是0.00)
			int j=html.lastIndexOf("dwr.engine");
			String jieguo = html.substring(i-3,j-3);  //截取到所需记录的最后一个；之前
			if(jieguo.startsWith(";")){   //将开头的那个;截取掉
				jieguo=jieguo.substring(1, jieguo.length());
			}
			//为转换为json格式做前期准备工作
			jieguo=jieguo.replaceAll("\\s*", "");   //放在此处替换，才能起作用
			jieguo=jieguo.replaceAll("\\['", ".");
			jieguo=jieguo.replaceAll("\\']", "");
			jieguo=jieguo.replaceAll(";", ",\"");
			jieguo=jieguo.replaceAll(":", "'");
			jieguo=jieguo.replaceAll("=", "\":");
			jieguo=jieguo.replaceAll("'", ":");
			//统计总条数  
			String[] array = jieguo.split("called");
			int count=array.length-1;  
			//将每条记录的索引值放在list集合中
			List<Integer> indexList=new ArrayList<Integer>();
			for(int n=0;n<=count+15;n++){    //索引值要比总条数多,故此处加上15，保证可以获取完全
				if(jieguo.contains("s"+n+".otherFee")){  //此处需要加上后边的那个类型名称，不然会出现没有15，有151，但是indexList中存储了15的情况
					indexList.add(n);
				}
			}
			//转为json串
			jieguo="{\""+jieguo+"}";
			JSONObject jsob = JSONObject.fromObject(jieguo);
			for (Integer index : indexList) {
				telecomJiangxiCallRecord=new TelecomJiangxiCallRecord();
				telecomJiangxiCallRecord.setBasicallcost(jsob.getString("s"+index+".otherFee"));
				telecomJiangxiCallRecord.setCallstartime(jsob.getString("s"+index+".callStartTime"));
				telecomJiangxiCallRecord.setCalltype(jsob.getString("s"+index+".callType"));
				telecomJiangxiCallRecord.setContactaddr(jsob.getString("s"+index+".callAddr"));
				telecomJiangxiCallRecord.setContactime(jsob.getString("s"+index+".times"));
				telecomJiangxiCallRecord.setContactype(jsob.getString("s"+index+".tonghuatype"));
				telecomJiangxiCallRecord.setLongwaycost(jsob.getString("s"+index+".longDistaFee"));
				telecomJiangxiCallRecord.setOthernum(jsob.getString("s"+index+".called"));
				telecomJiangxiCallRecord.setQrymonth(qryMonth);
//				telecomJiangxiCallRecord.setTotalcharge(jsob.getString("s"+index+".totalFee"));  //改版前有这个字段
				telecomJiangxiCallRecord.setTotalcharge(jsob.getString("s"+index+".longDistaFee"));  //改版后认为长途费就是通话总计
				telecomJiangxiCallRecord.setTaskid(taskMobile.getTaskid());
				list.add(telecomJiangxiCallRecord);
			}
		}
		return list;
	}
	/**
	 * 解析短信记录信息
	 * @param html
	 * @param taskMobile
	 * @param qryMonth
	 * @return
	 */
	public List<TelecomJiangxiSMSRecord> smsRecordParser(String html, TaskMobile taskMobile, String qryMonth) throws Exception{
		List<TelecomJiangxiSMSRecord> list=new ArrayList<TelecomJiangxiSMSRecord>();
		TelecomJiangxiSMSRecord telecomJiangxiSMSRecord=null;
		//判断有无数据
		if(html.contains("dialing")){
			//截取需要的信息
			int i=html.indexOf("dialing");
			int j=html.lastIndexOf("dwr.engine");
			String jieguo = html.substring(i-3,j-3);  //截取到所需记录的最后一个；之前
			jieguo=jieguo.replaceAll("\\s*", "");   //放在此处替换，才能起作用
			if(jieguo.startsWith(";")){   //将开头的那个;截取掉
				jieguo=jieguo.substring(1, jieguo.length());
			}
			//统计总条数  
			String[] array = jieguo.split("called");
			int count=array.length-1;  
			//将每条记录的索引值放在list集合中
			List<Integer> indexList=new ArrayList<Integer>();
			for(int n=0;n<=count+5;n++){    //索引值要比总条数多,故此处加上5，保证可以获取完全
				if(jieguo.contains("s"+n+".called")){
					indexList.add(n);
				}
			}
			//转为json串
			jieguo="{"+jieguo+"}";
			JSONObject json = JSONObject.fromObject(jieguo);
			for (Integer index : indexList) {
				telecomJiangxiSMSRecord=new TelecomJiangxiSMSRecord();
				telecomJiangxiSMSRecord.setGetnum(json.getString("s"+index+".called"));
				telecomJiangxiSMSRecord.setQrymonth(qryMonth);
				telecomJiangxiSMSRecord.setSendtime(json.getString("s"+index+".sendTime"));
				telecomJiangxiSMSRecord.setTaskid(taskMobile.getTaskid());
				telecomJiangxiSMSRecord.setTotalcost(json.getString("s"+index+".fee"));
				list.add(telecomJiangxiSMSRecord);
			}
		}
		return list;
	}
}
