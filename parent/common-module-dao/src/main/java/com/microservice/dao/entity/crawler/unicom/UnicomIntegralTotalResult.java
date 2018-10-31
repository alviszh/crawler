/**
  * Copyright 2017 bejson.com 
  */
package com.microservice.dao.entity.crawler.unicom;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

@Entity
@Table(name = "unicom_integrategralresult",indexes = {@Index(name = "index_unicom_integrategralresult_taskid", columnList = "taskid")})
public class UnicomIntegralTotalResult  extends IdEntity {

    private String begindate;
    private String useintegral;
    private String usedintegral;
    private String premonthintegral;
    private String totalintegral;//积分总数
    private String transid;
    private String errMessage;
    private String busiorder;
    private String rspJson;
    private String rspcode;
    private String rspdesc;
    private String rspts;
    private String reqsign;
    private String rspsign;
    private String trxid;
    
    
    private String areacode;
    private String availablescore;//积分总数
    private String cangivescore;//积分总数
    private String certnum;
    private String certtype;
    private String custscore;
    private String customerlevel;
    private String invalidscore;
    private String isfusionorshare;
    private String ismergescore;
    private String isprimerynumber;
    private String lastmonthscore;
    private String netage;
    private String serialnumber;
    private String totalscore;
    private String userstate;
   
    
    private Integer userid;

	private String taskid;
    public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public void setBegindate(String begindate) {
         this.begindate = begindate;
     }
     public String getBegindate() {
         return begindate;
     }

    public void setUseintegral(String useintegral) {
         this.useintegral = useintegral;
     }
     public String getUseintegral() {
         return useintegral;
     }

    public void setUsedintegral(String usedintegral) {
         this.usedintegral = usedintegral;
     }
     public String getUsedintegral() {
         return usedintegral;
     }

    public void setPremonthintegral(String premonthintegral) {
         this.premonthintegral = premonthintegral;
     }
     public String getPremonthintegral() {
         return premonthintegral;
     }

    public void setTotalintegral(String totalintegral) {
         this.totalintegral = totalintegral;
     }
     public String getTotalintegral() {
         return totalintegral;
     }

    public void setTransid(String transid) {
         this.transid = transid;
     }
     public String getTransid() {
         return transid;
     }

    public void setErrMessage(String errMessage) {
         this.errMessage = errMessage;
     }
     public String getErrMessage() {
         return errMessage;
     }

    public void setBusiorder(String busiorder) {
         this.busiorder = busiorder;
     }
     public String getBusiorder() {
         return busiorder;
     }

    public void setRspJson(String rspJson) {
         this.rspJson = rspJson;
     }
     public String getRspJson() {
         return rspJson;
     }

    public void setRspcode(String rspcode) {
         this.rspcode = rspcode;
     }
     public String getRspcode() {
         return rspcode;
     }

    public void setRspdesc(String rspdesc) {
         this.rspdesc = rspdesc;
     }
     public String getRspdesc() {
         return rspdesc;
     }

    public void setRspts(String rspts) {
         this.rspts = rspts;
     }
     public String getRspts() {
         return rspts;
     }

    public void setReqsign(String reqsign) {
         this.reqsign = reqsign;
     }
     public String getReqsign() {
         return reqsign;
     }

    public void setRspsign(String rspsign) {
         this.rspsign = rspsign;
     }
     public String getRspsign() {
         return rspsign;
     }

    public void setTrxid(String trxid) {
         this.trxid = trxid;
     }
     public String getTrxid() {
         return trxid;
     }
	public String getAreacode() {
		return areacode;
	}
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	public String getAvailablescore() {
		return availablescore;
	}
	public void setAvailablescore(String availablescore) {
		this.availablescore = availablescore;
	}
	public String getCangivescore() {
		return cangivescore;
	}
	public void setCangivescore(String cangivescore) {
		this.cangivescore = cangivescore;
	}
	public String getCertnum() {
		return certnum;
	}
	public void setCertnum(String certnum) {
		this.certnum = certnum;
	}
	public String getCerttype() {
		return certtype;
	}
	public void setCerttype(String certtype) {
		this.certtype = certtype;
	}
	public String getCustscore() {
		return custscore;
	}
	public void setCustscore(String custscore) {
		this.custscore = custscore;
	}
	public String getCustomerlevel() {
		return customerlevel;
	}
	public void setCustomerlevel(String customerlevel) {
		this.customerlevel = customerlevel;
	}
	public String getInvalidscore() {
		return invalidscore;
	}
	public void setInvalidscore(String invalidscore) {
		this.invalidscore = invalidscore;
	}
	public String getIsfusionorshare() {
		return isfusionorshare;
	}
	public void setIsfusionorshare(String isfusionorshare) {
		this.isfusionorshare = isfusionorshare;
	}
	public String getIsmergescore() {
		return ismergescore;
	}
	public void setIsmergescore(String ismergescore) {
		this.ismergescore = ismergescore;
	}
	public String getIsprimerynumber() {
		return isprimerynumber;
	}
	public void setIsprimerynumber(String isprimerynumber) {
		this.isprimerynumber = isprimerynumber;
	}
	public String getLastmonthscore() {
		return lastmonthscore;
	}
	public void setLastmonthscore(String lastmonthscore) {
		this.lastmonthscore = lastmonthscore;
	}
	public String getNetage() {
		return netage;
	}
	public void setNetage(String netage) {
		this.netage = netage;
	}
	public String getSerialnumber() {
		return serialnumber;
	}
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	public String getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(String totalscore) {
		this.totalscore = totalscore;
	}
	public String getUserstate() {
		return userstate;
	}
	public void setUserstate(String userstate) {
		this.userstate = userstate;
	}
     
     

}