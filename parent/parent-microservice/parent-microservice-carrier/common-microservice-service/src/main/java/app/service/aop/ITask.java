package app.service.aop;

import com.crawler.mobile.json.MobileJsonBean;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;

/**
 * 创建task接口
 */
public interface ITask {

    public TaskMobile createTask(MobileJsonBean mobileJsonBean);
}
