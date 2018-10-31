package com.microservice.dao.entity.crawler.insurance.lianyungang;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import com.microservice.dao.entity.IdEntity;

/**
 * 
 * 项目名称：common-microservice-insurance-lianyungang 类名称：InsuranceLianYunGangPay
 * 类描述： 创建人：hyx 创建时间：2018年3月21日 下午2:24:36
 * 
 * @version
 */
@Entity
@Table(name="insurance_lianyungang_pay",indexes = {@Index(name = "index_insurance_lianyungang_pay_taskid", columnList = "taskid")})
public class InsuranceLianYunGangPay extends IdEntity implements Serializable{


		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String company;//公司名
		private String type; //险种
		private String date;//所属期
		private String basepay;//缴费基数
		private String companyPay;//单位缴纳金
		private String manPay;//个人缴纳金
		private String overduefine;//滞纳金
		private String total;//合计
		private String payStatue;//缴费状态
		private String taskid;//

		public String getCompany() {
			return company;
		}

		public void setCompany(String company) {
			this.company = company;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getBasepay() {
			return basepay;
		}

		public void setBasepay(String basepay) {
			this.basepay = basepay;
		}

		public String getCompanyPay() {
			return companyPay;
		}

		public void setCompanyPay(String companyPay) {
			this.companyPay = companyPay;
		}

		public String getManPay() {
			return manPay;
		}

		public void setManPay(String manPay) {
			this.manPay = manPay;
		}

		public String getOverduefine() {
			return overduefine;
		}

		public void setOverduefine(String overduefine) {
			this.overduefine = overduefine;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getPayStatue() {
			return payStatue;
		}

		public void setPayStatue(String payStatue) {
			this.payStatue = payStatue;
		}

		public String getTaskid() {
			return taskid;
		}

		public void setTaskid(String taskid) {
			this.taskid = taskid;
		}

		@Override
		public String toString() {
			return "InsuranceLianYunGangPay [company=" + company + ", type=" + type + ", date=" + date + ", basepay="
					+ basepay + ", companyPay=" + companyPay + ", manPay=" + manPay + ", overduefine=" + overduefine
					+ ", total=" + total + ", payStatue=" + payStatue + ", taskid=" + taskid + "]";
		}

	}
