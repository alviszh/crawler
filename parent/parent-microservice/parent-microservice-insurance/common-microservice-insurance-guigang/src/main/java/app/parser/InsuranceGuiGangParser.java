package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangGongShangInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangShengYuInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangShiYeInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangUserInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangYangLaoInfo;
import com.microservice.dao.entity.crawler.insurance.guigang.InsuranceGuiGangYiLiaoInfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceGuiGangParser {
	@Autowired
	private TracerLog tracer;
	public InsuranceGuiGangUserInfo getuser(String html4, String html5, String taskid) {
		InsuranceGuiGangUserInfo user = null;
		try {
			JSONObject object = JSONObject.fromObject(html4);
			String username = object.getJSONObject("fieldData").getString("aac003");//姓名
			String gsname = object.getJSONObject("fieldData").getString("aab069");//公司名称
			String zt = object.getJSONObject("fieldData").getString("aac031");//状态
			String idcard = object.getJSONObject("fieldData").getString("aac002");//身份证
			String tel = object.getJSONObject("fieldData").getString("aae005");//电话
			String yl = object.getJSONObject("fieldData").getString("aae240yl");//养老账户余额
			String yb = object.getJSONObject("fieldData").getString("aae240yb");//医保账户余额
			String time = object.getJSONObject("fieldData").getString("maxaab191");//最近缴费时间
			JSONObject object2 = JSONObject.fromObject(html5);
			String num = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac001");//个人编号
			String sex = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac004");//性别
			String mz = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac005");//民族
			String brithday = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac006");//出生日期
			String cgtime = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac007");//参加工作日期
			String hkxz = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac009");//户口性质
			String hkszd = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac010");//户口所在地
			String ryzt = object2.getJSONObject("fieldData").getJSONObject("info").getString("aac008");//人员状态

			System.out.println("个人信息如下：\r姓名："+username+"\r公司名称:"+gsname+"\r状态:"+zt+"\r身份证:"
					+idcard+"\r电话:"+tel+"\r养老账户余额:"+yl+"\r医保账户余额:"+yb+"\r最近缴费时间:"+time+"\r个人编号:"+num
					+"\r性别:"+sex+"\r民族:"+mz+"\r出生日期:"+brithday+"\r参加工作日期:"+cgtime+"\r户口性质:"+hkxz+"\r户口所在地:"+hkszd+"\r人员状态:"+ryzt);

			user = new InsuranceGuiGangUserInfo(taskid,username,gsname,zt,idcard,tel,yl,yb,time
					,num,sex,mz,brithday,cgtime,hkxz,hkszd,ryzt);

		} catch (Exception e) {
			tracer.addTag("insuranceduigangService.crawler.getuserinfo.error", e.getMessage());
			return user;
		}
		return user;
	}
	public List<InsuranceGuiGangYangLaoInfo> getyanglaomsg(String taskId, String html6) {
		List<InsuranceGuiGangYangLaoInfo> list = null;
		try {
			list = new ArrayList<InsuranceGuiGangYangLaoInfo>();
			System.out.println("有数据：");
			JSONObject object = JSONObject.fromObject(html6);
			JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
			for (int i = 0; i < array.size(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				String string = object2.getString("aab069");//公司名称
				String string2 = object2.getString("aac003");//姓名
				String string3 = object2.getString("aae002");//期号
				String string4 = object2.getString("aic020");//缴费工资
				String string5 = object2.getString("dwjfhtcje");//单位缴费
				String string6 = object2.getString("grjnje");//个人缴费
				String string7 = object2.getString("aab191");//到账日期
				String string8 = object2.getString("aac066");//职务类型
				String type = "企业养老";
				System.out.println("第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				InsuranceGuiGangYangLaoInfo yanglao = new InsuranceGuiGangYangLaoInfo(
						taskId,string,string2,string3,string4,string5,string6,string7,string8,type);
				list.add(yanglao);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceguigangService.crawler.getyanglao.error", e.getMessage());
			return list;
		}
		return list;
	}
	public List<InsuranceGuiGangYiLiaoInfo> getyiliaoMsg(String taskId, String html) {
		List<InsuranceGuiGangYiLiaoInfo> list = null;
		try {
			list = new ArrayList<InsuranceGuiGangYiLiaoInfo>();
			JSONObject object = JSONObject.fromObject(html);
			JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
			for (int i = 0; i < array.size(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				String string = object2.getString("aab069");//公司名称
				String string2 = object2.getString("aac003");//姓名
				String string3 = object2.getString("aae002");//期号
				String string4 = object2.getString("aic020");//缴费工资
				String string5 = object2.getString("dwjfhtcje");//单位缴费
				String string6 = object2.getString("grjnje");//个人缴费
				String string7 = object2.getString("aab191");//到账日期
				String string8 = object2.getString("aac066");//职务类型
				String type = "基本医疗";
				System.out.println("第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				InsuranceGuiGangYiLiaoInfo yiliao = new InsuranceGuiGangYiLiaoInfo(
						taskId,string,string2,string3,string4,string5,string6,string7,string8,type);
				list.add(yiliao);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceguigangService.crawler.getyiliao.error", e.getMessage());
			return list;
		}
		return list;
	}
	public List<InsuranceGuiGangGongShangInfo> getgongshangmsg(String taskId, String html6) {
		List<InsuranceGuiGangGongShangInfo> list = null;
		try {
			list = new ArrayList<InsuranceGuiGangGongShangInfo>();
			JSONObject object = JSONObject.fromObject(html6);
			JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
			for (int i = 0; i < array.size(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				String string = object2.getString("aab069");//公司名称
				String string2 = object2.getString("aac003");//姓名
				String string3 = object2.getString("aae002");//期号
				String string4 = object2.getString("aic020");//缴费工资
				String string5 = object2.getString("dwjfhtcje");//单位缴费
				String string6 = object2.getString("grjnje");//个人缴费
				String string7 = object2.getString("aab191");//到账日期
				String string8 = object2.getString("aac066");//职务类型
				String type = "工伤保险";
				System.out.println("第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				InsuranceGuiGangGongShangInfo gs = new InsuranceGuiGangGongShangInfo(
						taskId,string,string2,string3,string4,string5,string6,string7,string8,type);
				list.add(gs);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceguigangService.crawler.getgongshang.error", e.getMessage());
			return list;
		}
		return list;
	}
	public List<InsuranceGuiGangShiYeInfo> getshiyemsg(String taskId, String html6) {
		List<InsuranceGuiGangShiYeInfo> list = null;
		try {
			list = new ArrayList<InsuranceGuiGangShiYeInfo>();
			JSONObject object = JSONObject.fromObject(html6);
			JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
			for (int i = 0; i < array.size(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				String string = object2.getString("aab069");//公司名称
				String string2 = object2.getString("aac003");//姓名
				String string3 = object2.getString("aae002");//期号
				String string4 = object2.getString("aic020");//缴费工资
				String string5 = object2.getString("dwjfhtcje");//单位缴费
				String string6 = object2.getString("grjnje");//个人缴费
				String string7 = object2.getString("aab191");//到账日期
				String string8 = object2.getString("aac066");//职务类型
				String type = "失业保险";
				System.out.println("第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				InsuranceGuiGangShiYeInfo sy = new InsuranceGuiGangShiYeInfo(
						taskId,string,string2,string3,string4,string5,string6,string7,string8,type);
				list.add(sy);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceguigangService.crawler.getshiye.error", e.getMessage());
			return list;
		}
		return list;
	}
	public List<InsuranceGuiGangShengYuInfo> getshengyuMsg(String taskId, String html6) {
		List<InsuranceGuiGangShengYuInfo> list = null;
		try {
			list = new ArrayList<InsuranceGuiGangShengYuInfo>();
			JSONObject object = JSONObject.fromObject(html6);
			JSONArray array = object.getJSONObject("lists").getJSONObject("dg_payment").getJSONArray("list");
			for (int i = 0; i < array.size(); i++) {
				JSONObject object2 = array.getJSONObject(i);
				String string = object2.getString("aab069");//公司名称
				String string2 = object2.getString("aac003");//姓名
				String string3 = object2.getString("aae002");//期号
				String string4 = object2.getString("aic020");//缴费工资
				String string5 = object2.getString("dwjfhtcje");//单位缴费
				String string6 = object2.getString("grjnje");//个人缴费
				String string7 = object2.getString("aab191");//到账日期
				String string8 = object2.getString("aac066");//职务类型
				String type = "生育保险";
				System.out.println("第"+(i+1)+"月："+string + string2 + string3 + string4 + string5 + string6 + string7 + string8 + type);
				InsuranceGuiGangShengYuInfo sy = new InsuranceGuiGangShengYuInfo(
						taskId,string,string2,string3,string4,string5,string6,string7,string8,type);
				list.add(sy);
			}
		} catch (Exception e) {
			tracer.addTag("insuranceguigangService.crawler.getshengyu.error", e.getMessage());
			return list;
		}
		return list;
	}

}
