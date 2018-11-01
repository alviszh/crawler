package app.bean2;

import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomPayMsgStatusResult;

public class PaymentRecordResult {
	private List<UnicomPayMsgStatusResult> payrecordsinfo;

	private boolean success;

	private String busiOrder;

	private String respCode;

	private String respDesc;

	public void setPayrecordsinfo(List<UnicomPayMsgStatusResult> payrecordsinfo) {
		this.payrecordsinfo = payrecordsinfo;
	}

	public List<UnicomPayMsgStatusResult> getPayrecordsinfo() {
		return this.payrecordsinfo;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setBusiOrder(String busiOrder) {
		this.busiOrder = busiOrder;
	}

	public String getBusiOrder() {
		return this.busiOrder;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespCode() {
		return this.respCode;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getRespDesc() {
		return this.respDesc;
	}

}
