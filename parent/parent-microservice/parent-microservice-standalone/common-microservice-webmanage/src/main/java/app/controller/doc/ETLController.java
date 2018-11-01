package app.controller.doc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.enums.CbConfModule;

/**
 * ETL接口页面说明
 * @author lvyuxin
 *
 */

@Controller
@RequestMapping("/doc/etl")
public class ETLController {
	
	public static final Logger log = LoggerFactory.getLogger(ETLController.class);
	
	/**
	 * 获取数据接口
	 * @param model
	 * @return
	 */
	
	@RequestMapping("/getdata")
	public String getAllData(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getAllData";
	}
	
	@RequestMapping("/getphonenum")
	public String getPhoneNum(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getPhoneNum";
	}
	
	@RequestMapping("/getinsurance")
	public String getInsurance(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getInsurance";
	}
	
	@RequestMapping("/gethousingfund")
	public String getHousingfund(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getHousingFund";
	}
	
	@RequestMapping("/getdebitcard")
	public String getDebitcard(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getDebitcard";
	}
	
	@RequestMapping("/getcreditcard")
	public String getCreditcard(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getCreditcard";
	}
	
	@RequestMapping("/gettaobao")
	public String getTaobao(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getTaobao";
	}
	
	@RequestMapping("/getsuning")
	public String getSuning(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getSuning";
	}
	
	@RequestMapping("/getjd")
	public String getJd(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getJd";
	}
	
	@RequestMapping("/getmobilereport")
	public String getMobileReport(Model model){
		model.addAttribute("module", CbConfModule.ETL.getCode());
		model.addAttribute("isApi", true);
		return "doc/etl/getMobileReport";
	}
}
