package app.controller.v2;


import app.bean.MessageResult;
import app.client.standalone.StandaloneTaskClient;
import app.client.v2.V2PbccrcClient;
import app.commontracerlog.TracerLog;
import com.crawler.domain.json.Result;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import com.crawler.pbccrc.json.ReportData;
import com.crawler.pbccrc.json.TaskStandalone;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/h5/pbccrc/v2")
public class V2PbccrcRestController {

    public static final Logger log = LoggerFactory.getLogger(V2PbccrcRestController.class);

    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private V2PbccrcClient v2PbccrcClient;

    @Value("${spring.appName}")
    String appName;
    @Autowired
    private StandaloneTaskClient standaloneTaskClient;

    /**
     * 获取人行征信报告
     * @param pbccrcJsonBean
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
    public  TaskStandalone loginAndGetcreditV1(PbccrcJsonBean pbccrcJsonBean) throws IOException {
        // Application app =getApplicationByName(appName);
        // List<InstanceInfo> instances = app.getInstances();
        System.out.println("pbccrcJsonBean:" + pbccrcJsonBean);
        long startTime = System.currentTimeMillis();
        // 设置任务id
        String mappingId = UUID.randomUUID().toString(); // 唯一标识
        pbccrcJsonBean.setMapping_id(mappingId);
        System.out.println("mappingId="+mappingId);

        //创建task
        pbccrcJsonBean.setServiceName("pbccrc-v2"); //服务名称（人行征信）
        TaskStandalone taskStandalone = standaloneTaskClient.createTask(pbccrcJsonBean);

        String owner = pbccrcJsonBean.getOwner();
        tracerLog.qryKeyValue("mappingId", pbccrcJsonBean.getMapping_id());
        tracerLog.qryKeyValue("taskid", taskStandalone.getTaskid());
        tracerLog.qryKeyValue("owner", owner);
        tracerLog.qryKeyValue("key", pbccrcJsonBean.getKey());
        tracerLog.qryKeyValue("username", pbccrcJsonBean.getUsername());// 账号
        tracerLog.qryKeyValue("password", pbccrcJsonBean.getPassword());// 密码
        tracerLog.qryKeyValue("tradecode", pbccrcJsonBean.getTradecode());// 授权码

        tracerLog.qryKeyValue("LoginController.loginAndGetcreditV1.creditParam",
                "开始获取报告，creditParam=" + pbccrcJsonBean);
        log.info("------------获取报告信息loginv1----------------" + pbccrcJsonBean);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MessageResult messageResult = new MessageResult();

        String reportDataResultStr = "";
        try {
            String result = v2PbccrcClient.loginAndGetcreditV(pbccrcJsonBean);
            tracerLog.addTag("loginAndGetcreditV.taskStandalone", result);

            if (result != null) {
                taskStandalone = gson.fromJson(result,
                        new TypeToken<TaskStandalone>() {
                        }.getType());
            }
        } catch (Exception e) {
            System.out.println("Exception" + e.toString());
            tracerLog.qryKeyValue("Exception", e.toString());
        }

        if (reportDataResultStr != null && !reportDataResultStr.equals("")) {
            Result<ReportData> reportDataResult = gson.fromJson(reportDataResultStr,
                    new TypeToken<Result<ReportData>>() {
                    }.getType());
            if (reportDataResult != null) {
                ReportData reportData = reportDataResult.getData();
                System.out.println("获取报告结果，reportDataResult=" + reportDataResult);
                String statusCode = reportData.getStatusCode();
                String message = reportData.getMessage();
                tracerLog.qryKeyValue("statusCode", statusCode);
                tracerLog.qryKeyValue("message", message);
                if (message != null && message.contains("查询成功")) {
                    String html = reportDataResult.getHtml();
                    String reportId = reportData.getReport().getReportBase().getReportId();// 报告编号
                    String realName = reportData.getReport().getReportBase().getRealname();// 姓名
                    String certificateNum = reportData.getReport().getReportBase().getCertificateNum();// 身份证号码
                    // tracerLog.qryKeyValue("html",html); //保存报告结果中的html
                    tracerLog.qryKeyValue("reportId", reportId);
                    tracerLog.qryKeyValue("realName", realName);
                    tracerLog.qryKeyValue("certificateNum", certificateNum);
                }
                messageResult.setStatusCode(reportData.getStatusCode());
                messageResult.setMessage(message);
            } else {
                messageResult.setMessage("报告获取失败！");
                tracerLog.qryKeyValue("message", "报告获取失败");
            }
        }
        tracerLog.qryKeyValue("LoginController.getCredit.taskStandalone",
                taskStandalone + "，key=" + pbccrcJsonBean.getKey());

        long endTime = System.currentTimeMillis();
        tracerLog.qryKeyValue("耗时", (endTime - startTime) + ":ms");
        return taskStandalone;
    }

    /**
     * 查看报告数据
     * 大金融-借么(薪易借、拍易借)
     * @param taskId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getReportJieMo", method = {RequestMethod.POST, RequestMethod.GET})
    public String getReportJieMo(@RequestParam("renhangzhengxinTaskId") String taskId) throws Exception {
        tracerLog.qryKeyValue("PbccrcController.getReportJieMo", "获取报告");
        tracerLog.qryKeyValue("taskid", taskId);

        String reportResult = standaloneTaskClient.getReport(taskId, "jiemo");
        System.out.println("PbccrcController.getReportJieMo.result===" + reportResult);
        tracerLog.addTag("借么-解析后的JSON", reportResult);

        return reportResult;
    }

    /**
     * 获取人行征信报告 - 产品化
     * @param taskid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/report/{taskid}", method = { RequestMethod.GET})
    public String report(@PathVariable String taskid) throws Exception {
        tracerLog.qryKeyValue("PbccrcController.report", "获取报告");
        tracerLog.qryKeyValue("taskid", taskid);

        String reportResult = standaloneTaskClient.getReport(taskid, "jiemo");
        System.out.println("PbccrcController.report.result===" + reportResult);
        tracerLog.addTag("报告JSON", reportResult);

        return reportResult;
    }

    /**
     * 获取报告
     * @param taskId 任务Id
     * @param schema 业务方（借么、贷乎...）
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getReport", method = {RequestMethod.POST, RequestMethod.GET})
     public String getReport(@RequestParam("taskid") String taskId,
                             @RequestParam(name="schema", required = false) String schema) throws Exception {
        tracerLog.qryKeyValue("PbccrcController.getReport", "获取报告");
        tracerLog.qryKeyValue("taskid", taskId);
        tracerLog.qryKeyValue("schema",schema);

        String reportResult = standaloneTaskClient.getReport(taskId, schema);
        System.out.println("PbccrcController.getReport.result===" + reportResult);
        tracerLog.addTag("解析后的JSON", reportResult);

        return reportResult;
    }

    /**
     * 间隔一秒获取task的状态
     * @param taskid
     * @return
     */
    @RequestMapping(value = "/standalone/tasks/status", method = {RequestMethod.GET, RequestMethod.POST})
    public TaskStandalone intervalStatus(@RequestParam(name = "taskid") String taskid) {
        log.info("-----------获取task的状态------------" + taskid);
        TaskStandalone taskStandalone = standaloneTaskClient.getTaskStandalone(taskid);
        log.info("-----------taskMobile------------" + taskStandalone);
        return  taskStandalone;
    }

    /**
     * 测试发送报告结果
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/sendResultTest", method = RequestMethod.POST)
    public
    /*String notifications(@RequestBody String creditReport,@RequestParam("key") String key) throws IOException {
        log.info("#############################" );
        log.info("^^^^^"+creditReport);
        return "发送成功";
    }*/
    String notifications(@RequestBody String precedingRule) throws IOException {
        System.out.println("#############################" );
        System.out.println("^^^^^" + precedingRule);
        return "发送成功";
    }
}
