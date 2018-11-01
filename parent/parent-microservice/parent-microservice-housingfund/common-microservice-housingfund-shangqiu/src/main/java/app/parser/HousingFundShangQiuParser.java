package app.parser;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuBasic;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuDetail;
import com.module.htmlunit.WebCrawler;

import app.common.WebParam;
import app.commontracerlog.TracerLog;
import app.service.ChaoJiYingOcrService;
import app.service.common.HousingBasicService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class HousingFundShangQiuParser extends HousingBasicService{

	@Autowired
	private ChaoJiYingOcrService chaoJiYingOcrService;
	@Autowired
	private TracerLog tracer;
	
	@Value("${filesavepath}") 
	public String fileSavePath;
	
	
	public WebParam login(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing)throws Exception{
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String url = "http://www.sqgjj.com/Convenient/PersonSearch";
		String codeurl = "http://www.sqgjj.com/Convenient/Index/1"; 
		WebRequest webRequest = new WebRequest(new URL(codeurl), HttpMethod.GET); 
//		webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
//		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
//		webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
//		webRequest.setAdditionalHeader("Connection", "keep-alive");
//		webRequest.setAdditionalHeader("Host", "www.sqgjj.com");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
//		webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.79 Mobile Safari/537.36");
		HtmlPage page = webClient.getPage(webRequest); 
		String html11 = page.getWebResponse().getContentAsString();
		System.out.println(html11);
//		String imgagePath=getImagePath(page,fileSavePath);
//		String code = chaoJiYingOcrService.callChaoJiYingService(imgagePath, "4004");
		WebParam webParam= new WebParam();
		WebRequest webRequest2 = new WebRequest(new URL(url), HttpMethod.POST);
//		String body = "PRadio=1&PCard="+messageLoginForHousing.getNum()+"&PPwd="+messageLoginForHousing.getNum()+"&PCode="+code;
//		webRequest2.setRequestBody(body);
		Page page0 = webClient.getPage(webRequest2);
		int status = page.getWebResponse().getStatusCode();
		System.out.println(page0.getWebResponse().getContentAsString());
		return null;
	}

	public WebParam<HousingShangQiuDetail> getDetailInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception {
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		String url  = "http://www.sqgjj.com/Person/GetJCMXInfo?pcard="+messageLoginForHousing.getNum()+"&ptype=1";
		
		Page page = webClient.getPage(url); 
		String html = page.getWebResponse().getContentAsString();
		System.out.println(html);
		List<HousingShangQiuDetail> infoList = new ArrayList<HousingShangQiuDetail>();
		tracer.addTag("HousingShangQiuParser.getDetailInfo 个人缴存明细" + messageLoginForHousing.getTask_id(),
				"<xmp>" + html + "</xmp>");
		infoList = htmlParserDetail(html,messageLoginForHousing,infoList);
		if(null!=infoList){
			webParam.setList(infoList);
			webParam.setUrl(url);
			webParam.setHtml(html);
			return webParam;
		}
		return null;
	}

	private List<HousingShangQiuDetail> htmlParserDetail(String html, MessageLoginForHousing messageLoginForHousing,List<HousingShangQiuDetail> infoList) {
		String substring2 = html.substring(1, html.length()-1); 
		substring2 = substring2.replace("\\", ""); 
		JSONObject fromObject = JSONObject.fromObject(substring2); 
		String string = fromObject.getString("list"); 
		JSONArray fromObject2 = JSONArray.fromObject(string); 
		for (int i = 0; i < fromObject2.size(); i++) { 
			JSONArray fromObject3 = JSONArray.fromObject(fromObject2.toString()); 
			Object obj = fromObject3.get(i); 
			JSONObject fromObject4 = JSONObject.fromObject(obj); 
			HousingShangQiuDetail  sqDetail = new HousingShangQiuDetail();
			String ooo = fromObject4.getString("GRM_RQ");
			sqDetail.setAccountingTime(fromObject4.getString("GRM_RQ"));
			sqDetail.setMonthPay(fromObject4.getString("GRM_YJE"));
			sqDetail.setPayMonths(fromObject4.getString("GRM_JJYS"));
			sqDetail.setHappenForehead(fromObject4.getString("GRM_FSJE"));
			sqDetail.setBalance(fromObject4.getString("GRM_YE"));
			sqDetail.setBeginMonth(fromObject4.getString("GRM_QSYF"));
			sqDetail.setMonthToMonth(fromObject4.getString("GRM_JZYF"));
			sqDetail.setHappenMode(fromObject4.getString("GRM_FSFS"));
			sqDetail.setTaskid(messageLoginForHousing.getTask_id());
			infoList.add(sqDetail);
		}
		
		return infoList;
	}

	public WebParam<HousingShangQiuBasic> getBasicInfo(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing)throws Exception{
		WebParam webParam = new WebParam();
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		String cookies = taskHousing.getCookies();
		Set<Cookie> set = CommonUnit.transferJsonToSet(cookies);
		Iterator<Cookie> i = set.iterator();
		while(i.hasNext()){
			webClient.getCookieManager().addCookie(i.next());
		}
		String url  = "http://www.sqgjj.com/Person/GetJCQKInfo?pcard="+messageLoginForHousing.getNum()+"&ptype=1";
		
		Page page = webClient.getPage(url); 
		String html = page.getWebResponse().getContentAsString();
		HousingShangQiuBasic basic = htmlParserBasic(html,messageLoginForHousing);
		webParam.setCode(page.getWebResponse().getStatusCode());
		webParam.setHtml(html);
		webParam.setUrl(url);
		webParam.setPage(page);
		webParam.setHousingShangQiuBasic(basic);
		return webParam;
	}



	private HousingShangQiuBasic htmlParserBasic(String html, MessageLoginForHousing messageLoginForHousing) {
		JSONObject jsonObj = JSONObject.fromObject(html);
		String  dwzh = jsonObj.getString("GRQ_DWBH");
		String  dwmc = jsonObj.getString("GRQ_DWMC");
		String  grzh = jsonObj.getString("GRQ_BH");
		String  sfzh = jsonObj.getString("GRQ_SFZH");
		String  zgxm = jsonObj.getString("GRQ_XM");
		String  gze  = jsonObj.getString("GRQ_GZE");
		String  yjje = jsonObj.getString("GRQ_YJE");
		String  jzyf = jsonObj.getString("GRQ_JZYF");
		String  sqye = jsonObj.getString("GRQ_SNYE");
		String  bqye = jsonObj.getString("GRQ_BNYE");
		Double a = Double.parseDouble(sqye);
		Double b = Double.parseDouble(bqye);
		Double c  =a+b;
		String zye = String.valueOf(c);
		String zgzt = jsonObj.getString("GRQ_ZQZT");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String rq = sdf.format(Long.parseLong(jzyf.substring(6, 19)));
		HousingShangQiuBasic basic = new HousingShangQiuBasic();
		basic.setCompanyNum(dwzh);
		basic.setCompanyName(dwmc);
		basic.setPerNum(grzh);
		basic.setNum(sfzh);
		basic.setWorkName(zgxm);
		basic.setWages(gze);
		basic.setMonthPay(yjje);
		basic.setMonthToMonth(rq);
		basic.setLastBalance(sqye);
		basic.setThisBalance(bqye);
		basic.setBalanceTotal(zye);
		basic.setWorkStatus(zgzt);
		basic.setTaskid(messageLoginForHousing.getTask_id());
		return basic;
	}



	
	


	


	
}
