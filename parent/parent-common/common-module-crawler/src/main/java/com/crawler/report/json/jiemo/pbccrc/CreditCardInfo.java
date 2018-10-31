package com.crawler.report.json.jiemo.pbccrc;

import java.io.Serializable;

/**
 * 信用卡明细
 * Created by zmy on 2018/6/23.
 */
public class CreditCardInfo implements Serializable{

    private String  queryId;   //编号
    private String  status;   //账户状态
    private String  accountType;   //币种
    private String  isDelq;   //是否发送过逾期
    private String  isClosed;   //该账户是否已销户
    private String  isActived;   //该账户是否激活过
    private String  cardType;   //信用卡类型
    private String  bankName;   //发放信用卡银行的名称
    private String  badDebts;   //是否已变成呆账
    private String  releaseDate;   //发放日期
    private String  dueDate;   //截至年月
    private String  creditAmt;   //额度
    private String  usedAmt;   //已使用额度
    private String  overDueAmount;   //逾期金额
    private String  delqL5yAmt;   //最近五年逾期次数
    private String  isSixtydaysOverdraft;   //准贷记卡账户是否有大于60天逾期记录
    private String  sixtydaysOverdraftMonth;   //准记卡账户有超过60天逾期记录月数
    private String  delqL5y90dayAmt;   //最近五年90天以上逾期次数
    private String  cancellDate;   //销户年月

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIsDelq() {
        return isDelq;
    }

    public void setIsDelq(String isDelq) {
        this.isDelq = isDelq;
    }

    public String getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(String isClosed) {
        this.isClosed = isClosed;
    }

    public String getIsActived() {
        return isActived;
    }

    public void setIsActived(String isActived) {
        this.isActived = isActived;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBadDebts() {
        return badDebts;
    }

    public void setBadDebts(String badDebts) {
        this.badDebts = badDebts;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(String creditAmt) {
        this.creditAmt = creditAmt;
    }

    public String getUsedAmt() {
        return usedAmt;
    }

    public void setUsedAmt(String usedAmt) {
        this.usedAmt = usedAmt;
    }

    public String getOverDueAmount() {
        return overDueAmount;
    }

    public void setOverDueAmount(String overDueAmount) {
        this.overDueAmount = overDueAmount;
    }

    public String getDelqL5yAmt() {
        return delqL5yAmt;
    }

    public void setDelqL5yAmt(String delqL5yAmt) {
        this.delqL5yAmt = delqL5yAmt;
    }

    public String getIsSixtydaysOverdraft() {
        return isSixtydaysOverdraft;
    }

    public void setIsSixtydaysOverdraft(String isSixtydaysOverdraft) {
        this.isSixtydaysOverdraft = isSixtydaysOverdraft;
    }

    public String getSixtydaysOverdraftMonth() {
        return sixtydaysOverdraftMonth;
    }

    public void setSixtydaysOverdraftMonth(String sixtydaysOverdraftMonth) {
        this.sixtydaysOverdraftMonth = sixtydaysOverdraftMonth;
    }

    public String getDelqL5y90dayAmt() {
        return delqL5y90dayAmt;
    }

    public void setDelqL5y90dayAmt(String delqL5y90dayAmt) {
        this.delqL5y90dayAmt = delqL5y90dayAmt;
    }

    public String getCancellDate() {
        return cancellDate;
    }

    public void setCancellDate(String cancellDate) {
        this.cancellDate = cancellDate;
    }

    @Override
    public String toString() {
        return "CreditCardInfo{" +
                "queryId='" + queryId + '\'' +
                ", status='" + status + '\'' +
                ", accountType='" + accountType + '\'' +
                ", isDelq='" + isDelq + '\'' +
                ", isClosed='" + isClosed + '\'' +
                ", isActived='" + isActived + '\'' +
                ", cardType='" + cardType + '\'' +
                ", bankName='" + bankName + '\'' +
                ", badDebts='" + badDebts + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", creditAmt='" + creditAmt + '\'' +
                ", usedAmt='" + usedAmt + '\'' +
                ", overDueAmount='" + overDueAmount + '\'' +
                ", delqL5yAmt='" + delqL5yAmt + '\'' +
                ", isSixtydaysOverdraft='" + isSixtydaysOverdraft + '\'' +
                ", sixtydaysOverdraftMonth='" + sixtydaysOverdraftMonth + '\'' +
                ", delqL5y90dayAmt='" + delqL5y90dayAmt + '\'' +
                ", cancellDate='" + cancellDate + '\'' +
                '}';
    }
}
