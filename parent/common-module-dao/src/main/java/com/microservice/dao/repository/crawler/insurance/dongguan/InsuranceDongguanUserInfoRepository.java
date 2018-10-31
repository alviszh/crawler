package com.microservice.dao.repository.crawler.insurance.dongguan;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.dongguan.InsuranceDongguanUserInfo;

public interface InsuranceDongguanUserInfoRepository extends JpaRepository<InsuranceDongguanUserInfo, Long>{
	
	List<InsuranceDongguanUserInfo> findByTaskid(String taskid);
	


}
