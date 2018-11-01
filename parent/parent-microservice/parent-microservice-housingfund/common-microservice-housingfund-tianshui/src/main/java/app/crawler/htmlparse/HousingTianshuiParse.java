package app.crawler.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiAccountInfo;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiPayDetailed;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiPayRecord;
import com.microservice.dao.entity.crawler.housing.tianshui.HousingTianshuiUserInfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

@Component
public class HousingTianshuiParse {

	@Autowired
	private TracerLog tracer;
	
	/**
	 * 解析帐户明细查询
	 * @param json
	 * @param taskHousing
	 * @return
	 */
	public List<HousingTianshuiPayDetailed> htmlPayDetailedParser(String json, TaskHousing taskHousing) {
		try {
			List<HousingTianshuiPayDetailed> list = new ArrayList<HousingTianshuiPayDetailed>();
			HousingTianshuiPayDetailed housingTianshuiPayDetailed = null;
			JSONObject jsonObj = JSONObject.fromObject(json);
			JSONObject lists = jsonObj.getJSONObject("lists");
			JSONObject dataList = lists.getJSONObject("dataList");
			String listjson = dataList.getString("list");
			Object obj = new JSONTokener(listjson).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 单位编号
				String corpcode = jsonObject.getString("corpcode");
				// 缴款单位名称
				String corpname = jsonObject.getString("corpname");
				// 交易日期
				String acctime = jsonObject.getString("acctime");
				// 代理机构
				String depname = jsonObject.getString("depname");
				// 摘要
				String remark = jsonObject.getString("remark");
				// 发生金额
				String depbal = jsonObject.getString("depbal");
				// 余额
				String accbal = jsonObject.getString("accbal");

				housingTianshuiPayDetailed = new HousingTianshuiPayDetailed(taskHousing.getTaskid(), corpcode, corpname, acctime, depname,
						remark, depbal, accbal);
				list.add(housingTianshuiPayDetailed);
			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 单位编号
					String corpcode = jsonObject.getString("corpcode");
					// 缴款单位名称
					String corpname = jsonObject.getString("corpname");
					// 交易日期
					String acctime = jsonObject.getString("acctime");
					// 代理机构
					String depname = jsonObject.getString("depname");
					// 摘要
					String remark = jsonObject.getString("remark");
					// 发生金额
					String depbal = jsonObject.getString("depbal");
					// 余额
					String accbal = jsonObject.getString("accbal");

					housingTianshuiPayDetailed = new HousingTianshuiPayDetailed(taskHousing.getTaskid(), corpcode, corpname, acctime, depname,
							remark, depbal, accbal);
					list.add(housingTianshuiPayDetailed);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingTianshuiParse.htmlPayRecordParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}
		return null;

	}
	
