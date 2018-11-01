package app.controller;

import app.bean.E_CommerceTask;
import app.client.*;
import app.commontracerlog.TracerLog;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/h5/sn")
    public class SnCrawlerController {
    private static final Logger log= LoggerFactory.getLogger(SnCrawlerController.class);
    @Autowired
    private TracerLog tracer;
    @Autowired
    private SnClient snClient;

    @PostMapping(value = "/login")
    public @ResponseBody
    E_CommerceTask login( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask e_CommerceTaska = new E_CommerceTask();
        log.info("-----------苏宁 登录------------" + e_CommerceJsonBean);
        e_CommerceTaska = snClient.login(e_CommerceJsonBean);

        log.info("-----------taskSna------------" + e_CommerceTaska);
        return  e_CommerceTaska;
    }

    @PostMapping(value = "/sendSMS")
    public @ResponseBody
    E_CommerceTask sendSMS( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask e_CommerceTaska = new E_CommerceTask();
        log.info("-----------苏宁 短信验证码------------" + e_CommerceJsonBean);
        e_CommerceTaska = snClient.sendSMS(e_CommerceJsonBean);

        log.info("-----------taskSna------------" + e_CommerceTaska);
        return  e_CommerceTaska;
    }
}
