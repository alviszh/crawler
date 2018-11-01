package app.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

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

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.paeser.InsuranceHangzhouParser;
import app.service.aop.InsuranceCrawler;
import app.service.aop.InsuranceLogin;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.crawler.microservice.unit.CommonUnit;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouHtml;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouInjury;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouMaternity;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouMedical;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouUnemployment;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsuranceHangzhouUserInfo;
import com.microservice.dao.entity.crawler.insurance.hangzhou.InsurancehangzhouPension;
import com.microservice.dao.entity.crawler.insurance.jiangmen.InsuranceJiangMenMedical;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.hangzhou.InsuranceHangzhouHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.hangzhou.InsuranceHangzhouInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.hangzhou.InsuranceHangzhouMaternityRepository;
import com.microservice.dao.repository.crawler.insurance.hangzhou.InsuranceHangzhouMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.hangzhou.InsuranceHangzhouPensionRepository;
import com.microservice.dao.repository.crawler.insurance.hangzhou.InsuranceHangzhouUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.hangzhou.InsuranceHangzhouUserInfoRepository;
import com.module.htmlunit.WebCrawler;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.hangzhou"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.hangzhou"})
public class InsuranceHangzhouService implements InsuranceLogin,InsuranceCrawler{
public static final Logger log = LoggerFactory.getLogger(InsuranceHangzhouService.class);
	
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceHangzhouParser insuranceHangzhouParser;
	@Autowired
	private InsuranceHangzhouUserInfoRepository insuranceHangzhouUserInfoRepository;
	@Autowired
	private TracerLog tracer;
	@Autowired
	private AsyncHangzhouGetAllDataService asyncHangzhouGetAllDataService;
	@Autowired
	private InsuranceHangzhouUnemploymentRepository insuranceHangzhouUnemploymentRepository;
	@Autowired
	private InsuranceHangzhouInjuryRepository insuranceHangzhouInjuryRepository;
	@Autowired
	private InsuranceHangzhouMaternityRepository insuranceHangzhouMaternityRepository;
	@Autowired
	private InsuranceHangzhouMedicalRepository insuranceHangzhouMedicalRepository;
	@Autowired
	private InsuranceHangzhouPensionRepository insuranceHangzhouPensionRepository;
	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception 
	 */
	@Async
	@Override
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
        TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		
		if(null != taskInsurance){

			WebParam webParam = null;
			try {
				webParam = asyncHangzhouGetAllDataService.login(insuranceRequestParameters);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tracer.addTag("登陆异常",e.toString());
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}
			taskInsurance.setLoginType(insuranceRequestParameters.getLoginType());
			
			if(null == webParam){
				tracer.addTag("ERROR ==>","登录页获取超时！");
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
				taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
				taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
				taskInsurance = taskInsuranceRepository.save(taskInsurance);
			}else{
				HtmlPage page = webParam.getPage();
				if(null != page){
					String html = page.getWebResponse().getContentAsString();
					//System.out.println(html);
					tracer.addTag("登陆html", html);
					//String html = webParam.getErrorMsg();
					if(html.contains("市民邮箱验证错误")||html.contains("用户信息不匹配或密码错误")){
						insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);
					}else if(html.contains("校验码错误")||html.contains("验证码错误")){
						insuranceService.changeLoginStatusCaptError(taskInsurance);
					}else if(html.contains("success")){
						insuranceService.changeLoginStatusSuccess(taskInsurance,page);
					}else{					
						insuranceService.changeLoginStatusIdnumOrPwdError(taskInsurance);	
					}
					
				}else{
					insuranceService.changeLoginStatusTimeOut(taskInsurance);	
				}
			}
						
		}
		return taskInsurance;
	}

	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {
		// TODO Auto-generated method stub
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		tracer.qryKeyValue("parser.crawler.getAllData", insuranceRequestParameters.getTaskId());
		try {
			getUserInfo(insuranceRequestParameters);
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("登陆异常",e.toString());
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
			taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
		} 
		
		for(int j = 11;j<=51;){
			Calendar calendar = Calendar.getInstance();
			for(int i=0;i<15;i++){
				String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
				tracer.qryKeyValue("parser.crawler.getPension", insuranceRequestParameters.getTaskId());
				Set<Cookie> cookies = CommonUnit.transferJsonToSet(taskInsurance.getCookies());
				String url = "http://wsbs.zjhz.hrss.gov.cn/unit/web_zgjf_query/web_zgjf_doQuery.html?m_aae002="+searchYear+"&m_aae140="+j+"&pageNo=1";
				
				WebClient webClient = insuranceService.getWebClient(cookies);
				try {
					WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
					HtmlPage page = webClient.getPage(webRequest);
				    String html = page.getWebResponse().getContentAsString();
				    tracer.addTag("流水html", html);
				    Document doc = Jsoup.parse(html);
				    Elements ele = doc.select("table.grid tr:nth-child(2) td");
					String payMonth = ele.get(0).text().trim();					//年月
					if (payMonth.equals("")||payMonth.equals(null)){
						break;
					}
					Elements ele1 = doc.select("td.page_num");
					String p = ele1.text().replace(" ", "");
					p = p.substring(p.indexOf("/")+1,p.lastIndexOf("页"));
					int page1 = Integer.parseInt(p);
					if(page1==1){
						if(j==11){
							//养老保险
							List<InsurancehangzhouPension> pension = insuranceHangzhouParser.parser(html,taskInsurance);
							if(pension==null){
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getError_code(), taskInsurance);
							}else{
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getError_code(), taskInsurance);
								insuranceHangzhouPensionRepository.saveAll(pension);
							}
						}else if(j==21){
							//失业保险
							List<InsuranceHangzhouUnemployment> unemployment = insuranceHangzhouParser.unemployment(html,taskInsurance);
							if(unemployment==null){
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getError_code(), taskInsurance);
							}else{
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getError_code(), taskInsurance);
								insuranceHangzhouUnemploymentRepository.saveAll(unemployment);
							}
						}else if(j==31){
							//医疗保险
							List<InsuranceHangzhouMedical> medical = insuranceHangzhouParser.medical(html,taskInsurance);
							if(medical==null){
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getError_code(), taskInsurance);
							}else{
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getError_code(), taskInsurance);
								insuranceHangzhouMedicalRepository.saveAll(medical);
							}
						}else if(j==41){
							//工伤保险
							List<InsuranceHangzhouInjury> injury = insuranceHangzhouParser.injury(html,taskInsurance);
							if(injury==null){
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getError_code(), taskInsurance);
							}else{
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
								insuranceHangzhouInjuryRepository.saveAll(injury);
							}
						}else if(j==51){
							//生育保险
							List<InsuranceHangzhouMaternity> maternity = insuranceHangzhouParser.maternity(html,taskInsurance);
							if(maternity==null){
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getError_code(), taskInsurance);
							}else{
								insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
										InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getError_code(), taskInsurance);
								insuranceHangzhouMaternityRepository.saveAll(maternity);
							}
						}
						
					}else if(page1>1){
						List<String> list = new ArrayList<String>();
						list.add(html);
						for(int k = 2;k<=page1;k++){
							String url1 = "http://wsbs.zjhz.hrss.gov.cn/unit/web_zgjf_query/web_zgjf_doQuery.html?m_aae002="+searchYear+"&m_aae140="+j+"&pageNo="+page1+"";
							WebRequest webRequest1 = new WebRequest(new URL(url1), HttpMethod.GET);
							HtmlPage page2 = webClient.getPage(webRequest1);
						    String html1 = page2.getWebResponse().getContentAsString();
						    list.add(html);
						}
						for(String html2:list){
							if(j==11){
								//养老保险
								List<InsurancehangzhouPension> pension = insuranceHangzhouParser.parser(html,taskInsurance);
								if(pension==null){
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_AGED_FAILUE.getError_code(), taskInsurance);
								}else{
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_AGED_SUCCESS.getError_code(), taskInsurance);
									insuranceHangzhouPensionRepository.saveAll(pension);
								}
							}else if(j==21){
								//失业保险
								List<InsuranceHangzhouUnemployment> unemployment = insuranceHangzhouParser.unemployment(html,taskInsurance);
								if(unemployment==null){
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_FAILUE.getError_code(), taskInsurance);
								}else{
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_SHIYE_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_UNEMPLOYMENT_SUCCESS.getError_code(), taskInsurance);
									insuranceHangzhouUnemploymentRepository.saveAll(unemployment);
								}
							}else if(j==31){
								//医疗保险
								List<InsuranceHangzhouMedical> medical = insuranceHangzhouParser.medical(html,taskInsurance);
								if(medical==null){
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_FAILUE.getError_code(), taskInsurance);
								}else{
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_YILIAO_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_MEDICAL_SUCCESS.getError_code(), taskInsurance);
									insuranceHangzhouMedicalRepository.saveAll(medical);
								}
							}else if(j==41){
								//工伤保险
								List<InsuranceHangzhouInjury> injury = insuranceHangzhouParser.injury(html,taskInsurance);
								if(injury==null){
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_FAILUE.getError_code(), taskInsurance);
								}else{
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_INJURY_SUCCESS.getError_code(), taskInsurance);
									insuranceHangzhouInjuryRepository.saveAll(injury);
								}
							}else if(j==51){
								//生育保险
								List<InsuranceHangzhouMaternity> maternity = insuranceHangzhouParser.maternity(html,taskInsurance);
								if(maternity==null){
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_FAILUE.getError_code(), taskInsurance);
								}else{
									insuranceService.changeCrawlerStatus(InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getDescription(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_SHENGYU_MSG_SUCCESS.getPhase(), 
											InsuranceStatusCode.INSURANCE_CRAWLER_MATERNITY_SUCCESS.getError_code(), taskInsurance);
									insuranceHangzhouMaternityRepository.saveAll(maternity);
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					tracer.addTag("登陆异常",e.toString());
					taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getDescription());
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhase());
					taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_AGENT_ERROR.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
				}
	
			}
			j=j+10;
		}
		
		