	/**
	 * 解析缴存记录查询
	 * @param json
	 * @param taskHousing
	 * @return
	 */
	public List<HousingTianshuiPayRecord> htmlPayRecordParser(String json, TaskHousing taskHousing) {
		try {
			List<HousingTianshuiPayRecord> list = new ArrayList<HousingTianshuiPayRecord>();
			HousingTianshuiPayRecord housingTianshuiPayRecord = null;
			JSONObject jsonObj = JSONObject.fromObject(json);
			JSONObject lists = jsonObj.getJSONObject("lists");
			JSONObject dataList = lists.getJSONObject("dataList");
			String listjson = dataList.getString("list");
			Object obj = new JSONTokener(listjson).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 单位编号
				String corpcode = jsonObject.getString("corpcode");
				// 单位名称
				String corpname = jsonObject.getString("corpname");
				// 业务类型
				String paybustype = jsonObject.getString("paybustype");
				// 缴存类型
				String deptype = jsonObject.getString("deptype");
				// 缴款月份起
				String starmnh = jsonObject.getString("starmnh");
				// 缴款月份止
				String endmnh = jsonObject.getString("endmnh");
				// 单位缴存额
				String corpdepmny = jsonObject.getString("corpdepmny");
				// 个人缴存额
				String perdepmny = jsonObject.getString("perdepmny");
				// 合计缴存额
				String depmny = jsonObject.getString("depmny");
				// 业务受理时间
				String dotime = jsonObject.getString("dotime");
				// 到账时间时间
				String suertime = "";

				housingTianshuiPayRecord = new HousingTianshuiPayRecord(taskHousing.getTaskid(), corpcode, corpname, paybustype, deptype,
						starmnh, endmnh, corpdepmny, perdepmny, depmny, dotime, suertime);
				list.add(housingTianshuiPayRecord);
			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 单位编号
					String corpcode = jsonObject.getString("corpcode");
					// 单位名称
					String corpname = jsonObject.getString("corpname");
					// 业务类型
					String paybustype = jsonObject.getString("paybustype");
					// 缴存类型
					String deptype = jsonObject.getString("deptype");
					// 缴款月份起
					String starmnh = jsonObject.getString("starmnh");
					// 缴款月份止
					String endmnh = jsonObject.getString("endmnh");
					// 单位缴存额
					String corpdepmny = jsonObject.getString("corpdepmny");
					// 个人缴存额
					String perdepmny = jsonObject.getString("perdepmny");
					// 合计缴存额
					String depmny = jsonObject.getString("depmny");
					// 业务受理时间
					String dotime = jsonObject.getString("dotime");
					// 到账时间时间
					String suertime = "";

					housingTianshuiPayRecord = new HousingTianshuiPayRecord(taskHousing.getTaskid(), corpcode, corpname, paybustype, deptype,
							starmnh, endmnh, corpdepmny, perdepmny, depmny, dotime, suertime);
					list.add(housingTianshuiPayRecord);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingTianshuiParse.htmlPayRecordParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}
		return null;

	}

