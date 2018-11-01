package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.jiamusi.HousingJiaMuSiHtml;
import com.microservice.dao.repository.crawler.housing.jiamusi.HousingJiaMuSiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.jiamusi.HousingJiaMuSiPayRepository;
import com.microservice.dao.repository.crawler.housing.jiamusi.HousingJiaMuSiUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingJiaMuSiParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.jiamusi")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.jiamusi")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingJiaMuSiUserinfoRepository housingJiaMuSiUserinfoRepository;
	@Autowired
	private HousingJiaMuSiHtmlRepository housingJiaMuSiHtmlRepository;
	@Autowired
	private HousingJiaMuSiPayRepository housingJiaMuSiPayRepository;
	@Autowired
	private HousingJiaMuSiParser housingJiaMuSiParser;

	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("crawler.GetDataService.getUserInfo.taskid", taskHousing.getTaskid());
		try {
			WebParam webParam = housingJiaMuSiParser.getUserInfo(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtml()){
				HousingJiaMuSiHtml html = new HousingJiaMuSiHtml();
				html.setUrl(webParam.getUrl());
				html.setType("userinfo");
				html.setPageCount(1);
				html.setHtml(webParam.getHtml());
				html.setTaskid(taskHousing.getTaskid());
				housingJiaMuSiHtmlRepository.save(html);
				tracer.addTag("crawler.GetDataService.getUserInfo.page.success", "用户信息页面已经入库");
			}
			if(null != webParam.getList()){
				housingJiaMuSiUserinfoRepository.saveAll(webParam.getList());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
				taskHousing.setDescription("[数据采集中]个人信息采集成功！");
				taskHousing.setUserinfoStatus(200);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getError_code());
				save(taskHousing);
				tracer.addTag("crawler.GetDataService.getUserInfo.success", "用户信息已经入库");
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription("[数据采集中]个人信息采集完成！");
				taskHousing.setUserinfoStatus(201);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
				save(taskHousing);
				tracer.addTag("crawler.GetDataService.getUserInfo.fail", "用户信息入库失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription("[数据采集中]个人信息采集完成！");
			taskHousing.setUserinfoStatus(404);
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
			save(taskHousing);
			tracer.addTag("crawler.GetDataService.getUserInfo.error", e.toString());
			tracer.addTag("crawler.GetDataService.getUserInfo.fail2", "用户信息入库失败");
		}
		
	}

	public void getTrans(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("crawler.getTrans.taskid", taskHousing.getTaskid());
		try {
			WebParam webParam = housingJiaMuSiParser.getTrans(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtml()){
				HousingJiaMuSiHtml html = new HousingJiaMuSiHtml();
				html.setUrl(webParam.getUrl());
				html.setType("trans");
				html.setPageCount(1);
				html.setHtml(webParam.getHtml());
				html.setTaskid(taskHousing.getTaskid());
				housingJiaMuSiHtmlRepository.save(html);
				tracer.addTag("crawler.getTrans.page.success", "流水信息页面已经入库");
			}
			if(null != webParam.getList()){
				housingJiaMuSiPayRepository.saveAll(webParam.getList());
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
				taskHousing.setDescription("[数据采集中]流水信息采集成功！");
				taskHousing.setPaymentStatus(200);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getError_code());
				save(taskHousing);
				tracer.addTag("crawler.getTrans.crawler.success", "流水信息已经入库");
			}else{
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription("[数据采集中]流水信息采集完成！");
				taskHousing.setPaymentStatus(201);
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
				save(taskHousing);
				tracer.addTag("crawler.getTrans.crawler.fail", "流水信息入库失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getPhasestatus());
			taskHousing.setDescription("[数据采集中]流水信息采集完成！");
			taskHousing.setPaymentStatus(404);
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_USER_MSG_ERROR.getError_code());
			save(taskHousing);
			tracer.addTag("crawler.getTrans.crawler.payment.fail", e.toString());
			tracer.addTag("crawler.getTrans.crawler.fail2", "流水信息入库失败");
		}
		
	}
	
	
}
