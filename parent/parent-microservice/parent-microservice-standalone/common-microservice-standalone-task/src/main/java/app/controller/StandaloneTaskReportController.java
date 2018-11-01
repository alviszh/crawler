package app.controller;

import app.commontracerlog.TracerLog;
import app.service.pbccrc.PbccrcReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/standalone")
public class StandaloneTaskReportController {


    @Autowired
    private TracerLog tracerLog;
    @Autowired
    private PbccrcReportService pbccrcReportService;

    /**
     * 获取征信报告（解析V2）
     * schema : jieme、daihu
     * @param taskId
     * @return
     * @throws Exception
     */
    @GetMapping(path = "pbccrc/getReport")
    public String getReport(@RequestParam("taskid") String taskId,
                            @RequestParam(name="schema", required = false) String schema) throws Exception {
        tracerLog.qryKeyValue("PbccrcController.getReport", "借么");
        tracerLog.qryKeyValue("taskid", taskId);
        //准备登录
        String reportResult = pbccrcReportService.getReport(taskId, schema);
        System.out.println("PbccrcController.getReport.result===" + reportResult);
        tracerLog.addTag("借么-解析后的JSON", reportResult);

        return reportResult;
    }
}
