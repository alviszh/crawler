package app.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
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
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditAccountSummaryRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditBasicInfoRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditBillsRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditCardSummaryRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditInterestInformationRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditOverdueInformationRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditRepaymentSummaryRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditSalesAmount12Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditSalesAmount3Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportCreditSalesAmount6Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportDebitDepositRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportDebitDetailRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportIncomeRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportInstallmentsRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportOtherAttribute12Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportOtherAttribute3Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportOtherAttribute6Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportOverdueCreditcardRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportParentRepository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportQuota12Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportQuota3Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportQuota6Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportRepayment12Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportRepayment3Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportRepayment6Repository;
import com.microservice.dao.repository.crawler.bank.etl.BankReportRepository;

import app.bean.BankEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataBankReport;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.etl",
		"com.microservice.dao.entity.crawler.bank" })
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.etl",
		"com.microservice.dao.repository.crawler.bank" })
public class BankReportService {

	@Autowired
	private TracerLog tracer;
	@Autowired
	private TaskBankRepository taskBankRepository;
	@Autowired
	private BankReportRepository bankReportRepository;
	@Autowired
	private BankReportCreditAccountSummaryRepository bankReportCreditAccountSummaryRepository;
	@Autowired
	private BankReportCreditBasicInfoRepository bankReportCreditBasicInfoRepository;
	@Autowired
	private BankReportCreditBillsRepository bankReportCreditBillsRepository;
	@Autowired
	private BankReportCreditCardSummaryRepository bankReportCreditCardSummaryRepository;
	@Autowired
	private BankReportCreditInterestInformationRepository bankReportCreditInterestInformationRepository;
	@Autowired
	private BankReportCreditOverdueInformationRepository bankReportCreditOverdueInformationRepository;
	@Autowired
	private BankReportCreditRepaymentSummaryRepository bankReportCreditRepaymentSummaryRepository;
	@Autowired
	private BankReportCreditSalesAmount12Repository bankReportCreditSalesAmount12Repository;
	@Autowired
	private BankReportCreditSalesAmount6Repository bankReportCreditSalesAmount6Repository;
	@Autowired
	private BankReportCreditSalesAmount3Repository bankReportCreditSalesAmount3Repository;
	@Autowired
	private BankReportDebitDepositRepository bankReportDebitDepositRepository;
	@Autowired
	private BankReportDebitDetailRepository bankReportDebitDetailRepository;
	@Autowired
	private BankReportIncomeRepository bankReportIncomeRepository;
	@Autowired
	private BankReportInstallmentsRepository bankReportInstallmentsRepository;
	@Autowired
	private BankReportOtherAttribute12Repository bankReportOtherAttribute12Repository;
	@Autowired
	private BankReportOtherAttribute6Repository bankReportOtherAttribute6Repository;
	@Autowired
	private BankReportOtherAttribute3Repository bankReportOtherAttribute3Repository;
	@Autowired
	private BankReportOverdueCreditcardRepository bankReportOverdueCreditcardRepository;
	@Autowired
	private BankReportParentRepository bankReportParentRepository;
	@Autowired
	private BankReportQuota12Repository bankReportQuota12Repository;
	@Autowired
	private BankReportQuota6Repository bankReportQuota6Repository;
	@Autowired
	private BankReportQuota3Repository bankReportQuota3Repository;
	@Autowired
	private BankReportRepayment12Repository bankReportRepayment12Repository;
	@Autowired
	private BankReportRepayment6Repository bankReportRepayment6Repository;
	@Autowired
	private BankReportRepayment3Repository bankReportRepayment3Repository;

	@Value("${spring.profiles.active}")
	String profile;

