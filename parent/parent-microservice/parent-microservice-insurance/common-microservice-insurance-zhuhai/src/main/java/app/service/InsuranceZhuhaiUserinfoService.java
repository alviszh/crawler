package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhuhai.InsuranceZhuhaiUserinfo;
import com.microservice.dao.repository.crawler.insurance.zhuhai.InsuranceZhuhaiUserinfoRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceZhuhaiParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhuhai"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhuhai"})
public class InsuranceZhuhaiUserinfoService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceZhuhaiParser insuranceZhuhaiParser;
	@Autowired
	private InsuranceZhuhaiUserinfoRepository insuranceZhuhaiUserinfoRepository;
	@Autowired
	private InsuranceZhuhaiPensionService insuranceZhuhaiPensionService;
	
	/**
	 * 获取用户信息
	 * @param insuranceRequestParameters
	 * @param webClient
	 */
	public void getUserInfo(WebClient webClient, TaskInsurance taskInsurance) {
		tracer.addTag("getuserinfo.taskid", taskInsurance.getTaskid());
		String userInfoUrl = "https://www.zhldj.gov.cn/zhrsClient/social.do";
		try {
			HtmlPage html = (HtmlPage) insuranceService.getHtml(userInfoUrl,webClient);
			tracer.addTag("用户信息页面", "<xmp>"+html.getWebResponse().getContentAsString()+"</xmp>");
			InsuranceZhuhaiUserinfo insuranceZhuhaiUserinfo = insuranceZhuhaiParser.parserUserinfo(html);
			if(null != insuranceZhuhaiUserinfo){
				insuranceZhuhaiUserinfo.setTaskid(taskInsurance.getTaskid());
				insuranceZhuhaiUserinfoRepository.save(insuranceZhuhaiUserinfo);				
				tracer.addTag("用户信息数据已入库", "success");
				insuranceService.changeCrawlerStatus("【用户信息】采集成功！", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
						200, taskInsurance);
				//获取养老保险
				insuranceZhuhaiPensionService.getPension(webClient,taskInsurance.getTaskid());
			}else{
				tracer.addTag("解析用户信息页面失败！", "error");
				insuranceService.changeCrawlerStatus("【用户信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
						404, taskInsurance);
				//获取养老保险
				insuranceZhuhaiPensionService.getPension(webClient,taskInsurance.getTaskid());
			}
			
		} catch (Exception e) {
			tracer.addTag("获取用户信息页面失败！", e.getMessage());
			insuranceService.changeCrawlerStatus("【用户信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_USER_MSG_SUCCESS.getPhase(), 
					404, taskInsurance);
			//获取养老保险
			insuranceZhuhaiPensionService.getPension(webClient,taskInsurance.getTaskid());
		}
		
		
	}

}
