package app.service;

import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.car.insurance.bean.CarInsuranceRequestBean;
import com.crawler.car.insurance.bean.CarInsuranceStatusCode;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.microservice.dao.entity.crawler.car.insurance.TaskCarInsurance;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxAssuredResult;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxCoverageResult;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxHtml;
import com.microservice.dao.entity.crawler.car.insurance.ygbx.CarInsuranceYGbxInsurerResult;
import com.microservice.dao.repository.crawler.car.insurance.ygbx.CarInsuranceYGbxAssuredResultRepository;
import com.microservice.dao.repository.crawler.car.insurance.ygbx.CarInsuranceYGbxCoverageResultRepository;
import com.microservice.dao.repository.crawler.car.insurance.ygbx.CarInsuranceYGbxHtmlRepository;
import com.microservice.dao.repository.crawler.car.insurance.ygbx.CarInsuranceYGbxInsurerResultRepository;
import com.module.htmlunit.WebCrawler;

import app.commontracerlog.TracerLog;
import app.parser.YgbxParser;
import app.service.aop.CarInsuranceVerify;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.car.insurance"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.car.insurance",})
public class YgbxService implements CarInsuranceVerify{
	
	@Autowired
	private TracerLog tracer;
	@Autowired
	private YgbxRetryService ygbxRetryService;
	@Autowired
	private CarInsuranceService carInsuranceService;
	@Autowired
	private YgbxParser parser;
	@Autowired
	private CarInsuranceYGbxAssuredResultRepository carInsuranceYGbxAssuredResultRepository;
	@Autowired
	private CarInsuranceYGbxInsurerResultRepository carInsuranceYGbxInsurerResultRepository;
	@Autowired
	private CarInsuranceYGbxHtmlRepository carInsuranceYGbxHtmlRepository;
	@Autowired
	private CarInsuranceYGbxCoverageResultRepository carInsuranceYGbxCoverageResultRepository;

	/* 
	 * 验证
	 * @see app.service.aop.CarInsuranceVerify#verify(com.crawler.car.insurance.bean.CarInsuranceRequestBean)
	 */
	public TaskCarInsurance verify(CarInsuranceRequestBean carInsuranceRequestBean) {
		tracer.addTag("ygbx.verify.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = carInsuranceService.getTaskCarInsurance(carInsuranceRequestBean.getTaskid());
		
		try{
			String src = ygbxRetryService.retry(carInsuranceRequestBean);
			taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_SUCCESS.getPhase());
			taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_SUCCESS.getPhasestatus());
			taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_SUCCESS.getDescription());
			taskCarInsurance.setParam(src);
			taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		}catch(RuntimeException e){
			taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_ERROR.getPhase());
			taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_ERROR.getPhasestatus());
			taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_VERIFY_ERROR.getDescription());
			taskCarInsurance.setFinished(false);
			taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		}
		return taskCarInsurance;
	}

	/* 获取数据
	 * @see app.service.aop.CarInsuranceCrawler#getAllData(com.crawler.car.insurance.bean.CarInsuranceRequestBean)
	 */
	@Override
	public TaskCarInsurance getAllData(CarInsuranceRequestBean carInsuranceRequestBean) {
		tracer.addTag("ygbx.crawler.taskid", carInsuranceRequestBean.getTaskid());
		TaskCarInsurance taskCarInsurance = carInsuranceService.getTaskCarInsurance(carInsuranceRequestBean.getTaskid());
		WebClient webClient = WebCrawler.getInstance().getNewWebClient();
		HtmlPage result = null;
		try {
			result = (HtmlPage) getHtml("https://epolicy.sinosig.com"+taskCarInsurance.getParam(),webClient);
		} catch (Exception e) {
			tracer.addTag("获取保单明细页面失败！", e.getMessage());
		}
		String text = result.asXml();
		
		CarInsuranceYGbxHtml carInsuranceYGbxHtml = parser.parserCarInsuranceYGbxHtml(text,carInsuranceRequestBean.getTaskid());
		carInsuranceYGbxHtmlRepository.save(carInsuranceYGbxHtml);
		tracer.addTag("网页源码入库", "success");
		
		try {
			CarInsuranceYGbxAssuredResult carInsuranceYGbxAssuredResult = parser.parserCarInsuranceYGbxAssuredResult(text,carInsuranceRequestBean.getTaskid());
			carInsuranceYGbxAssuredResultRepository.save(carInsuranceYGbxAssuredResult);
			tracer.addTag("被投保人信息入库", carInsuranceYGbxAssuredResult.toString());
		} catch (Exception e) {
			tracer.addTag("解析被投保人信息出错", e.getMessage());
		}
		
		try {
			CarInsuranceYGbxInsurerResult carInsuranceYGbxInsurerResult = parser.parserCarInsuranceYGbxInsurerResult(text, carInsuranceRequestBean.getTaskid());
			carInsuranceYGbxInsurerResultRepository.save(carInsuranceYGbxInsurerResult);
			tracer.addTag("保险人信息及合同表入库", carInsuranceYGbxInsurerResult.toString());
		} catch (Exception e) {
			tracer.addTag("解析保险人信息及合同信息出错", e.getMessage());
		}
		
		try {
			List<CarInsuranceYGbxCoverageResult> list = parser.parserCarInsuranceYGbxCoverageResult(text,carInsuranceRequestBean.getTaskid());
			carInsuranceYGbxCoverageResultRepository.saveAll(list);
			tracer.addTag("所上保险缴费明细入库", "success");
		} catch (Exception e) {
			tracer.addTag("解析保险缴费明细信息出错", e.getMessage());
		}
		
		taskCarInsurance.setPhase(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_SUCCESS.getPhase());
		taskCarInsurance.setPhase_status(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_SUCCESS.getPhasestatus());
		taskCarInsurance.setDescription(CarInsuranceStatusCode.CAR_INSURANCE_CRAWLER_SUCCESS.getDescription());
		taskCarInsurance.setFinished(true);
		taskCarInsurance = carInsuranceService.saveTaskCarInsurance(taskCarInsurance);
		
		return taskCarInsurance;
	}

	@Override
	public TaskCarInsurance getAllDataDone(String taskid) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Page getHtml(String url,WebClient webClient) throws Exception{
		WebRequest webRequest = new WebRequest(new URL(url), HttpMethod.GET);
		
		Page searchPage = webClient.getPage(webRequest);
		return searchPage;
		
	}

}
