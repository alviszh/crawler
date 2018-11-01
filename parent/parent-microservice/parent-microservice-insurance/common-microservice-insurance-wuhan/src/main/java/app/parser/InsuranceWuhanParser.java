package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.wuhan.InsuranceWuhanAllInfo;
import com.microservice.dao.entity.crawler.insurance.wuhan.InsuranceWuhanPersonalInfo;
import com.microservice.dao.entity.crawler.insurance.wuhan.InsuranceWuhanUserInfo;
import com.module.ocr.utils.ChaoJiYingUtils;

import app.commontracerlog.TracerLog;
import app.domain.WebParam;
import app.service.InsuranceService;


@Component
public class InsuranceWuhanParser {
	
	public static final Logger log = LoggerFactory.getLogger(InsuranceWuhanParser.class);
	
//	@Autowired
//	private ChaoJiYingOcrService chaoJiYingOcrService;
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private TracerLog tracer;

	static CookieStore cookieStore = new BasicCookieStore();  
	
	private final String LEN_MIN = "0";
	private final String TIME_ADD = "0";
	private final String STR_DEBUG = "a";
	private final String OCR_FILE_PATH = "/home/img";
	private String uuid = UUID.randomUUID().toString();
	private final String USERNAME = "harmonycredit"; // "md19841002";
	private final String PASSWORD = "HcPcPt20160919"; // "xhhcxy168";
																// //"12qwaszx";
	private final String SOFTID = "892052"; // "891477";

