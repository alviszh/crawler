package app.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.e_commerce.json.E_CommerceTask;

import app.client.ecom.EcomTaskClient;


@Controller
@RequestMapping("/h5/tb")
public class AjaxAuthController {
    private static final Logger log= LoggerFactory.getLogger(AjaxAuthController.class);

    @Autowired
    private EcomTaskClient tbTaskClient;

    /**
     * 间隔一秒获取task的状态
     * @param taskid
     * @return
     */
    @RequestMapping(value = "/tasks/status", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    E_CommerceTask intervalStatus(@RequestParam(name = "taskid") String taskid) {
        log.info("-----------获取task的状态------------" + taskid);
        E_CommerceTask e_CommerceTaska = tbTaskClient.taskStatus(taskid);
        log.info("-----------e_CommerceTaska------------" + e_CommerceTaska);
        return  e_CommerceTaska;
    }
}
