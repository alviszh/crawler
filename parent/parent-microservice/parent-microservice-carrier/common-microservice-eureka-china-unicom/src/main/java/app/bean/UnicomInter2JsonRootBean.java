/**
  * Copyright 2017 bejson.com 
  */
package app.bean;


/**
 * Auto-generated: 2017-07-30 11:27:30
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UnicomInter2JsonRootBean {

    private String busiorder;
    private String ecsbusiorder;
    private boolean existexception;
    private boolean isnotsuccess;
    private boolean issuccess;
    private String rspcode;
    private String rspdesc;
    private String rspsign;
    private String rspts;
    private UnicomInter2Result result;
    private String transid;
    private String trxid;
    
	private UnicomErrorMessage errorMessage;
	

    public UnicomErrorMessage getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(UnicomErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
	}
	public void setBusiorder(String busiorder) {
         this.busiorder = busiorder;
     }
     public String getBusiorder() {
         return busiorder;
     }

    public void setEcsbusiorder(String ecsbusiorder) {
         this.ecsbusiorder = ecsbusiorder;
     }
     public String getEcsbusiorder() {
         return ecsbusiorder;
     }

    public void setExistexception(boolean existexception) {
         this.existexception = existexception;
     }
     public boolean getExistexception() {
         return existexception;
     }

    public void setIsnotsuccess(boolean isnotsuccess) {
         this.isnotsuccess = isnotsuccess;
     }
     public boolean getIsnotsuccess() {
         return isnotsuccess;
     }

    public void setIssuccess(boolean issuccess) {
         this.issuccess = issuccess;
     }
     public boolean getIssuccess() {
         return issuccess;
     }

    public void setRspcode(String rspcode) {
         this.rspcode = rspcode;
     }
     public String getRspcode() {
         return rspcode;
     }

    public void setRspdesc(String rspdesc) {
         this.rspdesc = rspdesc;
     }
     public String getRspdesc() {
         return rspdesc;
     }

    public void setRspsign(String rspsign) {
         this.rspsign = rspsign;
     }
     public String getRspsign() {
         return rspsign;
     }

    public void setRspts(String rspts) {
         this.rspts = rspts;
     }
     public String getRspts() {
         return rspts;
     }

    public void setResult(UnicomInter2Result result) {
         this.result = result;
     }
     public UnicomInter2Result getResult() {
         return result;
     }

    public void setTransid(String transid) {
         this.transid = transid;
     }
     public String getTransid() {
         return transid;
     }

    public void setTrxid(String trxid) {
         this.trxid = trxid;
     }
     public String getTrxid() {
         return trxid;
     }


}