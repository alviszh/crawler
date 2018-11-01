package app.controller.data;


import app.client.standalone.StandaloneTaskClient;
import app.commontracerlog.TracerLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人行征信报告
 */
@RestController
@RequestMapping(value="/data/pbccrc")
public class PbccrcDataRestController {

    @Autowired
    private StandaloneTaskClient standaloneTaskClient;
    @Autowired
    private TracerLog tracerLog;

    /**
     * 获取人行征信报告JSON - 产品化
     * @param taskid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/report/{taskid}", method = { RequestMethod.GET})
    public String report(@PathVariable String taskid) throws Exception {
        tracerLog.qryKeyValue("/data/pbccrc PbccrcDataController.report", "获取报告");
        tracerLog.qryKeyValue("taskid", taskid);

        String reportResult = standaloneTaskClient.getReport(taskid, "jiemo");
        System.out.println("PbccrcController.report.result===" + reportResult);
        tracerLog.addTag("报告JSON", reportResult);
        return reportResult;
    }
}
