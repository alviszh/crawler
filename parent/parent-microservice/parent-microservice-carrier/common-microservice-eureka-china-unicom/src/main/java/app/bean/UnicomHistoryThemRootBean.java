/**
  * Copyright 2017 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomDetailList;

/**
 * Auto-generated: 2017-07-14 10:30:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UnicomHistoryThemRootBean {

	private UnicomHistoryErrorMessage errorMessage;
    public UnicomHistoryErrorMessage getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(UnicomHistoryErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setFixNum(boolean isFixNum) {
		this.isFixNum = isFixNum;
	}
	private List<UnicomDetailList> historyResultList;
    private String nowFee;
    private String historyResultState;
    private String service;
    private String isFix;
    private String billcycle;
    private boolean isFixNum;
    private String payTotal;
    private String discount;
    private UnicomHistoryTegral tegral;
    public void setHistoryResultList(List<UnicomDetailList> historyResultList) {
         this.historyResultList = historyResultList;
     }
     public List<UnicomDetailList> getHistoryResultList() {
         return historyResultList;
     }

    public void setNowFee(String nowFee) {
         this.nowFee = nowFee;
     }
     public String getNowFee() {
         return nowFee;
     }

    public void setHistoryResultState(String historyResultState) {
         this.historyResultState = historyResultState;
     }
     public String getHistoryResultState() {
         return historyResultState;
     }

    public void setService(String service) {
         this.service = service;
     }
     public String getService() {
         return service;
     }

    public void setIsFix(String isFix) {
         this.isFix = isFix;
     }
     public String getIsFix() {
         return isFix;
     }

    public void setBillcycle(String billcycle) {
         this.billcycle = billcycle;
     }
     public String getBillcycle() {
         return billcycle;
     }

    public void setIsFixNum(boolean isFixNum) {
         this.isFixNum = isFixNum;
     }
     public boolean getIsFixNum() {
         return isFixNum;
     }

    public void setPayTotal(String payTotal) {
         this.payTotal = payTotal;
     }
     public String getPayTotal() {
         return payTotal;
     }

    public void setDiscount(String discount) {
         this.discount = discount;
     }
     public String getDiscount() {
         return discount;
     }

    public void setTegral(UnicomHistoryTegral tegral) {
         this.tegral = tegral;
     }
     public UnicomHistoryTegral getTegral() {
         return tegral;
     }

}