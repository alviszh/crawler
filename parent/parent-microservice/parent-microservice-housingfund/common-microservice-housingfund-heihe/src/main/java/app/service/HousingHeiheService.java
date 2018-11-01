package app.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.heihe.HousingHeiheHtml;
import com.microservice.dao.repository.crawler.housing.basic.TaskHousingRepository;
import com.microservice.dao.repository.crawler.housing.heihe.HousingHeiheHtmlRepository;
import com.microservice.dao.repository.crawler.housing.heihe.HousingHeihePaydetailsRepository;

import app.commontracerlog.TracerLog;
import app.crawler.domain.WebParam;
import app.service.common.HousingBasicService;
import app.unit.HousingFundHeiheHtmlunit;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.heihe")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.heihe")
public class HousingHeiheService extends HousingBasicService{

	public static final Logger log = LoggerFactory.getLogger(HousingHeiheCrawlerService.class);
	@Autowired
	private HousingHeiheHtmlRepository housingHeiheHtmlRepository;
	@Autowired
	private HousingHeihePaydetailsRepository housingHeihePaydetailsRepository;
	@Autowired
	private HousingFundHeiheHtmlunit housingFundHeiheHtmlunit;
	@Autowired
	private TaskHousingRepository  taskHousingRepository;
	@Autowired
	private TracerLog tracer;
	@Async
	public void getPaydetails(MessageLoginForHousing messageLoginForHousing, int pageNum){		
		tracer.addTag("获取账户缴存明细信息 获取第"+pageNum+"页", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing=taskHousingRepository.findByTaskid(messageLoginForHousing.getTask_id());
		WebParam webParam=new WebParam();
		try {
			int temp = 0;
			for (int i = 1; i <= pageNum; i++) {
				webParam = housingFundHeiheHtmlunit.getPaydetails(messageLoginForHousing, taskHousing,pageNum);
				if (null != webParam.getHtml()) {
					HousingHeiheHtml housingHeiheHtml = new HousingHeiheHtml();
					housingHeiheHtml.setPageCount(pageNum);
					housingHeiheHtml.setHtml(webParam.getHtml());
					housingHeiheHtml.setType("paydetails");
					housingHeiheHtml.setUrl(webParam.getUrl());
					housingHeiheHtml.setTaskid(taskHousing.getTaskid());
					housingHeiheHtmlRepository.save(housingHeiheHtml);
				}
				if (null != webParam.getPaydetails()) {
					housingHeihePaydetailsRepository.saveAll(webParam.getPaydetails());
					tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息入库" + webParam.getPaydetails());
					temp++;
				} else {
					tracer.addTag("action.housing.crawler.getPaydetails", "缴存明细信息为空");

				}
			}
			if (temp>0) {
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 200,
						taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			}else{
				updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(), 201,
						taskHousing.getTaskid());
				updateTaskHousing(taskHousing.getTaskid());
			}

		} catch (Exception e) {
			updatePayStatusByTaskid(HousingfundStatusCodeEnum.HOUSING_CRAWLER_STATEMENT_MSG_SUCCESS.getDescription(),
					500 , taskHousing.getTaskid());
			tracer.addTag("action.housing.crawler.getPaydetails", "获取缴存明细信息失败");
			updateTaskHousing(taskHousing.getTaskid());
			e.printStackTrace();
		}
	}
}
