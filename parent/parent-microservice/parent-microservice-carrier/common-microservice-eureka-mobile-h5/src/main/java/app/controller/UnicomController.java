package app.controller;

import app.client.carrier.UnicomLoginClient;
import app.commontracerlog.TracerLog;

import com.crawler.mobile.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//@RequestMapping(value = {"/h5/carrier/unicom","/carrier/unicom"})
@RequestMapping(value = "/h5/carrier/unicom")
public class UnicomController {

    public static final Logger log = LoggerFactory.getLogger(UnicomController.class);  

    @Autowired
    private UnicomLoginClient unicomLoginClient;
    @Autowired
    private TracerLog tracer;

    /**
     * 运营商认证（登录）
     * 手机运营商UNICOM  中国联通
     * @param model
     * @param mobileJsonBean
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile unicomLogin(Model model, MobileJsonBean mobileJsonBean) {
        tracer.qryKeyValue("taskid", mobileJsonBean.getTask_id());
        tracer.addTag("UnicomController.unicomLogin","------中国联通 login----mobileJsonBean=" + mobileJsonBean);
        log.info(mobileJsonBean.getMobileNum() + "------中国联通 login------");
        log.info(mobileJsonBean.toString());
        TaskMobile taskMobile = null;
        MessageLogin messageLogin = new MessageLogin();
        messageLogin.setName(mobileJsonBean.getMobileNum());
        messageLogin.setPassword(mobileJsonBean.getPassword());
        messageLogin.setTask_id(mobileJsonBean.getTask_id());
        if (mobileJsonBean.getId() != null && !"".equals(mobileJsonBean.getId())) {
            messageLogin.setUser_id(Integer.parseInt(mobileJsonBean.getId().toString()));
        }
        log.info("-------messageLogin---------" + messageLogin);
        taskMobile = unicomLoginClient.unicomlong(messageLogin); //登录

        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 爬取总接口
     * 手机运营商UNICOM  中国联通
     * @param model
     * @param mobileJsonBean
     * @return
     */
    @RequestMapping(value = "/getAllData", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile unicom(Model model, MobileJsonBean mobileJsonBean) {
        tracer.qryKeyValue("taskid", mobileJsonBean.getTask_id());
        tracer.addTag("UnicomController.getAllData","------中国联通 crawler----mobileJsonBean=" + mobileJsonBean);
        log.info(mobileJsonBean.getMobileNum() + "------中国联通 login------");
        log.info(mobileJsonBean.toString());
        TaskMobile taskMobile = null;
        MessageLogin messageLogin = new MessageLogin();
        messageLogin.setName(mobileJsonBean.getMobileNum());
        messageLogin.setPassword(mobileJsonBean.getPassword());
        messageLogin.setTask_id(mobileJsonBean.getTask_id());
        if (mobileJsonBean.getId() != null && !"".equals(mobileJsonBean.getId())) {
            messageLogin.setUser_id(Integer.parseInt(mobileJsonBean.getId().toString()));
        }
        log.info("-------messageLogin---------" + messageLogin);
        taskMobile = unicomLoginClient.unicom(messageLogin); //获取详单信息

        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 图片验证码登录
     * @param model
     * @param changePassword
     * @return
     */
    @RequestMapping(value = "/password/imageCode", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile imageCode(Model model, UnicomChangePasswordResult changePassword) {
        tracer.addTag("UnicomController.password.imageCode","------中国联通 图片验证码登录----changePassword=" + changePassword);
        tracer.qryKeyValue("taskid", changePassword.getTask_id());
        log.info("--imageCode-----图片验证码登录---------");
        log.info("changePassword=" + changePassword );
        TaskMobile taskMobile = unicomLoginClient.unicompasswordlogin(changePassword);
//        TaskMobile taskMobile = new TaskMobile();

        model.addAttribute("tasokMbile", taskMobile);
        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 发送短信随机码（忘记密码）
     * @param model
     * @param changePassword
     * @return
     */
    @RequestMapping(value = "/password/sendCode", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile sendCode(Model model, UnicomChangePasswordResult changePassword) {
        tracer.addTag("UnicomController.sendCode","------中国联通 发送短信随机码----changePassword=" + changePassword);
        tracer.qryKeyValue("taskid", changePassword.getTask_id());
        log.info("--sendCode-----发送短信随机码---------");
        log.info("changePassword=" + changePassword );
        TaskMobile taskMobile = unicomLoginClient.unicompasswordgetCode(changePassword);
//		TaskMobile taskMobile = new TaskMobile();

        model.addAttribute("tasokMbile", taskMobile);
        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 验证短信随机码（忘记密码）
     * @param model
     * @param changePassword
     * @return
     */
    @RequestMapping(value = "/password/verifiCode", method = RequestMethod.POST)
     public @ResponseBody
     TaskMobile setCode(Model model, UnicomChangePasswordResult changePassword) {
        tracer.addTag("UnicomController.setCode","------中国联通 验证短信随机码----changePassword=" + changePassword);
        tracer.qryKeyValue("taskid", changePassword.getTask_id());
        log.info("--setCode-----验证短信随机码---------");
        log.info("changePassword=" + changePassword );
        TaskMobile taskMobile = unicomLoginClient.unicompasswordsetCode(changePassword);
//        TaskMobile taskMobile = new TaskMobile();

        model.addAttribute("tasokMbile", taskMobile);
        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 密码变更（忘记密码）
     * @param model
     * @param changePassword
     * @return
     */
    @RequestMapping(value = "/password/changePassword", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile changePassword(Model model, UnicomChangePasswordResult changePassword) {
        tracer.addTag("UnicomController.changePassword","------中国联通 密码变更----changePassword=" + changePassword);
        tracer.qryKeyValue("taskid", changePassword.getTask_id());
        log.info("--changePassword-----密码变更---------");
        log.info("changePassword=" + changePassword );
        TaskMobile taskMobile = unicomLoginClient.unicompasswordchange(changePassword);
//        TaskMobile taskMobile = new TaskMobile();

        model.addAttribute("tasokMbile", taskMobile);
        log.info("----taskMobile=" + taskMobile);
        return taskMobile;
    }

    /**
     * 发送短信验证码
     * @param model
     * @param messageLogin
     * @return
     */
    @PostMapping(path = "/sendCode")
    public @ResponseBody
    ResultData<TaskMobile> sendCode(Model model, MessageLogin messageLogin) {
        log.info("-----------获取短信验证码------------");
        tracer.addTag("UnicomController.sendCode","------中国联通认证 获取短信验证码----messageLogin=" + messageLogin);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        ResultData<TaskMobile> resultData = unicomLoginClient.getphonecode(messageLogin);
        return resultData;
    }

    /**
     * 验证短信验证码
     * @param model
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/verifiCode", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile verifiCode(Model model, MessageLogin messageLogin) {
        tracer.addTag("UnicomController.verifiCode","------中国联通认证 短信验证----messageLogin=" + messageLogin);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info("--verifiCode-----短信验证---------");
        log.info("messageLogin=" + messageLogin);
        TaskMobile taskMobile = unicomLoginClient.setphonecode(messageLogin);
        tracer.addTag("verifiCode.taskMobile", taskMobile.toString());
        model.addAttribute("tasokMbile", taskMobile);
        return taskMobile;
    }

    //发送第二次短信验证码
    @RequestMapping(value = "/sendCodeTwo", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile sendCodeTwo(Model model, MessageLogin messageLogin) {
        tracer.addTag("UnicomController.sendCodeTwo", "------中国联通  发送第二次短信验证码----messageLogin=" + messageLogin);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info("--sendCodeTwo-----发送第二次短信验证码---------");
        log.info("messageLogin=" + messageLogin);
        ResultData<TaskMobile> resultData = unicomLoginClient.sendSmsTwice(messageLogin);
        TaskMobile taskMobile = resultData.getData();
        model.addAttribute("tasokMbile", taskMobile);
        return taskMobile;
    }

    //验证第二次短信验证码
    @RequestMapping(value = "/verifiCodeTwo", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile verifiCodeTwo(Model model, MessageLogin messageLogin) {
        tracer.addTag("UnicomController.verifiCodeTwo", "------中国联通  验证第二次短信验证码----messageLogin=" + messageLogin);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info("--verifiCodeTwo-----短信第二次验证---------");
        log.info("messageLogin=" + messageLogin);
        ResultData<TaskMobile> resultData = unicomLoginClient.verifySmsTwice(messageLogin);
        TaskMobile taskMobile = resultData.getData();
        model.addAttribute("tasokMbile", taskMobile);
        return taskMobile;
    }
}
