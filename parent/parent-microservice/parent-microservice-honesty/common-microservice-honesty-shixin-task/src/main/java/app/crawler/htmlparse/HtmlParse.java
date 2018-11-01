package app.crawler.htmlparse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.honesty.shixin.ShiXinBean;

import app.bean.WebParamHonesty;

public class HtmlParse {
	private static Gson gs = new Gson();

	public static ShiXinBean shixin_parse(String html) {
		Type type = new TypeToken<ShiXinBean>() {
		}.getType();

		ShiXinBean jsonObject = gs.fromJson(html, type);
		return jsonObject;
	}

	public static String getcaptchaId(String html) {
		Document doc = Jsoup.parse(html);

		String captchaId = doc.select("input#captchaId").attr("value");

		return captchaId;
	}

	public static WebParamHonesty<Integer> getIDList(String html) {

		WebParamHonesty<Integer> webParam = new WebParamHonesty<>();
		List<Integer> list = new ArrayList<Integer>();
		Document doc = Jsoup.parse(html);
		Elements es = doc.getElementsByClass("View");
		if (es.size() > 0) {
			for (Element e : es) {
				try{
					Integer id =Integer.parseInt(e.id()) ;
					
					list.add(id);
				}catch(Exception e1){
					e1.printStackTrace();
				}
				
			}

			webParam.setList(list);
		} else {
			System.out.println("无失信记录");
		}
		

	    String pagnum =getnum(doc.select("script").last().toString());
	    
	    webParam.setPagnum(pagnum);

		return webParam;
	}
	
	public static String getnum(String txt) {
             
		 Pattern pattern = Pattern.compile("[^0-9]");
	        Matcher matcher = pattern.matcher(txt);
	        String all = matcher.replaceAll("");
	        System.out.println("phone:" + all);
	        
        return  all;
    }

	public static void main(String[] args) {

		File input = new File("C:/Users/Administrator/Desktop/aaa.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8");
			
			//System.out.println(doc);
			getIDList(doc.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}