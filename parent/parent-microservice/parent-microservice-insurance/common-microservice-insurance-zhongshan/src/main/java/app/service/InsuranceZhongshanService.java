package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanHtml;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanMedicalCare;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionAccount;
import com.microservice.dao.entity.crawler.insurance.zhongshan.InsuranceZhongShanPensionDetail;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;
import com.microservice.dao.repository.crawler.insurance.zhongshan.InsuranceZhongShanHtmlRepository;
import com.microservice.dao.repository.crawler.insurance.zhongshan.InsuranceZhongShanMedicalCareRepository;
import com.microservice.dao.repository.crawler.insurance.zhongshan.InsuranceZhongShanPensionAccountRepository;
import com.microservice.dao.repository.crawler.insurance.zhongshan.InsuranceZhongShanPensionDetailRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.parser.InsuranceZhongshanParser;
import net.sf.json.JSONObject;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.zhongshan" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.zhongshan" })
public class InsuranceZhongshanService {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;
	@Autowired
	private InsuranceZhongshanParser insuranceZhongshanParser;
	@Autowired
	private InsuranceZhongShanHtmlRepository insuranceZhongshanHtmlRepository;
	@Autowired
	private InsuranceZhongShanMedicalCareRepository insuranceZhongShanMedicalCareRepository;
	@Autowired
	private InsuranceZhongShanPensionAccountRepository insuranceZhongShanPensionAccountRepository;
	@Autowired
	private InsuranceZhongShanPensionDetailRepository insuranceZhongShanPensionDetailRepository;

	
	@Autowired
	private TracerLog tracer;

	/**
	 * @Des 登录
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	public TaskInsurance login(InsuranceRequestParameters insuranceRequestParameters, Integer count) throws Exception {

		tracer.addTag("InsuranceChengduService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());

		if (null != taskInsurance) {

			WebParam webParam = insuranceZhongshanParser.login(insuranceRequestParameters);

			if (null == webParam) {
				tracer.addTag("InsuranceZhongshanService.login", insuranceRequestParameters.getTaskId() + "登录页获取超时！");
				taskInsurance = insuranceService.changeLoginStatusTimeOut(taskInsurance);
				return taskInsurance;
			} else {
				String html = webParam.getPage().getWebResponse().getContentAsString();
				System.out.println(html);
				tracer.addTag("InsuranceZhongshanService.login",
						insuranceRequestParameters.getTaskId() + "<xmp>" + html + "</xmp>");
				if (html.contains("登录成功")) {
					taskInsurance = insuranceService.changeLoginStatusSuccess(taskInsurance, webParam.getPage());
					return taskInsurance;
				} else {
					tracer.addTag("InsuranceZhongshanService.login" + insuranceRequestParameters.getTaskId(),
							"登录失败次数" + count);
					if (count < 4) {
						login(insuranceRequestParameters, ++count);
					} else {
						taskInsurance = insuranceService.changeLoginIdcadOrPwdError(taskInsurance);
						return taskInsurance;
					}
				}

			}

		}

		return null;
	}

	/**
	 * 更新task表（doing 正在登录状态）
	 * 
	 * @param insuranceRequestParameters
	 * @return
	 */
	public TaskInsurance changeStatus(InsuranceRequestParameters insuranceRequestParameters) {

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		JSONObject jsonObject = JSONObject.fromObject(insuranceRequestParameters);
		taskInsurance.setTesthtml(jsonObject.toString());
		taskInsurance = insuranceService.changeLoginStatusDoing(taskInsurance);
		return taskInsurance;
	}

	/**
	 * @Des 更新task表（doing 正在采集）
	 * @param insuranceRequestParameters
	 */
	public TaskInsurance updateTaskInsurance(InsuranceRequestParameters insuranceRequestParameters) {
		TaskInsurance taskInsurance = insuranceService.changeCrawlerStatusDoing(insuranceRequestParameters);
		return taskInsurance;
	}

	/**
	 * 获取个人养老账户信息
	 * 
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getPensionaccount(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceService.getPensionaccount", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceZhongShanPensionAccount> webParam = insuranceZhongshanParser.getPensionaccount(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			tracer.addTag("InsuranceZhongshanService.getPensionaccount 个人养老账户信息", "个人养老账户信息已入库!");

			tracer.addTag("InsuranceZhongshanService.getPensionaccount:SUCCESS", insuranceRequestParameters.getTaskId());

			InsuranceZhongShanHtml insuranceZhongshanHtml = new InsuranceZhongShanHtml();
			insuranceZhongshanHtml.setHtml(webParam.getHtml());
			insuranceZhongshanHtml.setUrl(webParam.getUrl());
			insuranceZhongshanHtmlRepository.save(insuranceZhongshanHtml);
			insuranceZhongShanPensionAccountRepository.save(webParam.getInsuranceZhongShanPensionAccount());
			tracer.addTag("InsuranceZhongshanService.getSummary 个人养老账户信息", "个人养老账户信息源码表入库!");
		}
	}
	
	/**
	 * 医疗待遇信息查询
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getMedicalcare(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceService.getMedicalcare", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceZhongShanMedicalCare>  webParam = insuranceZhongshanParser.getMedicalcare(taskInsurance,
				taskInsurance.getCookies());
		if (null != webParam) {
			tracer.addTag("InsuranceZhongshanService.getMedicalcare 医疗待遇信息", "医疗待遇信息已入库!");

			tracer.addTag("InsuranceZhongshanService.getMedicalcare:SUCCESS", insuranceRequestParameters.getTaskId());
			
			InsuranceZhongShanHtml insuranceZhongshanHtml = new InsuranceZhongShanHtml();
			insuranceZhongshanHtml.setHtml(webParam.getHtml());
			insuranceZhongshanHtml.setUrl(webParam.getUrl());
			insuranceZhongshanHtmlRepository.save(insuranceZhongshanHtml);
			insuranceZhongShanMedicalCareRepository.save(webParam.getInsuranceZhongShanMedicalCare());
			tracer.addTag("InsuranceZhongshanService.getMedicalcare 医疗待遇信息", "医疗待遇信息源码表入库!");
		}
	
	}
	/**
	 * 养老账户明细查询
	 * @param insuranceRequestParameters
	 */
	@Async
	public void getPensionDetail(InsuranceRequestParameters insuranceRequestParameters) {
		tracer.addTag("InsuranceService.getPensionDetail", insuranceRequestParameters.getTaskId());
		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		WebParam<InsuranceZhongShanPensionDetail>  webParam = insuranceZhongshanParser.getPensionDetail(taskInsurance,taskInsurance.getCookies());
		if (null != webParam) {
			tracer.addTag("InsuranceZhongshanService.getPensionDetail 医疗待遇信息", "医疗待遇信息已入库!");

			tracer.addTag("InsuranceZhongshanService.getPensionDetail:SUCCESS", insuranceRequestParameters.getTaskId());
		
			InsuranceZhongShanHtml insuranceZhongshanHtml = new InsuranceZhongShanHtml();
			insuranceZhongshanHtml.setHtml(webParam.getHtml());
			insuranceZhongshanHtml.setUrl(webParam.getUrl());
			insuranceZhongshanHtmlRepository.save(insuranceZhongshanHtml);
			insuranceZhongShanPensionDetailRepository.saveAll(webParam.getList());
			tracer.addTag("InsuranceZhongshanService.getPensionDetail  养老账户明细信息", " 养老账户明细信息源码表入库!");
		}
	}
}
