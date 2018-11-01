package app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.yinchuan.HousingYinChuanHtml;
import com.microservice.dao.repository.crawler.housing.yinchuan.HousingYinChuanHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yinchuan.HousingYinChuanUserinfoRepository;

import app.common.WebParam;
import app.parser.HousingYinChuanParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.yinchuan")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.yinchuan")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingYinChuanUserinfoRepository housingYinChuanUserinfoRepository;
	@Autowired
	private HousingYinChuanHtmlRepository housingYinChuanHtmlRepository;
	@Autowired
	private HousingYinChuanParser housingYinChuanParser;

	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("crawler.GetDataService.getUserInfo.taskid", taskHousing.getTaskid());
		try {
			List<HousingYinChuanHtml> loginedHtmls = housingYinChuanHtmlRepository.findByTaskidAndTypeOrderByIdDesc(taskHousing.getTaskid(), "logined");
			WebParam webParam = housingYinChuanParser.getUserInfo(loginedHtmls.get(0), taskHousing);
			housingYinChuanUserinfoRepository.saveAll(webParam.getList());
			taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhase());
			taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getPhasestatus());
			taskHousing.setDescription("[数据采集中]个人信息采集成功！");
			taskHousing.setUserinfoStatus(200);
			taskHousing.setError_code(HousingfundStatusCodeEnum.HOUSING_CRAWLER_DONING.getError_code());
			save(taskHousing);
			tracer.addTag("crawler.GetDataService.getUserInfo.success", "用户信息已经入库");
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

}
