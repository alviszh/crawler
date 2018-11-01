package app.service.unit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.search.NewsContent;
import com.microservice.dao.entity.crawler.search.NewsListJson;
import com.microservice.dao.entity.crawler.search.SearchTask;

import app.commontracerlog.TracerLog;
import app.service.SearchUnitService;

/**
 * 
 * 项目名称：common-microservice-search 类名称：Unit 类描述： 创建人：hyx 创建时间：2018年2月7日
 * 下午4:43:43
 * 
 * @version
 */
@Component
public class SanWangUnitService {

	@Autowired
	private TracerLog tracerLog;
	
	@Autowired
	private SearchUnitService searchUnitService;

	public String getSouGouurl(String urlsougou, SearchTask searchTask) {
		
		try {
		
			Document doc = getDoc(urlsougou, searchTask);
			String url = doc.select(("meta[http-equiv]")).attr("content").split("URL=")[1].replaceAll("'", "");

			return url;

		} catch (Exception e) {
			
			try {
				Document doc = getDoc(urlsougou, searchTask);
				tracerLog.System(urlsougou+":::"+"搜狗获取原链接", doc.select(("meta[http-equiv]")).toString());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
				tracerLog.System(urlsougou+":::"+"搜狗获取原链接", "网络连接失败");
			}
			

			return urlsougou;
		}

	}

	// 转换为%E4%BD%A0形式
	public String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = String.valueOf(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return sb.toString();
	}


	public Document getDoc(String url, SearchTask searchTask) {


		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(searchTask.getIpaddress(), Integer.parseInt(searchTask.getIpport())));
	
		Document doc = null;
		try {
			doc = Jsoup.connect(url).proxy(proxy)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0")
					.header("Accept", "application/json, text/javascript, */*; q=0.01")
					.header("Accept-Encoding", "gzip, deflate").header("Accept-Language", "zh-CN,zh;q=0.9")
					.header("Connection", "keep-alive")
					.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8").followRedirects(true)
					.timeout(10000).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			tracerLog.System("异常"+url, ""+e.getMessage());
		}

		return doc;
	}

	/**   
	  *    
	  * 项目名称：common-microservice-honesty-search  
	  * 所属包名：app.search.unit
	  * 类描述：   获取详情页数据，不归为解析类
	  * 创建人：hyx 
	  * 创建时间：2018年10月18日 
	  * @version 1  
	  * 返回值    Future<NewsContent>
	  */
	@Async("ExecutorForSearch") //配置类中的方法名
	public Future<NewsContent> getContent(NewsListJson newsListJson, SearchTask searchTask) throws UnsupportedEncodingException {
		NewsContent newsContent = new NewsContent();
		tracerLog.System("抓取url:", newsListJson.getLinkUrl());

		if (searchTask.getType().indexOf("sougou") != -1||searchTask.getType().indexOf("haosou") != -1) {
			String url = getSouGouurl(newsListJson.getLinkUrl(), searchTask);
			if (url != null) {
				newsListJson.setLinkUrl(url);
			}

		}
		 
			long startTime_get = System.currentTimeMillis();

			Document doc = getDoc(newsListJson.getLinkUrl(), searchTask);
			if(doc==null){
				tracerLog.System("doc null:"+newsListJson.getLinkUrl(), newsListJson.getLinkUrl());
				return new AsyncResult<NewsContent>(newsContent);
			}else{
				long endTime_get = System.currentTimeMillis();
				tracerLog.System(newsListJson.getLinkUrl() +"  "+"详情页获取 耗时 :", (endTime_get - startTime_get) + "ms");
				tracerLog.System(newsListJson.getLinkUrl() +"  "+"详情页获取 耗时        当前共计程序耗时s：" , (endTime_get - startTime_get) / 1000 + "s");

				String urlbasic = new String(
						toUtf8String(doc.baseUri()
								).getBytes("gbk"), "UTF8");

				newsListJson.setLinkUrl(urlbasic);

				newsContent.setContent(doc.text());
				try{
					newsContent.setSensitivekey(searchUnitService.matchSensitive(doc.text()).toString().replaceAll("\\[", "").replaceAll("\\]", ""));

				}catch(Exception e){
					e.printStackTrace();
				}

				newsContent.setTaskid(searchTask.getTaskid());
				// newsContent.setTime(newsListJson.getTime());
				newsContent.setTitle(newsListJson.getTitle());
				newsContent.setUrl(newsListJson.getLinkUrl());

				newsContent.setNewsListJson(newsListJson);

				return new AsyncResult<NewsContent>(newsContent);
			}
			
		 
	

	}


	public static void main(String[] args) {
		String url = "https://www.baidu.com/s?wd=13915565806&pn=0";
		SanWangUnitService sanWangUnitService = new SanWangUnitService();
		SearchTask searchTask = new SearchTask();
		searchTask.setLinkurl(url);
		try {
			Document page = sanWangUnitService.getDoc(url, searchTask);
			
			System.out.println(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
