package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crawler.monitor.json.MonitorAllWebBankTempBean;
import com.microservice.dao.entity.crawler.monitor.MonitorAllWebUsable;

import app.pbccrc.TaskerPbccrcService;
import app.tasker.etlmail.MonitorETLMailService;
import app.taskerbank.MonitorBankService;
import app.taskerbank.TaskerBankService;
import app.taskercarrier.MonitorTelecomService;
import app.taskerecommerce.TaskerEcommerceService;
import app.taskerhousingfund.TaskerFundService;
import app.taskersocialinsurance.TaskerSocialInsurService;
import app.webchange.MonitorWebChangeService;
import app.webusable.MonitorWebUsableService;



/**
 * @description:   用于测试调用，打包发布后直接走定时任务
 * @author: sln 
 * @date: 2018年2月5日 上午11:05:59 
 */
@EnableFeignClients
@RestController
@RequestMapping("/monitor")
public class MonitorController {
	@Autowired
	private MonitorBankService monitorBankService;
	@Autowired
	public MonitorWebChangeService webChangeService;
	@Autowired
	private MonitorTelecomService taskerTelecomService;
	@Autowired
	private TaskerFundService taskerFundService;
	@Autowired
	private TaskerSocialInsurService taskerInsuranceService;
	@Autowired
	private TaskerBankService taskerBankService;
	@Autowired
	private TaskerPbccrcService taskerPbccrcService;
	@Autowired
	private MonitorETLMailService eTLMailService;
	@Autowired
	private MonitorWebUsableService webUsableService;
	@Autowired
	private TaskerEcommerceService taskerEcommerceService;
//	=================================================================
	//监测银行微服务可用性，便于前端页面做出相应处理
	@RequestMapping(value = "/bankusable",method = {RequestMethod.POST, RequestMethod.GET})
	public List<MonitorAllWebBankTempBean> monitorbank(){
		List<MonitorAllWebBankTempBean> linkbankUsable = monitorBankService.linkbankUsable();
		return linkbankUsable;
	}
//	=================================================================
	//监测网站可用性
	@GetMapping(path = "/webusable")
	public void webUsableTasker(){
		webUsableService.webUsableTasker();   
	}
	//监测网站是否改版      //程序经常走不动，故决定暂时不要这种方法 
	/*@GetMapping(path = "/webchange")
	public void webChangeTasker() { 
		webChangeService.webChangeTasker();
	}*/
//	=================================================================
	//运营商
	@GetMapping(path = "/alltelecom")
	public void telecomTasker(){
		taskerTelecomService.telecomTasker();
	}
	@GetMapping(path = "/onetelecom")
	public void eachTelecomTasker(@RequestParam(name = "province") String province) { 
		taskerTelecomService.oneWebByHand(province);
	}
//	=================================================================
	//公积金
	@GetMapping(value = "/allhousing")
	public void housingTasker(){
		taskerFundService.housingTasker();
	}
	@GetMapping(value = "/onehousing")
	public void eachHousingTasker(@RequestParam(name = "city") String city){
		taskerFundService.oneWebByHand(city);
	}
//	=================================================================
	//社保
	@GetMapping(value = "/allinsurance")
	public void insuranceTasker(){
		taskerInsuranceService.insuranceTasker();
	}
	@GetMapping(value = "/oneinsurance")
	public void eachInsuranceTasker(@RequestParam(name = "city") String city){
		taskerInsuranceService.oneWebByHand(city);
	}
//	=================================================================
	//银行
	@GetMapping(value = "/allbank")
	public void bankTasker(){
		taskerBankService.bankTasker();
	}
	@GetMapping(value = "/onebank")
	public void eachBankTasker(@RequestParam(name = "webtype") String webtype){
		taskerBankService.oneWebByHand(webtype);
	}
//	=================================================================
	//人行征信
	@GetMapping(value = "/allpbccrc")
	public void pbccrcTasker(){
		taskerPbccrcService.PbccrcTasker();
	}
	@GetMapping(value = "/onepbccrc")
	public void eachPbccrcTasker(@RequestParam(name = "username") String username){
		taskerPbccrcService.oneWebByHand(username);
	}
//	=================================================================
	//电商
	@GetMapping(value = "/allecom")
	public void ecomTasker(){
		taskerEcommerceService.eComTasker();
	}
	@GetMapping(value = "/oneecom")
	public void eachEcomTasker(@RequestParam(name = "webtype") String webtype){
		taskerEcommerceService.oneWebByHand(webtype);
	}
//	=================================================================
	//每日爬虫执行情况一览表
	@GetMapping(value = "/etlmail")
	public void taskerEtlResult(){
		eTLMailService.getAllWebTaskResultAndSendMail();
	}
//	=================================================================
	//所有网站的可用性展示
	@GetMapping(value = "/allweblinkstate")
	public List<MonitorAllWebUsable> showAllWebLinkState(){
		List<MonitorAllWebUsable> list = webUsableService.showAllWebUsable();
		System.out.println(list.toString());
		return list;
	}
	//根据网站名称，显示该网站指定天数之内的网络连接情况，用于波动展示
	@GetMapping(value = "/eachweblinkstate")
	public List<MonitorAllWebUsable> showEachWebLinkState(@RequestParam(name = "webtype") String webtype){
		List<MonitorAllWebUsable> list = webUsableService.showDaysWebUsableByWebType(webtype);
		System.out.println(list.toString());
		return list;
	}
}
