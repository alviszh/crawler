/**
  * Copyright 2017 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.bank.bocchina.BocchinaDebitCardTransFlow;

/**
 * Auto-generated: 2017-10-31 16:35:41
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TranFlowList {

    private List<BocchinaDebitCardTransFlow> List;
    private int recordNumber;
    public void setList(List<BocchinaDebitCardTransFlow> List) {
         this.List = List;
     }
     public List<BocchinaDebitCardTransFlow> getList() {
         return List;
     }

    public void setRecordNumber(int recordNumber) {
         this.recordNumber = recordNumber;
     }
     public int getRecordNumber() {
         return recordNumber;
     }

}