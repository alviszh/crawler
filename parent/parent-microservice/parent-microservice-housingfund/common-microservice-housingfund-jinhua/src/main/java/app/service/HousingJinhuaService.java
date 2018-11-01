package app.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jinhua.HousingJinHuaHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.jinhua.HousingJinHuaHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jinhua.HousingJinHuaPaydetailsRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundJinhuaHtmlunit;


@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jinhua")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jinhua")
public class HousingJinhuaService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingJinhuaService.class);
	@Autowired
	private HousingJinHuaHtmlRepository housingJinHuaHtmlRepository;
	@Autowired
	private HousingJinHuaPaydetailsRepository housingJinHuaPaydetailsRepository;
	
	@Autowired
	private HousingFundJinhuaHtmlunit housingFundJinhuaHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing,List<String>  urlList){		
		tracer.addTag("parser.HousingJinhuaService.getPaydetails.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		try {			
			for (String url : urlList) {
				WebParam webParam=housingFundJinhuaHtmlunit.getPaydetails(url,messageLoginForHousing, taskHousing);
				if (null !=webParam.getHtml()) {
					HousingJinHuaHtml housingJinHuaHtml = new HousingJinHuaHtml();
					housingJinHuaHtml.setPageCount(1);
					housingJinHuaHtml.setHtml(webParam.getHtml());
					housingJinHuaHtml.setType("paydetail");
					housingJinHuaHtml.setUrl(webParam.getUrl());
					housingJinHuaHtml.setTaskid(taskHousing.getTaskid());
					housingJinHuaHtmlRepository.save(housingJinHuaHtml);		
				}	
				if (null !=webParam.getPaydetails() && !webParam.getPaydetails().isEmpty()) {
					housingJinHuaPaydetailsRepository.saveAll(webParam.getPaydetails());
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息入库"+webParam.getPaydetails());
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							200 , taskHousing.getTaskid());
				}else{
					tracer.addTag("parser.housing.crawler.getPaydetails", "缴存明细信息为空");
					updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
							201 , taskHousing.getTaskid());
				}
			}
		} catch (Exception e) {
			tracer.addTag("parser.housing.crawler.getPaydetails.Exception", e.toString());
			updatePayStatusByTaskid(StatusCodeEnum.TASKMOBILE_CRAWLER_PAY_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			e.printStackTrace();
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
}