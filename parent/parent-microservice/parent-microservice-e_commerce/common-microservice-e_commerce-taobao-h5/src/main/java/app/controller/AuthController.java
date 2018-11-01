package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.crawler.e_commerce.json.E_CommerceJsonBean;
import com.crawler.e_commerce.json.E_CommerceTask;

import app.client.ecom.EcomTaskClient;
import app.commontracerlog.TracerLog;


@Controller
@RequestMapping("/h5")
public class AuthController {
    @Value("${spring.profiles.active}")
    String active;

    @Autowired
    private TracerLog tracer;

    public static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private EcomTaskClient tbTaskClient;

    /**
     * 认证首页
     * @param model
     * @param themeColor
     * @param isTopHide
     * @return
     */
    @RequestMapping(value = {"/tb","/auth"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String auth( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide
            , @RequestParam(name = "key", required = false) String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl, @RequestParam(name = "owner", required = false,defaultValue = "tianxi") String owner) {
//        themeColor = "#" + themeColor;
        model.addAttribute("themeColor",themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        System.out.println("************* themeColor="+themeColor + ", isTopHide="+isTopHide+",topHide="+topHide);
        return "auth";
    }

    /**
     * 京东登录页面
     * @param model
     * @param e_commerceJsonBean
     * @param themeColor
     * @param isTopHide
     * @return
     */
    @RequestMapping(value = "/tb/item", method = {RequestMethod.GET, RequestMethod.POST})
    public String item(Model model,E_CommerceJsonBean e_commerceJsonBean
            , @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide
            , @RequestParam(name = "key", required = false) String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl, @RequestParam(name = "owner", required = false,defaultValue = "tianxi") String owner) {
        System.out.println("******banks******* themeColor=" + themeColor + ", topHide=" + isTopHide + ",topHide=" + isTopHide);
        model.addAttribute("e_commerceJsonBean", e_commerceJsonBean);
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("isTopHide", isTopHide);
        model.addAttribute("appActive",active);
        /*创建task*/
        E_CommerceTask e_CommerceTask= tbTaskClient.createTask(e_commerceJsonBean);
        tracer.addTag("taskid", e_CommerceTask.getTaskid());

        model.addAttribute("e_CommerceTask", e_CommerceTask);
        log.info("e_CommerceTask=================>" + e_CommerceTask);
        return "tb_login";
    }

    @RequestMapping(value = {"/alipay"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String auth_alipay( Model model, @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide
            , @RequestParam(name = "key", required = false) String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl, @RequestParam(name = "owner", required = false,defaultValue = "tianxi") String owner) {
//        themeColor = "#" + themeColor;
        model.addAttribute("themeColor",themeColor);
        String topHide = "block";
        if (isTopHide) {
            topHide = "none";
        }
        model.addAttribute("topHide",topHide);
        System.out.println("************* themeColor="+themeColor + ", isTopHide="+isTopHide+",topHide="+topHide);
        return "auth_alipay";
    }


    @RequestMapping(value = "/alipay/item", method = {RequestMethod.GET, RequestMethod.POST})
    public String itemA(Model model,E_CommerceJsonBean e_commerceJsonBean
            , @RequestParam(name="themeColor",required = false,defaultValue = "5bc0de") String themeColor
            , @RequestParam(name = "isTopHide", required = false,defaultValue = "false") boolean isTopHide
            , @RequestParam(name = "key", required = false) String key, @RequestParam(name = "redirectUrl", required = false) String redirectUrl, @RequestParam(name = "owner", required = false,defaultValue = "tianxi") String owner) {
        System.out.println("******banks******* themeColor=" + themeColor + ", topHide=" + isTopHide + ",topHide=" + isTopHide);
        model.addAttribute("e_commerceJsonBean", e_commerceJsonBean);
        model.addAttribute("themeColor", themeColor);
        model.addAttribute("isTopHide", isTopHide);
        model.addAttribute("appActive",active);
        /*创建task*/
        E_CommerceTask e_CommerceTask= tbTaskClient.createTask(e_commerceJsonBean);
        tracer.addTag("taskid", e_CommerceTask.getTaskid());

        model.addAttribute("e_CommerceTask", e_CommerceTask);
        log.info("e_CommerceTask=================>" + e_CommerceTask);
        return "alipay_login";
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
