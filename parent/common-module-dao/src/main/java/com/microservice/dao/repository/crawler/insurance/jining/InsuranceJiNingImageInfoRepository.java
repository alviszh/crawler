package com.microservice.dao.repository.crawler.insurance.jining;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.jining.InsuranceJiNingImageInfo;

public interface InsuranceJiNingImageInfoRepository extends JpaRepository<InsuranceJiNingImageInfo, Long>{
	@Transactional
	@Modifying
	@Query(value = "from InsuranceJiNingImageInfo order by createtime desc")
	List<InsuranceJiNingImageInfo> findByCreatetime();
	
	@Transactional
	@Modifying
	@Query(value = "update InsuranceJiNingImageInfo set requestParameter=?1 where taskid=?2")
	void updateBytaskid(String requestParameter, String taskid);
	
	List<InsuranceJiNingImageInfo> findByTaskid(String taskid);
	
}
