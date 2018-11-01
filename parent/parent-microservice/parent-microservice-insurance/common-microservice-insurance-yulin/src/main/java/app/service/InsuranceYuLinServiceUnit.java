package app.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinGongShang;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinShengYu;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinShiYe;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinUserInfo;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinYangLao;
import com.microservice.dao.entity.crawler.insurance.yulin.InsuranceYuLinYiLiao;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.yulin.InsuranceYuLinGongShangRepository;
import com.microservice.dao.repository.crawler.insurance.yulin.InsuranceYuLinShengYuRepository;
import com.microservice.dao.repository.crawler.insurance.yulin.InsuranceYuLinShiYeRepository;
import com.microservice.dao.repository.crawler.insurance.yulin.InsuranceYuLinUserInfoRepository;
import com.microservice.dao.repository.crawler.insurance.yulin.InsuranceYuLinYangLaoRepository;
import com.microservice.dao.repository.crawler.insurance.yulin.InsuranceYuLinYiLiaoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceYuLinParser;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.yulin" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.yulin" })
public class InsuranceYuLinServiceUnit {
	@Autowired
	private TracerLog tracer;
	@Autowired
	private InsuranceYuLinParser insuranceYuLinParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceYuLinUserInfoRepository insuranceYuLinUserInfoRepository;
	@Autowired
	private InsuranceYuLinYangLaoRepository insuranceYuLinYangLaoRepository;
	@Autowired
	private InsuranceYuLinYiLiaoRepository insuranceYuLinYiLiaoRepository;
	@Autowired
	private InsuranceYuLinGongShangRepository insuranceYuLinGongShangRepository;
	@Autowired
	private InsuranceYuLinShiYeRepository insuranceYuLinShiYeRepository;
	@Autowired
	private InsuranceYuLinShengYuRepository insuranceYuLinShengYuRepository;
	/***
	 * 个人信息
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getuserinfo(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String url3 = "http://222.83.253.69/ylsbwz/grxxCx";
			Page page2 = getHtml(url3, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			System.out.println(contentAsString2);
			if(contentAsString2!=null){
				InsuranceYuLinUserInfo userinfo = insuranceYuLinParser.getuserinfo(contentAsString2,taskInsurance.getTaskid());
				if(userinfo!=null){
					insuranceYuLinUserInfoRepository.save(userinfo);
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
			}else{
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
				taskInsurance.setUserInfoStatus(500);
				taskInsuranceRepository.save(taskInsurance);
			}
		} catch (Exception e) {
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getDescription());
			taskInsurance.setUserInfoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}
		return new AsyncResult<String>("200");
	}
	/***
	 * 养老保险流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getyanglaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
					+ "xzlxdmxz=11";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			/**
			 * 获取页数
			 */
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);
			List<InsuranceYuLinYangLao> list = new ArrayList<>();
			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?"
						+ "xzlxdmxz=11"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				if(html.indexOf("查询不到数据")==-1){
					List<InsuranceYuLinYangLao> getyanglaoMsg = insuranceYuLinParser.getyanglaoMsg(taskInsurance.getTaskid(),html,j);
					if(getyanglaoMsg!=null){
						for (InsuranceYuLinYangLao insuranceYuLinYangLao : getyanglaoMsg) {
							insuranceYuLinYangLao.setPagenum(j);
							list.add(insuranceYuLinYangLao);
						}
					}else{
						System.out.println("第"+j+"页数据为空");
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
						taskInsurance.setError_message("第"+j+"页数据为空");
						taskInsurance.setYanglaoStatus(201);
						taskInsuranceRepository.save(taskInsurance);
					}
				}else{
					System.out.println("查询不到数据");
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
					taskInsurance.setError_message("查询不到数据");
					taskInsurance.setYanglaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}

			}

			if(list!=null){
				insuranceYuLinYangLaoRepository.saveAll(list);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getDescription());
				taskInsurance.setYanglaoStatus(200);
				taskInsuranceRepository.save(taskInsurance);
			}
			
		} catch (Exception e) {
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
	public Page gethtmlPost(WebClient webClient, List<NameValuePair> paramsList, String url) throws FailingHttpStatusCodeException, IOException {

		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.POST);
		if (paramsList != null) {
			webRequest.setRequestParameters(paramsList);
		}
		Page searchPage = webClient.getPage(webRequest);
		if (searchPage == null) {
			return null;
		}
		return searchPage;

	}
	/**
	 * 医疗保险流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getyiliaoMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		
		try {
			String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=31";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			/**
			 * 获取页数
			 */
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);
			List<InsuranceYuLinYiLiao> list = new ArrayList<>();
			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=31"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				if(html.indexOf("查询不到数据")==-1){
					List<InsuranceYuLinYiLiao> getyiliaoMsg = insuranceYuLinParser.getyiliaoMsg(taskInsurance.getTaskid(),html,j);
					if(getyiliaoMsg!=null){
						for (InsuranceYuLinYiLiao insuranceYuLinYiLiao : getyiliaoMsg) {
							insuranceYuLinYiLiao.setPagenum(j);
							list.add(insuranceYuLinYiLiao);
						}
					}else{
						System.out.println("第"+j+"页数据为空");
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
						taskInsurance.setError_message("第"+j+"页数据为空");
						taskInsurance.setYiliaoStatus(201);
						taskInsuranceRepository.save(taskInsurance);
					}
				}else{
					System.out.println("查询不到数据");
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
					taskInsurance.setError_message("查询不到数据");
					taskInsurance.setYiliaoStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}

			}

			if(list!=null){
				insuranceYuLinYiLiaoRepository.saveAll(list);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
				taskInsurance.setYiliaoStatus(200);
				taskInsuranceRepository.save(taskInsurance);
			}
			
		} catch (Exception e) {
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getDescription());
			taskInsurance.setYiliaoStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}

		return new AsyncResult<String>("200");
		
	}
	/***
	 * 工伤保险流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getgongshangMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=41";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			/**
			 * 获取页数
			 */
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);
			List<InsuranceYuLinGongShang> list = new ArrayList<>();
			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=41"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				if(html.indexOf("查询不到数据")==-1){
					List<InsuranceYuLinGongShang> getgonshangMsg = insuranceYuLinParser.getgonshangMsg(taskInsurance.getTaskid(),html,j);
					if(getgonshangMsg!=null){
						for (InsuranceYuLinGongShang insuranceYuLinGongShang : getgonshangMsg) {
							insuranceYuLinGongShang.setPagenum(j);
							list.add(insuranceYuLinGongShang);
						}
					}else{
						System.out.println("第"+j+"页数据为空");
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
						taskInsurance.setError_message("第"+j+"页数据为空");
						taskInsurance.setGongshangStatus(201);
						taskInsuranceRepository.save(taskInsurance);
					}
				}else{
					System.out.println("查询不到数据");
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
					taskInsurance.setError_message("查询不到数据");
					taskInsurance.setGongshangStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}

			}

			if(list!=null){
				insuranceYuLinGongShangRepository.saveAll(list);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
				taskInsurance.setGongshangStatus(200);
				taskInsuranceRepository.save(taskInsurance);
			}
			
		} catch (Exception e) {
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getDescription());
			taskInsurance.setGongshangStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}

		return new AsyncResult<String>("200");
		
	}
	/***
	 * 失业保险流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getshiyeMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=21";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			/**
			 * 获取页数
			 */
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);
			List<InsuranceYuLinShiYe> list = new ArrayList<>();
			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=21"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				if(html.indexOf("查询不到数据")==-1){
					List<InsuranceYuLinShiYe> getshiyeMsg = insuranceYuLinParser.getshiyeMsg(taskInsurance.getTaskid(),html,j);
					if(getshiyeMsg!=null){
						for (InsuranceYuLinShiYe insuranceYuLinShiYe : getshiyeMsg) {
							insuranceYuLinShiYe.setPagenum(j);
							list.add(insuranceYuLinShiYe);
						}
					}else{
						System.out.println("第"+j+"页数据为空");
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
						taskInsurance.setError_message("第"+j+"页数据为空");
						taskInsurance.setShiyeStatus(201);
						taskInsuranceRepository.save(taskInsurance);
					}
				}else{
					System.out.println("查询不到数据");
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
					taskInsurance.setError_message("查询不到数据");
					taskInsurance.setShiyeStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}

			}

			if(list!=null){
				insuranceYuLinShiYeRepository.saveAll(list);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
				taskInsurance.setShiyeStatus(200);
				taskInsuranceRepository.save(taskInsurance);
			}
			
		} catch (Exception e) {
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShiyeStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}

		return new AsyncResult<String>("200");
	}
	/**
	 * 生育保险流水
	 * @param parameter
	 * @param taskInsurance
	 * @param webClient
	 * @return
	 */
	public Future<String> getshengyuMsg(InsuranceRequestParameters parameter, TaskInsurance taskInsurance,
			WebClient webClient) {
		try {
			String url4 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=51";
			Page page2 = getHtml(url4, webClient);
			String contentAsString2 = page2.getWebResponse().getContentAsString();
			Document document = Jsoup.parse(contentAsString2);
			/**
			 * 获取页数
			 */
			String text = document.getElementsByClass("STYLE2").get(0).getElementById("maxPage").text();
			int i = Integer.parseInt(text);
			List<InsuranceYuLinShengYu> list = new ArrayList<>();
			for (int j = 1; j < i + 1; j++) {
				String url5 = "http://222.83.253.69/ylsbwz/getYl_ryyjsjListByXzlx?xzlxdmxz=51"
						+ "&page="+j;
				Page page4 = gethtmlPost(webClient, null, url5);
				String html = page4.getWebResponse().getContentAsString();
				if(html.indexOf("查询不到数据")==-1){
					List<InsuranceYuLinShengYu> getshengyuMsg = insuranceYuLinParser.getshengyuMsg(taskInsurance.getTaskid(),html,j);
					if(getshengyuMsg!=null){
						for (InsuranceYuLinShengYu insuranceYuLinShengYu : getshengyuMsg) {
							insuranceYuLinShengYu.setPagenum(j);
							list.add(insuranceYuLinShengYu);
						}
					}else{
						System.out.println("第"+j+"页数据为空");
						taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
						taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
						taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
						taskInsurance.setError_message("第"+j+"页数据为空");
						taskInsurance.setShengyuStatus(201);
						taskInsuranceRepository.save(taskInsurance);
					}
				}else{
					System.out.println("查询不到数据");
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
					taskInsurance.setError_message("查询不到数据");
					taskInsurance.setShengyuStatus(201);
					taskInsuranceRepository.save(taskInsurance);
				}

			}

			if(list!=null){
				insuranceYuLinShengYuRepository.saveAll(list);
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhasestatus());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getDescription());
				taskInsurance.setShengyuStatus(200);
				taskInsuranceRepository.save(taskInsurance);
			}
			
		} catch (Exception e) {
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhasestatus());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getDescription());
			taskInsurance.setShengyuStatus(404);
			taskInsuranceRepository.save(taskInsurance);
		}

		return new AsyncResult<String>("200");
	}
}
