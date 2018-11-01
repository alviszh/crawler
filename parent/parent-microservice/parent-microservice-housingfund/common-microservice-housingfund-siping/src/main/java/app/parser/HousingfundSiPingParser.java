package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.siping.HousingSiPingPay;
import com.microservice.dao.entity.crawler.housing.siping.HousingSiPingUserinfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingfundSiPingParser {

	public List<HousingSiPingPay> getcrawler(String html, String taskid) {
		List<HousingSiPingPay> list = null;
		try {
			list = new ArrayList<HousingSiPingPay>();
			JSONObject obj = JSONObject.fromObject(html);
			String string = obj.getString("results");
			JSONArray array = JSONArray.fromObject(string);
			for(int i =0;i<array.size();i++){
				String ywlsh = array.getJSONObject(i).getString("ywlsh");//流水号
				String rq = array.getJSONObject(i).getString("rq");//日期
				String jfje = array.getJSONObject(i).getString("jfje");//提取金额
				String dfje = array.getJSONObject(i).getString("dfje");//存入金额
				String ye = array.getJSONObject(i).getString("ye");//余额
				String zy = array.getJSONObject(i).getString("zy");//摘要
				HousingSiPingPay housingSiPingPay = new HousingSiPingPay(
						taskid,ywlsh,rq,jfje,dfje,ye,zy);
				list.add(housingSiPingPay);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return list;
		}
		return list;
	}

	public HousingSiPingUserinfo getuserinfo(String html) {
		HousingSiPingUserinfo user = null;
		try {
			JSONObject object = JSONObject.fromObject(html);
			String string = object.getString("results");
			JSONArray object2 = JSONArray.fromObject(string);
			JSONObject object3 = object2.getJSONObject(0);
			String string2 = object3.getString("a003");//单位帐号
			String string3 = object3.getString("a004");//单位名称
			String string4 = object3.getString("a001");//职工帐号
			String string5 = object3.getString("a002");//职工姓名
			String string6 = object3.getString("a021");//证件类型
			String string7 = object3.getString("a008");//证件号码
			String string8 = object3.getString("yddh");//移动电话
			String string9 = object3.getString("jtzz");//家庭住址
			String string10 = object3.getString("a079");//银行帐号
			String string11 = object3.getString("a083");//开户银行
			user = new HousingSiPingUserinfo(
					null,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11);
		} catch (Exception e) {
			System.out.println(e.toString());
			return user;
		}
		return user;
	}

}
