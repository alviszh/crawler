package com.microservice.dao.repository.crawler.insurance.haikou;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.haikou.InsuranceHaiKouEndowment;

@Repository
public interface InsuranceHaiKouRepositoryEndowment extends JpaRepository<InsuranceHaiKouEndowment, Long>{

}
