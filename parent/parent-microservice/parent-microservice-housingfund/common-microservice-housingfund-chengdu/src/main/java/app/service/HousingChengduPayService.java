package app.service;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chengdu.HousingChengduHtml;
import com.microservice.dao.entity.crawler.housing.chengdu.HousingChengduPay;
import com.microservice.dao.repository.crawler.housing.chengdu.HousingChengduHtmlRepository;
import com.microservice.dao.repository.crawler.housing.chengdu.HousingChengduPayRepository;

import app.commontracerlog.TracerLog;
import app.crawler.bean.WebParam;
import app.crawler.htmlparse.HousingChengduParse;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chengdu")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chengdu")
public class HousingChengduPayService extends HousingBasicService {

	@Autowired
	private HousingChengduParse housingChengduParse;
	
	@Autowired
	private HousingChengduHtmlRepository housingChengduHtmlRepository;
	
	@Autowired
	private HousingChengduPayRepository housingChengduPayRepository;
	
	@Autowired
	private TracerLog tracer;

	/**
	 * 缴费信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public Future<String> getPay(TaskHousing taskHousing,String data) {
		tracer.addTag("HousingChengduPayService.getPay", taskHousing.getTaskid());
		String statusCode="200";
		try {
			WebParam<HousingChengduPay> webParam = housingChengduParse.getPay(taskHousing, data);

			if (null != webParam) {

				housingChengduPayRepository.saveAll(webParam.getList());
				
				tracer.addTag("HousingChengduPayService.getPay---缴费信息", "缴费信息已入库!" + taskHousing.getTaskid());

				HousingChengduHtml housingChengduHtml = new HousingChengduHtml(taskHousing.getTaskid(),
						"housing_chengdu_pay", data, webParam.getUrl(), webParam.getHtml());
				housingChengduHtmlRepository.save(housingChengduHtml);

				tracer.addTag("HousingChengduPayService.getPay---缴费信息源码", "缴费信息源码表入库!" + taskHousing.getTaskid());

			} else {
				statusCode = "404";
				tracer.addTag("HousingChengduPayService.getPay.webParam is null", taskHousing.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = "500";
			tracer.addTag("HousingChengduPayService.getPay---ERROR", taskHousing.getTaskid() + "---ERROR:" + e);

		}
		return new AsyncResult<String>(statusCode);
	}

}