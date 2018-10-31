package com.microservice.dao.repository.crawler.insurance.binzhou;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouBaseInfo;
import com.microservice.dao.entity.crawler.insurance.binzhou.InsuranceBinZhouImageInfo;

/**
 * 滨州社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceBinZhouImageInfoRepository extends JpaRepository<InsuranceBinZhouImageInfo, Long>{
	@Transactional
	@Modifying
	@Query(value = "from InsuranceBinZhouImageInfo order by createtime desc")
	List<InsuranceBinZhouImageInfo> findByCreatetime();
	
	@Transactional
	@Modifying
	@Query(value = "update InsuranceBinZhouImageInfo set requestParameter=?1 where taskid=?2")
	void updateBytaskid(String requestParameter, String taskid);
	
	List<InsuranceBinZhouImageInfo> findByTaskid(String taskid);
	
}
