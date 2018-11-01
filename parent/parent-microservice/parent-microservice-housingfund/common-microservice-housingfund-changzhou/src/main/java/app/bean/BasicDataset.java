/**
  * Copyright 2018 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.changzhou.HousingChangZhouBasicRows;


/**
 * Auto-generated: 2018-02-26 17:40:59
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BasicDataset {

    private int total;
    private List<HousingChangZhouBasicRows> rows;
    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setRows(List<HousingChangZhouBasicRows> rows) {
         this.rows = rows;
     }
     public List<HousingChangZhouBasicRows> getRows() {
         return rows;
     }

}