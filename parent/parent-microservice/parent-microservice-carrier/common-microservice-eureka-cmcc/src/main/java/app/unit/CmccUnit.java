package app.unit;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

public class CmccUnit {
	
	private static Gson gson = new Gson();
	
	public static Set<Cookie> transferJsonToSet(TaskMobile taskMobile){
		String cookieJson = taskMobile.getCookies();
		Set<Cookie> set = new HashSet<Cookie>();
		Map<String, String> cookies = gson.fromJson(cookieJson, new TypeToken<Map<String, String>>() {}.getType());
		for (String key : cookies.keySet()) {  
			  Cookie cookie = new Cookie("shop.10086.cn",key, cookies.get(key));
			  set.add(cookie);
	    }
		return set;
		
	}
	
	
	public static void main(String[] args) {
		String aa = "remark你好房顶上来看解放路设计费极乐世界的路口附近是否是看得见啊发remark阿斯兰的客户反馈三凯峰数据发来撒即可加福禄寿remark";
		int count = StringUtils.countMatches(aa,"remark");
		System.out.println(count);
	}

}
