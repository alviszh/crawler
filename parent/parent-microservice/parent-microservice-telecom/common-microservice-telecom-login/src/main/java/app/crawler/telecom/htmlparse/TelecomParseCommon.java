package app.crawler.telecom.htmlparse;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonPointsAndCharges;
import com.microservice.dao.entity.crawler.telecom.common.TelecomStarlevel;

import app.bean.TelecomCommonPointsAndChargesRootBean;
import app.bean.WebParamTelecomByChrome;
import net.sf.json.JSONObject;

public class TelecomParseCommon {

	private static Gson gs = new Gson();

	public static String parserCityCodeForHeBei(String html) {
		Document doc = Jsoup.parse(html);
		String cityCode = doc.select("[name=AREA_CODE]").first().val();
		System.out.println("城市代码：" + cityCode);
		return cityCode;
	}

	public static TelecomCommonPointsAndCharges pointsAndCharges_parse(String html) {

		TelecomCommonPointsAndChargesRootBean jsonObject = gs.fromJson(html,
				TelecomCommonPointsAndChargesRootBean.class);
		return jsonObject.getObj();
	}

	/**
	 * 解析用户星级服务信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public static WebParamTelecomByChrome<TelecomStarlevel> starlevel_Parser(String html, TaskMobile taskMobile) {
		System.out.println(html);
		WebParamTelecomByChrome<TelecomStarlevel> webParam = new WebParamTelecomByChrome<>();
		List<TelecomStarlevel> list = new ArrayList<TelecomStarlevel>();
		try {
			TelecomStarlevel telecomStarlevel = null;

			JSONObject jsonObj = JSONObject.fromObject(html);

			JSONObject custInfo = jsonObj.getJSONObject("custInfo");

			if (null != custInfo && custInfo.containsKey("custName")) {
				String custName = custInfo.getString("custName");
				String membershipLevel = custInfo.getString("membershipLevel");
				String growthpoint = custInfo.getString("growthpoint");
				telecomStarlevel = new TelecomStarlevel(taskMobile.getTaskid(), custName, membershipLevel, growthpoint);
				list.add(telecomStarlevel);
				webParam.setList(list);
			}

		} catch (Exception e) {
			e.printStackTrace();
			webParam.setErrormessage(taskMobile.getTaskid() + "---ERROR:" + e.toString());
		}

		return webParam;

	}

	/**
	 * 获取错误信息
	 * 
	 * @param html
	 * @param taskInsurance
	 * @return
	 */
	public static WebParamTelecomByChrome<?> loginerror_Parser(String html, WebParamTelecomByChrome<?> webParam) {
		try {
			Document doc = Jsoup.parse(html);
			String errorMessage = doc.select("div#divErr").text();
			webParam.setErrormessage(errorMessage);
		} catch (Exception e) {
			e.printStackTrace();
			webParam.setErrormessage("---ERROR:" + e.toString());
		}

		return webParam;

	}

}
