package app.controller;

import app.client.carrier.*;
import app.commontracerlog.TracerLog;

import com.crawler.mobile.json.MessageLogin;
import com.crawler.mobile.json.ResultData;
import com.crawler.mobile.json.TaskMobile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping(value = {"/h5/carrier/telecom","/carrier/telecom"})
@RequestMapping(value = "/h5/carrier/telecom")
public class TelecomController {
    public static final Logger log = LoggerFactory.getLogger(TelecomController.class);  

    @Autowired
    private TracerLog tracer;
    @Autowired
    private TelecomHeilongjiangClient telecomHeilongjiangClient; 
    @Autowired
    private TelecomBeijingClient telecomBeijingClient;
    @Autowired
    private TelecomJilinClient telecomJilinClient;
    @Autowired
    private TelecomLiaoNingClient telecomLiaoNingClient;
    @Autowired
    private TelecomShanXi3Client telecomShanXi3Client;
    @Autowired
    private TelecomShanxiClient telecomShanxiClient;
    @Autowired
    private TelecomNingXiaClient telecomNingXiaClient;
    @Autowired
    private TelecomXinJiangClient telecomXinJiangClient;
    @Autowired
    private TelecomGanSuClient telecomGanSuClient;
    @Autowired
    private TelecomQingHaiClient telecomQingHaiClient;
    @Autowired
    private TelecomNeiMengGuClient telecomNeiMengGuClient;
    @Autowired
    private TelecomHebeiClient telecomHebeiClient;
    @Autowired
    private TelecomSiChuanClient telecomSiChuanClient;
    @Autowired
    private TelecomShanDongClient telecomShanDongClient;
    @Autowired
    private TelecomChongQingClient telecomChongQingClient;
    @Autowired
    private TelecomTianjinClient telecomTianjinClient;
    @Autowired
    private TelecomHunanClient telecomHunanClient;
    @Autowired
    private TelecomGuiZhouClient telecomGuiZhouClient;
    @Autowired
    private TelecomFujianClient telecomFujianClient;
    @Autowired
    private TelecomYunnanClient telecomYunnanClient;
    @Autowired
    private TelecomShanghaiClient telecomShanghaiClient;
    @Autowired
    private TelecomGuangxiClient telecomGuangxiClient;
    @Autowired
    private TelecomHenanClient telecomHenanClient;
    @Autowired
    private TelecomJiangxiClient telecomJiangxiClient;
    @Autowired
    private TelecomGuangDongClient telecomGuangDongClient;
    @Autowired
    private TelecomJiangSuClient telecomJiangSuClient;
    @Autowired
    private TelecomHubeiClient telecomHubeiClient;
    @Autowired
    private TelecomHainanClient telecomHainanClient;
    @Autowired
    private TelecomAnHuiClient telecomAnHuiClient;
    @Autowired
    private TelecomZhejiangClient telecomZhejiangClient;
    @Autowired
    private TelecomClient telecomClient;

