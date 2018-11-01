package app.htmlparse;

import com.google.gson.Gson;

import app.bean.BasicJsonRootBean;
import app.bean.LoginRootBean;
import app.bean.TranJsonRootBean;

/**
 * 
 * 项目名称：common-microservice-housingfund-daqing 类名称：HousingDQParse 类描述： 创建人：hyx
 * 创建时间：2017年11月7日 下午2:05:24
 * 
 * @version
 */
public class HousingCZParse {

	private static Gson gs = new Gson();

	public static BasicJsonRootBean basic_parse(String html) {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		BasicJsonRootBean jsonObject = gs.fromJson(html, BasicJsonRootBean.class);

		return jsonObject;
	}
	
	public static TranJsonRootBean tran_parse(String html) {
		TranJsonRootBean jsonObject = gs.fromJson(html, TranJsonRootBean.class);


		return jsonObject;
	}
	
	public static LoginRootBean login_parse(String html) {
		LoginRootBean jsonObject = gs.fromJson(html, LoginRootBean.class);

		return jsonObject;
	}
}
