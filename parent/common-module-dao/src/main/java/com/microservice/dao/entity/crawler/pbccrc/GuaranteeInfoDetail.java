package com.microservice.dao.entity.crawler.pbccrc;

import com.microservice.dao.entity.IdEntity;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 为他人担保信息
 * Created by zmy on 2018/1/3.
 */
@Entity
@Table(name="guarantee_info_detail")
public class GuaranteeInfoDetail extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 5655850933840475074L;

    private String mapping_id;  //uuid 唯一标识
    private String report_no;   //人行征信报告编号
    private Long recordDetail_autoId;   //对应于creditRecordDetails中的auto_id
    @JsonBackReference
    public CreditRecordDetail creditRecordDetail;

    private String guaranteedPerson;    //被担保人姓名
    private String guaranteedPersonIdNum;   //被担保人身份证号
    private String otherGuaranteeAmount;    //为他人贷款合同担保金额
    private String realPrincipal;   //被担保贷款实际本金余额
    private String actualDay;   //截至年月

    @Transient
    public Long getRecordDetail_autoId() {
        if (creditRecordDetail != null) {
            return creditRecordDetail.getAuto_id();
        }
        return recordDetail_autoId;
    }

    public void setRecordDetail_autoId(Long recordDetail_autoId) {
        this.recordDetail_autoId = recordDetail_autoId;
    }

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="recordDetail_autoId")
    public CreditRecordDetail getCreditRecordDetail() {
        return creditRecordDetail;
    }

    public void setCreditRecordDetail(CreditRecordDetail creditRecordDetail) {
        this.creditRecordDetail = creditRecordDetail;
    }

    public String getMapping_id() {
        return mapping_id;
    }

    public void setMapping_id(String mapping_id) {
        this.mapping_id = mapping_id;
    }

    public String getReport_no() {
        return report_no;
    }

    public void setReport_no(String report_no) {
        this.report_no = report_no;
    }

    public String getGuaranteedPerson() {
        return guaranteedPerson;
    }

    public void setGuaranteedPerson(String guaranteedPerson) {
        this.guaranteedPerson = guaranteedPerson;
    }

    public String getGuaranteedPersonIdNum() {
        return guaranteedPersonIdNum;
    }

    public void setGuaranteedPersonIdNum(String guaranteedPersonIdNum) {
        this.guaranteedPersonIdNum = guaranteedPersonIdNum;
    }

    public String getOtherGuaranteeAmount() {
        return otherGuaranteeAmount;
    }

    public void setOtherGuaranteeAmount(String otherGuaranteeAmount) {
        this.otherGuaranteeAmount = otherGuaranteeAmount;
    }

    public String getRealPrincipal() {
        return realPrincipal;
    }

    public void setRealPrincipal(String realPrincipal) {
        this.realPrincipal = realPrincipal;
    }

    public String getActualDay() {
        return actualDay;
    }

    public void setActualDay(String actualDay) {
        this.actualDay = actualDay;
    }

    @Override
    public String toString() {
        return "GuaranteeInfoDetail{" +
                "mapping_id='" + mapping_id + '\'' +
                ", report_no='" + report_no + '\'' +
                ", recordDetail_autoId=" + recordDetail_autoId +
                ", creditRecordDetail=" + creditRecordDetail +
                ", guaranteedPerson='" + guaranteedPerson + '\'' +
                ", guaranteedPersonIdNum='" + guaranteedPersonIdNum + '\'' +
                ", otherGuaranteeAmount='" + otherGuaranteeAmount + '\'' +
                ", realPrincipal='" + realPrincipal + '\'' +
                ", actualDay='" + actualDay + '\'' +
                '}';
    }
}
