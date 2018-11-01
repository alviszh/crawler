/**
  * Copyright 2018 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.housing.tongyi.fuyang.HousingAnHuiTongyiFuYangUserInfo;

/**
 * Auto-generated: 2018-05-03 15:1:20
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserInfoDataset {

    private int total;
    private List<HousingAnHuiTongyiFuYangUserInfo> rows;
    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setRows(List<HousingAnHuiTongyiFuYangUserInfo> rows) {
         this.rows = rows;
     }
     public List<HousingAnHuiTongyiFuYangUserInfo> getRows() {
         return rows;
     }
	@Override
	public String toString() {
		return "Dataset [total=" + total + ", rows=" + rows + "]";
	}
     
     

}