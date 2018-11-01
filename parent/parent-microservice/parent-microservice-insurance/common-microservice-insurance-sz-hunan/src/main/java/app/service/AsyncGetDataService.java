package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanHtml;
import com.microservice.dao.entity.crawler.insurance.sz.hunan.InsuranceSZHunanUserInfo;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hunan.InsuranceSZHunanBearRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hunan.InsuranceSZHunanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hunan.InsuranceSZHunanInjuryRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hunan.InsuranceSZHunanMedicalRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hunan.InsuranceSZHunanPensionRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hunan.InsuranceSZHunanUnemploymentRepository;
import com.microservice.dao.repository.crawler.insurance.sz.hunan.InsuranceSZHunanUserInfoRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceSZHunanParser;

@Component
@EntityScan(basePackages={"com.microservice.dao.entity.crawler.insurance.basic","com.microservice.dao.entity.crawler.insurance.sz.hunan"})
@EnableJpaRepositories(basePackages={"com.microservice.dao.repository.crawler.insurance.basic","com.microservice.dao.repository.crawler.insurance.sz.hunan"})
public class AsyncGetDataService {

	@Autowired
	private InsuranceSZHunanUnemploymentRepository insuranceSZHunanUnemploymentRepository;
	@Autowired
	private InsuranceSZHunanUserInfoRepository insuranceSZHunanUserInfoRepository;
	@Autowired
	private InsuranceSZHunanPensionRepository insuranceSZHunanPensionRepository;
	@Autowired
	private InsuranceSZHunanMedicalRepository insuranceSZHunanMedicalRepository;
	@Autowired
	private InsuranceSZHunanInjuryRepository insuranceSZHunanInjuryRepository;
	@Autowired
	private InsuranceSZHunanBearRepository insuranceSZHunanBearRepository;
	@Autowired
	private InsuranceSZHunanHtmlRepository insuranceSZHunanHtmlRepository;
	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceSZHunanParser insuranceSZHunanParser;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private TracerLog tracer;
	

