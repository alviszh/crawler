package app.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.crawler.e_commerce.json.E_ComerceStatusCode;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.microservice.dao.entity.crawler.e_commerce.basic.E_CommerceTask;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoAlipayInfo;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TaobaoDeliverAddress;
import com.microservice.dao.entity.crawler.e_commerce.taobao.TbHtml;
import com.microservice.dao.repository.crawler.e_commerce.basic.E_CommerceTaskRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaAlipayPaymentInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoAlipayBankCardInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoAlipayInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TaobaoDeliverAddressRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TbHtmlRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TbOrderInfoRepository;
import com.microservice.dao.repository.crawler.e_commerce.taobao.TbUserInfoRepository;

import app.bean.WebParamE;
import app.commontracerlog.TracerLog;
import app.parser.TaobaoParser;
import app.parser.TbParser;

@Component
@EntityScan(basePackages = {"com.microservice.dao.entity.crawler.e_commerce.basic",
        "com.microservice.dao.entity.crawler.e_commerce.taobao"})
@EnableJpaRepositories(basePackages = {"com.microservice.dao.repository.crawler.e_commerce.basic",
        "com.microservice.dao.repository.crawler.e_commerce.taobao"})
public class AsyncGetDataService {

    @Autowired
    private TracerLog tracerLog;

    @Autowired
    private E_CommerceTaskStatusService e_commerceTaskStatusService;

    @Autowired
    private E_CommerceTaskRepository e_commerceTaskRepository;

    @Autowired
    private TaobaoDeliverAddressRepository taobaoDeliverAddressRepository;

    @Autowired
    private TaobaAlipayPaymentInfoRepository taobaAlipayPaymentInfoRepository;

    @Autowired
    private TaobaoAlipayBankCardInfoRepository taobaoAlipayBankCardInfoRepository;

    @Autowired
    private TbUserInfoRepository tbUserInfoRepository;

    @Autowired
    private TaobaoAlipayInfoRepository taobaoAlipayInfoRepository;

    @Autowired
    private TbOrderInfoRepository tbOrderInfoRepository;

    @Autowired
    private TbHtmlRepository tbHtmlRepository;
    
    @Autowired
    private ChaoJiYingOcrService chaoJiYingOcrService;
    
    @Autowired
    private TbParser tbParser;

    @Autowired
    private TaobaoParser taobaoParser;


	public void getOrderInfo(E_CommerceTask e_commerceTask, WebClient webClient, Set<org.openqa.selenium.Cookie> cookiesDriver) {
		
		tracerLog.output("crawler.getdata.getOrderInfo.taskid", e_commerceTask.getTaskid());
		try {
			WebParamE webParamE = tbParser.getOrderInfo(e_commerceTask, webClient, cookiesDriver);
			Page page = webParamE.getPage();
			if(null != page){
				TbHtml html = new TbHtml();
				html.setTaskid(e_commerceTask.getTaskid());
				html.setHtml(page.getWebResponse().getContentAsString());
				html.setPagenumber(0);
				html.setType("orderInfo");
				html.setUrl(webParamE.getUrl());
				tbHtmlRepository.save(html);
				tracerLog.output("crawler.getdata.getOrderInfo.saveHtml", "订单列表页面入库");
			}
			List list = webParamE.getList();
			if(null != list && list.size() > 0){
				tbOrderInfoRepository.saveAll(list);
				tracerLog.output("crawler.getdata.getOrderInfo.saveList", "订单信息入库");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ORDER_SUCCESS.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_ORDER_SUCCESS.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_ORDER_SUCCESS.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_ORDER_SUCCESS.getError_code()
						, "orderinfo", 200, e_commerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.getdata.getOrderInfo.saveList", "订单信息无数据");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR.getError_code()
						, "orderinfo", 201, e_commerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.getdata.getOrderInfo.saveList.exception", e.getMessage());
			tracerLog.output("crawler.getdata.getOrderInfo.saveList", "订单信息异常");
			e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR2.getPhase()
					, E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR2.getPhasestatus()
					, E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR2.getDescription()
					, E_ComerceStatusCode.E_COMMERCE_ORDER_ERROR2.getError_code()
					, "orderinfo", 404, e_commerceTask.getTaskid());
		}
	}


