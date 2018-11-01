package app.controller;

import com.crawler.e_commerce.json.E_CommerceTask;
import app.commontracerlog.TracerLog;
import com.crawler.e_commerce.json.E_CommerceJsonBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import app.client.ecom.JdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/h5/jd")
    public class JdCrawlerController {
    private static final Logger log= LoggerFactory.getLogger(JdCrawlerController.class);
    @Autowired
    private TracerLog tracer;
    @Autowired
    private JdClient jdClient;

    @PostMapping(value = "/login")
    public @ResponseBody
    E_CommerceTask login( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask e_CommerceTaska = new E_CommerceTask();
            log.info("-----------京东 登录------------" + e_CommerceJsonBean);
        e_CommerceTaska = jdClient.login(e_CommerceJsonBean);

        log.info("-----------taskJda------------" + e_CommerceTaska);
        return  e_CommerceTaska;
    }

    @PostMapping(value = "/sendSmsCode")
    public @ResponseBody
    E_CommerceTask sendSmsCode( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask e_CommerceTaska = new E_CommerceTask();
        log.info("-----------京东 验证二维码------------" + e_CommerceJsonBean);
        e_CommerceTaska = jdClient.sendSmsCode(e_CommerceJsonBean);

        log.info("-----------taskJda------------" + e_CommerceTaska);
        return  e_CommerceTaska;
    }

    @PostMapping(value = "/checkQRcode")
    public @ResponseBody
    E_CommerceTask checkQRcode( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 登录------------" + e_CommerceJsonBean);
        taskTba = jdClient.checkQRcode(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/base64")
    public @ResponseBody
    E_CommerceTask base64( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 登录------------" + e_CommerceJsonBean);
        taskTba = jdClient.base64(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/quit")
    public @ResponseBody
    E_CommerceTask quit( E_CommerceJsonBean e_CommerceJsonBean) {

        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 二维码登录退出------------" + e_CommerceJsonBean);
        taskTba = jdClient.quit(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }
}
