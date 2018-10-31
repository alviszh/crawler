package com.microservice.dao.repository.crawler.taxation.beijing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.taxation.beijing.TaxationBeiJingAccount;

@Repository
public interface TaxationRepositoryBeiJingAccount extends JpaRepository<TaxationBeiJingAccount,Long>{

}
