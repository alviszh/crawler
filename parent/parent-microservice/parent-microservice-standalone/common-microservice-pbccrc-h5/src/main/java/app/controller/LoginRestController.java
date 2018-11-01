package app.controller;


import app.client.standalone.StandaloneTaskClient;
import com.crawler.pbccrc.json.TaskStandalone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/h5")
public class LoginRestController {

    public static final Logger log = LoggerFactory.getLogger(LoginRestController.class);

    @Autowired
    private StandaloneTaskClient standaloneTaskClient;


    /**
     * 间隔一秒获取task的状态
     * @param taskid
     * @return
     */
    @RequestMapping(value = "/pbccrc/standalone/tasks/status", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    TaskStandalone intervalStatus(@RequestParam(name = "taskid") String taskid) {
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
    @RequestMapping(value = "/pbccrc/sendResultTest", method = RequestMethod.POST)
    public String notifications(@RequestBody String precedingRule) throws IOException {
        System.out.println("#############################" );
        System.out.println("^^^^^" + precedingRule);
        return "发送成功";
    }

}
