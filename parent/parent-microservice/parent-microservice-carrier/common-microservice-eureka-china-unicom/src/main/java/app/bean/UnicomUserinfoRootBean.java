/**
  * Copyright 2017 bejson.com 
  */
package app.bean;

import com.microservice.dao.entity.crawler.unicom.UnicomUserInfo;

public class UnicomUserinfoRootBean {

    private String queryTime;
    private UnicomUserInfo userInfo;
    private UnicomUserinfoResult result;
    private boolean success;
    private String transId;
    private String rspJson;
    private String rspCode;
    private String ecsBusiOrder;
    private String rspDesc;
    private boolean notSuccess;
    private String rspTs;
    private String rspSign;
    private String trxId;
    private boolean existException;
    
    private UnicomErrorMessage errorMessage;
	
  	public UnicomErrorMessage getErrorMessage() {
  		return errorMessage;
  	}

  	public void setErrorMessage(UnicomErrorMessage errorMessage) {
  		this.errorMessage = errorMessage;
  	}
    
    public void setQueryTime(String queryTime) {
         this.queryTime = queryTime;
     }
     public String getQueryTime() {
         return queryTime;
     }

    public void setUserInfo(UnicomUserInfo userInfo) {
         this.userInfo = userInfo;
     }
     public UnicomUserInfo getUserInfo() {
         return userInfo;
     }

    public void setResult(UnicomUserinfoResult result) {
         this.result = result;
     }
     public UnicomUserinfoResult getResult() {
         return result;
     }

    public void setSuccess(boolean success) {
         this.success = success;
     }
     public boolean getSuccess() {
         return success;
     }

    public void setTransId(String transId) {
         this.transId = transId;
     }
     public String getTransId() {
         return transId;
     }

    public void setRspJson(String rspJson) {
         this.rspJson = rspJson;
     }
     public String getRspJson() {
         return rspJson;
     }

    public void setRspCode(String rspCode) {
         this.rspCode = rspCode;
     }
     public String getRspCode() {
         return rspCode;
     }

    public void setEcsBusiOrder(String ecsBusiOrder) {
         this.ecsBusiOrder = ecsBusiOrder;
     }
     public String getEcsBusiOrder() {
         return ecsBusiOrder;
     }

    public void setRspDesc(String rspDesc) {
         this.rspDesc = rspDesc;
     }
     public String getRspDesc() {
         return rspDesc;
     }

    public void setNotSuccess(boolean notSuccess) {
         this.notSuccess = notSuccess;
     }
     public boolean getNotSuccess() {
         return notSuccess;
     }

    public void setRspTs(String rspTs) {
         this.rspTs = rspTs;
     }
     public String getRspTs() {
         return rspTs;
     }

    public void setRspSign(String rspSign) {
         this.rspSign = rspSign;
     }
     public String getRspSign() {
         return rspSign;
     }

    public void setTrxId(String trxId) {
         this.trxId = trxId;
     }
     public String getTrxId() {
         return trxId;
     }

    public void setExistException(boolean existException) {
         this.existException = existException;
     }
     public boolean getExistException() {
         return existException;
     }

}