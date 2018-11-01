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
public class Leibie {

    @Override
	public String toString() {
		return "S [n=" + n + ", s=" + s + ", style=" + style + "]";
	}
	private String n;
    private List<Leibie> s;
    private int style;
    public void setN(String n) {
         this.n = n;
     }
     public String getN() {
         return n;
     }

    public void setS(List<Leibie> s) {
         this.s = s;
     }
     public List<Leibie> getS() {
         return s;
     }

    public void setStyle(int style) {
         this.style = style;
     }
     public int getStyle() {
         return style;
     }

}