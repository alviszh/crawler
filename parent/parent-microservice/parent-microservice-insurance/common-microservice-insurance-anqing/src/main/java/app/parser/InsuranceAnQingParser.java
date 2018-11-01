package app.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingInfo;
import com.microservice.dao.entity.crawler.insurance.anqing.InsuranceAnQingUserInfo;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.module.htmlunit.WebCrawler;
import com.module.ocr.utils.AbstractChaoJiYingHandler;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.InsuranceService;

@Component
public class InsuranceAnQingParser {
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	
	/**
	 * @Des 登录
	 * @param page
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public WebParam login(InsuranceRequestParameters insuranceRequestParameters) {		
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		try {
			String url = "http://220.179.13.107:7001/webeps/code.jsp";
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
			webRequest.setAdditionalHeader("Content-Type", "image/png");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Host", "58.59.38.82:8080");
			Page page = webClient.getPage(webRequest);
			WebResponse webResponse = page.getWebResponse();
			InputStream bodyStream = webResponse.getContentAsStream();
			byte[] responseContent = ByteStreams.toByteArray(bodyStream);
			bodyStream.close();
			String path = getPathBySystem();
			File imageFile = getImageLocalPath(path);
			// 创建输出流
			FileOutputStream outStream = new FileOutputStream(imageFile);
			// 写入数据
			outStream.write(responseContent);
			// 关闭输出流
			outStream.close();
			// 超级鹰识别
			String chaoJiYingResult = AbstractChaoJiYingHandler.getVerifycodeByChaoJiYing("1902",
					imageFile.getAbsolutePath());
			Gson gson = new GsonBuilder().create();
			String code = (String) gson.fromJson(chaoJiYingResult, Map.class).get("pic_str");
			System.out.println("code ====>>" + code);
			// 登陆
			String loginUrl = "http://220.179.13.107:7001/webeps/logonAction.do";
			List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
			paramsList = new ArrayList<NameValuePair>();
			paramsList.add(new NameValuePair("userlb", ""));
			paramsList.add(new NameValuePair("userid", insuranceRequestParameters.getUsername()));
			paramsList.add(new NameValuePair("passwd", insuranceRequestParameters.getPassword()));
			paramsList.add(new NameValuePair("checkcode", code));
			paramsList.add(new NameValuePair("cmdok", "+%B5%C7+%C2%BD+"));
			WebRequest loginWebRequest = new WebRequest(new URL(loginUrl), HttpMethod.POST);
			loginWebRequest.setAdditionalHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			loginWebRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			loginWebRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			loginWebRequest.setAdditionalHeader("Connection", "keep-alive");
			loginWebRequest.setAdditionalHeader("Host", "220.179.13.107:7001");
			loginWebRequest.setAdditionalHeader("Origin", "http://220.179.13.107:7001");
			loginWebRequest.setRequestParameters(paramsList);
			HtmlPage loginPage = webClient.getPage(loginWebRequest);
			webClient.getOptions().setJavaScriptEnabled(false); // 不需要解析js
			webClient.getOptions().setThrowExceptionOnScriptError(false); // 解析js出错时不抛异常
			Thread.sleep(1500);
			String html = loginPage.asXml();
			webParam.setCode(loginPage.getWebResponse().getStatusCode());
			webParam.setUrl(url);
			webParam.setPage(loginPage);
			webParam.setHtml(html);
			tracer.addTag("login", "<xmp>" + html + "</xmp>");
			return webParam;
		} catch (Exception e) {
			tracer.addTag("HousingRizhaoParse.login:",
					insuranceRequestParameters.getTaskId() + "---ERROR:" + e.toString());
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Des 获取个人信息
	 * @param taskInsurance
	 * @param cookies
	 * @return
	 * @throws Exception 
	 */
	public WebParam getUserInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://220.179.13.107:7001/webeps/sbyw/jbbuiness/grmain.jsp";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "220.179.13.107:7001");
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 
		HtmlPage page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.asXml();
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	      	InsuranceAnQingUserInfo userInfo = htmlParserForUserInfo(html,taskInsurance);	    		    
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceAnQingUserInfo(userInfo);    
	     	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);	
	    }
		return webParam;
	}
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private InsuranceAnQingUserInfo htmlParserForUserInfo(String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html);		
		String usernum = getNextLabelByKeyword(doc, "个人编号");
		String idnum = getNextLabelByKeyword(doc, "公民身份证号码");
		String username =getNextLabelByKeyword(doc, "姓名");		
		String gender = getNextLabelByKeyword(doc, "性别");
		String nation = getNextLabelByKeyword(doc, "民族");
		String birthdate =getNextLabelByKeyword(doc, "出生日期");
		
		String firstworddate = getNextLabelByKeyword(doc, "参加工作日期");
		String state = getNextLabelByKeyword(doc, "人员状态");
		String household =getNextLabelByKeyword(doc, "户口性质");
		String householdplace =getNextLabelByKeyword(doc, "户口所在地");
		String education = getNextLabelByKeyword(doc, "文化程度");
		String personalidentity = getNextLabelByKeyword(doc, "个人身份");
		String workstate =getNextLabelByKeyword(doc, "用工形式");
		String technicalposition = getNextLabelByKeyword(doc, "专业技术职务");
		String technicalgrde = getNextLabelByKeyword(doc, "工人技术等级");
		String contactnum =getNextLabelByKeyword(doc, "联系电话");
		
		String specicalworksign = getNextLabelByKeyword(doc, "特殊工种标识");
		String administjob = getNextLabelByKeyword(doc, "行政职务");
		String farmersign =getNextLabelByKeyword(doc, "农民工标识");
		InsuranceAnQingUserInfo userInfo = new InsuranceAnQingUserInfo(usernum, idnum, username, gender, nation,
				birthdate, firstworddate, state, household, householdplace, education, personalidentity, workstate,
				technicalposition, technicalgrde, contactnum, specicalworksign, administjob, farmersign,
				taskInsurance.getTaskid());	
		return userInfo;
	}
	
	public WebParam getInsuranceInfo(TaskInsurance taskInsurance, Set<Cookie> cookies) throws Exception {	
		WebParam webParam= new WebParam();
		String url = "http://220.179.13.107:7001/webeps/sbyw/getGrcbxx.do";
		WebClient webClient = insuranceService.getWebClient(cookies);
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);		
		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		webRequest.setAdditionalHeader("Accept-Language", "zh,zh-CN;q=0.8,en-US;q=0.5,en;q=0.3");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		webRequest.setAdditionalHeader("Host", "220.179.13.107:7001");
		webRequest.setAdditionalHeader("Referer", "http://220.179.13.107:7001/webeps/sbyw/jbbuiness/grmain.jsp"); 
		webClient.getOptions().setJavaScriptEnabled(false);  //不需要解析js  
		webClient.getOptions().setThrowExceptionOnScriptError(false);  //解析js出错时不抛异常 
		HtmlPage page = webClient.getPage(webRequest);		
		int statusCode = page.getWebResponse().getStatusCode();
	    if(200 == statusCode){
	    	String html = page.asXml();
	      	tracer.addTag("getUserInfo 个人信息","<xmp>"+html+"</xmp>");
	      	List<InsuranceAnQingInfo> insuranceAnQingInfos = htmlParserForInsuranceInfo(html,taskInsurance);	    		    
	    	webParam.setCode(statusCode);
	    	webParam.setInsuranceAnQingInfos(insuranceAnQingInfos); 
	     	webParam.setPage(page);
	    	webParam.setUrl(url);
	    	webParam.setHtml(html);	
	    }
		return webParam;
	}
	/**
	 * @Des 解析个人信息
	 * @param html
	 * @return
	 */
	private List<InsuranceAnQingInfo> htmlParserForInsuranceInfo(String html, TaskInsurance taskInsurance) {
		Document doc = Jsoup.parse(html,"utf-8");
		Element table =  doc.select("table").last();
		Elements trs = table.select("tr");
		int trs_size = trs.size();
		List<InsuranceAnQingInfo>  insuranceAnQingInfos=new ArrayList<InsuranceAnQingInfo>();
		if (trs_size > 0) {
			for (int i = 1; i < trs_size; i++) {
				Elements tds = trs.get(i).select("td");
				 String type=tds.get(0).text();//险种类型
				 String companynum=tds.get(1).text();//单位
				 String companyname=tds.get(2).text();//单位名称
				 String insuredtype=tds.get(3).text();//比例类型
				 String insuredway=tds.get(4).text();//人员参保关系
				 String paybase=tds.get(5).text();//缴费基数
				 String insuredstate=tds.get(6).text();//个人参保状态
				 String insureddate=tds.get(7).text();//个人参保日期
				 InsuranceAnQingInfo  insuranceAnQingInfo= new InsuranceAnQingInfo();
				 insuranceAnQingInfo.setType(type);
				 insuranceAnQingInfo.setCompanynum(companynum);
				 insuranceAnQingInfo.setCompanyname(companyname);
				 insuranceAnQingInfo.setInsuredtype(insuredtype);
				 insuranceAnQingInfo.setInsuredway(insuredway);
				 insuranceAnQingInfo.setPaybase(paybase);
				 insuranceAnQingInfo.setInsuredstate(insuredstate);
				 insuranceAnQingInfo.setInsureddate(insureddate);
				 insuranceAnQingInfo.setTaskid(taskInsurance.getTaskid());
				 insuranceAnQingInfos.add(insuranceAnQingInfo);
			}
		}
		return insuranceAnQingInfos;
	}
	
	
	/**
	 * @param document
	 * @param keyword
	 * @return
	 * @Des 获取目标标签的下一个兄弟标签的内容
	 */
	public static String getNextLabelByKeyword(Document document, String keyword) {
		Elements es = document.select("td:contains(" + keyword + ")");
		if (null != es && es.size() > 0) {
			Element element = es.last();
			Element nextElement = element.nextElementSibling();
			if (null != nextElement) {
				return nextElement.text();
			}
		}
		return null;
	}
	
	
	public static String getPathBySystem() {

		if (System.getProperty("os.name").toUpperCase().indexOf("Windows".toUpperCase()) != -1) {
			String path = System.getProperty("user.dir") + "/snapshot/";
			return path;
		} else {
			String path = System.getProperty("user.home") + "/snapshot/";
			return path;
		}

	}
	public static File getImageLocalPath(String path) {
		File parentDirFile = new File(path);
		parentDirFile.setReadable(true); //
		parentDirFile.setWritable(true); //
		if (!parentDirFile.exists()) {
			parentDirFile.mkdirs();
		}
		String imageName = UUID.randomUUID().toString() + ".png";
		File codeImageFile = new File(path+ "/" + imageName);
		codeImageFile.setReadable(true); //
		codeImageFile.setWritable(true); //
		return codeImageFile;

	}
}
