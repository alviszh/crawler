package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiHtml;
import com.microservice.dao.entity.crawler.telecom.hubei.TelecomHubeiSmsrecords;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiCallrecordsRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.hubei.TelecomHubeiSmsrecordsRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.TelecomHubeiHtmlUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.hubei")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.hubei")
public class TelecomOtherPageUnitHubeiService {
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomHubeiHtmlRepository telecomHubeiHtmlRepository;
	@Autowired
	private TelecomHubeiCallrecordsRepository telecomHubeiCallrecordsRepository;
	@Autowired
	private TelecomHubeiHtmlUnit telecomHubeiHtmlUnit;
	@Autowired
	private TelecomHubeiSmsrecordsRepository telecomHubeiSmsrecordsRepository;
	@Autowired
	private TracerLog tracer; 
	// 抓取湖北电信语言详单
	@Async
	public void getOtherPageVoiceRecord(MessageLogin messageLogin, TaskMobile taskMobile, String month, int pageNumber)
			throws Exception {
		tracer.addTag("parser.telecom.crawler.getOtherPageVoiceRecord 抓取湖北电信语言详单" + month + "第" + pageNumber + "页",
				messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		int count = 0;
		WebParam webParam = telecomHubeiHtmlUnit.getOtherPageVoiceRecord(messageLogin, taskMobile, month, pageNumber,
				count);
		if (null != webParam.getHtml()) {
			TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
			telecomHubeiHtml.setPageCount(pageNumber);
			telecomHubeiHtml.setHtml(webParam.getHtml());
			telecomHubeiHtml.setType("callRecord" + month + pageNumber);
			telecomHubeiHtml.setUrl(webParam.getUrl());
			telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
			telecomHubeiHtmlRepository.save(telecomHubeiHtml);
		}
		if (null != webParam.getCallrecords() && !webParam.getCallrecords().isEmpty()) {
			telecomHubeiCallrecordsRepository.saveAll(webParam.getCallrecords());
			tracer.addTag("parser.telecom.crawler.getOtherPageVoiceRecord" + month + pageNumber,"语言详单细信息入库" + webParam.getCallrecords());
		}else{
			tracer.addTag("parser.telecom.crawler.getOtherPageVoiceRecord" + month + pageNumber, "语言详单信息爬取为空");
		}
		tracer.addTag("parser.telecom.crawler.getOtherPageVoiceRecord", messageLogin.getTask_id());
	}
	//抓取湖北电信短信详情
	@Async
	public void getOtherPageSmsRecord(MessageLogin messageLogin, TaskMobile taskMobile, String month, int pageNumber)
			throws Exception {
		tracer.addTag("parser.telecom.crawler.getOtherPageSmsRecord 抓取湖北电信短信详情" + month + pageNumber,
				messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		int count = 0;
		WebParam webParam = telecomHubeiHtmlUnit.getOtherPageSmsRecord(messageLogin, taskMobile, month, pageNumber,
				count);
		if (null != webParam.getHtml()) {
			TelecomHubeiHtml telecomHubeiHtml = new TelecomHubeiHtml();
			telecomHubeiHtml.setPageCount(1);
			telecomHubeiHtml.setHtml(webParam.getHtml());
			telecomHubeiHtml.setType("smsRecord" + month + pageNumber);
			telecomHubeiHtml.setUrl(webParam.getUrl());
			telecomHubeiHtml.setTaskid(taskMobile.getTaskid());
			telecomHubeiHtmlRepository.save(telecomHubeiHtml);
		}
		List<TelecomHubeiSmsrecords> smsRecords = webParam.getSmsrecords();
		if (null != smsRecords && !smsRecords.isEmpty()) {
			telecomHubeiSmsrecordsRepository.saveAll(smsRecords);
			tracer.addTag("parser.telecom.crawler.getOtherPageSmsRecord" + month + pageNumber,"抓取短信明细信息入库" + smsRecords);
		}else{
			tracer.addTag("parser.telecom.crawler.getOtherPageSmsRecord" + month + pageNumber, "抓取短信明细信息为空");
		}
		tracer.addTag("parser.telecom.crawler.getOtherPageSmsRecord", messageLogin.getTask_id());
	}
}