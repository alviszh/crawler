/**
 * 
 */
package app.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author sln
 * @date 2018年10月17日下午3:57:43
 * @Description: 
 */
public class Test {
	public static void main(String[] args) throws Exception {
		List<String> list=new ArrayList<>();
		list.add("1a1007");
		list.add("1a43");
		list.add("1a5");
		list.add("1a6887");
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
					JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONObject("info").getJSONObject("cpuInfo").getJSONArray("loadAvg");
					int size = jsonArray2.size();
					for(int j=0;j<size;j++){
						if(Double.parseDouble((String) jsonArray2.get(j))>1){   //用1做测试
							System.out.println("超标的数据是："+jsonArray2.get(j));
							break;
						}
						System.out.println(jsonArray2.get(j));
					}
					System.out.println("==============================");
				}
			}
		}
	}
}
