package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.hulunbeier.HousingHuLunBeiErPay;
import com.microservice.dao.entity.crawler.housing.hulunbeier.HousingHuLunBeiErUserInfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Component
public class HousingfundHuLunBeiErParser {
	@Autowired
	private TracerLog tracer;
	
	public HousingHuLunBeiErUserInfo getuserinfo(String html) {
		HousingHuLunBeiErUserInfo user = null;
		try {
			JSONObject object = JSONObject.fromObject(html);
			String string = object.getString("lasttransdate");//最后交易日期
			String string2 = object.getString("certinum");//身份证号
			String string3 = object.getString("opnaccdate");//开户日期
			String string4 = object.getString("unitpayamt");//单位缴存额
			String string5 = object.getString("accnum");//个人公积金帐号
			String string6 = object.getString("accname1");//账户状态
			String string7 = object.getString("unitprop");//单位存缴比例
			String string8 = object.getString("unitaccname");//单位名称
			String string9 = object.getString("unitaccnum");//单位帐号
			String string10 = object.getString("indipayamt");//个人存缴额
			String string11 = object.getString("accname");//姓名
			String string12 = object.getString("indiprop");//个人存缴比例
			String string13 = object.getString("indipaysum");//月缴存额
			String string14 = object.getString("bal");//缴存余额
			
			user = new HousingHuLunBeiErUserInfo(
					null,string,string2,string3,string4,string5,string6,string7,string8,string9,string10,string11,string12,string13,string14);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			tracer.addTag("getuser", "个人信息解析错误"+e.getMessage());
			return user;
		}
		return user;
	}

	public List<HousingHuLunBeiErPay> getpay(String html, String taskid) {
		List<HousingHuLunBeiErPay> list = null;
		try {
			list = new ArrayList<HousingHuLunBeiErPay>();
			JSONArray object = JSONArray.fromObject(html);
			for(int i=0;i<object.size();i++){
				JSONObject obj = object.getJSONObject(i);
				String string = obj.getString("accnum");//个人帐号
				String string2 = obj.getString("amt");//发生额
				String string3 = obj.getString("ywtype");//业务类型
				String string4 = obj.getString("unitaccname");//公司名称
				String string5 = obj.getString("trandate");//日期
				String string6 = obj.getString("unitaccnum");//单位帐号
				String string7 = obj.getString("accname");//姓名
				String string8 = obj.getString("bal");//余额
				HousingHuLunBeiErPay pay = new HousingHuLunBeiErPay(
						taskid,string,string2,string3,string4,string5,string6,string7,string8);
				list.add(pay);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			tracer.addTag("getpay", "流水信息解析错误"+e.getMessage());
			return list;
		}
		return list;
	}

}
