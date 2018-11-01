package app.crawler.htmlparse;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfSingleLimit;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfo;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardUserinfoOpendate;

import app.bean.JsonRootBean;
import app.bean.ResultForTranFlow;

/**   
*    
* 项目名称：common-microservice-bank-boc   
* 类名称：BocParse2   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月1日 上午11:17:01   
* @version        
*/

public class BocParse2 {

	private static Gson gs = new Gson();

	public static JsonRootBean<BocchinaDebitCardUserinfo> userinfo_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<JsonRootBean<BocchinaDebitCardUserinfo>>() {}.getType();
		JsonRootBean<BocchinaDebitCardUserinfo> jsonObject = gs.fromJson(html, type);

		System.out.println(jsonObject.toString());

		return jsonObject;
	}

	public static JsonRootBean<BocchinaDebitCardUserinfoOpendate> userInfoOpendate_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<JsonRootBean<BocchinaDebitCardUserinfoOpendate>>() {}.getType();
		JsonRootBean<BocchinaDebitCardUserinfoOpendate> jsonObject = gs.fromJson(html, type);

		System.out.println(jsonObject.toString());

		return jsonObject;
	}

	public static JsonRootBean<BocchinaDebitCardUserinfSingleLimit> userinfoSingleLimit_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<JsonRootBean<BocchinaDebitCardUserinfSingleLimit>>() {}.getType();
		JsonRootBean<BocchinaDebitCardUserinfSingleLimit> jsonObject = gs.fromJson(html, type);
		System.out.println(jsonObject.toString());

		return jsonObject;
	}

	public static JsonRootBean<ResultForTranFlow> transFlow_parse(String html) {

		Type type = new TypeToken<JsonRootBean<ResultForTranFlow>>() {}.getType();
		JsonRootBean<ResultForTranFlow> jsonObject = gs.fromJson(html, type);
		System.out.println(jsonObject.toString());

		return jsonObject;
	}

}
