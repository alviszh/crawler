package app;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.module.htmlunit.WebCrawler;

import app.bean.IPbean;
import app.bean.IpResponseBean;

public class TestGetIp {
	
	public static String num = "5";				//ip数量
	public static String pro = "210000";		//省份
	public static String city = "211000";		//城市
	public static String yys = "0";				//运营商
	public static String port = "1";			//协议类型  例：http  ,https
	public static String time = "1";			//稳定时长
	public static String type = "2";			//返回数据格式  选择json格式返回
	public static String ts = "1";				//是否显示IP过期时间（1显示 2不显示）
	public static String ys = "1";				//是否显示IP运营商（1显示 0不显示）
	public static String cs = "1";				//是否显示位置信息（1显示 0不显示）
	public static String lb = "1";				//分隔符(1:\r\n 2:/br 3:\r 4:\n 5:\t 6 :自定义)
	public static String sb = "0";				//自定义分隔符
	public static String pb = "4";				//未知
	public static String mr = "0";				//未知
	
	public static String url = "http://d.jghttp.golangapi.com/getip?num=5&type=2&pro=210000&city=211000&yys=0&port=1&time=1&ts=1&ys=1&cs=1&lb=1&sb=0&pb=4&mr=0&regions=";
	
	
	public static void main(String[] args) {
		
		String json = getIP();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		IpResponseBean ipResponseBean = new Gson().fromJson(json, IpResponseBean.class);
		List<IPbean> list = ipResponseBean.getData();
		List<HttpProxyBean> proxyBeans = new ArrayList<HttpProxyBean>();
		for(IPbean bean :list){
			HttpProxyBean proxyBean = new HttpProxyBean();
			proxyBean.setIp(bean.getIp());
			proxyBean.setPort(bean.getPort());
			proxyBean.setName("");
			proxyBean.setInstanceId("");
			proxyBeans.add(proxyBean);
		}
		
		int random = (int) ( Math.random () * proxyBeans.size() ); 
		HttpProxyBean hpb = proxyBeans.get(random); 
		
		HttpProxyRes httpProxyRes = new HttpProxyRes();
		httpProxyRes.setCity(list.get(0).getCity());
		httpProxyRes.setHttpProxyBeanSet(proxyBeans);
		httpProxyRes.setIp(hpb.getIp());
		httpProxyRes.setPort(hpb.getPort());
		httpProxyRes.setTotalnum(proxyBeans.size());
		httpProxyRes.setErrornum(0);
		httpProxyRes.setInstanceId("");
		httpProxyRes.setName("");
		httpProxyRes.setUpdateTime(sdf.format(new Date()));
		
		String result = new Gson().toJson(httpProxyRes);
		System.out.println("==========================================");
		System.out.println(result);
		
		
	}


	private static String getIP() {
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest request;
		String html = null;
		try {
			request = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(request);
			html = page.getWebResponse().getContentAsString();
			System.out.println(html);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html; 
	}

}
