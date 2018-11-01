package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sanming.InsuranceSanmingUserinfo;
import com.microservice.dao.repository.crawler.insurance.sanming.InsuranceSanmingUserinfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceSanmingParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sanming"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sanming"})
public class InsuranceSanmingUserinfoService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceSanmingParser insuranceSanmingParser;
	@Autowired
	private InsuranceSanmingUserinfoRepository insuranceSanmingUserinfoRepository;
	@Autowired
	private InsuranceSanmingPensionService insuranceSanmingPensionService;

	/**
	 * 获取用户信息
	 * @param webClient
	 * @param taskInsurance
	 */
	public void getUserinfo(WebClient webClient, TaskInsurance taskInsurance) {
		tracer.addTag("getuserinfo.taskid", taskInsurance.getTaskid());
		String userinfoUrl = "http://www.smsic.cn:8080/sheB/sboper.do?action=6";
		try {
			HtmlPage html = (HtmlPage) insuranceService.getHtml(userinfoUrl,webClient);
			tracer.addTag("用户信息页面", "<xmp>"+html.getWebResponse().getContentAsString()+"</xmp>");
			InsuranceSanmingUserinfo insuranceSanmingUserinfo = insuranceSanmingParser.parserUserinfo(html.getWebResponse().getContentAsString());
			if(null != insuranceSanmingUserinfo){
				insuranceSanmingUserinfo.setTaskid(taskInsurance.getTaskid());
				insuranceSanmingUserinfoRepository.save(insuranceSanmingUserinfo);				
				tracer.addTag("用户信息数据已入库", "success");
				insuranceService.changeCrawlerStatus("【用户信息】采集成功！", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
						200, taskInsurance);
				//获取养老保险
				insuranceSanmingPensionService.getPension(webClient,taskInsurance.getTaskid());
			}else{
				tracer.addTag("解析用户信息页面失败！", "error");
				insuranceService.changeCrawlerStatus("【用户信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
						404, taskInsurance);
				//获取养老保险
				insuranceSanmingPensionService.getPension(webClient,taskInsurance.getTaskid());
			}
		
		} catch (Exception e) {
			tracer.addTag("获取用户信息页面失败！", e.getMessage());
			insuranceService.changeCrawlerStatus("【用户信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					404, taskInsurance);
			//获取养老保险
			insuranceSanmingPensionService.getPension(webClient,taskInsurance.getTaskid());
		}
	}

}
