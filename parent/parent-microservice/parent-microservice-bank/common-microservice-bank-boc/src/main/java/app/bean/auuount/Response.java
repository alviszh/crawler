/**
  * Copyright 2018 bejson.com 
  */
package app.bean.auuount;

/**
 * Auto-generated: 2018-06-28 11:7:32
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Response {

    private String id;
    private String method;
    private String status;
    private AccountSeqResult result;
    private String error;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setMethod(String method) {
         this.method = method;
     }
     public String getMethod() {
         return method;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setResult(AccountSeqResult result) {
         this.result = result;
     }
     public AccountSeqResult getResult() {
         return result;
     }

    public void setError(String error) {
         this.error = error;
     }
     public String getError() {
         return error;
     }

}