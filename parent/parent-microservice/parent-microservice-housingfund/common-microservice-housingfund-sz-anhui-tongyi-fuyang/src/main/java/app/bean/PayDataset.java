/**
  * Copyright 2018 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangPay;

/**
 * Auto-generated: 2018-05-03 15:58:4
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PayDataset {

    private int total;
    private List<HousingAnHuiTongyiFuYangPay> rows;
    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setRows(List<HousingAnHuiTongyiFuYangPay> rows) {
         this.rows = rows;
     }
     public List<HousingAnHuiTongyiFuYangPay> getRows() {
         return rows;
     }
	@Override
	public String toString() {
		return "PayDataset [total=" + total + ", rows=" + rows + "]";
	}
     
     

}