    /**
     * 运营商认证（登录）
     * 手机运营商  中国电信
     * @param model
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    ResultData<TaskMobile> telecomLogin(Model model, MessageLogin messageLogin,String province) {
        tracer.addTag("TelecomController.telecomLogin","------ 中国电信 login------"+messageLogin.toString()+"====province="+ province);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info(messageLogin.getName() + "------ 中国电信 login------");
        log.info("===========province="+ province+ "，" +messageLogin.toString());
        ResultData<TaskMobile> resultData = null;
        log.info("-------messageLogin---------" + messageLogin);
        if (province.equals("广西")) {
            resultData = telecomGuangxiClient.login(messageLogin);
        } else if (province.equals("广东")) {
            resultData = telecomGuangDongClient.login(messageLogin);
        } else if (province.equals("安徽")) {
            resultData = telecomAnHuiClient.login(messageLogin);
        } else if (province.equals("江苏")) {
            resultData = telecomJiangSuClient.login(messageLogin);
        } else if (province.equals("宁夏") || province.equals("河北") || province.equals("湖北")
                || province.equals("黑龙江")) {
            resultData = telecomClient.login(messageLogin);//调用公共登陆接口
        } else {
            resultData = telecomBeijingClient.login(messageLogin);//调用北京的登陆接口
        }

        resultData.getData().setPhonenum(messageLogin.getName()); //保存手机号码

        log.info("----resultData=" + resultData);
        log.info("--------------" + resultData.getData());
        model.addAttribute("taskMobile", resultData.getData());
        return resultData;
    }

    /**
     * 验证手机验证码（电信）
     * @param model
     * @return
     */
    @RequestMapping(value = "/verifiCode", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile verifiCode(Model model, MessageLogin messageLogin,String province) {
        tracer.addTag("TelecomController.verifiCode","------ 短信验证 verifiCode------"+messageLogin.toString()+"====province="+ province);
        log.info("--verifiCode-----短信验证---------地区：" + province);
        log.info("messageLogin=" + messageLogin  );
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        ResultData<TaskMobile> resultData = null;
        if (province.equals("黑龙江")) {
            resultData = telecomHeilongjiangClient.setphonecode(messageLogin);
        } else if (province.equals("山西")) {
            resultData = telecomShanxiClient.setphonecode(messageLogin);
        } else if (province.equals("宁夏")) {
            resultData = telecomNingXiaClient.setphonecode(messageLogin);
        } else if (province.equals("内蒙古")) {
            resultData = telecomNeiMengGuClient.setphonecode(messageLogin);
        } else if (province.equals("四川")) {
            resultData = telecomSiChuanClient.setphonecode(messageLogin);
        } else if (province.equals("山东")) {
            resultData = telecomShanDongClient.setphonecode(messageLogin);
        } else if (province.equals("重庆")) {
            resultData = telecomChongQingClient.setphonecode(messageLogin);
        } else if (province.equals("湖南")) {
            resultData = telecomHunanClient.setphonecode(messageLogin);
        } else if (province.equals("贵州")) {
            resultData = telecomGuiZhouClient.telecomsetcode(messageLogin);
        } else if (province.equals("福建")) {
            resultData = telecomFujianClient.setphonecode(messageLogin);
        } else if (province.equals("云南")) {
            resultData = telecomYunnanClient.setphonecode(messageLogin);
        } else if (province.equals("上海")) {
            resultData = telecomShanghaiClient.validate(messageLogin);
        } else if (province.equals("广西")) {
            resultData = telecomGuangxiClient.setphonecodeFirst(messageLogin);
        } else if (province.equals("河南")) {
            resultData = telecomHenanClient.telecomsetcode(messageLogin);
        } else if (province.equals("江西")) {
            resultData = telecomJiangxiClient.setphonecode(messageLogin);
        } else if (province.equals("广东")) {
            resultData = telecomGuangDongClient.setphonecode(messageLogin);
        } else if (province.equals("湖北")) {
            resultData = telecomHubeiClient.telecomsetcode(messageLogin);
        } else if (province.equals("海南")) {
            resultData = telecomHainanClient.setphonecode(messageLogin);
        } else if (province.equals("安徽")) {
            resultData = telecomAnHuiClient.setphonecode(messageLogin);
        } else if (province.equals("浙江")) {
            resultData = telecomZhejiangClient.validate(messageLogin);
        }
        TaskMobile taskMobile = resultData.getData();
		model.addAttribute("tasokMbile", taskMobile);
        return taskMobile;
    }

    /**
     * 发送手机验证码（电信）
     * @param messageLogin
     * @return
     */
    @PostMapping(path = "/sendCode")
    public @ResponseBody
    ResultData<TaskMobile> sendCode(Model model, MessageLogin messageLogin,String province) {
        tracer.addTag("TelecomController.sendCode","------ 获取电信短信验证码 sendCode------"+messageLogin.toString()+"====地区="+ province);
        log.info("-----------获取短信验证码------------地区：" + province+"--------" + messageLogin);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        ResultData<TaskMobile> resultData = null;
        if (province.equals("黑龙江")) {
            resultData = telecomHeilongjiangClient.getphonecode(messageLogin);
        } else if (province.equals("山西")) {
            resultData = telecomShanxiClient.getphonecode(messageLogin);
        } else if (province.equals("宁夏")) {
            resultData = telecomNingXiaClient.getphonecode(messageLogin);
        } else if (province.equals("内蒙古")) {
            resultData = telecomNeiMengGuClient.getphonecode(messageLogin);
        } else if (province.equals("四川")) {
            resultData = telecomSiChuanClient.getphonecode(messageLogin);
        } else if (province.equals("山东")) {
            resultData = telecomShanDongClient.getphonecode(messageLogin);
        } else if (province.equals("重庆")) {
            resultData = telecomChongQingClient.getphonecode(messageLogin);
        } else if (province.equals("湖南")) {
            resultData = telecomHunanClient.getphonecode(messageLogin);
        } else if (province.equals("贵州")) {
            resultData = telecomGuiZhouClient.telecomgetcode(messageLogin);
        } else if (province.equals("福建")) {
            resultData = telecomFujianClient.getphonecode(messageLogin);
        } else if (province.equals("云南")) {
            resultData = telecomYunnanClient.getphonecode(messageLogin);
        } else if (province.equals("上海")) {
            resultData = telecomShanghaiClient.sendSms(messageLogin);
        } else if (province.equals("广西")) {
            resultData = telecomGuangxiClient.getphonecodeFirst(messageLogin);
        } else if (province.equals("河南")) {
            resultData = telecomHenanClient.telecomgetcode(messageLogin);
        } else if (province.equals("江西")) {
            resultData = telecomJiangxiClient.getphonecode(messageLogin);
        } else if (province.equals("广东")) {
            resultData = telecomGuangDongClient.getphonecode(messageLogin);
        } else if (province.equals("湖北")) {
            resultData = telecomHubeiClient.telecomgetcode(messageLogin);
        } else if (province.equals("海南")) {
            resultData = telecomHainanClient.getphonecode(messageLogin);
        } else if (province.equals("安徽")) {
            resultData = telecomAnHuiClient.getphonecode(messageLogin);
        } else if (province.equals("浙江")) {
            resultData = telecomZhejiangClient.sendSms(messageLogin);
        } else if (province.equals("河北")) {
            resultData = telecomHebeiClient.sendSMS(messageLogin);
        }

        log.info("resultData="+resultData);
        return resultData;
    }

    /**
     * 数据采集
     * @param model
     * @param messageLogin
     * @return
     */
    @RequestMapping(value = "/getAllData", method = RequestMethod.POST)
    public @ResponseBody
    ResultData<TaskMobile> getAllData(Model model, MessageLogin messageLogin,String province) {
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        tracer.addTag("TelecomController.getAllData","------ 中国电信 数据采集------"+messageLogin.toString()+"====地区="+ province);
        log.info(messageLogin.getName() + "------中国电信 数据采集------地区：" + province);
        log.info(messageLogin.toString());
        log.info("-------messageLogin---------" + messageLogin);
        ResultData<TaskMobile> resultData = null;
        /*if (province.equals("黑龙江")) {  //黑龙江的电信爬取在验证短信接口里
            resultData = telecomHeilongjiangClient.telecom(messageLogin);
        } else */if (province.equals("北京")) {
            resultData = telecomBeijingClient.telecom(messageLogin);
        } else if (province.equals("吉林")) {
            resultData = telecomJilinClient.telecom(messageLogin);
        } else if (province.equals("辽宁")) {
            resultData = telecomLiaoNingClient.telecom(messageLogin);
        } else if (province.equals("陕西")) {
            resultData = telecomShanXi3Client.telecom(messageLogin);
        } else if (province.equals("山西")) {
            resultData = telecomShanxiClient.telecom(messageLogin);
        } else if (province.equals("宁夏")) {
            resultData = telecomNingXiaClient.telecom(messageLogin);
        } else if (province.equals("新疆")) {
            resultData = telecomXinJiangClient.telecom(messageLogin);
        } else if (province.equals("甘肃")) {
            resultData = telecomGanSuClient.telecom(messageLogin);
        } else if (province.equals("青海")) {
            resultData = telecomQingHaiClient.telecom(messageLogin);
        } else if (province.equals("内蒙古")) {
            resultData = telecomNeiMengGuClient.telecom(messageLogin);
        } else if (province.equals("河北")) {
            resultData = telecomHebeiClient.telecom(messageLogin);
        } else if (province.equals("四川")) {
            resultData = telecomSiChuanClient.telecom(messageLogin);
        } else if (province.equals("山东")) {
            resultData = telecomShanDongClient.telecom(messageLogin);
        } else if (province.equals("重庆")) {
            resultData = telecomChongQingClient.telecom(messageLogin);
        } else if (province.equals("天津")) {
            resultData = telecomTianjinClient.telecom(messageLogin);
        } else if (province.equals("湖南")) {
            resultData = telecomHunanClient.telecom(messageLogin);
        } else if (province.equals("贵州")) {
            resultData = telecomGuiZhouClient.telecom(messageLogin);
        } else if (province.equals("福建")) {
            resultData = telecomFujianClient.telecom(messageLogin);
        } else if (province.equals("云南")) {
            resultData = telecomYunnanClient.telecom(messageLogin);
        } else if (province.equals("上海")) {
            resultData = telecomShanghaiClient.crawler(messageLogin);
        } else if (province.equals("广西")) {
            resultData = telecomGuangxiClient.crawler(messageLogin);
        } else if (province.equals("河南")) {
            resultData = telecomHenanClient.crawler(messageLogin);
        } else if (province.equals("江西")) {
            resultData = telecomJiangxiClient.crawler(messageLogin);
        } else if (province.equals("广东")) {
            resultData = telecomGuangDongClient.crawler(messageLogin);
        } else if (province.equals("江苏")) {
            resultData = telecomJiangSuClient.crawler(messageLogin);
        } else if (province.equals("湖北")) {
            resultData = telecomHubeiClient.crawler(messageLogin);
        } else if (province.equals("海南")) {
            resultData = telecomHainanClient.crawler(messageLogin);
        } else if (province.equals("安徽")) {
            resultData = telecomAnHuiClient.crawlerTwo(messageLogin);
        } else if (province.equals("浙江")) {
            resultData = telecomZhejiangClient.crawler(messageLogin);
        }
        log.info("----resultData=" + resultData);
        return resultData;
    }

    /**
     * 第二次登录{江西}
     * 第二次发送短信验证码(广西)
     * @param model
     * @param messageLogin
     * @param province
     * @return
     */
    @PostMapping(path = "/sendCodeTwo")
    public @ResponseBody
    ResultData<TaskMobile> sendCodeTwo(Model model, MessageLogin messageLogin,String province) {
        tracer.addTag("TelecomController.sendCodeTwo","------ 中国电信 获取短信验证码------"+messageLogin.toString()+"====地区="+ province);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info("-----------获取短信验证码------------地区：" + province + "--------" + messageLogin);
        ResultData<TaskMobile> resultData = null;
        if (province.equals("广西")) {
            resultData = telecomGuangxiClient.getphonecodeTwo(messageLogin);
        } else if (province.equals("江西")) {
            resultData = telecomJiangxiClient.getphonecodelogin(messageLogin);
        }
        return resultData;
    }

    /**
     * 第一次验证短信验证码（江西）
     * 第二次发送短信验证码(广西)
     * @param model
     * @param messageLogin
     * @param province
     * @return
     */
    @RequestMapping(value = "/verifiCodeTwo", method = RequestMethod.POST)
    public @ResponseBody
    TaskMobile verifiCodeFirst(Model model, MessageLogin messageLogin,String province) {
        tracer.addTag("TelecomController.verifiCodeTwo","------ 中国电信 短信验证------"+messageLogin.toString()+"====地区="+ province);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info("--verifiCodeTwo-----短信验证---------地区：" + province);
        log.info("messageLogin=" + messageLogin);
        ResultData<TaskMobile> resultData = null;
        if (province.equals("广西")) {
            resultData = telecomGuangxiClient.setphonecodeTwo(messageLogin);
        } else if (province.equals("江西")) {
            resultData = telecomJiangxiClient.setphonecodelogin(messageLogin); //验证登录接口的验证码
        }
        tracer.addTag("resultData", resultData.toString());
        TaskMobile taskMobile = resultData.getData();
        model.addAttribute("tasokMbile", taskMobile);
        return taskMobile;
    }

    /**
     * 第二次登录{海南}
     *
     * @param model
     * @param messageLogin
     * @param province
     * @return
     */
    /*@RequestMapping(value = "/loginTwo", method = RequestMethod.POST)
    public @ResponseBody ResultData<TaskMobile> loginTwo(Model model, MessageLogin messageLogin,String province) {
        tracer.addTag("TelecomController.loginTwo","------ 中国电信 第二次登录------"+messageLogin.toString()+"====地区="+ province);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info(messageLogin.getName() + "------ 中国电信 loginTwo------");
        log.info(messageLogin.toString()+"===========province="+ province);
        ResultData<TaskMobile> resultData = null;
        log.info("-------messageLogin---------" + messageLogin);
        if (province.equals("海南")) {
            resultData = telecomHainanClient.loginTwo(messageLogin);
        }
        resultData.getData().setPhonenum(messageLogin.getName()); //保存手机号码

        log.info("----resultData=" + resultData);
        if (resultData != null) {
            log.info("--------------" + resultData.getData());
            model.addAttribute("taskMobile", resultData.getData());
        }
        return resultData;
    }*/

    /**
     * 浙江登录成功后需要处理的请求 (浙江)
     * @param model
     * @param messageLogin
     * @param province
     * @return
     */
    @RequestMapping(value = "/intermediate", method = RequestMethod.POST)
    public @ResponseBody TaskMobile intermediate(Model model, MessageLogin messageLogin,String province) {
        tracer.addTag("TelecomController.intermediate","------ 中国电信 浙江登录成功后需要处理的请求------"+messageLogin.toString()+"====地区="+ province);
        tracer.qryKeyValue("taskid", messageLogin.getTask_id());
        log.info("--intermediate-----浙江登录成功后需要处理的请求---------地区：" + province);
        log.info("messageLogin=" + messageLogin);
        ResultData<TaskMobile> resultData = null;
        resultData = telecomZhejiangClient.intermediate(messageLogin);
        TaskMobile taskMobile = resultData.getData();
        model.addAttribute("tasokMbile", taskMobile);
        return taskMobile;
    }
}