	public WebDataBankReport getAllData(RequestParam requestParam) {

		WebDataBankReport webDataBankReport = new WebDataBankReport();

		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())) {

			// 返回错误码
			webDataBankReport.setParam(requestParam);
			webDataBankReport.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NULL.getMessage());
			webDataBankReport.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NULL.getErrorCode());
			webDataBankReport.setProfile(profile);
			return webDataBankReport;
		}

		if (StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())) {
			TaskBank taskBank = taskBankRepository.findTopByLoginNameOrderByCreatetimeDesc(requestParam.getLoginName());
			return getData(taskBank, webDataBankReport, requestParam);
		}

		if (StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())) {
			TaskBank taskBank = taskBankRepository.findByTaskid(requestParam.getTaskid());
			return getData(taskBank, webDataBankReport, requestParam);
		}

		if (StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())) {
			TaskBank taskBank = taskBankRepository.findByTaskid(requestParam.getTaskid());
			return getData(taskBank, webDataBankReport, requestParam);
		}

		return webDataBankReport;
	}

	public WebDataBankReport getData(TaskBank taskBank, WebDataBankReport webDataBankReport,
			RequestParam requestParam) {

		if (null == taskBank) {
			webDataBankReport.setParam(requestParam);
			webDataBankReport.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getMessage());
			webDataBankReport.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getErrorCode());
			webDataBankReport.setProfile(profile);
			return webDataBankReport;
		}

		String basicUserId = taskBank.getBasicUserBank().getId() + "";

		List<BankReportCreditAccountSummary> bankReportCreditAccountSummary = bankReportCreditAccountSummaryRepository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditBasicInfo> bankReportCreditBasicInfo = bankReportCreditBasicInfoRepository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditBills> bankReportCreditBills = bankReportCreditBillsRepository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditCardSummary> bankReportCreditCardSummary = bankReportCreditCardSummaryRepository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditInterestInformation> bankReportCreditInterestInformation = bankReportCreditInterestInformationRepository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditOverdueInformation> bankReportCreditOverdueInformation = bankReportCreditOverdueInformationRepository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditRepaymentSummary> bankReportCreditRepaymentSummary = bankReportCreditRepaymentSummaryRepository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditSalesAmount12> bankReportCreditSalesAmount12 = bankReportCreditSalesAmount12Repository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditSalesAmount3> bankReportCreditSalesAmount3 = bankReportCreditSalesAmount3Repository
				.findByBasicUserId(basicUserId);
		List<BankReportCreditSalesAmount6> bankReportCreditSalesAmount6 = bankReportCreditSalesAmount6Repository
				.findByBasicUserId(basicUserId);
		List<BankReportDebitDeposit> bankReportDebitDeposit = bankReportDebitDepositRepository
				.findByBasicUserId(basicUserId);
		List<BankReportDebitDetail> bankReportDebitDetail = bankReportDebitDetailRepository
				.findByBasicUserId(basicUserId);
		List<BankReportIncome> bankReportIncome = bankReportIncomeRepository.findByBasicUserId(basicUserId);
		List<BankReportInstallments> bankReportInstallments = bankReportInstallmentsRepository
				.findByBasicUserId(basicUserId);
		List<BankReportOtherAttribute12> bankReportOtherAttribute12 = bankReportOtherAttribute12Repository
				.findByBasicUserId(basicUserId);
		List<BankReportOtherAttribute3> bankReportOtherAttribute3 = bankReportOtherAttribute3Repository
				.findByBasicUserId(basicUserId);
		List<BankReportOtherAttribute6> bankReportOtherAttribute6 = bankReportOtherAttribute6Repository
				.findByBasicUserId(basicUserId);
		List<BankReportOverdueCreditcard> bankReportOverdueCreditcard = bankReportOverdueCreditcardRepository
				.findByBasicUserId(basicUserId);
		List<BankReportParent> bankReportParent = bankReportParentRepository.findByBasicUserId(basicUserId);
		List<BankReportQuota12> bankReportQuota12 = bankReportQuota12Repository.findByBasicUserId(basicUserId);
		List<BankReportQuota3> bankReportQuota3 = bankReportQuota3Repository.findByBasicUserId(basicUserId);
		List<BankReportQuota6> bankReportQuota6 = bankReportQuota6Repository.findByBasicUserId(basicUserId);
		List<BankReportRepayment12> bankReportRepayment12 = bankReportRepayment12Repository
				.findByBasicUserId(basicUserId);
		List<BankReportRepayment3> bankReportRepayment3 = bankReportRepayment3Repository.findByBasicUserId(basicUserId);
		List<BankReportRepayment6> bankReportRepayment6 = bankReportRepayment6Repository.findByBasicUserId(basicUserId);

		webDataBankReport.setBankReportCreditAccountSummary(bankReportCreditAccountSummary);
		webDataBankReport.setBankReportCreditBasicInfo(bankReportCreditBasicInfo);
		webDataBankReport.setBankReportCreditBills(bankReportCreditBills);
		webDataBankReport.setBankReportCreditCardSummary(bankReportCreditCardSummary);
		webDataBankReport.setBankReportCreditInterestInformation(bankReportCreditInterestInformation);
		webDataBankReport.setBankReportCreditOverdueInformation(bankReportCreditOverdueInformation);
		webDataBankReport.setBankReportCreditRepaymentSummary(bankReportCreditRepaymentSummary);
		webDataBankReport.setBankReportCreditSalesAmount12(bankReportCreditSalesAmount12);
		webDataBankReport.setBankReportCreditSalesAmount6(bankReportCreditSalesAmount6);
		webDataBankReport.setBankReportCreditSalesAmount3(bankReportCreditSalesAmount3);
		webDataBankReport.setBankReportDebitDeposit(bankReportDebitDeposit);
		webDataBankReport.setBankReportDebitDetail(bankReportDebitDetail);
		webDataBankReport.setBankReportIncome(bankReportIncome);
		webDataBankReport.setBankReportInstallments(bankReportInstallments);
		webDataBankReport.setBankReportOtherAttribute12(bankReportOtherAttribute12);
		webDataBankReport.setBankReportOtherAttribute6(bankReportOtherAttribute6);
		webDataBankReport.setBankReportOtherAttribute3(bankReportOtherAttribute3);
		webDataBankReport.setBankReportOverdueCreditcard(bankReportOverdueCreditcard);
		webDataBankReport.setBankReportParent(bankReportParent);
		webDataBankReport.setBankReportQuota12(bankReportQuota12);
		webDataBankReport.setBankReportQuota6(bankReportQuota6);
		webDataBankReport.setBankReportQuota3(bankReportQuota3);
		webDataBankReport.setBankReportRepayment12(bankReportRepayment12);
		webDataBankReport.setBankReportRepayment6(bankReportRepayment6);
		webDataBankReport.setBankReportRepayment3(bankReportRepayment3);

		webDataBankReport.setParam(requestParam);
		webDataBankReport.setMessage(BankEtlEnum.BANK_ETL_SUCCESS.getMessage());
		webDataBankReport.setErrorCode(BankEtlEnum.BANK_ETL_SUCCESS.getErrorCode());
		webDataBankReport.setProfile(profile);

		return webDataBankReport;

	}

	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.READ_COMMITTED)
	public void bankReport(String taskid) {
		tracer.addTag("ETL bankReport", taskid);
		TaskBank taskBank = taskBankRepository.findByTaskid(taskid);
		if (taskBank != null && null != taskBank.getBankType() && null != taskBank.getCardType()) {
			tracer.addTag("ETL bankReport:taskBank---" + taskid, taskBank.toString());
			String etlStatus = "";
			if (taskBank.getBankType().equals("浙商银行")) {
				etlStatus = bankReportRepository.pro_czb_debit_etl(taskid);
			} else if (taskBank.getBankType().equals("恒丰银行")) {
				etlStatus = bankReportRepository.pro_hfb_debit_etl(taskid);

			} else if (taskBank.getBankType().equals("渤海银行")) {
				etlStatus = bankReportRepository.pro_bohc_debit_etl(taskid);

			} else if (taskBank.getBankType().equals("广发银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_cgb_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_cgb_debit_etl(taskid);
				}else{
					etlStatus = "广发银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("光大银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_ceb_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_ceb_debit_etl(taskid);
				}else{
					etlStatus = "光大银行对应的cardtype参数为空！";
				}

			} else if (taskBank.getBankType().equals("浦发银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_spdb_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_spdb_debit_etl(taskid);
				}else{
					etlStatus = "浦发银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("农业银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_abc_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_abc_debit_etl(taskid);
				}else{
					etlStatus = "农业银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("招商银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_cmb_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_cmb_debit_etl(taskid);
				}else{
					etlStatus = "招商银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("北京银行")) {
				if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_bob_debit_etl(taskid);
				}else{
					etlStatus = "北京银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("邮储银行")) {
				if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_psb_debit_etl(taskid);
				}else{
					etlStatus = "邮政银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("中国银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_boc_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_boc_debit_etl(taskid);
				}else{
					etlStatus = "中国银行对应的cardtype参数为空！";
				}

			} else if (taskBank.getBankType().equals("华夏银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_hxb_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_hxb_debit_etl(taskid);
				}else{
					etlStatus = "华夏银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("民生银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_cmbc_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_cmbc_debit_etl(taskid);
				}else{
					etlStatus = "民生银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("交通银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_bocom_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_bocom_debit_etl(taskid);
				}else{
					etlStatus = "交通银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("工商银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_icbc_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_icbc_debit_etl(taskid);
				}else{
					etlStatus = "工商银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("建设银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_ccb_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_ccb_debit_etl(taskid);
				}else{
					etlStatus = "建设银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("兴业银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_cib_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_cib_debit_etl(taskid);
				}else{
					etlStatus = "兴业银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("平安银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_pab_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_pab_debit_etl(taskid);
				}else{
					etlStatus = "平安银行对应的cardtype参数为空！";
				}
			} else if (taskBank.getBankType().equals("中信银行")) {
				if(taskBank.getCardType().equals("CREDIT_CARD")){
					etlStatus = bankReportRepository.pro_citic_credit_etl(taskid);
				}else if(taskBank.getCardType().equals("DEBIT_CARD")){
					etlStatus = bankReportRepository.pro_citic_debit_etl(taskid);
				}else{
					etlStatus = "中信银行对应的cardtype参数为空！";
				}
			} else {
				etlStatus = "没有对应的银行类型";
			}
			tracer.addTag("ETL bankReport:etlStatus---" + etlStatus, taskid);
//			String reportStatus = bankReportRepository.proBankReport(taskid);
//			tracer.addTag("ETL bankReport:reportStatus---" + reportStatus, taskid);
			taskBank.setEtl_status(etlStatus);
			taskBank.setReport_time(new Date()+"");
			taskBank.setReport_status("");
			taskBankRepository.save(taskBank);
		}
		tracer.addTag("ETL bankReport---end", taskid);
	}

}
