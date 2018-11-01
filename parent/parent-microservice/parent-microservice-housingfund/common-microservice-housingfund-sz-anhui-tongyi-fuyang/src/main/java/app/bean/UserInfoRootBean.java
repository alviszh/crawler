/**
  * Copyright 2018 bejson.com 
  */
package app.bean;

/**
 * Auto-generated: 2018-05-03 15:1:20
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserInfoRootBean {

    private int code;
    private String message;
    private UserInfoDataset dataset;
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

    public void setDataset(UserInfoDataset dataset) {
         this.dataset = dataset;
     }
     public UserInfoDataset getDataset() {
         return dataset;
     }
	@Override
	public String toString() {
		return "HousingFundSzAnHuiTongYiFuYangRootBean [code=" + code + ", message=" + message + ", dataset=" + dataset
				+ "]";
	}
     
     

}