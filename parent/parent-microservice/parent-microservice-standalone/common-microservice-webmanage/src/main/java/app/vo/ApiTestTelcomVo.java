package app.vo;

import java.util.List;

import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCallThemResult;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomCustomerThemResult;
import com.microservice.dao.entity.crawler.telecom.heilongjiang.TelecomPayMsgThemResult;


/**
 * API测试 中国电信 Vo
 * @author rongshengxu
 *
 */
public class ApiTestTelcomVo {
	
	/** 用户信息 */
	private List<TelecomCustomerThemResult> listofTelecomCustomerThemResult;
	/** 通话详单 */
	private List<TelecomCallThemResult> listOfTelecomCallThemResult;
	/** 缴费记录 */
	private List<TelecomPayMsgThemResult> listofTelecomPayMsgThemResult;

	public List<TelecomCallThemResult> getListOfTelecomCallThemResult() {
		return listOfTelecomCallThemResult;
	}

	public void setListOfTelecomCallThemResult(List<TelecomCallThemResult> listOfTelecomCallThemResult) {
		this.listOfTelecomCallThemResult = listOfTelecomCallThemResult;
	}

	public List<TelecomCustomerThemResult> getListofTelecomCustomerThemResult() {
		return listofTelecomCustomerThemResult;
	}

	public void setListofTelecomCustomerThemResult(List<TelecomCustomerThemResult> listofTelecomCustomerThemResult) {
		this.listofTelecomCustomerThemResult = listofTelecomCustomerThemResult;
	}

	public List<TelecomPayMsgThemResult> getListofTelecomPayMsgThemResult() {
		return listofTelecomPayMsgThemResult;
	}

	public void setListofTelecomPayMsgThemResult(List<TelecomPayMsgThemResult> listofTelecomPayMsgThemResult) {
		this.listofTelecomPayMsgThemResult = listofTelecomPayMsgThemResult;
	}
	
	
	
	
}
