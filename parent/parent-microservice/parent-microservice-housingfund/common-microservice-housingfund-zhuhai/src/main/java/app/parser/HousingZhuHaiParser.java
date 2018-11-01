package app.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.zhuhai.HousingZhuHaiPay;
import com.microservice.dao.entity.crawler.housing.zhuhai.HousingZhuHaiUserinfo;
import com.module.htmlunit.WebCrawler;

import app.common.Base64Util;
import app.common.FileUtil;
import app.common.HttpUtil;
import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;

@Component
public class HousingZhuHaiParser {
	
	@Value("${filesavepath}")
	String fileSavePath;
	
	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) throws Exception{
		tracer.addTag("housingFund.login.parser.login.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String loginUrl = "http://wap.zhgjj.org.cn/wt-web/grlogin";
		webParam.setUrl(loginUrl);
		tracer.addTag("housingFund.login.parser.login.loginUrl", loginUrl);
		WebRequest webRequest = new WebRequest(new URL(loginUrl), HttpMethod.GET);
		HtmlPage loginPage = webClient.getPage(webRequest);
		tracer.addTag("housingFund.login.parser.login.loginPage", "<xmp>"+loginPage.asXml()+"</xmp>");
		webParam.setPage(loginPage);
		HtmlTextInput username = loginPage.getFirstByXPath("//input[@id='username']");
		if(null != username){
			HtmlTextInput zjhm = loginPage.getFirstByXPath("//input[@id='zjhm']");
			HtmlTextInput captcha = loginPage.getFirstByXPath("//input[@id='captcha']");
			HtmlTextInput ssyzm = loginPage.getFirstByXPath("//input[@id='ssyzm']");
			HtmlImage img = loginPage.getFirstByXPath("//img[@id='captcha_img']");
			HtmlButtonInput login = loginPage.getFirstByXPath("//input[@id='gr_login']");
			
			username.setText(messageLoginForHousing.getTelephone());
			zjhm.setText(messageLoginForHousing.getNum());
			String imgcode = chaoJiYingOcrService.getVerifycode(img, "1902");
			captcha.setText(imgcode);
			//绕过了短信验证，所以这一栏随便填写
			ssyzm.setText("121212");
			HtmlPage loginedPage = login.click();
			webParam.setPage(loginedPage);
			tracer.addTag("housingFund.login.parser.login.loginedPage", loginedPage.getWebResponse().getContentAsString());
			if(!loginedPage.asXml().contains("今日登陆次数已达上限")){
				String url = "http://wap.zhgjj.org.cn/wt-web/grlogintwo?force_and_dxyz=1&grloginDxyz=0&username="+messageLoginForHousing.getTelephone()+"&zjhm="+messageLoginForHousing.getNum()+"&password=111111&force=&captcha="+imgcode+"&sliderCaptcha=&ssyzm=121212";
				webParam.setUrl(url);
				tracer.addTag("housingFund.login.parser.login.url", url);
				webRequest = new WebRequest(new URL(url), HttpMethod.POST);
				Page loginedPage2 = webClient.getPage(webRequest);
				webParam.setPage(loginedPage2);
				tracer.addTag("housingFund.login.parser.login.loginedPage2", loginedPage2.getWebResponse().getContentAsString());
				
				String url2 = "http://wap.zhgjj.org.cn/wt-web/home?logintype=1";
				webParam.setUrl(url2);
				tracer.addTag("housingFund.login.parser.login.url2", url2);
				webRequest = new WebRequest(new URL(url2), HttpMethod.GET);
				Page loginedPage3 = webClient.getPage(webRequest);
				webParam.setPage(loginedPage3);
				tracer.addTag("housingFund.login.parser.login.loginedPage3", loginedPage3.getWebResponse().getContentAsString());
				if(loginedPage3.getWebResponse().getContentAsString().contains("var grzh='")){
					String content = loginedPage3.getWebResponse().getContentAsString();
					int i = content.indexOf("var grzh='");
					int j = content.indexOf("';", i);
					String grzh = content.substring(i+10, j);
					if(null != grzh && !grzh.equals("")){
						webParam.setParam(grzh);
						webParam.setWebClient(webClient);
					}
				}else{
					webParam.setHtml("登陆失败，用户名无效！");
				}
			}else{
				webParam.setHtml("登陆失败，今日登陆次数已达上限！");
			}
		}else{
			webParam.setHtml("网络异常，请您稍后重试！");
		}
		
		return webParam;
	}

	public WebParam getUserInfo(TaskHousing taskHousing) throws Exception{
		tracer.addTag("housingFund.crawler.parser.getUserInfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		String userUrl = "http://wap.zhgjj.org.cn/wt-web/jcr/jcrkhxxcx_mh.service";
		String reqBody = "grxx=grbh&ffbm=01&ywfl=01&ywlb=99&cxlx=01";
		webParam.setUrl(userUrl+"?"+reqBody);
		tracer.addTag("housingFund.crawler.parser.getUserInfo.url", userUrl+"?"+reqBody);
		WebRequest webRequest = new WebRequest(new URL(userUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("Host", "wap.zhgjj.org.cn");
		webRequest.setAdditionalHeader("Origin", "http://wap.zhgjj.org.cn");
		webRequest.setAdditionalHeader("Referer", "http://wap.zhgjj.org.cn/wt-web/home?logintype=1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		webRequest.setRequestBody(reqBody);
		Page userPage = webClient.getPage(webRequest);
		webParam.setPage(userPage);
		tracer.addTag("housingFund.crawler.parser.getUserInfo.userPage", userPage.getWebResponse().getContentAsString());
		if(userPage.getWebResponse().getContentAsString().contains("success")){
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(userPage.getWebResponse().getContentAsString());
			String success = object.get("success").getAsString();
			if(success.contains("true")){
				JsonObject data = object.get("data").getAsJsonObject();
				String name = data.get("xingming").getAsString();
				String cardType = data.get("zjlx").getAsString();
				String idNum = data.get("zjhm").getAsString();
				String birthday = data.get("csny").getAsString();
				String gender = data.get("xingbie").getAsString();
				String isMarry = data.get("hyzk").getAsString();
				String postNum = data.get("yzbm").getAsString();
				String telNum = data.get("gddhhm").getAsString();
				String familyMonthIncome = data.get("jtysr").getAsString();
				String address = data.get("jtzz").getAsString();
				String phoneNum = data.get("sjhm").getAsString();
				String accountNum = data.get("grzh").getAsString();
				String accountStatus = data.get("grzhzt").getAsString();
				String accountBalance = data.get("grzhye").getAsString();
				String openDate = data.get("djrq").getAsString();
				String companyName = data.get("dwmc").getAsString();
				String loanStatus = data.get("sfyzfdk").getAsString();
				String payPercent = data.get("jcbl").getAsString();
				String payBase = data.get("grjcjs").getAsString();
				String monthPay = data.get("yjce").getAsString();
				String bankName = data.get("grckzhkhyhmc").getAsString();
				String bankNum = data.get("grckzhhm").getAsString();
				
				List<HousingZhuHaiUserinfo> userinfos = new ArrayList<HousingZhuHaiUserinfo>();
				HousingZhuHaiUserinfo userinfo = new HousingZhuHaiUserinfo();
				userinfo.setTaskid(taskHousing.getTaskid());
				userinfo.setName(name);
				userinfo.setCardType(cardType);
				userinfo.setIdNum(idNum);
				userinfo.setBirthday(birthday);
				userinfo.setGender(gender);
				userinfo.setIsMarry(isMarry);
				userinfo.setPostNum(postNum);
				userinfo.setTelNum(telNum);
				userinfo.setFamilyMonthIncome(familyMonthIncome);
				userinfo.setAddress(address);
				userinfo.setPhoneNum(phoneNum);
				userinfo.setAccountNum(accountNum);
				userinfo.setAccountStatus(accountStatus);
				userinfo.setAccountBalance(accountBalance);
				userinfo.setOpenDate(openDate);
				userinfo.setCompanyName(companyName);
				userinfo.setLoanStatus(loanStatus);
				userinfo.setPayPercent(payPercent);
				userinfo.setPayBase(payBase);
				userinfo.setMonthPay(monthPay);
				userinfo.setBankName(bankName);
				userinfo.setBankNum(bankNum);
				userinfos.add(userinfo);
				webParam.setList(userinfos);
			}
		}
		return webParam;
	}

	public WebParam getPayInfo(TaskHousing taskHousing) throws Exception{
		tracer.addTag("housingFund.crawler.parser.getPayInfo.taskid", taskHousing.getTaskid());
		WebParam webParam = new WebParam();
		WebClient webClient = taskHousing.getClient(taskHousing.getCookies());
		String payUrl = "http://wap.zhgjj.org.cn/wt-web/jcr/jcrxxcxzhmxcx_.service?dwzh=&ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2010-01-01&jsrq="+getDate("yyyy-MM-dd")+"&grxx="+taskHousing.getPassword()+"&fontSize=13px&pageNum=1&pageSize=100&totalcount=&pages=&random=0.7840922261826775";
		webParam.setUrl(payUrl);
		tracer.addTag("housingFund.crawler.parser.getPayInfo.payUrl", payUrl);
		WebRequest webRequest = new WebRequest(new URL(payUrl), HttpMethod.POST);
		webRequest.setAdditionalHeader("Host", "wap.zhgjj.org.cn");
		webRequest.setAdditionalHeader("Origin", "http://wap.zhgjj.org.cn");
		webRequest.setAdditionalHeader("Referer", "http://wap.zhgjj.org.cn/wt-web/home?logintype=1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		webParam.setPage(page);
		tracer.addTag("housingFund.crawler.parser.getPayInfo.page", page.getWebResponse().getContentAsString());
		if(page.getWebResponse().getContentAsString().contains("totalcount")){
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(page.getWebResponse().getContentAsString());
			int totalcount = obj.get("totalcount").getAsInt();
			if(totalcount > 0){
				List<HousingZhuHaiPay> pays = new ArrayList<HousingZhuHaiPay>();
				int pages = totalcount%8 == 0 ? totalcount/8 : (totalcount/8)+1;
				for (int i = 1; i < pages+1; i++) {
					pays = getPayByBaiDuApi(taskHousing, webClient, pays, i);
				}
				webParam.setList(pays);
			}
		}
		return webParam;
	}
	
	public List<HousingZhuHaiPay> getPayByBaiDuApi(TaskHousing taskHousing, WebClient webClient, List<HousingZhuHaiPay> pays, int i) throws Exception{
		String payUrl = "http://wap.zhgjj.org.cn/wt-web/jcr/jcrxxcxzhmxcx_.service?dwzh=&ffbm=01&ywfl=01&ywlb=99&blqd=wt_02&ksrq=2010-01-01&jsrq="+getDate("yyyy-MM-dd")+"&grxx="+taskHousing.getPassword()+"&fontSize=13px&pageNum="+i+"&pageSize=8&totalcount=&pages=&random=0.7840922261826775";
		tracer.addTag("housingFund.crawler.parser.getPayByBaiDuApi.url."+i, payUrl);
		WebRequest webRequest = new WebRequest(new URL(payUrl), HttpMethod.GET);
		webRequest.setAdditionalHeader("Host", "wap.zhgjj.org.cn");
		webRequest.setAdditionalHeader("Origin", "http://wap.zhgjj.org.cn");
		webRequest.setAdditionalHeader("Referer", "http://wap.zhgjj.org.cn/wt-web/home?logintype=1");
		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
		Page page = webClient.getPage(webRequest);
		//获取 百度API token
		String token = getAuth();
		// 通用识别urlconnection.setRequestProperty("templateSign", "ea3ae8a5c4145067eb987eb669c336b2");
		String otherHost = "https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise";
		tracer.addTag("housingFund.crawler.parser.getPayByBaiDuApi.otherHost."+i, otherHost);
		// 本地图片路径
		String filePath = getImagePath(page);
		tracer.addTag("housingFund.crawler.parser.getPayByBaiDuApi.filePath."+i, filePath);
		byte[] imgData = FileUtil.readFileByBytes(filePath);
		String imgStr = Base64Util.encode(imgData);
		String param = "image="+URLEncoder.encode(imgStr,"UTF-8")+"&templateSign=88718ba27c9519469d798b5f32221f7f";
		/**
		 * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取,30天
		 */
		String result = HttpUtil.post(otherHost, token, param);
		tracer.addTag("housingFund.crawler.parser.getPayByBaiDuApi.result."+i, result);
		//处理数据
		pays = parseJson(taskHousing, pays, result);
		return pays;
	}
	
	public List<HousingZhuHaiPay> parseJson(TaskHousing taskHousing, List<HousingZhuHaiPay> pays, String json) throws Exception{
		String taskid = taskHousing.getTaskid();
		if(json.contains("data")){
			JsonParser parser = new JsonParser();
			JsonObject obj = (JsonObject) parser.parse(json);
			JsonObject data = obj.get("data").getAsJsonObject();
			if(null != data){
				JsonArray ret = data.get("ret").getAsJsonArray();
				if(null != ret && ret.size() > 0){
					for (int i = 0; i < (ret.size()/6); i++) {
						String payDate = ret.get(0+6*i).getAsJsonObject().get("word").getAsString();
						String payType = ret.get(1+6*i).getAsJsonObject().get("word").getAsString();
						String fee = ret.get(2+6*i).getAsJsonObject().get("word").getAsString();
						String interest = ret.get(3+6*i).getAsJsonObject().get("word").getAsString();
						String getReason = ret.get(4+6*i).getAsJsonObject().get("word").getAsString();
						String getType = ret.get(5+6*i).getAsJsonObject().get("word").getAsString();
						
						HousingZhuHaiPay pay = new HousingZhuHaiPay();
						pay.setPayDate(payDate);
						pay.setPayType(payType);
						pay.setFee(fee);
						pay.setInterest(interest);
						pay.setGetReason(getReason);
						pay.setGetType(getType);
						pay.setTaskid(taskid);
						pays.add(pay);
					}
				}
			}
		}
		return pays;
	}
	
	// 利用IO流保存验证码成功后，返回验证码图片保存路径
	public String getImagePath(Page page) throws Exception {
		String path = fileSavePath;
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true);
		parentDirFile.setWritable(true);
		if (!parentDirFile.exists()) {
			System.out.println("==========创建文件夹==========");
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		String imgagePath = path + "/" + imageName;
		InputStream inputStream = page.getWebResponse().getContentAsStream();
		FileOutputStream outputStream = (new FileOutputStream(new File(imgagePath)));
		if (inputStream != null && outputStream != null) {
			int temp = 0;
			while ((temp = inputStream.read()) != -1) { // 开始拷贝
				outputStream.write(temp); // 边读边写
			}
			outputStream.close();
			inputStream.close(); // 关闭输入输出流
		}
		return imgagePath;
	}
	
	/**
	 * 获取权限token
	 */
	public String getAuth() {
		// API Key
		String clientId = "85Yh1jbkPVjTVAa0SWcvGqlC";
		// Secret Key
		String clientSecret = "N7s4LxZiVWug6bt5NrMa6eNqOad54F3v";
		return getAuth(clientId, clientSecret);
	}

	public String getAuth(String ak, String sk) {
		// 获取token地址
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// grant_type为固定参数
				+ "grant_type=client_credentials"
				// API Key
				+ "&client_id=" + ak
				// Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.err.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			/**
			 * 返回结果示例
			 */
			System.err.println("result:" + result);
			
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = (JsonObject) parser.parse(result);
			String access_token = jsonObject.get("access_token").getAsString();
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
		}
		return null;
	}
	
	/*
	 * @Des 获取当前的 时间
	 */
	public String getDate(String fmt) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		String date = format.format(new Date());
		return date;
	}
	
}
