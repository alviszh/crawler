package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**
 * 运营商接口说明
 * Created by tz on 2018/8/4.
 */
@Controller
@RequestMapping("/doc/mobile")
public class MobileDocController {

    public static final Logger log = LoggerFactory.getLogger(MobileDocController.class);

    //前置规则（贷乎）
    @RequestMapping("/precedingRuleDaihu")
    public String precedingRuleDaihu(Model model){
        model.addAttribute("module", CbConfModule.MOBILE.getCode());
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/mobile/precedingRuleDaihu";
    }

    //前置规则（借么）
    @RequestMapping("/precedingRuleJiemo")
    public String precedingRuleJiemo(Model model){
        model.addAttribute("module", CbConfModule.MOBILE.getCode());
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/mobile/precedingRuleJiemo";
    }

    //推送异步状态（借么）
    @RequestMapping("/asynStatusJiemo")
    public String AsynStatusJiemo(Model model){
        model.addAttribute("module", CbConfModule.MOBILE.getCode());
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", true);
        model.addAttribute("isReportData", false);
        return "doc/mobile/asynStatusJiemo";
    }

    /**
     * 获取报告数据-借么
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataJiemo")
    public String getReportDataJiemo(Model model){
        model.addAttribute("module", CbConfModule.MOBILE.getCode());
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/mobile/getReportDataJiemo";
    }
    /**
     * 获取报告数据-贷乎
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataDaihu")
    public String getReportDataDaihu(Model model){
        model.addAttribute("module", CbConfModule.MOBILE.getCode());
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/mobile/getReportDataDaihu";
    }
    
    /**
     * 获取报告数据-研究院-薪动钱包
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataXindongqianbao")
    public String getReportDataYanjiuyuan(Model model){
        model.addAttribute("module", CbConfModule.MOBILE.getCode());
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/mobile/getReportDataXindongqianbao";
    }
}
