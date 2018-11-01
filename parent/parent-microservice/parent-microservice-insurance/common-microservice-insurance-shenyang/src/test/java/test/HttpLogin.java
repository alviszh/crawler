package test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class HttpLogin {
    private static HttpClient httpClient=new DefaultHttpClient();

    //主登录入口
    public static void loginDouban(){
        String login="http://221.207.175.178:7989/uaa/api/person/idandmobile/login";
        String first="http://221.207.175.178:7989/uaa/personlogin#/personLogin";
        String form_email="231084198511174027";
        String form_password="851117";



//构建参数

        List<NameValuePair> list=new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("username", "231084198511174027"));
        list.add(new BasicNameValuePair("password", "851117"));


        try {

            HttpGet httpGet=new HttpGet("http://221.207.175.178:7989/uaa/personlogin#/personLogin");
            HttpResponse response1=httpClient.execute(httpGet);
            HttpEntity entity1=response1.getEntity();
            String result1=EntityUtils.toString(entity1,"utf-8");
            System.out.println(result1);
            System.out.println("\nresult1==="+result1);

            List<Cookie> cookies = ((AbstractHttpClient) httpClient).getCookieStore().getCookies();
            String captcha_id=getImgID();
            System.out.println("请输入验证码：");
            captcha_id = new Scanner(System.in).nextLine();
            list.add(new BasicNameValuePair("captchaWord", captcha_id));


            HttpPost httpPost = new HttpPost(login);
            httpPost.setEntity(new UrlEncodedFormEntity(list));
            HttpResponse response=httpClient.execute(httpPost);
            HttpEntity entity=response.getEntity();

            String result=EntityUtils.toString(entity,"utf-8");
            System.out.println("\nresult2==="+response.toString());
//            CookieStore cookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
            httpPost = new HttpPost("http://221.207.175.178:7989/api/security/user");
            response=httpClient.execute(httpPost);
            entity=response.getEntity();
            result=EntityUtils.toString(entity,"utf-8");
            System.out.println("\nresult3==="+result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 获取验证码图片“token”值
     * @return token
     */
    private static String getImgID(){
        String src="http://221.207.175.178:7989/uaa/captcha/img/";
        HttpGet httpGet=new HttpGet(src);
        String id="";
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity=response.getEntity();
            String content=EntityUtils.toString(entity,"utf-8");
            Map<String,String> mapList=getResultList(content);
            id=mapList.get("id");
            String url=src + id;
            downImg(url);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * 用JSON 把数据格式化，并生成迭代器，放入Map中返回
     * @param content 请求验证码时服务器返回的数据
     * @return Map集合
     */
    public static Map<String,String> getResultList(String content){
        Map<String,String> maplist=new HashMap();

        try {
            maplist= JSON.parseObject(content,new TypeReference<Map<String,String>>(){});

        } catch (JSONException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return maplist;
    }
    /**
     * 此方法是下载验证码图片到本地
     * @param src  给个验证图片完整的地址
     */
    private static void downImg(String src){
        File fileDir=new File("D:\\img\\http.jpg");
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file=new File("D:\\img\\http.jpg");
        if(file.exists()){
            file.delete();
        }
        InputStream input = null;
        FileOutputStream out= null;
        HttpGet httpGet=new HttpGet(src);
        try {
            HttpResponse response=httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            input = entity.getContent();
            int i=-1;
            byte[] byt=new byte[1024];
            out=new FileOutputStream(file);
            while((i=input.read(byt))!=-1){
                out.write(byt);
            }
            System.out.println("图片下载成功！");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        loginDouban();
    }
}