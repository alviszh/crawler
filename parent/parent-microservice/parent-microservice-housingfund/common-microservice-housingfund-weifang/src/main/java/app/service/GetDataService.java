package app.service;

import java.util.List;

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
import com.microservice.dao.entity.crawler.housing.weifang.HousingWeiFangHtml;
import com.microservice.dao.repository.crawler.housing.weifang.HousingWeiFangHtmlRepository;
import com.microservice.dao.repository.crawler.housing.weifang.HousingWeiFangPayRepository;
import com.microservice.dao.repository.crawler.housing.weifang.HousingWeiFangUserinfoRepository;

import app.common.WebParam;
import app.common.ReqParam;
import app.parser.HousingWeiFangParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.weifang")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.weifang")
public class GetDataService extends HousingBasicService{

	@Autowired
	private HousingWeiFangUserinfoRepository housingWeiFangUserinfoRepository;
	@Autowired
	private HousingWeiFangPayRepository housingWeiFangPayRepository;
	@Autowired
	private HousingWeiFangHtmlRepository housingWeiFangHtmlRepository;
	@Autowired
	private HousingWeiFangParser housingWeiFangParser;

	public void getUserInfo(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("crawler.GetDataService.getUserInfo.taskid", taskHousing.getTaskid());
		try {
			List<HousingWeiFangHtml> loginedHtmls = housingWeiFangHtmlRepository.findByTaskidAndTypeOrderByIdDesc(taskHousing.getTaskid(), "logined");
			WebParam webParam = housingWeiFangParser.getUserInfo(loginedHtmls.get(0), taskHousing);
			housingWeiFangUserinfoRepository.saveAll(webParam.getList());
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

	public void getTrans(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		tracer.addTag("crawler.getTrans.taskid", taskHousing.getTaskid());
		int a = 0;	//流水计数
		try {
			//先获取单位编号
			WebParam webParam = housingWeiFangParser.getParams(messageLoginForHousing, taskHousing);
			HousingWeiFangHtml html = new HousingWeiFangHtml();
			html.setUrl(webParam.getUrl());
			html.setType("selects");
			html.setPageCount(1);
			html.setHtml(webParam.getHtml());
			html.setTaskid(taskHousing.getTaskid());
			housingWeiFangHtmlRepository.save(html);
			tracer.addTag("crawler.getTrans.page.success1", "流水信息查询页面已经入库");
			if(null != webParam.getList() && webParam.getList().size() > 0){
				List<ReqParam> params = webParam.getList();
				for (ReqParam reqParam : params) {
					for (int i = 0; i < 5; i++) {
						WebParam webParam1 = housingWeiFangParser.getTrans(reqParam, taskHousing, webParam.getWebClient(), i);
						if(null != webParam1.getHtml()){
							HousingWeiFangHtml html1 = new HousingWeiFangHtml();
							html1.setUrl(webParam1.getUrl());
							html1.setType("trans"+i);
							html1.setPageCount(1);
							html1.setHtml(webParam1.getHtml());
							html1.setTaskid(taskHousing.getTaskid());
							housingWeiFangHtmlRepository.save(html1);
							tracer.addTag("crawler.getTrans.page.success", "流水信息页面已经入库");
						}
						if(null != webParam1.getList() && webParam1.getList().size() > 0){
							housingWeiFangPayRepository.saveAll(webParam1.getList());
							a++;
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			tracer.addTag("crawler.getTrans.crawler.payment.fail", e.toString());
		}
		if(a > 0){
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
			tracer.addTag("crawler.getTrans.crawler.fail2", "流水信息入库失败");
		}
	}
	
	
}
