package app.crawler.htmlparse;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaCebitCardUserInfoResult;

import app.bean.JsonRootBean;
import app.bean.auuount.AccountJsonRootBean;
import app.bean.creditcard.TransFlowResult;
import app.bean.creditcard.TransListResult;

/**   
*    
* 项目名称：common-microservice-bank-boc   
* 类名称：BocCreditParse   
* 类描述：   
* 创建人：hyx  
* 创建时间：2017年11月29日 下午4:13:54   
* @version        
*/
public class BocCreditParse {
	private static Gson gs = new Gson();

	public static JsonRootBean<BocchinaCebitCardUserInfoResult> account_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<JsonRootBean<BocchinaCebitCardUserInfoResult>>() {}.getType();
		JsonRootBean<BocchinaCebitCardUserInfoResult> jsonObject = gs.fromJson(html, type);

		System.out.println(jsonObject.toString());

		return jsonObject;
	}
	
	public static JsonRootBean<TransListResult> translist_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		
		System.out.println("====html="+html);
		Type type = new TypeToken<JsonRootBean<TransListResult>>() {}.getType();
		JsonRootBean<TransListResult> jsonObject = gs.fromJson(html, type);

		System.out.println("translist_parse="+jsonObject.toString());

		return jsonObject;
	}
	
	public static AccountJsonRootBean accountSeq_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<AccountJsonRootBean>() {}.getType();
		AccountJsonRootBean jsonObject = gs.fromJson(html, type);

		System.out.println("translist_parse="+jsonObject.toString());

		return jsonObject;
	}
	
	public static JsonRootBean<TransFlowResult> transflow_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<JsonRootBean<TransFlowResult>>() {}.getType();
		JsonRootBean<TransFlowResult> jsonObject = gs.fromJson(html, type);

		System.out.println(jsonObject.toString());

		return jsonObject;
	}
}
