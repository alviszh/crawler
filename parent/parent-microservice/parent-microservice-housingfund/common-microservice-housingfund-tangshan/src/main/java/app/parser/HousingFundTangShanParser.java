package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.tangshan.HousingTangShanPay;
import com.microservice.dao.entity.crawler.housing.tangshan.HousingTangShanUserInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundTangShanParser {

	public HousingTangShanUserInfo getuserinfo(String html) {
		
		HousingTangShanUserInfo h = null;
		try{
			if(html!=null){
				JSONObject object2 = JSONObject.fromObject(html);
				String accname = object2.getString("xingming");//姓名
				String certinum = object2.getString("zjhm");//身份证
				String accnum = object2.getString("grzh");//个人帐号
				String unitaccname = object2.getString("dwmc");//单位名称
				String grjcjs = object2.getString("grjcjs");//个人缴存基数
				String monpaysum = object2.getString("yjce");//月缴存额
				String unitprop = object2.getString("unitprop");//单位公积金比例 
				String indiprop = object2.getString("indiprop");//个人公积金比例 
				String dwyjce = object2.getString("dwyjce");//单位月缴存额
				String gryjce = object2.getString("gryjce");//个人月缴存额
				String balance = object2.getString("grzhye");//余额
				String lastpaydate = object2.getString("jzny");//最后汇缴月
				String opendate = object2.getString("khrq");//开户日期
				String grzhzt = object2.getString("grzhzt");//个人账户状态
				if(grzhzt.equals("01")){
					grzhzt = "正常";
				}else{
					grzhzt = "封存";
				}
				h = new HousingTangShanUserInfo(null,null,accname,certinum,accnum,unitaccname,grjcjs,monpaysum,
						unitprop,indiprop,dwyjce,gryjce,balance,lastpaydate,opendate,grzhzt);
				
			}
			return h;
		}catch (Exception e) {
			// TODO: handle exception
			return h;
		}

	}

	public List<HousingTangShanPay> getpay(String html, String taskid) {
		List<HousingTangShanPay> list = null;
		try {
			list = new ArrayList<HousingTangShanPay>();
			JSONArray array = JSONObject.fromObject(html).getJSONArray("results");
			for (int i = 1; i < array.size(); i++) {
				String string = array.getJSONObject(i).getString("grzh");
				String string2 = array.getJSONObject(i).getString("jzrq");
				String string3 = array.getJSONObject(i).getString("remark");
				String string4 = array.getJSONObject(i).getString("ywmxlx");
				String string5 = array.getJSONObject(i).getString("fse");
				String string6 = array.getJSONObject(i).getString("bal");
				String string7 = array.getJSONObject(i).getString("transtype");
				HousingTangShanPay pay = new HousingTangShanPay(taskid,string,string2,string3,string4,string5,string6,string7);
				list.add(pay);
			}
		} catch (Exception e) {
			// TODO: handle exception
			return list;
		}
		
		return list;
	}

}