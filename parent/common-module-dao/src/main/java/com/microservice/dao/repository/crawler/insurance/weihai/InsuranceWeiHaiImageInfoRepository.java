package com.microservice.dao.repository.crawler.insurance.weihai;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.weihai.InsuranceWeiHaiImageInfo;

public interface InsuranceWeiHaiImageInfoRepository extends JpaRepository<InsuranceWeiHaiImageInfo, Long>{
	@Transactional
	@Modifying
	@Query(value = "update InsuranceWeiHaiImageInfo set requestParameter=?1 where taskid=?2")
	void updateBytaskid(String requestParameter, String taskid);
	
	List<InsuranceWeiHaiImageInfo> findByTaskid(String taskid);
	
}
