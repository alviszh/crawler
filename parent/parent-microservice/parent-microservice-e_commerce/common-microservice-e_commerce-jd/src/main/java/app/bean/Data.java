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
public class Data {

    @Override
	public String toString() {
		return "Data [id=" + id + ", t=" + t + ", b=" + b + ", s=" + s + ", c=" + c + ", p=" + p + "]";
	}
	private String id;
    private List<String> t;
    private List<String> b;
    private List<Leibie> s;
    private List<String> c;
    private List<String> p;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setT(List<String> t) {
         this.t = t;
     }
     public List<String> getT() {
         return t;
     }

    public void setB(List<String> b) {
         this.b = b;
     }
     public List<String> getB() {
         return b;
     }

    public void setS(List<Leibie> s) {
         this.s = s;
     }
     public List<Leibie> getS() {
         return s;
     }

    public void setC(List<String> c) {
         this.c = c;
     }
     public List<String> getC() {
         return c;
     }

    public void setP(List<String> p) {
         this.p = p;
     }
     public List<String> getP() {
         return p;
     }

}