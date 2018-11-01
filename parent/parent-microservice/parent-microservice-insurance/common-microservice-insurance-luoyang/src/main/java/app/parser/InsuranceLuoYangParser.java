package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangUserInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.luoyang.InsuranceLuoYangYiLiaoInfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceLuoYangParser {
	@Autowired
	private TracerLog tracer;
	
	public InsuranceLuoYangUserInfo getuserinfo(String taskId, String html) {
		InsuranceLuoYangUserInfo insuranceLuoYangUserInfo =null;
		try {
		String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jbxx_data")).getString("data");
		JSONArray object = JSONArray.fromObject(string);
		JSONObject obj = object.getJSONObject(0);
		if(obj.equals("")){
			obj = object.getJSONObject(1);
		}
		String string2 = obj.getString("F007");//出生日期
		String string3 = obj.getString("F006ZH");//民族
		String string4 = obj.getString("F008");//首次参保时间
		String string5 = obj.getString("F001");//单位编号
		String string6 = obj.getString("F001ZH");//单位名称
		String string7 = obj.getString("F002");//个人编号
		String string8 = obj.getString("F003");//身份证号
		String string9 = obj.getString("F004");//姓名
		String string10 = obj.getString("F005ZH");//性别
		String string11 = obj.getString("F009ZH");//人员状态
		
//		System.out.println("出生日期:"+string2+"\r民族："+string3+"\r首次参保时间:"+string4+"\r单位编号:"
//				+string5+"\r单位名称:"+string6+"\r个人编号:"+string7+"\r身份证号:"+string8+"\r姓名:"+
//				string9+"\r性别:"+string10+"\r人员状态:"+string11);
		insuranceLuoYangUserInfo = new InsuranceLuoYangUserInfo(
				taskId,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11);
		return insuranceLuoYangUserInfo;
		} catch (Exception e) {
			tracer.addTag("insuranceluoyangService.crawler.getuserinfo.error", e.getMessage());
			return insuranceLuoYangUserInfo;
		}
	}
	//养老
	public List<InsuranceLuoYangYangLaoInfo> getyanglaoMsg(String html, String taskId) {
		List<InsuranceLuoYangYangLaoInfo> list = null;
		try {
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
			JSONArray object = JSONArray.fromObject(string);
			list = new ArrayList<InsuranceLuoYangYangLaoInfo>();
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string10 = object.getJSONObject(i).getString("F003");//征缴月份
				String string11 = object.getJSONObject(i).getString("F004");//计划月份
				String string12 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string13 = object.getJSONObject(i).getString("F001");//单位编号
				
//				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
//						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string10+"\r计划月份:"+string11+"\r单位名称："+string12+"\r单位编号："+string13+"\r\r");
				InsuranceLuoYangYangLaoInfo insuranceLuoYangYangLaoInfo = new InsuranceLuoYangYangLaoInfo(
						taskId,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12,string13);
				list.add(insuranceLuoYangYangLaoInfo);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getyanglaoinfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	//医疗
	public List<InsuranceLuoYangYiLiaoInfo> getyiliaoMsg(String taskId, String html) {
		List<InsuranceLuoYangYiLiaoInfo> list = null;
		try {
			list = new ArrayList<InsuranceLuoYangYiLiaoInfo>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string10 = object.getJSONObject(i).getString("F003");//征缴月份
				String string11 = object.getJSONObject(i).getString("F004");//计划月份
				String string12 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string13 = object.getJSONObject(i).getString("F001");//单位编号
//				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
//						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string10+"\r计划月份:"+string11+"\r单位名称："+string12+"\r单位编号："+string13+"\r\r");
				InsuranceLuoYangYiLiaoInfo yiliao = new InsuranceLuoYangYiLiaoInfo(
						taskId,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12,string13);
				list.add(yiliao);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getyiliaoinfo.error", e.getMessage());
			return list;
		}
		
		return list;
	}
	//工伤
	public List<InsuranceLuoYangGongShangInfo> getgongshangmsg(String taskId, String html) {
		List<InsuranceLuoYangGongShangInfo> list = null;
		try {
			list = new ArrayList<InsuranceLuoYangGongShangInfo>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string10 = object.getJSONObject(i).getString("F003");//征缴月份
				String string11 = object.getJSONObject(i).getString("F004");//计划月份
				String string12 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string13 = object.getJSONObject(i).getString("F001");//单位编号
//				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
//						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string10+"\r计划月份:"+string11+"\r单位名称："+string12+"\r单位编号："+string13+"\r\r");
				InsuranceLuoYangGongShangInfo gongshang = new InsuranceLuoYangGongShangInfo(
						taskId,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12,string13);
				list.add(gongshang);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getgongshanginfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	//失业
	public List<InsuranceLuoYangShiYeInfo> getshiyemsg(String taskId, String html) {
		List<InsuranceLuoYangShiYeInfo> list = null;
		try {
			list = new ArrayList<InsuranceLuoYangShiYeInfo>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string10 = object.getJSONObject(i).getString("F003");//征缴月份
				String string11 = object.getJSONObject(i).getString("F004");//计划月份
				String string12 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string13 = object.getJSONObject(i).getString("F001");//单位编号
//				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
//						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string10+"\r计划月份:"+string11+"\r单位名称："+string12+"\r单位编号："+string13+"\r\r");
				InsuranceLuoYangShiYeInfo shiye = new InsuranceLuoYangShiYeInfo(
						taskId,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12,string13);
				list.add(shiye);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getshiyeinfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	//生育
	public List<InsuranceLuoYangShengYuInfo> getshengyuMsg(String taskId, String html) {
		List<InsuranceLuoYangShengYuInfo> list = null;
		try {
			list = new ArrayList<InsuranceLuoYangShengYuInfo>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data")).getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for(int i=0;i<object.size();i++){
				String string2 = object.getJSONObject(i).getString("F012ZH");//摘要
				String string3 = object.getJSONObject(i).getString("F007");//合计
				String string4 = object.getJSONObject(i).getString("F006");//缴费基数
				String string5 = object.getJSONObject(i).getString("F009");//个人缴费
				String string6 = object.getJSONObject(i).getString("F008");//单位缴费
				String string7 = object.getJSONObject(i).getString("F013");//到账日期
				String string8 = object.getJSONObject(i).getString("F005ZH");//缴费标志
				String string9 = object.getJSONObject(i).getString("F011");//记入个人部分
				String string10 = object.getJSONObject(i).getString("F003");//征缴月份
				String string11 = object.getJSONObject(i).getString("F004");//计划月份
				String string12 = object.getJSONObject(i).getString("F001ZH");//单位名称
				String string13 = object.getJSONObject(i).getString("F001");//单位编号
//				System.out.println("摘要："+string2+"\r合计："+string3+"\r缴费基数："+string4+"\r个人缴费："+string5+"\r单位缴费："+string6+"\r到账日期："+string7
//						+"\r缴费标志："+string8+"\r记入个人部分："+string9+"\r征缴月份:"+string10+"\r计划月份:"+string11+"\r单位名称："+string12+"\r单位编号："+string13+"\r\r");
				InsuranceLuoYangShengYuInfo shengyu = new InsuranceLuoYangShengYuInfo(
						taskId,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12,string13);
				list.add(shengyu);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceLuoYangService.crawler.getshengyuinfo.error", e.getMessage());
			return list;
		}
		return list;
	}

}
