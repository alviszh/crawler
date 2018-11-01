package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoUserInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.jiaozuo.InsuranceJiaoZuoYiLiaoInfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceJiaoZuoParser {

	@Autowired
	private TracerLog tracer;
	//个人
	public InsuranceJiaoZuoUserInfo getuserinfo(String html2, String taskid) {
		InsuranceJiaoZuoUserInfo user = null;
		try {
		JSONObject object = JSONObject.fromObject(html2).getJSONObject("DetailData");
		String string = object.getString("PersonID");//个人编号
		String string2 = object.getString("Name");//姓名
		String string3 = object.getString("IdCard");//身份证
		String string4 = object.getString("Gender");//性别
		String string5 = object.getString("Nation");//民族
		String string6 = object.getString("Birthday");//出生日期
		String string7 = object.getString("PersonStatus");//人员状态
		String string8 = object.getString("UnitId");//单位编号
		String string9 = object.getString("UnitName");//单位名称
		String string10 = object.getString("FirstInsuredDate");//参保日期
		String string11 = object.getString("CreateAccountDate");//参保年月
		String string12 = object.getString("InsuredStatus");//参保状态
		String string13 = object.getString("ShouldPayMonths");//应缴月数
		String string14 = object.getString("RealPayMonths");//实缴月数
		String string15 = object.getString("Address");//地址
		String string16 = object.getString("Telephone");//联系方式
		
		System.out.println("个人编号:"+string+"\r姓名："+string2+"\r身份证："+string3+"\r性别："+string4+"\r民族："
		+string5+"\r出生日期："+string6+"\r人员状态："+string7+"\r单位编号："+string8+"\r单位名称："+string9+"\r参保日期："+string10+"\r参保年月："+string11
		+"\r参保状态："+string12+"\r应缴月数："+string13+"\r实缴月数："+string14+"\r地址："+string15+"\r联系方式："+string16+"\r");
		user = new InsuranceJiaoZuoUserInfo(string,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12
				,string13,string14,string15,string16,taskid);
		} catch (Exception e) {
			tracer.addTag("insurancejiaozuoService.crawler.getuserinfo.error", e.getMessage());
			return user;
		}
		return user;
	}
	//养老
	public List<InsuranceJiaoZuoYangLaoInfo> getyanglaoMsg(String html2, String taskid) {
		List<InsuranceJiaoZuoYangLaoInfo> list = null;
		try {
			list = new ArrayList<InsuranceJiaoZuoYangLaoInfo>();
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
				InsuranceJiaoZuoYangLaoInfo yanglao = new InsuranceJiaoZuoYangLaoInfo(
						string2,string3,string4,string5,string6,string7,string8,string9,string10,taskid);
				list.add(yanglao);
			}
		} catch (Exception e) {
			tracer.addTag("insurancejiaozuoService.crawler.getyanglaoinfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	//医疗
	public List<InsuranceJiaoZuoYiLiaoInfo> getyiliaomsg(String html2, String taskid) {
		List<InsuranceJiaoZuoYiLiaoInfo> list = null;
		try {
			list = new ArrayList<InsuranceJiaoZuoYiLiaoInfo>();
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
				InsuranceJiaoZuoYiLiaoInfo yiliao = new InsuranceJiaoZuoYiLiaoInfo(
						string2,string3,string4,string5,string6,string7,string8,string9,string10,taskid);
				list.add(yiliao);
			}
		} catch (Exception e) {
			tracer.addTag("insurancejiaozuoService.crawler.getyiliaoinfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	//工伤
	public List<InsuranceJiaoZuoGongShangInfo> getgongshangmsg(String html2, String taskid) {
		List<InsuranceJiaoZuoGongShangInfo> list = null;
		try {
			list = new ArrayList<InsuranceJiaoZuoGongShangInfo>();
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
				InsuranceJiaoZuoGongShangInfo gs = new InsuranceJiaoZuoGongShangInfo(
						string2,string3,string4,string5,string6,string7,string8,string9,string10,taskid);
				list.add(gs);
			}
		} catch (Exception e) {
			tracer.addTag("insurancejiaozuoService.crawler.getgongshanginfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	//失业
	public List<InsuranceJiaoZuoShiYeInfo> getshiyemsg(String html2, String taskid) {
		List<InsuranceJiaoZuoShiYeInfo> list = null;
		try {
			list = new ArrayList<InsuranceJiaoZuoShiYeInfo>();
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
				InsuranceJiaoZuoShiYeInfo sy = new InsuranceJiaoZuoShiYeInfo(
						string2,string3,string4,string5,string6,string7,string8,string9,string10,taskid);
				list.add(sy);
			}
		} catch (Exception e) {
			tracer.addTag("insurancejiaozuoService.crawler.getshiyeinfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	//生育
	public List<InsuranceJiaoZuoShengYuInfo> getshengyumsg(String html2, String taskid) {
		List<InsuranceJiaoZuoShengYuInfo> list = null;
		try {
			list = new ArrayList<InsuranceJiaoZuoShengYuInfo>();
			String string = JSONObject.fromObject(html2).getString("data");
			String all = string.replace("\\", "");
			System.out.println(all);
			JSONArray object = JSONArray.fromObject(all);
			for (int i = 0; i < object.size(); i++) {
				String string2 = object.getJSONObject(i).getString("Category");
				String string3 = object.getJSONObject(i).getString("Unit");
				String string4 = object.getJSONObject(i).getString("Time");
				String string5 = object.getJSONObject(i).getString("Base");
				String string6 = object.getJSONObject(i).getString("Sign");
				String string7 = object.getJSONObject(i).getString("UnitPay");
				String string8 = object.getJSONObject(i).getString("PersonalPay");
				String string9 = object.getJSONObject(i).getString("Total");
				String string10 = object.getJSONObject(i).getString("PayType");
				System.out.println("保险类型："+string2+"\r单位名称："+string3+"\r缴费年月："+string4+"\r缴费基数："+
				string5+"\r缴费标志："+string6+"\r单位缴费："+string7+"\r个人缴费："+string8+"\r缴费金额："+string9+"\r缴费类型:"+string10+"\r");
				InsuranceJiaoZuoShengYuInfo sy = new InsuranceJiaoZuoShengYuInfo(
						string2,string3,string4,string5,string6,string7,string8,string9,string10,taskid);
				list.add(sy);
			}
		} catch (Exception e) {
			tracer.addTag("insurancejiaozuoService.crawler.getshengyuinfo.error", e.getMessage());
			return list;
		}
		return list;
	}
	
	
}
