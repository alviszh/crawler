package TestUrl;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.module.htmlunit.WebCrawler;

/**
 * @description:
 * @author: sln 
 * @date: 2018年3月29日 上午9:26:55 
 */
//该测试类用于需要跳过ssl认证的网站
public class SLLTest {
	public static void main(String[] args) throws Exception {
		String loginUrl="http://public.tj.hrss.gov.cn/uaa/api/person/idandmobile/login";
		
		String html = getHtmlSourceCodeByIgnoreSSL(loginUrl,null,"utf-8");
		System.out.println("获取的登录响应页面是："+html);
		String md5Result = HelperUtils.getMd5Result(html);
		System.out.println("本次登录页面加密后的MD5结果是："+md5Result);
		
		//如下信息获取该登录页面相关的所有js的全路径
		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("script[src]");
		for(Element jsElement:eles){
			String absJsPath = jsElement.attr("src");    //返回js的绝对路径
			System.out.println("返回的js绝对路径是："+absJsPath);
			
			String jsContent = getHtmlSourceCodeByIgnoreSSL(absJsPath,null,"utf-8");
		    String jsmd5 = DigestUtils.md5Hex(jsContent); 
		    System.out.println(absJsPath+"-------jsmd5-------"+jsmd5);
		}
	}
	//////////////////////////////////////////////////////////////////////////
	//调研的过程中忽视如下信息，如下内容用于跳过部分网站的ssl认证
	
	
	//跳过ssl认证，请求html
	public static String getHtmlSourceCodeByIgnoreSSL(String loginUrl,Map<String,String> map,String encoding){
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
			e.printStackTrace();
			System.out.println("跳过ssl登录模拟请求时出现异常："+e.toString());
		}
		return sourceCode;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////////////

	//用htmlunit方式请求js
	public  String getJsByHtmlunit(String jsPath) throws Exception{
		String sourceCode="";
		WebClient webClient = WebCrawler.getInstance().getWebClient(); 
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
	    String sourceCode = HelperUtils.readStream(ins);
		return sourceCode;
	}
	//跳过ssl认证，请求js
	public  String getJsSourceCodeByIgnoreSSL(String loginUrl,Map<String,String> map,String encoding){
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
			e.printStackTrace();
			System.out.println("跳过ssl登录模拟请求时出现异常："+e.toString());
		}
		return sourceCode;
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
}
