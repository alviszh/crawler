package app.controller.doc;

import app.enums.CbConfModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 苏宁接口说明
 * Created by wpy on 2018/2/27.
 */
@Controller
@RequestMapping("/doc/commerce")
public class EcommerceController {

    public static final Logger log = LoggerFactory.getLogger(ETLController.class);

    @RequestMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("module", CbConfModule.SUNING.getCode());
        model.addAttribute("isApi", true);
        return "doc/ecommerce/suning/login";
    }

    @RequestMapping("/sendSMS")
    public String getCredit(Model model){
        model.addAttribute("module", CbConfModule.SUNING.getCode());
        model.addAttribute("isApi", true);
        return "doc/ecommerce/suning/sendSMS";
    }
}
