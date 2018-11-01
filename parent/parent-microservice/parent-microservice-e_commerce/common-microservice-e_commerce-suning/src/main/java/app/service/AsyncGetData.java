package app.service;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.suning.SuNingHtml;
import com.microservice.dao.repository.crawler.e_commerce.suning.SuNingAccountInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.suning.SuNingAddressInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.suning.SuNingBankCardRepository;
import com.microservice.dao.repository.crawler.e_commerce.suning.SuNingHtmlRepository;
import com.microservice.dao.repository.crawler.e_commerce.suning.SuNingOrderDetailRepository;
import com.microservice.dao.repository.crawler.e_commerce.suning.SuNingUserInfoRepository;

import app.bean.WebParamE;
import app.commontracerlog.TracerLog;
import app.parser.SuNingParser;

@Component
@EnableAsync
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.e_commerce.suning" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.e_commerce.suning" })
public class AsyncGetData {

	@Autowired
    private E_CommerceTaskStatusService e_commerceTaskStatusService;
	@Autowired
	private SuNingOrderDetailRepository suNingOrderDetailRepository;
	@Autowired
	private SuNingAccountInfoRepository suNingAccountInfoRepository;
	@Autowired
	private SuNingAddressInfoRepository suNingAddressInfoRepository;
	@Autowired
	private SuNingBankCardRepository suNingBankCardRepository;
	@Autowired
	private SuNingUserInfoRepository suNingUserInfoRepository;
	@Autowired
	private SuNingHtmlRepository suNingHtmlRepository;
	@Autowired
	private SuNingParser suNingParser;
	@Autowired
	private TracerLog tracerLog;
	
//	@Async
	public void getOrderList(E_CommerceTask ecommerceTask, WebClient webClient, WebDriver driver) {
		tracerLog.output("crawler.AsyncGetData.getOrderList.taskid", ecommerceTask.getTaskid());
		try {
			WebParamE webParamE = suNingParser.getOrderList(ecommerceTask, webClient);
			
			SuNingHtml html = new SuNingHtml();
			html.setTaskid(ecommerceTask.getTaskid());
			html.setPagenumber(1);
			html.setType("orderDetail");
			html.setHtml(webParamE.getHtml());
			html.setUrl(webParamE.getUrl());
			suNingHtmlRepository.save(html);
			if(null != webParamE.getList() && webParamE.getList().size() > 0){
				suNingOrderDetailRepository.saveAll(webParamE.getList());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_SUCCESS.getPhase(), E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_SUCCESS.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_SUCCESS.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_SUCCESS.getError_code(), "orderinfo", 200, ecommerceTask.getTaskid());
				tracerLog.output("crawler.AsyncGetData.getOrderList.success", ecommerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.AsyncGetData.getOrderList.fail", ecommerceTask.getTaskid());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR.getPhase(), E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR.getError_code(), "orderinfo", 201, ecommerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.AsyncGetData.getOrderList.Exception", e.toString());
			e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR2.getPhase(), E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR2.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR2.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_TRANSFLOW_ERROR2.getError_code(), "orderinfo", 404, ecommerceTask.getTaskid());
		}
//		e_commerceTaskStatusService.changecommerceTaskJDFinish(ecommerceTask.getTaskid(), driver);
	}

//	@Async
	public void getUserInfo(E_CommerceTask ecommerceTask, WebClient webClient, WebDriver driver) {
		tracerLog.output("crawler.AsyncGetData.getUserInfo.taskid", ecommerceTask.getTaskid());
		try {
			WebParamE webParamE = suNingParser.getUserInfo(ecommerceTask, webClient);
			
			SuNingHtml html = new SuNingHtml();
			html.setTaskid(ecommerceTask.getTaskid());
			html.setPagenumber(1);
			html.setType("userinfo");
			html.setHtml(webParamE.getHtml());
			html.setUrl(webParamE.getUrl());
			suNingHtmlRepository.save(html);
			
			if(null != webParamE.getList() && webParamE.getList().size() > 0){
				suNingUserInfoRepository.saveAll(webParamE.getList());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getPhase(), E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getError_code(), "userinfo", 200, ecommerceTask.getTaskid());
				tracerLog.output("crawler.AsyncGetData.getUserInfo.success", ecommerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.AsyncGetData.getUserInfo.fail", ecommerceTask.getTaskid());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhase(), E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getError_code(), "userinfo", 201, ecommerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.AsyncGetData.getUserInfo.Exception", e.toString());
			e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getPhase(), E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getError_code(), "userinfo", 404, ecommerceTask.getTaskid());
		}
//		e_commerceTaskStatusService.changecommerceTaskJDFinish(ecommerceTask.getTaskid(), driver);
	}