	/**
	 * @Des 个人信息
	 * @param taskInsurance
	 * @throws Exception 
	 */
	@Async
	public void getUserInfo(TaskInsurance taskInsurance) {
		tracer.addTag("parser.crawler.AsyncGetDataService.getUserInfo", taskInsurance.getTaskid());
		try {
			WebParam<InsuranceSZHunanUserInfo> webParam = insuranceSZHunanParser.getUserInfo(taskInsurance);
			if(null != webParam.getList()){
				List<InsuranceSZHunanUserInfo> list = webParam.getList();
				insuranceSZHunanUserInfoRepository.saveAll(list);
				insuranceService.changeCrawlerStatus("数据采集中，【个人信息】采集成功", "CRAWLER_USER_MSG", 200, taskInsurance);
				tracer.addTag("parser.crawler.AsyncGetDataService.getUserInfo.success", "【个人信息】采集成功"+webParam.getList());
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【个人信息】采集完成", "CRAWLER_USER_MSG", 201, taskInsurance);
				tracer.addTag("parser.crawler.AsyncGetDataService.getUserInfo.fail", "【个人信息】采集完成");
			}
			if(null != webParam.getHtmlPage()){
				InsuranceSZHunanHtml html = new InsuranceSZHunanHtml();
				html.setUrl(webParam.getUrl());
				html.setHtml(webParam.getHtmlPage().asXml());
				html.setPageCount(1);
				html.setType("userinfo");
				html.setTaskid(taskInsurance.getTaskid());
				insuranceSZHunanHtmlRepository.save(html);
				tracer.addTag("parser.crawler.AsyncGetDataService.getUserInfo.html", "个人信息页面保存入库");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.AsyncGetDataService.getUserInfo.error", e.toString());
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
		insuranceService.changeCrawlerStatusSuccess(taskInsurance);
	}
	
	/**
	 * @Des 社保信息
	 * @param taskInsurance
	 * @throws Exception 
	 */
	@Async
	public void getInsuranceInfo(TaskInsurance taskInsurance) {
		tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo", taskInsurance.getTaskid());
		//养老，医疗，工伤，生育，失业险计数
		int pe = 0;
		int me = 0;
		int in = 0;
		int be = 0;
		int un = 0;
		for (int i = 0; i < 10; i++) {
			try {
				WebParam webParam = insuranceSZHunanParser.getInsuranceInfo(taskInsurance, i);
				if(null != webParam.getHtmlPage()){
					InsuranceSZHunanHtml html = new InsuranceSZHunanHtml();
					html.setUrl(webParam.getUrl());
					html.setHtml(webParam.getHtmlPage().asXml());
					html.setPageCount(1);
					html.setType("insuranceInfo");
					html.setTaskid(taskInsurance.getTaskid());
					insuranceSZHunanHtmlRepository.save(html);
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.html."+i, "社保信息页面保存入库");
				}
				
				if(null != webParam.getListPension()){
					insuranceSZHunanPensionRepository.saveAll(webParam.getListPension());
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.pension."+i, "【养老保险信息】采集成功");
					pe++;
				}else{
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.pension."+i, "【养老保险信息】采集完成");
				}
				
				if(null != webParam.getListMedical()){
					insuranceSZHunanMedicalRepository.saveAll(webParam.getListMedical());
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.medical."+i, "【医疗保险信息】采集成功");
					me++;
				}else{
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.medical."+i, "【医疗保险信息】采集完成");
				}
				
				if(null != webParam.getListInjury()){
					insuranceSZHunanInjuryRepository.saveAll(webParam.getListInjury());
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.injury."+i, "【工伤保险信息】采集成功");
					in++;
				}else{
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.injury."+i, "【工伤保险信息】采集完成");
				}
				
				if(null != webParam.getListBear()){
					insuranceSZHunanBearRepository.saveAll(webParam.getListBear());
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.bear."+i, "【生育保险信息】采集成功");
					be++;
				}else{
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.bear."+i, "【生育保险信息】采集完成");
				}
				
				if(null != webParam.getListUnemployment()){
					insuranceSZHunanUnemploymentRepository.saveAll(webParam.getListUnemployment());
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.unemployment."+i, "【失业保险信息】采集成功");
					un++;
				}else{
					tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.unemployment."+i, "【失业保险信息】采集完成");
				}
			} catch (Exception e) {
				e.printStackTrace();
				tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.error", e.toString());
			}
		}
		if(pe > 0){
			insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】采集成功", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.pension.success", "【养老保险信息】采集成功");
		}else{
//			insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】采集完成", "CRAWLER_YANGLAO_MSG", 201, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.pension.fail", "【养老保险信息】采集完成");
		}
		
		if(me > 0){
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】采集成功", "CRAWLER_YILIAO_MSG", 200, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.medical.success", "【医疗保险信息】采集成功");
		}else{
			insuranceService.changeCrawlerStatus("数据采集中，【医疗保险信息】采集完成", "CRAWLER_YILIAO_MSG", 201, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.medical.fail", "【医疗保险信息】采集完成");
		}

		if(in > 0){
			insuranceService.changeCrawlerStatus("数据采集中，【工伤保险信息】采集成功", "CRAWLER_GONGSHANG_MSG", 200, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.injury.success", "【工伤保险信息】采集成功");
		}else{
			insuranceService.changeCrawlerStatus("数据采集中，【工伤保险信息】采集完成", "CRAWLER_GONGSHANG_MSG", 201, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.injury.fail", "【工伤保险信息】采集完成");
		}

		if(be > 0){
			insuranceService.changeCrawlerStatus("数据采集中，【生育保险信息】采集成功", "CRAWLER_SHENGYU_MSG", 200, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.bear.success", "【生育保险信息】采集成功");
		}else{
			insuranceService.changeCrawlerStatus("数据采集中，【生育保险信息】采集完成", "CRAWLER_SHENGYU_MSG", 201, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.bear.fail", "【生育保险信息】采集完成");
		}

		if(un > 0){
			insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】采集成功", "CRAWLER_SHIYE_MSG", 200, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.unemployment.success", "【失业保险信息】采集成功");
		}else{
			insuranceService.changeCrawlerStatus("数据采集中，【失业保险信息】采集完成", "CRAWLER_SHIYE_MSG", 201, taskInsurance);
			tracer.addTag("parser.crawler.AsyncGetDataService.getInsuranceInfo.unemployment.fail", "【失业保险信息】采集完成");
		}
		
		taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
		insuranceService.changeCrawlerStatusSuccess(taskInsurance);
	}

	/**
	 * @param insuranceRequestParameters 
	 * @Des 养老信息，单独通过http://www.hn12333.com:81/comm_front/query/bsjylbxgrzh_query.jsp 网站来获取养老信息
	 * @param taskInsurance
	 * @throws Exception 
	 */
	@Async
	public void getPensionInfo(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		tracer.addTag("parser.crawler.AsyncGetDataService.getPensionInfo", taskInsurance.getTaskid());
		try {
			WebParam webParam = insuranceSZHunanParser.getPensionInfo(insuranceRequestParameters, taskInsurance);
			InsuranceSZHunanHtml html = new InsuranceSZHunanHtml();
			html.setUrl(webParam.getHtmlPage().getBaseURI());
			html.setHtml(webParam.getHtmlPage().asXml());
			html.setPageCount(1);
			html.setType("pension");
			html.setTaskid(taskInsurance.getTaskid());
			insuranceSZHunanHtmlRepository.save(html);
			tracer.addTag("parser.crawler.AsyncGetDataService.getPensionInfo.html", "养老保险信息页面保存入库");
			
			if(null != webParam.getListPension() && webParam.getListPension().size() > 0){
				insuranceSZHunanPensionRepository.saveAll(webParam.getListPension());
				insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】采集成功", "CRAWLER_YANGLAO_MSG", 200, taskInsurance);
				tracer.addTag("parser.crawler.AsyncGetDataService.getPensionInfo.success", "【养老保险信息】采集成功");
			}else{
				insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】采集成功", "CRAWLER_YANGLAO_MSG", 201, taskInsurance);
				tracer.addTag("parser.crawler.AsyncGetDataService.getPensionInfo.fail2", "【养老保险信息】采集失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("parser.crawler.AsyncGetDataService.getPensionInfo.error", e.toString());
			tracer.addTag("parser.crawler.AsyncGetDataService.getPensionInfo.Exception", "【养老保险信息】采集失败");
			insuranceService.changeCrawlerStatus("数据采集中，【养老保险信息】采集完成", "CRAWLER_YANGLAO_MSG", 404, taskInsurance);
		}
		taskInsurance = taskInsuranceRepository.findByTaskid(taskInsurance.getTaskid());
		insuranceService.changeCrawlerStatusSuccess(taskInsurance);
	}
	
}
