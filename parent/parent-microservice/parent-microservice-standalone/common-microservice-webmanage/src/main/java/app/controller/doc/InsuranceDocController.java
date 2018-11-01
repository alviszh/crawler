package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**
 * 社保接口说明
 * Created by yl on 2018/8/24
 */

@Controller
@RequestMapping("/doc/insurance")
public class InsuranceDocController {

	public static final Logger log = LoggerFactory.getLogger(ETLController.class);
	
	 //前置规则（贷乎）
    @RequestMapping("/precedingRuleDaihu")
    public String precedingRuleDaihu(Model model){
        model.addAttribute("module", CbConfModule.INSURANCE.getCode());
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/insurance/precedingRuleDaihu";
    }

    //前置规则（借么）
    @RequestMapping("/precedingRuleJiemo")
    public String precedingRuleJiemo(Model model){
        model.addAttribute("module", CbConfModule.INSURANCE.getCode());
        model.addAttribute("isPrecedingRule", true);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", false);
        return "doc/insurance/precedingRuleJiemo";
    }
    
    //推送异步状态（贷乎）
    @RequestMapping("/asynStatusDaihu")
    public String AsynStatusDaihu(Model model){
        model.addAttribute("module", CbConfModule.INSURANCE.getCode());
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", true);
        model.addAttribute("isReportData", false);
        return "doc/insurance/asynStatusJiemo";
    }
    
    
    /**
     * 获取报告数据-借么
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataJiemo")
    public String getReportDataJiemo(Model model){
        model.addAttribute("module", CbConfModule.INSURANCE.getCode());
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/insurance/getReportDataJiemo";
    }
    /**
     * 获取报告数据-贷乎
     * @param model
     * @return
     */
    @RequestMapping("/getReportDataDaihu")
    public String getReportDataDaihu(Model model){
        model.addAttribute("module", CbConfModule.INSURANCE.getCode());
        model.addAttribute("isPrecedingRule", false);
        model.addAttribute("isAsynStatus", false);
        model.addAttribute("isReportData", true);
        return "doc/insurance/getReportDataDaihu";
    }
}
