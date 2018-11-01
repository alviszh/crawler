package app.entity.insuranceETL;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.insurance.etl.ETLInsuranceDetail;
import com.microservice.dao.entity.crawler.insurance.etl.ETLInsuranceUserinfo;

public class WebData {
	public RequestParam param;
	public List<ETLInsuranceDetail> etlInsuranceDetail;
	public List<ETLInsuranceUserinfo> etlInsuranceUserinfo;
	public String message;
	public Date createtime = new Date();
	public Integer errorCode;
	
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}
	public List<ETLInsuranceDetail> getEtlInsuranceDetail() {
		return etlInsuranceDetail;
	}
	public void setEtlInsuranceDetail(List<ETLInsuranceDetail> etlInsuranceDetail) {
		this.etlInsuranceDetail = etlInsuranceDetail;
	}
	public List<ETLInsuranceUserinfo> getEtlInsuranceUserinfo() {
		return etlInsuranceUserinfo;
	}
	public void setEtlInsuranceUserinfo(List<ETLInsuranceUserinfo> etlInsuranceUserinfo) {
		this.etlInsuranceUserinfo = etlInsuranceUserinfo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	@Override
	public String toString() {
		return "WebData [param=" + param + ", etlInsuranceDetail=" + etlInsuranceDetail + ", etlInsuranceUserinfo="
				+ etlInsuranceUserinfo + ", message=" + message + ", createtime=" + createtime + ", errorCode="
				+ errorCode + "]";
	}	
}
