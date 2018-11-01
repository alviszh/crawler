package app.entity.bankETL;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.bank.etl.DebiCardTransDetail;
import com.microservice.dao.entity.crawler.bank.etl.DebitCardBaseInfo;
import com.microservice.dao.entity.crawler.bank.etl.DebitCardDepositInfo;


public class WebDataDebitcard {
	
	public RequestParam param;

	public String message;
	public List<DebitCardBaseInfo> debitCardBaseInfo;
	public List<DebiCardTransDetail> debiCardTransDetail;
	public List<DebitCardDepositInfo> debitCardDepositInfo;
	public Date createtime = new Date();
	public Integer errorCode;
	public String profile;
	
	public List<DebiCardTransDetail> getDebiCardTransDetail() {
		return debiCardTransDetail;
	}
	public void setDebiCardTransDetail(List<DebiCardTransDetail> debiCardTransDetail) {
		this.debiCardTransDetail = debiCardTransDetail;
	}
	public List<DebitCardBaseInfo> getDebitCardBaseInfo() {
		return debitCardBaseInfo;
	}
	public void setDebitCardBaseInfo(List<DebitCardBaseInfo> debitCardBaseInfo) {
		this.debitCardBaseInfo = debitCardBaseInfo;
	}
	public List<DebitCardDepositInfo> getDebitCardDepositInfo() {
		return debitCardDepositInfo;
	}
	public void setDebitCardDepositInfo(List<DebitCardDepositInfo> debitCardDepositInfo) {
		this.debitCardDepositInfo = debitCardDepositInfo;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	@Override
	public String toString() {
		return "WebDataDebitcard [debitCardBaseInfo=" + debitCardBaseInfo + ", debiCardTransDetail="
				+ debiCardTransDetail + ", debitCardDepositInfo=" + debitCardDepositInfo + "]";
	}
	
}
