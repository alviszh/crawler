package com.microservice.dao.repository.crawler.insurance.basic;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.basic.AreaCode;

public interface AreaCodeRepository extends JpaRepository<AreaCode, Long>{

	List<AreaCode> findByIsInsuranceFinishedLessThan(int i);

}
