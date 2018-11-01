package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.gson.Gson;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinInjury;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMaternity;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMedical;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinPension;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUnemployment;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.tianjin.InsuranceTianjinUserInfoRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.GetId;
import app.crawler.domain.InsuranceTianjinInjuryList;
import app.crawler.domain.InsuranceTianjinMaternityList;
import app.crawler.domain.InsuranceTianjinMedicalList;
import app.crawler.domain.InsuranceTianjinPensionList;
import app.crawler.domain.InsuranceTianjinUnemploymentList;
import app.crawler.domain.WebParam;
import app.service.ChaoJiYingOcrService;
import app.service.InsuranceService;
import net.sf.json.JSONObject;

@Component
public class InsuranceTianjinParser{

	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceTianjinUserInfoRepository insuranceTianjinUserInfoRepository;
	
	@Autowired
	private  ChaoJiYingOcrService chaoJiYingOcrService;

	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) throws Exception{
		
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://public.hrss.tj.gov.cn/uaa/personlogin#/personLogin";//登陆路径
//		String url = "http://public.tj.hrss.gov.cn/uaa/api/person/idandmobile/login";
		              
		HtmlPage html = getHtml(url,webClient);
		webClient.waitForBackgroundJavaScript(1000);
		webClient = html.getWebClient();
		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		//通过验证码请求得到它的url
		GetId getId = new GetId(webClient);
		String id = getId.getId1(webClient);
		String imgUrl = "http://public.tj.hrss.gov.cn/uaa/captcha/img/"+id;
		String verifycode = chaoJiYingOcrService.getVerifycode(imgUrl,cookies, "1004");
		System.out.println(verifycode);
		
		// 清楚缓存请求
		webClient.getCache().setMaxSize(0);
		webClient.waitForBackgroundJavaScript(1000);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		// 正在登陆状态
		insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_LOGIN_DOING.getDescription(),InsuranceStatusCode.INSURANCE_LOGIN_DOING.getPhase(), 200, taskInsurance);
		taskInsurance.setTaskid(insuranceRequestParameters.getTaskId());
		insuranceService.changeLoginStatusDoing(taskInsurance);
		WebParam webParam = new WebParam();
		
		
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Cache-Control", "max-age=0");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("Origin", "http://public.tj.hrss.gov.cn");
		requestSettings.setAdditionalHeader("Host", "public.tj.hrss.gov.cn");
		requestSettings.setAdditionalHeader("Referer", "http://public.tj.hrss.gov.cn/uaa/personlogin");
		requestSettings.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		
		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
		requestSettings.getRequestParameters().add(new NameValuePair("username",insuranceRequestParameters.getUsername()));
		requestSettings.getRequestParameters().add(new NameValuePair("password",insuranceRequestParameters.getPassword()));
		requestSettings.getRequestParameters().add(new NameValuePair("captchaId", id));
		requestSettings.getRequestParameters().add(new NameValuePair("captchaWord", verifycode));

		HtmlPage successPage = webClient.getPage(requestSettings);
		webClient.waitForBackgroundJavaScript(1000);
		Set<Cookie> cookies2 = webClient.getCookieManager().getCookies();
		for(Cookie cookie : cookies2){
			System.out.println("登录后的cookie ===> "+cookie.getName()+": "+cookie.getValue());
		}
			webParam.setPage(successPage);
			webParam.setCode(successPage.getWebResponse().getStatusCode());
			String cookies3 = CommonUnit.transcookieToJson(successPage.getWebClient());
			taskInsurance.setCookies(cookies3);
			tracer.addTag("parser.login.webParam", webParam.toString());
			return webParam;
	}

	//通过地址获得weclient
	public HtmlPage getHtml(String url, WebClient webClient) {
		WebRequest webRequest;
		try {
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			// webClient.get
			webClient.getOptions().setJavaScriptEnabled(false);
			webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			HtmlPage searchPage = webClient.getPage(webRequest);
			webClient.waitForBackgroundJavaScript(1000);
			return searchPage;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
//	//验证码的真是路径
//	public static String sendGetRequest(String getUrl) {
//		StringBuffer sb = new StringBuffer();
//		InputStreamReader isr = null;
//		BufferedReader br = null;
//		try {
//			URL url = new URL(getUrl);
//			URLConnection urlConnection = url.openConnection();
//			urlConnection.setAllowUserInteraction(false);
//			isr = new InputStreamReader(url.openStream());
//			br = new BufferedReader(isr);
//			String line;
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return sb.toString();
//	}

//    private static String getId(WebClient webClient) {
//		
//		String url ="http://public.tj.hrss.gov.cn/uaa/captcha/img";
//		try {
//			WebRequest  requestSettings = new WebRequest(new URL(url), HttpMethod.GET);
//			
//			requestSettings.setAdditionalHeader("Accept", "application/json, text/plain, */*");
//			requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
//			requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
//			requestSettings.setAdditionalHeader("Connection", "keep-alive");
//			requestSettings.setAdditionalHeader("Host", "public.tj.hrss.gov.cn");
//			requestSettings.setAdditionalHeader("Referer", "http://public.tj.hrss.gov.cn/uaa/personlogin");
//			requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//			
//			Page page = webClient.getPage(requestSettings); 
//			String loggedhtml = page.getWebResponse().getContentAsString();
//			JSONObject jsonObject = new JSONObject(loggedhtml);
//			return jsonObject.getString("id");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
//    private String getImg(String id) {
//		String url = "http://public.tj.hrss.gov.cn/uaa/captcha/img/"+id;
//		
//		Connection con = Jsoup.connect(url).header("Accept","image/webp,image/*,*/*;q=0.8")
//					.header("Accept-Encoding", "gzip, deflate, sdch")
//					.header("Accept-Language", "zh-CN,zh;q=0.8")
//					.header("Connection", "keep-alive")
//					.header("Host", "public.tj.hrss.gov.cn")
//					.header("Content-Type","image/jpeg")
//					.header("Referer", "http://public.tj.hrss.gov.cn/uaa/personlogin")
//					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//		
//		try {
//			Response response = con.ignoreContentType(true).execute();
//			String imageName = UUID.randomUUID() + ".jpg";
//			File file = new File("D:\\img\\"+imageName);
//			//chaoJiYingOcrService.getImageLocalPath();
//			String imgagePath = file.getAbsolutePath();
//			FileOutputStream out = (new FileOutputStream(new java.io.File(imgagePath)));
//			
//			out.write(response.bodyAsBytes()); 
//			out.close();
//			return imgagePath;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
	
	
//	private static String getOcrCode(String imgFilePath) {
//		String chaoJiYingResult = getVerifycodeByChaoJiYing("1902", LEN_MIN, TIME_ADD, STR_DEBUG, imgFilePath);
//		//System.out.println("chaoJiYingResult ====>>"+chaoJiYingResult);
//		Gson gson = new GsonBuilder().create();
//		String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
//		return code;
//	}
	
	
	//去空
	public String getOutNull(String string){
		if(string=="null")
		{
			string="";
		}
		return string;
	}
	
	
	/**
	 * 解析个人信息
	 */
	public WebParam htmlParserUserInfo(TaskInsurance taskInsurance) throws Exception {
		InsuranceTianjinUserInfo insuranceTianjinUserInfo = new InsuranceTianjinUserInfo();
		String url1 = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/persons/21000845870";
		tracer.addTag("parser.getUserInfo", url1);

		WebRequest	webRequest = new WebRequest(new URL(url1), HttpMethod.GET); 
		
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		WebParam webParam = new WebParam();
		
		Page page2=webClient.getPage(webRequest);
		if(null != page2){
			int code = page2.getWebResponse().getStatusCode();
			if(code==200)
			{
				String json = page2.getWebResponse().getContentAsString();
				if(json.startsWith("{"))
				{
					JSONObject jsonObj = JSONObject.fromObject(json);
					String name=jsonObj.getString("name");  
					String sex=jsonObj.getString("sex"); 
					String birthday=jsonObj.getString("birthday"); 
					String nation=jsonObj.getString("nation");  
					String idType=jsonObj.getString("idType");  
					
					String idNumber=jsonObj.getString("idNumber"); 
					String idSocialensureNumber=jsonObj.getString("idSocialensureNumber"); 
					String idCountry=jsonObj.getString("idCountry"); 
					String socialSecurityCardNumber=jsonObj.getString("socialSecurityCardNumber");  
					String workDate=jsonObj.getString("workDate");  
					String householdType=jsonObj.getString("householdType");  
					String zipCode=jsonObj.getString("zipCode");  
					String homePhone=jsonObj.getString("homePhone");  
					String householdState=jsonObj.getString("householdState"); 
					String householdAddress=jsonObj.getString("householdAddress");  
					String householdNumber=jsonObj.getString("householdNumber");  
					String retireStatus=jsonObj.getString("retireStatus");
					String retireDate=jsonObj.getString("retireDate");  
					String mobileNumber=jsonObj.getString("mobileNumber"); 
					String residentAddressAddress=jsonObj.getString("residentAddressAddress"); 
					
					insuranceTianjinUserInfo.setName(getOutNull(name));
					insuranceTianjinUserInfo.setSex(getOutNull(sex));
					insuranceTianjinUserInfo.setNation(getOutNull(nation));
					insuranceTianjinUserInfo.setCardType(getOutNull(idType));
					insuranceTianjinUserInfo.setBirthday(getOutNull(birthday));
					insuranceTianjinUserInfo.setCardNum(getOutNull(idSocialensureNumber));
					insuranceTianjinUserInfo.setCountry(getOutNull(idCountry));
					insuranceTianjinUserInfo.setJoinWork(getOutNull(workDate));
					insuranceTianjinUserInfo.setZjNum(getOutNull(idNumber));
					insuranceTianjinUserInfo.setFixedNum(getOutNull(homePhone));
					insuranceTianjinUserInfo.setLivingAddress(getOutNull(residentAddressAddress));
				//	insuranceTianjinUserInfo.setCardBank();
					insuranceTianjinUserInfo.setWorkType(getOutNull(retireStatus));
					insuranceTianjinUserInfo.setResidence(getOutNull(householdType));
					insuranceTianjinUserInfo.setPhone(getOutNull(mobileNumber));
				//	insuranceTianjinUserInfo.setCardStatus();
					insuranceTianjinUserInfo.setPostalcode(getOutNull(zipCode));
					insuranceTianjinUserInfo.setRegisterNum(getOutNull(householdNumber));
					insuranceTianjinUserInfo.setOutWork(getOutNull(retireDate));
					insuranceTianjinUserInfo.setAddr(getOutNull(householdAddress));
					if(null != insuranceTianjinUserInfo)
					{
						insuranceTianjinUserInfo.setTaskid(taskInsurance.getTaskid());
						insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription(), InsuranceStatusCode.INSURANCE_PARSER_BASE_INFO_SUCCESS.getPhase(),200, taskInsurance);
						tracer.addTag("parser.crawler.saveUserInfo", taskInsurance.getTaskid());
						insuranceTianjinUserInfoRepository.save(insuranceTianjinUserInfo);
						webParam.setInsuranceTianjinUserInfo(insuranceTianjinUserInfo);
						webParam.setUrl(url1);
						webParam.setPage1(page2);
						webParam.setCode(200);
						return webParam;
					}
				}
			}
		}
		else
		{
			insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription(), InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase(), 201, taskInsurance);
			tracer.addTag("parser.crawler.saveUserInfo.ERROR", taskInsurance.getTaskid());
		}
		return null;
	    
	}

	/**
	 * 解析养老信息
	 * @throws Exception 
	 */
	public WebParam getPension( TaskInsurance taskInsurance) throws Exception {
		InsuranceTianjinPension insuranceTianjinPension = new InsuranceTianjinPension();
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM-dd hh:mm:ss");
		
		//当前时间和十年前时间
		String hehe = dateFormat.format( now ); 
		String endtime = hehe.substring(0,6);
		int startint = Integer.parseInt(endtime);
		int start1 = (startint-1000);
		String starttime = start1+"";
		String url = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/payment/emp/21000845870?insureCode=110&beginTime="+starttime+"&endTime="+endtime+"&paymentFlag=1";
		tracer.addTag("parser.getPensionInfo", url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		Page page2=webClient.getPage(webRequest);
		String json = page2.getWebResponse().getContentAsString();
		
		
		Gson gson = new Gson();
		InsuranceTianjinPensionList fromJson = gson.fromJson(json, InsuranceTianjinPensionList.class);
		if(null != fromJson)
		{
			List<InsuranceTianjinPension> insuranceTianjinPension2 = fromJson.getEmpPaymentDTO();
			
			WebParam webParam = new WebParam();
			if(null != insuranceTianjinPension2)
			{
				
				for (InsuranceTianjinPension insuranceTianjinPension3 : insuranceTianjinPension2) {
					insuranceTianjinPension3.setTaskid(taskInsurance.getTaskid());
				}
				insuranceTianjinPension.setTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),200,taskInsurance);
				tracer.addTag("parser.crawler.savePensionInfo", taskInsurance.getTaskid());
				//insuranceTianjinPensionRepository.save(insuranceTianjinPension2);
				webParam.setInsuranceTianjinPension(insuranceTianjinPension2);
				webParam.setUrl(url);
				webParam.setPage1(page2);
				webParam.setCode(200);
				return webParam;
			}
		}
		return null;
	}

	/**
	 * 解析医疗信息
	 * @throws Exception 
	 */
	public WebParam getMedicalInfo(TaskInsurance taskInsurance) throws Exception {
		InsuranceTianjinMedical insuranceTianjinMedical = new InsuranceTianjinMedical();
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM-dd hh:mm:ss");
		
		//当前时间和十年前时间
		String hehe = dateFormat.format( now ); 
		String endtime = hehe.substring(0,6);
		int startint = Integer.parseInt(endtime);
		int start1 = (startint-1000);
		String starttime = start1+"";
		String url = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/payment/emp/21000845870?insureCode=310&beginTime="+starttime+"&endTime="+endtime+"&paymentFlag=1";
		
		tracer.addTag("parser.getMedicalInfo", url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
	//	TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		Page page2=webClient.getPage(webRequest);
		String json = page2.getWebResponse().getContentAsString();
		
		Gson gson = new Gson();
		InsuranceTianjinMedicalList fromJson = gson.fromJson(json, InsuranceTianjinMedicalList.class);
		if(fromJson != null)
		{
			List<InsuranceTianjinMedical> insuranceTianjinMedicals2 = fromJson.getEmpPaymentDTO();
			
			
			WebParam webParam = new WebParam();
			if(null != insuranceTianjinMedicals2)
			{
				for (InsuranceTianjinMedical insuranceTianjinMedical3 : insuranceTianjinMedicals2) {
					insuranceTianjinMedical3.setTaskid(taskInsurance.getTaskid());
				}
				insuranceTianjinMedical.setTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription(),InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(),200,taskInsurance);
				tracer.addTag("parser.crawler.saveMedicalInfo", taskInsurance.getTaskid());
				webParam.setInsuranceTianjinMedical(insuranceTianjinMedicals2);
				webParam.setUrl(url);
				webParam.setPage1(page2);
				webParam.setCode(200);
				return webParam;
			}
		}
		
		return null;
	}

	/**
	 * 解析工伤信息
	 */
	public WebParam getInjuryInfo( TaskInsurance taskInsurance) throws Exception{
		InsuranceTianjinInjury insuranceTianjinInjury = new InsuranceTianjinInjury();
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM-dd hh:mm:ss");
		
		//当前时间和十年前时间
		String hehe = dateFormat.format( now ); 
		String endtime = hehe.substring(0,6);
		int startint = Integer.parseInt(endtime);
		int start1 = (startint-1000);
		String starttime = start1+"";
		String url = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/payment/emp/21000845870?insureCode=410&beginTime="+starttime+"&endTime="+endtime+"&paymentFlag=1";
		tracer.addTag("parser.getPensionInfo", url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
	//	TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		Page page2=webClient.getPage(webRequest);
		String json = page2.getWebResponse().getContentAsString();
		
		Gson gson = new Gson();
		InsuranceTianjinInjuryList fromJson = gson.fromJson(json, InsuranceTianjinInjuryList.class);
		if(fromJson != null)
		{
			List<InsuranceTianjinInjury> insuranceTianjinInjury2 = fromJson.getEmpPaymentDTO();
			WebParam webParam = new WebParam();
			if(null != insuranceTianjinInjury2)
			{
				
				for (InsuranceTianjinInjury insuranceTianjinInjury3 : insuranceTianjinInjury2) {
					insuranceTianjinInjury3.setTaskid(taskInsurance.getTaskid());
				}
				insuranceTianjinInjury.setTaskid(taskInsurance.getTaskid());
				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription(),InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(),200,taskInsurance);
				tracer.addTag("parser.crawler.saveInjuryInfo", taskInsurance.getTaskid());
				//insuranceTianjinInjuryRepository.save(insuranceTianjinInjury2);
				webParam.setInsuranceTianjinInjury(insuranceTianjinInjury2);
				webParam.setUrl(url);
				webParam.setPage1(page2);
				webParam.setCode(200);
				return webParam;
			}
		}
		return null;
	}

	/**
	 * 解析生育信息
	 */
	public WebParam getMaternityInfo( TaskInsurance taskInsurance) throws Exception{
		InsuranceTianjinMaternity insuranceTianjinMaternity = new InsuranceTianjinMaternity();
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM-dd hh:mm:ss");
		
		//当前时间和十年前时间
		String hehe = dateFormat.format( now ); 
		String endtime = hehe.substring(0,6);
		int startint = Integer.parseInt(endtime);
		int start1 = (startint-1000);
		String starttime = start1+"";
		String url = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/payment/emp/21000845870?insureCode=510&beginTime="+starttime+"&endTime="+endtime+"&paymentFlag=1";
		tracer.addTag("parser.getMaternityInfo", url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
	//	TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		Page page2=webClient.getPage(webRequest);
		String json = page2.getWebResponse().getContentAsString();
		
		Gson gson = new Gson();
		InsuranceTianjinMaternityList fromJson = gson.fromJson(json, InsuranceTianjinMaternityList.class);
		if(null != fromJson)
		{
			List<InsuranceTianjinMaternity> insuranceTianjinMaternity2 = fromJson.getEmpPaymentDTO();
			
			WebParam webParam = new WebParam();
			if(null != insuranceTianjinMaternity2)
			{
				
				for (InsuranceTianjinMaternity insuranceTianjinMaternity3 : insuranceTianjinMaternity2) {
					insuranceTianjinMaternity3.setTaskid(taskInsurance.getTaskid());
				}
				insuranceTianjinMaternity.setTaskid(taskInsurance.getTaskid());
			//	insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription(),InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(),200,taskInsurance);
				tracer.addTag("parser.crawler.saveMaternityInfo", taskInsurance.getTaskid());
			//	insuranceTianjinMaternityRepository.save(insuranceTianjinMaternity2);
				webParam.setInsuranceTianjinMaternity(insuranceTianjinMaternity2);
				webParam.setUrl(url);
				webParam.setPage1(page2);
				webParam.setCode(200);
				return webParam;
			}
		}
		return null;
	}

	/**
	 * 解析失业信息
	 */
	public WebParam getUnemploymentInfo( TaskInsurance taskInsurance) throws Exception {
		InsuranceTianjinUnemployment insuranceTianjinUnemployment = new InsuranceTianjinUnemployment();
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM-dd hh:mm:ss");
		
		//当前时间和十年前时间
		String hehe = dateFormat.format( now ); 
		String endtime = hehe.substring(0,6);
		int startint = Integer.parseInt(endtime);
		int start1 = (startint-1000);
		String starttime = start1+"";
		String url = "http://public.tj.hrss.gov.cn/ehrss-si-person/api/rights/payment/emp/21000845870?insureCode=210&beginTime="+starttime+"&endTime="+endtime+"&paymentFlag=1";
		tracer.addTag("parser.getUnemploymentInfo", url);
		
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		//TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		Page page2=webClient.getPage(webRequest);
		String json = page2.getWebResponse().getContentAsString();
		Gson gson = new Gson();
		InsuranceTianjinUnemploymentList fromJson = gson.fromJson(json, InsuranceTianjinUnemploymentList.class);
		if(null != fromJson)
		{
			List<InsuranceTianjinUnemployment> insuranceTianjinUnemployment2 = fromJson.getEmpPaymentDTO();
			
			WebParam webParam = new WebParam();
			
			
			if(null != insuranceTianjinUnemployment2)
			{
				
				for (InsuranceTianjinUnemployment insuranceTianjinUnemployment3 : insuranceTianjinUnemployment2) {
					insuranceTianjinUnemployment3.setTaskid(taskInsurance.getTaskid());
				}
				insuranceTianjinUnemployment.setTaskid(taskInsurance.getTaskid());
//				insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription(),InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(),200,taskInsurance);
				tracer.addTag("parser.crawler.saveInjuryInfo", taskInsurance.getTaskid());
				//insuranceTianjinUnemploymentRepository.save(insuranceTianjinUnemployment2);
				webParam.setInsuranceTianjinUnemployment(insuranceTianjinUnemployment2);
				webParam.setUrl(url);
				webParam.setPage1(page2);
				webParam.setCode(200);
				return webParam;
			}
		}
		
		return null;
	}

}
