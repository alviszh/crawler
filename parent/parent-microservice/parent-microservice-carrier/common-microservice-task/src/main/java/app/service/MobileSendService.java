package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.mobile.TaskMobile;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;

/**
 *  推送前置规则、推送状态
 */

@Component
public class MobileSendService{
    @Autowired
    private TracerLog tracer;
    @Autowired
    private MobilePrecedingRuleService mobilePrecedingRuleService;

    /**
     * 推送前置规则
     * @param taskStandalone
     * @param reportDataResultStr
     */
    public void sendPrecedingRule(TaskMobile taskMobile){
    	tracer.qryKeyValue("retryPrecedingRule-start",taskMobile.getTaskid());
        try {
            String sendCreditReport = mobilePrecedingRuleService.retryPrecedingRule(taskMobile);
            System.out.println("推送结果：" + sendCreditReport);
        } catch (RuntimeException e) {
            tracer.addTag("前置规则推送失败-重试三次", ExUtils.getEDetail(e));
        }
        tracer.qryKeyValue("retryPrecedingRule-end",taskMobile.getTaskid());

    }
}
