package app.crawler.domain;

import java.util.List;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMedical;

public class InsuranceTianjinMedicalList {

	private List<InsuranceTianjinMedical> empPaymentDTO;

	private String total;

	public List<InsuranceTianjinMedical> getEmpPaymentDTO() {
		return empPaymentDTO;
	}

	public void setEmpPaymentDTO(List<InsuranceTianjinMedical> empPaymentDTO) {
		this.empPaymentDTO = empPaymentDTO;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	
	
}
