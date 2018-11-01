package app.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sanming.InsuranceSanmingInjury;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sanming.InsuranceSanmingInjuryRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSanmingParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sanming"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sanming"})
public class InsuranceSanmingInjuryService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSanmingParser insuranceSanmingParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSanmingInjuryRepository insuranceSanmingInjuryRepository;
	
	/**
	 * 获取工伤和生育保险
	 * @param webClient
	 * @param taskid
	 */
	public void getInjury(WebClient webClient, String taskid) {
		tracer.addTag("getInjury.taskid",taskid);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskid);
		
		String injuryUrl = "http://www.smsic.cn:8080/sheB/gssy.do?action=0";
		try {
			HtmlPage html = (HtmlPage) insuranceService.getHtml(injuryUrl,webClient);
			String page = html.getWebResponse().getContentAsString();
			Document doc = Jsoup.parse(page);
			String countNum = doc.getElementById("RecuSetCountys").attr("value");
			tracer.addTag("获取的总数：", countNum);
			if(StringUtils.isNotBlank(countNum)){
				
				for(int i = 1;i<=Integer.parseInt(countNum)/10+1;i++){				
					List<String> params = getInjuryParams(webClient,injuryUrl,countNum,i);
					if(null == params){
						continue;
					}else{						
						getInjuryDetail(webClient,params,taskid);					
					}
				}
				//爬取完毕
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
	            taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
	            taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
	            taskInsurance.setFinished(true);
	            taskInsuranceRepository.save(taskInsurance);
				
			}else{
				tracer.addTag("工伤和生育信息获取总页数失败！", "error");
				insuranceService.changeCrawlerStatus("【工伤和生育信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
						404, taskInsurance);
				taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
	            taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
	            taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
	            taskInsurance.setFinished(true);
	            taskInsuranceRepository.save(taskInsurance);
				
				
				
			}
		} catch (Exception e) {
			tracer.addTag("获取工伤和生育信息所需参数页面失败！", e.getMessage());
			insuranceService.changeCrawlerStatus("【工伤和生育信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_GONGSHANG_MSG_SUCCESS.getPhase(), 
					404, taskInsurance);
			taskInsurance.setDescription(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getDescription());
            taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhase());
            taskInsurance.setPhase_status(InsuranceStatusCode.INSURANCE_CRAWLER_ALL_SUCCESS.getPhasestatus());
            taskInsurance.setFinished(true);
            taskInsuranceRepository.save(taskInsurance);
		}
		
	}

	/**
	 * 获取缴费详情
	 * @param webClient
	 * @param params
	 * @param taskid
	 * @throws Exception
	 */
	private void getInjuryDetail(WebClient webClient, List<String> params, String taskid) throws Exception {
		for(String param : params){
			String detailUrl = "http://www.smsic.cn:8080/sheB/gssy.do?action=1&id="+param;
			HtmlPage html = (HtmlPage) insuranceService.getHtml(detailUrl,webClient);
			String detailHtml = html.getWebResponse().getContentAsString();
			tracer.addTag("缴费详情页面：", "<xmp>"+detailHtml+"</xmp>");
			
			InsuranceSanmingInjury insuranceSanmingInjury = insuranceSanmingParser.parserInjury(taskid,detailHtml);
			if(null == insuranceSanmingInjury){
				continue;
			}else{
				insuranceSanmingInjuryRepository.save(insuranceSanmingInjury);
			}
			
		}
		
		
	}

	/**
	 * 再获取缴费详情前 ，需要得到请求必带的参数
	 * @param webClient
	 * @param injuryUrl
	 * @param countNum
	 * @param i
	 * @return
	 * @throws Exception
	 */
	private List<String> getInjuryParams(WebClient webClient, String injuryUrl, String countNum, int i) throws Exception {
		WebRequest requestSettings = new WebRequest(new URL(injuryUrl), HttpMethod.POST); 
		
		requestSettings.setAdditionalHeader("Referer", "http://www.smsic.cn:8080/sheB/gssy.do?action=0");
		requestSettings.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		requestSettings.setAdditionalHeader("Accept-Encoding", "gzip, deflate");
		requestSettings.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.6");
		requestSettings.setAdditionalHeader("Connection", "keep-alive");
		requestSettings.setAdditionalHeader("Host", "www.smsic.cn:8080"); 
		requestSettings.setAdditionalHeader("Origin", "http://www.smsic.cn:8080");
		requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded");
		requestSettings.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
//		requestSettings.setRequestParameters(new ArrayList<NameValuePair>());
//		
//		requestSettings.getRequestParameters().add(new NameValuePair("callback", "jQuery183008660428427514177_"+String.valueOf(System.currentTimeMillis())));
//		requestSettings.getRequestParameters().add(new NameValuePair("_", String.valueOf(System.currentTimeMillis())));		
		
		String requestBody = "pageCountys=10&RecuSetCountys="+countNum+"&pageIndexys="+i+"&aae140=-1&page=1";		
		requestSettings.setRequestBody(requestBody);	
		HtmlPage page = webClient.getPage(requestSettings);
		List<String> params = parserParam(page,page.getWebClient());
		return params;
	}

	private List<String> parserParam(HtmlPage html,WebClient webClient){
		
		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(html.getWebResponse().getContentAsString());
		try{
			Elements inputs = doc.select("[value=详细]");
			for(int i=0;i<inputs.size();i++){
				String onClick = inputs.get(i).attr("onclick");
				String code = onClick.substring(onClick.indexOf("\'")+1, onClick.length()-2).trim();
				list.add(code);
			}
			
			return list;
		}catch(Exception e){
			return null;
		}
	}

}
