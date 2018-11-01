/**
  * Copyright 2017 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomDetailList;


public class UnicomBilinfoResult {

    private List<UnicomDetailList> billinfo;
	public List<UnicomDetailList> getBillinfo() {
		return billinfo;
	}
	public void setBillinfo(List<UnicomDetailList> billinfo) {
		this.billinfo = billinfo;
	}
    
}