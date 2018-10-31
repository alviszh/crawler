package com.microservice.dao.repository.crawler.insurance.dezhou;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.dezhou.InsuranceDeZhouImageInfo;

/**
 * 德州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceDeZhouImageInfoRepository extends JpaRepository<InsuranceDeZhouImageInfo, Long>{
	@Transactional
	@Modifying
	@Query(value = "from InsuranceDeZhouImageInfo order by createtime desc")
	List<InsuranceDeZhouImageInfo> findByCreatetime();
	
	@Transactional
	@Modifying
	@Query(value = "update InsuranceDeZhouImageInfo set requestParameter=?1 where taskid=?2")
	void updateBytaskid(String requestParameter, String taskid);
	
	List<InsuranceDeZhouImageInfo> findByTaskid(String taskid);
	
}
