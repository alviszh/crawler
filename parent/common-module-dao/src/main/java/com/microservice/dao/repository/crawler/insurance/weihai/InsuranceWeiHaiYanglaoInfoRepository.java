package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiYanglaoInfo;
public interface InsuranceWeiHaiYanglaoInfoRepository extends JpaRepository<InsuranceWeiHaiYanglaoInfo, Long>{
	List<InsuranceWeiHaiYanglaoInfo> findByTaskid(String taskid);
}
