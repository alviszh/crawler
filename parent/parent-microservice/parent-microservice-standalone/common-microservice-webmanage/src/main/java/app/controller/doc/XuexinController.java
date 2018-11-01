package app.controller.doc;

import app.enums.CbConfModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 学信接口说明
 * @author tz
 *
 */
@Controller
@RequestMapping("/doc/xuexin")
public class XuexinController {

    public static final Logger log = LoggerFactory.getLogger(XuexinController.class);
    
    @RequestMapping("/check")
    public String check(Model model){
        model.addAttribute("module", CbConfModule.XUEXIN.getCode());
        model.addAttribute("isApi", true);
        return "doc/xuexin/check";
    }

    @RequestMapping("/login")
    public String login(Model model){
        model.addAttribute("module", CbConfModule.XUEXIN.getCode());
        model.addAttribute("isApi", true);
        return "doc/xuexin/login";
    }

    @RequestMapping("/getdata")
    public String getData(Model model){
        model.addAttribute("module", CbConfModule.XUEXIN.getCode());
        model.addAttribute("isApi", true);
        return "doc/xuexin/getdata";
    }
}
