package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.bank.etl.CreditCardBaseInfo;
import com.microservice.dao.entity.crawler.bank.etl.CreditCardBillInfo;
import com.microservice.dao.entity.crawler.bank.etl.CreditCardInstallmentBill;
import com.microservice.dao.entity.crawler.bank.etl.CreditCardTransDetail;


public class WebDataCreditcard {

	public RequestParam param;
	public String message;
	public List<CreditCardBaseInfo> creditCardBaseInfo;
	public List<CreditCardBillInfo> creditCardBillInfo;
	public List<CreditCardInstallmentBill> creditCardInstallmentBill;
	public List<CreditCardTransDetail> creditCardTransDetail;
	public Date createtime = new Date();
	public Integer errorCode;
	public String profile;
	
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<CreditCardBaseInfo> getCreditCardBaseInfo() {
		return creditCardBaseInfo;
	}
	public void setCreditCardBaseInfo(List<CreditCardBaseInfo> creditCardBaseInfo) {
		this.creditCardBaseInfo = creditCardBaseInfo;
	}
	public List<CreditCardBillInfo> getCreditCardBillInfo() {
		return creditCardBillInfo;
	}
	public void setCreditCardBillInfo(List<CreditCardBillInfo> creditCardBillInfo) {
		this.creditCardBillInfo = creditCardBillInfo;
	}
	public List<CreditCardInstallmentBill> getCreditCardInstallmentBill() {
		return creditCardInstallmentBill;
	}
	public void setCreditCardInstallmentBill(List<CreditCardInstallmentBill> creditCardInstallmentBill) {
		this.creditCardInstallmentBill = creditCardInstallmentBill;
	}
	public List<CreditCardTransDetail> getCreditCardTransDetail() {
		return creditCardTransDetail;
	}
	public void setCreditCardTransDetail(List<CreditCardTransDetail> creditCardTransDetail) {
		this.creditCardTransDetail = creditCardTransDetail;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	
}
