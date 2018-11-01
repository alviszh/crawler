package app.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.microservice.dao.entity.crawler.bank.basic.TaskBank;
import com.microservice.dao.entity.crawler.bank.etl.CreditCardBaseInfo;
import com.microservice.dao.entity.crawler.bank.etl.CreditCardBillInfo;
import com.microservice.dao.entity.crawler.bank.etl.CreditCardInstallmentBill;
import com.microservice.dao.entity.crawler.bank.etl.CreditCardTransDetail;
import com.microservice.dao.entity.crawler.bank.etl.DebiCardTransDetail;
import com.microservice.dao.repository.crawler.bank.basic.TaskBankRepository;
import com.microservice.dao.repository.crawler.bank.etl.CreditCardBaseInfoRepository;
import com.microservice.dao.repository.crawler.bank.etl.CreditCardBillInfoRepository;
import com.microservice.dao.repository.crawler.bank.etl.CreditCardInstallmentBillRepository;
import com.microservice.dao.repository.crawler.bank.etl.CreditCardTransDetailRepository;

import app.bean.BankEtlEnum;
import app.bean.RequestParam;
import app.bean.WebDataCreditcard;
import app.commontracerlog.TracerLog;

@Component
@EntityScan(basePackages = { "com.microservice.dao.entity.crawler.bank.etl","com.microservice.dao.entity.crawler.bank"})
@EnableJpaRepositories(basePackages = { "com.microservice.dao.repository.crawler.bank.etl","com.microservice.dao.repository.crawler.bank"})

public class CreditBankETLService {

	@Autowired 
	private TracerLog tracer;
	@Autowired
	private TaskBankRepository taskBankRepository;	
	@Autowired
	private CreditCardBaseInfoRepository creditCardBaseInfoRepository;
	@Autowired
	private CreditCardBillInfoRepository creditCardBillInfoRepository;
	@Autowired
	private CreditCardInstallmentBillRepository creditCardInstallmentBillRepository;
	@Autowired
	private CreditCardTransDetailRepository creditCardTransDetailRepository;
	
	@Value("${spring.profiles.active}")
	String profile;
	
	public WebDataCreditcard getAllData(RequestParam requestParam){
		
		WebDataCreditcard webDataCreditcard = new WebDataCreditcard();
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			
			webDataCreditcard.setParam(requestParam);
			webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NULL.getMessage());
			webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NULL.getErrorCode());
			webDataCreditcard.setProfile(profile);
			return webDataCreditcard;
		}
		
		if(StringUtils.isBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){
			TaskBank taskBank = taskBankRepository.findTopByLoginNameAndFinishedAndCardTypeAndDescriptionOrderByCreatetimeDesc(requestParam.getLoginName(),true,"CREDIT_CARD","数据采集成功！");
			return getData(taskBank,webDataCreditcard,requestParam);			
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isBlank(requestParam.getLoginName())){
			
			TaskBank taskBank = taskBankRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != taskBank){
				if(taskBank.getFinished() && taskBank.getDescription().equals("数据采集成功!")){
					return getData(taskBank,webDataCreditcard,requestParam);
				}else{
					webDataCreditcard.setParam(requestParam);
					webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getMessage());
					webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getErrorCode());
					webDataCreditcard.setProfile(profile);
					return webDataCreditcard;
				}	
			}else{
				webDataCreditcard.setParam(requestParam);
				webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getMessage());
				webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataCreditcard.setProfile(profile);
				return webDataCreditcard;
			}
		}
		
		if(StringUtils.isNotBlank(requestParam.getTaskid()) && StringUtils.isNotBlank(requestParam.getLoginName())){
			
			TaskBank taskBank = taskBankRepository.findByTaskid(requestParam.getTaskid());
			
			if(null != taskBank){
				if(taskBank.getLoginName().equals(requestParam.getLoginName())){
					if(taskBank.getFinished() && taskBank.getDescription().equals("数据采集成功!")){
						return getData(taskBank,webDataCreditcard,requestParam);
					}else{
						webDataCreditcard.setParam(requestParam);
						webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getMessage());
						webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_CRAWLER_ERROR.getErrorCode());
						webDataCreditcard.setProfile(profile);
						return webDataCreditcard;
					}
				}else{
					webDataCreditcard.setParam(requestParam);
					webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_ERROR.getMessage());
					webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_ERROR.getErrorCode());
					webDataCreditcard.setProfile(profile);
					return webDataCreditcard;
				}	
			}else{
				webDataCreditcard.setParam(requestParam);
				webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getMessage());
				webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getErrorCode());
				webDataCreditcard.setProfile(profile);
				return webDataCreditcard;
			}	
		}
		
		return webDataCreditcard;
	}

	private WebDataCreditcard getData(TaskBank taskBank, WebDataCreditcard webDataCreditcard,RequestParam requestParam) {
		
		if(null != taskBank){
			if(null == taskBank.getEtltime()){
				webDataCreditcard.setParam(requestParam);
				webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_NOT_EXCUTE.getMessage());
				webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_NOT_EXCUTE.getErrorCode());
				webDataCreditcard.setProfile(profile);
				return webDataCreditcard;
			}
			
			List<CreditCardBaseInfo> baseInfos = creditCardBaseInfoRepository.findByTaskId(taskBank.getTaskid());
			List<CreditCardBillInfo> billInfos = creditCardBillInfoRepository.findByTaskId(taskBank.getTaskid());
			List<CreditCardInstallmentBill> installmentBill = creditCardInstallmentBillRepository.findByTaskId(taskBank.getTaskid());
			List<CreditCardTransDetail> transDetails = creditCardTransDetailRepository.findByTaskId(taskBank.getTaskid());
			
						
			webDataCreditcard.setCreditCardBaseInfo(baseInfos);
			webDataCreditcard.setCreditCardBillInfo(billInfos);
			webDataCreditcard.setCreditCardInstallmentBill(installmentBill);
			webDataCreditcard.setCreditCardTransDetail(transDetails);
			webDataCreditcard.setParam(requestParam);
			webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_SUCCESS.getMessage());
			webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_SUCCESS.getErrorCode());
			webDataCreditcard.setProfile(profile);
			return webDataCreditcard;
			
		}else{
			webDataCreditcard.setParam(requestParam);
			webDataCreditcard.setMessage(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getMessage());
			webDataCreditcard.setErrorCode(BankEtlEnum.BANK_ETL_PARAMS_NO_RESULT.getErrorCode());
			webDataCreditcard.setProfile(profile);
			return webDataCreditcard;
		}
	}
}
