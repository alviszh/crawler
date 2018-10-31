package com.microservice.dao.repository.crawler.insurance.nantong;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.dao.entity.crawler.insurance.nantong.InsuranceNanTongYanglaoInfo;
public interface InsuranceNanTongYanglaoInfoRepository extends JpaRepository<InsuranceNanTongYanglaoInfo, Long>{
	List<InsuranceNanTongYanglaoInfo> findByTaskid(String taskid);
}
