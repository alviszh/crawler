/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

/**
 * Auto-generated: 2018-02-27 11:21:48
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TranJsonRootBean {

    private int code;
    private String message;
    private TranDataset dataset;
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

    public void setDataset(TranDataset dataset) {
         this.dataset = dataset;
     }
     public TranDataset getDataset() {
         return dataset;
     }

}