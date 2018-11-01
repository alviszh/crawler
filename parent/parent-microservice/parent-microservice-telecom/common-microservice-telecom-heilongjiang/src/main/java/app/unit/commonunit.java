package app.unit;

import java.util.Set;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gson.Gson;


public class commonunit {

	Gson gson=new Gson();
	public String setcookieTojson(Set<Cookie> setcookie){
		return gson.toJson(setcookie);
	}
}
