package app.utils;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
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
import org.springframework.stereotype.Component;


/**
 * 此工具类用于忽略人行征信系统的ssl认证问题——————————————————————————（调研时使用）
 * 
 * 该工具类中有几种跳过ssl认证的方式，其各有利弊，在使用的过程中：
 * 
 * （1）方式一：启动微服务的时候，相关程序会正常执行一般，但貌似会修改容器的运行环境，导致
 * 		    第二次执行任务的时候，某些网站无法正确执行js，报错：javax.net.ssl.SSLHandshakeException: java.security.cert.CertificateException: Certificates does not conform to algorithm constraints
 * 
 * （2）方式二：该方式和方式一相比，首次执行不会影响下次执行的环境，但是添加了部分社保和公积金以后，发现某些社保和公积金还是会存在ssl证书的问题，
 * 		甚至是其他证书，若是在程序中用if判断控制，会很复杂，期初想法是将url表中添加一个字段（whetherssl）用于区分是否需要跳过ssl证书问题，
 * 		然后在程序中分别写，后决定不管是解析html还是其对应的js，都用方式二,程序可以正确执行，但是发现一个问题：某些网站（如cmcc）用方式一执行之后
 * 		相关js数量不为0，但是用方式二执行之后，js数量为0，且这样的情况不止在一个网站中出现，故决定尝试方式三
 * 
 * （3）方式三：出现的问题是，某些网站利用如下代码不能获取js的绝对路径（后期rancher上运行发现，此方法也会影响下次执行的运行环境）
 * 		(String jsPath = jsElement.attr("abs:src");
 * 		导致运行时报错：no protocol
 * 		出错原因在于：请求的绝大部分的js的url没有以http等信息开头
 * 
 * 		为解决如上问题，决定将这样的网站获取js全路径的方式按照如下方式进行：（以吉林市公积金为例）
 * 		String jsPath=jsElement.attr("src");    //得到以"/"开头的/jlwsyyt/scripts/jquery.min.js
		jsPath="https://old.jlgjj.gov.cn:9443"+jsPath;
 * 	
 * 		综上所述，决定将请求的url进行截取，取到域名部分，即第三个“/”之前
 * 
 * 		有的网站html中直接写的就是全路径，若是按照如上方式，就会在已经完整的路径前重复添加http等信息，
 * 		故决定获取全路径方法执行后，判断是否为空，若是为空，就用这句代码jsPath=jsElement.attr("src");
 * 		获取js部分路径，接下来还需要判断返回的路径是不是以http开头（此处还需要分为两种方式进行）
 * 	
 *      /////////////////////////////////////
 *     
 *      综上所述，关于跳过ssl证书的认证，最后采用的是第二种方法
 *     
 * @author sln
 *
 */
@Component
public class SSLUtils {
	/////////////////////////////尝试的第一种方式start///////////////////////////////////
	//获取登录页面的html
	public static String getHtmlContentRequest(String url,int timeOut) throws Exception{  
		URL u = new URL(url);  
		if("https".equalsIgnoreCase(u.getProtocol())){  
			ignoreSsl();  
		}  
		Document doc = Jsoup.parse(u, timeOut);    			
		String html = doc.html();	     
		return html;
	}  
	//获取js内容，用如下URLConnection方式(此方法目的是为了跳过javax.net.ssl.SSLHandshakeException: java.security.cert.CertificateException: Certificates does not conform to algorithm constraints )
	/*public static String getJsContentRequest(String url,int timeOut) throws Exception{  
        URL u = new URL(url);  
        if("https".equalsIgnoreCase(u.getProtocol())){  
            ignoreSsl();  
        }  
        URLConnection conn = u.openConnection();  
        conn.setConnectTimeout(timeOut);  
        conn.setReadTimeout(timeOut);  
        String html= IOUtils.toString(conn.getInputStream());  
        return html;
    }  */
	
