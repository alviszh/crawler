package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.wuhai.HousingWuHaiHtml;
import com.microservice.dao.repository.crawler.housing.wuhai.HousingWuHaiHtmlRepository;
import com.microservice.dao.repository.crawler.housing.wuhai.HousingWuHaiUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingWuHaiParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;
import app.service.common.aop.ICrawlerLogin;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.wuhai")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.wuhai")
public class HousingWuHaiService extends HousingBasicService implements ICrawler, ICrawlerLogin{

	@Autowired
	private HousingWuHaiUserinfoRepository housingWuHaiUserinfoRepository;
	@Autowired
	private HousingWuHaiHtmlRepository housingWuHaiHtmlRepository;
	@Autowired
	private HousingWuHaiParser housingWuHaiParser;

	@Override
	@Async
	public TaskHousing login(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("crawler.housingFund.login.start", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		String messageLoginJson = gs.toJson(messageLoginForHousing);
		taskHousing.setLoginMessageJson(messageLoginJson);
		save(taskHousing);
		try {
			WebParam webParam = housingWuHaiParser.login(messageLoginForHousing, taskHousing);
			if(null != webParam.getHtml()){
				tracer.addTag("crawler.housingFund.login.success", "登录成功");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhase());
				taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getPhasestatus());
				taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getDescription());
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_SUCCESS.getError_code());
				taskHousing.setPaymentStatus(104);
				save(taskHousing); 
				
				HousingWuHaiHtml html = new HousingWuHaiHtml();
				html.setHtml(webParam.getHtml());
				html.setPageCount(0);
				html.setTaskid(taskHousing.getTaskid());
				html.setType("logined");
				html.setUrl(webParam.getUrl());
				housingWuHaiHtmlRepository.save(html);
			}else{
				tracer.addTag("crawler.housingFund.login.ERROR", "登录失败");
				taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription("没有查到公积金余额，请确认姓名及身份证是否正确！");
				taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getError_code());
				taskHousing.setPaymentStatus(104);
				save(taskHousing); 
			}
		} catch (Exception e) {
			tracer.addTag("crawler.housingFund.login.TimeOUT2", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_ERROR.getPhase());
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("登陆超时，请核对信息及其网络");
			taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
			taskHousing.setPaymentStatus(104);
			save(taskHousing);
			e.printStackTrace();
		}
		return taskHousing;
	}

	@Override
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("crawler.housingFund.crawler.start.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		HousingWuHaiHtml housingWuHaiHtml = housingWuHaiHtmlRepository.findByTaskidAndTypeOrderByIdDesc(taskHousing.getTaskid(), "logined").get(0);
		String html = housingWuHaiHtml.getHtml();
		try {
			WebParam webParam = housingWuHaiParser.crawler(html, taskHousing);
			housingWuHaiUserinfoRepository.saveAll(webParam.getList());
			tracer.addTag("crawler.housingFund.getUserinfo.success", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
			taskHousing.setDescription("数据采集中，用户信息采集成功！");
			taskHousing.setUserinfoStatus(200);
			save(taskHousing);
		} catch (Exception e) {
			tracer.addTag("crawler.housingFund.getUserinfo.TimeOUT", taskHousing.getTaskid());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
			taskHousing.setDescription("数据采集中，用户信息采集完成！");
			taskHousing.setUserinfoStatus(404);
			save(taskHousing);
			e.printStackTrace();
		}
		updateTaskHousing(taskHousing.getTaskid());       
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}