package com.microservice.dao.repository.crawler.insurance.fuzhou3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.fuzhou3.InsuranceFuZhou3Account;

@Repository
public interface InsuranceFuZhou3AccountRepository extends JpaRepository<InsuranceFuZhou3Account, Long> {
}
