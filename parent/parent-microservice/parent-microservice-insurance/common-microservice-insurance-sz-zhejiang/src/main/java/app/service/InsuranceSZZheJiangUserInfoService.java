package app.service;

import java.net.URL;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZZheJiangParser;
@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.sz.zhejiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.sz.zhejiang" })
public class InsuranceSZZheJiangUserInfoService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZZheJiangParser insuranceSZZheJiangParser;
	@Autowired
	private InsuranceSZZheJiangUserInfoRepository insuranceSZZheJiangUserInfoRepository;
	/***
	 * 个人基本信息
	 * @param webClient
	 * @param taskInsurance
	 * @param parameter
	 * @return
	 */
	public Future<String> getUserInfo(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters parameter) {
		String city = taskInsurance.getCity();
		try {
			if(city.indexOf("衢州市")!=-1){
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_grjbxx";
				String url3 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_ylzh";
				getHtml(url2, webClient);
				Page page = getHtml(url2, webClient);
				String html2 = page.getWebResponse().getContentAsString();
				System.out.println(html2);
				Page page2 = getHtml(url3, webClient);
				String html4 = page2.getWebResponse().getContentAsString();
				System.out.println(html4);
				String url0 = "https://puser.zjzwfw.gov.cn/sso/usp.do?action=user";
				Page html = (HtmlPage) getHtml(url0, webClient);
				String html6 = html.getWebResponse().getContentAsString();

				InsuranceSZZheJiangUserInfo userInfo = insuranceSZZheJiangParser.getuserinfo(html2,html4,parameter.getTaskId(),html6);

				if(userInfo!=null){
					insuranceSZZheJiangUserInfoRepository.save(userInfo);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}

			}else if(city.indexOf("丽水市")!=-1){
				
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?dataset=sbcx_sheng$sbcx_grjbxx";
				String url3 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_ylzh";
				getHtml(url2, webClient);
				Page page = getHtml(url2, webClient);
				String html2 = page.getWebResponse().getContentAsString();
				System.out.println(html2);
				Page page2 = getHtml(url3, webClient);
				String html4 = page2.getWebResponse().getContentAsString();
				System.out.println(html4);
				String url0 = "https://puser.zjzwfw.gov.cn/sso/usp.do?action=user";
				Page html = (HtmlPage) getHtml(url0, webClient);
				String html6 = html.getWebResponse().getContentAsString();

				InsuranceSZZheJiangUserInfo userInfo = insuranceSZZheJiangParser.getuserinfo(html2,html4,parameter.getTaskId(),html6);

				if(userInfo!=null){
					insuranceSZZheJiangUserInfoRepository.save(userInfo);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("台州市")!=-1){
				
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?dataset=sbcx_sheng$sbcx_grjbxx";
				String url3 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_ylzh";
				getHtml(url2, webClient);
				Page page = getHtml(url2, webClient);
				String html2 = page.getWebResponse().getContentAsString();
				System.out.println(html2);
				Page page2 = getHtml(url3, webClient);
				String html4 = page2.getWebResponse().getContentAsString();
				System.out.println(html4);
				String url0 = "https://puser.zjzwfw.gov.cn/sso/usp.do?action=user";
				Page html = (HtmlPage) getHtml(url0, webClient);
				String html6 = html.getWebResponse().getContentAsString();

				InsuranceSZZheJiangUserInfo userInfo = insuranceSZZheJiangParser.getuserinfo(html2,html4,parameter.getTaskId(),html6);

				if(userInfo!=null){
					insuranceSZZheJiangUserInfoRepository.save(userInfo);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
					
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("金华市")!=-1){
				
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/MjYwLThhNGEtN2UwZTQ5NmZiNGNj?dataset=sbcx_sheng$sbcx_grjbxx";
				String url3 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_ylzh";
				getHtml(url2, webClient);
				Page page = getHtml(url2, webClient);
				String html2 = page.getWebResponse().getContentAsString();
				System.out.println(html2);
				Page page2 = getHtml(url3, webClient);
				String html4 = page2.getWebResponse().getContentAsString();
				System.out.println(html4);
				String url0 = "https://puser.zjzwfw.gov.cn/sso/usp.do?action=user";
				Page html = (HtmlPage) getHtml(url0, webClient);
				String html6 = html.getWebResponse().getContentAsString();

				InsuranceSZZheJiangUserInfo userInfo = insuranceSZZheJiangParser.getuserinfo(html2,html4,parameter.getTaskId(),html6);

				if(userInfo!=null){
					insuranceSZZheJiangUserInfoRepository.save(userInfo);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("义乌市")!=-1){
				
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NDlhLTgwMzAtNjZiOTY1ZGNjMTdj?dataset=sbcx_sheng$sbcx_grjbxx";
				String url3 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?dataset=sbcx_sheng$sbcx_ylzh";
				getHtml(url2, webClient);
				Page page = getHtml(url2, webClient);
				String html2 = page.getWebResponse().getContentAsString();
				System.out.println(html2);
				Page page2 = getHtml(url3, webClient);
				String html4 = page2.getWebResponse().getContentAsString();
				System.out.println(html4);
				String url0 = "https://puser.zjzwfw.gov.cn/sso/usp.do?action=user";
				Page html = (HtmlPage) getHtml(url0, webClient);
				String html6 = html.getWebResponse().getContentAsString();

				InsuranceSZZheJiangUserInfo userInfo = insuranceSZZheJiangParser.getuserinfo(html2,html4,parameter.getTaskId(),html6);

				if(userInfo!=null){
					insuranceSZZheJiangUserInfoRepository.save(userInfo);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
					taskInsurance.setUserInfoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.zhejiang.getuserinfo", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}

		return new AsyncResult<String>("200");
	}
	public Page getHtml(String url, WebClient webClient) throws Exception {
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		webClient.setJavaScriptTimeout(50000); 
		webClient.getOptions().setTimeout(50000); // 15->60 
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
	}
}
