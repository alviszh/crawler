package app.crawler.domain;

import java.util.List;

import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinInjury;
import com.microservice.dao.entity.crawler.insurance.tianjin.InsuranceTianjinMedical;

public class InsuranceTianjinInjuryList {

	private List<InsuranceTianjinInjury> empPaymentDTO;

	private String total;

	public List<InsuranceTianjinInjury> getEmpPaymentDTO() {
		return empPaymentDTO;
	}

	public void setEmpPaymentDTO(List<InsuranceTianjinInjury> empPaymentDTO) {
		this.empPaymentDTO = empPaymentDTO;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