	public void getUserInfo(E_CommerceTask e_commerceTask, WebClient webClient) {
		tracerLog.output("crawler.getdata.getUserInfo.taskid", e_commerceTask.getTaskid());
		try {
			WebParamE webParamE = tbParser.getUserInfo(e_commerceTask, webClient);
			Page page = webParamE.getPage();
			if(null != page){
				TbHtml html = new TbHtml();
				html.setTaskid(e_commerceTask.getTaskid());
				html.setHtml(page.getWebResponse().getContentAsString());
				html.setPagenumber(0);
				html.setType("userInfo");
				html.setUrl(webParamE.getUrl());
				tbHtmlRepository.save(html);
				tracerLog.output("crawler.getdata.getUserInfo.saveHtml", "用户信息页面入库");
			}
			List list = webParamE.getList();
			if(null != list && list.size() > 0){
				tbUserInfoRepository.saveAll(list);
				tracerLog.output("crawler.getdata.getUserInfo.saveList", "用户信息入库");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_USERINFO_SUCCESS.getError_code()
						, "userinfo", 200, e_commerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.getdata.getUserInfo.saveList", "用户信息无数据");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR.getError_code()
						, "userinfo", 201, e_commerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.getdata.getUserInfo.save.exception", e.getMessage());
			tracerLog.output("crawler.getdata.getUserInfo.save", "淘宝用户信息异常");
			e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getPhase()
					, E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getPhasestatus()
					, E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getDescription()
					, E_ComerceStatusCode.E_COMMERCE_USERINFO_ERROR2.getError_code()
					, "userinfo", 404, e_commerceTask.getTaskid());
		}
	}


