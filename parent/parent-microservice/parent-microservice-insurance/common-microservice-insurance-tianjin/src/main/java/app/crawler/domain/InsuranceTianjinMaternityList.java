package app.crawler.domain;

import java.util.List;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMaternity;

public class InsuranceTianjinMaternityList {

	private List<InsuranceTianjinMaternity> empPaymentDTO;

	private String total;

	public List<InsuranceTianjinMaternity> getEmpPaymentDTO() {
		return empPaymentDTO;
	}

	public void setEmpPaymentDTO(List<InsuranceTianjinMaternity> empPaymentDTO) {
		this.empPaymentDTO = empPaymentDTO;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	
}
