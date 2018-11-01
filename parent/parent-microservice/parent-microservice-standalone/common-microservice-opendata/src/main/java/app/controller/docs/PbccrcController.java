package app.controller.docs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 人行征信报告
 */
@Controller
@RequestMapping(value="/docs/pbccrc")
public class PbccrcController {

    @Value("${pbccrc.prod.url.domain}")
    String prodDomain; //url地址域名

    /**
     * 人行征信 - 报告说明
     * @return
     */
    @RequestMapping("/reportExplain")
    public String reportExplain(Model model) {
        System.out.println("getCarrierReport");
        model.addAttribute("prodDomain", prodDomain);
        return "docs/pbccrc/reportExplain";
    }
}
