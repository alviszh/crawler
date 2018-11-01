package app.crawler.domain;

import java.util.List;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinPension;

public class InsuranceTianjinPensionList {

	private List<InsuranceTianjinPension> empPaymentDTO;

	private String total;

	public List<InsuranceTianjinPension> getEmpPaymentDTO() {
		return empPaymentDTO;
	}

	public void setEmpPaymentDTO(List<InsuranceTianjinPension> empPaymentDTO) {
		this.empPaymentDTO = empPaymentDTO;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
