/**
  * Copyright 2017 bejson.com 
  */
package app.bean;
import java.util.List;

/**
 * Auto-generated: 2017-07-14 10:30:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UnicomHistoryTegral {

    private List<UnicomHistoryScoreInfo> scoreInfo;
    private boolean success;
    private String busiOrder;
    private String respCode;
    private String respDesc;
    public void setScoreInfo(List<UnicomHistoryScoreInfo> scoreInfo) {
         this.scoreInfo = scoreInfo;
     }
     public List<UnicomHistoryScoreInfo> getScoreInfo() {
         return scoreInfo;
     }

    public void setSuccess(boolean success) {
         this.success = success;
     }
     public boolean getSuccess() {
         return success;
     }

    public void setBusiOrder(String busiOrder) {
         this.busiOrder = busiOrder;
     }
     public String getBusiOrder() {
         return busiOrder;
     }

    public void setRespCode(String respCode) {
         this.respCode = respCode;
     }
     public String getRespCode() {
         return respCode;
     }

    public void setRespDesc(String respDesc) {
         this.respDesc = respDesc;
     }
     public String getRespDesc() {
         return respDesc;
     }

}