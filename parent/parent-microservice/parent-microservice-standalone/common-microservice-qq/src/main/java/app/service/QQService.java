package app.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.mobile.json.CookieJson;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.qq.json.QQStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microservice.dao.entity.crawler.qq.TaskQQ;
import com.microservice.dao.repository.crawler.qq.TaskQQRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.service.aop.ICrawlerLogin;
import app.unit.QQunit;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.qq"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.qq"})
public class QQService extends QQunit{
	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private QQServiceUnit qQServiceUnit;
	@Autowired
	private TaskQQRepository taskQQRepository;
	Map<String, Future<String>> listfuture = new HashMap<>();
	String skey = "";
//	@Override
//	@Async
	public TaskQQ login(PbccrcJsonBean pbccrcJsonBean) {
		String taskid = pbccrcJsonBean.getMapping_id();
		TaskQQ taskqq = taskQQRepository.findByTaskid(taskid);
		try {
			taskqq = qQServiceUnit.login(pbccrcJsonBean,taskqq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracerLog.addTag("qq登录网页问题", e.getMessage());
			quit(pbccrcJsonBean);
		}
		return taskqq;
		
	}

//	@Override
//	@Async
	public TaskQQ getAllData(PbccrcJsonBean pbccrcJsonBean) {
		TaskQQ taskqq = taskQQRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		Set<Cookie> set = transferJsonToSet(taskqq.getCookies());
		for (Cookie cookie : set) {
			if(cookie.getName().equals("p_skey")){
				skey = cookie.getValue();
			}
		}
		System.out.println(skey);		
		Iterator<Cookie> j = set.iterator();
		while(j.hasNext()){
			webClient.getCookieManager().addCookie(j.next());
		}
		String g_tk = "";
		try {
			g_tk = encryptedPhone("skey.js", "getskey", skey, "");
		} catch (Exception e) {
			e.printStackTrace();
			quit(pbccrcJsonBean);
		}
		
		/*Future<String> qun = qQServiceUnit.getqqqun(pbccrcJsonBean,taskqq,g_tk);
		listfuture.put("getqqqun", qun);
		
		
		Future<String> friend = qQServiceUnit.getfriend(pbccrcJsonBean,taskqq,g_tk);
		listfuture.put("getfriend", friend);
		
		
		Future<String> user = qQServiceUnit.getuser(pbccrcJsonBean,taskqq,g_tk);
		listfuture.put("getuser", user);
		
		while (true) {
			for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
				if (entry.getValue().isDone()) { // 判断是否执行完毕
					listfuture.remove(entry.getKey());
					break;
				}
			}
			if (listfuture.size() == 0) {
				break;
			}
		}*/
		taskqq = qQServiceUnit.getqqqun(pbccrcJsonBean,taskqq,g_tk);
		taskqq = qQServiceUnit.getfriend(pbccrcJsonBean,taskqq,g_tk);
		taskqq = qQServiceUnit.getuser(pbccrcJsonBean,taskqq,g_tk);
		taskqq = updateTaskQQ(pbccrcJsonBean.getMapping_id());
		
		return taskqq;
	}

	
	public static Set<Cookie> transferJsonToSet(String json) {

		Set<Cookie> set = new HashSet<Cookie>();
		Set<CookieJson> cookiesJsonSet = new Gson().fromJson(json, new TypeToken<Set<CookieJson>>() {
		}.getType());
		for (CookieJson cookieJson : cookiesJsonSet) {
			Cookie cookie = new Cookie(cookieJson.getDomain(), cookieJson.getKey(), cookieJson.getValue());
			set.add(cookie);
		}
		
		return set;

	}
	public static String encryptedPhone(String jsname,String Navi,String writid, String runEval) throws Exception{    
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		String path = readResource(jsname, Charsets.UTF_8);
		//System.out.println(path);
		//FileReader reader1 = new FileReader(path); // 执行指定脚本
		engine.eval(path); 
		final Invocable invocable = (Invocable) engine;  
		Object data = invocable.invokeFunction(Navi,writid,runEval);
		return data.toString(); 
	}
	public static String readResource(final String fileName, Charset charset) throws IOException {
		return Resources.toString(Resources.getResource(fileName), charset);
	}

	/**
     * @Des 系统退出，释放资源
     * @param pbccrcJsonBean
     */
    public void quit(PbccrcJsonBean pbccrcJsonBean){
        //调用公用释放资源方法
    	qQServiceUnit.quit(pbccrcJsonBean);
    }
	
	
}
