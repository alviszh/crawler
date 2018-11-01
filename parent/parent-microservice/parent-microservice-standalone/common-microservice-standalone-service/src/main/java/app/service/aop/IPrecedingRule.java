package app.service.aop;

import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;

/**
 * 人行征信-回调接口
 */
public interface IPrecedingRule {

    /**
     * 发送前置规则、回调接口请求的封装
     * @param taskStandalone
     * @param reportDataResultStr
     */
    public String retryPrecedingRule(TaskStandalone taskStandalone, String reportDataResultStr);
}
