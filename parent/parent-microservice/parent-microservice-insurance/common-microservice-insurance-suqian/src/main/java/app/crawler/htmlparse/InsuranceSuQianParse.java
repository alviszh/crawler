package app.crawler.htmlparse;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.insurance.suqian.InsuranceUser;

import app.bean.InsuranceBasicSuQianChangZhouLianYunGangBean;
import app.bean.InsurancePaySuQianJsonRootBean;


public class InsuranceSuQianParse {

	private static Gson gs = new Gson();

	public static InsuranceBasicSuQianChangZhouLianYunGangBean UserNeedParse(String html) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<InsuranceBasicSuQianChangZhouLianYunGangBean>() {
		}.getType();
		InsuranceBasicSuQianChangZhouLianYunGangBean jsonObject = gs.fromJson(html, type);
		
		return jsonObject;
	}
	
	public static InsuranceUser UserParse(String html) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<InsuranceUser>() {
		}.getType();
		InsuranceUser jsonObject = gs.fromJson(html, type);
		
		return jsonObject;
	}
	
	public static InsurancePaySuQianJsonRootBean PayParse(String html) throws Exception {
		
		Type type = new TypeToken<InsurancePaySuQianJsonRootBean>() {
		}.getType();
		InsurancePaySuQianJsonRootBean jsonObject = gs.fromJson(html, type);
		
		return jsonObject;
	}

}
