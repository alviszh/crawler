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
@RequestMapping("/doc/bank")
public class BankDocController {

    public static final Logger log = LoggerFactory.getLogger(BankDocController.class);


    @RequestMapping("/getCredit")
    public String getCredit(Model model){
        model.addAttribute("module", CbConfModule.BANK.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", true);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/bank/getCredit";
    }
    //前置规则（贷乎）
    @RequestMapping("/precedingRuleDaihu")
    public String precedingRuleDaihu(Model model){
        model.addAttribute("module", CbConfModule.BANK.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/bank/precedingRuleDaihu";
    }

    //前置规则（借么）
    @RequestMapping("/precedingRuleJiemo")
    public String precedingRuleJiemo(Model model){
        model.addAttribute("module", CbConfModule.BANK.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/bank/precedingRuleJiemo";
    }

    //推送异步状态（借么）
    @RequestMapping("/asynStatusJiemo")
    public String AsynStatusJiemo(Model model){
        model.addAttribute("module", CbConfModule.BANK.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", true);
        model.addAttribute("isReportData", false);
        return "doc/bank/asynStatusJiemo";
    }

    /**
     * 获取报告数据-借么
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataJiemo")
    public String getReportDataJiemo(Model model){
        model.addAttribute("module", CbConfModule.BANK.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/bank/getReportDataJiemo";
    }
    /**
     * 获取报告数据-贷乎
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataDaihu")
    public String getReportDataDaihu(Model model){
        model.addAttribute("module", CbConfModule.BANK.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/bank/getReportDataDaihu";
    }
}
