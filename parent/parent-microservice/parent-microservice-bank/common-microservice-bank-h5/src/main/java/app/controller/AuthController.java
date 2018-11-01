package app.controller;


import app.commontracerlog.TracerLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.crawler.bank.json.BankUserBean;
import com.crawler.bank.json.TaskBank;

import app.client.bank.BankTaskClient;



@Controller
@RequestMapping("/h5/bank")
public class AuthController {
    public static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private BankTaskClient bankTaskClient;
    @Autowired
    private TracerLog tracer;

    /**
     * 认证首页
     * @param model
     * @param themeColor
     * @param isTopHide
     * @return
     */
    @RequestMapping(value = {"","/auth"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String auth( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide
            , @RequestParam(name = "key", required = false) String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl, @RequestParam(name = "owner", required = false,defaultValue = "tianxi") String owner) {
        tracer.addTag("key", key);
        tracer.addTag("authController.auth","key=" + key + ",redirectUrl=" + redirectUrl + ",owner=" + owner);
        model.addAttribute("themeColor",themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        model.addAttribute("key",key);
        model.addAttribute("redirectUrl",redirectUrl);
        model.addAttribute("owner",owner);
        System.out.println("************* themeColor="+themeColor + ", isTopHide="+isTopHide+",topHide="+topHide);
        return "auth";
    }

    /**
     * 进入显示所有城市的页面
     * @param model
     * @param bankUserBean
     * @param themeColor
     * @param topHide
     * @return
     */
    @RequestMapping(value = "/citys",  method = {RequestMethod.GET, RequestMethod.POST})
    public String citys(Model model, BankUserBean bankUserBean,String themeColor, String topHide
     , @RequestParam(name = "key", required = false) String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl, @RequestParam(name = "owner", required = false,defaultValue = "tianxi") String owner) {
        tracer.addTag("key", key);
        tracer.addTag("authController.auth","key=" + key + ",redirectUrl=" + redirectUrl + ",owner=" + owner);
        System.out.println("******banks******* themeColor=" + themeColor + ", topHide=" + topHide + ",topHide=" + topHide);
        model.addAttribute("key",key);
        model.addAttribute("redirectUrl",redirectUrl);
        model.addAttribute("owner",owner);
        model.addAttribute("bankJsonBean", bankUserBean);
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("topHide", topHide);

        return "citys";
    }


    @RequestMapping(value = "/item",  method = {RequestMethod.GET, RequestMethod.POST})
    public String test(Model model, BankUserBean bankUserBean,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
                       @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                       @RequestParam(name = "city") String city,
                       @RequestParam(name = "idNum") String idNum,
                       @RequestParam(name = "username") String username,
                       @RequestParam(name = "cardType") String cardType ,
                       @RequestParam(name = "key", required = false) String key,
                       @RequestParam(name = "redirectUrl", required = false) String redirectUrl,
                       @RequestParam(name = "owner",required = false,defaultValue = "tianxi") String owner) {
        log.info("-----------银行登录页面------------");

        model.addAttribute("themeColor", themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }

        model.addAttribute("topHide", topHide);
        model.addAttribute("bankJsonBean", bankUserBean);
        model.addAttribute("key",key);
        model.addAttribute("redirectUrl",redirectUrl);
        model.addAttribute("owner",owner);
        model.addAttribute("city", city);
        model.addAttribute("idNum", idNum);
        model.addAttribute("username", username);
        model.addAttribute("cardType", cardType);
        System.out.println("******bank******* themeColor=" + themeColor + ", topHide=" + topHide + ",topHide=" + topHide);
        /*创建task*/
        TaskBank taskBank= bankTaskClient.createTask(bankUserBean);
        tracer.addTag("taskid", taskBank.getTaskid());
        /*TaskBank taskBank = new TaskBank();
        taskHousingfund.setTaskid("cbb472a8-4dbd-4552-9308-18233baa8857");*/
        model.addAttribute("taskBank", taskBank);
        model.addAttribute("taskId",taskBank.getTaskid());
        log.info("taskBank==================>" +taskBank);
        if (city.equals("中国银行")) {
            return "auth_BOC";
        } else if (city.equals("建设银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_CCB";
        } else if (city.equals("建设银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_CCB_C";
        } else if (city.equals("招商银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_CMB";
        } else if (city.equals("中信银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_CITIC";
        } else if (city.equals("光大银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_CEB";
        } else if (city.equals("农业银行")) {
            return "auth_ABC";
        } else if (city.equals("工商银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_ICBC";
        } else if (city.equals("浦发银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_SPDB";
        } else if (city.equals("浦发银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_SPDB_C";
        } else if (city.equals("兴业银行")) {
            return "auth_CIB";
        } else if (city.equals("民生银行")) {
            return "auth_CMBC";
        } else if (city.equals("华夏银行")) {
            return "auth_HXB";
        } else if (city.equals("交通银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_BOCOM";
        } else if (city.equals("交通银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_BOCOM_C";
        } else if (city.equals("恒丰银行")) {
            return "auth_HFB";
        } else if (city.equals("浙商银行")) {
            return "auth_CZB";
        } else if (city.equals("广发银行")) {
            return "auth_CGB";
        } else if (city.equals("渤海银行")) {
            return "auth_BOHC";
        } else if (city.equals("平安银行") && cardType.equals("DEBIT_CARD")) {
            return "auth_PAB";
        } else if (city.equals("平安银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_PAB_C";
        } else if (city.equals("建设银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_CCB_C";
        } else if (city.equals("招商银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_CMB_C";
        } else if (city.equals("中信银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_CITIC_C";
        } else if (city.equals("光大银行") && cardType.equals("CREDIT_CARD")) {
            return "auth_CEB_C";
        } else if (city.equals("工商银行")) {
            return "auth_ICBC";
        } else if (city.equals("邮储银行")) {
            return "auth_PSBC";
        } else if (city.equals("北京银行")) {
            return "auth_BOB";
        }
        else {
            return "default_login";
        }
    }

    @RequestMapping(value = {"/smscode"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String smscode( Model model,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                           @RequestParam(name = "taskid") String taskid,
                           @RequestParam(name = "bankType") String bankType,
                           @RequestParam(name = "loginName") String loginName,
                           @RequestParam(name = "cardType") String cardType,
                           @RequestParam(name = "loginType") String loginType) {
//        themeColor = "#" + themeColor;
        model.addAttribute("themeColor",themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        model.addAttribute("taskid",taskid);
        model.addAttribute("bankType", bankType);
        model.addAttribute("loginName", loginName);
        model.addAttribute("cardType", cardType);
        model.addAttribute("loginType", loginType);
        System.out.println("************* themeColor="+themeColor + ", isTopHide="+isTopHide+",topHide="+topHide);
        return "auth_sms";
    }

    @RequestMapping(value = {"/qacode"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String qacode( Model model,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                           @RequestParam(name = "taskid") String taskid,
                           @RequestParam(name = "bankType") String bankType,
                           @RequestParam(name = "loginName") String loginName,
                           @RequestParam(name = "cardType") String cardType,
                           @RequestParam(name = "loginType") String loginType,
                           @RequestParam(name = "question") String question) {

        model.addAttribute("themeColor",themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        model.addAttribute("taskid",taskid);
        model.addAttribute("bankType", bankType);
        model.addAttribute("loginName", loginName);
        model.addAttribute("cardType", cardType);
        model.addAttribute("loginType", loginType);
        model.addAttribute("question", question);
        System.out.println("************* question"+ question);
        return "auth_qasms";
    }

    /**
     * 跳转到采集成功页
     * @param model
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
    public String success( Model model,@RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor,
                           @RequestParam(name = "taskId") String taskId,
                           @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide,
                           @RequestParam(name = "city") String city) {
        log.info("-----------数据采集成功------------" + taskId);
//        themeColor = "#" + themeColor;
        model.addAttribute("taskId",taskId);
        model.addAttribute("themeColor", themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        model.addAttribute("city",city);
        return "success";
    }



    @RequestMapping("/test")
    public String test(Model model){
        return "test";
    }
}
