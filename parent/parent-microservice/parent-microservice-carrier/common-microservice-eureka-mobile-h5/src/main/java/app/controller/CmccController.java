package app.controller;

import app.client.carrier.CmccClient;
import app.commontracerlog.TracerLog;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.TaskMobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/h5/carrier/cmcc")
public class CmccController {

    public static final Logger log = LoggerFactory.getLogger(CmccController.class);  

    @Autowired
    private CmccClient cmccClient;
    @Autowired
    private TracerLog tracer;

    /**
     * 发送登录短信随机码
     * @param model
     * @param messageLogin
     * (mobileNum,taskid)
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile sendCode(Model model, MessageLogin messageLogin) {
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        tracer.addTag("CmccController.sendCode", "-----发送短信验证----messageLogin=" + messageLogin);
        log.info("--sendCode-----短信验证---------");
        log.info("messageLogin=" + messageLogin );
        TaskMobile taskMobile = cmccClient.sendSMS(messageLogin);
		/*TaskMobile taskMobile = new TaskMobile();
        taskMobile.setError_code(1010);
        taskMobile.setError_message("尊敬的用户，单位时间内下发短信次数过多，请稍后再使用！");*/

        model.addAttribute("tasokMbile", taskMobile);
        tracer.addTag("tasokMbile=",taskMobile.toString());
        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 登录中国移动
     * @param model
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody TaskMobile cmccLogin(Model model, MessageLogin messageLogin) {
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        tracer.addTag("CmccController.cmccLogin","------中国移动第一次认证----messageLogin--" + messageLogin);
        log.info(messageLogin.getName() + "------中国移动第一次认证------");
        log.info(messageLogin.toString());
        log.info("-------messageLogin---------" + messageLogin);
        TaskMobile taskMobile = cmccClient.login(messageLogin);
        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 发送第二次认证短信随机码
     * @param model
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/sendCodeTwo", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile sendCodeTwo(Model model, MessageLogin messageLogin) {
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        tracer.addTag("CmccController.sendCodeTwo","---发送第二次认证短信随机码，messageLogin=" + messageLogin );
        log.info("--sendCodeTwo-----短信验证---------");
        log.info("messageLogin=" + messageLogin );
        TaskMobile taskMobile = cmccClient.sendVerifySMS(messageLogin);
        /*TaskMobile taskMobile = new TaskMobile();
        taskMobile.setError_code(1011);
        taskMobile.setDescription("尊敬的用户，今天下发短信次数过多，请明天再使用！");
        taskMobile.setPhase("LOGIN");
        taskMobile.setPhase_status("NEED");*/
        model.addAttribute("tasokMbile", taskMobile);
        return taskMobile;
    }

    /**
     * 第二次认证
     * @param model
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/loginTwo", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile loginTwo(Model model, MessageLogin messageLogin) {
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        tracer.addTag("CmccController.loginTwo","-----中国移动第二次认证---");
        log.info(messageLogin.getName() + "------中国移动第二次认证，messageLogin=" + messageLogin);
        log.info(messageLogin.toString());
        log.info("-------messageLogin---------" + messageLogin);
        TaskMobile taskMobile = cmccClient.secondAttestation(messageLogin);
        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 数据爬取移动（总接口）
     * @param model
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/getAllData", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile getAllData(Model model, MessageLogin messageLogin) {
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        tracer.addTag("CmccController.getAllData","------数据爬取移动，messageLogin---------" + messageLogin);
        log.info(messageLogin.getName() + "------数据爬取移动------");
        log.info(messageLogin.toString());
        log.info("-------messageLogin---------" + messageLogin);
        String result = cmccClient.getAllData(messageLogin); //
        log.info("----result=" + result);
        TaskMobile taskMobile = new TaskMobile();
        return taskMobile;
    }

}