//	@Async
	public void getAddress(E_CommerceTask ecommerceTask, WebClient webClient, WebDriver driver) {
		tracerLog.output("crawler.AsyncGetData.getAddress.taskid", ecommerceTask.getTaskid());
		try {
			WebParamE webParamE = suNingParser.getAddress(ecommerceTask, webClient);
			
			SuNingHtml html = new SuNingHtml();
			html.setTaskid(ecommerceTask.getTaskid());
			html.setPagenumber(1);
			html.setType("address");
			html.setHtml(webParamE.getPage().getWebResponse().getContentAsString());
			html.setUrl(webParamE.getUrl());
			suNingHtmlRepository.save(html);
			
			if(null != webParamE.getList() && webParamE.getList().size() > 0){
				suNingAddressInfoRepository.saveAll(webParamE.getList());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getPhase(), E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getError_code(), "addressinfo", 200, ecommerceTask.getTaskid());
				tracerLog.output("crawler.AsyncGetData.getAddress.success", ecommerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.AsyncGetData.getAddress.fail", ecommerceTask.getTaskid());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhase(), E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getError_code(), "addressinfo", 201, ecommerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.AsyncGetData.getAddress.Exception", e.toString());
			e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getPhase(), E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getError_code(), "addressinfo", 404, ecommerceTask.getTaskid());
		}
//		e_commerceTaskStatusService.changecommerceTaskJDFinish(ecommerceTask.getTaskid(), driver);
	}

//	@Async
	public void getAccountInfo(E_CommerceTask ecommerceTask, WebClient webClient, WebDriver driver) {
		tracerLog.output("crawler.AsyncGetData.getAccountInfo.taskid", ecommerceTask.getTaskid());
		try {
			WebParamE webParamE = suNingParser.getAccountInfo(ecommerceTask, webClient);

			SuNingHtml html = new SuNingHtml();
			html.setTaskid(ecommerceTask.getTaskid());
			html.setPagenumber(1);
			html.setType("account");
			html.setHtml(webParamE.getHtml());
			html.setUrl(webParamE.getUrl());
			suNingHtmlRepository.save(html);
			
			if(null != webParamE.getList() && webParamE.getList().size() > 0){
				suNingAccountInfoRepository.saveAll(webParamE.getList());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getPhase(), E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_RENZHENG_SUCCESS.getError_code(), "renzhenginfo", 200, ecommerceTask.getTaskid());
				tracerLog.output("crawler.AsyncGetData.getAccountInfo.success", ecommerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.AsyncGetData.getAccountInfo.fail", ecommerceTask.getTaskid());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhase(), E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_RENZHENG_ERROR.getError_code(), "renzhenginfo", 201, ecommerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.AsyncGetData.getAccountInfo.Exception", e.toString());
			e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_RENZHENGE_ERROR2.getPhase(), E_ComerceStatusCode.E_COMMERCE_RENZHENGE_ERROR2.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_RENZHENGE_ERROR2.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_RENZHENGE_ERROR2.getError_code(), "renzhenginfo", 404, ecommerceTask.getTaskid());
		}
//		e_commerceTaskStatusService.changecommerceTaskJDFinish(ecommerceTask.getTaskid(), driver);
	}

//	@Async
	public void getBankCardInfo(E_CommerceTask ecommerceTask, WebClient webClient, WebDriver driver) {
		tracerLog.output("crawler.AsyncGetData.getBankCardInfo.taskid", ecommerceTask.getTaskid());
		try {
			WebParamE webParamE = suNingParser.getBankCardInfo(ecommerceTask, webClient);

			SuNingHtml html = new SuNingHtml();
			html.setTaskid(ecommerceTask.getTaskid());
			html.setPagenumber(1);
			html.setType("bankcard");
			html.setHtml(webParamE.getHtml());
			html.setUrl(webParamE.getUrl());
			suNingHtmlRepository.save(html);
			
			if(null != webParamE.getList() && webParamE.getList().size() > 0){
				suNingBankCardRepository.saveAll(webParamE.getList());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getPhase(), E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getError_code(), "bankcardinfo", 200, ecommerceTask.getTaskid());
				tracerLog.output("crawler.AsyncGetData.getAccountInfo.success", ecommerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.AsyncGetData.getAccountInfo.fail", ecommerceTask.getTaskid());
				e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhase(), E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getDescription(),
						E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getError_code(), "bankcardinfo", 201, ecommerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.AsyncGetData.getBankCardInfo.Exception", e.toString());
			e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getPhase(), E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getPhasestatus(), E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getDescription(),
					E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getError_code(), "bankcardinfo", 404, ecommerceTask.getTaskid());
		}
//		e_commerceTaskStatusService.changecommerceTaskJDFinish(ecommerceTask.getTaskid(), driver);
	}

}
