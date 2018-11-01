package app.crawler.htmlparse;

import java.util.List;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.housing.changchun.HousingChangChunPay;
import com.microservice.dao.entity.crawler.housing.changchun.HousingChangChunUserInfo;

public class HousingChangChunParse {

	private static   Gson gs = new Gson();
	
	public static HousingChangChunUserInfo userinfo_parse(String html) {

		System.out.println("userinfo_parse======="+html);
		
		return  gs.fromJson(html, new TypeToken<HousingChangChunUserInfo>(){}.getType());
	}

	public static List<HousingChangChunPay> pay_parse(String html) {
		System.out.println("pay_parse======="+html);

		return  gs.fromJson(html, new TypeToken<List<HousingChangChunPay>>(){}.getType());
	}

}
