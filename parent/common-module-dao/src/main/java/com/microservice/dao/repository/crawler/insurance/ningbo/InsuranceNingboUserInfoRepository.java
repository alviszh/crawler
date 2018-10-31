package com.microservice.dao.repository.crawler.insurance.ningbo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.dao.entity.crawler.insurance.ningbo.InsuranceNingboUserInfo;

@Repository
public interface InsuranceNingboUserInfoRepository extends JpaRepository<InsuranceNingboUserInfo, Long>{

	List<InsuranceNingboUserInfo> findByTaskid(String taskid);

}