	public void getAddressInfo(E_CommerceTask e_commerceTask, String pageSource) {
		tracerLog.output("crawler.getdata.getAddressInfo.taskid", e_commerceTask.getTaskid());
		tracerLog.output("crawler.getdata.getAddressInfo.pageSource", pageSource);
		System.out.println("crawler.getdata.getAddressInfo.pageSource"+pageSource);
		try {
			TbHtml html = new TbHtml();
			html.setTaskid(e_commerceTask.getTaskid());
			html.setHtml(pageSource);
			html.setPagenumber(0);
			html.setType("addressInfo");
			html.setUrl("https://member1.taobao.com/member/fresh/deliver_address.htm");
			tbHtmlRepository.save(html);
			tracerLog.output("crawler.getdata.getAddressInfo.saveHtml", "收货地址信息页面入库");
			List<TaobaoDeliverAddress> addresses = tbParser.getAddInfo(e_commerceTask, pageSource);
			if(null != addresses && addresses.size() > 0){
				taobaoDeliverAddressRepository.saveAll(addresses);
				tracerLog.output("crawler.getdata.getAddressInfo.saveList", "淘宝用户收货地址信息入库");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_ADDRESS_SUCCESS.getError_code()
						, "addressinfo", 200, e_commerceTask.getTaskid());
				
			}else{
				tracerLog.output("crawler.getdata.getAddressInfo.save", "淘宝用户收货地址信息无数据");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR.getError_code()
						, "addressinfo", 201, e_commerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.getdata.getAddressInfo.save.exception", e.getMessage());
			tracerLog.output("crawler.getdata.getAddressInfo.save", "淘宝用户收货地址信息异常");
			e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getPhase()
					, E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getPhasestatus()
					, E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getDescription()
					, E_ComerceStatusCode.E_COMMERCE_ADDRESS_ERROR2.getError_code()
					, "addressinfo", 404, e_commerceTask.getTaskid());
		}
	}

	public void getAliPayInfo(E_CommerceTask e_commerceTask, WebClient webClient, TaobaoAlipayInfo alipayInfo) {
		tracerLog.output("crawler.getdata.getAliPayInfo.taskid", e_commerceTask.getTaskid());
		try {
			WebParamE webParamE = tbParser.getAliPayInfo(e_commerceTask, webClient, alipayInfo);
			Page page = webParamE.getPage();
			if(null != page){
				TbHtml html = new TbHtml();
				html.setTaskid(e_commerceTask.getTaskid());
				html.setHtml(page.getWebResponse().getContentAsString());
				html.setPagenumber(0);
				html.setType("aliPayInfo");
				html.setUrl(webParamE.getUrl());
				tbHtmlRepository.save(html);
				tracerLog.output("crawler.getdata.getAliPayInfo.saveHtml", "用户信息页面入库");
			}
			List list = webParamE.getList();
			taobaoAlipayInfoRepository.saveAll(list);
			tracerLog.output("crawler.getdata.getAddressInfo.saveList", "淘宝用户收货地址信息入库");
			e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ALIUSER_SUCCESS.getPhase()
					, E_ComerceStatusCode.E_COMMERCE_ALIUSER_SUCCESS.getPhasestatus()
					, E_ComerceStatusCode.E_COMMERCE_ALIUSER_SUCCESS.getDescription()
					, E_ComerceStatusCode.E_COMMERCE_ALIUSER_SUCCESS.getError_code()
					, "alipayinfo", 200, e_commerceTask.getTaskid());
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.getdata.getAliPayInfo.save.exception", e.getMessage());
			tracerLog.output("crawler.getdata.getAliPayInfo.save", "支付宝用户基本信息异常");
			e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_ALIUSER_ERROR2.getPhase()
					, E_ComerceStatusCode.E_COMMERCE_ALIUSER_ERROR2.getPhasestatus()
					, E_ComerceStatusCode.E_COMMERCE_ALIUSER_ERROR2.getDescription()
					, E_ComerceStatusCode.E_COMMERCE_ALIUSER_ERROR2.getError_code()
					, "alipayinfo", 404, e_commerceTask.getTaskid());
		}
		
	}


	public void getAliBankInfo(E_CommerceTask e_commerceTask, WebClient webClient) {
		tracerLog.output("crawler.getdata.getAliBankInfo.taskid", e_commerceTask.getTaskid());
		try {
			WebParamE webParamE = tbParser.getAliBankInfo(e_commerceTask, webClient);
			Page page = webParamE.getPage();
			if(null != page){
				TbHtml html = new TbHtml();
				html.setTaskid(e_commerceTask.getTaskid());
				html.setHtml(page.getWebResponse().getContentAsString());
				html.setPagenumber(0);
				html.setType("aliBankInfo");
				html.setUrl(webParamE.getUrl());
				tbHtmlRepository.save(html);
				tracerLog.output("crawler.getdata.getAliBankInfo.saveHtml", "支付宝绑定银行卡页面入库");
			}
			List list = webParamE.getList();
			if(null != list && list.size() > 0){
				taobaoAlipayBankCardInfoRepository.saveAll(list);
				tracerLog.output("crawler.getdata.getAliBankInfo.saveList", "支付宝绑定银行卡信息入库");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_BANKCARD_SUCCESS.getError_code()
						, "bankcardinfo", 200, e_commerceTask.getTaskid());
			}else{
				tracerLog.output("crawler.getdata.getAliBankInfo.saveList", "支付宝绑定银行卡信息无数据");
				e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhase()
						, E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getPhasestatus()
						, E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getDescription()
						, E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR.getError_code()
						, "bankcardinfo", 201, e_commerceTask.getTaskid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			tracerLog.output("crawler.getdata.getAliBankInfo.save.exception", e.getMessage());
			tracerLog.output("crawler.getdata.getAliBankInfo.save", "支付宝绑定银行卡信息异常");
			e_commerceTask = e_commerceTaskStatusService.changeStatusCode(E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getPhase()
					, E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getPhasestatus()
					, E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getDescription()
					, E_ComerceStatusCode.E_COMMERCE_BANKCARD_ERROR2.getError_code()
					, "bankcardinfo", 404, e_commerceTask.getTaskid());
		}
	}

}