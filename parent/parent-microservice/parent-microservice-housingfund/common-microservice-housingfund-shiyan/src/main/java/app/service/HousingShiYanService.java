package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.shiyan.HousingFundShiYanHtml;
import com.microservice.dao.repository.crawler.housing.shiyan.HousingFundShiYanRepositoryHtml;
import com.microservice.dao.repository.crawler.housing.shiyan.HousingFundShiYanRepositoryUserInfo;

import app.common.WebParam;
import app.parser.HousingShiYanParser;
import app.service.common.HousingBasicService;
import app.service.common.aop.ICrawler;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.shiyan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.shiyan")
public class HousingShiYanService extends HousingBasicService implements ICrawler{

	@Autowired
	private HousingFundShiYanRepositoryUserInfo housingFundShiYanRepositoryUserInfo;
	@Autowired
	private HousingFundShiYanRepositoryHtml housingFundShiYanRepositoryHtml;
	@Autowired
	private HousingShiYanParser housingShiYanParser;

	@Override
	@Async
	public TaskHousing getAllData(MessageLoginForHousing messageLoginForHousing) {
		tracer.addTag("crawler.HousingShiYanService.crawler.taskid", messageLoginForHousing.getTask_id());
		TaskHousing taskHousing = findTaskHousing(messageLoginForHousing.getTask_id());
		try {
			WebParam webParam = housingShiYanParser.crawler(messageLoginForHousing);
			HousingFundShiYanHtml html = new HousingFundShiYanHtml();
			html.setTaskid(taskHousing.getTaskid());
			html.setPagenumber(1);
			html.setUrl(webParam.getUrl());
			html.setHtml(webParam.getPage().getWebResponse().getContentAsString());
			housingFundShiYanRepositoryHtml.save(html);
			
			if(null != webParam.getList()){
				tracer.addTag("crawler.HousingShiYanService.crawler.success", "数据采集成功！");
				housingFundShiYanRepositoryUserInfo.saveAll(webParam.getList());
				updateUserInfoStatusByTaskid("用户信息采集成功！", 200, taskHousing.getTaskid());
				taskHousing = findTaskHousing(taskHousing.getTaskid());
				taskHousing.setFinished(true);
				save(taskHousing);
			}else{
				tracer.addTag("crawler.HousingShiYanService.crawler.fail", "数据采集失败！");
				taskHousing.setPhase_status("FAIL");
				taskHousing.setDescription(webParam.getHtml());
				taskHousing.setError_code(201);
				save(taskHousing);
			}
		} catch (Exception e) {
			e.printStackTrace();
			taskHousing.setPhase_status("FAIL");
			taskHousing.setDescription("网络异常，请您稍后重试！");
			taskHousing.setError_code(404);
			save(taskHousing);
		}
		return taskHousing;
	}

	@Override
	public TaskHousing getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

}