package app.test;

import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 *先获取所有存在的环境，再分别监控
 */
public class RancherAPITest {
	public static void main(String[] args) throws Exception {
		String baseUrl="http://676AF07DFFA0C10A51C9:uoGBrQfv4RMCXq5XzMWG9kfXosTmheEWgTHgSUSR@10.167.211.158:8080/v2-beta/projects";
		WebRequest webRequest = new WebRequest(new URL(baseUrl), HttpMethod.GET);
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		webClient.getOptions().setJavaScriptEnabled(false);
		Page page = webClient.getPage(webRequest); 
		if(null!=page){
			String html=page.getWebResponse().getContentAsString();
			//响应回来的页面会有js代码干扰，故如下方式截取
			String jsonString = html.substring(html.indexOf("\"type")-1, html.lastIndexOf("}")+1);
			//获取应有的环境
			JSONArray jsonArray = JSONObject.fromObject(jsonString).getJSONArray("data");
			int size = jsonArray.size();
			//遍历获取所有需要监控的环境
			for(int i=0;i<size;i++){
				String id = jsonArray.getJSONObject(i).getString("id");
				String name = jsonArray.getJSONObject(i).getString("name");
				System.out.println(id+"==============="+name);
			}
		}
	}
}
