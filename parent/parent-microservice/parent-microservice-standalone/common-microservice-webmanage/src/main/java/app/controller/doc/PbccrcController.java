package app.controller.doc;

import app.enums.CbConfModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 人行征信接口说明
 * Created by zmy on 2018/1/8.
 */
@Controller
@RequestMapping("/doc/pbccrc")
public class PbccrcController {

    public static final Logger log = LoggerFactory.getLogger(ETLController.class);

    /**
     * 获取登录页面
     * @param model
     * @return
     */

    @RequestMapping("/getLoginPage")
    public String getLoginPage(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApi", true);
        return "doc/pbccrc/getLoginPage";
    }

    @RequestMapping("/getCreditV1")
    public String getCreditV1(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApiV1", true);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/pbccrc/getCreditV1";
    }

    @RequestMapping("/getCredit")
    public String getCredit(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", true);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/pbccrc/getCredit";
    }
    //前置规则（贷乎）
    @RequestMapping("/precedingRuleDaihu")
    public String precedingRuleDaihu(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/pbccrc/precedingRuleDaihu";
    }

    //前置规则（借么）
    @RequestMapping("/precedingRuleJiemo")
    public String precedingRuleJiemo(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/pbccrc/precedingRuleJiemo";
    }

    //推送异步状态（借么）
    @RequestMapping("/asynStatusJiemo")
    public String AsynStatusJiemo(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", true);
        model.addAttribute("isReportData", false);
        return "doc/pbccrc/asynStatusJiemo";
    }

    /**
     * 获取报告数据-借么
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataJiemo")
    public String getReportDataJiemo(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/pbccrc/getReportDataJiemo";
    }
    /**
     * 获取报告数据-贷乎
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataDaihu")
    public String getReportDataDaihu(Model model){
        model.addAttribute("module", CbConfModule.PBCCRC.getCode());
        model.addAttribute("isApiV1", false);
        model.addAttribute("isApiV2", false);
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/pbccrc/getReportDataDaihu";
    }
}
