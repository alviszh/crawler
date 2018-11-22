package app.crawler.htmlparse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.housing.yvlin.HousingBasicPayResult;
import com.microservice.dao.entity.crawler.housing.yvlin.HousingBasicUserData;

import app.bean.BasicPayData;
import app.bean.BasicPayRootBean;
import app.bean.BasicUserJsonRootBean;

public class HousingYvLinParse {

	private static Gson gs = new Gson();

	public static Map<String, String> UserBasicParameterParse(String html) throws Exception {

		String reg = "数据总线varpoolSelect=\\{(.*?)\\};//页面组件初始化";

		html = patSub(html, reg).replaceAll("\"", "").replaceAll("'", "");

		String[] canshus = html.split(",");
		Map<String,String> paramsListMap = new HashMap<>();
		for (int i = 0; i < canshus.length; i++) {

			String key = canshus[i].split(":")[0];
			String value = canshus[i].split(":")[1];
			System.out.println("key = " + key + "          value = " + value);

			paramsListMap.put(key.trim(), value.trim());
		}

		return paramsListMap;
	}
	
	public static Map<String, String> UserPayParameterParse(String html) throws Exception {

			//File input = new File("C:\\Users\\Administrator\\Desktop\\bocom.html");
			Document doc = Jsoup.parse(html, "UTF-8");

			//System.out.println("UserPayParameterParse=======" + doc);

			Elements eles = doc.select("textarea");
			Map<String,String> paramsListMap = new HashMap<>();
			for(Element ele : eles){
				
				paramsListMap.put(ele.attr("name"), ele.text());
			}
			
		//	System.out.println(paramsListMap.toString());

			return paramsListMap;
		}

	public static HousingBasicUserData basicusereParse(String html, String taskid) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<BasicUserJsonRootBean>() {
		}.getType();
		BasicUserJsonRootBean jsonObject = gs.fromJson(html, type);
		HousingBasicUserData housingBasicUserData = jsonObject.getData();
		housingBasicUserData.setTaskid(taskid);

		return housingBasicUserData;
	}

	public static List<HousingBasicPayResult> basicpayParse(String html, String taskid) throws Exception {
		// JsonRootBean<Userinfo> resultroot = new JsonRootBean<Userinfo>();
		Type type = new TypeToken<BasicPayRootBean>() {
		}.getType();
		BasicPayRootBean jsonObject = gs.fromJson(html, type);
		BasicPayData basicPayData = jsonObject.getData();
		List<HousingBasicPayResult> data = basicPayData.getData();
		
		List<HousingBasicPayResult> list = new ArrayList<>();
		
		for(HousingBasicPayResult result : data){
			result.setTaskid(taskid);
			list.add(result);
		}

		return list;
	}
	
	public static String patSub(String eleScrietString, String reg) {

		Pattern pattern2 = Pattern.compile(reg);

		Matcher matcher2 = pattern2.matcher(eleScrietString.replaceAll("\\s", ""));// 使用正则表达式判断日期
		String html2 = null;
		while (matcher2.find()) {
			html2 = matcher2.group(1);
		}
		// System.out.println("html2===" + html2);

		return html2;
	}

	public static void main(String[] args) {
		try {
			UserPayParameterParse(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
