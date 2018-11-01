package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sanming.InsuranceSanmingPension;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sanming.InsuranceSanmingPensionRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSanmingParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sanming"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sanming"})
public class InsuranceSanmingPensionService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSanmingParser insuranceSanmingParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSanmingPensionRepository insuranceSanmingPensionRepository;
	@Autowired
	private InsuranceSanmingInjuryService insuranceSanmingInjuryService;

	/**
	 * 获取养老信息
	 * @param webClient
	 * @param taskid
	 */
	public void getPension(WebClient webClient, String taskid) {
		
		tracer.addTag("getPension.taskid",taskid);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskid);
		
		try {
			String pensionUrl = "http://www.smsic.cn:8080/sheB/ac10a.do?action=0";
			HtmlPage html = (HtmlPage) insuranceService.getHtml(pensionUrl,webClient);
			tracer.addTag("养老信息页面 所需参数", "<xmp>"+html.getWebResponse().getContentAsString()+"</xmp>");
			List<String> params = insuranceSanmingParser.parserPensionParam(html);
			if(null == params){
				tracer.addTag("获取养老信息所需参数页面失败！", "error");
				insuranceService.changeCrawlerStatus("【养老信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
						404, taskInsurance);
				//获取工伤生育信息
				insuranceSanmingInjuryService.getInjury(webClient,taskid);
			}else{
				for(int i=0;i<params.size();i++){
					String detail = "http://www.smsic.cn:8080/sheB/ac10a.do?action=1&baeah2="+params.get(i);
					HtmlPage detailHtml = (HtmlPage) insuranceService.getHtml(detail,webClient);
					List<InsuranceSanmingPension> list = insuranceSanmingParser.parserPensionDetail(detailHtml,taskid);
					if(null == list){
						continue;
					}else{
						insuranceSanmingPensionRepository.saveAll(list);
						insuranceService.changeCrawlerStatus("【养老信息】采集成功", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
								200, taskInsurance);
					}
				}
				
				//获取工伤生育信息
				insuranceSanmingInjuryService.getInjury(webClient,taskid);
			}
		} catch (Exception e) {
			tracer.addTag("获取养老信息页面失败！", e.getMessage());
			insuranceService.changeCrawlerStatus("【养老信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
					404, taskInsurance);
			
			//获取工伤生育信息
			insuranceSanmingInjuryService.getInjury(webClient,taskid);
		}
	}

}
