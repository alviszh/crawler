package com.microservice.dao.repository.crawler.insurance.yantai;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.microservice.dao.entity.crawler.insurance.yantai.InsuranceYantaiImageInfo;

/**
 * 烟台社保  Repository
 * @author qizhongbin
 *
 */
public interface InsuranceYantaiImageInfoRepository extends JpaRepository<InsuranceYantaiImageInfo, Long>{
	List<InsuranceYantaiImageInfo> findByTaskid(String taskid);
	@Transactional
	@Modifying
	@Query(value = "update InsuranceYantaiImageInfo set requestParameter=?1 where taskid=?2")
	void updateBytaskid(String requestParameter, String taskid);
}