//		for(int i=0;i<15;i++){
//			String searchYear = String.valueOf(calendar.get(Calendar.YEAR)-i);
//			asyncHangzhouMedicalService.getMedical(insuranceRequestParameters,searchYear);
//		}
		insuranceService.changeCrawlerStatusSuccess(taskInsurance.getTaskid());		
		return taskInsurance;
		
		
	}

	/**
	 * @Des 获取个人信息
	 * @param insuranceRequestParameters
	 * @throws Exception 
	 */
	public void getUserInfo(InsuranceRequestParameters insuranceRequestParameters) throws Exception {
		
		tracer.addTag("parser.login", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebClient webClient = taskInsurance.getClient(taskInsurance.getCookies());
		
		String url = "http://wsbs.zjhz.hrss.gov.cn/person/personInfo/index.html";
		WebRequest requestSettings = new WebRequest(new URL(url), HttpMethod.POST);
		
		//WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
		requestSettings.setAdditionalHeader("Host", "wsbs.zjhz.hrss.gov.cn");
//		webRequest.setAdditionalHeader("Referer", "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp");
//		webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

	    HtmlPage page = webClient.getPage(requestSettings);
	    String html = page.getWebResponse().getContentAsString();
	    tracer.addTag("个人信息html", html);
	    InsuranceHangzhouUserInfo userInfo = insuranceHangzhouParser.htmlParser(html,taskInsurance);
	    if(userInfo!=null||!userInfo.equals(" ")){
	    	insuranceHangzhouUserInfoRepository.save(userInfo);
	    	taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhase());
		    taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getPhasestatus());
			taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_SUCCESS.getError_code());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
	 		tracer.addTag("getUserInfo 个人信息","个人信息已入库!");
	    }else{
	    	taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getDescription());
			taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhase());
		    taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getPhasestatus());
			taskInsurance.setUserInfoStatus(InsuranceStatusCode.INSURANCE_CRAWLER_BASE_INFO_FAILUE.getError_code());
			taskInsurance = taskInsuranceRepository.save(taskInsurance);
	 		tracer.addTag("getUserInfo 个人信息","个人信息入库失败!");
	    }
	   
	    
	}

	
	@HystrixCommand(fallbackMethod = "fallback")
	public String hystrix() {
		tracer.addTag("InsuranceHangzhouService hystrix", "start");
		String url = "http://wsbs.zjhz.hrss.gov.cn/";
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		
		try{
			WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);	
			webRequest.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			webRequest.setAdditionalHeader("Connection", "keep-alive");
			webRequest.setAdditionalHeader("Accept-Encoding", "gzip, deflate, sdch");
			webRequest.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
			webRequest.setAdditionalHeader("Host", "wsbs.zjhz.hrss.gov.cn");
//			webRequest.setAdditionalHeader("Referer", "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp");
//			webRequest.setAdditionalHeader("Upgrade-Insecure-Requests", "1");
			webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
			
			tracer.addTag("hystrix 获取页面之前",url);
			HtmlPage page = webClient.getPage(webRequest);	
			int status = page.getWebResponse().getStatusCode();
			tracer.addTag("hystrix 杭州社保登录页状态码",String.valueOf(status));
			if(200 == status){
				String html = page.getWebResponse().getContentAsString();
				tracer.addTag("hystrix 杭州社保登录页",html);	
				if(html.contains("在此期间系统暂停服务")){
					tracer.addTag("hystrix 获取页面","网站维护");
					return "ERROR";
				}else{
					return "SUCCESS";
				}
			}
			
		}catch(Exception e){
			tracer.addTag("hystrix 杭州社保登录页exception",e.getMessage());
			e.printStackTrace();
		}
		return "ERROR";
	}

	

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
