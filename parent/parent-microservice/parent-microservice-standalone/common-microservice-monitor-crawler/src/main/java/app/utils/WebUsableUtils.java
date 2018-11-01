package app.utils;

import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

import com.crawler.monitor.json.MonitorWebUsableBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

/**
 * @description:监测网站可用性的工具类
 * @author: sln 
 * @date: 2018年3月9日 上午10:41:18 
 */
public class WebUsableUtils {
	public static void main(String[] args) {   
	    MonitorWebUsableBean connectingAddress = connectingAddress("https://ipcrs.pbccrc.org.cn/page/login/loginreg.jsp","人行征信");
	    System.out.println("获取的源码是："+connectingAddress.getSourcecode());
	}
	    
	
	static HostnameVerifier hv = new HostnameVerifier() {  
        public boolean verify(String urlHostName, SSLSession session) {  
            return true;  
        }  
    };  
    /**
     * 如下方法用于统一调用，方法中根据传入的url类型选择对应的方法连接
     * @param remoteInetAddr
     * @return
     */
    public static MonitorWebUsableBean connectingAddress(String remoteInetAddr,String webType){
    	MonitorWebUsableBean monitorWebUsableBean=null;
        String tempUrl=remoteInetAddr.substring(0, 5);//取出地址前5位
        if(tempUrl.contains("http")){//判断传过来的地址中是否有http
            if(tempUrl.equals("https")){//判断服务器是否是https协议
                try {
                    trustAllHttpsCertificates();//当协议是https时
                } catch (Exception e) {
                    e.printStackTrace();
                }  
                HttpsURLConnection.setDefaultHostnameVerifier(hv);//当协议是https时
            }
            monitorWebUsableBean = isConnServerByHttp(remoteInetAddr,webType);
        }else{//传过来的是IP地址(此方法监测任务中用不到，因为测试的是链接url)
            isReachable(remoteInetAddr);
        }
		return monitorWebUsableBean;
    }
    /**
     * 传入需要连接的IP，返回是否连接成功
     * 
     * @param remoteInetAddr
     * @return
     */
    public static boolean isReachable(String remoteInetAddr) {// IP地址是否可达，相当于Ping命令
        boolean reachable = false;
        try {
            InetAddress address = InetAddress.getByName(remoteInetAddr);
            reachable = address.isReachable(2000);
        } catch (Exception e) {
        	System.out.println(remoteInetAddr+"   在尝试是否可以ping通的时候出现异常"+e.toString());
        }
        return reachable;
    }
    public static MonitorWebUsableBean isConnServerByHttp(String serverUrl, String webType) {// 服务器是否开启
    	int responseCode=200;
    	URL url;
    	HttpURLConnection conn = null;
    	MonitorWebUsableBean monitorWebUsableBean=new MonitorWebUsableBean();
    	try {
    		//没有这句代码，会报错：java.net.ProtocolException: Server redirected too many  times (20)(出现在宿迁社保)，但是如下代码貌似并没有起到多大作用
    		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));  
    		url = new URL(serverUrl);
    		conn = (HttpURLConnection) url.openConnection();
    		conn.setRequestProperty("User-agent","Mozilla/4.0");   //响应403的问题：如果用java代码HttpURLConnection去连的话 http header 中的User-Agent就为空，解决方法就是在连接之前先设置这个属性。
    		conn.setConnectTimeout(60 * 1000);   //有的网站响应很慢
//          conn.setInstanceFollowRedirects(false);  //防止服务器默认的跳转(这句代码如果存在，宿迁社保报的是302)
    		responseCode = conn.getResponseCode(); //获取测试连接的响应状态码
    		InputStream inputStream = conn.getInputStream();
    		String sourceCode = PatternUtils.readStream(inputStream);
    		monitorWebUsableBean.setSourcecode(sourceCode);
    		monitorWebUsableBean.setWebStatusCode(responseCode);
    		monitorWebUsableBean.setExceptioninfo(responseCode+"");
    	} catch (Exception e) {
    		System.out.println(webType+"网站的异常信息是："+e.toString());
    		responseCode=201;  //对于未知的错误，响应这个状态码，呼和浩特公积金出现过的情况：java.net.SocketException: Unexpected end of file from server，网页显示未发送任何数据
    		monitorWebUsableBean.setWebStatusCode(responseCode);
    		monitorWebUsableBean.setExceptioninfo(e.toString());
    		monitorWebUsableBean.setSourcecode("由于"+e.toString()+"异常，未能正常响应登录页源码");
    	} finally {   
    		conn.disconnect();
    	}
    	return monitorWebUsableBean;
    }
    /////////////////////////////////////////////////////////////
    /*以下是Https适用*/
    private static void trustAllHttpsCertificates() throws Exception {  
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
        javax.net.ssl.TrustManager tm = new miTM();  
        trustAllCerts[0] = tm;  
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext  
                .getInstance("SSL");  
        sc.init(null, trustAllCerts, null);  
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc  
                .getSocketFactory());  
    }  
  
    static class miTM implements javax.net.ssl.TrustManager,  
            javax.net.ssl.X509TrustManager {  
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
            return null;  
        }  
  
        public boolean isServerTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public boolean isClientTrusted(  
                java.security.cert.X509Certificate[] certs) {  
            return true;  
        }  
  
        public void checkServerTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  
  
        public void checkClientTrusted(  
                java.security.cert.X509Certificate[] certs, String authType)  
                throws java.security.cert.CertificateException {  
            return;  
        }  
    } 
    //==========================如上是默认监测网站可连接与否的方法，但是有的网站需要用如下方式测试可连接==========================
    //用htmlunit的方式获取响应的状态码
    public static MonitorWebUsableBean connectingByHtmlunit(String url,String webType){
    	MonitorWebUsableBean monitorWebUsableBean=new MonitorWebUsableBean();
    	int responseCode=200;
    	try {
    		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
    		webClient.getOptions().setJavaScriptEnabled(false);
    		webClient.getOptions().setTimeout(30*1000);   //设置超时时间
    		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
    		Page page = webClient.getPage(webRequest); 
    		if(null!=page){
    			responseCode = page.getWebResponse().getStatusCode();
    			String sourcecode = page.getWebResponse().getContentAsString();
    			monitorWebUsableBean.setSourcecode(sourcecode);
				monitorWebUsableBean.setWebStatusCode(responseCode);
				monitorWebUsableBean.setExceptioninfo(responseCode+"");
    		}
		} catch (Exception e) {
			System.out.println(webType+"网站的异常信息是："+e.toString());
			responseCode=201;  //对于未知的错误，响应这个状态码，呼和浩特公积金出现过的情况：java.net.SocketException: Unexpected end of file from server，网页显示未发送任何数据
			monitorWebUsableBean.setWebStatusCode(responseCode);
			monitorWebUsableBean.setExceptioninfo(e.toString());
			monitorWebUsableBean.setSourcecode("由于"+e.toString()+"异常，未能正常响应登录页源码");
		} 
		return monitorWebUsableBean;
    }
    
    
    //用跳过ssl认证的方式获取响应的状态码
    public static MonitorWebUsableBean connectingByIgnoreSSL(String url,String webType){
    	MonitorWebUsableBean monitorWebUsableBean=new MonitorWebUsableBean();
    	int responseCode=200;
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
		    HttpPost httpPost = new HttpPost(url);  
		    //装填参数  
		    List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
		    //设置参数到请求对象中  
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));  
		    //设置header信息  
		    //指定报文头【Content-type】、【User-Agent】  
		    httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");  
		    httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");  
		    //执行请求操作，并拿到结果（同步阻塞）  
		    CloseableHttpResponse response = client.execute(httpPost);  
		    //获取结果实体  
		    HttpEntity entity = response.getEntity();  
		    if (entity != null) { 
		    	responseCode = response.getStatusLine().getStatusCode();
		    	InputStream inputStream = response.getEntity().getContent();  //响应的源码
	    		String sourceCode = PatternUtils.readStream(inputStream);
	    		monitorWebUsableBean.setSourcecode(sourceCode);
				monitorWebUsableBean.setWebStatusCode(responseCode);
				monitorWebUsableBean.setExceptioninfo(responseCode+"");
		    }  
		    //释放链接  
		    response.close();  
		} catch (Exception e) {
			System.out.println("跳过ssl登录模拟请求时出现异常："+e.toString());
			responseCode=201;  //对于未知的错误，响应这个状态码，呼和浩特公积金出现过的情况：java.net.SocketException: Unexpected end of file from server，网页显示未发送任何数据
			monitorWebUsableBean.setWebStatusCode(responseCode);
			monitorWebUsableBean.setExceptioninfo(e.toString());
			monitorWebUsableBean.setSourcecode("由于"+e.toString()+"异常，未能正常响应登录页源码");
		}
		return monitorWebUsableBean;
    }
    /** 
	 * 绕过验证 
	 *   
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 * @throws KeyManagementException  
	 */  
	public static  SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
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
	//
	public static MonitorWebUsableBean connectingByHttpClient(String loginUrl, String webType) {
		MonitorWebUsableBean monitorWebUsableBean=new MonitorWebUsableBean();
    	int responseCode=200;
    	try {
    		
    		CloseableHttpClient httpClient=null;
    		CloseableHttpResponse response=null;
    		
    		httpClient=HttpClients.createDefault(); // 创建httpClient实例  
    		RequestConfig requestConfig = RequestConfig.custom()
    	                .setConnectTimeout(20000)   //设置连接超时时间
    	                .setConnectionRequestTimeout(20000) // 设置请求超时时间
    	                .setSocketTimeout(20000)
    	                .setRedirectsEnabled(true)//默认允许自动重定向
    	                .build();
    		HttpGet httpGet=new HttpGet(loginUrl); // 创建httpget实例  
    		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0"); // 设置请求头消息User-Agent  
    		httpGet.setConfig(requestConfig);
    		response=httpClient.execute(httpGet); // 执行http get请求  
    		int statusCode = response.getStatusLine().getStatusCode();
    		monitorWebUsableBean.setWebStatusCode(statusCode);
    		monitorWebUsableBean.setExceptioninfo(responseCode+"");
    		InputStream inputStream = response.getEntity().getContent();  //响应的源码
    		String sourceCode = PatternUtils.readStream(inputStream);
    		monitorWebUsableBean.setSourcecode(sourceCode);
    		response.close(); // response关闭  
    		httpClient.close(); // httpClient关闭  
		} catch (Exception e) {
			System.out.println(webType+"网站的异常信息是："+e.toString());
			responseCode=201;  //对于未知的错误，响应这个状态码，呼和浩特公积金出现过的情况：java.net.SocketException: Unexpected end of file from server，网页显示未发送任何数据
			monitorWebUsableBean.setWebStatusCode(responseCode);
			monitorWebUsableBean.setExceptioninfo(e.toString());
			monitorWebUsableBean.setSourcecode("由于"+e.toString()+"异常，未能正常响应登录页源码");
		} 
		return monitorWebUsableBean;
	}
}
