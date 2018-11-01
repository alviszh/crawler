package app.htmlparse;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.bean.PayRootBean;
import app.bean.UserInfoRootBean;



public class HousingFuYangParse {

	private static Gson gs = new Gson();

	public static UserInfoRootBean basicusereParse(String html, String taskid) throws Exception {
		Type type = new TypeToken<UserInfoRootBean>() {
		}.getType();
		UserInfoRootBean jsonObject = gs.fromJson(html, type);

		return jsonObject;
	}
	
	public static PayRootBean payParse(String html, String taskid) throws Exception {
		Type type = new TypeToken<PayRootBean>() {
		}.getType();
		PayRootBean jsonObject = gs.fromJson(html, type);

		return jsonObject;
	}

}
