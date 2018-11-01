package app.service.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * 项目名称：common-microservice-search-task 类名称：Log 类描述： 创建人：hyx 创建时间：2018年7月18日
 * 上午10:42:45
 * 
 * @version
 */
@Component
public class SysLog {

	static final Logger log = LoggerFactory.getLogger(SysLog.class);

	public void output(String key, String value) {
		log.info("[KEY]:{},[VALUE]{}", key, value);
	}

}
