package app.controller;

import app.commontracerlog.TracerLog;
import app.service.AgentService;
import app.service.PbccrcService;
import com.crawler.callback.json.OwnerConfig;
import com.crawler.domain.json.Result;
import com.crawler.pbccrc.json.MessageResult;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.ReportData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microservice.dao.entity.crawler.pbccrc.TaskStandalone;
import com.microservice.dao.repository.crawler.pbccrc.TaskStandaloneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by zmy on 2018/3/27.
 */
@RestController
@Configuration
@RequestMapping("/pbccrc")
public class PbccrcController {

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private PbccrcService pbccrcService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private TaskStandaloneRepository taskStandaloneRepository;

    /**
     * @Des POST 登录的代理接口，对登录请求转发到限制的实例上
     * @param pbccrcJsonBean
     */
    @PostMapping(path = "/v1/getCreditAgent")
    public String getCreditAgent(@RequestBody PbccrcJsonBean pbccrcJsonBean) {
        Result<ReportData> reportResult =  null;
        String mapping_id = pbccrcJsonBean.getMapping_id();
        tracerLog.qryKeyValue("mappingId", mapping_id);
        tracerLog.addTag("crawler.pbccrc.getCreditAgent", pbccrcJsonBean.getMapping_id());

        String result = null;
        try {
            result =  agentService.postAgent(pbccrcJsonBean, "/pbccrc/v1/getCredit");
            tracerLog.addTag("getCreditAgent.result", result);
            System.out.println("getCreditAgent.result="+ result);

        }  catch (RuntimeException e) {
            tracerLog.qryKeyValue("RuntimeException", "没有闲置的实例");
           /* reportResult = new Result<ReportData>();
            ReportData reportData = new ReportData("3", "系统繁忙，请稍后再试", null, null);
            reportResult.setData(reportData);*/
            tracerLog.addTag("PbccrcController.getCreditAgent.exception", e.getMessage());
            System.out.println("PbccrcController.getCreditAgent.exception="+ e.getMessage());
//            return gson.toJson(reportResult);
        }
        return result;
    }

    /**
     * 人行征信-chp3 1.0
     * @des: 登录、获取报告
     * @return
     */
    @PostMapping(path = "/v1/getCredit")
    public String getCreditV1(@RequestBody PbccrcJsonBean pbccrcJsonBean) throws Exception {
        tracerLog.addTag("PbccrcController.getCreditV1", pbccrcJsonBean.toString());
        tracerLog.qryKeyValue("PbccrcController.getCreditV1.username", pbccrcJsonBean.getUsername());
        tracerLog.addTag("是否需要返回源码html", pbccrcJsonBean.isHtml()+"");
        tracerLog.qryKeyValue("mappingId", pbccrcJsonBean.getMapping_id());
        tracerLog.qryKeyValue("ip",pbccrcJsonBean.getIp());
        tracerLog.qryKeyValue("port",pbccrcJsonBean.getPort());

        Gson gson = new GsonBuilder().create();
        TaskStandalone taskStandalone = taskStandaloneRepository.findByTaskid(pbccrcJsonBean.getMapping_id());
        System.out.println("taskStandalone====="+taskStandalone);
        //准备登录
        pbccrcJsonBean.setVersion(1);
        if (pbccrcJsonBean.getOwner().equals(OwnerConfig.HUIJIN)) {
            String result = pbccrcService.getCredit(pbccrcJsonBean);//【汇金】调用同步接口
            return result;
        } else {
            tracerLog.qryKeyValue("key", pbccrcJsonBean.getKey());
            pbccrcService.getCreditAsync(pbccrcJsonBean); //调用异步接口
            return gson.toJson(taskStandalone);
        }
    }

    /**
     * 系统退出，释放资源
     * @param pbccrcJsonBean
     */
    @PostMapping(path = "/v1/quit")
    public void quit(@RequestBody PbccrcJsonBean pbccrcJsonBean){
        pbccrcService.quit(pbccrcJsonBean);
    }

    /**
     * 测试发送状态接口
     * @param status
     * @throws IOException
     */
    @RequestMapping(value = "/sendMessageResultTest", method = RequestMethod.POST)
    public @ResponseBody
    String sentstatus(@RequestBody MessageResult status) throws IOException {
        System.out.println("############# status ################" );
        System.out.println("^^^^^" + status);
        return "成功";
    }
}
