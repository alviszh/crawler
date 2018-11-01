package app.webchange;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.monitor.json.MonitorJsCompare;
import com.crawler.monitor.json.MonitorJsTempBean;
import com.crawler.monitor.json.MonitorTempBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebLoginUrl;
import com.microservice.dao.entity.crawler.monitor.MonitorHtmlAfterTreat;
import com.microservice.dao.entity.crawler.monitor.MonitorJsAfterTreat;
import com.microservice.dao.repository.crawler.monitor.MonitorHtmlAfterTreatRepository;
import com.microservice.dao.repository.crawler.monitor.MonitorJsAfterTreatRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.utils.MD5Utils;
import app.utils.PatternUtils;

/**
 * @description: 该工具类提供请求不同网站或者js的方法
 * 
 * 如果下列所有方法只返回html，对返回的html再次加工成doc,将无法得到js绝对路径，故后来决定用实体bean返回
 * @author: sln 
 * @date: 2018年3月22日 下午5:31:51 
 */
@Component
public class MonitorHelperService {
	@Autowired
	private MonitorHtmlAfterTreatRepository htmlAfterTreatRepository;
	@Autowired
	private MonitorJsAfterTreatRepository jsAfterTreatRepository;
	@Autowired
	private HtmlOrJsContentAfterTreatService htmlOrJsContentAfterTreatService;
	@Autowired
	private TracerLog tracer;
	/////////////////////////////////////①//////////////////////////////////////////
	//用htmlunit方式请求html
	public  MonitorTempBean getHtmlByHtmlunit(String webType,String url){
		String html="";
		int webStatusCode = 200;
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setTimeout(20000);
		MonitorTempBean tempBean=new MonitorTempBean();
		try {
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			Page page = webClient.getPage(webRequest); 
			if(null!=page){
				webStatusCode = page.getWebResponse().getStatusCode();
				if(200==webStatusCode){
					html = page.getWebResponse().getContentAsString();
				}
			}
			tempBean.setWebStatusCode(webStatusCode);
			tempBean.setExceptioninfo(webStatusCode+"");
		} catch (Exception e) {
			tracer.addTag(webType+"   获取登录源码页过程中出现异常，暂时认为网站不可用：",e.toString());
			tempBean.setWebStatusCode(201);
			tempBean.setExceptioninfo(e.toString());
		}
		Document doc = Jsoup.parse(html);
		int currentJsCount = getCurrentJsCount(doc);
		tempBean.setCurrentJsCount(currentJsCount);
		tempBean.setHtml(html);
		tempBean.setDoc(doc);
		return tempBean;
	}
	//用httpclient方式请求html
	public  MonitorTempBean getHtmlByHttpClient(String webType,String url){
		String html="";
		CloseableHttpClient httpClient=null;
		CloseableHttpResponse response=null;
		int webStatusCode = 200;
		MonitorTempBean tempBean=new MonitorTempBean();
		try {
			httpClient=HttpClients.createDefault(); // 创建httpClient实例  
			RequestConfig requestConfig = RequestConfig.custom()
		               .setConnectTimeout(20000)   //设置连接超时时间
		               .setConnectionRequestTimeout(20000) // 设置请求超时时间
		               .setSocketTimeout(20000)
		               .setRedirectsEnabled(true)//默认允许自动重定向
		               .build();
			HttpGet httpGet=new HttpGet(url); // 创建httpget实例  
			httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0"); // 设置请求头消息User-Agent  
			httpGet.setConfig(requestConfig);
			response=httpClient.execute(httpGet); // 执行http get请求  
			HttpEntity entity = response.getEntity();
			webStatusCode = response.getStatusLine().getStatusCode();
			if (entity!=null) {
				html = EntityUtils.toString(entity,"UTF-8");  
		        EntityUtils.consume(entity);//关闭内容流  
		    }  
			tempBean.setWebStatusCode(webStatusCode);
			tempBean.setExceptioninfo(webStatusCode+"");
			response.close(); // response关闭  
			httpClient.close(); // httpClient关闭  
		} catch (Exception e) {
			tracer.addTag(webType+"   获取登录源码页过程中出现异常，暂时认为网站不可用：",e.toString());
			tempBean.setWebStatusCode(201);
			tempBean.setExceptioninfo(e.toString());
		}
		Document doc = Jsoup.parse(html);
		int currentJsCount = getCurrentJsCount(doc);
		tempBean.setCurrentJsCount(currentJsCount);
		tempBean.setHtml(html);
		tempBean.setDoc(doc);
		return tempBean;
	}
	
