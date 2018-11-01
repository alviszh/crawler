package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.crawler.e_commerce.json.E_CommerceTask;

import app.client.ecom.TbClient;
import app.commontracerlog.TracerLog;

@Controller
@RequestMapping("/h5/tb")
public class TbCrawlerController {
    private static final Logger log= LoggerFactory.getLogger(TbCrawlerController.class);
    @Autowired
    private TracerLog tracer;
    @Autowired
    private TbClient tbClient;

    @PostMapping(value = "/login")
    public @ResponseBody
    E_CommerceTask login( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());

        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 登录------------" + e_CommerceJsonBean);
        taskTba = tbClient.login(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/base64Tb")
    public @ResponseBody
    E_CommerceTask base64Tb( E_CommerceJsonBean e_CommerceJsonBean) {

        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝获取二维码------------" + e_CommerceJsonBean);
        taskTba = tbClient.base64Tb(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/base64Ap")
    public @ResponseBody
    E_CommerceTask base64Ap( E_CommerceJsonBean e_CommerceJsonBean) {

        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------支付宝获取二维码------------" + e_CommerceJsonBean);
        taskTba = tbClient.base64Ap(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/checkQRcodeTb")
    public @ResponseBody
    E_CommerceTask checkQRcodeTb( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 登录------------" + e_CommerceJsonBean);
        taskTba = tbClient.checkQRcodeTb(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/checkQRcodeAp")
    public @ResponseBody
    E_CommerceTask checkQRcodeAp( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 登录------------" + e_CommerceJsonBean);
        taskTba = tbClient.checkQRcodeAp(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/verfiySMS")
    public @ResponseBody
    E_CommerceTask verfiySMS( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 ------------" + e_CommerceJsonBean);
        taskTba = tbClient.verfiySMS(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/crawler")
    public @ResponseBody
    E_CommerceTask crawler( E_CommerceJsonBean e_CommerceJsonBean) {
        tracer.addTag("taskid", e_CommerceJsonBean.getTaskid());
        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 爬取------------" + e_CommerceJsonBean);
        taskTba = tbClient.crawler(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }

    @PostMapping(value = "/quit")
    public @ResponseBody
    E_CommerceTask quit( E_CommerceJsonBean e_CommerceJsonBean) {

        E_CommerceTask taskTba = new E_CommerceTask();
        log.info("-----------淘宝 二维码登录退出------------" + e_CommerceJsonBean);
        taskTba = tbClient.quit(e_CommerceJsonBean);

        log.info("-----------taskTba------------" + taskTba);
        return  taskTba;
    }
}
