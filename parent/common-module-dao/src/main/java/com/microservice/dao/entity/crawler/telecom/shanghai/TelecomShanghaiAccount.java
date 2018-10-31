package com.microservice.dao.entity.crawler.telecom.shanghai;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 上海电信-账单详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_shanghai_account")
public class TelecomShanghaiAccount extends IdEntity{
	
	private String taskid;
	private String accountName;						//账户名
	private String billingAccount;					//票据账户
	private String billingAccountCSN;				//票据号
	private String billingName;						//票据账户名
	private String billingProfile;					//票据概况
	private String billingProfileAddress;			//发票概况地址
	private String bureau;							//办公处
	private String cSN;								//发票账户
	private String currentStatus;					//当前状态
	private String extension;						//分机
	private String installDate;						//开户 日期
	private String installationAddress;				//开户地址
	private String parentPromotionAssetId;			//套餐代号
	private String parentPromotionProductName;		//套餐名
	private String prodPromId;						//	
	private String prodPromRuleId;
	private String productName;						//产品名称
	private String productPartNumber;
	private String promotionAssetId;
	private String sHCTPromotionConstrExpireDate;
	private String sHCTPromotionExpireDate;
	private String sHCTPromotionStartDate;
	private String serialNumber;
	private String subBureau;
	
	@Override
	public String toString() {
		return "TelecomShanghaiAccount [taskid=" + taskid + ", accountName=" + accountName + ", billingAccount="
				+ billingAccount + ", billingAccountCSN=" + billingAccountCSN + ", billingName=" + billingName
				+ ", billingProfile=" + billingProfile + ", billingProfileAddress=" + billingProfileAddress
				+ ", bureau=" + bureau + ", cSN=" + cSN + ", currentStatus=" + currentStatus + ", extension="
				+ extension + ", installDate=" + installDate + ", installationAddress=" + installationAddress
				+ ", parentPromotionAssetId=" + parentPromotionAssetId + ", parentPromotionProductName="
				+ parentPromotionProductName + ", prodPromId=" + prodPromId + ", prodPromRuleId=" + prodPromRuleId
				+ ", productName=" + productName + ", productPartNumber=" + productPartNumber + ", promotionAssetId="
				+ promotionAssetId + ", sHCTPromotionConstrExpireDate=" + sHCTPromotionConstrExpireDate
				+ ", sHCTPromotionExpireDate=" + sHCTPromotionExpireDate + ", sHCTPromotionStartDate="
				+ sHCTPromotionStartDate + ", serialNumber=" + serialNumber + ", subBureau=" + subBureau + "]";
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBillingAccount() {
		return billingAccount;
	}
	public void setBillingAccount(String billingAccount) {
		this.billingAccount = billingAccount;
	}
	public String getBillingAccountCSN() {
		return billingAccountCSN;
	}
	public void setBillingAccountCSN(String billingAccountCSN) {
		this.billingAccountCSN = billingAccountCSN;
	}
	public String getBillingName() {
		return billingName;
	}
	public void setBillingName(String billingName) {
		this.billingName = billingName;
	}
	public String getBillingProfile() {
		return billingProfile;
	}
	public void setBillingProfile(String billingProfile) {
		this.billingProfile = billingProfile;
	}
	public String getBillingProfileAddress() {
		return billingProfileAddress;
	}
	public void setBillingProfileAddress(String billingProfileAddress) {
		this.billingProfileAddress = billingProfileAddress;
	}
	public String getBureau() {
		return bureau;
	}
	public void setBureau(String bureau) {
		this.bureau = bureau;
	}
	public String getcSN() {
		return cSN;
	}
	public void setcSN(String cSN) {
		this.cSN = cSN;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getInstallDate() {
		return installDate;
	}
	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}
	public String getInstallationAddress() {
		return installationAddress;
	}
	public void setInstallationAddress(String installationAddress) {
		this.installationAddress = installationAddress;
	}
	public String getParentPromotionAssetId() {
		return parentPromotionAssetId;
	}
	public void setParentPromotionAssetId(String parentPromotionAssetId) {
		this.parentPromotionAssetId = parentPromotionAssetId;
	}
	public String getParentPromotionProductName() {
		return parentPromotionProductName;
	}
	public void setParentPromotionProductName(String parentPromotionProductName) {
		this.parentPromotionProductName = parentPromotionProductName;
	}
	public String getProdPromId() {
		return prodPromId;
	}
	public void setProdPromId(String prodPromId) {
		this.prodPromId = prodPromId;
	}
	public String getProdPromRuleId() {
		return prodPromRuleId;
	}
	public void setProdPromRuleId(String prodPromRuleId) {
		this.prodPromRuleId = prodPromRuleId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPartNumber() {
		return productPartNumber;
	}
	public void setProductPartNumber(String productPartNumber) {
		this.productPartNumber = productPartNumber;
	}
	public String getPromotionAssetId() {
		return promotionAssetId;
	}
	public void setPromotionAssetId(String promotionAssetId) {
		this.promotionAssetId = promotionAssetId;
	}
	public String getsHCTPromotionConstrExpireDate() {
		return sHCTPromotionConstrExpireDate;
	}
	public void setsHCTPromotionConstrExpireDate(String sHCTPromotionConstrExpireDate) {
		this.sHCTPromotionConstrExpireDate = sHCTPromotionConstrExpireDate;
	}
	public String getsHCTPromotionExpireDate() {
		return sHCTPromotionExpireDate;
	}
	public void setsHCTPromotionExpireDate(String sHCTPromotionExpireDate) {
		this.sHCTPromotionExpireDate = sHCTPromotionExpireDate;
	}
	public String getsHCTPromotionStartDate() {
		return sHCTPromotionStartDate;
	}
	public void setsHCTPromotionStartDate(String sHCTPromotionStartDate) {
		this.sHCTPromotionStartDate = sHCTPromotionStartDate;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getSubBureau() {
		return subBureau;
	}
	public void setSubBureau(String subBureau) {
		this.subBureau = subBureau;
	}
	

}
