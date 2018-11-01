package app.service;

import java.net.URL;
import java.util.List;
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
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangGongShanginfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangGongShangInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZZheJiangParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.sz.zhejiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.sz.zhejiang" })
public class InsuranceSZZheJiangGongShangService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZZheJiangParser insuranceSZZheJiangParser;
	@Autowired
	private InsuranceSZZheJiangGongShangInfoRepository insuranceSZZheJiangGongShangInfoRepository;
	/***
	 * 工伤保险缴费流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getgongshangMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		String city = taskInsurance.getCity();
		try {
			if(city.indexOf("衢州市")!=-1){
				String url5 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
						+ "dataset=sbcx_sheng$sbcx_gssyxx"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page = getHtml(url5, webClient);
				String html = page.getWebResponse().getContentAsString();
				System.out.println(html);
				List<InsuranceSZZheJiangGongShanginfo> list = insuranceSZZheJiangParser.getgongshang(parameter.getTaskId(),html);
				if(list!=null){
					insuranceSZZheJiangGongShangInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("丽水市")!=-1){
				String url5 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?"
						+ "dataset=sbcx_sheng$sbcx_ybjzxx"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page = getHtml(url5, webClient);
				String html = page.getWebResponse().getContentAsString();
				System.out.println(html);
				List<InsuranceSZZheJiangGongShanginfo> list = insuranceSZZheJiangParser.getgongshang(parameter.getTaskId(),html);
				if(list!=null){
					insuranceSZZheJiangGongShangInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("台州市")!=-1){
				String url5 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/ZmUxLWJlYzktMDgxMmI2MGQ3NWI3?"
						+ "dataset=sbcx_sheng$sbcx_gssyxx"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page = getHtml(url5, webClient);
				String html = page.getWebResponse().getContentAsString();
				System.out.println(html);
				List<InsuranceSZZheJiangGongShanginfo> list = insuranceSZZheJiangParser.getgongshang(parameter.getTaskId(),html);
				if(list!=null){
					insuranceSZZheJiangGongShangInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("金华市")!=-1){
				String url5 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/MjYwLThhNGEtN2UwZTQ5NmZiNGNj?"
						+ "dataset=sbcx_sheng$sbcx_gssyxx"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page = getHtml(url5, webClient);
				String html = page.getWebResponse().getContentAsString();
				System.out.println(html);
				List<InsuranceSZZheJiangGongShanginfo> list = insuranceSZZheJiangParser.getgongshang(parameter.getTaskId(),html);
				if(list!=null){
					insuranceSZZheJiangGongShangInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("义乌市")!=-1){
				String url5 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NDlhLTgwMzAtNjZiOTY1ZGNjMTdj?"
						+ "dataset=sbcx_sheng$sbcx_gssyxx"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page = getHtml(url5, webClient);
				String html = page.getWebResponse().getContentAsString();
				System.out.println(html);
				List<InsuranceSZZheJiangGongShanginfo> list = insuranceSZZheJiangParser.getgongshang(parameter.getTaskId(),html);
				if(list!=null){
					insuranceSZZheJiangGongShangInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setGongshangStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}
		} catch (Exception e) {
			tracer.addTag("action.zhejiang.getgongshangMsg", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
			taskInsurance.setGongshangStatus(404);
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
