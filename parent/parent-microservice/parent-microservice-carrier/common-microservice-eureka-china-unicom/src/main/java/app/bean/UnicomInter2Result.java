/**
  * Copyright 2017 bejson.com 
  */
package app.bean;
import java.util.Date;


public class UnicomInter2Result {

    private Date begindate;
    private String busiorder;
    private Date enddate;
    private boolean issuccess;
    private String respcode;
    private String respdesc;
    private UnicomInter2Returninfo returninfo;
    
        
    private int totalscore;
    public void setBegindate(Date begindate) {
         this.begindate = begindate;
     }
     public Date getBegindate() {
         return begindate;
     }

    public void setBusiorder(String busiorder) {
         this.busiorder = busiorder;
     }
     public String getBusiorder() {
         return busiorder;
     }

    public void setEnddate(Date enddate) {
         this.enddate = enddate;
     }
     public Date getEnddate() {
         return enddate;
     }

    public void setIssuccess(boolean issuccess) {
         this.issuccess = issuccess;
     }
     public boolean getIssuccess() {
         return issuccess;
     }

    public void setRespcode(String respcode) {
         this.respcode = respcode;
     }
     public String getRespcode() {
         return respcode;
     }

    public void setRespdesc(String respdesc) {
         this.respdesc = respdesc;
     }
     public String getRespdesc() {
         return respdesc;
     }

    public void setReturninfo(UnicomInter2Returninfo returninfo) {
         this.returninfo = returninfo;
     }
     public UnicomInter2Returninfo getReturninfo() {
         return returninfo;
     }

    public void setTotalscore(int totalscore) {
         this.totalscore = totalscore;
     }
     public int getTotalscore() {
         return totalscore;
     }

}