package app.controller.docs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import app.bean.WebDataReportV2;
import app.bean.WebRawDataReportV2;
import app.client.CarrierETLClient;

/**
 * 文档中心
 * @param <WebDataReportV2>
 * @return
 */
@Controller
@RequestMapping(value="/docs/carrier/v2")
public class CarrierController{
	
	
	@Autowired
	private CarrierETLClient carrierETLClient;

	/**
	 * 运营商 -运营商报告
	 */
	@RequestMapping(value = "/reportdata")
	public String report(Model model, @ModelAttribute(value = "taskid") String taskid) {
		System.out.println("report");
		WebDataReportV2  datas= carrierETLClient.getDataReport(taskid);
		model.addAttribute("taskid",taskid);
	    model.addAttribute("datas", datas);		
		return "carrier/report";
	}
	
	/**
	 * 运营商 -运营商原始数据报告
	 */
	@RequestMapping(value = "/rawreportdata")
	public String rawreport(Model model, @ModelAttribute(value = "taskid") String taskid) {
		System.out.println("rawreport");
		WebRawDataReportV2  datas= carrierETLClient.getReportRawdata(taskid);
		model.addAttribute("taskid",taskid);
	    model.addAttribute("datas", datas);		
		return "carrier/rawreport";
	}
	
	
	/**
	 * 运营商 -运营商报告
	 */
	@RequestMapping("/reportexplain")
	public String getCarrierReport() {
		System.out.println("reportExplain");
		return "docs/carrier/reportExplain";
	}
	
	/**
	 * 运营商 -原始数据说明
	 */
	@RequestMapping("/rawreportexplain")
	public String getReportrawdata() {
		System.out.println("rawreportExplain");
		return "docs/carrier/rawreportExplain";
	}
	/**
	 * 运营商 - 接入指引
	 */
	@RequestMapping("/carrier-enter")
	public String carrierEnter() {
		System.out.println("carrierenter");
		return "docs/carrier/carrier-enter";
	}
	
	/**
	 * 运营商 - 快速接入
	 */
	@RequestMapping("/carrier-quickenter")
	public String carrierQuickenter() {
		System.out.println("carrierquickenter");
		return "docs/carrier/carrier-quickenter";
	}
	/**
	 * 运营商 - 快速接入
	 */
	@RequestMapping("/carrier-questionfaq")
	public String carrierQuestionfaq() {
		System.out.println("carrierquestionfaq");
		return "docs/carrier/carrier-questionfaq";
	}
	/**
	 * 运营商 - 异常处理文档
	 */
	@RequestMapping("/carrier-exceptdoc")
	public String carrierExceptdoc() {
		System.out.println("carrierexceptdoc");
		return "docs/carrier/carrier-exceptdoc";
	}
	/**
	 * 运营商 - 在线认证文档
	 */
	@RequestMapping("/carrier-online")
	public String carrierOnline() {
		System.out.println("carrieronline");
		return "docs/carrier/carrier-online";
	}
	/**
	 * 运营商 - 文档资源下载
	 */
	@RequestMapping("/carrier-download")
	public String carrierDownload() {
		System.out.println("carrierdownload");
		return "docs/carrier/carrier-download";
	}
	
	
	/**
	 * 运营商 -原始数据文档- 原始数据文档简介
	 */
	@RequestMapping("/carrier-docintro")
	public String carrierDocintro() {
		System.out.println("carrierDocintro");
		return "docs/carrier/carrier-docintro";
	}
	
	/**
	 * 运营商 -原始数据文档- 获取运营商原始数据增强版（推荐使用）
	 */
	@RequestMapping("/carrier-rawdataplus")
	public String carrierRawdataplus() {
		System.out.println("carrierRawdataplus");
		return "docs/carrier/carrier-rawdataplus";
	}
	

	/**
	 * 运营商 -原始数据文档- 获取运营商原始数据
	 */
	@RequestMapping("/carrier-rawdata")
	public String carrierRawdata() {
		System.out.println("carrierRawdata");
		return "docs/carrier/carrier-rawdata";
	}
	

	/**
	 * 运营商 -原始数据文档- 获取运营商原始数据(含通话详单采集结果)
	 */
	@RequestMapping("/carrier-rawdatacalldetails")
	public String carrierRawdatacalldetails() {
		System.out.println("carrierRawdatacalldetails");
		return "docs/carrier/carrier-rawdatacalldetails";
	}
	
	/**
	 * 运营商 -原始数据文档- 获取账号运营商数据(分拆)
	 */
	@RequestMapping("/carrier-rawdatasplit")
	public String carrierRawdatasplit() {
		System.out.println("carrierRawdatasplit");
		return "docs/carrier/carrier-rawdatasplit";
	}
	
	/**
	 * 运营商 -用户报告文档-用户报告文档简介
	 */
	@RequestMapping("/carrier-reportintro")
	public String carrierReportintro() {
		System.out.println("carrierReportintro");
		return "docs/carrier/carrier-reportintro";
	}
	/**
	 * 运营商 -用户报告文档-查询报告状态接口
	 */
	@RequestMapping("/carrier-reportstate")
	public String carrierReportstate() {
		System.out.println("carrierReportstate");
		return "docs/carrier/carrier-reportstate";
	}
	/**
	 * 运营商 -用户报告文档-用户报告API文档
	 */
	@RequestMapping("/carrier-reportdoc")
	public String carrierReportdoc() {
		System.out.println("carrierReportdoc");
		return "docs/carrier/carrier-reportdoc";
	}
	/**
	 * 运营商 -用户报告文档-用户报告在线展示文档
	 */
	@RequestMapping("/carrier-reportonline")
	public String carrierReportonline() {
		System.out.println("carrierReportonline");
		return "docs/carrier/carrier-reportonline";
	}
	/**
	 * 运营商 -用户报告文档-用户报告在线展示（兼容版本）文档
	 */
	@RequestMapping("/carrier-reportonlinecomp")
	public String carrierReportonlinecomp() {
		System.out.println("carrierReportonlinecomp");
		return "docs/carrier/carrier-reportonlinecomp";
	}
	/**
	 * 运营商 -服务器异步回调说明-服务器异步回调说明详解
	 */
	@RequestMapping("/carrier-asyncallback")
	public String carrierAsyncallback() {
		System.out.println("carrierAsyncallback");
		return "docs/carrier/carrier-asyncallback";
	}
	/**
	 * 运营商 -服务器异步回调说明-查询回调信息接口
	 */
	@RequestMapping("/carrier-callbackinter")
	public String carrierCallbackinter() {
		System.out.println("carrierCallbackinter");
		return "docs/carrier/carrier-callbackinter";
	}
	
	/**
	 * 运营商 -服务器异步回调说明-主动触发回调通知接口
	 */
	@RequestMapping("/carrier-trignoteinter")
	public String carrierTrignoteinter() {
		System.out.println("carrierTrignoteinter");
		return "docs/carrier/carrier-trignoteinter";
	}
}
