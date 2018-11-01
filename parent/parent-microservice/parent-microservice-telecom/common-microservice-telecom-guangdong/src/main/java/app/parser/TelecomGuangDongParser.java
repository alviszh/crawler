package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongBusinessmessage;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongCallThremResult;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongPayMent;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongSMSThrem;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongStatusCode;
import com.microservice.dao.entity.crawler.telecom.guangdong.TelecomGuangDongUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class TelecomGuangDongParser {

	public TelecomGuangDongUserInfo userinfo_parse(String html, String html2, String html3, String html4) {

		TelecomGuangDongUserInfo telecomGuangDongUserInfo = new TelecomGuangDongUserInfo();
		try {

			if(html!=null&&html!=""){
				Document doc = Jsoup.parse(html);

				telecomGuangDongUserInfo.setUsername(doc.select("th:contains(客户姓名)+td").first().text());
				telecomGuangDongUserInfo.setIndentNbrType(doc.select("th:contains(证件类型)+td").first().text());
				telecomGuangDongUserInfo.setIndentCode(doc.select("th:contains(证件号码)+td").first().text());
				telecomGuangDongUserInfo.setUserAddress(doc.select("th:contains(证件地址)+td").first().text());
				telecomGuangDongUserInfo.setPost_code(doc.getElementById("post_code_id").val());
				telecomGuangDongUserInfo.setPost_addr(doc.getElementById("post_addr_id").val());
				telecomGuangDongUserInfo.setRela_man(doc.getElementById("rela_man_id").val());
				telecomGuangDongUserInfo.setDhNumber(doc.getElementById("dhNumber_id").val());
				telecomGuangDongUserInfo.setMoblie(doc.getElementById("moblie_id").val());
				telecomGuangDongUserInfo.setEmail(doc.getElementById("email_id").val());
				telecomGuangDongUserInfo.setQqNumber(doc.getElementById("qqNumber_id").val());
				String val = doc.getElementById("cust_sexs_id").val();
				if(val.equals("1"))
				{
					telecomGuangDongUserInfo.setSex("男");
				}
				else if(val.equals("0")){
					telecomGuangDongUserInfo.setSex("女");
				}
				else{
					telecomGuangDongUserInfo.setSex("");
				}

				//学历
				String education_id = doc.getElementById("education_id").val();
				if(education_id.equals("01")){
					telecomGuangDongUserInfo.setEducation("");
				}else if(education_id.equals("02")){
					telecomGuangDongUserInfo.setEducation("硕士");
				}
				else if(education_id.equals("03")){
					telecomGuangDongUserInfo.setEducation("本科");
				}
				else if(education_id.equals("04")){
					telecomGuangDongUserInfo.setEducation("专科");
				}
				else if(education_id.equals("05")){
					telecomGuangDongUserInfo.setEducation("高中/中专");
				}
				else if(education_id.equals("06")){
					telecomGuangDongUserInfo.setEducation("初中及以下");
				}
				else if(education_id.equals("99")){
					telecomGuangDongUserInfo.setEducation("不详");
				}
				//报销方式
				String val1 = doc.getElementById("px_id_id").val();
				if(val1.equals("01")){
					telecomGuangDongUserInfo.setPx("单位帐户划账");
				}
				else if(val1.equals("02")){
					telecomGuangDongUserInfo.setPx("单位报销发票");
				}
				else if(val1.equals("03")){
					telecomGuangDongUserInfo.setPx("单位话费补贴");
				}
				else if(val1.equals("04")){
					telecomGuangDongUserInfo.setPx("全额自己支付");
				}

				//职业类别
				String zel_id_id = doc.getElementById("zel_id_id").val();
				if(zel_id_id.equals("A01")){
					telecomGuangDongUserInfo.setZell("管理人员");
				}
				else if(zel_id_id.equals("A02")){
					telecomGuangDongUserInfo.setZell("普通职员/文员");
				}
				else if(zel_id_id.equals("A03")){
					telecomGuangDongUserInfo.setZell("务工及商业服务业人员");
				}
				else if(zel_id_id.equals("A04")){
					telecomGuangDongUserInfo.setZell("务农/养殖");
				}
				else if(zel_id_id.equals("A05")){
					telecomGuangDongUserInfo.setZell("专业技术人员");
				}
				else if(zel_id_id.equals("A06")){
					telecomGuangDongUserInfo.setZell("军人");
				}
				else if(zel_id_id.equals("A07")){
					telecomGuangDongUserInfo.setZell("自由职业者");
				}
				else if(zel_id_id.equals("A08")){
					telecomGuangDongUserInfo.setZell("学生");
				}
				else if(zel_id_id.equals("A09")){
					telecomGuangDongUserInfo.setZell("退休人员");
				}
				else if(zel_id_id.equals("A10")){
					telecomGuangDongUserInfo.setZell("无工作/待业");
				}

				//月收入
				String moneyid_id = doc.getElementById("moneyid_id").val();
				if(moneyid_id.equals("P01")){
					telecomGuangDongUserInfo.setMoneyy("2000元以下");
				}
				else if(moneyid_id.equals("P02")){
					telecomGuangDongUserInfo.setMoneyy("2000-4000元");
				}
				else if(moneyid_id.equals("P03")){
					telecomGuangDongUserInfo.setMoneyy("4000-6000元");
				}
				else if(moneyid_id.equals("P04")){
					telecomGuangDongUserInfo.setMoneyy("6000-8000元");
				}
				else if(moneyid_id.equals("P05")){
					telecomGuangDongUserInfo.setMoneyy("8000-12000元");
				}
				else if(moneyid_id.equals("P06")){
					telecomGuangDongUserInfo.setMoneyy("12000以上");
				}else if(moneyid_id.equals("")){
					telecomGuangDongUserInfo.setMoneyy("");
				}

				//行业
				String calling_id = doc.getElementById("calling_id").val();
				if(calling_id.equals("01")){
					telecomGuangDongUserInfo.setCal("农林牧渔");
				}
				else if(calling_id.equals("02")){
					telecomGuangDongUserInfo.setCal("邮电通讯");
				}
				else if(calling_id.equals("03")){
					telecomGuangDongUserInfo.setCal("房地产");
				}
				else if(calling_id.equals("04")){
					telecomGuangDongUserInfo.setCal("科教文卫");
				}
				else if(calling_id.equals("05")){
					telecomGuangDongUserInfo.setCal("工业");
				}
				else if(calling_id.equals("06")){
					telecomGuangDongUserInfo.setCal("银行");
				}
				else if(calling_id.equals("07")){
					telecomGuangDongUserInfo.setCal("证券");
				}
				else if(calling_id.equals("08")){
					telecomGuangDongUserInfo.setCal("保险");
				}
				else if(calling_id.equals("09")){
					telecomGuangDongUserInfo.setCal("商业");
				}
				else if(calling_id.equals("10")){
					telecomGuangDongUserInfo.setCal("机关团体");
				}
				else if(calling_id.equals("11")){
					telecomGuangDongUserInfo.setCal("其他");
				}
			}
			if(html2!=null&&html2!=""){
				//积分
				JSONObject object = JSONObject.fromObject(html2);
				String string = object.getString("r");
				JSONObject object2 = JSONObject.fromObject(string);
				String jifen = object2.getString("r01");
				telecomGuangDongUserInfo.setJifen(jifen);
			}
			//余额
			if(html3!=null&&html3!=""){
				JSONObject object3 = JSONObject.fromObject(html3);
				String string2 = object3.getString("r");
				JSONObject object4 = JSONObject.fromObject(string2);

				String string4 = object4.getString("r04");
				JSONObject object = JSONObject.fromObject(string4);
				if(string4!="null"&&string4!=""){
					String r0401 = object.getString("r0401");
					telecomGuangDongUserInfo.setAccountexpense(r0401);
				}

				String string3 = object4.getString("r05");
				JSONObject object5 = JSONObject.fromObject(string3);
				if(string3!="null"){
					String accountexpense = object5.getString("r0501");
					telecomGuangDongUserInfo.setAccountexpense(accountexpense);
				}


			}
			//		//星级
			//		if(html4!="null"&html4!=""){
			//		JSONObject object6 = JSONObject.fromObject(html4);
			//		String string4 = object6.getString("r");
			//		JSONObject object7 = JSONObject.fromObject(string4);
			//		String stars = object7.getString("r04");
			//		String starsfen = object7.getString("r09");
			//		telecomGuangDongUserInfo.setStars(stars);
			//		telecomGuangDongUserInfo.setStarsfen(starsfen);
			//		
			//		}
			return telecomGuangDongUserInfo;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public List<TelecomGuangDongStatusCode> statusCode_parse(String html) {
		List<TelecomGuangDongStatusCode> list = new ArrayList<>();
		if (html.indexOf("r0101")!=-1) {
			JSONObject object = JSONObject.fromObject(html);
			String r = object.getString("r");
			JSONObject object2 = JSONObject.fromObject(r);
			String r01 = object2.getString("r01");
			JSONArray object3 = JSONArray.fromObject(r01);
			for(int i=0;i<object3.size();i++){
				TelecomGuangDongStatusCode t = new TelecomGuangDongStatusCode();
				JSONObject object4 = object3.getJSONObject(i);
				String yue = object4.getString("r0101");
				String zhouqi = object4.getString("r0102");
				String dangqian = object4.getString("r0106");
				String xinzeng = object4.getString("r0103");
				t.setMonth(yue);
				t.setCdate(zhouqi);
				t.setDang(dangqian);
				t.setXin(xinzeng);
				list.add(t);
			}
		}else{
			list = null;
		}
		return list;
	}

	public List<TelecomGuangDongPayMent> getPayMent_parse(String html) {

		List<TelecomGuangDongPayMent> list = new ArrayList<>();
		try{
			if(html!=null&&html!=""){
				JSONObject object = JSONObject.fromObject(html);
				String string = object.getString("r");
				JSONObject object2 = JSONObject.fromObject(string);
				if(object2.getString("msg").equals("没有数据")){
					return null;
				}
				else if(object2.getString("msg").equals("查询云清单为空")){
					return null;
				}
				else if(object2.getString("msg").equals("查询成功")){
					String string2 = object2.getString("r01");
					JSONArray object3 = JSONArray.fromObject(string2);
					for(int i = 0; i<object3.size();i++){
						JSONObject object4 = object3.getJSONObject(i);
						TelecomGuangDongPayMent t = new TelecomGuangDongPayMent();
						t.setPretime(object4.getString("r0102"));
						t.setMoney(object4.getString("r0103"));
						t.setType(object4.getString("r0104"));
						t.setChannels(object4.getString("r0105"));
						t.setPaymode(object4.getString("r0107"));
						t.setUsamode(object4.getString("r0109"));
						list.add(t);
					}
				}
			}
			return list;
		}catch (Exception e) {
			// TODO: handle exception
			return list;
		}
	}

	public List<TelecomGuangDongBusinessmessage> Businessmessage_parse(String html, String html1) {

		JSONObject object = JSONObject.fromObject(html);
		String r = object.getString("r");
		JSONObject object2 = JSONObject.fromObject(r);
		String ztaocan = object2.getString("r02");//主套餐
		TelecomGuangDongBusinessmessage t1 = new TelecomGuangDongBusinessmessage();
		t1.setZtaoname(ztaocan);

		JSONObject object3 = JSONObject.fromObject(html1);
		String r1 = object3.getString("r");
		JSONObject object4 = JSONObject.fromObject(r1);
		String r03 = object4.getString("r03");
		JSONArray object5 = JSONArray.fromObject(r03);
		List<TelecomGuangDongBusinessmessage> list = new ArrayList<>();
		list.add(t1);
		for(int i=0;i<object5.size();i++){
			TelecomGuangDongBusinessmessage t = new TelecomGuangDongBusinessmessage();
			JSONObject object6 = object5.getJSONObject(i);
			String tname = object6.getString("r0302");//名称
			String danwei = object6.getString("r0303");//单位
			String zong = object6.getString("r0305");//总量
			String shengyu = object6.getString("r0306");//剩余
			String miaoshu = object6.getString("r0311");//描述

			t.setTcontent(tname);
			t.setGross(zong);
			t.setResidue(shengyu);
			t.setUnit(danwei);
			t.setDescribe(miaoshu);

			list.add(t);
		}
		return list;

	}

	public List<TelecomGuangDongCallThremResult> callThrem_parse(String html) {
		List<TelecomGuangDongCallThremResult> list = new ArrayList<>();
		if(html.indexOf("r03")!=-1){
			JSONObject object = JSONObject.fromObject(html);
			String string = object.getString("r");
			JSONObject object2 = JSONObject.fromObject(string);
			String r03 = object2.getString("r03");
			if(r03!=null){
				JSONArray object3 = JSONArray.fromObject(r03);

				for(int i=0;i<object3.size();i++){
					TelecomGuangDongCallThremResult t = new TelecomGuangDongCallThremResult();
					JSONArray obj = object3.getJSONArray(i);
					String thlx = obj.getString(0);
					String dfhm = obj.getString(1);
					String thrq = obj.getString(2);
					String thsc = obj.getString(3);
					String thfy = obj.getString(4);
					String hjlx = obj.getString(5);
					String zjthd = obj.getString(6);

					t.setCallland(zjthd);
					t.setCalldate(thrq);
					t.setCalltype(hjlx);
					t.setDialnumber(dfhm);
					t.setDuration(thsc);
					t.setDatecalltype(thlx);
					t.setCallmoney(thfy);
					list.add(t);
				}
			}else{
				list = null;
			}
		
		}else{
			list = null;
		}
		return list;

	}

	public List<TelecomGuangDongSMSThrem> sMSThrem_parse(String html) {
		List<TelecomGuangDongSMSThrem> list = new ArrayList<>();
			if(html.indexOf("r03")!=-1){
				JSONObject object = JSONObject.fromObject(html);
				String string = object.getString("r");
				JSONObject object2 = JSONObject.fromObject(string);
				String r03 = object2.getString("r03");
				JSONArray object3 = JSONArray.fromObject(r03);
				System.out.println(object3.toString());
				for(int i=0;i<object3.size();i++){
					TelecomGuangDongSMSThrem t = new TelecomGuangDongSMSThrem();
					JSONArray obj = object3.getJSONArray(i);
					String yhhm = obj.getString(0);
					String dfhm = obj.getString(1);
					String txrq = obj.getString(2);
					String txf = obj.getString(3);
					String dxlx = obj.getString(4);

					t.setDnumber(dfhm);
					t.setSmsdate(txrq);
					t.setSmsmoney(txf);
					t.setSmstype(dxlx);
					t.setUsernumber(yhhm);
					list.add(t);

				}
			}else{
				list=null;
			}
			return list;
		
	}

}
