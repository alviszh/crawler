package com.microservice.dao.repository.crawler.standalone.bank.ccbchina;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.standalone.bank.ccbchina.CcbChinaDebitCardBillDetails;

public interface CcbChinaDebitCardBillDetailsRepository extends JpaRepository<CcbChinaDebitCardBillDetails, Long>{
	
	@Query(value = "select num from js_excel_jianshe_trans_total where account = ?1 order by num+0 desc limit 1", nativeQuery = true)
	String findBylastData(String account);

	@Query(value = "SELECT count(0) from js_excel_jianshe_trans_total  where deal_date = ?1 and remark = ?2 and balance = ?3", nativeQuery = true)
	int findByCcbChinaDebitCardBillDetails(String deal_date, String remark, String balance);
	
}
