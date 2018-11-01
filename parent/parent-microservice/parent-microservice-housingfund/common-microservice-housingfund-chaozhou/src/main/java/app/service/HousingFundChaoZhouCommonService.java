package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.housingfund.json.HousingfundStatusCodeEnum;
import com.crawler.housingfund.json.MessageLoginForHousing;
import com.crawler.microservice.unit.CommonUnit;
import com.crawler.mobile.json.StatusCodeEnum;
import com.microservice.dao.entity.crawler.housing.basic.TaskHousing;
import com.microservice.dao.entity.crawler.housing.chaozhou.HousingChaoZhouBase;
import com.microservice.dao.entity.crawler.housing.chaozhou.HousingChaoZhouHtml;
import com.microservice.dao.entity.crawler.housing.dalibaizu.HousingDaLiBaiZuDetail;
import com.microservice.dao.entity.crawler.housing.dalibaizu.HousingDaLiBaiZuHtml;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuBasic;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuDetail;
import com.microservice.dao.entity.crawler.housing.shangqiu.HousingShangQiuHtml;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinBasic;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinDetail;
import com.microservice.dao.entity.crawler.housing.yulin.HousingYuLinHtml;
import com.microservice.dao.entity.crawler.housing.zhaotong.HousingZhaoTongBase;
import com.microservice.dao.entity.crawler.housing.zhaotong.HousingZhaoTongHtml;
import com.microservice.dao.repository.crawler.housing.chaozhou.HousingChaoZhouBaseRepository;
import com.microservice.dao.repository.crawler.housing.chaozhou.HousingChaoZhouHtmlRepository;
import com.microservice.dao.repository.crawler.housing.dalibaizu.HousingDaLiBaiZuDetailRepository;
import com.microservice.dao.repository.crawler.housing.dalibaizu.HousingDaLiBaiZuHtmlRepository;
import com.microservice.dao.repository.crawler.housing.shangqiu.HousingShangqiuBasicRepository;
import com.microservice.dao.repository.crawler.housing.shangqiu.HousingShangqiuDetailRepository;
import com.microservice.dao.repository.crawler.housing.shangqiu.HousingShangqiuHtmlRepository;
import com.microservice.dao.repository.crawler.housing.yulin.HousingYulinBasicRepository;
import com.microservice.dao.repository.crawler.housing.yulin.HousingYulinDetailRepository;
import com.microservice.dao.repository.crawler.housing.yulin.HousingYulinHtmlRepository;
import com.microservice.dao.repository.crawler.housing.zhaotong.HousingZhaotongBaseRepository;
import com.microservice.dao.repository.crawler.housing.zhaotong.HousingZhaotongHtmlRepository;

import app.common.WebParam;
import app.parser.HousingFundChaoZhouParser;
import app.service.common.HousingBasicService;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.housing.chaozhou")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.housing.chaozhou")
public class HousingFundChaoZhouCommonService extends HousingBasicService{

	
	@Autowired
	private HousingFundChaoZhouParser housingFundChaoZhouParser;
	@Autowired
	private HousingChaoZhouBaseRepository baseRepository;
	@Autowired
	private HousingChaoZhouHtmlRepository htmlRepository;



	public void crawler(MessageLoginForHousing messageLoginForHousing, TaskHousing taskHousing) {
		try {
			WebParam<HousingChaoZhouBase> webParam = housingFundChaoZhouParser.crawler(messageLoginForHousing,taskHousing);
			if(null!=webParam.getHtml()){
				if(webParam.getHtml().contains("抱歉，您输入的身份证号、密码有误或不存在")){
					tracer.addTag("parser.login.ERROR.NUMORPASSWORD", taskHousing.getTaskid());
					taskHousing.setPhase(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhase());
					taskHousing.setPhase_status(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getPhasestatus());
					taskHousing.setDescription(HousingfundStatusCodeEnum.HOUSING_LOGIN_IDNUMORPWD_ERROR.getDescription());
					taskHousing.setPassword(messageLoginForHousing.getPassword().trim());
					taskHousing.setPaymentStatus(102);
					save(taskHousing);
				}else if(webParam.getHtml().contains("个人公积金用户基本信息")){
					tracer.addTag("HousingChaozhou.action.getuserInfo.SUCCESS", taskHousing.getTaskid());
					updateUserInfoStatusByTaskid("【个人基本信息】采集完成！", 200, taskHousing.getTaskid());
					updatePayStatusByTaskid("【缴费明细信息】已采集完成！", 201, taskHousing.getTaskid());
					HousingChaoZhouHtml  html = new HousingChaoZhouHtml();
					html.setHtml(webParam.getHtml());
					html.setTaskid(messageLoginForHousing.getTask_id());
					html.setUrl(webParam.getUrl());
					htmlRepository.save(html);
					baseRepository.save(webParam.getChaozhouBase());
					updateTaskHousing(taskHousing.getTaskid());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

}
