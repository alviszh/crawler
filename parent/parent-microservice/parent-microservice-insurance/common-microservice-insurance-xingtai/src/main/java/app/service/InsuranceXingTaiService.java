package app.service;

import java.net.URL;
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
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.xingtai.InsuranceXingTaiInjury;
import com.microservice.dao.entity.crawler.insurance.xingtai.InsuranceXingTaiPension;
import com.microservice.dao.entity.crawler.insurance.xingtai.InsuranceXingTaiUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.xingtai.InsuranceXIngTaiUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.xingtai.InsuranceXingTaiInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.xingtai.InsuranceXingTaiPensionRepository;

import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.InsuranceXTParse;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.xingtai"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.xingtai"})
public class InsuranceXingTaiService {
	public static final Logger log = LoggerFactory.getLogger(InsuranceXingTaiService.class);
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceXIngTaiUserInfoRepository insuranceXIngTaiUserInfoRepository;
	@Autowired
	private InsuranceXingTaiPensionRepository insuranceXingTaiPensionRepository;
	@Autowired
	private InsuranceXingTaiInjuryRepository insuranceXingTaiInjuryRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	protected TaskInsuranceRepository taskInsuranceRepository;
	@Async
	public  Future<String> getResult(InsuranceRequestParameters insuranceRequestParameters,TaskInsurance taskInsurance, WebClient webClient, String html) {
		//个人信息
		InsuranceXingTaiUserInfo userInfo = InsuranceXTParse.userinfo_parse(html);
		if(userInfo==null){
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getError_code(), taskInsurance);
		}else{
			userInfo.setTaskid(taskInsurance.getTaskid());
			insuranceXIngTaiUserInfoRepository.save(userInfo);
			insuranceService.changeCrawlerStatus(
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription(),
					InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getError_code(), taskInsurance);
		}
		
		Document doc = Jsoup.parse(html);
		Elements ele = doc.select("table.result > tbody > tr > td > font");
		String a = null;
		int b = 0;
		if(ele.size()>0){
			a = ele.get(9).text().trim();
			b = Integer.parseInt(a);
			b = b/10;
			if (b%10>0){
				b = b+1;
			}else if(b%10==0){
				b = b;
			}
		}
		//养老缴费
		for(int i = 0;i<b;i++){
			String htmlPension = pensionHtml(insuranceRequestParameters, webClient, b, i);
			if(htmlPension == null){
				System.out.println("登陆异常");
				break;
			}else if(htmlPension.contains("养老保险账户信息")){
				List<InsuranceXingTaiPension> persion= InsuranceXTParse.persion_parse(htmlPension,taskInsurance);
				if(persion==null){
					insuranceService.changeCrawlerStatus(
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getError_code(), taskInsurance);
				}else{
					
					insuranceXingTaiPensionRepository.saveAll(persion);
					insuranceService.changeCrawlerStatus(
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(),
							InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getError_code(), taskInsurance);
				}
					
				
			}else{
				taskInsurance.setYanglaoStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getError_code());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getPhasestatus());
				
			}
		}
		
		
		//工伤缴费
		for(int i = 0;i<b;i++){
			String htmlInjury = injuryHtml(insuranceRequestParameters, webClient, b, i);
			if(htmlInjury == null){
				System.out.println("登陆异常");
				break;
			}else if(htmlInjury.contains("工伤保险帐户明细信息")){
				List<InsuranceXingTaiInjury> injury= InsuranceXTParse.injury_parse(htmlInjury,taskInsurance);
				if(injury==null){
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getError_code(), taskInsurance);
				}else{
					insuranceXingTaiInjuryRepository.saveAll(injury);
					insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
							InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
				}
					
				
			}else{
				taskInsurance.setGongshangStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getError_code());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getPhasestatus());
				
			}
		}
		taskInsurance.setYiliaoStatus(0);
		taskInsurance.setShengyuStatus(0);
		taskInsurance.setShiyeStatus(0);
		taskInsuranceRepository.save(taskInsurance);
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());
		return null;
		
	}
	
	//养老HTML
	public String pensionHtml(InsuranceRequestParameters insuranceRequestParameters, WebClient webClient, int b, int i){
		String html = null;
		try {	
			String url1 = "http://hext.lss.gov.cn/ecdomain/searchYLData";
			WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "hext.lss.gov.cn");
			webRequest.setAdditionalHeader("Origin", "http://hext.lss.gov.cn");
			webRequest.setAdditionalHeader("Referer", "http://hext.lss.gov.cn/ecdomain/searchYLData");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			
			String requestBody = "totalPage="+b+"&idCard="+insuranceRequestParameters.getUserIDNum().trim()+"&personNumber="+insuranceRequestParameters.getPassword().trim()+"&pagenum="+i+"";
			webRequest.setRequestBody(requestBody);
			Page searchPage = webClient.getPage(webRequest);
			html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.crawler.login.page", "<xmp>"+html+"</xmp>");
			return html;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("e", "xingtaishebao");
			tracer.addTag("parser.login.auth", e.getMessage());
			return html;
		}
		
	}
	
	//工伤HTML
	public String injuryHtml(InsuranceRequestParameters insuranceRequestParameters, WebClient webClient, int b, int i){
		String html = null;
		try {	
			String url1 = "http://hext.lss.gov.cn/ecdomain/searchGSData";
			WebRequest webRequest = new WebRequest(new URL(url1), HttpMethod.POST);
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.9");
			webRequest.setAdditionalHeader("Cache-Control", "max-age=0");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
			webRequest.setAdditionalHeader("Host", "hext.lss.gov.cn");
			webRequest.setAdditionalHeader("Origin", "http://hext.lss.gov.cn");
			webRequest.setAdditionalHeader("Referer", "http://hext.lss.gov.cn/ecdomain/searchGSData");
			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			
			String requestBody = "totalPage="+b+"&idCard="+insuranceRequestParameters.getUserIDNum().trim()+"&personNumber="+insuranceRequestParameters.getPassword().trim()+"&pagenum="+i+"";
			webRequest.setRequestBody(requestBody);
			Page searchPage = webClient.getPage(webRequest);
			html = searchPage.getWebResponse().getContentAsString();
			tracer.addTag("parser.crawler.login.page", "<xmp>"+html+"</xmp>");
			return html;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tracer.addTag("e", "xingtaishebao");
			tracer.addTag("parser.login.auth", e.getMessage());
			return html;
		}
		
	}
	

}
