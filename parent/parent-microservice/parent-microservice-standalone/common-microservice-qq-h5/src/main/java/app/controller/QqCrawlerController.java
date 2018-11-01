package app.controller;

import com.crawler.qq.json.TaskQQ;
import app.commontracerlog.TracerLog;
import com.crawler.pbccrc.json.PbccrcJsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.client.QqClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/h5/qq")
    public class QqCrawlerController {
    private static final Logger log= LoggerFactory.getLogger(QqCrawlerController.class);
    @Autowired
    private TracerLog tracer;
    @Autowired
    private QqClient qqClient;

    @PostMapping(value = "/login")
    public @ResponseBody
    TaskQQ login( PbccrcJsonBean pbccrcJsonBean) {
        tracer.addTag("taskid", pbccrcJsonBean.getMapping_id());
        TaskQQ taskQQa = new TaskQQ();
            log.info("-----------qq 登录------------" + pbccrcJsonBean);
        taskQQa = qqClient.login(pbccrcJsonBean);

        log.info("-----------taskJda------------" + taskQQa);
        return  taskQQa;
    }

    @PostMapping(value = "/crawler")
    public @ResponseBody
    TaskQQ crawler( PbccrcJsonBean pbccrcJsonBean) {
        tracer.addTag("taskid", pbccrcJsonBean.getMapping_id());
        TaskQQ taskQQa = new TaskQQ();
        log.info("-----------京东 验证二维码------------" + pbccrcJsonBean);
        taskQQa = qqClient.crawler(pbccrcJsonBean);

        log.info("-----------taskJda------------" + taskQQa);
        return  taskQQa;
    }
}
