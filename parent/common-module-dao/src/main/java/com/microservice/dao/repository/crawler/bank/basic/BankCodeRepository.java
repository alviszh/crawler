package com.microservice.dao.repository.crawler.bank.basic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.bank.basic.BankCode;

public interface BankCodeRepository extends JpaRepository<BankCode, Long>{


	List<BankCode> findByIsFlagInOrderByBankId(List<Integer> isFlag);

}