	public String getVerifycodeByChaoJiYing(String codetype, String len_min, String time_add, String str_debug,
			String filePath) {
		return ChaoJiYingUtils.PostPic(USERNAME, PASSWORD, SOFTID, codetype, len_min, time_add, str_debug, filePath);
	}
	
	
	/**
	 * 初始化登录页
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static CloseableHttpClient initLogin() throws ClientProtocolException, IOException{ 
		CloseableHttpClient closeableHttpClient =  getCloseableHttpClient();  
		CloseableHttpResponse closeableHttpResponse = null;  
		HttpGet get = new HttpGet("https://221.232.64.242:7022/grws/login.jsp"); 
		
		RequestConfig requestConfig = RequestConfig.custom()    
		        .setConnectTimeout(50000).setConnectionRequestTimeout(10000)    
		        .setSocketTimeout(50000).build();    
		get.setConfig(requestConfig);  
		
		if (closeableHttpClient != null) {
			closeableHttpResponse = closeableHttpClient.execute(get);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for(Header header:headers){
//				System.out.println(header.toString());
				/*if(header.getName().equals("JSESSIONID")){
					JSESSIONID = header.getValue();
				}*/
			}
			
			HttpEntity entity = closeableHttpResponse.getEntity();
			if (entity != null) {  
//	   			String body = EntityUtils.toString(entity, "utf-8");  
//	   			System.out.println("***********************************************initLogin");
//	   			System.out.println(body);
	   		}
		}
		
		
		return closeableHttpClient;
	}
	

	/**
	 *https安全证书build 
	 * @return
	 */
	public static CloseableHttpClient getCloseableHttpClient() {
		HttpClient client = new HttpClient();
		CloseableHttpClient closeableHttpClient = null;
		CloseableHttpResponse closeableHttpResponse = null; 
		TrustStrategy trustStrategy = null;
		SSLContext sslContext = null;
		X509HostnameVerifier x509HostnameVerifier = null;
		LayeredConnectionSocketFactory sslConnectionSocketFactory = null;
		Registry<ConnectionSocketFactory> registry = null;
		PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;
		RequestConfig requestConfig = null;  
		trustStrategy = new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] xcs, String authType) throws CertificateException {
				return true;
			}
		}; 
		try {
			sslContext = SSLContexts.custom().useSSL().loadTrustMaterial(null, trustStrategy)
					.setSecureRandom(new SecureRandom()).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) { 
			e.printStackTrace();
		} 
		x509HostnameVerifier = new X509HostnameVerifier() {
			@Override
			public void verify(String host, SSLSocket ssl) throws IOException {
				// do nothing
			} 
			@Override
			public void verify(String host, X509Certificate cert) throws SSLException {
				// do nothing //do nothing
			} 
			@Override
			public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
				// do nothing
			} 
			@Override
			public boolean verify(String string, SSLSession ssls) {
				return true;
			}
		}; 
		sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,x509HostnameVerifier); 
		registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", sslConnectionSocketFactory).build(); 
		poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(registry); 
		requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
		closeableHttpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig)
				.setSslcontext(sslContext).setHostnameVerifier(x509HostnameVerifier)
				.setSSLSocketFactory(sslConnectionSocketFactory)
				.setConnectionManager(poolingHttpClientConnectionManager).setDefaultCookieStore(cookieStore).build();
		return closeableHttpClient;
	}
	/**
	 * 超级鹰ocr解析验证码
	 * @param imgPath
	 * @param codeType
	 * @return
	 */
	public String callChaoJiYingService(String imgPath, String codeType) {
		Gson gson = new GsonBuilder().create();
	
		String chaoJiYingResult = getVerifycodeByChaoJiYing(codeType, LEN_MIN, TIME_ADD, STR_DEBUG, imgPath);
	
		String errNo = ChaoJiYingUtils.getErrNo(chaoJiYingResult);
		if (!ChaoJiYingUtils.RESULT_SUCCESS.equals(errNo)) {
			// tracer.addTag("errNo ======>>",errNo);
			return ChaoJiYingUtils.getErrMsg(errNo);
		}
	
		 tracer.addTag("ChaoJiYingResult [CODETYPE={}]: {}",
		 chaoJiYingResult);
	
		return (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
	
	}
	
	/**
	 * 将网页图片验证码保存到本地
	 * @param closeableHttpClient
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String getCode(CloseableHttpClient closeableHttpClient) throws ClientProtocolException, IOException{
		
		CloseableHttpResponse closeableHttpResponse = null; 
		String code = "";
		HttpGet get = new HttpGet("https://221.232.64.242:7022/grws/number.jsp"); 
		if (closeableHttpClient != null) {
			closeableHttpResponse = closeableHttpClient.execute(get);
			Header[] headers = closeableHttpResponse.getAllHeaders();
			for(Header header:headers){
//				System.out.println(header.toString());
			}
			File parentDirFile = new File(OCR_FILE_PATH);
			parentDirFile.setReadable(true); //
			parentDirFile.setWritable(true); //
			
			if (!parentDirFile.exists()) {
				parentDirFile.mkdirs();
			}
			String imageName = uuid + ".jpg";
			File codeImageFile = new File(OCR_FILE_PATH + "/" + imageName);
			FileOutputStream output = new FileOutputStream(codeImageFile);
			byte[] b = new byte[1024]; 
			InputStream is = closeableHttpResponse.getEntity().getContent();
			while ((is.read(b)) != -1) { 
				output.write(b); 
			} 
			is.close(); 
			output.close(); 
			code = callChaoJiYingService(codeImageFile.getAbsolutePath(), "4004");

		}
		
		return code;
	}
	
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	
	//登录入口
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		WebParam webParam= new WebParam();
		CloseableHttpClient closeableHttpClient = initLogin();
		String code = getCode(closeableHttpClient);
		webParam = loginParse(code,insuranceRequestParameters,closeableHttpClient);
	
		return webParam;
	}
	/**
	 * 登录解析
	 * @param code
	 * @param insuranceRequestParameters
	 * @param closeableHttpClient
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private WebParam loginParse(String code,InsuranceRequestParameters insuranceRequestParameters,CloseableHttpClient closeableHttpClient)throws ClientProtocolException, IOException {
			WebParam webParam= new WebParam();
			CloseableHttpResponse closeableHttpResponse = null; 
			String 	userName = insuranceRequestParameters.getUsername();
			String  passWord = insuranceRequestParameters.getPassword();
			
			HttpGet get = new HttpGet("https://221.232.64.242:7022/grws/dologin?j_username="+userName+"&j_password="+passWord+"&j_certCode="+code); 
			
			get.setHeader("Referer", "https://221.232.64.242:7022/grws/login.jsp");  
			get.setHeader("Host", "221.232.64.242:7022");
			get.setHeader("Content-Type", "application/x-www-form-urlencoded");
			get.setHeader("DNT", "1");
	   		
				closeableHttpResponse = closeableHttpClient.execute(get);
				Header[] headers = closeableHttpResponse.getAllHeaders();
				
				for(Header header:headers){
					
					if(header.getName().equals("Cookie")){
						webParam.setCookie(header.getValue());
					}
				
			}
				HttpEntity entity = closeableHttpResponse.getEntity();
				if (entity != null) {  
//		   			String body = EntityUtils.toString(entity, "utf-8"); 
		   			webParam.setHtml(EntityUtils.toString(entity, "utf-8"));
		   		}
				return webParam;
		}
	
	
	
	
	

	
	
	
	
	
	//爬取用户信息
	public WebParam getUserInfo(TaskInsurance taskInsurance, String cookies) throws Exception {
		WebParam webParam= new WebParam();	
		CloseableHttpResponse closeableHttpResponse = null;
		CloseableHttpClient closeableHttpClient =  getCloseableHttpClient();
		String body ="";
		String url ="https://221.232.64.242:7022/grws/parser/MainAction?menuid=grwssb_grzlcx_grcbzlcx&ActionType=grwssb_grzlcx_grcbzlcx_q";
		HttpGet get = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()    
		        .setConnectTimeout(50000).setConnectionRequestTimeout(10000)    
		        .setSocketTimeout(50000).build();    
		get.setConfig(requestConfig);  
		
		//	List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("Accept", "text/html, application/xhtml+xml, */*"));
		get.setHeader("Accept", "text/html, application/xhtml+xml, */*");
		get.setHeader("Accept-Encoding", "gzip, deflate");
		get.setHeader("Accept-Language", "zh-CN");
		get.setHeader("Cache-Control", "max-age=0");
		get.setHeader("Connection", "Keep-Alive");
		get.setHeader("Host", "221.232.64.242:7022");
		get.setHeader("Referer", "https://221.232.64.242:7022/grws/parser/MainAction?ActionType=Left");
		get.setHeader("Upgrade-Insecure-Requests", "1");
		get.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
		closeableHttpResponse = closeableHttpClient.execute(get);
		HttpEntity entity = closeableHttpResponse.getEntity();
   		if (entity != null) {  
   			body = EntityUtils.toString(entity, "utf-8");  
   			tracer.addTag("InsuranceWuhaniParser.getUserInfo 个人信息" + taskInsurance.getTaskid(),
					"<xmp>" + body + "</xmp>");
   		}
   		EntityUtils.consume(entity);
	    	
	    	//调用解析方法
	    	InsuranceWuhanUserInfo userInfo = htmlParserUserInfo(body,taskInsurance);
	    	
	    	//保存各种网页信息
	    	webParam.setWuhanUserInfo(userInfo);
	    	webParam.setUrl(url);
	    	webParam.setHtml(body);
	    	return webParam;
		
	}
	
	public static String getNextLabelByKeyword(Document document, String keyword, String tag){ 
		Elements es = document.select(tag+":contains("+keyword+")"); 
		if(null != es && es.size()>0){ 
		Element element = es.first(); 
		Element nextElement = element.nextElementSibling(); 
			if(null != nextElement){ 
				return nextElement.getElementsByAttribute("value").val(); 
			} 
		} 
		return null; 
	}
	
	
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */	
	
	//html的解析方法
	private InsuranceWuhanUserInfo htmlParserUserInfo(String html,TaskInsurance taskInsurance) {
		
		InsuranceWuhanUserInfo insuranceWuhanUserInfo = new InsuranceWuhanUserInfo();
		Document doc = Jsoup.parse(html);
		
		String sbjg =getNextLabelByKeyword(doc,"社保机构","td");
		String dwbh = getNextLabelByKeyword(doc,"单位编号","td");
		String dwmc = getNextLabelByKeyword(doc,"单位名称","td");
		String grbh = getNextLabelByKeyword(doc,"个人编号","td");
		String xm = getNextLabelByKeyword(doc,"姓名","td");
		String xb = getNextLabelByKeyword(doc,"性别","td");
		String gmsfzhm = getNextLabelByKeyword(doc,"公民身份证号码","td");
		String dacsrq = getNextLabelByKeyword(doc,"档案出生日期","td");		
		String hkxz = getNextLabelByKeyword(doc,"户口性质","td");
		String mz = getNextLabelByKeyword(doc,"民族","td");
		String whcd = getNextLabelByKeyword(doc,"文化程度","td");
		String cjgzsj = getNextLabelByKeyword(doc,"参加工作时间","td");
		String jkzk = getNextLabelByKeyword(doc,"健康状况","td");
		String hyzk = getNextLabelByKeyword(doc,"婚姻状况","td");
		String grzt = getNextLabelByKeyword(doc,"个人状态","td");
		String zglb = getNextLabelByKeyword(doc,"职工类别","td");
		String gwylb = getNextLabelByKeyword(doc,"公务员类别","td");
		String ydrybz = getNextLabelByKeyword(doc,"异地人员标志","td");
		String ltxlb = getNextLabelByKeyword(doc,"离退休类别","td");
		String ltxrq = getNextLabelByKeyword(doc,"离退休日期","td");
		String jzddz = getNextLabelByKeyword(doc,"居住地地址","td");
		String jzdyzbm = getNextLabelByKeyword(doc,"居住地邮政编码","td");
		String cbrlxdh = getNextLabelByKeyword(doc,"参保人联系电话","td");
		String cbrlxsj = getNextLabelByKeyword(doc,"参保人联系手机","td");
		
		insuranceWuhanUserInfo.setMechanism(sbjg);
		insuranceWuhanUserInfo.setCompanyNum(dwbh);
		insuranceWuhanUserInfo.setCompanyName(dwmc);
		insuranceWuhanUserInfo.setPersonalNum(grbh);
		insuranceWuhanUserInfo.setName(xm);
		insuranceWuhanUserInfo.setSex(xb);
		insuranceWuhanUserInfo.setCardNume(gmsfzhm);
		insuranceWuhanUserInfo.setBirthDate(dacsrq);
		insuranceWuhanUserInfo.setResidenceNature(hkxz);
		insuranceWuhanUserInfo.setNation(mz);
		insuranceWuhanUserInfo.setCultureDegree(whcd);;
		insuranceWuhanUserInfo.setWorkTime(cjgzsj);
		insuranceWuhanUserInfo.setHealth(jkzk);
		insuranceWuhanUserInfo.setMaritalStatus(hyzk);
		insuranceWuhanUserInfo.setPersonalStatus(grzt);
		insuranceWuhanUserInfo.setEmployeeCategory(zglb);	
		insuranceWuhanUserInfo.setCivilServiceCategory(gwylb);
		insuranceWuhanUserInfo.setRemotePersonnelFlag(ydrybz);
		insuranceWuhanUserInfo.setRetirementCategory(ltxlb);
		insuranceWuhanUserInfo.setRetirementTime(ltxrq);
		insuranceWuhanUserInfo.setAddress(jzddz);
		insuranceWuhanUserInfo.setAddressZipCode(jzdyzbm);
		insuranceWuhanUserInfo.setLandlineNum(cbrlxdh);
		insuranceWuhanUserInfo.setPhoneNum(cbrlxsj);
		insuranceWuhanUserInfo.setTaskid(taskInsurance.getTaskid());
		return insuranceWuhanUserInfo;
	}
	
	/**
	 * 获取五险信息
	 */
	public WebParam getAllInfo(TaskInsurance taskInsurance, String cookies) throws Exception {
		String  body ="";
		WebParam webParam= new WebParam();	
		CloseableHttpResponse closeableHttpResponse = null;
		CloseableHttpClient closeableHttpClient =  getCloseableHttpClient();
		String url = "https://221.232.64.242:7022/grws/parser/MainAction?menuid=grwssb_grzlcx_grjfxxcx&ActionType=grwssb_grzlcx_grjfxxcx_q";
//		String url = "https://221.232.64.242:7022/grws/jsp/common/tabledata.jsp";
		HttpPost post = new HttpPost(url);
		RequestConfig requestConfig = RequestConfig.custom()    
		        .setConnectTimeout(50000).setConnectionRequestTimeout(10000)    
		        .setSocketTimeout(50000).build();    
		post.setConfig(requestConfig);  
		List<NameValuePair> nvps = new ArrayList<NameValuePair>(); 
//		WebClient webClient = insuranceService.getWebClient(cookies);
		nvps.add(new BasicNameValuePair("id", "grwssb_grsbyw_grjfxxcx_l")); 
		nvps.add(new BasicNameValuePair("grwssb_grsbyw_grjfxxcx_l_page", "0"));
		nvps.add(new BasicNameValuePair("ActionType", "grwssb_grzlcx_grjfxxcx_q"));
		nvps.add(new BasicNameValuePair("filterOnNoDataRight", "false"));	
		nvps.add(new BasicNameValuePair("subTotalName", "%25E5%25B0%258F%25E8%25AE%25A1"));
		nvps.add(new BasicNameValuePair("display", "block"));
		nvps.add(new BasicNameValuePair("pageSize", "600")); 
		nvps.add(new BasicNameValuePair("hasPage", "true"));
		nvps.add(new BasicNameValuePair("hasTitle", "true"));
		nvps.add(new BasicNameValuePair("whereCls", "%2520G.grsxh%2520%253D%25204595465%2520and%2520DWJFBZ%2520in%2520('1'%252C'2')%2520and%2520XZLX%2520in%2520('10'%252C'20'%252C'30'%252C'40'%252C'50'%252C'53'%252C'54')%2520order%2520by%2520JFNY%2520desc%252Cxzlx"));
		nvps.add(new BasicNameValuePair("type", "q"));
		nvps.add(new BasicNameValuePair("isQuery", "true"));
		nvps.add(new BasicNameValuePair("isPageOper", "true")); 
		nvps.add(new BasicNameValuePair("useAjaxPostPars", "true"));
		nvps.add(new BasicNameValuePair("rowCnt", "338"));
		//post.setEntity(new UrlEncodedFormEntity(nvps,"gbk"));
		
		
		post.setHeader("Accept", "*/*");
		post.setHeader("Accept-Encoding", "gzip, deflate");
		post.setHeader("Accept-Language","zh-CN");
		post.setHeader("Connection","Kep-Alive");
		post.setHeader("Host","221.232.64.242:7022");
		post.setHeader("User-Agent","Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
		post.setHeader("Referer", "https://221.232.64.242:7022/grws/parser/MainAction?menuid=grwssb_grzlcx_grjfxxcx&ActionType=grwssb_grzlcx_grjfxxcx_q");
//		post.setHeader("Content-Length","695");
		post.setHeader("Content-Type","application/x-www-form-urlencoded");
		post.setHeader("Cache-Control",	"no-cache");
		//		webRequest.setAdditionalHeader("Referer", "https://221.232.64.242:7022/grws/action/MainAction?menuid=grwssb_grzlcx_grjfxxcx&ActionType=grwssb_grzlcx_grjfxxcx_q");
		
		
		List<InsuranceWuhanAllInfo> infoList = new  ArrayList<InsuranceWuhanAllInfo>();
		
		
		closeableHttpResponse = closeableHttpClient.execute(post);
		HttpEntity entity = closeableHttpResponse.getEntity();
			if (entity != null) {
				body = EntityUtils.toString(entity, "utf-8");  
				Document doc = Jsoup.parse(body);
				Elements link1 = doc.getElementsByTag("input");
				String  i = link1.get(10).val();
				int a  = Integer.parseInt(i);
				double d = a;
				double e =10;
				double b = d/e;
				int c = (int) Math.ceil(b);
				for (int i1 = 0; i1 < c; i1++) {
					String  body1 ="";
					CloseableHttpResponse closeableHttpResponse1 = null;
					CloseableHttpClient closeableHttpClient1 =  getCloseableHttpClient();
					String url1 = "https://221.232.64.242:7022/grws/parser/MainAction?menuid=grwssb_grzlcx_grjfxxcx&ActionType=grwssb_grzlcx_grjfxxcx_q&grwssb_grsbyw_grjfxxcx_l_page="+i1;
					HttpPost post1 = new HttpPost(url1);
					RequestConfig requestConfig1 = RequestConfig.custom()    
					        .setConnectTimeout(50000).setConnectionRequestTimeout(10000)    
					        .setSocketTimeout(50000).build();    
					post.setConfig(requestConfig);  
					List<NameValuePair> nvps1 = new ArrayList<NameValuePair>(); 
//					WebClient webClient = insuranceService.getWebClient(cookies);
					nvps1.add(new BasicNameValuePair("id", "grwssb_grsbyw_grjfxxcx_l")); 
					nvps1.add(new BasicNameValuePair("grwssb_grsbyw_grjfxxcx_l_page", "0"));
					nvps1.add(new BasicNameValuePair("ActionType", "grwssb_grzlcx_grjfxxcx_q"));
					nvps1.add(new BasicNameValuePair("filterOnNoDataRight", "false"));	
					nvps1.add(new BasicNameValuePair("subTotalName", "%25E5%25B0%258F%25E8%25AE%25A1"));
					nvps1.add(new BasicNameValuePair("display", "block"));
					nvps1.add(new BasicNameValuePair("pageSize", "600")); 
					nvps1.add(new BasicNameValuePair("hasPage", "true"));
					nvps1.add(new BasicNameValuePair("hasTitle", "true"));
					nvps1.add(new BasicNameValuePair("whereCls", "%2520G.grsxh%2520%253D%25204595465%2520and%2520DWJFBZ%2520in%2520('1'%252C'2')%2520and%2520XZLX%2520in%2520('10'%252C'20'%252C'30'%252C'40'%252C'50'%252C'53'%252C'54')%2520order%2520by%2520JFNY%2520desc%252Cxzlx"));
					nvps1.add(new BasicNameValuePair("type", "q"));
					nvps1.add(new BasicNameValuePair("isQuery", "true"));
					nvps1.add(new BasicNameValuePair("isPageOper", "true")); 
					nvps1.add(new BasicNameValuePair("useAjaxPostPars", "true"));
					nvps1.add(new BasicNameValuePair("rowCnt", "338"));
					//post.setEntity(new UrlEncodedFormEntity(nvps,"gbk"));
					
					
					post1.setHeader("Accept", "*/*");
					post1.setHeader("Accept-Encoding", "gzip, deflate");
					post1.setHeader("Accept-Language","zh-CN");
					post1.setHeader("Connection","Kep-Alive");
					post1.setHeader("Host","221.232.64.242:7022");
					post1.setHeader("User-Agent","Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
					post1.setHeader("Referer", "https://221.232.64.242:7022/grws/parser/MainAction?menuid=grwssb_grzlcx_grjfxxcx&ActionType=grwssb_grzlcx_grjfxxcx_q");
//					post1.setHeader("Content-Length","695");
					post1.setHeader("Content-Type","application/x-www-form-urlencoded");
					post1.setHeader("Cache-Control",	"no-cache");
					closeableHttpResponse = closeableHttpClient.execute(post1);
					HttpEntity entity1 = new HttpEntity() {
						
						@Override
						public void writeTo(OutputStream outstream) throws IOException {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public boolean isStreaming() {
							// TODO Auto-generated method stub
							return false;
						}
						
						@Override
						public boolean isRepeatable() {
							// TODO Auto-generated method stub
							return false;
						}
						
						@Override
						public boolean isChunked() {
							// TODO Auto-generated method stub
							return false;
						}
						
						@Override
						public Header getContentType() {
							// TODO Auto-generated method stub
							return null;
						}
						
						@Override
						public long getContentLength() {
							// TODO Auto-generated method stub
							return 0;
						}
						
						@Override
						public Header getContentEncoding() {
							// TODO Auto-generated method stub
							return null;
						}
						
						@Override
						public InputStream getContent() throws IOException, UnsupportedOperationException {
							// TODO Auto-generated method stub
							return null;
						}
						
						@Override
						public void consumeContent() throws IOException {
							// TODO Auto-generated method stub
							
						}
					}; 
					entity1 =closeableHttpResponse.getEntity();
					if (entity1 != null) {  
						body1 = EntityUtils.toString(entity1, "utf-8");
						EntityUtils.consume(entity1);
						tracer.addTag("InsuranceWuhanParser.getAllInfo 五险信息" + taskInsurance.getTaskid(),
								"<xmp>" + body1 + "</xmp>");
						infoList= htmlParseAll(taskInsurance,infoList,body1);
						if(null != infoList){
							webParam.setList(infoList);
						}
						webParam.setHtml(body1);
					}
				}
			}
			return webParam;
	}
	
	private List<InsuranceWuhanAllInfo> htmlParseAll(TaskInsurance taskInsurance,List<InsuranceWuhanAllInfo> infoList, String html) {
		Document doc = Jsoup.parse(html);
		Elements link1 = doc.getElementsByTag("tr");
		for (int i = 5; i < link1.size(); i++) {
			Elements link2 = link1.get(i).getElementsByTag("td");
			if(link2.size()==15){
				InsuranceWuhanAllInfo aInfo = new InsuranceWuhanAllInfo();
				aInfo.setType(link2.get(0).text());
				aInfo.setPayTime(link2.get(1).text());
				aInfo.setLedger(link2.get(2).text());
				aInfo.setPayType(link2.get(3).text());
				aInfo.setPayMode(link2.get(4).text());
				aInfo.setPayBase(link2.get(5).text());
				aInfo.setPayMoney(link2.get(6).text());
				aInfo.setPersonalPayMoney(link2.get(7).text());
				aInfo.setPersonalAccountMoney(link2.get(8).text());
				aInfo.setIntegratedPlanningMoney(link2.get(9).text());
				aInfo.setBackInterest(link2.get(10).text());
				aInfo.setLateFee(link2.get(11).text());
				aInfo.setPayFlag(link2.get(12).text());
				aInfo.setCompanyNum(link2.get(13).text());
				aInfo.setCompanyName(link2.get(14).text());
				infoList.add(aInfo);
			}	
		}
		return infoList;
	}

	/**
	 * @Des 个人参保信息
	 * @return
	 */
	public WebParam getPersonalInfo(TaskInsurance taskInsurance, String cookies) throws Exception{
		String  body ="";
		WebParam webParam= new WebParam();	
		CloseableHttpResponse closeableHttpResponse = null;
		CloseableHttpClient closeableHttpClient =  getCloseableHttpClient();
		
		CookieStore cookieStore =  new BasicCookieStore();
		List<Cookie> cookie = cookieStore.getCookies(); 
		String url = "https://221.232.64.242:7022/grws/parser/MainAction?menuid=grwssb_grzlcx_grcbzlcx&ActionType=grwssb_grzlcx_grcbzlcx_q";
		HttpGet get = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()    
		        .setConnectTimeout(50000).setConnectionRequestTimeout(10000)    
		        .setSocketTimeout(50000).build();    
		get.setConfig(requestConfig);  
		get.setHeader("Accept","text/html, application/xhtml+xml, */*");
		get.setHeader("Accept-Encoding","gzip, deflate");
		get.setHeader("Accept-Language","zh-CN");
		get.setHeader("Connection","keep-alive");
		get.setHeader("Host","221.232.64.242:7022");
		get.setHeader("User-Agent","Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
		get.setHeader("Referer", "https://221.232.64.242:7022/grws/parser/MainAction?ActionType=Left");
		get.setHeader("Origin","http://szsbzx.jsszhrss.gov.cn:9900");
		List<InsuranceWuhanPersonalInfo> infoList  = new ArrayList<InsuranceWuhanPersonalInfo>();
		closeableHttpResponse = closeableHttpClient.execute(get);
		HttpEntity entity = closeableHttpResponse.getEntity();
		if (null != entity) {  
			body = EntityUtils.toString(entity, "utf-8");  
		}
				EntityUtils.consume(entity);
				tracer.addTag("InsuranceWuhanParser.getPersonalInfo 个人参保信息" + taskInsurance.getTaskid(),
						"<xmp>" + body + "</xmp>");
				infoList= htmlParsePersonal(taskInsurance,infoList,body);
				if(null != infoList){
					webParam.setList(infoList);
				}
				webParam.setHtml(body);
			return webParam;
			
	}
	
	private List<InsuranceWuhanPersonalInfo> htmlParsePersonal(TaskInsurance taskInsurance,List<InsuranceWuhanPersonalInfo> infoList, String html) {
			
			Document doc = Jsoup.parse(html);
			Elements link1 = doc.getElementsByTag("tr");
			for (int i = 13; i < link1.size(); i++) {
				
//			}
//			for (Element element : link1) {
//				
				Elements link2 = link1.get(i).getElementsByTag("td");
				if (link2.size() ==4) {
					InsuranceWuhanPersonalInfo pInfo = new InsuranceWuhanPersonalInfo();
					pInfo.setType(link2.get(0).text());
					pInfo.setPersonalInsuranceStatus(link2.get(1).text());
					pInfo.setPersonalFirstTime(link2.get(2).text());
					pInfo.setPayMonths(link2.get(3).text());
					pInfo.setTaskid(taskInsurance.getTaskid());
					infoList.add(pInfo);
				}
			}
		return infoList;
	}

	

	
}
