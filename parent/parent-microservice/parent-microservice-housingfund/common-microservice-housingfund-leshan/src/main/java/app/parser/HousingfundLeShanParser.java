package app.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.housing.leshan.HousingLeShanPay;
import com.microservice.dao.entity.crawler.housing.leshan.HousingLeShanUserInfo;

import app.commontracerlog.TracerLog;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingfundLeShanParser {

	@Autowired
	private TracerLog tracer;

	public HousingLeShanUserInfo getuserinfo(String html, String task_id) {
		HousingLeShanUserInfo user = null;
		try {
			Document document = Jsoup.parse(html);
			String text = document.getElementsByClass("glname").get(0).text();//姓名
			String text2 = document.getElementsByClass("glitem").get(0).text();//身份证号
			String text3 = document.getElementsByClass("glitem").get(1).text();//个人帐号
			String text4 = document.getElementsByClass("glitem").get(2).text();//手机号码
			String text5 = document.getElementById("corpcode2").text();//单位名称
			String text6 = document.getElementById("corpcode").text();//单位账号
			String text7 = document.getElementById("depname").text();//缴存管理部
			String text8 = document.getElementById("bkname").text();//缴存银行
			String text9 = document.getElementById("bmny").text();//缴存基数
			String text10 = document.getElementById("perperscale").text();//个人缴存比例
			String text11 = document.getElementById("percorpscale").text();//单位缴存比例
			
			String text12 = document.getElementById("bkname").text();//工资基数
			
			String text13 = document.getElementById("perdepmny").text();//个人月汇缴额
			String text14 = document.getElementById("corpdepmny").text();//单位月汇缴额
			String text15 = document.getElementById("depmny").text();//总计月汇缴额
			
			String text16 = document.getElementById("accbal").text();//缴存余额
			String text17 = document.getElementById("accstate").text();//账户状态
			String text18 = document.getElementById("regtime").text();//开户日期
			String text19 = document.getElementById("payendmnh").text();//缴至年月
			String text20 = document.getElementById("bkcardname").text();//绑定银行
			String text21 = document.getElementById("bkcard").text();//绑定银行卡号
			
			user = new HousingLeShanUserInfo(task_id,text,text2,text3,text4,text5,text6,text7,text8,text9,text10
					,text11,text12,text13,text14,text15,text16,text17,text18,text19,text20,text21);
		} catch (Exception e) {
			// TODO: handle exception
			tracer.addTag("HousingLeShanUserInfo.parser.getuserinfo", task_id);
			return user;
		}
		
		return user;
	}

	public List<HousingLeShanPay> getpaymsg(String task_id, String contentAsString) {
		List<HousingLeShanPay> list = null;
		try {
			list = new ArrayList<HousingLeShanPay>();
			JSONArray jsonArray = JSONObject.fromObject(contentAsString).getJSONObject("lists").getJSONObject("dataList").getJSONArray("list");
			for (int i = 0; i < jsonArray.size(); i++) {
				String string = jsonArray.getJSONObject(i).getString("accmnh");//存缴年月
				String string2 = jsonArray.getJSONObject(i).getString("acctime");//入账时间
				String string3 = jsonArray.getJSONObject(i).getString("corpname");//公司名称
				String string4 = jsonArray.getJSONObject(i).getString("income");//缴存
				String string5 = jsonArray.getJSONObject(i).getString("outcome");//提取
				String string6 = jsonArray.getJSONObject(i).getString("accbal");//当前余额
				String string7 = jsonArray.getJSONObject(i).getString("remark");//业务类型
				String string8 = jsonArray.getJSONObject(i).getString("percode");//个人帐号
				String string9 = jsonArray.getJSONObject(i).getString("pername");//姓名
				String string10 = jsonArray.getJSONObject(i).getString("corpcode");//公司帐号
				HousingLeShanPay pay = new HousingLeShanPay(task_id,string,string2,string3,string4,string5,string6,string7,string8,string9,string10);
				list.add(pay);
			}
			
		} catch (Exception e) {
			tracer.addTag("HousingLeShanUserInfo.parser.getpay", task_id);
			return list;
		}
		return list;
	}
}
