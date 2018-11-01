package app.controller;

import app.commontracerlog.TracerLog;
import app.service.E_CommerceTaskService;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zz
 */
@RestController
@RequestMapping("/ecommerce")
public class E_CommerceTaskController {

    @Autowired
    private TracerLog tracer;
    @Autowired
    private E_CommerceTaskService e_commerceTaskService;

    public static final Logger log = LoggerFactory.getLogger(E_CommerceTaskController.class);

    @PostMapping(path = "/check")
    public E_CommerceTask createTask(@RequestBody E_CommerceJsonBean e_commerceJsonBean) {

        tracer.addTag("E_CommerceJsonBean =======>>", e_commerceJsonBean.toString());

        return e_commerceTaskService.createTask(e_commerceJsonBean);

    }


    @GetMapping(path = "/tasks/{taskid}/status")
    public E_CommerceTask taskStatus(@PathVariable String taskid) {

        E_CommerceTask e_commerceTask = e_commerceTaskService.getE_CommerceTask(taskid);
        tracer.addTag("Request task status", "taskid:" + taskid);
        return e_commerceTask;
    }
}
