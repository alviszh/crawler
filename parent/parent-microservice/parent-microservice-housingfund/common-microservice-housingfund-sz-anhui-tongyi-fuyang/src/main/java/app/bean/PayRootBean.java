/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

/**
 * Auto-generated: 2018-05-03 15:58:4
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PayRootBean {

    private int code;
    private String message;
    private PayDataset dataset;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setDataset(PayDataset dataset) {
         this.dataset = dataset;
     }
     public PayDataset getDataset() {
         return dataset;
     }
	@Override
	public String toString() {
		return "PayRootBean [code=" + code + ", message=" + message + ", dataset=" + dataset + "]";
	}
     
     

}