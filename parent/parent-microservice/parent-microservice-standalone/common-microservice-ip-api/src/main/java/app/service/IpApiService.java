package app.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import com.crawler.aws.json.HttpProxyBean;
import com.crawler.aws.json.HttpProxyRes;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.mobile.IpCityCode;
import com.microservice.dao.repository.crawler.mobile.IpCityCodeRepository;
import com.module.htmlunit.WebCrawler;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import app.bean.IPbean;
import app.bean.IpResponseBean;
import app.client.aws.AwsApiClient;
import app.client.proxy.HttpProxyClient;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages="com.microservice.dao.entity.crawler.mobile")
@EnableJpaRepositories(
		basePackages="com.microservice.dao.repository.crawler.mobile")
public class IpApiService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private IpCityCodeRepository ipCityCodeRepository;
	@Autowired
    private AwsApiClient awsApiClient;
	private HttpProxyRes httpProxyRes1 = null;
//	@Autowired
//    private HttpProxyClient httpProxyClient;
//	private HttpProxyRes httpProxyRes = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String jgCity = "";
	@Value("${url}")
    String urlPath;
//	//获取代理IP、端口
//    public HttpProxyRes getProxyClient(String num, String pro,String useCache){
//    	httpProxyRes = httpProxyClient.getProxy(num,pro,useCache);
//        return httpProxyRes;
//    }
	
	//获取代理IP、端口
	public HttpProxyRes getProxy(){
		  httpProxyRes1 = awsApiClient.getApiProxy();
	      return httpProxyRes1;
	} 
	public HttpProxyRes reliableShowji(String num) {
		tracer.addTag("ShowjiResult", "reliableShowji");
		return null;
	}
	
	//调用aws代理ip
	@HystrixCommand(fallbackMethod = "reliableShowji")
	public HttpProxyRes getAwsIp(String num){
		httpProxyRes1 = getProxy();
		int n = Integer.parseInt(num);
		List<HttpProxyBean> proxyBeans = new ArrayList<HttpProxyBean>();
		for(int i = 0; i<n ;i++){
			HttpProxyBean proxyBean = new HttpProxyBean();
			if(i==0){
				proxyBean.setIp(httpProxyRes1.getIp());
				proxyBean.setPort(httpProxyRes1.getPort());
			}else{
				proxyBean.setIp(httpProxyRes1.getHttpProxyBeanSet().get(i).getIp());
				proxyBean.setPort(httpProxyRes1.getHttpProxyBeanSet().get(i).getPort());	
			}
			
			proxyBean.setName("");
			proxyBean.setInstanceId("");
			proxyBeans.add(proxyBean);
		}
		
		HttpProxyRes httpProxyRes = new HttpProxyRes();
		httpProxyRes.setCity(jgCity);
		httpProxyRes.setHttpProxyBeanSet(proxyBeans);
		httpProxyRes.setIp(httpProxyRes1.getIp());
		httpProxyRes.setPort(httpProxyRes1.getPort());
		httpProxyRes.setTotalnum(Integer.parseInt(num));
		httpProxyRes.setErrornum(0);
		httpProxyRes.setInstanceId("");
		httpProxyRes.setName("");
		httpProxyRes.setUpdateTime(sdf.format(new Date()));
		httpProxyRes.setResult(true);
		httpProxyRes.setMessage("SUCCESS");
		return httpProxyRes;
		
	}
	
	public HttpProxyRes getIpExamination(HttpProxyRes httpProxyRes,String num, String pro,String useCache){
//		try {
//			httpProxyRes = getProxyClient("2","","");
//			
//        } catch (Exception ex) {
//            System.out.println("获取代理IP、端口出错。");
//        }
		String url = urlPath;
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		webClient.getOptions().setJavaScriptEnabled(false);
//		if (httpProxyRes != null) {
		for(int i = 0;i < httpProxyRes.getHttpProxyBeanSet().size();i++){
			ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
			System.err.println("代理："+httpProxyRes.getHttpProxyBeanSet().get(i).getIp()+":"+Integer.parseInt(httpProxyRes.getHttpProxyBeanSet().get(i).getPort()));
			proxyConfig.setProxyHost(httpProxyRes.getHttpProxyBeanSet().get(i).getIp()); 
			proxyConfig.setProxyPort(Integer.parseInt(httpProxyRes.getHttpProxyBeanSet().get(i).getPort()));
			try {
				//
				//        }
				WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
				HtmlPage searchPage1 = webClient.getPage(webRequest);
				String html = searchPage1.getWebResponse().getContentAsString();
//				System.err.println(html);
//				if (html.contains("请输入登录密码")) {
//					System.err.println("代理ip可用");
//				} 
				System.err.println("代理ip可用");
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("代理ip不可用");
				delete(num,pro);
				httpProxyRes = getIP(num,pro,useCache);
//				if(httpProxyRes.getHttpProxyBeanSet().size()==1){
//					delete(num,pro);
//					getIP(num,pro,useCache);
//				}else{
//					httpProxyRes.getHttpProxyBeanSet().remove(i);
//				}
			}
		}
		
		return httpProxyRes;
		
	}
	
	@CacheEvict(value="mycache",key = "#num+#pro")
	public String delete(String num,String pro) {
		System.out.println("删除成功");
		return "删除成功";
	}
	
	//调用极光代理ip
	@Cacheable(value ="mycache", key = "#num+#pro",unless="#useCache eq 'false'",condition="#httpProxyRes==null")
	public HttpProxyRes getIP(String num, String pro,String useCache) {
		tracer.addTag("获取极光IP开始", "数量："+num+" ;省份："+pro);
		System.out.println("aaa");
		//参数错误，num为空的情况
		if(StringUtils.isBlank(num)){			
			HttpProxyRes httpProxyRes = getHttpProxyRes(num,false,"参数有误，num不能为0！",null);
			return  httpProxyRes;
		}
		//省份及城市为空的情况  （省份为空，城市必为空）
		if(StringUtils.isBlank(pro)){
			HttpProxyRes httpProxyRes = getIpByNumAndPro(num,"");
			return httpProxyRes;
		}
		//省份不为空的情况
		if(StringUtils.isNoneBlank(pro)){
			HttpProxyRes httpProxyRes = getIpByNumAndPro(num,pro);
			return httpProxyRes;
		}

		HttpProxyRes httpProxyRes = getHttpProxyRes(num,false,"未知错误，未能返回IP！",null);
		
		return  httpProxyRes;
	}


	private HttpProxyRes getHttpProxyRes(String num, boolean result, 
			String message, List<HttpProxyBean> proxyBeans) {
		HttpProxyRes httpProxyRes = new HttpProxyRes();
		if(!result){
			httpProxyRes.setCity("");
			httpProxyRes.setHttpProxyBeanSet(null);
			httpProxyRes.setIp("");
			httpProxyRes.setPort("");
			httpProxyRes.setTotalnum(0);
			httpProxyRes.setErrornum(0);
			httpProxyRes.setInstanceId("");
			httpProxyRes.setName("");
			httpProxyRes.setUpdateTime(sdf.format(new Date()));
			httpProxyRes.setResult(false);
			httpProxyRes.setMessage(message);
			return  null;
		}else{
			int random = (int) ( Math.random () * proxyBeans.size() ); 
			HttpProxyBean hpb = proxyBeans.get(random); 
			httpProxyRes.setCity(jgCity);
			httpProxyRes.setHttpProxyBeanSet(proxyBeans);
			httpProxyRes.setIp(hpb.getIp());
			httpProxyRes.setPort(hpb.getPort());
			httpProxyRes.setTotalnum(proxyBeans.size());
			httpProxyRes.setErrornum(0);
			httpProxyRes.setInstanceId("");
			httpProxyRes.setName("");
			httpProxyRes.setUpdateTime(sdf.format(new Date()));
			httpProxyRes.setResult(result);
			httpProxyRes.setMessage(message);
			return httpProxyRes;
		}
	}

	/**
	 * 通过获取数量取得IP
	 * @param num
	 * @return
	 */
	private HttpProxyRes getIpByNumAndPro(String num, String pro) {
		
		pro = getCityCode(pro);
		String url = "http://d.jghttp.golangapi.com/getip?num="+num+"&type=2&"
				+ "pro="+pro+"&city=0&yys=0&port=1&time=1&"
				+ "ts=1&ys=1&cs=1&lb=1&sb=0&pb=4&mr=0&regions=";
		String result = getIPByWebClient(url);
		if(null != result){
			List<HttpProxyBean> proxyBeans = getProxyBeans(result);
			if(null == proxyBeans){
				HttpProxyRes httpProxyRes = getHttpProxyRes(num,false,"解析极光IP出错！",null);
				return httpProxyRes;
			}else{
				HttpProxyRes httpProxyRes = getHttpProxyRes(num,true,"SUCCESS",proxyBeans);
				return httpProxyRes;
			}
		}else{
			HttpProxyRes httpProxyRes = getHttpProxyRes(num,false,"获取极光IP接口出错",null);
			return httpProxyRes;
		}
	}
	
	/**
	 * 格式转换
	 * @param result
	 * @return
	 */
	private List<HttpProxyBean> getProxyBeans(String result){
		try{
			IpResponseBean ipResponseBean = new Gson().fromJson(result, IpResponseBean.class);
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
			jgCity = list.get(0).getCity();
			return proxyBeans; 
		}catch(Exception e){
			tracer.addTag("解析json出错", e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * 访问极光IP请求接口获取IP
	 * @param url
	 * @return
	 */
	public String getIPByWebClient(String url) {
		WebClient webClient = WebCrawler.getInstance().getWebClient();
		WebRequest request;
		String html = null;
		try {
			request = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage page = webClient.getPage(request);
			html = page.getWebResponse().getContentAsString();
			System.out.println(html);
		} catch (Exception e) {
			tracer.addTag("通过极光接口获取IP出错", e.getMessage());
		}
		return html; 
	}
	
	private String getCityCode(String region){
		List<IpCityCode> ipCityCodes = ipCityCodeRepository.findByRegion(region);
		if(null != ipCityCodes && ipCityCodes.size()>0){
			return ipCityCodes.get(0).getRegionId();			
		}else{
			return "";
		}
	}

}