	//如下暂时未用到
   /* public String postRequest(String urlAddress,String args,int timeOut) throws Exception{  
        URL url = new URL(urlAddress);  
        if("https".equalsIgnoreCase(url.getProtocol())){  
            SslUtils.ignoreSsl();  
        }  
        URLConnection u = url.openConnection();  
        u.setDoInput(true);  
        u.setDoOutput(true);  
        u.setConnectTimeout(timeOut);  
        u.setReadTimeout(timeOut);  
        OutputStreamWriter osw = new OutputStreamWriter(u.getOutputStream(), "UTF-8");  
        osw.write(args);  
        osw.flush();  
        osw.close();  
        u.getOutputStream();  
        return IOUtils.toString(u.getInputStream());  
    }  */
	private static void trustAllHttpsCertificates() throws Exception {  
	    TrustManager[] trustAllCerts = new TrustManager[1];  
	    TrustManager tm = new miTM();  
	    trustAllCerts[0] = tm;  
	    SSLContext sc = SSLContext.getInstance("SSL");  
	    sc.init(null, trustAllCerts, null);  
	    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
	}  
  
	static class miTM implements TrustManager,X509TrustManager {  
	    public X509Certificate[] getAcceptedIssuers() {  
	        return null;  
	    }  
	  
	    public boolean isServerTrusted(X509Certificate[] certs) {  
	        return true;  
	    }  
	  
	    public boolean isClientTrusted(X509Certificate[] certs) {  
	        return true;  
	    }  
	  
	    public void checkServerTrusted(X509Certificate[] certs, String authType)  
	            throws CertificateException {  
	        return;  
	    }  
	  
	    public void checkClientTrusted(X509Certificate[] certs, String authType)  
	            throws CertificateException {  
	        return;  
	    }  
	}  
   
/** 
 * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用 
 * @throws Exception 
 */  
	public static void ignoreSsl() throws Exception{  
	    HostnameVerifier hv = new HostnameVerifier() {  
	        public boolean verify(String urlHostName, SSLSession session) {  
	            System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());  
	            return true;  
	        }  
	    };  
	    trustAllHttpsCertificates();  
	    HttpsURLConnection.setDefaultHostnameVerifier(hv);  
	}  
	/////////////////////////////尝试的第一种方式end///////////////////////////////////
	
	
	/////////////////////////////尝试的第二种方式start///////////////////////////////////
	/** 
	 * 绕过验证 
	 *   
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 * @throws KeyManagementException  
	 */  
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
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
	
	/** 
	 * 模拟请求 
	 *  
	 * @param url       资源地址 
	 * @param map   参数列表 
	 * @param encoding  编码 
	 * @return 
	 * @throws NoSuchAlgorithmException  
	 * @throws KeyManagementException  
	 * @throws IOException  
	 * @throws ClientProtocolException  
	 */  
	public static String send(String url, Map<String,String> map,String encoding){  
		String body = "";  
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
		        body = EntityUtils.toString(entity, encoding);  
		    }  
		    EntityUtils.consume(entity);  
		    //释放链接  
		    response.close();  
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("跳过ssl登录模拟请求时出现异常："+e.toString());
		}
		return body; 
	}  
	/////////////////////////////尝试的第二种方式end///////////////////////////////////
	
	/////////////////////////////尝试的第三种方式start///////////////////////////////////
/*	public static String getHtmlOrJsContent(String targetUrl){
		String html="";
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new SSLUtils().new NullHostNameVerifier());
	        SSLContext sc = SSLContext.getInstance("TLS");
//	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        URL url = new URL(targetUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");// POST GET PUT DELETE
	        // 设置访问提交模式，表单提交
	        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
	        conn.setConnectTimeout(150000);// 连接超时 单位毫秒
	        conn.setReadTimeout(150000);// 读取超时 单位毫秒
	        InputStream inStream = conn.getInputStream();
	        html = IOUtils.toString(inStream);  
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("跳过ssl登录模拟请求时出现异常："+e.toString());
		}
		return html;
		
	}*/
	static TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    } };

    public class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    }
	
	/////////////////////////////尝试的第三种方式end///////////////////////////////////
}  