package app.service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.crawler.bank.json.BankJsonBean;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;

import app.commontracerlog.TracerLog;
import app.exceptiondetail.ExUtils;
import app.service.aop.ICrawler;
import app.service.common.CmbcChinaHelperService;
import net.sf.json.JSONObject;

/**
 * @description:
 * @author: sln 
 * @date: 2017年11月14日 下午2:50:03 
 */
@Component
public class CmbcChinaCreditCrawlerAllService  implements ICrawler{
	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private CmbcChinaCreditCrawlerService cmbcChinaCreditCrawlerService;
	@Autowired
	private TaskBankStatusService taskBankStatusService;
	public String creditAcType;
	public String billDay;
	@Autowired
	private CmbcChinaHelperService cmbcChinaHelperService;
	//爬取所有的数据
	@Async
	@Override
	public TaskBank getAllData(BankJsonBean bankJsonBean) {
		TaskBank taskBank = taskBankRepository.findByTaskid(bankJsonBean.getTaskid());
		Map<String, Future<String>> listfuture = new HashMap<String, Future<String>>();   //判断异步爬取是否完成
		//将信用卡信息和我的账户信息一致归为用户信息，故调用同样的更新数据库状态的方法
		//爬取信用卡信息
		try {
			Future<String> future =cmbcChinaCreditCrawlerService.getGeneralInfo(taskBank);
			listfuture.put("getGeneralInfo", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getGeneralInfo.e", taskBank.getTaskid()+"===>"+e.toString());
			taskBankStatusService.updateTaskBankUserinfo(201, "数据采集中，信用卡信息已采集完成", taskBank.getTaskid());
		}
		//爬取我的账户信息
		try {
			Future<String> future =cmbcChinaCreditCrawlerService.getMyAccount(taskBank);
			listfuture.put("getMyAccount", future);
		} catch (Exception e) {
			tracer.addTag("action.crawler.getMyAccount.e", taskBank.getTaskid()+"===>"+e.toString());
			taskBankStatusService.updateTaskBankUserinfo(201, "数据采集中，我的账户信息已采集完成", taskBank.getTaskid());
		}
		//爬取账单概要信息
		//先通过两个链接，获取一部分参数
		try {
			String url="https://nper.cmbc.com.cn/pweb/CreditAcTypeQry.do";
			WebRequest webRequest=new WebRequest(new URL(url),HttpMethod.POST);
			WebClient webClient = cmbcChinaHelperService.addcookie(taskBank);
			Page page = webClient.getPage(webRequest);
			if(null!=page){
				String html=page.getWebResponse().getContentAsString();
				JSONObject jsob=JSONObject.fromObject(html).getJSONArray("List").getJSONObject(0);
				creditAcType=jsob.getString("CreditAcType");
				//接下来获取别的参数(账单日)
				url="https://nper.cmbc.com.cn/pweb/CreditCardServiceTypeQry.do";
				String requestBody="{\"CreditAcType\":\""+creditAcType+"\"}";
				webRequest=new WebRequest(new URL(url),HttpMethod.POST);
				webRequest.setRequestBody(requestBody);
				page=webClient.getPage(webRequest);
				if(null!=page){
					html=page.getWebResponse().getContentAsString();
					billDay = JSONObject.fromObject(html).getString("BillDay");
				}
			}
		} catch (Exception e) {
			tracer.addTag("获取爬取账单信息需要的参数", ExUtils.getEDetail(e));
		}
		//爬取账单概要信息
		try {			
			String billDate=null;
			for(int i=1;i<=12;i++){
				billDate=CmbcChinaHelperService.getCreditBeforeMonth(i);
				Future<String> future =cmbcChinaCreditCrawlerService.getBillGeneral(taskBank,billDate,billDay,creditAcType);
				listfuture.put(billDate+"getBillGeneral", future);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBillGeneral.e", taskBank.getTaskid()+"===>"+e.toString());
			taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，账单概要信息已采集完成", taskBank.getTaskid());
		}
		//爬取账单明细信息（近期六个月的信息，请求参数和之后的六个月请求参数不一样,之后的六个月多了一个请求参数："BillSixQry":"BillSixQry"）
		try {
			String billDetailDate=null;
			for(int i=0;i<=5;i++){
				billDetailDate=CmbcChinaHelperService.getCreditBeforeMonth(i);
				cmbcChinaCreditCrawlerService.getBillDetailBeginSixMonth(taskBank,billDetailDate,billDay,creditAcType);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBillDetailBeginSixMonth.e", taskBank.getTaskid()+"===>"+e.toString());
			taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，账单明细信息已采集完成", taskBank.getTaskid());
		}
		//爬取账单明细信息（之后六个月的信息，多了一个请求参数："BillSixQry":"BillSixQry"）
		try {
			String billDetailDate=null;
			for(int i=6;i<=11;i++){
				billDetailDate=CmbcChinaHelperService.getCreditBeforeMonth(i);
				cmbcChinaCreditCrawlerService.getBillDetailOtherSixMonth(taskBank,billDetailDate,billDay,creditAcType);
			}
		} catch (Exception e) {
			tracer.addTag("action.crawler.getBillDetailOtherSixMonth.e", taskBank.getTaskid()+"===>"+e.toString());
			taskBankStatusService.updateTaskBankTransflow(201, "数据采集中，账单明细信息已采集完成", taskBank.getTaskid());
		}
		//最终状态的更新
		try {
			while (true) {
				for (Map.Entry<String, Future<String>> entry : listfuture.entrySet()) {
					if (entry.getValue().isDone()) { // 判断是否执行完毕
						tracer.addTag(taskBank.getTaskid() + entry.getKey() + "---get", entry.getValue().get());
						tracer.addTag(taskBank.getTaskid() + entry.getKey() + "---isDone", entry.getValue().get());
						listfuture.remove(entry.getKey());
						break;
					}
				}
				if (listfuture.size() == 0) {
					break;
				}
			}
			taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
		} catch (Exception e) {
			taskBankStatusService.changeTaskBankFinish(taskBank.getTaskid());
			tracer.addTag("listfuture--ERROR", taskBank.getTaskid() + "---ERROR:" + e);
		}
		return taskBank;
	}
	@Override
	public TaskBank getAllDataDone(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}
}
