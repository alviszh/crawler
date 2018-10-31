package com.microservice.dao.repository.crawler.bank.basic;

import org.springframework.data.jpa.repository.JpaRepository;
import com.microservice.dao.entity.crawler.bank.basic.BasicUserBank;

public interface BasicUserBankRepository extends JpaRepository<BasicUserBank, Long>{

	BasicUserBank findByIdnum(String idNum);

}
