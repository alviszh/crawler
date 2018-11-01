package app.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crawler.mobile.json.MessageLogin;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonHtml;
import com.microservice.dao.entity.crawler.telecom.common.TelecomCommonPointsAndCharges;
import com.microservice.dao.entity.crawler.telecom.common.TelecomStarlevel;
import app.bean.WebParamTelecom;
import app.crawler.telecom.htmlparse.TelecomParseCommon;

@Component
@Service
@EnableAsync
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.telecom.common")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.telecom.common")
public class TelecomAyscCommonService extends TelecomBasicService{

	public static final Logger log = LoggerFactory.getLogger(TelecomAyscCommonService.class);


	@Autowired
	private LoginAndGetService loginAndGetService;


	
	// 抓取用户余额
	@Async
	public String getPointsAndCharges(MessageLogin messageLogin, TaskMobile taskMobile) {
		tracerLog.addTag("==============>中国电信抓取客户余额和积<===============", messageLogin.getTask_id());

		String html = null;
		try {
			html = loginAndGetService.getPointsAndCharges(messageLogin, taskMobile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		taskMobile = taskMobileRepository.findByTaskid(taskMobile.getTaskid());

		tracerLog.addTag("==============>中国电信抓取客户余额和积月  <===============", taskMobile.toString());

		if (html == null) {
			return null;
		}

		tracerLog.addTag("==============>中国电信抓取客户用户余额和积  存储客户账单信息详单<===============", messageLogin.getTask_id());

		TelecomCommonPointsAndCharges result = TelecomParseCommon.pointsAndCharges_parse(html);
		if (result == null) {
			save(taskMobile);
			crawlerStatusMobileService.updateTaskMobile(taskMobile.getTaskid());
			return null;
		}
		result.setUserid(messageLogin.getUser_id());
		result.setTaskid(taskMobile.getTaskid());
		save(result);
		tracerLog.addTag("==============>中国电信抓取客户用户余额和积  存储客户账单信息详单<===============", messageLogin.getTask_id());

		return "sucess";
	}

	/**
	 * 用户星级服务信息
	 * 
	 * @param messageLogin
	 * @param taskMobile
	 * @return
	 */
	@Async
	public void getStarlevel(MessageLogin messageLogin,TaskMobile taskMobile) {
		tracerLog.addTag("TelecomShanxi1Service.getStarlevel", taskMobile.getTaskid());
		try {
			WebParamTelecom<TelecomStarlevel> webParam = loginAndGetService.getStarlevel(taskMobile,0);

			if (null != webParam) {

				if (null != webParam.getList()) {

					telecomCommonStarlevelRepository.saveAll(webParam.getList());

					tracerLog.addTag("TelecomShanxi1Service.getStarlevel---用户星级服务信息",
							"用户星级服务信息已入库!" + taskMobile.getTaskid());
				}

				TelecomCommonHtml telecomCommonHtml = new TelecomCommonHtml(taskMobile.getTaskid(),
						"telecom_starlevel", "1", webParam.getUrl(), webParam.getHtml());
				telecomCommonHtmlRepository.save(telecomCommonHtml);

				tracerLog.addTag("用户星级服务信息源码", "用户星级服务信息源码表入库!" + taskMobile.getTaskid());

			} else {
				tracerLog.addTag("webParam is null", taskMobile.getTaskid());
			}
		} catch (Exception e) {

			e.printStackTrace();
			tracerLog.addTag("TelecomShanxi1Service.getStarlevel---ERROR", taskMobile.getTaskid() + "---ERROR:" + e);
		}
	}

	public boolean isDoing(MessageLogin messageLogin) {
		TaskMobile taskMobile = taskMobileRepository.findByTaskid(messageLogin.getTask_id());
		tracerLog.addTag("正在进行上次未完成的爬取任务。。。", taskMobile.toString());
		if ("CRAWLER".equals(taskMobile.getPhase()) && "DOING".equals(taskMobile.getPhase_status())) {
			return true;
		}
		return false;
	}

	protected void save(TaskMobile taskMobile) {
		taskMobileRepository.save(taskMobile);
	}

	protected TaskMobile findtaskMobile(String taskid) {
		return taskMobileRepository.findByTaskid(taskid);
	}

	protected void save(TelecomCommonPointsAndCharges result) {
		telecomCommonPointsAndChargesRepository.save(result);
	}
	
	protected void save(TelecomCommonHtml result) {
		telecomCommonHtmlRepository.save(result);
	}

}