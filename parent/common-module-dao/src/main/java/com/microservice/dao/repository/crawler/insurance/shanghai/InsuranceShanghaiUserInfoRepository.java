package com.microservice.dao.repository.crawler.insurance.shanghai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.shanghai.InsuranceShanghaiUserInfo;

public interface InsuranceShanghaiUserInfoRepository extends JpaRepository<InsuranceShanghaiUserInfo, Long>{
	List<InsuranceShanghaiUserInfo> findByTaskid(String taskid);
}
