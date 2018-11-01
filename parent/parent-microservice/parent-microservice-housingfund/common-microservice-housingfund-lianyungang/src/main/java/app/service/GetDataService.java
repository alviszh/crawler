package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.repository.crawler.housing.lianyungang.HousingLianYunGangPayRepository;
import com.microservice.dao.repository.crawler.housing.lianyungang.HousingLianYunGangUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingLianYunGangParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.lianyungang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.lianyungang")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingLianYunGangUserinfoRepository housingLianYunGangUserinfoRepository;
	@Autowired
	private HousingLianYunGangPayRepository housingLianYunGangPayRepository;
	@Autowired
	private HousingLianYunGangParser housingLianYunGangParser;

	@Async
	public void getUserinfo(String html, TaskHousing taskHousing) throws Exception{
		tracer.addTag("crawler.GetDataService.getUserinfo.begin", taskHousing.getTaskid());
		WebParam webParam = housingLianYunGangParser.getUserinfo(html, taskHousing);
		if(webParam.getList().size() > 0){
			housingLianYunGangUserinfoRepository.saveAll(webParam.getList());
			tracer.addTag("crawler.GetDataService.getUserinfo.success", "用户信息入库成功");
			updateUserInfoStatusByTaskid("数据采集中，用户信息采集成功！", 200, taskHousing.getTaskid());
		}else{
			tracer.addTag("crawler.GetDataService.getUserinfo.fail", "用户信息入库失败");
			updateUserInfoStatusByTaskid("数据采集中，用户信息采集完成！", 201, taskHousing.getTaskid());
		}
		updateTaskHousing(taskHousing.getTaskid());
	}

	@Async
	public void getPayinfo(List<String> htmls, TaskHousing taskHousing) throws Exception{
		tracer.addTag("crawler.GetDataService.getPayinfo.begin", taskHousing.getTaskid());
		int i = 0;//入库成功次数计数
		for (String html : htmls) {
			WebParam webParam = housingLianYunGangParser.getPayinfo(html, taskHousing);
			if(null != webParam.getList()){
				housingLianYunGangPayRepository.saveAll(webParam.getList());
				tracer.addTag("crawler.GetDataService.getPayinfo.success", "缴费信息入库成功");
				i++;
			}else{
				tracer.addTag("crawler.GetDataService.getPayinfo.fail", "缴费信息入库失败");
			}
		}
		if(i > 0){
			updatePayStatusByTaskid("数据采集中，缴费信息采集成功！", 200, taskHousing.getTaskid());
		}else{
			updatePayStatusByTaskid("数据采集中，缴费信息采集完成！", 201, taskHousing.getTaskid());
		}
		updateTaskHousing(taskHousing.getTaskid());
	}
	
	
}
