package app.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTookit {
	
	public static final Logger log = LoggerFactory.getLogger(HttpTookit.class);
	
	/** 
     * 模拟请求 
     *  
     * @param url       资源地址 
     * @param map   参数列表 
     * @param headerMap   header参数列表 
     * @param encoding  编码 
     * @return 
     * @throws ParseException 
     * @throws IOException 
     */  
    public static Map<String,Object> httpPostSend(String url, Map<String,String> map,Map<String,String> headerMap,String encoding) throws ParseException, IOException{  
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	
        //创建httpclient对象  
        CloseableHttpClient client = HttpClients.createDefault();  
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
        if(headerMap!=null){  
            for (Entry<String, String> entry : headerMap.entrySet()) {  
            	httpPost.setHeader(entry.getKey(),entry.getValue());
            }  
        } 
          
        //执行请求操作，并拿到结果（同步阻塞）  
        CloseableHttpResponse response = client.execute(httpPost);  
        //获取结果实体  
        HttpEntity entity = response.getEntity();
        
        String body = "";  
        if (entity != null) {  
            //按指定编码转换结果实体为String类型  
            body = EntityUtils.toString(entity, encoding);  
        }  
        EntityUtils.consume(entity);  
        //释放链接  
        response.close();  
        resultMap.put("status", response.getStatusLine().getStatusCode());
        resultMap.put("body", body);
        return resultMap;  
    } 
}
