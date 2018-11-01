package app.controller.developer;


import app.bean.ApitestResult;
import app.client.PbccrcH5Client;
import com.crawler.report.json.jiemo.pbccrc.PbccrcCreditReportJiemo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/developer/app")
public class AppPbccrcRestController {

    @Autowired
    private PbccrcH5Client pbccrcH5Client;

    @Value("${pbccrc.prod.url.domain}")
    String prodDomain;

    @RequestMapping(value = "/pbccrc/v2/report", method = RequestMethod.POST)
    public ApitestResult report(String taskId){
        System.out.println("taskId="+taskId);
        ApitestResult result = new ApitestResult();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String reportResult = pbccrcH5Client.report(taskId);
        result.setData(reportResult);
        result.setUrl(prodDomain + "/h5/pbccrc/v2/report/" + taskId);
        System.out.println("AppPbccrcRestController.report===" + reportResult);
        return result;
    }
}
