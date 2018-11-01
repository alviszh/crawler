package app.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.dalian.HousingDaLianPay;
import com.microservice.dao.entity.crawler.housing.dalian.HousingDaLianUserinfo;
import com.microservice.dao.repository.crawler.housing.dalian.HousingDaLianPayRepository;
import com.microservice.dao.repository.crawler.housing.dalian.HousingDaLianUserInfoRepository;

import app.crawler.htmlparse.HousingDLParse;
import app.service.common.HousingBasicService;
@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.dalian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.dalian")
public class HousingDaLianService extends HousingBasicService{
	public static final Logger log = LoggerFactory.getLogger(HousingDaLianService.class);
	
	@Autowired
	public HousingDaLianUserInfoRepository housingDaLianUserInfoRepository;
	
	@Autowired
	public HousingDaLianPayRepository housingDaLianPayRepository;
	@Async
	public  Future<String> getResult(MessageLoginForHousing messageLoginForHousing,TaskHousing taskHousing, WebClient webClient) {
		String urlInfo = "https://bg.gjj.dl.gov.cn/person/logon/showHomePage.act";
		Page page = null;
		//基本信息
		try {
			page = loginAndGetCommon.getHtml(urlInfo, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("登陆异常", e.toString());
		}
		Document doc = Jsoup.parse(page.getWebResponse().getContentAsString());
		Elements ele = doc.select("div.form-group");
		String idCard = null;            //证件号码	
		if(ele.size()>0){			
			idCard  = ele.get(11).text().trim();
			if (idCard.length()>5){
				idCard = idCard.substring(idCard.indexOf("：")+1);
				System.out.println("idCard"+idCard);
			}	
		}
		String urlInfo1 = "https://bg.gjj.dl.gov.cn/person/C0151/list.act?zjhm1="+idCard+"";
		Page page1 = null;
		//基本信息
		try {
			WebRequest webRequest = new WebRequest(new URL(urlInfo1), HttpMethod.POST);
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			page1 = webClient.getPage(webRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("登陆异常", e.toString());
		}
		tracer.addTag("基本信息html", page.getWebResponse().getContentAsString());
		tracer.addTag("基本信息html", page1.getWebResponse().getContentAsString());
		HousingDaLianUserinfo userinfo = HousingDLParse.userinfo_parse(page1.getWebResponse().getContentAsString(),page.getWebResponse().getContentAsString());
		userinfo.setTaskid(taskHousing.getTaskid());
		housingDaLianUserInfoRepository.save(userinfo);
		
		//缴费信息明细
		List<String> urlPayDetailslist = null;
		try {
			urlPayDetailslist = getURLforPayDetails(userinfo.getPersonalAccount(),webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("登陆异常", e.toString());
		}
		for (String url_result : urlPayDetailslist) {
			tracer.addTag("流水信息html", url_result);
			List<HousingDaLianPay> listresult = HousingDLParse.paydetails_parse(url_result);
			for(HousingDaLianPay result : listresult){
				result.setTaskid(taskHousing.getTaskid());
				housingDaLianPayRepository.save(result);
			}
		}
		
		return null;
	
	}
	
	public  List<String> getURLforPayDetails(String personalAccount,WebClient webClient) throws Exception{
		List<String> list = new ArrayList<String>();
		Calendar now = Calendar.getInstance();
		int endNian = now.get(Calendar.YEAR);
		int beginNian = endNian-10;
		int yue = now.get(Calendar.MONTH) +1;
		int ri = now.get(Calendar.DAY_OF_MONTH);
		String yu = null;
		String r = null;
		if (yue>9){
			yu = String.valueOf(yue);
		}else{
			yu = "0"+yue;
		}
		if (ri>9){
			r = String.valueOf(ri);
		}else{
			r = "0"+ri;
		}
		String beginDate =beginNian+"-"+ yu+"-"+r;
		String endDate =endNian+"-"+ yu+"-"+r;
		String url = "https://bg.gjj.dl.gov.cn/person/C0165/doQuery.act?grzh="+personalAccount+"&gjjzy=&"
				+ "qsrq="+beginDate+"&zzrq="+endDate+"&gjjzhlx=1";
		try {
			Page page = loginAndGetCommon.getHtml(url, webClient);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String urlpay = "https://bg.gjj.dl.gov.cn/person/C0165/list.act";
		WebRequest webRequest = new WebRequest(new URL(urlpay), HttpMethod.POST);
		webRequest.setAdditionalHeader("Accept", "text/javascript, text/html, application/xml,application/json, text/xml, */*");
		webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, br");
		webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		webRequest.setAdditionalHeader("Connection", "keep-alive");
		//webRequest.setAdditionalHeader("Content-Length", "322");
		webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		webRequest.setAdditionalHeader("Host", "bg.gjj.dl.gov.cn");
		webRequest.setAdditionalHeader("Origin", "https://bg.gjj.dl.gov.cn");;
		webRequest.setAdditionalHeader("Referer", url); 
		webRequest.setRequestBody("_gt_json=%7B%22recordType%22%3A%22object%22%2C%22pageInfo%22%3A%7B%22"
				+ "pageSize%22%3A10%2C%22pageNum%22%3A1%2C%22totalRowNum%22%3A-1%2C%22totalPageNum%22%3A0%2C%22"
				+ "startRowNum%22%3A1%2C%22endRowNum%22%3A0%7D%2C%22columnInfo%22%3A%5B%7B%22id%22%3A%22jyrq%22%2C%22"
				+ "header%22%3A%22%E4%BA%A4%E6%98%93%E6%97%A5%E6%9C%9F%22%2C%22fieldName%22%3A%22jyrq%22%2C%22"
				+ "fieldIndex%22%3A%22jyrq%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3"
				+ "Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22"
				+ "valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22jyms%22%2C%22header%22%3A%22%E6%91%98%E8%A6%81%22%2C%22"
				+ "fieldName%22%3A%22jyms%22%2C%22fieldIndex%22%3A%22jyms%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
				+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
				+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22jffse%22%2C%22"
				+ "header%22%3A%22%E5%80%9F%E6%96%B9%E5%8F%91%E7%94%9F%E9%A2%9D%22%2C%22fieldName%22%3A%22"
				+ "jffse%22%2C%22fieldIndex%22%3A%22jffse%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
				+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
				+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22null%22%7D%2C%7B%22id%22%3A%22dffse%22%2C%22"
				+ "header%22%3A%22%E8%B4%B7%E6%96%B9%E5%8F%91%E7%94%9F%E9%A2%9D%22%2C%22fieldName%22%3A%22"
				+ "dffse%22%2C%22fieldIndex%22%3A%22dffse%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
				+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
				+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22null%22%7D%2C%7B%22id%22%3A%22yue%22%2C%22"
				+ "header%22%3A%22%E4%BD%99%E9%A2%9D%22%2C%22fieldName%22%3A%22yue%22%2C%22fieldIndex%22%3A%22"
				+ "yue%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3Atrue%2C%22"
				+ "printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22valFormat%22%3A%22"
				+ "null%22%7D%2C%7B%22id%22%3A%22dwmc1%22%2C%22header%22%3A%22%E5%8D%95%E4%BD%8D%E5%90%8D%E7%A7%B0%22%2C%22"
				+ "fieldName%22%3A%22dwmc1%22%2C%22fieldIndex%22%3A%22dwmc1%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
				+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
				+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22qsrq%22%2C%22"
				+ "header%22%3A%22%E8%B5%B7%E5%A7%8B%E6%97%A5%E6%9C%9F%22%2C%22fieldName%22%3A%22qsrq%22%2C%22"
				+ "fieldIndex%22%3A%22qsrq%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22"
				+ "exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
				+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22zzrq%22%2C%22"
				+ "header%22%3A%22%E7%BB%88%E6%AD%A2%E6%97%A5%E6%9C%9F%22%2C%22fieldName%22%3A%22"
				+ "zzrq%22%2C%22fieldIndex%22%3A%22zzrq%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
				+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
				+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22jylxms%22%2C%22"
				+ "header%22%3A%22%E4%BA%A4%E6%98%93%E7%8A%B6%E6%80%81%22%2C%22fieldName%22%3A%22jylxms%22%2C%22"
				+ "fieldIndex%22%3A%22jylxms%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3"
				+ "Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22"
				+ "valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22qdlyms%22%2C%22header%22%3A%22%E5%8A%9E%E7%90%86%E6%96%B9%E5%BC%8F%22%2C%22"
				+ "fieldName%22%3A%22qdlyms%22%2C%22fieldIndex%22%3A%22qdlyms%22%2C%22sortOrder%22%3"
				+ "Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22"
				+ "mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%5D%2C%22"
				+ "sortInfo%22%3A%5B%5D%2C%22filterInfo%22%3A%5B%5D%2C%22remotePaging%22%3Atrue%2C%22"
				+ "remoteSort%22%3Afalse%2C%22parameters%22%3A%7B%22"
				+ "grzh%22%3A%22"+personalAccount+"%22%2C%22gjjzy%22%3A%22%22%2C%22"
			    + "qsrq%22%3A%22"+beginNian+"%5Cu002d"+yue+"%5Cu002d"+ri+"%22%2C%22"
			    + "zzrq%22%3A%22"+endDate+"%5Cu002d"+yue+"%5Cu002d"+ri+"%22%2C%22gjjzhlx%22%3A%221%22%7D%2C%22action%22%3A%22load%22%7D");
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		System.out.println(searchPage.getWebResponse().getContentAsString());
		String ss = searchPage.getWebResponse().getContentAsString();
		String rowNum = ss.substring(ss.indexOf("totalRowNum")+13);
		String totalRowNum = rowNum.substring(0,rowNum.indexOf(","));
		String pageNum = ss.substring(ss.indexOf("totalPageNum")+14);
		String totalPageNum = pageNum.substring(0,pageNum.indexOf(","));
		list.add(ss);
		for (int i = 2;i<=Integer.parseInt(totalPageNum);i++){
			int startRowNum = 0;
			int endRowNum = 0;
			if(i==Integer.parseInt(totalPageNum)){
				startRowNum = (i-1)*10+1;
				endRowNum = Integer.parseInt(totalRowNum);
			}else{
				startRowNum = (i-1)*10+1;
				endRowNum = i*10;
			}
			
			webRequest.setRequestBody("_gt_json=%7B%22recordType%22%3A%22object%22%2C%22pageInfo%22%3A%7B%22"
					+ "pageSize%22%3A10%2C%22pageNum%22%3A"+i+"%2C%22totalRowNum%22%3A"+totalRowNum+"%2C%22totalPageNum%22%3A"+totalPageNum+"%2C%22"
					+ "startRowNum%22%3A"+startRowNum+"%2C%22endRowNum%22%3A"+endRowNum+"%7D%2C%22columnInfo%22%3A%5B%7B%22id%22%3A%22jyrq%22%2C%22"
					+ "header%22%3A%22%E4%BA%A4%E6%98%93%E6%97%A5%E6%9C%9F%22%2C%22fieldName%22%3A%22jyrq%22%2C%22"
					+ "fieldIndex%22%3A%22jyrq%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3"
					+ "Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22"
					+ "valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22jyms%22%2C%22header%22%3A%22%E6%91%98%E8%A6%81%22%2C%22"
					+ "fieldName%22%3A%22jyms%22%2C%22fieldIndex%22%3A%22jyms%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
					+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
					+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22jffse%22%2C%22"
					+ "header%22%3A%22%E5%80%9F%E6%96%B9%E5%8F%91%E7%94%9F%E9%A2%9D%22%2C%22fieldName%22%3A%22"
					+ "jffse%22%2C%22fieldIndex%22%3A%22jffse%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
					+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
					+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22null%22%7D%2C%7B%22id%22%3A%22dffse%22%2C%22"
					+ "header%22%3A%22%E8%B4%B7%E6%96%B9%E5%8F%91%E7%94%9F%E9%A2%9D%22%2C%22fieldName%22%3A%22"
					+ "dffse%22%2C%22fieldIndex%22%3A%22dffse%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
					+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
					+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22null%22%7D%2C%7B%22id%22%3A%22yue%22%2C%22"
					+ "header%22%3A%22%E4%BD%99%E9%A2%9D%22%2C%22fieldName%22%3A%22yue%22%2C%22fieldIndex%22%3A%22"
					+ "yue%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3Atrue%2C%22"
					+ "printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22valFormat%22%3A%22"
					+ "null%22%7D%2C%7B%22id%22%3A%22dwmc1%22%2C%22header%22%3A%22%E5%8D%95%E4%BD%8D%E5%90%8D%E7%A7%B0%22%2C%22"
					+ "fieldName%22%3A%22dwmc1%22%2C%22fieldIndex%22%3A%22dwmc1%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
					+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
					+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22qsrq%22%2C%22"
					+ "header%22%3A%22%E8%B5%B7%E5%A7%8B%E6%97%A5%E6%9C%9F%22%2C%22fieldName%22%3A%22qsrq%22%2C%22"
					+ "fieldIndex%22%3A%22qsrq%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22"
					+ "exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
					+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22zzrq%22%2C%22"
					+ "header%22%3A%22%E7%BB%88%E6%AD%A2%E6%97%A5%E6%9C%9F%22%2C%22fieldName%22%3A%22"
					+ "zzrq%22%2C%22fieldIndex%22%3A%22zzrq%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3"
					+ "Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22"
					+ "keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22jylxms%22%2C%22"
					+ "header%22%3A%22%E4%BA%A4%E6%98%93%E7%8A%B6%E6%80%81%22%2C%22fieldName%22%3A%22jylxms%22%2C%22"
					+ "fieldIndex%22%3A%22jylxms%22%2C%22sortOrder%22%3Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3"
					+ "Atrue%2C%22printable%22%3Atrue%2C%22mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22"
					+ "valFormat%22%3A%22%22%7D%2C%7B%22id%22%3A%22qdlyms%22%2C%22header%22%3A%22%E5%8A%9E%E7%90%86%E6%96%B9%E5%BC%8F%22%2C%22"
					+ "fieldName%22%3A%22qdlyms%22%2C%22fieldIndex%22%3A%22qdlyms%22%2C%22sortOrder%22%3"
					+ "Anull%2C%22hidden%22%3Afalse%2C%22exportable%22%3Atrue%2C%22printable%22%3Atrue%2C%22"
					+ "mappingItem%22%3A%22%22%2C%22keyVals%22%3A%22%22%2C%22valFormat%22%3A%22%22%7D%5D%2C%22"
					+ "sortInfo%22%3A%5B%5D%2C%22filterInfo%22%3A%5B%5D%2C%22remotePaging%22%3Atrue%2C%22"
					+ "remoteSort%22%3Afalse%2C%22parameters%22%3A%7B%22"
					+ "grzh%22%3A%22"+personalAccount+"%22%2C%22gjjzy%22%3A%22%22%2C%22"
				    + "qsrq%22%3A%22"+beginNian+"%5Cu002d"+yue+"%5Cu002d"+ri+"%22%2C%22"
				    + "zzrq%22%3A%22"+endDate+"%5Cu002d"+yue+"%5Cu002d"+ri+"%22%2C%22gjjzhlx%22%3A%221%22%7D%2C%22action%22%3A%22load%22%7D");
			webClient.setJavaScriptTimeout(50000); 
			webClient.getOptions().setTimeout(50000); // 15->60 
			Page searchPage1 = webClient.getPage(webRequest);
			System.out.println(searchPage1.getWebResponse().getContentAsString());
			String sss = searchPage1.getWebResponse().getContentAsString();
			list.add(sss);
		}
		return list;
		
	}

}
