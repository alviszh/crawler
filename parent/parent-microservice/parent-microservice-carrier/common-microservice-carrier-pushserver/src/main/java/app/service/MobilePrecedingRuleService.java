package app.service;

import app.client.MobileResultClient;
import app.commontracerlog.TracerLog;
import com.crawler.callback.json.OwnerConfig;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

@Component
@EntityScan(basePackages = "com.microservice.dao.entity.crawler.mobile")
@EnableJpaRepositories(basePackages = "com.microservice.dao.repository.crawler.mobile")
public class MobilePrecedingRuleService {
	public static final Logger log = LoggerFactory.getLogger(MobilePrecedingRuleService.class);

	@Autowired
	private TracerLog tracerLog;
	@Autowired
	private MobileResultClient mobileResultClient;

	public String retryPrecedingRule(TaskMobile taskMobile) {
		tracerLog.qryKeyValue("MobilePrecedingRuleService.retryPrecedingRuleStart",taskMobile.getTaskid());
		String owner = taskMobile.getOwner();
		String key = taskMobile.getKey();
		String taskId = taskMobile.getTaskid();
		tracerLog.addTag("MobilePrecedingRuleService.retryPrecedingRule",
				"回调接口" + taskId + "-key=" + taskMobile.getKey() + ",owner=" + owner);
		log.info("进入回调接口sendMobileResult,taskid=" + taskId + "-key=" + key + ",owner=" + owner);
		String result = null;
		int code = 0;
		try {
			code = OwnerConfig.getOwnerMap().get(owner);
		} catch (NullPointerException e) {
			tracerLog.addTag("未知的owner", "owner=" + owner);
		}
		switch (code) {
		case OwnerConfig.HUICHENG_INT:
			tracerLog.addTag("mobileinfo.report", "开始调用回调接口， taskId=" + taskId + "**** key=" + key + "，owner=" + owner);
			result = mobileResultClient.sendMobileResult(taskId, key);
			tracerLog.addTag("mobileinfo.report.result", "完成回调接口的调用，返回结果result=" + result);
			break;
		default:
			tracerLog.addTag("mobileinfo.report",
					"没有传入合适的owner值，taskId=" + taskId + "**** key=" + key + "，owner=" + owner);
			break;
		}
		return result;
	}

}
