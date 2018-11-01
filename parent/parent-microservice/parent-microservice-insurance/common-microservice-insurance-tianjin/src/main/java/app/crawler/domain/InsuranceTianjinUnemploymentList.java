package app.crawler.domain;

import java.util.List;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinUnemployment;

public class InsuranceTianjinUnemploymentList {

	private List<InsuranceTianjinUnemployment> empPaymentDTO;

	private String total;

	public List<InsuranceTianjinUnemployment> getEmpPaymentDTO() {
		return empPaymentDTO;
	}

	public void setEmpPaymentDTO(List<InsuranceTianjinUnemployment> empPaymentDTO) {
		this.empPaymentDTO = empPaymentDTO;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	
}
