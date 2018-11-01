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
import com.microservice.dao.entity.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangYangLaoinfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.zhejiang.InsuranceSZZheJiangYangLaoInfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSZZheJiangParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.sz.zhejiang" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.sz.zhejiang" })
public class InsuranceSZZheJiangYangLaoService {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZZheJiangParser insuranceSZZheJiangParser;
	@Autowired
	private InsuranceSZZheJiangYangLaoInfoRepository insuranceSZZheJiangYangLaoInfoRepository;
	/***
	 * 养老保险缴费流水
	 * @param webClient
	 * @param taskInsurance
	 * @param parameter
	 * @return
	 */
	public Future<String> getyanglaoInfo(WebClient webClient, TaskInsurance taskInsurance,
			InsuranceRequestParameters parameter) {
		String city = taskInsurance.getCity();
		try {
			if(city.indexOf("衢州市")!=-1){
				String url4 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NjlhLWJlNDUtY2Y3M2NkNGJiNWU2?"
						+ "dataset=sbcx_sheng$sbcx_yljf"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page5 = getHtml(url4, webClient);
				String html5 = page5.getWebResponse().getContentAsString();
				System.out.println(html5);
				List<InsuranceSZZheJiangYangLaoinfo> list = insuranceSZZheJiangParser.getyanglaoinfo(html5,parameter.getTaskId());
				if(list!=null){
					insuranceSZZheJiangYangLaoInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("丽水市")!=-1){
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NWU5LTkxNTUtNWE2MjNiOTkxNTI4?"
						+ "dataset=sbcx_sheng$sbcx_yljf"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page4 = getHtml(url2, webClient);
				String html4 = page4.getWebResponse().getContentAsString();
				System.out.println(html4);
				List<InsuranceSZZheJiangYangLaoinfo> list = insuranceSZZheJiangParser.getyanglaoinfo(html4,parameter.getTaskId());
				if(list!=null){
					insuranceSZZheJiangYangLaoInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("台州市")!=-1){
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/ZmUxLWJlYzktMDgxMmI2MGQ3NWI3?"
						+ "dataset=sbcx_sheng$sbcx_yljf"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page4 = getHtml(url2, webClient);
				String html4 = page4.getWebResponse().getContentAsString();
				System.out.println(html4);
				List<InsuranceSZZheJiangYangLaoinfo> list = insuranceSZZheJiangParser.getyanglaoinfo(html4,parameter.getTaskId());
				if(list!=null){
					insuranceSZZheJiangYangLaoInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("金华市")!=-1){
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/MjYwLThhNGEtN2UwZTQ5NmZiNGNj?"
						+ "dataset=sbcx_sheng$sbcx_yljf"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page4 = getHtml(url2, webClient);
				String html4 = page4.getWebResponse().getContentAsString();
				System.out.println(html4);
				List<InsuranceSZZheJiangYangLaoinfo> list = insuranceSZZheJiangParser.getyanglaoinfo(html4,parameter.getTaskId());
				if(list!=null){
					insuranceSZZheJiangYangLaoInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}else if(city.indexOf("义乌市")!=-1){
				String url2 = "http://sbcx.pcyyhj.zjzwfw.gov.cn/proxy/services/BizData/NDlhLTgwMzAtNjZiOTY1ZGNjMTdj?"
						+ "dataset=sbcx_sheng$sbcx_yljf"
						+ "&limit=500"
						+ "&pageNo=1"
						+ "&pageSize=500";
				Page page4 = getHtml(url2, webClient);
				String html4 = page4.getWebResponse().getContentAsString();
				System.out.println(html4);
				List<InsuranceSZZheJiangYangLaoinfo> list = insuranceSZZheJiangParser.getyanglaoinfo(html4,parameter.getTaskId());
				if(list!=null){
					insuranceSZZheJiangYangLaoInfoRepository.saveAll(list);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(200);
					taskInsuranceRepository.save(taskInsurance);
				}else{
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setYanglaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}
			}

		} catch (Exception e) {
			tracer.addTag("action.zhejiang.getyanglaoinfo", e.toString());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYanglaoStatus(404);
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
