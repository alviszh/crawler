package app.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import app.service.CcbChinaService;

/**
 * 定时任务
 * @author tz
 *
 */
@Component
public class TimedTask {
	
	public static final Logger log = LoggerFactory.getLogger(TimedTask.class);
	
	@Autowired
	private CcbChinaService ccbChinaService;
	
	@Value("${loginName}")
	String loginName;
	
	@Value("${password}")
	String password;
	
	
	@Scheduled(cron = "${taskQuartz}")
	public void crawler(){
		log.info("开始爬取任务");
		
		try {
			ccbChinaService.loginByAccountNum(loginName,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