	/////////////////////////////////////②//////////////////////////////////////////
	//如果对返回的html再次加工成doc,将无法得到js绝对路径，故用实体bean返回
	public MonitorTempBean getHtmlByJsoup(String webType,String loginUrl){
		String sourceCode = "";  
		int webStatusCode = 200;
		Document doc = null;
		MonitorTempBean tempBean=new MonitorTempBean();
		try {
			URL url = new URL(loginUrl);   
			doc = Jsoup.parse(url, 10*1000);    
			sourceCode = doc.html();
			tempBean.setWebStatusCode(webStatusCode);
			tempBean.setExceptioninfo(webStatusCode+"");
		} catch (Exception e) {
			tracer.addTag(webType+"   获取登录源码页过程中出现异常，暂时认为网站不可用：",e.toString());
			tempBean.setWebStatusCode(201);
			tempBean.setExceptioninfo(e.toString());
		}
		//获取该网页对应的js数量
		int currentJsCount = getCurrentJsCount(doc);
		tempBean.setCurrentJsCount(currentJsCount);
		tempBean.setHtml(sourceCode);
		tempBean.setDoc(doc);
		return tempBean;
	}
	
	/////////////////////////////////////③//////////////////////////////////////////
	//跳过ssl认证，请求html
	public  MonitorTempBean getHtmlSourceCodeByIgnoreSSL(String webType,String loginUrl,Map<String,String> map,String encoding){
		String sourceCode = "";  
		int webStatusCode = 200;
		MonitorTempBean tempBean=new MonitorTempBean();
		try {
		    //采用绕过验证的方式处理https请求  
		    SSLContext sslcontext = createIgnoreVerifySSL();  
		      
	       // 设置协议http和https对应的处理socket链接工厂的对象  
	        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
	           .register("http", PlainConnectionSocketFactory.INSTANCE)  
	           .register("https", new SSLConnectionSocketFactory(sslcontext))  
	           .build();  
	        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
	        HttpClients.custom().setConnectionManager(connManager);  
		  
		    //创建自定义的httpclient对象  
		    CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();  
		    //创建post方式请求对象  
		    HttpPost httpPost = new HttpPost(loginUrl);  
		      
		    //装填参数  
		    List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
		    if(map!=null){  
		        for (Entry<String, String> entry : map.entrySet()) {  
		            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
		        }  
		    }  
		    //设置参数到请求对象中  
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));  
		    //设置header信息  
		    //指定报文头【Content-type】、【User-Agent】  
		    httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
		    httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
		    //执行请求操作，并拿到结果（同步阻塞）  
		    CloseableHttpResponse response = client.execute(httpPost);  
		    webStatusCode = response.getStatusLine().getStatusCode();
		    //获取结果实体  
		    HttpEntity entity = response.getEntity();  
		    if (entity != null) {  
		        //按指定编码转换结果实体为String类型  
		    	sourceCode = EntityUtils.toString(entity, encoding);  
		    }  
			tempBean.setWebStatusCode(webStatusCode);
		    tempBean.setExceptioninfo(webStatusCode+"");
		    EntityUtils.consume(entity);  
		    //释放链接  
		    response.close();  
		} catch (Exception e) {
			tracer.addTag(webType+"   获取登录源码页过程中出现异常，暂时认为网站不可用：",e.toString());
			tempBean.setWebStatusCode(201);
			tempBean.setExceptioninfo(e.toString());
			
		}
		Document doc = Jsoup.parse(sourceCode);
		//获取该网页对应的js数量
		int currentJsCount = getCurrentJsCount(doc);
		tempBean.setCurrentJsCount(currentJsCount);
		tempBean.setHtml(sourceCode);
		tempBean.setDoc(doc);
		return tempBean;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////////////

	//用htmlunit方式请求js
	public  String getJsByHtmlunit(String jsPath) throws Exception{
		String sourceCode="";
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		WebRequest webRequest = new WebRequest(new URL(jsPath), HttpMethod.GET);
		Page page = webClient.getPage(webRequest); 
		if(null!=page){
			int statusCode = page.getWebResponse().getStatusCode();
			if(200==statusCode){
				sourceCode = page.getWebResponse().getContentAsString();
			}
		}
		return sourceCode;
		
	}
	//用jsoup方式请求js
	public  String getJsByJsoup(String jsPath) throws Exception{
		URL url= new URL(jsPath);     
	    InputStream ins = url.openStream();    
	    String sourceCode = PatternUtils.readStream(ins);
		return sourceCode;
	}
	//跳过ssl认证，请求js
	public  String getJsSourceCodeByIgnoreSSL(String webType,String loginUrl, Map<String,String> map,String encoding){
		String sourceCode = "";  
		try {
			//采用绕过验证的方式处理https请求  
			SSLContext sslcontext = createIgnoreVerifySSL();  
			
			// 设置协议http和https对应的处理socket链接工厂的对象  
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
					.register("http", PlainConnectionSocketFactory.INSTANCE)  
					.register("https", new SSLConnectionSocketFactory(sslcontext))  
					.build();  
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
			HttpClients.custom().setConnectionManager(connManager);  
			
			//创建自定义的httpclient对象  
			CloseableHttpClient client = HttpClients.custom().setConnectionManager(connManager).build();  
			//创建post方式请求对象  
			HttpPost httpPost = new HttpPost(loginUrl);  
			
			//装填参数  
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
			if(map!=null){  
				for (Entry<String, String> entry : map.entrySet()) {  
					nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));  
				}  
			}  
			//设置参数到请求对象中  
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));  
			//设置header信息  
			//指定报文头【Content-type】、【User-Agent】  
			httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
			httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
			//执行请求操作，并拿到结果（同步阻塞）  
			CloseableHttpResponse response = client.execute(httpPost);  
			//获取结果实体  
			HttpEntity entity = response.getEntity();  
			if (entity != null) {  
				//按指定编码转换结果实体为String类型  
				sourceCode = EntityUtils.toString(entity, encoding);  
			}  
			EntityUtils.consume(entity);  
			//释放链接  
			response.close();  
		} catch (Exception e) {
			tracer.addTag(webType+"跳过ssl登录模拟请求时出现异常：", e.toString());
		}
		return sourceCode;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	/** 
	 * 绕过验证 
	 *   
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 * @throws KeyManagementException  
	 */  
	public  SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
	    SSLContext sc = SSLContext.getInstance("SSLv3");  
	  
	    // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
	    X509TrustManager trustManager = new X509TrustManager() {  
	        @Override  
	        public void checkClientTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override  
	        public void checkServerTrusted(  
	                java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                String paramString) throws CertificateException {  
	        }  
	  
	        @Override  
	        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	            return null;  
	        }  
	    };  
	  
	    sc.init(null, new TrustManager[] { trustManager }, null);  
	    return sc;  
	}  
	//////////////////////////////////////////////////////////////////////////////////////
	public int getCurrentJsCount(Document doc){
		int currentJsCount=0;
		if(null!=doc){
			Elements eles = doc.select("script[src]");
			currentJsCount=eles.size();
		}		
		return currentJsCount;
	}
	public  MonitorJsTempBean getMonitorJsTempBean(String sourceCode){
		MonitorJsTempBean monitorJsTempBean=new MonitorJsTempBean();
//		String filter = PatternUtils.filter(sourceCode);
//	    int length = filter.length();
	    String jsmd5 = DigestUtils.md5Hex(sourceCode); 
//	    monitorJsTempBean.setAfterfilterjscode(filter);
	    monitorJsTempBean.setJscode(sourceCode);
//	    monitorJsTempBean.setJscontentlength(length);
	    monitorJsTempBean.setJsmd5(jsmd5);
		return monitorJsTempBean;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	//判断html源码需不需要进行加工处理
	/**
	 * 	对html源码的处理方法：
	 * 
	 *  removeByElementLocate;  //根据元素定位并且移除
		onlyRemainByElementLocate;  //根据元素定位，只留取这部分
		setEmptyByElementLocate;  //根据元素定位，根据属性置空变化的部分
		remainSourceCodeByAdd;    //根据所指定的字符串的起止位置截取该部分，最后进行累计（加法思路）
		replaceAllNoteSymbol;   //去除注释符号
	 *
	 */
	public String getHtmlAfterTreat(MonitorAllWebLoginUrl webLoginInfo,String html,Document doc){
		String webType = webLoginInfo.getWebtype().trim();
		boolean isHtmlaftertreatment = webLoginInfo.isHtmlaftertreatment();
		List<MonitorHtmlAfterTreat> list = null;
		String afterTreatHtml = null;
		if(isHtmlaftertreatment==true){  //需要进行处理(尽量全文用统一的方式处理)
			//根据登录url查询出该网站的html源码都需要经过什么方式处理
			List<String> htmlTreats = htmlAfterTreatRepository.findTreatmethodByWebType(webType);
			Set<String> set = new HashSet<String>(); 
			set.addAll(htmlTreats);
			List<String> newList=new ArrayList<>();
			newList.addAll(set);
			int size = newList.size();
			if(size>1){   //有两种或者两种以上的处理方法，可以用元素定位的，和不可以用元素定位的
				afterTreatHtml=html;
				if(htmlTreats.contains("removeByElementLocate")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,"removeByElementLocate");
					afterTreatHtml=htmlOrJsContentAfterTreatService.removeByElementLocate(doc, list);
				}
				if(htmlTreats.contains("setEmptyByElementLocate")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,"setEmptyByElementLocate");
					afterTreatHtml=htmlOrJsContentAfterTreatService.setEmptyByElementLocate(doc, list);
				}
				if(htmlTreats.contains("replaceAllByElementLocate")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,"replaceAllByElementLocate");
					afterTreatHtml=htmlOrJsContentAfterTreatService.replaceAllByElementLocate(afterTreatHtml,doc,list);
				}
				if(htmlTreats.contains("setEmptyBySplitAndReplace")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,"setEmptyBySplitAndReplace");
					afterTreatHtml=htmlOrJsContentAfterTreatService.setEmptyBySplitAndReplace(afterTreatHtml,list);
				}
			}else{   //仅有一种处理方法
				String treatMethod=htmlTreats.get(0);
				if(treatMethod.equals("removeByElementLocate")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,treatMethod);
					if(list.size()>0){  //需要经过该类型的处理
						afterTreatHtml=htmlOrJsContentAfterTreatService.removeByElementLocate(doc, list);
					}
				}else if(treatMethod.equals("onlyRemainByElementLocate")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,treatMethod);
					if(list.size()>0){
						afterTreatHtml=htmlOrJsContentAfterTreatService.onlyRemainByElementLocate(doc, list);
					}
				}else if(treatMethod.equals("setEmptyByElementLocate")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,treatMethod);
					if(list.size()>0){
						afterTreatHtml=htmlOrJsContentAfterTreatService.setEmptyByElementLocate(doc, list);
					}
				}else if(treatMethod.equals("remainSourceCodeByAdd")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,treatMethod);
					if(list.size()>0){  //大部分用这个方法
						afterTreatHtml=htmlOrJsContentAfterTreatService.remainSourceCodeByAdd(html, list);
					}
				}else if(treatMethod.equals("replaceAllNoteSymbol")){ //replaceAllNoteSymbol
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,treatMethod);
					if(list.size()>0){  //将注释符号去除
						afterTreatHtml=CutAllNoteSymbolService.replaceAllNoteSymbol(html);
					}
				}else if(treatMethod.equals("setEmptyBySplitAndReplace")){  //通过切割的方式定位并置空
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,treatMethod);
					afterTreatHtml=htmlOrJsContentAfterTreatService.setEmptyBySplitAndReplace(html,list);
				}else if(treatMethod.equals("replaceAllByElementLocate")){
					list = htmlAfterTreatRepository.findByWebtypeAndTreatmethod(webType,treatMethod);
					afterTreatHtml=htmlOrJsContentAfterTreatService.replaceAllByElementLocate(html,doc,list);
				}else{  //青岛特殊处理
					tracer.addTag("新的处理方式","根据调研中出现的新情况添加");
				}
			}
		}else{//不需要进行处理，返回的源码同最初源码
			afterTreatHtml=html;
		}
		return afterTreatHtml;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////
	//对js的源码进行处理
	public MonitorJsCompare getJsAfterTreat(MonitorJsAfterTreat monitorJsAfterTreat, String webType,String absJsPath, String jsContent) {
		String needTreatJs="";
		String jsOrLength="";
		String treatMethod="";
		String afterTreatJs="";
		int currentJsContentLength=0;
		if(monitorJsAfterTreat!=null){
			jsOrLength=monitorJsAfterTreat.getMd5orlength();
			if(jsOrLength.contains("md5")){
				treatMethod=monitorJsAfterTreat.getTreatmethod().trim();
				List<MonitorJsAfterTreat> list=null;
				if(treatMethod.equals("remainSourceCodeByAdd")){
					list = jsAfterTreatRepository.findByJspathAndTreatmethod(needTreatJs,treatMethod);
					if(list.size()>0){  //大部分用这个方法
						afterTreatJs=htmlOrJsContentAfterTreatService.remainJsSourceCodeByAdd(jsContent, list);
					}
				}else{ //replaceAllNoteSymbol
					list = jsAfterTreatRepository.findByJspathAndTreatmethod(needTreatJs,treatMethod);
					if(list.size()>0){  //将注释符号去除
						afterTreatJs=CutAllNoteSymbolService.replaceAllNoteSymbol(jsContent);
					}
				}
			}else if(jsOrLength.contains("length")){   //统计长度
				//统计长度之前先替换掉所有的注释符号
				jsContent=CutAllNoteSymbolService.replaceAllNoteSymbol(jsContent);
				currentJsContentLength = jsContent.length();
			}else{ //忽视该js的执行   ignore   部分js路径后边带有时间戳
				afterTreatJs=jsContent;
			}
		}else{
			afterTreatJs=jsContent;
		}
		String currentJsMd5=MD5Utils.StringToMd5(afterTreatJs);
		MonitorJsCompare monitorJsCompare=new MonitorJsCompare();
		monitorJsCompare.setCurrentJsContentLength(currentJsContentLength);
		monitorJsCompare.setCurrentJsMd5(currentJsMd5);
		monitorJsCompare.setAfterTreatJs(afterTreatJs);
		return monitorJsCompare;
	}
}
