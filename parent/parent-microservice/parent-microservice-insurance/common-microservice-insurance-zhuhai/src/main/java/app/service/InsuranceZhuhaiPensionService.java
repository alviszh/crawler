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
import com.microservice.dao.entity.crawler.insurance.zhuhai.InsuranceZhuhaiUnify;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zhuhai.InsuranceZhuhaiUnifyRepository;

import app.commontracerlog.TracerLog;
import app.parser.InsuranceZhuhaiParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.zhuhai"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.zhuhai"})
public class InsuranceZhuhaiPensionService {
	
	@Autowired
	private TracerLog tracer; 
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceZhuhaiParser insuranceZhuhaiParser;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceZhuhaiUnifyRepository insuranceZhuhaiUnifyRepository;
	@Autowired
	private InsuranceZhuhaiUnemploymentService insuranceZhuhaiUnemploymentService;

	/**
	 * 获取养老信息
	 * @param webClient
	 * @param taskid
	 */
	public void getPension(WebClient webClient, String taskid) {
		tracer.addTag("getPension.taskid",taskid);
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(taskid);
		for(int i = 1; i<30; i++){
			String pensionUrl = "https://www.zhldj.gov.cn/zhrsClient/social.do?doMethod=listFee&insured_kind=1&curr_page="+String.valueOf(i);
			try {
				HtmlPage html = (HtmlPage) insuranceService.getHtml(pensionUrl,webClient);
				tracer.addTag("养老信息页面 "+i, html.getWebResponse().getContentAsString());
				List<InsuranceZhuhaiUnify> list = insuranceZhuhaiParser.parserInsurance(html,"养老保险",taskid);
				if(null != list){
					insuranceZhuhaiUnifyRepository.saveAll(list);
					insuranceService.changeCrawlerStatus("【养老信息】采集成功", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							200, taskInsurance);
				}else{
					insuranceService.changeCrawlerStatus("【养老信息】采集成功", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
							200, taskInsurance);
					break;
				}
			} catch (Exception e) {
				tracer.addTag("获取养老信息页面失败！", e.getMessage());
				insuranceService.changeCrawlerStatus("【养老信息】获取超时", InsuranceStatusCode.INSURANCE_CRAWLER_YANGLAO_MSG_SUCCESS.getPhase(), 
						404, taskInsurance);
				continue;
			}		
		}
		//获取失业保险
		insuranceZhuhaiUnemploymentService.getUnemployment(webClient,taskid);
		
		
		
	}

}
