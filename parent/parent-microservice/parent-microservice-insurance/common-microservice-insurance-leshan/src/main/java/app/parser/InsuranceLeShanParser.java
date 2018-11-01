package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanGongShang;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanShengYu;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanShiYe;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanUserInfo;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanYanglao;
import com.microservice.dao.entity.crawler.insurance.leshan.InsuranceLeShanYiLiao;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceLeShanParser {

	@Autowired
	private TracerLog tracer;

	public InsuranceLeShanUserInfo getuserinfo(String taskId, String contentAsString) {
		InsuranceLeShanUserInfo userinfo = null;
		try {
			JSONObject object = JSONObject.fromObject(contentAsString).getJSONObject("fieldData");
			String string = object.getString("aac004").trim();//性别
			String string2 = object.getString("aac003").trim();//姓名
			String string3 = object.getString("aac006").trim();//出生日期
			String string4 = object.getString("aac005").trim();//民族
			String string5 = object.getString("aac009").trim();//户口类型
			String string6 = object.getString("aac060").trim();//人员状态
			String string7 = object.getString("age").trim();//年龄
			String string8 = object.getString("aaf002").trim();//银行类别
			String string9 = object.getString("aae008").trim();//银行网点
			String string10 = object.getString("aae009").trim();//户名
			String string11 = object.getString("aae010").trim();//代扣银行账号
			String string12 = object.getString("aae005").trim();//电话
			String string13 = object.getString("aae006").trim();//常住地址
			System.out.println("性别:"+string+"\r姓名:"+string2+"\r出生日期:"+string3+"\r民族:"+string4+"\r户口类型:"+
			string5+"\r人员状态:"+string6+"\r年龄:"+string7+"\r银行类别:"+string8+"\r银行网点:"+string9+"\r户名:"+string10+"\r代扣银行账号:"+
					string11+"\r电话:"+string12+"\r常住地址:"+string13);
			userinfo = new InsuranceLeShanUserInfo(taskId,string,string2,string3,string4,string5,
					string6,string7,string8,string9,string10,string11,string12,string13);
		} catch (Exception e) {
			tracer.addTag("getuserinfo.parser", taskId);
			return userinfo;
		}
		return userinfo;
		
	}

	public List<InsuranceLeShanYanglao> getyanglaoMsg(String contentAsString2, String taskId) {
		List<InsuranceLeShanYanglao> list = null;
		try {
			list = new ArrayList<InsuranceLeShanYanglao>();
			JSONArray jsonArray = JSONObject.fromObject(contentAsString2).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
				String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
				String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
				String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
				String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
				String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
				String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
				String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
				String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
				
				System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
						"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
				InsuranceLeShanYanglao info = new InsuranceLeShanYanglao(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9);
				list.add(info);
			}
		} catch (Exception e) {
			tracer.addTag("getyanglaoMsg.parser", taskId);
			return list;
		}
		return list;
	}

	public List<InsuranceLeShanYiLiao> getyiliaoMsg(String taskId, String html) {
		List<InsuranceLeShanYiLiao> list = null;
		try {
			list = new ArrayList<>();
			JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
				String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
				String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
				String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
				String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
				String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
				String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
				String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
				String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
				System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
						"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
				InsuranceLeShanYiLiao yiliao = new InsuranceLeShanYiLiao(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9);
				list.add(yiliao);
			}
		} catch (Exception e) {
			tracer.addTag("getyiliaoMsg.parser", taskId);
			return list;
		}
		return list;
	}

	public List<InsuranceLeShanGongShang> getgongshangmsg(String taskId, String html) {
		List<InsuranceLeShanGongShang> list = null;
		try {
			list = new ArrayList<>();
			JSONArray jsonArray = JSONObject.fromObject(html).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
				String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
				String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
				String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
				String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
				String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
				String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
				String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
				String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
				System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
						"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
				InsuranceLeShanGongShang gs = new InsuranceLeShanGongShang(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9);
				list.add(gs);
			}
		} catch (Exception e) {
			tracer.addTag("getgongshangmsg.parser", taskId);
			return list;
		}
		return list;
	}

	public List<InsuranceLeShanShiYe> getshiyemsg(String taskId, String contentAsString3) {
		List<InsuranceLeShanShiYe> list = null;
		try {
			list = new ArrayList<>();
			JSONArray jsonArray = JSONObject.fromObject(contentAsString3).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
				String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
				String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
				String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
				String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
				String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
				String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
				String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
				String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
				System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
						"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
				InsuranceLeShanShiYe sy = new InsuranceLeShanShiYe(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9);
				list.add(sy);
			}
		} catch (Exception e) {
			tracer.addTag("getshiyemsg.parser", taskId);
			return list;
		}
		return list;
	}

	public List<InsuranceLeShanShengYu> getshengyuMsg(String taskId, String contentAsString3) {
		List<InsuranceLeShanShengYu> list = null;
		try {
			list = new ArrayList<>();
			JSONArray jsonArray = JSONObject.fromObject(contentAsString3).getJSONObject("lists").getJSONObject("resultset").getJSONArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				String text = jsonArray.getJSONObject(i).getString("aab004").trim();//单位名称
				String text2 = jsonArray.getJSONObject(i).getString("aae003").trim();//缴费年月
				String text3 = jsonArray.getJSONObject(i).getString("yae180").trim();//个人缴费基数
				String text4 = jsonArray.getJSONObject(i).getString("dwjf").trim();//单位缴费部分
				String text5 = jsonArray.getJSONObject(i).getString("grjf").trim();//个人缴费部分
				String text6 = jsonArray.getJSONObject(i).getString("aae078_").trim();//缴费状态
				String text7 = jsonArray.getJSONObject(i).getString("aaa027_").trim();//参保地
				String text8 = jsonArray.getJSONObject(i).getString("hrgz").trim();//划入个账金额
				String text9 = jsonArray.getJSONObject(i).getString("yac038_").trim();//参保类型
				System.out.println("\r单位名称:"+text+"\r缴费年月:"+text2+"\r个人缴费基数:"+text3+"\r单位缴费部分:"+text4+
						"\r个人缴费部分:"+text5+"\r缴费状态:"+text6+"\r参保地:"+text7+"\r划入个账金额:"+text8+"\r参保类型:"+text9);
				InsuranceLeShanShengYu sy = new InsuranceLeShanShengYu(taskId,text,text2,text3,text4,text5,text6,text7,text8,text9);
				list.add(sy);
			}
		} catch (Exception e) {
			tracer.addTag("getshengyuMsg.parser", taskId);
			return list;
		}
		return list;
	}
	
	
}
