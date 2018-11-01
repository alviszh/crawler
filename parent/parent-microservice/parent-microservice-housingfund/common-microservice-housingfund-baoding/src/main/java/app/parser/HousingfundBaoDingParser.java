package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.baoding.HousingBaoDingPay;
import com.microservice.dao.entity.crawler.housing.baoding.HousingBaoDingUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingfundBaoDingParser {

	public HousingBaoDingUserInfo getuserinfo(String words_result) {
		HousingBaoDingUserInfo housingBaoDingUserInfo =null;
		try {
			String dwnum = null;
			String[] split = words_result.split("单位账号:");
			String[] strings = split[1].split("},");
			dwnum = strings[0].substring(0, strings[0].length()-1);
			
			String dwname = null;
			String[] split2 = words_result.split("单位名称:");
			String[] strings2 = split2[1].split("},");
			dwname = strings2[0].substring(0, strings2[0].length()-1);
			
			String zgzh = null;
			String[] split3 = words_result.split("职工账号:");
			String[] strings3 = split3[1].split("},");
			zgzh = strings3[0].substring(0, strings3[0].length()-1);
			
			String zgname = null;
			String[] split4 = words_result.split("职工姓名:");
			String[] strings4 = split4[1].split("},");
			zgname = strings4[0].substring(0, strings4[0].length()-1);
			
			String zhzt = null;
			String[] split5 = words_result.split("账户状态:");
			String[] strings5 = split5[1].split("},");
			zhzt = strings5[0].substring(0, strings5[0].length()-1);
			
			String idcard = null;
			String[] split6 = words_result.split("证件号码:");
			String[] strings6 = split6[1].split("},");
			idcard = strings6[0].substring(0, strings6[0].length()-1);
			
			String date = null;
			String[] split7 = words_result.split("开户日期:");
			String[] strings7 = split7[1].split("},");
			date = strings7[0].substring(0, strings7[0].length()-1);
			
			housingBaoDingUserInfo = new HousingBaoDingUserInfo(null,dwnum,dwname,zgzh,zgname,zhzt,idcard,date);
			return housingBaoDingUserInfo;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return housingBaoDingUserInfo;
		}
	}

	public List<HousingBaoDingPay> getpay(String ocr, MessageLoginForHousing messageLogin) {
		List<HousingBaoDingPay> list = null;
		try {
			JSONObject fromObject = JSONObject.fromObject(ocr);
			String string = fromObject.getString("results");
			JSONArray object = JSONArray.fromObject(string);
			list = new ArrayList<HousingBaoDingPay>();
			for (int i = 0; i < object.size(); i++) {
				JSONObject obj = object.getJSONObject(i);
				String string2 = obj.getString("ywlsh");//流水号
				String string3 = obj.getString("rq");//日期
				String string4 = obj.getString("jfje");
				String string5 = obj.getString("dfje");
				String string6 = obj.getString("ye");//余额
				String string7 = obj.getString("zy");//摘要
				HousingBaoDingPay housingBaoDingPay = new HousingBaoDingPay(
						messageLogin.getTask_id(), string2, string3, string4, string5, string6, string7);
				list.add(housingBaoDingPay);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return list;
		}
		return list;
	}

}
