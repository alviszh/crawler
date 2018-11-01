package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanBusinessMessage;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanCallThremResult;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanConsumptionPoints;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanPayMent;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanPhoneBill;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanSMSThremResult;
import com.microservice.dao.entity.crawler.telecom.sichuan.TelecomSiChuanUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomSiChuanParser {



	public TelecomSiChuanUserInfo userinfo_parse(String html,String html2, String html3, String html4) {
		TelecomSiChuanUserInfo telecomSiChuanUserInfo = new TelecomSiChuanUserInfo();
		try {
			if(html.indexOf("当前登录号码")!=-1){
				Document doc = Jsoup.parse(html);
				Elements select = doc.select("div.datacon");
				String s = select.text();
				String[] type1 = s.split("当前登录号码");
				String s1=type1[0];
				String[] type1_name = s1.split(": ");
				String username = type1_name[1];//机主名称
				telecomSiChuanUserInfo.setUsername(username);

				String s2 = type1[1];
				String[] type2 = s2.split("产品名称");
				String s3 = type2[0];
				String[] type2_tel = s3.split(": ");
				String tel = type2_tel[1];//电话帐号
				telecomSiChuanUserInfo.setTel(tel);

				String s4 = type2[1];
				String[] type3 = s4.split("安装地址");
				String s5 = type3[0];
				String[] type3_cname = s5.split(": ");
				String cname = type3_cname[1];//产品名称
				telecomSiChuanUserInfo.setProductname(cname);

				String s6 = type3[1];
				String[] type4 = s6.split("合同号");
				String s7 = type4[0];
				String[] type4_address = s7.split(": ");
				String address = type4_address[1];//安装地址
				telecomSiChuanUserInfo.setAddress(address);

				String s8 = type4[1];
				String[] type5 = s8.split("安装日期");
				String s9 = type5[0];
				String[] type5_ht = s9.split(": ");
				String ht = type5_ht[1];//合同号
				telecomSiChuanUserInfo.setContractnumber(ht);

				String n1 = type5[1];
				String[] type6 = n1.split("状态");
				String n2 = type6[0];
				String[] type6_date = n2.split(": ");
				String data = type6_date[1];//安装日期
				telecomSiChuanUserInfo.setCreatedtime(data);

				String n3 = type6[1];
				String[] type7 = n3.split("电子账单邮箱");
				String n4 = type7[0];
				String[] type_ser = n4.split(": ");
				String ser = type_ser[1];//状态
				telecomSiChuanUserInfo.setStatus(ser);

				//		String n5 = type7[1];
				//		String[] type8_em = n5.split(": ");
				//		String em = type8_em[1];//邮箱
				//		if(em.equals("修改")){
				//			em = null;
				//		}
				//		telecomSiChuanUserInfo.setEmail(em);
			}

			//		//星级
			if(html2!=null&&html2!=""){
				Document doc2 = Jsoup.parse(html2);
				Elements select2 = doc2.select("h6");
				String string = select2.text();
				String[] split = string.split("中国电信");
				String string2 = split[1];
				String[] split2 = string2.split("星级");
				String stars = split2[0];
				//[2, 用户成长值为：1313，可享受下述, 权益。（蓝色为可享受权益）]
				String string3 = split2[1];
				if(string3.indexOf("：")!=-1){
					String[] split3 = string3.split("：");
					String string4 = split3[1];
					String[] split4 = string4.split("，");
					String starsfen = split4[0];
					telecomSiChuanUserInfo.setStarsfen(starsfen);//星级成长值
				}else{
					String starsfen = "0";
					telecomSiChuanUserInfo.setStarsfen(starsfen);//星级成长值
				}
				telecomSiChuanUserInfo.setStars(stars);//星级
			}
			//积分
			if(html3.indexOf("obj")!=-1){
				JSONObject object = JSONObject.fromObject(html3);
				String obj = object.getString("obj");
				JSONObject fromObject = JSONObject.fromObject(obj);
				String jifen = fromObject.getString("myjifen");
				telecomSiChuanUserInfo.setJifen(jifen);
			}
			if(html4.indexOf("accountexpense")!=-1){
				//当前余额
				JSONObject object2 = JSONObject.fromObject(html4);
				String accountexpense = object2.getString("accountexpense");
				telecomSiChuanUserInfo.setAccountexpense(accountexpense);
			}
		} catch (Exception e) {
			return null;
		}
		return telecomSiChuanUserInfo;
	}

	public List<TelecomSiChuanConsumptionPoints> ConsumptionPoints_parse(String html) {
		List<TelecomSiChuanConsumptionPoints> list = new ArrayList<TelecomSiChuanConsumptionPoints>();
		if(html.indexOf("message")!=-1){
			JSONObject object = JSONObject.fromObject(html);
			String message = object.getString("message");
			JSONArray object2 = JSONArray.fromObject(message);
			for(int i=0 ; i<object2.size();i++){
				JSONObject object3 = object2.getJSONObject(i);
				String Month = object3.getString("Month");//月份
				String PointValue = object3.getString("PointValue");//新增积分
				String PointTypeName = object3.getString("PointTypeName");//积分类型
				String BasicsPointValue ="";
				if(object3.has("BasicsPointValue")){
					BasicsPointValue = object3.getString("BasicsPointValue");//消费基础分
				}
				TelecomSiChuanConsumptionPoints telecomSiChuanConsumptionPoints = new TelecomSiChuanConsumptionPoints();
				telecomSiChuanConsumptionPoints.setMonth(Month);
				telecomSiChuanConsumptionPoints.setPointTypeName(PointTypeName);
				telecomSiChuanConsumptionPoints.setBasicsPointValue(BasicsPointValue);
				telecomSiChuanConsumptionPoints.setPointValue(PointValue);
				list.add(telecomSiChuanConsumptionPoints);
			}
		}else{
			list = null;
		}
		return list;
	}

	public List<TelecomSiChuanBusinessMessage> BusinessMessage_parse(String html, String html2, String html3) {
		List<TelecomSiChuanBusinessMessage> list = null;
		String productnumber = "";
		try {
			list = new ArrayList<TelecomSiChuanBusinessMessage>();
			//html
			TelecomSiChuanBusinessMessage telecomSiChuanBusinessMessage = new TelecomSiChuanBusinessMessage();

			if(html.indexOf("tr-bg")!=-1){
				Document doc = Jsoup.parse(html);
				Element td = doc.select("tr.tr-bg").get(0);
				String business1 = td.text();
				productnumber = business1.substring(0,11);//产品帐号
				telecomSiChuanBusinessMessage.setProductnumber(productnumber);

				String string = business1.substring(11);
				String[] business2 = string.split(" ");
				String businessname = business2[1];//产品名称
				telecomSiChuanBusinessMessage.setBusinessname(businessname);

				String[] string2=string.split(businessname);
				String string3 = string2[1];
				String createdtime = string3.substring(1, 11);//开始时间
				telecomSiChuanBusinessMessage.setStarttime(createdtime);
				String endtime = string3.substring(12);//结束时间
				telecomSiChuanBusinessMessage.setEndtime(endtime);

				list.add(telecomSiChuanBusinessMessage);

				Elements ele = doc.select("tr.tr-bg");
				for(int i=1;i<ele.size();i++){
					TelecomSiChuanBusinessMessage telecomSiChuanBusinessMessage1 = new TelecomSiChuanBusinessMessage();
					String td2 = ele.get(i).text();
					String[] businessmessage = td2.split(" ");
					String businessname1 = businessmessage[0];
					String createdtime1 = businessmessage[1];
					String endtime1 = businessmessage[2];
					telecomSiChuanBusinessMessage1.setProductnumber(productnumber);
					telecomSiChuanBusinessMessage1.setBusinessname(businessname1);
					telecomSiChuanBusinessMessage1.setStarttime(createdtime1);
					telecomSiChuanBusinessMessage1.setEndtime(endtime1);
					list.add(telecomSiChuanBusinessMessage1);
				}
			}
			if(html.indexOf("tr-bg")!=-1){
				//html2
				Document doc2 = Jsoup.parse(html2);
				Elements yewu = doc2.select("tr.tr-bg");

				for(int i=0;i<yewu.size();i++){
					TelecomSiChuanBusinessMessage telecomSiChuanBusinessMessage2 = new TelecomSiChuanBusinessMessage();
					String string4 = yewu.get(i).text();
					String[] split = string4.split(" ");
					if(split.length>3){
						String name = split[2];
						telecomSiChuanBusinessMessage2.setBusinessname(name);
						telecomSiChuanBusinessMessage2.setProductnumber(productnumber);
						list.add(telecomSiChuanBusinessMessage2);
					}
					if(split.length==3){
						String name = split[1];
						telecomSiChuanBusinessMessage2.setBusinessname(name);
						telecomSiChuanBusinessMessage2.setProductnumber(productnumber);
						list.add(telecomSiChuanBusinessMessage2);
					}
					if(split.length<3){
						String name = split[0];
						telecomSiChuanBusinessMessage2.setBusinessname(name);
						telecomSiChuanBusinessMessage2.setProductnumber(productnumber);
						list.add(telecomSiChuanBusinessMessage2);
					}

				}
			}
			if(html.indexOf("tr-bg")!=-1){
				//html3
				Document doc3 = Jsoup.parse(html3);
				Elements zifei = doc3.select("tr.tr-bg");
				for(int i=0;i<zifei.size();i++){
					TelecomSiChuanBusinessMessage telecomSiChuanBusinessMessage3=new TelecomSiChuanBusinessMessage();
					String zf = zifei.get(i).text();
					String[] strings = zf.split(" ");
					if(strings.length>3){
						String zfname = strings[0];
						String[] split = zfname.split("（查看介绍）");
						String zfname1 = split[0];
						String time = strings[1];
						telecomSiChuanBusinessMessage3.setBusinessname(zfname1);
						telecomSiChuanBusinessMessage3.setStarttime(time);
						list.add(telecomSiChuanBusinessMessage3);
					}
				}
			}
		} catch (Exception e) {
			return list=null;
		}
		return list;

	}

	public List<TelecomSiChuanPayMent> PayMent_parse(String html) {
		List<TelecomSiChuanPayMent> list = new ArrayList<TelecomSiChuanPayMent>();
		if(html.indexOf("retMsg")!=-1){
			JSONObject object = JSONObject.fromObject(html);
			String retMsg = object.getString("retMsg");
			JSONArray object2 = JSONArray.fromObject(retMsg);
			for(int i=0;i<object2.size();i++){
				TelecomSiChuanPayMent telecomSiChuanPayMent = new TelecomSiChuanPayMent();
				String string = object2.getString(i);
				JSONObject object3 = JSONObject.fromObject(string);
				String amount = object3.getString("amount");//缴费金额
				String createTime = object3.getString("createTime");//缴费时间
				String paymentMethodName = object3.getString("paymentMethodName");//缴费渠道
				telecomSiChuanPayMent.setAmount(amount);
				telecomSiChuanPayMent.setCreatedate(createTime);
				telecomSiChuanPayMent.setPaymentMethodName(paymentMethodName);
				list.add(telecomSiChuanPayMent);
			}
		}else{
			list = null;
		}
		
		return list;
	}

	public List<TelecomSiChuanPhoneBill> PhoneBill_parse(String html, String month) {
		List<TelecomSiChuanPhoneBill> list = new ArrayList<TelecomSiChuanPhoneBill>();
		if(html.indexOf("list")!=-1){
			JSONObject object = JSONObject.fromObject(html);
			String string = object.getString("list");
			JSONArray object2 = JSONArray.fromObject(string);
			for(int i=0;i<object2.size();i++){
				TelecomSiChuanPhoneBill telecomSiChuanPhoneBill = new TelecomSiChuanPhoneBill();
				JSONObject object3 = object2.getJSONObject(i);
				String LEVEL_ID = object3.getString("LEVEL_ID");//分级
				String ACC_ITEM_TYPE = object3.getString("ACC_ITEM_TYPE");//分级名称
				String ACC_ITEM_DETAIL = object3.getString("ACC_ITEM_DETAIL");//扣费名称
				String FEE = object3.getString("FEE");//费用
				String FEE_TOTAL = object3.getString("FEE_TOTAL");//总花费

				telecomSiChuanPhoneBill.setLevel_id(LEVEL_ID);
				telecomSiChuanPhoneBill.setAcc_item_type(ACC_ITEM_TYPE);
				telecomSiChuanPhoneBill.setAcc_item_detail(ACC_ITEM_DETAIL);
				telecomSiChuanPhoneBill.setFee(FEE);
				telecomSiChuanPhoneBill.setTotal(FEE_TOTAL);
				telecomSiChuanPhoneBill.setBelongsDate(month);//当前月
				list.add(telecomSiChuanPhoneBill);
			}

		}else{
			list = null;
		}

		return list;

	}

	public List<TelecomSiChuanCallThremResult> getCallThrem_parse(String html) {
		List<TelecomSiChuanCallThremResult> list = new ArrayList<TelecomSiChuanCallThremResult>();
		if(html.indexOf("json")!=-1){
			JSONObject object = JSONObject.fromObject(html);
			String string = object.getString("json");
			JSONObject object2 = JSONObject.fromObject(string);
			String string2 = object2.getString("retInfo");
			JSONArray object3 = JSONArray.fromObject(string2);
			for(int i = 0 ; i<object3.size();i++)
			{
				TelecomSiChuanCallThremResult t = new TelecomSiChuanCallThremResult();
				JSONObject object4 = object3.getJSONObject(i);
				String CALL_TYPE = object4.getString("CALL_TYPE");//呼叫类型
				String OTHERPHONE = object4.getString("OTHERPHONE");//对方号码
				String START_TIME = object4.getString("START_TIME");//时间
				String TIMELONG = object4.getString("TIMELONG");//通话时长
				String CALLED_CITYCODE = object4.getString("CALLED_CITYCODE");//对方地点
				String CALCUNIT = object4.getString("CALCUNIT");//通话类型
				String YH_MONEY = object4.getString("YH_MONEY");//优惠减免
				String BILLING_AREA = object4.getString("BILLING_AREA");//通话地点
				String MONEY = object4.getString("MONEY");//基本或者漫游费
				String DEGREE = object4.getString("DEGREE");// 长途费用
				String CT_MONEY = object4.getString("CT_MONEY");//总费用
				String OTHER_MONEY = object4.getString("OTHER_MONEY");//其他费用

				t.setCallType(CALL_TYPE);
				t.setCounterArea(CALLED_CITYCODE);
				t.setCounterNumber(OTHERPHONE);
				t.setCallDate(START_TIME);
				t.setDuration(TIMELONG);
				t.setType(CALCUNIT);
				t.setFavour(YH_MONEY);
				t.setBilling(BILLING_AREA);
				t.setBaseFee(MONEY);
				t.setTotal(CT_MONEY);
				t.setTollAdd(DEGREE);
				t.setOtherFee(OTHER_MONEY);
				list.add(t);

			}
		}else{
			list = null;
		}
		return list;
	}

	public List<TelecomSiChuanSMSThremResult> getBillDetail_parse(String html)throws Exception {
		List<TelecomSiChuanSMSThremResult> list = new ArrayList<TelecomSiChuanSMSThremResult>();
		if(html.indexOf("json")!=-1){
			JSONObject object = JSONObject.fromObject(html);
			String string = object.getString("json");
			JSONObject object2 = JSONObject.fromObject(string);
			String string2 = object2.getString("retInfo");
			JSONArray object3 = JSONArray.fromObject(string2);
			for(int i = 0 ; i<object3.size();i++)
			{
				TelecomSiChuanSMSThremResult t = new TelecomSiChuanSMSThremResult();
				JSONObject object4 = object3.getJSONObject(i);
				String CALCUNIT = object4.getString("CALCUNIT");//通信类型
				String ACC_NUMBER = object4.getString("OTHERPHONE");//对方号码
				String START_TIME = object4.getString("START_TIME");//时间
				String MONEY = object4.getString("MONEY");//总费用

				t.setTotal(MONEY);
				t.setCallPhone(ACC_NUMBER);
				t.setBeginDate(START_TIME);
				t.setFeeKind(CALCUNIT);

				list.add(t);
			}

		}else{
			list = null;
		}
		return list;
	}

}
