package app.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.insurance.json.InsuranceRequestParameters;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.insurance.basic.TaskInsurance;
import com.microservice.dao.entity.crawler.insurance.suqian.InsurancePay;
import com.microservice.dao.entity.crawler.insurance.suqian.InsuranceUser;
import com.microservice.dao.repository.crawler.insurance.suqian.InSuranceUserRepository;
import com.microservice.dao.repository.crawler.insurance.suqian.InsurancePayRepository;

import app.bean.AssociatedPersons;
import app.bean.InsuranceBasicSuQianChangZhouLianYunGangBean;
import app.bean.InsurancePaySuQianJsonRootBean;
import app.bean.WebParamInsurance;
import app.commontracerlog.TracerLog;
import app.crawler.htmlparse.InsuranceSuQianParse;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.insurance.suqian")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.insurance.suqian")
public class InsuranceSuQianFutureService extends InsuranceService {

	public static final Logger log = LoggerFactory.getLogger(InsuranceSuQianFutureService.class);

	@Autowired
	private LoginAngGetService loginAngGetService;

	@Autowired
	private InSuranceUserRepository inSuranceUserRepository;

	@Autowired
	private InsurancePayRepository insurancePayRepository;
	
	@Autowired
	private TracerLog tracerLog;

	public WebParamInsurance<?> login(InsuranceRequestParameters insuranceRequestParameters, TaskInsurance taskInsurance) {
		try {
			return loginAngGetService.loginChrome(insuranceRequestParameters, taskInsurance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<AssociatedPersons> getUserNeed(WebClient webClient, TaskInsurance taskInsurance) {
		try {
			Page page = loginAngGetService.getUser(webClient);

			InsuranceBasicSuQianChangZhouLianYunGangBean result = InsuranceSuQianParse
					.UserNeedParse(page.getWebResponse().getContentAsString());

			List<AssociatedPersons> listPayNeed = result.getAssociatedPersons();

			return listPayNeed;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return null;

	}

	public void getUser(WebClient webClient, TaskInsurance taskInsurance, String persinoId) {
		try {
			Page page = loginAngGetService.getUserBasic(webClient, persinoId);

			InsuranceUser result = InsuranceSuQianParse.UserParse(page.getWebResponse().getContentAsString());
			tracerLog.output("getUser  result" + persinoId, result.toString());
			inSuranceUserRepository.save(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getPayResult(WebClient webClient, TaskInsurance taskInsurance, String persinoId) {
		try {

			LocalDate nowdate = LocalDate.now();

			String startDate = nowdate.plusYears(-10).getYear() + "01";

			String month = nowdate.getMonthValue() + "";
			if (month.length() == 1) {
				month = "0" + month;
			}
			String endDate = nowdate.getYear() + month;

			Page page = loginAngGetService.getPay(webClient, startDate, endDate, persinoId);

			InsurancePaySuQianJsonRootBean result = InsuranceSuQianParse
					.PayParse(page.getWebResponse().getContentAsString());
			tracerLog.output("getPayResult  result" + persinoId, result.toString());
			List<InsurancePay> list = result.getList();
			if (list != null) {
				tracerLog.output("getPayResult " + persinoId, "缴费信息共" + list.size() + "条");
				for (InsurancePay insurancePay : list) {
					insurancePay.setTaskId(taskInsurance.getTaskid());
					insurancePay.setCity("suqian");
					insurancePay.setPersinoId(persinoId);
					insurancePayRepository.save(insurancePay);
				}
			} else {
				tracerLog.output("getPayResult " + persinoId, "没有缴费信息");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}