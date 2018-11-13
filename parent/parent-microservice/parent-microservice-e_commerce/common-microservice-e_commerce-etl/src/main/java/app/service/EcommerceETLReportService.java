package app.service;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportBaseInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatisticsSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportCallStatisticsThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumption;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportConsumptionThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportContactsStatisticsSixMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportContactsStatisticsThreeMonth;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportFamilyDetail;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportFamilyInfo;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportLocationStatistics;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportPayment;
import com.microservice.dao.entity.crawler.mobile.etl.MobileReportSMS;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.etl.pro.ProEcommerceReportRepository;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportBaseInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportCallStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportCallStatisticsSixMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportCallStatisticsThreeMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportConsumptionRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportConsumptionSixMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportConsumptionThreeMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportContactsStatisticsSixMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportContactsStatisticsThreeMonthRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportFamilyDetailRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportFamilyInfoRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportLocationStatisticsRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportPaymentRepository;
import com.microservice.dao.repository.crawler.mobile.etl.MobileReportSMSRepository;
import com.microservice.dao.repository.crawler.mobile.etl.ProMobileReportRepository;

import app.bean.RequestParam;
import app.commontracerlog.TracerLog;


@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.mobile.etl","com.microservice.dao.entity.crawler.mobile"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.mobile.etl","com.microservice.dao.repository.crawler.mobile"})
public class EcommerceETLReportService {

	@Autowired 
	private TracerLog tracer;
	
	@Autowired
	private E_CommerceTaskRepository e_CommerceTaskRepository;
	@Autowired
	private ProEcommerceReportRepository ecommerceReportRepository;
	
	
	@Value("${spring.profiles.active}")
	String profile;
	
	@Transactional(propagation=Propagation.SUPPORTS,isolation = Isolation.READ_COMMITTED)
	public void ecommerceReport(String taskid) {
		tracer.addTag("ETL ecommerceReport", taskid);
		E_CommerceTask e_CommerceTask = e_CommerceTaskRepository.findByTaskid(taskid);
		if (e_CommerceTask != null && null != e_CommerceTask.getWebsiteType()) {
			tracer.addTag("ETL ecommerceReport:e_CommerceTask---" + taskid, e_CommerceTask.toString());
			String etlStatus = "";
			if (e_CommerceTask.getWebsiteType().equals("taobao")) {
				etlStatus = ecommerceReportRepository.taobaoEtl(taskid);
			} else if (e_CommerceTask.getWebsiteType().equals("jd")) {
				etlStatus = ecommerceReportRepository.jdEtl(taskid);
			} else if (e_CommerceTask.getWebsiteType().equals("alipay")) {
				etlStatus = ecommerceReportRepository.alipayEtl(taskid);
			} else if (e_CommerceTask.getWebsiteType().equals("sn")) {
				etlStatus = ecommerceReportRepository.snEtl(taskid);
			}
			
			tracer.addTag("ETL mobileReport:etlStatus---" + etlStatus, taskid);
			String reportStatus = ecommerceReportRepository.proEcommerceReport(taskid);
			e_CommerceTask.setEtlStatus(etlStatus);
			e_CommerceTask.setReport_time((new Date()).toString());
			e_CommerceTask.setReport_status(reportStatus);
			e_CommerceTaskRepository.save(e_CommerceTask);
			tracer.addTag("ETL mobileReport:reportStatus---" + reportStatus, taskid);
		}
		tracer.addTag("ETL mobileReport---end", taskid);
	}
	
}
