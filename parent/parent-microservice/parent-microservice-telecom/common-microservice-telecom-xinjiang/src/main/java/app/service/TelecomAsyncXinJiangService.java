package app.service;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangHtml;
import com.microservice.dao.entity.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecord;
import com.microservice.dao.repository.crawler.mobile.TaskMobileRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangHtmlRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangSmsRecordRepository;
import com.microservice.dao.repository.crawler.telecom.xinjiang.TelecomXinjiangVoiceRecordRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.htmlunit.TelecomXinJiangHtmlUnit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.xinjiang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.xinjiang")
public class TelecomAsyncXinJiangService {
	@Autowired
	private TelecomXinjiangHtmlRepository  telecomXinjiangHtmlRepository;
	@Autowired
	private TaskMobileRepository taskMobileRepository;
	@Autowired
	private TelecomXinJiangHtmlUnit telecomXinJiangHtmlUnit;
	@Autowired
	private TelecomXinjiangVoiceRecordRepository telecomXinjiangVoiceRecordRepository;
	@Autowired
	private TelecomXinjiangSmsRecordRepository telecomXinjiangSmsRecordRepository;
	@Autowired
	private TracerLog tracer; 
	//抓取新疆电信语言详单第二页之后的数据
	@Async
	public Future<String> getVoiceRecordOtherPage(MessageLogin messageLogin, TaskMobile taskMobile, String month,String pageIndex) {
		tracer.addTag("抓取新疆电信语言详单start" + month + pageIndex, messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		try {
			int count = 0;
			WebParam webParam = telecomXinJiangHtmlUnit.getVoiceRecord(messageLogin, taskMobile, month, pageIndex, count);
			if (null != webParam.getHtml()) {
				TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
				telecomXinjiangHtml.setPageCount(1);
				telecomXinjiangHtml.setHtml(webParam.getHtml());
				telecomXinjiangHtml.setType("voiceRecord" + month + pageIndex);
				telecomXinjiangHtml.setUrl(webParam.getUrl());
				telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
				telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);
			}
			if (null != webParam.voiceRecords && !webParam.voiceRecords.isEmpty()) {
				List<TelecomXinjiangVoiceRecord> list = telecomXinjiangVoiceRecordRepository.saveAll(webParam.getVoiceRecords());
				tracer.addTag("crawler.getVoiceRecordOtherPage 月" + month + "第" + pageIndex + "页",
						month + "月语言详单细信息入库成功 第" + pageIndex + "页"+"共"+list.size()+"条数据");
			} else {
				tracer.addTag("crawler.getVoiceRecordOtherPage 月" + month + "第" + pageIndex + "页",
						month + "语言详单信息爬取失败" + pageIndex);				
			}
		} catch (Exception e) {
			tracer.addTag("crawler.getVoiceRecordOtherPage.Exception"+month+pageIndex, e.getMessage());
			e.printStackTrace();
		}		
		tracer.addTag("抓取新疆电信语言详单end" + month + pageIndex, messageLogin.getTask_id());
		return new AsyncResult<>("分页数据采集成功");	
	}
	@Async
	public void getSmsRecordOtherPage(MessageLogin messageLogin, TaskMobile taskMobile, String month, String pageIndex)
			throws Exception {
		tracer.addTag("抓取新疆电信短信详情" + month + pageIndex, messageLogin.getTask_id());
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());
		int count = 0;
		WebParam webParam = telecomXinJiangHtmlUnit.getSmsRecord(messageLogin, taskMobile, month, pageIndex, count);
		if (null != webParam.getHtml()) {
			TelecomXinjiangHtml telecomXinjiangHtml = new TelecomXinjiangHtml();
			telecomXinjiangHtml.setPageCount(1);
			telecomXinjiangHtml.setHtml(webParam.getHtml());
			telecomXinjiangHtml.setType("smsRecord" + month + pageIndex);
			telecomXinjiangHtml.setUrl(webParam.getUrl());
			telecomXinjiangHtml.setTaskid(taskMobile.getTaskid());
			telecomXinjiangHtmlRepository.save(telecomXinjiangHtml);
		}
		if (null != webParam.getSmsRecords() && !webParam.getSmsRecords().isEmpty()) {
			telecomXinjiangSmsRecordRepository.saveAll(webParam.getSmsRecords());
			tracer.addTag("parser.telecom.crawler.getSmsRecord" + month + "第" + pageIndex + "页",
					month + "月短信明细信息入库" + webParam.getSmsRecords());
		} else {
			tracer.addTag("parser.telecom.crawler.getSmsRecord" + month, month + "月短信明细信息为空");
		}
		tracer.addTag("抓取新疆电信短信详情", messageLogin.getTask_id());
	}
}
