package app.bean;

import java.util.Date;
import java.util.List;

import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditAccountSummary;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditBasicInfo;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditBills;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditCardSummary;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditInterestInformation;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditOverdueInformation;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditRepaymentSummary;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditSalesAmount12;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditSalesAmount3;
import com.microservice.dao.entity.crawler.bank.etl.BankReportCreditSalesAmount6;
import com.microservice.dao.entity.crawler.bank.etl.BankReportDebitDeposit;
import com.microservice.dao.entity.crawler.bank.etl.BankReportDebitDetail;
import com.microservice.dao.entity.crawler.bank.etl.BankReportIncome;
import com.microservice.dao.entity.crawler.bank.etl.BankReportInstallments;
import com.microservice.dao.entity.crawler.bank.etl.BankReportOtherAttribute12;
import com.microservice.dao.entity.crawler.bank.etl.BankReportOtherAttribute3;
import com.microservice.dao.entity.crawler.bank.etl.BankReportOtherAttribute6;
import com.microservice.dao.entity.crawler.bank.etl.BankReportOverdueCreditcard;
import com.microservice.dao.entity.crawler.bank.etl.BankReportParent;
import com.microservice.dao.entity.crawler.bank.etl.BankReportQuota12;
import com.microservice.dao.entity.crawler.bank.etl.BankReportQuota3;
import com.microservice.dao.entity.crawler.bank.etl.BankReportQuota6;
import com.microservice.dao.entity.crawler.bank.etl.BankReportRepayment12;
import com.microservice.dao.entity.crawler.bank.etl.BankReportRepayment3;
import com.microservice.dao.entity.crawler.bank.etl.BankReportRepayment6;


public class WebDataBankReport {

