package app.controller;

import com.crawler.qq.json.TaskQQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import app.client.QqTaskClient;

@Controller
@RequestMapping("/h5/qq")
public class AjaxAuthController {
    private static final Logger log= LoggerFactory.getLogger(AjaxAuthController.class);

    @Autowired
    private QqTaskClient qqTaskClient;

    /**
     * 间隔一秒获取task的状态
     * @param
     * @return
     */
    @RequestMapping(value = "/tasks/status", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    TaskQQ intervalStatus(@RequestParam(name = "taskid") String taskid) {
        log.info("-----------获取task的状态------------" + taskid);
        TaskQQ taskQQa = qqTaskClient.taskStatus(taskid);
        log.info("-----------taskQQa------------" + taskQQa);
        return  taskQQa;
    }
}
