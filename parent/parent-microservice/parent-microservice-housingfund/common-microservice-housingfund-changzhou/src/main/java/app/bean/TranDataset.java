/**
  * Copyright 2018 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.changzhou.HousingChangZhouTranRows;

/**
 * Auto-generated: 2018-02-27 11:21:48
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TranDataset {

    private int total;
    private List<HousingChangZhouTranRows> rows;
    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setRows(List<HousingChangZhouTranRows> rows) {
         this.rows = rows;
     }
     public List<HousingChangZhouTranRows> getRows() {
         return rows;
     }

}