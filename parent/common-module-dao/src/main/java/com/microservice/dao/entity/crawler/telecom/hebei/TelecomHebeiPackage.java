package com.microservice.dao.entity.crawler.telecom.hebei;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.microservice.dao.entity.IdEntity;

/**
 * 河北电信-套餐详情表
 * @author zz
 *
 */
@Entity
@Table(name="telecom_hebei_package")
public class TelecomHebeiPackage extends IdEntity{
	
	private String taskid;
	private String productNum;				//产品号码
	private String productName;				//产品名称
	private String packageName;				//套餐名称
	private String status;					//状态
	private String netTime;					//入网时间
	private String packageDetail;			//套餐详情
	
	
	public String getNetTime() {
		return netTime;
	}
	public void setNetTime(String netTime) {
		this.netTime = netTime;
	}
	
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
	@Column(length=4000)
	public String getPackageDetail() {
		return packageDetail;	
	}
	public void setPackageDetail(String packageDetail) {
		this.packageDetail = packageDetail;
	}
	
	
	@Override
	public String toString() {
		return "TelecomHebeiPackage [taskid=" + taskid + ", productNum=" + productNum + ", productName=" + productName
				+ ", packageName=" + packageName + ", status=" + status 
				+ ", packageDetail=" + packageDetail + "]";
	}
}
