/**
  * Copyright 2018 bejson.com 
  */
package app.bean;
import java.util.List;

/**
 * Auto-generated: 2018-08-28 16:55:56
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JDFenLeiJsonRootBean {

    private List<Data> data;
    @Override
	public String toString() {
		return "JsonRootBean [data=" + data + "]";
	}
	public void setData(List<Data> data) {
         this.data = data;
     }
     public List<Data> getData() {
         return data;
     }

}