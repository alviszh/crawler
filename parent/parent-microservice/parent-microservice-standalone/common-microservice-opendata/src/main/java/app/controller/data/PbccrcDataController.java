package app.controller.data;

import app.client.standalone.StandaloneTaskClient;
import com.crawler.pbccrc.WebData;
import com.crawler.report.json.jiemo.pbccrc.PbccrcCreditReportJiemo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 人行征信报告
 */
@Controller
@RequestMapping(value="/data/pbccrc")
public class PbccrcDataController {

    @Autowired
    private StandaloneTaskClient standaloneTaskClient;

    /**
     *  人行征信 - 获取人行征信报告页面
     * @param model
     * @param taskId
     * @return
     */
    @RequestMapping("/reportdetail")
    public String report(Model model, String taskId){
        System.out.println("taskId="+taskId);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String reportResult = standaloneTaskClient.getReport(taskId, "jiemo");
        System.out.println("PbccrcReportRestController.reportDetail===" + reportResult);
        WebData<PbccrcCreditReportJiemo> webData = gson.fromJson(reportResult,
                new TypeToken<WebData<PbccrcCreditReportJiemo>>() {
                }.getType());
        System.out.println("webData:"+webData);
        PbccrcCreditReportJiemo report = webData.getReport();
        System.out.println("report=" +report);
        model.addAttribute("taskId", taskId);
        model.addAttribute("report", report);
        return "pbccrc/report";
    }

}
