/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

import java.util.List;

import com.microservice.dao.entity.crawler.insurance.changzhou.InsuranceChangZhouPay;

/**
 * Auto-generated: 2018-03-15 13:57:19
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class InsuranceChangZhouPayJsonRootBean {

	private List<InsuranceChangZhouPay> personPaymentInfoDetailDTO;

	public void setPersonPaymentInfoDetailDTO(List<InsuranceChangZhouPay> personPaymentInfoDetailDTO) {
		this.personPaymentInfoDetailDTO = personPaymentInfoDetailDTO;
	}

	public List<InsuranceChangZhouPay> getPersonPaymentInfoDetailDTO() {
		return personPaymentInfoDetailDTO;
	}

	@Override
	public String toString() {
		return "InsuranceChangZhouPayJsonRootBean [personPaymentInfoDetailDTO=" + personPaymentInfoDetailDTO + "]";
	}
	
	

}