/**
  * Copyright 2018 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.insurance.suqian.InsurancePay;

/**
 * Auto-generated: 2018-03-13 14:59:18
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class InsurancePaySuQianJsonRootBean {

    private String personId;
    private String startDate;
    private String endDate;
    private List<InsurancePay> list;
    public void setPersonId(String personId) {
         this.personId = personId;
     }
     public String getPersonId() {
         return personId;
     }

    public void setStartDate(String startDate) {
         this.startDate = startDate;
     }
     public String getStartDate() {
         return startDate;
     }

    public void setEndDate(String endDate) {
         this.endDate = endDate;
     }
     public String getEndDate() {
         return endDate;
     }

    public void setList(List<InsurancePay> list) {
         this.list = list;
     }
     public List<InsurancePay> getList() {
         return list;
     }
	@Override
	public String toString() {
		return "InsurancePaySuQianJsonRootBean [personId=" + personId + ", startDate=" + startDate + ", endDate="
				+ endDate + ", list=" + list + "]";
	}
     
     

}