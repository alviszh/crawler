package app.test;

import java.net.URL;
import java.util.Iterator;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class IteratorTest {
	public static void main(String[] args) throws Exception {
		String url="http://676AF07DFFA0C10A51C9:uoGBrQfv4RMCXq5XzMWG9kfXosTmheEWgTHgSUSR@10.167.211.158:8080/v2-beta/projects/1a5/hosts";
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		webClient.getOptions().setJavaScriptEnabled(false);
		Page page = webClient.getPage(webRequest); 
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			if(html.contains("state")){ //可以获取网络连接状态
				//响应回来的页面会有js代码干扰，故如下方式截取
				String jsonString = html.substring(html.indexOf("\"type")-1, html.lastIndexOf("}")+1);
				JSONArray jsonArray = JSONObject.fromObject(jsonString).getJSONArray("data");
				for(int i=0;i<jsonArray.size();i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("diskInfo").getJSONObject("mountPoints");
					@SuppressWarnings("unchecked")
					Iterator<String> it = jsonObject.keys();
					while(it.hasNext()){
						// 获得key
						String key = it.next(); 
						String value = jsonObject.getString(key);    
						System.out.println("key: "+key+",value:"+value);
						}
					System.out.println("==============================");
				}
			}
		}
	}
}
