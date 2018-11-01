package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangGongShanginfo;
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangShengYuinfo;
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangShiYeinfo;
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangUserInfo;
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangYangLaoinfo;
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangYiLiaoinfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class InsuranceSZZheJiangParser {

	@Autowired
	private TracerLog tracer;

	public InsuranceSZZheJiangUserInfo getuserinfo(String html2, String html4, String taskId, String html6) {
		InsuranceSZZheJiangUserInfo user = null;
		try {
			user = new InsuranceSZZheJiangUserInfo();
			JSONObject object = JSONObject.fromObject(html2);
			JSONObject obj = object.getJSONArray("data").getJSONObject(0);
			//{"aac003":"*海琴","jfzt":"参保缴费","cbzt":"正常参保","eac101":"15158984773","aab004":"浙江中通文博服务有限公司义乌分公司","aac002":"3****************5"}
			String string = obj.getString("aac003");//姓名
			user.setName(string);
			if(obj.has("aac031")){
			String string2 = obj.getString("aac031");//参保状态
			user.setInsured_state(string2);
			}
			if(obj.has("cbzt")){
				String string2 = obj.getString("cbzt");//参保状态
				user.setInsured_state(string2);
			}
			String string3 = obj.getString("aab004");//公司名称
			String string4 = obj.getString("aac002");//社会保障号
			user.setTaskid(taskId);
			
			user.setDw_name(string3);
			user.setInsurance_num(string4);
			JSONObject object2 = JSONObject.fromObject(html4);
			JSONObject obj2 = object2.getJSONArray("data").getJSONObject(0);
			if(obj2.has("aic111")){
			String string5 = obj2.getString("eic058");//至上年末实际缴费月数
			user.setLastyear_pay_month(string5);
			String string6 = obj2.getString("aae001");//年度
			user.setDate(string6);
			String string7 = obj2.getString("aic072");//上年末记账金额
			user.setLastyear_tally(string7);
			String string8 = obj2.getString("aic073");//上年末记账利息
			user.setInterest(string8);
			String string9 = obj2.getString("eic001");//截至上年末个人账户累计储存额
			user.setLastyear_account(string9);
			String string10 = obj2.getString("aic111");//本年末个人账户累计存储额
			user.setYear_account(string10);
			String string11 = obj2.getString("aic112");//本年末记账金额
			user.setYear_tally(string11);
			}
			Document parse = Jsoup.parse(html6);
			Elements elementsByClass = parse.getElementsByClass("lanmu_sub2_1");
			String string12 = elementsByClass.get(0).text().trim();//帐号名
			user.setNum_name(string12);
			String string13 = elementsByClass.get(3).text().trim();//手机号
			user.setTel(string13);
			String string14 = elementsByClass.get(5).text().trim();//邮箱号
			user.setEmail(string14);
			String string15 = elementsByClass.get(6).text().trim();//安全等级
			user.setSecurity(string15);
			String string16 = elementsByClass.get(7).text().trim();//性别
			user.setSex(string16);
			String string17 = elementsByClass.get(8).text().trim();//民族
			user.setNation(string17);
			String string18 = elementsByClass.get(9).text().trim();//护照
			user.setPassport(string18);
		} catch (Exception e) {
			tracer.addTag("action.sz.zhejiang.getuserinfo", "解析错误："+e.toString());
			return user;
		}
		return user;

	}

	public List<InsuranceSZZheJiangYangLaoinfo> getyanglaoinfo(String html5, String taskId) {
		List<InsuranceSZZheJiangYangLaoinfo> list = null;
		try {
			JSONObject object5 = JSONObject.fromObject(html5);
			JSONArray obArray = object5.getJSONArray("data");
			list = new ArrayList<InsuranceSZZheJiangYangLaoinfo>();
			for (int i = 0; i < obArray.size(); i++) {
				if(obArray.getJSONObject(i).has("aae002")){
					String string2 = obArray.getJSONObject(i).getString("aae002");//缴费年月
					String string3 = obArray.getJSONObject(i).getString("jfjs");//缴费基数
					String string4 = obArray.getJSONObject(i).getString("gryj");//个人缴费金额
					String string5 = null;
					if(obArray.getJSONObject(i).has("aab004")){
						string5 = obArray.getJSONObject(i).getString("aab004");//公司名称
					}
					String string6 = null;
					if(obArray.getJSONObject(i).has("aae143")){
						string6 = obArray.getJSONObject(i).getString("aae143");//缴费类型
					}
					String string7 = obArray.getJSONObject(i).getString("aae111");//缴纳状态
					System.out.println("缴费年月:"+string2+"\r缴费基数:"+string3+"\r个人缴费金额:"+string4+"\r公司名称:"+
							string5+"\r缴费类型:"+string6+"\r缴纳状态:"+string7);
					list.add(new InsuranceSZZheJiangYangLaoinfo(taskId,string2,string3,string4,string5,string6,string7,"养老保险"));
				}else if(obArray.getJSONObject(i).has("pay_money")){
					String string2 = obArray.getJSONObject(i).getString("period");//缴费年月
					String string3 = obArray.getJSONObject(i).getString("aae180");//缴费基数
					String string4 = obArray.getJSONObject(i).getString("pay_money");//个人缴费金额
					String string7 = obArray.getJSONObject(i).getString("aae078");//缴纳状态
					list.add(new InsuranceSZZheJiangYangLaoinfo(taskId,string2,string3,string4,null,null,string7,"养老保险"));
				}else{
					System.out.println("无缴纳记录");
					list = null;
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.sz.zhejiang.getyanglao", "解析错误："+e.toString());
			return list;
		}
		return list;
	}

	public List<InsuranceSZZheJiangYiLiaoinfo> getyiliao(String html6, String taskId) {
		List<InsuranceSZZheJiangYiLiaoinfo> list = null;
		try {
			JSONObject object6 = JSONObject.fromObject(html6);
			list = new ArrayList<InsuranceSZZheJiangYiLiaoinfo>();
			JSONArray obArray6 = object6.getJSONArray("data");
			for (int i = 0; i < obArray6.size(); i++) {
				if(obArray6.getJSONObject(i).has("aae002")){
					String string2 = obArray6.getJSONObject(i).getString("aae002");//缴费年月
					String string3 = obArray6.getJSONObject(i).getString("jfjs");//缴费基数
					String string4 = obArray6.getJSONObject(i).getString("gryj");//个人缴费金额
					String string5 = null;
					if(obArray6.getJSONObject(i).has("aab004")){
						string5 = obArray6.getJSONObject(i).getString("aab004");//公司名称
					}
					String string6 = null;
					if(obArray6.getJSONObject(i).has("aae143")){
						string6 = obArray6.getJSONObject(i).getString("aae143");//缴费类型
					}
					String string7 = obArray6.getJSONObject(i).getString("aae111");//缴纳状态
					System.out.println("缴费年月:"+string2+"\r缴费基数:"+string3+"\r个人缴费金额:"+string4+"\r公司名称:"+
							string5+"\r缴费类型:"+string6+"\r缴纳状态:"+string7);
					list.add(new InsuranceSZZheJiangYiLiaoinfo(taskId,string2,string3,string4,string5,string6,string7,"医疗保险"));
				}else if(obArray6.getJSONObject(i).has("aae013")){
					String string12 = obArray6.getJSONObject(i).getString("aae035");//变动日期
					String string13 = obArray6.getJSONObject(i).getString("aae013");//备考
					String string14 = obArray6.getJSONObject(i).getString("aad088");//支出金额
					String string15 = obArray6.getJSONObject(i).getString("aad089");//收入金额
					String string16 = obArray6.getJSONObject(i).getString("aaa097");//收支类型
					System.out.println("变动日期:"+string12+"\r备考:"+string13+"\r支出金额:"+string14+"\r收入金额:"+
							string15+"\r收支类型:"+string16);
					list.add(new InsuranceSZZheJiangYiLiaoinfo(taskId,"医疗保险",string12,string13,string14,string15,string16));
				}else if(obArray6.getJSONObject(i).has("pay_money")){
					String string2 = obArray6.getJSONObject(i).getString("period");//缴费年月
					String string3 = obArray6.getJSONObject(i).getString("aae180");//缴费基数
					String string4 = obArray6.getJSONObject(i).getString("pay_money");//个人缴费金额
					String string7 = obArray6.getJSONObject(i).getString("aae078");//缴纳状态
					list.add(new InsuranceSZZheJiangYiLiaoinfo(taskId,string2,string3,string4,null,null,string7,"医疗保险"));
				}else{
					System.out.println("无缴纳记录");
					list = null;
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.sz.zhejiang.getyiliao", "解析错误："+e.toString());
			return list;
		}
		return list;
	}

	public List<InsuranceSZZheJiangGongShanginfo> getgongshang(String taskId, String html) {
		List<InsuranceSZZheJiangGongShanginfo> list = null;
		try {
			JSONObject object7 = JSONObject.fromObject(html);
			list = new ArrayList<InsuranceSZZheJiangGongShanginfo>();
			JSONArray obArray7 = object7.getJSONArray("data");
			for (int i = 0; i < obArray7.size(); i++) {
				if(obArray7.getJSONObject(i).has("aae002")){
					String string2 = obArray7.getJSONObject(i).getString("aae002");//缴费年月
					String string3 = obArray7.getJSONObject(i).getString("jfjs");//缴费基数
					String string4 = obArray7.getJSONObject(i).getString("gryj");//个人缴费金额
					String string5 = null;
					if(obArray7.getJSONObject(i).has("aab004")){
						string5 = obArray7.getJSONObject(i).getString("aab004");//公司名称
					}
					String string6 = null;
					if(obArray7.getJSONObject(i).has("aae143")){
						string6 = obArray7.getJSONObject(i).getString("aae143");//缴费类型
					}
					String string7 = obArray7.getJSONObject(i).getString("aae111");//缴纳状态
					System.out.println("缴费年月:"+string2+"\r缴费基数:"+string3+"\r个人缴费金额:"+string4+"\r公司名称:"+
							string5+"\r缴费类型:"+string6+"\r缴纳状态:"+string7);
					list.add(new InsuranceSZZheJiangGongShanginfo(taskId,string2,string3,string4,string5,string6,string7,"工伤保险"));
				}else if(obArray7.getJSONObject(i).has("aae013")){
					String string12 = obArray7.getJSONObject(i).getString("aae035");//变动日期
					String string13 = obArray7.getJSONObject(i).getString("aae013");//备考
					String string14 = obArray7.getJSONObject(i).getString("aad088");//支出金额
					String string15 = obArray7.getJSONObject(i).getString("aad089");//收入金额
					String string16 = obArray7.getJSONObject(i).getString("aaa097");//收支类型
					System.out.println("变动日期:"+string12+"\r备考:"+string13+"\r支出金额:"+string14+"\r收入金额:"+
							string15+"\r收支类型:"+string16);
					list.add(new InsuranceSZZheJiangGongShanginfo(taskId,string12,null,string13,null,string14,string13,"工伤保险"));
				}else if(obArray7.getJSONObject(i).has("pay_money")){
					String string2 = obArray7.getJSONObject(i).getString("period");//缴费年月
					String string3 = obArray7.getJSONObject(i).getString("aae180");//缴费基数
					String string4 = obArray7.getJSONObject(i).getString("pay_money");//个人缴费金额
					String string7 = obArray7.getJSONObject(i).getString("aae078");//缴纳状态
					list.add(new InsuranceSZZheJiangGongShanginfo(taskId,string2,string3,string4,null,null,string7,"工伤保险"));
				}else{
					System.out.println("无缴纳记录");
					list = null;
				}
			}

		} catch (Exception e) {
			tracer.addTag("action.sz.zhejiang.getgongshang", "解析错误："+e.toString());
			return list;
		}
		return list;
	}

	public List<InsuranceSZZheJiangShiYeinfo> getshiyeMsg(String html, String taskId) {
		List<InsuranceSZZheJiangShiYeinfo> list = null;
		try {
			JSONObject object7 = JSONObject.fromObject(html);
			list = new ArrayList<InsuranceSZZheJiangShiYeinfo>();
			JSONArray obArray7 = object7.getJSONArray("data");
			for (int i = 0; i < obArray7.size(); i++) {
				if(obArray7.getJSONObject(i).has("aae002")){
					String string2 = obArray7.getJSONObject(i).getString("aae002");//缴费年月
					String string3 = obArray7.getJSONObject(i).getString("jfjs");//缴费基数
					String string4 = obArray7.getJSONObject(i).getString("gryj");//个人缴费金额
					String string5 = null;
					if(obArray7.getJSONObject(i).has("aab004")){
						string5 = obArray7.getJSONObject(i).getString("aab004");//公司名称
					}
					String string6 = null;
					if(obArray7.getJSONObject(i).has("aae143")){
						string6 = obArray7.getJSONObject(i).getString("aae143");//缴费类型
					}
					String string7 = obArray7.getJSONObject(i).getString("aae111");//缴纳状态
					System.out.println("缴费年月:"+string2+"\r缴费基数:"+string3+"\r个人缴费金额:"+string4+"\r公司名称:"+
							string5+"\r缴费类型:"+string6+"\r缴纳状态:"+string7);
					list.add(new InsuranceSZZheJiangShiYeinfo(taskId,string2,string3,string4,string5,string6,string7,"失业保险"));
				}else if(obArray7.getJSONObject(i).has("pay_money")){
					String string2 = obArray7.getJSONObject(i).getString("period");//缴费年月
					String string3 = obArray7.getJSONObject(i).getString("aae180");//缴费基数
					String string4 = obArray7.getJSONObject(i).getString("pay_money");//个人缴费金额
					String string7 = obArray7.getJSONObject(i).getString("aae078");//缴纳状态
					list.add(new InsuranceSZZheJiangShiYeinfo(taskId,string2,string3,string4,null,null,string7,"失业保险"));
				}else{
					System.out.println("无缴纳记录");
					list = null;
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.sz.zhejiang.getshiyeMsg", "解析错误："+e.toString());
			return list;
		}
		return list;
	}

	public List<InsuranceSZZheJiangShengYuinfo> getshengyuMsg(String html, String taskId) {
		List<InsuranceSZZheJiangShengYuinfo> list = null;
		try {
			JSONObject object7 = JSONObject.fromObject(html);
			list = new ArrayList<InsuranceSZZheJiangShengYuinfo>();
			JSONArray obArray7 = object7.getJSONArray("data");
			for (int i = 0; i < obArray7.size(); i++) {
				if(obArray7.getJSONObject(i).has("aae002")){
					String string2 = obArray7.getJSONObject(i).getString("aae002");//缴费年月
					String string3 = obArray7.getJSONObject(i).getString("jfjs");//缴费基数
					String string4 = obArray7.getJSONObject(i).getString("gryj");//个人缴费金额
					String string5 = null;
					if(obArray7.getJSONObject(i).has("aab004")){
						string5 = obArray7.getJSONObject(i).getString("aab004");//公司名称
					}
					String string6 = null;
					if(obArray7.getJSONObject(i).has("aae143")){
						string6 = obArray7.getJSONObject(i).getString("aae143");//缴费类型
					}
					String string7 = obArray7.getJSONObject(i).getString("aae111");//缴纳状态
					System.out.println("缴费年月:"+string2+"\r缴费基数:"+string3+"\r个人缴费金额:"+string4+"\r公司名称:"+
							string5+"\r缴费类型:"+string6+"\r缴纳状态:"+string7);
					list.add(new InsuranceSZZheJiangShengYuinfo(taskId,string2,string3,string4,string5,string6,string7,"生育保险"));
				}else if(obArray7.getJSONObject(i).has("pay_money")){
					String string2 = obArray7.getJSONObject(i).getString("period");//缴费年月
					String string3 = obArray7.getJSONObject(i).getString("aae180");//缴费基数
					String string4 = obArray7.getJSONObject(i).getString("pay_money");//个人缴费金额
					String string7 = obArray7.getJSONObject(i).getString("aae078");//缴纳状态
					list.add(new InsuranceSZZheJiangShengYuinfo(taskId,string2,string3,string4,null,null,string7,"失业保险"));
				}else{
					System.out.println("无缴纳记录");
					list = null;
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.sz.zhejiang.getshengyuMsg", "解析错误："+e.toString());
			return list;
		}
		return list;
	}

}
