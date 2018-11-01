package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheBear;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheInjury;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheMedical;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuohePension;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheUnemployment;
import com.microservice.dao.entity.crawler.insurance.luohe.InsuranceLuoheUserInfo;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.EUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class InsuranceLuoheParser {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private EUtils eutils;

	public InsuranceLuoheUserInfo getuserinfo(String taskId, String html) {
		try {
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jbxx_data"))
					.getString("data");
			JSONArray object = JSONArray.fromObject(string);
			JSONObject obj = object.getJSONObject(0);
			String gr_num = obj.getString("F002");// 个人编号
			String name = obj.getString("F004");// 职工姓名
			String sex = obj.getString("F005ZH");// 性别
			String nation = obj.getString("F006ZH");// 民族
			String idcard = obj.getString("F003");// 身份证号
			String birthday = obj.getString("F007");// 出生日期
			String insuredtime = obj.getString("F008");// 参加工作时间
			String state = obj.getString("F009ZH");// 人员状态
			String dw_num = obj.getString("F001");// 单位编号
			String dw_name = obj.getString("F001ZH");// 单位名称

			InsuranceLuoheUserInfo insuranceLuoheUserInfo = new InsuranceLuoheUserInfo(taskId, gr_num, name, sex,
					nation, idcard, birthday, insuredtime, state, dw_num, dw_name);
			return insuranceLuoheUserInfo;
		} catch (Exception e) {
			tracer.addTag("IinsuranceLuoheService.crawler.getuserinfo.error---Taskid--",
					taskId + eutils.getEDetail(e));
		}
		return null;
	}

	// 养老
	public List<InsuranceLuohePension> getPension(String html, String taskId) {

		try {
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data"))
					.getString("data");
			List<InsuranceLuohePension> list = new ArrayList<InsuranceLuohePension>();
			JSONArray object = JSONArray.fromObject(string);

			for (int i = 0; i < object.size(); i++) {
				String plan_month = object.getJSONObject(i).getString("F004");// 计划月份
				String impose_month = object.getJSONObject(i).getString("F003");// 征缴月份
				String pay_num = object.getJSONObject(i).getString("F006");// 缴费基数
				String dw_pay = object.getJSONObject(i).getString("F008");// 单位缴费
				String gr_pay = object.getJSONObject(i).getString("F009");// 个人缴费
				String total = object.getJSONObject(i).getString("F007");// 合计
				String write_gr = object.getJSONObject(i).getString("F011");// 记入个人部分
				String pay_sign = object.getJSONObject(i).getString("F005ZH");// 缴费标志
				String arrivetime = object.getJSONObject(i).getString("F013");// 到账日期
				String dw_name = object.getJSONObject(i).getString("F001ZH");// 单位名称
				String dw_num = object.getJSONObject(i).getString("F001");// 单位编号
				String abstracts = object.getJSONObject(i).getString("F012ZH");// 摘要
				InsuranceLuohePension insuranceLuohePension = new InsuranceLuohePension(taskId, plan_month,
						impose_month, pay_num, dw_pay, gr_pay, total, write_gr, pay_sign, arrivetime, dw_name, dw_num,
						abstracts);
				list.add(insuranceLuohePension);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("IinsuranceLuoheService.crawler.getyanglaoinfo.error---Taskid--",
					taskId + eutils.getEDetail(e));
		}
		return null;
	}

	// 医疗
	public List<InsuranceLuoheMedical> getMedical(String taskId, String html) {
		try {
			List<InsuranceLuoheMedical> list = new ArrayList<InsuranceLuoheMedical>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data"))
					.getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for (int i = 0; i < object.size(); i++) {
				String plan_month = object.getJSONObject(i).getString("F004");// 计划月份
				String impose_month = object.getJSONObject(i).getString("F003");// 征缴月份
				String pay_num = object.getJSONObject(i).getString("F006");// 缴费基数
				String dw_pay = object.getJSONObject(i).getString("F008");// 单位缴费
				String gr_pay = object.getJSONObject(i).getString("F009");// 个人缴费
				String total = object.getJSONObject(i).getString("F007");// 合计
				String write_gr = object.getJSONObject(i).getString("F011");// 记入个人部分
				String pay_sign = object.getJSONObject(i).getString("F005ZH");// 缴费标志
				String arrivetime = object.getJSONObject(i).getString("F013");// 到账日期
				String dw_name = object.getJSONObject(i).getString("F001ZH");// 单位名称
				String dw_num = object.getJSONObject(i).getString("F001");// 单位编号
				String abstracts = object.getJSONObject(i).getString("F012ZH");// 摘要

				InsuranceLuoheMedical insuranceLuoheMedical = new InsuranceLuoheMedical(taskId, plan_month,
						impose_month, pay_num, dw_pay, gr_pay, total, write_gr, pay_sign, arrivetime, dw_name, dw_num,
						abstracts);
				list.add(insuranceLuoheMedical);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("IinsuranceLuoheService.crawler.getMedical.error---Taskid--",
					taskId + eutils.getEDetail(e));
		}
		return null;
	}

	// 工伤
	public List<InsuranceLuoheInjury> getInjury(String taskId, String html) {
		try {
			List<InsuranceLuoheInjury> list = new ArrayList<InsuranceLuoheInjury>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data"))
					.getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for (int i = 0; i < object.size(); i++) {
				String plan_month = object.getJSONObject(i).getString("F004");// 计划月份
				String impose_month = object.getJSONObject(i).getString("F003");// 征缴月份
				String pay_num = object.getJSONObject(i).getString("F006");// 缴费基数
				String dw_pay = object.getJSONObject(i).getString("F008");// 单位缴费
				String gr_pay = object.getJSONObject(i).getString("F009");// 个人缴费
				String total = object.getJSONObject(i).getString("F007");// 合计
				String write_gr = object.getJSONObject(i).getString("F011");// 记入个人部分
				String pay_sign = object.getJSONObject(i).getString("F005ZH");// 缴费标志
				String arrivetime = object.getJSONObject(i).getString("F013");// 到账日期
				String dw_name = object.getJSONObject(i).getString("F001ZH");// 单位名称
				String dw_num = object.getJSONObject(i).getString("F001");// 单位编号
				String abstracts = object.getJSONObject(i).getString("F012ZH");// 摘要
				InsuranceLuoheInjury insuranceLuoheInjury = new InsuranceLuoheInjury(taskId, plan_month, impose_month,
						pay_num, dw_pay, gr_pay, total, write_gr, pay_sign, arrivetime, dw_name, dw_num, abstracts);
				list.add(insuranceLuoheInjury);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("IinsuranceLuoheService.crawler.getInjury.error---Taskid--",
					taskId + eutils.getEDetail(e));
		}
		return null;
	}

	// 失业
	public List<InsuranceLuoheUnemployment> getUnemployment(String taskId, String html) {
		try {
			List<InsuranceLuoheUnemployment> list = new ArrayList<InsuranceLuoheUnemployment>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data"))
					.getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for (int i = 0; i < object.size(); i++) {
				String plan_month = object.getJSONObject(i).getString("F004");// 计划月份
				String impose_month = object.getJSONObject(i).getString("F003");// 征缴月份
				String pay_num = object.getJSONObject(i).getString("F006");// 缴费基数
				String dw_pay = object.getJSONObject(i).getString("F008");// 单位缴费
				String gr_pay = object.getJSONObject(i).getString("F009");// 个人缴费
				String total = object.getJSONObject(i).getString("F007");// 合计
				String write_gr = object.getJSONObject(i).getString("F011");// 记入个人部分
				String pay_sign = object.getJSONObject(i).getString("F005ZH");// 缴费标志
				String arrivetime = object.getJSONObject(i).getString("F013");// 到账日期
				String dw_name = object.getJSONObject(i).getString("F001ZH");// 单位名称
				String dw_num = object.getJSONObject(i).getString("F001");// 单位编号
				String abstracts = object.getJSONObject(i).getString("F012ZH");// 摘要
				InsuranceLuoheUnemployment insuranceLuoheUnemployment = new InsuranceLuoheUnemployment(taskId,
						plan_month, impose_month, pay_num, dw_pay, gr_pay, total, write_gr, pay_sign, arrivetime,
						dw_name, dw_num, abstracts);
				list.add(insuranceLuoheUnemployment);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("IinsuranceLuoheService.crawler.getUnemployment.error---Taskid--",
					taskId + eutils.getEDetail(e));

		}
		return null;
	}

	// 生育
	public List<InsuranceLuoheBear> getBear(String taskId, String html) {
		try {
			List<InsuranceLuoheBear> list = new ArrayList<InsuranceLuoheBear>();
			String string = JSONObject.fromObject(JSONObject.fromObject(html).getString("json_jfxx_data"))
					.getString("data");
			JSONArray object = JSONArray.fromObject(string);
			for (int i = 0; i < object.size(); i++) {
				String plan_month = object.getJSONObject(i).getString("F004");// 计划月份
				String impose_month = object.getJSONObject(i).getString("F003");// 征缴月份
				String pay_num = object.getJSONObject(i).getString("F006");// 缴费基数
				String dw_pay = object.getJSONObject(i).getString("F008");// 单位缴费
				String gr_pay = object.getJSONObject(i).getString("F009");// 个人缴费
				String total = object.getJSONObject(i).getString("F007");// 合计
				String write_gr = object.getJSONObject(i).getString("F011");// 记入个人部分
				String pay_sign = object.getJSONObject(i).getString("F005ZH");// 缴费标志
				String arrivetime = object.getJSONObject(i).getString("F013");// 到账日期
				String dw_name = object.getJSONObject(i).getString("F001ZH");// 单位名称
				String dw_num = object.getJSONObject(i).getString("F001");// 单位编号
				String abstracts = object.getJSONObject(i).getString("F012ZH");// 摘要
				InsuranceLuoheBear insuranceLuoheBear = new InsuranceLuoheBear(taskId, plan_month, impose_month,
						pay_num, dw_pay, gr_pay, total, write_gr, pay_sign, arrivetime, dw_name, dw_num, abstracts);
				list.add(insuranceLuoheBear);
			}
			return list;
		} catch (Exception e) {
			tracer.addTag("IinsuranceLuoheService.crawler.getBear.error---Taskid--",
					taskId + eutils.getEDetail(e));

		}
		return null;
	}

}