	/**
	 * 解析个人缴存账户
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public List<HousingTianshuiAccountInfo> htmlAccountInfoParser(String json, TaskHousing taskHousing) {

		try {
			List<HousingTianshuiAccountInfo> list = new ArrayList<HousingTianshuiAccountInfo>();
			HousingTianshuiAccountInfo housingTianshuiAccountInfo = null;
			JSONObject jsonObj = JSONObject.fromObject(json);
			JSONObject lists = jsonObj.getJSONObject("lists");
			JSONObject dataList = lists.getJSONObject("dataList");
			String listjson = dataList.getString("list");
			Object obj = new JSONTokener(listjson).nextValue();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				// 单位编号
				String corpcode = jsonObject.getString("corpcode");
				// 单位名称
				String corpname = jsonObject.getString("corpname");
				// 缴存基数
				String bmny = jsonObject.getString("bmny");
				// 单位月缴额
				String corpdepmny = jsonObject.getString("corpdepmny");
				// 个人月缴额
				String perdepmny = jsonObject.getString("perdepmny");
				// 合计月缴额
				String depmny = jsonObject.getString("depmny");
				// 缴止月份
				String payendmnh = jsonObject.getString("payendmnh");
				// 缴存状态
				String depstate = jsonObject.getString("depstate");
				// 账户余额
				String accbal = jsonObject.getString("accbal");

				housingTianshuiAccountInfo = new HousingTianshuiAccountInfo(taskHousing.getTaskid(), corpcode, corpname, bmny, corpdepmny,
						perdepmny, depmny, payendmnh, depstate, accbal);
				list.add(housingTianshuiAccountInfo);
			} else if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;
				for (Object object : jsonArray) {
					JSONObject jsonObject = JSONObject.fromObject(object);
					// 单位编号
					String corpcode = jsonObject.getString("corpcode");
					// 单位名称
					String corpname = jsonObject.getString("corpname");
					// 缴存基数
					String bmny = jsonObject.getString("bmny");
					// 单位月缴额
					String corpdepmny = jsonObject.getString("corpdepmny");
					// 个人月缴额
					String perdepmny = jsonObject.getString("perdepmny");
					// 合计月缴额
					String depmny = jsonObject.getString("depmny");
					// 缴止月份
					String payendmnh = jsonObject.getString("payendmnh");
					// 缴存状态
					String depstate = jsonObject.getString("depstate");
					// 账户余额
					String accbal = jsonObject.getString("accbal");

					housingTianshuiAccountInfo = new HousingTianshuiAccountInfo(taskHousing.getTaskid(), corpcode, corpname, bmny, corpdepmny,
							perdepmny, depmny, payendmnh, depstate, accbal);
					list.add(housingTianshuiAccountInfo);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingChengduParse.htmlUserInfoParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return null;

	}
	
	
	/**
	 * 解析用户信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public HousingTianshuiUserInfo htmlUserInfoParser(String json, TaskHousing taskHousing) {

		try {
			Document doc = Jsoup.parse(json);
			// 个人编号
			String percode = doc.getElementById("percode").val();

			// 个人姓名
			String pername = doc.getElementById("pername_a").val();

			// 证件类型
			String codetype = "";
			if (json.contains("slectInput_codetype_a[0]")) {
				String slectInput_codetype_a = json.substring(json.indexOf("slectInput_codetype_a[0]"));
				String[] split = slectInput_codetype_a.split("\"");
				if (split.length > 1) {
					codetype = split[1];
				}
			}

			// 证件号码
			String codeno = doc.getElementById("codeno_a").val();

			// 性别
			String sex = "";
			if (json.contains("slectInput_sex_a[0]")) {
				String slectInput_sex_a = json.substring(json.indexOf("slectInput_sex_a[0]"));
				String[] split = slectInput_sex_a.split("\"");
				if (split.length > 1) {
					sex = split[1];
				}
			}

			// 出生日期
			String birthday = doc.getElementById("birthday_a").val();

			// 个人邮箱
			String email = doc.getElementById("email_a").val();

			// 移动电话
			String phone = doc.getElementById("phone_a").val();

			// 民族
			String nation = "";
			if (json.contains("slectInput_nation_a[0]")) {
				String slectInput_nation_a = json.substring(json.indexOf("slectInput_nation_a[0]"));
				String[] split = slectInput_nation_a.split("\"");
				if (split.length > 1) {
					nation = split[1];
				}
			}

			// 国籍
			String country = "";
			if (json.contains("slectInput_country_a[0]")) {
				String slectInput_country_a = json.substring(json.indexOf("slectInput_country_a[0]"));
				String[] split = slectInput_country_a.split("\"");
				if (split.length > 1) {
					country = split[1];
				}
			}

			// 文化程度
			String edulev = "";
			if (json.contains("slectInput_edulev_a[0]")) {
				String slectInput_edulev_a = json.substring(json.indexOf("slectInput_edulev_a[0]"));
				String[] split = slectInput_edulev_a.split("\"");
				if (split.length > 1) {
					edulev = split[1];
				}
			}

			// 婚姻状况
			String marstate = "";
			if (json.contains("slectInput_marstate_a[0]")) {
				String slectInput_marstate_a = json.substring(json.indexOf("slectInput_marstate_a[0]"));
				String[] split = slectInput_marstate_a.split("\"");
				if (split.length > 1) {
					marstate = split[1];
				}
			}

			// 邮政编码
			String postcode = doc.getElementById("postcode_a").val();

			HousingTianshuiUserInfo housingTianshuiUserInfo = new HousingTianshuiUserInfo(taskHousing.getTaskid(),
					percode, pername, codetype, codeno, sex, birthday, email, phone, nation, country, edulev, marstate,
					postcode);

			return housingTianshuiUserInfo;

		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("HousingChengduParse.htmlUserInfoParser---ERROR:",
					taskHousing.getTaskid() + "---ERROR:" + e.toString());
		}

		return null;

	}

}
