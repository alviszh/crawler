package app.service;

import app.client.standalone.OpendataClient;
import app.commontracerlog.TracerLog;
import com.crawler.NoticeInfo;
import com.crawler.mobile.json.StatusCodeEnum;
import com.crawler.opendata.json.developer.AppProductList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.mobile.TaskMobile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *  推送状态
 */

@Component
public class MobileSendService{
    @Autowired
    private TracerLog tracer;
    @Autowired
    private OpendataClient opendataClient;
    @Autowired
    private PushServerService pushServerService;

    /**
     * 推送通知
     * @param taskStandalone
     */
    public void sendMessageResult(TaskMobile taskMobile){
        tracer.qryKeyValue("taskid", taskMobile.getTaskid());

        Gson gson = new GsonBuilder().create();
        NoticeInfo noticeInfo = new NoticeInfo(taskMobile.getKey(), taskMobile.getBasicUser().getId()+"",
                taskMobile.getTaskid(),taskMobile.getPhase() , taskMobile.getDescription(),
                String.valueOf(System.currentTimeMillis()));
        String requestBody = gson.toJson(noticeInfo);
        tracer.addTag("准备发送状态:requestBody=",requestBody);
        System.out.println("准备发送状态:" + taskMobile.getDescription());

        //获取回调地址
        AppProductList appProductList = opendataClient.findAppProductList(taskMobile.getOwner(), "carrier",
                taskMobile.getEnvironmentId());
        System.out.println("appProductList==="+appProductList);
        tracer.addTag("appProductList=", appProductList+"");
        if (appProductList != null) {
            System.out.println("callbackparams=" + appProductList.getCallbackparams());
            String task_url = appProductList.getTask_notice_url(); //任务创建通知接口
            String login_url = appProductList.getLogin_notice_url(); //授权结果通知接口
            String crawler_url = appProductList.getCrawler_notice_url();//采集结果通知接口
            String report_url = appProductList.getReport_notice_url();//报告生成通知接口

            //任务创建结果


            //授权结果
            if (taskMobile.getPhase().equals(StatusCodeEnum.TASKMOBILE_LOGIN_LOADING.getPhase())) {
                if (login_url != null) {
                    Map<String, Object> result = pushServerService.requestnoticeurl(login_url, requestBody, null);
                    System.out.println("sendResul=" + result);
                }
            }
            //采集结果
            if (taskMobile.getPhase().equals(StatusCodeEnum.TASKMOBILE_CRAWLER_SUCCESS.getPhase())) {
                if (crawler_url != null) {
                    Map<String, Object> result = pushServerService.requestnoticeurl(crawler_url, requestBody, null);
                    System.out.println("sendResul=" + result);
                }
            }
            //推送报告
            if (taskMobile.getPhase().equals(StatusCodeEnum.TASKMOBILE_REPORT_DONING.getPhase())) {
                if (report_url != null) {
                    Map<String, Object> result = pushServerService.requestnoticeurl(report_url, requestBody, null);
                    System.out.println("sendResul=" + result);
                }
            }
        }
    }
}
