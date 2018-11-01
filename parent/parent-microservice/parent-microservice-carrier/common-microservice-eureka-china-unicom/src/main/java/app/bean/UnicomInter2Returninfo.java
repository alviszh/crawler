/**
  * Copyright 2017 bejson.com 
  */
package app.bean;
import java.util.List;

import com.microservice.dao.entity.crawler.unicom.UnicomIntegraThemlResult;

public class UnicomInter2Returninfo {

    private String score;
    private List<UnicomIntegraThemlResult> scoredetailinfo;
    public void setScore(String score) {
         this.score = score;
     }
     public String getScore() {
         return score;
     }

    public void setScoredetailinfo(List<UnicomIntegraThemlResult> scoredetailinfo) {
         this.scoredetailinfo = scoredetailinfo;
     }
     public List<UnicomIntegraThemlResult> getScoredetailinfo() {
         return scoredetailinfo;
     }

}