package app.crawler.htmlparse;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.insurance.changzhou.InsuranceChangZhouBasicUser;
import com.microservice.dao.entity.crawler.insurance.changzhou.InsuranceChangZhouPay;

import app.bean.InsuranceBasicSuQianChangZhouLianYunGangBean;
import app.bean.InsuranceChangZhouPayJsonRootBean;


public class InsuranceChangZhouParse {

	private static Gson gs = new Gson();

	public static InsuranceBasicSuQianChangZhouLianYunGangBean UserNeedParse(String html) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<InsuranceBasicSuQianChangZhouLianYunGangBean>() {
		}.getType();
		InsuranceBasicSuQianChangZhouLianYunGangBean jsonObject = gs.fromJson(html, type);
		System.out.println(jsonObject.toString());
		return jsonObject;
	}
	
	public static InsuranceChangZhouBasicUser UserParse(String html) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<InsuranceChangZhouBasicUser>() {
		}.getType();
		InsuranceChangZhouBasicUser jsonObject = gs.fromJson(html, type);
		
		return jsonObject;
	}
	
	public static List<InsuranceChangZhouPay> PayParse(String html) throws Exception {
		
		Type type = new TypeToken<InsuranceChangZhouPayJsonRootBean>() {
		}.getType();
		InsuranceChangZhouPayJsonRootBean jsonObject = gs.fromJson(html, type);
		
		return jsonObject.getPersonPaymentInfoDetailDTO();
	}

}