	public RequestParam param;	
	public List<BankReportParent> bankReportParent;
	public List<BankReportCreditAccountSummary> bankReportCreditAccountSummary;
	public List<BankReportCreditBasicInfo> bankReportCreditBasicInfo;
	public List<BankReportCreditBills> bankReportCreditBills;
	public List<BankReportCreditCardSummary> bankReportCreditCardSummary;
	public List<BankReportCreditInterestInformation> bankReportCreditInterestInformation;
	public List<BankReportCreditOverdueInformation> bankReportCreditOverdueInformation;
	public List<BankReportCreditRepaymentSummary> bankReportCreditRepaymentSummary;
	public List<BankReportCreditSalesAmount12> bankReportCreditSalesAmount12;
	public List<BankReportCreditSalesAmount6> bankReportCreditSalesAmount6;
	public List<BankReportCreditSalesAmount3> bankReportCreditSalesAmount3;
	public List<BankReportDebitDeposit> bankReportDebitDeposit;
	public List<BankReportDebitDetail> bankReportDebitDetail;
	public List<BankReportIncome> bankReportIncome;
	public List<BankReportInstallments> bankReportInstallments;
	public List<BankReportOtherAttribute12> bankReportOtherAttribute12;
	public List<BankReportOtherAttribute6> bankReportOtherAttribute6;
	public List<BankReportOtherAttribute3> bankReportOtherAttribute3;
	public List<BankReportOverdueCreditcard> bankReportOverdueCreditcard;	
	public List<BankReportQuota12> bankReportQuota12;
	public List<BankReportQuota6> bankReportQuota6;
	public List<BankReportQuota3> bankReportQuota3;
	public List<BankReportRepayment12> bankReportRepayment12;
	public List<BankReportRepayment6> bankReportRepayment6;
	public List<BankReportRepayment3> bankReportRepayment3;
	public Date createtime = new Date();
	public String message;
	public Integer errorCode;
	public String profile;
	public RequestParam getParam() {
		return param;
	}
	public void setParam(RequestParam param) {
		this.param = param;
	}
	public List<BankReportParent> getBankReportParent() {
		return bankReportParent;
	}
	public void setBankReportParent(List<BankReportParent> bankReportParent) {
		this.bankReportParent = bankReportParent;
	}
	public List<BankReportCreditAccountSummary> getBankReportCreditAccountSummary() {
		return bankReportCreditAccountSummary;
	}
	public void setBankReportCreditAccountSummary(List<BankReportCreditAccountSummary> bankReportCreditAccountSummary) {
		this.bankReportCreditAccountSummary = bankReportCreditAccountSummary;
	}
	public List<BankReportCreditBasicInfo> getBankReportCreditBasicInfo() {
		return bankReportCreditBasicInfo;
	}
	public void setBankReportCreditBasicInfo(List<BankReportCreditBasicInfo> bankReportCreditBasicInfo) {
		this.bankReportCreditBasicInfo = bankReportCreditBasicInfo;
	}
	public List<BankReportCreditBills> getBankReportCreditBills() {
		return bankReportCreditBills;
	}
	public void setBankReportCreditBills(List<BankReportCreditBills> bankReportCreditBills) {
		this.bankReportCreditBills = bankReportCreditBills;
	}
	public List<BankReportCreditCardSummary> getBankReportCreditCardSummary() {
		return bankReportCreditCardSummary;
	}
	public void setBankReportCreditCardSummary(List<BankReportCreditCardSummary> bankReportCreditCardSummary) {
		this.bankReportCreditCardSummary = bankReportCreditCardSummary;
	}
	public List<BankReportCreditInterestInformation> getBankReportCreditInterestInformation() {
		return bankReportCreditInterestInformation;
	}
	public void setBankReportCreditInterestInformation(
			List<BankReportCreditInterestInformation> bankReportCreditInterestInformation) {
		this.bankReportCreditInterestInformation = bankReportCreditInterestInformation;
	}
	public List<BankReportCreditOverdueInformation> getBankReportCreditOverdueInformation() {
		return bankReportCreditOverdueInformation;
	}
	public void setBankReportCreditOverdueInformation(
			List<BankReportCreditOverdueInformation> bankReportCreditOverdueInformation) {
		this.bankReportCreditOverdueInformation = bankReportCreditOverdueInformation;
	}
	public List<BankReportCreditRepaymentSummary> getBankReportCreditRepaymentSummary() {
		return bankReportCreditRepaymentSummary;
	}
	public void setBankReportCreditRepaymentSummary(
			List<BankReportCreditRepaymentSummary> bankReportCreditRepaymentSummary) {
		this.bankReportCreditRepaymentSummary = bankReportCreditRepaymentSummary;
	}
	public List<BankReportCreditSalesAmount12> getBankReportCreditSalesAmount12() {
		return bankReportCreditSalesAmount12;
	}
	public void setBankReportCreditSalesAmount12(List<BankReportCreditSalesAmount12> bankReportCreditSalesAmount12) {
		this.bankReportCreditSalesAmount12 = bankReportCreditSalesAmount12;
	}
	public List<BankReportCreditSalesAmount6> getBankReportCreditSalesAmount6() {
		return bankReportCreditSalesAmount6;
	}
	public void setBankReportCreditSalesAmount6(List<BankReportCreditSalesAmount6> bankReportCreditSalesAmount6) {
		this.bankReportCreditSalesAmount6 = bankReportCreditSalesAmount6;
	}
	public List<BankReportCreditSalesAmount3> getBankReportCreditSalesAmount3() {
		return bankReportCreditSalesAmount3;
	}
	public void setBankReportCreditSalesAmount3(List<BankReportCreditSalesAmount3> bankReportCreditSalesAmount3) {
		this.bankReportCreditSalesAmount3 = bankReportCreditSalesAmount3;
	}
	public List<BankReportDebitDeposit> getBankReportDebitDeposit() {
		return bankReportDebitDeposit;
	}
	public void setBankReportDebitDeposit(List<BankReportDebitDeposit> bankReportDebitDeposit) {
		this.bankReportDebitDeposit = bankReportDebitDeposit;
	}
	public List<BankReportDebitDetail> getBankReportDebitDetail() {
		return bankReportDebitDetail;
	}
	public void setBankReportDebitDetail(List<BankReportDebitDetail> bankReportDebitDetail) {
		this.bankReportDebitDetail = bankReportDebitDetail;
	}
	public List<BankReportIncome> getBankReportIncome() {
		return bankReportIncome;
	}
	public void setBankReportIncome(List<BankReportIncome> bankReportIncome) {
		this.bankReportIncome = bankReportIncome;
	}
	public List<BankReportInstallments> getBankReportInstallments() {
		return bankReportInstallments;
	}
	public void setBankReportInstallments(List<BankReportInstallments> bankReportInstallments) {
		this.bankReportInstallments = bankReportInstallments;
	}
	public List<BankReportOtherAttribute12> getBankReportOtherAttribute12() {
		return bankReportOtherAttribute12;
	}
	public void setBankReportOtherAttribute12(List<BankReportOtherAttribute12> bankReportOtherAttribute12) {
		this.bankReportOtherAttribute12 = bankReportOtherAttribute12;
	}
	public List<BankReportOtherAttribute6> getBankReportOtherAttribute6() {
		return bankReportOtherAttribute6;
	}
	public void setBankReportOtherAttribute6(List<BankReportOtherAttribute6> bankReportOtherAttribute6) {
		this.bankReportOtherAttribute6 = bankReportOtherAttribute6;
	}
	public List<BankReportOtherAttribute3> getBankReportOtherAttribute3() {
		return bankReportOtherAttribute3;
	}
	public void setBankReportOtherAttribute3(List<BankReportOtherAttribute3> bankReportOtherAttribute3) {
		this.bankReportOtherAttribute3 = bankReportOtherAttribute3;
	}
	public List<BankReportOverdueCreditcard> getBankReportOverdueCreditcard() {
		return bankReportOverdueCreditcard;
	}
	public void setBankReportOverdueCreditcard(List<BankReportOverdueCreditcard> bankReportOverdueCreditcard) {
		this.bankReportOverdueCreditcard = bankReportOverdueCreditcard;
	}
	public List<BankReportQuota12> getBankReportQuota12() {
		return bankReportQuota12;
	}
	public void setBankReportQuota12(List<BankReportQuota12> bankReportQuota12) {
		this.bankReportQuota12 = bankReportQuota12;
	}
	public List<BankReportQuota6> getBankReportQuota6() {
		return bankReportQuota6;
	}
	public void setBankReportQuota6(List<BankReportQuota6> bankReportQuota6) {
		this.bankReportQuota6 = bankReportQuota6;
	}
	public List<BankReportQuota3> getBankReportQuota3() {
		return bankReportQuota3;
	}
	public void setBankReportQuota3(List<BankReportQuota3> bankReportQuota3) {
		this.bankReportQuota3 = bankReportQuota3;
	}
	public List<BankReportRepayment12> getBankReportRepayment12() {
		return bankReportRepayment12;
	}
	public void setBankReportRepayment12(List<BankReportRepayment12> bankReportRepayment12) {
		this.bankReportRepayment12 = bankReportRepayment12;
	}
	public List<BankReportRepayment6> getBankReportRepayment6() {
		return bankReportRepayment6;
	}
	public void setBankReportRepayment6(List<BankReportRepayment6> bankReportRepayment6) {
		this.bankReportRepayment6 = bankReportRepayment6;
	}
	public List<BankReportRepayment3> getBankReportRepayment3() {
		return bankReportRepayment3;
	}
	public void setBankReportRepayment3(List<BankReportRepayment3> bankReportRepayment3) {
		this.bankReportRepayment3 = bankReportRepayment3;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
		
}
