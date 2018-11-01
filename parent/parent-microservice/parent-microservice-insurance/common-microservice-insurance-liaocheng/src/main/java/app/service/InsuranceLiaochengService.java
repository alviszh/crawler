package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.crawler.insurance.json.InsuranceStatusCode;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.repository.crawler.insurance.basic.TaskInsuranceRepository;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.EUtils;
import app.parser.InsuranceLiaochengParser;
import app.service.aop.InsuranceCrawler;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.insurance.basic",
		"com.microservice.dao.entity.crawler.insurance.liaocheng" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.insurance.basic",
		"com.microservice.dao.repository.crawler.insurance.liaocheng" })
public class InsuranceLiaochengService  implements InsuranceCrawler {

	@Autowired
	private TaskInsuranceRepository taskInsuranceRepository;
	@Autowired
	private InsuranceService insuranceService;

	@Autowired
	private InsuranceLiaochengParser insuranceLiaochengParser;

	@Autowired
	private TracerLog tracer;  
	
	@Autowired
	private EUtils eutils;

	/**
	 * @Des 登录爬取
	 * @param insuranceRequestParameters
	 * @return TaskInsurance
	 * @throws Exception
	 */
	@Async
	@Override
	public TaskInsurance getAllData(InsuranceRequestParameters insuranceRequestParameters) {

		tracer.addTag("InsuranceLiaochengService.login", insuranceRequestParameters.getTaskId());

		TaskInsurance taskInsurance = taskInsuranceRepository.findByTaskid(insuranceRequestParameters.getTaskId());
		try {
			if (null != taskInsurance) {

				String msg = insuranceLiaochengParser.getMedicalInfo(insuranceRequestParameters);

				tracer.addTag("InsuranceLiaochengService.login", insuranceRequestParameters.getTaskId() + msg);
				if (msg.equals("SUCCESS")) {

				} else if (msg != null && msg.length() > 0) {
					taskInsurance.setDescription(msg);
					taskInsurance.setPhase(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhase());
					taskInsurance
							.setPhase_status(InsuranceStatusCode.INSURANCE_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
					taskInsurance = taskInsuranceRepository.save(taskInsurance);
					return taskInsurance;
				} else {
					taskInsurance = insuranceService.changeLoginStatusException(taskInsurance);
					return taskInsurance;
				}

			}
		} catch (Exception e) {
			tracer.addTag("InsuranceLiaochengService.login---Taskid--",
					taskInsurance.getTaskid() + eutils.getEDetail(e));
		}

		return taskInsurance;
	}

	@Override
	public TaskInsurance getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}


}
